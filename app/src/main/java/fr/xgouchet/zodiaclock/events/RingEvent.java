package fr.xgouchet.zodiaclock.events;

import android.support.annotation.IntRange;

import fr.xgouchet.zodiaclock.game.behaviors.Constants;

/**
 * @author Xavier Gouchet
 */
public class RingEvent {

    @Constants.RingId
    private final int ringId;

    @IntRange(from = 0, to = Constants.STEP_COUNT - 1)
    private int snapped;

    public RingEvent(@Constants.RingId int ringId) {
        this.ringId = ringId;
    }

    @Constants.RingId
    public int getRingId() {
        return ringId;
    }

    public void setSnapped(@IntRange(from = 0, to = Constants.STEP_COUNT - 1) int snapped) {
        this.snapped = snapped;
    }

    @IntRange(from = 0, to = Constants.STEP_COUNT - 1)
    public int getSnapped() {
        return snapped;
    }
}
