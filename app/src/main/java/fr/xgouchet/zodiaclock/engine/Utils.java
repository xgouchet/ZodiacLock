package fr.xgouchet.zodiaclock.engine;

import android.opengl.GLES20;

/**
 * @author Xavier Gouchet
 */
public final class Utils {


    public static void checkGlError() throws GLException {
        int error = GLES20.glGetError();
        if (error != GLES20.GL_NO_ERROR) {
            throw new GLException(error);
        }
    }

}
