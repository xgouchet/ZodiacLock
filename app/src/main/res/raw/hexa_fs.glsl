//FRAGMENT SHADER \n" +
precision mediump float;

// Global
uniform sampler2D u_DiffuseTexture;
uniform sampler2D u_NormalTexture;
uniform vec3 u_LightPos;
uniform vec3 u_EyePos;

uniform vec4 u_DiffuseColor;
uniform vec4 u_SpecularColor;
uniform vec4 u_EmissiveColor;

// per fragment input
varying vec3 v_Position;
varying vec2 v_TexCoords;
varying vec3 v_Normal;
varying vec3 v_Tangent;
varying vec3 v_Binorm;

void main(){
    // Normal map deform
    vec3 normalDeform = normalize((texture2D(u_NormalTexture, v_TexCoords).rgb * 2.0) - 1.0);
    vec3 normalDir = vec3(
                            (v_Tangent.x * normalDeform.x) + (v_Binorm.x * normalDeform.y) + (v_Normal.x * normalDeform.z),
                            (v_Tangent.y * normalDeform.x) + (v_Binorm.y * normalDeform.y) + (v_Normal.y * normalDeform.z),
                            (v_Tangent.z * normalDeform.x) + (v_Binorm.z * normalDeform.y) + (v_Normal.z * normalDeform.z)
                            );

    // diffuse
    vec3 lightDir = normalize(u_LightPos - v_Position);
    float diffuse = max(dot(normalDir, lightDir), 0.5);

    // specular
    vec3 eyeDir = normalize(u_EyePos - v_Position);
    vec3 halfVector = normalize(eyeDir + lightDir);
    float spec = pow (max (dot (halfVector, normalDir), 0.0), 50.0)  ;

    vec4 texture = texture2D(u_DiffuseTexture, v_TexCoords);
    gl_FragColor = (((diffuse * u_DiffuseColor) + (u_EmissiveColor / 2.0)) * texture)
                    + (spec * u_SpecularColor);
//    gl_FragColor = vec4(normalDir, 1);
}