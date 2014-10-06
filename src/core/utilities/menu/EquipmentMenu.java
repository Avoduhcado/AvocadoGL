package core.utilities.menu;

import java.util.ArrayList;

import core.entity.character.Party;
import core.keyboard.Keybinds;
import core.scene.Stage;
import core.utilities.text.PlainText;
import core.utilities.text.SelectBox;
import core.utilities.text.Textbox;

public class EquipmentMenu extends MenuPage {

	private Party party;
	private int selectedMember = 0;
	private ArrayList<PlainText> members = new ArrayList<PlainText>();
	private int menuPage = 0;
	private SelectBox armor;
	private SelectBox weapons;
	private SelectBox quiver;
	private SelectBox pockets;
	private int selectedEquipment;
	private SelectBox equipmentSelect;
	
	public EquipmentMenu(Party party) {
		this.party = party;
		if(party == null || party.getMembers().isEmpty()) {
			PlainText temp = new PlainText(false, 0f, false);
			temp.buildText("Your party is empty.");
			temp.setStill(true);
			temp.setPositionOverTime(0 - temp.getFullWidth(), 15, 0 - temp.getFullHeight(), 15, 0.1f);	
		} else {
			for(int x = 0; x<party.getMembers().size(); x++) {
				PlainText temp = new PlainText(false, 0f, false);
				temp.setStill(true);
				temp.buildText(party.getMember(x).getName() + ";" + party.getMember(x).getEquipment().printSimple());
				if(x > 0) {
					temp.setPositionOverTime(0 - temp.getFullWidth(), 15, 15 + (x * members.get(x - 1).getFullHeight()), 15 + (x * members.get(x - 1).getFullHeight()), 0.1f);
				} else {
					temp.setPositionOverTime(0 - temp.getFullWidth(), 15, 15, 15, 0.1f);
				}
				members.add(temp);
			}
		}
	}
	
