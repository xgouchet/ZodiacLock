package fr.xgouchet.zodiaclock.game;

import android.support.annotation.NonNull;

import com.squareup.otto.Bus;

import fr.xgouchet.zodiaclock.R;
import fr.xgouchet.zodiaclock.engine.entities.Entity;
import fr.xgouchet.zodiaclock.engine.entities.EntityAggregator;
import fr.xgouchet.zodiaclock.engine.environment.Background;
import fr.xgouchet.zodiaclock.engine.environment.Camera;
import fr.xgouchet.zodiaclock.engine.environment.Light;
import fr.xgouchet.zodiaclock.engine.rendering.Material;
import fr.xgouchet.zodiaclock.engine.rendering.Shader;
import fr.xgouchet.zodiaclock.engine.rendering.Texture;
import fr.xgouchet.zodiaclock.engine.rendering.Transform;
import fr.xgouchet.zodiaclock.game.behaviors.Constants;
import fr.xgouchet.zodiaclock.game.behaviors.FlickeringLight;
import fr.xgouchet.zodiaclock.game.behaviors.InteractiveDisc;
import fr.xgouchet.zodiaclock.game.behaviors.InteractiveRing;
import fr.xgouchet.zodiaclock.game.behaviors.Marbles;
import fr.xgouchet.zodiaclock.game.shapes.GuideShape;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * @author Xavier Gouchet
 */
public class GameScene extends EntityAggregator {

    @NonNull
    private final Bus bus;

    private InteractiveRing innerRing, middleRing, outerRing;

    @NonNull
    private final int[] swapAngles = new int[]{5, 6, 7};
    private final int gap = 2;

    public GameScene(@NonNull Bus bus) {
        this.bus = bus;
        add(createBackgtround());
        add(createCamera());
        add(createLights());

        add(createRings());
        add(createCentralButton());

        add(createMarbles());

        add(createGuides());
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
        ringsGroup.add(new Texture(R.drawable.rings_color, Texture.TYPE_DIFFUSE));
        ringsGroup.add(new Texture(R.drawable.flat_normal, Texture.TYPE_NORMAL));

        innerRing = new InteractiveRing(bus, Constants.RING_ID_INNER, 1.25f);
        ringsGroup.add(innerRing);

        middleRing = new InteractiveRing(bus, Constants.RING_ID_MIDDLE, 2.25f);
        ringsGroup.add(middleRing);

        outerRing = new InteractiveRing(bus, Constants.RING_ID_OUTER, 3.25f);
        ringsGroup.add(outerRing);

        return ringsGroup;
    }

    private Entity createCentralButton() {
        return new InteractiveDisc(bus, 0.65f);
    }

    private Entity createMarbles() {

        final Marbles marbles = new Marbles(bus,
                swapAngles,
                new Transform[]{
                        innerRing.getTransform(),
                        middleRing.getTransform(),
                        outerRing.getTransform()},
                gap);
        marbles.scramble();
        return marbles;
    }

    private Entity createGuides() {
        EntityAggregator group = new EntityAggregator();

        group.add(new Shader(R.raw.hexa_vs, R.raw.hexa_fs));
        group.add(new Texture(R.drawable.rings_color, Texture.TYPE_DIFFUSE));
        group.add(new Texture(R.drawable.sand_normal, Texture.TYPE_NORMAL));

        GuideShape shape = new GuideShape();

        for (int swapAngle : swapAngles) {
            float angle = swapAngle * Constants.STEP_ANGLE;
            EntityAggregator guide = new EntityAggregator();

            Transform transform = new Transform();
            transform.translateTo(0, 0, -0.25f);
            transform.setOrientation(
                    (float) cos(angle), (float) sin(angle), 0,
                    (float) -sin(angle), (float) cos(angle), 0);

            guide.add(new Material(R.color.gold_diff, R.color.gold_spec));
            guide.add(transform);
            guide.add(shape);
            group.add(guide);
        }

        return group;
    }

}
