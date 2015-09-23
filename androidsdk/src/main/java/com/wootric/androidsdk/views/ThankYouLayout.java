package com.wootric.androidsdk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wootric.androidsdk.R;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.utils.SocialHandler;

/**
 * Created by maciejwitowski on 9/18/15.
 */
public class ThankYouLayout extends RelativeLayout {

    private Context mContext;

    private RelativeLayout mLayoutBody;
    private TextView mTvThankYou;
    private TextView mTvFacebook;
    private TextView mTvTwitter;
    private TextView mBtnDone;

    private ThankYouLayoutListener mThankYouLayoutListener;

    private SocialHandler mSocialHandler;
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
        mContext = context;
        mSocialHandler = new SocialHandler(context);

        inflate(context, R.layout.wootric_thank_you_layout, this);

        mLayoutBody = (RelativeLayout) findViewById(R.id.wootric_thank_you_layout_body);
        mTvThankYou = (TextView) mLayoutBody.findViewById(R.id.wootric_tv_thank_you);
        mTvFacebook = (TextView) mLayoutBody.findViewById(R.id.wootric_tv_facebook);
        mTvTwitter = (TextView) mLayoutBody.findViewById(R.id.wootric_tv_twitter);
        mBtnDone = (TextView) mLayoutBody.findViewById(R.id.wootric_btn_thank_you_done);
        mBtnDone.setOnClickListener(onBtnDoneClick());

        mTvFacebook.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFacebook();
            }
        });

        mTvTwitter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                goToTwitter();
            }
        });
    }

    private void goToFacebook() {
        if(mSocialHandler == null) return;

        String facebookId = mSettings.getFacebookPageId();
        mSocialHandler.goToFacebook(facebookId);
    }

    private void goToTwitter() {
        if(mSocialHandler == null) return;

        String twitterPage = mSettings.getTwitterPage();
        mSocialHandler.goToTwitter(twitterPage, mFeedback);
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

        boolean promoterScore = (mScore >= 9);

        if(promoterScore)
            showSocialLinks();
    }

    private void showSocialLinks() {
        boolean shouldShowFacebookBtn = (mSettings.getFacebookPageId() != null);

        if(shouldShowFacebookBtn) {
            mTvFacebook.setVisibility(VISIBLE);
        }

        boolean shouldShowTwitterBtn = (
                        mSettings.getTwitterPage() != null &&
                        mFeedback != null &&
                        !mFeedback.isEmpty());

        if(shouldShowTwitterBtn)
            mTvTwitter.setVisibility(VISIBLE);
    }

    public interface ThankYouLayoutListener {
        void onThankYouFinished();
    }
}
