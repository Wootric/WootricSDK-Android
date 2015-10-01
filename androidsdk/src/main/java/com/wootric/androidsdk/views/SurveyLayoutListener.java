package com.wootric.androidsdk.views;

/**
 * Created by maciejwitowski on 9/30/15.
 */
interface SurveyLayoutListener extends ThankYouLayoutListener {

    void onSurveySubmit(int score, String text);

    void onSurveyFinished();
}