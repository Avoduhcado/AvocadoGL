package core.scene;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import core.Theater;
import core.entity.Actor;
import core.entity.Entity;

public class PropQuad {
	
	public static int totalChildren;
	
	private PropQuad parent;
	private PropQuad[] propChildren;
	private ArrayList<Entity> props = new ArrayList<Entity>();
	private ArrayList<Rectangle2D> rects = new ArrayList<Rectangle2D>();
	private ArrayList<Rectangle2D> flatRects = new ArrayList<Rectangle2D>();
	private Rectangle2D container;
	private int size = 15;
	
	private static PropQuad quad;
	
	public static PropQuad get() {
		return quad;
	}
	
	public static void init(PropQuad propQuad) {
		quad = propQuad;
	}
	
	public static void clear() {
		quad = null;
	}
	
	public PropQuad(PropQuad parent, Rectangle2D container, ArrayList<Entity> props, ArrayList<Rectangle2D> rects, ArrayList<Rectangle2D> flatRects) {
		this.parent = parent;
		this.container = container;
		for(int x = 0; x<props.size(); x++) {
			if(container.intersects(props.get(x).getBox())) {
				this.props.add(props.get(x));
			}
		}
		for(int x = 0; x<rects.size(); x++) {
			if(container.intersects(rects.get(x))) {
				this.rects.add(rects.get(x));
			}
		}
		for(int x = 0; x<flatRects.size(); x++) {
			if(container.intersects(flatRects.get(x))) {
				this.flatRects.add(flatRects.get(x));
			}
		}
		
		PropQuad.totalChildren += 1;
		
		if(this.props.size() + this.rects.size() > size && container.getWidth() > Theater.get().getScreen().camera.getWidth()/10 && noChildren()) {
			propChildren = new PropQuad[4];
			addChildren(0);
		}
	}
	
	public void update(Entity entity) {
		if(props.contains(entity) || container.intersects(entity.getBox())) {
			if(!container.intersects(entity.getBox())) {
				props.remove(entity);
			} else if(container.intersects(entity.getBox()) && !props.contains(entity)) {
				props.add(entity);
			}
			
			if(!noChildren()) {
				for(int x = 0; x<propChildren.length; x++) {
					propChildren[x].update(entity);
				}
			}
			
			adjustChildren();
		}
	}

	public void draw() {
		GL11.glPushMatrix();
		GL11.glTranslatef((float)(container.getX() - Theater.get().getScreen().camera.getX()), (float)(container.getY() - Theater.get().getScreen().camera.getY()), 0);
		GL11.glColor3f(0.75f, 0.5f, 1.0f);
		GL11.glLineWidth(1.0f);
		
		GL11.glBegin(GL11.GL_LINE_LOOP);
		{
			GL11.glVertex2d(0, 0);
			GL11.glVertex2d(container.getWidth(), 0);
			GL11.glVertex2d(container.getWidth(), container.getHeight());
			GL11.glVertex2d(0, container.getHeight());
		}
		GL11.glEnd();
		GL11.glPopMatrix();
	 
		if(!noChildren()) {
			for(int x = 0; x<propChildren.length; x++) {
				propChildren[x].draw();
			}
		}
	}

	public void addChildren(int x) {
		if(x >= 3) {
			propChildren[x] = new PropQuad(this, new Rectangle2D.Double(container.getX() + (container.getWidth()/2), container.getY() + (container.getHeight()/2),
					container.getWidth()/2, container.getHeight()/2), props, rects, flatRects);
		} else {
			if(x < 2) {
				propChildren[x] = new PropQuad(this, new Rectangle2D.Double(container.getX() + (x * (container.getWidth()/2)), container.getY(),
						container.getWidth()/2, container.getHeight()/2), props, rects, flatRects);
			} else {
				propChildren[x] = new PropQuad(this, new Rectangle2D.Double(container.getX(), container.getY() + (container.getHeight()/2),
						container.getWidth()/2, container.getHeight()/2), props, rects, flatRects);
			}
			addChildren(x+1);
		}
	}
	
