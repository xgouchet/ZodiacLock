package fr.xgouchet.zodiaclock.engine;

import android.opengl.GLES20;

/**
 * @author Xavier Gouchet
 */
public class GLException extends Exception {
    int errorCode;

    public GLException(int error) {
        errorCode = error;
    }

    @Override
    public String getMessage() {
        return "Open GL Error : " + errorCode;
    }

    public static void checkGlError() throws GLException {
        int error = GLES20.glGetError();
        if (error != GLES20.GL_NO_ERROR) {
            throw new GLException(error);
        }
    }
}
