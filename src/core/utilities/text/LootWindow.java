package core.utilities.text;

import java.util.ArrayList;

import core.entity.Prop;
import core.items.Item;
import core.items.ArmorItem;
import core.items.WeaponItem;
import core.keyboard.Keybinds;
import core.scene.Stage;

public class LootWindow extends SelectBox {

	private Prop source;
	private HoldBox hold;
	private ArrayList<Item> lootTable = new ArrayList<Item>();
	private boolean empty = false;
	
	public LootWindow(Prop source) {
		super(true);
		this.source = source;
		fillLootTable();
	}

	public void update(Stage stage) {
		// Adjust location (Shouldn't happen but whatever)
		if(source != null) {
			setCenterPosition(source.getX(), source.getY());
		}

		if(hold == null) {
			// Sort selecting items to take
			switch(select()) {
			case(-1):
				break;
			default:
				// Only loot if there's something to loot
				if(!empty) {
					loot(lootTable.get(getSelected()), stage);
					Keybinds.inMenu();
				}
				if(!source.getContainer().isEmpty() && source.getContainer().isRemoveOnEmpty())
					Keybinds.inMenu();
			}
		} else {
			switch(hold.select()) {
			case(0):
				loot(lootTable.get(getSelected()), stage);
				stage.script.removeText(hold);
				hold = null;
				Keybinds.inMenu();
				break;
			case(1):
				stage.script.removeText(hold);
				hold = null;
				Keybinds.inMenu();
				break;
			}
		}
		
		// Close if cancelled
		if(Keybinds.CANCEL.clicked()) {
			Keybinds.closeMenu();
			stage.script.removeAction(getAction());
			if(hold != null)
				stage.script.removeText(hold);
			stage.script.removeText(this);
			stage.script.setInteractPrompt(false);
		}
	}
	
	public void loot(Item item, Stage stage) {
		// Get slot of item to loot
		int slot = 0;
		if(item instanceof WeaponItem)
			slot = item.getWeaponSlot();
		else if(item instanceof ArmorItem)
			slot = item.getArmorSlot();
		
		// If player can equip, equip
		if(stage.player.getEquipment().canEquip(item, slot)) {
			stage.player.getEquipment().equip(item);
			// Remove item from source and rebuild loot table
			source.getContainer().removeItem(item);
			fillLootTable();
			this.setSelected(this.getSelected());
			checkRemove(stage);
		} else if(stage.player.getEquipment().canHold()) {
			if(hold == null) {
				hold = (HoldBox) stage.player.getEquipment().holdItemPrompt(item, stage);
				hold.setSource(stage.player);
				stage.script.addText(hold);
			} else {
				stage.player.getEquipment().holdItem(item);
				// Remove item from source and rebuild loot table
				source.getContainer().removeItem(item);
				fillLootTable();
				this.setSelected(this.getSelected());
				checkRemove(stage);
			}
		} else {
			Textbox text = new PlainText(false, 2.0f, false);
			text.buildText("You have no room for that!");
			text.setSource(stage.player);
			stage.script.addText(text);
		}
	}
	
	public void fillLootTable() {
		lootTable.clear();
		lootTable.addAll(source.getContainer().getItems());
		
		String loot = "-" + source.getName() + "-";
		if(lootTable.isEmpty()) {
			loot += ";Empty";
			empty = true;
		} else {
			for(int x = 0; x<lootTable.size(); x++) {
				loot += ";" + lootTable.get(x).getName();
			}
		}
		clear();
		buildText(loot);
	}
	
	public void checkRemove(Stage stage) {
		if(source.getContainer().isEmpty() && source.getContainer().isRemoveOnEmpty()) {
			stage.remove(source);
			stage.script.removeAction(getAction());
			stage.script.removeText(this);
			stage.script.setInteractPrompt(false);
		}
	}
	
}
