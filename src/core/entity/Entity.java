package core.entity;

import java.awt.geom.Rectangle2D;

import core.Theater;
import core.render.Shadow;
import core.render.Sprite;
import core.scene.Box;
import core.scene.Stage;
import core.utilities.actions.Action;

public class Entity {

	public static int entityCount = 0;
	
	protected String ID;
	protected float x;
	protected float y;
	protected Sprite sprite;
	protected Rectangle2D box;
	protected Rectangle2D fullBox;
	private Box colBox;
	protected boolean solid;
	protected boolean flat;
	protected String name;
	protected boolean alive = true;
	protected Action action;
	
	public Entity(String ref, float x, float y, boolean solid) {
		this.x = x;
		this.y = y;
		sprite = Sprite.loadSprite(ref);
		setBox(new Rectangle2D.Double(x, y, sprite.getWidth(), sprite.getHeight()));
		this.solid = solid;
		name = ref;
		this.ID = name + "-" + entityCount++;
	}
	
	public void draw() {
		/* TODO Collect sprite batch directly from visible quadtree during update loop? */
		if(Theater.get().getScreen().camera.intersects(fullBox) || sprite.still) {
			//System.out.println(this.ID);
			this.sprite.draw(x, y);
		}
	}
	
	public void drawShadow() {
		if(sprite.shadow != null) {
			sprite.shadow.draw(sprite, x, y);
		}
 	}
	
	public void animate() {
		this.sprite.animate();
	}
	
	public float getDistance(float targetX, float targetY) {
		return (float) Math.sqrt(Math.pow(getX() - targetX, 2) + Math.pow(getY() - targetY, 2));
	}
	
	// TODO Hitbox gets placed weirdly during sprite swap
	public void changeSprite(String ref) {
		int swapWidth = this.sprite.getWidth();
		int swapHeight = this.sprite.getHeight();
		boolean hasShadow = false;
		if(sprite.shadow != null)
			hasShadow = true;
		int dir = sprite.dir;
		this.sprite = Sprite.loadSprite(ref);
		this.sprite.setDir(dir);
		if(hasShadow)
			sprite.setShadow(new Shadow());
		this.x -= (float)(this.sprite.getWidth() - swapWidth) / 2;
		this.y -= (float)(this.sprite.getHeight() - swapHeight) / 2;
		this.getBox().setFrame(this.getBox().getX() - (float)(this.sprite.getWidth() - swapWidth) / 2, this.getBox().getY() - (float)(this.sprite.getHeight() - swapHeight) / 2,
				this.getBox().getWidth(), this.getBox().getHeight());
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public String getID() {
		return ID;
	}
	
	public void setID(String ID) {
		this.ID = ID;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getX() {
		return x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public float getY() {
		return y;
	}
	
	public void setDir(int dir) {
		this.sprite.setDir(dir);
	}
	
	public int getDir() {
		return this.sprite.dir;
	}
	
	public int getFrame() {
		return this.sprite.frame;
	}
	
	public int getMaxFrame() {
		return this.sprite.getMaxFrame();
	}
	
	public void setActing(boolean acting) {
		this.sprite.acting = acting;
	}
	
	public boolean isActing() {
		return this.sprite.acting;
	}
	
	public void setStill(boolean still) {
		this.sprite.setStill(still);
	}
	
	public void setColBox(Box box) {
		this.colBox = box;
	}
	
	public void setBox(Rectangle2D box) {
		this.box = new Rectangle2D.Double(box.getX(), box.getY(), box.getWidth(), box.getHeight());
		this.fullBox = new Rectangle2D.Double(x, y, sprite.getWidth(), sprite.getHeight());
	}
	
	public Rectangle2D getBox() {
		return box;
	}
	
	public Rectangle2D getBox(float x, float y) {
		if(colBox != null) {
			Rectangle2D temp = new Rectangle2D.Double(box.getX() + colBox.xOffset + x, box.getY() + colBox.yOffset + y, 
					sprite.getWidth() * colBox.width, sprite.getHeight() * colBox.height);
			return temp;
		} else {
			Rectangle2D temp = new Rectangle2D.Double(box.getX() + x, box.getY() + y,
					box.getWidth(), box.getHeight()); 
			return temp;
		}
	}
	
	public void updateBox() {
		fullBox.setFrame(x, y, sprite.getWidth(), sprite.getHeight());
		if(colBox != null) {
			box.setFrame(x + colBox.xOffset, y + colBox.yOffset, box.getWidth(), box.getHeight());
		} else {
			box.setFrame(x, y, box.getWidth(), box.getHeight());
		}
	}
	
	public void resizeBox(int x, int y, float width, float height) {
		colBox = new Box(x, y, width, height);
		box = new Rectangle2D.Double(this.x + colBox.xOffset, this.y + colBox.yOffset, sprite.getWidth() * colBox.width, sprite.getHeight() * colBox.height);
	}
	
	public void die(Stage stage) {
		stage.remove(this);
	}
	
	public boolean isSolid() {
		return solid;
	}
	
	public void setFlat(boolean flat) {
		this.flat = flat;
	}
	
	public boolean isFlat() {
		return flat;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	public Action getAction() {
		return action;
	}
	
	public void setAction(Action action) {
		this.action = action;
	}
	
}
