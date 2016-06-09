package fr.xgouchet.zodiaclock.engine;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static fr.xgouchet.zodiaclock.engine.Utils.checkGlError;

/**
 * @author Xavier Gouchet
 */
public abstract class FixedShape extends IEntity {

    public static final int BYTES_PER_FLOAT = Float.SIZE / Byte.SIZE;

    private final int verticesCount;
    private final int vertexSize;
    private final int vertexStrideBytes;
    private final int renderMode;
    private FloatBuffer verticesBuffer;

    public FixedShape(int verticesCount, int vertexSize, int renderMode) {

        this.verticesCount = verticesCount;
        this.vertexSize = vertexSize;
        this.vertexStrideBytes = vertexSize * BYTES_PER_FLOAT;
        this.renderMode = renderMode;
    }

    @Override
    public final void onPrepare(Context context) {
        verticesBuffer = ByteBuffer.allocateDirect(verticesCount * vertexSize * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        verticesBuffer.put(getVerticesData());
        verticesBuffer.position(0);
    }

    @Override
    public final void onDraw(RenderContext renderContext) throws GLException {

        if (renderContext.attrVertexPosition != -1) {
            if (getVertexPositionDataSize() > 0) {
                verticesBuffer.position(getVertexPositionDataOffset());
                GLES20.glVertexAttribPointer(renderContext.attrVertexPosition,
                        getVertexPositionDataSize(),
                        GLES20.GL_FLOAT,
                        false,
                        vertexStrideBytes,
                        verticesBuffer);
                checkGlError();
                GLES20.glEnableVertexAttribArray(renderContext.attrVertexPosition);
                checkGlError();
            } else {
                GLES20.glDisableVertexAttribArray(renderContext.attrVertexPosition);
                checkGlError();
            }
        }

        if (renderContext.attrVertexTexCoords != -1) {
            if (getVertexTexCoordsDataSize() > 0) {
                verticesBuffer.position(getVertexTexCoordsDataOffset());
                GLES20.glVertexAttribPointer(renderContext.attrVertexTexCoords,
                        getVertexTexCoordsDataSize(),
                        GLES20.GL_FLOAT,
                        false,
                        vertexStrideBytes,
                        verticesBuffer);
                checkGlError();
                GLES20.glEnableVertexAttribArray(renderContext.attrVertexTexCoords);
                checkGlError();
            } else {
                GLES20.glDisableVertexAttribArray(renderContext.attrVertexTexCoords);
                checkGlError();
            }
        }

        GLES20.glDrawArrays(renderMode, 0, verticesCount);
        checkGlError();
    }

    protected abstract float[] getVerticesData();

    protected abstract int getVertexPositionDataOffset();

    protected abstract int getVertexPositionDataSize();

    protected abstract int getVertexTexCoordsDataOffset();

    protected abstract int getVertexTexCoordsDataSize();

}
