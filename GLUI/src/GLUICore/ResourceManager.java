package GLUICore;

import java.io.File;
import java.util.Collection;
import java.util.Hashtable;

import GLUIRenderer.Texture;
import GLUIUtil.FileUtil;
import GLUIUtil.TEXExtractor;

public class ResourceManager {

	private static File root = new File("/res");
	private static String vertexRoot = "/Texture";
	private static String textureRoot = "/Texture";
	public static byte pngFilter = Texture.NEAREST;

	private static final Hashtable<String, Texture> textures = new Hashtable<String, Texture>();

	static {
		setRoot(root);
	}

	@Deprecated
	public static float[] getVertexData(String filename) {
		return ITMReader.readData(root + vertexRoot + filename);
	}

	public static void setRoot(File directory) {
		if (!directory.isDirectory()) {
			Debug.log("Resource Manager: Root must specify a directory. Currently specifies: " + directory.getPath());
			return;
		}
		ResourceManager.root = directory.getAbsoluteFile();
		setTextureRoot(textureRoot);
	}

	public static Texture getTexture(String texture) {
		return textures.get(texture);
	}

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

	private static void searchForTextures(File directory) {
		if (!isDirectory(directory))
			return;
		for (File file : FileUtil.getFilesOfType(directory, "png"))
			addTexture(new Texture(file.getName(), file.getPath(), 1, 1, 0, Texture.TEXTURE_2D, pngFilter));
		for (File file : FileUtil.getFilesOfType(directory, "tex"))
			addTextures(TEXExtractor.readPTEX(file));
	}

	public static void addTexture(Texture t) {
		textures.put(t.name, t);
	}

	public static void addTextures(Collection<Texture> c) {
		for (Texture t : c)
			addTexture(t);
	}

	private static boolean isDirectory(File f) {
		if (f.isDirectory())
			return true;
		Debug.log("File " + f.getPath() + " is not a valid directory.");
		return false;
	}

}
