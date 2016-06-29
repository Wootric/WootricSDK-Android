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

package com.wootric.androidsdk.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;

import java.util.List;

/**
 * Created by maciejwitowski on 9/22/15.
 */
public class SocialHandler {

    private static final String LOG_TAG = "WOOTRIC_SDK";

    private final Context mContext;

    public SocialHandler(Context mContext) {
        this.mContext = mContext;
    }

    public void shareOnFacebook(String facebookId) {
        if (mContext == null) return;

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
        if (mContext == null) return;

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
        if (mContext == null) return;

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
        } else {
            final Intent browserTweeter = new Intent(Intent.ACTION_VIEW);
            final Uri tweetUri = Uri.parse("https://twitter.com/intent/tweet?text=" + tweetContent);
            browserTweeter.setData(tweetUri);
            mContext.startActivity(browserTweeter);
        }
    }
}
