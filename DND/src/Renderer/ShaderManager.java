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
	private static Hashtable<String, Integer> uniforms = new Hashtable<String, Integer>();
	private static Hashtable<String, Integer> programs = new Hashtable<String, Integer>();
	private static String root = "src/Shader/";
	private static int currentProgram = 0;
	
	public static final int TEXTURE = 0;
	public static final int ATLAS = 1;
	
	public static void useDefault(int program){
		String name = null;
		String vert = null;
		String frag = null;
		switch (program) {
			case TEXTURE:
				name = "TEXTURE";
				vert = frag = "Basic/Textured";
				break;
			case ATLAS:
				name = "ATLAS";
				vert = "Basic/Textured";
				frag = "Basic/Atlas";
				break;
			default:
				System.out.println("Invalid default Shader " + program);
				break;
		}
		if (name != null && !programs.containsKey(name))
			createProgram(name, vert, frag);
		useProgram(name);
	}
	
	/**
	 * Uses the shader program specified. Uses default if program is null. Throws {@link ProgramException} if the specified program does not exist. Instantiate programs with
	 * {@link #createProgram(String, String, String)}.
	 * @param program name of the program.
	 * @throws ProgramException if program has not been created.
	 * @see #createProgram(String, String, String)
	 */
	public static void useProgram(String program) {
		if (program == null){
			currentProgram = 0;
			GL20.glUseProgram(currentProgram);
		}
		if (programs.containsKey(program)){
			currentProgram = programs.get(program);
			GL20.glUseProgram(currentProgram);
		}
		else
			throw new ProgramException("Binding");
	}

	/**
	 * Initializes a shader program from specified shaders under the given name.
	 * @param name Name of the program. Used for future accesses to the program.
	 * @param vert filename of the vertex shader. The shader file must exist in the folder specified by root. The filename is specified without
	 *        filetype. Ex: "Diffuse" not "Diffuse.vert".
	 * @param frag filename of the fragment shader. The shader file must exist in in the folder specified by root. The filename is specified without
	 *        filetype. Ex: "Diffuse" not "Diffuse.frag".
	 */
	public static void createProgram(String name, String vert, String frag) {
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
			return; // TODO throw exception
		// Attach shaders
		glAttachShader(program, v);
		glAttachShader(program, f);
		// Link shaders
		glLinkProgram(program);
		glGetShader(program, GL_LINK_STATUS);
		System.out.println(GL11.glGetError());
//		TODO fix invalid enum.
//		if (glGetShader(program, GL_LINK_STATUS) == GL11.GL_FALSE)
//			throw new ProgramException("Linking");
		glValidateProgram(program);
		if (glGetShader(program, GL_VALIDATE_STATUS) == GL11.GL_FALSE)
			throw new ProgramException("Validation");
		programs.put(name, program);
	}

	/**
	 * Gets the shader ID of the specified shader. Initializes the shader if it does not exist yet.
	 * @param shader name of the shader file. The shader file must exist in the folder specified by root. The filename is specified without filetype.
	 *        Ex: "Diffuse" not "Diffuse.vert".
	 * @param type type of the shader. Valid values: GL_VERTEX, GL_FRAGMENT.
	 * @return shader ID.
	 */
	public static int getShader(String shader, int type) {
		if (!shaders.containsKey(shader))
			initShader(shader, type);
		return shaders.get(shader);
	}

	/**
	 * Initializes the specified shader.
	 * @param shader name of the shader file. The shader file must exist in the folder specified by root. The filename is specified without filetype.
	 *        Ex: "Diffuse" not "Diffuse.vert".
	 * @param type type of the shader. Valid values: GL_VERTEX, GL_FRAGMENT.
	 */
	public static void initShader(String shader, int type) {
		int buffer = glCreateShader(type);
		if (buffer == 0)
			return;// TODO Free buffer? Clean up failed initialization.
		// Initialize Shader
		glShaderSource(buffer, getShaderData(shader));
		// Compile shader
		glCompileShader(buffer);
		if (glGetShader(buffer, GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println("compile error " + getType(type));
			System.out.println(glGetShaderInfoLog(buffer, 1000));
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

	// TODO Clean uniforms
	/**
	 * Sets the root directory for the ShaderManager. All shaders will be searched for in the root directory.
	 * @param root new root directory.
	 */
	public static void setRoot(String root) {
		ShaderManager.root = root;
	}

	/**
	 * Cleans up the specified shader program.
	 * @param program the specified shader program.
	 */
	public static void cleanProgram(String program) {
		GL20.glDeleteProgram(programs.remove(program));
	}

	/**
	 * Cleans up the specified shader programs.
	 * @param programs the specified shader programs.
	 */
	public static void cleanPrograms(String[] programs) {
		for (int i = 0; i < programs.length; i++)
			cleanProgram(programs[i]);
	}

	/**
	 * Cleans up the specified shader.
	 * @param shader the specified shader.
	 */
	public static void cleanShader(String shader) {
		GL20.glDeleteProgram(shaders.remove(shader));
	}

	/**
	 * Cleans up the specified shaders.
	 * @param shaders the specified shaders.
	 */
	public static void cleanShaders(String[] shaders) {
		for (int i = 0; i < shaders.length; i++)
			cleanProgram(shaders[i]);
	}

	/**
	 * Cleans up all the shaders and shader programs.
	 */
	public static void cleanAll() {
		GL20.glUseProgram(0);
		for (int program : programs.values())
			GL20.glDeleteProgram(program);
		for (int shader : shaders.values())
			GL20.glDeleteShader(shader);
		programs.clear();
		shaders.clear();
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
	
	public static void setUniform1f(String uniform, float value) {
		String uid = currentProgram + "~" + uniform;
		if (!uniforms.containsKey(uid))
			uniforms.put(uid, glGetUniformLocation(currentProgram, uniform));
		glUniform1f(uniforms.get(uid), value);
	}

	public static void setUniform2f(String uniform, float value1, float value2) {
		String uid = currentProgram + "~" + uniform;
		if (!uniforms.containsKey(uid))
			uniforms.put(uid, glGetUniformLocation(currentProgram, uniform));
		glUniform2f(uniforms.get(uid), value1, value2);
	}

	public static void setUniform3f(String uniform, float value1, float value2, float value3) {
		String uid = currentProgram + "~" + uniform;
		if (!uniforms.containsKey(uid))
			uniforms.put(uid, glGetUniformLocation(currentProgram, uniform));
		glUniform3f(uniforms.get(uid), value1, value2, value3);
	}

	public static void setUniform4f(String uniform, float value1, float value2, float value3, float value4) {
		String uid = currentProgram + "~" + uniform;
		if (!uniforms.containsKey(uid))
			uniforms.put(uid, glGetUniformLocation(currentProgram, uniform));
		glUniform4f(uniforms.get(uid), value1, value2, value3, value4);
	}

	public static void setUniform1i(String uniform, int value) {
		String uid = currentProgram + "~" + uniform;
		if (!uniforms.containsKey(uid))
			uniforms.put(uid, glGetUniformLocation(currentProgram, uniform));
		glUniform1i(uniforms.get(uid), value);
	}

	public static void setUniform2i(String uniform, int value1, int value2) {
		String uid = currentProgram + "~" + uniform;
		if (!uniforms.containsKey(uid))
			uniforms.put(uid, glGetUniformLocation(currentProgram, uniform));
		glUniform2i(uniforms.get(uid), value1, value2);
	}

	public static void setUniform3i(String uniform, int value1, int value2, int value3) {
		String uid = currentProgram + "~" + uniform;
		if (!uniforms.containsKey(uid))
			uniforms.put(uid, glGetUniformLocation(currentProgram, uniform));
		glUniform3i(uniforms.get(uid), value1, value2, value3);
	}

	public static void setUniform4i(String uniform, int value1, int value2, int value3, int value4) {
		String uid = currentProgram + "~" + uniform;
		if (!uniforms.containsKey(uid))
			uniforms.put(uid, glGetUniformLocation(currentProgram, uniform));
		glUniform4i(uniforms.get(uid), value1, value2, value3, value4);
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
