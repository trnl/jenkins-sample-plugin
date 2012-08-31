package org.jenkinsci.plugins;

import java.io.Serializable;
import java.util.Date;

public class TwitterMessage implements Serializable {
    private String text;
    private String username;
    private String profileImage;
    private Date dateCreated;

    public TwitterMessage(String text, String username, String profileImage, Date dateCreated) {
        this.text = text;
        this.username = username;
        this.profileImage = profileImage;
        this.dateCreated = dateCreated;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}