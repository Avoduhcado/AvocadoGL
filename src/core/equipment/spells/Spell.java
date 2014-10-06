package core.equipment.spells;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import core.entity.projectiles.ProjectileType;

public abstract class Spell {

	protected int ID;
	protected String name;
	protected ProjectileType spellType;
	protected float cost;
	
	public static Spell loadSpell(String ref) {
		BufferedReader reader;
		
		try {
			reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/database/spells"));

	    	String line;
	    	while((line = reader.readLine()) != null) {
	    		String[] temp = line.split(";");
	    		
	    		if(temp[0].matches(ref)) {
	    			Spell spell = null;
	    			switch(ProjectileType.parseType(temp[1])) {
	    			case PROJECTILE:
	    				spell = new ProjectileSpell(Integer.parseInt(temp[0]), temp[2], Float.parseFloat(temp[3]), Float.parseFloat(temp[4]),
	    						Boolean.parseBoolean(temp[5]), Float.parseFloat(temp[6]), Float.parseFloat(temp[7]));
	    				break;
	    			case HOMING:
	    				spell = new HomingSpell(Integer.parseInt(temp[0]), temp[2], Float.parseFloat(temp[3]), Float.parseFloat(temp[4]),
	    						Boolean.parseBoolean(temp[5]), Float.parseFloat(temp[6]), Float.parseFloat(temp[7]), Boolean.parseBoolean(temp[8]), temp[9]);
	    				break;
	    			case AREA:
	    				spell = new AreaSpell(Integer.parseInt(temp[0]), temp[2], Float.parseFloat(temp[3]), Float.parseFloat(temp[4]),
	    						Float.parseFloat(temp[5]), Boolean.parseBoolean(temp[6]), Float.parseFloat(temp[7]), temp[8]);
	    				break;
	    			case ENCHANT:
	    				spell = new EnchantSpell(Integer.parseInt(temp[0]), temp[2], Float.parseFloat(temp[3]));
	    				break;
	    			}
	    			
	    			reader.close();
	    			return spell;
	    		}
	    	}

	    	reader.close();
	    } catch (FileNotFoundException e) {
	    	System.out.println("The spell database has been misplaced!");
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	System.out.println("Spell database failed to load!");
	    	e.printStackTrace();
	    }
		
		return new ProjectileSpell(0, "Blank", 0, 0, false, 0, 0);
	}
	
	public static boolean validID(int ID) {
		BufferedReader reader;
		
		try {
			reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/database/spells"));

	    	String line;
	    	while((line = reader.readLine()) != null) {
	    		String[] temp = line.split(";");
	    		
	    		if(Integer.parseInt(temp[0]) == ID) {
		    		return true;
	    		}
	    	}

	    	reader.close();
	    } catch (FileNotFoundException e) {
	    	System.out.println("The spell database has been misplaced!");
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	System.out.println("Spell database failed to load!");
	    	e.printStackTrace();
	    }
		
		return false;
	}
	
	public int getID() {
		return ID;
	}
	
	public String getName() {
		return name;
	}
	
	public ProjectileType getSpellType() {
		return spellType;
	}
	
	public float getCost() {
		return cost;
	}
	
	public abstract void cast();
	public abstract float getSpeed();
	public abstract boolean getPiercing();
	public abstract float getDistance();
	public abstract float getDamage();
	public abstract float getDiameter();
	public abstract boolean getMulti();
	public abstract boolean getMoving();
	public abstract float getDuration();
	public abstract String getAreaTexture();
	
}
