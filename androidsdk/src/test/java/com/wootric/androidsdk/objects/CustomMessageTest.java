package com.wootric.androidsdk.objects;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by maciejwitowski on 9/17/15.
 */
public class CustomMessageTest {

    CustomMessage customMessage;
    @Before
    public void setUp() {
        customMessage = new Gson().fromJson(CUSTOM_MESSAGE_JSON, CustomMessage.class);
    }

    @Test
    public void customMessageIsCorrectlyBuild() throws Exception {
        CustomMessage customMessage = new CustomMessage();
        customMessage.setFollowupQuestion("followup_text");
        customMessage.setDetractorFollowupQuestion("detractor_followup_text");
        customMessage.setPassiveFollowupQuestion("passive_followup_text");
        customMessage.setPromoterFollowupQuestion("promoter_followup_text");
        customMessage.setPlaceholderText("prompt_text");
        customMessage.setDetractorPlaceholderText("detractor_prompt_text");
        customMessage.setPassivePlaceholderText("passive_prompt_text");
        customMessage.setPromoterPlaceholderText("promoter_prompt_text");

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
    public void customMessageIsCorrectlyParsedFromJson() throws Exception {
        assertThat(customMessage.getFollowupQuestion()).isEqualTo("followup_text");
        assertThat(customMessage.getDetractorQuestion()).isEqualTo("detractor_followup_text");
        assertThat(customMessage.getPassiveQuestion()).isEqualTo("passive_followup_text");
        assertThat(customMessage.getPromoterQuestion()).isEqualTo("promoter_followup_text");
        assertThat(customMessage.getPlaceholderText()).isEqualTo("prompt_text");
        assertThat(customMessage.getDetractorPlaceholder()).isEqualTo("detractor_prompt_text");
        assertThat(customMessage.getPassivePlaceholder()).isEqualTo("passive_prompt_text");
        assertThat(customMessage.getPromoterPlaceholder()).isEqualTo("promoter_prompt_text");
    }

    /**
     * getFollowupQuestionForScore(int score)
     */
    @Test
    public void returnsDetractorQuestion_forDetractorScore() throws Exception {
        assertThat(customMessage.getFollowupQuestionForScore(0)).isEqualTo("detractor_followup_text");
        assertThat(customMessage.getFollowupQuestionForScore(6)).isEqualTo("detractor_followup_text");
    }

    @Test
    public void returnsPassiveQuestion_forPassiveScore() throws Exception {
        assertThat(customMessage.getFollowupQuestionForScore(7)).isEqualTo("passive_followup_text");
        assertThat(customMessage.getFollowupQuestionForScore(8)).isEqualTo("passive_followup_text");
    }

    @Test
    public void returnsPromoterQuestion_forPromoterScore() throws Exception {
        assertThat(customMessage.getFollowupQuestionForScore(9)).isEqualTo("promoter_followup_text");
        assertThat(customMessage.getFollowupQuestionForScore(10)).isEqualTo("promoter_followup_text");
    }

    @Test
    public void returnsDefaultQuestion() throws Exception {
        customMessage.setFollowupQuestionsList(new HashMap<String, String>());
        assertThat(customMessage.getFollowupQuestionForScore(9)).isEqualTo("followup_text");
    }

    /**
     * getPlaceholderForScore(int score)
     */
    @Test
    public void returnsDetractorPlaceholder_forDetractorScore() throws Exception {
        assertThat(customMessage.getPlaceholderForScore(0)).isEqualTo("detractor_prompt_text");
        assertThat(customMessage.getPlaceholderForScore(6)).isEqualTo("detractor_prompt_text");
    }

    @Test
    public void returnsPassivePlaceholder_forPassiveScore() throws Exception {
        assertThat(customMessage.getPlaceholderForScore(7)).isEqualTo("passive_prompt_text");
        assertThat(customMessage.getPlaceholderForScore(8)).isEqualTo("passive_prompt_text");
    }

    @Test
    public void returnsPromoterPlaceholder_forPromoterScore() throws Exception {
        assertThat(customMessage.getPlaceholderForScore(9)).isEqualTo("promoter_prompt_text");
        assertThat(customMessage.getPlaceholderForScore(10)).isEqualTo("promoter_prompt_text");
    }

    @Test
    public void returnsDefaultPlaceholder() throws Exception {
        customMessage.setPlaceholderTextsList(new HashMap<String, String>());
        assertThat(customMessage.getPlaceholderForScore(9)).isEqualTo("prompt_text");
    }

    private static final String CUSTOM_MESSAGE_JSON = "{" +
            "\"followup_question\":\"followup_text\"," +
            "\"followup_questions_list\":{" +
                "\"detractor_question\":\"detractor_followup_text\"," +
                "\"passive_question\":\"passive_followup_text\"," +
                "\"promoter_question\":\"promoter_followup_text\"" +
            "}," +
            "\"placeholder_text\":\"prompt_text\"," +
            "\"placeholder_texts_list\":{" +
                "\"detractor_text\":\"detractor_prompt_text\"," +
                "\"passive_text\":\"passive_prompt_text\"," +
                "\"promoter_text\":\"promoter_prompt_text\"}" +
            "}";
}
