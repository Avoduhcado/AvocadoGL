package core.entity.projectiles;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.util.ArrayList;

import core.Theater;
import core.entity.Actor;
import core.entity.CharState;
import core.entity.Moving;
import core.entity.Prop;
import core.entity.ai.AIAlert;
import core.entity.weapons.Weapon;
import core.equipment.ItemContainer;
import core.equipment.spells.Spell;
import core.items.Item;
import core.render.Shadow;
import core.scene.PropQuad;
import core.scene.Stage;
import core.utilities.actions.Action;
import core.utilities.sounds.Ensemble;
import core.utilities.sounds.SoundEffect;

public class Projectile extends Moving {

	protected Actor source;
	protected Spell spell;
	protected Item ammo;
	protected boolean piercing;
	protected float charge;
	protected float maxCharge = 3f;
	protected boolean charging;
	protected float scale = 1f;
	protected boolean contact;
	
	public Projectile(String ref, Actor source, Spell spell, Item ammo) {
		super(ref, 0, 0, false);
		if(source != null) {
			this.source = source;
			this.setDir(source.getDir());
		}
		charge = 1f;
		charging = true;
		sprite.setScale(scale);
		sprite.shadow = new Shadow();
		setFloating(true);
		this.spell = spell;
		this.ammo = ammo;
	}
	
