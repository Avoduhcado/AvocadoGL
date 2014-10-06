package core.utilities.menu;

import core.Theater;
import core.keyboard.Keybinds;
import core.scene.Stage;
import core.utilities.Save;
import core.utilities.sounds.Ensemble;
import core.utilities.text.InputBox;
import core.utilities.text.PlainText;
import core.utilities.text.SelectBox;
import core.utilities.text.Textbox;

public class OptionsMenu extends MenuPage {

	private SelectBox mainOptions;
	private InputBox newSave;
	private SelectBox fieldSave;
	private SelectBox saveList;
	private SelectBox loadConfirm;
	private String tempSave;
	
	public OptionsMenu(Boolean town) {
		mainOptions = new SelectBox(true);
		mainOptions.setStill(true);
		mainOptions.showCursor(false);
		if(town)	// Change between normal save and field save
			mainOptions.buildText("Options:;Toggle Mute;Save Game;Load Game;Quit to Desktop");
		else
			mainOptions.buildText("Options:;Toggle Mute;Field Save;Load Game;Quit to Desktop");
		mainOptions.setPositionOverTime(0 - mainOptions.getFullWidth(), 15, 15, 15, 0.1f);
	}
	
	@Override
	public void update(Stage stage) {
		movePages();
		
		if(focus == mainOptions) {
			switch(mainOptions.select()) {
			case(0):	// Mute sounds
				Ensemble.get().mute();
				Keybinds.inMenu();
				break;
			case(1):	// Save the game
				if(stage.map.isTown()) {	// Perform regular Save
					if(Theater.saveFile.matches("New")) {	// If new save file
						newSave = new InputBox(false);
						newSave.setStill(true);
						newSave.setPositionOverTime(0 - newSave.getFullWidth(), 15, mainOptions.getY() + mainOptions.getFullHeight(),
									mainOptions.getY() + mainOptions.getFullHeight(), 0.1f);
						focus = newSave;
						Keybinds.inMenu();
					} else {								// Save over old save file
						Save.get().save(stage, Theater.saveFile);
						Keybinds.inMenu();
					}
				} else {					// Change to Field Save
					fieldSave = new SelectBox(true);
					fieldSave.setStill(true);
					fieldSave.buildText("Field Saving will quit the game afterwards.;Ok;Cancel");
					fieldSave.setPositionOverTime(0 - fieldSave.getFullWidth(), 15, mainOptions.getY() + mainOptions.getFullHeight(),
							mainOptions.getY() + mainOptions.getFullHeight(), 0.1f);
					focus = fieldSave;
					Keybinds.inMenu();
				}
				break;
			case(2):	// Load a saved game
				if(Save.get().getSaves() == null) {		// No previous saves to load
					Textbox noSaves = new PlainText(false, 3f, false);
					noSaves.setStill(true);
					noSaves.buildText("No saves, save this one!");
					noSaves.setPosition(mainOptions.getFullWidth() + mainOptions.getX(), mainOptions.getY());
					stage.addText(noSaves);
				} else {								// Show saved games
					saveList = new SelectBox(false);
					saveList.setStill(true);
					saveList.buildText(Save.get().getSaves());
					saveList.setPositionOverTime(0 - saveList.getFullWidth(), 15, mainOptions.getY() + mainOptions.getFullHeight(),
							mainOptions.getY() + mainOptions.getFullHeight(), 0.1f);
					focus = saveList;
				}
				Keybinds.inMenu();
				break;
			case(3):	// Exit the game
				Theater.toBeClosed = true;
				break;
			default:
				break;	
			}
			
			if(Keybinds.CANCEL.clicked()) {
				focus = null;
				mainOptions.showCursor(false);
			}
		} else if(focus == newSave) {
			if(stage.map.isTown()) {	// Perform regular Save
				if(newSave.input() != null) {
					Save.get().save(stage, newSave.input());
					newSave = null;
					focus = null;
					Keybinds.inMenu();
				}
			} else {					// Perform Field Save
				if(newSave.input() != null) {
					Save.get().fieldSave(stage, newSave.input());
					newSave = null;
					focus = null;
					Theater.toBeClosed = true;
				}
			}
			newSave.update(stage);
		} else if(focus == fieldSave) {
			switch(fieldSave.select()) {	// Confirm Field Save
			case(-1):
				break;
			case(0):
				fieldSave = null;
				if(Theater.saveFile.matches("New")) {	// If it's a new save file
					newSave = new InputBox(false);
					newSave.setStill(true);
					newSave.setPositionOverTime(0 - newSave.getFullWidth(), 15, mainOptions.getY() + mainOptions.getFullHeight(),
								mainOptions.getY() + mainOptions.getFullHeight(), 0.1f);
					focus = newSave;
				} else {								// If it's a previous save
					Save.get().fieldSave(stage, Theater.saveFile);
					Keybinds.inMenu();
					Theater.toBeClosed = true;
				}
				break;
			case(1):
				fieldSave = null;
				focus = mainOptions;
				Keybinds.inMenu();
				break;
			}	
		} else if(focus == saveList) {
			switch(saveList.select()) {	// Load Game menu
			case(-1):
				break;
			default:	// Load selected save file
				tempSave = Theater.saveFile;
				Theater.saveFile = saveList.getText().get(saveList.getSelected()).getLine();
				if(Save.get().hasFieldSave()) {
					loadConfirm = new SelectBox(true);
					loadConfirm.setStill(true);
					loadConfirm.buildText("This file has a Field Save. Resuming will delete it.;Resume;Cancel");
					loadConfirm.setPositionOverTime(mainOptions.getFullWidth() + mainOptions.getX(), mainOptions.getFullWidth() + mainOptions.getX(),
								0 - loadConfirm.getFullHeight(), mainOptions.getY(), 0.1f);
					focus = loadConfirm;
					Keybinds.inMenu();
				} else {
					Save.get().loadSave(stage, false);
					saveList = null;
					focus = null;
					Keybinds.closeMenu();
				}
				break;
			}

			if(Keybinds.CANCEL.clicked()) {
				saveList = null;
				focus = mainOptions;
				Keybinds.inMenu();
			}
		} else if(focus == loadConfirm) {
			switch(loadConfirm.select()) {
			case(-1):
				break;
			case(0):	// Confirm loading a game and restart
				Save.get().loadSave(stage, true);
				Save.get().clearFieldFromSave();
				saveList = null;
				focus = null;
				Keybinds.closeMenu();
				break;
			case(1):	// Cancel loading a game
				Theater.saveFile = tempSave;
				loadConfirm = null;
				focus = saveList;
				Keybinds.inMenu();
				break;
			}
		}
	}
	
	@Override
	public void movePages() {
		mainOptions.move();
		if(newSave != null)
			newSave.move();
		if(fieldSave != null)
			fieldSave.move();
		if(saveList != null)
			saveList.move();
		if(loadConfirm != null)
			loadConfirm.move();
	}
	
	@Override
	public void draw() {
		mainOptions.draw();
		if(newSave != null)
			newSave.draw();
		if(fieldSave != null)
			fieldSave.draw();
		if(saveList != null)
			saveList.draw();
		if(loadConfirm != null)
			loadConfirm.draw();
	}
	
	@Override
	public void focus() {
		focus = mainOptions;
		mainOptions.showCursor(true);
	}
	
	@Override
	public void refresh() {
	}
	
	@Override
	public void close() {
		mainOptions.setPositionOverTime(mainOptions.getX(), 0 - mainOptions.getFullWidth(), mainOptions.getY(), mainOptions.getY(), 0.15f);
	}
	
}
