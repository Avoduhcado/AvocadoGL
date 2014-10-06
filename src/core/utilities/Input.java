package core.utilities;

import core.Theater;
import core.editor.Editor;
import core.entity.CharState;
import core.keyboard.Keybinds;
import core.scene.PropQuad;
import core.scene.Stage;

public class Input {

	public static void checkInput(Stage stage) {
		Keybinds.update();
		
		playerInput(stage);
	
		if(Keybinds.MENU.clicked()) {
			stage.menu.openMenu(stage);
		}
		if(Keybinds.DEBUG.clicked()) {
			Theater.debug = !Theater.debug;
		}
		if(Keybinds.EDIT.clicked()) {
			Editor.open(stage);
		}
		if(Keybinds.PAUSE.clicked()) {
			Theater.paused = !Theater.paused;
		}
		
		if(Keybinds.CANCEL.clicked()) {
			//stage.player.setCourse(PathQuadStar.get().findPath(stage.player, 15, 15));
			//Ensemble.get().getBackground().setFadeOut(-10f);
			//Screen.centerOn(stage.player());
			//Screen.clearText();
		}
	}
	
	private static void playerInput(Stage stage) {
		if(stage.player != null && !Theater.paused) {
			if(!stage.player.inBusyState()) {
				// TODO Set up proper stats to get dodge capabilities from, possibly remaining vivacity
				if(Keybinds.RUN.press() && stage.player.getStats().canRun()) {
					if(stage.player.getState() != CharState.RUNNING) {
						stage.player.setState(CharState.RUNNING);
					}
					stage.player.setSpeedMod(2f);
					//stage.player.setSpeedBuff(stage.player.getSpeed());
				} else {
					if(stage.player.getState() == CharState.RUNNING) {
						stage.player.setState(CharState.IDLE);
					}
					stage.player.setSpeedMod(1f);
					//stage.player.setSpeedBuff(0);
				}
				
				if(Keybinds.DOWN.press() && !Keybinds.UP.press()) {
					stage.player.setDy(1f);
				} if(Keybinds.RIGHT.press() && !Keybinds.LEFT.press()) {
					stage.player.setDx(1f);
				} if(Keybinds.LEFT.press() && !Keybinds.RIGHT.press()) {
					stage.player.setDx(-1f);
				} if(Keybinds.UP.press() && !Keybinds.DOWN.press()) {
					stage.player.setDy(-1f);
				}
				
				if(Keybinds.DODGE.clicked() && stage.player.getStats().getVivacity().getStat() > 0) {
					//stage.player.dodge(stage.player.getSprite().getWidth() * 2, 0.45f);
					//stage.player.setSpeedBuff(stage.player.getSpeed());
					if(stage.player.getDx() != 0 || stage.player.getDy() != 0)
						stage.player.dodge(stage.player.getSprite().getWidth() * 2, 0.45f, stage.player.getDx(), stage.player.getDy());
					else {
						stage.player.setLockDir(stage.player.getDir());
						stage.player.dodge(stage.player.getSprite().getWidth() * 2, 0.45f, stage.player.getOppositeFace().x, stage.player.getOppositeFace().y);
					}
					stage.player.setSpeedMod(2.5f);
				}
				
				// Equipping weapons
				if(Keybinds.RIGHT_EQUIP.held() || Keybinds.LEFT_EQUIP.held()) {
					if(Keybinds.SLOT1.clicked()) {
						stage.player.getEquipment().getWeapons().holdWeapon(0, Keybinds.RIGHT_EQUIP.held());
					}
					if(Keybinds.SLOT2.clicked()) {
						stage.player.getEquipment().getWeapons().holdWeapon(1, Keybinds.RIGHT_EQUIP.held());
					}
					if(Keybinds.SLOT3.clicked()) {
						stage.player.getEquipment().getWeapons().holdWeapon(2, Keybinds.RIGHT_EQUIP.held());
					}
					if(Keybinds.SLOT4.clicked()) {
						stage.player.getEquipment().getWeapons().holdWeapon(3, Keybinds.RIGHT_EQUIP.held());
					}
					if(Keybinds.SLOT5.clicked()) {
						stage.player.getEquipment().getWeapons().holdWeapon(4, Keybinds.RIGHT_EQUIP.held());
					}
					if(Keybinds.SLOT6.clicked()) {
						stage.player.getEquipment().getWeapons().holdWeapon(5, Keybinds.RIGHT_EQUIP.held());
					}
					if(Keybinds.SLOT7.clicked()) {
						stage.player.getEquipment().getWeapons().holdWeapon(6, Keybinds.RIGHT_EQUIP.held());
					}
					if(Keybinds.SLOT8.clicked()) {
						stage.player.getEquipment().getWeapons().holdWeapon(7, Keybinds.RIGHT_EQUIP.held());
					}
				} else {
					// Swapping styles
					if(stage.player.getEquipment().getWeapons().getEquippedRight() != -1) {
						if(Keybinds.SLOT1.clicked()) {
							stage.player.getEquipment().getWeapons().getHeldRight().setCurrentStyle(0);
							if(Keybinds.SLOT1.held()) {
								//stage.player.chargeProjectile(stage);
							} else if(Keybinds.SLOT1.released()) {
								//stage.player.fireProjectile(stage);
							}
						}
						if(Keybinds.SLOT2.clicked()) {
							stage.player.getEquipment().getWeapons().getHeldRight().setCurrentStyle(1);
							//Benchmark.buildLog();
						}
						if(Keybinds.SLOT3.clicked()) {
							stage.player.getEquipment().getWeapons().getHeldRight().setCurrentStyle(2);
							//stage.player.setCourse(PathQuadStar.get().findPath(stage.player, 200, 450));
						}
						if(Keybinds.SLOT4.clicked()) {
							stage.player.getEquipment().getWeapons().getHeldRight().setCurrentStyle(3);
							//Screenshot.saveScreenshot(new File(System.getProperty("user.dir")), 800, 600);
						}
						if(Keybinds.SLOT5.clicked()) {
							stage.player.getEquipment().getWeapons().getHeldRight().setCurrentStyle(4);
						}
					}
					if(stage.player.getEquipment().getWeapons().getEquippedLeft() != -1) {
						if(Keybinds.SLOT6.clicked()) {
							stage.player.getEquipment().getWeapons().getHeldLeft().setCurrentStyle(0);
						}
						if(Keybinds.SLOT7.clicked()) {
							stage.player.getEquipment().getWeapons().getHeldLeft().setCurrentStyle(1);
						}
						if(Keybinds.SLOT8.clicked()) {
							stage.player.getEquipment().getWeapons().getHeldLeft().setCurrentStyle(2);
						}
						if(Keybinds.SLOT9.clicked()) {
							stage.player.getEquipment().getWeapons().getHeldLeft().setCurrentStyle(3);
						}
						if(Keybinds.SLOT0.clicked()) {
							stage.player.getEquipment().getWeapons().getHeldLeft().setCurrentStyle(4);
						}
					}
				}
				if(Keybinds.WEP_SPECIAL.clicked()) {
					stage.player.getEquipment().weaponSpecial();
				}
				if(Keybinds.ITEM_USE.clicked()) {
					stage.addText(stage.player.getEquipment().getPockets().useSelected(stage.player));
				}
				if(Keybinds.ITEM_CYCLE.clicked()) {
					stage.player.getEquipment().getPockets().cycleSelected();
				}
				if(Keybinds.INTERACT.clicked()) {
					stage.player.interactWith(PropQuad.get().getProps(stage.player), stage);
				}
			}
			
			if(Keybinds.ATTACK_MAIN.press()) {
				if(!Keybinds.ATTACK_MAIN.held() && stage.player.canAttack())
					stage.player.startAttack(true);
				if(movementPressed() && stage.player.getEquipment().getWeapons().getHeldRight().isCharging()) {
					stage.player.cancelCharge(true, stage);
				}
			} else if(Keybinds.ATTACK_MAIN.released()) {
				stage.player.releaseAttack(true, stage);
			}
			
			if(Keybinds.ATTACK_OFFHAND.press()) {
				if(!Keybinds.ATTACK_OFFHAND.held() && stage.player.canAttack())
					stage.player.startAttack(false);
				if(movementPressed() && stage.player.getEquipment().getWeapons().getHeldLeft().isCharging()) {
					stage.player.cancelCharge(false, stage);
				}
			} else if(Keybinds.ATTACK_OFFHAND.released()) {
				stage.player.releaseAttack(false, stage);
			}
			
			if(Keybinds.INTERACT.clicked()) {
				stage.player.interactWith(PropQuad.get().getProps(stage.player), stage);
			}
			
			if(!stage.player.isAlive()) {
				stage.playerDeath();
			}
			
			stage.player.update(stage);
		}
	}
	
	public static boolean movementPressed() {
		if(Keybinds.DOWN.press() || Keybinds.RIGHT.press() || Keybinds.LEFT.press() || Keybinds.UP.press())
			return true;
		return false;
	}
	
}
