package core.entity.character;

import core.entity.character.stats.Virtue;
import core.entity.character.stats.Vigor;
import core.entity.character.stats.Vitality;
import core.entity.character.stats.Vivacity;

public class Stats {

	private Vitality vitality;
	private Vigor vigor;
	private Vivacity vivacity;
	private Virtue virtue;
	
	public Stats() {
		setVitality(new Vitality());
		setVigor(new Vigor());
		setVivacity(new Vivacity());
		setVirtue(new Virtue());
	}
	
	public void update() {
		vitality.update();
		vigor.update();
		vivacity.update();
		virtue.update();
	}
	
	public boolean isDead() {
		if(vitality.getStat() <= 0)
			return true;
		
		return false;
	}
	
	public boolean canCast(float cost) {
		if(virtue.getStat() >= cost)
			return true;
		
		return false;
	}
	
	public boolean canSwingFullForce() {
		if(vigor.getStat() > 2.5f)
			return true;
		
		return false;
	}
	
	public boolean canRun() {
		if(vivacity.getPercentRemaining() > 0.4f && !vivacity.isExhausted())
			return true;
		
		return false;
	}
	
	public String printSimpleStats() {
		String temp = vitality.print();
		
		return temp;
	}
	
	public String print() {
		String temp = "";
		temp += vitality.print() + ";";
		temp += vivacity.print() + ";";
		temp += vigor.print() + ";";
		temp += virtue.print();
		
		return temp;
	}
		
	public Vitality getVitality() {
		return vitality;
	}

	public void setVitality(Vitality vitality) {
		this.vitality = vitality;
	}

	public Vigor getVigor() {
		return vigor;
	}

	public void setVigor(Vigor vigor) {
		this.vigor = vigor;
	}

	public Vivacity getVivacity() {
		return vivacity;
	}

	public void setVivacity(Vivacity vivacity) {
		this.vivacity = vivacity;
	}

	public Virtue getVirtue() {
		return virtue;
	}

	public void setVirtue(Virtue virtue) {
		this.virtue = virtue;
	}
}
