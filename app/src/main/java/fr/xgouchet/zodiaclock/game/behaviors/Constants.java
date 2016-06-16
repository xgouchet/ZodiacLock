package fr.xgouchet.zodiaclock.game.behaviors;

import android.support.annotation.ColorRes;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;

import fr.xgouchet.zodiaclock.R;

/**
 * @author Xavier Gouchet
 */
public class Constants {
    public static final double TWO_PI = Math.PI * 2.0f;

    public static final float DETECT_THRESHOLD = 0.05f;

    public static final float TEX_COORDS_SCALE = 1f / 4f;

    public static final int STEP_COUNT = 12;
    public static final float STEP_ANGLE = (float) (TWO_PI / STEP_COUNT);

    public static final int RING_ID_INNER = 0;
    public static final int RING_ID_MIDDLE = 1;
    public static final int RING_ID_OUTER = 2;



    @IntDef({RING_ID_INNER, RING_ID_MIDDLE, RING_ID_OUTER})
    public @interface RingId {
    }

    public static final float CENTER_RADIUS = 0.65f;

    public static final float RING_THICKNESS = 0.8f;
    public static final float RING_HALF_THICKNESS = RING_THICKNESS / 2;

    public static final float RING_RADIUS_INNER = 1.15f;
    public static final float RING_RADIUS_MIDDLE = 2.05f;
    public static final float RING_RADIUS_OUTER = 2.95f;

    public static final float MARBLE_RADIUS = RING_THICKNESS / 2.75f;
    public static final int[] MARBLE_COLORS = new int[]{
            R.color.marble_0_red,
            R.color.marble_1_orange,
            R.color.marble_2_yellow,
            R.color.marble_3_lime,
            R.color.marble_4_green,
            R.color.marble_5_teal,
            R.color.marble_6_turquoise,
            R.color.marble_7_azure,
            R.color.marble_8_blue,
            R.color.marble_9_violet,
            R.color.marble_10_pink,
            R.color.marble_11_chartreuse
    };


    public static final float GUIDE_LENGTH = 4f;

    public static float getRingRadius(@Constants.RingId int ringId) {
        switch (ringId) {
            case Constants.RING_ID_INNER:
                return Constants.RING_RADIUS_INNER;
            case Constants.RING_ID_MIDDLE:
                return Constants.RING_RADIUS_MIDDLE;
            case Constants.RING_ID_OUTER:
                return Constants.RING_RADIUS_OUTER;
            default:
                throw new IllegalArgumentException();
        }
    }

    @ColorRes
    public static int getColor(@IntRange(from = 0, to = STEP_COUNT - 1) int step) {
        return MARBLE_COLORS[step];
    }

    @RingId
    public static int nextRingId(@RingId int ringId) {
        switch (ringId) {
            case RING_ID_INNER:
                return RING_ID_MIDDLE;
            case RING_ID_MIDDLE:
                return RING_ID_OUTER;
            case RING_ID_OUTER:
                return RING_ID_INNER;
            default:
                throw new IllegalArgumentException();
        }
    }

}
