uniform sampler2D robotTexture;
varying vec3 posVec;

void main()
{
    vec4 color = texture2D(robotTexture, gl_TexCoord[0].st);
    gl_FragColor = color;
}
