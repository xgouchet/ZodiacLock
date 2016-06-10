//VERTEX SHADER

// global
uniform mat4 u_MVPMatrix;
uniform mat4 u_ModelMatrix;

// Per vertex attribute
attribute vec4 a_Position;
attribute vec2 a_TexCoords;

// per vertex output
varying vec3 v_Position;
varying vec2 v_TexCoords;
varying vec3 v_Normal;
varying vec3 v_Tangent;
varying vec3 v_Binorm;

void main() {

    v_TexCoords = a_TexCoords;

    v_Position = (u_ModelMatrix * a_Position).xyz;
    v_Normal = (u_ModelMatrix * vec4(0.0, 0.0, 1.0, 0.0)).xyz;
    v_Tangent = (u_ModelMatrix * vec4(-1.0, 0.0, 0.0, 0.0)).xyz;
    v_Binorm = (u_ModelMatrix * vec4(0.0, 1.0, 0.0, 0.0)).xyz;


    gl_Position = u_MVPMatrix * a_Position;
}