package core.utilities.pathfinding;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import core.Theater;
import core.entity.Moving;
import core.scene.PropQuad;

public class PathQuadStar {

	private ArrayList<PathQuad> openList = new ArrayList<PathQuad>();
	private ArrayList<PathQuad> closedList = new ArrayList<PathQuad>();
	private ArrayList<CourseNode> course = new ArrayList<CourseNode>();
	private PathQuad target;
	private Rectangle2D rect;
	
	public static PathQuadStar pathQuadStar = new PathQuadStar();
	
	public static PathQuadStar get() {
		return pathQuadStar;
	}
	
	public ArrayList<CourseNode> findPath(Moving source, float targetX, float targetY) {
		target = PathQuad.get().findContainer(targetX, targetY);
		openList.clear();
		closedList.clear();
		course = new ArrayList<CourseNode>();
		rect = source.getBox();
		PathQuad.get().clearPathParents();
		
		if(target != null && !target.isBlocked() && PathQuad.get().findContainer((float)rect.getX(), (float)rect.getY()) != null 
				&& !PathQuad.get().findContainer((float)rect.getX(), (float)rect.getY()).isBlocked()) {
			PathQuad a = PathQuad.get().findContainer((float)rect.getX(), (float)rect.getY());
			//a.highlight = true;
			openList.add(a);
			//getSurroundings(a);
			getNodes(a);
			while(!closedList.contains(target) && !openList.isEmpty()) {
				a = getLowestF();
				//getSurroundings(a);
				getNodes(a);
				//if(closedList.contains(target))
					//closedList.get(closedList.size()-1).highlight = true;
			}
			/*for(int x = 0; x<closedList.size(); x++) {
				closedList.get(x).highlight = true;
			}*/
			//System.out.println(closedList.size() + " " + openList.size());
			buildCourse(targetX, targetY);
		}
						
		return course;
	}
	
