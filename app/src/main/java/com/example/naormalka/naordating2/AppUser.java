package com.example.naormalka.naordating2;


import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;


/**
 * Created by user on 7/23/2017.
 */

public class AppUser implements Serializable {
    private String displayName;
    private String uid;
    private String profileImage ="https://d26btdus0guqxg.cloudfront.net/assets/no-profile-image-68ac03754ec47c2c54e94935f62feceb.png";
    private String gender;
//for firebase
    public AppUser() {
    }
//constractour


    public AppUser(FirebaseUser user ,String gender) {
        this.displayName = user.getDisplayName();
        this.uid = user.getUid();
        this.gender = gender;
        if (user.getPhotoUrl() != null)
        this.profileImage = user.getPhotoUrl().toString();

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
        return "AppUser{" +
                "displayName='" + displayName + '\'' +
                ", uid='" + uid + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }



}
