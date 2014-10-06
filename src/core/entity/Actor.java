package core.entity;

import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import core.Theater;
import core.entity.Entity;
import core.entity.ai.AI;
import core.entity.ai.AIAlert;
import core.entity.ai.Hostility;
import core.entity.character.Stats;
import core.equipment.Equipment;
import core.equipment.ItemContainer;
import core.render.Shadow;
import core.scene.PropQuad;
import core.scene.Stage;
import core.utilities.actions.Action;
import core.utilities.pathfinding.CourseNode;
import core.utilities.sounds.Ensemble;
import core.utilities.sounds.SoundEffect;

public class Actor extends Moving {
		
	private boolean stationary;
	private int lockDir = -1;
	protected CharState state;
	private float cooldown;
	private AI ai;
	private Stats stats;
	private Equipment equipment;
	private Costume costume;
	/* TODO Change to a variable */
	private long deathDebt = 0L;
	private int inParty = 0;
	private String hitSound = "Hit.ogg";
		
	public Actor(String ref, float x, float y, boolean solid) {
		super(ref, x, y, solid);
		setSpeed(2.0f);
		ai = new AI(this, Hostility.CUSTOM);
		setStats(new Stats());
		setCostume(new Costume(this));
		setEquipment(new Equipment());
		setState(CharState.IDLE);
	}

	public static Actor loadActor(String ID, float x, float y) {
		BufferedReader reader;
		
		try {
			reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/database/entities"));

	    	String line;
	    	while((line = reader.readLine()) != null) {
	    		final String[] temp = line.split(";");
	    		
	    		if(temp[0].matches(ID)) {
	    			Actor actor = new Actor(temp[0], x, y, Boolean.parseBoolean(temp[1])) {{
	    				resizeBox((int)(sprite.getWidth()*Float.parseFloat(temp[2])), 
	    						(int)(sprite.getHeight()*Float.parseFloat(temp[3])), 
	    						Float.parseFloat(temp[4]), Float.parseFloat(temp[5]));
	    				sprite.setShadow(new Shadow());
	    			}};
	    			
	    			reader.close();
	    			return actor;
	    		}
	    	}

	    	reader.close();
	    } catch (FileNotFoundException e) {
	    	System.out.println("The actor database has been misplaced!");
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	System.out.println("Actor database failed to load!");
	    	e.printStackTrace();
	    }
		
		System.out.println("Failed to find actor: " + ID);
		return new Actor(null, 0, 0, false);
	}
	
	public static Actor loadActor(BufferedReader reader) {
		// TODO Break up loading to be handled in respective classes
		try {
			
			String line = reader.readLine();
			String[] start = line.split(";");
			Actor actor = Actor.loadActor(start[0], Float.parseFloat(start[1]), Float.parseFloat(start[2]));
			if(start.length > 3)
				actor.setID(start[3]);
			
			while((line = reader.readLine()) != null && !line.matches("<END>")) {
				if(line.matches("<SPEED>")) {
					while((line = reader.readLine()) != null && !line.matches("<BREAK>")) {
						String[] temp = line.split(";");
						actor.setSpeed(Float.parseFloat(temp[0]));
					}
				} else if(line.matches("<AI>")) {
					actor.ai = AI.loadAI(reader, actor);
				} else if(line.matches("<STATS>")) {
					while((line = reader.readLine()) != null && !line.matches("<BREAK>")) {
						String[] temp = line.split(";");
						if(temp[0].matches("VITALITY")) {
							actor.getStats().getVitality().load(temp);
						} else if(temp[0].matches("VIGOR")) {
							actor.getStats().getVigor().load(temp);
						} else if(temp[0].matches("VIVACITY")) {
							
						} else if(temp[0].matches("VIRTUE")) {
							actor.getStats().getVirtue().load(temp);
						}
					}
				} else if(line.matches("<EQUIPMENT>")) {
					while((line = reader.readLine()) != null && !line.matches("<BREAK>")) {
						String[] temp = line.split(";");
						switch(Integer.parseInt(temp[0])) {
						case(0):
							actor.getEquipment().setGold(Integer.parseInt(temp[1]));
						break;
						case(1):
							actor.getEquipment().getArmor().load(temp);
							break;
						case(2):
							actor.getEquipment().getWeapons().load(temp);
							break;
						case(3):
							actor.getEquipment().getPockets().load(temp);
							break;
						case(4):
							actor.getEquipment().getQuiver().load(temp);
							break;
						case(5):
							actor.getEquipment().getSpellbook().load(temp);
							break;
						}
					}
				} else if(line.matches("<DIALOGUE>")) {
					String switches = reader.readLine();
					ArrayList<String> temp = new ArrayList<String>();
					while((line = reader.readLine()) != null && !line.matches("<BREAK>")) {
						temp.add(line);
					}
					actor.setAction(new Action(switches, temp.toArray(new String[temp.size()])));
				} else if(line.matches("<SOUND>")) {
					while((line = reader.readLine()) != null && !line.matches("<BREAK>")) {
						String[] temp = line.split(";");
						actor.setHitSound(temp[0]);
					}
				}
			}
			
			return actor;
		} catch (FileNotFoundException e) {
	    	System.out.println("The actor database has been misplaced!");
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	System.out.println("Actor database failed to load!");
	    	e.printStackTrace();
	    }

		return new Actor(null, 0, 0, false);
	}
	
