package Util;

// import static org.lwjgl.opengl.GL11.*; import static org.lwjgl.opengl.GL12.*; import static org.lwjgl.opengl.GL13.*;
// import static org.lwjgl.opengl.GL14.*; import static org.lwjgl.opengl.GL15.*; import static org.lwjgl.opengl.GL20.*;
// import static org.lwjgl.opengl.GL21.*; import static org.lwjgl.opengl.GL30.*; import static org.lwjgl.opengl.GL31.*;
// import static org.lwjgl.opengl.GL32.*; import static org.lwjgl.opengl.GL33.*; import static org.lwjgl.opengl.GL40.*;
// import static org.lwjgl.opengl.GL41.*; import static org.lwjgl.opengl.GL42.*;

import static org.lwjgl.opengl.EXTTextureCompressionS3TC.GL_COMPRESSED_RGBA_S3TC_DXT1_EXT;
import static org.lwjgl.opengl.EXTTextureCompressionS3TC.GL_COMPRESSED_RGBA_S3TC_DXT3_EXT;
import static org.lwjgl.opengl.EXTTextureCompressionS3TC.GL_COMPRESSED_RGBA_S3TC_DXT5_EXT;
import static org.lwjgl.opengl.EXTTextureCompressionS3TC.GL_COMPRESSED_RGB_S3TC_DXT1_EXT;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_BGR;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_3D;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_BYTE_2_3_3_REV;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_BYTE_3_3_2;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_INT_10_10_10_2;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_INT_2_10_10_10_REV;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_INT_8_8_8_8;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_INT_8_8_8_8_REV;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_SHORT_1_5_5_5_REV;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_SHORT_4_4_4_4;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_SHORT_4_4_4_4_REV;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_SHORT_5_5_5_1;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_SHORT_5_6_5;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_SHORT_5_6_5_REV;
import static org.lwjgl.opengl.GL13.GL_COMPRESSED_RGB;
import static org.lwjgl.opengl.GL13.GL_COMPRESSED_RGBA;
import static org.lwjgl.opengl.GL13.GL_PROXY_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT16;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT24;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL21.GL_COMPRESSED_SRGB;
import static org.lwjgl.opengl.GL21.GL_COMPRESSED_SRGB_ALPHA;
import static org.lwjgl.opengl.GL21.GL_SRGB;
import static org.lwjgl.opengl.GL21.GL_SRGB8;
import static org.lwjgl.opengl.GL21.GL_SRGB8_ALPHA8;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.GL_PROXY_TEXTURE_RECTANGLE;
import static org.lwjgl.opengl.GL31.GL_R16_SNORM;
import static org.lwjgl.opengl.GL31.GL_R8_SNORM;
import static org.lwjgl.opengl.GL31.GL_RG16_SNORM;
import static org.lwjgl.opengl.GL31.GL_RG8_SNORM;
import static org.lwjgl.opengl.GL31.GL_RGB16_SNORM;
import static org.lwjgl.opengl.GL31.GL_RGB8_SNORM;
import static org.lwjgl.opengl.GL31.GL_RGBA16_SNORM;
import static org.lwjgl.opengl.GL31.GL_RGBA8_SNORM;
import static org.lwjgl.opengl.GL31.GL_TEXTURE_RECTANGLE;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_2D_MULTISAMPLE;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_2D_MULTISAMPLE_ARRAY;
import static org.lwjgl.opengl.GL33.GL_RGB10_A2UI;
import static org.lwjgl.opengl.GL40.GL_PROXY_TEXTURE_CUBE_MAP_ARRAY;
import static org.lwjgl.opengl.GL40.GL_TESS_CONTROL_SHADER;
import static org.lwjgl.opengl.GL40.GL_TESS_EVALUATION_SHADER;
import static org.lwjgl.opengl.GL42.GL_COMPRESSED_RGBA_BPTC_UNORM;
import static org.lwjgl.opengl.GL42.GL_COMPRESSED_RGB_BPTC_SIGNED_FLOAT;
import static org.lwjgl.opengl.GL42.GL_COMPRESSED_RGB_BPTC_UNSIGNED_FLOAT;
import static org.lwjgl.opengl.GL42.GL_COMPRESSED_SRGB_ALPHA_BPTC_UNORM;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public final class GLCONST {

	public static final int VBO_STATIC = GL_STATIC_DRAW;
	public static final int VBO_DYNAMIC = GL_DYNAMIC_DRAW;
	public static final int VBO_ARRAY_BUFFER = GL_ARRAY_BUFFER;
	public static final int VBO_ELEMENT_ARRAY_BUFFER = GL_ELEMENT_ARRAY_BUFFER;

	public static final int TEXTURE_RECT = GL_TEXTURE_RECTANGLE;
	public static final int TEXTURE_1D = GL_TEXTURE_1D;
	public static final int TEXTURE_1D_ARRAY = GL_TEXTURE_1D_ARRAY;
	public static final int TEXTURE_2D = GL_TEXTURE_2D;
	public static final int TEXTURE_2D_ARRAY = GL_TEXTURE_2D_ARRAY;
	public static final int TEXTURE_3D = GL_TEXTURE_3D;
	public static final int TEXTURE_CUBEMAP = GL_TEXTURE_CUBE_MAP;
	public static final int TEXTURE_2D_MULTISAMPLE = GL_TEXTURE_2D_MULTISAMPLE;
	public static final int TEXTURE_2D_MULTISAMPLE_ARRAY = GL_TEXTURE_2D_MULTISAMPLE_ARRAY;

	public static final int TEXTURE_RECT_PROXY = GL_PROXY_TEXTURE_RECTANGLE;
	public static final int TEXTURE_1D_PROXY = GL_PROXY_TEXTURE_1D;
	public static final int TEXTURE_1D_PROXY_ARRAY = GL_PROXY_TEXTURE_1D_ARRAY;
	public static final int TEXTURE_2D_PROXY = GL_PROXY_TEXTURE_2D;
	public static final int TEXTURE_CUBEMAP_PROXY = GL_PROXY_TEXTURE_CUBE_MAP;
	public static final int TEXTURE_CUBEMAP_PROXY_ARRAY = GL_PROXY_TEXTURE_CUBE_MAP_ARRAY;

	// Pixel format specifiers.
	public static final int FORMAT_R = GL11.GL_RED;
	public static final int FORMAT_R_I = GL_RED_INTEGER;
	public static final int FORMAT_RG = GL_RG;
	public static final int FORMAT_RG_I = GL_RG_INTEGER;
	public static final int FORMAT_RGB = GL_RGB;
	public static final int FORMAT_RGB_I = GL_RGB_INTEGER;
	public static final int FORMAT_RGBA = GL_RGBA;
	public static final int FORMAT_RGBA_I = GL_RGBA_INTEGER;
	public static final int FORMAT_BGR = GL_BGR;
	public static final int FORMAT_BGRA = GL12.GL_BGRA;
	public static final int FORMAT_A = GL_ALPHA;
	public static final int FORMAT_L = GL_LUMINANCE;
	public static final int FORMAT_LA = GL_LUMINANCE_ALPHA;
	public static final int FORMAT_D = GL_DEPTH_COMPONENT;
	public static final int FORMAT_DS = GL_DEPTH_STENCIL;
	// Specific format specifiers.
	public static final int FORMAT_R8 = GL_R8;
	public static final int FORMAT_R8S = GL_R8_SNORM;
	public static final int FORMAT_R8I = GL_R8I;
	public static final int FORMAT_R8UI = GL_R8UI;
	public static final int FORMAT_R16 = GL_R16;
	public static final int FORMAT_R16S = GL_R16_SNORM;
	public static final int FORMAT_R16I = GL_R16I;
	public static final int FORMAT_R16UI = GL_R16UI;
	public static final int FORMAT_R16F = GL_R16F;
	public static final int FORMAT_R32I = GL_R32I;
	public static final int FORMAT_R32UI = GL_R32UI;
	public static final int FORMAT_R32F = GL_R32F;
	public static final int FORMAT_RG8 = GL_RG8;
	public static final int FORMAT_RG8S = GL_RG8_SNORM;
	public static final int FORMAT_RG8I = GL_RG8I;
	public static final int FORMAT_RG8UI = GL_RG8UI;
	public static final int FORMAT_RG16 = GL_RG16;
	public static final int FORMAT_RG16S = GL_RG16_SNORM;
	public static final int FORMAT_RG16I = GL_RG16I;
	public static final int FORMAT_RG16UI = GL_RG16UI;
	public static final int FORMAT_RG16F = GL_RG16F;
	public static final int FORMAT_RG32I = GL_RG32I;
	public static final int FORMAT_RG32UI = GL_RG32UI;
	public static final int FORMAT_RG32F = GL_RG32F;
	public static final int FORMAT_RGB8 = GL_RGB8;
	public static final int FORMAT_RGB8S = GL_RGB8_SNORM;
	public static final int FORMAT_RGB8I = GL_RGB8I;
	public static final int FORMAT_RGB8UI = GL_RGB8UI;
	public static final int FORMAT_RGB16 = GL_RGB16;
	public static final int FORMAT_RGB16S = GL_RGB16_SNORM;
	public static final int FORMAT_RGB16I = GL_RGB16I;
	public static final int FORMAT_RGB16UI = GL_RGB16UI;
	public static final int FORMAT_RGB16F = GL_RGB16F;
	public static final int FORMAT_RGB32I = GL_RGB32I;
	public static final int FORMAT_RGB32UI = GL_RGB32UI;
	public static final int FORMAT_RGB32F = GL_RGB32F;
	public static final int FORMAT_RGBA8 = GL_RGBA8;
	public static final int FORMAT_RGBA8S = GL_RGBA8_SNORM;
	public static final int FORMAT_RGBA8I = GL_RGBA8I;
	public static final int FORMAT_RGBA8UI = GL_RGBA8UI;
	public static final int FORMAT_RGBA16 = GL_RGBA16;
	public static final int FORMAT_RGBA16S = GL_RGBA16_SNORM;
	public static final int FORMAT_RGBA16I = GL_RGBA16I;
	public static final int FORMAT_RGBA16UI = GL_RGBA16UI;
	public static final int FORMAT_RGBA16F = GL_RGBA16F;
	public static final int FORMAT_RGBA32I = GL_RGBA32I;
	public static final int FORMAT_RGBA32UI = GL_RGBA32UI;
	public static final int FORMAT_RGBA32F = GL_RGBA32F;
	// Compressed RGB formats with reduced or no A.
	public static final int FORMAT_RG3B2 = GL_R3_G3_B2;
	public static final int FORMAT_RGB4 = GL_RGB4;
	public static final int FORMAT_RGB5 = GL_RGB5;
	public static final int FORMAT_RGB5A1 = GL_RGB5_A1;
	public static final int FORMAT_RGB10 = GL_RGB10;
	public static final int FORMAT_RGB10A2 = GL_RGB10_A2;
	public static final int FORMAT_RGB10A2UI = GL_RGB10_A2UI;
	public static final int FORMAT_RGB12 = GL_RGB12;
	public static final int FORMAT_RG11B10F = GL_R11F_G11F_B10F;
	public static final int FORMAT_RGB9E5 = GL_RGB9_E5;
	// Compressed RGBA formats.
	public static final int FORMAT_RGBA2 = GL_RGBA2;
	public static final int FORMAT_RGBA4 = GL_RGBA4;
	public static final int FORMAT_RGBA12 = GL_RGBA12;
	// SRGB formats.
	public static final int FORMAT_SRGB = GL_SRGB;
	public static final int FORMAT_SRGB8 = GL_SRGB8;
	public static final int FORMAT_SRGBA = GL_SRGB_ALPHA;
	public static final int FORMAT_SRGBA8 = GL_SRGB8_ALPHA8;
	// Compressed formats.
	public static final int FORMAT_COMP_R = GL_COMPRESSED_RED;
	public static final int FORMAT_COMP_RG = GL_COMPRESSED_RG;
	public static final int FORMAT_COMP_RGB = GL_COMPRESSED_RGB;
	public static final int FORMAT_COMP_RGBA = GL_COMPRESSED_RGBA;
	public static final int FORMAT_COMP_SRGB = GL_COMPRESSED_SRGB;
	public static final int FORMAT_COMP_SRGBA = GL_COMPRESSED_SRGB_ALPHA;
	public static final int FORMAT_COMP_RGTC_R = GL_COMPRESSED_RED_RGTC1;
	public static final int FORMAT_COMP_RGTC_R_S = GL_COMPRESSED_SIGNED_RED_RGTC1;
	public static final int FORMAT_COMP_RGTC_RG = GL_COMPRESSED_RED_GREEN_RGTC2;
	public static final int FORMAT_COMP_RGTC_RG_S = GL_COMPRESSED_SIGNED_RED_GREEN_RGTC2;
	public static final int FORMAT_COMP_BPTC_RGB = GL_COMPRESSED_RGB_BPTC_UNSIGNED_FLOAT;
	public static final int FORMAT_COMP_BPTC_RGB_S = GL_COMPRESSED_RGB_BPTC_SIGNED_FLOAT;
	public static final int FORMAT_COMP_BPTC_RGBA_UNORM = GL_COMPRESSED_RGBA_BPTC_UNORM;
	public static final int FORMAT_COMP_BPTC_SRGBA_UNORM = GL_COMPRESSED_SRGB_ALPHA_BPTC_UNORM;
	// DX compatability formats.
	public static final int FORMAT_DX_BC1 = GL_COMPRESSED_RGB_S3TC_DXT1_EXT;
	public static final int FORMAT_DX_BC1_A = GL_COMPRESSED_RGBA_S3TC_DXT1_EXT;
	public static final int FORMAT_DX_BC2 = GL_COMPRESSED_RGBA_S3TC_DXT3_EXT;
	public static final int FORMAT_DX_BC3 = GL_COMPRESSED_RGBA_S3TC_DXT5_EXT;
	// Depth formats
	public static final int FORMAT_D16 = GL_DEPTH_COMPONENT16;
	public static final int FORMAT_D24 = GL_DEPTH_COMPONENT24;
	public static final int FORMAT_D32 = GL_DEPTH_COMPONENT32F;
	public static final int FORMAT_D24S8 = GL_DEPTH24_STENCIL8;
	public static final int FORMAT_D32S8 = GL_DEPTH32F_STENCIL8;

	// Value type specifiers.
	public static final int TYPE_BYTE = GL_BYTE;
	public static final int TYPE_UBYTE = GL_UNSIGNED_BYTE;
	public static final int TYPE_SHORT = GL_SHORT;
	public static final int TYPE_USHORT = GL_UNSIGNED_SHORT;
	public static final int TYPE_INT = GL_INT;
	public static final int TYPE_UINT = GL_UNSIGNED_INT;
	public static final int TYPE_FLOAT = GL_FLOAT;

	// Special compressed value type specifiers.
	public static final int TYPE_UBYTE_332 = GL_UNSIGNED_BYTE_3_3_2;
	public static final int TYPE_UBYTE_233R = GL_UNSIGNED_BYTE_2_3_3_REV;
	public static final int TYPE_USHORT_565 = GL_UNSIGNED_SHORT_5_6_5;
	public static final int TYPE_USHORT_565R = GL_UNSIGNED_SHORT_5_6_5_REV;
	public static final int TYPE_USHORT_4444 = GL_UNSIGNED_SHORT_4_4_4_4;
	public static final int TYPE_USHORT_4444R = GL_UNSIGNED_SHORT_4_4_4_4_REV;
	public static final int TYPE_USHORT_5551 = GL_UNSIGNED_SHORT_5_5_5_1;
	public static final int TYPE_USHORT_1555R = GL_UNSIGNED_SHORT_1_5_5_5_REV;
	public static final int TYPE_UINT_5999R = GL_UNSIGNED_INT_5_9_9_9_REV;
	public static final int TYPE_UINT_8888 = GL_UNSIGNED_INT_8_8_8_8;
	public static final int TYPE_UINT_8888R = GL_UNSIGNED_INT_8_8_8_8_REV;
	public static final int TYPE_UINT_1010102 = GL_UNSIGNED_INT_10_10_10_2;
	public static final int TYPE_UINT_2101010R = GL_UNSIGNED_INT_2_10_10_10_REV;
	public static final int TYPE_UINT_248 = GL_UNSIGNED_INT_24_8;

	// Shader type constants.
	public static final int SHADER_VERT = GL_VERTEX_SHADER;
	public static final int SHADER_FRAG = GL_FRAGMENT_SHADER;
	public static final int SHADER_GEO = GL_GEOMETRY_SHADER;
	public static final int SHADER_TESC = GL_TESS_CONTROL_SHADER;
	public static final int SHADER_TESE = GL_TESS_EVALUATION_SHADER;

	// FBO target constants.
	public static final int FBO_DRAW = GL_DRAW_FRAMEBUFFER;
	public static final int FBO_READ = GL_READ_FRAMEBUFFER;
	public static final int FBO_FRAMEBUFFER = GL_FRAMEBUFFER;
	// FBO attachment constants.
	public static final int FBO_COLOR0 = GL_COLOR_ATTACHMENT0;
	public static final int FBO_COLOR1 = GL_COLOR_ATTACHMENT1;
	public static final int FBO_COLOR2 = GL_COLOR_ATTACHMENT2;
	public static final int FBO_COLOR3 = GL_COLOR_ATTACHMENT3;
	public static final int FBO_COLOR4 = GL_COLOR_ATTACHMENT4;
	public static final int FBO_COLOR5 = GL_COLOR_ATTACHMENT5;
	public static final int FBO_COLOR6 = GL_COLOR_ATTACHMENT6;
	public static final int FBO_COLOR7 = GL_COLOR_ATTACHMENT7;
	public static final int FBO_COLOR8 = GL_COLOR_ATTACHMENT8;
	public static final int FBO_COLOR9 = GL_COLOR_ATTACHMENT9;
	public static final int FBO_COLOR10 = GL_COLOR_ATTACHMENT10;
	public static final int FBO_COLOR11 = GL_COLOR_ATTACHMENT11;
	public static final int FBO_COLOR12 = GL_COLOR_ATTACHMENT12;
	public static final int FBO_COLOR13 = GL_COLOR_ATTACHMENT13;
	public static final int FBO_COLOR14 = GL_COLOR_ATTACHMENT14;
	public static final int FBO_COLOR15 = GL_COLOR_ATTACHMENT15;
	public static final int FBO_DEPTH = GL_DEPTH_ATTACHMENT;
	public static final int FBO_STENCIL = GL_STENCIL_ATTACHMENT;
	public static final int FBO_DEPTH_STENCIL = GL_DEPTH_STENCIL_ATTACHMENT;

	// Validation constants
	public static final int FBO_COMPLETE = GL_FRAMEBUFFER_COMPLETE;

}
