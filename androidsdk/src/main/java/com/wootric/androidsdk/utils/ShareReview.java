package com.wootric.androidsdk.utils;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.widget.Toast;

import com.wootric.androidsdk.R;

import java.util.List;

/**
 * Created by Tommy on 27/5/15.
 */
public class ShareReview {
    public static void startPlaystore(Context context){
        Toast.makeText(context, context.getPackageName(), Toast.LENGTH_LONG).show();
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }
    public static void startFacebook(Context context, String title, String text){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        // See if official Facebook app is found
        boolean facebookAppFound = false;
        List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook")) {
                intent.setPackage(info.activityInfo.packageName);
                facebookAppFound = true;
                break;  }}
        // As fallback, launch sharer.php in a browser
        if (!facebookAppFound) {
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + text;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
        }
        context.startActivity(intent);
    }
    public static void startTwitter(Context context, String title, String text){
        PackageManager pm= context.getPackageManager();
        try {
            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");

            @SuppressWarnings("unused")
            PackageInfo info=pm.getPackageInfo("com.twitter.android", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.twitter.android");

            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            context.startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(context, "Twitter not Installed", Toast.LENGTH_SHORT).show();
        }
    }
    public static void startGooglePlus(Context context, String title, String text){

    }
}
