package org.igs.android.ogl.engine;

public class AndromedaException extends Exception {

	private static final long serialVersionUID = 8996814688866239655L;

	private int id;
	
	public static final int NODE_NOT_FOUND_EXCEPTION = 1;
	public static final int TEXTURE_NOT_FOUND_EXCEPTION = 2;
	
	public static final int SCENE_NOT_FOUND_EXCEPTION = 3;
	public static final int LIGHT_NOT_FOUND_EXCEPTION = 4;
	public static final int PROPERTY_NOT_FOUND_EXCEPTION = 5;
	public static final int ASSET_BITMAP_LOAD_EXCEPTION = 6;
	
	public static final int TEXTURE_CREATE_EXCEPTION = 7;
	public static final int MODEL_CREATE_EXCEPTION = 8;
	
	public static final int LEVEL_LOAD_EXCEPTION = 9;
	public static final int LEVEL_DESCRIPTOR_EXCEPTION = 9;
	
	public AndromedaException(int id) {
		super();
		this.id = id;
	}

	public AndromedaException(int id, String message) {
		super(message);
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
}
