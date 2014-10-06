package core.entity.weapons;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import core.Theater;
import core.entity.Actor;
import core.entity.projectiles.AreaProjectile;
import core.entity.projectiles.HomingProjectile;
import core.entity.projectiles.Projectile;
import core.equipment.spells.Spell;
import core.items.Item;
import core.scene.PropQuad;
import core.scene.Stage;

public class RangedWeapon extends Weapon {

	private Item ammo;
	private Projectile projectile;
	
	public RangedWeapon(String ref) {
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
				if(ammo == null) {
					if(source.getEquipment().getWeapons().getSwingingItem().isWeapon())
						ammo = source.getEquipment().getWeapons().getSwingingItem();
					else
						ammo = source.getEquipment().getQuiver().getEquippedAmmo();
				}
				switch(ammo.getAmmoType()) {
				case PROJECTILE:
					if(projectile == null) {
						if(ammo.getID() != 0)
							projectile = new Projectile(ammo.getAmmoName(), source, null, ammo);
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
						if(ammo.getID() != 0)
							projectile = new HomingProjectile(ammo.getAmmoName(), source, null, ammo);
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
						if(ammo.getID() != 0)
							projectile = new AreaProjectile(ammo.getAmmoName(), source, null, ammo);
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
			shoot((float)point.getX(), (float)point.getY());
			break;
		case(3):
			swing((float)point.getX(), (float)point.getY());
			break;
		case(4):
			defend((float)point.getX(), (float)point.getY());
			break;
		}
	}
	
	public void shoot(float x, float y) {
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
			changeSprite(getName() + "_shoot");
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
				if(ammo.isStackable()) {
					if(ammo.isWeapon()) {
						ammo.setAmount(ammo.getAmount() - 1);
						if(ammo.getAmount() == 0) {
							source.getEquipment().unequip(ammo, stage);
						}
					} else {
						source.getEquipment().getQuiver().useAmmo();
					}
				}
				if(ammo.getID() == 0) {
					cancelCharge(stage);
					return;
				}
				
				projectile.fire(stage);
				projectile = null;
				ammo = null;
			}
		}
		swinging = false;
	}

	public void setAmmo(Item ammo) {
		this.ammo = ammo;
	}
	
	public void setCurrentStyle(int currentStyle) {
		this.currentStyle = currentStyle;
		swapSprite();
	}

	public boolean isCharging() {
		if(projectile != null)
			return projectile.isCharging();
		return false;
	}
	
	@Override
	public void bindSpells(Spell[] spells) {		
	}

	@Override
	public void bindSpell(int slot, Spell spell) {		
	}

	@Override
	public boolean hasSpellBinds() {
		return false;
	}

	@Override
	public boolean isParrying() {
		return false;
	}

	@Override
	public Spell getCurrentSpell() {
		return null;
	}

	@Override
	public void cancelCharge(Stage stage) {
		stage.scenery.remove(projectile);
		PropQuad.get().removeProp(projectile);
		projectile = null;
		swinging = false;
	}

}
