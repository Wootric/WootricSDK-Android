package com.wootric.androidsdk;

import android.app.Activity;
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
public class SurveyManagerTest {

    private Context context;
    private SurveyManager surveyManager;
    private SurveyValidator surveyValidator;

    @Before
    public void setup() {
        context = RuntimeEnvironment.application;
        surveyValidator = new SurveyValidator(context,
                TestUtils.ACCOUNT_TOKEN, TestUtils.END_USER_EMAIL);

        surveyManager = new SurveyManager((Activity)context, surveyValidator, TestUtils.END_USER_EMAIL,
                TestUtils.ORIGIN_URL);
    }

    @Test
    public void failsWhenConstructorArgumentsAreInvalid() throws Exception {
        try {
            new SurveyManager(null, surveyValidator, TestUtils.END_USER_EMAIL, TestUtils.ACCOUNT_TOKEN);
            fail("Null context should throw exception");
        } catch (IllegalArgumentException expected) {
        }

        try {
            new SurveyManager((Activity)context, null, TestUtils.END_USER_EMAIL, TestUtils.ACCOUNT_TOKEN);
            fail("Null survey validator email should throw exception");
        } catch (IllegalArgumentException expected) {
        }

        try {
            new SurveyManager((Activity) context, surveyValidator, null, TestUtils.ACCOUNT_TOKEN);
            fail("Null end user email should throw exception");
        } catch (IllegalArgumentException expected) {
        }

        try {
            new SurveyManager((Activity) context, surveyValidator, TestUtils.END_USER_EMAIL, null);
            fail("Null account token should throw exception");
        } catch (IllegalArgumentException expected) {
        }
    }
}