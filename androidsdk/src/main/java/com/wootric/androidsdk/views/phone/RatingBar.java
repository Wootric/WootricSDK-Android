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
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.wootric.androidsdk.R;
import com.wootric.androidsdk.objects.Score;

/**
 * Created by maciejwitowski on 9/7/15.
 */
public class RatingBar extends View implements View.OnTouchListener {

    private Context mContext;

    private String mSurveyType;
    private int mSurveyTypeScale;
    private int mScaleMinimum;
    private int mScaleMaximum;

    private int mWidthSize;
    private int mHeightSize;

    private int mColorNotSelected;
    private int mColorSelected;

    private float mNotchRadius;
    private float mNotchMarginHorizontal;
    private float mTrackWidth;
    private int mRatingBarPaddingVertical;

    private Paint mNotSelectedNotchPaint;
    private Paint mSelectedNotchPaint;
    private Paint mNotSelectedTrackPaint;
    private Paint mSelectedTrackPaint;

    private int mNotchCount;
    private int mNotchTop;
    private float[] mNotchesLeftXCoordinates;

    public static final int SCORE_NOT_SET = -1;
    private int mSelectedScore = SCORE_NOT_SET;

    private OnScoreChangedListener mOnScoreChangedListener;

    public RatingBar(Context context) {
        super(context);
        init(context);
    }

    public RatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;

        initResources();
        initPaints();

        setPadding(0, mRatingBarPaddingVertical, 0, mRatingBarPaddingVertical);

