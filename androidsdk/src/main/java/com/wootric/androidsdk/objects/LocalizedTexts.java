package com.wootric.androidsdk.objects;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by maciejwitowski on 9/3/15.
 */
public class LocalizedTexts {

    final String npsQuestion;
    final String anchorLikely;
    final String anchorNotLikely;
    final String followupQuestion;
    final String followupPlaceholder;
    final String finalThankYou;
    final String send;
    final String dismiss;
    final String socialShareQuestion;
    final String socialShareDecline;

    public LocalizedTexts(String npsQuestion, String anchorLikely, String anchorNotLikely,
                          String followupQuestion, String followupPlaceholder, String finalThankYou,
                          String send, String dismiss, String socialShareQuestion, String socialShareDecline) {
        this.npsQuestion = npsQuestion;
        this.anchorLikely = anchorLikely;
        this.anchorNotLikely = anchorNotLikely;
        this.followupQuestion = followupQuestion;
        this.followupPlaceholder = followupPlaceholder;
        this.finalThankYou = finalThankYou;
        this.send = send;
        this.dismiss = dismiss;
        this.socialShareQuestion = socialShareQuestion;
        this.socialShareDecline = socialShareDecline;
    }


    public static LocalizedTexts fromJson(JSONObject jsonObject) {
        if(jsonObject == null) return null;

        try {
            return new LocalizedTexts(
                    jsonObject.getString("nps_question"),
                    jsonObject.getJSONObject("anchors").getString("likely"),
                    jsonObject.getJSONObject("anchors").getString("not_likely"),
                    jsonObject.getString("followup_question"),
                    jsonObject.getString("followup_placeholder"),
                    jsonObject.getString("final_thank_you"),
                    jsonObject.getString("send"),
                    jsonObject.getString("dismiss"),
                    jsonObject.getJSONObject("social_share").getString("question"),
                    jsonObject.getJSONObject("social_share").getString("decline")
            );
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
