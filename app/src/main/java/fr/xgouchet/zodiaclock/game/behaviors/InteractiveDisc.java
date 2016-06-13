package fr.xgouchet.zodiaclock.game.behaviors;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import fr.xgouchet.zodiaclock.R;
import fr.xgouchet.zodiaclock.engine.GLException;
import fr.xgouchet.zodiaclock.engine.entities.Entity;
import fr.xgouchet.zodiaclock.engine.rendering.Material;
import fr.xgouchet.zodiaclock.engine.rendering.RenderContext;
import fr.xgouchet.zodiaclock.engine.rendering.Transform;
import fr.xgouchet.zodiaclock.events.TouchEvent;
import fr.xgouchet.zodiaclock.game.shapes.DiscShape;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

/**
 * @author Xavier Gouchet
 */
public class InteractiveDisc extends Entity {

    @NonNull
    private final Bus bus;

    @NonNull
    private final Material material;
    @NonNull
    private final Transform transform;
    @NonNull
    private final DiscShape shape;

    private final float radius;
    private boolean downOnDisc;

    private float snappedZ, displayZ;


    public InteractiveDisc(@NonNull Bus bus, float radius) {
        this.bus = bus;
        this.radius = radius;

        material = new Material(R.color.blue, R.color.white);
        transform = new Transform();
        shape = new DiscShape(radius, Constants.TEX_COORDS_SCALE);

        bus.register(this);
    }

    @Override
    public void onPrepare(@NonNull Context context) throws GLException {
        material.onPrepare(context);
        transform.onPrepare(context);
        shape.onPrepare(context);
    }

    @Override
    public boolean needsUpdate() {
        return true;
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
        if (abs(snappedZ - displayZ) > .00001) {
            displayZ = displayZ + ((snappedZ - displayZ) * .35f);
            transform.translateTo(0, 0, displayZ);
        }

        material.onRender(renderContext);
        transform.onRender(renderContext);
        shape.onRender(renderContext);
    }

    @Subscribe
    public void onTouchEvent(TouchEvent event) {
        switch (event.getType()) {
            case MotionEvent.ACTION_DOWN:
                onTouchDown(event.getWorldPosition());
                break;
            case MotionEvent.ACTION_MOVE:
                onTouchMove(event.getWorldPosition());
                break;
            case MotionEvent.ACTION_UP:
                onTouchUp(event.getWorldPosition());
                break;
        }
    }

    private void onTouchDown(float[] position) {
        double distFromCenter = sqrt(
                (position[0] * position[0])
                        + (position[1] * position[1])
                        + (position[2] * position[2]));

        downOnDisc = (distFromCenter <= (radius + Constants.DETECT_THRESHOLD));
        if (downOnDisc) snappedZ = -0.5f;
    }

    private void onTouchMove(float[] position) {
        if (!downOnDisc) return;
        snappedZ = 0;

        double distFromCenter = sqrt(
                (position[0] * position[0])
                        + (position[1] * position[1])
                        + (position[2] * position[2]));
        if (distFromCenter <= (radius + Constants.DETECT_THRESHOLD)) {
            snappedZ = -0.5f;
        } else {
            snappedZ = 0;
        }
    }


    private void onTouchUp(float[] position) {
        if (!downOnDisc) return;
        downOnDisc = false;
        snappedZ = 0;

        double distFromCenter = sqrt(
                (position[0] * position[0])
                        + (position[1] * position[1])
                        + (position[2] * position[2]));

        if ((distFromCenter <= (radius + Constants.DETECT_THRESHOLD)))
            Log.i("Disc", "Clicked on disc !");

    }
}
