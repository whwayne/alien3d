package org.igs.android.ogl.engine.utils;

import javax.microedition.khronos.opengles.GL10;

public class Color {

	/** The fixed colour white */
	public static final Color white = new Color(1,1,1,1);
	
	/** The fixed colour yellow */
	public static final Color yellow = new Color(1,1,0,1);
	
	/** The fixed colour red */
	public static final Color red = new Color(1,0,0,1);
	
	/** The fixed colour blue */
	public static final Color blue = new Color(0,0,1,1);
	
	/** The fixed colour green */
	public static final Color green = new Color(0,1,0,1);
	
	/** The fixed colour black */
	public static final Color black = new Color(0,0,0,1);
	
	public static final Color darkGray = new Color(0.1f,0.1f,0.1f, 1);
	
	/** The red component of the colour */
	public float r;
	
	/** The green component of the colour */
	public float g;
	
	/** The blue component of the colour */
	public float b;
	
	/** The alpha component of the colour */
	public float a;
	
	/**
	 * Create a 3 component colour
	 * 
	 * @param r The red component of the colour
	 * @param g The green component of the colour
	 * @param b The blue component of the colour
	 */
	public Color(float r,float g,float b) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = 1;
	}

	/**
	 * Create a 3 component colour
	 * 
	 * @param r The red component of the colour
	 * @param g The green component of the colour
	 * @param b The blue component of the colour
	 * @param a The alpha component of the colour
	 */
	public Color(float r,float g,float b,float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public Color(String hexColor) {
		this.r = this.hexToR(hexColor) / 255.0f;
		this.g = this.hexToG(hexColor) / 255.0f;
		this.b = this.hexToB(hexColor) / 255.0f;
	}
	
	private int hexToR(String h) {
		return Integer.parseInt((cutHex(h)).substring(0,2),16);
	}
	
	private int hexToG(String h) {
		return Integer.parseInt((cutHex(h)).substring(2,4),16);
	}
	
	private int hexToB(String h) {
		return Integer.parseInt((cutHex(h)).substring(4,6),16);
	}
	
	private String cutHex(String h) {
		
		return (h.substring(0, 1).equals("#")) ? h.substring(1,7) : h;
	}
	
	/**
	 * Bind this colour to the GL context
	 */
	public void bind(GL10 gl) {
		gl.glColor4f(r,g,b,a);
	}
	
	/**
	 * Make a darker instance of this colour
	 * 
	 * @return The darkver version of this colour
	 */
	public Color darker() {
		Color temp = new Color(r * 0.8f,g * 0.8f,b * 0.8f,a);
		
		return temp;
	}
	
	public void interpolate(Color other, float percent)	{
		this.r = this.r + ((other.r - this.r) * percent);
		this.b = this.b + ((other.b - this.b) * percent);
		this.g = this.g + ((other.g - this.g) * percent);
		this.a = this.a + ((other.a - this.a) * percent);
		
		if (this.r > 1.0f)
			this.r -= 1.0f;
		if (this.g > 1.0f)
			this.g -= 1.0f;
		if (this.b > 1.0f)
			this.b -= 1.0f;
		if (this.a > 1.0f)
			this.a = 1.0f;
		if (this.r < 0.0f)
			this.r += 1.0f;
		if (this.g < 0.0f)
			this.g += 1.0f;
		if (this.b < 0.0f)
			this.b += 1.0f;
		if (this.a < 0.0f)
			this.a += 1.0f;
	}
	
	public static void interpolate(Color into, Color begin, Color other, float percent)	{
		into.r = begin.r + ((other.r - begin.r) * percent);
		into.b = begin.b + ((other.b - begin.b) * percent);
		into.g = begin.g + ((other.g - begin.g) * percent);
		into.a = begin.a + ((other.a - begin.a) * percent);
		
		if (into.r > 1.0f)
			into.r -= 1.0f;
		if (into.g > 1.0f)
			into.g -= 1.0f;
		if (into.b > 1.0f)
			into.b -= 1.0f;
		if (into.a > 1.0f)
			into.a = 1.0f;
		if (into.r < 0.0f)
			into.r += 1.0f;
		if (into.g < 0.0f)
			into.g += 1.0f;
		if (into.b < 0.0f)
			into.b += 1.0f;
		if (into.a < 0.0f)
			into.a += 1.0f;
	}	

}
