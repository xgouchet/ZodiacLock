//FRAGMENT SHADER \n" +
precision mediump float;

// Global
uniform sampler2D u_DiffuseTexture;
uniform sampler2D u_NormalTexture;
uniform vec3 u_LightPos;

uniform vec4 u_DiffuseColor;
uniform vec4 u_SpecularColor;

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
                            (v_Tangent.x * normalDeform.x) + (v_Tangent.y * normalDeform.y) + (v_Tangent.z * normalDeform.z),
                            (v_Binorm.x * normalDeform.x) + (v_Binorm.y * normalDeform.y) + (v_Binorm.z * normalDeform.z),
                            (v_Normal.x * normalDeform.x) + (v_Normal.y * normalDeform.y) + (v_Normal.z * normalDeform.z)
                            );

//    vec3 normalDir = v_Normal ;

    // diffuse
    vec3 lightDir = normalize(u_LightPos - v_Position);
    float diffuse = max(dot(normalDir, lightDir), 0.15);

    // specular
    vec3 halfVector = normalize(v_Position + lightDir);
    float spec = pow (max (dot (halfVector, normalDir), 0.0), 10.0)  ;

    vec4 texture = texture2D(u_DiffuseTexture, v_TexCoords);
    gl_FragColor = (diffuse * texture * u_DiffuseColor)
                    + (spec * u_SpecularColor);
}