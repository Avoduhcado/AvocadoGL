package core.entity;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import core.render.Sprite;

public class Costume {

	private Actor doll;
	private Sprite[] armor = new Sprite[18];
	private AnimationPoints[] armorPoints = new AnimationPoints[18];
	private Sprite[] weapons = new Sprite[8];
	private Point2D weaponPoints;
	
	public Costume(Actor doll) {
		this.doll = doll;
		updatePoints(doll.name);
	}
	
	public void update() {
		for(int x = 0; x<armor.length; x++) {
			if(armor[x] != null)
				this.armor[x].dir = doll.getDir();
		}
		for(int x = 0; x<weapons.length; x++) {
			if(weapons[x] != null)
				this.weapons[x].dir = doll.getDir();
		}
	}
	
	public void updatePoints(String ref) {
		BufferedReader reader;
		
		try {
			reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/database/animSkeletons"));
			String line;
			
			while((line = reader.readLine()) != null) {
				if(line.matches("<" + ref + ">")) {
					armorPoints = new AnimationPoints[18];
					// Slots 1, 5, 15
					//while((line = reader.readLine()) != null && !line.matches("<BREAK>")) {
					line = reader.readLine();
					armorPoints[1] = new AnimationPoints(4, doll.getMaxFrame(), line);
					line = reader.readLine();
					armorPoints[5] = new AnimationPoints(4, doll.getMaxFrame(), line);
					line = reader.readLine();
					armorPoints[15] = new AnimationPoints(4, doll.getMaxFrame(), line);
					//}
					break;
				}
			}

			reader.close();
	    } catch (FileNotFoundException e) {
	    	System.out.println("The animation skeleton database has been misplaced!");
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	System.out.println("Animation Skeleton database failed to load!");
	    	e.printStackTrace();
	    }
	}
	
	public void draw() {
		for(int x = 0; x<armor.length; x++) {
			if(armor[x] != null) {
				this.armor[x].draw(getXDrawArmor(x), getYDrawArmor(x));
			}
		}
	}
	
	public float getXDrawArmor(int index) {
		try {
			return (float) (doll.getX() + (armorPoints[index].getPoint(doll.getDir(), doll.getFrame()).getX() - (armor[index].getWidth() / 2)));
		} catch(NullPointerException e) {
			System.out.println(doll.getName());
			System.exit(1);
		}
		return 0;
	}
	
	public float getYDrawArmor(int index) {
		return (float) (doll.getY() + (armorPoints[index].getPoint(doll.getDir(), doll.getFrame()).getY() - (armor[index].getHeight() / 2)));
	}
	
	public void equip() {
		
	}
	
	public void unequip() {
		if(armor != null)
			armor[1] = null;
	}
	
	public void setDoll(Actor doll) {
		this.doll = doll;
	}
	
	public void setHat(String ref) {
		armor[1] = Sprite.loadSprite(ref);
		//armor[1].shadow = new Shadow();
	}

	public Sprite[] getWeapons() {
		return weapons;
	}

	public void setWeapons(Sprite[] weapons) {
		this.weapons = weapons;
	}

	public Point2D getWeaponPoints() {
		return weaponPoints;
	}

	public void setWeaponPoints(Point2D weaponPoints) {
		this.weaponPoints = weaponPoints;
	}
	
}
