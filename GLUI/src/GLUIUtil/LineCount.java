package GLUIUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class LineCount {

	public static final File root = new File("../DND");

	public static void main(String[] args) {
		countDirLines(new File("../DND"));
		countDirLines(new File("."));
	}

	public static void countFileLines(File root) {
		LinkedList<File> javas = FileUtil.getFilesOfType(root, "java");
		for (File java : javas)
			System.out.println("    " + java.getPath() + " : " + countLines(java) + " lines.");
	}

	public static void countDirLines(File root) {
		File[] dirs = root.listFiles();
		for (File dir : dirs)
			if (dir.isDirectory()) {
				LinkedList<File> javas = FileUtil.getFilesOfType(dir, "java");
				int lines = 0;
				for (File java : javas)
					lines += countLines(java);
				if (lines != 0)
					System.out.println(dir.getPath() + " : " + lines + " lines.");
			}
	}

	private static int countLines(File file) {
		BufferedReader br = null;
		int count = 0;
		String line;
		try {
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null)
				if (!line.matches("\\s*") && !line.matches("\\s*//.*"))
					count++;
			br.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return count;
	}
}
