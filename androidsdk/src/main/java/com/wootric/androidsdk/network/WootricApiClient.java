package com.wootric.androidsdk.network;

import android.util.Log;

import com.wootric.androidsdk.network.responses.AuthenticationResponse;
import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by maciejwitowski on 9/11/15.
 */
public class WootricApiClient {

    private static final String LOG_TAG = WootricApiClient.class.getSimpleName();

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
                endUser.getCreatedAtOrNull(), buildEndUserPropertiesParams(endUser), new Callback<EndUser>() {
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
        wootricApiInterface.updateEndUser(bearerFrom(accessToken), endUser.getId(), buildEndUserPropertiesParams(endUser), new Callback<Object>() {
            @Override
            public void success(Object o, Response response) {
                Log.d(LOG_TAG, "updateEndUser success");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(LOG_TAG, "updateEndUser failure: " + error.getResponse().getStatus());

            }
        });
    }

    private Map<String, String> buildEndUserPropertiesParams(EndUser endUser) {
        if(!endUser.hasProperties())
            return null;

        Map<String, String> properties = new HashMap<String, String>();
        for (Map.Entry<String, String> property : endUser.getProperties().entrySet()) {
            properties.put("properties[" + property.getKey() + "]", property.getValue());
        }

        return properties;
    }

    public void createDecline(EndUser endUser, String accessToken, String originUrl) {
        wootricApiInterface.createDecline(bearerFrom(accessToken), endUser.getId(), originUrl, new Callback<Object>() {
            @Override
            public void success(Object o, Response response) {
                Log.d(LOG_TAG, "createDecline success");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(LOG_TAG, "createDecline failure: " + error.getResponse().getStatus());
            }
        });
    }

    public void createResponse(EndUser endUser, String accessToken, String originUrl, int score, String text) {
        wootricApiInterface.createResponse(bearerFrom(accessToken), endUser.getId(), originUrl, score, text, new Callback<Object>() {
            @Override
            public void success(Object o, Response response) {
                Log.d(LOG_TAG, "createResponse success");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(LOG_TAG, "createResponse failure: " + error.getResponse().getStatus());
            }
        });
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
