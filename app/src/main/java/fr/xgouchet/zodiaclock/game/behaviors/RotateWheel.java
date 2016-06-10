package fr.xgouchet.zodiaclock.game.behaviors;

import android.content.Context;
import android.support.annotation.NonNull;

import fr.xgouchet.zodiaclock.engine.GLException;
import fr.xgouchet.zodiaclock.engine.entities.EntityDecorator;
import fr.xgouchet.zodiaclock.engine.rendering.RenderContext;
import fr.xgouchet.zodiaclock.engine.rendering.Transform;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * @author Xavier Gouchet
 */
public class RotateWheel extends EntityDecorator<Transform> {

    private float angle;

    public RotateWheel(@NonNull Transform decorated) {
        super(decorated);
    }

    @Override
    protected void onDecoratedWillPrepare(@NonNull Context context, @NonNull Transform decorated) throws GLException {
    }

    @Override
    protected void onDecoratedPrepared(@NonNull Context context, @NonNull Transform decorated) throws GLException {
    }

    @Override
    protected boolean decoratorNeedsUpdate() {
        return true;
    }

    @Override
    protected void onDecoratedWillUpdate(long deltaNanos, long timeMs, @NonNull Transform decorated) {
        angle = timeMs / 1000.0f;

        decorated.setOrientation(
                (float) cos(angle), (float) sin(angle), 0f,
                0f, 1f, 0f
        );
        // TODO Rotate
    }

    @Override
    protected void onDecoratedUpdated(long deltaNanos, long timeMs, @NonNull Transform decorated) {
    }

    @Override
    protected boolean decoratorNeedsRender() {
        return false;
    }

    @Override
    protected void onDecoratedWillRender(@NonNull RenderContext renderContext, @NonNull Transform decorated) throws GLException {
    }

    @Override
    protected void onDecoratedRendered(@NonNull RenderContext renderContext, @NonNull Transform decorated) throws GLException {
    }
}
