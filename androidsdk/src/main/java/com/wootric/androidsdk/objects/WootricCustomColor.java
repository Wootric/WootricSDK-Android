package com.wootric.androidsdk.objects;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.wootric.androidsdk.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class WootricCustomColor implements Parcelable  {
    private String customPrimaryColor;
    private String customSecondaryColor;
    private String customScoreScaleType;

    public WootricCustomColor() {}

    public WootricCustomColor(WootricCustomColor wootricCustomColor) {
        if (wootricCustomColor == null) return;
        this.customPrimaryColor = wootricCustomColor.customPrimaryColor;
        this.customSecondaryColor = wootricCustomColor.customSecondaryColor;
        this.customScoreScaleType = wootricCustomColor.customScoreScaleType;
    }

    public int getCustomPrimaryColor() { return Color.parseColor(this.customPrimaryColor); }

    public int getCustomSecondaryColor() { return Color.parseColor(customSecondaryColor); }

    public String getCustomScoreScaleType() {
        return customScoreScaleType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.customPrimaryColor);
        dest.writeString(this.customSecondaryColor);
        dest.writeString(this.customScoreScaleType);
    }

    private WootricCustomColor(Parcel in) {
        this.customPrimaryColor = in.readString();
        this.customSecondaryColor = in.readString();
        this.customScoreScaleType = in.readString();
    }

    public static final Creator<WootricCustomColor> CREATOR = new Creator<WootricCustomColor>() {
        public WootricCustomColor createFromParcel(Parcel source) {
            return new WootricCustomColor(source);
        }
        public WootricCustomColor[] newArray(int size) {
            return new WootricCustomColor[size];
        }
    };

    public static WootricCustomColor fromJson(JSONObject customColorsJson) throws JSONException {
        if(customColorsJson == null) return null;

        WootricCustomColor wootricCustomColor = new WootricCustomColor();
        wootricCustomColor.customPrimaryColor = customColorsJson.optString("custom_primary_color");
        wootricCustomColor.customSecondaryColor = customColorsJson.optString("custom_secondary_color");
        wootricCustomColor.customScoreScaleType = customColorsJson.optString("score_scale_type");

        return wootricCustomColor;
    }
}
