uniform bool ambient, diffuse, specular;
varying vec3 P, N, E, V, R;
vec3 L;

void main() {
	// pick up light LIGHT0 and material properties from GL
	gl_LightSourceParameters light = gl_LightSource[0];
	gl_MaterialParameters mat = gl_FrontMaterial;

	// there are 4 spaces object, world, view, clip

	// gl_ModelViewMatrix is pre-defined uniform variable set from GL_MODELVIEW matrix
	// gl_NormalMatrix is transpose(inverse(gl_ModelViewMatrix)) , which is the same matrix but with inverted scale factors.

	// transform object to view space: gl_ModelViewMatrix
	// but since we are transforming a normal, we need to use the transpose of the inverse gl_ModelViewMatrix, which is gl_NormalMatrix

	// normalize returns a vector with the same direction as its parameter, v , but with length 1.
	// this needs to be the case if we want to use it as direction vector, or else the length changes
	N = normalize(gl_NormalMatrix * gl_Normal); // transform normal vector to view space
	P = vec3(gl_ModelViewMatrix * gl_Vertex); // compute vertex position in 3-D view coordinates

	L = normalize(light.position.xyz - P); // vector towards light source

	E = vec3(0); // position of camera in View space
	V = normalize(E - P); // direction towards viewer

	// R + L = 2 * (N.L)N
	// R = 2 * (N*L)N - L
	R = normalize(2.0 * dot(N, L)*N - L);  // reflected light vector

	// output of vertex shader
	gl_TexCoord[0] = gl_MultiTexCoord0;
	// gl_FrontColor = shading(P, N, light, mat);
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}