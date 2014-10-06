package core.scene;

import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import core.Theater;
import core.entity.Actor;
import core.entity.CharState;
import core.entity.Entity;
import core.entity.Moving;
import core.entity.Prop;
import core.entity.character.Party;
import core.entity.projectiles.Projectile;
import core.keyboard.Keybinds;
import core.scene.weather.Rain;
import core.scene.weather.Snow;
import core.scene.weather.Weather;
import core.utilities.Save;
import core.utilities.Variables;
import core.utilities.menu.GameMenu;
import core.utilities.pathfinding.DebugNode;
import core.utilities.pathfinding.PathQuad;
import core.utilities.sounds.Ensemble;
import core.utilities.sounds.Track;
import core.utilities.text.Script;
import core.utilities.text.Textbox;

public class Stage {
	
	public Prop backdrop;
	public Map map;
	public Actor player = null;
	public Party party = new Party();
	public ArrayList<Actor> cast = new ArrayList<Actor>();
	public ArrayList<Prop> props = new ArrayList<Prop>();
	public ArrayList<Moving> effects = new ArrayList<Moving>();
	public ArrayList<Entity> scenery = new ArrayList<Entity>();
	public ArrayList<Pathway> paths = new ArrayList<Pathway>();
	public ArrayList<Firework> fireworks = new ArrayList<Firework>();
	public Weather weather = null;
	public Script script = new Script();
	public GameMenu menu = new GameMenu();
	public String scene = "Title";
	public String backgroundTrack = "";
	public boolean reload = false;
	
	public DebugNode debugNodes = new DebugNode();
		
	private long startTime;
	public static long actTime;
		
	public Stage() {
		clearScript();
		loadStage("Blank");
		Variables.loadVariables();
	}
	
	public Stage(String level) {
		clearScript();
		loadStage(level);
	}