	public void update(Stage stage) {
		stats.update();
		/*if(this.isAlive() && stats.isDead()) {
			this.alive = false;
		}*/
		
		if(!course.isEmpty()) {
			followCourse(stage);
		} else {
			if((dx != 0f || dy != 0f) && !inCooldownMoveState()) {
				checkMove(stage);
				if(dx != 0f || dy != 0f)
					move();
				dx = 0f;
				dy = 0f;
			}
		}
		
		costume.update();
		
		if(cooldown > 0) {
			cooldown -= Theater.getDeltaSpeed(0.025f);
			if(cooldown < 0)
				cooldown = 0;
		}
		
		if(getEquipment().getWeapons().getHeldRight().isSwinging()) {
			setActing(true);
			getEquipment().getWeapons().getHeldRight().update(stage, this);
			if(getEquipment().getWeapons().getHeldRight().isDefending()) {
				if(!isDefending()) {
					setState(CharState.DEFEND);
				}
			} else if(!getEquipment().getWeapons().getHeldRight().isCharging()) {
				if(state != CharState.ATTACKING)
					setState(CharState.ATTACKING);
				if(getEquipment().getWeapons().getHeldRight().getFrame() < getMaxFrame()) {
					sprite.frame = getEquipment().getWeapons().getHeldRight().getFrame();
					setActing(false);
				}
				if(!getEquipment().getWeapons().getHeldRight().isParrying())
					attack();
				if(getEquipment().getWeapons().getHeldRight().swung()) {
					setState(CharState.IDLE);
				}
			}
		} else if(getEquipment().getWeapons().getHeldLeft().isSwinging()) {
			setActing(true);
			getEquipment().getWeapons().getHeldLeft().update(stage, this);
			if(getEquipment().getWeapons().getHeldLeft().isDefending()) {
				if(!isDefending()) {
					setState(CharState.DEFEND);
				}
			} else if(!getEquipment().getWeapons().getHeldLeft().isCharging()) {
				setState(CharState.ATTACKING);
				if(getEquipment().getWeapons().getHeldLeft().getFrame() < getMaxFrame()) {
					sprite.frame = getEquipment().getWeapons().getHeldLeft().getFrame();
					setActing(false);
				}
				if(!getEquipment().getWeapons().getHeldLeft().isParrying())
					attack();
				if(getEquipment().getWeapons().getHeldLeft().swung()) {
					setState(CharState.IDLE);
				}
			}
		}
		
		switch(getState()) {
		case DODGING:
		case HURT:
			setActing(true);
			if(cooldown <= 0) {
				setState(CharState.IDLE);
				getStats().getVivacity().setActive(false);
				setSpeedMod(1f);
				dx = 0f;
				dy = 0f;
				lockDir = -1;
			}
			break;
		}
		if(!isActing() && cooldown == 0 && inMovingState() && !isDefending() && isAlive()) {
			setState(CharState.IDLE);
		}
	}
	
