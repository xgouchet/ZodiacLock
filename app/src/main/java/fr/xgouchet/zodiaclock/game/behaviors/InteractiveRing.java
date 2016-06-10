package fr.xgouchet.zodiaclock.game.behaviors;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import fr.xgouchet.zodiaclock.engine.GLException;
import fr.xgouchet.zodiaclock.engine.entities.Entity;
import fr.xgouchet.zodiaclock.engine.rendering.RenderContext;
import fr.xgouchet.zodiaclock.engine.rendering.Transform;
import fr.xgouchet.zodiaclock.game.shapes.RingShape;

import static java.lang.Math.abs;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.round;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 * @author Xavier Gouchet
 */
public class InteractiveRing extends Entity implements TouchListener {

    public static final double TWO_PI = Math.PI * 2.0f;
    private static final int SNAP_COUNT = 12;
    private static final double SNAP_ANGLE = Math.PI * 2.0 / SNAP_COUNT;

    private static final float DETECT_THRESHOLD = 0.05f;

    private final float radius, thickness;
    private final float innerRadius;
    private final float outerRadius;

    private final Transform transform;
    private final RingShape shape;
    private boolean dragging;
    private float initialDragAngle;
    private float angle = 0;
    private float displayAngle = 0;
    private float snappedAngle = 0;


    public InteractiveRing(float radius, float thickness) {
        this.radius = radius;
        this.thickness = thickness;
        this.innerRadius = radius - (thickness / 2.0f);
        this.outerRadius = radius + (thickness / 2.0f);

        transform = new Transform();
        shape = new RingShape(radius, thickness, 1 / 4.0f);
    }


    @Override
    public void onPrepare(@NonNull Context context) throws GLException {
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

        if (!dragging) {
            if (abs(snappedAngle - angle) > .00001f) {
                angle = angle + ((snappedAngle - angle) * .15f);
            }
            displayAngle = angle;
        }

        transform.setOrientation((float) cos(displayAngle), (float) sin(displayAngle), 0,
                (float) -sin(displayAngle), (float) cos(displayAngle), 0);

        transform.onRender(renderContext);
        shape.onRender(renderContext);
    }

    @Override
    public void onTouchDown(float[] position) {
        double distFromCenter = sqrt(
                (position[0] * position[0])
                        + (position[1] * position[1])
                        + (position[2] * position[2]));

        dragging = (distFromCenter >= (innerRadius - DETECT_THRESHOLD))
                && (distFromCenter <= (outerRadius + DETECT_THRESHOLD));
        initialDragAngle = (float) atan2(position[1], position[0]);
    }

    @Override
    public void onTouchMove(float[] position) {
        if (!dragging) return;

        Log.i("Ring", "Moving ring " + radius);
        float draggingAngle = (float) atan2(position[1], position[0]);
        float angleDelta = (draggingAngle - initialDragAngle);
        displayAngle = angle + angleDelta;
    }


    @Override
    public void onTouchUp(float[] position) {
        if (!dragging) return;

        int snapped = (int) (round(displayAngle / SNAP_ANGLE) + SNAP_COUNT);
        snappedAngle = (float) ((snapped % SNAP_COUNT) * SNAP_ANGLE);
        angle = displayAngle;

        while (snappedAngle - angle > Math.PI) {
            angle += TWO_PI;
        }
        if (angle - snappedAngle > Math.PI) {
            angle -= TWO_PI;
        }
        Log.i("Ring", "Snapping  ring " + radius + " from " + angle + " to " + snappedAngle);
        dragging = false;
    }
}
