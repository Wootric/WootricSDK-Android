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
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wootric.androidsdk.R;
import com.wootric.androidsdk.objects.Score;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.utils.ScreenUtils;
import com.wootric.androidsdk.views.SurveyLayout;
import com.wootric.androidsdk.views.SurveyLayoutListener;
import com.wootric.androidsdk.views.ThankYouLayoutListener;

import java.lang.reflect.Field;

import static com.wootric.androidsdk.utils.ScreenUtils.setViewsVisibility;

/**
 * Created by maciejwitowski on 9/21/15.
 */
public class SurveyLayoutPhone extends LinearLayout
        implements SurveyLayout, OnScoreChangedListener, ThankYouLayoutListener {

    private Context mContext;

    private ConstraintLayout mLayoutBody;
    private TextView mTvSurveyHeader;

    private RatingBar mRatingBar;
    private LinearLayout mScoreLayout;
    private TextView[] mScoreViews;
    private TextView mAnchorNotLikely;
    private TextView mAnchorLikely;
    private TextView mBtnSubmit;
    private TextView mBtnDismiss;
    private TextView mBtnEditScore;

    private EditText mEtFeedback;

    private ThankYouLayout mThankYouLayout;

    private float mScoreTextSizeSelected;
    private float mScoreTextSizeNotSelected;

    private int mColorSelected;
    private int mColorNotSelected;
    private int mColorBlack;
    private int mColorEnabled;

    private String mSurveyType;
    private int mScaleMinimum;
    private int mScaleMaximum;
    private int mScoresCount;

    private static final int STATE_SURVEY = 0;
    private static final int STATE_FEEDBACK = 1;
    private static final int STATE_THANK_YOU = 2;

    private int mCurrentState = STATE_SURVEY;

    private View[] mCommonSurveyViews;
    private View[] mSurveyViews;
    private View[] mFeedbackViews;

    private SurveyLayoutListener mSurveyLayoutListener;

    private Settings mSettings;

    public SurveyLayoutPhone(Context context) {
        super(context);
        init(context);
    }

    public SurveyLayoutPhone(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SurveyLayoutPhone(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;

        LayoutInflater.from(mContext).inflate(R.layout.wootric_survey_layout, this);
    }

    private void initResources() {
        Resources res = mContext.getResources();
        mColorNotSelected = res.getColor(R.color.wootric_dark_gray);
        mColorSelected = res.getColor(R.color.wootric_score_color);
        mColorBlack = res.getColor(android.R.color.black);
        mColorEnabled = res.getColor(R.color.wootric_survey_layout_header_background);

        mScoreTextSizeSelected = res.getDimension(R.dimen.wootric_selected_score_text_size);
        mScoreTextSizeNotSelected = res.getDimension(R.dimen.wootric_not_selected_score_text_size);

        mScaleMinimum = mSurveyType == null ? 0 : Score.minimumScore(mSurveyType);
        mScaleMaximum = mSurveyType == null ? 10 : Score.maximumScore(mSurveyType);

        mScoresCount = mScaleMaximum + 1;
    }

    private void initViews() {
        mBtnSubmit = (TextView) mLayoutBody.findViewById(R.id.wootric_btn_submit);
        mBtnDismiss = (TextView) mLayoutBody.findViewById(R.id.wootric_btn_dismiss);
        mBtnSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                submitSurvey();
            }
        });
        mBtnDismiss.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissSurvey();
            }
        });

        mTvSurveyHeader = (TextView) findViewById(R.id.wootric_survey_layout_tv_header);

        mCommonSurveyViews = new View[]{ mLayoutBody, mTvSurveyHeader };

        mThankYouLayout = (ThankYouLayout) findViewById(R.id.wootric_thank_you_layout);
        mThankYouLayout.setThankYouLayoutListener(this);
    }

    private void initSurveyViewElements() {
        mLayoutBody = (ConstraintLayout) findViewById(R.id.wootric_survey_layout_body);
        mScoreLayout = (LinearLayout) mLayoutBody.findViewById(R.id.wootric_score_layout);
        mAnchorLikely = (TextView) mLayoutBody.findViewById(R.id.wootric_anchor_likely);
        mAnchorNotLikely = (TextView) mLayoutBody.findViewById(R.id.wootric_anchor_not_likely);
        mRatingBar = (RatingBar) mLayoutBody.findViewById(R.id.wootric_rating_bar);

        mSurveyViews = new View[]{ mRatingBar, mScoreLayout, mAnchorLikely, mAnchorNotLikely };

        initScoreLayout();
        initViews();

        mRatingBar.setOnScoreChangedListener(this);
    }

    private void initFeedbackViewElements() {
        mEtFeedback = (EditText) mLayoutBody.findViewById(R.id.wootric_et_feedback);
        Drawable etFeedbackBackground = mEtFeedback.getBackground();
        etFeedbackBackground.setColorFilter(mColorBlack, PorterDuff.Mode.SRC_ATOP);
        etFeedbackBackground.setAlpha(26);
        mEtFeedback.setOnFocusChangeListener(onEtFeedbackFocusChanged());

        mBtnEditScore = (TextView) findViewById(R.id.wootric_btn_edit_score);
        mBtnEditScore.setOnClickListener(onEditScoreClick());

        mFeedbackViews = new View[] {mBtnEditScore, mEtFeedback };
    }

    private void initScoreLayout() {
        mScoreViews = new TextView[mScoresCount];
        for(int score = mScaleMinimum; score < mScoreViews.length; score++) {
            TextView scoreView = buildScoreView(score);
            mScoreViews[score] = scoreView;
            mScoreLayout.addView(scoreView);
        }
    }

    private TextView buildScoreView(int score) {
        TextView scoreView = new TextView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        scoreView.setLayoutParams(params);

        scoreView.setGravity(Gravity.CENTER);
        scoreView.setText(String.valueOf(score));
        scoreView.setTextSize(ScreenUtils.pxToDp(mScoreTextSizeNotSelected));
        scoreView.setTextColor(mColorNotSelected);

        return scoreView;
    }

    private OnFocusChangeListener onEtFeedbackFocusChanged() {
        return new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    Drawable etFeedbackBackground = mEtFeedback.getBackground();
                    etFeedbackBackground.setColorFilter(mColorSelected, PorterDuff.Mode.SRC_ATOP);
                    etFeedbackBackground.setAlpha(255);
                }
            }
        };
    }

    @Override
    public void onScoreChanged(int oldScore, int newScore) {
        updateSelectedScore(oldScore, newScore);
        updateSubmitBtn(true);
        updateAnchors(newScore);
    }

    private void updateSelectedScore(int oldScore, int newScore) {
        if(oldScore != RatingBar.SCORE_NOT_SET) {
            TextView oldScoreView = mScoreViews[oldScore];
            oldScoreView.setTextColor(mColorNotSelected);
            oldScoreView.setTextSize(ScreenUtils.pxToDp(mScoreTextSizeNotSelected));
        }

        TextView newScoreView = mScoreViews[newScore];
        newScoreView.setTextColor(mColorSelected);
        newScoreView.setTextSize(ScreenUtils.pxToDp(mScoreTextSizeSelected));
    }

    private void updateAnchors(int newScore) {
        boolean selectAnchorNotLikely = (newScore == mScaleMinimum);
        mAnchorNotLikely.setTextColor(selectAnchorNotLikely ? mColorSelected : Color.BLACK);
        mAnchorNotLikely.setAlpha(selectAnchorNotLikely ? 1f : 0.38f);

        boolean selectAnchorLikely = (newScore == mScaleMaximum);
        mAnchorLikely.setTextColor(selectAnchorLikely ? mColorSelected : Color.BLACK);
        mAnchorLikely.setAlpha(selectAnchorLikely ? 1f : 0.38f);
    }

    private void submitSurvey() {
        notifyListener();

        Score score = new Score(mRatingBar.getSelectedScore(), mSettings.getSurveyType());
        boolean shouldSkipFeedbackScreen = score.isPromoter() &&
                mSettings.shouldSkipFollowupScreenForPromoters();


        if(isFeedbackState() || shouldSkipFeedbackScreen) {
            updateState(STATE_THANK_YOU);
        } else if(isSurveyState()) {
            updateState(STATE_FEEDBACK);
        }
    }

    private void notifyListener() {
        if(mSurveyLayoutListener == null)
            return;

        String text = mEtFeedback.getText().toString();
        mSurveyLayoutListener.onSurveySubmit(mRatingBar.getSelectedScore(), text);
    }

    private void dismissSurvey() {
        if(mSurveyLayoutListener != null) {
            mSurveyLayoutListener.onDismissClick();
        }
    }

    private OnClickListener onEditScoreClick() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateState(STATE_SURVEY);
            }
        };
    }

    public void setSurveyLayoutListener(SurveyLayoutListener surveyLayoutListener) {
        mSurveyLayoutListener = surveyLayoutListener;
    }

    public int getSelectedScore() {
        return mRatingBar.getSelectedScore();
    }

    @Override
    public void initWithSettings(Settings settings) {
        mSettings = settings;
        mSurveyType = mSettings.getSurveyType();

        initResources();
        initSurveyViewElements();
        initFeedbackViewElements();

        setTexts();
        setColors();
        updateState(mCurrentState);
        mRatingBar.setScale(mSurveyType);
    }

    private void setTexts() {
        mAnchorLikely.setText(mSettings.getAnchorLikely());
        mAnchorNotLikely.setText(mSettings.getAnchorNotLikely());
        mBtnSubmit.setText(mSettings.getBtnSubmit());
        mBtnDismiss.setText(mSettings.getBtnDismiss());
        mBtnEditScore.setText(mSettings.getBtnEditScore());
        mEtFeedback.setHint(mSettings.getFollowupPlaceholder(mRatingBar.getSelectedScore()));
    }

    private void setColors() {
        Resources res = mContext.getResources();
        mColorSelected = res.getColor(mSettings.getScoreColor());
        mColorEnabled = res.getColor(mSettings.getSurveyColor());
        mRatingBar.setSelectedColor(mColorSelected);

        mBtnDismiss.setTextColor(mColorEnabled);

        mBtnEditScore.setBackgroundColor(mColorEnabled);
        mTvSurveyHeader.setBackgroundColor(mColorEnabled);

        setCursorDrawableColor(mEtFeedback, mColorSelected);
    }

    private static void setCursorDrawableColor(EditText editText, int color) {
        try {
            Field fCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            fCursorDrawableRes.setAccessible(true);
            int mCursorDrawableRes = fCursorDrawableRes.getInt(editText);
            Field fEditor = TextView.class.getDeclaredField("mEditor");
            fEditor.setAccessible(true);
            Object editor = fEditor.get(editText);
            Class<?> clazz = editor.getClass();
            Field fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
            fCursorDrawable.setAccessible(true);

            Drawable[] drawables = new Drawable[2];
            Resources res = editText.getContext().getResources();
            drawables[0] = res.getDrawable(mCursorDrawableRes);
            drawables[1] = res.getDrawable(mCursorDrawableRes);
            drawables[0].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            drawables[1].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            fCursorDrawable.set(editor, drawables);
        } catch (final Throwable t) {
            Log.e("Wootric-SDK", t.toString());
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
        setViewsVisibility(mCommonSurveyViews, true);
        setViewsVisibility(mFeedbackViews, false);
        setViewsVisibility(mSurveyViews, true);

        mTvSurveyHeader.setText(mSettings.getSurveyQuestion());
        mThankYouLayout.setVisibility(GONE);
        setKeyboardVisibility(false);


        final boolean isScoreSelected = mRatingBar.isScoreSelected();
        updateSubmitBtn(isScoreSelected);
    }

    private void setupFeedbackState() {
        setViewsVisibility(mCommonSurveyViews, true);
        setViewsVisibility(mFeedbackViews, true);
        setViewsVisibility(mSurveyViews, false);

        int currentScore = mRatingBar.getSelectedScore();
        mTvSurveyHeader.setText(mSettings.getFollowupQuestion(currentScore));
        mEtFeedback.setHint(mSettings.getFollowupPlaceholder(currentScore));

        mThankYouLayout.setVisibility(GONE);
        setKeyboardVisibility(true);
    }

    private void setupThankYouState() {
        setViewsVisibility(mCommonSurveyViews, false);
        setViewsVisibility(mFeedbackViews, false);
        setViewsVisibility(mSurveyViews, false);
        setKeyboardVisibility(false);

        mThankYouLayout.setVisibility(VISIBLE);
        mThankYouLayout.initValues(mSettings, mRatingBar.getSelectedScore(), getFeedback());
    }

    private void updateSubmitBtn(boolean enable) {
        mBtnSubmit.setTextColor(enable ? mColorEnabled : mColorBlack);
        mBtnSubmit.setAlpha(enable ? 1f : 0.26f);
        mBtnSubmit.setEnabled(enable);
    }

    private boolean isSurveyState() {
        return mCurrentState == STATE_SURVEY;
    }

    private boolean isFeedbackState() {
        return mCurrentState == STATE_FEEDBACK;
    }

    public String getFeedback() {
        return mEtFeedback.getText().toString();
    }

    private void setKeyboardVisibility(boolean showKeyboard) {
        final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        if(showKeyboard) {
            mEtFeedback.requestFocus();
            mEtFeedback.setHorizontallyScrolling(false);

            imm.showSoftInput(mEtFeedback, InputMethodManager.SHOW_IMPLICIT);
        } else {
            mEtFeedback.clearFocus();
            imm.hideSoftInputFromWindow(mEtFeedback.getWindowToken(), 0);
        }
    }

    public void showThankYouLayout() {
        updateState(STATE_THANK_YOU);
    }

    @Override
    public void onFacebookLikeBtnClick() { mSurveyLayoutListener.onFacebookLikeBtnClick(); }

    @Override
    public void onFacebookBtnClick() { mSurveyLayoutListener.onFacebookBtnClick(); }

    @Override
    public void onTwitterBtnClick() {
        mSurveyLayoutListener.onTwitterBtnClick();
    }

    @Override
    public void onThankYouActionClick() {
        mSurveyLayoutListener.onThankYouActionClick();
    }

    @Override
    public void onShouldShowSimpleDialog() {
        mSurveyLayoutListener.onShouldShowSimpleDialog();
    }

    @Override
    public void onDismissClick() {
        mSurveyLayoutListener.onDismissClick();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        SurveyLayoutSavedState savedState = new SurveyLayoutSavedState(superState);
        savedState.setCurrentState(mCurrentState);
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SurveyLayoutSavedState savedState = (SurveyLayoutSavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        updateState(savedState.getCurrentState());
    }

    private static class SurveyLayoutSavedState extends View.BaseSavedState {
        private int currentState;

        public SurveyLayoutSavedState(Parcelable superState) {
            super(superState);
        }

        public SurveyLayoutSavedState(Parcel source) {
            super(source);
            currentState = source.readInt();
        }

        public int getCurrentState() {
            return currentState;
        }

        public void setCurrentState(int currentState) {
            this.currentState = currentState;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(currentState);
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