	public void draw() {
		if(getState() == CharState.ATTACKING || isDefending()) {
			if(getEquipment().getWeapons().getHeldRight().isSwinging()) {
				if(getDir() == 2 || getDir() == 3)
					getEquipment().getWeapons().getHeldRight().draw();
			} else if(getEquipment().getWeapons().getHeldLeft().isSwinging()) {
				if(getDir() == 1 || getDir() == 3)
					getEquipment().getWeapons().getHeldLeft().draw();
			}
		}
		
		if(Theater.get().getScreen().camera.intersects(fullBox) || sprite.still) {
			this.sprite.draw(x, y);
			this.costume.draw();
			
			if(Theater.debug()) {
				GL11.glDisable(GL11.GL_TEXTURE_2D);

				GL11.glPushMatrix();
				GL11.glTranslatef((float)(getBox().getX() - Theater.get().getScreen().camera.getX()), (float)(getBox().getY() - Theater.get().getScreen().camera.getY()), 0);
				GL11.glColor3f(0.0f, 0.5f, 1.0f);
				GL11.glLineWidth(1.0f);

				GL11.glBegin(GL11.GL_LINE_LOOP);
				{
					GL11.glVertex2f(0, 0);
					GL11.glVertex2f((float) getBox().getWidth(), 0);
					GL11.glVertex2f((float) getBox().getWidth(), (float) getBox().getHeight());
					GL11.glVertex2f(0, (float) getBox().getHeight());
				}
				GL11.glEnd();
				GL11.glPopMatrix();

				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}
		}
		
		if(getState() == CharState.ATTACKING || isDefending()) {
			if(getEquipment().getWeapons().getHeldRight().isSwinging()) {
				if(getDir() == 0 || getDir() == 1)
					getEquipment().getWeapons().getHeldRight().draw();
			} else if(getEquipment().getWeapons().getHeldLeft().isSwinging()) {
				if(getDir() == 0 || getDir() == 2)
					getEquipment().getWeapons().getHeldLeft().draw();
			}
		}
	}
	
	public void iterateState() {
		switch(getState()) {
		case DEAD:
			changeSprite("Bonepile");
			break;
		default:
			changeSprite(name + getState().getSuffix());
			costume.updatePoints(name + getState().getSuffix());
			break;	
		}
	}
	
	public void move() {
		if(!inMovingState()) {
			setState(CharState.WALKING);
		}
		if((dx != 0 || dy != 0) && getState() == CharState.RUNNING) {
			stats.getVivacity().reduce();
		}
		if(Theater.get().getScreen().getFocus() == this) {
			Theater.get().getScreen().follow();
		}

		if(lockDir == -1) {
			if(Math.abs(dx) > Math.abs(dy)) {
				if(dx < 0)
					setDir(2);
				else if(dx > 0)
					setDir(1);
			} else {
				if(dy < 0)
					setDir(3);
				else if(dy > 0)
					setDir(0);
			}
		}
		
		x += dx;
		y += dy;
				
		PropQuad.get().update(this);
		setActing(true);
		updateBox();
	}
	
	public void manageAI(Stage stage) {
		if(ai != null)
			ai.update(stage);
	}
	
	/**
	 * Set a distance to send the actor and a duration for the move.
	 * @param distance Distance to be moved.
	 * @param cooldown Duration of movement.
	 */
	public void dodge(float distance, float cooldown, float dx, float dy) {
		if(getState() != CharState.DODGING)
			setState(CharState.DODGING);
		this.getStats().getVivacity().setStat(this.getStats().getVivacity().getStat() - (this.getStats().getVivacity().getMax() / 10f));
		this.getStats().getVivacity().setActive(true);
		this.course.clear();
		this.course.add(new CourseNode(distance, dx, dy));
		this.cooldown = cooldown;
	}
	
	public void knockback(float distance, float cooldown, int dir, float dx, float dy) {
		System.out.println("Knockback: " + distance + " Cooldown: " + cooldown + " " + dx + " " + dy);
		if(getState() != CharState.HURT)
			setState(CharState.HURT);
		this.lockDir = dir;
		this.course.clear();
		this.course.add(new CourseNode(distance, dx, dy));
		this.cooldown = cooldown;
	}

	public void startAttack(boolean right) {
		setState(CharState.ATTACKING);
		if(right)
			getEquipment().getWeapons().getHeldRight().attack(getDir(), getBox(), true);
		else
			getEquipment().getWeapons().getHeldLeft().attack(getDir(), getBox(), false);
	}
	
