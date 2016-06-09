package fr.xgouchet.zodiaclock.game;

import fr.xgouchet.zodiaclock.R;
import fr.xgouchet.zodiaclock.engine.Entity;
import fr.xgouchet.zodiaclock.engine.EntityAggregator;
import fr.xgouchet.zodiaclock.engine.Transform;
import fr.xgouchet.zodiaclock.engine.environment.Background;
import fr.xgouchet.zodiaclock.engine.environment.Camera;
import fr.xgouchet.zodiaclock.engine.environment.Light;
import fr.xgouchet.zodiaclock.engine.rendering.Shader;
import fr.xgouchet.zodiaclock.engine.rendering.Texture;
import fr.xgouchet.zodiaclock.game.behaviors.FlickeringLight;

/**
 * @author Xavier Gouchet
 */
public class GameScene extends EntityAggregator {


    public GameScene() {

        add(createBackgtround());
        add(createCamera());
        add(createLights());
        add(createRings());
    }

    private Entity createBackgtround() {
        return new Background();
    }

    private Entity createLights() {
        Light light = new Light();
        light.translateTo(-5, 5, 5);
        return new FlickeringLight(light);
    }

    private Entity createCamera() {
        Camera camera = new Camera();
        camera.moveTo(0, 0, 5f);
        camera.moveTargetTo(0, 0, 0f);
        return camera;
    }

    private Entity createRings() {
        EntityAggregator ringsGroup = new EntityAggregator();

        ringsGroup.add(new Shader(R.raw.hexa_vs, R.raw.hexa_fs));
        ringsGroup.add(new Texture(R.drawable.debug, Texture.TYPE_DIFFUSE));
        ringsGroup.add(new Texture(R.drawable.debug_normal, Texture.TYPE_NORMAL));

        // inner ring
        ringsGroup.add(
                new EntityAggregator(
                        new Transform(),
                        new RingShape(1.0f, 0.5f, 1.0f / 5.0f)
                )
        );
        // middle ring
        ringsGroup.add(
                new EntityAggregator(
                        new Transform(),
                        new RingShape(2.0f, 0.5f, 1.0f / 5.0f)
                )
        );
        // outer ring
        ringsGroup.add(
                new EntityAggregator(
                        new Transform(),
                        new RingShape(3.0f, 0.5f, 1.0f / 5.0f)
                )
        );
        return ringsGroup;
    }
}
