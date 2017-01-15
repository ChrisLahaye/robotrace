uniform sampler2D terrainTexture;
varying vec3 posVec;

void main()
{
    vec4 color = texture2D(terrainTexture, gl_TexCoord[0].st);
    gl_FragColor = color;
}
