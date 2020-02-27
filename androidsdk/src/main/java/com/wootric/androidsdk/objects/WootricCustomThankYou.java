/*
 * Copyright (c) 2016 Wootric (https://wootric.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.wootric.androidsdk.objects;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.wootric.androidsdk.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by maciejwitowski on 9/23/15.
 */
public class WootricCustomThankYou implements Parcelable {

    private String finalThankYou;
    private String detractorFinalThankYou;
    private String passiveFinalThankYou;
    private String promoterFinalThankYou;

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

    public String getFinalThankYouForScore(int scoreValue, String surveyType, int surveyTypeScale) {
        Score score = new Score(scoreValue, surveyType, surveyTypeScale);

        if(score.isDetractor() && detractorFinalThankYou != null) {
            return detractorFinalThankYou;
        } else if(score.isPassive() && passiveFinalThankYou != null) {
            return passiveFinalThankYou;
        } else if(score.isPromoter() && promoterFinalThankYou != null) {
            return promoterFinalThankYou;
        } else {
            return finalThankYou;
        }
    }

    public String getTextForScore(int scoreValue, String surveyType, int surveyTypeScale) {
        Score score = new Score(scoreValue, surveyType, surveyTypeScale);

        if(score.isDetractor() && detractorText != null) {
            return detractorText;
        } else if(score.isPassive() && passiveText != null) {
            return passiveText;
        } else if(score.isPromoter() && promoterText != null) {
            return promoterText;
        } else {
            return text;
        }
    }

    public String getLinkTextForScore(int scoreValue, String surveyType, int surveyTypeScale) {
        Score score = new Score(scoreValue, surveyType, surveyTypeScale);

        if(score.isDetractor() && detractorLinkText != null) {
            return detractorLinkText;
        } else if(score.isPassive() && passiveLinkText != null) {
            return passiveLinkText;
        } else if(score.isPromoter() && promoterLinkText != null) {
            return promoterLinkText;
        } else {
            return linkText;
        }
    }

    public Uri getLinkUri(int score, String surveyType, int surveyTypeScale) {
        return getLinkUriForScore(score, surveyType, surveyTypeScale);
    }

    private Uri getLinkUriForScore(int scoreValue, String surveyType, int surveyTypeScale) {
        Score score = new Score(scoreValue, surveyType, surveyTypeScale);

        if(score.isDetractor() && detractorLinkUri != null) {
            return detractorLinkUri;
        } else if(score.isPassive() && passiveLinkUri != null) {
            return passiveLinkUri;
        } else if(score.isPromoter() && promoterLinkUri != null) {
            return promoterLinkUri;
        } else {
            return linkUri;
        }
    }

    public Boolean getEmailInUri(int score, String surveyType, int surveyTypeScale) {
        return getEmailInUriForScore(score, surveyType, surveyTypeScale);
    }

    private Boolean getEmailInUriForScore(int scoreValue, String surveyType, int surveyTypeScale) {
        Score score = new Score(scoreValue, surveyType, surveyTypeScale);

        if (this.uniqueByScore) {
            if(score.isDetractor()) {
                return detractorEmailInUrl;
            } else if(score.isPassive()) {
                return passiveEmailInUrl;
            } else if(score.isPromoter()) {
                return promoterEmailInUrl;
            }
        }

        return emailInUrl;
    }

    public Boolean getScoreInUri(int score, String surveyType, int surveyTypeScale) {
        return getScoreInUriForScore(score, surveyType, surveyTypeScale);
    }

    private Boolean getScoreInUriForScore(int scoreValue, String surveyType, int surveyTypeScale) {
        Score score = new Score(scoreValue, surveyType, surveyTypeScale);

        if (this.uniqueByScore) {
            if(score.isDetractor()) {
                return detractorScoreInUrl;
            } else if(score.isPassive()) {
                return passiveScoreInUrl;
            } else if(score.isPromoter()) {
                return promoterScoreInUrl;
            }
        }

        return scoreInUrl;
    }

    public Boolean getCommentInUri(int score, String surveyType, int surveyTypeScale) {
        return getCommentInUriForScore(score, surveyType, surveyTypeScale);
    }

    private Boolean getCommentInUriForScore(int scoreValue, String surveyType, int surveyTypeScale) {
        Score score = new Score(scoreValue, surveyType, surveyTypeScale);

        if (this.uniqueByScore) {
            if(score.isDetractor()) {
                return detractorCommentInUrl;
            } else if(score.isPassive()) {
                return passiveCommentInUrl;
            } else if(score.isPromoter()) {
                return promoterCommentInUrl;
            }
        }

        return commentInUrl;
    }

