package com.wootric.androidsdk.objects;

import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by diegoserranoa on 4/7/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class ScoreTest {

    @Test
    public void whenDetractorAndScaleNPS_itReturnsTrue() throws Exception {
        Score score = new Score(0, "NPS", 0);
        Score score2 = new Score(6, "NPS", 0);
        Score score3 = new Score(6, "NPS", 1);
        Score score4 = new Score(0, "NPS", -1);

        assertThat(score.isDetractor()).isTrue();
        assertThat(score2.isDetractor()).isTrue();
        assertThat(score3.isDetractor()).isTrue();
        assertThat(score4.isDetractor()).isTrue();
    }

    @Test
    public void whenNotDetractorAndScaleNPS_itReturnsFalse() throws Exception {
        Score score = new Score(-1, "NPS",0);
        Score score2 = new Score(7, "NPS",0);
        Score score3 = new Score(-1, "NPS",1);
        Score score4 = new Score(7, "NPS",1);

        assertThat(score.isDetractor()).isFalse();
        assertThat(score2.isDetractor()).isFalse();
        assertThat(score3.isDetractor()).isFalse();
        assertThat(score4.isDetractor()).isFalse();
    }

    @Test
    public void whenPassiveAndScaleNPS_itReturnsTrue() throws Exception {
        Score score = new Score(7, "NPS", 0);
        Score score2 = new Score(8, "NPS", 0);
        Score score3 = new Score(7, "NPS", 1);
        Score score4 = new Score(8, "NPS", 1);

        assertThat(score.isPassive()).isTrue();
        assertThat(score2.isPassive()).isTrue();
        assertThat(score3.isPassive()).isTrue();
        assertThat(score4.isPassive()).isTrue();
    }

    @Test
    public void whenNotPassiveAndScaleNPS_itReturnsFalse() throws Exception {
        Score score = new Score(6, "NPS", 0);
        Score score2 = new Score(9, "NPS", 0);
        Score score3 = new Score(6, "NPS", 1);
        Score score4 = new Score(9, "NPS", 1);

        assertThat(score.isPassive()).isFalse();
        assertThat(score2.isPassive()).isFalse();
        assertThat(score3.isPassive()).isFalse();
        assertThat(score4.isPassive()).isFalse();
    }

    @Test
    public void whenPromoterAndScaleNPS_itReturnsTrue() throws Exception {
        Score score = new Score(9, "NPS", 0);
        Score score2 = new Score(10, "NPS", 0);
        Score score3 = new Score(9, "NPS", 1);
        Score score4 = new Score(10, "NPS", 1);

        assertThat(score.isPromoter()).isTrue();
        assertThat(score2.isPromoter()).isTrue();
        assertThat(score3.isPromoter()).isTrue();
        assertThat(score4.isPromoter()).isTrue();
    }

    @Test
    public void whenNotPromoterAndScaleNPS_itReturnsTrue() throws Exception {
        Score score = new Score(8, "NPS", 0);
        Score score2 = new Score(11, "NPS", 0);
        Score score3 = new Score(8, "NPS", 1);
        Score score4 = new Score(11, "NPS", 1);

        assertThat(score.isPromoter()).isFalse();
        assertThat(score2.isPromoter()).isFalse();
        assertThat(score3.isPromoter()).isFalse();
        assertThat(score4.isPromoter()).isFalse();
    }

    @Test
    public void whenScaleNPS_itReturnsCorrectMin() throws Exception {
        Score score = new Score(8, "NPS", 0);
        Score score2 = new Score(8, "NPS", 1);
        Score score3 = new Score(8, "NPS", -1);
        assertThat(score.minimumScore()).isEqualTo(0);
        assertThat(score2.minimumScore()).isEqualTo(0);
        assertThat(score3.minimumScore()).isEqualTo(0);
    }

    @Test
    public void whenScaleNPS_itReturnsCorrectMax() throws Exception {
        Score score = new Score(8, "NPS", 0);
        Score score2 = new Score(8, "NPS", 1);
        Score score3 = new Score(8, "NPS", -1);
        assertThat(score.maximumScore()).isEqualTo(10);
        assertThat(score2.maximumScore()).isEqualTo(10);
        assertThat(score3.maximumScore()).isEqualTo(10);
    }

    @Test
    public void whenDetractorAndScaleCES_itReturnsTrue() throws Exception {
        Score score = new Score(1, "CES", 0);
        Score score2 = new Score(3, "CES", 0);
        Score score3 = new Score(1, "CES", 1);
        Score score4 = new Score(3, "CES", 1);

        assertThat(score.isDetractor()).isTrue();
        assertThat(score2.isDetractor()).isTrue();
        assertThat(score3.isDetractor()).isTrue();
        assertThat(score4.isDetractor()).isTrue();
    }

    @Test
    public void whenNotDetractorAndScaleCES_itReturnsFalse() throws Exception {
        Score score = new Score(0, "CES", 0);
        Score score2 = new Score(4, "CES", 0);
        Score score3 = new Score(0, "CES", 1);
        Score score4 = new Score(4, "CES", 1);

        assertThat(score.isDetractor()).isFalse();
        assertThat(score2.isDetractor()).isFalse();
        assertThat(score3.isDetractor()).isFalse();
        assertThat(score4.isDetractor()).isFalse();
    }

    @Test
    public void whenPassiveAndScaleCES_itReturnsTrue() throws Exception {
        Score score = new Score(4, "CES", 0);
        Score score2 = new Score(5, "CES", 0);
        Score score3 = new Score(4, "CES", 1);
        Score score4 = new Score(5, "CES", 1);

        assertThat(score.isPassive()).isTrue();
        assertThat(score2.isPassive()).isTrue();
        assertThat(score3.isPassive()).isTrue();
        assertThat(score4.isPassive()).isTrue();
    }

    @Test
    public void whenNotPassiveAndScaleCES_itReturnsFalse() throws Exception {
        Score score = new Score(3, "CES", 0);
        Score score2 = new Score(6, "CES", 0);
        Score score3 = new Score(3, "CES", 1);
        Score score4 = new Score(6, "CES", 1);

        assertThat(score.isPassive()).isFalse();
        assertThat(score2.isPassive()).isFalse();
        assertThat(score3.isPassive()).isFalse();
        assertThat(score4.isPassive()).isFalse();
    }

    @Test
    public void whenPromoterAndScaleCES_itReturnsTrue() throws Exception {
        Score score = new Score(6, "CES", 0);
        Score score2 = new Score(7, "CES", 0);
        Score score3 = new Score(6, "CES", 1);
        Score score4 = new Score(7, "CES", 1);

        assertThat(score.isPromoter()).isTrue();
        assertThat(score2.isPromoter()).isTrue();
        assertThat(score3.isPromoter()).isTrue();
        assertThat(score4.isPromoter()).isTrue();
    }

    @Test
    public void whenNotPromoterAndScaleCES_itReturnsTrue() throws Exception {
        Score score = new Score(5, "CES", 0);
        Score score2 = new Score(8, "CES", 0);
        Score score3 = new Score(5, "CES", 1);
        Score score4 = new Score(8, "CES", 1);

        assertThat(score.isPromoter()).isFalse();
        assertThat(score2.isPromoter()).isFalse();
        assertThat(score3.isPromoter()).isFalse();
        assertThat(score4.isPromoter()).isFalse();
    }

    @Test
    public void whenScaleCES_itReturnsCorrectMin() throws Exception {
        Score score = new Score(0, "CES", 0);
        Score score2 = new Score(0, "CES", 1);
        Score score3 = new Score(0, "CES", -1);
        assertThat(score.minimumScore()).isEqualTo(1);
        assertThat(score2.minimumScore()).isEqualTo(1);
        assertThat(score3.minimumScore()).isEqualTo(1);
    }

    @Test
    public void whenScaleCES_itReturnsCorrectMax() throws Exception {
        Score score = new Score(0, "CES", 0);
        Score score2 = new Score(0, "CES", 1);
        Score score3 = new Score(0, "CES", -1);
        assertThat(score.maximumScore()).isEqualTo(7);
        assertThat(score2.maximumScore()).isEqualTo(7);
        assertThat(score3.maximumScore()).isEqualTo(7);
    }

    @Test
    public void whenDetractorAndScaleCSAT_itReturnsTrue() throws Exception {
        Score score = new Score(1, "CSAT", 0);
        Score score2 = new Score(2, "CSAT", 0);
        Score score3= new Score(1, "CSAT", 1);
        Score score4 = new Score(6, "CSAT", 1);

        assertThat(score.isDetractor()).isTrue();
        assertThat(score2.isDetractor()).isTrue();
        assertThat(score3.isDetractor()).isTrue();
        assertThat(score4.isDetractor()).isTrue();
    }

    @Test
    public void whenNotDetractorAndScaleCSAT_itReturnsFalse() throws Exception {
        Score score = new Score(0, "CSAT", 0);
        Score score2 = new Score(3, "CSAT", 0);
        Score score3 = new Score(0, "CSAT", 1);
        Score score4 = new Score(7, "CSAT", 1);

        assertThat(score.isDetractor()).isFalse();
        assertThat(score2.isDetractor()).isFalse();
        assertThat(score3.isDetractor()).isFalse();
        assertThat(score4.isDetractor()).isFalse();
    }

    @Test
    public void whenPassiveAndScaleCSAT_itReturnsTrue() throws Exception {
        Score score = new Score(3, "CSAT", 0);
        Score score2 = new Score(7, "CSAT", 1);
        Score score3 = new Score(8, "CSAT", 1);

        assertThat(score.isPassive()).isTrue();
        assertThat(score2.isPassive()).isTrue();
        assertThat(score3.isPassive()).isTrue();
    }

    @Test
    public void whenNotPassiveAndScaleCSAT_itReturnsFalse() throws Exception {
        Score score = new Score(2, "CSAT", 0);
        Score score2 = new Score(4, "CSAT", 0);
        Score score3 = new Score(5, "CSAT", 1);
        Score score4 = new Score(9, "CSAT", 1);

        assertThat(score.isPassive()).isFalse();
        assertThat(score2.isPassive()).isFalse();
        assertThat(score3.isPassive()).isFalse();
        assertThat(score4.isPassive()).isFalse();
    }

    @Test
    public void whenPromoterAndScaleCSAT_itReturnsTrue() throws Exception {
        Score score = new Score(4, "CSAT", 0);
        Score score2 = new Score(5, "CSAT", 0);
        Score score3 = new Score(9, "CSAT", 1);
        Score score4 = new Score(10, "CSAT", 1);

        assertThat(score.isPromoter()).isTrue();
        assertThat(score2.isPromoter()).isTrue();
        assertThat(score3.isPromoter()).isTrue();
        assertThat(score4.isPromoter()).isTrue();
    }

    @Test
    public void whenNotPromoterAndScaleCSAT_itReturnsTrue() throws Exception {
        Score score = new Score(3, "CSAT", 0);
        Score score2 = new Score(6, "CSAT", 0);
        Score score3 = new Score(0, "CSAT", 1);
        Score score4 = new Score(8, "CSAT", 1);

        assertThat(score.isPromoter()).isFalse();
        assertThat(score2.isPromoter()).isFalse();
        assertThat(score3.isPromoter()).isFalse();
        assertThat(score4.isPromoter()).isFalse();
    }

    @Test
    public void whenScaleCSAT_itReturnsCorrectMin() throws Exception {
        Score score = new Score(0, "CSAT", 0);
        Score score2 = new Score(0, "CSAT", 1);
        Score score3 = new Score(0, "CSAT", -1);
        assertThat(score.minimumScore()).isEqualTo(1);
        assertThat(score2.minimumScore()).isEqualTo(1);
        assertThat(score3.minimumScore()).isEqualTo(1);
    }

    @Test
    public void whenScaleCSAT_itReturnsCorrectMax() throws Exception {
        Score score = new Score(0, "CSAT", 0);
        Score score2 = new Score(0, "CSAT", 1);
        Score score3 = new Score(0, "CSAT", -1);
        assertThat(score.maximumScore()).isEqualTo(5);
        assertThat(score2.maximumScore()).isEqualTo(10);
        assertThat(score3.maximumScore()).isEqualTo(5);
    }

    @Test
    public void whenIncorrectType_itDefaultsToNPS() throws Exception {
        Score score = new Score(0, "incorrect", 0);
        Log.e("A", score.toString());
        assertThat(score.minimumScore()).isEqualTo(0);
        assertThat(score.maximumScore()).isEqualTo(10);

        assertThat(score.isPromoter()).isFalse();
        assertThat(score.isPassive()).isFalse();
        assertThat(score.isDetractor()).isTrue();
    }


}
