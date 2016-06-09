//FRAGMENT SHADER \n" +
precision mediump float;

// Global
uniform sampler2D u_DiffuseTexture;
uniform sampler2D u_NormalTexture;
uniform vec3 u_LightPos;

// per fragment input
varying vec3 v_Position;
varying vec2 v_TexCoords;
varying vec3 v_Normal;
varying vec3 v_Tangent;
varying vec3 v_Binorm;

void main(){

    vec3 normalDeform = normalize((texture2D(u_NormalTexture, v_TexCoords).rgb * 2.0) - 1.0);
    vec3 normalDir = (v_Tangent * normalDeform.x) + (v_Binorm * normalDeform.y) + (v_Normal * normalDeform.z);
    // soften the normal map...
    normalDir = normalize(normalDir + v_Normal);

    vec4 texture = texture2D(u_DiffuseTexture, v_TexCoords);
    vec3 lightDir = normalize(u_LightPos - v_Position);
    float diffuse = max(dot(normalDir, lightDir), 0.25);
    gl_FragColor = diffuse * texture;
}