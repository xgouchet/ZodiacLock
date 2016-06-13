package fr.xgouchet.zodiaclock.game.behaviors;

import android.content.Context;
import android.support.annotation.NonNull;

import fr.xgouchet.zodiaclock.engine.GLException;
import fr.xgouchet.zodiaclock.engine.entities.EntityDecorator;
import fr.xgouchet.zodiaclock.engine.environment.Light;
import fr.xgouchet.zodiaclock.engine.rendering.RenderContext;

import static java.lang.Math.cos;
import static java.lang.Math.sin;


/**
 * @author Xavier Gouchet
 */
public class FlickeringLight extends EntityDecorator<Light> {

    public FlickeringLight(Light light) {
        super(light);
    }

    @Override
    protected void onDecoratedWillPrepare(@NonNull Context context, @NonNull Light decorated) throws GLException {
    }

    @Override
    protected void onDecoratedPrepared(@NonNull Context context, @NonNull Light decorated) throws GLException {
    }


    @Override
    protected boolean decoratorNeedsUpdate() {
        return true;
    }

    @Override
    protected void onDecoratedWillUpdate(long deltaNanos, long timeMs, @NonNull Light decorated) {
        double turnPerSeconds = (timeMs / 1000.0) * Math.PI * 2.0;
        decorated.translateTo(
                (float) (-50 + 2 * cos(0.25 * turnPerSeconds)),
                (float) (50 + 10 * sin(0.25 * turnPerSeconds)),
                50);
    }

    @Override
    protected void onDecoratedUpdated(long deltaNanos, long timeMs, @NonNull Light decorated) {

    }


    @Override
    protected boolean decoratorNeedsRender() {
        return false;
    }

    @Override
    protected void onDecoratedWillRender(@NonNull RenderContext renderContext, @NonNull Light decorated) throws GLException {
    }

    @Override
    protected void onDecoratedRendered(@NonNull RenderContext renderContext, @NonNull Light decorated) throws GLException {
    }
}
