package core.render;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;

import core.Theater;
import core.utilities.exceptions.FailedTextureException;
import core.utilities.loader.TextureLoader;

public class Sprite {
	
	private Texture texture;
	public Shadow shadow;
	private int maxFrame = 1;
	private int maxDir = 1;
	public int frame = 0;
	public int dir = 0;
	private float animStep = 0f;
	public boolean acting;
	public boolean still = false;
	private float scale = 1f;
	private Vector3f rotation = new Vector3f(0,0,0);
	
	public float width;
	public float height;
	public float textureX;
	public float textureY;
	public float textureXWidth;
	public float textureYHeight;
	
	public Sprite(String ref, int maxFrame, int maxDir) {
		setTexture(ref);
		this.maxFrame = maxFrame;
		this.maxDir = maxDir;
	}
	
	public static Sprite loadSprite(String ref) {
		BufferedReader reader;
		
		try {
			reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/database/sprites"));

	    	String line;
	    	while((line = reader.readLine()) != null) {
	    		String[] temp = line.split(";");
	    		
	    		if(temp[0].matches(ref)) {
	    			Sprite sprite = new Sprite(temp[0], Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
	    			
	    			reader.close();
	    			return sprite;
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
		
		System.out.println("Failed to load sprite: " + ref);
		
		return new Sprite(null, 1, 1);
	}
	
	public void draw(float x, float y) {
		//Theater.drawcount++;
		texture.bind();

		width = texture.getWidth() / maxFrame;
		height = texture.getHeight() / maxDir;

		textureX = 0;
		textureY = 0;
		textureXWidth = width;
		textureYHeight = height;
		textureX = width * frame;
		textureXWidth = (width * frame) + width;
		if(maxDir > 1) {
			textureY = height * dir;
			textureYHeight = (height * dir) + height;
		}

		if(shadow != null) {
			//shadow.rotate();
			GL11.glPushMatrix();
			if(still)
				GL11.glTranslatef(x, y, 0);
			else
				GL11.glTranslatef(x - (float)Theater.get().getScreen().camera.getX(), y - 1 - (float)Theater.get().getScreen().camera.getY(), 0);
			GL11.glColor4f(0, 0, 0, 0.5f);
			GL11.glScalef(scale, scale, 1f);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

			GL11.glBegin(GL11.GL_QUADS);
			{
				GL11.glColor4f(0, 0, 0, 0.1f);
				GL11.glTexCoord2f(textureX, textureY);
				GL11.glVertex2f(shadow.getHorizontalRotation(), shadow.getVerticalRotation());
				GL11.glColor4f(0, 0, 0, 0.1f);
				GL11.glTexCoord2f(textureXWidth, textureY);
				GL11.glVertex2f(getWidth()+shadow.getHorizontalRotation(), shadow.getVerticalRotation());
				GL11.glColor4f(0, 0, 0, 0.5f);
				GL11.glTexCoord2f(textureXWidth, textureYHeight);
				if(shadow.getVerticalRotation() > getHeight())
					GL11.glVertex2f(getWidth(), shadow.getRightY());
				else
					GL11.glVertex2f(getWidth(), getHeight());
				GL11.glColor4f(0, 0, 0, 0.5f);
				GL11.glTexCoord2f(textureX, textureYHeight);
				if(shadow.getVerticalRotation() > getHeight())
					GL11.glVertex2f(0, shadow.getLeftY());
				else
					GL11.glVertex2f(0, getHeight());
			}
			GL11.glEnd();
			GL11.glPopMatrix();
		}

		GL11.glPushMatrix();
		if(still)
			GL11.glTranslatef(x, y, 0);
		else
			GL11.glTranslatef(x - (float)Theater.get().getScreen().camera.getX(), y - (float)Theater.get().getScreen().camera.getY(), 0);
		GL11.glColor3f(1f, 1f, 1f);
		GL11.glScalef(scale, scale, 1f);
		GL11.glRotatef(rotation.z, rotation.x, rotation.y, 1);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

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
		GL11.glPopMatrix();

		/*if(Theater.debug()) {
			GL11.glDisable(GL11.GL_TEXTURE_2D);

			GL11.glPushMatrix();
			if(still)
				GL11.glTranslatef(0, 0, 0);
			else
				GL11.glTranslatef(x - (float)Theater.get().getScreen().camera.getX(), y - (float)Theater.get().getScreen().camera.getY(), 0);
			GL11.glColor3f(1.0f, 0.0f, 1.0f);
			GL11.glLineWidth(1.0f);

			GL11.glBegin(GL11.GL_LINE_LOOP);
			{
				GL11.glVertex2f(0, 0);
				GL11.glVertex2f(getWidth() * scale, 0);
				GL11.glVertex2f(getWidth() * scale, getHeight() * scale);
				GL11.glVertex2f(0, getHeight() * scale);
			}
			GL11.glEnd();
			GL11.glPopMatrix();

			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}*/
	}
	
	public void animate() {
		if(maxFrame > 1) {
			animStep += Theater.getDeltaSpeed(0.025f);
			if (animStep >= 0.16f) {
				animStep = 0f;
				frame++;
				if (frame >= maxFrame) {
					frame = 0;
				}
			}
			acting = false;
		}
	}
	
	public void setTexture(String ref) {
		String temp = System.getProperty("resources") + "/sprites/" + ref + ".png";
		try {
			this.texture = TextureLoader.get().getSlickTexture(temp);
		} catch (FailedTextureException e) {
			this.texture = e.loadErrorTexture();
		}
	}
	
	public void setShadow(Shadow shadow) {
		this.shadow = shadow;
	}
	
	public void setFrame(int frame) {
		this.frame = frame;
	}
	
	public void setDir(int dir) {
		this.dir = dir;
	}

	public int getMaxFrame() {
		return maxFrame;
	}
	
	public int getMaxDir() {
		return maxDir;
	}
	
	public int getWidth() {
		return texture.getImageWidth() / maxFrame;
	}
	
	public int getHeight() {
		return texture.getImageHeight() / maxDir;
	}
	
	public void setStill(boolean still) {
		this.still = still;
	}
	
	public float getScale() {
		return scale;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public Vector3f getRotation() {
		return rotation;
	}
	
	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}
	
}
