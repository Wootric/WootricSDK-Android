package com.wootric.androidsdk.network;

import android.util.Log;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.User;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by maciejwitowski on 9/11/15.
 */
public class TrackingPixelClient {

    private static final String LOG_TAG = TrackingPixelClient.class.getName();

    private static final String TRACKING_PIXEL_BASE_URL = "https://d8myem934l1zi.cloudfront.net";

    private TrackingPixelInterface trackingPixelInterface;

    public TrackingPixelClient() {
        RestAdapter retrofit = new RestAdapter.Builder()
                .setEndpoint(TRACKING_PIXEL_BASE_URL)
                .build();

        trackingPixelInterface = retrofit.create(TrackingPixelInterface.class);
    }

    public void getTrackingPixel(User user, EndUser endUser, String originUrl) {

        trackingPixelInterface.getTrackingPixel(user.getAccountToken(), endUser.getEmail(),
                originUrl, String.valueOf(Math.random()), new Callback<Object>() {
                    @Override
                    public void success(Object o, Response response) {
                        Log.d(LOG_TAG, "success: " + o.toString());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(LOG_TAG, "failure: " + error.toString());
                    }
                });
    }

    public interface TrackingPixelInterface {
        @GET("/pixel.gif")
        void getTrackingPixel(@Query("account_token") String accountToken,
                              @Query("email") String email,
                              @Query("url") String url,
                              @Query("random") String random,
                              Callback<Object> objectCallback);
    }
}
