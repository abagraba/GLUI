package Rendering;

import static Util.GLCONST.TYPE_UBYTE;
import static Util.GLCONST.TYPE_FLOAT;
import static Util.GLCONST.TYPE_UINT;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL15;

import Util.BufferUtil;
import Util.Debug;

public class VBO {

	protected final String name;
	protected final int buffer;
	private final int dataType;

	/**
	 * Creates a new uninitialized VBO object. This should only be called by {@link VBOManager#createVBO(String, int)}
	 * and related methods.
	 * @param name name associated with this VBO.
	 * @param buffer the VBO id associated with this VBO.
	 * @param dataType the type of data accepted by this VBO. Takes {@link #TYPE_UBYTE}, {@link #TYPE_UINT}, {@link #TYPE_FLOAT}.
	 */
	public VBO(String name, int buffer, int dataType) {
		this.name = name;
		this.buffer = buffer;
		this.dataType = dataType;
	}

	/**
	 * Buffers data into the VBO. Has overhead due to conversion to ByteBuffer. If possible use
	 * {@link #bufferData(int, ByteBuffer, int)}.
	 * @param target binding target for the VBO.
	 * @param data byte data to be buffered into the VBO.
	 * @param hint usage hint. Accepts {@link Util.GLCONST#VBO_STATIC}, {@link Util.GLCONST#VBO_DYNAMIC}.
	 * @return this VBO if data successfully buffered. null otherwise.
	 */
	public VBO bufferData(int target, byte[] data, int hint) {
		if (!VBOManager.isVBOBound(this, target))
			VBOManager.bindVBO(this, target);
		if (!isType(TYPE_UBYTE)) {
			Debug.log(Debug.VBO_MANAGER, "VBO buffering failed. Data is of type byte while [", name, "] is of type ",
					typeName(dataType), ".");
			return null;
		}
		GL15.glBufferData(target, BufferUtil.asDirectByteBuffer(data), hint);
		return this;
	}

	/**
	 * Buffers data into the VBO.
	 * @param target binding target for the VBO.
	 * @param data byte data to be buffered into the VBO.
	 * @param hint usage hint. Accepts {@link Util.GLCONST#VBO_STATIC}, {@link Util.GLCONST#VBO_DYNAMIC}.
	 * @return this VBO if data successfully buffered. null otherwise.
	 */
	public VBO bufferData(int target, ByteBuffer data, int hint) {
		if (!VBOManager.isVBOBound(this, target))
			VBOManager.bindVBO(this, target);
		if (!isType(TYPE_UBYTE)) {
			Debug.log(Debug.VBO_MANAGER, "VBO buffering failed. Data is of type byte while [", name, "] is of type ",
					typeName(dataType), ".");
			return null;
		}
		GL15.glBufferData(target, data, hint);
		return this;
	}

	/**
	 * Buffers data into the VBO. Has overhead due to conversion to IntBuffer. If possible use
	 * {@link #bufferData(int, IntBuffer, int)}.
	 * @param target binding target for the VBO.
	 * @param data int data to be buffered into the VBO.
	 * @param hint usage hint. Accepts {@link Util.GLCONST#VBO_STATIC}, {@link Util.GLCONST#VBO_DYNAMIC}.
	 * @return this VBO if data successfully buffered. null otherwise.
	 */
	public VBO bufferData(int target, int[] data, int hint) {
		if (!VBOManager.isVBOBound(this, target))
			VBOManager.bindVBO(this, target);
		if (!isType(TYPE_UINT)) {
			Debug.log(Debug.VBO_MANAGER, "VBO buffering failed. Data is of type int while [", name, "] is of type ",
					typeName(dataType), ".");
			return null;
		}
		GL15.glBufferData(target, BufferUtil.asDirectIntBuffer(data), hint);
		return this;
	}

