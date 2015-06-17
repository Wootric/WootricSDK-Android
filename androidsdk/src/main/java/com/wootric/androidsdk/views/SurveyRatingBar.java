package com.wootric.androidsdk.views;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.wootric.androidsdk.R;
import com.wootric.androidsdk.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maciejwitowski on 4/16/15.
 */
public class SurveyRatingBar extends LinearLayout {

    private final int mGradesCount = 10;
    private final List<ScoreView> mScoreViews = new ArrayList<>();
    private ScoreView mCurrentScore;

    private boolean mActive;

    private Callbacks mCallbacks;

    public SurveyRatingBar(Context context) {
        super(context);
        init();
    }

    public SurveyRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SurveyRatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setupBackground();
        initDisabledGrades();
    }

    private void setupBackground() {
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);

                GradientDrawable background = new GradientDrawable();
                background.setCornerRadius(getHeight() / 2);

                if (mActive) {
                    background.setColor(getResources().getColor(R.color.gray));
                } else {
                    background.setColor(getResources().getColor(R.color.white));
                    background.setStroke(ScreenUtils.dpToPx(4), getResources().getColor(R.color.gray));
                }

                setBackground(background);

                return true;
            }
        });
    }

    private void initDisabledGrades() {
        for(int i = 0; i <= mGradesCount; i++) {
            ScoreView grade = ScoreView.createDisabled(getContext());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);

            grade.setText(String.valueOf(i));
            grade.setTextSize(16f);
            grade.setId(i);
            grade.setLayoutParams(params);

            mScoreViews.add(grade);
            addView(grade);
        }
    }

    private ScoreView getGradeViewForPosition(float x) {
        for(ScoreView grade : mScoreViews) {
            boolean selected = (grade.getLeft() <= x && grade.getRight() >= x);
            if(selected) {
                return grade;
            }
        }

        return null;
    }

    private void activate() {
        mActive = true;
        setupBackground();
        setActiveGrades();
    }

    public void setSelectedGrade(int selectedGrade) {
        if(selectedGrade == -1) {
            return;
        }

        if(!mActive) {
            activate();
        }

        if(mCurrentScore != null) {
            mCurrentScore.select(false);
        }

        mCurrentScore = mScoreViews.get(selectedGrade);
        mCurrentScore.select(true);
    }

    private void setActiveGrades() {
        for(ScoreView grade : mScoreViews) {
            grade.select(false);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if(!mActive) {
            activate();
        }

        int action = event.getAction();

        if(action == MotionEvent.ACTION_UP && mCallbacks != null) {
            mCallbacks.onScoreTouchReleased();
        } else if(action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE){
            ScoreView selectedGrade = getGradeViewForPosition(event.getX());

            if(selectedGrade != null && selectedGrade != mCurrentScore) {
                updateScore(action, selectedGrade);
            }
        }

        return true;
    }

    private void updateScore(int eventAction, ScoreView selectedGrade) {
        int gradeValue = mScoreViews.indexOf(selectedGrade);
        setSelectedGrade(gradeValue);

        if(mCallbacks == null) {
            return;
        }

        if(eventAction == MotionEvent.ACTION_DOWN) {
            mCallbacks.onScoreSelected(gradeValue);
        } else if(eventAction == MotionEvent.ACTION_MOVE) {
            int xCenter = selectedGrade.getLeft() + selectedGrade.getWidth()/2;
//            selectedGrade.getLocationOnScreen(location);
            mCallbacks.onScoreDragged(gradeValue, xCenter);
        }
    }

    public void setOnGradeSelectedListener(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    public interface Callbacks {
        void onScoreSelected(int gradeValue);
        void onScoreDragged(int gradeValue, int xCenter);
        void onScoreTouchReleased();
    }
}
