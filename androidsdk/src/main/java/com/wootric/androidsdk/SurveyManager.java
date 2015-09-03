package com.wootric.androidsdk;

import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.tasks.GetTrackingPixelTask;
import com.wootric.androidsdk.utils.ConnectionUtils;
import com.wootric.androidsdk.utils.PreferencesUtils;

/**
 * Created by maciejwitowski on 9/3/15.
 */
public class SurveyManager implements SurveyValidator.OnSurveyValidatedListener {
    private final Wootric wootric;
    private final SurveyValidator surveyValidator;
    private final ConnectionUtils connectionUtils;
    private final PreferencesUtils preferencesUtils;

    SurveyManager(Wootric wootric, ConnectionUtils connectionUtils, PreferencesUtils preferencesUtils, SurveyValidator surveyValidator) {
        this.wootric = wootric;
        this.connectionUtils = connectionUtils;
        this.preferencesUtils = preferencesUtils;
        this.surveyValidator = surveyValidator;
    }

    void start() {
        sendGetTrackingPixelRequest();
        preferencesUtils.touchLastSeen();

        validateSurvey();
    }

    private void sendGetTrackingPixelRequest() {
        new GetTrackingPixelTask(
                wootric.getUser(),
                wootric.getEndUser(),
                wootric.getOriginUrl()
        ).execute();
    }

    void validateSurvey() {
        surveyValidator.setOnSurveyValidatedListener(this);
        surveyValidator.validate();
    }

    @Override
    public void onSurveyValidated(Settings settings) {

    }
}
