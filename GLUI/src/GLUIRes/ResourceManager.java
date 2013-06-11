package GLUIRes;

import java.io.File;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Set;

import GLUI.Debug;
import GLUIRenderer.Texture;
import GLUIUtil.FNTExtractor;
import GLUIUtil.FileUtil;
import GLUIUtil.TEXExtractor;

/**
 * The Resource Manager controls where resources are pulled from. Setting the root variables causes the Resource Manager
 * to clear its respective resource cache and repopulate it with new data from the specified location in the filesystem.
 * These calls should be kept to a minimum and reserved only for initializing the program and the occasional context
 * change (TexturePack changes and such).
 */
public class ResourceManager {

	private static File root = new File("res");
	private static String textureRoot = "/Texture";
	private static String fontRoot = "/Font";
	private static String shaderRoot = "/Shader";

	public static byte pngFilter = Texture.NEAREST;

	private static final Hashtable<String, Texture> textures = new Hashtable<String, Texture>();
	private static final Hashtable<String, Font> fonts = new Hashtable<String, Font>();
	private static final Hashtable<String, Shader> shaders = new Hashtable<String, Shader>();;

	static {
		setRoot(root);
	}

	public static void setRoot(File directory) {
		if (!directory.isDirectory()) {
			Debug.log("Resource Manager: Root must specify a directory. Currently specifies: " + directory.getAbsolutePath());
			return;
		}
		ResourceManager.root = directory.getAbsoluteFile();
		setTextureRoot(textureRoot);
		setFontRoot(fontRoot);
	}

	/**
	 * Lists all textures found under root/textureRoot.
	 * @return Set of texture names.
	 * @see #getTexture(String) .
	 */
	public static Set<String> listTexturesAvailable() {
		return textures.keySet();
	}

	/**
	 * Lists all shaders found under root/shaderRoot.
	 * @return Set of shader names.
	 * @see #getShader(String) .
	 */
	public static Set<String> listShadersAvailable() {
		return fonts.keySet();
	}

	/**
	 * Lists all fonts found under root/fontRoot.
	 * @return Set of font names.
	 * @see #getFont(String) .
	 */
	public static Set<String> listFontsAvailable() {
		return fonts.keySet();
	}

	/**
	 * Returns the {@link Texture} object associated with the provided texture name.
	 * @param textureName name of the requested texture.
	 * @return Texture object associated with the texture name. Returns null if the name does not exist.
	 */
	public static Texture getTexture(String textureName) {
		return textures.get(textureName);
	}

	/**
	 * Returns the {@link Font} object associated with the provided font name.
	 * @param fontName name of the requested font.
	 * @return Font object associated with the font name. Returns null if the name does not exist.
	 */
	public static Font getFont(String fontName) {
		return fonts.get(fontName);
	}

	/**
	 * Returns the {@link Shader} object associated with the provided shader name.
	 * @param shaderName name of the requested shader.
	 * @return Shader object associated with the shader name. Returns null if the name does not exist.
	 */
	public static Shader getShader(String shaderName) {
		return shaders.get(shaderName);
	}

	/**
	 * Manually adds a Texture to the {@link Texture} cache.
	 * @param texture Texture to be added to cache.
	 */
	public static void addTexture(Texture texture) {
		textures.put(texture.name, texture);
	}

	/**
	 * Manually adds multiple Textures to the {@link Texture} cache.
	 * @param textures Textures to be added to cache.
	 * @see #addTexture(Texture) .
	 */
	public static void addTextures(Collection<Texture> textures) {
		for (Texture texture : textures)
			addTexture(texture);
	}

	/**
	 * Manually adds a Font to the {@link Font} cache.
	 * @param font Font to be added to cache.
	 */
	public static void addFont(Font font) {
		fonts.put(font.name, font);
	}

	/**
	 * Manually adds a Font to the {@link Font} cache.
	 * @param fonts Font to be added to cache.
	 * @see #addFont(Font) .
	 */

	public static void addFonts(Collection<Font> fonts) {
		for (Font font : fonts)
			addFont(font);
	}

	/**
	 * Manually adds a Shader to the {@link Shader} cache.
	 * @param shader Shader to be added to cache.
	 */
	public static void addShader(Shader shader) {
		shaders.put(shader.name, shader);
	}

	/**
	 * Manually adds a Shader to the {@link Shader} cache.
	 * @param shaders Shader to be added to cache.
	 * @see #addShader(Shader) .
	 */

	public static void addShaders(Collection<Shader> shaders) {
		for (Shader shader : shaders)
			addShader(shader);
	}

	/**
	 * Clears the existing {@link Texture} cache and repopulates it using files found in the directory specified by
	 * root/textureRoot.
	 * @param textureRoot directory under root in which Texture definition files are found.
	 */
	public static void setTextureRoot(String textureRoot) {
		textures.clear();
		ResourceManager.textureRoot = textureRoot;
		File f = new File(root.getPath() + ResourceManager.textureRoot);
		if (!f.isDirectory()) {
			Debug.log("Resource Manager: Texture Root must specify a directory. Currently specifies: " + f.getPath());
			return;
		}
		searchForTextures(f);
	}

	/**
	 * Clears the existing {@link Font} cache and repopulates it using files found in the directory specified by
	 * root/fontRoot.
	 * @param fontRoot directory under root in which Font definition files are found.
	 */
	public static void setFontRoot(String fontRoot) {
		fonts.clear();
		ResourceManager.fontRoot = fontRoot;
		File f = new File(root.getPath() + ResourceManager.fontRoot);
		if (!f.isDirectory()) {
			Debug.log("Resource Manager: Font Root must specify a directory. Currently specifies: " + f.getPath());
			return;
		}
		searchForFonts(f);
	}

	/**
	 * Clears the existing {@link Shader} cache and repopulates it using files found in the directory specified by
	 * root/shaderRoot.
	 * @param shaderRoot directory under root in which Shader definition files are found.
	 */
	public static void setShaderRoot(String shaderRoot) {
		fonts.clear();
		ResourceManager.shaderRoot = shaderRoot;
		File f = new File(root.getPath() + ResourceManager.shaderRoot);
		if (!f.isDirectory()) {
			Debug.log("Resource Manager: Shader Root must specify a directory. Currently specifies: " + f.getPath());
			return;
		}
		searchForShaders(f);
	}

	private static void searchForTextures(File directory) {
		if (!isDirectory(directory))
			return;
		for (File file : FileUtil.getFilesOfType(directory, "png"))
			addTexture(new Texture(file.getName(), file.getPath(), 1, 1, 0, Texture.TEXTURE_2D, pngFilter));
		for (File file : FileUtil.getFilesOfType(directory, "tex"))
			addTextures(TEXExtractor.readPTEX(file));
	}

	private static void searchForFonts(File directory) {
		if (!isDirectory(directory))
			return;
		for (File file : FileUtil.getFilesOfType(directory, "fnt"))
			addFont(FNTExtractor.readFONT(file));
	}

	private static void searchForShaders(File directory) {
		if (!isDirectory(directory))
			return;
		// FIXME add Shader reading.
		// for (File file : FileUtil.getFilesOfType(directory, "fnt"))
		// addFont(FNTExtractor.readFONT(file));
	}

	private static boolean isDirectory(File f) {
		if (f.isDirectory())
			return true;
		Debug.log("File " + f.getPath() + " is not a valid directory.");
		return false;
	}

}
