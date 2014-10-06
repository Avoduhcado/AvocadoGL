package core.utilities.text;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import core.render.Sprite;

public class Text {

	private Font font = new Font("Comic Sans", Font.PLAIN, 16);
	private String fontName = "Something.ttf";
	private static String defaultFontName = "Something.ttf";
	private UnicodeFont unifont;
	public static UnicodeFont defaultUnifont;
	
	private String text;
	private float size;
	private static float defaultSize = 18f;
	private Color color;
	public static Color defaultColor = new Color(255, 100, 0);
	private Sprite icon;
	
	public Text(String text) {
		if(defaultUnifont == null)
			setDefaultFont();
		parseText(text);
	}
	
	public void parseText(String text) {
		if(text.contains(">")) {
			String[] params = text.substring(0, text.indexOf('>')).split(",");
			for(int x = 0; x<params.length; x++) {
				if(params[x].startsWith("c")) {
					color = parseColor(params[x].substring(1));
				} else if(params[x].startsWith("s")) {
					parseSize(params[x].substring(1));
				} else if(params[x].startsWith("i")) {
					parseIcon(params[x].substring(1));
				}
			}
			this.text = text.split(">")[1];
		} else {
			this.text = text;
		}
	}
	
	public Color parseColor(String text) {
		try {
			if(text.startsWith("#")) {
				return Color.decode(text);
			} else {
				Field f = Color.class.getField(text);
				
				return (Color) f.get(null);
			}
		} catch (Exception e) {			
			return defaultColor;
		}
	}
	
	public void parseSize(String text) {
		size = Float.parseFloat(text);
		setFont();
	}
	
	public void parseIcon(String text) {
		icon = Sprite.loadSprite(text);
		icon.setStill(true);
	}
	
	@SuppressWarnings("unchecked")
	public void setFont() {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		try {
			font = Font.createFont(Font.PLAIN, new FileInputStream(System.getProperty("resources") + "/fonts/" + fontName));
			if(size == 0f)
				font = font.deriveFont(defaultSize);
			else
				font = font.deriveFont(size);
		} catch (FontFormatException e) {
			System.err.println("Invalid font format");
		} catch (IOException e) {
			System.err.println("Could not find font");
		}
		unifont = new UnicodeFont(font);
		unifont.getEffects().add(new ColorEffect());
		unifont.addAsciiGlyphs();
		
		try {
			unifont.loadGlyphs();
		} catch(SlickException e) {
			System.err.println("Could not load ASCII Glyphs");
		}
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	@SuppressWarnings("unchecked")
	public void setDefaultFont() {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		try {
			font = Font.createFont(Font.PLAIN, new FileInputStream(System.getProperty("resources") + "/fonts/" + defaultFontName));
			font = font.deriveFont(defaultSize);
		} catch (FontFormatException e) {
			System.err.println("Invalid font format");
		} catch (IOException e) {
			System.err.println("Could not find font");
		}
		
		defaultUnifont = new UnicodeFont(font);
		defaultUnifont.getEffects().add(new ColorEffect());
		defaultUnifont.addAsciiGlyphs();
		try {
			defaultUnifont.loadGlyphs();
		} catch(SlickException e) {
			System.err.println("Could not load ASCII Glyphs");
		}
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public static void drawString(String text, float x, float y) {
		defaultUnifont.drawString(x, y, text);
	}
	
	public static void drawString(String text, float x, float y, Color col) {
		defaultUnifont.drawString(x, y, text, col);
	}
	
	public UnicodeFont getFont() {
		if(unifont != null)
			return unifont;
		
		return defaultUnifont;
	}
	
	public String getText() {
		return text;
	}
	
	public float getSize() {
		return size;
	}
	
	public Color getColor() {
		if(color != null)
			return color;
		
		return defaultColor;
	}
	
	public Sprite getIcon() {
		return icon;
	}
	
}
