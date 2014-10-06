package core.utilities.menu;

import java.util.ArrayList;

import core.Theater;
import core.keyboard.Keybinds;
import core.scene.Stage;
import core.utilities.Save;
import core.utilities.sounds.Ensemble;
import core.utilities.text.InputBox;
import core.utilities.text.PlainText;
import core.utilities.text.SelectBox;
import core.utilities.text.Textbox;

enum TitleMenuType {
	TITLE,
	LOADGAME,
	OPTIONS;
}

enum GameMenuType {
	STATS,
	EQUIPMENT,
	OPTIONS;
}

public class Menu {
	
	private boolean open = false;
	private Textbox titleMenu;
	private boolean titleMenuOpen = false;
	private TitleMenuType titleMenuType;
	private ArrayList<Textbox> gameMenu = new ArrayList<Textbox>();
	private GameMenuType gameMenuType;
	private int partyMember = 0;
	private int equipPage = -1;
	private int selectedEquipment = 0;
	private int optionPage = -1;
	private boolean doUpdate;
	private String tempSave = "";
	private float refreshTimer = 0f;
	
	public void draw() {
		if(titleMenu != null) {
			titleMenu.draw();
		}
		if(!gameMenu.isEmpty()) {
			for(int x = 0; x<gameMenu.size(); x++)
				gameMenu.get(x).draw();
		}
	}
	
	public void update(Stage stage) {
		if(titleMenuOpen) {
			switch(titleMenuType) {
			case TITLE:
				title();
				break;
			case LOADGAME:
				loadGame(stage);
				break;
			// TODO (Replace with options? Add an options menu?)
			case OPTIONS:
				titleOptions();
				break;
			}
		} else {
			switch(gameMenuType) {
			case STATS:
				stats(stage);
				break;
			case EQUIPMENT:
				equipment(stage);
				break;
			case OPTIONS:
				gameOptions(stage);
				break;
			}
			if(doUpdate || refreshTimer > 2.0f) {
				if(gameMenuType == GameMenuType.EQUIPMENT && doUpdate)
					buildGameMenu(stage);
				refreshTimer = 0f;
			}
			refreshTimer += Theater.getDeltaSpeed(0.025f);
		}
	}
	
	public void openTitleMenu() {
		titleMenuOpen = true;
		titleMenuType = TitleMenuType.TITLE;
		open = true;
		Keybinds.inMenu();
		
		buildTitleMenu();
	}
	
	public void buildTitleMenu() {
		switch(titleMenuType) {
		case TITLE:
			titleMenu = new SelectBox(false) {{
				setCenterPosition((float) (Theater.get().getScreen().camera.getX() + (Theater.get().getScreen().camera.getWidth() * 0.4f)), 
						(float) (Theater.get().getScreen().camera.getY() + (Theater.get().getScreen().camera.getHeight() * 0.6f)));
				buildText("New Game;Load Game;Read Manual;Exit");
			}};
			break;
		case LOADGAME:
			if(Save.get().getSaves() == null) {
				titleMenu = new PlainText(false, 0f, false) {{
					setCenterPosition((float) (Theater.get().getScreen().camera.getX() + (Theater.get().getScreen().camera.getWidth() * 0.0625f)),
							(float) (Theater.get().getScreen().camera.getY() + (Theater.get().getScreen().camera.getHeight() * 0.15f)));
					buildText("No saves, start a new game!");
				}};
			} else {
				titleMenu = new SelectBox(false) {{
					setCenterPosition((float) (Theater.get().getScreen().camera.getX() + (Theater.get().getScreen().camera.getWidth() * 0.0625f)),
							(float) (Theater.get().getScreen().camera.getY() + (Theater.get().getScreen().camera.getHeight() * 0.15f)));
					buildText(Save.get().getSaves());
				}};
			}
			break;
		case OPTIONS:
			titleMenu = new PlainText(false, 0f, false) {{
				setCenterPosition((float) (Theater.get().getScreen().camera.getX() + (Theater.get().getScreen().camera.getWidth() * 0.125f)), 
						(float) (Theater.get().getScreen().camera.getY() + (Theater.get().getScreen().camera.getHeight() * 0.6f)));
				buildText("Arrow keys are for moving, W interacts with objects/Up in menu;" +
					"A is attack/Left in menu, S is dodge/Down in menu;" +
					"D is Right in menu, E opens menu, F closes the menu/Cancel;" +
					"Hold left Shift to run, Enter is confirm.;" +
					"Press 4 to take a screenshot! Esc will close the game.");
			}};
			break;
		}
	}
	