	/**
	 * Buffers data into the VBO.
	 * @param target binding target for the VBO.
	 * @param data int data to be buffered into the VBO.
	 * @param hint usage hint. Accepts {@link Util.GLCONST#VBO_STATIC}, {@link Util.GLCONST#VBO_DYNAMIC}.
	 * @return this VBO if data successfully buffered. null otherwise.
	 */
	public VBO bufferData(int target, IntBuffer data, int hint) {
		if (!VBOManager.isVBOBound(this, target))
			VBOManager.bindVBO(this, target);
		if (!isType(TYPE_UINT)) {
			Debug.log(Debug.VBO_MANAGER, "VBO buffering failed. Data is of type int while [", name, "] is of type ",
					typeName(dataType), ".");
			return null;
		}
		GL15.glBufferData(target, data, hint);
		return this;
	}

	/**
	 * Buffers data into the VBO. Has overhead due to conversion to FloatBuffer. If possible use
	 * {@link #bufferData(int, FloatBuffer, int)}.
	 * @param target binding target for the VBO.
	 * @param data float data to be buffered into the VBO.
	 * @param hint usage hint. Accepts {@link Util.GLCONST#VBO_STATIC}, {@link Util.GLCONST#VBO_DYNAMIC}.
	 * @return this VBO if data successfully buffered. null otherwise.
	 */
	public VBO bufferData(int target, float[] data, int hint) {
		if (!VBOManager.isVBOBound(this, target))
			VBOManager.bindVBO(this, target);
		if (!isType(TYPE_FLOAT)) {
			Debug.log(Debug.VBO_MANAGER, "VBO buffering failed. Data is of type float while [", name, "] is of type ",
					typeName(dataType), ".");
			return null;
		}
		GL15.glBufferData(target, BufferUtil.asDirectFloatBuffer(data), hint);
		return this;
	}

	/**
	 * Buffers data into the VBO.
	 * @param target binding target for the VBO.
	 * @param data float data to be buffered into the VBO.
	 * @param hint usage hint. Accepts {@link Util.GLCONST#VBO_STATIC}, {@link Util.GLCONST#VBO_DYNAMIC}.
	 * @return this VBO if data successfully buffered. null otherwise.
	 */
	public VBO bufferData(int target, FloatBuffer data, int hint) {
		if (!VBOManager.isVBOBound(this, target))
			VBOManager.bindVBO(this, target);
		if (!isType(TYPE_FLOAT)) {
			Debug.log(Debug.VBO_MANAGER, "VBO buffering failed. Data is of type float while [", name, "] is of type ",
					typeName(dataType), ".");
			return null;
		}
		GL15.glBufferData(target, data, hint);
		return this;
	}

	public VBO bufferNone(int target, int hint) {
		if (!VBOManager.isVBOBound(this, target))
			VBOManager.bindVBO(this, target);
		IntBuffer ib = null;
		GL15.glBufferData(target, ib, hint);
		return this;
	}

	/**
	 * Removes this buffer from GPU memory.
	 */
	public void delete() {
		GL15.glDeleteBuffers(buffer);
	}

	/**
	 * Tests what type of values the VBO takes.
	 * @param type takes {@link Util.GLCONST#TYPE_UBYTE}, {@link Util.GLCONST#TYPE_UINT}, {@link Util.GLCONST#TYPE_FLOAT}.
	 * @return whether this VBO accepts values of this type.
	 */
	public boolean isType(int type) {
		return dataType == type;
	}

	/**
	 * Returns a String that represents the name of the provided type.
	 * @param type takes {@link Util.GLCONST#TYPE_UBYTE}, {@link Util.GLCONST#TYPE_UINT}, {@link Util.GLCONST#TYPE_FLOAT}.
	 * @return a String that represents the name of the provided type.
	 */
	private static String typeName(int type) {
		switch (type) {
			case TYPE_UBYTE:
				return "byte";
			case TYPE_UINT:
				return "int";
			case TYPE_FLOAT:
				return "float";
			default:
				return "INVALID_TYPE";
		}
	}
}
