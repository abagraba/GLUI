package Renderer;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Hashtable;

import GLUICore.Debug;

public class PropertyReader {

	public Hashtable<String, String> defaults;
	public Hashtable<String, String> properties = new Hashtable<String, String>();
	private final File file;

	public PropertyReader(File file) {
		this.file = file;
		update();
	}

	public PropertyReader(File file, Hashtable<String, String> defaults) {
		this.file = file;
		this.defaults = defaults;
		update();
	}

	public String getProperty(String property) {
		if (properties.containsKey(property))
			return properties.get(property);
		logError("    Property does not exist.");
		return getDefaultStringProperty(property);
	}

	private String getDefaultStringProperty(String property) {
		if (defaults != null && defaults.containsKey(property))
			return defaults.get(property);
		return null;
	}

	public int getIntProperty(String property) {
		try {
			if (properties.containsKey(property))
				return Integer.parseInt(properties.get(property));
			logError("    Property does not exist.");
			return getDefaultIntProperty(property);
		} catch (NumberFormatException nfe) {
			logError("    Property is not int: " + property + ".");
			return getDefaultIntProperty(property);
		}
	}

	private int getDefaultIntProperty(String property) {
		try {
			if (defaults != null && defaults.containsKey(property))
				return Integer.parseInt(defaults.get(property));
		} catch (NumberFormatException nfe) {
			return -1;
		}
		return -1;
	}

	public float getFloatProperty(String property) {
		try {
			if (properties.containsKey(property))
				return Float.parseFloat(properties.get(property));
			logError("    Property does not exist.");
			return getDefaultFloatProperty(property);
		} catch (NumberFormatException nfe) {
			logError("    Property is not float: " + property + ".");
			return getDefaultFloatProperty(property);
		}
	}

	private float getDefaultFloatProperty(String property) {
		try {
			if (defaults != null && defaults.containsKey(property))
				return Float.parseFloat(defaults.get(property));
		} catch (NumberFormatException nfe) {
			return -1;
		}
		return -1;
	}

	public Dimension getDimensionProperty(String property) {
		try {
			if (properties.containsKey(property)) {
				String[] dim = properties.get(property).split("(( |\t)*)x(( |\t)*)");
				if (dim.length != 2)
					throw new NumberFormatException();
				return new Dimension(Integer.parseInt(dim[0]), Integer.parseInt(dim[1]));
			}
			logError("    Property does not exist.");
			return getDefaultDimensionProperty(property);
		} catch (NumberFormatException nfe) {
			logError("    Property is not a Dimension (int x int): " + property + ".");
			return getDefaultDimensionProperty(property);
		}
	}

	private Dimension getDefaultDimensionProperty(String property) {
		try {
			if (defaults != null && defaults.containsKey(property)) {
				String[] dim = defaults.get(property).split("(( |\t)*)x(( |\t)*)");
				if (dim.length != 2)
					throw new NumberFormatException();
				return new Dimension(Integer.parseInt(dim[0]), Integer.parseInt(dim[1]));
			}
		} catch (NumberFormatException nfe) {
			return null;
		}
		return null;
	}

	public Collection<String> propertyNames() {
		return properties.keySet();
	}

	public void update() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			Debug.log("Property file not found: " + file.getPath() + ".");
		}
		String line;
		int lineNum = 1;
		try {
			while ((line = br.readLine()) != null) {
				if (!line.equals("") && line.charAt(0) != '!') {
					int open = line.indexOf('[');
					int close = line.lastIndexOf(']');
					if (open != -1 && close != -1) {
						String[] data = line.substring(open + 1, close).split("(( |\t)*):(( |\t)*)", 2);
						if (data.length < 2)
							throw new IOException();
						properties.put(data[0], data[1]);
					}
				}
				lineNum++;
			}
		} catch (IOException e) {
			Debug.log("Property file read error:" + file.getPath());
			Debug.log("    Line number: " + lineNum + ".");
		}
	}

	private void logError(String s) {

		Debug.log("Property format error:" + file.getPath());
		Debug.log(s);
		if (defaults != null)
			Debug.log("      Attempting to use default value.");
		Debug.log("");
	}

}
