package com.wootric.androidsdk.objects;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class WootricCustomMessageTest {

    WootricCustomMessage customMessage;

    @Before
    public void setUp() {
        customMessage = new WootricCustomMessage();
        customMessage.setFollowupQuestion("followup_text");
        customMessage.setDetractorFollowupQuestion("detractor_followup_text");
        customMessage.setPassiveFollowupQuestion("passive_followup_text");
        customMessage.setPromoterFollowupQuestion("promoter_followup_text");
        customMessage.setPlaceholderText("prompt_text");
        customMessage.setDetractorPlaceholderText("detractor_prompt_text");
        customMessage.setPassivePlaceholderText("passive_prompt_text");
        customMessage.setPromoterPlaceholderText("promoter_prompt_text");
    }

    @Test
    public void customMessageIsCorrectlyBuild() throws Exception {
        assertThat(customMessage.getFollowupQuestion()).isEqualTo("followup_text");
        assertThat(customMessage.getDetractorQuestion()).isEqualTo("detractor_followup_text");
        assertThat(customMessage.getPassiveQuestion()).isEqualTo("passive_followup_text");
        assertThat(customMessage.getPromoterQuestion()).isEqualTo("promoter_followup_text");
        assertThat(customMessage.getPlaceholderText()).isEqualTo("prompt_text");
        assertThat(customMessage.getDetractorPlaceholder()).isEqualTo("detractor_prompt_text");
        assertThat(customMessage.getPassivePlaceholder()).isEqualTo("passive_prompt_text");
        assertThat(customMessage.getPromoterPlaceholder()).isEqualTo("promoter_prompt_text");
    }

    @Test
    public void returnsDetractorQuestion_forNPSDetractorScore() throws Exception {
        assertThat(customMessage.getFollowupQuestionForScore(0, "NPS", 0)).isEqualTo("detractor_followup_text");
        assertThat(customMessage.getFollowupQuestionForScore(6, "NPS", 0)).isEqualTo("detractor_followup_text");
    }

    @Test
    public void returnsPassiveQuestion_forNPSPassiveScore() throws Exception {
        assertThat(customMessage.getFollowupQuestionForScore(7, "NPS", 0)).isEqualTo("passive_followup_text");
        assertThat(customMessage.getFollowupQuestionForScore(8, "NPS", 0)).isEqualTo("passive_followup_text");
    }

    @Test
    public void returnsPromoterQuestion_forNPSPromoterScore() throws Exception {
        assertThat(customMessage.getFollowupQuestionForScore(9, "NPS", 0)).isEqualTo("promoter_followup_text");
        assertThat(customMessage.getFollowupQuestionForScore(10, "NPS", 0)).isEqualTo("promoter_followup_text");
    }

    @Test
    public void returnsDefaultQuestion_forNPS() throws Exception {
        customMessage.setFollowupQuestionsList(new HashMap<String, String>());
        assertThat(customMessage.getFollowupQuestionForScore(9, "NPS", 0)).isEqualTo("followup_text");
    }

    @Test
    public void returnsDetractorPlaceholder_forNPSDetractorScore() throws Exception {
        assertThat(customMessage.getPlaceholderForScore(0, "NPS", 0)).isEqualTo("detractor_prompt_text");
        assertThat(customMessage.getPlaceholderForScore(6, "NPS", 0)).isEqualTo("detractor_prompt_text");
    }

    @Test
    public void returnsPassivePlaceholder_forNPSPassiveScore() throws Exception {
        assertThat(customMessage.getPlaceholderForScore(7, "NPS", 0)).isEqualTo("passive_prompt_text");
        assertThat(customMessage.getPlaceholderForScore(8, "NPS", 0)).isEqualTo("passive_prompt_text");
    }

    @Test
    public void returnsPromoterPlaceholder_forNPSPromoterScore() throws Exception {
        assertThat(customMessage.getPlaceholderForScore(9, "NPS", 0)).isEqualTo("promoter_prompt_text");
        assertThat(customMessage.getPlaceholderForScore(10, "NPS", 0)).isEqualTo("promoter_prompt_text");
    }

    @Test
    public void returnsDefaultPlaceholder_forNPS() throws Exception {
        customMessage.setPlaceholderTextsList(new HashMap<String, String>());
        assertThat(customMessage.getPlaceholderForScore(9, "NPS", 0)).isEqualTo("prompt_text");
    }

    @Test
    public void returnsDetractorQuestion_forCESDetractorScore() throws Exception {
        assertThat(customMessage.getFollowupQuestionForScore(1, "CES", 0)).isEqualTo("detractor_followup_text");
        assertThat(customMessage.getFollowupQuestionForScore(3, "CES", 0)).isEqualTo("detractor_followup_text");
    }

    @Test
    public void returnsPassiveQuestion_forCESPassiveScore() throws Exception {
        assertThat(customMessage.getFollowupQuestionForScore(4, "CES", 0)).isEqualTo("passive_followup_text");
        assertThat(customMessage.getFollowupQuestionForScore(5, "CES", 0)).isEqualTo("passive_followup_text");
    }

    @Test
    public void returnsPromoterQuestion_forCESPromoterScore() throws Exception {
        assertThat(customMessage.getFollowupQuestionForScore(6, "CES", 0)).isEqualTo("promoter_followup_text");
        assertThat(customMessage.getFollowupQuestionForScore(7, "CES", 0)).isEqualTo("promoter_followup_text");
    }

    @Test
    public void returnsDefaultQuestion_forCES() throws Exception {
        customMessage.setFollowupQuestionsList(new HashMap<String, String>());
        assertThat(customMessage.getFollowupQuestionForScore(1, "CES", 0)).isEqualTo("followup_text");
        assertThat(customMessage.getFollowupQuestionForScore(7, "CES", 0)).isEqualTo("followup_text");
    }

    @Test
    public void returnsDetractorPlaceholder_forCESDetractorScore() throws Exception {
        assertThat(customMessage.getPlaceholderForScore(1, "CES", 0)).isEqualTo("detractor_prompt_text");
        assertThat(customMessage.getPlaceholderForScore(3, "CES", 0)).isEqualTo("detractor_prompt_text");
    }

    @Test
    public void returnsPassivePlaceholder_forCESPassiveScore() throws Exception {
        assertThat(customMessage.getPlaceholderForScore(4, "CES", 0)).isEqualTo("passive_prompt_text");
        assertThat(customMessage.getPlaceholderForScore(5, "CES", 0)).isEqualTo("passive_prompt_text");
    }

    @Test
    public void returnsPromoterPlaceholder_forCESPromoterScore() throws Exception {
        assertThat(customMessage.getPlaceholderForScore(6, "CES", 0)).isEqualTo("promoter_prompt_text");
        assertThat(customMessage.getPlaceholderForScore(7, "CES", 0)).isEqualTo("promoter_prompt_text");
    }

    @Test
    public void returnsDefaultPlaceholder_forCES() throws Exception {
        customMessage.setPlaceholderTextsList(new HashMap<String, String>());
        assertThat(customMessage.getPlaceholderForScore(1, "CES", 0)).isEqualTo("prompt_text");
        assertThat(customMessage.getPlaceholderForScore(7, "CES", 0)).isEqualTo("prompt_text");
    }
    //////////
    @Test
    public void returnsDetractorQuestion_forCSATDetractorScore() throws Exception {
        assertThat(customMessage.getFollowupQuestionForScore(1, "CSAT", 0)).isEqualTo("detractor_followup_text");
        assertThat(customMessage.getFollowupQuestionForScore(2, "CSAT", 0)).isEqualTo("detractor_followup_text");
    }

    @Test
    public void returnsPassiveQuestion_forCSATPassiveScore() throws Exception {
        assertThat(customMessage.getFollowupQuestionForScore(3, "CSAT", 0)).isEqualTo("passive_followup_text");
    }

    @Test
    public void returnsPromoterQuestion_forCSATPromoterScore() throws Exception {
        assertThat(customMessage.getFollowupQuestionForScore(4, "CSAT", 0)).isEqualTo("promoter_followup_text");
        assertThat(customMessage.getFollowupQuestionForScore(5, "CSAT", 0)).isEqualTo("promoter_followup_text");
    }

    @Test
    public void returnsDefaultQuestion_forCSAT() throws Exception {
        customMessage.setFollowupQuestionsList(new HashMap<String, String>());
        assertThat(customMessage.getFollowupQuestionForScore(1, "CSAT", 0)).isEqualTo("followup_text");
        assertThat(customMessage.getFollowupQuestionForScore(5, "CSAT", 0)).isEqualTo("followup_text");
    }

    @Test
    public void returnsDetractorPlaceholder_forCSATDetractorScore() throws Exception {
        assertThat(customMessage.getPlaceholderForScore(1, "CSAT", 0)).isEqualTo("detractor_prompt_text");
        assertThat(customMessage.getPlaceholderForScore(2, "CSAT", 0)).isEqualTo("detractor_prompt_text");
    }

    @Test
    public void returnsPassivePlaceholder_forCSATPassiveScore() throws Exception {
        assertThat(customMessage.getPlaceholderForScore(3, "CSAT", 0)).isEqualTo("passive_prompt_text");
    }

    @Test
    public void returnsPromoterPlaceholder_forCSATPromoterScore() throws Exception {
        assertThat(customMessage.getPlaceholderForScore(5, "CSAT", 0)).isEqualTo("promoter_prompt_text");
    }

    @Test
    public void returnsDefaultPlaceholder_forCSAT() throws Exception {
        customMessage.setPlaceholderTextsList(new HashMap<String, String>());
        assertThat(customMessage.getPlaceholderForScore(1, "CSAT", 0)).isEqualTo("prompt_text");
        assertThat(customMessage.getPlaceholderForScore(5, "CSAT", 0)).isEqualTo("prompt_text");
    }
}
