package core.utilities.actions;

import java.awt.geom.Rectangle2D;
import core.Theater;
import core.entity.Actor;
import core.entity.Entity;
import core.entity.Prop;
import core.items.Item;
import core.scene.Stage;
import core.utilities.Events;
import core.utilities.Variables;
import core.utilities.text.InputBox;
import core.utilities.text.LootWindow;
import core.utilities.text.PlainText;
import core.utilities.text.SelectBox;
import core.utilities.text.SpellBind;
import core.utilities.text.Textbox;

public class Action {

	/* TODO
	 * 
	 * Reduce Debt
	 * Saving input to variables - Variable exists in Event class
	 * Entities
	 * Remove text box requirement
	 */
	
	private String[] actions;
	private int position = 0;
	private boolean[] switches = new boolean[4];
	
	private Entity source;
	private Rectangle2D sourceBox;
	private Entity activator;
	private Textbox text;
	
	private boolean waiting;
	private float waitTimer;
	
	public Action(String switches, String[] actions) {
		if(switches != null) {
			String[] temp = switches.split(";");
			for(int x = 0; x<this.switches.length; x++) 
				this.switches[x] = Boolean.parseBoolean(temp[x]);
		}
		
		this.actions = actions;
		position = 0;
	}
	
	public void reset() {
		position = 0;
		waitTimer = 0;
		text = null;
	}
	
	public void update(Stage stage) {
		if(text != null) {
			text.update(stage);
		} else {
			if(waiting) {
				if(waitTimer > 0f) {
					waitTimer -= Theater.getDeltaSpeed(0.025f);
				} else {
					waiting = false;
				}
			} else {
				readAction(stage);
			}
		}
		
		if(source != null && !activator.getBox().intersects(sourceBox)) {
			this.reset();
			if(text != null)
				stage.script.removeText(text);
			stage.script.removeAction(this);
			stage.script.setInteractPrompt(false);
		}
	}

	// All actions must end with a blank @ line
	public void readAction(Stage stage) {
		String[] action = actions[position].split(":");
		
		if(action[0].contains("@")) {
			if(action[0].trim().matches("@")) {
				if(position < actions.length - 1) {
					position++;
					this.readAction(stage);
				} else {
					reset();
					stage.script.removeAction(this);
					stage.removeText(text);
					stage.script.setInteractPrompt(false);
				}
			} else {
				parseAction(action, stage);
				if(position < actions.length - 1)
					position++;
				else
					reset();
			}
		} else if(action[0].contains(">")) {
			if(action[0].contains(">End")) {
				position++;
				this.readAction(stage);
			} else {
				int indent = action[0].indexOf('>');
				String prefix = "";
				for(int x = 0; x<indent; x++)
					prefix += " ";
				if(text instanceof SelectBox) {
					int option = 0;
					for(int x = position; !actions[x].startsWith(prefix + ">End"); x++) {
						if(actions[x].startsWith(prefix + ">")) {
							if(text.getSelected() == option) {
								position = x + 1;
								this.readAction(stage);
								break;
							} else
								option++;
						}
					}
				} else {
					while(!actions[position].startsWith(prefix + ">End"))
						position++;
					position++;
					this.readAction(stage);
				}
			}
		}
	}
	
	public void parseAction(String[] action, Stage stage) {
		if(text != null) {
			stage.removeText(text);
			text = null;
		}
		
		if(action[0].contains("Wait")) {
			setWait(action);
		} else if(action[0].contains("Text")) {
			displayText(action);
		} else if(action[0].contains("Choice")) {
			showChoice(action);
		} else if(action[0].contains("Input")) {
			takeInput(action);
		} else if(action[0].contains("Adjust Money")) {
			adjustMoney(action, stage);
		} else if(action[0].contains("Revive")) {
			revive(action, stage);
		} else if(action[0].contains("Loot")) {
			loot(action, stage);
		} else if(action[0].contains("Hold Item")) {
			holdItem(action, stage);
		} else if(action[0].contains("Spell Bind")) {
			spellBind(stage);
		} else if(action[0].contains("Siphon Virtue")) {
			siphonVirtue(action, stage);
		} else if(action[0].contains("Remove This")) {
			stage.remove(source);
		} else if(action[0].contains("Condition")) {
			int indent = action[0].indexOf('@');
			String prefix = "";
			for(int x = 0; x<indent; x++)
				prefix += " ";
			
			parseCondition(action, prefix, stage);
		} else if(action[0].contains("Switch")) {
			adjustSwitches(action);
		} else if(action[0].contains("Adjust Stats")) {
			adjustStats(action, stage);
		} else if(action[0].contains("Adjust Equipment")) {
			adjustEquipment(action, stage);
		}
		
		if(text != null) {
			if(text.getSource() == null)
				text.setSource(source);
			text.setAction(this);
		}
	}
	
