package com.wootric.androidsdk.objects;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by maciejwitowski on 9/23/15.
 */
public class CustomThankYouTest {

    /**
     * getTextForScore(int score)
     */
    @Test
    public void returnsDetractorQuestion_forDetractorScore() throws Exception {
        CustomThankYou customThankYou = new CustomThankYou();
        customThankYou.setDetractorText("detractor");

        assertThat(customThankYou.getTextForScore(0)).isEqualTo("detractor");
        assertThat(customThankYou.getTextForScore(6)).isEqualTo("detractor");
    }

    @Test
    public void returnsPassiveQuestion_forPassiveScore() throws Exception {
        CustomThankYou customThankYou = new CustomThankYou();
        customThankYou.setPassiveText("passive");

        assertThat(customThankYou.getTextForScore(7)).isEqualTo("passive");
        assertThat(customThankYou.getTextForScore(8)).isEqualTo("passive");
    }

    @Test
    public void returnsPromoterQuestion_forPromoterScore() throws Exception {
        CustomThankYou customThankYou = new CustomThankYou();
        customThankYou.setPromoterText("promoter");

        assertThat(customThankYou.getTextForScore(9)).isEqualTo("promoter");
        assertThat(customThankYou.getTextForScore(10)).isEqualTo("promoter");
    }

    @Test
    public void returnsDefaultQuestion() throws Exception {
        CustomThankYou customThankYou = new CustomThankYou();
        customThankYou.setText("thank you");

        assertThat(customThankYou.getTextForScore(0)).isEqualTo("thank you");
        assertThat(customThankYou.getTextForScore(9)).isEqualTo("thank you");
    }

    /**
     * getLinkTextForScore(int score)
     */
    @Test
    public void returnsDetractorLinkText_forDetractorScore() throws Exception {
        CustomThankYou customThankYou = new CustomThankYou();
        customThankYou.setDetractorLinkText("detractor");

        assertThat(customThankYou.getLinkTextForScore(0)).isEqualTo("detractor");
        assertThat(customThankYou.getLinkTextForScore(6)).isEqualTo("detractor");
    }

    @Test
    public void returnsPassiveLinkText_forPassiveScore() throws Exception {
        CustomThankYou customThankYou = new CustomThankYou();
        customThankYou.setPassiveLinkText("passive");

        assertThat(customThankYou.getLinkTextForScore(7)).isEqualTo("passive");
        assertThat(customThankYou.getLinkTextForScore(8)).isEqualTo("passive");
    }

    @Test
    public void returnsPromoterLinkText_forPromoterScore() throws Exception {
        CustomThankYou customThankYou = new CustomThankYou();
        customThankYou.setPromoterLinkText("promoter");

        assertThat(customThankYou.getLinkTextForScore(9)).isEqualTo("promoter");
        assertThat(customThankYou.getLinkTextForScore(10)).isEqualTo("promoter");
    }

    @Test
    public void returnsDefaultLinkText() throws Exception {
        CustomThankYou customThankYou = new CustomThankYou();
        customThankYou.setLinkText("thank you");

        assertThat(customThankYou.getLinkTextForScore(0)).isEqualTo("thank you");
        assertThat(customThankYou.getLinkTextForScore(9)).isEqualTo("thank you");
    }

    /**
     * getLinkUrlForScore(int score)
     */
    @Test
    public void returnsDetractorLinkUrl_forDetractorScore() throws Exception {
        CustomThankYou customThankYou = new CustomThankYou();
        customThankYou.setDetractorLinkUrl("detractor");

        assertThat(customThankYou.getLinkUrlForScore(0)).isEqualTo("detractor");
        assertThat(customThankYou.getLinkUrlForScore(6)).isEqualTo("detractor");
    }

    @Test
    public void returnsPassiveLinkUrl_forPassiveScore() throws Exception {
        CustomThankYou customThankYou = new CustomThankYou();
        customThankYou.setPassiveLinkUrl("passive");

        assertThat(customThankYou.getLinkUrlForScore(7)).isEqualTo("passive");
        assertThat(customThankYou.getLinkUrlForScore(8)).isEqualTo("passive");
    }

    @Test
    public void returnsPromoterLinkUrl_forPromoterScore() throws Exception {
        CustomThankYou customThankYou = new CustomThankYou();
        customThankYou.setPromoterLinkUrl("promoter");

        assertThat(customThankYou.getLinkUrlForScore(9)).isEqualTo("promoter");
        assertThat(customThankYou.getLinkUrlForScore(10)).isEqualTo("promoter");
    }

    @Test
    public void returnsDefaultLinkUrl() throws Exception {
        CustomThankYou customThankYou = new CustomThankYou();
        customThankYou.setLinkUrl("thank you");

        assertThat(customThankYou.getLinkUrlForScore(0)).isEqualTo("thank you");
        assertThat(customThankYou.getLinkUrlForScore(9)).isEqualTo("thank you");
    }
}
