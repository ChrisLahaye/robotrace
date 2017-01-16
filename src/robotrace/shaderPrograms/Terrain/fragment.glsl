uniform sampler2D terrainTexture;
varying vec4 position;

void main()
{
  if (position.z < 0.0) {
    gl_FragColor = vec4(0, 0, 1, 1); // blue
  } else if(position.z >= 0.0 && position.z < 0.5) {
    gl_FragColor = vec4(1, 1, 0, 1); // yellow
  } else if(position.z >= 0.5) {
    gl_FragColor = vec4(0,1,0,1); // green
  }
}
