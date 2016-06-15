package fr.xgouchet.zodiaclock.engine.entities;

import android.content.Context;
import android.support.annotation.NonNull;

import fr.xgouchet.zodiaclock.engine.GLException;
import fr.xgouchet.zodiaclock.engine.rendering.RenderContext;

/**
 * @author Xavier Gouchet
 */
public abstract class Entity {


    private boolean prepared;

    public boolean isPrepared() {
        return prepared;
    }

    public final void prepare(@NonNull Context context) throws GLException {
        if (prepared) return;

        onPrepare(context);
        prepared = true;
    }

    public abstract void onPrepare(@NonNull Context context) throws GLException;

    public abstract boolean needsUpdate();

    public abstract void onUpdate(long deltaNanos, long timeMs);

    public abstract boolean needsRender();

    public abstract void onRender(@NonNull RenderContext renderContext) throws GLException;

}
