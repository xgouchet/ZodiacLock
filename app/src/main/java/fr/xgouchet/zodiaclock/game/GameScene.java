package fr.xgouchet.zodiaclock.game;

import android.support.annotation.NonNull;

import com.squareup.otto.Bus;

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
import fr.xgouchet.zodiaclock.game.behaviors.InteractiveDisc;
import fr.xgouchet.zodiaclock.game.behaviors.InteractiveRing;
import fr.xgouchet.zodiaclock.game.shapes.DiscShape;

/**
 * @author Xavier Gouchet
 */
public class GameScene extends EntityAggregator {

    @NonNull
    private final Bus bus;

    private InteractiveRing innerRing, middleRing, outerRing;

    public GameScene(@NonNull Bus bus) {
        this.bus = bus;
        add(createBackgtround());
        add(createCamera());
        add(createLights());

        add(createRings());
        add(createCentralButton());
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

        innerRing = new InteractiveRing(bus, InteractiveRing.RING_ID_INNER, 1.25f, 0.75f);
        ringsGroup.add(innerRing);

        middleRing = new InteractiveRing(bus, InteractiveRing.RING_ID_MIDDLE, 2.25f, 0.75f);
        ringsGroup.add(middleRing);

        outerRing = new InteractiveRing(bus, InteractiveRing.RING_ID_OUTER, 3.25f, 0.75f);
        ringsGroup.add(outerRing);

        return ringsGroup;
    }

    private Entity createCentralButton() {
        return new InteractiveDisc(bus, 0.65f);
    }


}
