package core.entity.weapons;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import core.Theater;
import core.entity.Actor;
import core.equipment.spells.Spell;
import core.scene.PropQuad;
import core.scene.Stage;

public class FistWeapon extends Weapon {

	public FistWeapon(String ref) {
		super(ref);
	}

	@Override
	public void update(Stage stage, Actor source) {
		if(currentTime < styles[0].getSwingTime() && !interrupt) {
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
		}
	}

	public void attack(int dir, Rectangle2D box, boolean right) {
		sprite.dir = dir;
		Point2D point = getAttackPosition(box, right);
		swing((float)point.getX(), (float)point.getY());
	}
	
	@Override
	public void swapSprite() {
		
	}

	@Override
	public void setCurrentStyle(int currentStyle) {
		
	}

	@Override
	public void bindSpells(Spell[] spells) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bindSpell(int slot, Spell spell) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasSpellBinds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isParrying() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Spell getCurrentSpell() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCharging() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void release(Stage stage, Actor source) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancelCharge(Stage stage) {
		// TODO Auto-generated method stub
		
	}

}