	public void loadStage(String ref) {
		scene = ref;
		clear();
		BufferedReader reader;
		
		try {
			reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/database/scenes/" + ref));
			String line;
			String[] temp;
			while((line = reader.readLine()) != null) {
				if(line.matches("<BACKDROP>")) {
					while(!(line = reader.readLine()).matches("<END>")) {
						temp = line.split(";");
						backdrop = new Prop(temp[0], Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), false);
	    				backdrop.setStill(true);
					}
				} else if(line.matches("<BGM>")) {
					while(!(line = reader.readLine()).matches("<END>")) {
						if(!line.matches(backgroundTrack)) {
							backgroundTrack = line;
							Ensemble.get().swapBackground(new Track(backgroundTrack, 2f, 3f, true));
		    			}
					}
				} else if(line.matches("<PROP>")) {
					props.add(Prop.loadProp(reader));
				} else if(line.matches("<PLAYER>") && player == null) {
					player = Actor.loadActor(reader);
					player.setID("Player");
					player.getAI().setAllies("Player");
					player.getCostume().setHat("Cavalier");
					party.getMembers().clear();
					party.addMember(player, this);
				} else if(line.matches("<ACTOR>")) {
					cast.add(Actor.loadActor(reader));
					cast.get(cast.size() - 1).getCostume().setHat("Cavalier");
					//party.addMember(cast.get(0), this);
				} else if(line.matches("<PATHWAY>")) {
					paths.add(Pathway.loadPathway(reader));
				} else if(line.matches("<HITMAP>")) {
					addScenery();
					PathQuad.init(PathQuad.load(reader, scenery));
					PathQuad.get().buildNeighbors();
					PropQuad.init(new PropQuad(null, new Rectangle2D.Double(0, 0, PathQuad.size.width, PathQuad.size.height), scenery, PathQuad.get().getAllBlockedBoxes(),
							PathQuad.get().getAllFlatBoxes()));
				} else if(line.matches("<MAP>")) {
					map = Map.loadMap(reader);
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
	    	System.err.println("The scene file has been misplaced!");
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	System.err.println("Scene file failed to load!");
	    	e.printStackTrace();
	    }
		
		if(PathQuad.get() == null && (player != null || !cast.isEmpty() || !props.isEmpty())) {
			addScenery();
			PathQuad.init(new PathQuad(null, 0, 0, PathQuad.buildHitmap(scenery)));
			PathQuad.get().buildNeighbors();
			PropQuad.init(new PropQuad(null, new Rectangle2D.Double(0, 0, PathQuad.size.width, PathQuad.size.height), scenery, PathQuad.get().getAllBlockedBoxes(),
					PathQuad.get().getAllFlatBoxes()));
		}
				
		if(new File(System.getProperty("saves") + "/field/" + ref).exists()) {
			loadFromTemp();
		}
	}
	
	public void loadFromTemp() {
		cast.clear();
		PropQuad.clear();
		BufferedReader reader;
		
		try {
			reader = new BufferedReader(new FileReader(System.getProperty("saves") + "/field/" + scene));
			String line;
			while((line = reader.readLine()) != null) {
				if(line.matches("<ACTOR>")) {
					cast.add(Actor.loadActor(reader));
				} else if(line.matches("<PROP>")) {
					props.add(Prop.loadProp(reader));
				}
			}
		} catch (FileNotFoundException e) {
	    	System.err.println("The field save file has been misplaced!");
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	System.err.println("Field save file failed to load!");
	    	e.printStackTrace();
	    }
		
		addScenery();
		PropQuad.init(new PropQuad(null, new Rectangle2D.Double(0, 0, PathQuad.size.width, PathQuad.size.height), scenery, PathQuad.get().getAllBlockedBoxes(),
				PathQuad.get().getAllFlatBoxes()));
	}
	
	public void loadPlayer(BufferedReader reader) {
		try {
			String line;
			String[] temp;
			while((line = reader.readLine()) != null && !line.matches("<END>")) {
				if(line.matches("<LOCATION>")) {
					while(!(line = reader.readLine()).matches("<BREAK>")) {
						temp = line.split(";");
						player = Actor.loadActor("Player", Float.parseFloat(temp[1]), Float.parseFloat(temp[2]));
						loadStage(temp[0]);
					}
				} else if(line.matches("<STATS>")) {
					while(!(line = reader.readLine()).matches("<BREAK>")) {
						temp = line.split(";");
						if(temp[0].matches("VITALITY")) {
							player.getStats().getVitality().load(temp);
						} else if(temp[0].matches("VIGOR")) {
							player.getStats().getVigor().load(temp);
						} else if(temp[0].matches("VIVACITY")) {
							player.getStats().getVivacity().load(temp);
						} else if(temp[0].matches("VIRTUE")) {
							player.getStats().getVirtue().load(temp);
						}
					}
				} else if(line.matches("<EQUIPMENT>")) {
					while(!(line = reader.readLine()).matches("<BREAK>")) {
						temp = line.split(";");
						switch(Integer.parseInt(temp[0])) {
						case(0):
							player.getEquipment().setGold(Integer.parseInt(temp[1]));
							break;
						case(1):
							player.getEquipment().getArmor().load(temp);
							break;
						case(2):
							player.getEquipment().getWeapons().load(temp);
							break;
						case(3):
							player.getEquipment().getPockets().load(temp);
							break;
						case(4):
							player.getEquipment().getQuiver().load(temp);
							break;
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
	    	System.out.println("The save file has been misplaced!");
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	System.out.println("Save file failed to load!");
	    	e.printStackTrace();
	    }
		player.setID("Player");
		party.getMembers().clear();
		party.addMember(player, this);
	}
	
	public void act() {
		if(Theater.debug())
			startTime = Theater.getTime();
		// Animate and update all the scenery
		for(int x = 0; x<scenery.size(); x++) {
			scenery.get(x).animate();
			scenery.get(x).updateBox();
		}
		// Update any present effects
		for(int x = 0; x<effects.size(); x++) {
			effects.get(x).update(this);
			// TODO Add custom death effects for effects
			if(!effects.get(x).isAlive()) {
				effects.get(x).die(this);
			}
		}
		// Animate map tiles
		if(map != null) {
			map.animate();
		}
		
		script.update(this);
		Ensemble.get().update();
		if(menu.isOpen()) {
			menu.update(this);
		}
		
		if(weather != null) {
			weather.update();
		}
		
		for(int x = 0; x<fireworks.size(); x++) {
			if(fireworks.get(x).isBurntOut())
				fireworks.remove(x);
		}
		
		// NPC handling
		for(int x = 0; x<cast.size(); x++) {
			if(!cast.get(x).inBusyState() && cast.get(x).isAlive()) {
				cast.get(x).manageAI(this);
			}
			
			cast.get(x).update(this);
			
			if(!cast.get(x).isAlive() && cast.get(x).getState() != CharState.DEAD) {
				castDeath(cast.get(x));
			}
		}
		
		// Path collisions
		for(int x = 0; x<paths.size(); x++) {
			if(paths.get(x).collide(player)) {
				//System.out.println("Colliding with path! " + paths.get(x).getDestination());
				changeStage(paths.get(x).getDestination(), paths.get(x).getDestX(), paths.get(x).getDestY());
			}
		}
		
		if(reload)
			reload();

		if(Theater.debug())
			actTime = Theater.getTime() - startTime;
	}
	
	public void clear() {
		backdrop = null;
		scenery.clear();
		cast.clear();
		props.clear();
		effects.clear();
		paths.clear();
		fireworks.clear();
		//map.clear();
		clearScript();
		PathQuad.clear();
		PropQuad.clear();
		Keybinds.clear();
	}
	
	public void addScenery() {
		scenery.clear();
		scenery.add(player);
		scenery.addAll(cast);
		scenery.addAll(props);
	}
	
	public void changeStage(String ref, float x, float y) {
		//Ensemble.get().setFading(backgroundTrack, 3);
		if(!map.isTown()) {
			Save.get().saveTempField(this);
		}
		scene = ref;
		loadStage(ref);
		player.setX(x);
		player.setY(y);
		player.updateBox();
		if(map.isTown())
			Save.get().clearField();
		
		//addScenery();
		Theater.get().getScreen().setFocus(player);
		Theater.get().getScreen().centerOn();
	}
	
	public void playerDeath() {
		int holdX = (int)player.getX();
		int holdY = (int)player.getY();
		String holdDest = scene;
		changeStage("Times End", 385, 320);
		//if(paths.size() > 0)
			//paths.remove(0);
		paths.add(0, new Pathway(new Rectangle2D.Double(0,0,1,1), holdDest, holdX, holdY));
		player.setAlive(true);
	}
	
	public void castDeath(Actor actor) {
		scenery.remove(actor);
		if(party.getMember(actor) != null)
			party.removeMember(actor);
		PropQuad.get().removeProp(actor);
		cast.remove(actor);
		
		if(actor.getName().matches("Bonepile")) {
			actor.setState(CharState.DEAD);
			actor.getSprite().shadow = null;
			actor.setFlat(true);
			
			cast.add(actor);
			PropQuad.get().update(actor);
			scenery.add(0, actor);
		} else if(actor.getAction() == null ? false : actor.getAction().getActions()[0].contains("@Loot:")) {
			actor.setState(CharState.DEAD);
			actor.getSprite().shadow = null;
			actor.setFlat(true);
			actor.changeSprite(actor.getName());
			
			cast.add(actor);
			PropQuad.get().update(actor);
			scenery.add(0, actor);
		} else {
			actor.die(this);
		}
	}
	
	public void reload() {
		reload = false;
		changeStage(scene, player.getX(), player.getY());
	}
	
	public void addWeather(int weatherType) {
		switch(weatherType) {
		case(0):
			weather = new Rain(2);
			break;
		case(1):
			weather = new Snow(2, 0);
			break;
		default:
			System.out.println("That's no weather system!");
		}
	}
	
	public void add(Entity entity) {
		if(entity instanceof Prop) {
			props.add((Prop) entity);
		} else if(entity instanceof Actor) {
			cast.add((Actor) entity);
		} else if(entity instanceof Projectile) {
			effects.add((Moving) entity);
		}
		PropQuad.get().update(entity);
		scenery.add(0, entity);
	}
	
	public void remove(Entity entity) {
		if(entity instanceof Prop) {
			props.remove((Prop) entity);
		} else if(entity instanceof Actor) {
			cast.remove((Actor) entity);
		} else if(entity instanceof Projectile) {
			effects.remove((Moving) entity);
		}
		PropQuad.get().removeProp(entity);
		scenery.remove(entity);
	}
	
	public void addText(Textbox text) {
		script.addText(text);
	}
	
	public void clearScript() {
		script.clear();
	}
	
	public void removeText(Textbox text) {
		script.removeText(text);
	}
	
}
