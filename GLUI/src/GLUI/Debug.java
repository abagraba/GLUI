package GLui;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;

/**
 * Writes debugging information to the command line or a file as applicable.
 */
public class Debug {

	/**
	 * Constant used by {@link Debug#logTo(int)}. Resulting logs are printed to file specified by logDest().
	 */
	public static final int PRINT_TO_LOG = 0;
	/**
	 * Constant used by {@link Debug#logTo(int)}. Resulting logs are printed to console.
	 */
	public static final int PRINT_TO_CONSOLE = 1;

	public static boolean[] profile = new boolean[10];
	public static int[] profileOffset = new int[10];

	public static int verboseRenderPass = 0;
	public static int verboseTexturePass = 1;
	public static int verboseBatchDataPass = 2;
	public static int verboseVBO = 3;

	public static int rendererMessage = 1;
	public static int drawMessage = 2;

	private static Stack<String> profileStack = new Stack<String>();

	private static int dest = PRINT_TO_LOG;
	private static String output = "src/Debug/log.txt";
	private static BufferedWriter writer = null;

	static {
		dest = PRINT_TO_CONSOLE;
		for (int i = 0; i < profile.length; i++)
			profile[i] = true;
		profileOffset[rendererMessage] = 1;
		profileOffset[drawMessage] = profileOffset[rendererMessage] + (profile[verboseRenderPass] ? 1 : 0)
				+ (profile[verboseTexturePass] ? 1 : 0) + (profile[verboseBatchDataPass] ? 1 : 0);

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

	public static void logDest(String output) {
		Debug.output = output;
	}

	/**
	 * Writes the error to the log.
	 * @param error String to be written to the log.
	 */
	public static void log(String error) {
		if (dest == PRINT_TO_CONSOLE)
			System.out.println(error);
		else
			try {
				if (writer == null)
					writer = new BufferedWriter(new FileWriter(output));
				writer.write(error);
				writer.newLine();
				writer.flush();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
	}

	public static void profile(String message, long nanoTime, int messageIndent, int type) {
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < messageIndent + profileOffset[type]; i++)
			res.append("  ");
		res.append(message).append(": ");
		res.append(nanoTime * 0.000001f);
		res.append(" ms");
		profileStack.push(res.toString());
	}

	public static void profile(String message, int messageIndent, int type) {
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < messageIndent + profileOffset[type]; i++)
			res.append("  ");
		res.append(message);
		profileStack.push(res.toString());
	}

	public static void logProfile() {
		while (!profileStack.isEmpty())
			Debug.log(profileStack.pop());
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
}
