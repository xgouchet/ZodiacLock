package fr.xgouchet.zodiaclock.engine.rendering;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.opengl.GLES20;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;

import fr.xgouchet.zodiaclock.engine.GLException;
import fr.xgouchet.zodiaclock.engine.entities.Entity;

/**
 * @author Xavier Gouchet
 */
public class Material extends Entity {

    private final float[] diffuseColor;
    private final float[] specularColor;

    @ColorRes
    private final int diffuseColorId, specularColorId;

    public Material(int diffuseColorId, int specularColorId) {

        this.diffuseColorId = diffuseColorId;
        this.specularColorId = specularColorId;

        diffuseColor = new float[4];
        specularColor = new float[4];
    }

    @Override
    public void onPrepare(@NonNull Context context) throws GLException {
        Resources res = context.getResources();
        fillColor(res, diffuseColorId, diffuseColor, 1.0f);
        fillColor(res, specularColorId, specularColor, 1.0f);
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
        if (renderContext.uniformDiffuseColor >= 0) {
            GLES20.glUniform4fv(renderContext.uniformDiffuseColor, 1, diffuseColor, 0);
        }
        if (renderContext.uniformSpecularColor >= 0) {
            GLES20.glUniform4fv(renderContext.uniformSpecularColor, 1, specularColor, 0);
        }
    }

    private void fillColor(@NonNull Resources res, @ColorRes int colorId, @NonNull float[] colorArray, float scale) {
        int color;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = res.getColor(colorId, null);
        } else {
            color = res.getColor(colorId);
        }

        colorArray[0] = Color.red(color) * scale / 255.0f;
        colorArray[1] = Color.green(color) * scale / 255.0f;
        colorArray[2] = Color.blue(color) * scale / 255.0f;
        colorArray[3] = Color.alpha(color) * scale / 255.0f;
    }
}
