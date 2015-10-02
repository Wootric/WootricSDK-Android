package com.wootric.androidsdk.objects;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by maciejwitowski on 9/23/15.
 */
public class WootricCustomThankYou implements Parcelable {

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

    private boolean scoreInUrl;
    private boolean commentInUrl;

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

    public Uri getLinkUri(int score, String comment) {
        Uri uri = getLinkUriForScore(score);

        if(uri != null) {
            if(scoreInUrl) {
                uri = uri.buildUpon()
                        .appendQueryParameter("wootric_score", String.valueOf(score))
                        .build();
            }

            if(commentInUrl) {
                uri = uri.buildUpon()
                        .appendQueryParameter("wootric_comment", comment)
                        .build();
            }
        }

        return uri;
    }

    private Uri getLinkUriForScore(int score) {
        if(score <= 6 && detractorLinkUri != null) {
            return detractorLinkUri;
        } else if(score >= 7 && score <= 8 && passiveLinkUri != null) {
            return passiveLinkUri;
        } else if(score >= 9 && score <= 10 && promoterLinkUri != null) {
            return promoterLinkUri;
        } else {
            return linkUri;
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

    public void setScoreInUrl(boolean scoreInUrl) {
        this.scoreInUrl = scoreInUrl;
    }

    public void setCommentInUrl(boolean commentInUrl) {
        this.commentInUrl = commentInUrl;
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
        dest.writeByte(scoreInUrl ? (byte) 1 : (byte) 0);
        dest.writeByte(commentInUrl ? (byte) 1 : (byte) 0);
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
        this.scoreInUrl = in.readByte() != 0;
        this.commentInUrl = in.readByte() != 0;
    }

    public static final Creator<WootricCustomThankYou> CREATOR = new Creator<WootricCustomThankYou>() {
        public WootricCustomThankYou createFromParcel(Parcel source) {
            return new WootricCustomThankYou(source);
        }

        public WootricCustomThankYou[] newArray(int size) {
            return new WootricCustomThankYou[size];
        }
    };
}
