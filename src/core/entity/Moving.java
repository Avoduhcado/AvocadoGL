package core.entity;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import core.Theater;
import core.scene.PropQuad;
import core.scene.Stage;
import core.utilities.pathfinding.CourseNode;

public abstract class Moving extends Entity {

	// TODO Different terrain tags, make a custom class for them
	
	protected float speed;
	protected float speedMod = 1f;
	protected float dx, dy;
	protected float distance;
	protected boolean floating;
	protected ArrayList<CourseNode> course = new ArrayList<CourseNode>();
	
	public Moving(String ref, float x, float y, boolean solid) {
		super(ref, x, y, solid);
	}
	
	public abstract void update(Stage stage);
		
	public void move() {
		if(Theater.get().getScreen().getFocus() == this) {
			Theater.get().getScreen().follow();
		}

		if(Math.abs(dx) > Math.abs(dy)) {
			if(dx < 0)
				setDir(2);
			else if(dx > 0)
				setDir(1);
		} else {
			if(dy < 0)
				setDir(3);
			else if(dy > 0)
				setDir(0);
		}
		
		x += dx;
		y += dy;
				
		PropQuad.get().update(this);
		setActing(true);
		updateBox();
	}
	
	public void checkMove(Stage stage) {
		Rectangle2D tempBox = new Rectangle2D.Double(getBox().getX(), getBox().getY(), getBox().getWidth(), getBox().getHeight());
		boolean brushingPast = false;
		ArrayList<Rectangle2D> rects;
		ArrayList<Rectangle2D> flatRects;
		ArrayList<Entity> props;
		float tempDX = 0f;
		float tempDY = 0f;
				
		float stepX = 0f;
		if(dx != 0) {
			tempDX = dx;
			if(Theater.getDeltaSpeed(getFullSpeed()) > 1f)
				stepX = 1f * dx;
			else
				stepX = Theater.getDeltaSpeed(getFullSpeed()) * dx;
			dx = 0f;
		}
		float xSteps = stepX;
		
		float stepY = 0f;
		if(dy != 0) {
			tempDY = dy;
			if(Theater.getDeltaSpeed(getFullSpeed()) > 1f)
				stepY = 1f * dy;
			else
				stepY = Theater.getDeltaSpeed(getFullSpeed()) * dy;
			dy = 0f;
		}
		float ySteps = stepY;
		
		while((Math.abs(xSteps) <= Math.abs(Theater.getDeltaSpeed(getFullSpeed()) * tempDX) && xSteps != 0f) ||
				(Math.abs(ySteps) <= Math.abs(Theater.getDeltaSpeed(getFullSpeed()) * tempDY) && ySteps != 0f)) {
			if(Math.abs(xSteps) <= Math.abs(Theater.getDeltaSpeed(getFullSpeed()) * tempDX) && xSteps != 0f) {
				dx += stepX;
				
				tempBox = new Rectangle2D.Double(getBox().getX() + xSteps, getBox().getY(), getBox().getWidth(), getBox().getHeight());
				rects = PropQuad.get().getRects(tempBox);
				flatRects = PropQuad.get().getFlatRects(tempBox);
				props = PropQuad.get().getProps(tempBox);
				
				if(PropQuad.get().getTopQuad().contains(tempBox)) {
					if(rects != null) {
						for(int x = 0; x<rects.size(); x++) {
							if(tempBox.intersects(rects.get(x))) {
								brushingPast = false;
								xSteps = 0f;
								dx -= stepX;
								break;
							}
						}
					}
					// Add in terrain specific checks better
					if(!floating && flatRects != null) {
						for(int x = 0; x<flatRects.size(); x++) {
							if(tempBox.intersects(flatRects.get(x))) {
								brushingPast = false;
								xSteps = 0f;
								dx -= stepX;
								break;
							}
						}
					}
					if(xSteps != 0f && props != null) {
						for(int x = 0; x<props.size(); x++) {
							if(tempBox.intersects(props.get(x).getBox()) && props.get(x) != this && !props.get(x).isFlat()) {
								brushingPast = true;
								break;
							}
						}
					}
				} else {
					dx -= stepX;
					break;
				}
				
				if(xSteps != 0f)
					xSteps += stepX;
			}
			
			if(Math.abs(ySteps) <= Math.abs(Theater.getDeltaSpeed(getFullSpeed()) * tempDY) && ySteps != 0f) {
				dy += stepY;
				
				tempBox = new Rectangle2D.Double(getBox().getX(), getBox().getY() + ySteps, getBox().getWidth(), getBox().getHeight());
				rects = PropQuad.get().getRects(tempBox);
				flatRects = PropQuad.get().getFlatRects(tempBox);
				props = PropQuad.get().getProps(tempBox);
				
				if(PropQuad.get().getTopQuad().contains(tempBox)) {
					if(rects != null) {
						for(int x = 0; x<rects.size(); x++) {
							if(tempBox.intersects(rects.get(x))) {
								brushingPast = false;
								ySteps = 0f;
								dy -= stepY;
								break;
							}
						}
					}
					// TODO Add in terrain specific checks better
					if(!floating && flatRects != null) {
						for(int x = 0; x<flatRects.size(); x++) {
							if(tempBox.intersects(flatRects.get(x))) {
								brushingPast = false;
								ySteps = 0f;
								dy -= stepY;
								break;
							}
						}
					}
					if(ySteps != 0f && props != null) {
						for(int x = 0; x<props.size(); x++) {
							if(tempBox.intersects(props.get(x).getBox()) && props.get(x) != this && !props.get(x).isFlat()) {
								brushingPast = true;
								break;
							}
						}
					}
				} else {
					dy -= stepY;
					break;
				}
				
				if(ySteps != 0f)
					ySteps += stepY;
			}
		}
		
		if(brushingPast) {
			dx = dx / 2f;
			dy = dy / 2f;
		}
		
		if(dx != 0f && dy != 0f) {
			dx = dx / (float) Math.sqrt(2);
			dy = dy / (float) Math.sqrt(2);
		}
	}
	
	public abstract void dodge(float distance, float cooldown, float dx, float dy);
	
	public void followCourse(Stage stage) {
		if(course.get(0).getDistance() <= 0) {
			course.remove(0);
		} else {
			dx = course.get(0).getDx();
			dy = course.get(0).getDy();
			checkMove(stage);
			if(dx != 0 || dy != 0) {
				if((float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)) > course.get(0).getDistance()) {
					if(dx != 0)
						dx = course.get(0).getDistance() * course.get(0).getDx();
					if(dy != 0)
						dy = course.get(0).getDistance() * course.get(0).getDy();
				}
				course.get(0).reduceDistance((float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)));
				this.move();
			} else {
				course.clear();
			}
		}
	}
	
	public float getDx() {
		return dx;
	}
	
	public void setDx(float dx) {
		this.dx = dx;
	}
	
	public float getDy() {
		return dy;
	}
	
	public void setDy(float dy) {
		this.dy = dy;
	}

	public float getSpeed() {
		return speed;
	}
	
	public void setSpeedMod(float speedMod) {
		this.speedMod = speedMod;
	}

	public float getFullSpeed() {
		//return speed + speedBuff;
		return speed * speedMod;
	}
	
	public void setSpeed(float speed) {
		//Not sure what I was going for with this
		this.speed = speed / (25.0f / Theater.deltaMax);
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public boolean isFloating() {
		return floating;
	}
	
	public void setFloating(boolean floating) {
		this.floating = floating;
	}
	
	public ArrayList<CourseNode> getCourse() {
		return course;
	}
	
	public void setCourse(ArrayList<CourseNode> course) {
		this.course = course;
	}
	
}