	public void openGameMenu(Stage stage) {
		open = true;
		gameMenuType = GameMenuType.STATS;
		partyMember = 0;
		Keybinds.inMenu();
		
		buildGameMenu(stage);
	}
	
	public void buildGameMenu(final Stage stage) {
		switch(gameMenuType) {
		// Party menu
		case STATS:
			gameMenu.clear();
			for(int x = 0; x<stage.party.getMembers().size(); x++) {
				gameMenu.add(new PlainText(false, 0f, false));
				if(x > 0) {
					int y = 0;
					for(int z = x; z>0; z--)
						y += gameMenu.get(z-1).getFullHeight();
					gameMenu.get(x).setCenterPosition(15, y + 50);
				} else
					gameMenu.get(x).setCenterPosition(15, 50);
				gameMenu.get(x).setStill(true);
				gameMenu.get(x).buildText(stage.party.getMember(x).getName() + ";" + stage.party.getMember(x).getStats().print());
			}
			partyMember = 0;
			break;
		// Player equipment menu
		case EQUIPMENT:
			gameMenu.clear();
			gameMenu.add(new SelectBox(true) {{ 
				setCenterPosition(15, 50);
				setStill(true);
				buildText("Armor:;" + stage.party.getMember(partyMember).getEquipment().getArmor().print());
				showCursor(false);
			}});
			gameMenu.add(new SelectBox(true) {{ 
				setCenterPosition(15 + gameMenu.get(0).getFullWidth(), 50);
				setStill(true);
				buildText("Weapons:;" + stage.party.getMember(partyMember).getEquipment().getWeapons().print());
				showCursor(false);
			}});
			gameMenu.add(new SelectBox(true) {{
				setCenterPosition(15 + gameMenu.get(0).getFullWidth(), 50 + gameMenu.get(1).getFullHeight());
				setStill(true);
				buildText("Quiver:;" + stage.party.getMember(partyMember).getEquipment().getQuiver().print());
				showCursor(false);
			}});
			gameMenu.add(new SelectBox(true) {{ 
				setCenterPosition(15 + gameMenu.get(0).getFullWidth() + gameMenu.get(1).getFullWidth(), 50);
				setStill(true);
				buildText("Pockets:;" + stage.party.getMember(partyMember).getEquipment().getPockets().print());
				showCursor(false);
			}});
			gameMenu.add(new PlainText(false, 0.0f, false) {{ 
				setCenterPosition(15, gameMenu.get(0).getFullHeight() + 50);
				setStill(true);
				buildText("Bone Splinters: " + stage.party.getMember(partyMember).getEquipment().getGold());
			}});
			equipPage = -1;
			break;
		// Options menu
		case OPTIONS:
			gameMenu.clear();
			gameMenu.add(new SelectBox(true) {{
				setCenterPosition(15, 50);
				setStill(true);
				showCursor(false);
				// Change between normal save and field save
				if(stage.map.isTown())
					buildText("Options:;Toggle Mute;Save Game;Load Game;Quit to Desktop");
				else
					buildText("Options:;Toggle Mute;Field Save;Load Game;Quit to Desktop");
			}});
			optionPage = -1;
			break;
		default:
			System.out.println("Menu is lost, abandon ship!");
		}
		
		doUpdate = false;
	}

	public void title() {
		switch(titleMenu.select()) {
		case(-1):
			break;
		// Starting a new game
		case(0):
			if(Theater.saveFile.matches("New")) {
				Theater.get().loadStage();
			} else {
				System.out.println("This shoudn't happen");
			}
			closeMenu();
			titleMenu = null;
			break;
		// Loading a previous save game
		case(1):
			titleMenuType = TitleMenuType.LOADGAME;
			buildTitleMenu();
			break;
		// Checking the Options
		case(2):
			titleMenuType = TitleMenuType.OPTIONS;
			buildTitleMenu();
			break;
		// Exiting the game
		case(3):
			Theater.toBeClosed = true;
			closeMenu();
			titleMenu = null;
			break;
		}
	}
	
