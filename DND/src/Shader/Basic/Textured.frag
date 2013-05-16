varying vec2 tex; 
uniform sampler2D texture;
 
void main()
{
 
    gl_FragColor = texture2D(texture, tex);
}