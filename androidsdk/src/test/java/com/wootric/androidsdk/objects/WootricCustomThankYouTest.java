package com.wootric.androidsdk.objects;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by maciejwitowski on 9/23/15.
 */
public class WootricCustomThankYouTest {

    @Test
    public void returnsDetractorQuestion_forNPSDetractorScore() throws Exception {
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setDetractorText("detractor");

        assertThat(customThankYou.getTextForScore(0, "NPS", 0)).isEqualTo("detractor");
        assertThat(customThankYou.getTextForScore(6, "NPS", 0)).isEqualTo("detractor");
    }

    @Test
    public void returnsPassiveQuestion_forNPSPassiveScore() throws Exception {
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setPassiveText("passive");

        assertThat(customThankYou.getTextForScore(7, "NPS", 0)).isEqualTo("passive");
        assertThat(customThankYou.getTextForScore(8, "NPS", 0)).isEqualTo("passive");
    }

    @Test
    public void returnsPromoterQuestion_forNPSPromoterScore() throws Exception {
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setPromoterText("promoter");

        assertThat(customThankYou.getTextForScore(9, "NPS", 0)).isEqualTo("promoter");
        assertThat(customThankYou.getTextForScore(10, "NPS", 0)).isEqualTo("promoter");
    }

    @Test
    public void returnsDefaultQuestion_forNPS() throws Exception {
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setText("thank you");

        assertThat(customThankYou.getTextForScore(0, "NPS", 0)).isEqualTo("thank you");
        assertThat(customThankYou.getTextForScore(9, "NPS", 0)).isEqualTo("thank you");
    }

    @Test
    public void returnsDetractorLinkText_forNPSDetractorScore() throws Exception {
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setDetractorLinkText("detractor");

        assertThat(customThankYou.getLinkTextForScore(0, "NPS", 0)).isEqualTo("detractor");
        assertThat(customThankYou.getLinkTextForScore(6, "NPS", 0)).isEqualTo("detractor");
    }

    @Test
    public void returnsPassiveLinkText_forNPSPassiveScore() throws Exception {
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setPassiveLinkText("passive");

        assertThat(customThankYou.getLinkTextForScore(7, "NPS", 0)).isEqualTo("passive");
        assertThat(customThankYou.getLinkTextForScore(8, "NPS", 0)).isEqualTo("passive");
    }

    @Test
    public void returnsPromoterLinkText_forNPSPromoterScore() throws Exception {
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setPromoterLinkText("promoter");

        assertThat(customThankYou.getLinkTextForScore(9, "NPS", 0)).isEqualTo("promoter");
        assertThat(customThankYou.getLinkTextForScore(10, "NPS", 0)).isEqualTo("promoter");
    }

    @Test
    public void returnsDefaultLinkText_forNPS() throws Exception {
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setLinkText("thank you");

        assertThat(customThankYou.getLinkTextForScore(0, "NPS", 0)).isEqualTo("thank you");
        assertThat(customThankYou.getLinkTextForScore(9, "NPS", 0)).isEqualTo("thank you");
    }

    @Test
    public void returnsDetractorQuestion_forCESDetractorScore() throws Exception {
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setDetractorText("detractor");

        assertThat(customThankYou.getTextForScore(1, "CES", 0)).isEqualTo("detractor");
        assertThat(customThankYou.getTextForScore(3, "CES", 0)).isEqualTo("detractor");
    }

    @Test
    public void returnsPassiveQuestion_forCESPassiveScore() throws Exception {
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setPassiveText("passive");

        assertThat(customThankYou.getTextForScore(4, "CES", 0)).isEqualTo("passive");
        assertThat(customThankYou.getTextForScore(5, "CES", 0)).isEqualTo("passive");
    }

    @Test
    public void returnsPromoterQuestion_forCESPromoterScore() throws Exception {
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setPromoterText("promoter");

        assertThat(customThankYou.getTextForScore(6, "CES", 0)).isEqualTo("promoter");
        assertThat(customThankYou.getTextForScore(7, "CES", 0)).isEqualTo("promoter");
    }

