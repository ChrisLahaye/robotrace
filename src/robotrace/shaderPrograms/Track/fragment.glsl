uniform bool ambient, diffuse, specular;
uniform sampler2D trackTexture;
varying vec3 P, N, E, V, R;
vec3 L;

vec4 shading(vec3 P, vec3 N, gl_LightProducts light, gl_MaterialParameters mat) {
	vec4 result = vec4(0,0,0,1); // opaque black
	for(int i = 0; i < 5; i++) { // 5 light sources are used
	result += gl_LightSource[i].ambient; // compute ambient contribution
	L = normalize(gl_LightSource[i].position.xyz - P);
	// a . b = ||a|| ||b|| cos(q)
	// to calculate cos q, with q the angle between two vectors, we can use above formula
	// since our vectors are normalized, its dot product equals the cosine

	// for diffuse we use cos q, with q the angle between the light and normal vector
	result += gl_LightSource[i].diffuse * max(dot(L, N), 0.0) * mat.diffuse; // compute diffuse contribution

	// for specular we use cos q, with q the angle between the reflected light and viewer vector
	result += gl_LightSource[i].specular * pow(max(dot(R, V), 0.0), mat.shininess); // compute specular contribution
	}
	return result;
}

void main()
{
    gl_LightProducts light = gl_FrontLightProduct[0];
    gl_MaterialParameters mat = gl_FrontMaterial;
    gl_FragColor = texture2D(trackTexture, gl_TexCoord[0].st) + shading(P, N, light, mat);
}
