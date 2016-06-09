package fr.xgouchet.zodiaclock.engine;

import android.content.Context;

/**
 * @author Xavier Gouchet
 */
public class EntityAggregator extends IEntity {

    private final IEntity[] mEntities;

    public EntityAggregator(IEntity... entities) {
        mEntities = entities;
    }

    @Override
    public void onPrepare(Context context) throws GLException {
        for (IEntity entity : mEntities) {
            entity.prepare(context);
        }
    }

    @Override
    public void onDraw(RenderContext renderContext) throws GLException {
        for (IEntity entity : mEntities) {
            entity.onDraw(renderContext);
        }
    }
}
