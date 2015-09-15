package com.wootric.androidsdk.network;

import com.wootric.androidsdk.network.responses.AuthenticationResponse;
import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;

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
        long endUserCreatedAt = new Date().getTime();
        endUser.setCreatedAt(endUserCreatedAt);
        final String accessToken = "123test";
        wootricClientTest.createEndUser(endUser, accessToken, new MockWootricApiCallback());

        verify(wootricApiInterface, times(1)).createEndUser(eq("Bearer " + accessToken), eq(email),
                eq(endUserCreatedAt), any(Callback.class));
    }

    /**
     * updateEndUser(EndUser endUser, String accessToken)
     */

    @Test
    public void sendsCorrectUpdateEndUserRequest() throws Exception {
        // TODO
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
