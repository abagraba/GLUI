

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LineCount {

	public static final File f = new File("src");

	public static void main(String[] args) {
		File[] files = f.listFiles();
		for (File file : files)
			if (!file.isFile())
				countLines(file.listFiles());
	}

	private static void countLines(File[] files) {
		if (files.length > 0) {
			int totalCount = 0;
			int totalAdjust = 0;
			for (File file : files) {
				String path = file.getName();
				String line;
				int count = 0;
				int adjustedCount = 0;

				if (path.substring(path.lastIndexOf(".") + 1).equals("java")) {
					BufferedReader br;
					try {
						br = new BufferedReader(new FileReader(file));
						while ((line = br.readLine()) != null) {
							count++;
							if (!line.equals("") && !line.contains("//"))
								adjustedCount++;
						}
					}
					catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
				totalCount += count;
				totalAdjust += adjustedCount;
				System.out.println(file + " line count: " + count + " : " + adjustedCount);
			}
			System.out.println("       " + files[0].getParent() + " folder count: " + totalCount + " : " + totalAdjust);
		}
	}

}
