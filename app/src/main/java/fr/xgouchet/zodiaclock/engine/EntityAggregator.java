package fr.xgouchet.zodiaclock.engine;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Xavier Gouchet
 */
public class EntityAggregator extends Entity {

    private final List<Entity> mEntities = new LinkedList<>();

    private boolean needsUpdate = false;
    private boolean needsRender = false;

    public EntityAggregator(Entity... entities) {
        if (entities != null) {
            for (Entity entity : entities) {
                add(entity);
            }
        }
    }

    public void add(Entity entity) {
        needsUpdate |= entity.needsUpdate();
        needsRender |= entity.needsRender();

        mEntities.add(entity);
    }

    @Override
    public void onPrepare(@NonNull Context context) throws GLException {
        for (Entity entity : mEntities) {
            entity.prepare(context);
        }
    }

    @Override
    public void onUpdate(long deltaNanos, long timeMs) {
        for (Entity entity : mEntities) {
            if (entity.needsUpdate()) entity.onUpdate(deltaNanos, timeMs);
        }
    }

    @Override
    public void onRender(@NonNull RenderContext renderContext) throws GLException {
        for (Entity entity : mEntities) {
            if (entity.needsRender()) entity.onRender(renderContext);
        }
    }

    @Override
    public boolean needsUpdate() {
        return needsUpdate;
    }

    @Override
    public boolean needsRender() {
        return needsRender;
    }
}
