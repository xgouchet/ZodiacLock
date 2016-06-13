package fr.xgouchet.zodiaclock.engine.rendering;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import fr.xgouchet.zodiaclock.engine.GLException;
import fr.xgouchet.zodiaclock.engine.entities.Entity;

import static fr.xgouchet.zodiaclock.engine.GLException.checkGlError;

/**
 * @author Xavier Gouchet
 */
public class Transform extends Entity {

    private final float[] worldModelMatrix = new float[16];
    private final float[] modelMatrix = new float[16];


    @Nullable
    private Transform parent;


    public Transform() {
        Matrix.setIdentityM(worldModelMatrix, 0);
        Matrix.setIdentityM(modelMatrix, 0);
    }

    public void setParent(@Nullable Transform parent) {
        this.parent = parent;
    }

    //region Entity
    @Override
    public void onPrepare(@NonNull Context context) {

    }


    @Override
    public boolean needsUpdate() {
        return true;
    }

    @Override
    public void onUpdate(long deltaNanos, long timeMs) {
        if (parent == null) {
            System.arraycopy(modelMatrix, 0, worldModelMatrix, 0, worldModelMatrix.length);
        } else {
            parent.onUpdate(deltaNanos, timeMs);
            Matrix.multiplyMM(worldModelMatrix, 0, parent.worldModelMatrix, 0, modelMatrix, 0);
        }
    }

    @Override
    public boolean needsRender() {
        return true;
    }

    @Override
    public void onRender(@NonNull RenderContext renderContext) throws GLException {

        Matrix.multiplyMM(renderContext.matrixMV, 0, renderContext.matrixV, 0, worldModelMatrix, 0);
        Matrix.multiplyMM(renderContext.matrixMVP, 0, renderContext.matrixP, 0, renderContext.matrixMV, 0);

        GLES20.glUniformMatrix4fv(renderContext.uniformModelMatrix, 1, false, worldModelMatrix, 0);
        checkGlError();

        GLES20.glUniformMatrix4fv(renderContext.uniformMVPMatrix, 1, false, renderContext.matrixMVP, 0);
        checkGlError();

    }
    //endregion

    public void copy(float[] dest, int size, int offset) {
        System.arraycopy(modelMatrix, offset, dest, 0, size);
    }

    public void translateTo(float x, float y, float z) {
        modelMatrix[12] = x;
        modelMatrix[13] = y;
        modelMatrix[14] = z;
    }

    public void setOrientation(float dirX, float dirY, float dirZ, float upX, float upY, float upZ) {
        modelMatrix[0] = dirX;
        modelMatrix[1] = dirY;
        modelMatrix[2] = dirZ;

        modelMatrix[4] = upX;
        modelMatrix[5] = upY;
        modelMatrix[6] = upZ;

        modelMatrix[8] = (dirY * upZ) - (dirZ * upY);
        modelMatrix[9] = (dirZ * upX) - (dirX * upZ);
        modelMatrix[10] = (dirX * upY) - (dirY * upX);
    }
}
