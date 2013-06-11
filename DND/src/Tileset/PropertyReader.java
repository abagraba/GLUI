package Tileset;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Hashtable;

import GLUI.Debug;

/**
 * Creates a HashTable of properties defined in the supplied .pro file.
 */
public class PropertyReader {

	private Hashtable<String, String> defaults;
	private final Hashtable<String, String> properties = new Hashtable<String, String>();
	private final File file;

	/**
	 * Initializes with data from the supplied file.
	 * @param file a .pro file containing property information.
	 */
	public PropertyReader(File file) {
		this.file = file;
		update();
	}

	/**
	 * Initializes with data from the supplied file. If file does not exist or file does not contain properties,
	 * properties are taken from the file containing default properties.
	 * @param file a .pro file containing property information.
	 * @param defaults file containing default properties.
	 */
	public PropertyReader(File file, File defaults) {
		PropertyReader def = new PropertyReader(defaults);
		this.file = file;
		this.defaults = def.properties;
		update();
	}

	/**
	 * Initializes with data from the supplied file. If file does not exist or file does not contain properties,
	 * properties are taken from the default properties.
	 * @param file a .pro file containing property information.
	 * @param defaults default properties.
	 */
	public PropertyReader(File file, Hashtable<String, String> defaults) {
		this.file = file;
		this.defaults = defaults;
		update();
	}

	/**
	 * Returns the property as a String. If it does not exist, the default property is used.
	 * @param key a String representing the name of the property to be retrieved.
	 * @return the property.
	 */
	public String getProperty(String key) {
		if (properties.containsKey(key))
			return properties.get(key);
		logError("    Property does not exist.");
		return getDefaultStringProperty(key);
	}

	private String getDefaultStringProperty(String key) {
		if (defaults != null && defaults.containsKey(key))
			return defaults.get(key);
		return null;
	}

	/**
	 * Returns the property as an int. If it does not exist or is invalid, the default property is used.
	 * @param key a String representing the name of the property to be retrieved.
	 * @return the property.
	 */
	public int getIntProperty(String key) {
		try {
			if (properties.containsKey(key))
				return Integer.parseInt(properties.get(key));
			logError("    Property does not exist.");
			return getDefaultIntProperty(key);
		}
		catch (NumberFormatException nfe) {
			logError("    Property is not int: " + key + ".");
			return getDefaultIntProperty(key);
		}
	}

	private int getDefaultIntProperty(String key) {
		try {
			if (defaults != null && defaults.containsKey(key))
				return Integer.parseInt(defaults.get(key));
		}
		catch (NumberFormatException nfe) {
			return -1;
		}
		return -1;
	}

	/**
	 * Returns the property as an float. If it does not exist or is invalid, the default property is used.
	 * @param key a String representing the name of the property to be retrieved.
	 * @return the property.
	 */
	public float getFloatProperty(String key) {
		try {
			if (properties.containsKey(key))
				return Float.parseFloat(properties.get(key));
			logError("    Property does not exist.");
			return getDefaultFloatProperty(key);
		}
		catch (NumberFormatException nfe) {
			logError("    Property is not float: " + key + ".");
			return getDefaultFloatProperty(key);
		}
	}

	private float getDefaultFloatProperty(String property) {
		try {
			if (defaults != null && defaults.containsKey(property))
				return Float.parseFloat(defaults.get(property));
		}
		catch (NumberFormatException nfe) {
			return -1;
		}
		return -1;
	}

	/**
	 * Returns the property as an Dimension. If it does not exist or is invalid, the default property is used.
	 * @param key a String representing the name of the property to be retrieved.
	 * @return the property. Defined as ixi, where i is an integer.
	 */

	public Dimension getDimensionProperty(String key) {
		try {
			if (properties.containsKey(key)) {
				String[] dim = properties.get(key).split("(\\s*)x(\\s*)");
				if (dim.length != 2)
					throw new NumberFormatException();
				return new Dimension(Integer.parseInt(dim[0]), Integer.parseInt(dim[1]));
			}
			logError("    Property does not exist.");
			return getDefaultDimensionProperty(key);
		}
		catch (NumberFormatException nfe) {
			logError("    Property is not a Dimension (int x int): " + key + ".");
			return getDefaultDimensionProperty(key);
		}
	}

	private Dimension getDefaultDimensionProperty(String key) {
		try {
			if (defaults != null && defaults.containsKey(key)) {
				String[] dim = defaults.get(key).split("(\\s*)x(\\s*)");
				if (dim.length != 2)
					throw new NumberFormatException();
				return new Dimension(Integer.parseInt(dim[0]), Integer.parseInt(dim[1]));
			}
		}
		catch (NumberFormatException nfe) {
			return null;
		}
		return null;
	}

	/**
	 * Gets a list of all property keys.
	 * @return Collection of property keys.
	 */
	public Collection<String> propertyNames() {
		return properties.keySet();
	}

	/**
	 * Reads the file for property values and populates the property HashTable.
	 */
	public void update() {
		FileReader fr = null;
		try {
			fr = new FileReader(file);
		}
		catch (FileNotFoundException fnfe) {
			Debug.log("Property file not found: " + file.getPath() + ".");
		}
		BufferedReader br = new BufferedReader(fr);
		String line;
		int lineNum = 1;
		try {
			while ((line = br.readLine()) != null) {
				if (!line.equals("") && line.charAt(0) != '!') {
					int open = line.indexOf('[');
					int close = line.lastIndexOf(']');
					if (open != -1 && close != -1) {
						String[] data = line.substring(open + 1, close).split("(\\s*):(\\s*)", 2);
						if (data.length < 2)
							throw new IOException();
						properties.put(data[0], data[1]);
					}
				}
				lineNum++;
			}
			br.close();
		}
		catch (IOException e) {
			Debug.log("Property file read error:" + file.getPath());
			Debug.log("    Line number: " + lineNum + ".");
		}
	}

	private void logError(String s) {
		Debug.log("Property format error:" + file.getPath());
		Debug.log(s);
		if (defaults != null)
			Debug.log("      Attempting to use default value.");
		Debug.newLine();
	}

}
