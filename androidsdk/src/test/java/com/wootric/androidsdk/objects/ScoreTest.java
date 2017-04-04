package com.wootric.androidsdk.objects;

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
        Score score = new Score(0, "NPS");
        Score score2 = new Score(6, "NPS");

        assertThat(score.isDetractor()).isTrue();
        assertThat(score2.isDetractor()).isTrue();
    }

    @Test
    public void whenNotDetractorAndScaleNPS_itReturnsFalse() throws Exception {
        Score score = new Score(-1, "NPS");
        Score score2 = new Score(7, "NPS");

        assertThat(score.isDetractor()).isFalse();
        assertThat(score2.isDetractor()).isFalse();
    }

    @Test
    public void whenPassiveAndScaleNPS_itReturnsTrue() throws Exception {
        Score score = new Score(7, "NPS");
        Score score2 = new Score(8, "NPS");

        assertThat(score.isPassive()).isTrue();
        assertThat(score2.isPassive()).isTrue();
    }

    @Test
    public void whenNotPassiveAndScaleNPS_itReturnsFalse() throws Exception {
        Score score = new Score(6, "NPS");
        Score score2 = new Score(9, "NPS");

        assertThat(score.isPassive()).isFalse();
        assertThat(score2.isPassive()).isFalse();
    }

    @Test
    public void whenPromoterAndScaleNPS_itReturnsTrue() throws Exception {
        Score score = new Score(9, "NPS");
        Score score2 = new Score(10, "NPS");

        assertThat(score.isPromoter()).isTrue();
        assertThat(score2.isPromoter()).isTrue();
    }

    @Test
    public void whenNotPromoterAndScaleNPS_itReturnsTrue() throws Exception {
        Score score = new Score(8, "NPS");
        Score score2 = new Score(11, "NPS");

        assertThat(score.isPromoter()).isFalse();
        assertThat(score2.isPromoter()).isFalse();
    }

    @Test
    public void whenScaleNPS_itReturnsCorrectMin() throws Exception {
        assertThat(Score.minimumScore("NPS")).isEqualTo(0);
    }

    @Test
    public void whenScaleNPS_itReturnsCorrectMax() throws Exception {
        assertThat(Score.maximumScore("NPS")).isEqualTo(10);
    }

    @Test
    public void whenDetractorAndScaleCES_itReturnsTrue() throws Exception {
        Score score = new Score(1, "CES");
        Score score2 = new Score(3, "CES");

        assertThat(score.isDetractor()).isTrue();
        assertThat(score2.isDetractor()).isTrue();
    }

    @Test
    public void whenNotDetractorAndScaleCES_itReturnsFalse() throws Exception {
        Score score = new Score(0, "CES");
        Score score2 = new Score(4, "CES");

        assertThat(score.isDetractor()).isFalse();
        assertThat(score2.isDetractor()).isFalse();
    }

    @Test
    public void whenPassiveAndScaleCES_itReturnsTrue() throws Exception {
        Score score = new Score(4, "CES");
        Score score2 = new Score(5, "CES");

        assertThat(score.isPassive()).isTrue();
        assertThat(score2.isPassive()).isTrue();
    }

    @Test
    public void whenNotPassiveAndScaleCES_itReturnsFalse() throws Exception {
        Score score = new Score(3, "CES");
        Score score2 = new Score(6, "CES");

        assertThat(score.isPassive()).isFalse();
        assertThat(score2.isPassive()).isFalse();
    }

    @Test
    public void whenPromoterAndScaleCES_itReturnsTrue() throws Exception {
        Score score = new Score(6, "CES");
        Score score2 = new Score(7, "CES");

        assertThat(score.isPromoter()).isTrue();
        assertThat(score2.isPromoter()).isTrue();
    }

    @Test
    public void whenNotPromoterAndScaleCES_itReturnsTrue() throws Exception {
        Score score = new Score(5, "CES");
        Score score2 = new Score(8, "CES");

        assertThat(score.isPromoter()).isFalse();
        assertThat(score2.isPromoter()).isFalse();
    }

    @Test
    public void whenScaleCES_itReturnsCorrectMin() throws Exception {
        assertThat(Score.minimumScore("CES")).isEqualTo(1);
    }

    @Test
    public void whenScaleCES_itReturnsCorrectMax() throws Exception {
        assertThat(Score.maximumScore("CES")).isEqualTo(7);
    }

    @Test
    public void whenDetractorAndScaleCSAT_itReturnsTrue() throws Exception {
        Score score = new Score(1, "CSAT");
        Score score2 = new Score(2, "CSAT");

        assertThat(score.isDetractor()).isTrue();
        assertThat(score2.isDetractor()).isTrue();
    }

    @Test
    public void whenNotDetractorAndScaleCSAT_itReturnsFalse() throws Exception {
        Score score = new Score(0, "CSAT");
        Score score2 = new Score(3, "CSAT");

        assertThat(score.isDetractor()).isFalse();
        assertThat(score2.isDetractor()).isFalse();
    }

    @Test
    public void whenPassiveAndScaleCSAT_itReturnsTrue() throws Exception {
        Score score = new Score(3, "CSAT");

        assertThat(score.isPassive()).isTrue();
    }

    @Test
    public void whenNotPassiveAndScaleCSAT_itReturnsFalse() throws Exception {
        Score score = new Score(2, "CSAT");
        Score score2 = new Score(4, "CSAT");

        assertThat(score.isPassive()).isFalse();
        assertThat(score2.isPassive()).isFalse();
    }

    @Test
    public void whenPromoterAndScaleCSAT_itReturnsTrue() throws Exception {
        Score score = new Score(4, "CSAT");
        Score score2 = new Score(5, "CSAT");

        assertThat(score.isPromoter()).isTrue();
        assertThat(score2.isPromoter()).isTrue();
    }

    @Test
    public void whenNotPromoterAndScaleCSAT_itReturnsTrue() throws Exception {
        Score score = new Score(3, "CSAT");
        Score score2 = new Score(6, "CSAT");

        assertThat(score.isPromoter()).isFalse();
        assertThat(score2.isPromoter()).isFalse();
    }

    @Test
    public void whenScaleCSAT_itReturnsCorrectMin() throws Exception {
        assertThat(Score.minimumScore("CSAT")).isEqualTo(1);
    }

    @Test
    public void whenScaleCSAT_itReturnsCorrectMax() throws Exception {
        assertThat(Score.maximumScore("CSAT")).isEqualTo(5);
    }

    @Test
    public void whenIncorrectType_itDefaultsToNPS() throws Exception {
        Score score = new Score(0, "incorrect");
        assertThat(Score.minimumScore("incorrect")).isEqualTo(0);
        assertThat(Score.maximumScore("incorrect")).isEqualTo(10);

        assertThat(score.isPromoter()).isFalse();
        assertThat(score.isPassive()).isFalse();
        assertThat(score.isDetractor()).isTrue();
    }


}
