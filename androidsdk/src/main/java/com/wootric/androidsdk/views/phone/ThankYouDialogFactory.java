package com.wootric.androidsdk.views.phone;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.wootric.androidsdk.objects.Settings;

/**
 * Created by maciejwitowski on 10/2/15.
 */
public class ThankYouDialogFactory {

    public static Dialog create(Context context, Settings settings, int score) {
        AlertDialog thankYouDialog = new AlertDialog.Builder(context).create();
        final String customThankYouText = settings.getCustomThankYouMessage(score);
        final String thankYouText = settings.getThankYouMessage();

        if (customThankYouText != null) {
            thankYouDialog.setMessage(customThankYouText);
        } else {
            thankYouDialog.setMessage(thankYouText);
        }

        thankYouDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return thankYouDialog;
    }
}
