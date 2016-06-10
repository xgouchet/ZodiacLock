package fr.xgouchet.zodiaclock.game.behaviors;

/**
 * @author Xavier Gouchet
 */
public interface TouchListener {

    void onTouchDown(float[] position);

    void onTouchMove(float[] position);

    void onTouchUp(float[] position);
}
