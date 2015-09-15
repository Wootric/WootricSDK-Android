package com.wootric.androidsdk.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by maciejwitowski on 4/21/15.
 */
public class EndUser implements Parcelable {

    private long id = -1;
    private String email;
    private long createdAt = -1;
    private HashMap properties = new HashMap<>();

    public EndUser() {}

    public EndUser(String email) {
        this.email      = email;
    }

    public EndUser(String email, HashMap properties) {
        this.email = email;
        this.properties = properties;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public boolean isCreatedAtSet() {
        return createdAt != -1;
    }

    public void setProperties(HashMap properties) {
        this.properties = properties;
    }

    public HashMap<String, String> getProperties() {
        return properties;
    }

    public boolean hasProperties() {
        return properties != null && properties.size() > 0;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.email);
        dest.writeLong(this.createdAt);
        dest.writeSerializable(this.properties);
    }

    private EndUser(Parcel in) {
        this.id = in.readLong();
        this.email = in.readString();
        this.createdAt = in.readLong();
        this.properties = (HashMap) in.readSerializable();
    }

    public static final Creator<EndUser> CREATOR = new Creator<EndUser>() {
        public EndUser createFromParcel(Parcel source) {
            return new EndUser(source);
        }

        public EndUser[] newArray(int size) {
            return new EndUser[size];
        }
    };

    public Long getCreatedAtOrNull() {
        return isCreatedAtSet() ? getCreatedAt() : null;
    }
}