	/**
	 * Builds the course to be followed starting from the target to the entity's location.
	 * @param targetX X coordinate of target
	 * @param targetY Y coordinate of target
	 */
	public void buildCourse(float targetX, float targetY) {
		Theater.get().getStage().debugNodes = new DebugNode();
		Theater.get().getStage().debugNodes.addNode(new Point2D.Double(targetX, targetY));
		
		PathQuad temp = getLowestClosedF();
		if(closedList.contains(target))
			temp = closedList.get(closedList.indexOf(target));
		
		// If source is already in same box as target
		if(temp.getBox().contains(rect.getX(), rect.getY())) {
			Theater.get().getStage().debugNodes.addNode(new Point2D.Double(rect.getX(), rect.getY()));
			setNode(new Point2D.Double(targetX, targetY), new Point2D.Double(rect.getX(), rect.getY()));
			return;
		} else if(temp.getPathParent().getBox().contains(rect.getX(), rect.getY())) {
			// If next pathquad contains target, end early and navigate straight to goal
			Polygon path = buildDirectPath(new Rectangle2D.Double(targetX, targetY, rect.getWidth(), rect.getHeight()), rect);
			for(Rectangle2D rects : PropQuad.get().getRects(path)) {
				if(path.intersects(rects)) {
					path = null;
					break;
				}
			}
			if(path != null) {
				Theater.get().getStage().debugNodes.addNode(new Point2D.Double(rect.getX(), rect.getY()));
				setNode(new Point2D.Double(targetX, targetY), new Point2D.Double(rect.getX(), rect.getY()));
				return;
			} else {
				path = buildDirectPath(new Rectangle2D.Double(targetX, targetY, rect.getWidth(), rect.getHeight()), 
						new Rectangle2D.Double(temp.getBox().getX(), temp.getBox().getY(), rect.getWidth(), rect.getHeight()));
				for(Rectangle2D rects : PropQuad.get().getRects(path)) {
					if(path.intersects(rects)) {
						path = null;
						break;
					}
				}
				if(path != null) {
					Theater.get().getStage().debugNodes.addNode(new Point2D.Double(temp.getBox().getX(), temp.getBox().getY()));
					setNode(new Point2D.Double(targetX, targetY), new Point2D.Double(temp.getBox().getX(), temp.getBox().getY()));
					
					Theater.get().getStage().debugNodes.addNode(new Point2D.Double(rect.getX(), rect.getY()));
					setNode(new Point2D.Double(temp.getBox().getX(), temp.getBox().getY()), new Point2D.Double(rect.getX(), rect.getY()));
					return;
				}
			}
		} else {
			// Last ditch effort for a simple path
			Polygon path = buildDirectPath(new Rectangle2D.Double(targetX, targetY, rect.getWidth(), rect.getHeight()), rect);
			for(Rectangle2D rects : PropQuad.get().getRects(path)) {
				if(path.intersects(rects)) {
					path = null;
					break;
				}
			}
			if(path != null) {
				Theater.get().getStage().debugNodes.addNode(new Point2D.Double(rect.getX(), rect.getY()));
				setNode(new Point2D.Double(targetX, targetY), new Point2D.Double(rect.getX(), rect.getY()));
				return;
			}
		}
			
		Theater.get().getStage().debugNodes.addNode(new Point2D.Double(temp.getBox().getX(), temp.getBox().getY()));
		setNode(new Point2D.Double(targetX, targetY), new Point2D.Double(temp.getBox().getX(), temp.getBox().getY()));
		
		while(temp.getPathParent() != null) {
			// If there's a direct path from the start to a further node
			Polygon path = buildDirectPath(new Rectangle2D.Double(temp.getBox().getX(), temp.getBox().getY(), rect.getWidth(), rect.getHeight()), rect);
			for(Rectangle2D rects : PropQuad.get().getRects(path)) {
				if(path.intersects(rects)) {
					path = null;
					break;
				}
			}
			if(path != null) {
				Theater.get().getStage().debugNodes.addNode(new Point2D.Double(rect.getX(), rect.getY()));
				setNode(new Point2D.Double(temp.getBox().getX(), temp.getBox().getY()), new Point2D.Double(rect.getX(), rect.getY()));
				return;
			}
			
			setNode(new Point2D.Double(temp.getBox().getX(), temp.getBox().getY()), new Point2D.Double(temp.getPathParent().getBox().getX(), temp.getPathParent().getBox().getY()));

			// Avoid infinite loop that probably shouldn't happen
			if(temp.getPathParent().getPathParent() != null) {
				if(temp == temp.getPathParent().getPathParent())
					break;
			}
			
			// Set temp to next pathquad in the chain
			temp = temp.getPathParent();
			
			// Check if the path can be directed to the goal at any time
			path = buildDirectPath(new Rectangle2D.Double(targetX, targetY, rect.getWidth(), rect.getHeight()), 
					new Rectangle2D.Double(temp.getBox().getX(), temp.getBox().getY(), rect.getWidth(), rect.getHeight()));
			for(Rectangle2D rects : PropQuad.get().getRects(path)) {
				if(path.intersects(rects)) {
					path = null;
					break;
				}
			}
			if(path != null) {
				// Clear up previous path for new direct route
				course.clear();
				Theater.get().getStage().debugNodes.getNodes().clear();
				Theater.get().getStage().debugNodes.addNode(new Point2D.Double(targetX, targetY));
				Theater.get().getStage().debugNodes.addNode(new Point2D.Double(temp.getBox().getX(), temp.getBox().getY()));
				setNode(new Point2D.Double(targetX, targetY), new Point2D.Double(temp.getBox().getX(), temp.getBox().getY()));
			}
			
			Theater.get().getStage().debugNodes.addNode(new Point2D.Double(temp.getBox().getX(), temp.getBox().getY()));
		}

		Theater.get().getStage().debugNodes.addNode(new Point2D.Double(rect.getX(), rect.getY()));
		setNode(new Point2D.Double(temp.getBox().getX(), temp.getBox().getY()), new Point2D.Double(rect.getX(), rect.getY()));
	}

