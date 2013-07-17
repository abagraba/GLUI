attribute vec3 position;
attribute vec4 rotation;
attribute vec3 scale;

vec4 transform(){
	
	vec4 square = rotation * rotation;
	float inorm = inversesqrt(square.x + square.y + square.z + square.w);

	float x = rotation.x * inorm;
	float y = rotation.y * inorm;
	float z = rotation.z * inorm;
	float w = rotation.w * inorm;

    float x2 = 2*x;
    float y2 = 2*y;
    float z2 = 2*z;
    float w2 = 2*w;
    
    float xx = x*x2;
    float xy = x*y2;
    float xz = x*z2;
    float xw = x*w2;
    float yy = y*y2;
    float yz = y*z2;
    float yw = y*w2;
    float zz = z*z2;
    float zw = z*w2;
    float ww = w*w2;
    
    mat4 transform = mat4(	1.0-yy-zz, xy+zw, xz-yw, 0.0,
    						xy-zw, 1.0-xx-zz, yz+xw, 0.0,
   							xz+yw, yz-xw, 1.0-xx-yy, 0.0,
    						position, 1.0);

	return transform * (vec4(scale, 1) * gl_Vertex);
}