package com.wootric.androidsdk;

import com.wootric.androidsdk.tasks.CheckEligibilityTask;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import static com.wootric.androidsdk.TestUtils.*;

/**
 * Created by maciejwitowski on 4/13/15.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, emulateSdk = 21)
public class CheckEligibilityTaskTest {

    @Before
    public void setup() {
    }

    @Test
    public void failsWhenConstructorArgumentsAreInvalid() throws Exception {
        try {
            new CheckEligibilityTask(null, END_USER_EMAIL, -1, -1, -1, -1);
            fail("Null account token should throw exception");
        } catch (IllegalArgumentException expected) {
        }

        try {
            new CheckEligibilityTask(ACCOUNT_TOKEN, null, -1, -1, -1, -1);
            fail("Null end user email token should throw exception");
        } catch (IllegalArgumentException expected) {
        }
    }
}
