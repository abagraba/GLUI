package Tileset;

import java.awt.Dimension;
import java.io.File;
import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;

import GLcomponent.URect;
import GLres.VBOManager;
import GLui.Debug;
import GLui.RenderContainer;

public class TSR extends RenderContainer {

	// TODO implement defaulting to default tileset when error occur.
	private final String defaultTileset = "src/Tilesets/default16/";
	protected String root = "src/Tilesets/";
	protected int resolution;

	private final Hashtable<String, Dimension> textures = new Hashtable<String, Dimension>();
	private final Hashtable<String, TilesetGroup> tilesets = new Hashtable<String, TilesetGroup>();

	public TSR() {
		PropertyReader tilesetData = new PropertyReader(new File(root + "TileSetData.pro"));
		root += tilesetData.getProperty("Tileset") + "/";
		resolution = tilesetData.getIntProperty("Resolution");

		loadTextureData();
		loadGroupDefinitions();

	}

	protected void renderTiles(Collection<T> tiles) {
		if (tiles == null)
			return;
		Hashtable<String, LinkedList<T>> batches = new Hashtable<String, LinkedList<T>>();

		for (T tile : tiles) {
			TilesetGroup group = tilesets.get(tile.group);
			if (group != null) {
				if (!batches.containsKey(group.texture))
					batches.put(group.texture, new LinkedList<T>());
				batches.get(group.texture).add(tile);
			}
			else
				Debug.log("Error rendering tile. Group " + tile.group + " not found.");
		}

		for (String texture : batches.keySet())
			renderBatch(batches.get(texture), texture);
	}

	private void renderBatch(Collection<T> batch, String texture) {
		float[] f = new float[batch.size() * 16];
		int i = 0;
		for (T tile : batch) {
			TilesetGroup group = groupData(tile.group);

			fillBuffer(URect.vrect().translate(tile.x, tile.y).scale(resolution, resolution), f, 16 * i);

			int texX = (tile.texID + group.offset) % textureDimensions(group.texture).width;
			int texY = (tile.texID + group.offset) / textureDimensions(group.texture).width;
			float texWidth = 1.0f / textureDimensions(group.texture).width;
			float texHeight = 1.0f / textureDimensions(group.texture).height;
			fillBuffer(URect.trect().translate(texX, texY).scale(texWidth, texHeight), f, 16 * i + 2);
			i++;
		}
		// TextureManager.useTexture(texture, GL11.GL_TEXTURE_2D, GL11.GL_NEAREST);
		// VBOManager.createDynamicVBO(texture, GL11.GL_QUADS, VBOManager.V2T2);
		VBOManager.dynamicDraw(texture, f);

	}

	private void fillBuffer(URect rect, float[] buffer, int offset) {
		buffer[offset] = rect.x1;
		buffer[offset + 1] = rect.y1;
		buffer[offset + 4] = rect.x1;
		buffer[offset + 5] = rect.y2;
		buffer[offset + 8] = rect.x2;
		buffer[offset + 9] = rect.y2;
		buffer[offset + 12] = rect.x2;
		buffer[offset + 13] = rect.y1;
	}

	private TilesetGroup groupData(String tileset) {
		return tilesets.get(tileset);
	}

	private Dimension textureDimensions(String texture) {
		return textures.get(texture);
	}

	private void loadTextureData() {
		File textureDef = new File(root + "TextureDef.pro");
		if (!textureDef.exists()) {
			Debug.log("TextureDef file missing from " + root);
			return;
		}
		PropertyReader data = new PropertyReader(textureDef);
		Collection<String> properties = data.propertyNames();
		for (String property : properties) {
			Dimension d = data.getDimensionProperty(property);
			if (d != null) {
				File f = new File(root + property + ".png");
				if (f.exists())
					textures.put(property, d);
				else {
					Debug.log("Property File Error: TextureDef");
					Debug.log("    " + property + ".png does not exist.");
				}
			}
		}
	}

	private void loadGroupDefinitions() {
		File groupDef = new File(root + "GroupDef.pro");
		if (!groupDef.exists()) {
			Debug.log("TextureDef file missing from " + root);
			return;
		}
		PropertyReader data = new PropertyReader(groupDef);
		Collection<String> properties = data.propertyNames();
		for (String property : properties) {
			String offset = property + "Offset";
			if (properties.contains(offset)) {
				String texture = data.getProperty(property);
				if (textures.containsKey(texture))
					tilesets.put(property, new TilesetGroup(texture, data.getIntProperty(offset)));
				else {
					Debug.log("Property File Error: GroupDef");
					Debug.log("    " + texture + " texture not defined in TextureDef.");
				}
			}
		}
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateContents() {
		// TODO Auto-generated method stub

	}

}
