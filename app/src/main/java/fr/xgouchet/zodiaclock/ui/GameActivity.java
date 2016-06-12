package fr.xgouchet.zodiaclock.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.widget.Toast;

import fr.xgouchet.zodiaclock.game.GameRenderer;

/**
 * @author Xavier Gouchet
 */
public class GameActivity extends Activity {

    private GLSurfaceView glSurfaceView;
    private GameRenderer gameRenderer;

    @Override
    @MainThread
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if the system supports OpenGL ES 2.0.
        final boolean supportsEs2 = isOpenGLES2Supported();

        if (!supportsEs2) {
            Toast.makeText(this, "We don't support crappy phones", Toast.LENGTH_SHORT).show();
            finish();
        }

        // prepare surface view
        glSurfaceView = new GLSurfaceView(this);
        prepareGLSurfaceView();

        gameRenderer = new GameRenderer(this, glSurfaceView);

        setContentView(glSurfaceView);

    }

    private boolean isOpenGLES2Supported() {
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        return configurationInfo.reqGlEsVersion >= 0x20000;
    }

    private void prepareGLSurfaceView() {
        glSurfaceView.setPreserveEGLContextOnPause(true);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setZOrderOnTop(true);
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        glSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);
        glSurfaceView.setEGLConfigChooser(new GameConfigChooser());
    }

    @Override
    @MainThread
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

    @Override
    @MainThread
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }
}
