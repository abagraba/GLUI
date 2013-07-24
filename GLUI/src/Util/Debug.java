package Util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

/**
 * Writes debugging information to the command line or a file as applicable.
 */
public class Debug {

	/**
	 * Constant used by {@link Debug#logTo(int)}. Resulting logs are printed to file specified by logDest().
	 */
	public static final int PRINT_TO_LOG = -1;
	/**
	 * Constant used by {@link Debug#logTo(int)}. Resulting logs are printed to console.
	 */
	public static final int PRINT_TO_CONSOLE = -2;

	public static final byte VBO_MANAGER = 0;
	public static final byte TEXTURE_MANAGER = 1;
	public static final byte SHADER_MANAGER = 2;
	public static final byte RESOURCE_MANAGER = 3;
	public static final byte INSTANCE_MANAGEMENT = 4;
	public static final byte FBO = 5;

	public static final boolean[] verbosity = new boolean[128];

	private static int dest = PRINT_TO_CONSOLE;
	private static File output = new File("src/Debug/log.txt");
	private static BufferedWriter writer = null;

	static {
		for (int i = 0; i < verbosity.length; i++)
			verbosity[i] = true;
	}

	/**
	 * Sets where the error logs are printed to.
	 * @param dest Destination of logged errors. Takes constants {@link Debug#PRINT_TO_LOG} and
	 *        {@link Debug#PRINT_TO_CONSOLE}. Invalid arguments default to PRINT_TO_LOG.
	 */
	public static void logTo(int dest) {
		switch (dest) {
			case PRINT_TO_LOG:
			case PRINT_TO_CONSOLE:
				Debug.dest = dest;
				break;
			default:
				Debug.dest = PRINT_TO_LOG;
				break;
		}
	}

	public static void logDest(File output) {
		Debug.output = output;
	}

	/**
	 * Writes the error to the log.
	 * @param errors Strings to be written to the log.
	 */
	public static void log(String... errors) {
		String e = null;
		if (errors.length == 0)
			newLine();
		else if (errors.length == 1)
			e = errors[0];
		else {
			StringBuilder sb = new StringBuilder();
			for (String error : errors)
				sb.append(error);
			e = sb.toString();
		}
		if (dest == PRINT_TO_CONSOLE)
			System.out.println(e);
		else
			try {
				if (writer == null) {
					output.createNewFile();
					writer = new BufferedWriter(new FileWriter(output));
				}
				writer.write(e);
				writer.newLine();
				writer.flush();
			}
			catch (IOException ioe) {
				ioe.printStackTrace();
			}
	}

	/**
	 * Writes a new line to the log.
	 */
	public static void newLine() {
		if (dest == PRINT_TO_CONSOLE)
			System.out.println();
		else
			try {
				writer.newLine();
				writer.flush();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
	}

	public static boolean glError() {
		int i = GL11.glGetError();
		if (i == GL11.GL_NO_ERROR)
			return false;
		Debug.log(GLU.gluErrorString(i));
		return true;
	}

	public static void log(byte verboseness, String... error) {
		if (Debug.verbosity[verboseness])
			log(error);
	}

	public static void newLine(byte verboseness) {
		if (Debug.verbosity[verboseness])
			newLine();
	}

	public static boolean glError(byte verboseness) {
		int i = GL11.glGetError();
		if (i == GL11.GL_NO_ERROR)
			return false;
		if (Debug.verbosity[verboseness])
			Debug.log(verboseness, GLU.gluErrorString(i));
		return true;
	}
}
