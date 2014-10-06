package core.entity.weapons;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.lwjgl.util.vector.Vector3f;

import core.Theater;
import core.entity.Actor;
import core.equipment.spells.Spell;
import core.scene.PropQuad;
import core.scene.Stage;

public class MeleeWeapon extends Weapon {

	private boolean parrying;
	
	public MeleeWeapon(String ref) {
		super(ref);
	}

	public void update(Stage stage, Actor source) {
		if(currentTime < styles[currentStyle].getSwingTime() && !interrupt && !parrying) {
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
		} else if(parrying) {
			currentTime += Theater.getDeltaSpeed(0.025f);
			sprite.animate();
		}
	}

	public void attack(int dir, Rectangle2D box, boolean right) {
		sprite.dir = dir;
		Point2D point = getAttackPosition(box, right);
		switch(currentStyle) {
		case(0):
			thrust();
		case(1):
		case(2):
			swing((float)point.getX(), (float)point.getY());
			break;
		case(3):
			parry((float)point.getX(), (float)point.getY());
			break;
		case(4):
			defend((float)point.getX(), (float)point.getY());
			break;
		}
	}
	
	public void parry(float x, float y) {
		parrying = true;
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
	
	public void swapSprite() {
		switch(currentStyle) {
		case(0):
			changeSprite(getName() + "_thrust");
			break;
		case(1):
			changeSprite(getName() + "_crush");
			break;
		case(2):
			changeSprite(getName() + "_slash");
			break;
		case(3):
			changeSprite(getName() + "_parry");
			break;
		case(4):
			changeSprite(getName() + "_defend");
			break;
		}
	}

	public void setCurrentStyle(int currentStyle) {
		this.currentStyle = currentStyle;
		swapSprite();
	}

	public boolean swung() {
		if(currentTime >= styles[currentStyle].getSwingTime() || interrupt) {
			PropQuad.get().removeProp(this);
			sprite.frame = 0;
			currentFrame = 0;
			currentTime = 0.0f;
			swinging = false;
			parrying = false;
			interrupt = false;
			sprite.setRotation(new Vector3f(0, 1, 1));
			return true;
		}
		
		return false;
	}
	
	public void thrust() {
		
	}
	
	public void slash() {
		sprite.setRotation(new Vector3f(270, 0, 0));
	}
	
	public boolean isParrying() {
		return parrying;
	}

	public void release(Stage stage, Actor source) {
		if(isDefending()) {
			defending = false;
		}
		swinging = false;
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
	public Spell getCurrentSpell() {
		return null;
	}

	@Override
	public boolean isCharging() {
		return false;
	}

	@Override
	public void cancelCharge(Stage stage) {		
	}

}
