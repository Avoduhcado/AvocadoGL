package core.utilities.pathfinding;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import core.Theater;
import core.entity.Entity;

public class PathQuad {

	private PathQuad parent;
	private PathQuad[] children;
	private ArrayList<PathQuad> right, down, left, up;
	private boolean blocked;
	private boolean flat;
	private int pos;
	private Rectangle2D box;
	private int f;
	private int g = 0;
	private PathQuad pathParent;
	public static Dimension size = new Dimension(800, 600);
	public boolean highlight = false;
	
	private static PathQuad quad;
	
	public static PathQuad get() {
		return quad;
	}
	
	public static void init(PathQuad pathQuad) {
		quad = pathQuad;
	}
	
	public static void clear() {
		quad = null;
	}
	
	public PathQuad(PathQuad parent, float x, float y, BufferedImage hitmap) {
		this.parent = parent;
		right = new ArrayList<PathQuad>();
		down = new ArrayList<PathQuad>();
		left = new ArrayList<PathQuad>();
		up = new ArrayList<PathQuad>();
		if(parent == null) {
			//box = new Rectangle2D.Float(x, y, (float)foreground.getBox().getWidth(), (float)foreground.getBox().getHeight());
			box = new Rectangle2D.Double(x, y, hitmap.getWidth(), hitmap.getHeight());
			right.add(null);
			down.add(null);
			left.add(null);
			up.add(null);
			pos = -1;
		} else {
			box = new Rectangle2D.Double(x, y, (float)Math.ceil(parent.getBox().getWidth()/2), (float)Math.ceil(parent.getBox().getHeight()/2));
		}
		buildQuad(hitmap);
	}
	
	public static PathQuad load(BufferedReader reader, ArrayList<Entity> props) {
		String line;
		try {
			line = reader.readLine();
			String[] temp = line.split(";");
			
			BufferedImage hitmap = new BufferedImage(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), BufferedImage.TYPE_INT_RGB);
			PathQuad.size = new Dimension(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
			Graphics2D g = hitmap.createGraphics();
			
			while((line = reader.readLine()) != null && !line.matches("<END>")) {
				if(line.matches("<FLAT>"))
					g.setColor(Color.RED);
				else {
					temp = line.split(";");
					g.fillRect(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), Integer.parseInt(temp[3]));
				}
			}
			
			g.setColor(Color.WHITE);
			for(int x = 0; x<props.size(); x++) {
				if(props.get(x).isSolid())
					g.fillRect((int)props.get(x).getBox().getX(), (int)props.get(x).getBox().getY(), (int)props.get(x).getBox().getWidth(), (int)props.get(x).getBox().getHeight());
			}
			g.dispose();

			return new PathQuad(null, 0, 0, hitmap);
		} catch (IOException e) {
			System.err.println("The scene file has been misplaced!");
	    	e.printStackTrace();
		}
		
