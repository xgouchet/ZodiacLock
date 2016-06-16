package fr.xgouchet.zodiaclock.game.shapes;

import android.opengl.GLES20;

import fr.xgouchet.zodiaclock.engine.rendering.GeneratedShape;
import fr.xgouchet.zodiaclock.game.behaviors.Constants;

/**
 * @author Xavier Gouchet
 */
public class GuideShape extends GeneratedShape {

    public static final int STEPS = 4;
    private static final int VERTICES_COUNT = (STEPS * 2);


    private static final int VERTEX_POS_SIZE = 3;
    private static final int VERTEX_TEXCOORDS_SIZE = 2;
    private static final int VERTEX_SIZE = VERTEX_POS_SIZE + VERTEX_TEXCOORDS_SIZE;

    public GuideShape() {
        super(VERTICES_COUNT, VERTEX_SIZE, GLES20.GL_TRIANGLE_STRIP);
    }

    @Override
    protected float[] getVerticesData() {
        float[] data = new float[VERTICES_COUNT * VERTEX_SIZE];

        float stepLength = (Constants.GUIDE_LENGTH / (STEPS - 1));
        float texCoordsStepLength = 1.0f / (STEPS - 1);
        int index = 0;

        float angle = 0;
        for (int s = 0; s < STEPS; ++s) {
            // Pos
            data[index++] = stepLength * s;
            data[index++] = Constants.RING_HALF_THICKNESS;
            data[index++] = 0;
            // Tex coords
            data[index++] = texCoordsStepLength * s;
            data[index++] = 0;

            // Pos
            data[index++] = stepLength * s;
            data[index++] = -Constants.RING_HALF_THICKNESS;
            data[index++] = 0;
            // Tex coords
            data[index++] = texCoordsStepLength * s;
            data[index++] = 1;
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
