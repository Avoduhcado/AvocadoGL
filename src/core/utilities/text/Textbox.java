package core.utilities.text;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

import core.Theater;
import core.entity.Entity;
import core.keyboard.Keybinds;
import core.scene.Stage;
import core.utilities.actions.Action;
import core.utilities.exceptions.FailedTextureException;
import core.utilities.loader.TextureLoader;

public abstract class Textbox implements Cloneable {

	protected ArrayList<TextLine> text = new ArrayList<TextLine>();
	protected Rectangle2D bounds = new Rectangle2D.Double(0, 0, 1, 1);
	protected float lineheight;
	protected Texture background;
	private Texture down;
	protected Action action;
	protected boolean overflow;
	protected Entity source;
	protected boolean still;
	protected float x;
	protected float y;
	protected int width;
	protected int height;
	
	protected float moveX;
	protected float moveY;
	protected Vector2f speed = new Vector2f();
	
	public Textbox() {
		try {
			background = TextureLoader.get().getSlickTexture(System.getProperty("resources") + "/ui/menu.png");
		} catch (FailedTextureException e) {
			background = e.loadErrorTexture();
		}
		try {
			down = TextureLoader.get().getSlickTexture(System.getProperty("resources") + "/ui/menuDown.png");
		} catch (FailedTextureException e) {
			down = e.loadErrorTexture();
		}
		
		width = background.getImageWidth()/3;
		height = background.getImageHeight()/3;
	}
	
	public void update(Stage stage) {
		
	}

	public Object clone() {
		Textbox text = new PlainText(false, 0f, false);
		text.action = this.action;
		text.source = this.source;
		
		return text;
	}
	
	public void move() {
		if(this.x != moveX) {
			if(this.x < moveX) {
				this.x += Theater.getDeltaSpeed(speed.x);
				if(this.x >= moveX) {
					speed.x = 0;
					this.x = moveX;
				}
			} else {
				this.x += Theater.getDeltaSpeed(speed.x);
				if(this.x <= moveX) {
					speed.x = 0;
					this.x = moveX;
				}
			}
		}
		
		if(this.y != moveY) {
			if(this.y < moveY) {
				this.y += Theater.getDeltaSpeed(speed.y);
				if(this.y >= moveY) {
					speed.y = 0;
					this.y = moveY;
				}
			} else {
				this.y += Theater.getDeltaSpeed(speed.y);
				if(this.y <= moveY) {
					speed.y = 0;
					this.y = moveY;
				}
			}
		}
	}
	
	public void draw() {
		GL11.glPushMatrix();
		background.bind();

		if(still)
			GL11.glTranslatef(x, y, 0);
		else
			GL11.glTranslated(x - Math.floor(Theater.get().getScreen().camera.getX()), y - Math.floor(Theater.get().getScreen().camera.getY()), 0);
		GL11.glColor4f(1, 1, 1, 0.8f);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

		for(int x = 0; x<3; x++) {
			for(int y = 0; y<3; y++) {
				if((x + 1)%2 != 0 && (y+1)%2 != 0) {
					setCornerQuads(x, y);
				} else if((x + 1)%2 == 0 && (y+1)%2 == 0) {
					setInnerQuads(x, y);
				} else if ((x + 1)%2 != 0){
					setVertQuads(x, y);
				} else {
					setHorizQuads(x, y);
				}
			}
		}	    
		GL11.glEnd();
		GL11.glPopMatrix();

		for(int x = 0; x<text.size(); x++) {
			if(overflow && (this.y + 40 + ((x+1) * lineheight)) >= Theater.get().getScreen().camera.getHeight())
				break;
			if(text.get(x) == null)
				x = text.size();

			if(still) {
				//unifont.drawString(this.x + 21, this.y + 21 + (x * lineheight), text.get(x), Color.black);
				//unifont.drawString(this.x + 20, this.y + 20 + (x * lineheight), text.get(x), defaultColor);
				for(int i = 0; i<text.size(); i++) {
					text.get(i).drawShadow(this.x + 21, this.y + 21 + (x * lineheight));
					text.get(i).draw(this.x + 20, this.y + 20 + (x * lineheight));
				}
			} else {
				/*unifont.drawString(this.x + 21 - (float)Math.floor(Theater.get().getScreen().camera.getX()), 
						(this.y + 21 + (x * lineheight)) - (float)Math.floor(Theater.get().getScreen().camera.getY()), text.get(x), Color.black);
				unifont.drawString(this.x + 20 - (float)Math.floor(Theater.get().getScreen().camera.getX()), 
						(this.y + 20 + (x * lineheight)) - (float)Math.floor(Theater.get().getScreen().camera.getY()), text.get(x), defaultColor);*/
				for(int i = 0; i<text.size(); i++) {
					text.get(i).drawShadow(this.x + 21 - (float)Math.floor(Theater.get().getScreen().camera.getX()), 
							(this.y + 21 + (x * lineheight)) - (float)Math.floor(Theater.get().getScreen().camera.getY()));
					text.get(i).draw(this.x + 20 - (float)Math.floor(Theater.get().getScreen().camera.getX()), 
							(this.y + 20 + (x * lineheight)) - (float)Math.floor(Theater.get().getScreen().camera.getY()));
				}
			}
		}
		if(overflow) {
			drawScroll();
		}
	}
	
