package core.entity.weapons;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import core.Theater;
import core.entity.Actor;
import core.entity.projectiles.AreaProjectile;
import core.entity.projectiles.HomingProjectile;
import core.entity.projectiles.Projectile;
import core.equipment.spells.Spell;
import core.scene.PropQuad;
import core.scene.Stage;

public class MagicWeapon extends Weapon {

	private Spell[] spells;
	private Projectile projectile;
	
	public MagicWeapon(String ref) {
		super(ref);
	}

	public void update(Stage stage, Actor source) {
		if(currentTime < styles[currentStyle].getSwingTime() && !interrupt) {
			currentTime += Theater.getDeltaSpeed(0.025f);
			if(currentTime >= styles[currentStyle].getCurrentBoxFrame(currentFrame) && currentFrame < sprite.getMaxFrame() - 1) {
				currentFrame++;
				sprite.frame = currentFrame;
				if(!styles[currentStyle].getHitbox().isEmpty()) {
					setBox(styles[currentStyle].getHitbox((sprite.dir * sprite.getMaxFrame()) + currentFrame));
					getBox().setFrame(this.x + getBox().getX(), this.y + getBox().getY(), getBox().getWidth(), getBox().getHeight());
					PropQuad.get().update(this);
				}
			}
		} else if(styles[currentStyle].getSwingTime() == -1) {
			sprite.animate();
			getBox().setFrame(this.x, this.y, 0f, 0f);
			if(currentStyle < 3) {
				switch(spells[currentStyle].getSpellType()) {
				case PROJECTILE:
					if(projectile == null) {
						if(source.getStats().canCast(spells[currentStyle].getCost()))
							projectile = new Projectile(spells[currentStyle].getName(), source, spells[currentStyle], null);
						else
							projectile = new Projectile("Fist", source, null, null);
						projectile.setLocation(x, y);
						stage.scenery.add(projectile);
					} else {
						projectile.adjustLocation(this);
						projectile.update(stage);
					}
					break;
				case HOMING:
					if(projectile == null) {
						if(source.getStats().canCast(spells[currentStyle].getCost()))
							projectile = new HomingProjectile(spells[currentStyle].getName(), source, spells[currentStyle], null);
						else
							projectile = new HomingProjectile("Fist", source, null, null);
						projectile.setLocation(x, y);
						stage.scenery.add(0, projectile);
					} else {
						projectile.adjustLocation(this);
						projectile.update(stage);
					}
					break;
				case AREA:
					if(projectile == null) {
						if(source.getStats().canCast(spells[currentStyle].getCost()))
							projectile = new AreaProjectile(spells[currentStyle].getName(), source, spells[currentStyle], null);
						else
							projectile = new AreaProjectile("Fist", source, null, null);
						projectile.setLocation(x, y);
						stage.scenery.add(0, projectile);
					} else {
						projectile.adjustLocation(this);
						projectile.update(stage);
					}
					break;
				}
			}
		}
	}
	
	public void attack(int dir, Rectangle2D box, boolean right) {
		sprite.dir = dir;
		Point2D point = getAttackPosition(box, right);
		switch(currentStyle) {
		case(0):
		case(1):
		case(2):
			castSpell((float)point.getX(), (float)point.getY());
			break;
		case(3):
			swing((float)point.getX(), (float)point.getY());
			break;
		case(4):
			defend((float)point.getX(), (float)point.getY());
			break;
		}
	}
	
	public void castSpell(float x, float y) {
		sprite.frame = 0;
		this.x = x;
		this.y = y;
		swinging = true;
		setBox(new Rectangle2D.Double(x, y, sprite.getWidth(), sprite.getHeight()));
	}
	
	public void swapSprite() {
		switch(currentStyle) {
		case(0):
		case(1):
		case(2):
			changeSprite(getName() + "_spell");
			break;
		case(3):
			changeSprite(getName() + "_attack");
			break;
		case(4):
			changeSprite(getName() + "_defend");
			break;
		}
	}
	
