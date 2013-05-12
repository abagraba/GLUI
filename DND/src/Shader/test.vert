varying vec2 tex;
 
void main()
{
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
 
 	tex = vec2(gl_MultiTexCoord0);
}