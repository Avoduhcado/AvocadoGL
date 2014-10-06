package core.scene;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import core.Theater;
import core.utilities.exceptions.FailedTextureException;
import core.utilities.loader.TextureLoader;
import core.utilities.sounds.Ensemble;
import core.utilities.sounds.SoundEffect;

public class Firework {

	private Texture trail;
	private Texture explosion;
	private Texture particles;
	private float x;
	private float y;
	private float speed;
	private float fuse;
	private float[] color = new float[3];
	private float size = 0.1f;
	private float fade = 0.85f;
	private boolean burntOut;
	
	public Firework(float x, float y, float speed, float fuse) {
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.fuse = fuse;
		try {
			this.trail = TextureLoader.get().getSlickTexture("database/sprites/Trail.png");
		} catch (FailedTextureException e) {
			this.trail = e.loadErrorTexture();
		}
		try {
			this.explosion = TextureLoader.get().getSlickTexture("database/sprites/Explosion.png");
		} catch (FailedTextureException e) {
			this.explosion = e.loadErrorTexture();
		}
		try {
			this.particles = TextureLoader.get().getSlickTexture("database/sprites/Particles.png");
		} catch (FailedTextureException e) {
			this.particles = e.loadErrorTexture();
		}
		for(int a = 0; a<color.length; a++) {
			color[a] = (float) Math.random();
		}
	}
	
	public void update() {
		//System.out.println("Uhh, updating " + fuse);
		if(fuse > 0) {
			fuse -= Theater.getDeltaSpeed(0.025f);
			y -= speed * Theater.getDeltaSpeed(0.025f);
			if(fuse <= 0) {
				Ensemble.get().playSoundEffect(new SoundEffect("Firework.ogg", 1f));
				fuse = (int)(Math.random()*100) - 275;
			}
		} else {
			fuse += Theater.getDeltaSpeed(1);
			if(fuse < -20)
				size += 0.0075f * Theater.getDeltaSpeed(0.025f);
			if(fuse % 10 >= -1 && fuse % 10 <= 0)
				fade -= 0.02f * Theater.getDeltaSpeed(0.025f);
			if(fuse >= 0)
				burntOut = true;
		}
	}
	
	public void draw() {
		if(fuse > 0) {
			GL11.glPushMatrix();
			trail.bind();
			
			GL11.glTranslatef(x - (float)Theater.get().getScreen().camera.getX(), y - (float)Theater.get().getScreen().camera.getY(), 0);
		    GL11.glColor3f(1,1,1);
		    GL11.glEnable(GL11.GL_BLEND);
		    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		    
		    GL11.glBegin(GL11.GL_QUADS);
		    {
		    	GL11.glTexCoord2f(0, 0);
			    GL11.glVertex2f(0, 0);
			    GL11.glTexCoord2f(trail.getWidth(), 0);
			    GL11.glVertex2f(trail.getImageWidth(), 0);
			    GL11.glTexCoord2f(trail.getWidth(), trail.getHeight());
			    GL11.glVertex2f(trail.getImageWidth(), trail.getImageHeight());
			    GL11.glTexCoord2f(0, trail.getHeight());
			    GL11.glVertex2f(0, trail.getImageHeight());
			}
			GL11.glEnd();
			GL11.glPopMatrix();
		} else {
			GL11.glPushMatrix();
			explosion.bind();
			
			GL11.glTranslatef(x - (float)Theater.get().getScreen().camera.getX() - ((explosion.getImageWidth() * size) / 2),
					y - (float)Theater.get().getScreen().camera.getY() - ((explosion.getImageHeight() * size) / 2), 0.0f);
		    //GL11.glColor4d(color[0], color[1], color[2], fade);
		    GL11.glScaled(size, size, 0.0);
		    GL11.glEnable(GL11.GL_BLEND);
		    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		    
		    GL11.glBegin(GL11.GL_QUADS);
		    {
		    	GL11.glColor4f(color[0], color[1], color[2], fade);
		    	GL11.glTexCoord2f(0, 0);
			    GL11.glVertex2f(0, 0);
			    GL11.glTexCoord2f(explosion.getWidth(), 0);
			    GL11.glVertex2f(explosion.getImageWidth(), 0);
			    GL11.glTexCoord2f(explosion.getWidth(), explosion.getHeight());
			    GL11.glVertex2f(explosion.getImageWidth(), explosion.getImageHeight());
			    GL11.glTexCoord2f(0, explosion.getHeight());
			    GL11.glVertex2f(0, explosion.getImageHeight());
			}
			GL11.glEnd();
			GL11.glPopMatrix();
			
			GL11.glPushMatrix();
			particles.bind();
			
			GL11.glTranslatef(x - (float)Theater.get().getScreen().camera.getX() - ((explosion.getImageWidth() * size) / 1.2f),
					y - (float)Theater.get().getScreen().camera.getY() - ((explosion.getImageHeight() * size) / 1.2f), 0.0f);
			
		    GL11.glScalef(size * 1.5f, size * 1.5f, 0.0f);
		    GL11.glEnable(GL11.GL_BLEND);
		    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		    
		    GL11.glBegin(GL11.GL_QUADS);
		    {
		    	if(fuse + 25 < 0)
					GL11.glColor4f(color[0], color[1], color[2], fade - 0.1f);
				else if(fuse % 2 >= -1 && fuse % 2 <= 0)
					GL11.glColor4f(color[0], color[1], color[2], 0.0f);
		    	GL11.glTexCoord2f(0, 0);
			    GL11.glVertex2f(0, 0);
			    GL11.glTexCoord2f(explosion.getWidth(), 0);
			    GL11.glVertex2f(explosion.getImageWidth(), 0);
			    GL11.glTexCoord2f(explosion.getWidth(), explosion.getHeight());
			    GL11.glVertex2f(explosion.getImageWidth(), explosion.getImageHeight());
			    GL11.glTexCoord2f(0, explosion.getHeight());
			    GL11.glVertex2f(0, explosion.getImageHeight());
			}
			GL11.glEnd();
			GL11.glPopMatrix();
		}
	}
	
	public boolean isBurntOut() {
		return burntOut;
	}
	
}
