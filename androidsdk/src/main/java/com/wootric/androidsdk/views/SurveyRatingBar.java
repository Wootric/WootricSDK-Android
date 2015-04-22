package com.wootric.androidsdk.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.wootric.androidsdk.R;

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
    private int mBackgroundRes = R.drawable.survey_grades_disabled;

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
        setBackground();

        initDisabledGrades();
    }

    private void setBackground() {
        setBackground(ResourcesCompat.getDrawable(getResources(), mBackgroundRes, null));
    }

    private void initDisabledGrades() {
        for(int i = 0; i <= mGradesCount; i++) {
            ScoreView grade = ScoreView.createDisabled(getContext());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);


            if(i == 0) {
                params.setMargins(24, 0,0,0);
            }

            if(i == mGradesCount) {
                params.setMargins(0,0,24,0);
            }

            grade.setText(String.valueOf(i));
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

        mBackgroundRes = R.drawable.survey_grades;
        setBackground();
        setActiveGrades();
    }

    public void setSelectedGrade(int selectedGrade) {
        if(mCurrentScore != null) {
            mCurrentScore.activate(true);
        }

        mCurrentScore = mScoreViews.get(selectedGrade);
        mCurrentScore.select();

        if(mCallbacks != null) {
            mCallbacks.onScoreSelected(selectedGrade);
        }
    }

    private void setActiveGrades() {
        for(ScoreView grade : mScoreViews) {
            grade.activate(false);
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

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                ScoreView selectedGrade = getGradeViewForPosition(event.getX());

                if(selectedGrade != null && selectedGrade != mCurrentScore) {
                    setSelectedGrade(mScoreViews.indexOf(selectedGrade));
                }
        }
        return true;
    }

    public void setOnGradeSelectedListener(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    public interface Callbacks {
        void onScoreSelected(int gradeValue);
    }
}
