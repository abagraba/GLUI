package GLUICore;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class BufferUtil {
	
	public static ByteBuffer asDirectByteBuffer(byte[] data) {
		ByteBuffer b = BufferUtils.createByteBuffer(data.length);
		b.put(data).flip();
		return b;
	}

	public static FloatBuffer asDirectFloatBuffer(float[] data) {
		FloatBuffer f = BufferUtils.createFloatBuffer(data.length);
		f.put(data).flip();
		return f;
	}

	public static IntBuffer asDirectIntBuffer(int[] data) {
		IntBuffer i = BufferUtils.createIntBuffer(data.length);
		i.put(data).flip();
		return i;
	}
	
}
