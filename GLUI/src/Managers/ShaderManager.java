package Managers;

import java.io.File;
import java.util.Hashtable;

import Util.Debug;
import Util.GLCONST;

public class ShaderManager {

	private static Hashtable<String, Shader> shaders = new Hashtable<String, Shader>();
	private static Hashtable<String, Program> programs = new Hashtable<String, Program>();

	public static Program currentProgram = null;
	public static Program instanceProgram = null;

	/**
	 * Sets the Instancing program if it exists and is valid.
	 * @param program instancing program. Attributes: position, rotation.
	 */
	public static void setInstancingProgram(Program program) {
		if (program == null)
			return;
		int pos = program.getAttribute("position");
		int rot = program.getAttribute("rotation");
		int sca = program.getAttribute("scale");
		if (pos == -1 || rot == -1 || sca == -1) {
			Debug.log(Debug.INSTANCE_MANAGEMENT, "Failure to get shader attribute locations for [", program.name, "]");
			return;
		}
		instanceProgram = program;
	}

	/**
	 * Gets the Program associated with the given name.
	 * @param name name associated with the Program.
	 * @return Program object associated with given name. null if the Program does not exist.
	 */
	public static Program getProgram(String name) {
		return programs.get(name);
	}

	/**
	 * Creates a shader program from specified shader and associates it with the given name.
	 * @param name Name of the program. Used for future accesses to the program.
	 * @param shaders Shaders to be used in this program.
	 */
	public static Program createProgram(String name, Shader... shaders) {
		Program p = Program.createProgram(name, shaders);
		if (p != null)
			programs.put(name, p);
		return p;
	}

	public static Program createProgram(String name, String... shaders) {
		Shader[] shades = new Shader[shaders.length];
		for (int i = 0; i < shaders.length; i++) {
			shades[i] = getShader(shaders[i]);
			if (shades[i] == null) {
				Debug.log(Debug.SHADER_MANAGER, "Cannot create program [", name, "]. Shader [", shaders[i],
						"] does not exist.");
				return null;
			}
		}
		return createProgram(name, shades);
	}

	/**
	 * Gets the Shader associated with the given name.
	 * @param name name associated with the Shader.
	 * @return Shader object associated with given name. null if the Shader does not exist.
	 */
	public static Shader getShader(String name) {
		return shaders.get(name);
	}

	/**
	 * Creates and initializes the specified shader and associates it with the given name.
	 * @param name name of the shader. Used to get and bind the shader later on.
	 * @param shader File containing the shader data.
	 * @param type type of the shader. Valid Values: {@link GLCONST#SHADER_VERT}, {@link GLCONST#SHADER_FRAG},
	 *        {@link GLCONST#SHADER_GEO}, {@link GLCONST#SHADER_TESC}, {@link GLCONST#SHADER_TESE}.
	 */
	public static void createShader(String name, File shader, int type) {
		Shader s = Shader.createShader(name, shader, type);
		if (s != null)
			shaders.put(name, s);
	}

}
