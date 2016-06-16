package fr.xgouchet.zodiaclock.game.shapes;

import android.opengl.GLES20;

import fr.xgouchet.zodiaclock.engine.rendering.GeneratedShape;

/**
 * @author Xavier Gouchet
 */
public class LockShape extends GeneratedShape {

    private static final int VERTICES_COUNT = 3;

    private static final int VERTEX_POS_SIZE = 3;
    private static final int VERTEX_TEXCOORDS_SIZE = 2;

    private static final int VERTEX_SIZE = VERTEX_POS_SIZE + VERTEX_TEXCOORDS_SIZE;

    public LockShape() {
        super(VERTICES_COUNT, VERTEX_SIZE, GLES20.GL_TRIANGLES);
    }

    @Override
    protected float[] getVerticesData() {
        float[] data = new float[VERTICES_COUNT * VERTEX_SIZE];

        int index = 0;

        // Pos
        data[index++] = 0.15f;
        data[index++] = 0;
        data[index++] = 0;
        // Tex coords
        data[index++] = 0.5f;
        data[index++] = 0;

        // Pos
        data[index++] = 0.35f;
        data[index++] = -0.25f;
        data[index++] = 0;
        // Tex coords
        data[index++] = 0;
        data[index++] = 1;

        // Pos
        data[index++] = 0.35f;
        data[index++] = 0.25f;
        data[index++] = 0;
        // Tex coords
        data[index++] = 1;
        data[index++] = 1;

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
