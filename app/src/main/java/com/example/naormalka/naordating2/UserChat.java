package com.example.naormalka.naordating2;

import android.os.Parcel;
import android.os.Parcelable;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by naor malka on 14/10/2017.
 */

public class UserChat implements Parcelable {


    String displayName= "";
    String profileImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQGKiEahbMrl60poEt87ZTN4NfiI37dIE-Fx9lXN0QGNv8k4ZGB3w";

    public UserChat() {
    }



    public UserChat(String userName, String userImage) {
        if (userName != null) {
            this.displayName = userName;
        }
        if (userImage != null) {
            this.profileImage = userImage;
        }

    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public String toString() {
        return "UserChat{" +
                "displayName='" + displayName + '\'' +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.displayName);
        dest.writeString(this.profileImage);
    }

    protected UserChat(Parcel in) {
        this.displayName = in.readString();
        this.profileImage = in.readString();
    }

    public static final Parcelable.Creator<UserChat> CREATOR = new Parcelable.Creator<UserChat>() {
        @Override
        public UserChat createFromParcel(Parcel source) {
            return new UserChat(source);
        }

        @Override
        public UserChat[] newArray(int size) {
            return new UserChat[size];
        }
    };
}

