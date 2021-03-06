package IO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tokenizer takes a text file and splits each line into a Token. Each Token is represented by two Strings separated by
 * a given delimiter. Whitespace around the delimiter is ignored. Empty lines and lines beginning with "//" or "!" are
 * ignored, as are invalid lines.
 */
public class Tokenizer {

	/**
	 * Tokenizes the given file using the delimiter.
	 * @param file File to be tokenized.
	 * @param delimiter Regex specifying where to split the token.
	 * @return LinkedList of Tokens in the file.
	 */
	public static LinkedList<Token> tokenize(File file, String delimiter) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		LinkedList<Token> tokens = new LinkedList<Token>();
		String line;
		try {
			while ((line = br.readLine()) != null) {
				Token t = tokenize(line, delimiter);
				if (t != null)
					tokens.add(t);
			}
			br.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return tokens;
	}

	private static Token tokenize(String line, String delimiter) {
		if (line.equals(""))
			return null;
		if (line.matches("\\s*//.*"))
			return null;
		return getToken(line, delimiter);
	}

	private static Token getToken(String line, String delimiter) {
		Pattern p = Pattern.compile("\\s*(.+?)\\s*" + delimiter + "\\s*(.+)\\s*");
		Matcher m = p.matcher(line);
		if (m.find())
			return new Token(m.group(1), m.group(2));
		return null;
	}

	/**
	 * A Token consists of a pair of Strings: a key and a value. The Token is created by the Tokenizer from a provided
	 * String and a delimiter. For example: Given the String "foo: bar:bar" and delimiter ":", the String is Tokenized
	 * into "foo" and "bar:bar".
	 */
	public static class Token {
		public final String key;
		public final String value;

		/**
		 * Creates a token using the provided key and value.
		 * @param key The key
		 * @param value The value
		 */
		public Token(String key, String value) {
			this.key = key;
			this.value = value;
		}
	}

}
