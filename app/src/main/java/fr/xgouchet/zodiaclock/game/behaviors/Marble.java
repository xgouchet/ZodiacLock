package fr.xgouchet.zodiaclock.game.behaviors;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import fr.xgouchet.zodiaclock.R;
import fr.xgouchet.zodiaclock.engine.GLException;
import fr.xgouchet.zodiaclock.engine.entities.Entity;
import fr.xgouchet.zodiaclock.engine.rendering.Material;
import fr.xgouchet.zodiaclock.engine.rendering.RenderContext;
import fr.xgouchet.zodiaclock.engine.rendering.Transform;
import fr.xgouchet.zodiaclock.game.shapes.DiscShape;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * @author Xavier Gouchet
 */
public class Marble extends Entity {

    @NonNull
    private final Material material;
    @NonNull
    private final Transform transform;
    @NonNull
    private final DiscShape discShape;

    @IntRange(from = 0, to = Constants.STEP_COUNT - 1)
    private final int originalStepAngle;
    @IntRange(from = 0, to = Constants.STEP_COUNT - 1)
    private int stepAngle;
    @Constants.RingId
    private int ringId;

    private float radius, snappedRadius, angle;

    public Marble(@Constants.RingId int ringId, @IntRange(from = 0, to = Constants.STEP_COUNT - 1) int stepAngle, @NonNull DiscShape shape) {
        this.stepAngle = stepAngle;
        this.ringId = ringId;
        originalStepAngle = stepAngle;

        snappedRadius = radius = Constants.getRingRadius(ringId);
        angle = stepAngle * Constants.STEP_ANGLE;
        transform = new Transform();
        transform.translateTo((float) (cos(angle) * radius), (float) (sin(angle) * radius), 0.05f);

        material = new Material(Constants.getColor(stepAngle), R.color.white);

        discShape = shape;
    }

    //region Entity
    @Override
    public void onPrepare(@NonNull Context context) throws GLException {
        transform.onPrepare(context);
        material.onPrepare(context);
        if (!discShape.isPrepared()) {
            discShape.onPrepare(context);
        }
    }

    @Override
    public boolean needsUpdate() {
        return true;
    }

    @Override
    public void onUpdate(long deltaNanos, long timeMs) {
        if (abs(radius - snappedRadius) > 0.0001f) {
            radius = radius + ((snappedRadius - radius) * .15f);
            transform.translateTo((float) (cos(angle) * radius), (float) (sin(angle) * radius), 0.05f);
        }
        transform.onUpdate(deltaNanos, timeMs);
    }

    @Override
    public boolean needsRender() {
        return true;
    }

    @Override
    public void onRender(@NonNull RenderContext renderContext) throws GLException {


        material.onRender(renderContext);
        transform.onRender(renderContext);
        discShape.onRender(renderContext);
    }

    public void setParentTransform(@Nullable Transform parent) {
        this.transform.setParent(parent);
    }
    //endregion

    @IntRange(from = 0, to = Constants.STEP_COUNT - 1)
    public int getStepAngle() {
        return stepAngle;
    }

    @IntRange(from = 0, to = Constants.STEP_COUNT - 1)
    public int getOriginalStepAngle() {
        return originalStepAngle;
    }

    @Constants.RingId
    public int getRingId() {
        return ringId;
    }

    public void swap(@Constants.RingId int ringId,
                     @IntRange(from = 0, to = Constants.STEP_COUNT - 1) int stepAngle) {
        radius = Constants.getRingRadius(this.ringId);
        snappedRadius = Constants.getRingRadius(ringId);
        angle = stepAngle * Constants.STEP_ANGLE;
        transform.translateTo((float) (cos(angle) * radius), (float) (sin(angle) * radius), 0.05f);

        this.stepAngle = stepAngle;
        this.ringId = ringId;
    }

    public void setHighlighted(boolean highlighted) {
        material.setEmissive(highlighted);
    }
}
