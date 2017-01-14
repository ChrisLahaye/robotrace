// simple vertex shader
varying vec3 posVec;

void main()
{
    posVec = vec3(gl_Vertex); // position in world/eye space
    gl_Position    = gl_ModelViewProjectionMatrix * gl_Vertex;      // model view transform
    gl_FrontColor = gl_Color;
    gl_TexCoord[0] = gl_MultiTexCoord0;
}