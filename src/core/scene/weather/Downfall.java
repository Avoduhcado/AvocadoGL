package core.scene.weather;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

public class Downfall {

	protected Texture flake;
	protected float x;
	protected float y;
	protected float speed;
	protected int destY;
	private boolean landed;
	protected float scale;
	protected int rotation;
	
	public void update() {
		
	}
	
	public void draw() {
		GL11.glPushMatrix();
		flake.bind();
		
		GL11.glTranslatef(x, y, 0.0f);
	    GL11.glColor3d(1,1,1);
	    //GL11.glScaled(scale, scale, 0);
	    //GL11.glRotated(rotation++, 1, 1, 1);
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
	
	public boolean isLanded() {
		return landed;
	}

	public void setLanded(boolean landed) {
		this.landed = landed;
	}
}
