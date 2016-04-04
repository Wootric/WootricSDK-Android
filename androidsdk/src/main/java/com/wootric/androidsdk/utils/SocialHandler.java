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

    public void shareOnFacebook(String facebookId) {
        String urlToShare = "https://www.facebook.com/" + facebookId;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, urlToShare);

        boolean facebookAppFound = false;
        List<ResolveInfo> matches = mContext.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                intent.setPackage(info.activityInfo.packageName);
                facebookAppFound = true;
                break;
            }
        }

        if (!facebookAppFound) {
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + urlToShare;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
        }

        mContext.startActivity(intent);
    }

    public void goToFacebook(String facebookId) {
        final String url = "https://www.facebook.com/" + facebookId;
        final Uri facebookSchemeUri = Uri.parse("fb://facewebmodal/f?href=" + url);
        Intent intent = new Intent(Intent.ACTION_VIEW, facebookSchemeUri);

        boolean facebookAppFound = false;
        List<ResolveInfo> matches = mContext.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                intent.setPackage(info.activityInfo.packageName);
                facebookAppFound = true;
                break;
            }
        }

        if (!facebookAppFound) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
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
