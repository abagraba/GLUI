package Managers;

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
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import Util.BufferUtil;
import Util.Debug;

public class TextureManager {

	private static final int MAG_FILTER = 0;
	private static final int MIN_FILTER = 1;

	// FIXME implement version checking
	public static boolean gl30supported = true;

	private static int boundTexture = 0;
	private static int boundTarget = 0;
	private static HashMap<String, Texture> textures = new HashMap<String, Texture>();
	private static HashMap<File, Integer> sourceData = new HashMap<File, Integer>();

	private static final IntBuffer SWIZZLE_ABGR = BufferUtil.asDirectIntBuffer(new int[] {GL_ALPHA, GL_BLUE, GL_GREEN,
																							GL11.GL_RED});

	protected static void addNewTexture(Texture texture) {
		if (texture == null)
			return;
		textures.put(texture.name, texture);
	}

	/**
	 * Binds the specified texture if found. Binds 0 if none are found. Does nothing if the specified Texture is already
	 * bound.
	 * @param textureName name of the Texture to be bound. Searches the (@link ResourceManager) Texture cache for the
	 *        specified textureName.
	 */
	public static void useTexture(String textureName) {
		Texture t = textures.get(textureName);
		if (t == null)
			Debug.log(Debug.TEXTURE_MANAGER, "Texture: ", textureName, " cannot be found.");
		useTexture(t);
	}

	/**
	 * Binds the specified texture. Unbinds the bound Texture if null. Does nothing if the specified Texture is already
	 * bound.
	 * @param texture Texture to be bound.
	 */
	public static void useTexture(Texture texture) {
		if (texture == null) {
			useNone();
			return;
		}
		if (!textures.containsValue(texture))
			textures.put(texture.name, texture);
		int buffer = 0;
		if (sourceData.containsKey(texture.source))
			buffer = sourceData.get(texture.source);
		else {
			buffer = bufferTextureData(texture.source, texture.getGLTarget(), texture.getGLFilter(), texture.usingMipMaps());
			sourceData.put(texture.source, buffer);
		}
		if (buffer == 0)
			Debug.log(Debug.TEXTURE_MANAGER, "Cannot use texture. Buffer cannot be allocated.");
		bind(texture.getGLTarget(), buffer);
	}

	private static void bind(int target, int id) {
		if (boundTexture == id && boundTarget == target)
			return;
		glBindTexture(boundTarget = target, boundTexture = id);
	}

	private static int bufferTextureData(File source, int target, int filter, boolean mipmaps) {
		int texID = glGenTextures();
		glBindTexture(target, texID);
		if (texID == 0)
			return texID;

		// Initializing Parameters
		switch (target) {
			case GL_TEXTURE_3D:
				glTexParameterf(target, GL_TEXTURE_WRAP_R, GL_REPEAT);
			case GL_TEXTURE_2D:
			case GL_TEXTURE_2D_ARRAY:
				glTexParameterf(target, GL_TEXTURE_WRAP_T, GL_REPEAT);
			case GL_TEXTURE_1D:
			case GL_TEXTURE_1D_ARRAY:
				glTexParameterf(target, GL_TEXTURE_WRAP_S, GL_REPEAT);
		}
		glTexParameteri(target, GL_TEXTURE_MAG_FILTER, getFilter(filter, MAG_FILTER));
		glTexParameteri(target, GL_TEXTURE_MIN_FILTER, getFilter(filter, MIN_FILTER));
		glTexParameter(target, GL_TEXTURE_SWIZZLE_RGBA, SWIZZLE_ABGR);

		// Buffering Texture Data.
		// FIXME Ensure GL_TEXTURE_3D GL_TEXTURE_1D support Check buffering methods
		if (mipmaps && !gl30supported)
			glTexParameteri(target, GL_GENERATE_MIPMAP, GL_TRUE);

		BufferedImage i = getImage(source);
		glTexImage2D(target, 0, GL_RGBA8, i.getWidth(), i.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE,
				BufferUtil.asDirectByteBuffer(getImageData(i)));

		if (mipmaps && gl30supported)
			glGenerateMipmap(target);

		useNone();

		sourceData.put(source, texID);
		return texID;
	}

	/**
	 * Unbinds the bound Texture.
	 */
	public static void useNone() {
		if (boundTexture != 0)
			glBindTexture(GL_TEXTURE_2D, boundTexture = 0);
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

	/**
	 * Cleans up unused texture data. Should be called after deleting large numbers of textures to free up Texture
	 * buffers.
	 */
	public static void cleanData() {
		Set<Integer> buffers = new HashSet<Integer>();
		Set<File> inUse = new HashSet<File>();
		for (Texture texture : textures.values())
			inUse.add(texture.source);
		for (File f : sourceData.keySet())
			if (!inUse.contains(f))
				buffers.add(sourceData.remove(f));
		GL11.glDeleteTextures(BufferUtil.asDirectIntBuffer(buffers));
	}

	/**
	 * Gets Image from file.
	 * @param file file to get Image from.
	 * @return Image containing data from file.
	 */
	private static BufferedImage getImage(File file) {
		try {
			return ImageIO.read(file);
		}
		catch (IOException e) {
			Debug.log(Debug.TEXTURE_MANAGER, "Image File not found: ", file.getAbsolutePath());
			return null;
		}
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
}
