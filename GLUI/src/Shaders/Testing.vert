void vtexture();
vec4 transform();

void main(){
	gl_Position = gl_ModelViewProjectionMatrix * transform();
	vtexture();
	gl_FrontColor = gl_Color;
	gl_BackColor = vec4(vec3(1, 1, 1) - gl_Color.rgb, gl_Color.a);
}