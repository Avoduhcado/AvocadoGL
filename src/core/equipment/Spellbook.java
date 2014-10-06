package core.equipment;

import java.util.ArrayList;

import core.equipment.spells.Spell;

public class Spellbook {

	private ArrayList<Spell> spells = new ArrayList<Spell>();
	
	public Spellbook() {
		
	}
	
	public ArrayList<Spell> getSpells() {
		return spells;
	}
	
	public Spell getSpell(int x) {
		return spells.get(x);
	}
	
	public void setSpells(ArrayList<Spell> spells) {
		this.spells = spells;
	}
	
	public void addSpell(Spell spell) {
		spells.add(spell);
	}
	
	public String print() {
		String temp = "";
		for(int x = 0; x<spells.size(); x++) {
			temp += spells.get(x).getName() + ";";
		}
		
		return temp;
	}
	
	public void load(String[] temp) {
		spells.clear();
		for(int x = 1; x<temp.length; x++) {
			spells.add(Spell.loadSpell(temp[x]));
		}
	}
	
	public String save() {
		String temp = "5;";
		for(int x = 0; x<spells.size(); x++) {
			temp += spells.get(x).getID() + ";";
		}
		
		return temp;
	}
	
}
