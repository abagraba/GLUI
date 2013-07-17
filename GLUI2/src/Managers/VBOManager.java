package Managers;

import static Util.GLCONST.*;

import java.util.HashMap;

import org.lwjgl.opengl.GL15;

import Util.Debug;
//@off
//@on

public class VBOManager {

	private static final int[] glTarget = new int[] {GL15.GL_ARRAY_BUFFER, GL15.GL_ELEMENT_ARRAY_BUFFER};
	private static final VBO[] boundBuffers = new VBO[3];

	private static final HashMap<String, VBO> vbos = new HashMap<String, VBO>();

	/**
	 * Creates a VBO associated with the name and buffers the specified data.
	 * @param name name to be associated with the VBO.
	 * @param data byte data to be buffered into the VBO.
	 * @param target target for the VBO to be bound to.
	 * @return the VBO created. null if creation failed.
	 */
	public static VBO createStaticVBO(String name, byte[] data, int target) {
		int buffer = GL15.glGenBuffers();
		if (buffer == 0) {
			Debug.log(Debug.VBO_MANAGER, "Vertex Buffer for [", name, "] could not be allocated.");
			return null;
		}
		VBO vbo = new VBO(name, buffer, BYTE);
		mapVBO(name, vbo);
		return vbo.bufferData(glTarget[target], data, STATIC);
	}

	/**
	 * Creates a VBO associated with the name and buffers the specified data.
	 * @param name name to be associated with the VBO.
	 * @param data int data to be buffered into the VBO.
	 * @param target target for the VBO to be bound to.
	 * @return the VBO created. null if creation failed.
	 */
	public static VBO createStaticVBO(String name, int[] data, int target) {
		int buffer = GL15.glGenBuffers();
		if (buffer == 0) {
			Debug.log(Debug.VBO_MANAGER, "Vertex Buffer for [", name, "] could not be allocated.");
			return null;
		}
		VBO vbo = new VBO(name, buffer, INT);
		mapVBO(name, vbo);
		return vbo.bufferData(target, data, STATIC);
	}

	/**
	 * Creates a VBO associated with the name and buffers the specified data.
	 * @param name name to be associated with the VBO.
	 * @param data float data to be buffered into the VBO.
	 * @param target target for the VBO to be bound to.
	 * @return the VBO created. null if creation failed.
	 */
	public static VBO createStaticVBO(String name, float[] data, int target) {
		int buffer = GL15.glGenBuffers();
		if (buffer == 0) {
			Debug.log(Debug.VBO_MANAGER, "Vertex Buffer for [", name, "] could not be allocated.");
			return null;
		}
		VBO vbo = new VBO(name, buffer, FLOAT);
		mapVBO(name, vbo);
		return vbo.bufferData(target, data, STATIC);
	}

	/**
	 * Creates a VBO associated with the name and accepting the specified type.
	 * @param name name to be associated with the VBO.
	 * @param type type of data accepted.
	 * @return the VBO created. null if creation failed.
	 */
	public static VBO createVBO(String name, int type) {
		int buffer = GL15.glGenBuffers();
		if (buffer == 0) {
			Debug.log(Debug.VBO_MANAGER, "Vertex Buffer for [", name, "] could not be allocated.");
			return null;
		}
		VBO vbo = new VBO(name, buffer, type);
		mapVBO(name, vbo);
		return vbo;
	}

	/**
	 * Gets the VBO associated with the specified name.
	 * @param vboName name associated with the VBO.
	 * @return the VBO associated with the specified name.
	 */
	public static VBO getVBO(String vboName) {
		return vbos.get(vboName);
	}

	/**
	 * Associates specified name with the specified VBO. Used for manual VBO management.
	 * @param name name to be associated.
	 * @param vbo VBO to be associated.
	 */
	private static void mapVBO(String name, VBO vbo) {
		VBO old = vbos.put(name, vbo);
		if (old != null) {
			for (int i = 0; i < boundBuffers.length; i++)
				if (isVBOBound(old, i))
					unbindVBO(i);
			old.delete();
		}
	}

	/**
	 * Binds VBO associated with vboName to target. No-op is already bound.
	 * @param vboName VBO to be bound. Unbinds VBOs if null.
	 * @param target takes {@link Util.GLCONST#ARRAY_BUFFER}, {@link Util.GLCONST#ELEMENT_ARRAY_BUFFER}.
	 */
	public static void bindVBO(String vboName, int target) {
		bindVBO(getVBO(vboName), target);
	}

	/**
	 * Binds VBO to the target. Should only be used if not using VBOMANAGER management. No-op if already bound.
	 * @param vbo VBO to be bound. Unbinds VBOs if null.
	 * @param target takes {@link Util.GLCONST#ARRAY_BUFFER}, {@link Util.GLCONST#ELEMENT_ARRAY_BUFFER}.
	 */
	public static void bindVBO(VBO vbo, int target) {
		if (vbo == null)
			unbindVBO(target);
		if (isVBOBound(vbo, target))
			return;
		GL15.glBindBuffer(target, (boundBuffers[targetIndex(target)] = vbo).buffer);
	}

	/**
	 * Unbind VBO from the target if any are bound. No-op if nothing is bound.
	 * @param target takes {@link Util.GLCONST#ARRAY_BUFFER}, {@link Util.GLCONST#ELEMENT_ARRAY_BUFFER}.
	 */
	public static void unbindVBO(int target) {
		if (boundBuffers[targetIndex(target)] == null)
			return;
		GL15.glBindBuffer(target, 0);
		boundBuffers[targetIndex(target)] = null;
	}

	/**
	 * Checks whether specified VBO is bound to the target.
	 * @param vbo VBO to be checked.
	 * @param target target to be checked.
	 * @return whether the specified VBO is bound to the target.
	 */
	public static boolean isVBOBound(VBO vbo, int target) {
		return boundBuffers[targetIndex(target)] == vbo;
	}

	private static int targetIndex(int target) {
		switch (target) {
			case ARRAY_BUFFER:
				return 0;
			case ELEMENT_ARRAY_BUFFER:
				return 1;
			default:
				return -1;
		}
	}

}
