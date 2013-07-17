
vec4 texture();

void main(){
	gl_FragColor = gl_Color * texture();
}