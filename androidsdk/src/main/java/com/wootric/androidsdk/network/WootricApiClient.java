package com.wootric.androidsdk.network;

import com.wootric.androidsdk.network.responses.AuthenticationResponse;
import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.User;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by maciejwitowski on 9/11/15.
 */
public class WootricApiClient {

    private static final String API_ENDPOINT = "https://api.wootric.com";
    private static final String GRANT_TYPE_CLIENT_CREDENTIALS   = "client_credentials";

    private final WootricApiInterface wootricApiInterface;

    public WootricApiClient() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_ENDPOINT)
                .build();

        wootricApiInterface = restAdapter.create(WootricApiInterface.class);
    }

    public WootricApiClient(WootricApiInterface wootricApiInterface) {
        this.wootricApiInterface = wootricApiInterface;
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
}
