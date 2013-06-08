package GLUICore;

import static org.lwjgl.opengl.GL11.GL_ALPHA;
import static org.lwjgl.opengl.GL11.GL_BLUE;
import static org.lwjgl.opengl.GL11.GL_GREEN;
import static org.lwjgl.opengl.GL11.GL_INVALID_VALUE;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RED;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_1D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameter;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_3D;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.opengl.GL14.GL_GENERATE_MIPMAP;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_1D_ARRAY;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_2D_ARRAY;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.opengl.GL33.GL_TEXTURE_SWIZZLE_RGBA;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import GLUIRenderer.Texture;

public class TextureManager {

	private static final int MAG_FILTER = 0;
	private static final int MIN_FILTER = 1;

	// FIXME implement version checking
	public static boolean gl30supported = true;

	private static Texture boundTexture = null;
	private static HashMap<Texture, Integer> textures = new HashMap<Texture, Integer>();

	public static void useTexture(String name) {
		Texture t = ResourceManager.getTexture(name);
		if (t == null) {
			Debug.log("Cannot use texture. " + name + " not found by ResourceManager.");
			useNone();
			return;
		}
		if (!t.equals(boundTexture)) {
			if (!textures.containsKey(t))
				createTexture(t);
			if (textures.containsKey(t)) {
				boundTexture = t;
				glBindTexture(t.getGLType(), textures.get(t));
			}
			else
				Debug.log("Cannot use texture. " + name + " failed to initialize.");
		}
	}

	public static void useTexture(Texture t) {
		if (t == null) {
			useNone();
			return;
		}
		if (!t.equals(boundTexture)) {
			if (!textures.containsKey(t))
				createTexture(t);
			if (textures.containsKey(t)) {
				boundTexture = t;
				glBindTexture(t.getGLType(), textures.get(t));
			}
			else
				Debug.log("Cannot use texture. " + t.name + " failed to initialize.");
		}
	}

	public static void useNone() {
		glBindTexture(GL_TEXTURE_2D, 0);
		boundTexture = null;
	}

	private static void createTexture(Texture texture) {
		BufferedImage i = getImage(texture);
		int texID = glGenTextures();
		if (texID == 0) {
			Debug.log("Cannot allocate texture buffer.");
			return;
		}
		int type = texture.getGLType();
		glBindTexture(type, texID);

		// Initializing Parameters
		switch (type) {
			case GL_TEXTURE_3D:
				glTexParameterf(type, GL_TEXTURE_WRAP_R, GL_REPEAT);
			case GL_TEXTURE_2D:
			case GL_TEXTURE_2D_ARRAY:
				glTexParameterf(type, GL_TEXTURE_WRAP_T, GL_REPEAT);
			case GL_TEXTURE_1D:
			case GL_TEXTURE_1D_ARRAY:
				glTexParameterf(type, GL_TEXTURE_WRAP_S, GL_REPEAT);
		}
		glTexParameteri(type, GL_TEXTURE_MAG_FILTER, getFilter(texture.getGLFilter(), MAG_FILTER));
		glTexParameteri(type, GL_TEXTURE_MIN_FILTER, getFilter(texture.getGLFilter(), MIN_FILTER));
		glTexParameter(type, GL_TEXTURE_SWIZZLE_RGBA,
				BufferUtil.asDirectIntBuffer(new int[] {GL_ALPHA, GL_BLUE, GL_GREEN, GL_RED}));

		// Buffering Texture Data.
		// FIXME Ensure GL_TEXTURE_3D GL_TEXTURE_1D support
		if (texture.usingMipMaps() && !gl30supported)
			glTexParameteri(type, GL_GENERATE_MIPMAP, GL_TRUE);
		if (texture.usingMipMaps() && gl30supported)
			glGenerateMipmap(type);
		else
			glTexImage2D(type, 0, GL_RGBA8, i.getWidth(), i.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE,
					BufferUtil.asDirectByteBuffer(getImageData(i)));

		textures.put(texture, texID);
		useNone();
	}

	/**
	 * Gets ABGR data from an image in the form of a byte[].
	 * @param image image to get data from.
	 * @return a byte[] representing the raw image data in ABGR form.
	 */
	private static byte[] getImageData(BufferedImage image) {
		if (image.getType() != BufferedImage.TYPE_4BYTE_ABGR) {
			BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
			bi.getGraphics().drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
			image = bi;
		}
		return ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	}

	/**
	 * Returns the proper filter given a intended filter.
	 * @param filter the intended filter. Accepts GL_
	 * @param filterType the type of the filter. Use GL_TEXTURE_MIN_FILTER or GL_TEXTURE_MAG_FILTER.
	 * @return the proper filter.
	 */
	private static int getFilter(int filter, int filterType) {
		if (filterType == MIN_FILTER)
			return filter;
		if (filterType == MAG_FILTER) {
			if (filter == GL_NEAREST || filter == GL_NEAREST_MIPMAP_NEAREST || filter == GL_NEAREST_MIPMAP_LINEAR)
				return GL_NEAREST;
			if (filter == GL_LINEAR || filter == GL_LINEAR_MIPMAP_NEAREST || filter == GL_LINEAR_MIPMAP_LINEAR)
				return GL_LINEAR;
		}
		return GL_INVALID_VALUE;
	}

	private static BufferedImage getImage(Texture t) {
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(new File(t.source));
		}
		catch (IOException e) {
			System.out.println("File not found: " + t.source);
			e.printStackTrace();
		}
		return bi;
	}

	/**
	 * Cleans up the specified texture.
	 * @param texture the specified texture.
	 */
	public static void clean(String texture) {
		glDeleteTextures(textures.remove(texture));
	}

	/**
	 * Cleans up the specified textures.
	 * @param textures the specified textures.
	 */
	public static void clean(String[] textures) {
		for (String texture : textures)
			clean(texture);
	}

	/**
	 * Cleans up all the textures.
	 */
	public static void cleanAll() {
		glBindTexture(GL_TEXTURE_2D, 0);
		for (int texture : textures.values())
			glDeleteTextures(texture);
		textures.clear();
	}

}
