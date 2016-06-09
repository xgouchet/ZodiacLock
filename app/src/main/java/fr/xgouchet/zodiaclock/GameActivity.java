package fr.xgouchet.zodiaclock;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.widget.Toast;

import fr.xgouchet.zodiaclock.game.GameRenderer;

/**
 * @author Xavier Gouchet
 */
public class GameActivity extends Activity {

    private GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glSurfaceView = new GLSurfaceView(this);

        // Check if the system supports OpenGL ES 2.0.
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        if (supportsEs2) {

            glSurfaceView.setPreserveEGLContextOnPause(true);
            glSurfaceView.setEGLContextClientVersion(2);
            glSurfaceView.setZOrderOnTop(true);
            glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
            glSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);
            glSurfaceView.setEGLConfigChooser(new GameConfigChooser());
            glSurfaceView.setRenderer(new GameRenderer(this));
            setContentView(glSurfaceView);
        } else {
            Toast.makeText(this, "We don't support crappy phones", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }
}
