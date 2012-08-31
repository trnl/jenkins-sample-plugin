package org.jenkinsci.plugins;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import org.kohsuke.stapler.DataBoundConstructor;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TwitterNotifier extends Notifier {
    private static final Logger LOGGER = Logger.getLogger(TwitterNotifier.class.getName());

    @DataBoundConstructor
    public TwitterNotifier() {
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.BUILD;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        Configuration configuration = new ConfigurationBuilder()
                .setOAuthConsumerKey(TwitterJobProperty.DESCRIPTOR.getConsumerKey())
                .setOAuthConsumerSecret(TwitterJobProperty.DESCRIPTOR.getConsumerSecret())
                .setOAuthAccessToken(TwitterJobProperty.DESCRIPTOR.getAccessToken())
                .setOAuthAccessTokenSecret(TwitterJobProperty.DESCRIPTOR.getAccessTokenSecret())
                .setUseSSL(true)
                .build();
        Twitter twitter = new TwitterFactory(configuration).getInstance();

        try {
            twitter.updateStatus("I'm done. Client: " + InetAddress.getLocalHost().getHostName() + ". Time: "+ new Date());
        } catch (TwitterException e) {
            LOGGER.log(Level.SEVERE, "Error updating status", e);
        }
        return true;
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Update Twitter Status";
        }
    }
}
