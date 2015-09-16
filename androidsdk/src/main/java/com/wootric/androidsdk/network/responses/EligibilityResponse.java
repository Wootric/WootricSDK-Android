package com.wootric.androidsdk.network.responses;

import com.wootric.androidsdk.objects.Settings;

/**
 * Created by maciejwitowski on 9/14/15.
 */
public class EligibilityResponse {

    private final boolean eligible;
    private final Settings settings;

    public EligibilityResponse(boolean eligible, Settings settings) {
        this.eligible = eligible;
        this.settings = settings;
    }

    public boolean isEligible() {
        return eligible;
    }

    public Settings getSettings() {
        return settings;
    }
}