	public void releaseAttack(boolean right, Stage stage) {
		if(right) {
			if(getEquipment().getWeapons().getHeldRight().isDefending() || getEquipment().getWeapons().getHeldRight().isCharging()) {
				getEquipment().getWeapons().getHeldRight().release(stage, this);
				setState(CharState.IDLE);
			}
		} else {
			if(getEquipment().getWeapons().getHeldLeft().isDefending() || getEquipment().getWeapons().getHeldLeft().isCharging()) {
				getEquipment().getWeapons().getHeldLeft().release(stage, this);
				setState(CharState.IDLE);
			}
		}
	}
	
	public void cancelCharge(boolean right, Stage stage) {
		if(right) {
			if(getEquipment().getWeapons().getHeldRight().isCharging()) {
				getEquipment().getWeapons().getHeldRight().cancelCharge(stage);
				setState(CharState.IDLE);
			}
		} else {
			if(getEquipment().getWeapons().getHeldLeft().isCharging()) {
				getEquipment().getWeapons().getHeldLeft().cancelCharge(stage);
				setState(CharState.IDLE);
			}
		}
	}
	
	public void cancelAttack() {
		this.getEquipment().getWeapons().getSwingingWeapon().setInterrupt(true);
		this.getEquipment().getWeapons().getSwingingWeapon().swung();
	}
	
	public void cancelDefence() {
		this.getEquipment().getWeapons().getSwingingWeapon().setDefending(false);
		this.getEquipment().getWeapons().getSwingingWeapon().setInterrupt(true);
		this.getEquipment().getWeapons().getSwingingWeapon().swung();
	}
	
	public void attack() {
		ArrayList<Actor> targets = getEquipment().getWeapons().getSwingingWeapon().getTargets();

		for(int x = 0; x<targets.size(); x++) {
			if(targets.get(x).getState() != CharState.HURT && targets.get(x).getState() != CharState.DODGING && targets.get(x).isAlive() && targets.get(x) != this) {
				if(targets.get(x).isParrying()) {
					parryStun();
					break;
				} else {
					damage(targets.get(x), getDir());
				}
			}
		}
	}
	
	public boolean canAttack() {
		if(getEquipment().getWeapons().getHeldRight().isSwinging() || getEquipment().getWeapons().getHeldLeft().isSwinging() || getState() == CharState.HURT) {
			return false;
		}

		return true;
	}
	
	public float getAttackRange(int dir) {
		return equipment.getWeapons().getEquippedItemRight().getAttackRange(dir);
	}
	
	public boolean canHit(Actor target) {
		for(int x = 0; x<sprite.getMaxDir(); x++) {
			if(getEquipment().getWeapons().getHeldRight().getHitRange(x, getBox(), true).intersects(target.getBox()) 
					|| getEquipment().getWeapons().getHeldLeft().getHitRange(x, getBox(), false).intersects(target.getBox())) {
				return true;
			}
		}
		
		return false;
	}
	
