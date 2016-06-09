package fr.xgouchet.zodiaclock.engine;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.support.annotation.NonNull;

import static fr.xgouchet.zodiaclock.engine.GLException.checkGlError;

/**
 * @author Xavier Gouchet
 */
public class Transform extends Entity {

    private final float[] modelMatrix = new float[16];

    public Transform() {
        Matrix.setIdentityM(modelMatrix, 0);
    }


    public void translateTo(float x, float y, int z) {
        modelMatrix[12] = x;
        modelMatrix[13] = y;
        modelMatrix[14] = z;
    }

    public void copy(float[] dest, int size, int offset) {
        System.arraycopy(modelMatrix, offset, dest, 0, size);
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
    public void onRender(@NonNull RenderContext renderContext) throws GLException {
        Matrix.multiplyMM(renderContext.matrixMV, 0, renderContext.matrixV, 0, modelMatrix, 0);
        Matrix.multiplyMM(renderContext.matrixMVP, 0, renderContext.matrixP, 0, renderContext.matrixMV, 0);

        GLES20.glUniformMatrix4fv(renderContext.uniformModelMatrix, 1, false, modelMatrix, 0);
        checkGlError();

    }
}
