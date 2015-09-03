package com.wootric.androidsdk;

import com.wootric.androidsdk.objects.CustomMessage;
import com.wootric.androidsdk.objects.Settings;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by maciejwitowski on 5/5/15.
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, emulateSdk = 21)
public class CustomMessageTest {

    Settings settings;

    @Before
    public void setup() {
        settings = new Settings();
    }

    @Test
    public void fromJson_buildsSurveyObject() throws Exception {
        JSONObject json = new JSONObject();
        json.put("followup_question", "FOLLOWUP");
        JSONObject followupQuestions = new JSONObject();
        followupQuestions.put("detractor_question", "FOLLOWUP DETRACTOR");
        followupQuestions.put("passive_question", "FOLLOWUP PASSIVE");
        followupQuestions.put("promoter_question", "FOLLOWUP PROMOTER");
        json.put("followup_questions_list", followupQuestions);

        json.put("placeholder_text", "PLACEHOLDER");
        JSONObject placeholderQuestions = new JSONObject();
        placeholderQuestions.put("detractor_text", "PLACEHOLDER DETRACTOR");
        placeholderQuestions.put("passive_text", "PLACEHOLDER PASSIVE");
        placeholderQuestions.put("promoter_text", "PLACEHOLDER PROMOTER");
        json.put("placeholder_texts_list", placeholderQuestions);

        json.put("wootric_recommend_target", "RECOMMEND TARGET");

        CustomMessage message = CustomMessage.fromJson(json);
        assertThat(message.getFollowupQuestion()).isEqualTo("FOLLOWUP");
        assertThat(message.getDetractorFollowupQuestion()).isEqualTo("FOLLOWUP DETRACTOR");
        assertThat(message.getPassiveFollowupQuestion()).isEqualTo("FOLLOWUP PASSIVE");
        assertThat(message.getPromoterFollowupQuestion()).isEqualTo("FOLLOWUP PROMOTER");
        assertThat(message.getPlaceholder()).isEqualTo("PLACEHOLDER");
        assertThat(message.getDetractorPlaceholder()).isEqualTo("PLACEHOLDER DETRACTOR");
        assertThat(message.getPassivePlaceholder()).isEqualTo("PLACEHOLDER PASSIVE");
        assertThat(message.getPromoterPlaceholder()).isEqualTo("PLACEHOLDER PROMOTER");
        assertThat(message.getRecommendTarget()).isEqualTo("RECOMMEND TARGET");
    }
}
