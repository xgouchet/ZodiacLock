package fr.xgouchet.zodiaclock.events;

import android.support.annotation.NonNull;

/**
 * @author Xavier Gouchet
 */
public class TouchEvent {


    private final float[] worldPosition = new float[3];
    private int type;

    public TouchEvent() {
    }

    public void update(@NonNull float[] position, int type) {
        System.arraycopy(position, 0, worldPosition, 0, 3);
        this.type = type;
    }

    public float[] getWorldPosition() {
        return worldPosition;
    }

    public int getType() {
        return type;
    }
}
