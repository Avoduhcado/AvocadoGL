package core.editor.entities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JPanel;

import core.editor.entities.sprites.EditSprite;

public class EntitySpritePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EditSprite sprite;
	private int maxDir, maxFrame;
	private boolean solid = false;
	private float xOffset, yOffset, width, height;
	
	/**
	 * Create the panel.
	 */
	public EntitySpritePanel(EditSprite sprite) {
		setBackground(Color.MAGENTA);
		if(sprite != null) {
			this.sprite = new EditSprite(System.getProperty("resources") + "/sprites/" + sprite.getName() + ".png");
			setPreferredSize(new Dimension(this.sprite.getSprite().getWidth(), this.sprite.getSprite().getHeight()));
			maxDir = this.sprite.getMaxDir();
			maxFrame = this.sprite.getMaxFrame();
			xOffset = 0.0f;
			yOffset = 0.0f;
			width = 1.0f;
			height = 1.0f;
		}
		if(this.sprite != null) {
			loadHitbox(this.sprite.getName());
		}
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(Color.MAGENTA);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		if(sprite != null) {
			sprite.draw(g2, null);
			
			g2.setColor(Color.RED);
			g2.drawLine(0, sprite.getSprite().getHeight() / maxDir, sprite.getSprite().getWidth(), sprite.getSprite().getHeight() / maxDir);
			g2.drawLine(sprite.getSprite().getWidth() / maxFrame, 0, sprite.getSprite().getWidth() / maxFrame, sprite.getSprite().getHeight());
			
			if(solid)
				g2.setColor(Color.BLUE);
			else
				g2.setColor(Color.GREEN);
			g2.drawRect((int)((sprite.getSprite().getWidth() / maxFrame) * xOffset), (int)((sprite.getSprite().getHeight() / maxDir) * yOffset), 
					(int)((sprite.getSprite().getWidth() / maxFrame) * width), (int)((sprite.getSprite().getHeight() / maxDir) * height));
		}
	}
	
	public void loadHitbox(String name) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/database/entities"));

	    	String line;
	    	while((line = reader.readLine()) != null) {
	    		String[] temp = line.split(";");
	    		
	    		if(temp[0].matches(name)) {
	    			solid = Boolean.parseBoolean(temp[1]);
	    			xOffset = Float.parseFloat(temp[2]);
	    			yOffset = Float.parseFloat(temp[3]);
	    			width = Float.parseFloat(temp[4]);
	    			height = Float.parseFloat(temp[5]);
	    		}
	    	}
	    	reader.close();
	    } catch (FileNotFoundException e) {
	    	System.out.println("The entity database has been misplaced!");
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	System.out.println("Entity database failed to load!");
	    	e.printStackTrace();
	    }
	}
	
	public void setMaxDir(int maxDir) {
		this.maxDir = maxDir;
	}
	
	public int getMaxDir() {
		return maxDir;
	}
	
	public void setMaxFrame(int maxFrame) {
		this.maxFrame = maxFrame;
	}
	
	public int getMaxFrame() {
		return maxFrame;
	}
	
	public void setSolid(boolean solid) {
		this.solid = solid;
	}
	
	public boolean isSolid() {
		return solid;
	}
	
	public void setXOffset(float xOffset) {
		this.xOffset = xOffset;
	}
	
	public float getXOffset() {
		return xOffset;
	}
	
	public void setYOffset(float yOffset) {
		this.yOffset = yOffset;
	}
	
	public float getYOffset() {
		return yOffset;
	}
	
	public void setWidth(float width) {
		this.width = width;
	}
	
	public float getSpriteWidth() {
		return width;
	}
	
	public void setHeight(float height) {
		this.height = height;
	}
	
	public float getSpriteHeight() {
		return height;
	}
}
