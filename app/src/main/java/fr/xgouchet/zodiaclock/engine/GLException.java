package fr.xgouchet.zodiaclock.engine;

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
}
