package com.wootric.androidsdk.objects;

import org.junit.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by maciejwitowski on 9/23/15.
 */
public class CustomThankYouMessageTest {

    /**
     * getFollowupQuestionForScore(int score)
     */
    @Test
    public void returnsDetractorQuestion_forDetractorScore() throws Exception {
        CustomThankYouMessage customThankYouMessage = new CustomThankYouMessage();
        customThankYouMessage.setDetractorThankYou("detractor");

        assertThat(customThankYouMessage.getThankYouForScore(0)).isEqualTo("detractor");
        assertThat(customThankYouMessage.getThankYouForScore(6)).isEqualTo("detractor");
    }

    @Test
    public void returnsPassiveQuestion_forPassiveScore() throws Exception {
        CustomThankYouMessage customThankYouMessage = new CustomThankYouMessage();
        customThankYouMessage.setPassiveThankYou("passive");

        assertThat(customThankYouMessage.getThankYouForScore(7)).isEqualTo("passive");
        assertThat(customThankYouMessage.getThankYouForScore(8)).isEqualTo("passive");
    }

    @Test
    public void returnsPromoterQuestion_forPromoterScore() throws Exception {
        CustomThankYouMessage customThankYouMessage = new CustomThankYouMessage();
        customThankYouMessage.setPromoterThankYou("promoter");

        assertThat(customThankYouMessage.getThankYouForScore(9)).isEqualTo("promoter");
        assertThat(customThankYouMessage.getThankYouForScore(10)).isEqualTo("promoter");
    }

    @Test
    public void returnsDefaultQuestion() throws Exception {
        CustomThankYouMessage customThankYouMessage = new CustomThankYouMessage();
        customThankYouMessage.setThankYou("thank you");

        assertThat(customThankYouMessage.getThankYouForScore(0)).isEqualTo("thank you");
        assertThat(customThankYouMessage.getThankYouForScore(9)).isEqualTo("thank you");
    }
}
