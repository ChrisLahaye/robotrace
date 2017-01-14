uniform sampler2D trackTexture;
varying vec3 posVec;

void main()
{
    vec4 color = texture2D(trackTexture, gl_TexCoord[0].st);
    gl_FragColor = color;
}
