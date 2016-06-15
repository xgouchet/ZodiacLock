package fr.xgouchet.zodiaclock.game.behaviors;

import android.content.Context;
import android.support.annotation.NonNull;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import fr.xgouchet.zodiaclock.R;
import fr.xgouchet.zodiaclock.engine.GLException;
import fr.xgouchet.zodiaclock.engine.entities.Entity;
import fr.xgouchet.zodiaclock.engine.entities.EntityAggregator;
import fr.xgouchet.zodiaclock.engine.rendering.RenderContext;
import fr.xgouchet.zodiaclock.engine.rendering.Shader;
import fr.xgouchet.zodiaclock.engine.rendering.Texture;
import fr.xgouchet.zodiaclock.engine.rendering.Transform;
import fr.xgouchet.zodiaclock.events.RingEvent;
import fr.xgouchet.zodiaclock.events.SwapMarblesEvent;
import fr.xgouchet.zodiaclock.game.shapes.DiscShape;

/**
 * @author Xavier Gouchet
 */
public class Marbles extends Entity {

    private final EntityAggregator entities = new EntityAggregator();
    private final List<Marble> marbles = new ArrayList<>();

    private final int[] ringAngles = new int[]{0, 0, 0};
    private final int[] swapAngles;
    private final Transform[] ringTransforms;


    public Marbles(@NonNull Bus bus, int[] swapAngles, @NonNull Transform[] ringTransforms) {
        this.swapAngles = swapAngles;
        this.ringTransforms = ringTransforms;

        entities.add(new Shader(R.raw.hexa_vs, R.raw.hexa_fs));
        entities.add(new Texture(R.drawable.scratched_metal_shutterstock, Texture.TYPE_DIFFUSE));
        entities.add(new Texture(R.drawable.marble_normal_low, Texture.TYPE_NORMAL));

        DiscShape shape = new DiscShape(Constants.MARBLE_RADIUS, .75f / Constants.MARBLE_RADIUS);

        generateRing(shape, Constants.RING_ID_INNER);
        generateRing(shape, Constants.RING_ID_MIDDLE);
        generateRing(shape, Constants.RING_ID_OUTER);

        bus.register(this);

    }

    private void generateRing(DiscShape shape, @Constants.RingId int ringId) {
        for (int step = 0; step < 12; step += 2) {
            Marble marble = new Marble(ringId, step, shape);
            marble.setParentTransform(ringTransforms[ringId]);
            entities.add(marble);
            marbles.add(marble);
        }
    }


    //region Entity
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
    //endregion

    @Subscribe
    public void onSwapEvent(@NonNull SwapMarblesEvent event) {
        for (Marble marble : marbles) {
            int stepAngle = (marble.getStepAngle() + ringAngles[marble.getRingId()]) % Constants.STEP_COUNT;
            if (isSwapAngle(stepAngle)) {
                swapMarble(marble, stepAngle);
            }
        }
    }

    private boolean isSwapAngle(int stepAngle) {
        for (int swapAngle : swapAngles) {
            if (swapAngle == stepAngle) return true;
        }
        return false;
    }

    private void swapMarble(Marble marble, int stepAngle) {
        @Constants.RingId
        int nextRing = Constants.nextRingId(marble.getRingId());
        int newAngle = (Constants.STEP_COUNT + stepAngle - ringAngles[nextRing]) % Constants.STEP_COUNT;
        marble.swap(nextRing, newAngle);
        marble.setParentTransform(ringTransforms[nextRing]);
    }

    @Subscribe
    public void onRingEvent(@NonNull RingEvent event) {
        ringAngles[event.getRingId()] = event.getSnapped();
    }

}
