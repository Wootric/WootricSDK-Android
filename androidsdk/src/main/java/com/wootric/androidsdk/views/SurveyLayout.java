package com.wootric.androidsdk.views;

import com.wootric.androidsdk.objects.Settings;

/**
 * Created by maciejwitowski on 9/30/15.
 */
public interface SurveyLayout {

    void setSurveyLayoutListener(SurveyLayoutListener surveyLayoutListener);

    void initWithSettings(Settings settings);
    int getSelectedScore();
    String getFeedback();

    void showThankYouLayout();
}
