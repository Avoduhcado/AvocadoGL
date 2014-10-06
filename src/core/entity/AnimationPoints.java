package core.entity;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class AnimationPoints {

	private Point2D[][] points;
	
	public AnimationPoints(int directions, int frames, String points) {
		this.points = new Point2D[directions][frames];
		
		ArrayList<Point2D> temp = new ArrayList<Point2D>();
		String[] line = points.split(";");
		for(int x = 0; x<line.length; x++) {
			String[] tempPoint = line[x].split(",");
			temp.add(new Point2D.Float(Float.parseFloat(tempPoint[0]), Float.parseFloat(tempPoint[1])));
		}
		
		for(int x = 0; x<directions; x++) {
			for(int y = 0; y<frames; y++) {
				this.points[x][y] = temp.get(y + (x * directions));
			}
		}
	}
	
	public Point2D[][] getPoints() {
		return points;
	}
	
	public Point2D getPoint(int direction, int frame) {
		return points[direction][frame];
	}
	
}
