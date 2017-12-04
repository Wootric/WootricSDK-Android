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
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wootric.androidsdk.Constants;
import com.wootric.androidsdk.R;
import com.wootric.androidsdk.objects.Score;
import com.wootric.androidsdk.objects.Settings;
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

        mLayoutBody = findViewById(R.id.wootric_thank_you_layout_body);
        mTvThankYou = mLayoutBody.findViewById(R.id.wootric_tv_thank_you);
        mFaFacebookLike = mLayoutBody.findViewById(R.id.wootric_fa_facebook_like);
        mFaFacebook = mLayoutBody.findViewById(R.id.wootric_fa_facebook);
        mFaTwitter = mLayoutBody.findViewById(R.id.wootric_fa_twitter);
        mTvFacebookLike = mLayoutBody.findViewById(R.id.wootric_tv_facebook_like);
        mTvFacebook = mLayoutBody.findViewById(R.id.wootric_tv_facebook);
        mTvTwitter = mLayoutBody.findViewById(R.id.wootric_tv_twitter);
        mLayoutFacebookLike = mLayoutBody.findViewById(R.id.wootric_layout_facebook_like);
        mLayoutFacebook = mLayoutBody.findViewById(R.id.wootric_layout_facebook);
        mLayoutTwitter = mLayoutBody.findViewById(R.id.wootric_layout_twitter);
        mBtnThankYouAction = mLayoutBody.findViewById(R.id.wootric_btn_thank_you_action);

        mBtnDone = mLayoutBody.findViewById(R.id.wootric_btn_thank_you_done);
        mBtnDone.setOnClickListener(onBtnDoneClick());

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

    public void initValues(Settings settings, int score, String feedback) {
        mSettings = settings;
        mScore = score;
        mFeedback = feedback;

        initValues();
        initFonts();
    }

    private void initFonts() {
        if(mSettings.getSurveyDefaultFontResId() != Constants.NOT_SET){
            mTvThankYou.setTypeface(ResourcesCompat.getFont(getContext(), mSettings.getSurveyDefaultFontResId()));
            mTvFacebookLike.setTypeface(ResourcesCompat.getFont(getContext(), mSettings.getSurveyDefaultFontResId()));
            mTvFacebook.setTypeface(ResourcesCompat.getFont(getContext(), mSettings.getSurveyDefaultFontResId()));
            mBtnThankYouAction.setTypeface(ResourcesCompat.getFont(getContext(), mSettings.getSurveyDefaultFontResId()));
            mBtnDone.setTypeface(ResourcesCompat.getFont(getContext(), mSettings.getSurveyDefaultFontResId()));
        }

        if(mSettings.getSurveyTitleFontResId() != Constants.NOT_SET){
            mTvThankYou.setTypeface(ResourcesCompat.getFont(getContext(), mSettings.getSurveyTitleFontResId()));
        }

        if(mSettings.getSurveyBtnFontResId() != Constants.NOT_SET){
            mBtnThankYouAction.setTypeface(ResourcesCompat.getFont(getContext(), mSettings.getSurveyBtnFontResId()));
            mBtnDone.setTypeface(ResourcesCompat.getFont(getContext(), mSettings.getSurveyBtnFontResId()));
        }
    }

    private void initValues() {
        final String customThankYouText = mSettings.getCustomThankYouMessage(mScore);
        final String thankYouText = mSettings.getThankYouMessage();

        if (customThankYouText != null) {
            mTvThankYou.setText(customThankYouText);
        } else {
            mTvThankYou.setText(thankYouText);
        }

        mBtnDone.setTextColor(getResources().getColor(mSettings.getSurveyBtnColor()));
        mBtnDone.setAlpha(0.26f);
        mBtnDone.setText(mSettings.getBtnDismiss());

        initSocialLinks();
        initThankYouActionBtn();

        showSimpleDialogIfNeeded();
    }

    private void initThankYouActionBtn() {
        boolean shouldShowThankYouAction = mSettings.isThankYouActionConfigured(mScore, mFeedback);
        final String thankYouLinkText = mSettings.getThankYouLinkText(mScore);
        final int thankYouBackgroundColor =  getResources().getColor(mSettings.getThankYouButtonBackgroundColorResId());

        mBtnThankYouAction.setVisibility(shouldShowThankYouAction ? VISIBLE : GONE);
        mBtnThankYouAction.setText(thankYouLinkText);
        mBtnThankYouAction.setBackgroundColor(thankYouBackgroundColor);
    }

    private void initSocialLinks() {
        Score score = new Score(mScore, mSettings.getSurveyType());
        boolean shouldShowFacebookBtn = (score.isPromoter() && mSettings.getFacebookPageId() != null);

        mLayoutFacebook.setVisibility(shouldShowFacebookBtn ? VISIBLE : GONE);
        mLayoutFacebookLike.setVisibility(shouldShowFacebookBtn ? VISIBLE : GONE);

        mFaFacebook.setTextColor(getResources().getColor(mSettings.getSocialSharingColorResId()));
        mFaFacebookLike.setTextColor(getResources().getColor(mSettings.getSocialSharingColorResId()));

        boolean shouldShowTwitterBtn =
                        score.isPromoter() &&
                        mSettings.getTwitterPage() != null &&
                        mFeedback != null &&
                        !mFeedback.isEmpty();

        mLayoutTwitter.setVisibility(shouldShowTwitterBtn ? VISIBLE : GONE);

        mFaTwitter.setTextColor(getResources().getColor(mSettings.getSocialSharingColorResId()));
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