	public void loadGame(Stage stage) {
		// If there are save files
		if(Save.get().getSaves() != null && Theater.saveFile.matches("New")) {
			switch(titleMenu.select()) {
			case(-1):
				break;
			// Load a selected save file
			default:
				Theater.saveFile = titleMenu.getText().get(titleMenu.getSelected()).getLine();
				// If there's a current field save
				if(Save.get().hasFieldSave()) {
					titleMenu = new SelectBox(true) {{
						setCenterPosition((float) (Theater.get().getScreen().camera.getX() + (Theater.get().getScreen().camera.getWidth() * 0.0625f)),
								(float) (Theater.get().getScreen().camera.getY() + (Theater.get().getScreen().camera.getHeight() * 0.15f)));
						buildText("This file has a Field Save. Resuming will delete it.;Resume;Cancel");
					}};
				// Proceed with regular loading
				} else {
					Save.get().loadSave(stage, false);
					titleMenu = null;
					titleMenuOpen = false;
					closeMenu();
				}
				break;
			}
		} else if(!Theater.saveFile.matches("New")) {
			switch(titleMenu.select()) {
			case(-1):
				break;
			case(0):
				// Load field save and delete it
				Save.get().loadSave(stage, true);
				Save.get().clearFieldFromSave();
				titleMenu = null;
				titleMenuOpen = false;
				closeMenu();
				break;
			case(1):
				// Cancel field loading return to main title
				Theater.saveFile = "New";
				titleMenuType = TitleMenuType.TITLE;
				buildTitleMenu();
				break;
			}
		}
		// Cancel loading return to main title
		if(Keybinds.CANCEL.clicked()) {
			titleMenuType = TitleMenuType.TITLE;
			buildTitleMenu();
		}
	}
	
	public void titleOptions() {
		if(Keybinds.CANCEL.clicked() || Keybinds.CONFIRM.clicked()) {
			titleMenuType = TitleMenuType.TITLE;
			buildTitleMenu();
		}
	}
	
	public void stats(Stage stage) {
		if(Keybinds.CANCEL.clicked()) {
			closeMenu();
		} else if(Keybinds.MENU_RIGHT.clicked()) {
			gameMenuType = GameMenuType.EQUIPMENT;
			buildGameMenu(stage);
		} else if(Keybinds.MENU_LEFT.clicked()) {
			gameMenuType = GameMenuType.OPTIONS;
			buildGameMenu(stage);
		} else if(Keybinds.MENU_DOWN.clicked()) {
			if(partyMember + 1 < stage.party.getMembers().size())
				partyMember++;
			else
				partyMember = 0;
		} else if(Keybinds.MENU_UP.clicked()) {
			if(partyMember > 0)
				partyMember--;
			else
				partyMember = stage.party.getMembers().size() - 1;
		}
	}
	
