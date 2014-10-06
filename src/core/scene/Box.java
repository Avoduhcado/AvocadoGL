package core.scene;

public class Box {

	public float x;
	public float y;
	public int xOffset;
	public int yOffset;
	public float width;
	public float height;
	
	public Box(float x, int xOffset, float y, int yOffset, float width, float height) {
		this.x = x;
		this.y = y;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
	}
	
	public Box(int xOffset, int yOffset, float width, float height) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
	}
	
	public void translate(float x, float y) {
		this.x = this.x + x;
		this.y = this.y + y;
	}
	
	public void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float getX() {
		return x + xOffset;
	}
	
	public float getY() {
		return y + yOffset;
	}
	
	public int getXOffset() {
		return xOffset;
	}

	public void setXOffset(int x) {
		this.xOffset = x;
	}

	public int getYOffset() {
		return yOffset;
	}

	public void setYOffset(int y) {
		this.yOffset = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
}
