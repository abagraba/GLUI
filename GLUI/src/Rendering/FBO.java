package Rendering;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import Util.Debug;
import Util.GLCONST;

public class FBO {

	private static HashMap<String, FBO> fbos = new HashMap<String, FBO>(4);
	private static int[] colorAttachments = new int[] {GLCONST.FBO_COLOR0, GLCONST.FBO_COLOR1, GLCONST.FBO_COLOR2,
														GLCONST.FBO_COLOR3, GLCONST.FBO_COLOR4, GLCONST.FBO_COLOR5,
														GLCONST.FBO_COLOR6, GLCONST.FBO_COLOR7, GLCONST.FBO_COLOR8,
														GLCONST.FBO_COLOR9, GLCONST.FBO_COLOR10, GLCONST.FBO_COLOR11,
														GLCONST.FBO_COLOR12, GLCONST.FBO_COLOR13, GLCONST.FBO_COLOR14,
														GLCONST.FBO_COLOR15};
	private final int buffer;

	private FBO(String name, int width, int height, int colorBuffers, boolean depthBuffer, boolean stencilbuffer) {
		int buffer = GL30.glGenFramebuffers();
		if (buffer == -1) {
			this.buffer = 0;
			return;
		}
		GL30.glBindFramebuffer(GLCONST.FBO_DRAW, buffer);

		for (int b = 0; b < colorBuffers; b++) {
			int color = GL11.glGenTextures();
			if (color == -1) {
				Debug.log(Debug.FBO, "Failure to allocate FBO [", name, "] color texture ", b + ".");
				this.buffer = 0;
				return;
			}
			GL11.glBindTexture(GLCONST.TEXTURE_2D, color);
			GL11.glTexImage2D(GLCONST.TEXTURE_2D, 0, GLCONST.FORMAT_RGB32I, width, height, 0, GLCONST.FORMAT_RGB_I,
					GLCONST.TYPE_UINT, 0);
			GL30.glFramebufferTexture2D(GLCONST.FBO_DRAW, colorAttachments[b], GLCONST.TEXTURE_2D, color, 0);
		}

		if (depthBuffer) {
			int iformat = stencilbuffer ? GLCONST.FORMAT_D24S8 : GLCONST.FORMAT_D32;
			int format = stencilbuffer ? GLCONST.FORMAT_DS : GLCONST.FORMAT_D;
			int bufferTarget = stencilbuffer ? GLCONST.FBO_DEPTH_STENCIL : GLCONST.FBO_DEPTH;

			int depth = GL11.glGenTextures();
			if (depth == -1) {
				Debug.log(Debug.FBO, "Failure to allocate FBO [", name, "] depth/stencil texture.");
				this.buffer = 0;
				return;
			}
			GL11.glBindTexture(GLCONST.TEXTURE_2D, depth);
			GL11.glTexImage2D(GLCONST.TEXTURE_2D, 0, iformat, width, height, 0, format, GLCONST.TYPE_FLOAT, 0);
			GL30.glFramebufferTexture2D(GLCONST.FBO_DRAW, bufferTarget, GLCONST.TEXTURE_2D, depth, 0);
		}

		if (GL30.glCheckFramebufferStatus(buffer) != GLCONST.FBO_COMPLETE) {
			Debug.log(Debug.FBO, "FBO [", name, "] incomplete.");
			this.buffer = 0;
			return;
		}

		GL11.glBindTexture(GLCONST.TEXTURE_2D, 0);
		GL30.glBindFramebuffer(GLCONST.FBO_DRAW, 0);

		this.buffer = buffer;
	}

	public void bindToDraw() {
		GL30.glBindFramebuffer(GLCONST.FBO_DRAW, buffer);
	}

	public void bindToRead() {
		GL30.glBindFramebuffer(GLCONST.FBO_READ, buffer);
	}

	public void bindToAll() {
		GL30.glBindFramebuffer(GLCONST.FBO_FRAMEBUFFER, buffer);
	}

	@SuppressWarnings("static-method")
	public void unbind() {
		GL30.glBindFramebuffer(GLCONST.FBO_FRAMEBUFFER, 0);
	}
}
