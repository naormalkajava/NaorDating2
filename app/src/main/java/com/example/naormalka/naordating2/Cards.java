package com.example.naormalka.naordating2;

import java.io.Serializable;

/**
 * Created by naor malka on 10/09/2017.
 */

public class Cards implements Serializable {
    private String userId;
    private String name;
    private String profileImageUrl = "https://upload.wikimedia.org/wikipedia/commons/7/7c/Profile_avatar_placeholder_large.png";

    public Cards() {
    }

    public Cards(String userId, String name, String profileImageUrl) {
        this.userId = userId;
        this.name = name;
        if (profileImageUrl != null)
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    @Override
    public String toString() {
        return "Cards{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", imagesource='" + profileImageUrl + '\'' +
                '}';
    }
}
