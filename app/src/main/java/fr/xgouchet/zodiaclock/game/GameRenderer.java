package fr.xgouchet.zodiaclock.game;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import fr.xgouchet.zodiaclock.R;
import fr.xgouchet.zodiaclock.engine.Camera;
import fr.xgouchet.zodiaclock.engine.EntityAggregator;
import fr.xgouchet.zodiaclock.engine.GLException;
import fr.xgouchet.zodiaclock.engine.IEntity;
import fr.xgouchet.zodiaclock.engine.RenderContext;
import fr.xgouchet.zodiaclock.engine.Shader;
import fr.xgouchet.zodiaclock.engine.Texture;
import fr.xgouchet.zodiaclock.engine.Transform;

import static fr.xgouchet.zodiaclock.engine.Utils.checkGlError;

/**
 * @author Xavier Gouchet
 */
public class GameRenderer implements GLSurfaceView.Renderer {


    @NonNull
    private final Context context;

    private final Camera camera = new Camera();
    private final List<IEntity> entities;

    private final RenderContext renderContext = new RenderContext();
    private boolean mError;

    public GameRenderer(@NonNull Context context) {
        this.context = context;

        camera.moveTo(0, 0, 5f);
        camera.moveTargetTo(0, 0, 0f);

        entities = new ArrayList<>();

        entities.add(new Shader(R.raw.hexa_vs, R.raw.hexa_fs));
        entities.add(new Texture(R.drawable.scratched_metal_shutterstock, Texture.TYPE_DIFFUSE));
        entities.add(new Texture(R.drawable.debug_normal, Texture.TYPE_NORMAL));

        RingShape innerRing = new RingShape(1.0f, 0.5f, 1.0f / 5.0f);
        RingShape middleRing = new RingShape(2.0f, 0.5f, 1.0f / 5.0f);
        RingShape outerRing = new RingShape(3.0f, 0.5f, 1.0f / 5.0f);


        entities.add(
                new EntityAggregator(
                        new Transform(),
                        innerRing
                )
        );
        entities.add(
                new EntityAggregator(
                        new Transform(),
                        middleRing
                )
        );
        entities.add(
                new EntityAggregator(
                        new Transform(),
                        outerRing
                )
        );
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        try {
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            checkGlError();

            // prepare scene
            camera.prepare(context);
            for (IEntity entity : entities) {
                entity.prepare(context);
            }
        } catch (GLException e) {
            Log.e("GameRenderer", "onSurfaceCreated", e);
            mError = true;
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        try {
            GLES20.glViewport(0, 0, width, height);
            checkGlError();
        } catch (GLException e) {
            Log.e("GameRenderer", "onSurfaceChanged", e);
            mError = true;
        }

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
    public void onDrawFrame(GL10 gl) {
        if (mError) return;

        try {
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            checkGlError();

            camera.onDraw(renderContext);
            for (IEntity entity : entities) {
                entity.onDraw(renderContext);
            }
        } catch (GLException e) {
            Log.e("GameRenderer", "onDrawFrame", e);
            mError = true;
        }
    }
}
