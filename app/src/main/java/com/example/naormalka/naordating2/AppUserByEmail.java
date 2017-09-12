package com.example.naormalka.naordating2;

import java.io.Serializable;


public class AppUserByEmail implements Serializable {
    private String displayName;
    private String uid;
    private String profileImage = "http://eadb.org/wp-content/uploads/2015/08/profile-placeholder.jpg";
    private String gender;

    public AppUserByEmail() {
    }

    public AppUserByEmail(String displayName, String uid, String profileImage, String gender) {
        this.displayName = displayName;
        this.gender = gender;
        if (profileImage != null) {
            this.profileImage = profileImage;
        }
        this.uid = uid;

    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "AppUserByEmail{" +
                "displayName='" + displayName + '\'' +
                ", uid='" + uid + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}

