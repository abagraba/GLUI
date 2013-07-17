package IO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import Util.ByteUtil;
import Util.FileUtil;

import GLcomponent.Texture;
import GLui.Debug;

public class PNGtoTEX {

	public static void convert(File png, File txt, File tex) {
		if (!FileUtil.isType(png, "png")) {
			Debug.log("PNG to TEX conversion failed. " + png + " is not PNG.");
			return;
		}
		if (!FileUtil.isType(txt, "txt")) {
			Debug.log("PNG to TEX conversion failed. " + txt + " is not TXT.");
			return;
		}
		LinkedList<PNGChunk> chunks = PNGReader.readPNG(png);
		PNGWriter w = new PNGWriter(tex);
		PNGChunk ptex = new PNGChunk("ptEx", getTexData(txt));
		for (PNGChunk chunk : chunks) {
			if (chunk.name.equals("ptEx"))
				;
			if (chunk.name.equals("IEND")) {
				w.writeChunk(ptex);
				w.writeChunk(chunk);
			}
			else
				w.writeChunk(chunk);
		}
	}

	private static byte[] getTexData(File texData) {
		boolean dimensionChanged = false;
		boolean typeChanged = false;
		int w = 1, h = 1;
		byte typeData = Texture.TEXTURE_2D | Texture.NEAREST;
		LinkedList<Tokenizer.Token> tokens = Tokenizer.tokenize(texData, ":");
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		try {
			for (Tokenizer.Token token : tokens)
				if (token.key.equalsIgnoreCase("Width")) {
					w = Integer.parseInt(token.value);
					dimensionChanged = true;
				}
				else if (token.key.equalsIgnoreCase("Height")) {
					h = Integer.parseInt(token.value);
					dimensionChanged = true;
				}
				else if (token.key.equalsIgnoreCase("Type")) {
					typeData = (byte) (typeData & Texture.filterMask | Texture.getType(token.value));
					typeChanged = true;
				}
				else if (token.key.equalsIgnoreCase("Filter")) {
					typeData = (byte) (typeData & Texture.typeMask | Texture.getFilter(token.value));
					typeChanged = true;
				}
				else {
					if (dimensionChanged) {
						b.write(ByteUtil.toBytes(-2));
						b.write(ByteUtil.toBytes(w));
						b.write(ByteUtil.toBytes(h));
						dimensionChanged = false;
					}
					if (typeChanged) {
						b.write(ByteUtil.toBytes(-3));
						b.write(typeData);
					}
					b.write(ByteUtil.toBytes(token.key.length()));
					b.write(ByteUtil.toBytes(token.key));
					b.write(ByteUtil.toBytes(Integer.parseInt(token.value)));
				}
			b.write(ByteUtil.toBytes(-1));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return b.toByteArray();
	}

	public static void main(String[] args) {
		String root = "../DND/res/Texture/";
		File f = new File(root);
		LinkedList<File> pngs = FileUtil.getFilesOfType(f, "png");
		for (File png : pngs) {
			String path = png.getPath();
			Debug.log("PNG found: " + path);
			File txt = new File(path.substring(0, path.lastIndexOf('.')) + ".txt");
			if (txt.exists()) {
				Debug.log("  TXT found: " + txt.getPath());
				File tex = new File(path.substring(0, path.lastIndexOf('.')) + ".tex");
				if (!tex.exists())
					try {
						tex.createNewFile();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				Debug.log("    Outputting TEX file: " + tex.getPath());
				convert(png, txt, tex);
			}
		}
	}
}