    public boolean getUniqueByScore() {
        return this.uniqueByScore;
    }

    public void setFinalThankYou(String finalThankYou) {
        this.finalThankYou = finalThankYou;
    }

    public void setDetractorFinalThankYou(String detractorFinalThankYou) {
        this.detractorFinalThankYou = detractorFinalThankYou;
    }

    public void setPassiveFinalThankYou(String passiveFinalThankYou) {
        this.passiveFinalThankYou = passiveFinalThankYou;
    }

    public void setPromoterFinalThankYou(String promoterFinalThankYou) {
        this.promoterFinalThankYou = promoterFinalThankYou;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDetractorText(String detractorText) {
        this.detractorText = detractorText;
    }

    public void setPassiveText(String passiveText) {
        this.passiveText = passiveText;
    }

    public void setPromoterText(String promoterText) {
        this.promoterText = promoterText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public void setLinkUri(Uri linkUri) {
        this.linkUri = linkUri;
    }

    public void setDetractorLinkText(String detractorLinkText) {
        this.detractorLinkText = detractorLinkText;
    }

    public void setDetractorLinkUri(Uri detractorLinkUri) {
        this.detractorLinkUri = detractorLinkUri;
    }

    public void setPassiveLinkText(String passiveLinkText) {
        this.passiveLinkText = passiveLinkText;
    }

    public void setPassiveLinkUri(Uri passiveLinkUrl) {
        this.passiveLinkUri = passiveLinkUrl;
    }

    public void setPromoterLinkText(String promoterLinkText) {
        this.promoterLinkText = promoterLinkText;
    }

    public void setPromoterLinkUri(Uri promoterLinkUrl) {
        this.promoterLinkUri = promoterLinkUrl;
    }

    public void setEmailInUrl(boolean emailInUrl) {
        this.emailInUrl = emailInUrl;
    }

    public void setDetractorEmailInUrl(boolean detractorEmailInUrl) {
        this.detractorEmailInUrl = detractorEmailInUrl;
    }

    public void setPassiveEmailInUrl(boolean passiveEmailInUrl) {
        this.passiveEmailInUrl = passiveEmailInUrl;
    }

    public void setPromoterEmailInUrl(boolean promoterEmailInUrl) {
        this.promoterEmailInUrl = promoterEmailInUrl;
    }

    public void setScoreInUrl(boolean scoreInUrl) {
        this.scoreInUrl = scoreInUrl;
    }

    public void setDetractorScoreInUrl(boolean detractorScoreInUrl) {
        this.detractorScoreInUrl = detractorScoreInUrl;
    }

    public void setPassiveScoreInUrl(boolean passiveScoreInUrl) {
        this.passiveScoreInUrl = passiveScoreInUrl;
    }

    public void setPromoterScoreInUrl(boolean promoterScoreInUrl) {
        this.promoterScoreInUrl = promoterScoreInUrl;
    }

    public void setCommentInUrl(boolean commentInUrl) {
        this.commentInUrl = commentInUrl;
    }

    public void setDetractorCommentInUrl(boolean detractorCommentInUrl) {
        this.detractorCommentInUrl = detractorCommentInUrl;
    }

    public void setPassiveCommentInUrl(boolean passiveCommentInUrl) {
        this.passiveCommentInUrl = passiveCommentInUrl;
    }

    public void setPromoterCommentInUrl(boolean promoterCommentInUrl) {
        this.promoterCommentInUrl = promoterCommentInUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeString(this.detractorText);
        dest.writeString(this.passiveText);
        dest.writeString(this.promoterText);
        dest.writeString(this.linkText);
        dest.writeString(this.detractorLinkText);
        dest.writeString(this.passiveLinkText);
        dest.writeString(this.promoterLinkText);
        dest.writeParcelable(this.linkUri, 0);
        dest.writeParcelable(this.detractorLinkUri, 0);
        dest.writeParcelable(this.passiveLinkUri, 0);
        dest.writeParcelable(this.promoterLinkUri, 0);
        dest.writeByte(Utils.getByteValue(this.emailInUrl));
        dest.writeByte(Utils.getByteValue(this.detractorEmailInUrl));
        dest.writeByte(Utils.getByteValue(this.passiveEmailInUrl));
        dest.writeByte(Utils.getByteValue(this.promoterEmailInUrl));
        dest.writeByte(Utils.getByteValue(this.scoreInUrl));
        dest.writeByte(Utils.getByteValue(this.detractorScoreInUrl));
        dest.writeByte(Utils.getByteValue(this.passiveScoreInUrl));
        dest.writeByte(Utils.getByteValue(this.promoterScoreInUrl));
        dest.writeByte(Utils.getByteValue(this.commentInUrl));
        dest.writeByte(Utils.getByteValue(this.detractorCommentInUrl));
        dest.writeByte(Utils.getByteValue(this.passiveCommentInUrl));
        dest.writeByte(Utils.getByteValue(this.promoterCommentInUrl));
    }

    public WootricCustomThankYou() {
    }

    private WootricCustomThankYou(Parcel in) {
        this.text = in.readString();
        this.detractorText = in.readString();
        this.passiveText = in.readString();
        this.promoterText = in.readString();
        this.linkText = in.readString();
        this.detractorLinkText = in.readString();
        this.passiveLinkText = in.readString();
        this.promoterLinkText = in.readString();
        this.linkUri = in.readParcelable(Uri.class.getClassLoader());
        this.detractorLinkUri = in.readParcelable(Uri.class.getClassLoader());
        this.passiveLinkUri = in.readParcelable(Uri.class.getClassLoader());
        this.promoterLinkUri = in.readParcelable(Uri.class.getClassLoader());
        this.emailInUrl = in.readByte() != 0;
        this.detractorEmailInUrl = in.readByte() != 0;
        this.passiveEmailInUrl = in.readByte() != 0;
        this.promoterEmailInUrl = in.readByte() != 0;
        this.scoreInUrl = in.readByte() != 0;
        this.detractorScoreInUrl = in.readByte() != 0;
        this.passiveScoreInUrl = in.readByte() != 0;
        this.promoterScoreInUrl = in.readByte() != 0;
        this.commentInUrl = in.readByte() != 0;
        this.detractorCommentInUrl = in.readByte() != 0;
        this.passiveCommentInUrl = in.readByte() != 0;
        this.promoterCommentInUrl = in.readByte() != 0;
    }

    public static final Creator<WootricCustomThankYou> CREATOR = new Creator<WootricCustomThankYou>() {
        public WootricCustomThankYou createFromParcel(Parcel source) {
            return new WootricCustomThankYou(source);
        }

        public WootricCustomThankYou[] newArray(int size) {
            return new WootricCustomThankYou[size];
        }
    };

    public static WootricCustomThankYou fromJson(JSONObject customThankYouJson) throws JSONException {
        if(customThankYouJson == null) return null;

        WootricCustomThankYou wootricCustomThankYou = new WootricCustomThankYou();

        JSONObject thankYouMessagesJson = customThankYouJson.optJSONObject("thank_you_messages");
        JSONObject thankYouLinksJson = customThankYouJson.optJSONObject("thank_you_links");

        if (thankYouMessagesJson != null) {
            JSONObject thankYouMainList = thankYouMessagesJson.optJSONObject("thank_you_main_list");
            JSONObject thankYouSetupList = thankYouMessagesJson.optJSONObject("thank_you_setup_list");

            wootricCustomThankYou.finalThankYou = thankYouMessagesJson.optString("thank_you_main");
            wootricCustomThankYou.setText(thankYouMessagesJson.optString("thank_you_setup"));

            if (thankYouMainList != null) {
                wootricCustomThankYou.uniqueByScore = true;
                wootricCustomThankYou.detractorFinalThankYou = thankYouMainList.optString("detractor_thank_you_main");
                wootricCustomThankYou.passiveFinalThankYou = thankYouMainList.optString("passive_thank_you_main");
                wootricCustomThankYou.promoterFinalThankYou = thankYouMainList.optString("promoter_thank_you_main");
            }

            if (thankYouSetupList != null) {
                wootricCustomThankYou.uniqueByScore = true;
                wootricCustomThankYou.setDetractorText(thankYouSetupList.optString("detractor_thank_you_setup"));
                wootricCustomThankYou.setPassiveText(thankYouSetupList.optString("passive_thank_you_setup"));
                wootricCustomThankYou.setPromoterText(thankYouSetupList.optString("promoter_thank_you_setup"));
            }
        }

        if (thankYouLinksJson != null) {
            JSONObject thankYouLinkTextList = thankYouLinksJson.optJSONObject("thank_you_link_text_list");
            JSONObject thankYouLinkUrlList = thankYouLinksJson.optJSONObject("thank_you_link_url_list");
            JSONObject thankYouLinkUrlSettingsList =  thankYouLinksJson.optJSONObject("thank_you_link_url_settings_list");
            JSONObject thankYouLinkUrlSettings = thankYouLinksJson.optJSONObject("thank_you_link_url_settings");

            wootricCustomThankYou.setLinkText(thankYouLinksJson.optString("thank_you_link_text", null));
            wootricCustomThankYou.setLinkUri(Uri.parse(thankYouLinksJson.optString("thank_you_link_url")));

            if (thankYouLinkUrlSettings != null) {
                wootricCustomThankYou.setEmailInUrl(thankYouLinkUrlSettings.getBoolean("add_email_param_to_url"));
                wootricCustomThankYou.setScoreInUrl(thankYouLinkUrlSettings.getBoolean("add_score_param_to_url"));
                wootricCustomThankYou.setCommentInUrl(thankYouLinkUrlSettings.getBoolean("add_comment_param_to_url"));
            }

            if (thankYouLinkTextList != null) {
                wootricCustomThankYou.uniqueByScore = true;
                wootricCustomThankYou.setDetractorLinkText(thankYouLinkTextList.optString("detractor_thank_you_link_text", null));
                wootricCustomThankYou.setPassiveLinkText(thankYouLinkTextList.optString("passive_thank_you_link_text", null));
                wootricCustomThankYou.setPromoterLinkText(thankYouLinkTextList.optString("promoter_thank_you_link_text", null));
            }

            if (thankYouLinkUrlList != null) {
                wootricCustomThankYou.uniqueByScore = true;
                wootricCustomThankYou.setDetractorLinkUri(Uri.parse(thankYouLinkUrlList.optString("detractor_thank_you_link_url")));
                wootricCustomThankYou.setPassiveLinkUri(Uri.parse(thankYouLinkUrlList.optString("passive_thank_you_link_url")));
                wootricCustomThankYou.setPromoterLinkUri(Uri.parse(thankYouLinkUrlList.optString("promoter_thank_you_link_url")));
            }

            if (thankYouLinkUrlSettingsList != null) {
                JSONObject detractorThankYouLinkUrlSettingsList =  thankYouLinkUrlSettingsList.optJSONObject("detractor_thank_you_link_url_settings");
                JSONObject passiveThankYouLinkUrlSettingsList =  thankYouLinkUrlSettingsList.optJSONObject("passive_thank_you_link_url_settings");
                JSONObject promoterThankYouLinkUrlSettingsList =  thankYouLinkUrlSettingsList.optJSONObject("promoter_thank_you_link_url_settings");

                if (detractorThankYouLinkUrlSettingsList != null) {
                    wootricCustomThankYou.setDetractorEmailInUrl(detractorThankYouLinkUrlSettingsList.getBoolean("add_email_param_to_url"));
                    wootricCustomThankYou.setDetractorScoreInUrl(detractorThankYouLinkUrlSettingsList.getBoolean("add_score_param_to_url"));
                    wootricCustomThankYou.setDetractorCommentInUrl(detractorThankYouLinkUrlSettingsList.getBoolean("add_comment_param_to_url"));
                }

                if (passiveThankYouLinkUrlSettingsList != null) {
                    wootricCustomThankYou.setPassiveEmailInUrl(passiveThankYouLinkUrlSettingsList.getBoolean("add_email_param_to_url"));
                    wootricCustomThankYou.setPassiveScoreInUrl(passiveThankYouLinkUrlSettingsList.getBoolean("add_score_param_to_url"));
                    wootricCustomThankYou.setPassiveCommentInUrl(passiveThankYouLinkUrlSettingsList.getBoolean("add_comment_param_to_url"));
                }

                if (promoterThankYouLinkUrlSettingsList != null) {
                    wootricCustomThankYou.setPromoterEmailInUrl(promoterThankYouLinkUrlSettingsList.getBoolean("add_email_param_to_url"));
                    wootricCustomThankYou.setPromoterScoreInUrl(promoterThankYouLinkUrlSettingsList.getBoolean("add_score_param_to_url"));
                    wootricCustomThankYou.setPromoterCommentInUrl(promoterThankYouLinkUrlSettingsList.getBoolean("add_comment_param_to_url"));
                }
            }
        }

        return wootricCustomThankYou;
    }
}
