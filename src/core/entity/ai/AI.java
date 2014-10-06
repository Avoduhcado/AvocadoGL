package core.entity.ai;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import core.entity.Actor;
import core.scene.Stage;

public class AI {
		
	private ArrayList<AIComponent> idleComponents = new ArrayList<AIComponent>();
	private ArrayList<AIComponent> activeComponents = new ArrayList<AIComponent>();
	private boolean active;
	private int highPriority = 0;
	private Actor target;
	private Hostility hostility = Hostility.NEUTRAL;
	private ArrayList<String> allies = new ArrayList<String>();
	private ArrayList<String> enemies = new ArrayList<String>();

	public AI(Actor source, Hostility hostility) {
		this.hostility = hostility;
		
		switch(hostility) {
		case TIMID:
			idleComponents.add(new IdleWander(source));
			idleComponents.add(new IdleFlee(source));
			activeComponents.add(new ActiveFlee(source));
			break;
		case COWARD:
			idleComponents.add(new IdleWander(source));
			activeComponents.add(new ActiveFlee(source));
			break;
		case NEUTRAL:
			idleComponents.add(new IdleWander(source));
			activeComponents.add(new ActiveRetaliate(source));
			break;
		case ANGRY:
			idleComponents.add(new IdleWander(source));
			activeComponents.add(new ActiveAttack(source));
			break;
		case AGGRESSIVE:
			idleComponents.add(new IdleWander(source));
			idleComponents.add(new IdlePatrol(source));
			activeComponents.add(new ActiveAttack(source));
			break;
		}
	}
	
	public static AI loadAI(BufferedReader reader, Actor source) {
		try {
			String temp = reader.readLine();
			AI ai = new AI(source, Hostility.valueOf(temp));
			temp = reader.readLine();
			if(!temp.matches("<BREAK>")) {
				ai.setAllies(reader.readLine());
				temp = reader.readLine();
				if(!temp.matches("<BREAK>"))
					ai.setEnemies(reader.readLine());
			}
			
			if(ai.hostility == Hostility.CUSTOM) {
				if(!temp.matches("<BREAK>")) {
					while((temp = reader.readLine()) != null && !temp.matches("<BREAK>")) {
						if(temp.startsWith("Idle"))
							ai.idleComponents.add(AIComponent.loadComponent(temp, source));
						else if(temp.startsWith("Active"))
							ai.activeComponents.add(AIComponent.loadComponent(temp, source));
					}
				}
			}
			
			return ai;
		} catch (FileNotFoundException e) {
	    	System.out.println("The actor database has been misplaced!");
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	System.out.println("Actor database failed to load!");
	    	e.printStackTrace();
	    }
		
		return null;
	}
	
	public void update(Stage stage) {
		if(active && !activeComponents.isEmpty()) {
			for(int x = 0; x<activeComponents.size(); x++) {
				if(activeComponents.get(x).getPriority() > activeComponents.get(highPriority).getPriority() && x != highPriority)
					highPriority = x;
			}
			
			activeComponents.get(highPriority).update(stage);
			
			if(activeComponents.get(highPriority).getPriority() < 0.3f) {
				active = false;
			}
		} else {
			if(!idleComponents.isEmpty()) {
				for(int x = 0; x<idleComponents.size(); x++) {
					idleComponents.get(x).update(stage);
				}
			}
		}
	}
	
	public void sendAlert(AIAlert alert, float urgency) {
		for(int x = 0; x<activeComponents.size(); x++) {
			activeComponents.get(x).receiveAlert(alert, urgency, hostility);
			if(activeComponents.get(x).getPriority() >= 0.3f)
				active = true;
		}
	}
	
	public boolean isAllied(Actor target) {
		for(int x = 0; x<allies.size(); x++) {
			if(target.getAI().getAllies().contains(allies.get(x)))
				return true;
		}
		
		return false;
	}
	
	public boolean areEnemies(Actor target) {
		for(int x = 0; x<enemies.size(); x++) {
			if(target.getAI().getEnemies().contains(enemies.get(x)))
				return true;
		}
		
		return false;
	}
	
	public ArrayList<AIComponent> getComponents() {
		return activeComponents;
	}

	public void addComponent(AIComponent component) {
		this.activeComponents.add(component);
	}
	
	public void setComponents(ArrayList<AIComponent> components) {
		this.activeComponents = components;
	}

	public Actor getTarget() {
		return target;
	}

	public void setTarget(Actor target) {
		this.target = target;
	}

	public Hostility getHostility() {
		return hostility;
	}

	public void setHostility(Hostility hostility) {
		this.hostility = hostility;
	}
	
	public ArrayList<String> getAllies() {
		return allies;
	}
	
	public void setAllies(String allies) {
		String[] temp = allies.split(";");
		
		for(int x = 0; x<temp.length; x++) {
			this.allies.add(temp[x]);
		}
	}
	
	public void addAlly(String ally) {
		if(!allies.contains(ally)) {
			allies.add(ally);
		}
	}
	
	public void removeAlly(String ally) {
		allies.remove(ally);
	}
	
	public ArrayList<String> getEnemies() {
		return enemies;
	}
	
	public void setEnemies(String enemies) {
		String[] temp = enemies.split(";");
		
		for(int x = 0; x<temp.length; x++) {
			this.enemies.add(temp[x]);
		}
	}
	
	public void addEnemy(String enemy) {
		if(!enemies.contains(enemy)) {
			enemies.add(enemy);
		}
	}
	
	public void removeEnemy(String enemy) {
		enemies.remove(enemy);
	}

}
