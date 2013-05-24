package GLUICore;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Debug {

	private static boolean testing = true;
	private static String output = "src/Debug/log.txt";

	private static BufferedWriter writer = null;

	public static void log(String s) {
		if (testing)
			System.out.println(s);
		else {
			try {
				if (writer == null)
					writer = new BufferedWriter(new FileWriter(output));
				writer.write(s);
				writer.newLine();
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
