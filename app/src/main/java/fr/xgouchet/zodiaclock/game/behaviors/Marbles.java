package fr.xgouchet.zodiaclock.game.behaviors;

import android.content.Context;
import android.support.annotation.NonNull;

import fr.xgouchet.zodiaclock.R;
import fr.xgouchet.zodiaclock.engine.GLException;
import fr.xgouchet.zodiaclock.engine.entities.Entity;
import fr.xgouchet.zodiaclock.engine.entities.EntityAggregator;
import fr.xgouchet.zodiaclock.engine.rendering.RenderContext;
import fr.xgouchet.zodiaclock.engine.rendering.Shader;
import fr.xgouchet.zodiaclock.engine.rendering.Texture;
import fr.xgouchet.zodiaclock.engine.rendering.Transform;
import fr.xgouchet.zodiaclock.game.shapes.DiscShape;

/**
 * @author Xavier Gouchet
 */
public class Marbles extends Entity {

    private final EntityAggregator entities = new EntityAggregator();


    public Marbles(Transform inner, Transform middle, Transform outer) {

        entities.add(new Shader(R.raw.hexa_vs, R.raw.hexa_fs));
        entities.add(new Texture(R.drawable.debug, Texture.TYPE_DIFFUSE));
        entities.add(new Texture(R.drawable.flat_normal, Texture.TYPE_NORMAL));

        DiscShape shape = new DiscShape(Constants.MARBLE_RADIUS, 1);

        for (int i = 0; i < 12; i += 2) {
            Marble marble = new Marble(i, Constants.RING_RADIUS_OUTER, shape, R.color.blue);
            marble.setParentTransform(outer);
            entities.add(marble);
        }

    }

    @Override
    public void onPrepare(@NonNull Context context) throws GLException {
        entities.onPrepare(context);
    }

    @Override
    public boolean needsUpdate() {
        return true;
    }

    @Override
    public void onUpdate(long deltaNanos, long timeMs) {
        entities.onUpdate(deltaNanos, timeMs);
    }

    @Override
    public boolean needsRender() {
        return true;
    }

    @Override
    public void onRender(@NonNull RenderContext renderContext) throws GLException {
        entities.onRender(renderContext);
    }
}
