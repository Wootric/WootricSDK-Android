package com.wootric.androidsdk.network;

import com.wootric.androidsdk.network.responses.AuthenticationResponse;
import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;

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
public class WootricApiClientTest {

    @Mock
    WootricApiInterface wootricApiInterface;

    /**
     * authenticate(User user, final WootricApiCallback wootricApiCallback)
     */
    @Test
    public void sendsCorrectAuthenticateRequest() throws Exception {
        WootricApiClient wootricClientTest = new WootricApiClient(wootricApiInterface);

        User user = testUser();
        wootricClientTest.authenticate(user, new MockWootricApiCallback());

        verify(wootricApiInterface, times(1)).authenticate(eq("client_credentials"), eq(user.getClientId()),
                eq(user.getClientSecret()), any(Callback.class));
    }

    /**
     * getEndUserByEmail(String email, String accessToken, final WootricApiCallback wootricApiCallback)
     */
    @Test
    public void sendsCorrectGetEndUserByEmailRequest() throws Exception {
        WootricApiClient wootricClientTest = new WootricApiClient(wootricApiInterface);

        final String email = "nps@example.com";
        final String accessToken = "123test";
        wootricClientTest.getEndUserByEmail(email, accessToken, new MockWootricApiCallback());

        verify(wootricApiInterface, times(1)).getEndUserByEmail(eq(email), eq("Bearer " + accessToken), any(Callback.class));
    }

    /**
     * createEndUser(EndUser endUser, String accessToken, final WootricApiCallback wootricApiCallback)
     */
    @Test
    public void sendsCorrectCreateEndUserRequest() throws Exception {
        WootricApiClient wootricClientTest = new WootricApiClient(wootricApiInterface);

        final String email = "nps@example.com";
        EndUser endUser = new EndUser(email);
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("company", "Wootric");
        properties.put("type", "free");
        endUser.setProperties(properties);

        HashMap<String, String> propertiesParams = new HashMap<String, String>();
        propertiesParams.put("properties[company]", "Wootric");
        propertiesParams.put("properties[type]", "free");


        long endUserCreatedAt = new Date().getTime();
        endUser.setCreatedAt(endUserCreatedAt);
        final String accessToken = "123test";
        wootricClientTest.createEndUser(endUser, accessToken, new MockWootricApiCallback());

        verify(wootricApiInterface, times(1)).createEndUser(eq("Bearer " + accessToken), eq(email),
                eq(endUserCreatedAt), eq(propertiesParams), any(Callback.class));
    }

    /**
     * updateEndUser(EndUser endUser, String accessToken)
     */

    @Test
    public void sendsCorrectUpdateEndUserRequest() throws Exception {
        WootricApiClient wootricClientTest = new WootricApiClient(wootricApiInterface);

        EndUser endUser = new EndUser();
        long endUserId = 1;
        endUser.setId(endUserId);
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("company", "Wootric");
        properties.put("type", "free");
        endUser.setProperties(properties);

        HashMap<String, String> propertiesParams = new HashMap<String, String>();
        propertiesParams.put("properties[company]", "Wootric");
        propertiesParams.put("properties[type]", "free");

        final String accessToken = "123test";
        wootricClientTest.updateEndUser(endUser, accessToken);

        verify(wootricApiInterface, times(1)).updateEndUser(eq("Bearer " + accessToken), eq(endUserId),
                eq(propertiesParams), any(Callback.class));
    }

    /**
     * createDecline(EndUser endUser, String accessToken, String originUrl)
     */
    @Test
    public void sendsCorrectCreateDeclineRequest() throws Exception {
        WootricApiClient wootricApiClient = new WootricApiClient(wootricApiInterface);

        EndUser endUser = new EndUser();
        final long endUserId = 123;
        endUser.setId(endUserId);
        final String accessToken = "123test";

        wootricApiClient.createDecline(endUser, accessToken, ORIGIN_URL);

        verify(wootricApiInterface, times(1)).createDecline(eq("Bearer " + accessToken), eq(endUserId),
                eq(ORIGIN_URL), any(Callback.class));
    }

    @Test
    public void sendsCorrectCreateResponseRequest() throws Exception {
        WootricApiClient wootricApiClient = new WootricApiClient(wootricApiInterface);

        EndUser endUser = new EndUser();
        final long endUserId = 123;
        endUser.setId(endUserId);
        final String accessToken = "123test";
        final int score = 10;
        final String text = "Great work!";

        wootricApiClient.createResponse(endUser, accessToken, ORIGIN_URL, score, text);

        verify(wootricApiInterface, times(1)).createResponse(eq("Bearer " + accessToken), eq(endUserId),
                eq(ORIGIN_URL), eq(score), eq(text), any(Callback.class));
    }

    private class MockWootricApiCallback implements WootricApiClient.WootricApiCallback {
        @Override
        public void onAuthenticateSuccess(AuthenticationResponse authenticationResponse) {

        }

        @Override
        public void onGetEndUserSuccess(List<EndUser> endUser) {

        }

        @Override
        public void onCreateEndUserSuccess(EndUser endUser) {

        }

        @Override
        public void onApiError(RetrofitError error) {

        }
    }
}
