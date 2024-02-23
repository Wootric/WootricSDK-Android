/*
 * Copyright (c) 2024 Wootric (https://wootric.com)
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

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.wootric.androidsdk.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by diegoserranoa on 3/28/24.
 */
public class WootricCustomColor implements Parcelable  {
    private static final String CUSTOM_PRIMARY_COLOR_KEY = "custom_primary_color";
    private static final String CUSTOM_SECONDARY_COLOR_KEY = "custom_secondary_color";
    private static final String SCORE_SCALE_TYPE_KEY = "score_scale_type";
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

    public int getCustomPrimaryColor() {
        if (this.customPrimaryColor != "null") {
            try {
                return Color.parseColor(this.customPrimaryColor);
            } catch (IllegalArgumentException ex) {
                return Color.parseColor("#253746");
            }
        }
        return Color.parseColor("#253746");
    }

    public int getCustomSecondaryColor() {
        if (Utils.isNotEmpty(this.customSecondaryColor) && this.customSecondaryColor != "null") {
            try {
                return Color.parseColor(this.customSecondaryColor);
            } catch (IllegalArgumentException ex) {
                return Color.parseColor("#B3CDFF");
            }
        }
        return Color.parseColor("#B3CDFF");
    }

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
        wootricCustomColor.customPrimaryColor = customColorsJson.optString(CUSTOM_PRIMARY_COLOR_KEY);
        wootricCustomColor.customSecondaryColor = customColorsJson.optString(CUSTOM_SECONDARY_COLOR_KEY);
        wootricCustomColor.customScoreScaleType = customColorsJson.optString(SCORE_SCALE_TYPE_KEY);

        return wootricCustomColor;
    }
}
