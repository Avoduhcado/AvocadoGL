package core.items;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import core.entity.Actor;
import core.entity.character.stats.Virtue;
import core.entity.character.stats.Vigor;
import core.entity.character.stats.Vitality;
import core.entity.character.stats.Vivacity;

public class Consumable {

	private String flavorText;
	private ArrayList<Integer> stats;
	private ArrayList<Integer> effects;
	private ArrayList<Float> values;
	private ArrayList<Boolean> percents;
	private ArrayList<Float> durations;
	
	public Consumable() {
		setFlavorText("");
		stats = new ArrayList<Integer>();
		effects = new ArrayList<Integer>();
		values = new ArrayList<Float>();
		percents = new ArrayList<Boolean>();
		durations = new ArrayList<Float>();
	}
	
	// TODO Add other attributes
	public static Consumable loadConsumable(int ID) {
		BufferedReader reader;
		
		try {
			reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/database/consumables"));

	    	String line;
	    	while((line = reader.readLine()) != null) {
	    		final String[] temp = line.split(";");
	    		
	    		if(Integer.parseInt(temp[0]) == ID) {
	    			reader.close();
	    			
	    			return new Consumable() {{
	    				setFlavorText(temp[1]);
			    		for(int x = 2; x<temp.length; x++) {
			    			addStat(Integer.parseInt(temp[x]));
			    			addEffect(Integer.parseInt(temp[x+1]));
			    			addValue(Float.parseFloat(temp[x+2]));
			    			addPercent(Boolean.parseBoolean(temp[x+3]));
			    			addDuration(Float.parseFloat(temp[x+4]));
			    			x += 4;
			    		}
	    			}};
		    		
	    		}
	    	}

	    	reader.close();
	    } catch (FileNotFoundException e) {
	    	System.out.println("The consumable database has been misplaced!");
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	System.out.println("Consumable database failed to load!");
	    	e.printStackTrace();
	    }
		
		return null;
	}

	// This is more than likely going to end up being XBOX HUGE
	public void cycleEffects(Actor player) {
		for(int x = 0; x<stats.size(); x++) {
			boolean percent = percents.get(x);
			float value = values.get(x);
			float duration = durations.get(x);
			switch(stats.get(x)) {
			case(1):
				// Vitality
				Vitality vitality = player.getStats().getVitality();
				switch(effects.get(x)) {
				case(1):
					if(percent)
						vitality.buffMax(vitality.getMax() / value, duration);
					else
						vitality.buffMax(value, duration);
					break;
				case(2):
					if(percent)
						vitality.buffStat(vitality.getStat() / value, duration);
					else
						vitality.buffStat(value, duration);
					break;
				}
				break;
			case(2):
				// Vigor
				Vigor vigor = player.getStats().getVigor();
				switch(effects.get(x)) {
				case(1):
					if(percent)
						vigor.buffMax(vigor.getMax() / value, duration);
					else
						vigor.buffMax(value, duration);
					break;
				case(2):
					if(percent)
						vigor.buffStat(vigor.getStat() / value, duration);
					else
						vigor.buffStat(value, duration);
					break;
				}
				break;
			case(3):
				// Essence
				Virtue essence = player.getStats().getVirtue();
				switch(effects.get(x)) {
				case(1):
					if(percent)
						essence.buffMax(essence.getMax() / value, duration);
					else
						essence.buffMax(value, duration);
					break;
				case(2):
					if(percent)
						essence.buffStat(essence.getStat() / value, duration);
					else
						essence.buffStat(value, duration);
					break;
				}
				break;
			case(4):
				// Vivacity
				Vivacity vivacity = player.getStats().getVivacity();
				switch(effects.get(x)) {
				case(1):
					if(percent)
						vivacity.buffMax(vivacity.getMax() / value, duration);
					else
						vivacity.buffMax(value, duration);
					break;
				case(2):
					if(percent)
						vivacity.buffStat(vivacity.getStat() / value, duration);
					else
						vivacity.buffStat(value, duration);
					break;
				}
				break;
			}
		}
	}
	
	public String getFlavorText() {
		return flavorText;
	}

	public void setFlavorText(String flavorText) {
		this.flavorText = flavorText;
	}

	public int getEffectsTotal() {
		return stats.size();
	}
	
	public String toString() {
		String data = flavorText;
		for(int x = 0; x<getEffectsTotal(); x++)
			data += ";" + stats.get(x) + ";" + effects.get(x) + ";" + values.get(x) + ";" + percents.get(x) + ";" + durations.get(x);
		
		return data;
	}
	
	public int getStat(int x) {
		return stats.get(x);
	}
	
	public void addStat(int stat) {
		stats.add(stat);
	}

	public int getEffect(int x) {
		return effects.get(x);
	}
	
	public void addEffect(int effect) {
		effects.add(effect);
	}

	public float getValue(int x) {
		return values.get(x);
	}
	
	public void addValue(float value) {
		values.add(value);
	}

	public boolean getPercent(int x) {
		return percents.get(x);
	}
	
	public void addPercent(boolean percent) {
		percents.add(percent);
	}
	
	public float getDuration(int x) {
		return durations.get(x);
	}
	
	public void addDuration(float duration) {
		durations.add(duration);
	}
}
