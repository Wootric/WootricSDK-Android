package com.wootric.androidsdk.network;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import retrofit.Callback;

import static com.wootric.androidsdk.TestHelper.ORIGIN_URL;
import static com.wootric.androidsdk.TestHelper.testUser;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by maciejwitowski on 9/15/15.
 */

@RunWith(MockitoJUnitRunner.class)
public class TrackingPixelClientTest {

    @Mock
    TrackingPixelInterface trackingPixelInterface;

    /**
     * getTrackingPixel(User user, EndUser endUser, String originUrl)
     */
    @Test
    public void sendsCorrectRequest() throws Exception {
        TrackingPixelClient trackingPixelClient = new TrackingPixelClient(trackingPixelInterface);

        User user = testUser();
        final String email = "nps@example.com";
        EndUser endUser = new EndUser(email);

        trackingPixelClient.getTrackingPixel(user, endUser, ORIGIN_URL);

        verify(trackingPixelInterface, times(1)).getTrackingPixel(
                eq(testUser().getAccountToken()), eq(email), eq(ORIGIN_URL),
                any(String.class), any(Callback.class));
    }
}
