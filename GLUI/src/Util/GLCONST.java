package Util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.GL40.*;

public final class GLCONST {

	/**
	 * Buffering hint. Used for buffers that will not change often. {@value #STATIC}
	 */
	public static final int STATIC = GL_STATIC_DRAW;
	/**
	 * Buffering hint. Used for buffers that will change often. {@value #DYNAMIC}
	 */
	public static final int DYNAMIC = GL_DYNAMIC_DRAW;

	/**
	 * Target. {@value #ARRAY_BUFFER}
	 */
	public static final int ARRAY_BUFFER = GL_ARRAY_BUFFER;
	/**
	 * Target. {@value #ELEMENT_ARRAY_BUFFER}
	 */
	public static final int ELEMENT_ARRAY_BUFFER = GL_ELEMENT_ARRAY_BUFFER;

	public static final int BYTE = GL_UNSIGNED_BYTE;
	public static final int INT = GL_UNSIGNED_INT;
	public static final int FLOAT = GL_FLOAT;

	public static final int SHADER_VERT = GL_VERTEX_SHADER;
	public static final int SHADER_FRAG = GL_FRAGMENT_SHADER;
	public static final int SHADER_GEO = GL_GEOMETRY_SHADER;
	public static final int SHADER_TESC = GL_TESS_CONTROL_SHADER;
	public static final int SHADER_TESE = GL_TESS_EVALUATION_SHADER;

}