	@Override
	public void update(Stage stage) {
		for(int x = 0; x<members.size(); x++)
			members.get(x).move();
		
		if(members.contains(focus)) {	// Selecting a party member
			if(armor != null)
				armor.move();
			if(weapons != null)
				weapons.move();
			if(quiver != null)
				quiver.move();
			if(pockets != null)
				pockets.move();
			
			if(Keybinds.CONFIRM.clicked()) {				// Select an equipment page
				if(quiver != null)
					focus = quiver;
				if(weapons != null)
					focus = weapons;
				if(armor != null)
					focus = armor;
				if(pockets != null)
					focus = pockets;
				
				focus.showCursor(true);
			} else if(Keybinds.MENU_DOWN.clicked()) {		// Change current party member
				resetPreviousFocus();
				if(selectedMember + 1 < party.getMembers().size())
					selectedMember++;
				else
					selectedMember = 0;
				cycleFocus();
			} else if(Keybinds.MENU_UP.clicked()) {			// Change current party member
				resetPreviousFocus();
				if(selectedMember > 0)
					selectedMember--;
				else
					selectedMember = party.getMembers().size() - 1;
				cycleFocus();
			} else if(Keybinds.MENU_RIGHT.clicked()) {		// Change current equipment page
				if(menuPage < 3)
					menuPage++;
				else
					menuPage = 0;
				cyclePage();
			} else if(Keybinds.MENU_LEFT.clicked()) {		// Change current equipment page
				if(menuPage > 0)
					menuPage--;
				else
					menuPage = 3;
				cyclePage();
			} else if(Keybinds.CANCEL.clicked()) {			// Close the party selecting
				resetFocus();
				focus = null;
				quiver = null;
				weapons = null;
				armor = null;
				pockets = null;
				selectedMember = 0;
			}
		} else {						// Selecting an equipment page
			if(focus == armor) {				// Armor page
				armor.move();
				switch(armor.select()) {
				case(-1):
					break;
				default:
					equipmentSelect = party.getMember(selectedMember).getEquipment().getArmor().interactMenu(armor.getSelected());
					equipmentSelect.setPositionOverTime(0 - equipmentSelect.getFullWidth(), armor.getX() + armor.getFullWidth(), armor.getY(), armor.getY(), 0.1f);
					armor.showCursor(false);
					selectedEquipment = armor.getSelected();
					focus = equipmentSelect;
					break;
				}

				if(Keybinds.CANCEL.clicked()) {
					armor.showCursor(false);
					focus = members.get(selectedMember);
				}
			} else if(focus == weapons) {		// Weapons page
				weapons.move();
				switch(weapons.select()) {
				case(-1):
					break;
				default:
					equipmentSelect = party.getMember(selectedMember).getEquipment().getWeapons().interactMenu(weapons.getSelected());
					equipmentSelect.setPositionOverTime(0 - equipmentSelect.getFullWidth(), weapons.getX() + weapons.getFullWidth(), weapons.getY(), weapons.getY(), 0.1f);
					weapons.showCursor(false);
					selectedEquipment = weapons.getSelected();
					focus = equipmentSelect;
					break;
				}

				if(Keybinds.CANCEL.clicked()) {
					weapons.showCursor(false);
					focus = members.get(selectedMember);
				}
			} else if(focus == quiver) {		// Quiver page
				quiver.move();
				switch(quiver.select()) {
				case(-1):
					break;
				default:
					equipmentSelect = party.getMember(selectedMember).getEquipment().getQuiver().interactMenu(quiver.getSelected());
					equipmentSelect.setPositionOverTime(0 - equipmentSelect.getFullWidth(), quiver.getX() + quiver.getFullWidth(), quiver.getY(), quiver.getY(), 0.1f);
					quiver.showCursor(false);
					selectedEquipment = quiver.getSelected();
					focus = equipmentSelect;
					break;
				}

				if(Keybinds.CANCEL.clicked()) {
					quiver.showCursor(false);
					focus = members.get(selectedMember);
				}
			} else if(focus == pockets) {		// Pockets page
				pockets.move();
				switch(pockets.select()) {
				case(-1):
					break;
				default:
					equipmentSelect = party.getMember(selectedMember).getEquipment().getPockets().interactMenu(pockets.getSelected());
					equipmentSelect.setPositionOverTime(0 - equipmentSelect.getFullWidth(), pockets.getX() + pockets.getFullWidth(), pockets.getY(), pockets.getY(), 0.1f);
					pockets.showCursor(false);
					selectedEquipment = pockets.getSelected();
					focus = equipmentSelect;
					break;
				}

				if(Keybinds.CANCEL.clicked()) {
					pockets.showCursor(false);
					focus = members.get(selectedMember);
				}
			} else if(focus == equipmentSelect) {	// Equipment interaction window
				equipmentSelect.move();
				switch(equipmentSelect.select()) {
				case(-1):
					break;
				default:
					Textbox text = new PlainText(false, 0f, false);
					if(armor != null) {
						text = party.getMember(selectedMember).getEquipment().getArmor().interact(selectedEquipment, equipmentSelect.getSelected(), 
								party.getMember(selectedMember));
						focus = armor;
					} else if(weapons != null) {
						text = party.getMember(selectedMember).getEquipment().getWeapons().interact(selectedEquipment, equipmentSelect.getSelected(), 
								party.getMember(selectedMember));
						focus = weapons;
					} else if(quiver != null) {
						text = party.getMember(selectedMember).getEquipment().getQuiver().interact(selectedEquipment, equipmentSelect.getSelected(), 
								party.getMember(selectedMember));
						focus = quiver;
					} else if(pockets != null) {
						text = party.getMember(selectedMember).getEquipment().getPockets().interact(selectedEquipment, equipmentSelect.getSelected(), 
								party.getMember(selectedMember));
						focus = pockets;
					}
					refresh();
					text.setPositionOverTime(members.get(selectedMember).getFullWidth() + members.get(selectedMember).getX(), 
							members.get(selectedMember).getFullWidth() + members.get(selectedMember).getX(), 0 - text.getFullHeight(), members.get(selectedMember).getY(), 0.1f);
					stage.addText(text);
					equipmentSelect = null;
					focus.showCursor(true);
					Keybinds.inMenu();
					break;
				}
				
				if(Keybinds.CANCEL.clicked()) {
					if(armor != null) {
						focus = armor;
					} else if(weapons != null) {
						focus = weapons;
					} else if(quiver != null) {
						focus = quiver;
					} else if(pockets != null) {
						focus = pockets;
					}
				
					equipmentSelect = null;
					focus.showCursor(true);
				}
			} else {			// Close menu
				if(Keybinds.CANCEL.clicked()) {
					focus = null;
				}
			}
		}
	}
	
