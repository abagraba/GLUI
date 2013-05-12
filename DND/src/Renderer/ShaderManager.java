package Renderer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import static org.lwjgl.opengl.GL20.*;

public class ShaderManager {

	private static Hashtable<String, Integer> shaders = new Hashtable<String, Integer>();
	private static Hashtable<String, Integer> programs = new Hashtable<String, Integer>();
	private static String root = "src/Shader/";

	/**
	 * Uses the shader program specified. Throws {@link ProgramException} if the specified program does not exist. Instantiate programs with
	 * {@link #createProgram(String, String, String)}.
	 * @param program name of the program.
	 * @throws ProgramException if program has not been created.
	 * @see #createProgram(String, String, String)
	 */
	public static void useProgram(String program) {
		if (programs.containsKey(program))
			GL20.glUseProgram(programs.get(program));
		else
			throw new ProgramException("Binding");
	}

	/**
	 * Creates a shader program from specified shaders under the given name.
	 * @param name Name of the program. Used for future accesses to the program.
	 * @param vert filename of the vertex shader. The shader file must exist in the folder specified by root. The filename is specified without filetype. Ex:
	 *        "Diffuse" not "Diffuse.vert".
	 * @param frag filename of the fragment shader. The shader file must exist in in the folder specified by root. The filename is specified without filetype. Ex:
	 *        "Diffuse" not "Diffuse.frag".
	 * @return program ID of the program created.
	 */
	public static int createProgram(String name, String vert, String frag) {
		int v = getShader(root + vert + ".vert", GL_VERTEX_SHADER);
		int f = getShader(root + frag + ".frag", GL_FRAGMENT_SHADER);
		// Check if shaders are valid
		if (v < 0)
			throw new ShaderException(GL_VERTEX_SHADER);
		if (f < 0)
			throw new ShaderException(GL_FRAGMENT_SHADER);
		// Initialize program
		int program = glCreateProgram();
		if (program == 0)
			return -1;
		// Attach shaders
		glAttachShader(program, v);
		glAttachShader(program, f);
		// Link shaders
		glLinkProgram(program);
		if (glGetShader(program, GL_LINK_STATUS) == GL11.GL_FALSE)
			throw new ProgramException("Linking");
		glValidateProgram(program);
		if (glGetShader(program, GL_VALIDATE_STATUS) == GL11.GL_FALSE)
			throw new ProgramException("Validation");

		programs.put(name, program);
		return program;
	}

	/**
	 * Gets the shader ID of the specified shader. Initializes the shader if it does not exist yet.
	 * @param shader name of the shader file. The shader file must exist in the folder specified by root. The filename is specified without filetype. Ex:
	 *        "Diffuse" not "Diffuse.vert".
	 * @param type type of the shader. Valid values: GL_VERTEX, GL_FRAGMENT.
	 * @return shader ID.
	 */
	public static int getShader(String shader, int type) {
		if ( !shaders.containsKey(shader))
			initShader(shader, type);
		return shaders.get(shader);
	}

	/**
	 * Initializes the specified shader.
	 * @param shader name of the shader file. The shader file must exist in the folder specified by root. The filename is specified without filetype. Ex:
	 *        "Diffuse" not "Diffuse.vert".
	 * @param type type of the shader. Valid values: GL_VERTEX, GL_FRAGMENT.
	 */
	public static void initShader(String shader, int type) {
		int buffer = glCreateShader(type);
		if (buffer == 0)
			return;// TODO Free buffer?
		// Initialize Shader
		glShaderSource(buffer, getShaderData(shader));
		// Compile shader
		glCompileShader(buffer);
		if (glGetShader(buffer, GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println("compile error " + getType(type));
			return;
		}
		// Return shader
		shaders.put(shader, buffer);
	}

	private static CharSequence getShaderData(String filename) throws ShaderException {
		StringBuilder data = new StringBuilder();
		BufferedReader file = null;
		String line;
		try {
			file = new BufferedReader(new FileReader((filename)));
			while ((line = file.readLine()) != null) {
				data.append(line).append("\n");
			}
			file.close();
		} catch (FileNotFoundException e) {
			throw new ShaderException(filename);
		} catch (IOException e) {
			throw new ShaderException(filename);
		}
		return data;
	}
	
	public static void setRoot(String root){
		ShaderManager.root = root;
	}

	public static void cleanProgram(String program) {
		GL20.glDeleteProgram(programs.get(program));
	}
	public static void cleanPrograms(String[] programs) {
		for (int i = 0; i < programs.length; i++)
			cleanProgram(programs[i]);
	}
	
	public static void cleanShader(String shader) {
		GL20.glDeleteProgram(shaders.get(shader));
	}
	public static void cleanShaders(String[] shaders) {
		for (int i = 0; i < shaders.length; i++)
			cleanProgram(shaders[i]);
	}
	
	public static void cleanAll() {
		GL20.glUseProgram(0);
		for (int program : programs.values())
			GL20.glDeleteProgram(program);
		for (int shader: shaders.values())
			GL20.glDeleteShader(shader);
	}

	private static String getType(int type) {
		switch (type) {
			case GL_VERTEX_SHADER:
				return "Vertex";
			case GL_FRAGMENT_SHADER:
				return "Fragment";
			default:
				return "Invalid";
		}
	}

	@SuppressWarnings("serial")
	public static class ShaderException extends RuntimeException {
		public ShaderException(int type) {
			super(getType(type) + " shader failed to initialize.");
		}

		public ShaderException(String s) {
			super("Shader failed to initialize from " + s + ".");
		}
	}

	@SuppressWarnings("serial")
	public static class ProgramException extends RuntimeException {
		public ProgramException(String errorLocation) {
			super(errorLocation + " error.");
		}
	}
}
