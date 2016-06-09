package fr.xgouchet.zodiaclock.engine;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * @author Xavier Gouchet
 */
public abstract class EntityDecorator<E extends Entity> extends Entity {

    @NonNull
    private final E decorated;

    public EntityDecorator(@NonNull E decorated) {
        this.decorated = decorated;
    }

    @Override
    public final void onPrepare(@NonNull Context context) throws GLException {
        onDecoratedWillPrepare(context, decorated);
        decorated.onPrepare(context);
        onDecoratedPrepared(context, decorated);
    }

    protected abstract void onDecoratedWillPrepare(@NonNull Context context, @NonNull E decorated) throws GLException;

    protected abstract void onDecoratedPrepared(@NonNull Context context, @NonNull E decorated) throws GLException;

    @Override
    public final boolean needsUpdate() {
        return decorated.needsUpdate() | decoratorNeedsUpdate();
    }

    protected abstract boolean decoratorNeedsUpdate();


    @Override
    public final void onUpdate(long deltaNanos, long timeMs) {
        onDecoratedWillUpdate(deltaNanos, timeMs, decorated);
        if (decorated.needsUpdate()) decorated.onUpdate(deltaNanos, timeMs);
        onDecoratedUpdated(deltaNanos, timeMs, decorated);
    }

    protected abstract void onDecoratedWillUpdate(long deltaNanos, long timeMs, @NonNull E decorated);

    protected abstract void onDecoratedUpdated(long deltaNanos, long timeMs, @NonNull E decorated);

    @Override
    public final boolean needsRender() {
        return decorated.needsRender() | decoratorNeedsRender();
    }

    protected abstract boolean decoratorNeedsRender();

    @Override
    public final void onRender(@NonNull RenderContext renderContext) throws GLException {
        onDecoratedWillRender(renderContext, decorated);
        if (decorated.needsRender()) decorated.onRender(renderContext);
        onDecoratedRendered(renderContext, decorated);
    }

    protected abstract void onDecoratedWillRender(@NonNull RenderContext renderContext, @NonNull E decorated) throws GLException;

    protected abstract void onDecoratedRendered(@NonNull RenderContext renderContext, @NonNull E decorated) throws GLException;
}
