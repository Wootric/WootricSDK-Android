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

package com.wootric.androidsdk.views.phone;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.widget.Button;

import com.wootric.androidsdk.WootricSurveyCallback;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.views.OnSurveyFinishedListener;

import java.util.HashMap;

/**
 * Created by maciejwitowski on 10/2/15.
 */
public class ThankYouDialogFactory {
    public static Dialog create(final Context context, final Settings settings, final int score, final String text, final WootricSurveyCallback surveyCallback, final OnSurveyFinishedListener onSurveyFinishedListener, final HashMap<String, String> driverPicklist) {
        final AlertDialog thankYouDialog;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            thankYouDialog = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert).create();
        } else {
            thankYouDialog = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT).create();
        }
        thankYouDialog.setCancelable(false);
        final String thankYouText = settings.getFinalThankYou(score);

        thankYouDialog.setMessage(thankYouText);
        thankYouDialog.setCanceledOnTouchOutside(true);
        thankYouDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                if (onSurveyFinishedListener != null) {
                    onSurveyFinishedListener.onSurveyFinished();
                }
                if (surveyCallback != null) {
                    HashMap<String, Object> hashMap = new HashMap();
                    if (score != -1) {
                        hashMap.put("score", score);
                    }
                    hashMap.put("text", text);
                    hashMap.put("driver_picklist", driverPicklist);
                    surveyCallback.onSurveyDidHide(hashMap);
                }
            }
        });

        thankYouDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ColorStateList csl = ColorStateList.valueOf(Color.BLACK);
                    Button dismissButton = thankYouDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    dismissButton.setTextColor(csl);
                    dismissButton.setPaintFlags(dismissButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                }
            }
        });

        return thankYouDialog;
    }
}
