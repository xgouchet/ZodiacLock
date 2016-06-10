package fr.xgouchet.zodiaclock.game;

import java.util.ArrayList;
import java.util.List;

import fr.xgouchet.zodiaclock.R;
import fr.xgouchet.zodiaclock.engine.entities.Entity;
import fr.xgouchet.zodiaclock.engine.entities.EntityAggregator;
import fr.xgouchet.zodiaclock.engine.environment.Background;
import fr.xgouchet.zodiaclock.engine.environment.Camera;
import fr.xgouchet.zodiaclock.engine.environment.Light;
import fr.xgouchet.zodiaclock.engine.rendering.Shader;
import fr.xgouchet.zodiaclock.engine.rendering.Texture;
import fr.xgouchet.zodiaclock.engine.rendering.Transform;
import fr.xgouchet.zodiaclock.game.behaviors.FlickeringLight;
import fr.xgouchet.zodiaclock.game.behaviors.InteractiveRing;
import fr.xgouchet.zodiaclock.game.behaviors.TouchListener;
import fr.xgouchet.zodiaclock.game.shapes.DiscShape;

/**
 * @author Xavier Gouchet
 */
public class GameScene extends EntityAggregator implements TouchListener {

    List<TouchListener> touchListeners = new ArrayList<>();

    private InteractiveRing innerRing, middleRing, outerRing;

    public GameScene() {

        add(createBackgtround());
        add(createCamera());
        add(createLights());

        add(createRings());
        add(createMarbles());
    }

    private Entity createMarbles() {
        EntityAggregator marbles = new EntityAggregator();
        marbles.add(new Transform());
        marbles.add(new DiscShape(0.65f, 1 / 4.0f));
        return marbles;
    }

    private Entity createBackgtround() {
        return new Background();
    }

    private Entity createLights() {
        Light light = new Light();
        light.translateTo(-15, 25, 5);
        return new FlickeringLight(light);
//        return light;
    }

    private Entity createCamera() {
        Camera camera = new Camera();
        camera.moveTo(0, 0, 5.5f);
        camera.moveTargetTo(0, 0, 0f);
        return camera;
    }

    private Entity createRings() {
        EntityAggregator ringsGroup = new EntityAggregator();

        ringsGroup.add(new Shader(R.raw.hexa_vs, R.raw.hexa_fs));
        ringsGroup.add(new Texture(R.drawable.debug, Texture.TYPE_DIFFUSE));
        ringsGroup.add(new Texture(R.drawable.sand_normal, Texture.TYPE_NORMAL));

        innerRing = new InteractiveRing(1.25f, 0.75f);
        ringsGroup.add(innerRing);
        touchListeners.add(innerRing);

        middleRing = new InteractiveRing(2.25f, 0.75f);
        ringsGroup.add(middleRing);
        touchListeners.add(middleRing);

        outerRing = new InteractiveRing(3.25f, 0.75f);
        ringsGroup.add(outerRing);
        touchListeners.add(outerRing);

        return ringsGroup;
    }


    @Override
    public void onTouchDown(float[] position) {
        for (TouchListener listener : touchListeners) {
            listener.onTouchDown(position);
        }
    }

    @Override
    public void onTouchMove(float[] position) {
        for (TouchListener listener : touchListeners) {
            listener.onTouchMove(position);
        }
    }

    @Override
    public void onTouchUp(float[] position) {
        for (TouchListener listener : touchListeners) {
            listener.onTouchUp(position);
        }
    }
}
