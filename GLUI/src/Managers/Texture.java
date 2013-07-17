package Managers;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

import java.io.File;

import Util.Debug;
import Util.FileUtil;

public class Texture {

	public final String name;
	public final File source;
	private final byte typeData;

	public static final byte INVALID_VALUE = -1;

	private static final byte targetMask = 0x7;
	private static final byte filterMask = 0x38;
	private static final byte mipmapMask = 0x10;

	public static final byte TEXTURE_CUBE_MAP = 0x0;
	public static final byte TEXTURE_1D = 0x1;
	public static final byte TEXTURE_2D = 0x2;
	public static final byte TEXTURE_3D = 0x3;
	public static final byte TEXTURE_CUBE_MAP_ARRAY = 0x4;
	public static final byte TEXTURE_1D_ARRAY = 0x5;
	public static final byte TEXTURE_2D_ARRAY = 0x6;

	public static final byte NEAREST = 0x0;
	public static final byte LINEAR = 0x8;
	public static final byte NEAREST_NEAREST = 0x10;
	public static final byte NEAREST_LINEAR = 0x30;
	public static final byte LINEAR_NEAREST = 0x18;
	public static final byte LINEAR_LINEAR = 0x38;

	/**
	 * Creates a new Texture Data Object.
	 * @param name Name of this Texture object.
	 * @param source Image source of the texture object. Multiple Texture objects can share the same source.
	 * @param target byte representing Texture target. Valid values are {@link Texture#TEXTURE_1D},
	 *        {@link Texture#TEXTURE_2D}, {@link Texture#TEXTURE_3D}, {@link Texture#TEXTURE_1D_ARRAY},
	 *        {@link Texture#TEXTURE_2D_ARRAY}, {@link Texture#TEXTURE_CUBE_MAP}, {@link Texture#TEXTURE_CUBE_MAP_ARRAY}
	 *        . Only bits 6-8 may be set. Defaults to {@link Texture#TEXTURE_2D}.
	 * @param filter byte representing Texture filter. Valid values are {@link Texture#NEAREST}, {@link Texture#LINEAR},
	 *        {@link Texture#NEAREST_NEAREST}, {@link Texture#NEAREST_LINEAR}, {@link Texture#LINEAR_NEAREST},
	 *        {@link Texture#LINEAR_LINEAR}. Only bits 3-5 may be set. Defaults to {@link Texture#NEAREST}.
	 * @return Texture object containing the Texture defined. Returns null if source in invalid.
	 */
	public static Texture createTexture(String name, File source, byte target, byte filter) {
		if (!FileUtil.isType(source, "png"))
			return null;
		if (!isValidCode(target, targetMask)) {
			target = TEXTURE_2D;
			Debug.log(Debug.TEXTURE_MANAGER, name, ": Invalid target. Defaulting to TEXTURE_2D.");
		}
		if (!isValidCode(filter, filterMask)) {
			filter = NEAREST;
			Debug.log(Debug.TEXTURE_MANAGER, name, ": Invalid filter. Defaulting to NEAREST.");
		}
		Texture texture = new Texture(name, source, (byte) (target | filter));
		TextureManager.addNewTexture(texture);
		return texture;
	}

	/**
	 * Creates a new Texture Data Object.
	 * @param name Name of this Texture object.
	 * @param source Image source of the texture object. Multiple Texture objects can share the same source.
	 * @param typeData Contains data on texture type and filter as defined in
	 *        {@link Texture#createTexture(String, File, byte, byte)}.
	 * @return Texture object containing the Texture defined. Returns null if source in invalid.
	 */
	public static Texture createTexture(String name, File source, byte typeData) {
		if (!FileUtil.isType(source, "png"))
			return null;
		if (!isValidCode(typeData, (byte) (targetMask | filterMask))) {
			typeData = TEXTURE_2D | NEAREST;
			Debug.log(Debug.TEXTURE_MANAGER, name + ": Invalid type data. Defaulting to TEXTURE_2D and NEAREST.");
		}
		Texture texture = new Texture(name, source, typeData);
		TextureManager.addNewTexture(texture);
		return texture;
	}

