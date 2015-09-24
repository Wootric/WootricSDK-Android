package com.wootric.androidsdk.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by maciejwitowski on 9/23/15.
 */
public class CustomThankYou implements Parcelable {

    private String text;
    private String detractorText;
    private String passiveText;
    private String promoterText;

    private String linkText;
    private String linkUrl;
    private String detractorLinkText;
    private String detractorLinkUrl;
    private String passiveLinkText;
    private String passiveLinkUrl;
    private String promoterLinkText;
    private String promoterLinkUrl;

    private boolean scoreInUrl;
    private boolean textInUrl;

    public String getTextForScore(int score) {
        if(score <= 6 && detractorText != null) {
            return detractorText;
        } else if(score >= 7 && score <= 8 && passiveText != null) {
            return passiveText;
        } else if(score >= 9 && score <= 10 && promoterText != null) {
            return promoterText;
        } else {
            return text;
        }
    }

    public String getLinkTextForScore(int score) {
        if(score <= 6 && detractorLinkText != null) {
            return detractorLinkText;
        } else if(score >= 7 && score <= 8 && passiveLinkText != null) {
            return passiveLinkText;
        } else if(score >= 9 && score <= 10 && promoterLinkText != null) {
            return promoterLinkText;
        } else {
            return linkText;
        }
    }

    public String getLinkUrlForScore(int score) {
        if(score <= 6 && detractorLinkUrl != null) {
            return detractorLinkUrl;
        } else if(score >= 7 && score <= 8 && passiveLinkUrl != null) {
            return passiveLinkUrl;
        } else if(score >= 9 && score <= 10 && promoterLinkUrl != null) {
            return promoterLinkUrl;
        } else {
            return linkUrl;
        }
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

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public void setDetractorLinkText(String detractorLinkText) {
        this.detractorLinkText = detractorLinkText;
    }

    public void setDetractorLinkUrl(String detractorLinkUrl) {
        this.detractorLinkUrl = detractorLinkUrl;
    }

    public void setPassiveLinkText(String passiveLinkText) {
        this.passiveLinkText = passiveLinkText;
    }

    public void setPassiveLinkUrl(String passiveLinkUrl) {
        this.passiveLinkUrl = passiveLinkUrl;
    }

    public void setPromoterLinkText(String promoterLinkText) {
        this.promoterLinkText = promoterLinkText;
    }

    public void setPromoterLinkUrl(String promoterLinkUrl) {
        this.promoterLinkUrl = promoterLinkUrl;
    }

    public void setScoreInUrl(boolean scoreInUrl) {
        this.scoreInUrl = scoreInUrl;
    }

    public void setTextInUrl(boolean textInUrl) {
        this.textInUrl = textInUrl;
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
        dest.writeString(this.linkUrl);
        dest.writeString(this.detractorLinkText);
        dest.writeString(this.detractorLinkUrl);
        dest.writeString(this.passiveLinkText);
        dest.writeString(this.passiveLinkUrl);
        dest.writeString(this.promoterLinkText);
        dest.writeString(this.promoterLinkUrl);
        dest.writeByte(scoreInUrl ? (byte) 1 : (byte) 0);
        dest.writeByte(textInUrl ? (byte) 1 : (byte) 0);
    }

    public CustomThankYou() {
    }

    private CustomThankYou(Parcel in) {
        this.text = in.readString();
        this.detractorText = in.readString();
        this.passiveText = in.readString();
        this.promoterText = in.readString();
        this.linkText = in.readString();
        this.linkUrl = in.readString();
        this.detractorLinkText = in.readString();
        this.detractorLinkUrl = in.readString();
        this.passiveLinkText = in.readString();
        this.passiveLinkUrl = in.readString();
        this.promoterLinkText = in.readString();
        this.promoterLinkUrl = in.readString();
        this.scoreInUrl = in.readByte() != 0;
        this.textInUrl = in.readByte() != 0;
    }

    public static final Creator<CustomThankYou> CREATOR = new Creator<CustomThankYou>() {
        public CustomThankYou createFromParcel(Parcel source) {
            return new CustomThankYou(source);
        }

        public CustomThankYou[] newArray(int size) {
            return new CustomThankYou[size];
        }
    };
}