	public void setNode(Point2D destination, Point2D start) {
		if(destination.getX() == 0)
			destination = new Point2D.Double(1, destination.getY());
		if(destination.getY() == 0)
			destination = new Point2D.Double(destination.getX(), 1);
		if(start.getX() == 0)
			start = new Point2D.Double(1, start.getY());
		if(start.getY() == 0)
			start = new Point2D.Double(start.getX(), 1);
		
		float theta = (float) Math.atan2(destination.getX() - start.getX(), destination.getY() - start.getY());
		if(destination.getX() == start.getX()) {
			course.add(0, new CourseNode((float) Math.sqrt(Math.pow(start.getX() - destination.getX(), 2) + Math.pow(start.getY() - destination.getY(), 2)),
					0, destination.getY() > start.getY() ? 1 : -1));
		} else if(destination.getY() == start.getY()) {
			course.add(0, new CourseNode((float) Math.sqrt(Math.pow(start.getX() - destination.getX(), 2) + Math.pow(start.getY() - destination.getY(), 2)), 
					destination.getX() > start.getX() ? 1 : -1, 0));
		} else {
			course.add(0, new CourseNode((float) Math.sqrt(Math.pow(start.getX() - destination.getX(), 2) + Math.pow(start.getY() - destination.getY(), 2)),
					(float) Math.sin(theta), (float) Math.cos(theta)));
		}
		
		// Merge any paths going the same direction
		if(course.size() > 1) {
			if(course.get(0).getDx() == course.get(1).getDx() && course.get(0).getDy() == course.get(1).getDy()) {
				course.get(0).addDistance(course.get(1).getDistance());
				course.remove(1);
			}
		}
	}
	
	public void getNodes(PathQuad a) {
		openList.remove(a);
		closedList.add(a);
		Polygon path;
		
		for(int x = 0; x<a.getRight().size(); x++) {	// Sort through pathquads to the right
			if(a.getRight().get(x) != null && !closedList.contains(a.getRight().get(x))) {		// If there are pathquads and they are not already checked
				path = buildPathPoly(a, a.getRight().get(x));
				for(Rectangle2D rects : PropQuad.get().getRects(path)) {
					if(path.intersects(rects)) {
						path = null;
						break;
					}
				}
				if(path != null) {
					if(!openList.contains(a.getRight().get(x))) {	
						a.getRight().get(x).setUpPath(a, 10 + a.getG(), target);
						openList.add(a.getRight().get(x));
					} else if(a.getRight().get(x).getG() > 10 + a.getG()) {
						a.getRight().get(x).setUpPath(a, 10 + a.getG(), target);
					}
				}
			}
		}
		
		for(int x = 0; x<a.getDown().size(); x++) {
			if(a.getDown().get(x) != null && !closedList.contains(a.getDown().get(x))) {
				path = buildPathPoly(a, a.getDown().get(x));
				for(Rectangle2D rects : PropQuad.get().getRects(path)) {
					if(path.intersects(rects)) {
						path = null;
						break;
					}
				}
				if(path != null) {
					if(!openList.contains(a.getDown().get(x))) {
						a.getDown().get(x).setUpPath(a, 10 + a.getG(), target);
						openList.add(a.getDown().get(x));
					} else if(a.getDown().get(x).getG() > 10 + a.getG()) {
						a.getDown().get(x).setUpPath(a, 10 + a.getG(), target);
					}
				}
			}
		}
		
		for(int x = 0; x<a.getLeft().size(); x++) {
			if(a.getLeft().get(x) != null && !closedList.contains(a.getLeft().get(x))) {
				path = buildPathPoly(a, a.getLeft().get(x));
				for(Rectangle2D rects : PropQuad.get().getRects(path)) {
					if(path.intersects(rects)) {
						path = null;
						break;
					}
				}
				if(path != null) {
					if(!openList.contains(a.getLeft().get(x))) {
						a.getLeft().get(x).setUpPath(a, 10 + a.getG(), target);
						openList.add(a.getLeft().get(x));
					} else if(a.getLeft().get(x).getG() > 10 + a.getG()) {
						a.getLeft().get(x).setUpPath(a, 10 + a.getG(), target);
					}
				}
			}
		}
			
		for(int x = 0; x<a.getUp().size(); x++) {
			if(a.getUp().get(x) != null && !closedList.contains(a.getUp().get(x))) {
				path = buildPathPoly(a, a.getUp().get(x));
				for(Rectangle2D rects : PropQuad.get().getRects(path)) {
					if(path.intersects(rects)) {
						// TODO This is bad, I hope no one notices...
						Polygon path2 = buildDirectPath(new Rectangle2D.Double(a.getBox().getX(), a.getBox().getY(), rect.getWidth(), rect.getHeight()), 
								new Rectangle2D.Double(a.getUp().get(x).getBox().getCenterX(), a.getUp().get(x).getBox().getCenterY(), rect.getWidth(), rect.getHeight()));
						if(path2.intersects(rects)) {
							path = null;
							break;
						}
					}
				}
				if(path != null) {
					if(!openList.contains(a.getUp().get(x))) {
						a.getUp().get(x).setUpPath(a, 10 + a.getG(), target);
						openList.add(a.getUp().get(x));
					} else if(a.getUp().get(x).getG() > 10 + a.getG()) {
						a.getUp().get(x).setUpPath(a, 10 + a.getG(), target);
					}
				}
			}
		}
	}
	