	@Override
	public void release(Stage stage, Actor source) {
		if(isDefending()) {
			defending = false;
		} else if(currentStyle < 3) {
			if(projectile != null) {
				if(source.getStats().canCast(getSpellCost())) {
					source.getStats().getVirtue().expelEssence(getSpellCost());
				} else {
					cancelCharge(stage);
					return;
				}
				
				projectile.fire(stage);
				projectile = null;
			}
		}
		swinging = false;
	}
	
	@Override
	public float getAttackRange(int dir) {
		float range = 0;
		
		if(currentStyle >= 3) {
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
		} else {
			switch(spells[currentStyle].getSpellType()) {
			case PROJECTILE:
				range = spells[currentStyle].getDistance() / 2f;
				break;
			case HOMING:
			case AREA:
				range = spells[currentStyle].getDiameter();
				break;
			}
		}
		
		return range;
	}
	
	@Override
	public Rectangle2D getHitRange(int dir, Rectangle2D box, boolean right) {
		Rectangle2D hitRange = new Rectangle2D.Float(sprite.getWidth() + 1, sprite.getHeight() + 1, 0, 0);
		if(currentStyle >= 3) {
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
		} else {
			switch(spells[currentStyle].getSpellType()) {
			case PROJECTILE:
				Projectile temp = new Projectile(spells[currentStyle].getName(), null, spells[currentStyle], null);
				switch(dir) {
				case(0):
				case(3):
					hitRange.setFrame(0, 0, temp.getSprite().getWidth(), temp.getSprite().getHeight() * (spells[currentStyle].getDistance() / 2));
					break;
				case(1):
				case(2):
					hitRange.setFrame(0, 0, temp.getSprite().getWidth() * (spells[currentStyle].getDistance() / 2), temp.getSprite().getHeight());
					break;
				}
				break;
			case HOMING:
			case AREA:
				hitRange.setFrame(0, 0, spells[currentStyle].getDiameter(), spells[currentStyle].getDiameter() * 0.75f);
				break;
			}
		}
		
		Point2D point = getAttackPosition(box, right);
		hitRange.setFrame(point.getX() + hitRange.getX(), point.getY() + hitRange.getY(), hitRange.getWidth(), hitRange.getHeight());
		
		return hitRange;
	}
	
	public void bindSpells(Spell[] spells) {
		this.spells = new Spell[spells.length];
		for(int x = 0; x<spells.length; x++) {
			this.spells[x] = Spell.loadSpell(spells[x].getID() + "");
		}
	}
	
	public void bindSpell(int slot, Spell spell) {
		this.spells[slot] = spell;
	}
	
	public boolean hasSpellBinds() {
		for(int x = 0; x<spells.length; x++) {
			if(spells[x] != null)
				return true;
		}
		
		return false;
	}
	
	public Spell[] getSpells() {
		return spells;
	}
	
	public Spell getCurrentSpell() {
		return spells[currentStyle];
	}
	
	public float getSpellCost() {
		float cost = 0;
		switch(spells[currentStyle].getSpellType()) {
		case PROJECTILE:
		case AREA:
			cost = spells[currentStyle].getCost() * projectile.getCharge();
			break;
		case HOMING:
			if(((HomingProjectile) projectile).getTargets().size() > 0)
				cost = (spells[currentStyle].getCost() * projectile.getCharge());
			break;
		}
		
		return cost;
	}
	
	public void setCurrentStyle(int currentStyle) {
		this.currentStyle = currentStyle;
		swapSprite();
	}

	@Override
	public void cancelCharge(Stage stage) {
		switch(spells[currentStyle].getSpellType()) {
		case PROJECTILE:
		case HOMING:
		case AREA:
			stage.scenery.remove(projectile);
			PropQuad.get().removeProp(projectile);
			projectile = null;
			swinging = false;
			break;
		}
	}

	public boolean isCharging() {
		if(projectile != null)
			return projectile.isCharging();
		return false;
	}
	
	@Override
	public boolean isParrying() {
		return false;
	}

}