        setOnTouchListener(this);
    }

    private void initResources() {
        Resources res = mContext.getResources();
        Score score = new Score(-1, mSurveyType, mSurveyTypeScale);

        mScaleMinimum = mSurveyType == null ? 0 : score.minimumScore();
        mScaleMaximum = mSurveyType == null ? 10 : score.maximumScore();

        mNotchTop = mScaleMaximum + 1;
        mNotchCount = mNotchTop - mScaleMinimum;

        mColorNotSelected = res.getColor(R.color.wootric_dark_gray);
        mColorSelected = res.getColor(R.color.wootric_score_color);
        mNotchMarginHorizontal = res.getDimension(R.dimen.wootric_rating_notch_margin_horizontal);
        mNotchRadius = res.getDimension(R.dimen.wootric_rating_notch_radius);
        mTrackWidth = res.getDimension(R.dimen.wootric_rating_track_width);
        mRatingBarPaddingVertical = (int) res.getDimension(R.dimen.wootric_rating_bar_padding_vertical);

        mNotchesLeftXCoordinates = new float[mNotchTop];
    }

    private void initPaints() {
        mNotSelectedNotchPaint = new Paint() {
            {
                setStyle(Paint.Style.FILL);
                setColor(mColorNotSelected);
            }
        };

        mSelectedNotchPaint = new Paint() {
            {
                setStyle(Paint.Style.FILL);
                setColor(mColorSelected);
            }
        };

        mNotSelectedTrackPaint = new Paint() {
            {
                setStyle(Paint.Style.STROKE);
                setColor(mColorNotSelected);
                setStrokeWidth(mTrackWidth);
            }
        };

        mSelectedTrackPaint = new Paint() {
            {
                setStyle(Paint.Style.STROKE);
                setColor(mColorSelected);
                setStrokeWidth(mTrackWidth);
            }
        };
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        mHeightSize = (int) mNotchRadius * 2 + getPaddingBottom() + getPaddingTop();
        setMeasuredDimension(mWidthSize, mHeightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float totalNotchesLength = (mNotchCount + 1) * mNotchRadius * 2;
        float totalMarginHorizontal = mWidthSize - totalNotchesLength;
        mNotchMarginHorizontal = totalMarginHorizontal / (mNotchCount - 1);

        float dX = (mWidthSize - totalNotchesLength - totalMarginHorizontal) / 2.0f + mNotchRadius * 2;
        float dY = mHeightSize / 2;
        for (int i = mScaleMinimum; i < mNotchTop; i++) {
            initNotchCoordinates(i, dX);

            boolean markNotchSelected = (i <= mSelectedScore);

            float thisNotchRadius = mNotchRadius;
            if(i == mSelectedScore) {
                thisNotchRadius *= 1.5f;
            }

            canvas.drawCircle(dX, dY, thisNotchRadius , markNotchSelected ? mSelectedNotchPaint : mNotSelectedNotchPaint);

            float nextDx = dX + (2 * mNotchRadius + mNotchMarginHorizontal);
            if(i < mNotchTop - 1) {
                boolean markTrackSelected = (i < mSelectedScore);
                canvas.drawLine(dX + thisNotchRadius, dY, nextDx - mNotchRadius, dY, markTrackSelected ? mSelectedTrackPaint : mNotSelectedTrackPaint);
            }

            dX = nextDx;
        }
    }

    private void initNotchCoordinates(int index, float dX) {
        if(index == mScaleMinimum) {
            mNotchesLeftXCoordinates[index] = dX - mNotchRadius;
        } else {
            mNotchesLeftXCoordinates[index] = dX - mNotchMarginHorizontal / 2;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();

        if(action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE){
            int touchedScore = getTouchedScore(event.getX());
            setSelectedScore(touchedScore);
        }

        return true;
    }

    private int getTouchedScore(float touchedXCoordinate) {
        int lastNotchIndex = mNotchTop - 1;

        int touchedScore = SCORE_NOT_SET;

        if(touchedXCoordinate >= mNotchesLeftXCoordinates[lastNotchIndex]) {
            touchedScore = lastNotchIndex;
        } else {
            for(int i = mScaleMinimum; i < mNotchTop - 1; i++) {
                float thisX = mNotchesLeftXCoordinates[i];
                float nextX = mNotchesLeftXCoordinates[i + 1];

                boolean touchedThisNotchRange = (touchedXCoordinate >= thisX && touchedXCoordinate <= nextX);
                if (touchedThisNotchRange) {
                    touchedScore = i;
                    break;
                }
            }
        }

        return touchedScore;
    }

    public void setScale(String surveyType, int surveyTypeScale) {
        mSurveyType = surveyType;
        mSurveyTypeScale = surveyTypeScale;
        initResources();
    }

    public void setOnScoreChangedListener(OnScoreChangedListener onScoreChangedListener) {
        mOnScoreChangedListener = onScoreChangedListener;
    }

    public int getSelectedScore() {
        return mSelectedScore;
    }

    public boolean isScoreSelected() {
        return getSelectedScore() != SCORE_NOT_SET;
    }

    public void setSelectedScore(int selectedScore) {
        boolean scoreChanged = (selectedScore != SCORE_NOT_SET && selectedScore != mSelectedScore);

        if(scoreChanged) {
            if(mOnScoreChangedListener != null) {
                mOnScoreChangedListener.onScoreChanged(mSelectedScore, selectedScore);
            }

            mSelectedScore = selectedScore;
            invalidate();
        }
    }

    public void setSelectedColor(int selectedColor) {
        mColorSelected = selectedColor;
        initPaints();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        RatingBarSavedState savedState = new RatingBarSavedState(superState);
        savedState.selectedScore = mSelectedScore;
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        RatingBarSavedState ratingBarSavedState = (RatingBarSavedState) state;
        super.onRestoreInstanceState(ratingBarSavedState.getSuperState());
        setSelectedScore(ratingBarSavedState.selectedScore);
    }

    private static class RatingBarSavedState extends BaseSavedState {
        int selectedScore;

        public RatingBarSavedState(Parcelable superState) {
            super(superState);
        }

        public RatingBarSavedState(Parcel source) {
            super(source);
            selectedScore = source.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(selectedScore);
        }

        public static final Parcelable.Creator<RatingBarSavedState> CREATOR =
                new Parcelable.Creator<RatingBarSavedState>() {
                    @Override
                    public RatingBarSavedState createFromParcel(Parcel source) {
                        return new RatingBarSavedState(source);
                    }

                    @Override
                    public RatingBarSavedState[] newArray(int size) {
                        return new RatingBarSavedState[size];
                    }
                };
    }
}
