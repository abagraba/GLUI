package GLUICore;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import GLUICore.BufferUtil;

import static org.lwjgl.opengl.GL11.*;

public class TextureManager {

	private static final int MAG_FILTER = 0;
	private static final int MIN_FILTER = 1;

	private static Hashtable<String, Integer> textures = new Hashtable<String, Integer>();
	private static String root = "src/Texture/";

	//TODO fix Texture Binding. If texture already bound then ignore this.
	public static void bindTexture(String name, int type, int filter) {
		if (!textures.containsKey(name))
			createTexture(name, filter);
		GL11.glBindTexture(type, textures.get(name));
	}

	/**
	 * Initializes a texture from the specified PNG file.
	 * @param name name of the PNG file. The PNG file must exist in the folder specified by root. The filename is specified without filetype. Ex:
	 *        "Image" not "Image.png".
	 * @param filter filter to be used. Valid values: GL_NEAREST, GL_NEAREST_MIPMAP_NEAREST, GL_NEAREST_MIPMAP_LINEAR, GL_LINEAR,
	 *        GL_LINEAR_MIPMAP_NEAREST, GL_LINEAR_NEAREST_LINEAR.
	 */
	private static void createTexture(String name, int filter) {
		BufferedImage i = getImage(name);
		int texID = glGenTextures();
		if (texID == 0)
			throw new TextureException("Texture");

		glBindTexture(GL_TEXTURE_2D, texID);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, getFilter(filter, MAG_FILTER));
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, getFilter(filter, MIN_FILTER));
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, i.getWidth(), i.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE,
				BufferUtil.asDirectByteBuffer(getImageData(i)));
		glBindTexture(GL_TEXTURE_2D, 0);

		textures.put(name, texID);
	}

	/**
	 * Gets RGBA data from an image in the form of a byte[].
	 * @param image image to get data from.
	 * @return a byte[] representing the raw image data in RGBA form.
	 */
	private static byte[] getImageData(BufferedImage image) {
		if (image.getType() != BufferedImage.TYPE_4BYTE_ABGR) {
			BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
			bi.getGraphics().drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
			image = bi;
		}
		byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		for (int index = 0; index < data.length / 4; index++) {
			byte a = data[4 * index + 0];
			byte b = data[4 * index + 1];
			data[4 * index + 0] = data[4 * index + 3];
			data[4 * index + 3] = a;
			data[4 * index + 1] = data[4 * index + 2];
			data[4 * index + 2] = b;
		}
		return data;
	}

	/**
	 * Returns the proper filter given a filterType.
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
		return -1;
	}

	private static BufferedImage getImage(String name) {
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(new File(root + name + ".png"));
		} catch (IOException e) {
			System.out.println("File not found: " + name);
			e.printStackTrace();
		}
		return bi;
	}

	/**
	 * Sets the root directory for the TextureManager. All textures will be searched for in the root directory.
	 * @param root new root directory.
	 */
	public static void setRoot(String root) {
		TextureManager.root = root;
	}

	/**
	 * Cleans up the specified texture.
	 * @param texture the specified texture.
	 */
	public static void clean(String texture) {
		GL11.glDeleteTextures(textures.remove(texture));
	}

	/**
	 * Cleans up the specified textures.
	 * @param textures the specified textures.
	 */
	public static void clean(String[] textures) {
		for (int i = 0; i < textures.length; i++)
			clean(textures[i]);
	}

	/**
	 * Cleans up all the textures.
	 */
	public static void cleanAll() {
		GL11.glBindTexture(GL_TEXTURE_2D, 0);
		for (int texture : textures.values())
			GL11.glDeleteTextures(texture);
		textures.clear();
	}

	@SuppressWarnings("serial")
	public static class TextureException extends RuntimeException {
		public TextureException(String s) {
			super(s);
		}
	}

}