	public void equipment(Stage stage) {
		if(equipPage == -1) {
			// Enter equipment menu
			if(Keybinds.CONFIRM.clicked()) {
				equipPage = 0;
				gameMenu.get(0).showCursor(true);
			} else if(Keybinds.MENU_RIGHT.clicked()) {
				gameMenuType = GameMenuType.OPTIONS;
				buildGameMenu(stage);
			} else if(Keybinds.MENU_LEFT.clicked()) {
				gameMenuType = GameMenuType.STATS;
				buildGameMenu(stage);
			} else if (Keybinds.CANCEL.clicked()) {
				closeMenu();
			}
		} else if(equipPage < 4) {
			// Navigate equipment menu
			if(Keybinds.MENU_RIGHT.clicked()) {
				gameMenu.get(equipPage).showCursor(false);
				if(equipPage < 3)
					equipPage++;
				else
					equipPage = 0;
				gameMenu.get(equipPage).showCursor(true);
			} else if(Keybinds.MENU_LEFT.clicked()) {
				gameMenu.get(equipPage).showCursor(false);
				if(equipPage > 0)
					equipPage--;
				else
					equipPage = 3;
				gameMenu.get(equipPage).showCursor(true);
			}
			switch(gameMenu.get(equipPage).select()) {
			case(-1):
				break;
			default:
				switch(equipPage) {
				case(0):
					// Armor
					gameMenu.add(stage.party.getMember(partyMember).getEquipment().getArmor().interactMenu(gameMenu.get(equipPage).getSelected()));
					gameMenu.get(5).setCenterPosition(gameMenu.get(0).getFullWidth() + gameMenu.get(3).getFullWidth() + 35,
							gameMenu.get(1).getFullHeight() + gameMenu.get(3).getFullHeight() + 15);
					gameMenu.get(0).showCursor(false);
					selectedEquipment = gameMenu.get(equipPage).getSelected();
					equipPage = 4;
					break;
				case(1):
					// Weapons
					gameMenu.add(stage.party.getMember(partyMember).getEquipment().getWeapons().interactMenu(gameMenu.get(equipPage).getSelected()));
					gameMenu.get(5).setCenterPosition(gameMenu.get(0).getFullWidth() + gameMenu.get(3).getFullWidth() + 35,
							gameMenu.get(1).getFullHeight() + gameMenu.get(3).getFullHeight() + 15);
					gameMenu.get(1).showCursor(false);
					selectedEquipment = gameMenu.get(equipPage).getSelected();
					equipPage = 5;
					break;
				case(2):
					// Quiver
					gameMenu.add(stage.party.getMember(partyMember).getEquipment().getQuiver().interactMenu(gameMenu.get(equipPage).getSelected()));
					gameMenu.get(5).setCenterPosition(gameMenu.get(0).getFullWidth() + gameMenu.get(3).getFullWidth() + 35,
							gameMenu.get(1).getFullHeight() + gameMenu.get(3).getFullHeight() + 15);
					gameMenu.get(2).showCursor(false);
					selectedEquipment = gameMenu.get(equipPage).getSelected();
					equipPage = 6;
					break;
				case(3):
					// Pockets
					gameMenu.add(stage.party.getMember(partyMember).getEquipment().getPockets().interactMenu(gameMenu.get(equipPage).getSelected()));
					gameMenu.get(5).setCenterPosition(gameMenu.get(0).getFullWidth() + gameMenu.get(3).getFullWidth() + 35,
							gameMenu.get(1).getFullHeight() + gameMenu.get(3).getFullHeight() + 15);
					gameMenu.get(3).showCursor(false);
					selectedEquipment = gameMenu.get(equipPage).getSelected();
					equipPage = 7;
					break;
				default:
					System.out.println("There's no page for that equipment!");
					break;
				}
				break;
			}
			// Return to equipment overview
			if(Keybinds.CANCEL.clicked()) {
				Keybinds.inMenu();
				gameMenu.get(equipPage).showCursor(false);
				selectedEquipment = 0;
				equipPage = -1;
			}
		} else {
			// Navigate item interact menus
			switch(gameMenu.get(5).select()) {
			case(-1):
				break;
			default:
				Textbox text = new PlainText(false, 0f, false);
				switch(equipPage - 4) {
				case(0):
					text = stage.party.getMember(partyMember).getEquipment().getArmor().interact(selectedEquipment, gameMenu.get(5).getSelected(), 
							stage.party.getMember(partyMember));
					break;
				case(1):
					text = stage.party.getMember(partyMember).getEquipment().getWeapons().interact(selectedEquipment, gameMenu.get(5).getSelected(), 
							stage.party.getMember(partyMember));
					break;
				case(2):
					text = stage.party.getMember(partyMember).getEquipment().getQuiver().interact(selectedEquipment, gameMenu.get(5).getSelected(), 
							stage.party.getMember(partyMember));
					doUpdate();
					break;
				case(3):
					text = stage.party.getMember(partyMember).getEquipment().getPockets().interact(selectedEquipment, gameMenu.get(5).getSelected(), 
							stage.party.getMember(partyMember));
					doUpdate();
					break;
				}
				text.setCenterPosition(gameMenu.get(3).getFullWidth() + gameMenu.get(0).getFullWidth() + 40, gameMenu.get(0).getFullHeight() + 75);
				stage.addText(text);
				gameMenu.remove(4);
				Keybinds.inMenu();
				equipPage = -1;
			}

			// Cancel item adjustment
			if(Keybinds.CANCEL.clicked()) {
				gameMenu.remove(5);
				Keybinds.inMenu();
				equipPage = -1;
			}
		}
	}
	
