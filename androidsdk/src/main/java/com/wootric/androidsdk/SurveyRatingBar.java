package com.wootric.androidsdk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maciejwitowski on 4/16/15.
 */
public class SurveyRatingBar extends LinearLayout {

    private final int mGradesCount = 10;
    private final List<GradeView> mGradesViews = new ArrayList<>();
    private GradeView mCurrentGrade;

    private boolean mActive;
    private int mBackgroundRes = R.drawable.survey_grades_disabled;

    private OnGradeSelectedListener mOnGradeSelectedListener;

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
            GradeView grade = GradeView.createDisabled(getContext());

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

            mGradesViews.add(grade);
            addView(grade);
        }
    }

    private GradeView getGradeViewForPosition(float x) {
        for(GradeView grade : mGradesViews) {
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

    private void setSelectedGrade(GradeView grade) {
        if(mCurrentGrade != null) {
            mCurrentGrade.activate(true);
        }

        mCurrentGrade = grade;
        mCurrentGrade.select();

        if(mOnGradeSelectedListener != null) {
            mOnGradeSelectedListener.onGradeSelected(grade);
        }
    }

    private void setActiveGrades() {
        for(GradeView grade : mGradesViews) {
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
                GradeView selectedGrade = getGradeViewForPosition(event.getX());

                if(selectedGrade != null && selectedGrade != mCurrentGrade) {
                    setSelectedGrade(selectedGrade);
                }
        }
        return true;
    }

    public void setOnGradeSelectedListener(OnGradeSelectedListener onGradeSelectedListener) {
        mOnGradeSelectedListener = onGradeSelectedListener;
    }

    public interface OnGradeSelectedListener {
        void onGradeSelected(GradeView view);
    }
}
