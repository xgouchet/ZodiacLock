package fr.xgouchet.zodiaclock.engine;

import android.content.Context;
import android.opengl.Matrix;

/**
 * @author Xavier Gouchet
 */
public class Camera extends IEntity {

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
    public void onPrepare(Context context) {

    }

    @Override
    public void onDraw(RenderContext renderContext) {
        Matrix.setLookAtM(renderContext.matrixV, 0, eyeX, eyeY, eyeZ, targetX, targetY, targetZ, 0, 1, 0);
    }
}