	public void adjustChildren() {
		//Will crash if it exceeds around 2k objects for some reason and doesn't have size limit
		if(noChildren() && props.size() + rects.size() > size && container.getWidth() > 80) {
			propChildren = new PropQuad[4];
			addChildren(0);
		} else if(!noChildren() && props.size() + rects.size() <= size && parent != null) {
			propChildren = null;
			PropQuad.totalChildren -= 4;
		}
	}
	
	public Rectangle2D getTopQuad() {
		return container;
	}
	
	public ArrayList<Entity> getProps() {
		return props;
	}

	public ArrayList<Rectangle2D> getRects() {
		return rects;
	}
	
	public ArrayList<Entity> getProps(Entity entity) {
		if(noChildren()) {
			ArrayList<Entity> tempProps = new ArrayList<Entity>();
			for(int x = 0; x<props.size(); x++) {
				//if(!props.get(x).isSolid()) {
				tempProps.add(props.get(x));
				//}
			}
			return tempProps;
			//return props;
		} else {
			ArrayList<Entity> tempProps = new ArrayList<Entity>();
			for(int x = 0; x<propChildren.length; x++) {
				if(propChildren[x].props.contains(entity)) {
					tempProps.addAll(propChildren[x].getProps(entity));
				}
			}
			return tempProps;
		}
	}
	
	/**
	 * Recursively search through quadtree for rectangle.
	 * @param rectangle Rectangle2D to search for.
	 * @return All props contained in the same quadtree children as rectangle.
	 */
	public ArrayList<Entity> getProps(Rectangle2D rectangle) {
		if(noChildren()) {
			ArrayList<Entity> tempProps = new ArrayList<Entity>();
			for(int x = 0; x<props.size(); x++) {
				tempProps.add(props.get(x));
			}
			return tempProps;
		} else {
			ArrayList<Entity> tempProps = new ArrayList<Entity>();
			for(int x = 0; x<propChildren.length; x++) {
				if(propChildren[x].container.intersects(rectangle)) {
					tempProps.addAll(propChildren[x].getProps(rectangle));
				}
			}
			return tempProps;
		}
	}
	
	public ArrayList<Actor> getActors(Entity entity) {
		if(noChildren()) {
			ArrayList<Actor> tempActors = new ArrayList<Actor>();
			for(int x = 0; x<props.size(); x++) {
				if(props.get(x) instanceof Actor)
					tempActors.add((Actor) props.get(x));
			}
			return tempActors;
		} else {
			ArrayList<Actor> tempActors = new ArrayList<Actor>();
			for(int x = 0; x<propChildren.length; x++) {
				if(propChildren[x].props.contains(entity)) {
					tempActors.addAll(propChildren[x].getActors(entity));
				}
			}
			return tempActors;
		}
	}
	
	/**
	 * Recursively search through quadtree for actors inside polygon.
	 * @param shape Shape to search for.
	 * @return All actors in the same quadtree children as shape
	 */
	public ArrayList<Actor> getActors(RectangularShape shape) {
		if(noChildren()) {
			ArrayList<Actor> tempActors = new ArrayList<Actor>();
			for(int x = 0; x<props.size(); x++) {
				if(props.get(x) instanceof Actor)
					tempActors.add((Actor) props.get(x));
			}
			return tempActors;
		} else {
			ArrayList<Actor> tempActors = new ArrayList<Actor>();
			for(int x = 0; x<propChildren.length; x++) {
				if(shape.intersects(propChildren[x].container)) {
					tempActors.addAll(propChildren[x].getActors(shape));
				}
			}
			return tempActors;
		}
	}
	
