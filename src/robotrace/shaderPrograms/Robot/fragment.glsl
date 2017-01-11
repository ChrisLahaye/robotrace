uniform sampler2D headTexture;
varying vec3 posVec;

void main()
{
    vec4 color = texture2D(headTexture, gl_TexCoord[0].st);
    gl_FragColor = gl_Color;
}