	public void setWait(String[] action) {
		waiting = true;
		waitTimer = Float.parseFloat(action[1]);
	}
	
	public void displayText(String[] action) {
		text = new PlainText(false, 0f, true);
		text.buildText(action[1]);
	}
	
	public void showChoice(String[] action) {
		if(Boolean.parseBoolean(action[1]))
			text = new SelectBox(true);
		else
			text = new SelectBox(false);
		text.buildText(action[2]);
	}
	
	public void takeInput(String[] action) {
		text = new InputBox(Boolean.parseBoolean(action[1]));
		text.buildText("");
	}
	
	public void adjustMoney(String[] action, Stage stage) {
		Actor temp = null;
		if(action[1].matches("activator")) {
			temp = (Actor) activator;
		} else if(action[1].matches("source")) {
			temp = (Actor) source;
		} else {
			temp = getActor(action[1], stage);
		}
		
		if(action[2].matches("all")) {
			temp.getEquipment().setGold(0);
		} else if(action[2].matches("percent")) {
			float percent = Float.parseFloat(action[3]);
			temp.getEquipment().addGold((int)(temp.getEquipment().getGold() * percent));
		} else if(action[2].matches("static")) {
			temp.getEquipment().addGold(Integer.parseInt(action[3]));
		}
	}
	
	public void revive(String[] action, Stage stage) {
		stage.player.revive();
		stage.changeStage(stage.paths.get(0).getDestination(), stage.paths.get(0).getDestX(), stage.paths.get(0).getDestY());
	}
	
	public void loot(String[] action, Stage stage) {
		text = new LootWindow(getProp(action[1], stage));
	}
	
	public void holdItem(String[] action, Stage stage) {
		// TODO Figure out when and if this ever gets used
		Item item = Item.loadItem(action[2]);
		Events.get().holdItem(item);
		text = new PlainText(false, 3.0f, false);
		text.buildText("Now holding " + item.getName() + "!");
	}
	
	public void spellBind(Stage stage) {
		text = new SpellBind(stage.player);
	}
	
	public void siphonVirtue(String[] action, Stage stage) {
		((Actor) activator).getStats().getVirtue().buffStat(Float.parseFloat(action[1]), 0);
		stage.remove(source);
	}
	
	public void parseCondition(String[] action, String prefix, Stage stage) {
		boolean condition = false;
		if(action[1].matches("Switch")) {
			switch(action[2].charAt(0)) {
			case 'a':
				if(switches[0])
					condition = true;
				break;
			case 'b':
				if(switches[1])
					condition = true;
				break;
			case 'c':
				if(switches[2])
					condition = true;
				break;
			case 'd':
				if(switches[3])
					condition = true;
				break;
			}
		} else if(action[1].matches("Variable")) {
			String[] temp = action[2].split(";");
			if(temp[1].matches("I")) {
				char check = action[3].charAt(0);
				int value = Integer.parseInt(action[3].substring(1, action[3].length()));
				switch(check) {
				case '>':
					if(Variables.get().integers.get(temp[0]) > value)
						condition = true;
					break;
				case '<':
					if(Variables.get().integers.get(temp[0]) < value)
						condition = true;
					break;
				case '=':
					if(Variables.get().integers.get(temp[0]) == value)
						condition = true;
					break;
				}
			} else if(temp[1].matches("S")) {
				if(Variables.get().strings.get(temp[0]).matches(action[3]))
					condition = true;
			} else if(temp[1].matches("B")) {
				if(Variables.get().booleans.get(temp[0]))
					condition = true;
			} else if(temp[1].matches("F")) {
				char check = action[3].charAt(0);
				float value = Float.parseFloat(action[3].substring(1, action[3].length()));
				switch(check) {
				case '>':
					if(Variables.get().floats.get(temp[0]) > value)
						condition = true;
					break;
				case '<':
					if(Variables.get().floats.get(temp[0]) < value)
						condition = true;
					break;
				case '=':
					if(Variables.get().floats.get(temp[0]) == value)
						condition = true;
					break;
				}
			}
		} else if(action[1].matches("Equipment")) {
			Actor temp = null;
			if(action[2].matches("activator")) {
				temp = (Actor) activator;
			} else if(action[2].matches("source")) {
				temp = (Actor) source;
			} else {
				temp = getActor(action[2], stage);
			}
			
			if(action[3].matches("Gold")) {
				char check = action[4].charAt(0);
				int value = Integer.parseInt(action[4].substring(1, action[4].length()));
				switch(check) {
				case '>':
					if(temp.getEquipment().getGold() > value)
						condition = true;
					break;
				case '<':
					if(temp.getEquipment().getGold() < value)
						condition = true;
					break;
				case '=':
					if(temp.getEquipment().getGold() == value)
						condition = true;
					break;
				}
			}
		}
		
		if(condition) {
			position++;
		} else {
			while(!actions[position].startsWith(prefix + ">False")) {
				position++;
			}
		}
	}
	
