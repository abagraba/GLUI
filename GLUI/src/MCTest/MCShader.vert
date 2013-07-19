attribute vec3 position;
attribute vec4 tint;
attribute float misc;

void main(){

	gl_Position = gl_ModelViewProjectionMatrix * (gl_Vertex + vec4(position, 0));
	gl_FrontColor = tint;
	
	float texX = floor(mod(misc.x, 16));
	float texY = floor(misc.x/16);
	
	vec2 tex = (vec2(texX, texY) + gl_MultiTexCoord0.st)*vec2(0.0625, 0.0625);
	gl_TexCoord[0] = vec4(tex, 0, 1);

}