package core.scene;

import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.IOException;

import core.entity.Actor;

public class Pathway {

	private Rectangle2D loc;
	private String destination;
	private float x;
	private float y;
	private float destX;
	private float destY;
	
	public Pathway(Rectangle2D rect, String dest, float dX, float dY) {
		x = (float) rect.getX();
		y = (float) rect.getY();
		loc = rect;
		destination = dest;
		destX = dX;
		destY = dY;
	}

	public static Pathway loadPathway(BufferedReader reader) {
		String line;
		try {
			Pathway path = null;
			while((line = reader.readLine()) != null && !line.matches("<END>")) {
				String[] temp = line.split(";");
				path = new Pathway(new Rectangle2D.Double(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), 
	    					Integer.parseInt(temp[2]), Integer.parseInt(temp[3])), temp[4], Integer.parseInt(temp[5]), Integer.parseInt(temp[6]));
			}
			
			//reader.close();
			return path;
		} catch (IOException e) {
			System.out.println("Pathway failed to load");
		}
		
		return null;
	}
	
	public boolean collide(Actor player) {
		if(loc.contains(player.getBox().getCenterX(), player.getBox().getCenterY())) {
			return true;
		}
		
		return false;
	}
	
	public int getX() {
		return (int) loc.getX();
	}
	
	public int getY() {
		return (int) loc.getY();
	}
	
	public String getDestination() {
		return destination;
	}
	
	public float getDestX() {
		return destX;
	}
	
	public float getDestY() {
		return destY;
	}
	
	public String save() {
		String data = "";
		data += (int)x + ";" + (int)y + ";" + loc.getWidth() + ";" + loc.getHeight();
		data += ";" + destination;
		data += ";" + destX + ";" + destY;
		return data;
	}

}
