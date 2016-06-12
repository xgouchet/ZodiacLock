package fr.xgouchet.zodiaclock.game;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.squareup.otto.Bus;

import java.util.concurrent.TimeUnit;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import fr.xgouchet.zodiaclock.engine.GLException;
import fr.xgouchet.zodiaclock.engine.rendering.RenderContext;
import fr.xgouchet.zodiaclock.events.TouchEvent;

import static fr.xgouchet.zodiaclock.engine.GLException.checkGlError;
import static java.lang.Math.abs;

/**
 * @author Xavier Gouchet
 */
public class GameRenderer implements GLSurfaceView.Renderer {


    @NonNull
    private final Context context;
    @NonNull
    private final Bus bus = new Bus();

    @NonNull
    private final RenderContext renderContext = new RenderContext();
    @NonNull
    private final GameScene gameScene = new GameScene(bus);

    private int width, height;
    private boolean error;

    private long nanoTick;


    @MainThread
    public GameRenderer(@NonNull Context context, GLSurfaceView glSurfaceView) {
        this.context = context;

        glSurfaceView.setOnTouchListener(touchListener);
        glSurfaceView.setRenderer(this);
    }

    @Override
    @UiThread
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        try {
            // prepare scene
            gameScene.prepare(context);

        } catch (GLException e) {
            Log.e("GameRenderer", "onSurfaceCreated", e);
            error = true;
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
            error = true;
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

        // remember the size
        this.width = width;
        this.height = height;
    }

    @Override
    @UiThread
    public void onDrawFrame(GL10 gl) {
        if (error) return;

        try {

            long newTick = System.nanoTime();
            long deltaNanos = newTick - nanoTick;
            long timeMs = TimeUnit.NANOSECONDS.toMillis(newTick);
            nanoTick = newTick;

            if (gameScene.needsUpdate()) {
                gameScene.onUpdate(deltaNanos, timeMs);
            }

            if (gameScene.needsRender())
                gameScene.onRender(renderContext);
        } catch (GLException e) {
            Log.e("GameRenderer", "onDrawFrame", e);
            error = true;
        }
    }

    private final View.OnTouchListener touchListener = new View.OnTouchListener() {

        final float matrinxInv[] = new float[16];
        final float matrinxTransform[] = new float[16];
        final float screenPosition[] = new float[4];
        final float worldPosition[] = new float[4];
        final float planePosition[] = new float[3];

        final float ray[] = new float[3];

        final TouchEvent touchEvent = new TouchEvent();

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            screenPosition[0] = ((event.getX() / width) * 2) - 1;
            screenPosition[1] = 1 - ((event.getY() / height) * 2); // invert Y
            screenPosition[2] = -1.0f;
            screenPosition[3] = 1.0f;

            Matrix.multiplyMM(matrinxTransform, 0, renderContext.matrixP, 0, renderContext.matrixV, 0);
            Matrix.invertM(matrinxInv, 0,
                    matrinxTransform, 0);
            Matrix.multiplyMV(worldPosition, 0, matrinxInv, 0, screenPosition, 0);

            ray[0] = worldPosition[0] - renderContext.vecEyePos[0];
            ray[1] = worldPosition[1] - renderContext.vecEyePos[1];
            ray[2] = worldPosition[2] - renderContext.vecEyePos[2];

            float denom = /* (ray[0] * 0) + (ray[1] * 0) + */ (ray[2] * 1);
            if (abs(denom) > 0.00001) {
                float t = /* (-renderContext.vecEyePos[0] * 0) + (-renderContext.vecEyePos[1] * 0) + */ (-renderContext.vecEyePos[2] * 1);
                t /= denom;
                planePosition[0] = renderContext.vecEyePos[0] + (t * ray[0]);
                planePosition[1] = renderContext.vecEyePos[1] + (t * ray[1]);
                planePosition[2] = renderContext.vecEyePos[2] + (t * ray[2]);


                touchEvent.update(planePosition, event.getActionMasked());
                bus.post(touchEvent);
            }


            return true;
        }
    };
}
