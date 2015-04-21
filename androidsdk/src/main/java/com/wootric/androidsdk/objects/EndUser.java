package com.wootric.androidsdk.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.wootric.androidsdk.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by maciejwitowski on 4/21/15.
 */
public class EndUser implements Parcelable {

    private long id;
    private String email;
    private long createdAt;

    public EndUser(long id, String email) {
        this.id = id;
        this.email = email;
    }

    public EndUser(String email) {
        this.email      = email;
        this.id         = Constants.INVALID_ID;
        this.createdAt  = Constants.NOT_SET;
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

    public boolean hasCreatedAt() {
        return createdAt != Constants.NOT_SET;
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

    public static EndUser fromJson(JSONObject jsonObject) throws JSONException {
        return new EndUser(
                jsonObject.getLong("id"),
                jsonObject.getString("email")
        );
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
    }

    private EndUser(Parcel in) {
        this.id = in.readLong();
        this.email = in.readString();
        this.createdAt = in.readLong();
    }

    public static final Creator<EndUser> CREATOR = new Creator<EndUser>() {
        public EndUser createFromParcel(Parcel source) {
            return new EndUser(source);
        }

        public EndUser[] newArray(int size) {
            return new EndUser[size];
        }
    };
}
