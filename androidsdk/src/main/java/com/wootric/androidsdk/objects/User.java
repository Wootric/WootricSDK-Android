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

/**
 * Created by maciejwitowski on 4/21/15.
 */
public class User implements Parcelable {
    private String clientId;
    private String accountToken;

    public User(User user) {
        this.clientId = user.clientId;
        this.accountToken = user.accountToken;
    }

    public User(String clientId, String clientSecret, String accountToken) {
        this.clientId = clientId;
        this.accountToken = accountToken;
    }

    public User(String clientId, String accountToken) {
        this.clientId = clientId;
        this.accountToken = accountToken;
    }

    public User(String accountToken) {
        this.accountToken = accountToken;
    }

    public String getClientId() {
        return clientId;
    }

    public String getAccountToken() {
        return accountToken;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setAccountToken(String accountToken) {
        this.accountToken = accountToken;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.clientId);
        dest.writeString(this.accountToken);
    }

    private User(Parcel in) {
        this.clientId = in.readString();
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
