package com.wootric.androidsdk.views.phone;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wootric.androidsdk.R;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.utils.ScreenUtils;
import com.wootric.androidsdk.views.ThankYouLayoutListener;

/**
 * Created by maciejwitowski on 9/18/15.
 */
public class ThankYouLayout extends RelativeLayout {

    private RelativeLayout mLayoutBody;

    private LinearLayout mLayoutFacebook;
    private LinearLayout mLayoutTwitter;

    private TextView mTvThankYou;
    private TextView mTvFacebook;
    private TextView mTvTwitter;
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

        mLayoutBody = (RelativeLayout) findViewById(R.id.wootric_thank_you_layout_body);
        mTvThankYou = (TextView) mLayoutBody.findViewById(R.id.wootric_tv_thank_you);
        mTvFacebook = (TextView) mLayoutBody.findViewById(R.id.wootric_tv_facebook);
        mTvTwitter = (TextView) mLayoutBody.findViewById(R.id.wootric_tv_twitter);
        mLayoutFacebook = (LinearLayout) mLayoutBody.findViewById(R.id.wootric_layout_facebook);
        mLayoutTwitter = (LinearLayout) mLayoutBody.findViewById(R.id.wootric_layout_twitter);

        mBtnThankYouAction = (Button) mLayoutBody.findViewById(R.id.wootric_btn_thank_you_action);

        mBtnDone = (TextView) mLayoutBody.findViewById(R.id.wootric_btn_thank_you_done);
        mBtnDone.setOnClickListener(onBtnDoneClick());

        mTvFacebook.setOnClickListener(notifyFacebookClick());
        mTvTwitter.setOnClickListener(notifyTwitterClick());
        mBtnThankYouAction.setOnClickListener(notifyThankYouActionClick());

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBtnThankYouAction.setElevation(ScreenUtils.dpToPx(8));
        }
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
                    mThankYouLayoutListener.onThankYouFinished();
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
    }

    private void initValues() {
        mTvThankYou.setText(mSettings.getThankYouMessage(mScore));

        initSocialLinks();
        initThankYouActionBtn();

        showSimpleDialogIfNeeded();
    }

    private void initThankYouActionBtn() {
        boolean shouldShowThankYouAction = mSettings.isThankYouActionConfigured(mScore, mFeedback);
        final String thankYouLinkText = mSettings.getThankYouLinkText(mScore);

        mBtnThankYouAction.setVisibility(shouldShowThankYouAction ? VISIBLE : GONE);
        mBtnThankYouAction.setText(thankYouLinkText);
    }

    private void initSocialLinks() {
        boolean shouldShowFacebookBtn = (mScore >= 9 && mSettings.getFacebookPageId() != null);

        mLayoutFacebook.setVisibility(shouldShowFacebookBtn ? VISIBLE : GONE);

        boolean shouldShowTwitterBtn =
                        mScore >= 9 &&
                        mSettings.getTwitterPage() != null &&
                        mFeedback != null &&
                        !mFeedback.isEmpty();

        mLayoutTwitter.setVisibility(shouldShowTwitterBtn ? VISIBLE : GONE);
    }

    private void showSimpleDialogIfNeeded() {
        final boolean shouldShowSimpleDialog = mBtnThankYouAction.getVisibility() == GONE &&
                                                mLayoutFacebook.getVisibility() == GONE &&
                                                mLayoutTwitter.getVisibility() == GONE;

        if(shouldShowSimpleDialog && mThankYouLayoutListener != null) {
            mThankYouLayoutListener.onShouldShowSimpleDialog();
        }
    }
}
