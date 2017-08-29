package com.example.naormalka.naordating2;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

/**
 * Created by user on 7/23/2017.
 */

public class User implements Parcelable {
    private String displayName;
    private String uid;
    private String profileImage ="https://d26btdus0guqxg.cloudfront.net/assets/no-profile-image-68ac03754ec47c2c54e94935f62feceb.png";
    private double [] userLocation;
//for firebase
    public User() {
    }
//constractour
    public User(FirebaseUser user, Location location) {


        this.displayName = user.getDisplayName();
        this.uid = user.getUid();
            this.userLocation = new double[]{location.getLongitude(), location.getLatitude()};
        if (user.getPhotoUrl() != null) {
            this.profileImage = user.getPhotoUrl().toString();
        }
    }

    public User(FirebaseUser user) {
        this.displayName = user.getDisplayName();
        this.uid = user.getUid();
        if (user.getPhotoUrl() != null)
            this.profileImage = user.getPhotoUrl().toString();
    }

    //getters and setters
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

    public double[] getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(double[] userLocation) {
        this.userLocation = userLocation;
    }

    @Override
    public String toString() {
        return "User{" +
                "displayName='" + displayName + '\'' +
                ", uid='" + uid + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", userLocation=" + Arrays.toString(userLocation) +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.displayName);
        dest.writeString(this.uid);
        dest.writeString(this.profileImage);
        dest.writeDoubleArray(this.userLocation);
    }

    protected User(Parcel in) {
        this.displayName = in.readString();
        this.uid = in.readString();
        this.profileImage = in.readString();
        this.userLocation = in.createDoubleArray();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
