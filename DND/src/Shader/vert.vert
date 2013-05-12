#extension GL_EXT_gpu_shader4 : require
#extension GL_ARB_draw_instanced : require
 
uniform samplerBuffer tboTransform;
 
varying vec3 normal;
varying vec3 position;
 
void main()
{
	vec4 transform = texelFetchBuffer(tboTransform, gl_InstanceID);
	vec4 vertex = vec4((gl_Vertex.xyz * transform.w + transform.xyz),gl_Vertex.w);
 
	normal = normalize(gl_NormalMatrix * gl_Normal);
	position = vec3(gl_ModelViewMatrix * vertex);
	gl_Position = gl_ModelViewProjectionMatrix * vertex;
 
	gl_TexCoord[0] = gl_MultiTexCoord0;
}