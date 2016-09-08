/*
 * Copyright (c) 2016 Wootric (https://wootric.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.wootric.androidsdk.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.wootric.androidsdk.utils.Utils;

import java.util.HashMap;

/**
 * Created by maciejwitowski on 4/21/15.
 */
public class EndUser implements Parcelable {

    private static final String UNKNOWN_EMAIL = "Unknown";

    private long id = -1;
    private String email;
    private String externalId;
    private String phoneNumber;
    private long createdAt = -1;
    private HashMap properties = new HashMap<>();

    public EndUser() {}

    public EndUser(String email) {
        this.email = email;
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

    public String getExternalId() {
        return externalId;
    }

    public boolean hasExternalId() {
        return Utils.isNotEmpty(externalId);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean hasPhoneNumber() {
        return Utils.isNotEmpty(phoneNumber);
    }

    public String getEmailOrUnknown() {
        return Utils.isNotEmpty(email) ? email : UNKNOWN_EMAIL;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public boolean isCreatedAtSet() {
        return createdAt != -1;
    }

    public void setProperties(HashMap<String, String> properties) {
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

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
        dest.writeString(this.externalId);
        dest.writeString(this.phoneNumber);
        dest.writeLong(this.createdAt);
        dest.writeSerializable(this.properties);
    }

    private EndUser(Parcel in) {
        this.id = in.readLong();
        this.email = in.readString();
        this.externalId = in.readString();
        this.phoneNumber = in.readString();
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