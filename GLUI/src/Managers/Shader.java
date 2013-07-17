package Managers;

import static Util.GLCONST.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.GL11;

import Util.Debug;

public class Shader {

	public final String name;
	protected final int buffer;
	public final int type;

	protected static Shader createShader(String name, File shader, int type) {
		Shader s = new Shader(name, shader, type);
		if (s.buffer == 0)
			return null;
		return s;
	}

	private Shader(String name, File shader, int type) {
		this.name = name;
		this.type = type;
		int id = glCreateShader(type);
		if (id == 0) {
			Debug.log(Debug.SHADER_MANAGER, "Failure to allocate shader");
			buffer = 0;
			return;
		}
		// Initialize Shader
		glShaderSource(id, getShaderData(shader));
		// Compile shader
		glCompileShader(id);
		if (glGetShader(id, GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			Debug.log(Debug.SHADER_MANAGER, "Shader compile error: ", getTypeName(), " Shader: [", shader.getName(), "]");
			Debug.log(Debug.SHADER_MANAGER, glGetShaderInfoLog(id, 1000));
			buffer = 0;
			return;
		}
		buffer = id;
	}

	public String getTypeName() {
		switch (type) {
			case SHADER_VERT:
				return "Vertex";
			case SHADER_FRAG:
				return "Fragment";
			case SHADER_GEO:
				return "Geometry";
			case SHADER_TESC:
				return "Tesselation Control";
			case SHADER_TESE:
				return "Tesselation Evaluation";
			default:
				return "Invalid Type";
		}
	}

	private static CharSequence getShaderData(File shader) {
		StringBuilder data = new StringBuilder();
		BufferedReader file = null;
		String line;
		try {
			file = new BufferedReader(new FileReader(shader));
			while ((line = file.readLine()) != null)
				data.append(line).append("\n");
			file.close();
		}
		catch (FileNotFoundException e) {
			Debug.log(Debug.SHADER_MANAGER, "[", shader.getName(), "] shader not found.");
		}
		catch (IOException e) {
			Debug.log(Debug.SHADER_MANAGER, "Error reading shader [", shader.getName(), "].");
		}
		return data;
	}
}
