package com.wootric.androidsdk.objects;

import android.os.Parcel;

import com.wootric.androidsdk.TestHelper;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Created by maciejwitowski on 9/23/15.
 */
public class WootricCustomThankYouTest {

    private Parcel mParcel = mock(Parcel.class);

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

    @Test
    public void testWootricCustomThankYouParcelable(){
        doReturn("testing parcel").when(mParcel).readString();
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setText("testing parcel");

        Parcel parcel = mParcel;
        customThankYou.writeToParcel(parcel, 0);

        parcel.setDataPosition(0);

        WootricCustomThankYou createdFromParcel = WootricCustomThankYou.CREATOR.createFromParcel(parcel);
        assertThat(createdFromParcel.getTextForScore(10, "NPS", 0)).isEqualTo("testing parcel");
    }

    @Test
    public void testWootricCustomThankYouFromJson() throws IOException, JSONException {
        TestHelper helper = new TestHelper();
        JSONObject customThankYouJson = new JSONObject(helper.loadJSONFromAsset("custom_thank_you.json"));

        WootricCustomThankYou customThankYou = WootricCustomThankYou.fromJson(customThankYouJson);

        assertThat(customThankYou).isNotNull();
        assertThat(customThankYou.getEmailInUri(0, "NPS", 0)).isTrue();
        assertThat(customThankYou.getScoreInUri(0, "NPS", 0)).isFalse();
    }

    @Test
    public void testWootricCustomThankYouFromJsonWhenThankYouLinkUrlSettingsIsNull() throws IOException, JSONException {
        TestHelper helper = new TestHelper();
        JSONObject customThankYouJson = new JSONObject(helper.loadJSONFromAsset("custom_thank_you.json"));

        customThankYouJson.getJSONObject("thank_you_links").remove("thank_you_link_url_settings");

        WootricCustomThankYou customThankYou = WootricCustomThankYou.fromJson(customThankYouJson);

        assertThat(customThankYou).isNotNull();
        assertThat(customThankYouJson.optJSONObject("thank_you_links").optString("thank_you_link_url_settings")).isNullOrEmpty();
    }
}
