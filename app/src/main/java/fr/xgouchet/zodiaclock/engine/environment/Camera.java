package fr.xgouchet.zodiaclock.engine.environment;

import android.content.Context;
import android.opengl.Matrix;
import android.support.annotation.NonNull;

import fr.xgouchet.zodiaclock.engine.Entity;
import fr.xgouchet.zodiaclock.engine.RenderContext;

/**
 * @author Xavier Gouchet
 */
public class Camera extends Entity {

    private float eyeX, eyeY, eyeZ;
    private float targetX, targetY, targetZ;


    public void moveTo(float x, float y, float z) {
        eyeX = x;
        eyeY = y;
        eyeZ = z;
    }

    public void moveTargetTo(float x, float y, float z) {
        targetX = x;
        targetY = y;
        targetZ = z;
    }

    @Override
    public void onPrepare(@NonNull Context context) {

    }

    @Override
    public boolean needsUpdate() {
        return false;
    }

    @Override
    public void onUpdate(long deltaNanos, long timeMs) {
    }

    @Override
    public boolean needsRender() {
        return true;
    }

    @Override
    public void onRender(@NonNull RenderContext renderContext) {
        Matrix.setLookAtM(renderContext.matrixV, 0, eyeX, eyeY, eyeZ, targetX, targetY, targetZ, 0, 1, 0);
    }
}
