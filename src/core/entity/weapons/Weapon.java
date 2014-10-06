package core.entity.weapons;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import core.Theater;
import core.entity.Actor;
import core.entity.Entity;
import core.equipment.spells.Spell;
import core.items.Item;
import core.scene.PropQuad;
import core.scene.Stage;

public abstract class Weapon extends Entity {

	protected float currentTime = 0.0f;
	protected int currentFrame = 0;
	protected boolean interrupt;
	protected boolean swinging;
	protected int currentStyle;
	protected Style[] styles = new Style[5];
	protected boolean defending;
	
	public Weapon(String ref) {
		super(ref, 0, 0, false);
		setCurrentStyle(0);
	}
	
	public static Weapon loadWeapon(Item item) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/database/weapons"));
			
			String line;
			while((line = reader.readLine()) != null) {
				if(line.matches("-" + item.getWeaponName() + "-")) {
					Weapon weapon = null;
					switch(item.getWeaponType()) {
					case(0):
						weapon = new MeleeWeapon(item.getWeaponName());
						break;
					case(1):
						weapon = new MagicWeapon(item.getWeaponName());
						break;
					case(2):
						weapon = new RangedWeapon(item.getWeaponName());
						break;
					case(3):
						weapon = new FistWeapon(item.getWeaponName());
						break;
					}
					while((line = reader.readLine()) != null && !line.matches("<END>")) {
						if(line.matches("<DEFEND>")) {
							weapon.styles[4] = Style.loadStyle(reader);
						}
						if(item.getWeaponType() == 0) {
							if(line.matches("<THRUST>")) {
								weapon.styles[0] = Style.loadStyle(reader);
							} else if(line.matches("<CRUSH>")) {
								weapon.styles[1] = Style.loadStyle(reader);
							} else if(line.matches("<SLASH>")) {
								weapon.styles[2] = Style.loadStyle(reader);
							} else if(line.matches("<PARRY>")) {
								weapon.styles[3] = Style.loadStyle(reader);
							}
						} else if(item.getWeaponType() == 1) {
							if(line.matches("<SPELL1>")) {
								weapon.styles[0] = Style.loadStyle(reader);
							} else if(line.matches("<SPELL2>")) {
								weapon.styles[1] = Style.loadStyle(reader);
							} else if(line.matches("<SPELL3>")) {
								weapon.styles[2] = Style.loadStyle(reader);
							} else if(line.matches("<ATTACK>")) {
								weapon.styles[3] = Style.loadStyle(reader);
							}
						} else if(item.getWeaponType() == 2) {
							if(line.matches("<BODYSHOT>")) {
								weapon.styles[0] = Style.loadStyle(reader);
							} else if(line.matches("<HEADSHOT>")) {
								weapon.styles[1] = Style.loadStyle(reader);
							} else if(line.matches("<CRIPPLE>")) {
								weapon.styles[2] = Style.loadStyle(reader);
							} else if(line.matches("<ATTACK>")) {
								weapon.styles[3] = Style.loadStyle(reader);
							}
						} else if(item.getWeaponType() == 3) {
							if(line.matches("<THRUST>")) {
								weapon.styles[0] = Style.loadStyle(reader);
							}
						}
					}
					
					reader.close();
					return weapon;
				}
			}
			
