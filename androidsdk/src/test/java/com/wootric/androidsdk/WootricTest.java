package com.wootric.androidsdk;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.wootric.androidsdk.TestUtils.testActivity;
import static junit.framework.Assert.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by maciejwitowski on 4/11/15.
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, emulateSdk = 21)
public class WootricTest {

    @Test public void failsWhenContextIsInvalid() throws Exception {
        try {
            Wootric.singleton = null;
            Wootric.with(null);
            fail("Null activity should throw exception");
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test public void initializeAndReturnsSingleton() throws Exception {
        Wootric.singleton = null;
        Wootric newSingleton = Wootric.with(testActivity());

        assertThat(newSingleton).isNotNull();
        assertThat(newSingleton).isEqualTo(Wootric.singleton);
    }

    @Test public void with_returnsExistingSingleton() throws Exception {
        Wootric wootric = Wootric.with(testActivity());
        Wootric nextWootric = Wootric.with(testActivity());

        assertThat(nextWootric).isEqualTo(wootric);
    }

    @Test public void stop_CallsInvalidateActivityOnUserManager() throws Exception {
        UserManager mockedUserManager = mock(UserManager.class);

        Wootric wootric = Wootric.with(testActivity());
        wootric.userManager = mockedUserManager;

        Wootric.stop();

        verify(mockedUserManager).invalidateActivity();
    }
}
