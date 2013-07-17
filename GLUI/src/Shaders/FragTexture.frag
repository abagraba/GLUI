uniform sampler2D tex;

vec4 texture(){
    return texture2D(tex, gl_TexCoord[0].st);
}