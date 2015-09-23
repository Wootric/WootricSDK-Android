package com.wootric.androidsdk.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by maciejwitowski on 9/23/15.
 */
public class CustomThankYouMessage implements Parcelable {

    private String thankYou;
    private String detractorThankYou;
    private String passiveThankYou;
    private String promoterThankYou;

    public void setThankYou(String thankYou) {
        this.thankYou = thankYou;
    }

    public void setDetractorThankYou(String detractorThankYou) {
        this.detractorThankYou = detractorThankYou;
    }

    public void setPassiveThankYou(String passiveThankYou) {
        this.passiveThankYou = passiveThankYou;
    }

    public void setPromoterThankYou(String promoterThankYou) {
        this.promoterThankYou = promoterThankYou;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.thankYou);
        dest.writeString(this.detractorThankYou);
        dest.writeString(this.passiveThankYou);
        dest.writeString(this.promoterThankYou);
    }

    public CustomThankYouMessage() {
    }

    private CustomThankYouMessage(Parcel in) {
        this.thankYou = in.readString();
        this.detractorThankYou = in.readString();
        this.passiveThankYou = in.readString();
        this.promoterThankYou = in.readString();
    }

    public static final Creator<CustomThankYouMessage> CREATOR = new Creator<CustomThankYouMessage>() {
        public CustomThankYouMessage createFromParcel(Parcel source) {
            return new CustomThankYouMessage(source);
        }

        public CustomThankYouMessage[] newArray(int size) {
            return new CustomThankYouMessage[size];
        }
    };

    public String getThankYouForScore(int score) {
        if(score <= 6 && detractorThankYou != null) {
            return detractorThankYou;
        } else if(score >= 7 && score <= 8 && passiveThankYou  != null) {
            return passiveThankYou;
        } else if(score >= 9 && score <= 10 && promoterThankYou != null) {
            return promoterThankYou;
        } else {
            return thankYou;
        }
    }
}
