package Renderer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class ITMReader {


	public static float[] readData(String filename) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line;
		LinkedList<Float> dat = new LinkedList<Float>();
		try {
			while ((line = br.readLine()) != null) {
				String[] data = line.split("( |\t)+");
				for (int i = 0; i < data.length; i++)
					dat.add(Float.valueOf(data[i]));

			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		float[] res = new float[dat.size()];
		int i = 0;
		for (Float f : dat)
			res[i++] = f;
		return res;
	}


}
