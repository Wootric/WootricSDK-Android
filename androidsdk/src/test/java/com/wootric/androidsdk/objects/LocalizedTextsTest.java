package com.wootric.androidsdk.objects;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by maciejwitowski on 9/17/15.
 */
public class LocalizedTextsTest {

    LocalizedTexts localizedTexts;

    @Before
    public void setUp() {
        localizedTexts = new Gson().fromJson(LOCALIZED_TEXTS_JSON, LocalizedTexts.class);
    }

    @Test
    public void localizedTextsAreParsedFromJson() throws Exception {
        assertThat(localizedTexts.getNpsQuestion()).isEqualTo("How likely are you to recommend this product or service to a friend or co-worker?");
        assertThat(localizedTexts.getAnchorLikely()).isEqualTo("Extremely likely");
        assertThat(localizedTexts.getAnchorNotLikely()).isEqualTo("Not at all likely");
        assertThat(localizedTexts.getFollowupQuestion()).isEqualTo("Thank you! Care to tell us why?");
        assertThat(localizedTexts.getFollowupPlaceholder()).isEqualTo("Help us by explaining your score.");
        assertThat(localizedTexts.getFinalThankYou()).isEqualTo("Thank you for your response, and your feedback!");
        assertThat(localizedTexts.getSubmit()).isEqualTo("SEND");
        assertThat(localizedTexts.getDismiss()).isEqualTo("dismiss");
        assertThat(localizedTexts.getSocialShareQuestion()).isEqualTo("Would you be willing to share your positive comments?");
        assertThat(localizedTexts.getSocialShareDecline()).isEqualTo("No thanks...");
    }

    private static final String LOCALIZED_TEXTS_JSON = "{" +
            "\"nps_question\":\"How likely are you to recommend this product or service to a friend or co-worker?\"," +
            "\"anchors\":{" +
                "\"likely\":\"Extremely likely\"," +
                "\"not_likely\":\"Not at all likely\"" +
            "}," +
            "\"followup_question\":\"Thank you! Care to tell us why?\"," +
            "\"followup_placeholder\":\"Help us by explaining your score.\"," +
            "\"final_thank_you\":\"Thank you for your response, and your feedback!\"," +
            "\"send\":\"SEND\"," +
            "\"dismiss\":\"dismiss\"," +
            "\"social_share\":{" +
                "\"question\":\"Would you be willing to share your positive comments?\"," +
                "\"decline\":\"No thanks...\"}" +
            "}";
}