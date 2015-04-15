package com.wootric.androidsdk;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static com.wootric.androidsdk.TestUtils.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * Created by maciejwitowski on 4/11/15.
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, emulateSdk = 21)
public class UserManagerTest {

    private UserManager userManager;

    @Before
    public void setup() {
        userManager = new UserManager(testActivity(), TestUtils.CLIENT_ID, TestUtils.CLIENT_SECRET,
                TestUtils.ACCOUNT_TOKEN);
    }

    @Test public void failsWhenConstructorArgumentsAreInvalid() throws Exception {
        try {
            new UserManager(null, TestUtils.CLIENT_ID, TestUtils.CLIENT_SECRET,
                    TestUtils.ACCOUNT_TOKEN);
            fail("Null context should throw exception");
        } catch (IllegalArgumentException expected) {
        }

        try {
            new UserManager(testActivity(), null, TestUtils.CLIENT_SECRET, TestUtils.ACCOUNT_TOKEN);
            fail("Null client id should throw exception");
        } catch (IllegalArgumentException expected) {
        }

        try {
            new UserManager(null, TestUtils.CLIENT_ID, null, TestUtils.ACCOUNT_TOKEN);
            fail("Null client secret should throw exception");
        } catch (IllegalArgumentException expected) {
        }

        try {
            new UserManager(null, TestUtils.CLIENT_ID, TestUtils.CLIENT_SECRET, null);
            fail("Null account token should throw exception");
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test public void returnsInitializedObject() throws Exception {
        assertThat(userManager.getContext()).isEqualTo(testActivity());
        assertThat(userManager.getClientId()).isEqualTo(TestUtils.CLIENT_ID);
        assertThat(userManager.getClientSecret()).isEqualTo(TestUtils.CLIENT_SECRET);
        assertThat(userManager.getAccountToken()).isEqualTo(TestUtils.ACCOUNT_TOKEN);
    }

    @Test public void returnsSurveyManagerFromForEndUser() throws Exception {
        SurveyManager surveyManager = userManager
                .endUser(TestUtils.END_USER_EMAIL, TestUtils.ORIGIN_URL);

        assertThat(surveyManager).isNotNull();
    }
}
