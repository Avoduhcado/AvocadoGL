package core.utilities.menu;

import core.Theater;
import core.keyboard.Keybinds;
import core.scene.Stage;
import core.utilities.Save;
import core.utilities.text.PlainText;
import core.utilities.text.SelectBox;
import core.utilities.text.Textbox;

public class MainMenu {

	private boolean open;
	private Textbox focus;
	private SelectBox mainMenu;
	private Textbox loadMenu;
	private SelectBox fieldLoad;
	private SelectBox optionsMenu;
	
	public MainMenu() {
		open = true;
		
		buildMainMenu();
		focus = mainMenu;
	}
	
	public void update(Stage stage) {
		movePages();
		
		if(focus == mainMenu) {				// Title Menu
			switch(mainMenu.select()) {
			case(-1):
				break;
			case(0):	// Starting a new game
				if(Theater.saveFile.matches("New")) {
					Theater.get().loadStage();
				} else {
					System.out.println("This shoudn't happen");
				}
				mainMenu = null;
				closeMenu();
				break;
			case(1):	// Loading a previous save game
				buildLoadMenu();
				focus = loadMenu;
				break;
			case(2):	// Change the Options
				buildOptionsMenu();
				mainMenu = null;
				focus = optionsMenu;
				break;
			case(3):	// Exiting the game
				mainMenu = null;
				closeMenu();
				Theater.toBeClosed = true;
				break;
			}
		} else if(focus == loadMenu) {		// Load Menu
			if(loadMenu instanceof SelectBox) {				// There are saves to load
				switch(loadMenu.select()) {
				case(-1):
					break;
				default:	// Load a selected save file
					Theater.saveFile = loadMenu.getText().get(loadMenu.getSelected()).getLine();
					
					if(Save.get().hasFieldSave()) {		// If there's a current field save
						fieldLoad = new SelectBox(true);
						fieldLoad.buildText("This file has a Field Save. Resuming will delete it.;Resume;Cancel");
						fieldLoad.setPositionOverTime(0 - fieldLoad.getFullWidth(), 15, mainMenu.getY() + mainMenu.getFullHeight(), mainMenu.getY() + mainMenu.getFullHeight(), 0.1f);
						loadMenu.showCursor(false);
						focus = fieldLoad;
					} else {							// Proceed with regular loading
						Save.get().loadSave(stage, false);
						closeMenu();
					}
					break;
				}
			} else if(loadMenu instanceof PlainText) {		// There are no previous saves
				if(Keybinds.CONFIRM.clicked()) {
					loadMenu = null;
					mainMenu.setPositionOverTime(mainMenu.getX(), (Theater.get().getScreen().displayWidth / 2) - (mainMenu.getFullWidth() / 2), mainMenu.getY(), mainMenu.getY(), 0.1f);
					mainMenu.showCursor(true);
					focus = mainMenu;
				}
			}
			
			if(Keybinds.CANCEL.clicked()) {
				loadMenu = null;
				mainMenu.setPositionOverTime(mainMenu.getX(), (Theater.get().getScreen().displayWidth / 2) - (mainMenu.getFullWidth() / 2), mainMenu.getY(), mainMenu.getY(), 0.1f);
				mainMenu.showCursor(true);
				focus = mainMenu;
			}
		} else if(focus == fieldLoad) {		// Load a field save menu
			switch(fieldLoad.select()) {
			case(-1):
				break;
			case(0):	// Load field save and delete it
				Save.get().loadSave(stage, true);
				Save.get().clearFieldFromSave();
				closeMenu();
				break;
			case(1):	// Cancel field loading return to load menu
				Theater.saveFile = "New";
				fieldLoad = null;
				loadMenu.showCursor(true);
				focus = loadMenu;
				break;
			}
		} else if(focus == optionsMenu) {	// Change the options
			switch(optionsMenu.select()) {
			case(-1):
				break;
			case(0):
				optionsMenu = null;
				buildMainMenu();
				focus = mainMenu;
			}
		}
	}
	
	public void draw() {
		if(mainMenu != null)
			mainMenu.draw();
		if(loadMenu != null)
			loadMenu.draw();
		if(optionsMenu != null)
			optionsMenu.draw();
		if(fieldLoad != null)
			fieldLoad.draw();
	}
	
	public void buildMainMenu() {
		mainMenu = new SelectBox(false);
		mainMenu.buildText("New Game;Load Game;Options;Exit");
		mainMenu.setPosition((Theater.get().getScreen().displayWidth / 2) - (mainMenu.getFullWidth() / 2), 
				(Theater.get().getScreen().displayHeight / 1.5f) - (mainMenu.getFullHeight() / 2));
	}
	
	public void buildLoadMenu() {
		mainMenu.setPositionOverTime(mainMenu.getX(), (Theater.get().getScreen().displayWidth / 4) - (mainMenu.getFullWidth() / 2), mainMenu.getY(), mainMenu.getY(), 0.1f);
		mainMenu.showCursor(false);
		
		if(Save.get().getSaves() == null) {
			loadMenu = new PlainText(false, 0f, false);
			loadMenu.buildText("No saves, start a new game!");
			loadMenu.setPositionOverTime(mainMenu.getEventualX() + mainMenu.getFullWidth(), mainMenu.getEventualX() + mainMenu.getFullWidth(),
					0 - loadMenu.getFullHeight(), mainMenu.getY(), 0.1f);
		} else {
			loadMenu = new SelectBox(false);
			loadMenu.buildText(Save.get().getSaves());
			loadMenu.setPositionOverTime(mainMenu.getEventualX() + mainMenu.getFullWidth(), mainMenu.getEventualX() + mainMenu.getFullWidth(),
					0 - loadMenu.getFullHeight(), mainMenu.getY(), 0.1f);
		}
	}
	
	public void buildOptionsMenu() {
		optionsMenu = new SelectBox(true);
		optionsMenu.buildText("-Options-;I haven't made any options yet");
		optionsMenu.setPosition((Theater.get().getScreen().displayWidth / 2) - (optionsMenu.getFullWidth() / 2), 
				(Theater.get().getScreen().displayHeight / 1.5f) - (optionsMenu.getFullHeight() / 2));
	}
	
	public void movePages() {
		if(mainMenu != null)
			mainMenu.move();
		if(loadMenu != null)
			loadMenu.move();
		if(optionsMenu != null)
			optionsMenu.move();
		if(fieldLoad != null)
			fieldLoad.move();
	}
	
	public void closeMenu() {
		open = false;
		focus = null;
		Keybinds.closeMenu();
	}
	
	public boolean isOpen() {
		return open;
	}
	
}