	public ArrayList<Entity> getSolidProps(Entity entity) {
		if(noChildren()) {
			ArrayList<Entity> tempProps = new ArrayList<Entity>();
			for(int x = 0; x<props.size(); x++) {
				if(props.get(x).isSolid()) {
					tempProps.add(props.get(x));
				}
			}
			return tempProps;
			//return props;
		} else {
			ArrayList<Entity> tempProps = new ArrayList<Entity>();
			for(int x = 0; x<propChildren.length; x++) {
				if(propChildren[x].props.contains(entity)) {
					tempProps.addAll(propChildren[x].getSolidProps(entity));
				}
			}
			return tempProps;
		}
	}
	
	public ArrayList<Rectangle2D> getRects(Entity entity) {
		if(noChildren()) {
			ArrayList<Rectangle2D> tempRects = new ArrayList<Rectangle2D>();
			for(int x = 0; x<rects.size(); x++) {
				tempRects.add(rects.get(x));
			}
			return tempRects;
			//return props;
		} else {
			ArrayList<Rectangle2D> tempRects = new ArrayList<Rectangle2D>();
			for(int x = 0; x<propChildren.length; x++) {
				if(propChildren[x].props.contains(entity)) {
					tempRects.addAll(propChildren[x].getRects(entity));
				}
			}
			return tempRects;
		}
	}
	
	/**
	 * Recursively search through quadtree for rectangle.
	 * @param rectangle Rectangle2D to search for.
	 * @return All rectangles in the same quadtree children as rectangle
	 */
	public ArrayList<Rectangle2D> getRects(Rectangle2D rectangle) {
		if(noChildren()) {
			ArrayList<Rectangle2D> tempRects = new ArrayList<Rectangle2D>();
			for(int x = 0; x<rects.size(); x++) {
				tempRects.add(rects.get(x));
			}
			return tempRects;
		} else {
			ArrayList<Rectangle2D> tempRects = new ArrayList<Rectangle2D>();
			for(int x = 0; x<propChildren.length; x++) {
				if(propChildren[x].container.intersects(rectangle)) {
					tempRects.addAll(propChildren[x].getRects(rectangle));
				}
			}
			return tempRects;
		}
	}
	
	/**
	 * Recursively search through quadtree for rectangles inside polygon.
	 * @param shape Shape to search for.
	 * @return All rectangles in the same quadtree children as shape
	 */
	public ArrayList<Rectangle2D> getRects(Shape shape) {
		if(noChildren()) {
			ArrayList<Rectangle2D> tempRects = new ArrayList<Rectangle2D>();
			for(int x = 0; x<rects.size(); x++) {
				tempRects.add(rects.get(x));
			}
			return tempRects;
		} else {
			ArrayList<Rectangle2D> tempRects = new ArrayList<Rectangle2D>();
			for(int x = 0; x<propChildren.length; x++) {
				if(shape.intersects(propChildren[x].container)) {
					tempRects.addAll(propChildren[x].getRects(shape));
				}
			}
			return tempRects;
		}
	}
	
	/**
	 * Recursively search through quadtree for rectangle.
	 * @param rectangle Rectangle2D to search for.
	 * @return All flat rectangles in the same quadtree children as rectangle
	 */
	public ArrayList<Rectangle2D> getFlatRects(Rectangle2D rectangle) {
		if(noChildren()) {
			ArrayList<Rectangle2D> tempRects = new ArrayList<Rectangle2D>();
			for(int x = 0; x<flatRects.size(); x++) {
				tempRects.add(flatRects.get(x));
			}
			return tempRects;
		} else {
			ArrayList<Rectangle2D> tempRects = new ArrayList<Rectangle2D>();
			for(int x = 0; x<propChildren.length; x++) {
				if(propChildren[x].container.intersects(rectangle)) {
					tempRects.addAll(propChildren[x].getFlatRects(rectangle));
				}
			}
			return tempRects;
		}
	}

	public void removeProp(Entity entity) {
		if(props.contains(entity)) {
			props.remove(entity);
			
			if(!noChildren()) {
				for(int x = 0; x<propChildren.length; x++) {
					propChildren[x].removeProp(entity);
				}
			}
		}
	}

	public boolean noChildren() {
		if(propChildren == null)
			return true;
		else
			return false;
	}
	
}
