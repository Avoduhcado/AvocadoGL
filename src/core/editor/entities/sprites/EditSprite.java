package core.editor.entities.sprites;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import core.editor.Editor;
import core.utilities.loader.ImageLoader;

public class EditSprite implements Cloneable {

	//private static int count = 0;
	
	private int x;
	private int y;
	private int maxDir = 1;
	private int maxFrame = 1;
	private BufferedImage sprite;
	private String name;
	private String ID;
	private String type;
	private String attributes = "";
	
	public EditSprite(File file) {
		BufferedImage temp = ImageLoader.get().getSprite(file.getAbsolutePath());
		sprite = new BufferedImage(temp.getWidth(), temp.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = sprite.createGraphics();
		g.drawImage(temp, 0, 0, null);
		g.dispose();
		
		this.name = file.getName().replaceAll(".png", "");
		this.setID();
		loadSprite(name);
		sprite = sprite.getSubimage(0, 0, sprite.getWidth() / maxFrame, sprite.getHeight() / maxDir);
	}
	
	public EditSprite(Rectangle rect, String name) {
		this.sprite = new BufferedImage(rect.width, rect.height, BufferedImage.TYPE_INT_ARGB);
		this.name = name;
		this.setID();
		this.x = rect.x;
		this.y = rect.y;
	}
	
	public EditSprite(String name, int x, int y) {
		BufferedImage temp = ImageLoader.get().getSprite(name);
		sprite = new BufferedImage(temp.getWidth(), temp.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = sprite.createGraphics();
		g.drawImage(temp, 0, 0, null);
		g.dispose();
		
		this.name = name.substring(name.lastIndexOf('/') + 1, name.indexOf(".png"));
		this.setID();
		loadSprite(this.name);
		sprite = sprite.getSubimage(0, 0, sprite.getWidth() / maxFrame, sprite.getHeight() / maxDir);
		this.x = x;
		this.y = y;
	}
	
	public EditSprite(String name) {
		this.sprite = ImageLoader.get().getSprite(name);
		this.name = name.substring(name.lastIndexOf('/') + 1, name.indexOf(".png"));
		loadSprite(this.name);
	}

	public void loadSprite(String ref) {
		BufferedReader reader;
		
		try {
	    	reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/database/sprites"));
	
	    	String line;
	    	while((line = reader.readLine()) != null) {
	    		final String[] temp = line.split(";");
	    		
	    		if(temp[0].matches(ref)) {
	    			this.maxDir = Integer.parseInt(temp[1]);
	    			this.maxFrame = Integer.parseInt(temp[2]);
	    		}
	    	}
		} catch (FileNotFoundException e) {
	    	System.out.println("The sprite database has been misplaced!");
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	System.out.println("Sprite database failed to load!");
	    	e.printStackTrace();
	    }
	}
	
	public void loadAttributes(BufferedReader reader) {
		try {
			String line;
			while((line = reader.readLine()) != null && !line.matches("<END>")) {
				this.attributes += line + "\n";
			}
			if(attributes.endsWith("\n"))
				attributes = attributes.substring(0, attributes.length() - 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g, RescaleOp rop) {
		g.drawImage(sprite, rop, x, y);
	}
	
	public boolean contains(Point point) {
		if((point.x > x && point.x < x + sprite.getWidth()) && (point.y > y && point.y < y + sprite.getHeight()))
			return true;
		
		return false;
	}
	
	public EditSprite getClone() {
		EditSprite clone = null;
		try {
			clone = (EditSprite) this.clone();
			clone.setID();
		}  catch (CloneNotSupportedException e) {
			System.out.println("Cloning is illegal! And occasionally unethical!");
		}
		
		return clone;
	}
	
	public BufferedImage getSprite() {
		return sprite;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getID() {
		return ID;
	}
	
	public void setID(String ID) {
		this.ID = ID;
	}
	
	public void setID() {
		int x = 0;
		ID = name + "-" + x;
		while(Editor.IDList.contains(ID)) {
			x++;
			ID = name + "-" + x;
		}
		Editor.IDList.add(this.ID);
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public int getMaxDir() {
		return maxDir;
	}
	
	public int getMaxFrame() {
		return maxFrame;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getAttributes() {
		return attributes;
	}
	
	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}
	
	public String toString() {
		return type + "-" + ID;
	}
	
}
