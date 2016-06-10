package fr.xgouchet.zodiaclock.game.shapes;

import android.opengl.GLES20;

import fr.xgouchet.zodiaclock.engine.rendering.FixedShape;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * @author Xavier Gouchet
 */
public class RingShape extends FixedShape {

    public static final int STEPS = 64;
    public static final float ANGLE_STEP = (float) ((Math.PI * 2) / (STEPS - 1));
    private static final int VERTICES_COUNT = STEPS * 2;


    private static final int VERTEX_POS_SIZE = 3;
    private static final int VERTEX_TEXCOORDS_SIZE = 2;
    private static final int VERTEX_SIZE = VERTEX_POS_SIZE + VERTEX_TEXCOORDS_SIZE;

    private final float innerRadius;
    private final float outerRadius;
    private final float texCoordsScale;

    public RingShape(float radius, float thickness, float texCoordsScale) {
        super(VERTICES_COUNT, VERTEX_SIZE, GLES20.GL_TRIANGLE_STRIP);
        this.innerRadius = radius - (thickness / 2.0f);
        this.outerRadius = radius + (thickness / 2.0f);
        this.texCoordsScale = texCoordsScale;
    }

    @Override
    protected float[] getVerticesData() {
        float[] data = new float[VERTICES_COUNT * VERTEX_SIZE];

        int index = 0;
        float angle = 0;
        for (int s = 0; s < STEPS; ++s) {
            // Pos
            data[index++] = (float) (cos(angle) * innerRadius);
            data[index++] = (float) (sin(angle) * innerRadius);
            data[index++] = 0;
            // Tex coords
            data[index++] = (float) ((cos(angle) * innerRadius * texCoordsScale) + 1) / 2.0f;
            data[index++] = -(float) ((sin(angle) * innerRadius * texCoordsScale) + 1) / 2.0f;

            // Pos
            data[index++] = (float) (cos(angle) * outerRadius);
            data[index++] = (float) (sin(angle) * outerRadius);
            data[index++] = 0;
            // Tex coords
            data[index++] = (float) ((cos(angle) * outerRadius * texCoordsScale) + 1) / 2.0f;
            data[index++] = -(float) ((sin(angle) * outerRadius * texCoordsScale) + 1) / 2.0f;

            angle += ANGLE_STEP;
        }

        return data;
    }

    @Override
    protected int getVertexPositionDataOffset() {
        return 0;
    }

    @Override
    protected int getVertexPositionDataSize() {
        return VERTEX_POS_SIZE;
    }

    @Override
    protected int getVertexTexCoordsDataOffset() {
        return VERTEX_POS_SIZE;
    }

    @Override
    protected int getVertexTexCoordsDataSize() {
        return VERTEX_TEXCOORDS_SIZE;
    }
}