	private Texture(String name, File source, byte typeData) {
		this.name = name;
		this.source = source;
		this.typeData = typeData;
	}

	/**
	 * Returns whether the filtering mode uses mipmaps.
	 * @return whether or not the texture uses mipmaps.
	 */
	public boolean usingMipMaps() {
		return (typeData & mipmapMask) == mipmapMask;
	}

	/**
	 * Gets the LWJGL constant for the texture target.
	 * @return LWJGL constant for the texture target. INVALID_VALUE if invalid.
	 */
	public int getGLTarget() {
		switch (typeData & targetMask) {
			case TEXTURE_1D:
				return GL_TEXTURE_1D;
			case TEXTURE_2D:
				return GL_TEXTURE_2D;
			case TEXTURE_3D:
				return GL_TEXTURE_3D;
			default:
				Debug.log(Debug.TEXTURE_MANAGER, "Invalid Texture Type: " + (typeData & targetMask));
				return GL_INVALID_VALUE;
		}
	}

	/**
	 * Gets the LWJGL constant for the texture filter.
	 * @return LWJGL constant for the texture filter. INVALID_VALUE if invalid.
	 */
	public int getGLFilter() {
		switch (typeData & filterMask) {
			case NEAREST:
				return GL_NEAREST;
			case LINEAR:
				return GL_LINEAR;
			case NEAREST_NEAREST:
				return GL_NEAREST_MIPMAP_NEAREST;
			case NEAREST_LINEAR:
				return GL_NEAREST_MIPMAP_LINEAR;
			case LINEAR_NEAREST:
				return GL_LINEAR_MIPMAP_NEAREST;
			case LINEAR_LINEAR:
				return GL_LINEAR_MIPMAP_LINEAR;
			default:
				Debug.log(Debug.TEXTURE_MANAGER, "Invalid Texture Filter: " + (typeData & filterMask));
				return GL_INVALID_VALUE;
		}
	}

	/**
	 * Returns the byte representing the texture target specified by a String.
	 * @param value String representing texture target.
	 * @return the byte representing the specified target. TEXTURE_2D if invalid.
	 */
	public static byte getTarget(String value) {
		if (value.equalsIgnoreCase("1D"))
			return TEXTURE_1D;
		if (value.equalsIgnoreCase("2D"))
			return TEXTURE_2D;
		if (value.equalsIgnoreCase("3D"))
			return TEXTURE_3D;
		if (value.equalsIgnoreCase("1DARRAY"))
			return TEXTURE_1D_ARRAY;
		if (value.equalsIgnoreCase("2DARRAY"))
			return TEXTURE_2D_ARRAY;
		if (value.equalsIgnoreCase("CUBE"))
			return TEXTURE_CUBE_MAP;
		if (value.equalsIgnoreCase("CUBEARRAY"))
			return TEXTURE_CUBE_MAP_ARRAY;
		return TEXTURE_2D;
	}

	/**
	 * Returns the byte representing the texture filter specified by a String.
	 * @param value String representing texture filter.
	 * @return the byte representing the specified filter mode. NEAREST if invalid.
	 */
	public static byte getFilter(String value) {
		if (value.equalsIgnoreCase("NEAREST"))
			return NEAREST;
		if (value.equalsIgnoreCase("LINEAR"))
			return LINEAR;
		if (value.equalsIgnoreCase("NEAREST NEAREST"))
			return NEAREST_NEAREST;
		if (value.equalsIgnoreCase("NEAREST LINEAR"))
			return NEAREST_LINEAR;
		if (value.equalsIgnoreCase("LINEAR NEAREST"))
			return LINEAR_NEAREST;
		if (value.equalsIgnoreCase("LINEAR LINEAR"))
			return LINEAR_LINEAR;
		return NEAREST;
	}

	/**
	 * Ensures that the code specified only has bits set that are specified by the mask.
	 * @param code code to be checked.
	 * @param mask mask specifying the valid bits that may be set.
	 * @return whether or not the supplied code is valid.
	 */
	private static boolean isValidCode(byte code, byte mask) {
		return (code & ~mask) == 0;
	}

}
