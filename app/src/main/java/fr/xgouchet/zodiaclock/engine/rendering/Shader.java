package fr.xgouchet.zodiaclock.engine.rendering;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES20;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import fr.xgouchet.zodiaclock.engine.GLException;
import fr.xgouchet.zodiaclock.engine.entities.Entity;

import static fr.xgouchet.zodiaclock.engine.GLException.checkGlError;


/**
 * @author Xavier Gouchet
 */
public class Shader extends Entity {


    @IntDef({GLES20.GL_VERTEX_SHADER, GLES20.GL_FRAGMENT_SHADER})
    public @interface ShaderType {
    }

    private static final String TAG = Shader.class.getSimpleName();

    @RawRes
    final int vertexShaderId;
    @RawRes
    final int fragmentShaderId;

    private int programHandle;

    private int uniformModelMatrix, uniformMVPMatrix;
    private int uniformLightPosition;
    private int uniformDiffuseTexture, uniformNormalTexture;
    private int uniformDiffuseColor, uniformSpecularColor;

    private int attrVertexPosition;
    private int attrVertexTexCoords;

    public Shader(int vertexShaderId, int fragmentShaderId) {
        this.vertexShaderId = vertexShaderId;
        this.fragmentShaderId = fragmentShaderId;
    }

    @Override
    public void onPrepare(@NonNull Context context) throws GLException {
        int vsHandle = prepareShader(context, GLES20.GL_VERTEX_SHADER, vertexShaderId);
        int fsHandle = prepareShader(context, GLES20.GL_FRAGMENT_SHADER, fragmentShaderId);
        Log.i(TAG, String.format("vs : %d; fs : %d;", vsHandle, fsHandle));

        if ((vsHandle == 0) || (fsHandle == 0)) {
            return;
        }

        programHandle = prepareProgram(vsHandle, fsHandle);
        Log.i(TAG, String.format(" program : %d", programHandle));

        if (programHandle == 0) {
            return;
        }

        uniformModelMatrix = GLES20.glGetUniformLocation(programHandle, "u_ModelMatrix");
        checkGlError();
        uniformMVPMatrix = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        checkGlError();
        uniformLightPosition = GLES20.glGetUniformLocation(programHandle, "u_LightPos");
        checkGlError();
        uniformDiffuseTexture = GLES20.glGetUniformLocation(programHandle, "u_DiffuseTexture");
        checkGlError();
        uniformNormalTexture = GLES20.glGetUniformLocation(programHandle, "u_NormalTexture");
        checkGlError();

        uniformDiffuseColor = GLES20.glGetUniformLocation(programHandle, "u_DiffuseColor");
        checkGlError();
        uniformSpecularColor = GLES20.glGetUniformLocation(programHandle, "u_SpecularColor");
        checkGlError();

        attrVertexPosition = GLES20.glGetAttribLocation(programHandle, "a_Position");
        checkGlError();
        attrVertexTexCoords = GLES20.glGetAttribLocation(programHandle, "a_TexCoords");
        checkGlError();
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
    public void onRender(@NonNull RenderContext renderContext) throws GLException {
        if (programHandle == 0) {
            return;
        }

        GLES20.glUseProgram(programHandle);
        checkGlError();

        // environment
//        Log.v("Shader", "Update light position " + renderContext.uniformLightPosition
//                + " → " + Arrays.toString(renderContext.vecLightPos));
        GLES20.glUniform3fv(renderContext.uniformLightPosition, 1, renderContext.vecLightPos, 0);
        checkGlError();


        renderContext.attrVertexPosition = attrVertexPosition;
        renderContext.attrVertexTexCoords = attrVertexTexCoords;

        renderContext.uniformMVPMatrix = uniformMVPMatrix;
        renderContext.uniformModelMatrix = uniformModelMatrix;

        renderContext.uniformLightPosition = uniformLightPosition;

        renderContext.uniformDiffuseTexture = uniformDiffuseTexture;
        renderContext.uniformNormalTexture = uniformNormalTexture;

        renderContext.uniformDiffuseColor = uniformDiffuseColor;
        renderContext.uniformSpecularColor = uniformSpecularColor;
    }

    private int prepareShader(Context context, @ShaderType int type, @RawRes int id) throws GLException {

        int vsHandle = GLES20.glCreateShader(type);

        if (vsHandle != 0) {
            Log.d(TAG, "Set Shader source");
            GLES20.glShaderSource(vsHandle, loadShader(context, id));
            checkGlError();

            Log.d(TAG, "Compile VS Shader");
            GLES20.glCompileShader(vsHandle);
            checkGlError();

            Log.d(TAG, "Get compilation status");
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(vsHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
            checkGlError();

            if (compileStatus[0] != GLES20.GL_TRUE) {
                Log.d(TAG, "Bad Status : " + Arrays.toString(compileStatus));
                GLES20.glDeleteShader(vsHandle);
                checkGlError();
                vsHandle = 0;
            }
        }
        return vsHandle;
    }

    private int prepareProgram(int vsHandle, int fsHandle) throws GLException {
        Log.d(TAG, "Create program");
        int programHandle = GLES20.glCreateProgram();

        if (programHandle != 0) {
            Log.d(TAG, "Attach VS");
            GLES20.glAttachShader(programHandle, vsHandle);
            checkGlError();

            Log.d(TAG, "Attach FS");
            GLES20.glAttachShader(programHandle, fsHandle);
            checkGlError();

            Log.d(TAG, "Bind attributes");
            GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
            checkGlError();

            Log.d(TAG, "Link program");
            GLES20.glLinkProgram(programHandle);
            checkGlError();

            Log.d(TAG, "Get Link status");
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);
            checkGlError();

            // If the link failed, delete the program.
            if (linkStatus[0] == 0) {
                Log.d(TAG, "Bad Status : " + Arrays.toString(linkStatus));
                GLES20.glDeleteProgram(programHandle);
                checkGlError();
                programHandle = 0;
            }
        }

        return programHandle;
    }

    private String loadShader(@NonNull Context context, @RawRes int id) {

        AtomicReference<StringBuilder> shaderSource = new AtomicReference<>(new StringBuilder());

        InputStream inputStream = null;
        try {
            inputStream = context.getResources().openRawResource(id);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.get().append(line).append('\n');
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Error finding resource", e);
        } catch (IOException e) {
            Log.e(TAG, "Error reading resource shader", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.w(TAG, "Error while closing input stream");
                }
            }
        }

        Log.d(TAG, shaderSource.toString());

        return shaderSource.get().toString();
    }
}
