package GLUIUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import GLUI.Debug;

public class PNGtoFNT {

	public static void convert(File png, File txt, File fnt) {
		if (!FileUtil.isType(png, "png")) {
			Debug.log("PNG to TEX conversion failed. " + png + " is not PNG.");
			return;
		}
		if (!FileUtil.isType(txt, "txt")) {
			Debug.log("PNG to TEX conversion failed. " + txt + " is not TXT.");
			return;
		}
		LinkedList<PNGChunk> chunks = PNGReader.readPNG(png);
		PNGWriter w = new PNGWriter(fnt);
		PNGChunk font = new PNGChunk("foNt", getFntData(txt));
		for (PNGChunk chunk : chunks) {
			if (chunk.name.equals("foNt"))
				;
			if (chunk.name.equals("IEND")) {
				w.writeChunk(font);
				w.writeChunk(chunk);
			}
			else
				w.writeChunk(chunk);
		}
	}

	private static byte[] getFntData(File fntData) {
		int tw = 1, th = 1;
		int w = 1, h = 1;
		LinkedList<Tokenizer.Token> tokens = Tokenizer.tokenize(fntData, ":");
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		try {
			for (Tokenizer.Token token : tokens)
				if (token.key.equalsIgnoreCase("Width"))
					w = Integer.parseInt(token.value);
				else if (token.key.equalsIgnoreCase("Height"))
					h = Integer.parseInt(token.value);
				else if (token.key.equalsIgnoreCase("TileWidth"))
					tw = Integer.parseInt(token.value);
				else if (token.key.equalsIgnoreCase("TileHeight"))
					th = Integer.parseInt(token.value);
				else if (token.key.length() == 1) {
					b.write((byte) token.key.charAt(0));
					b.write(ByteUtil.toBytes(Integer.parseInt(token.value)));
				}
				else if (token.key.length() == 0) {
					b.write((byte) ' ');
					b.write(ByteUtil.toBytes(Integer.parseInt(token.value)));
				}
			b.write(-1);
			b.write(ByteUtil.toBytes(tw));
			b.write(ByteUtil.toBytes(th));
			b.write(ByteUtil.toBytes(w));
			b.write(ByteUtil.toBytes(h));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return b.toByteArray();
	}

	public static void main(String[] args) {
		String root = "../DND/res/Font/";
		File f = new File(root);
		LinkedList<File> pngs = FileUtil.getFilesOfType(f, "png");
		for (File png : pngs) {
			String path = png.getPath();
			Debug.log("PNG found: " + path);
			File txt = new File(path.substring(0, path.lastIndexOf('.')) + ".txt");
			if (txt.exists()) {
				Debug.log("  TXT found: " + txt.getPath());
				File fnt = new File(path.substring(0, path.lastIndexOf('.')) + ".fnt");
				if (!fnt.exists())
					try {
						fnt.createNewFile();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				Debug.log("    Outputting FNT file: " + fnt.getPath());
				convert(png, txt, fnt);
			}
		}
	}
}
