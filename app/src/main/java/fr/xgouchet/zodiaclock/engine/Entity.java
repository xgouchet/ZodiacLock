package fr.xgouchet.zodiaclock.engine;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * @author Xavier Gouchet
 */
public abstract class Entity {


    private boolean prepared;

    public final void prepare(@NonNull Context context) throws GLException {
        if (prepared) return;

        onPrepare(context);
        prepared = true;
    }

    public abstract void onPrepare(@NonNull Context context) throws GLException;

    public abstract void onUpdate();

    public abstract void onRender(@NonNull RenderContext renderContext) throws GLException;

    public abstract boolean needsRender();

    public abstract boolean needsUpdate();
}
