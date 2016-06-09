package fr.xgouchet.zodiaclock.engine;

/**
 * @author Xavier Gouchet
 */
public class RenderContext {

    public final float[] matrixV = new float[16];
    public final float[] matrixP = new float[16];
    public final float[] matrixMV = new float[16];
    public final float[] matrixMVP = new float[16];

    public int attrVertexPosition = 0;
    public int attrVertexTexCoords = 0;

    public int uniformModelMatrix = 0;
    public int uniformMVPMatrix = 0;
    public int uniformDiffuseTexture = 0;
    public int uniformNormalTexture = 0;
}