	public void damage(Actor target, int dir) {
		Ensemble.get().playSoundEffect(new SoundEffect(target.getHitSound(), 1f));		// Play hit sound effect
		
		target.setDistance(0);		// Cancel movement from target
		
		// Calculate damage from weapon
		float damage = this.getEquipment().getWeapons().getWeaponCrit(target.getEquipment(), target.isDefending() ? target.getEquipment().getWeapons().getSwingingItem() : null)
					* this.getEquipment().getWeapons().getWeaponDamage();
		System.out.println("Damage " + damage);
		if(damage <= 0)
			damage = 1f;
		float vigor = 1.0f;
		
		// Set up low vigor debuff
		if(!stats.canSwingFullForce()) {
			vigor = 0;
			if(damage > 0)
				damage = damage * 0.25f;
		}
		
		if(damage > 0) {
			// Set vigor to be drained
			vigor = (5 / this.getEquipment().getWeapons().getSwingingItem().getWeaponDamage()) * 0.1f;
			if(vigor < 0.5f && stats.canSwingFullForce())
				vigor = 0.5f;
			else if(vigor > 2f)
				vigor = 2f;
			
			// Calculate knock back
			float knockback = this.getAttackRange(0) + (damage / target.getStats().getVitality().getMax());
			Vector2f knockVector = target.getHitVector(this.getEquipment().getWeapons().getSwingingWeapon().getBox());
			if(target.isDefending()) {
				knockback = knockback / 2f;
				target.cancelDefence();
				this.cancelAttack();
				this.setState(CharState.HURT);
				this.knockback(0, 0.6f, -1, 0, 0);
			}
			//System.out.println(knockback + " Knockback");
			
			if(target.getState() == CharState.ATTACKING)
				target.cancelAttack();
			if(target.getState() != CharState.HURT)		// If not hurt, set target to hurt state
				target.setState(CharState.HURT);
			
			// Apply knock back and speed to travel
			// TODO Adjust cooldown time by armor value, higher armor means faster recovery / Stability formula
			target.knockback(knockback, target.getEquipment().getArmor().getStability(), dir, knockVector.x, knockVector.y);
			//target.setSpeedBuff(2f / target.getStats().getVivacity().getPercentRemaining());
			target.setSpeedMod(2f / target.getStats().getVivacity().getPercentRemaining());
			
			// Apply damage, check for death, check to set to hostile
			target.getStats().getVitality().takeDamage(damage);
			if(target.getStats().getVitality().getStat() <= 0) {
				target.setAlive(false);
				System.out.println(target.getName() + " has died!");
			} else {
				target.getAI().setTarget(this);
				target.getAI().sendAlert(AIAlert.ATTACKED, 1f);
			}

			if(target.getInParty() == 1)		// If the target is a party member, update menu
				Theater.get().getStage().menu.doUpdate();
		}
		stats.getVigor().consumeVigor(this.getEquipment().getWeapons().getSwingingItem().getVigorCost() * vigor);
	}

	public void faceTarget(Entity target) {
		int faceX = (int)(this.getBox().getCenterX() - target.getBox().getCenterX());
		int faceY = (int)(this.getBox().getCenterY() - target.getBox().getCenterY());
		
		if(Math.abs(faceX) > Math.abs(faceY)) {
			if(faceX > 0)
				this.setDir(2);
			else
				this.setDir(1);
		} else {
			if(faceY > 0)
				this.setDir(3);
			else
				this.setDir(0);
		}
	}
	
	public Vector2f getOppositeFace() {
		switch(getDir()) {
		case(0):
			return new Vector2f(0, -1);
		case(1):
			return new Vector2f(-1, 0);
		case(2):
			return new Vector2f(1, 0);
		case(3):
			return new Vector2f(0, 1);
		}
		
		return null;
	}
	
	public Vector2f getHitVector(Rectangle2D rect) {
		Vector2f vector = new Vector2f(0, 0);
		
		if(rect.getX() < getBox().getX() || rect.getX() < getBox().getX() + (getBox().getWidth() / 4)) {
			if(rect.getY() < getBox().getY() || rect.getY() < getBox().getY() + (getBox().getHeight() / 4)) {
				vector.set(1, 1);
			} else if(rect.getY() < getBox().getCenterY()) {
				vector.set(1, 0);
			} else {
				vector.set(1, -1);
			}
		} else if(rect.getX() < getBox().getCenterX()) {
			if(rect.getY() < getBox().getY() || rect.getY() < getBox().getY() + (getBox().getHeight() / 4)) {
				vector.set(0, 1);
			} else if(rect.getY() < getBox().getCenterY()) {
				vector.set(1, 1);
			} else {
				vector.set(0, -1);
			}
		} else {
			if(rect.getY() < getBox().getY() || rect.getY() < getBox().getY() + (getBox().getHeight() / 4)) {
				vector.set(-1, 1);
			} else if(rect.getY() < getBox().getCenterY()) {
				vector.set(-1, 0);
			} else {
				vector.set(-1, -1);
			}
		}
		
		return vector;
	}
	
	public boolean isParrying() {
		if(getEquipment().getWeapons().getSwingingWeapon() != null && getEquipment().getWeapons().getSwingingWeapon().isParrying())
			return true;
		return false;
	}
	
	public void parryStun() {
		// TODO Ensemble.get().playSoundEffect(new SoundEffect(target.getHitSound(), 1f));	// Play parry sound effect
		
		this.cancelAttack();
		this.setState(CharState.HURT);
		this.dodge(0, 1.2f, 0, 0);
	}
	
