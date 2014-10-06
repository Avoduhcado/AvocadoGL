package core.render;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import core.Theater;
import core.utilities.exceptions.FailedTextureException;
import core.utilities.loader.TextureLoader;

public class Tile {

	private Texture texture;
	private boolean autoTile;
	private float x, y;
	private Point[] corners = {new Point(0,1), new Point(1,1), new Point(1,0), new Point(0,0)};
	private int maxFrame = 1;
	public int frame = 0;
	private float animStep = 0f;
	
	private float width;
	private float height;
	private float textureX;
	private float textureY;
	private float textureXWidth;
	private float textureYHeight;
    
    public Tile(String ref, int x, int y, boolean auto) {
    	this.autoTile = auto;
    	setTexture(ref);
    	load(ref.replaceFirst(".png", ""));
    	this.x = x;
    	this.y = y;
    }
    
    public void load(String ref) {
    	BufferedReader reader;
		
		try {
			reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/database/sprites"));

	    	String line;
	    	while((line = reader.readLine()) != null) {
	    		String[] temp = line.split(";");
	    		
	    		if(temp[0].matches(ref)) {
	    			this.maxFrame = Integer.parseInt(temp[1]);
	    			
	    			reader.close();
	    			break;
	    		}
	    	}

	    	reader.close();
	    } catch (FileNotFoundException e) {
	    	System.out.println("The sprite database has been misplaced!");
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	System.out.println("Sprite database failed to load!");
	    	e.printStackTrace();
	    }
    }
    
    public void draw() {
    	texture.bind();

    	if(autoTile) {
    		width = (texture.getWidth()/6) / maxFrame;
    		height = texture.getHeight()/8;
    	} else {
    		width = texture.getWidth() / maxFrame;
    		height = texture.getHeight();
    	}

    	textureX = 0;
    	textureY = 0;
    	textureXWidth = width;
    	textureYHeight = height;
    	textureX = width * frame;
    	textureXWidth = (width * frame) + width;

    	GL11.glPushMatrix();
    	GL11.glTranslatef(x - (float)Theater.get().getScreen().camera.getX(), y - (float)Theater.get().getScreen().camera.getY(), 0);
    	GL11.glColor3f(1f, 1f, 1f);
    	GL11.glEnable(GL11.GL_BLEND);
    	GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

    	if(autoTile)
    		drawAuto();
    	else {
	    	GL11.glBegin(GL11.GL_QUADS);
	    	{
	    		GL11.glTexCoord2f(textureX, textureY);
	    		GL11.glVertex2f(0, 0);
	    		GL11.glTexCoord2f(textureXWidth, textureY);
	    		GL11.glVertex2f(getWidth(), 0);
	    		GL11.glTexCoord2f(textureXWidth, textureYHeight);
	    		GL11.glVertex2f(getWidth(), getHeight());
	    		GL11.glTexCoord2f(textureX, textureYHeight);
	    		GL11.glVertex2f(0, getHeight());
	    	}
	    	GL11.glEnd();
    	}
    	GL11.glPopMatrix();
    }
    
