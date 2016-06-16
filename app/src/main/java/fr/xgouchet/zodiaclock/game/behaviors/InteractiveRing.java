package fr.xgouchet.zodiaclock.game.behaviors;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import fr.xgouchet.zodiaclock.R;
import fr.xgouchet.zodiaclock.engine.GLException;
import fr.xgouchet.zodiaclock.engine.entities.Entity;
import fr.xgouchet.zodiaclock.engine.rendering.Material;
import fr.xgouchet.zodiaclock.engine.rendering.RenderContext;
import fr.xgouchet.zodiaclock.engine.rendering.Transform;
import fr.xgouchet.zodiaclock.events.RingEvent;
import fr.xgouchet.zodiaclock.events.TouchEvent;
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
public class InteractiveRing extends Entity {

    @NonNull
    private final Bus bus;


    private final float innerRadius;

    private final float outerRadius;
    @NonNull
    private final Material material;

    @NonNull
    private final Transform transform;
    @NonNull
    private final RingShape shape;
    @NonNull
    private final RingEvent ringEvent;

    private boolean dragging;

    private float initialDragAngle;
    private float angle = 0;
    private float displayAngle = 0;
    private float snappedAngle = 0;

    public InteractiveRing(@NonNull Bus bus, @Constants.RingId int id, float radius) {
        this.bus = bus;
        this.innerRadius = radius - Constants.RING_HALF_THICKNESS;
        this.outerRadius = radius + Constants.RING_HALF_THICKNESS;

        material = new Material(R.color.gold_diff, R.color.gold_spec);
        transform = new Transform();
        shape = new RingShape(radius, Constants.RING_HALF_THICKNESS, Constants.TEX_COORDS_SCALE);

        ringEvent = new RingEvent(id);

        this.bus.register(this);
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
        if (!dragging) {
            if (abs(snappedAngle - angle) > .000001f) {
                angle = angle + ((snappedAngle - angle) * .15f);
            }
            displayAngle = angle;
        }

        transform.setOrientation((float) cos(displayAngle), (float) sin(displayAngle), 0,
                (float) -sin(displayAngle), (float) cos(displayAngle), 0);
        transform.onUpdate(deltaNanos, timeMs);
    }

    @Override
    public boolean needsRender() {
        return true;
    }

    @Override
    public void onRender(@NonNull RenderContext renderContext) throws GLException {
        material.setEmissive(dragging);
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
                onTouchUp();
                break;
        }
    }


    private void onTouchDown(float[] position) {
        double distFromCenter = sqrt(
                (position[0] * position[0])
                        + (position[1] * position[1])
                        + (position[2] * position[2]));

        dragging = (distFromCenter >= (innerRadius - Constants.DETECT_THRESHOLD))
                && (distFromCenter <= (outerRadius + Constants.DETECT_THRESHOLD));
        initialDragAngle = (float) atan2(position[1], position[0]);
    }

    private void onTouchMove(float[] position) {
        if (!dragging) return;

        float draggingAngle = (float) atan2(position[1], position[0]);
        float angleDelta = (draggingAngle - initialDragAngle);
        displayAngle = angle + angleDelta;
    }

    private void onTouchUp() {
        if (!dragging) return;

        int snapped = (int) (round(displayAngle / Constants.STEP_ANGLE) + Constants.STEP_COUNT);
        snapped = snapped % Constants.STEP_COUNT;
        snappedAngle = (float) (snapped * Constants.STEP_ANGLE);
        angle = displayAngle;

        while (snappedAngle - angle > Math.PI) {
            angle += Constants.TWO_PI;
        }
        if (angle - snappedAngle > Math.PI) {
            angle -= Constants.TWO_PI;
        }

        dragging = false;

        ringEvent.setSnapped(snapped);
        bus.post(ringEvent);
    }


    @NonNull
    public Transform getTransform() {
        return transform;
    }
}
