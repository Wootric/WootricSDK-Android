package com.wootric.androidsdk.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.List;

/**
 * Created by maciejwitowski on 9/22/15.
 */
public class SocialHandler {

    private final Context mContext;

    public SocialHandler(Context mContext) {
        this.mContext = mContext;
    }

    public void goToFacebook(String facebookId) {
        final Uri facebookSchemeUri = Uri.parse("fb://page/" + facebookId);
        final Intent intent = new Intent(Intent.ACTION_VIEW, facebookSchemeUri);

        final PackageManager packageManager = mContext.getPackageManager();
        List<ResolveInfo> resolvedInfoList =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);

        if (resolvedInfoList.size() == 0) {
            final Uri facebookUri = Uri.parse("https://www.facebook.com/" + facebookId);
            intent.setData(facebookUri);
        }

        mContext.startActivity(intent);
    }

    public void goToTwitter(String twitterPage, String text) {
        final Intent twitterIntent = new Intent(Intent.ACTION_SEND);
        final String tweetContent = "@" + twitterPage + " " + text;

        twitterIntent.putExtra(Intent.EXTRA_TEXT, tweetContent);
        twitterIntent.setType("text/plain");

        final PackageManager packManager = mContext.getPackageManager();
        final List<ResolveInfo> resolvedInfoList =
                packManager.queryIntentActivities(twitterIntent,
                        PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for(ResolveInfo resolveInfo: resolvedInfoList){
            if(resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")){
                twitterIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name);
                resolved = true;
                break;
            }
        }

        if(resolved){
            mContext.startActivity(twitterIntent);
        }else{
            final Intent browserTweeter = new Intent(Intent.ACTION_VIEW);
            final Uri tweetUri = Uri.parse("https://twitter.com/intent/tweet?text=" + tweetContent);
            browserTweeter.setData(tweetUri);
            mContext.startActivity(browserTweeter);
        }
    }
}
