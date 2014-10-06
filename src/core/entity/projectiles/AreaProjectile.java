package core.entity.projectiles;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import core.Theater;
import core.entity.Actor;
import core.equipment.spells.Spell;
import core.items.Item;
import core.scene.PropQuad;
import core.scene.Stage;
import core.utilities.exceptions.FailedTextureException;
import core.utilities.loader.TextureLoader;

public class AreaProjectile extends Projectile {

	private float duration;
	private Point2D movement;
	private Point2D center;
	private float radius;
	private Ellipse2D ellipse;
	private Texture chargeField;
	
	public AreaProjectile(String ref, Actor source, Spell spell, Item ammo) {
		super(ref, source, spell, ammo);
		this.setSpeed(2f);
		this.flat = true;
		
		if(spell != null) {
			this.radius = spell.getDiameter();
			this.duration = spell.getDuration();
			try {
				this.chargeField = TextureLoader.get().getSlickTexture(System.getProperty("resources") + "/sprites/" + spell.getAreaTexture() + ".png");
			} catch (FailedTextureException e) {
				this.chargeField = e.loadErrorTexture();
			}
		} else if(ammo != null) {
			this.radius = ammo.getDiameter();
			this.duration = ammo.getDuration();
			try {
				this.chargeField = TextureLoader.get().getSlickTexture(System.getProperty("resources") + "/sprites/" + ammo.getAreaTexture() + ".png");
			} catch (FailedTextureException e) {
				this.chargeField = e.loadErrorTexture();
			}
		}
		
		movement = new Point2D.Float(0f, 0f);
		
		ellipse = new Ellipse2D.Float(0.0f, 0.0f, radius, radius * 0.75f);
		if((spell == null && ammo == null) || ((spell != null && !spell.getMoving()) || (ammo != null && !ammo.getMoving()))) {
			center = new Point2D.Double(source.getBox().getCenterX(), source.getBox().getCenterY());
		} else {
			center = new Point2D.Double(getBox().getCenterX(), getBox().getCenterY());
		}
		contact = true;
	}

	@Override
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
				
				setTrajectory();
				checkMove(stage);
				scale();
			}
		} else {
			ArrayList<Actor> targets = hitTargets();
			if(!targets.isEmpty()) {
				for(int x = 0; x<targets.size(); x++) {
					damage(targets.get(x), getDir());
				}
			}
			duration -= Theater.getDeltaSpeed(0.025f);
			if(duration <= 0) {
				setAlive(false);
			}
		}
	}
	
	@Override
	public void draw() {
		// TODO Collect sprite batch directly from visible quadtree during update loop?
		if(Theater.get().getScreen().camera.intersects(fullBox) || sprite.still) {
			if(chargeField != null) {
				chargeField.bind();
				GL11.glPushMatrix();
				
				GL11.glTranslatef((float)(ellipse.getX() - Theater.get().getScreen().camera.getX()), (float)(ellipse.getY() - Theater.get().getScreen().camera.getY()), 0);
				GL11.glScalef(charge, charge * 0.75f, 0f);
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
		PropQuad.get().update(this);
		charging = false;
		stage.effects.add(this);
	}
	
	@Override
	public ArrayList<Actor> hitTargets() {
		ArrayList<Actor> actors = PropQuad.get().getActors(ellipse);
		ArrayList<Actor> hits = new ArrayList<Actor>();
		
		for(int x = 0; x<actors.size(); x++) {
			if(ellipse.intersects(actors.get(x).getBox()) && actors.get(x).isAlive() && actors.get(x) != source) {
				hits.add(actors.get(x));
			}
		}
		
		return hits;
	}
	
	@Override
	public void scale() {
		ellipse.setFrameFromCenter(center.getX(), center.getY(), center.getX() - ((radius * charge) / 2), center.getY() - (((radius * 0.75f) * charge) / 2));
		
		if(spell != null || ammo != null) {
			if(((spell != null && spell.getMoving()) || (ammo != null && ammo.getMoving())) && (dx != 0 || dy != 0)) {
				switch(getDir()) {
				case(0):
					movement.setLocation(0, movement.getY() + dy);
					break;
				case(1):
					movement.setLocation(movement.getX() + dx, 0);
					break;
				case(2):
					movement.setLocation(movement.getX() + dx, 0);
					break;
				case(3):
					movement.setLocation(0, movement.getY() + dy);
					break;
				}
				ellipse.setFrame((getBox().getCenterX() + movement.getX() - (ellipse.getWidth() / 2)), (getBox().getCenterY() + movement.getY() - (ellipse.getHeight() / 2)),
						ellipse.getWidth(), ellipse.getHeight());
				center = new Point2D.Double(ellipse.getCenterX(), ellipse.getCenterY());
			}
			setScale(charge);
		} else {
			setScale(1f / (charge / 2f));
		}
	}
	
}
