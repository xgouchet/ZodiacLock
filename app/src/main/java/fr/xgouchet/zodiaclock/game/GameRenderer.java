package fr.xgouchet.zodiaclock.game;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import fr.xgouchet.zodiaclock.engine.GLException;
import fr.xgouchet.zodiaclock.engine.RenderContext;

import static fr.xgouchet.zodiaclock.engine.GLException.checkGlError;

/**
 * @author Xavier Gouchet
 */
public class GameRenderer implements GLSurfaceView.Renderer {


    @NonNull
    private final Context context;

    private final RenderContext renderContext = new RenderContext();
    private final GameScene gameScene = new GameScene();
    private boolean mError;

    @MainThread
    public GameRenderer(@NonNull Context context) {
        this.context = context;
    }

    @Override
    @UiThread
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        try {
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            checkGlError();

            // prepare scene
            gameScene.prepare(context);

        } catch (GLException e) {
            Log.e("GameRenderer", "onSurfaceCreated", e);
            mError = true;
        }
    }

    @Override
    @UiThread
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // Update viewport
        try {
            GLES20.glViewport(0, 0, width, height);
            checkGlError();
        } catch (GLException e) {
            Log.e("GameRenderer", "onSurfaceChanged", e);
            mError = true;
        }

        // update projection matrix
        final float ratio = (float) width / height;
        Matrix.frustumM(renderContext.matrixP, 0,
                -ratio,     // left
                ratio,      // right
                -1.0f,      // bottom
                1.0f,       // top
                1.0f,       // near
                10.0f       // far
        );
    }

    @Override
    @UiThread
    public void onDrawFrame(GL10 gl) {
        if (mError) return;

        try {
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            checkGlError();

            if (gameScene.needsUpdate())
                gameScene.onUpdate();
            if (gameScene.needsRender())
                gameScene.onRender(renderContext);
        } catch (GLException e) {
            Log.e("GameRenderer", "onDrawFrame", e);
            mError = true;
        }
    }
}
