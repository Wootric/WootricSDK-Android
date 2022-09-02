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

package com.wootric.androidsdk.views.support;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wootric.androidsdk.OfflineDataHandler;
import com.wootric.androidsdk.R;
import com.wootric.androidsdk.Wootric;
import com.wootric.androidsdk.WootricSurveyCallback;
import com.wootric.androidsdk.network.WootricRemoteClient;
import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.utils.PreferencesUtils;
import com.wootric.androidsdk.utils.SHAUtil;
import com.wootric.androidsdk.utils.ScreenUtils;
import com.wootric.androidsdk.utils.SocialHandler;
import com.wootric.androidsdk.utils.Utils;
import com.wootric.androidsdk.views.OnSurveyFinishedListener;
import com.wootric.androidsdk.views.SurveyLayout;
import com.wootric.androidsdk.views.SurveyLayoutListener;
import com.wootric.androidsdk.views.phone.ThankYouDialogFactory;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by maciejwitowski on 9/4/15.
 */
public class SurveyFragment extends DialogFragment implements SurveyLayoutListener {
    private static final String ARG_ORIGIN_URL = "com.wootric.androidsdk.arg.origin_url";
    private static final String ARG_END_USER = "com.wootric.androidsdk.arg.end_user";
    private static final String ARG_USER = "com.wootric.androidsdk.arg.user";
    private static final String ARG_SETTINGS = "com.wootric.androidsdk.arg.settings";
    private static final String ARG_RESPONSE_SENT = "com.wootric.androidsdk.arg.response_sent";
    private static final String ARG_ACCESS_TOKEN = "com.wootric.androidsdk.arg.access_token";

    private SurveyLayout mSurveyLayout;
    private LinearLayout mFooter;
    private WootricSurveyCallback mSurveyCallback;
    private OnSurveyFinishedListener mOnSurveyFinishedListener;

    private EndUser mEndUser;
    private User mUser;
    private String mOriginUrl;
    private String mAccessToken;
    private String mUniqueLink;
    private Settings mSettings;
    private LinearLayout mPoweredBy;
    private TextView mBtnOptOut;

    private int mScore = -1;
    private String mText;
    private boolean mResponseSent;
    private boolean mShouldShowSimpleDialog;

    private WootricRemoteClient mWootricApiClient;
    private SocialHandler mSocialHandler;

    private boolean mIsTablet;

    private boolean isResumedOnConfigurationChange;

    private int priority = 0;

    public static SurveyFragment newInstance(EndUser endUser, String originUrl, String accessToken,
                                             Settings settings, User user) {
        SurveyFragment fragment = new SurveyFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_END_USER, endUser);
        args.putParcelable(ARG_USER, user);
        args.putString(ARG_ORIGIN_URL, originUrl);
        args.putString(ARG_ACCESS_TOKEN, accessToken);
        args.putParcelable(ARG_SETTINGS, settings);

