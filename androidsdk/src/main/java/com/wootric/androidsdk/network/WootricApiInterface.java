package com.wootric.androidsdk.network;

import com.wootric.androidsdk.network.responses.AuthenticationResponse;
import com.wootric.androidsdk.objects.EndUser;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by maciejwitowski on 9/15/15.
 */
public interface WootricApiInterface {

    String AUTH_TOKEN_URL = "/oauth/token";
    String END_USERS_URL = "/v1/end_users";

    @FormUrlEncoded
    @POST(AUTH_TOKEN_URL)
    void authenticate(@Field("grant_type") String grantType,
                      @Field("client_id") String clientId,
                      @Field("client_secret") String clientSecret,
                      Callback<AuthenticationResponse> authenticationResponseCallback);

    @GET(END_USERS_URL)
    void getEndUserByEmail(@Query("email") String email,
                           @Header("Authorization") String accessToken,
                           Callback<List<EndUser>> objectCallback);

    @FormUrlEncoded
    @POST(END_USERS_URL)
    void createEndUser(@Header("Authorization") String accessToken,
                       @Field("email") String email,
                       @Field("external_created_at") Long externalCreatedAt,
                       Callback<EndUser> objectCallback);
}
