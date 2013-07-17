package Util;

import java.io.File;
import java.util.LinkedList;

/**
 * A Utility class with useful File accessing methods.
 */
public final class FileUtil {

	private FileUtil() {

	}

	/**
	 * Gets a LinkedList of Files in the specified directory and its sub-directories with the specified extension.
	 * @param directory The root directory to start searching from.
	 * @param type The file extension.
	 * @return A list of all files in the specified directory and its sub-directories with the specified extension.
	 */
	public static LinkedList<File> getFilesOfType(File directory, String type) {
		LinkedList<File> files = new LinkedList<File>();
		if (!directory.isDirectory())
			return files;
		File[] fileList = directory.listFiles();
		for (File file : fileList)
			if (file.isDirectory())
				files.addAll(getFilesOfType(file, type));
			else if (isType(file, type))
				files.add(file);
		return files;
	}

	/**
	 * Checks whether a file has a certain extension.
	 * @param file File to be checked.
	 * @param type Extension to be checked.
	 * @return True if the file extension matches. False if the extension doesn't match or the the file doesn't exist.
	 */
	public static boolean isType(File file, String type) {
		if (!file.exists())
			return false;
		return file.getName().matches(".+\\." + type);
	}
}
