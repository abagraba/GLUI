varying vec2 tex; 
uniform sampler2D texture;
uniform int id = 0;
uniform int rotation = 0;
uniform bool flip = false;
uniform ivec2 textureDimensions = ivec2(1, 1);
 
void main() {
	vec2 texx = tex;
	if (rotation == 1){
		texx.x = tex.y;
		texx.y = 1-tex.x;
	}
	if (rotation == 2){
		texx.x = 1-tex.x;
		texx.y = 1-tex.y;
	}

	if (rotation == 3){
		texx.x = 1-tex.y;
		texx.y = tex.x;
	}

	if (flip){
		texx.x = 1-texx.x;
	}

	vec2 off = vec2(id%textureDimensions.x, id/textureDimensions.x) + texx;
    gl_FragColor = texture2D(texture, off/textureDimensions);
    
}