    @Test
    public void returnsDefaultQuestion_forCES() throws Exception {
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setText("thank you");

        assertThat(customThankYou.getTextForScore(1, "CES", 0)).isEqualTo("thank you");
        assertThat(customThankYou.getTextForScore(7, "CES", 0)).isEqualTo("thank you");
    }

    @Test
    public void returnsDetractorLinkText_forCESDetractorScore() throws Exception {
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setDetractorLinkText("detractor");

        assertThat(customThankYou.getLinkTextForScore(1, "CES", 0)).isEqualTo("detractor");
        assertThat(customThankYou.getLinkTextForScore(3, "CES", 0)).isEqualTo("detractor");
    }

    @Test
    public void returnsPassiveLinkText_forCESPassiveScore() throws Exception {
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setPassiveLinkText("passive");

        assertThat(customThankYou.getLinkTextForScore(4, "CES", 0)).isEqualTo("passive");
        assertThat(customThankYou.getLinkTextForScore(5, "CES", 0)).isEqualTo("passive");
    }

    @Test
    public void returnsPromoterLinkText_forCESPromoterScore() throws Exception {
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setPromoterLinkText("promoter");

        assertThat(customThankYou.getLinkTextForScore(6, "CES", 0)).isEqualTo("promoter");
        assertThat(customThankYou.getLinkTextForScore(7, "CES", 0)).isEqualTo("promoter");
    }

    @Test
    public void returnsDefaultLinkText_CES() throws Exception {
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setLinkText("thank you");

        assertThat(customThankYou.getLinkTextForScore(1, "CES", 0)).isEqualTo("thank you");
        assertThat(customThankYou.getLinkTextForScore(7, "CES", 0)).isEqualTo("thank you");
    }

    @Test
    public void returnsDetractorQuestion_forCSATDetractorScore() throws Exception {
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setDetractorText("detractor");

        assertThat(customThankYou.getTextForScore(1, "CSAT", 0)).isEqualTo("detractor");
        assertThat(customThankYou.getTextForScore(2, "CSAT", 0)).isEqualTo("detractor");
    }

    @Test
    public void returnsPassiveQuestion_forCSATPassiveScore() throws Exception {
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setPassiveText("passive");

        assertThat(customThankYou.getTextForScore(3, "CSAT", 0)).isEqualTo("passive");
    }

    @Test
    public void returnsPromoterQuestion_forCSATPromoterScore() throws Exception {
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setPromoterText("promoter");

        assertThat(customThankYou.getTextForScore(4, "CSAT", 0)).isEqualTo("promoter");
        assertThat(customThankYou.getTextForScore(5, "CSAT", 0)).isEqualTo("promoter");
    }

    @Test
    public void returnsDefaultQuestion_forCSAT() throws Exception {
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setText("thank you");

        assertThat(customThankYou.getTextForScore(1, "CSAT", 0)).isEqualTo("thank you");
        assertThat(customThankYou.getTextForScore(5, "CSAT", 0)).isEqualTo("thank you");
    }

    @Test
    public void returnsDetractorLinkText_forCSATDetractorScore() throws Exception {
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setDetractorLinkText("detractor");

        assertThat(customThankYou.getLinkTextForScore(1, "CSAT", 0)).isEqualTo("detractor");
        assertThat(customThankYou.getLinkTextForScore(2, "CSAT", 0)).isEqualTo("detractor");
    }

    @Test
    public void returnsPassiveLinkText_forCSATPassiveScore() throws Exception {
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setPassiveLinkText("passive");

        assertThat(customThankYou.getLinkTextForScore(3, "CSAT", 0)).isEqualTo("passive");
    }

    @Test
    public void returnsPromoterLinkText_forCSATPromoterScore() throws Exception {
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setPromoterLinkText("promoter");

        assertThat(customThankYou.getLinkTextForScore(4, "CSAT", 0)).isEqualTo("promoter");
        assertThat(customThankYou.getLinkTextForScore(5, "CSAT", 0)).isEqualTo("promoter");
    }

    @Test
    public void returnsCSATDefaultLinkText() throws Exception {
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setLinkText("thank you");

        assertThat(customThankYou.getLinkTextForScore(1, "CSAT", 0)).isEqualTo("thank you");
        assertThat(customThankYou.getLinkTextForScore(5, "CSAT", 0)).isEqualTo("thank you");
    }
}