	public Polygon buildPathPoly(PathQuad a, PathQuad target) {
		Polygon p = new Polygon();
		
		if(target.getBox().getX() >= a.getBox().getX() && target.getBox().getY() <= a.getBox().getY()) {
			p.addPoint((int) a.getBox().getX(), (int) a.getBox().getY());		// 1
			p.addPoint((int) target.getBox().getX(), (int) target.getBox().getY());		// 2
			p.addPoint((int) (target.getBox().getX() + rect.getWidth()), (int) target.getBox().getY());		// 3
			p.addPoint((int) (target.getBox().getX() + rect.getWidth()), (int) (target.getBox().getY() + rect.getHeight()));	// 4
			p.addPoint((int) (a.getBox().getX() + rect.getWidth()), (int) (a.getBox().getY() + rect.getHeight()));		// 5
			p.addPoint((int) a.getBox().getX(), (int) (a.getBox().getY() + rect.getHeight()));		// 6
		} else if(target.getBox().getX() < a.getBox().getX() && target.getBox().getY() <= a.getBox().getY()) {
			p.addPoint((int) target.getBox().getX(), (int) target.getBox().getY());		// 2
			p.addPoint((int) (target.getBox().getX() + rect.getWidth()), (int) target.getBox().getY());		// 3
			p.addPoint((int) (a.getBox().getX() + rect.getWidth()), (int) a.getBox().getY());		// 7
			p.addPoint((int) (a.getBox().getX() + rect.getWidth()), (int) (a.getBox().getY() + rect.getHeight()));		// 5
			p.addPoint((int) a.getBox().getX(), (int) (a.getBox().getY() + rect.getHeight()));		// 6
			p.addPoint((int) target.getBox().getX(), (int) (target.getBox().getY() + rect.getHeight()));	// 8
		} else if(target.getBox().getX() >= a.getBox().getX() && target.getBox().getY() > a.getBox().getY()) {
			p.addPoint((int) a.getBox().getX(), (int) a.getBox().getY());		// 1
			p.addPoint((int) (a.getBox().getX() + rect.getWidth()), (int) a.getBox().getY());		// 7
			p.addPoint((int) (target.getBox().getX() + rect.getWidth()), (int) target.getBox().getY());		// 3
			p.addPoint((int) (target.getBox().getX() + rect.getWidth()), (int) (target.getBox().getY() + rect.getHeight()));	// 4
			p.addPoint((int) target.getBox().getX(), (int) (target.getBox().getY() + rect.getHeight()));	// 8
			p.addPoint((int) a.getBox().getX(), (int) (a.getBox().getY() + rect.getHeight()));		// 6
		} else if(target.getBox().getX() < a.getBox().getX() && target.getBox().getY() > a.getBox().getY()) {
			p.addPoint((int) a.getBox().getX(), (int) a.getBox().getY());		// 1
			p.addPoint((int) (a.getBox().getX() + rect.getWidth()), (int) a.getBox().getY());		// 7
			p.addPoint((int) (a.getBox().getX() + rect.getWidth()), (int) (a.getBox().getY() + rect.getHeight()));		// 5
			p.addPoint((int) (target.getBox().getX() + rect.getWidth()), (int) (target.getBox().getY() + rect.getHeight()));	// 4
			p.addPoint((int) target.getBox().getX(), (int) (target.getBox().getY() + rect.getHeight()));	// 8
			p.addPoint((int) target.getBox().getX(), (int) target.getBox().getY());		// 2
		}
					
		return p;
	}
	
