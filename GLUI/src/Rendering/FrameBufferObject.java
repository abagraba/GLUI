package Rendering;

import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.opengl.GL11;

import Managers.Texture;

public class FrameBufferObject extends RenderTarget {

	public final static int FRAMEBUFFER = GL_FRAMEBUFFER;
	public final static int COLOR0 = GL_COLOR_ATTACHMENT0;
	public final static int COLOR1 = GL_COLOR_ATTACHMENT1;
	public final static int COLOR2 = GL_COLOR_ATTACHMENT2;
	public final static int COLOR3 = GL_COLOR_ATTACHMENT3;
	public final static int COLOR4 = GL_COLOR_ATTACHMENT4;
	public final static int COLOR5 = GL_COLOR_ATTACHMENT5;
	public final static int COLOR6 = GL_COLOR_ATTACHMENT6;
	public final static int COLOR7 = GL_COLOR_ATTACHMENT7;
	public final static int COLOR8 = GL_COLOR_ATTACHMENT8;
	public final static int COLOR9 = GL_COLOR_ATTACHMENT9;
	public final static int COLOR10 = GL_COLOR_ATTACHMENT10;
	public final static int COLOR11 = GL_COLOR_ATTACHMENT11;
	public final static int COLOR12 = GL_COLOR_ATTACHMENT12;
	public final static int COLOR13 = GL_COLOR_ATTACHMENT13;
	public final static int COLOR14 = GL_COLOR_ATTACHMENT14;
	public final static int COLOR15 = GL_COLOR_ATTACHMENT15;
	public final static int NONE = GL11.GL_NONE;

	protected final int bufferID;

	public FrameBufferObject() {
		int buffer = glGenFramebuffers();
		if (buffer <= 0) {
			bufferID = 0;
			return;
		}

		bufferID = buffer;
	}

	public void attachTexture(Texture t, int attachment, int mipMapLevel) {
		glBindFramebuffer(FRAMEBUFFER, bufferID);
		glFramebufferTexture2D(FRAMEBUFFER, attachment, t.getGLTarget(), t.bufferID, mipMapLevel);
		glBindFramebuffer(FRAMEBUFFER, 0);
	}

	public void setDrawBuffer(int mode) {
		glBindFramebuffer(FRAMEBUFFER, bufferID);
		GL11.glReadBuffer(mode);
		glBindFramebuffer(FRAMEBUFFER, 0);
	}

	public void setReadBuffer(int mode) {
		glBindFramebuffer(FRAMEBUFFER, bufferID);
		GL11.glReadBuffer(mode);
		glBindFramebuffer(FRAMEBUFFER, 0);
	}

	@Override
	public void init() {

	}

	@Override
	protected void passInit() {
		glBindFramebuffer(FRAMEBUFFER, bufferID);
	}

	@Override
	protected void passFinish() {
		glBindFramebuffer(FRAMEBUFFER, 0);
	}

}
