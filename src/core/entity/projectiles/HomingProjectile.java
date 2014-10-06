package core.entity.projectiles;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

import core.Theater;
import core.entity.Actor;
import core.equipment.spells.Spell;
import core.items.Item;
import core.scene.PropQuad;
import core.scene.Stage;
import core.utilities.exceptions.FailedTextureException;
import core.utilities.loader.TextureLoader;
import core.utilities.pathfinding.PathQuadStar;

public class HomingProjectile extends Projectile {

	private Actor target;
	private ArrayList<Actor> targets = new ArrayList<Actor>();
	private int maxTargets = 1;
	private float radius;
	private Ellipse2D ellipse;
	private Texture chargeField;
	
	public HomingProjectile(String ref, Actor source, Spell spell, Item ammo) {
		super(ref, source, spell, ammo);
		this.flat = true;
		
		if(spell != null) {
			this.radius = spell.getDiameter();
			try {
				this.chargeField = TextureLoader.get().getSlickTexture(System.getProperty("resources") + "/sprites/" + spell.getAreaTexture() + ".png");
			} catch (FailedTextureException e) {
				this.chargeField = e.loadErrorTexture();
			}
		} else if(ammo != null) {
			this.radius = ammo.getDiameter();
			try {
				this.chargeField = TextureLoader.get().getSlickTexture(System.getProperty("resources") + "/sprites/" + ammo.getAreaTexture() + ".png");
			} catch (FailedTextureException e) {
				this.chargeField = e.loadErrorTexture();
			}
		}
		ellipse = new Ellipse2D.Float(0.0f, 0.0f, radius, radius * 0.75f);
	}
	
	@Override
	public void update(Stage stage) {
		if(charging) {
			if(charge < maxCharge) {
				if((spell != null && spell.getMulti()) || (ammo != null && ammo.getMulti())) {
					maxTargets = (int) charge;
				}
				
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
				
				updateBox();
				findTargets();
				scale();
			}
		} else {
			if(target != null) {
				home();
				Vector2f vector = new Vector2f(this.dx, this.dy);
				checkMove(stage);
				move();
				if((this.dx == 0 && vector.x != 0) || (this.dy == 0 && vector.y != 0)) {
					System.out.println("HEY UHH NO THANKS PL:SSSSSSSSSSSSSSSSSSS");
					setAlive(false);
				}
				
				ArrayList<Actor> targets = hitTargets();
				if(!targets.isEmpty()) {
					for(int x = 0; x<targets.size(); x++) {
						damage(targets.get(x), getDir());
					}
					if(piercing && targets.contains(target))
						setAlive(false);
					contact = true;
				}
			} else {
				setAlive(false);
			}
		}
	}
	
	@Override
	public void draw() {
		// TODO Collect sprite batch directly from visible quadtree during update loop?
		if(Theater.get().getScreen().camera.intersects(fullBox) || sprite.still) {
			if(charging && chargeField != null) {
				chargeField.bind();
				GL11.glPushMatrix();
								
				GL11.glTranslatef((float)(ellipse.getX() - Theater.get().getScreen().camera.getX()), (float)(ellipse.getY() - Theater.get().getScreen().camera.getY()), 0);
				GL11.glScalef(charge, (charge * 0.75f), 0f);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

				GL11.glBegin(GL11.GL_QUADS);
				{
					GL11.glTexCoord2f(0, 0);
					GL11.glVertex2f(0, 0);
					GL11.glTexCoord2f(chargeField.getWidth(), 0);
					GL11.glVertex2f(chargeField.getImageWidth(), 0);
					GL11.glTexCoord2f(chargeField.getWidth(), chargeField.getHeight());
					GL11.glVertex2f(chargeField.getImageWidth(), chargeField.getImageHeight());
					GL11.glTexCoord2f(0, chargeField.getHeight());
					GL11.glVertex2f(0, chargeField.getImageHeight());
				}
				GL11.glEnd();
				GL11.glPopMatrix();
			}
			
			this.sprite.draw(x, y);
		}
	}
	