	@Override
	public void movePages() {
		for(int x = 0; x<members.size(); x++)
			members.get(x).move();
		if(quiver != null)
			quiver.move();
		if(weapons != null)
			weapons.move();
		if(armor != null)
			armor.move();
		if(pockets != null)
			pockets.move();
	}
	
	@Override
	public void draw() {
		if(quiver != null)
			quiver.draw();
		if(weapons != null)
			weapons.draw();
		if(armor != null)
			armor.draw();
		if(pockets != null)
			pockets.draw();
		if(equipmentSelect != null)
			equipmentSelect.draw();
		for(int x = 0; x<members.size(); x++) {
			members.get(x).draw();
		}
	}

	@Override
	public void focus() {
		members.get(selectedMember).setPosition(15, 15);
		members.get(selectedMember).clear();
		members.get(selectedMember).buildText(party.getMember(selectedMember).getName() + ";" + party.getMember(selectedMember).getEquipment().getGold() + " <cwhite>Bone Splinters");
		focus = members.get(selectedMember);
		
		for(int x = 1; x<members.size(); x++) {
			members.get(x).setPositionOverTime(15, 0 - members.get(x).getFullWidth(), members.get(x).getY(), members.get(x).getY(), 0.1f);
		}
		
		armor = new SelectBox(true);
		armor.setStill(true);
		armor.showCursor(false);
		armor.buildText("<iArmor Icon> <cwhite,s22,iArmor Icon>-Armor-;" + party.getMember(selectedMember).getEquipment().getArmor().print());
		armor.setPositionOverTime(0 - armor.getFullWidth(), 35, members.get(selectedMember).getY() + members.get(selectedMember).getFullHeight(),
				members.get(selectedMember).getY() + members.get(selectedMember).getFullHeight(), 0.1f);
	}
	
	@Override
	public void refresh() {
		if(members.contains(focus)) {
			for(int x = 0; x<members.size(); x++) {
				members.get(x).clear();
				members.get(x).buildText(party.getMember(x).getName() + ";" + party.getMember(x).getEquipment().getGold() + " <cwhite>Bone Splinters");
			}
		}
		
		if(armor != null) {
			armor.clear();
			armor.buildText("<iArmor Icon> <cwhite,s22,iArmor Icon>-Armor-;" + party.getMember(selectedMember).getEquipment().getArmor().print());
		} else if(weapons != null) {
			weapons.clear();
			weapons.buildText("<iWeapons Icon> <cwhite,s22,iWeapons Icon>-Weapons-;" + party.getMember(selectedMember).getEquipment().getWeapons().print());
		} else if(quiver != null) {
			quiver.clear();
			quiver.buildText("<iQuiver Icon> <cwhite,s22,iQuiver Icon>-Quiver-;" + party.getMember(selectedMember).getEquipment().getQuiver().print());
		} else if(pockets != null) {
			pockets.clear();
			pockets.buildText("<iPocket Icon> <cwhite,s22,iPocket Icon>-Pockets-;" + party.getMember(selectedMember).getEquipment().getPockets().print());
		}
	}
	
	@Override
	public void close() {
		for(int x = 0; x<party.getMembers().size(); x++) {
			members.get(x).setPositionOverTime(members.get(x).getX(), 0 - members.get(x).getFullWidth(), members.get(x).getY(), members.get(x).getY(), 0.15f);
		}
	}
	
	public void resetFocus() {
		for(int x = 0; x<party.getMembers().size(); x++) {
			members.get(x).clear();
			members.get(x).buildText(party.getMember(x).getName() + ";" + party.getMember(x).getEquipment().printSimple());
			if(x == 0)
				members.get(x).setPosition(0 - members.get(x).getFullWidth(), 15);
			else
				members.get(x).setPosition(0 - members.get(x).getFullWidth(), members.get(x - 1).getY() + members.get(x - 1).getFullHeight());
			members.get(x).setPositionOverTime(members.get(x).getX(), 15, members.get(x).getY(), members.get(x).getY(), 0.1f);
		}
	}
	
