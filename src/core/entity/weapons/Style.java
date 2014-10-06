package core.entity.weapons;

import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class Style {

	private float swingTime;
	private ArrayList<Float> boxFrame = new ArrayList<Float>();
	private ArrayList<Rectangle2D> hitbox = new ArrayList<Rectangle2D>();
	
	public Style(float swingTime) {
		this.swingTime = swingTime;
	}
	
	public static Style loadStyle(BufferedReader reader) {
		try {
			String line = reader.readLine();
			String[] temp = line.split(";");
			Style style = new Style(Float.parseFloat(temp[0]));
			for(int x = 1; x<temp.length; x++) {
				style.boxFrame.add(Float.parseFloat(temp[x]));
			}
			while((line = reader.readLine()) != null && !line.matches("<BREAK>")) {
				temp = line.split(";");
				for(int x = 0; x<temp.length; x++) {
					style.hitbox.add(new Rectangle2D.Float(Float.parseFloat(temp[x]), Float.parseFloat(temp[x+=1]), Float.parseFloat(temp[x+=1]), Float.parseFloat(temp[x+=1])));
				}
			}
			
			return style;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public float getSwingTime() {
		return swingTime;
	}
	
	public ArrayList<Float> getBoxFrame() {
		return boxFrame;
	}
	
	public float getBoxFrame(int x) {
		return boxFrame.get(x);
	}
	
	public float getCurrentBoxFrame(int x) {
		float total = 0;
		for(int a = 0; a<=x; a++) {
			total += boxFrame.get(a);
		}
		
		return total;
	}
	
	public ArrayList<Rectangle2D> getHitbox() {
		return hitbox;
	}
	
	public Rectangle2D getHitbox(int x) {
		return hitbox.get(x);
	}
	
}
