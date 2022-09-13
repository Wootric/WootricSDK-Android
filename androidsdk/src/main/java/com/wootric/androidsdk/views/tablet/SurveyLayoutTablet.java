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

package com.wootric.androidsdk.views.tablet;

import static com.wootric.androidsdk.utils.ScreenUtils.fadeInView;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wootric.androidsdk.Constants;
import com.wootric.androidsdk.R;
import com.wootric.androidsdk.objects.Score;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.utils.FontManager;
import com.wootric.androidsdk.utils.ScreenUtils;
import com.wootric.androidsdk.views.SurveyLayout;
import com.wootric.androidsdk.views.SurveyLayoutListener;
import com.wootric.androidsdk.views.ThankYouLayoutListener;
import com.wootric.androidsdk.views.driverpicklist.DriverPicklist;
import com.wootric.androidsdk.views.driverpicklist.DriverPicklistButtonListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by maciejwitowski on 10/8/15.
 */
public class SurveyLayoutTablet extends LinearLayout
        implements SurveyLayout, ScoreView.OnScoreClickListener, ThankYouLayoutListener {

    private static final int STATE_SURVEY = 0;
    private static final int STATE_FEEDBACK = 1;
    private static final int STATE_THANK_YOU = 2;

    private Context mContext;

    RelativeLayout mSurveyLayout;
    private TextView mTvSurveyQuestion;
    private TextView mTvAnchorLikely;
    private TextView mTvAnchorNotLikely;
    private LinearLayout mScoreLayout;
    private LinearLayout mRatingContainer;
    private LinearLayout mLinearLayout;

    private View mWootricFooter;
    private View mWootricFooter2;

    private RelativeLayout mLayoutFollowup;
    private TextView mTvFollowupQuestion;
    private EditText mEtFeedback;
    private Button mBtnSubmit;
    private Button mBtnSubmit2;
    private TextView mBtnDismiss;
    private DriverPicklist mDriverPicklist;

    private RelativeLayout mThankYouLayout;
    private LinearLayout mPoweredByLayout;
    private TextView mPoweredByTv;
    private TextView mInMomentTv;
    private TextView mDotSeparatorTv;

    private Button mBtnFacebookLike;
    private Button mBtnTwitter;
    private Button mBtnFacebook;
    private Button mBtnThankYouDone;
    private Button mBtnThankYouAction;
    private TextView mBtnThankYouDismiss;
    private TextView mTvThankYou;
    private TextView mTvCustomThankYou;

    private Settings mSettings;

    private int mCurrentState;

    private String mSurveyType;
    private int mScaleMinimum;
    private int mScaleMaximum;

    private Score mScore;
    private int mScoresTop;
    private ScoreView[] mScoreViews;
    private int mCurrentScore = -1;
    private String mCurrentEmail;

    private HashMap<String, String> selectedAnswers;

    private SurveyLayoutListener mSurveyLayoutListener;

    private static final int CONTAINER_PADDING = (int) ScreenUtils.dpToPx(8);

    public SurveyLayoutTablet(Context context) {
        super(context);
        init(context);
    }

    public SurveyLayoutTablet(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SurveyLayoutTablet(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;

        LayoutInflater.from(mContext).inflate(R.layout.wootric_survey_layout, this);

        Typeface iconFont = FontManager.getTypeface(context, FontManager.FONTAWESOME);

        mSurveyLayout = (RelativeLayout) findViewById(R.id.wootric_nps_layout);

        mTvSurveyQuestion = (TextView) findViewById(R.id.wootric_survey_layout_tv_header);

        mRatingContainer = (LinearLayout) findViewById(R.id.wootric_layout_rating_with_anchors);
        mTvAnchorLikely = (TextView) mRatingContainer.findViewById(R.id.wootric_anchor_likely);
        mTvAnchorNotLikely = (TextView) mRatingContainer.findViewById(R.id.wootric_anchor_not_likely);
        mScoreLayout = (LinearLayout) mRatingContainer.findViewById(R.id.wootric_rating_bar);
        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        mLayoutFollowup = (RelativeLayout) findViewById(R.id.wootric_layout_followup);
        mTvFollowupQuestion = (TextView) mLayoutFollowup.findViewById(R.id.wootric_tv_followup);
        mEtFeedback = (EditText) mLayoutFollowup.findViewById(R.id.wootric_et_feedback);
        mEtFeedback.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mEtFeedback.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    submitSurvey();
                    return true;
                }
                return false;
            }
        });

        mBtnSubmit = (Button) mLayoutFollowup.findViewById(R.id.wootric_btn_submit);
        mBtnSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                submitSurvey();
            }
        });
        mBtnSubmit2 = (Button) findViewById(R.id.wootric_btn_submit_2);
        mBtnSubmit2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                submitSurvey();
            }
        });
        mBtnDismiss = (TextView) findViewById(R.id.wootric_btn_dismiss);
        mBtnDismiss.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissSurvey();
            }
        });

        mDriverPicklist = (DriverPicklist) findViewById(R.id.wootric_driver_picklist);
        selectedAnswers = new HashMap<>();

        mWootricFooter = (View) findViewById(R.id.wootric_footer);
        mWootricFooter2 = (View) findViewById(R.id.wootric_footer_2);
        mBtnThankYouDismiss = (TextView) findViewById(R.id.wootric_btn_thank_you_dismiss);
        mBtnThankYouDismiss.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissSurvey();
            }
        });

        mBtnFacebookLike = (Button) findViewById(R.id.btn_facebook_like);
        mBtnTwitter = (Button) findViewById(R.id.btn_twitter);
        mBtnFacebook = (Button) findViewById(R.id.btn_facebook);
        mBtnThankYouDone = (Button) findViewById(R.id.wootric_btn_thank_you_done);
        mBtnThankYouAction = (Button) findViewById(R.id.wootric_btn_thank_you_action);
        mTvThankYou = (TextView) findViewById(R.id.wootric_tv_thank_you);
        mTvCustomThankYou = (TextView) findViewById(R.id.wootric_tv_custom_thank_you);

        mBtnFacebookLike.setTypeface(iconFont);
        mBtnTwitter.setTypeface(iconFont);
        mBtnFacebook.setTypeface(iconFont);

        mBtnFacebookLike.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onFacebookLikeBtnClick();
            }
        });

        mBtnTwitter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onTwitterBtnClick();
            }
        });

        mBtnFacebook.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onFacebookBtnClick();
            }
        });

        mBtnThankYouDone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissSurvey();
            }
        });

        mBtnThankYouAction.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onThankYouActionClick();
            }
        });

        mThankYouLayout = (RelativeLayout) findViewById(R.id.wootric_thank_you_layout_body);

        mPoweredByLayout = (LinearLayout) findViewById(R.id.wootric_layout_powered_by);
        mPoweredByTv = (TextView) findViewById(R.id.wootric_tv_powered_by);
        mInMomentTv = (TextView) findViewById(R.id.wootric_tv_im);
        mDotSeparatorTv = (TextView) findViewById(R.id.footer_dot_separator);

        setPadding(CONTAINER_PADDING, CONTAINER_PADDING, CONTAINER_PADDING, CONTAINER_PADDING);

        initScoreLayout();
        updateState(mCurrentState);
    }

    private void initResources() {
        final Resources res = getResources();

        mScaleMinimum = mSurveyType == null ? 0 : mScore.minimumScore();
        mScaleMaximum = mSurveyType == null ? 10 : mScore.maximumScore();

        mScoresTop = mScaleMaximum + 1;

        new DriverPicklist.Configure()
                .driverPicklist(mDriverPicklist)
                .selectedColor(mContext.getResources().getColor(mSettings.getScoreColor()))
                .selectedFontColor(Color.parseColor("#ffffff"))
                .deselectedColor(Color.parseColor("#ffffff"))
                .deselectedFontColor(Color.parseColor("#253746"))
                .selectTransitionMS(100)
                .deselectTransitionMS(100)
                .labels(null)
                .mode(DriverPicklist.Mode.MULTI)
                .allCaps(false)
                .gravity(DriverPicklist.Gravity.CENTER)
                .textSize(getResources().getDimensionPixelSize(R.dimen.default_textsize))
                .verticalSpacing(getResources().getDimensionPixelSize(R.dimen.vertical_spacing))
                .minHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.min_horizontal_spacing))
                .typeface(Typeface.DEFAULT)
                .setDriverPicklistButtonListener(new DriverPicklistButtonListener() {
                    @Override
                    public void buttonSelected(int index) {
                    }
                    @Override
                    public void buttonDeselected(int index) {
                    }
                })
                .build();
    }

    private void initScoreLayout() {
        mScoreViews = new ScoreView[mScoresTop];

        for(int score = mScaleMinimum; score < mScoresTop; score++) {
            ScoreView scoreView = new ScoreView(mContext);
            scoreView.setText(String.valueOf(score));
            scoreView.setOnScoreClickListener(this);
            mScoreViews[score] = scoreView;
            mScoreLayout.addView(scoreView);
        }
    }

    @Override
    public void setSurveyLayoutListener(SurveyLayoutListener surveyLayoutListener) {
        mSurveyLayoutListener = surveyLayoutListener;
    }

    @Override
    public void initWithSettings(Settings settings, String email) {
        mSettings = settings;
        mCurrentEmail = email;
        mSurveyType = mSettings.getSurveyType();

        mScore = new Score(mCurrentScore, mSurveyType, mSettings.getSurveyTypeScale());

        if (!mSettings.isShowPoweredBy() && mPoweredByLayout != null) {
            mPoweredByLayout.setVisibility(View.GONE);
            mDotSeparatorTv.setVisibility(View.GONE);
        }

        initResources();
        initScoreLayout();
        updateState(mCurrentState);
        setTexts();
    }

    private void setTexts() {
        if(mSettings != null) {
            mTvSurveyQuestion.setText(mSettings.getSurveyQuestion());
            mTvAnchorLikely.setText(mSettings.getAnchorLikely());
            mTvAnchorNotLikely.setText(mSettings.getAnchorNotLikely());
            mBtnSubmit.setText(mSettings.getBtnSubmit());
            mBtnSubmit2.setText(mSettings.getBtnSubmit());
            mEtFeedback.setImeActionLabel(mSettings.getBtnSubmit(), KeyEvent.KEYCODE_ENTER);
        }
    }

    private void updateState(int state) {
        mCurrentState = state;

        if(STATE_SURVEY == mCurrentState) {
            setupSurveyState();
        } else if(STATE_FEEDBACK == mCurrentState) {
            setupFeedbackState();
        } else {
            setupThankYouState();
        }
    }

    private void setupSurveyState() {
        mLayoutFollowup.setVisibility(GONE);
    }

    private void setupFeedbackState() {
        setFeedbackTexts();

        mDriverPicklist.removeAllViews();
        selectedAnswers.clear();
        try {
            JSONObject dplSettings = mSettings.getDriverPicklistSettings(mCurrentScore);
            JSONObject dpl = mSettings.getDriverPicklist(mCurrentScore);

            if (dplSettings.getBoolean("dpl_multi_select")) {
                mDriverPicklist.setMode(DriverPicklist.Mode.MULTI);
            } else {
                mDriverPicklist.setMode(DriverPicklist.Mode.SINGLE);
            }

            if (dplSettings.getBoolean("dpl_hide_open_ended")) {
                mLinearLayout.setVisibility(GONE);
                mBtnSubmit2.setVisibility(VISIBLE);
                mWootricFooter.setVisibility(GONE);
                mWootricFooter2.setVisibility(VISIBLE);
                if (!mSettings.isShowPoweredBy()) {
                    mWootricFooter2.findViewById(R.id.wootric_tv_powered_by).setVisibility(View.GONE);
                    mWootricFooter2.findViewById(R.id.footer_dot_separator).setVisibility(View.GONE);
                    mWootricFooter2.findViewById(R.id.wootric_tv_im).setVisibility(View.GONE);
                }
            } else {
                mLinearLayout.setVisibility(VISIBLE);
                mBtnSubmit2.setVisibility(GONE);
                mWootricFooter.setVisibility(VISIBLE);
                mWootricFooter2.setVisibility(GONE);
            }
            ArrayList<String> dplList = new ArrayList<>();
            if (dpl != null) {
                Iterator<String> keys = dpl.keys();

                while(keys.hasNext()) {
                    String key = keys.next();
                    dplList.add(dpl.get(key).toString());
                }
            }

            if (dplSettings.getBoolean("dpl_randomize_list")) {
                ArrayList<String> shuffled = new ArrayList<>(dplList);
                Collections.shuffle(shuffled);
                for (String value : shuffled) {
                    mDriverPicklist.addButton(value);
                }
            } else {
                for (String value : dplList) {
                    mDriverPicklist.addButton(value);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mTvSurveyQuestion.setVisibility(GONE);
        mLayoutFollowup.setAlpha(0);
        mLayoutFollowup.setVisibility(VISIBLE);
        fadeInView(mLayoutFollowup);
    }

    private void setFeedbackTexts() {
        mEtFeedback.setHint(mSettings.getFollowupPlaceholder(mCurrentScore));
        mTvFollowupQuestion.setText(mSettings.getFollowupQuestion(mCurrentScore));
    }

    private void initThankYouActionBtn() {
        final String feedback = getFeedback();
        boolean shouldShowThankYouAction = mSettings.isThankYouActionConfigured(mCurrentEmail, mCurrentScore, feedback);
        final String thankYouLinkText = mSettings.getThankYouLinkText(mCurrentScore);

        mBtnThankYouAction.setVisibility(shouldShowThankYouAction ? VISIBLE : GONE);
        mBtnThankYouAction.setText(thankYouLinkText);
    }

    private void setupThankYouState() {
        final String feedback = getFeedback();
        mSurveyLayout.setVisibility(GONE);
        setKeyboardVisibility(false);

        initThankYouActionBtn();
        initSocialLinks();

        final boolean shouldShowThankYouDone = mBtnThankYouAction.getVisibility() == GONE &&
                mBtnFacebook.getVisibility() == GONE &&
                mBtnFacebookLike.getVisibility() == GONE &&
                mBtnTwitter.getVisibility() == GONE;

        mBtnThankYouDone.setVisibility(shouldShowThankYouDone ? GONE : VISIBLE);
        mBtnThankYouDone.setText(mSettings.getSocialShareDecline());

        final String thankYouText = mSettings.getFinalThankYou(mCurrentScore);
        final String thankYouSetupText = mSettings.getCustomThankYouMessage(mCurrentScore);

        mTvThankYou.setText(thankYouText);

        if (thankYouSetupText != null) {
            mTvCustomThankYou.setText(thankYouSetupText);
            mTvCustomThankYou.setVisibility(VISIBLE);
        } else {
            mTvCustomThankYou.setVisibility(GONE);
        }

        mThankYouLayout.setAlpha(0);
        mThankYouLayout.setVisibility(VISIBLE);
        if (!mSettings.isShowPoweredBy() && mThankYouLayout != null) {
            mThankYouLayout.findViewById(R.id.wootric_layout_powered_by).setVisibility(View.GONE);
        }
        fadeInView(mThankYouLayout);
    }

    private void initSocialLinks() {
        Score score = new Score(mCurrentScore, mSettings.getSurveyType(), mSettings.getSurveyTypeScale());
        boolean shouldShowFacebookBtn = (score.isPromoter() &&
                                            mSettings.isFacebookEnabled() &&
                                            mSettings.getFacebookPageId() != null);

        mBtnFacebook.setVisibility(shouldShowFacebookBtn ? VISIBLE : GONE);
        mBtnFacebookLike.setVisibility(shouldShowFacebookBtn ? VISIBLE : GONE);

        final String feedback = getFeedback();

        boolean shouldShowTwitterBtn =
                        score.isPromoter() &&
                        mSettings.isTwitterEnabled() &&
                        mSettings.getTwitterPage() != null &&
                        feedback != null &&
                        !feedback.isEmpty();
        mBtnTwitter.setVisibility(shouldShowTwitterBtn ? VISIBLE : GONE);
    }

    private void setKeyboardVisibility(boolean showKeyboard) {
        final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        if(showKeyboard) {
            mEtFeedback.requestFocus();
            mEtFeedback.setHorizontallyScrolling(false);
            mEtFeedback.setMaxLines(2);
            imm.showSoftInput(mEtFeedback, InputMethodManager.SHOW_IMPLICIT);
        } else {
            mEtFeedback.clearFocus();
            imm.hideSoftInputFromWindow(mEtFeedback.getWindowToken(), 0);
        }
    }

    private void setupState(String email, int surveyState, int selectedScore) {
        updateState(surveyState);

        mCurrentScore = selectedScore;
        mCurrentEmail = email;
        if(mCurrentScore != Constants.NOT_SET) {
            mScoreViews[mCurrentScore].setSelected(true);
        }
    }

    @Override
    public int getSelectedScore() {
        return mCurrentScore;
    }

    @Override
    public String getFeedback() {
        return mEtFeedback.getText().toString();
    }

    @Override
    public String getEmail() { return mCurrentEmail; }

    @Override
    public void showThankYouLayout() {
        updateState(STATE_THANK_YOU);
    }

    @Override
    public void onScoreClick(int scoreValue) {
         if(mCurrentScore != -1) {
            mScoreViews[mCurrentScore].setSelected(false);
        }

        mCurrentScore = scoreValue;

        Score score = new Score(mCurrentScore, mSettings.getSurveyType(), mSettings.getSurveyTypeScale());
        boolean shouldSkipFeedbackScreen = mSettings.skipFeedbackScreen() ||
                (score.isPromoter() && mSettings.shouldSkipFollowupScreenForPromoters());

        if(mCurrentScore != Constants.NOT_SET) {
            if(shouldSkipFeedbackScreen) {
                updateState(STATE_THANK_YOU);
            } else {
                updateState(STATE_FEEDBACK);
            }
        } else {
            setFeedbackTexts();
            mTvFollowupQuestion.setAlpha(0);
            fadeInView(mTvFollowupQuestion);
        }

        notifyListener();
    }

    private void notifyListener() {
        if(mSurveyLayoutListener == null)
            return;

        String text = mEtFeedback.getText().toString();

        try {
            JSONObject dpl = mSettings.getDriverPicklist(mCurrentScore);
            if (dpl != null) {
                Iterator<String> keys = dpl.keys();
                while(keys.hasNext()) {
                    String key = keys.next();
                    if (mDriverPicklist.selectedButtons().contains(dpl.getString(key))) {
                        selectedAnswers.put(key, dpl.getString(key));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSurveyLayoutListener.onSurveySubmit(mCurrentScore, text, selectedAnswers);
    }

    private void submitSurvey() {
        notifyListener();
        showThankYouLayout();
    }

    private void dismissSurvey() {
        setKeyboardVisibility(false);
        if(mSurveyLayoutListener != null) {
            mSurveyLayoutListener.onDismissClick();
        }
    }

    @Override
    public void onFacebookLikeBtnClick() {
        if(mSurveyLayoutListener != null) {
            mSurveyLayoutListener.onFacebookLikeBtnClick();
        }
    }

    @Override
    public void onFacebookBtnClick() {
        if(mSurveyLayoutListener != null) {
            mSurveyLayoutListener.onFacebookBtnClick();
        }
    }

    @Override
    public void onTwitterBtnClick() {
        if(mSurveyLayoutListener != null) {
            mSurveyLayoutListener.onTwitterBtnClick();
        }
    }

    @Override
    public void onThankYouActionClick() {
        if(mSurveyLayoutListener != null) {
            mSurveyLayoutListener.onThankYouActionClick();
        }
    }

    @Override
    public void onShouldShowSimpleDialog() {
        // Empty
    }

    @Override
    public void onDismissClick() {
        dismissSurvey();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        SurveyLayoutSavedState savedState = new SurveyLayoutSavedState(superState);
        savedState.setCurrentState(mCurrentState);
        savedState.setCurrentScore(mCurrentScore);
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SurveyLayoutSavedState savedState = (SurveyLayoutSavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        setupState(savedState.getCurrentEmail(), savedState.getCurrentState(), savedState.getCurrentScore());
    }

    private static class SurveyLayoutSavedState extends View.BaseSavedState {
        private int currentState;
        private int currentScore;
        private String currentEmail;

        public SurveyLayoutSavedState(Parcelable superState) {
            super(superState);
        }

        public SurveyLayoutSavedState(Parcel source) {
            super(source);
            currentEmail = source.readString();
            currentState = source.readInt();
            currentScore = source.readInt();
        }

        public String getCurrentEmail() {
            return currentEmail;
        }

        public void setCurrentEmail(String currentEmail) {
            this.currentEmail = currentEmail;
        }

        public int getCurrentState() {
            return currentState;
        }

        public void setCurrentState(int currentState) {
            this.currentState = currentState;
        }

        public int getCurrentScore() {
            return currentScore;
        }

        public void setCurrentScore(int currentScore) {
            this.currentScore = currentScore;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(currentState);
            out.writeInt(currentScore);
        }

        public static final Parcelable.Creator<SurveyLayoutSavedState> CREATOR =
                new Parcelable.Creator<SurveyLayoutSavedState>() {
                    @Override
                    public SurveyLayoutSavedState createFromParcel(Parcel source) {
                        return new SurveyLayoutSavedState(source);
                    }

                    @Override
                    public SurveyLayoutSavedState[] newArray(int size) {
                        return new SurveyLayoutSavedState[size];
                    }
                };
    }
}