	public Polygon buildDirectPath(Rectangle2D target, Rectangle2D source) {
		Polygon p = new Polygon();
		
		if(target.getX() >= source.getX() && target.getY() <= source.getY()) {
			p.addPoint((int) source.getX(), (int) source.getY());		// 1
			p.addPoint((int) target.getX(), (int) target.getY());		// 2
			p.addPoint((int) target.getMaxX(), (int) target.getY());	// 3
			p.addPoint((int) target.getMaxX(), (int) target.getMaxY());	// 4
			p.addPoint((int) source.getMaxX(), (int) source.getMaxY());	// 5
			p.addPoint((int) source.getX(), (int) source.getMaxY());	// 6
		} else if(target.getX() < source.getX() && target.getY() <= source.getY()) {
			p.addPoint((int) target.getX(), (int) target.getY());		// 2
			p.addPoint((int) target.getMaxX(), (int) target.getY());	// 3
			p.addPoint((int) source.getMaxX(), (int) source.getY());	// 7
			p.addPoint((int) source.getMaxX(), (int) source.getMaxY());	// 5
			p.addPoint((int) source.getX(), (int) source.getMaxY());	// 6
			p.addPoint((int) target.getX(), (int) target.getMaxY());	// 8
		} else if(target.getX() >= source.getX() && target.getY() > source.getY()) {
			p.addPoint((int) source.getX(), (int) source.getY());		// 1
			p.addPoint((int) source.getMaxX(), (int) source.getY());	// 7
			p.addPoint((int) target.getMaxX(), (int) target.getY());	// 3
			p.addPoint((int) target.getMaxX(), (int) target.getMaxY());	// 4
			p.addPoint((int) target.getX(), (int) target.getMaxY());	// 8
			p.addPoint((int) source.getX(), (int) source.getMaxY());	// 6
		} else if(target.getX() < source.getX() && target.getY() > source.getY()) {
			p.addPoint((int) source.getX(), (int) source.getY());		// 1
			p.addPoint((int) source.getMaxX(), (int) source.getY());	// 7
			p.addPoint((int) source.getMaxX(), (int) source.getMaxY());	// 5
			p.addPoint((int) target.getMaxX(), (int) target.getMaxY());	// 4
			p.addPoint((int) target.getX(), (int) target.getMaxY());	// 8
			p.addPoint((int) target.getX(), (int) target.getY());		// 2
		}
					
		return p;
	}
	
	public PathQuad getLowestF() {
		int hold = 0;
		if(openList.size() > 1) {
			for(int x = 1; x<openList.size(); x++) {
				//System.out.println(openList.get(x).getF());
				if(openList.get(x).getF() < openList.get(hold).getF()) {
					hold = x;
				}
			}
		}
		
		return openList.get(hold);
	}
	
	public PathQuad getLowestClosedF() {
		int hold = 0;
		if(closedList.size() > 1) {
			for(int x = 1; x<closedList.size(); x++) {
				//System.out.println(closedList.get(x).getF() + " " + hold);
				if(closedList.get(x).getF() < closedList.get(hold).getF() || closedList.get(hold).getF() == 0) {
					hold = x;
				}
			}
		}

		return closedList.get(hold);
	}
	
}