	public void update(Stage stage) {
		if(charging) {
			if(charge < maxCharge) {
				if(spell != null) {
					if(source.getStats().canCast(spell.getCost() * (charge + Theater.getDeltaSpeed(0.025f)))) {
						if(source.getEquipment().getWeapons().isTwoHand() && source.getStats().canCast(spell.getCost() * (charge + (Theater.getDeltaSpeed(0.025f) * 2)))) {
							charge += Theater.getDeltaSpeed(0.025f) * 2;
						} else {
							charge += Theater.getDeltaSpeed(0.025f);
						}
					}
				} else {
					charge += Theater.getDeltaSpeed(0.025f);
				}
				if(charge > maxCharge)
					charge = maxCharge;
				
				scale();
			}
		} else {
			ArrayList<Actor> targets = hitTargets();
			if(!targets.isEmpty()) {
				for(int x = 0; x<targets.size(); x++) {
					damage(targets.get(x), getDir());
				}
				contact = true;
			}
			if(getDistance() > 1) {
				setTrajectory();
				checkMove(stage);
				if(dx != 0 || dy != 0) {
					move();
					setDistance(getDistance() - (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)));
					PropQuad.get().update(this);
				} else {
					setAlive(false);
				}
			} else {
				setAlive(false);
			}
		}
	}
	
	public void checkMove(Stage stage) {
		Rectangle2D tempBox = new Rectangle2D.Double(getBox().getX(), getBox().getY(), getBox().getWidth(), getBox().getHeight());
		ArrayList<Rectangle2D> rects;
		ArrayList<Rectangle2D> flatRects;
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
				
				if(PropQuad.get().getTopQuad().contains(tempBox)) {
					if(rects != null) {
						for(int x = 0; x<rects.size(); x++) {
							if(tempBox.intersects(rects.get(x))) {
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
								xSteps = 0f;
								dx -= stepX;
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
				
				if(PropQuad.get().getTopQuad().contains(tempBox)) {
					if(rects != null) {
						for(int x = 0; x<rects.size(); x++) {
							if(tempBox.intersects(rects.get(x))) {
								ySteps = 0f;
								dy -= stepY;
								break;
							}
						}
					}
					// Add in terrain specific checks better
					if(!floating && flatRects != null) {
						for(int x = 0; x<flatRects.size(); x++) {
							if(tempBox.intersects(flatRects.get(x))) {
								ySteps = 0f;
								dy -= stepY;
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
		
		if(dx != 0f && dy != 0f) {
			dx = dx / (float) Math.sqrt(2);
			dy = dy / (float) Math.sqrt(2);
		}
	}
	
	public void fire(Stage stage) {
		PropQuad.get().update(this);
		charging = false;
		if(spell != null) {
			setDistance(spell.getDistance() / charge);
		} else if(ammo != null) {
			setDistance(ammo.getDistance() * charge);
		}
		stage.effects.add(this);
	}
	
	public void damage(Actor target, int dir) {
		// Play hit sound effect
		Ensemble.get().playSoundEffect(new SoundEffect(target.getHitSound(), 1f));

		// Cancel movement from target
		target.setDistance(0);

		// Calculate damage from weapon
		float damage = 1f; // this.getEquipment().getWeapons().getWeaponCrit(target.getEquipment()) * this.getEquipment().getWeapons().getWeaponDamage();
		if(spell != null) {
			damage = spell.getDamage() * charge;
		} else if(ammo != null) {
			damage = ammo.getDamage() * charge;
		}
		System.out.println("Damage " + damage);
		if(damage <= 0)
			damage = 1f;

		if(damage > 0) {
			// Calculate knock back
			float knockback = (damage / target.getStats().getVitality().getMax());
			if(spell != null) {
				knockback += spell.getDamage();
			} else if(ammo != null) {
				knockback += ammo.getDamage();
			}
			/* TODO Magical defence?
			if(target.isDefending()) {
				knockback = knockback / 2f;
				target.cancelDefence();
				this.cancelAttack();
				this.setState(CharState.HURT);
				this.knockback(0, 0.6f, -1, 0, 0);
			}*/
			System.out.println(knockback + " Knockback");
			
			if(target.getState() != CharState.HURT)
				target.setState(CharState.HURT);		// Set target to hurt state

			// Apply knock back and speed to travel
			// TODO Adjust cooldown time by armor value, higher armor means faster recovery / Stability formula
			target.knockback(knockback, target.getEquipment().getArmor().getStability(), dir,
					target.getHitVector(this.getBox()).x, target.getHitVector(this.getBox()).y);
			//target.setSpeedBuff(2f / target.getStats().getVivacity().getPercentRemaining());
			target.setSpeedMod(2f / target.getStats().getVivacity().getPercentRemaining());

			// Apply damage, check for death, check to set to hostile
			target.getStats().getVitality().takeDamage(damage);
			if(target.getStats().getVitality().getStat() <= 0) {
				target.setAlive(false);
				System.out.println(target.getName() + " has died!");
			} else {
				target.getAI().setTarget(source);
				target.getAI().sendAlert(AIAlert.ATTACKED, 1f);
			}

			if(target.getInParty() == 1)		// If the target is a party member, update menu
				Theater.get().getStage().menu.doUpdate();
		}		
	}
	
	public ArrayList<Actor> hitTargets() {
		ArrayList<Rectangle2D> rects = PropQuad.get().getRects(this);
		ArrayList<Actor> actors = PropQuad.get().getActors(this);
		ArrayList<Actor> hits = new ArrayList<Actor>();
		
		for(int x = 0; x<rects.size(); x++) {
			if(getBox().intersects(rects.get(x)))
				setAlive(false);
		}
		for(int x = 0; x<actors.size(); x++) {
			if(getBox().intersects(actors.get(x).getBox()) && actors.get(x).isAlive() && actors.get(x) != source) {
				hits.add(actors.get(x));
				if(!piercing) {
					setAlive(false);
					break;
				}
			}
		}
		
		return hits;
	}

	public void scale() {
		if(spell != null) {
			setSpeed(spell.getSpeed() / charge);
			setScale(charge);
		} else if(ammo != null) {
			setSpeed(ammo.getSpeed() * charge);
			setScale(1f / (charge / 2f));
		}
	}
	
	public void adjustLocation(Weapon weapon) {
		Point2D start = new Point2D.Double();
		switch(getDir()) {
		case(0):
			start.setLocation(weapon.getBox().getCenterX(), weapon.getBox().getMaxY());
			break;
		case(1):
			start.setLocation(weapon.getBox().getMaxX(), weapon.getBox().getY());
			break;
		case(2):
			start.setLocation(weapon.getBox().getX() - this.getBox().getWidth(), weapon.getBox().getY());
			break;
		case(3):
			start.setLocation(weapon.getBox().getCenterX(), weapon.getBox().getY() - this.getBox().getHeight());
			break;
		}
		this.setLocation((float) start.getX(), (float) start.getY());
	}
	
	public void setTrajectory() {
		switch(getDir()) {
		case(0):
			dy = 1;
			break;
		case(1):
			dx = 1;
			break;
		case(2):
			dx = -1;
			break;
		case(3):
			dy = -1;
			break;
		}
	}
	
	public void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
		if(scale > 1f) {
			switch(getDir()) {
			case(0):
			case(3):
				this.x = (float) (x - (getBox().getWidth() / 3));
				break;
			case(1):
			case(2):
				this.y = (float) (y - (getBox().getHeight() / 3));
				break;
			}
		}
	}
	
	public void setScale(float scale) {
		this.scale = scale;
		resizeBox(0, 0, scale, scale);
		sprite.setScale(scale);
	}
	
	public boolean isCharging() {
		return charging;
	}
	
	public float getCharge() {
		return charge;
	}
	
	public boolean madeContact() {
		return contact;
	}
	
	@Override
	public void die(Stage stage) {
		stage.remove(this);
		if(!madeContact()) {
			if(spell != null) {
				int chance = (int)(Math.random() * this.charge);
				if(chance > 1) {
					Prop essenceSpring = Prop.loadProp("Essence Spring", (float)getBox().getX(), (float)getBox().getY());
					String[] action = new String[]{"@Siphon Virtue:" + this.spell.getCost(), "@"};
					essenceSpring.setAction(new Action(null, action));
					stage.add(essenceSpring);
				}
			} else if(ammo != null) {
				if(Math.random() <= ammo.getBreakChance()) {
					Prop droppedAmmo = Prop.loadProp(ammo.getName(), (float)getBox().getX(), (float)getBox().getY());
					droppedAmmo.setContainer(new ItemContainer(null));
					droppedAmmo.getContainer().addItem(ammo.getID() + "");
					droppedAmmo.getContainer().setRemoveOnEmpty(true);
					String[] action = {"@Loot:" + droppedAmmo.getID(), "@"};
					droppedAmmo.setAction(new Action(null, action));
					droppedAmmo.setFlat(true);
					droppedAmmo.getSprite().shadow = null;
					stage.add(droppedAmmo);
				}
			}
		}
	}

	@Override
	public void dodge(float distance, float cooldown, float dx, float dy) {		
	}
	
}