	public void adjustSwitches(String[] action) {
		switch(action[1].charAt(0)) {
		case 'a':
			switches[0] = Boolean.parseBoolean(action[2]);
			break;
		case 'b':
			switches[1] = Boolean.parseBoolean(action[2]);
			break;
		case 'c':
			switches[2] = Boolean.parseBoolean(action[2]);
			break;
		case 'd':
			switches[3] = Boolean.parseBoolean(action[2]);
			break;
		}
	}

	public void adjustStats(String[] action, Stage stage) {
		Actor temp = null;
		if(action[1].matches("activator")) {
			temp = (Actor) activator;
		} else if(action[1].matches("source")) {
			temp = (Actor) source;
		} else {
			temp = getActor(action[1], stage);
		}
		
		// Vitality
		if(action[2].matches("max")) {
			temp.getStats().getVitality().restore();
		} else if(action[2].matches("empty")) {
			temp.getStats().getVitality().setStat(0);
		} else {
			temp.getStats().getVitality().buffStat(Float.parseFloat(action[2]), 0);
		}
		// Vigor
		if(action[3].matches("max")) {
			temp.getStats().getVigor().restore();
		} else if(action[3].matches("empty")) {
			temp.getStats().getVigor().setStat(0);
		} else {
			temp.getStats().getVigor().buffStat(Float.parseFloat(action[3]), 0);
		}
		// Virtue
		if(action[4].matches("max")) {
			temp.getStats().getVirtue().restore();
		} else if(action[4].matches("empty")) {
			temp.getStats().getVirtue().setStat(0);
		} else {
			temp.getStats().getVirtue().buffStat(Float.parseFloat(action[4]), 0);
		}
		// Vivacity
		if(action[5].matches("max")) {
			temp.getStats().getVivacity().restore();
		} else if(action[5].matches("empty")) {
			temp.getStats().getVivacity().setStat(0);
		} else {
			temp.getStats().getVivacity().buffStat(Float.parseFloat(action[5]), 0);
		}
	}
	
	public void adjustEquipment(String[] action, Stage stage) {
		Actor temp = null;
		if(action[1].matches("activator")) {
			temp = (Actor) activator;
		} else if(action[1].matches("source")) {
			temp = (Actor) source;
		} else {
			temp = getActor(action[1], stage);
		}
		
		Item item = Item.loadItem(action[4]);
		if(action[2].matches("give")) {
			if(action[3].matches("armor")) {
				temp.getEquipment().getArmor().setGear(item.getArmorSlot(), item);
			} else if(action[3].matches("weapon")) {
				temp.getEquipment().getWeapons().setGear(item.getWeaponSlot(), item);
			} else if(action[3].matches("pocket")) {
				temp.getEquipment().getPockets().addItem(item);
			}
		} else if(action[2].matches("take")) {
			if(action[3].matches("armor")) {
				temp.getEquipment().getArmor().setGear(item.getArmorSlot(), Item.loadItem("0"));
			} else if(action[3].matches("weapon")) {
				temp.getEquipment().getWeapons().setGear(item.getWeaponSlot(), Item.loadItem("0"));
			} else if(action[3].matches("pocket")) {
				temp.getEquipment().getPockets().removeItem(item);
			}
		}
	}
	
	public Prop getProp(String ID, Stage stage) {
		for(int x = 0; x<stage.props.size(); x++) {
			if(ID.matches(stage.props.get(x).getID()))
				return stage.props.get(x);
		}
		
		return null;
	}
	
	public Actor getActor(String ID, Stage stage) {
		for(int x = 0; x<stage.cast.size(); x++) {
			if(ID.matches(stage.cast.get(x).getID()))
				return stage.cast.get(x);
		}
		
		return null;
	}
	
	public String[] getActions() {
		return actions;
	}
	
	public boolean[] getSwitches() {
		return switches;
	}
	
	public void setInteractions(Entity source, Entity activator) {
		this.source = source;
		this.sourceBox = new Rectangle2D.Double(source.getBox().getX() - (source.getBox().getWidth() / 2), source.getBox().getY() - (source.getBox().getHeight() / 2),
				source.getBox().getWidth() * 2f, source.getBox().getHeight() * 2f);
		this.activator = activator;
	}
	
	public boolean hasText() {
		if(text != null)
			return true;
		
		return false;
	}
	
	public Textbox getText() {
		return text;
	}
	
	public String saveSwitches() {
		String data = "";
		data += switches[0] + ";" + switches[1] + ";" + switches[2] + ";" + switches[3];
		
		return data;
	}
	
}
