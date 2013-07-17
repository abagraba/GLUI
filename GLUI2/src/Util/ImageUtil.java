package Util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtil {

	public static void toGreyScale(String file) {
		File in = new File(file);
		BufferedImage bi = getImage(in);
		File out = new File(file.substring(0, file.length() - 4) + "Grey.png");
		BufferedImage grey = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		try {
			ImageIO.write(grey, "png", out);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static BufferedImage getImage(File f) {
		try {
			return ImageIO.read(f);
		}
		catch (IOException e) {
			return null;
		}
	}

}
