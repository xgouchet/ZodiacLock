package fr.xgouchet.zodiaclock.engine;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import static fr.xgouchet.zodiaclock.engine.Utils.checkGlError;

/**
 * @author Xavier Gouchet
 */
public class Transform extends IEntity {

    private final float[] modelMatrix = new float[16];

    public Transform() {
        Matrix.setIdentityM(modelMatrix, 0);
    }


    public void translateTo(float x, float y, int z) {
        modelMatrix[12] = x;
        modelMatrix[13] = y;
        modelMatrix[14] = z;
    }

    @Override
    public void onPrepare(Context context) {

    }

    @Override
    public void onDraw(RenderContext renderContext) throws GLException {
        Matrix.multiplyMM(renderContext.matrixMV, 0, renderContext.matrixV, 0, modelMatrix, 0);
        Matrix.multiplyMM(renderContext.matrixMVP, 0, renderContext.matrixP, 0, renderContext.matrixMV, 0);

        GLES20.glUniformMatrix4fv(renderContext.uniformModelMatrix, 1, false, modelMatrix, 0);
        checkGlError();

        GLES20.glUniformMatrix4fv(renderContext.uniformMVPMatrix, 1, false, renderContext.matrixMVP, 0);
        checkGlError();
    }
}
