package com.wootric.androidsdk.objects;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.wootric.androidsdk.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class WootricDriverPicklist implements Parcelable {
    private HashMap<String, String> driverPicklist;
    private HashMap<String, String> detractorDriverPicklist;
    private HashMap<String, String> passiveDriverPicklist;
    private HashMap<String, String> promoterDriverPicklist;

    private String text;
    private String detractorText;
    private String passiveText;
    private String promoterText;

    private String linkText;
    private String detractorLinkText;
    private String passiveLinkText;
    private String promoterLinkText;

    private Uri linkUri;
    private Uri detractorLinkUri;
    private Uri passiveLinkUri;
    private Uri promoterLinkUri;

    private Boolean emailInUrl;
    private Boolean detractorEmailInUrl;
    private Boolean passiveEmailInUrl;
    private Boolean promoterEmailInUrl;
    private Boolean scoreInUrl;
    private Boolean detractorScoreInUrl;
    private Boolean passiveScoreInUrl;
    private Boolean promoterScoreInUrl;
    private Boolean commentInUrl;
    private Boolean detractorCommentInUrl;
    private Boolean passiveCommentInUrl;
    private Boolean promoterCommentInUrl;

    private boolean uniqueByScore = false;

    public WootricDriverPicklist() {}

    public WootricDriverPicklist(WootricDriverPicklist wootricDriverPicklist) {
        if (wootricDriverPicklist == null) return;
//        this.finalThankYou = wootricCustomThankYou.finalThankYou;
//        this.detractorFinalThankYou = wootricCustomThankYou.detractorFinalThankYou;
//        this.passiveFinalThankYou = wootricCustomThankYou.passiveFinalThankYou;
//        this.promoterFinalThankYou = wootricCustomThankYou.promoterFinalThankYou;
//        this.text = wootricCustomThankYou.text;
//        this.detractorText = wootricCustomThankYou.detractorText;
//        this.passiveText = wootricCustomThankYou.passiveText;
//        this.promoterText = wootricCustomThankYou.promoterText;
//        this.linkText = wootricCustomThankYou.linkText;
//        this.detractorLinkText = wootricCustomThankYou.detractorLinkText;
//        this.passiveLinkText = wootricCustomThankYou.passiveLinkText;
//        this.promoterLinkText = wootricCustomThankYou.promoterLinkText;
//        this.linkUri = wootricCustomThankYou.linkUri;
//        this.detractorLinkUri = wootricCustomThankYou.detractorLinkUri;
//        this.passiveLinkUri = wootricCustomThankYou.passiveLinkUri;
//        this.promoterLinkUri = wootricCustomThankYou.promoterLinkUri;
//        this.emailInUrl = wootricCustomThankYou.emailInUrl;
//        this.detractorEmailInUrl = wootricCustomThankYou.detractorEmailInUrl;
//        this.passiveEmailInUrl = wootricCustomThankYou.passiveEmailInUrl;
//        this.promoterEmailInUrl = wootricCustomThankYou.promoterEmailInUrl;
//        this.scoreInUrl = wootricCustomThankYou.scoreInUrl;
//        this.detractorScoreInUrl = wootricCustomThankYou.detractorScoreInUrl;
//        this.passiveScoreInUrl = wootricCustomThankYou.passiveScoreInUrl;
//        this.promoterScoreInUrl = wootricCustomThankYou.promoterScoreInUrl;
//        this.commentInUrl = wootricCustomThankYou.commentInUrl;
//        this.detractorCommentInUrl = wootricCustomThankYou.detractorCommentInUrl;
//        this.passiveCommentInUrl = wootricCustomThankYou.passiveCommentInUrl;
//        this.promoterCommentInUrl = wootricCustomThankYou.promoterCommentInUrl;
//        this.uniqueByScore = wootricCustomThankYou.uniqueByScore;
    }

    public HashMap<String, String> getDriverPicklist(int scoreValue, String surveyType, int surveyTypeScale) {
        Score score = new Score(scoreValue, surveyType, surveyTypeScale);

        if(score.isDetractor() && detractorDriverPicklist != null) {
            return detractorDriverPicklist;
        } else if(score.isPassive() && passiveDriverPicklist != null) {
            return passiveDriverPicklist;
        } else if(score.isPromoter() && promoterDriverPicklist != null) {
            return promoterDriverPicklist;
        } else {
            return driverPicklist;
        }
    }
//
//    public String getTextForScore(int scoreValue, String surveyType, int surveyTypeScale) {
//        Score score = new Score(scoreValue, surveyType, surveyTypeScale);
//
//        if(score.isDetractor() && detractorText != null) {
//            return detractorText;
//        } else if(score.isPassive() && passiveText != null) {
//            return passiveText;
//        } else if(score.isPromoter() && promoterText != null) {
//            return promoterText;
//        } else {
//            return text;
//        }
//    }
//
//    public String getLinkTextForScore(int scoreValue, String surveyType, int surveyTypeScale) {
//        Score score = new Score(scoreValue, surveyType, surveyTypeScale);
//
//        if(score.isDetractor() && detractorLinkText != null) {
//            return detractorLinkText;
//        } else if(score.isPassive() && passiveLinkText != null) {
//            return passiveLinkText;
//        } else if(score.isPromoter() && promoterLinkText != null) {
//            return promoterLinkText;
//        } else {
//            return linkText;
//        }
//    }
//
//    public Uri getLinkUri(int score, String surveyType, int surveyTypeScale) {
//        return getLinkUriForScore(score, surveyType, surveyTypeScale);
//    }
//
//    private Uri getLinkUriForScore(int scoreValue, String surveyType, int surveyTypeScale) {
//        Score score = new Score(scoreValue, surveyType, surveyTypeScale);
//
//        if(score.isDetractor() && detractorLinkUri != null) {
//            return detractorLinkUri;
//        } else if(score.isPassive() && passiveLinkUri != null) {
//            return passiveLinkUri;
//        } else if(score.isPromoter() && promoterLinkUri != null) {
//            return promoterLinkUri;
//        } else {
//            return linkUri;
//        }
//    }
//
//    public Boolean getEmailInUri(int score, String surveyType, int surveyTypeScale) {
//        return getEmailInUriForScore(score, surveyType, surveyTypeScale);
//    }
//
//    private Boolean getEmailInUriForScore(int scoreValue, String surveyType, int surveyTypeScale) {
//        Score score = new Score(scoreValue, surveyType, surveyTypeScale);
//
//        if (this.uniqueByScore) {
//            if(score.isDetractor()) {
//                return detractorEmailInUrl;
//            } else if(score.isPassive()) {
//                return passiveEmailInUrl;
//            } else if(score.isPromoter()) {
//                return promoterEmailInUrl;
//            }
//        }
//
//        return emailInUrl;
//    }
//
//    public Boolean getScoreInUri(int score, String surveyType, int surveyTypeScale) {
//        return getScoreInUriForScore(score, surveyType, surveyTypeScale);
//    }
//
//    private Boolean getScoreInUriForScore(int scoreValue, String surveyType, int surveyTypeScale) {
//        Score score = new Score(scoreValue, surveyType, surveyTypeScale);
//
//        if (this.uniqueByScore) {
//            if(score.isDetractor()) {
//                return detractorScoreInUrl;
//            } else if(score.isPassive()) {
//                return passiveScoreInUrl;
//            } else if(score.isPromoter()) {
//                return promoterScoreInUrl;
//            }
//        }
//
//        return scoreInUrl;
//    }
//
//    public Boolean getCommentInUri(int score, String surveyType, int surveyTypeScale) {
//        return getCommentInUriForScore(score, surveyType, surveyTypeScale);
//    }
//
//    private Boolean getCommentInUriForScore(int scoreValue, String surveyType, int surveyTypeScale) {
//        Score score = new Score(scoreValue, surveyType, surveyTypeScale);
//
//        if (this.uniqueByScore) {
//            if(score.isDetractor()) {
//                return detractorCommentInUrl;
//            } else if(score.isPassive()) {
//                return passiveCommentInUrl;
//            } else if(score.isPromoter()) {
//                return promoterCommentInUrl;
//            }
//        }
//
//        return commentInUrl;
//    }
//
//    public boolean getUniqueByScore() {
//        return this.uniqueByScore;
//    }
//
//    public void setFinalThankYou(String finalThankYou) {
//        this.finalThankYou = finalThankYou;
//    }
//
//    public void setDetractorFinalThankYou(String detractorFinalThankYou) {
//        this.detractorFinalThankYou = detractorFinalThankYou;
//    }
//
//    public void setPassiveFinalThankYou(String passiveFinalThankYou) {
//        this.passiveFinalThankYou = passiveFinalThankYou;
//    }
//
//    public void setPromoterFinalThankYou(String promoterFinalThankYou) {
//        this.promoterFinalThankYou = promoterFinalThankYou;
//    }
//
//    public void setText(String text) {
//        this.text = text;
//    }
//
//    public void setDetractorText(String detractorText) {
//        this.detractorText = detractorText;
//    }
//
//    public void setPassiveText(String passiveText) {
//        this.passiveText = passiveText;
//    }
//
//    public void setPromoterText(String promoterText) {
//        this.promoterText = promoterText;
//    }
//
//    public void setLinkText(String linkText) {
//        this.linkText = linkText;
//    }
//
//    public void setLinkUri(Uri linkUri) {
//        this.linkUri = linkUri;
//    }
//
//    public void setDetractorLinkText(String detractorLinkText) {
//        this.detractorLinkText = detractorLinkText;
//    }
//
//    public void setDetractorLinkUri(Uri detractorLinkUri) {
//        this.detractorLinkUri = detractorLinkUri;
//    }
//
//    public void setPassiveLinkText(String passiveLinkText) {
//        this.passiveLinkText = passiveLinkText;
//    }
//
//    public void setPassiveLinkUri(Uri passiveLinkUrl) {
//        this.passiveLinkUri = passiveLinkUrl;
//    }
//
//    public void setPromoterLinkText(String promoterLinkText) {
//        this.promoterLinkText = promoterLinkText;
//    }
//
//    public void setPromoterLinkUri(Uri promoterLinkUrl) {
//        this.promoterLinkUri = promoterLinkUrl;
//    }
//
//    public void setEmailInUrl(boolean emailInUrl) {
//        this.emailInUrl = emailInUrl;
//    }
//
//    public void setDetractorEmailInUrl(boolean detractorEmailInUrl) {
//        this.detractorEmailInUrl = detractorEmailInUrl;
//    }
//
//    public void setPassiveEmailInUrl(boolean passiveEmailInUrl) {
//        this.passiveEmailInUrl = passiveEmailInUrl;
//    }
//
//    public void setPromoterEmailInUrl(boolean promoterEmailInUrl) {
//        this.promoterEmailInUrl = promoterEmailInUrl;
//    }
//
//    public void setScoreInUrl(boolean scoreInUrl) {
//        this.scoreInUrl = scoreInUrl;
//    }
//
//    public void setDetractorScoreInUrl(boolean detractorScoreInUrl) {
//        this.detractorScoreInUrl = detractorScoreInUrl;
//    }
//
//    public void setPassiveScoreInUrl(boolean passiveScoreInUrl) {
//        this.passiveScoreInUrl = passiveScoreInUrl;
//    }
//
//    public void setPromoterScoreInUrl(boolean promoterScoreInUrl) {
//        this.promoterScoreInUrl = promoterScoreInUrl;
//    }
//
//    public void setCommentInUrl(boolean commentInUrl) {
//        this.commentInUrl = commentInUrl;
//    }
//
//    public void setDetractorCommentInUrl(boolean detractorCommentInUrl) {
//        this.detractorCommentInUrl = detractorCommentInUrl;
//    }
//
//    public void setPassiveCommentInUrl(boolean passiveCommentInUrl) {
//        this.passiveCommentInUrl = passiveCommentInUrl;
//    }
//
//    public void setPromoterCommentInUrl(boolean promoterCommentInUrl) {
//        this.promoterCommentInUrl = promoterCommentInUrl;
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(this.text);
//        dest.writeParcelable(this.linkUri, 0);
//        dest.writeByte(Utils.getByteValue(this.emailInUrl));
    }

    private WootricDriverPicklist(Parcel in) {
//        this.text = in.readString();
//        this.linkUri = in.readParcelable(Uri.class.getClassLoader());
//        this.emailInUrl = in.readByte() != 0;
    }

    public static final Creator<WootricDriverPicklist> CREATOR = new Creator<WootricDriverPicklist>() {
        public WootricDriverPicklist createFromParcel(Parcel source) {
            return new WootricDriverPicklist(source);
        }

        public WootricDriverPicklist[] newArray(int size) {
            return new WootricDriverPicklist[size];
        }
    };

    public static WootricDriverPicklist fromJson(JSONObject driverPicklistJson) throws JSONException {
        if(driverPicklistJson == null) return null;

        WootricDriverPicklist wootricDriverPicklist = new WootricDriverPicklist();

        JSONObject driverPicklistAnswersJson = driverPicklistJson.optJSONObject("driver_picklist");

        if (driverPicklistAnswersJson != null) {
//            JSONObject thankYouMainList = thankYouMessagesJson.optJSONObject("thank_you_main_list");
//            JSONObject thankYouSetupList = thankYouMessagesJson.optJSONObject("thank_you_setup_list");
//
//            wootricCustomThankYou.finalThankYou = thankYouMessagesJson.optString("thank_you_main");
//            wootricCustomThankYou.setText(thankYouMessagesJson.optString("thank_you_setup"));
//
//            if (thankYouMainList != null) {
//                wootricCustomThankYou.uniqueByScore = true;
//                wootricCustomThankYou.detractorFinalThankYou = thankYouMainList.optString("detractor_thank_you_main");
//                wootricCustomThankYou.passiveFinalThankYou = thankYouMainList.optString("passive_thank_you_main");
//                wootricCustomThankYou.promoterFinalThankYou = thankYouMainList.optString("promoter_thank_you_main");
//            }
//
//            if (thankYouSetupList != null) {
//                wootricCustomThankYou.uniqueByScore = true;
//                wootricCustomThankYou.setDetractorText(thankYouSetupList.optString("detractor_thank_you_setup"));
//                wootricCustomThankYou.setPassiveText(thankYouSetupList.optString("passive_thank_you_setup"));
//                wootricCustomThankYou.setPromoterText(thankYouSetupList.optString("promoter_thank_you_setup"));
//            }
        }

//        if (thankYouLinksJson != null) {
//            JSONObject thankYouLinkTextList = thankYouLinksJson.optJSONObject("thank_you_link_text_list");
//            JSONObject thankYouLinkUrlList = thankYouLinksJson.optJSONObject("thank_you_link_url_list");
//            JSONObject thankYouLinkUrlSettingsList =  thankYouLinksJson.optJSONObject("thank_you_link_url_settings_list");
//            JSONObject thankYouLinkUrlSettings = thankYouLinksJson.optJSONObject("thank_you_link_url_settings");
//
//            wootricCustomThankYou.setLinkText(thankYouLinksJson.optString("thank_you_link_text", null));
//            wootricCustomThankYou.setLinkUri(Uri.parse(thankYouLinksJson.optString("thank_you_link_url")));
//
//            if (thankYouLinkUrlSettings != null) {
//                wootricCustomThankYou.setEmailInUrl(thankYouLinkUrlSettings.getBoolean("add_email_param_to_url"));
//                wootricCustomThankYou.setScoreInUrl(thankYouLinkUrlSettings.getBoolean("add_score_param_to_url"));
//                wootricCustomThankYou.setCommentInUrl(thankYouLinkUrlSettings.getBoolean("add_comment_param_to_url"));
//            }
//
//            if (thankYouLinkTextList != null) {
//                wootricCustomThankYou.uniqueByScore = true;
//                wootricCustomThankYou.setDetractorLinkText(thankYouLinkTextList.optString("detractor_thank_you_link_text", null));
//                wootricCustomThankYou.setPassiveLinkText(thankYouLinkTextList.optString("passive_thank_you_link_text", null));
//                wootricCustomThankYou.setPromoterLinkText(thankYouLinkTextList.optString("promoter_thank_you_link_text", null));
//            }
//
//            if (thankYouLinkUrlList != null) {
//                wootricCustomThankYou.uniqueByScore = true;
//                wootricCustomThankYou.setDetractorLinkUri(Uri.parse(thankYouLinkUrlList.optString("detractor_thank_you_link_url")));
//                wootricCustomThankYou.setPassiveLinkUri(Uri.parse(thankYouLinkUrlList.optString("passive_thank_you_link_url")));
//                wootricCustomThankYou.setPromoterLinkUri(Uri.parse(thankYouLinkUrlList.optString("promoter_thank_you_link_url")));
//            }
//
//            if (thankYouLinkUrlSettingsList != null) {
//                JSONObject detractorThankYouLinkUrlSettingsList =  thankYouLinkUrlSettingsList.optJSONObject("detractor_thank_you_link_url_settings");
//                JSONObject passiveThankYouLinkUrlSettingsList =  thankYouLinkUrlSettingsList.optJSONObject("passive_thank_you_link_url_settings");
//                JSONObject promoterThankYouLinkUrlSettingsList =  thankYouLinkUrlSettingsList.optJSONObject("promoter_thank_you_link_url_settings");
//
//                if (detractorThankYouLinkUrlSettingsList != null) {
//                    wootricCustomThankYou.setDetractorEmailInUrl(detractorThankYouLinkUrlSettingsList.getBoolean("add_email_param_to_url"));
//                    wootricCustomThankYou.setDetractorScoreInUrl(detractorThankYouLinkUrlSettingsList.getBoolean("add_score_param_to_url"));
//                    wootricCustomThankYou.setDetractorCommentInUrl(detractorThankYouLinkUrlSettingsList.getBoolean("add_comment_param_to_url"));
//                }
//
//                if (passiveThankYouLinkUrlSettingsList != null) {
//                    wootricCustomThankYou.setPassiveEmailInUrl(passiveThankYouLinkUrlSettingsList.getBoolean("add_email_param_to_url"));
//                    wootricCustomThankYou.setPassiveScoreInUrl(passiveThankYouLinkUrlSettingsList.getBoolean("add_score_param_to_url"));
//                    wootricCustomThankYou.setPassiveCommentInUrl(passiveThankYouLinkUrlSettingsList.getBoolean("add_comment_param_to_url"));
//                }
//
//                if (promoterThankYouLinkUrlSettingsList != null) {
//                    wootricCustomThankYou.setPromoterEmailInUrl(promoterThankYouLinkUrlSettingsList.getBoolean("add_email_param_to_url"));
//                    wootricCustomThankYou.setPromoterScoreInUrl(promoterThankYouLinkUrlSettingsList.getBoolean("add_score_param_to_url"));
//                    wootricCustomThankYou.setPromoterCommentInUrl(promoterThankYouLinkUrlSettingsList.getBoolean("add_comment_param_to_url"));
//                }
//            }
//        }

        return wootricDriverPicklist;
    }
}
