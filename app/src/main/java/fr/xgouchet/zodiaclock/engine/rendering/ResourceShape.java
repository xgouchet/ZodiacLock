package fr.xgouchet.zodiaclock.engine.rendering;

import android.content.Context;
import android.support.annotation.NonNull;

import fr.xgouchet.zodiaclock.engine.GLException;
import fr.xgouchet.zodiaclock.engine.entities.Entity;

/**
 * @author Xavier Gouchet
 */
public class ResourceShape extends Entity {


    //region Entity
    @Override
    public void onPrepare(@NonNull Context context) throws GLException {

    }

    @Override
    public boolean needsUpdate() {
        return false;
    }

    @Override
    public void onUpdate(long deltaNanos, long timeMs) {

    }

    @Override
    public boolean needsRender() {
        return false;
    }

    @Override
    public void onRender(@NonNull RenderContext renderContext) throws GLException {

    }
    //endregion
}
