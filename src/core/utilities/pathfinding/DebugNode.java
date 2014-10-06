package core.utilities.pathfinding;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import core.Theater;

public class DebugNode {

	private ArrayList<Point2D> nodes = new ArrayList<Point2D>();
	
	public DebugNode() {
	}
	
	public void draw() {
		for(int x = 0; x<nodes.size() - 1; x++) {
			GL11.glPushMatrix();
			GL11.glTranslated(nodes.get(x).getX() - Theater.get().getScreen().camera.getX(), nodes.get(x).getY() - Theater.get().getScreen().camera.getY(), 0);
			GL11.glColor3d(0, 1.0, 1.0);
			GL11.glLineWidth(1.0f);
			
			GL11.glBegin(GL11.GL_LINES);
			{
				GL11.glVertex2d(0, 0);
				GL11.glVertex2d(nodes.get(x + 1).getX() - nodes.get(x).getX(), nodes.get(x + 1).getY() - nodes.get(x).getY());
			}
			GL11.glEnd();
			GL11.glPopMatrix();
		}
	}
	
	public ArrayList<Point2D> getNodes() {
		return nodes;
	}
	
	public void addNode(Point2D node) {
		this.nodes.add(node);
	}
	
	public void showDistances() {
		for(int x = 0; x<nodes.size() - 1; x++) {
			System.out.println((float) Math.sqrt(Math.pow(nodes.get(x).getX() - nodes.get(x + 1).getX(), 2) + Math.pow(nodes.get(x).getY() - nodes.get(x + 1).getY(), 2)));
		}
	}
	
}
