package com.wootric.androidsdk;

import com.wootric.androidsdk.utils.ConnectionUtils;
import com.wootric.androidsdk.utils.PreferencesUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.wootric.androidsdk.TestUtils.END_USER_EMAIL;
import static com.wootric.androidsdk.TestUtils.ORIGIN_URL;
import static com.wootric.androidsdk.TestUtils.testActivity;
import static com.wootric.androidsdk.TestUtils.testUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * Created by maciejwitowski on 4/11/15.
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, emulateSdk = 21)
public class UserManagerTest {

    private UserManager userManager;

    @Mock ConnectionUtils connectionUtils;
    @Mock PreferencesUtils preferencesUtils;

    @Before
    public void setup() {
        userManager = new UserManager(testActivity(), testUser(), connectionUtils, preferencesUtils);
    }

    @Test public void failsWhenConstructorArgumentsAreInvalid() throws Exception {
        try {
            new UserManager(null, testUser(), connectionUtils, preferencesUtils);
            fail("Null context should throw exception");
        } catch (IllegalArgumentException expected) {
        }

        try {
            new UserManager(testActivity(), null, connectionUtils, preferencesUtils);
            fail("Null user should throw exception");
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test public void endUser_returnsSurveyManagerWithEndUser() throws Exception {
        SurveyManager surveyManager = userManager
                .endUser(END_USER_EMAIL, ORIGIN_URL);

        assertThat(surveyManager).isNotNull();
        assertThat(surveyManager.getEndUser().getEmail()).isEqualTo(END_USER_EMAIL);
    }
}
