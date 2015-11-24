package com.wootric.androidsdk.objects;

/**
 * Created by maciejwitowski on 11/24/15.
 */
public final class Score {
    private final int score;

    public Score(int score) {
        this.score = score;
    }

    public boolean isDetractor() {
        return score <= 6;
    }

    public boolean isPassive() {
        return score == 7 || score == 8;
    }

    public boolean isPromoter() {
        return score == 9 || score == 10;
    }
}
