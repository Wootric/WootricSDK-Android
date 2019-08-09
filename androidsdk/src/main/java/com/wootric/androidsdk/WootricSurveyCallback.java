package com.wootric.androidsdk;

import java.util.HashMap;

public interface WootricSurveyCallback {
    void onSurveyWillShow();
    void onSurveyDidShow();
    void onSurveyWillHide();
    void onSurveyDidHide(HashMap data);
}