        fragment.setArguments(args);
        return fragment;
    }

    public void setSurveyCallback(WootricSurveyCallback surveyCallback) {
        mSurveyCallback = surveyCallback;
    }

    public void setOnSurveyFinishedListener(OnSurveyFinishedListener onSurveyFinishedListener) {
        mOnSurveyFinishedListener = onSurveyFinishedListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.app.DialogFragment.STYLE_NO_TITLE, 0);
        setupState(savedInstanceState);

        mSocialHandler = new SocialHandler(getActivity());
        PreferencesUtils prefUtils = new PreferencesUtils(new WeakReference<Context>(this.getActivity()));
        OfflineDataHandler offlineDataHandler = new OfflineDataHandler(prefUtils);
        mWootricApiClient = new WootricRemoteClient(offlineDataHandler, mUser.getAccountToken());

        mIsTablet = getResources().getBoolean(R.bool.isTablet);

        Date date = new Date();
        mUniqueLink = SHAUtil.buildUniqueLink(mUser.getAccountToken(), mEndUser.getEmail(), (date.getTime() / 1000), SHAUtil.randomString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wootric_fragment_survey, container, false);
        mPoweredBy = (LinearLayout) view.findViewById(R.id.wootric_powered_by);

        if (!mIsTablet) {
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        }

        mSurveyLayout = (SurveyLayout) view.findViewById(R.id.wootric_survey_layout);
        mSurveyLayout.setSurveyLayoutListener(this);

        mFooter = (LinearLayout) view.findViewById(R.id.wootric_footer);
        mBtnOptOut = (TextView) view.findViewById(R.id.wootric_btn_opt_out);
        mBtnOptOut.setText(mSettings.getBtnOptOut());

        if (!mSettings.isShowPoweredBy() && mPoweredBy != null) {
            mPoweredBy.setVisibility(View.GONE);
        }

        if (mSettings.isShowOptOut()) {
            mBtnOptOut.setVisibility(View.VISIBLE);
            mBtnOptOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    optOut();
                }
            });

            if(!mIsTablet) {
                mPoweredBy.setGravity(Gravity.RIGHT);
            } else {
                TextView mDotSeparator = (TextView) view.findViewById(R.id.footer_dot_separator);
                mDotSeparator.setVisibility(View.VISIBLE);
            }
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSurveyLayout.initWithSettings(mSettings, mEndUser.getEmail());
    }

    @Override
    public void onStart() {
        super.onStart();

        if(!mIsTablet)
            measurePhoneDialog();
    }

    private void measurePhoneDialog() {
        final Activity activity = getActivity();
        if (activity == null) return;

        Dialog dialog = getDialog();
        if (dialog == null) return;

        final Window window = dialog.getWindow();
        if (window == null) return;

        final WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());

        final int screenHeight = ScreenUtils.getScreenHeight(activity);
        final int screenWidth = ScreenUtils.getScreenWidth(activity);

        final boolean launchedInPortrait = (screenHeight > screenWidth);
        final boolean isPortraitMode = (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);

        if(launchedInPortrait) {
            if(isPortraitMode) {
                lp.height = screenHeight*4/5;
                lp.width = screenWidth;
            } else {
                lp.height = screenWidth*19/20;
                lp.width = screenHeight*4/5;
            }
        } else {
            if(isPortraitMode) {
                lp.height = screenWidth*4/5;
                lp.width = screenHeight;
            } else {
                lp.height = screenHeight;
                lp.width = screenWidth*4/5;
            }
        }

        window.setAttributes(lp);
    }

    private void setupState(Bundle savedInstanceState) {
        Bundle args = getArguments();
        mEndUser = args.getParcelable(ARG_END_USER);
        mUser = args.getParcelable(ARG_USER);
        mOriginUrl = args.getString(ARG_ORIGIN_URL);
        mAccessToken = args.getString(ARG_ACCESS_TOKEN);
        mSettings = args.getParcelable(ARG_SETTINGS);

        if(savedInstanceState != null) {
            mResponseSent = savedInstanceState.getBoolean(ARG_RESPONSE_SENT);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(ARG_RESPONSE_SENT, mResponseSent);
        super.onSaveInstanceState(outState);
    }

    private void createDecline() {
        mWootricApiClient.createDecline(mEndUser.getId(), mSettings.getUserID(), mSettings.getAccountID(), priority, mAccessToken, mOriginUrl, mUniqueLink);
    }

    @Override
    public void onSurveySubmit(int score, String text) {
        mWootricApiClient.createResponse(mEndUser.getId(), mSettings.getUserID(), mSettings.getAccountID(), mAccessToken, mOriginUrl, score, priority, text, mUniqueLink, mSettings.getLanguageCode());
        mScore = score;
        mText = text;
        mResponseSent = true;
        priority++;
    }

    @Override
    public void onHideOptOut() {
        if (mSettings.isShowOptOut() && mBtnOptOut != null && !mIsTablet) {
            mPoweredBy.setGravity(Gravity.CENTER);
            mBtnOptOut.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFacebookLikeBtnClick() {
        if (mSocialHandler == null) return;

        String facebookId = mSettings.getFacebookPageId();
        mSocialHandler.goToFacebook(facebookId);

        dismiss();
    }

    @Override
    public void onFacebookBtnClick() {
        if (mSocialHandler == null) return;

        String facebookId = mSettings.getFacebookPageId();
        mSocialHandler.shareOnFacebook(facebookId);

        dismiss();
    }

    @Override
    public void onTwitterBtnClick() {
        if (mSocialHandler == null) return;

        String twitterPage = mSettings.getTwitterPage();
        mSocialHandler.goToTwitter(twitterPage, mSurveyLayout.getFeedback());

        dismiss();
    }

    @Override
    public void onThankYouActionClick() {
        final Uri uri = mSettings.getThankYouLinkUri(mSurveyLayout.getEmail(), mSurveyLayout.getSelectedScore(), mSurveyLayout.getFeedback());
        final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        final Activity activity = getActivity();

        if (activity != null) {
            activity.startActivity(intent);
        }

        dismiss();
    }

    @Override
    public void dismiss() {
        super.dismiss();

        if(mIsTablet) {
            notifySurveyFinished();
        }
    }

    @Override
    public void onDismissClick() {
        if (!mResponseSent) {
            createDecline();
        }

        dismiss();
    }

    @Override
    public void onShouldShowSimpleDialog() {
        final Activity activity = getActivity();

        if (activity != null) {
            mShouldShowSimpleDialog = true;
            ThankYouDialogFactory.create(activity, mSettings, mSurveyLayout.getSelectedScore(), mText, mSurveyCallback, mOnSurveyFinishedListener).show();
        }

        dismiss();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSurveyCallback != null) {
            mSurveyCallback.onSurveyWillHide();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        isResumedOnConfigurationChange = true;

        if (!mShouldShowSimpleDialog) {
            if (mOnSurveyFinishedListener != null) {
                mOnSurveyFinishedListener.onSurveyFinished();
            }
            if (mSurveyCallback != null) {
                HashMap<String, Object> hashMap = new HashMap();
                if (mScore != -1) {
                    hashMap.put("score", mScore);
                }
                hashMap.put("text", mText);
                mSurveyCallback.onSurveyDidHide(hashMap);
            }
        }

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if(!isResumedOnConfigurationChange) {
            notifySurveyFinished();
        }
    }

    private void notifySurveyFinished() {
        Integer resurvey_days = mResponseSent ? mSettings.getResurveyThrottle() : mSettings.getDeclineResurveyThrottle();
        Wootric.notifySurveyFinished(true, mResponseSent, resurvey_days);
    }

    private void optOut() {
        String tld = Utils.startsWithEU(mUser.getAccountToken()) ? "eu" : "com";
        String optOutUrl = "https://app.wootric." + tld + "/opt_out?token=" + mUser.getAccountToken()
                + "&metric_type=" + mSettings.getSurveyType()
                + "&end_user_id=" + Long.toString(mEndUser.getId())
                + "&end_user_email=" + mEndUser.getEmail()
                + "&unique_link=" + mUniqueLink
                + "&opt_out_token=" + mAccessToken;

        Intent optOut = new Intent(Intent.ACTION_VIEW, Uri.parse(optOutUrl));
        startActivity(optOut);

        dismiss();
    }
}
