package com.wootric.androidsdk.views.phone;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

    private RelativeLayout mLayoutBody;
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

    private int mScoresCount;

    private static final int STATE_NPS = 0;
    private static final int STATE_FEEDBACK = 1;
    private static final int STATE_THANK_YOU = 2;

    private int mCurrentState = STATE_NPS;

    private View[] mCommonSurveyViews;
    private View[] mNpsViews;
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

        initResources();
        LayoutInflater.from(mContext).inflate(R.layout.wootric_survey_layout, this);

        initNpsViewElements();
        initFeedbackViewElements();

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

    private void initResources() {
        Resources res = mContext.getResources();
        mColorNotSelected = res.getColor(R.color.wootric_dark_gray);
        mColorSelected = res.getColor(R.color.wootric_score_color);
        mColorBlack = res.getColor(android.R.color.black);
        mColorEnabled = res.getColor(R.color.wootric_survey_layout_header_background);

        mScoreTextSizeSelected = res.getDimension(R.dimen.wootric_selected_score_text_size);
        mScoreTextSizeNotSelected = res.getDimension(R.dimen.wootric_not_selected_score_text_size);

        mScoresCount = res.getInteger(R.integer.wootric_max_score) + 1;
    }

    private void initNpsViewElements() {
        mLayoutBody = (RelativeLayout) findViewById(R.id.wootric_survey_layout_body);
        mScoreLayout = (LinearLayout) mLayoutBody.findViewById(R.id.wootric_score_layout);
        mAnchorLikely = (TextView) mLayoutBody.findViewById(R.id.wootric_anchor_likely);
        mAnchorNotLikely = (TextView) mLayoutBody.findViewById(R.id.wootric_anchor_not_likely);
        mRatingBar = (RatingBar) mLayoutBody.findViewById(R.id.wootric_rating_bar);

        mNpsViews = new View[]{ mRatingBar, mScoreLayout, mAnchorLikely, mAnchorNotLikely };

        initScoreLayout();

        mRatingBar.setOnScoreChangedListener(this);
    }

    private void initFeedbackViewElements() {
        mEtFeedback = (EditText) mLayoutBody.findViewById(R.id.wootric_et_feedback);
        Drawable etFeedbackBackground = mEtFeedback.getBackground();
        etFeedbackBackground.setColorFilter(mColorBlack, PorterDuff.Mode.SRC_ATOP);
        etFeedbackBackground.setAlpha(26);
        mEtFeedback.setOnFocusChangeListener(onEtFeedbackFocusChanged());
        mEtFeedback.addTextChangedListener(etFeedbackTextWatcher());

        mBtnEditScore = (TextView) mLayoutBody.findViewById(R.id.wootric_btn_edit_score);
        mBtnEditScore.setOnClickListener(onEditScoreClick());

        mFeedbackViews = new View[] {mBtnEditScore, mEtFeedback };
    }

    private void initScoreLayout() {
        mScoreViews = new TextView[mScoresCount];
        for(int score = 0; score < mScoreViews.length; score++) {
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

    private TextWatcher etFeedbackTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isFeedbackState()) {
                    final boolean hasFeedback = !mEtFeedback.getText().toString().isEmpty();
                    updateSubmitBtn(hasFeedback);
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
        boolean selectAnchorNotLikely = (newScore == 0);
        mAnchorNotLikely.setTextColor(selectAnchorNotLikely ? mColorSelected : Color.BLACK);
        mAnchorNotLikely.setAlpha(selectAnchorNotLikely ? 1f : 0.38f);

        boolean selectAnchorLikely = (newScore == 10);
        mAnchorLikely.setTextColor(selectAnchorLikely ? mColorSelected : Color.BLACK);
        mAnchorLikely.setAlpha(selectAnchorLikely ? 1f : 0.38f);
    }

    private void submitSurvey() {
        notifyListener();

        Score score = new Score(mRatingBar.getSelectedScore());
        boolean shouldSkipFeedbackScreen = score.isPromoter() &&
                mSettings.shouldSkipFollowupScreenForPromoters();


        if(isFeedbackState() || shouldSkipFeedbackScreen) {
            updateState(STATE_THANK_YOU);
        } else if(isNpsState()) {
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
                updateState(STATE_NPS);
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
        setTexts();
        setColors();
        updateState(mCurrentState);
    }

    private void setTexts() {
        mAnchorLikely.setText(mSettings.getAnchorLikely());
        mAnchorNotLikely.setText(mSettings.getAnchorNotLikely());
        mBtnSubmit.setText(mSettings.getBtnSubmit());
        mBtnDismiss.setText(mSettings.getBtnDismiss());
        mEtFeedback.setHint(mSettings.getFollowupPlaceholder(mRatingBar.getSelectedScore()));
    }

    private void setColors() {
        Resources res = mContext.getResources();
        mColorSelected = res.getColor(mSettings.getScoreColor());
        mColorEnabled = res.getColor(mSettings.getSurveyColor());
        mRatingBar.setSelectedColor(mColorSelected);

        mBtnEditScore.setTextColor(mColorEnabled);
        mBtnDismiss.setTextColor(mColorEnabled);

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

        if(STATE_NPS == mCurrentState) {
            setupNpsState();
        } else if(STATE_FEEDBACK == mCurrentState) {
            setupFeedbackState();
        } else {
            setupThankYouState();
        }
    }

    private void setupNpsState() {
        setViewsVisibility(mCommonSurveyViews, true);
        setViewsVisibility(mFeedbackViews, false);
        setViewsVisibility(mNpsViews, true);

        mTvSurveyHeader.setText(mSettings.getNpsQuestion());
        mThankYouLayout.setVisibility(GONE);
        setKeyboardVisibility(false);

        final boolean isScoreSelected = mRatingBar.isScoreSelected();
        updateSubmitBtn(isScoreSelected);
    }

    private void setupFeedbackState() {
        setViewsVisibility(mCommonSurveyViews, true);
        setViewsVisibility(mFeedbackViews, true);
        setViewsVisibility(mNpsViews, false);

        int currentScore = mRatingBar.getSelectedScore();
        mTvSurveyHeader.setText(mSettings.getFollowupQuestion(currentScore));
        mEtFeedback.setHint(mSettings.getFollowupPlaceholder(currentScore));

        mThankYouLayout.setVisibility(GONE);
        setKeyboardVisibility(true);

        final boolean hasFeedback = !mEtFeedback.getText().toString().isEmpty();
        updateSubmitBtn(hasFeedback);
    }

    private void setupThankYouState() {
        setViewsVisibility(mCommonSurveyViews, false);
        setViewsVisibility(mFeedbackViews, false);
        setViewsVisibility(mNpsViews, false);

        mThankYouLayout.setVisibility(VISIBLE);
        mThankYouLayout.initValues(mSettings, mRatingBar.getSelectedScore(), getFeedback());
    }

    private void updateSubmitBtn(boolean enable) {
        mBtnSubmit.setTextColor(enable ? mColorEnabled : mColorBlack);
        mBtnSubmit.setAlpha(enable ? 1f : 0.26f);
        mBtnSubmit.setEnabled(enable);
    }

    private boolean isNpsState() {
        return mCurrentState == STATE_NPS;
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
            imm.hideSoftInputFromWindow(mEtFeedback.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
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
