package org.jenkinsci.plugins;

import hudson.model.AbstractBuild;
import hudson.model.Action;

import java.util.List;

public class TwitterBuildAction implements Action {

    private final AbstractBuild<?, ?> build;
    private List<TwitterMessage> tweets;

    public TwitterBuildAction(AbstractBuild<?, ?> build, List<TwitterMessage> tweets) {
        this.build = build;

        this.tweets = tweets;
    }

    public String getIconFileName() {
        // "cool" is the name given to maven during creation, name of the folder
        return (tweets != null && tweets.size() > 0) ? "/plugin/twitter-plugin/img/twitter.png" : null;
    }

    public String getDisplayName() {
        return "Twitter";  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getUrlName() {
        return "twitter";  //To change body of implemented methods use File | Settings | File Templates.
    }

    public AbstractBuild<?, ?> getBuild() {
        return build;
    }

    public List<TwitterMessage> getTweets() {

        return tweets;
    }

    public void setTweets(List<TwitterMessage> tweets) {
        this.tweets = tweets;
    }
}