package com.wootric.androidsdk.network;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by maciejwitowski on 9/15/15.
 */
interface TrackingPixelInterface {
    @GET("/pixel.gif")
    void getTrackingPixel(@Query("account_token") String accountToken,
                          @Query("email") String email,
                          @Query("url") String url,
                          @Query("random") String random,
                          Callback<Object> objectCallback);
}
