package fr.xgouchet.zodiaclock.engine.environment;

import android.content.Context;
import android.opengl.GLES20;
import android.support.annotation.NonNull;

import fr.xgouchet.zodiaclock.engine.GLException;
import fr.xgouchet.zodiaclock.engine.entities.Entity;
import fr.xgouchet.zodiaclock.engine.rendering.RenderContext;

import static fr.xgouchet.zodiaclock.engine.GLException.checkGlError;

/**
 * @author Xavier Gouchet
 */
public class Background extends Entity {
    @Override
    public void onPrepare(@NonNull Context context) throws GLException {


        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        checkGlError();

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        checkGlError();

        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        checkGlError();

        GLES20.glDepthMask(true);
        checkGlError();

        GLES20.glClearDepthf(1.0f);
        checkGlError();
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
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        checkGlError();
    }
}