		System.out.println("Failed to load PathQuad.");
		return null;
	}
	
	public static BufferedImage buildHitmap(ArrayList<Entity> props) {
		BufferedImage hitmap = new BufferedImage(800, 600, BufferedImage.TYPE_BYTE_BINARY);
		PathQuad.size = new Dimension(800, 600);
		Graphics2D g = hitmap.createGraphics();
		
		for(int x = 0; x<props.size(); x++) {
			if(props.get(x).isSolid())
				g.fillRect((int)props.get(x).getBox().getX(), (int)props.get(x).getBox().getY(), (int)props.get(x).getBox().getWidth(), (int)props.get(x).getBox().getHeight());
		}
		g.dispose();
		
		return hitmap;
	}
	
	public void draw() {
		if(noChildren()) {
			GL11.glPushMatrix();
			GL11.glTranslated(box.getX() - Theater.get().getScreen().camera.getX(), box.getY() - Theater.get().getScreen().camera.getY(), 0);
			if(blocked)
				GL11.glColor3d(1.0, 0, 0);
			else if(highlight)
				GL11.glColor3d(0, 1.0, 1.0);
			else if(flat)
				GL11.glColor3d(1.0, 1.0, 0);
			else
				GL11.glColor3d(1.0, 1.0, 1.0);
			GL11.glLineWidth(1.0f);
			
			GL11.glBegin(GL11.GL_LINE_LOOP);
			{
				GL11.glVertex2d(0, 0);
				GL11.glVertex2d(getBox().getWidth(), 0);
				GL11.glVertex2d(getBox().getWidth(), getBox().getHeight());
				GL11.glVertex2d(0, getBox().getHeight());
			}
			GL11.glEnd();
			GL11.glPopMatrix();
		} else {
		//if(!noChildren()) {
			for(int x = 0; x<children.length; x++) {
				children[x].draw();
			}
		}
	}
	
	public PathQuad getParent() {
		return parent;
	}
	
	public void buildQuad(BufferedImage hitmap) {
		boolean block = false;
		boolean impass = false;
		boolean open = false;

		for(int a = (int) getBox().getX(); a<getBox().getMaxX(); a++) {
			for(int b = (int) getBox().getY(); b<getBox().getMaxY(); b++) {
				if(hitmap.getRGB(a, b) == Color.WHITE.getRGB()) {
					block = true;
				} else if(hitmap.getRGB(a, b) == Color.RED.getRGB()) {
					impass = true;
				} else {
					open = true;
				}
				
				if(block && open) {
					if(getBox().getWidth() == 1 || getBox().getHeight() == 1) {
						this.blocked = true;
					} else {
						this.addChildren(hitmap);
					}
					a = (int) (getBox().getMaxX() + 1);
					b = (int) (getBox().getMaxY() + 1);
				} else if(impass && open) {
					if(getBox().getWidth() == 1 || getBox().getHeight() == 1) {
						this.flat = true;
					} else {
						this.addChildren(hitmap);
					}
					a = (int) (getBox().getMaxX() + 1);
					b = (int) (getBox().getMaxY() + 1);
				}
			}
		}
		
		if(noChildren()) {
			if(block)
				this.blocked = true;
			else if(impass)
				this.flat = true;
		}
	}
	
	public void buildNeighbors() {
		if(!this.isBlocked()) {
			if(this.noChildren()) {
				switch(this.pos) {
				case(0): // Top Left
					if(!this.parent.getChild(1).isBlocked())
						this.buildRight(this.parent.getChild(1));
					if(!this.parent.getChild(2).isBlocked())
						this.buildDown(this.parent.getChild(2));
					if(this.getBox().getX() > 0) {
						if(!this.parent.getLeft().get(0).isBlocked())
							this.buildLeft(this.parent.getLeft().get(0));
					}
					if(this.getBox().getY() > 0) {
						if(!this.parent.getUp().get(0).isBlocked())
							this.buildUp(this.parent.getUp().get(0));
					}
					break;
				case(1): // Top Right
					//if(this.getBox().getMaxX() < Pathfinder.getGrid().length && this.parent.getRight().get(0) != null) {
					if(this.getBox().getMaxX() < size.getWidth() && this.parent.getRight().get(0) != null) {
						if(!this.parent.getRight().get(0).isBlocked())
							this.buildRight(this.parent.getRight().get(0));
					}
					if(!this.parent.getChild(3).isBlocked())
						this.buildDown(this.parent.getChild(3));
					if(!this.parent.getChild(0).isBlocked())
						this.buildLeft(this.parent.getChild(0));
					if(this.getBox().getY() > 0) {
						if(!this.parent.getUp().get(0).isBlocked())
							this.buildUp(this.parent.getUp().get(0));
					}
					break;
				case(2): // Bottom Left
					if(!this.parent.getChild(3).isBlocked())
						this.buildRight(this.parent.getChild(3));
					//if(this.getBox().getMaxY() < Pathfinder.getGrid()[0].length && this.parent.getDown().get(0) != null) {
					if(this.getBox().getMaxY() < size.getHeight() && this.parent.getDown().get(0) != null) {
						if(!this.parent.getDown().get(0).isBlocked())
							this.buildDown(this.parent.getDown().get(0));
					}
					if(this.getBox().getX() > 0) {
						if(!this.parent.getLeft().get(0).isBlocked())
							this.buildLeft(this.parent.getLeft().get(0));
					}
					if(!this.parent.getChild(0).isBlocked())
						this.buildUp(this.parent.getChild(0));
					break;
				case(3): // Bottom Right
					//if(this.getBox().getMaxX() < Pathfinder.getGrid().length && this.parent.getRight().get(0) != null) {
					if(this.getBox().getMaxX() < size.getWidth() && this.parent.getRight().get(0) != null) {
						if(!this.parent.getRight().get(0).isBlocked())
							this.buildRight(this.parent.getRight().get(0));
					}
					//if(this.getBox().getMaxY() < Pathfinder.getGrid()[0].length  && this.parent.getDown().get(0) != null) {
					if(this.getBox().getMaxY() < size.getHeight() && this.parent.getDown().get(0) != null) {
						if(!this.parent.getDown().get(0).isBlocked())
							this.buildDown(this.parent.getDown().get(0));
					}
					if(!this.parent.getChild(2).isBlocked())
						this.buildLeft(this.parent.getChild(2));
					if(!this.parent.getChild(1).isBlocked())
						this.buildUp(this.parent.getChild(1));
					break;
				default:
					this.parent = null;
				}
			} else { // It has children, perform recursion, add buffer links
				switch(this.pos) {
				case(-1):
					break;
				case(0):
					right.add(parent.getChild(1));
					down.add(parent.getChild(2));
					left.add(parent.getLeft().get(0));
					up.add(parent.getUp().get(0));
					break;
				case(1):
					right.add(parent.getRight().get(0));
					down.add(parent.getChild(3));
					left.add(parent.getChild(0));
					up.add(parent.getUp().get(0));
					break;
				case(2):
					right.add(parent.getChild(3));
					down.add(parent.getDown().get(0));
					left.add(parent.getLeft().get(0));
					up.add(parent.getChild(0));
					break;
				case(3):
					right.add(parent.getRight().get(0));
					down.add(parent.getDown().get(0));
					left.add(parent.getChild(2));
					up.add(parent.getChild(1));
					break;
				default:
					System.out.println("Path quads are obnoxious");
				}
				for(int x = 0; x<children.length; x++) {
					this.children[x].buildNeighbors();
				}
			}
		}
	}
	
	public PathQuad findContainer(float targetX, float targetY) {
		PathQuad temp = this;
		// Avoid infinite looping of uncontained coordinates
		if(!temp.box.contains(targetX, targetY))
			return null;
		
		while(!temp.noChildren()) {
			for(int a = 0; a<temp.children.length; a++) {
				if(temp.children[a].box.contains(targetX, targetY)) {
					temp = temp.children[a];
					break;
				}
			}
		}
		
		return temp;
	}
	
	public void addChildren(BufferedImage hitmap) {
		children = new PathQuad[4];
		children[0] = new PathQuad(this, (int)this.getBox().getX(), (int)this.getBox().getY(), hitmap);
		children[0].pos = 0;
		children[1] = new PathQuad(this, (int)this.getBox().getCenterX(), (int)this.getBox().getY(), hitmap);
		children[1].pos = 1;
		children[2] = new PathQuad(this, (int)this.getBox().getX(), (int)this.getBox().getCenterY(), hitmap);
		children[2].pos = 2;
		children[3] = new PathQuad(this, (int)this.getBox().getCenterX(), (int)this.getBox().getCenterY(), hitmap);
		children[3].pos = 3;
	}
	
	public PathQuad getChild(int child) {
		return children[child];
	}
	
	public boolean noChildren() {
		if(children == null)
			return true;
		else
			return false;
	}
	
	public boolean isBlocked() {
		return blocked;
	}

	public boolean isImpassable() {
		return flat;
	}

	public ArrayList<PathQuad> getRight() {
		return right;
	}
	
	public void buildRight(PathQuad quad) {
		if(!quad.noChildren()) {
			this.buildRight(quad.getChild(0));
			this.buildRight(quad.getChild(2));
		} else if(!quad.isBlocked() && ((this.getBox().getY() >= quad.getBox().getY() && this.getBox().getY() < quad.getBox().getMaxY()) 
				|| (quad.getBox().getY() >= this.getBox().getY() && quad.getBox().getY() < this.getBox().getMaxY()))) {
			this.right.add(quad);
		}
	}
	
	public ArrayList<PathQuad> getDown() {
		return down;
	}
	
	public void buildDown(PathQuad quad) {
		if(!quad.noChildren()) {
			this.buildDown(quad.getChild(0));
			this.buildDown(quad.getChild(1));
		} else if(!quad.isBlocked() && ((this.getBox().getX() >= quad.getBox().getX() && this.getBox().getX() < quad.getBox().getMaxX()) 
				|| (quad.getBox().getX() >= this.getBox().getX() && quad.getBox().getX() < this.getBox().getMaxX()))) {
			this.down.add(quad);
		}
	}
	
	public ArrayList<PathQuad> getLeft() {
		return left;
	}
	
	public void buildLeft(PathQuad quad) {
		if(!quad.noChildren()) {
			this.buildLeft(quad.getChild(1));
			this.buildLeft(quad.getChild(3));
		} else if(!quad.isBlocked() && ((this.getBox().getY() >= quad.getBox().getY() && this.getBox().getY() < quad.getBox().getMaxY()) 
				|| (quad.getBox().getY() >= this.getBox().getY() && quad.getBox().getY() < this.getBox().getMaxY()))) {
			this.left.add(quad);
		}
	}
	
	public ArrayList<PathQuad> getUp() {
		return up;
	}
	
	public void buildUp(PathQuad quad) {
		if(!quad.noChildren()) {
			this.buildUp(quad.getChild(2));
			this.buildUp(quad.getChild(3));
		} else if(!quad.isBlocked() && ((this.getBox().getX() >= quad.getBox().getX() && this.getBox().getX() < quad.getBox().getMaxX()) 
				|| (quad.getBox().getX() >= this.getBox().getX() && quad.getBox().getX() < this.getBox().getMaxX()))) {
			this.up.add(quad);
		}
	}
	
	public ArrayList<Rectangle2D> getAllBlockedBoxes() {
		if(noChildren()) {
			ArrayList<Rectangle2D> tempBoxes = new ArrayList<Rectangle2D>();
			if(this.blocked)
				tempBoxes.add(this.box);
			
			return tempBoxes;
		} else {
			ArrayList<Rectangle2D> tempBoxes = new ArrayList<Rectangle2D>();
			for(int x = 0; x < this.children.length; x++) {
				tempBoxes.addAll(children[x].getAllBlockedBoxes());
			}
			//System.out.println(tempBoxes.size());
			return tempBoxes;
		}
	}
	
	public ArrayList<Rectangle2D> getAllFlatBoxes() {
		if(noChildren()) {
			ArrayList<Rectangle2D> tempBoxes = new ArrayList<Rectangle2D>();
			if(this.flat)
				tempBoxes.add(this.box);
			
			return tempBoxes;
		} else {
			ArrayList<Rectangle2D> tempBoxes = new ArrayList<Rectangle2D>();
			for(int x = 0; x < this.children.length; x++) {
				tempBoxes.addAll(children[x].getAllFlatBoxes());
			}
			
			return tempBoxes;
		}
	}
	
	public void clearPathParents() {
		if(noChildren()) {
			this.pathParent = null;
		} else {
			for(int x = 0; x<this.children.length; x++) {
				this.children[x].clearPathParents();
			}
		}
	}
	
	public void setUpPath(PathQuad pathParent, int g, PathQuad target) {
		setPathParent(pathParent);
		setG(g);
		findF(target);
	}
	
	public void findF(PathQuad target) {
		f = (int) (g + (Math.abs(getBox().getCenterX() - target.getBox().getCenterX()) + Math.abs(getBox().getCenterY() - target.getBox().getCenterY())*15));
	}
	
	public int getF() {
		return f;
	}
	
	public int getG() {
		return g;
	}
	
	public void setG(int g) {
		this.g = g;
	}

	public PathQuad getPathParent() {
		return pathParent;
	}
	
	public void setPathParent(PathQuad par) {
		this.pathParent = par;
	}

	public Rectangle2D getBox() {
		return box;
	}

	public void setBox(Rectangle2D box) {
		this.box = box;
	}
}
