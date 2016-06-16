package fr.xgouchet.zodiaclock.engine.rendering;

import android.content.Context;
import android.opengl.GLES20;
import android.support.annotation.NonNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import fr.xgouchet.zodiaclock.engine.GLException;
import fr.xgouchet.zodiaclock.engine.entities.Entity;

import static fr.xgouchet.zodiaclock.engine.GLException.checkGlError;


/**
 * @author Xavier Gouchet
 */
public abstract class GeneratedShape extends Entity {

    public static final int BYTES_PER_FLOAT = Float.SIZE / Byte.SIZE;

    private final int verticesCount;
    private final int vertexSize;
    private final int vertexStrideBytes;
    private final int renderMode;
    private FloatBuffer verticesBuffer;

    /**
     * @param verticesCount the number of vertices in the shape
     * @param vertexSize    the size of each vertex (that is, the number of float components in each vertex)
     * @param renderMode    the mode to use when rendering the shape (GL_POINTS, GL_LINE_STRIP,
     *                      GL_LINE_LOOP, GL_LINES, GL_TRIANGLE_STRIP, GL_TRIANGLE_FAN, GL_TRIANGLES)
     */
    public GeneratedShape(int verticesCount, int vertexSize, int renderMode) {
        this.verticesCount = verticesCount;
        this.vertexSize = vertexSize;
        this.vertexStrideBytes = vertexSize * BYTES_PER_FLOAT;
        this.renderMode = renderMode;
    }

    @Override
    public final void onPrepare(@NonNull Context context) {
        verticesBuffer = ByteBuffer.allocateDirect(verticesCount * vertexSize * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        verticesBuffer.put(getVerticesData());
        verticesBuffer.position(0);
    }

    @Override
    public boolean needsUpdate() {
        return false;
    }

    @Override
    public void onUpdate(long deltaNanos, long timeMs) {
    }

    @Override
    public boolean needsRender() {
        return true;
    }

    @Override
    public final void onRender(@NonNull RenderContext renderContext) throws GLException {

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
