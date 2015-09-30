package com.wootric.androidsdk.views;

import com.wootric.androidsdk.objects.Settings;

/**
 * Created by maciejwitowski on 9/30/15.
 */
public interface SurveyLayout {

    void setSurveyLayoutListener(SurveyLayoutListener surveyLayoutListener);

    void initWithSettings(Settings settings);
    void setupState(int surveyState, int selectedScore);

    int getSelectedScore();
    int getSelectedState();
    String getFeedback();

    void showThankYouLayout();
}