			reader.close();
		} catch (FileNotFoundException e) {
	    	System.out.println("The weapons database has been misplaced!");
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	System.out.println("Weapons database failed to load!");
	    	e.printStackTrace();
	    }
		
		return null;
	}
	
	public static String getWeaponNames() {
		String names = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/database/weapons"));
			
			String line;
			while((line = reader.readLine()) != null) {
				if(line.startsWith("-") && line.endsWith("-")) {
					names += line.substring(1, line.length() - 1) + ";";
				}
			}
			
			reader.close();
		} catch (FileNotFoundException e) {
	    	System.out.println("The weapons database has been misplaced!");
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	System.out.println("Weapons database failed to load!");
	    	e.printStackTrace();
	    }
		
		return names;
	}
	
	public abstract void update(Stage stage, Actor source);
	
	public void draw() {
		sprite.draw(x, y);
		
		if(Theater.debug()) {
			GL11.glDisable(GL11.GL_TEXTURE_2D);

			GL11.glPushMatrix();
			GL11.glTranslatef((float) (getBox().getX() - Theater.get().getScreen().camera.getX()), (float)(getBox().getY() - Theater.get().getScreen().camera.getY()), 0);
			GL11.glColor3f(0.2f, 0.1f, 1.0f);
			GL11.glLineWidth(1.0f);

			GL11.glBegin(GL11.GL_LINE_LOOP);
			{
				GL11.glVertex2f(0, 0);
				GL11.glVertex2f((float)getBox().getWidth(), 0);
				GL11.glVertex2f((float)getBox().getWidth(), (float)getBox().getHeight());
				GL11.glVertex2f(0, (float)getBox().getHeight());
			}
			GL11.glEnd();
			GL11.glPopMatrix();

			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}
	}
	
	public abstract void attack(int dir, Rectangle2D box, boolean right);
	
	public Point2D getAttackPosition(Rectangle2D box, boolean right) {
		Point2D point = new Point2D.Float();
		switch(getDir()) {
		case(0):
			if(right)
				point.setLocation((float)box.getX(), (float)box.getCenterY());
			else
				point.setLocation((float)box.getCenterX(), (float)box.getCenterY());
			break;
		case(1):
			if(right)
				point.setLocation((float)(box.getCenterX() + (box.getWidth()/4)), (float)box.getY());
			else
				point.setLocation((float)(box.getCenterX() + (box.getWidth()/4)), (float)box.getY());
			break;
		case(2):
			if(right)
				point.setLocation((float)(box.getCenterX() - (box.getWidth()/2)), (float)box.getY());
			else
				point.setLocation((float)(box.getCenterX() - (box.getWidth()/2)), (float)box.getY());
			break;
		case(3):
			if(right)
				point.setLocation((float)box.getCenterX(), (float)(box.getY() - (box.getWidth()/2)));
			else
				point.setLocation((float)box.getX(), (float)(box.getY() - (box.getWidth()/2)));
			break;
		}
		
		return point;
	}
	
	public void swing(float x, float y) {
		currentTime = 0.0f;
		currentFrame = 0;
		this.x = x;
		this.y = y;
		interrupt = false;
		swinging = true;
		if(styles[currentStyle].getHitbox().isEmpty()) {
			getBox().setFrame(x, y, sprite.getWidth(), sprite.getHeight());
			for(int b = 0; b<sprite.getMaxDir(); b++)
				for(int a = 0; a<sprite.getMaxFrame(); a++)
					styles[currentStyle].getBoxFrame().add((styles[currentStyle].getSwingTime() / sprite.getMaxFrame()) * (a + 1));
		} else {
			setBox(styles[currentStyle].getHitbox(sprite.dir * sprite.getMaxFrame()));
			getBox().setFrame(this.x + getBox().getX(), this.y + getBox().getY(), getBox().getWidth(), getBox().getHeight());
		}
		PropQuad.get().update(this);
	}
	
	public void defend(float x, float y) {
		defending = true;
		swinging = true;
		this.x = x;
		this.y = y;
		switch(getDir()) {
		case(0):
			this.y -= sprite.getHeight();
			break;
		case(1):
			this.x -= sprite.getWidth();
			break;
		}
		sprite.frame = 0;
		setBox(new Rectangle2D.Double(x, y, sprite.getWidth(), sprite.getHeight()));
	}

	public boolean swung() {
		if(currentTime >= styles[currentStyle].getSwingTime() || interrupt) {
			PropQuad.get().removeProp(this);
			sprite.frame = 0;
			currentFrame = 0;
			currentTime = 0.0f;
			swinging = false;
			interrupt = false;
			return true;
		}
		
		return false;
	}
	
	public ArrayList<Actor> getTargets() {
		ArrayList<Entity> props = PropQuad.get().getSolidProps(this);
		ArrayList<Actor> targets = PropQuad.get().getActors(this);
		ArrayList<Actor> hits = new ArrayList<Actor>();
		
		for(int x = 0; x<props.size(); x++) {
			if(getBox().intersects(props.get(x).getBox())) {
				interrupt = true;
				break;
			}
		}
		for(int x = 0; x<targets.size(); x++) {
			if(getBox().intersects(targets.get(x).getBox())) {
				hits.add(targets.get(x));
			}
		}
		
		return hits;
	}
	
	public abstract void release(Stage stage, Actor source);
	
	public float getAttackRange(int dir) {
		float range = 0;
		
		if(!styles[currentStyle].getHitbox().isEmpty()) {
			for(int x = (sprite.getMaxFrame() / sprite.getMaxDir()) * dir; x<sprite.getMaxFrame(); x++) {
				switch(dir) {
				case 0:
				case 3:
					range += styles[currentStyle].getHitbox(x).getY() + styles[currentStyle].getHitbox(x).getHeight();
					break;
				case 1:
				case 2:
					range += styles[currentStyle].getHitbox(x).getX() + styles[currentStyle].getHitbox(x).getWidth();
					break;
				}
			}
			range = range / sprite.getMaxFrame();
		} else {
			switch(dir) {
			case 0:
			case 3:
				range = sprite.getHeight();
				break;
			case 1:
			case 2:
				range = sprite.getWidth();
				break;
			}
		}
		
		return range;
	}
	
	public Rectangle2D getHitRange(int dir, Rectangle2D box, boolean right) {
		Rectangle2D hitRange = new Rectangle2D.Float(sprite.getWidth() + 1, sprite.getHeight() + 1, 0, 0);
		if(sprite.getMaxDir() > 1) {
			dir = 0;
		}
		
		if(!styles[currentStyle].getHitbox().isEmpty()) {
			for(int a = (sprite.getMaxFrame() / sprite.getMaxDir()) * dir; a<sprite.getMaxFrame(); a++) {
				if(styles[currentStyle].getHitbox(a).getWidth() > 0 && styles[currentStyle].getHitbox(a).getHeight() > 0) {
					if(styles[currentStyle].getHitbox(a).getX() < hitRange.getX())
						hitRange.setFrame(styles[currentStyle].getHitbox(a).getX(), hitRange.getY(), hitRange.getWidth(), hitRange.getHeight());
					if(styles[currentStyle].getHitbox(a).getY() < hitRange.getY())
						hitRange.setFrame(hitRange.getX(), styles[currentStyle].getHitbox(a).getY(), hitRange.getWidth(), hitRange.getHeight());
					if(styles[currentStyle].getHitbox(a).getWidth() > hitRange.getWidth())
						hitRange.setFrame(hitRange.getX(), hitRange.getY(), styles[currentStyle].getHitbox(a).getWidth(), hitRange.getHeight());
					if(styles[currentStyle].getHitbox(a).getHeight() > hitRange.getHeight())
						hitRange.setFrame(hitRange.getX(), hitRange.getY(), hitRange.getWidth(), styles[currentStyle].getHitbox(a).getHeight());
				}
			}
		} else {
			hitRange.setFrame(0, 0, sprite.getWidth(), sprite.getHeight());
		}
		
		Point2D point = getAttackPosition(box, right);
		hitRange.setFrame(point.getX() + hitRange.getX(), point.getY() + hitRange.getY(), hitRange.getWidth(), hitRange.getHeight());
		
		return hitRange;
	}
	
	public boolean isInterrupt() {
		return interrupt;
	}
	
	public void setInterrupt(boolean interrupt) {
		this.interrupt = interrupt;
	}
	
	public boolean isSwinging() {
		return swinging;
	}
	
	public abstract boolean isCharging();
	
	public boolean isDefending() {
		return defending;
	}

	public void setDefending(boolean defending) {
		this.defending = defending;
	}

	public void setScale(float scale) {
		this.sprite.setScale(scale);
	}
	
	public abstract void setCurrentStyle(int currentStyle);
	
	public int getCurrentStyle() {
		return currentStyle;
	}

	public abstract void swapSprite();
	
	public abstract boolean isParrying();
	
	public abstract Spell getCurrentSpell();
	
	public abstract void cancelCharge(Stage stage);
	
	public abstract void bindSpells(Spell[] spells);
	public abstract void bindSpell(int slot, Spell spell);
	public abstract boolean hasSpellBinds();
}
