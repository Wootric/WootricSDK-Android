package com.wootric.androidsdk.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by maciejwitowski on 4/21/15.
 */
public class User implements Parcelable {

    private final String clientId;
    private final String clientSecret;
    private final String accountToken;

    public User(String clientId, String clientSecret, String accountToken) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.accountToken = accountToken;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getAccountToken() {
        return accountToken;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.clientId);
        dest.writeString(this.clientSecret);
        dest.writeString(this.accountToken);
    }

    private User(Parcel in) {
        this.clientId = in.readString();
        this.clientSecret = in.readString();
        this.accountToken = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