	public void setCornerQuads(int x, int y) {
		double texWidth = background.getWidth()/3;
		double texHeight = background.getHeight()/3;
		double verWidth = (x * width) + ((bounds.getWidth()/2) * x);
		double verHeight = 0;
		if(overflow)
			verHeight = (((Theater.get().getScreen().camera.getY() + Theater.get().getScreen().camera.getHeight()) - this.y - height)/2) * y;
		else
			verHeight = (height + (((text.size() - 1) * lineheight) /2)) * y;
				
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2d(x * texWidth, y * texHeight);
		    GL11.glVertex2d(verWidth, verHeight);
		    
		    GL11.glTexCoord2d((x * texWidth) + texWidth, y * texHeight);
		    GL11.glVertex2d(verWidth + width, verHeight);
		    
		    GL11.glTexCoord2d((x * texWidth) + texWidth, (y * texHeight) + texHeight);
		    GL11.glVertex2d(verWidth + width, verHeight + height);
		    
		    GL11.glTexCoord2d(x * texWidth, (y * texHeight) + texHeight);
		    GL11.glVertex2d(verWidth, verHeight + height);
		}
		GL11.glEnd();
	}
	
	public void setVertQuads(int x, int y) {
		double texWidth = background.getWidth()/3;
		double texHeight = background.getHeight()/3;
		double verWidth = (x * width) + (((bounds.getWidth())/2) * x);
		double verHeight = 0;
		if(overflow)
			verHeight =  (Theater.get().getScreen().camera.getY() + Theater.get().getScreen().camera.getHeight()) - this.y - height*2 - 1;
		else
			verHeight = height + ((text.size() - 1) * lineheight);
		
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2d(x * texWidth, y * texHeight);
		    GL11.glVertex2d(verWidth, height);
		    
		    GL11.glTexCoord2d((x * texWidth) + texWidth, y * texHeight);
		    GL11.glVertex2d(verWidth + width, height);
		    
		    GL11.glTexCoord2d((x * texWidth) + texWidth, (y * texHeight) + texHeight);
		    GL11.glVertex2d(verWidth + width, verHeight + height);
		    
		    GL11.glTexCoord2d(x * texWidth, (y * texHeight) + texHeight);
		    GL11.glVertex2d(verWidth, verHeight + height);
		}
	}
	
	public void setHorizQuads(int x, int y) {
		double texWidth = background.getWidth()/3;
		double texHeight = background.getHeight()/3;
		double verWidth = width + bounds.getWidth();
		double verHeight = 0;
		if(overflow)
			verHeight = (((Theater.get().getScreen().camera.getY() + Theater.get().getScreen().camera.getHeight()) - this.y - height)/2) * y;
		else
			verHeight = (height + (((text.size() - 1) * lineheight) /2)) * y;
		
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2d(texWidth, y * texHeight);
		    GL11.glVertex2d(width, verHeight);
		    
		    GL11.glTexCoord2d((x * texWidth) + texWidth, y * texHeight);
		    GL11.glVertex2d(verWidth + width, verHeight);
		    
		    GL11.glTexCoord2d((x * texWidth) + texWidth, (y * texHeight) + texHeight);
		    GL11.glVertex2d(verWidth + width, verHeight + height);
		    
		    GL11.glTexCoord2d(x * texWidth, (y * texHeight) + texHeight);
		    GL11.glVertex2d(width, verHeight + height);
		}
	}
	
	public void setInnerQuads(int x, int y) {
		double texWidth = background.getWidth()/3;
		double texHeight = background.getHeight()/3;
		double verWidth = width + bounds.getWidth();
		double verHeight = 0;
		if(overflow)
			verHeight = (Theater.get().getScreen().camera.getY() + Theater.get().getScreen().camera.getHeight()) - this.y - height*2 - 1;
		else
			verHeight = height + ((text.size() - 1) * lineheight);
		
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2d(x * texWidth, y * texHeight);
			GL11.glVertex2d(width, height);
		    
		    GL11.glTexCoord2d((x * texWidth) + texWidth, y * texHeight);
		    GL11.glVertex2d(verWidth + width, height);
		    
		    GL11.glTexCoord2d((x * texWidth) + texWidth, (y * texHeight) + texHeight);
		    GL11.glVertex2d(verWidth + width, verHeight + height);
		    
		    GL11.glTexCoord2d(x * texWidth, (y * texHeight) + texHeight);
		    GL11.glVertex2d(width, verHeight + height);
		}
	}
	
	public void drawScroll() {
		GL11.glPushMatrix();
		down.bind();
		
		if(still)
			GL11.glTranslatef((float) (x + (getFullWidth() / 2) - down.getWidth()), (float)Theater.get().getScreen().camera.getHeight() - down.getImageHeight(), 0);
		else
			GL11.glTranslatef((float) (this.x + (background.getImageWidth()/2) + (bounds.getWidth() / 2) + 0.5 - (down.getImageWidth()/2)) - (float)Theater.get().getScreen().camera.getX(), 
					(float) (Theater.get().getScreen().camera.getHeight() - down.getImageHeight()) - (float)Theater.get().getScreen().camera.getY(), 0.0f);
		
		GL11.glColor3f(1,1,1);
		GL11.glEnable(GL11.GL_BLEND);
	    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
	    
	    GL11.glBegin(GL11.GL_QUADS);
	    {
	    	GL11.glTexCoord2f(0, 0);
		    GL11.glVertex2f(0, 0);
		    GL11.glTexCoord2f(down.getWidth(), 0);
		    GL11.glVertex2f(down.getImageWidth(), 0);
		    GL11.glTexCoord2f(down.getWidth(), down.getHeight());
		    GL11.glVertex2f(down.getImageWidth(), down.getImageHeight());
		    GL11.glTexCoord2f(0, down.getHeight());
		    GL11.glVertex2f(0, down.getImageHeight());
	    }
	    GL11.glEnd();
	    GL11.glPopMatrix();
	    
	    GL11.glPushMatrix();
		down.bind();
		
		if(still)
			GL11.glTranslatef((float) (x + (getFullWidth() / 2) - down.getWidth()), y, 0);
		else
			GL11.glTranslatef((float)(this.x + (background.getImageWidth()/2) + (bounds.getWidth() / 2) + 0.5 - (down.getImageWidth()/2)) - (float)Theater.get().getScreen().camera.getX(),
					this.y - (float)Theater.get().getScreen().camera.getY(), 0.0f);
		GL11.glColor3f(1,1,1);
		GL11.glEnable(GL11.GL_BLEND);
	    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
	    
	    GL11.glBegin(GL11.GL_QUADS);
	    {
	    	GL11.glTexCoord2f(0, down.getHeight());
		    GL11.glVertex2f(0, 0);
		    GL11.glTexCoord2f(down.getWidth(), down.getHeight());
		    GL11.glVertex2f(down.getImageWidth(), 0);
		    GL11.glTexCoord2f(down.getWidth(), 0);
		    GL11.glVertex2f(down.getImageWidth(), down.getImageHeight());
		    GL11.glTexCoord2f(0, 0);
		    GL11.glVertex2f(0, down.getImageHeight());
	    }
	    GL11.glEnd();
	    GL11.glPopMatrix();
	}
	
	public void buildText(String message) {
		String[] temp = message.split(";");
		for(int x = 0; x<temp.length; x++) {
			text.add(new TextLine(temp[x]));
			if(text.get(x).getWidth() > bounds.getWidth())
				bounds = new Rectangle2D.Double(0, 0, text.get(x).getWidth(), 0);
			if(text.get(x).getHeight() > lineheight)
				lineheight = text.get(x).getHeight();
		}
				
		if((lineheight * text.size()-1) + y > Theater.get().getScreen().camera.getY() + Theater.get().getScreen().camera.getHeight()) {
			overflow = true;
		} else
			overflow = false;
	}
	
	public void clear() {
		text.clear();
		bounds = new Rectangle2D.Double(0, 0, 0, 0);
	}
	
	public void clearKeyboard() {
		Keybinds.clear();
	}
	
	public void setSource(Entity source) {
		this.source = source;
	}
	
	public void setAction(Action action) {
		this.action = action;
	}
	
	public void setStill(boolean still) {
		this.still = still;
	}
	
	public float getX() {
		return x;
	}
	
	public float getEventualX() {
		return moveX;
	}
	
	public float getY() {
		return y;
	}
	
	public float getEventualY() {
		return moveY;
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setCenterPosition(float x, float y) {
		if(x - (bounds.getWidth() / 2f) > 0) {
			this.x = (float) (x - (bounds.getWidth() / 2f));
		} else {
			this.x = 0f;
		}
		
		if(y - getFullHeight() > 0) {
			this.y = (float) (y - getFullHeight());
		} else {
			this.y = 0f;
		}
	}
	
	public void setPositionOverTime(float startX, float endX, float startY, float endY, float time) {
		this.x = startX;
		this.moveX = endX;
		this.y = startY;
		this.moveY = endY;
		this.speed.x = (endX - startX) * time;
		this.speed.y = (endY - startY) * time;
	}
		
	public ArrayList<TextLine> getText() {
		return text;
	}
	
	public Entity getSource() {
		return source;
	}
	
	public Action getAction() {
		return action;
	}
	
	public float getFullHeight() {
		return ((lineheight * text.size()) + (height * 2));
	}
	
	public float getFullWidth() {
		return (float) (bounds.getWidth() + (width * 3) + 10);
	}
	
	// Plain functions
	public abstract void cancelFill();
	
	// Select functions
	public abstract int select();
	public abstract int getSelected();
	public abstract void showCursor(boolean showCursor);
	
	// Input functions
	public abstract String input();
	
}