	public void interactWith(ArrayList<Entity> targets, Stage stage) {
		Entity target = null;
		for(int x = 0; x<targets.size(); x++) {
			if(targets.get(x).getAction() != null && targets.get(x) != this) {
				if(this.getBox().intersects(targets.get(x).getBox())) {
					target = targets.get(x);
				} else {
					switch(this.getDir()) {
					case(0):
						if(this.getBox(0, (float) (this.getBox().getHeight() * 1.5f)).intersects(targets.get(x).getBox()))
							target = targets.get(x);
						break;
					case(1):
						if(this.getBox((float) (this.getBox().getWidth() * 1.5f), 0).intersects(targets.get(x).getBox()))
							target = targets.get(x);
						break;
					case(2):
						if(this.getBox((float) (this.getBox().getWidth() * -1.5f), 0).intersects(targets.get(x).getBox()))
							target = targets.get(x);
						break;
					case(3):
						if(this.getBox(0, (float) (this.getBox().getHeight() * -1.5f)).intersects(targets.get(x).getBox()))
							target = targets.get(x);
						break;
					}
				}
			}
		}
		if(target != null && !stage.script.interactActive()) {
			stage.script.setInteractPrompt(true);
			target.getAction().setInteractions(target, this);
			stage.script.addAction(target.getAction());
		}
	}
	
	@Override
	public void die(Stage stage) {
		stage.cast.remove(this);
		if(stage.party.getMember(this) != null)
			stage.party.removeMember(this);
		PropQuad.get().removeProp(this);
		stage.scenery.remove(this);
		
		Prop bonepile = Prop.loadProp("Bonepile", (float)this.getBox().getX(), (float)this.getBox().getY());
		bonepile.setAlive(false);
		stage.player.getEquipment().addGold(this.getEquipment().getGold());
		bonepile.setContainer(new ItemContainer(this));
		String[] action = {"@Loot:" + bonepile.getID(), "@"};
		bonepile.setAction(new Action(null, action));
		bonepile.getSprite().shadow = null;
		bonepile.setFlat(true);
		
		stage.props.add(bonepile);
		PropQuad.get().update(bonepile);
		stage.scenery.add(0, bonepile);
	}
	
	public void revive() {
		setState(CharState.IDLE);
		this.stats.getVitality().restore();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isStationary() {
		return stationary;
	}

	public void setStationary(boolean stationary) {
		this.stationary = stationary;
	}
	
	public void setLockDir(int dir) {
		this.lockDir = dir;
	}

	public CharState getState() {
		return state;
	}

	public void setState(CharState state) {
		this.state = state;
		iterateState();
	}
	
	/**
	 * @return true if attacking, defending, hurt, dead or dodging
	 */
	public boolean inBusyState() {
		if(getState() == CharState.ATTACKING || getState() == CharState.DEFEND || getState() == CharState.HURT || getState() == CharState.DEAD || getState() == CharState.DODGING)
			return true;
		
		return false;
	}
	
	/**
	 * @return true if not idle, defending, attacking and dead
	 */
	public boolean inMovingState() {
		if(getState() != CharState.IDLE && getState() != CharState.DEFEND && getState() != CharState.ATTACKING && getState() != CharState.DEAD)
			return true;
		
		return false;
	}
	
	/**
	 * @return true if not hurt or dodging
	 */
	public boolean inCooldownMoveState() {
		if(getState() == CharState.HURT || getState() == CharState.DODGING)
			return true;
		return false;
	}
	
	public boolean isDefending() {
		if(state == CharState.DEFEND) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int getDir() {
		if(lockDir != -1)
			return lockDir;
		
		return super.getDir();
	}
	
	public float getCooldown() {
		return cooldown;
	}

	public void setCooldown(float cooldown) {
		this.cooldown = cooldown;
	}

	public AI getAI() {
		return ai;
	}
	
	public void setAI(AI ai) {
		this.ai = ai;
	}

	public Stats getStats() {
		return stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}

	public Costume getCostume() {
		return costume;
	}
	
	public void setCostume(Costume costume) {
		this.costume = costume;
	}
	
	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}
	
	public long getDeathDebt() {
		return deathDebt;
	}

	public void setDeathDebt(long deathDebt) {
		this.deathDebt = deathDebt;
	}

	public int getInParty() {
		return inParty;
	}

	public void setInParty(int inParty) {
		this.inParty = inParty;
	}

	public String getHitSound() {
		return hitSound;
	}

	public void setHitSound(String hitSound) {
		this.hitSound = hitSound;
	}
}
