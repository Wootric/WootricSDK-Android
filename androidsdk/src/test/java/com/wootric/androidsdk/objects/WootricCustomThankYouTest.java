package com.wootric.androidsdk.objects;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by maciejwitowski on 9/23/15.
 */
public class WootricCustomThankYouTest {

    /**
     * getTextForScore(int score)
     */
    @Test
    public void returnsDetractorQuestion_forDetractorScore() throws Exception {
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setDetractorText("detractor");

        assertThat(customThankYou.getTextForScore(0)).isEqualTo("detractor");
        assertThat(customThankYou.getTextForScore(6)).isEqualTo("detractor");
    }

    @Test
    public void returnsPassiveQuestion_forPassiveScore() throws Exception {
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setPassiveText("passive");

        assertThat(customThankYou.getTextForScore(7)).isEqualTo("passive");
        assertThat(customThankYou.getTextForScore(8)).isEqualTo("passive");
    }

    @Test
    public void returnsPromoterQuestion_forPromoterScore() throws Exception {
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setPromoterText("promoter");

        assertThat(customThankYou.getTextForScore(9)).isEqualTo("promoter");
        assertThat(customThankYou.getTextForScore(10)).isEqualTo("promoter");
    }

    @Test
    public void returnsDefaultQuestion() throws Exception {
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setText("thank you");

        assertThat(customThankYou.getTextForScore(0)).isEqualTo("thank you");
        assertThat(customThankYou.getTextForScore(9)).isEqualTo("thank you");
    }

    /**
     * getLinkTextForScore(int score)
     */
    @Test
    public void returnsDetractorLinkText_forDetractorScore() throws Exception {
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setDetractorLinkText("detractor");

        assertThat(customThankYou.getLinkTextForScore(0)).isEqualTo("detractor");
        assertThat(customThankYou.getLinkTextForScore(6)).isEqualTo("detractor");
    }

    @Test
    public void returnsPassiveLinkText_forPassiveScore() throws Exception {
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setPassiveLinkText("passive");

        assertThat(customThankYou.getLinkTextForScore(7)).isEqualTo("passive");
        assertThat(customThankYou.getLinkTextForScore(8)).isEqualTo("passive");
    }

    @Test
    public void returnsPromoterLinkText_forPromoterScore() throws Exception {
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setPromoterLinkText("promoter");

        assertThat(customThankYou.getLinkTextForScore(9)).isEqualTo("promoter");
        assertThat(customThankYou.getLinkTextForScore(10)).isEqualTo("promoter");
    }

    @Test
    public void returnsDefaultLinkText() throws Exception {
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setLinkText("thank you");

        assertThat(customThankYou.getLinkTextForScore(0)).isEqualTo("thank you");
        assertThat(customThankYou.getLinkTextForScore(9)).isEqualTo("thank you");
    }
}
