package core.render;

import org.lwjgl.opengl.GL11;

import core.Theater;

public class Shadow {

	private float leftY;
	private float rightY;
	private float horizontalRotation = 30f;
	private float verticalRotation = 15f;
	private boolean hRotate, vRotate;
	
	public Shadow() {
		//leftY = ly;
		//rightY = ry;
	}
	
	public void draw(Sprite sprite, float x, float y) {
		GL11.glPushMatrix();
		if(sprite.still)
			GL11.glTranslatef(0, 0, 0);
		else
			GL11.glTranslatef(x - (float)Theater.get().getScreen().camera.getX(), y - (float)Theater.get().getScreen().camera.getY(), 0);
		//GL11.glTranslated(getBox().getMaxX() - 5, getBox().getMaxY() + (getHeight()-5), 0);
		//GL11.glTranslated(x + getWidth()-(getWidth()/5), y + (getHeight() - (getHeight()/10))*2, 0);
		GL11.glColor4f(0, 0, 0, 0.5f);
		GL11.glScalef(sprite.getScale(), sprite.getScale(), 1f);
		//GL11.glRotated(180, 1, 0, 0);
		//GL11.glRotated(30, 0, 0, 1);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2f(sprite.textureX, sprite.textureY);
			GL11.glVertex2f(getHorizontalRotation(), getVerticalRotation());
			GL11.glTexCoord2f(sprite.textureXWidth, sprite.textureY);
			GL11.glVertex2f(sprite.getWidth() + getHorizontalRotation(), getVerticalRotation());
			GL11.glTexCoord2f(sprite.textureXWidth, sprite.textureYHeight);
			if(getVerticalRotation() > sprite.getHeight())
				GL11.glVertex2f(sprite.getWidth(), getRightY());
			else
				GL11.glVertex2f(sprite.getWidth(), sprite.getHeight());
			GL11.glTexCoord2f(sprite.textureX, sprite.textureYHeight);
			if(getVerticalRotation() > sprite.getHeight())
				GL11.glVertex2f(0, getLeftY());
			else
				GL11.glVertex2f(0, sprite.getHeight());
		}
		GL11.glEnd();
		GL11.glPopMatrix();
	}
	
	public float getLeftY() {
		return leftY;
	}
	
	public float getRightY() {
		return rightY;
	}
	
	public void rotate() {
		horizontalRotate();
		verticalRotate();
	}
	
	public void horizontalRotate() {
		if(hRotate) {
			horizontalRotation += Theater.getDeltaSpeed(0.25f);
			//if(horizontalRotation >= width * 1.5)
			if(horizontalRotation >= 50f)
				hRotate = false;
		} else {
			horizontalRotation -= Theater.getDeltaSpeed(0.25f);
			//if(horizontalRotation <= -width * 1.5)
			if(horizontalRotation <= -50f)
				hRotate = true;
		}
	}
	
	public void verticalRotate() {
		if(vRotate) {
			verticalRotation += Theater.getDeltaSpeed(0.25f);
			//if(verticalRotation >= height * 1.5)
			if(verticalRotation >= 25f)
				vRotate = false;
		} else {
			verticalRotation -= Theater.getDeltaSpeed(0.25f);
			//if(verticalRotation <= -height * 1.5)
			if(verticalRotation <= -25f)
				vRotate = true;
		}
	}
	
	public float getHorizontalRotation() {
		return horizontalRotation;
	}
	
	public float getVerticalRotation() {
		return verticalRotation;
	}
	
}
