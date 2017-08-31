package com.example.naormalka.naordating2;

import java.io.Serializable;

/**
 * Created by eric.bell on 8/31/2017.
 */

public class AppUserByEmail  implements Serializable {
    private String displayName;
    private String uid;
    private String profileImage ="https://d26btdus0guqxg.cloudfront.net/assets/no-profile-image-68ac03754ec47c2c54e94935f62feceb.png";
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
