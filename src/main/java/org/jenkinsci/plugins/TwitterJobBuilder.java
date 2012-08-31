package org.jenkinsci.plugins;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.kohsuke.stapler.DataBoundConstructor;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TwitterJobBuilder extends Builder {

    @DataBoundConstructor
    public TwitterJobBuilder() {
        super();
    }

    private static final Logger LOGGER = Logger.getLogger(TwitterJobBuilder.class.getName());

    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) {
        Configuration configuration = new ConfigurationBuilder()
                .setOAuthConsumerKey(TwitterJobProperty.DESCRIPTOR.getConsumerKey())
                .setOAuthConsumerSecret(TwitterJobProperty.DESCRIPTOR.getConsumerSecret())
                .setOAuthAccessToken(TwitterJobProperty.DESCRIPTOR.getAccessToken())
                .setOAuthAccessTokenSecret(TwitterJobProperty.DESCRIPTOR.getAccessTokenSecret())
                .setUseSSL(true)
                .build();
        Twitter twitter = new TwitterFactory(configuration).getInstance();

        try {
            List<Status> statuses = twitter.getPublicTimeline();

            List<TwitterMessage> messages = new ArrayList<TwitterMessage>();
            CollectionUtils.collect(statuses, new Transformer() {
                public Object transform(Object input) {
                    Status s = (Status) input;
                    return new TwitterMessage(
                            s.getText(),
                            s.getUser().getName(),
                            s.getUser().getProfileImageURL().toString(),
                            s.getCreatedAt()
                    );
                }
            },
                    messages);
            build.addAction(new TwitterBuildAction(build,messages));
        } catch (TwitterException e) {
            LOGGER.log(Level.SEVERE, "Error getting public timeline", e);
        }
        return true;
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        public String getDisplayName() {
            return "Grab Twitter Public Timeline";
        }
    }
}