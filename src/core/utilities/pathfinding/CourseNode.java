package core.utilities.pathfinding;

public class CourseNode {

	private float distance;
	private float dx;
	private float dy;
	
	public CourseNode(float distance, float dx, float dy) {
		this.setDistance(distance);
		this.dx = dx;
		this.dy = dy;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}
	
	public void addDistance(float distance) {
		this.distance += distance;
	}
	
	public void reduceDistance(float distance) {
		setDistance(getDistance() - distance);
	}
	
	public float getDx() {
		return dx;
	}
	
	public float getDy() {
		return dy;
	}
	
	public void setAngles(float dx, float dy) {
		this.dx = dx;
		this.dy = dy;
	}

}