    public void drawAuto() {
    	textureX = width * (frame * 6);
    	textureXWidth = (width * (frame * 6)) + width;
    	int verWidth = getWidth() / 2;
    	int verHeight = getHeight() / 2;
    	// Bottom Left
    	GL11.glBegin(GL11.GL_QUADS);
    	{
    		GL11.glTexCoord2f(textureX + (corners[0].x * width), textureY + (corners[0].y * height));
    		GL11.glVertex2f(0, verHeight);
    		GL11.glTexCoord2f(textureXWidth + (corners[0].x * width), textureY + (corners[0].y * height));
    		GL11.glVertex2f(verWidth, verHeight);
    		GL11.glTexCoord2f(textureXWidth + (corners[0].x * width), textureYHeight + (corners[0].y * height));
    		GL11.glVertex2f(verWidth, verHeight * 2);
    		GL11.glTexCoord2f(textureX + (corners[0].x * width), textureYHeight + (corners[0].y * height));
    		GL11.glVertex2f(0, verHeight * 2);
    	}
    	GL11.glEnd();
    	// Bottom Right
    	GL11.glBegin(GL11.GL_QUADS);
    	{
    		GL11.glTexCoord2f(textureX + (corners[1].x * width), textureY + (corners[1].y * height));
    		GL11.glVertex2f(verWidth, verHeight);
    		GL11.glTexCoord2f(textureXWidth + (corners[1].x * width), textureY + (corners[1].y * height));
    		GL11.glVertex2f(verWidth * 2, verHeight);
    		GL11.glTexCoord2f(textureXWidth + (corners[1].x * width), textureYHeight + (corners[1].y * height));
    		GL11.glVertex2f(verWidth * 2, verHeight * 2);
    		GL11.glTexCoord2f(textureX + (corners[1].x * width), textureYHeight + (corners[1].y * height));
    		GL11.glVertex2f(verWidth, verHeight * 2);
    	}
    	GL11.glEnd();
    	// Top Right
    	GL11.glBegin(GL11.GL_QUADS);
    	{
    		GL11.glTexCoord2f(textureX + (corners[2].x * width), textureY + (corners[2].y * height));
    		GL11.glVertex2f(verWidth, 0);
    		GL11.glTexCoord2f(textureXWidth + (corners[2].x * width), textureY + (corners[2].y * height));
    		GL11.glVertex2f(verWidth * 2, 0);
    		GL11.glTexCoord2f(textureXWidth + (corners[2].x * width), textureYHeight + (corners[2].y * height));
    		GL11.glVertex2f(verWidth * 2, verHeight);
    		GL11.glTexCoord2f(textureX + (corners[2].x * width), textureYHeight + (corners[2].y * height));
    		GL11.glVertex2f(verWidth, verHeight);
    	}
    	GL11.glEnd();
    	// Top Left
    	GL11.glBegin(GL11.GL_QUADS);
    	{
    		GL11.glTexCoord2f(textureX + (corners[3].x * width), textureY + (corners[3].y * height));
    		GL11.glVertex2f(0, 0);
    		GL11.glTexCoord2f(textureXWidth + (corners[3].x * width), textureY + (corners[3].y * height));
    		GL11.glVertex2f(16, 0);
    		GL11.glTexCoord2f(textureXWidth + (corners[3].x * width), textureYHeight + (corners[3].y * height));
    		GL11.glVertex2f(16, verHeight);
    		GL11.glTexCoord2f(textureX + (corners[3].x * width), textureYHeight + (corners[3].y * height));
    		GL11.glVertex2f(0, verHeight);
    	}
    	GL11.glEnd();
    }
    
    public void animate() {
    	if(maxFrame > 1) {
			animStep += Theater.getDeltaSpeed(0.025f);
			if (animStep >= 0.25f) {
				animStep = 0f;
				frame++;
				if (frame >= maxFrame) {
					frame = 0;
				}
			}
		}
    }
    
    public void setTexture(String ref) {
		String temp;
		if(autoTile)
			temp = System.getProperty("resources") + "/tiles/autotiles/" + ref;
		else
			temp = System.getProperty("resources") + "/tiles/" + ref;
		try {
			this.texture = TextureLoader.get().getSlickTexture(temp);
		} catch (FailedTextureException e) {
			this.texture = e.loadErrorTexture();
		}
	}
    
    public void setCorners(Point[] corners) {
    	this.corners = corners;
    }
    
    public int getWidth() {
    	if(autoTile)
    		return (texture.getImageWidth()/3) / maxFrame;
    	else
    		return texture.getImageWidth() / maxFrame;
	}
	
	public int getHeight() {
		if(autoTile)
			return texture.getImageHeight()/4;
		else
			return texture.getImageHeight();
	}
}
