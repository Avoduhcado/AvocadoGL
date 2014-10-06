package core.scene.weather;

import org.lwjgl.opengl.GL11;

import core.Theater;
import core.utilities.exceptions.FailedTextureException;
import core.utilities.loader.TextureLoader;

public class Snowflake extends Downfall {
	
	public Snowflake(int destY, float speed) {
		try {
			flake = TextureLoader.get().getSlickTexture(System.getProperty("resources") + "/sprites/Snowflake.png");
		} catch (FailedTextureException e) {
			flake = e.loadErrorTexture();
		}
		this.destY = destY;
		this.speed = speed;
		y = (float) (destY - Theater.get().getScreen().camera.getHeight() - flake.getImageHeight());
		x = (float) (Math.random() * (Theater.get().getScreen().camera.getX() + Theater.get().getScreen().camera.getWidth() + 800) - 400);
		scale = (float) ((Math.random()*0.8) + 0.2);
		rotation = (int)(Math.random()*360) + 1;
	}
	
	public void update() {
		if(y >= destY) {
			setLanded(true);
		} else {
			if(y + 50 >= destY) {
				y += (speed/2) * Theater.getDeltaSpeed(1);
			} else {
				y += speed * Theater.getDeltaSpeed(1);
			}
		}
	}
	
	public void draw() {
		GL11.glPushMatrix();
		flake.bind();

		GL11.glTranslatef(x - (float) Theater.get().getScreen().camera.getX(), y - (float) Theater.get().getScreen().camera.getY(), 0.0f);
		GL11.glColor3d(1,1,1);
		GL11.glScalef(scale, scale, 0);
		GL11.glRotatef(rotation++, 1, 1, 1);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2f(0, 0);
			GL11.glTexCoord2f(flake.getWidth(), 0);
			GL11.glVertex2f(flake.getImageWidth(), 0);
			GL11.glTexCoord2f(flake.getWidth(), flake.getHeight());
			GL11.glVertex2f(flake.getImageWidth(), flake.getImageHeight());
			GL11.glTexCoord2f(0, flake.getHeight());
			GL11.glVertex2f(0, flake.getImageHeight());
		}
		GL11.glEnd();
		GL11.glPopMatrix();
	}
	
}
