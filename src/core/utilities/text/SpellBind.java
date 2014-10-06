package core.utilities.text;

import core.entity.Actor;
import core.equipment.Spellbook;
import core.equipment.Weapons;
import core.keyboard.Keybinds;
import core.scene.Stage;
import core.utilities.text.PlainText;
import core.utilities.text.SelectBox;
import core.utilities.text.Textbox;

public class SpellBind extends SelectBox {

	private Actor source;
	private Textbox spellWindow;
	private Textbox weaponWindow;
	private boolean binding;
	private Textbox confirmationWindow;
	private boolean confirming;
		
	public SpellBind(Actor source) {
		super(false);
		this.source = source;
		fillSpells(source.getEquipment().getSpellbook());
		spellWindow.showCursor(false);
		fillWeapons(source.getEquipment().getWeapons());
	}
	
	@Override
	public void update(Stage stage) {
		if(!binding) {
			switch(weaponWindow.select()) {
			case(-1):
				break;
			default:
				if(source.getEquipment().getWeapons().getGear(weaponWindow.getSelected()).getWeaponType() == 1) {
					spellWindow.showCursor(true);
					binding = true;
				} else {
					confirmationWindow = new PlainText(false, 2.0f, false);
					confirmationWindow.setStill(true);
					confirmationWindow.setCenterPosition(30f + spellWindow.getFullWidth(), 55f + weaponWindow.getFullHeight());
					confirmationWindow.buildText("You can't bind spells to this weapon!");
					stage.addText(confirmationWindow);
					confirmationWindow = null;
				}
				Keybinds.inMenu();
				break;
			}
			if(Keybinds.CANCEL.clicked()) {
				stage.script.removeAction(getAction());
				stage.script.removeText(this);
				if(action != null)
					action.reset();
				stage.script.setInteractPrompt(false);
				Keybinds.closeMenu();
			}
		} else {
			if(!confirming) {
				switch(spellWindow.select()) {
				case(-1):
					break;
				default:
					if(source.getEquipment().getWeapons().getGear(weaponWindow.getSelected()).getFreeSpellSlot() != -1) {
						source.getEquipment().getWeapons().getGear(weaponWindow.getSelected()).addSpell(source.getEquipment().getSpellbook().getSpell(spellWindow.getSelected()));
						binding = false;
						spellWindow.showCursor(false);
						System.out.println("Bound spell: " + source.getEquipment().getSpellbook().getSpell(spellWindow.getSelected()).getName());
					} else {
						confirmationWindow = new SelectBox(true);
						confirmationWindow.setStill(true);
						confirmationWindow.setCenterPosition(30f + spellWindow.getFullWidth(), 55f + weaponWindow.getFullHeight());
						confirmationWindow.buildText("This weapon's binds are full! Override a slot?;1;2;3;Cancel");
						confirming = true;
					}
					Keybinds.inMenu();
					break;
				}
				if(Keybinds.CANCEL.clicked()) {
					binding = false;
					spellWindow.showCursor(false);
					Keybinds.inMenu();
				}
			} else {
				switch(confirmationWindow.select()) {
				case(-1):
					break;
				case(3):
					confirming = false;
					confirmationWindow = null;
					Keybinds.inMenu();
					break;
				default:
					source.getEquipment().getWeapons().getGear(weaponWindow.getSelected()).setSpell(confirmationWindow.getSelected(), 
							source.getEquipment().getSpellbook().getSpell(spellWindow.getSelected()));
					binding = false;
					spellWindow.showCursor(false);
					confirming = false;
					confirmationWindow = null;
					Keybinds.inMenu();
					break;
				}
			}
		}
	}
	
	@Override
	public void draw() {
		spellWindow.draw();
		weaponWindow.draw();
		if(confirmationWindow != null)
			confirmationWindow.draw();
	}
	
	public void fillSpells(Spellbook spellbook) {
		spellWindow = new SelectBox(true);
		spellWindow.setStill(true);
		spellWindow.setCenterPosition(25f, 50f);
		spellWindow.buildText("Spells:;" + spellbook.print());
	}
	
	public void fillWeapons(Weapons weapons) {
		weaponWindow = new SelectBox(true);
		weaponWindow.setStill(true);
		weaponWindow.setCenterPosition(30f + spellWindow.getFullWidth(), 50f);
		weaponWindow.buildText("Weapons:;" + weapons.print());
	}
	
}
