package Util;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Collection;

import org.lwjgl.BufferUtils;

public class BufferUtil {

	public static ByteBuffer asDirectByteBuffer(byte[] data) {
		ByteBuffer b = BufferUtils.createByteBuffer(data.length);
		b.put(data).flip();
		return b;
	}

	public static ByteBuffer asDirectByteBuffer(Byte[] data) {
		ByteBuffer b = BufferUtils.createByteBuffer(data.length);
		for (Byte x : data)
			b.put(x);
		b.flip();
		return b;
	}

	public static ByteBuffer asDirectByteBuffer(Collection<Byte> data) {
		ByteBuffer b = BufferUtils.createByteBuffer(data.size());
		for (Byte x : data)
			b.put(x);
		b.flip();
		return b;
	}

	public static FloatBuffer asDirectFloatBuffer(float[] data) {
		FloatBuffer f = BufferUtils.createFloatBuffer(data.length);
		f.put(data).flip();
		return f;
	}

	public static FloatBuffer asDirectFloatBuffer(Float[] data) {
		FloatBuffer f = BufferUtils.createFloatBuffer(data.length);
		for (Float x : data)
			f.put(x);
		f.flip();
		return f;
	}

	public static FloatBuffer asDirectFloatBuffer(Collection<Float> data) {
		FloatBuffer f = BufferUtils.createFloatBuffer(data.size());
		for (Float x : data)
			f.put(x);
		f.flip();
		return f;
	}

	public static IntBuffer asDirectIntBuffer(int[] data) {
		IntBuffer i = BufferUtils.createIntBuffer(data.length);
		i.put(data).flip();
		return i;
	}

	public static IntBuffer asDirectIntBuffer(Integer[] data) {
		IntBuffer i = BufferUtils.createIntBuffer(data.length);
		for (Integer x : data)
			i.put(x);
		i.flip();
		return i;
	}

	public static IntBuffer asDirectIntBuffer(Collection<Integer> data) {
		IntBuffer i = BufferUtils.createIntBuffer(data.size());
		for (Integer x : data)
			i.put(x);
		i.flip();
		return i;
	}

}
