package com.wootric.androidsdk.network.responses;

import com.wootric.androidsdk.objects.Settings;

/**
 * Created by maciejwitowski on 9/14/15.
 */
public class EligibilityResponse {

    boolean eligible;
    Settings settings;

    public boolean isEligible() {
        return eligible;
    }

    public Settings getSettings() {
        return settings;
    }
}
