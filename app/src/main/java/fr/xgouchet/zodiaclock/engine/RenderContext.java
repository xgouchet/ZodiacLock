package fr.xgouchet.zodiaclock.engine;

/**
 * @author Xavier Gouchet
 */
public class RenderContext {

    public final float[] matrixV = new float[16];
    public final float[] matrixP = new float[16];
    public final float[] matrixMV = new float[16];
    public final float[] matrixMVP = new float[16];

    public final float[] vecLightPos = new float[3];

    public int attrVertexPosition = -1;
    public int attrVertexTexCoords = -1;

    public int uniformModelMatrix = -1;
    public int uniformMVPMatrix = -1;

    public int uniformLightPosition = -1;

    public int uniformDiffuseTexture = -1;
    public int uniformNormalTexture = -1;
}