	public void gameOptions(Stage stage) {
		if(optionPage == -1) {
			if(Keybinds.CANCEL.clicked()) {
				closeMenu();
			} else if(Keybinds.MENU_RIGHT.clicked()) {
				gameMenuType = GameMenuType.STATS;
				buildGameMenu(stage);
			} else if(Keybinds.MENU_LEFT.clicked()) {
				gameMenuType = GameMenuType.EQUIPMENT;
				buildGameMenu(stage);
			} else if(Keybinds.CONFIRM.clicked()) {
				gameMenu.get(0).showCursor(true);
				optionPage = 0;
			}
		} else if(optionPage == 0) {
			switch(gameMenu.get(0).select()) {
			// Mute sounds
			case(0):
				Ensemble.get().mute();
				Keybinds.inMenu();
				break;
			// Save the game
			case(1):
				// Perform regular Save
				if(stage.map.isTown()) {
					// If new save file
					if(Theater.saveFile.matches("New")) {
						gameMenu.add(new InputBox(false) {{ 
							setStill(true);
						}});
						optionPage = 0;
						Keybinds.inMenu();
					// Save over old save file
					} else {
						Save.get().save(stage, Theater.saveFile);
						Keybinds.inMenu();
					}
				// Change to Field Save
				} else {
					gameMenu.add(new SelectBox(true) {{ 
						setStill(true);
						setCenterPosition(15, gameMenu.get(0).getFullHeight() + 50);
						buildText("Field Saving will quit the game afterwards.;Ok;Cancel");
					}});
					optionPage = 1;
					Keybinds.inMenu();
				}
				break;
			// Load a saved game
			case(2):
				// No previous saves to load
				if(Save.get().getSaves() == null) {
					Textbox noSaves = new PlainText(false, 3f, false) {{ 
						setStill(true);
						setCenterPosition(15, gameMenu.get(0).getFullHeight() + 50);
						buildText("No saves, save this one!");
					}};
					stage.addText(noSaves);
				// Show saved games
				} else {
					gameMenu.add(new SelectBox(false) {{ 
						setStill(true);
						setCenterPosition(15, gameMenu.get(0).getFullHeight() + 50);
						buildText(Save.get().getSaves());
					}});
					optionPage = 2;
				}
				Keybinds.inMenu();
				break;
			// Exit the game
			case(3):
				Theater.toBeClosed = true;
				break;
			default:
				break;	
			}
			
			if(Keybinds.CANCEL.clicked()) {
				optionPage = -1;
				gameMenu.get(0).showCursor(false);
			}
		} else if(optionPage == 1) {
			// Perform regular Save
			if(stage.map.isTown()) {
				if(gameMenu.get(1).input() != null) {
					Save.get().save(stage, gameMenu.get(1).input());
					gameMenu.remove(1);
					optionPage = -1;
					Keybinds.inMenu();
				}
			// Confirm Field Save
			} else {
				// Confirm Field Save menu
				if(gameMenu.get(1) instanceof SelectBox) {
					switch(gameMenu.get(1).select()) {
					case(-1):
						break;
					case(0):
						gameMenu.remove(1);
						// If it's a new save file
						if(Theater.saveFile.matches("New")) {
							gameMenu.add(new InputBox(false) {{ 
								setStill(true);
								setCenterPosition(15, gameMenu.get(0).getFullHeight() + 50);
							}});
						// If it's a previous save
						} else {
							Save.get().fieldSave(stage, Theater.saveFile);
							Keybinds.inMenu();
							Theater.toBeClosed = true;
						}
						break;
					case(1):
						gameMenu.remove(1);
						optionPage = -1;
						Keybinds.inMenu();
						break;
					}
				// Entering name for Field Save
				} else {
					if(gameMenu.get(1).input() != null) {
						Save.get().fieldSave(stage, gameMenu.get(1).input());
						gameMenu.remove(1);
						Theater.toBeClosed = true;
					} else {
						gameMenu.get(1).update(stage);
					}
				}
			}
		} else if(optionPage == 2) {
			// Load Game menu
			if(gameMenu.size() < 3) {
				switch(gameMenu.get(1).select()) {
				case(-1):
					break;
				// Load selected save file
				default:
					tempSave = Theater.saveFile;
					Theater.saveFile = gameMenu.get(1).getText().get(gameMenu.get(1).getSelected()).getLine();
					if(Save.get().hasFieldSave()) {
						gameMenu.add(new SelectBox(true) {{ 
							setStill(true);
							setCenterPosition(15 + gameMenu.get(0).getFullWidth(), 50);
							buildText("This file has a Field Save. Resuming will delete it.;Resume;Cancel");
						}});
					} else {
						Save.get().loadSave(stage, false);
						gameMenu.remove(1);
						closeMenu();
					}
					break;
				}
				
				if(Keybinds.CANCEL.clicked()) {
					optionPage = 0;
					gameMenu.remove(1);
				}
			} else {
				switch(gameMenu.get(2).select()) {
				case(-1):
					break;
				case(0):
					Save.get().loadSave(stage, true);
					Save.get().clearFieldFromSave();
					titleMenu = null;
					titleMenuOpen = false;
					closeMenu();
					break;
				case(1):
					Theater.saveFile = tempSave;
					optionPage = 2;
					gameMenu.remove(2);
					Keybinds.inMenu();
					break;
				}
			}
		}
	}
	
	public void doUpdate() {
		doUpdate = true;
	}
	
	public void closeMenu() {
		titleMenuOpen = false;
		open = false;
		Keybinds.closeMenu();
	}

	public boolean isOpen() {
		return open;
	}
}
