package core.utilities.menu;

import core.Theater;
import core.keyboard.Keybinds;
import core.scene.Stage;

public class GameMenu {

	private PartyMenu party;
	private EquipmentMenu equipment;
	private OptionsMenu options;
	private int menu = -1;
	private boolean focused = false;
	private float closing;
	
	public GameMenu() {
	}
	
	public void update(Stage stage) {
		if(focused) {
			switch(menu) {
			case(0):
				party.update(stage);
				break;
			case(1):
				equipment.update(stage);
				break;
			case(2):
				options.update(stage);
				break;
			}
			if(Keybinds.CANCEL.clicked()) {
				if((menu == 0 && party.getFocus() == null) || (menu == 1 && equipment.getFocus() == null) || (menu == 2 && options.getFocus() == null))
					focused = false;
			}
		} else {
			switch(menu) {
			case(0):
				party.movePages();
				break;
			case(1):
				equipment.movePages();
				break;
			case(2):
				options.movePages();
				break;
			}
			
			if(closing == 0) {
				if(Keybinds.CONFIRM.clicked()) {
					focused = true;
					focusOnMenu();
				} else if(Keybinds.MENU_RIGHT.clicked()) {
					if(menu < 2)
						menu++;
					else
						menu = 0;
					refreshMenu(stage);
				} else if(Keybinds.MENU_LEFT.clicked()) {
					if(menu > 0)
						menu--;
					else
						menu = 2;
					refreshMenu(stage);
				} else if(Keybinds.CANCEL.clicked()) {
					closeMenu();
				}
			} else {
				closing += Theater.getDeltaSpeed(0.025f);
				if(closing >= 0.25f)
					menu = -1;
			}
		}
	}
	
	public void draw() {
		switch(menu) {
		case(0):
			party.draw();
			break;
		case(1):
			equipment.draw();
			break;
		case(2):
			options.draw();
			break;
		}
	}
	
	public void focusOnMenu() {
		switch(menu) {
		case(0):
			party.focus();
			break;
		case(1):
			equipment.focus();
			break;
		case(2):
			options.focus();
			break;
		}
	}
	
	public void refreshMenu(Stage stage) {
		switch(menu) {
		case(0):
			party = new PartyMenu(stage.party);
			break;
		case(1):
			equipment = new EquipmentMenu(stage.party);
			break;
		case(2):
			options = new OptionsMenu(stage.map.isTown());
			break;
		}
	}
	
	public void openMenu(Stage stage) {
		closing = 0;
		menu = 0;
		party = new PartyMenu(stage.party);
		equipment = new EquipmentMenu(stage.party);
		options = new OptionsMenu(stage.map.isTown());
	}
	
	public void closeMenu() {
		switch(menu) {
		case(0):
			party.close();
			break;
		case(1):
			equipment.close();
			break;
		case(2):
			options.close();
			break;
		}
		Keybinds.closeMenu();
		closing += Theater.getDeltaSpeed(0.025f);
	}
	
	public boolean isOpen() {
		if(menu == -1)
			return false;
		
		return true;
	}
	
	public void doUpdate() {
		switch(menu) {
		case(0):
			party.refresh();
			break;
		case(1):
			equipment.refresh();
			break;
		case(2):
			options.refresh();
			break;
		}
	}
	
	public PartyMenu getParty() {
		return party;
	}
	
	public void setParty(PartyMenu party) {
		this.party = party;
	}

	public EquipmentMenu getEquipment() {
		return equipment;
	}

	public void setEquipment(EquipmentMenu equipment) {
		this.equipment = equipment;
	}

	public OptionsMenu getOptions() {
		return options;
	}

	public void setOptions(OptionsMenu options) {
		this.options = options;
	}
	
}
