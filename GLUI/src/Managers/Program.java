package Managers;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.util.ArrayList;

import Util.Debug;

public class Program {

	public final String name;
	private final int buffer;
	private final ArrayList<Shader> shaders = new ArrayList<Shader>();

	protected static Program createProgram(String name, Shader... shaders) {
		Program p = new Program(name, shaders);
		if (p.buffer == 0)
			return null;
		return p;
	}

	private Program(String name, Shader... shaders) {
		this.name = name;
		int program = glCreateProgram();
		if (program == 0) {
			Debug.log(Debug.SHADER_MANAGER, "Failure to create program [", name, "]");
			buffer = 0;
			return;
		}
		for (Shader shader : shaders) {
			glAttachShader(program, shader.buffer);
			this.shaders.add(shader);
		}
		glLinkProgram(program);
		if (glGetShader(program, GL_LINK_STATUS) == GL_FALSE) {
			Debug.log(Debug.SHADER_MANAGER, "Failure to link program [", name, "]");
			buffer = 0;
			return;
		}
		glValidateProgram(program);
		if (glGetShader(program, GL_VALIDATE_STATUS) == GL_FALSE) {
			Debug.log(Debug.SHADER_MANAGER, "Failure to validate program [", name, "]");
			buffer = 0;
			return;
		}
		buffer = program;
	}

	public void use() {
		if (ShaderManager.currentProgram == this)
			return;
		glUseProgram(buffer);
		ShaderManager.currentProgram = this;
	}

	public int getAttribute(String name) {
		return glGetAttribLocation(buffer, name);
	}

}
