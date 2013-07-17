package GLcomponent;

import static org.lwjgl.opengl.GL11.GL_INVALID_VALUE;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_NEAREST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_1D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_3D;

import java.io.File;

import GLui.Debug;

public class Texture {

	public final String name;
	public final int w;
	public final int h;
	public final String source;
	public final int offset;
	public final byte typeData;

	public static final byte typeMask = 0x7;
	public static final byte filterMask = 0x38;
	public static final byte mipmapMask = 0x10;
	public static final byte validityMask = 0x3F;

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
	 * @param source Image source of the texture object. Multiple Texture objects can share the same source. This
	 *        parameter must represent an existing path as used by {@link File}.
	 * @param w Number of tiles into which the texture is split horizontally.
	 * @param h Number of tiles into which the texture is split vertically.
	 * @param offset Integer offset to the Texture tile ID.
	 * @param type byte representing Texture target. Valid values are {@link Texture#TEXTURE_1D},
	 *        {@link Texture#TEXTURE_2D}, {@link Texture#TEXTURE_3D}, {@link Texture#TEXTURE_1D_ARRAY},
	 *        {@link Texture#TEXTURE_2D_ARRAY}, {@link Texture#TEXTURE_CUBE_MAP}, {@link Texture#TEXTURE_CUBE_MAP_ARRAY}
	 *        . Only bits 6-8 may be set. Defaults to {@link Texture#TEXTURE_2D}.
	 * @param filter byte representing Texture filter. Valid values are {@link Texture#NEAREST}, {@link Texture#LINEAR},
	 *        {@link Texture#NEAREST_NEAREST}, {@link Texture#NEAREST_LINEAR}, {@link Texture#LINEAR_NEAREST},
	 *        {@link Texture#LINEAR_LINEAR}. Only bits 3-5 may be set. Defaults to {@link Texture#NEAREST}.
	 */
	public Texture(String name, String source, int w, int h, int offset, byte type, byte filter) {
		this.name = name;
		this.w = w;
		this.h = h;
		this.source = source;
		this.offset = offset;
		if (!isValidCode(type, typeMask)) {
			type = TEXTURE_2D;
			Debug.log("Texture: " + name + ": Invalid type. Defaulting to TEXTURE_2D.");
		}
		if (!isValidCode(filter, filterMask)) {
			filter = NEAREST;
			Debug.log("Texture: " + name + ": Invalid filter. Defaulting to NEAREST.");
		}
		typeData = (byte) (type | filter);
	}

	/**
	 * Creates a new Texture Data Object.
	 * @param name Name of this Texture object.
	 * @param source Image source of the texture object. Multiple Texture objects can share the same source. This
	 *        parameter must represent an existing path as used by {@link File}.
	 * @param w Number of tiles into which the texture is split horizontally.
	 * @param h Number of tiles into which the texture is split vertically.
	 * @param offset Integer offset to the Texture tile ID.
	 * @param typeData Contains data on texture type and filter as defined in
	 *        {@link Texture#Texture(String, String, int, int, int, byte, byte)}.
	 */
	public Texture(String name, String source, int w, int h, int offset, byte typeData) {
		this.name = name;
		this.w = w;
		this.h = h;
		this.source = source;
		this.offset = offset;
		if (!isValidCode(typeData, validityMask)) {
			typeData = TEXTURE_2D | NEAREST;
			Debug.log("Texture: " + name + ": Invalid type data. Defaulting to TEXTURE_2D and NEAREST.");
		}
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
	 * Returns a {@link URect} representing the bounds of the texture rectangle bounding the ith tile.
	 * @param i tile index.
	 * @return texture coordinates of bounding rectangle.
	 */
	public URect getTile(int i) {
		return URect.trect().translate((i + offset) % w, (i + offset) / w).scale(1.0f / w, 1.0f / h);
	}

	public int getGLType() {
		switch (typeData & typeMask) {
			case TEXTURE_1D:
				return GL_TEXTURE_1D;
			case TEXTURE_2D:
				return GL_TEXTURE_2D;
			case TEXTURE_3D:
				return GL_TEXTURE_3D;
			default:
				Debug.log("Invalid Texture Type: " + (typeData & typeMask));
				return GL_INVALID_VALUE;
		}
	}

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
				Debug.log("Invalid Texture Filter: " + (typeData & filterMask));
				return GL_INVALID_VALUE;
		}
	}

	public static byte getType(String value) {
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

	private static boolean isValidCode(byte code, byte mask) {
		return (code & ~mask) == 0;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof Texture))
			return false;
		Texture t = (Texture) o;
		if (!source.equals(t.source))
			return false;
		if (typeData != t.typeData)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		return source.hashCode() & ~0x1F | typeData;
	}

}
