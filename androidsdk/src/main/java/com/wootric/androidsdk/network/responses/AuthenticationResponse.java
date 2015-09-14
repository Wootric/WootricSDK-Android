package com.wootric.androidsdk.network.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by maciejwitowski on 9/11/15.
 */
public class AuthenticationResponse {

    @SerializedName("access_token")
    public final String accessToken;

    public AuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
