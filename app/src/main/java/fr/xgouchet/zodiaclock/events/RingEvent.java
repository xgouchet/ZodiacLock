package fr.xgouchet.zodiaclock.events;

import fr.xgouchet.zodiaclock.game.behaviors.InteractiveRing;

/**
 * @author Xavier Gouchet
 */
public class RingEvent {

    @InteractiveRing.RingId
    private final int ringId;

    private float displayAngle;

    public RingEvent(@InteractiveRing.RingId int ringId) {
        this.ringId = ringId;
    }

    public float getDisplayAngle() {
        return displayAngle;
    }

    public void setDisplayAngle(float displayAngle) {
        this.displayAngle = displayAngle;
    }

    @InteractiveRing.RingId
    public int getRingId() {
        return ringId;
    }
}
