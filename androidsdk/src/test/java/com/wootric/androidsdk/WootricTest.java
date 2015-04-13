package com.wootric.androidsdk;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * Created by maciejwitowski on 4/11/15.
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, emulateSdk = 21)
public class WootricTest {

    private Context context;

    @Before
    public void setup() {
        context = RuntimeEnvironment.application;
    }

    @Test public void failsWhenContextIsInvalid() throws Exception {
        try {
            Wootric.singleton = null;
            Wootric.with(null);
            fail("Null context should throw exception");
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test public void initializeAndReturnsSingleton() throws Exception {
        Wootric.singleton = null;
        Wootric newSingleton = Wootric.with(context);

        assertThat(newSingleton.context).isNotNull();
        assertThat(newSingleton).isEqualTo(Wootric.singleton);
    }

    @Test public void returnsExistingSingletonFromWith() throws Exception {
        Wootric wootric = Wootric.with(context);
        Wootric nextWootric = Wootric.with(context);

        assertThat(nextWootric).isEqualTo(wootric);
    }

    @Test public void returnInitializedWootricUserManager() throws Exception {
        Wootric wootric = Wootric.with(context);
        UserManager userManager =
                wootric.user(TestUtils.CLIENT_ID, TestUtils.CLIENT_SECRET, TestUtils.ACCOUNT_TOKEN);

        assertThat(userManager).isNotNull();
        assertThat(userManager.getClientId()).isEqualTo(TestUtils.CLIENT_ID);
        assertThat(userManager.getClientSecret()).isEqualTo(TestUtils.CLIENT_SECRET);
        assertThat(userManager.getAccountToken()).isEqualTo(TestUtils.ACCOUNT_TOKEN);
    }
}
