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

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wootric.androidsdk.R;
import com.wootric.androidsdk.objects.Score;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.utils.FontManager;
import com.wootric.androidsdk.utils.ScreenUtils;
import com.wootric.androidsdk.views.ThankYouLayoutListener;

/**
 * Created by maciejwitowski on 9/18/15.
 */
public class ThankYouLayout extends RelativeLayout {

    private RelativeLayout mLayoutBody;

    private LinearLayout mLayoutFacebookLike;
    private LinearLayout mLayoutFacebook;
    private LinearLayout mLayoutTwitter;

    private TextView mTvThankYou;
    private TextView mTvThankYouSetup;
    private TextView mTvFacebookLike;
    private TextView mTvFacebook;
    private TextView mTvTwitter;
    private TextView mFaFacebookLike;
    private TextView mFaFacebook;
    private TextView mFaTwitter;
    private TextView mBtnDone;
    private Button mBtnThankYouAction;

    private ThankYouLayoutListener mThankYouLayoutListener;

    private String mFeedback;
    private int mScore;
    private String mEmail;
    private Settings mSettings;

    public ThankYouLayout(Context context) {
        super(context);
        init(context);
    }

    public ThankYouLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ThankYouLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.wootric_thank_you_layout, this);

        Typeface iconFont = FontManager.getTypeface(context, FontManager.FONTAWESOME);

        mLayoutBody = (RelativeLayout) findViewById(R.id.wootric_thank_you_layout_body);
        mTvThankYou = (TextView) mLayoutBody.findViewById(R.id.wootric_tv_thank_you);
        mTvThankYouSetup = (TextView) mLayoutBody.findViewById(R.id.wootric_tv_thank_you_setup);
        mFaFacebookLike = (TextView) mLayoutBody.findViewById(R.id.wootric_fa_facebook_like);
        mFaFacebook = (TextView) mLayoutBody.findViewById(R.id.wootric_fa_facebook);
        mFaTwitter = (TextView) mLayoutBody.findViewById(R.id.wootric_fa_twitter);
        mTvFacebookLike = (TextView) mLayoutBody.findViewById(R.id.wootric_tv_facebook_like);
        mTvFacebook = (TextView) mLayoutBody.findViewById(R.id.wootric_tv_facebook);
        mTvTwitter = (TextView) mLayoutBody.findViewById(R.id.wootric_tv_twitter);
        mLayoutFacebookLike = (LinearLayout) mLayoutBody.findViewById(R.id.wootric_layout_facebook_like);
        mLayoutFacebook = (LinearLayout) mLayoutBody.findViewById(R.id.wootric_layout_facebook);
        mLayoutTwitter = (LinearLayout) mLayoutBody.findViewById(R.id.wootric_layout_twitter);
        mBtnThankYouAction = (Button) mLayoutBody.findViewById(R.id.wootric_btn_thank_you_action);

        mBtnDone = (TextView) mLayoutBody.findViewById(R.id.wootric_btn_thank_you_done);
        mBtnDone.setOnClickListener(onBtnDoneClick());

        mFaFacebookLike.setTypeface(iconFont);
        mFaFacebook.setTypeface(iconFont);
        mFaTwitter.setTypeface(iconFont);

        mTvFacebookLike.setOnClickListener(notifyFacebookLikeClick());
        mTvFacebook.setOnClickListener(notifyFacebookClick());
        mTvTwitter.setOnClickListener(notifyTwitterClick());
        mBtnThankYouAction.setOnClickListener(notifyThankYouActionClick());

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBtnThankYouAction.setElevation(ScreenUtils.dpToPx(8));
        }
    }

    private OnClickListener notifyFacebookLikeClick() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mThankYouLayoutListener != null) {
                    mThankYouLayoutListener.onFacebookLikeBtnClick();
                }
            }
        };
    }

    private OnClickListener notifyFacebookClick() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mThankYouLayoutListener != null) {
                    mThankYouLayoutListener.onFacebookBtnClick();
                }
            }
        };
    }

    private OnClickListener notifyTwitterClick() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mThankYouLayoutListener != null) {
                    mThankYouLayoutListener.onTwitterBtnClick();
                }
            }
        };
    }

    private OnClickListener notifyThankYouActionClick() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mThankYouLayoutListener != null) {
                    mThankYouLayoutListener.onThankYouActionClick();
                }
            }
        };
    }

    private OnClickListener onBtnDoneClick() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mThankYouLayoutListener != null) {
                    mThankYouLayoutListener.onDismissClick();
                }
            }
        };
    }

    public void setThankYouLayoutListener(ThankYouLayoutListener thankYouLayoutListener) {
        mThankYouLayoutListener = thankYouLayoutListener;
    }

    public void initValues(Settings settings, String email, int score, String feedback) {
        mSettings = settings;
        mEmail = email;
        mScore = score;
        mFeedback = feedback;

        initValues();
    }

    private void initValues() {
        final String thankYouText = mSettings.getFinalThankYou(mScore);
        final String thankYouSetupText = mSettings.getCustomThankYouMessage(mScore);

        mTvThankYou.setText(thankYouText);

        if (thankYouSetupText != null) {
            mTvThankYouSetup.setText(thankYouSetupText);
        }

        mBtnDone.setTextColor(getResources().getColor(mSettings.getSurveyColor()));
        mBtnDone.setText(mSettings.getBtnDismiss());

        initSocialLinks();
        initThankYouActionBtn();

        showSimpleDialogIfNeeded();
    }

    private void initThankYouActionBtn() {
        boolean shouldShowThankYouAction = mSettings.isThankYouActionConfigured(mEmail, mScore, mFeedback);
        final String thankYouLinkText = mSettings.getThankYouLinkText(mScore);
        final int thankYouBackgroundColor =  getResources().getColor(mSettings.getThankYouButtonBackgroundColor());

        mBtnThankYouAction.setVisibility(shouldShowThankYouAction ? VISIBLE : GONE);
        mBtnThankYouAction.setText(thankYouLinkText);
        mBtnThankYouAction.setBackgroundColor(thankYouBackgroundColor);
    }

    private void initSocialLinks() {
        Score score = new Score(mScore, mSettings.getSurveyType(), mSettings.getSurveyTypeScale());
        boolean shouldShowFacebookBtn = (score.isPromoter() &&
                                            mSettings.isFacebookEnabled() &&
                                            mSettings.getFacebookPageId() != null);

        mLayoutFacebook.setVisibility(shouldShowFacebookBtn ? VISIBLE : GONE);
        mLayoutFacebookLike.setVisibility(shouldShowFacebookBtn ? VISIBLE : GONE);

        mFaFacebook.setTextColor(getResources().getColor(mSettings.getSocialSharingColor()));
        mFaFacebookLike.setTextColor(getResources().getColor(mSettings.getSocialSharingColor()));

        boolean shouldShowTwitterBtn =
                        score.isPromoter() &&
                        mSettings.isTwitterEnabled() &&
                        mSettings.getTwitterPage() != null &&
                        mFeedback != null &&
                        !mFeedback.isEmpty();

        mLayoutTwitter.setVisibility(shouldShowTwitterBtn ? VISIBLE : GONE);

        mFaTwitter.setTextColor(getResources().getColor(mSettings.getSocialSharingColor()));
    }

    private void showSimpleDialogIfNeeded() {
        final boolean shouldShowSimpleDialog = mBtnThankYouAction.getVisibility() == GONE &&
                                                mLayoutFacebookLike.getVisibility() == GONE &&
                                                mLayoutFacebook.getVisibility() == GONE &&
                                                mLayoutTwitter.getVisibility() == GONE;

        if(shouldShowSimpleDialog && mThankYouLayoutListener != null) {
            mThankYouLayoutListener.onShouldShowSimpleDialog();
        }
    }
}
