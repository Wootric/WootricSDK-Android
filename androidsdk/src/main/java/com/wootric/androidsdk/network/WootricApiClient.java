package com.wootric.androidsdk.network;

import com.wootric.androidsdk.network.responses.AuthenticationResponse;
import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.User;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by maciejwitowski on 9/11/15.
 */
public class WootricApiClient {

    private static final String API_ENDPOINT = "https://api.wootric.com";
    private static final String AUTH_TOKEN_URL = "/oauth/token";
    private static final String END_USERS_URL = "/v1/end_users";

    private static final String GRANT_TYPE_CLIENT_CREDENTIALS   = "client_credentials";

    private final WootricApiInterface wootricApiInterface;

    public WootricApiClient() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_ENDPOINT)
                .build();

        wootricApiInterface = restAdapter.create(WootricApiInterface.class);
    }

    public void authenticate(User user, final WootricApiCallback wootricApiCallback) {
        wootricApiInterface.authenticate(GRANT_TYPE_CLIENT_CREDENTIALS, user.getClientId(),
            user.getClientSecret(), new Callback<AuthenticationResponse>() {
                @Override
                public void success(AuthenticationResponse authenticationResponse, Response response) {
                    wootricApiCallback.onAuthenticateSuccess(authenticationResponse);
                }

                @Override
                public void failure(RetrofitError error) {
                    wootricApiCallback.onApiError(error);
                }
            });
    }

    public void getEndUserByEmail(String email, String accessToken, final WootricApiCallback wootricApiCallback) {
        wootricApiInterface.getEndUserByEmail(email, bearerFrom(accessToken), new Callback<List<EndUser>>() {
            @Override
            public void success(List<EndUser> endUsers, Response response) {
                wootricApiCallback.onGetEndUserSuccess(endUsers);
            }

            @Override
            public void failure(RetrofitError error) {
                wootricApiCallback.onApiError(error);
            }
        });
    }

    public void createEndUser(EndUser endUser, String accessToken, final WootricApiCallback wootricApiCallback) {
        wootricApiInterface.createEndUser(bearerFrom(accessToken), endUser.getEmail(),
                endUser.getCreatedAtOrNull(), new Callback<EndUser>() {
            @Override
            public void success(EndUser endUser, Response response) {
                wootricApiCallback.onCreateEndUserSuccess(endUser);
            }

            @Override
            public void failure(RetrofitError error) {
                wootricApiCallback.onApiError(error);
            }
        });
    }

    public void updateEndUser(EndUser endUser, String accessToken) {
        // TODO
    }

    private static String bearerFrom(String accessToken) {
        return "Bearer " + accessToken;
    }

    public interface WootricApiCallback {
        void onAuthenticateSuccess(AuthenticationResponse authenticationResponse);
        void onGetEndUserSuccess(List<EndUser> endUser);
        void onCreateEndUserSuccess(EndUser endUser);

        void onApiError(RetrofitError error);
    }

    public interface WootricApiInterface {
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
//                           @FieldMap Map properties,
                           Callback<EndUser> objectCallback);
    }
}
