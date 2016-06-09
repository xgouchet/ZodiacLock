package fr.xgouchet.zodiaclock.game.behaviors;

import android.content.Context;
import android.support.annotation.NonNull;

import fr.xgouchet.zodiaclock.engine.EntityDecorator;
import fr.xgouchet.zodiaclock.engine.GLException;
import fr.xgouchet.zodiaclock.engine.RenderContext;
import fr.xgouchet.zodiaclock.engine.Transform;

/**
 * @author Xavier Gouchet
 */
public class RotateWheel extends EntityDecorator<Transform> {

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