	public void resetPreviousFocus() {
		focus.setPositionOverTime(15, 0 - focus.getFullWidth(), focus.getY(), focus.getY(), 0.1f);
		focus.clear();
		focus.buildText(party.getMember(selectedMember).getName() + ";" + party.getMember(selectedMember).getEquipment().printSimple());
		
		for(int x = 0; x<party.getMembers().size(); x++) {
			if(x != selectedMember) {
				if(x == 0)
					members.get(x).setPosition(0 - members.get(x).getFullWidth(), 15);
				else
					members.get(x).setPosition(0 - members.get(x).getFullWidth(), members.get(x - 1).getY() + members.get(x - 1).getFullHeight());
			}
		}
		
		quiver = null;
		weapons = null;
		armor = null;
		pockets = null;
	}
	
	public void cycleFocus() {
		members.get(selectedMember).clear();
		members.get(selectedMember).buildText(party.getMember(selectedMember).getName() + ";" + party.getMember(selectedMember).getEquipment().getGold() + " <cwhite>Bone Splinters");
		members.get(selectedMember).setPosition(0 - members.get(selectedMember).getFullWidth(), 15);
		members.get(selectedMember).setPositionOverTime(members.get(selectedMember).getX(), 15, 15, 15, 0.1f);
		focus = members.get(selectedMember);
		
		armor = null;
		weapons = null;
		quiver = null;
		pockets = null;
		
		armor = new SelectBox(true);
		armor.setStill(true);
		armor.showCursor(false);
		armor.buildText("<iArmor Icon> <cwhite,s22,iArmor Icon>-Armor-;" + party.getMember(selectedMember).getEquipment().getArmor().print());
		armor.setPositionOverTime(0 - armor.getFullWidth(), 35, members.get(selectedMember).getEventualY() + members.get(selectedMember).getFullHeight(),
				members.get(selectedMember).getEventualY() + members.get(selectedMember).getFullHeight(), 0.1f);
		
		for(int x = 0; x<party.getMembers().size(); x++) {
			if(x != selectedMember) {
				members.get(x).setPositionOverTime(members.get(x).getX(), 0 - members.get(x).getFullWidth(), members.get(x).getY(), members.get(x).getY(), 0.1f);
			}
		}
	}
	
	public void cyclePage() {
		armor = null;
		weapons = null;
		quiver = null;
		pockets = null;
		
		switch(menuPage) {
		case(0):
			armor = new SelectBox(true);
			armor.setStill(true);
			armor.showCursor(false);
			armor.buildText("<iArmor Icon> <cwhite,s22,iArmor Icon>-Armor-;" + party.getMember(selectedMember).getEquipment().getArmor().print());
			armor.setPositionOverTime(0 - armor.getFullWidth(), 35, members.get(selectedMember).getEventualY() + members.get(selectedMember).getFullHeight(),
					members.get(selectedMember).getEventualY() + members.get(selectedMember).getFullHeight(), 0.1f);
			break;
		case(1):
			weapons = new SelectBox(true);
			weapons.setStill(true);
			weapons.showCursor(false);
			weapons.buildText("<iWeapons Icon> <cwhite,s22,iWeapons Icon>-Weapons-;" + party.getMember(selectedMember).getEquipment().getWeapons().print());
			weapons.setPositionOverTime(0 - weapons.getFullWidth(), 35, members.get(selectedMember).getEventualY() + members.get(selectedMember).getFullHeight(),
					members.get(selectedMember).getEventualY() + members.get(selectedMember).getFullHeight(), 0.1f);
			break;
		case(2):
			quiver = new SelectBox(true);
			quiver.setStill(true);
			quiver.showCursor(false);
			quiver.buildText("<iQuiver Icon> <cwhite,s22,iQuiver Icon>-Quiver-;" + party.getMember(selectedMember).getEquipment().getQuiver().print());
			quiver.setPositionOverTime(0 - quiver.getFullWidth(), 35, members.get(selectedMember).getEventualY() + members.get(selectedMember).getFullHeight(),
					members.get(selectedMember).getEventualY() + members.get(selectedMember).getFullHeight(), 0.1f);
			break;
		case(3):
			pockets = new SelectBox(true);
			pockets.setStill(true);
			pockets.showCursor(false);
			pockets.buildText("<iPocket Icon> <cwhite,s22,iPocket Icon>-Pockets-;" + party.getMember(selectedMember).getEquipment().getPockets().print());
			pockets.setPositionOverTime(0 - pockets.getFullWidth(), 35, members.get(selectedMember).getEventualY() + members.get(selectedMember).getFullHeight(),
					members.get(selectedMember).getEventualY() + members.get(selectedMember).getFullHeight(), 0.1f);
			break;
		}
	}
	
}
