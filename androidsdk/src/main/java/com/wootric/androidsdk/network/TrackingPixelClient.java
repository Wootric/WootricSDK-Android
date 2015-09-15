package com.wootric.androidsdk.network;

import android.util.Log;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.User;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by maciejwitowski on 9/11/15.
 */
public class TrackingPixelClient {

    private static final String LOG_TAG = TrackingPixelClient.class.getName();

    private static final String TRACKING_PIXEL_BASE_URL = "https://d8myem934l1zi.cloudfront.net";

    private final TrackingPixelInterface trackingPixelInterface;

    public TrackingPixelClient() {
        RestAdapter retrofit = new RestAdapter.Builder()
                .setEndpoint(TRACKING_PIXEL_BASE_URL)
                .build();

        trackingPixelInterface = retrofit.create(TrackingPixelInterface.class);
    }

    // For testing purposes
    public TrackingPixelClient(TrackingPixelInterface trackingPixelInterface) {
        this.trackingPixelInterface = trackingPixelInterface;
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
}
