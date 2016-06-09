package fr.xgouchet.zodiaclock.engine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;

import static fr.xgouchet.zodiaclock.engine.Utils.checkGlError;


/**
 * @author Xavier Gouchet
 */
public class Texture extends IEntity {

    public static final int TYPE_DIFFUSE = 0;
    public static final int TYPE_NORMAL = 1;

    @IntDef({TYPE_DIFFUSE, TYPE_NORMAL})
    public @interface Type {
    }

    @DrawableRes
    private final int textureId;

    @Type
    private final int textureType;

    private int mTextureHandle;

    public Texture(@DrawableRes int textureId) {
        this(textureId, TYPE_DIFFUSE);
    }

    public Texture(@DrawableRes int textureId, @Type int textureType) {
        this.textureId = textureId;
        this.textureType = textureType;
    }

    @Override
    public void onPrepare(Context context) throws GLException {

        // generate handle
        final int[] textureHandle = new int[1];
        GLES20.glGenTextures(1, textureHandle, 0);
        checkGlError();
        mTextureHandle = textureHandle[0];

        if (mTextureHandle == 0) {
            return;
        }

        // Read in the resource
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), textureId);

        // Bind to the texture in OpenGL
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureHandle);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        // Recycle the bitmap, since its data has been loaded into OpenGL.
        bitmap.recycle();
    }

    @Override
    public void onDraw(RenderContext renderContext) throws GLException {
        int textureSlot, textureSlotId, uniformHandle;
        switch (textureType) {
            case TYPE_DIFFUSE:
                textureSlot = 0;
                textureSlotId = GLES20.GL_TEXTURE0;
                uniformHandle = renderContext.uniformDiffuseTexture;
                break;
            case TYPE_NORMAL:
                textureSlot = 1;
                textureSlotId = GLES20.GL_TEXTURE1;
                uniformHandle = renderContext.uniformNormalTexture;
                break;
            default:
                return;
        }

        // Set the active texture slot
        GLES20.glActiveTexture(textureSlotId);
        checkGlError();
        // bind the texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureHandle);
        checkGlError();

        // bind to the uniform handle
        GLES20.glUniform1i(uniformHandle, textureSlot);
        checkGlError();
    }
}
