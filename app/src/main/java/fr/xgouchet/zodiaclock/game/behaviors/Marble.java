package fr.xgouchet.zodiaclock.game.behaviors;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import fr.xgouchet.zodiaclock.R;
import fr.xgouchet.zodiaclock.engine.GLException;
import fr.xgouchet.zodiaclock.engine.entities.Entity;
import fr.xgouchet.zodiaclock.engine.rendering.Material;
import fr.xgouchet.zodiaclock.engine.rendering.RenderContext;
import fr.xgouchet.zodiaclock.engine.rendering.Transform;
import fr.xgouchet.zodiaclock.game.shapes.DiscShape;

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

    public Marble(int step, float radius, DiscShape shape, @ColorRes int color) {
        transform = new Transform();
        float angle = step * Constants.STEP_ANGLE;
        transform.translateTo((float) (cos(angle) * radius), (float) (sin(angle) * radius), 0.15f);
        material = new Material(color, R.color.white);
        discShape = shape;
    }

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
}
