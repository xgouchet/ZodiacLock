package fr.xgouchet.zodiaclock.engine;

import android.content.Context;

/**
 * @author Xavier Gouchet
 */
public abstract class IEntity {


    private boolean prepared;

    public final void prepare(Context context) throws GLException {
        if (prepared) return;

        onPrepare(context);
        prepared = true;
    }

    public abstract void onPrepare(Context context) throws GLException;

    public abstract void onDraw(RenderContext renderContext) throws GLException;
}
