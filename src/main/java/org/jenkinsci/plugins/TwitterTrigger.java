package org.jenkinsci.plugins;

import antlr.ANTLRException;
import hudson.Extension;
import hudson.model.Cause;
import hudson.model.Item;
import hudson.model.Project;
import hudson.triggers.Trigger;
import hudson.triggers.TriggerDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;
import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TwitterTrigger extends Trigger<Project> {

    private static final Logger LOGGER = Logger.getLogger(TwitterTrigger.class.getName());

    @Extension
    public static class DescriptorImpl extends TriggerDescriptor {

        @Override
        public boolean isApplicable(Item item) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Poll Twitter";
        }
    }

    @DataBoundConstructor
    public TwitterTrigger(String cronTabSpec) throws ANTLRException {
        super(cronTabSpec);
    }


    private long latestMention = -1;

    @Override
    public void run() {
        if (TwitterJobProperty.DESCRIPTOR.isFilled()) {
            Configuration configuration = new ConfigurationBuilder()
                    .setOAuthConsumerKey(TwitterJobProperty.DESCRIPTOR.getConsumerKey())
                    .setOAuthConsumerSecret(TwitterJobProperty.DESCRIPTOR.getConsumerSecret())
                    .setOAuthAccessToken(TwitterJobProperty.DESCRIPTOR.getAccessToken())
                    .setOAuthAccessTokenSecret(TwitterJobProperty.DESCRIPTOR.getAccessTokenSecret())
                    .setUseSSL(true)
                    .build();
            Twitter twitter = new TwitterFactory(configuration).getInstance();
            try {
                Status s = twitter.getMentions(new Paging(1, 1)).get(0);
                if (latestMention != s.getId()) {
                    latestMention = s.getId();
                    job.scheduleBuild(0, new TwitterCause("Found a tweet:" + s.getText()));
                }
            } catch (TwitterException e) {
                LOGGER.log(Level.SEVERE, "Error getting timeline", e);
            }
        }

    }

    private static class TwitterCause extends Cause {
        private final String description;

        public TwitterCause(String description) {
            this.description = description;
        }

        public String getShortDescription() {
            return description;
        }
    }
}