	@Override
	public void fire(Stage stage) {
		charging = false;
		flat = false;
		
		// Remove stationary projectile
		PropQuad.get().removeProp(this);
		stage.scenery.remove(this);	
		// Create now mobile projectiles
		for(int x = 0; x<targets.size(); x++) {
			HomingProjectile copy = this.clone();
			copy.target = this.targets.get(x);
			if(spell != null) {
				copy.setSpeed(spell.getSpeed() * (charge / 2));
			} else if(ammo != null) {
				copy.setSpeed(ammo.getSpeed() * (charge / 2));
			}
			copy.setCourse(PathQuadStar.get().findPath(copy, (float)copy.target.getBox().getCenterX(), (float)copy.target.getBox().getCenterY()));
			if(!copy.getCourse().isEmpty()) {
				PropQuad.get().update(copy);
				stage.scenery.add(copy);
				stage.effects.add(copy);
			} else
				copy = null;
		}	
	}

	@Override
	public void scale() {
		ellipse.setFrame(getBox().getCenterX() - ((radius * charge) / 2), getBox().getCenterY() - (((radius * 0.75f) * charge) / 2), radius * charge, ((radius * 0.75f) * charge));
	}
	
	@Override
	public ArrayList<Actor> hitTargets() {
		ArrayList<Actor> actors = PropQuad.get().getActors(this);
		ArrayList<Actor> hits = new ArrayList<Actor>();

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
	
	public void home() {
		float theta = (float) Math.atan2(target.getBox().getCenterX() - this.getX(), target.getBox().getCenterY() - this.getY());
		Vector2f vector = new Vector2f(this.dx, this.dy);
		
		if(target.getX() == this.getX()) {
			this.setDx(0);
			this.setDy(target.getY() > this.getY() ? 1 : -1);
		} else if(target.getY() == this.getY()) {
			this.setDx(target.getX() > this.getX() ? 1 : -1);
			this.setDy(0);
		} else {
			this.setDx((float) Math.sin(theta));
			this.setDy((float) Math.cos(theta));
		}
		
		if(vector.x < this.dx - 0.2f || vector.x < this.dx + 0.2f) {
			if(vector.x < this.dx - 0.2f)
				this.dx = vector.x + 0.2f;
			else if(vector.x < this.dx + 0.2f)
				this.dx = vector.x - 0.2f;
		}
		
		if(vector.y < this.dy - 0.2f || vector.y < this.dy + 0.2f) {
			if(vector.y < this.dy - 0.2f)
				this.dy = vector.y + 0.2f;
			else if(vector.y < this.dy + 0.2f)
				this.dy = vector.y - 0.2f;
		}
	}
	
	public void findTargets() {
		ArrayList<Actor> potentialTargets = PropQuad.get().getActors(ellipse);
		
		for(int x = 0; x<potentialTargets.size(); x++) {
			if(ellipse.intersects(potentialTargets.get(x).getBox()) && !this.targets.contains(potentialTargets.get(x)) 
					&& this.targets.size() <= maxTargets && potentialTargets.get(x).isAlive() && potentialTargets.get(x) != source) {
				this.targets.add(potentialTargets.get(x));
			} else if((!ellipse.intersects(potentialTargets.get(x).getBox()) || !potentialTargets.get(x).isAlive()) && this.targets.contains(potentialTargets.get(x))) {
				this.targets.remove(potentialTargets.get(x));
			}
		}
	}
	
	public ArrayList<Actor> getTargets() {
		return targets;
	}
	
	public void setTarget(Actor target) {
		this.target = target;
	}
	
	public HomingProjectile clone() {
		Projectile copy = new HomingProjectile(this.getName(), this.source, this.spell, this.ammo);
		copy.setX(this.getX());
		copy.setY(this.getY());
		copy.setBox(new Rectangle2D.Double(this.getBox().getX(), this.getBox().getY(), this.getBox().getWidth(), this.getBox().getHeight()));
		copy.setSpeed(this.getSpeed());
		copy.piercing = this.piercing;
		copy.charge = this.charge;
		copy.charging = false;
		
		return (HomingProjectile) copy;
	}
	
}
