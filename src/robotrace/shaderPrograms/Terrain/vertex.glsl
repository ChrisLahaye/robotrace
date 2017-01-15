varying vec4 position;

float height(vec2 st) {
	return 0.6 * cos(0.3 * st.x + 0.2 * st.y) + 0.4 * cos(st.x - 0.5 * st.y);
}

void main()
{
	position = gl_Vertex;
	position.z = height(position.st);

	gl_Position = gl_ModelViewProjectionMatrix * position;
  gl_FrontColor = gl_Color;
  gl_TexCoord[0] = gl_MultiTexCoord0;
}
