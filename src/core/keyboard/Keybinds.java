package core.keyboard;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;


public enum Keybinds {
	
	CONFIRM (Keyboard.KEY_RETURN),
	CANCEL (Keyboard.KEY_F),
	RIGHT (Keyboard.KEY_RIGHT),
	LEFT (Keyboard.KEY_LEFT),
	UP (Keyboard.KEY_UP),
	DOWN (Keyboard.KEY_DOWN),
	RUN (Keyboard.KEY_LSHIFT),
	MENU (Keyboard.KEY_E),
	PAUSE (Keyboard.KEY_P),
	ATTACK_MAIN (Keyboard.KEY_D),
	ATTACK_OFFHAND (Keyboard.KEY_A),
	DODGE (Keyboard.KEY_S),
	INTERACT (Keyboard.KEY_W),
	LEFT_EQUIP (Keyboard.KEY_LCONTROL),
	RIGHT_EQUIP (Keyboard.KEY_RCONTROL),
	WEP_SPECIAL (Keyboard.KEY_R),
	ITEM_USE (Keyboard.KEY_C),
	ITEM_CYCLE (Keyboard.KEY_V),
	MENU_UP (Keyboard.KEY_W),
	MENU_RIGHT (Keyboard.KEY_D),
	MENU_LEFT (Keyboard.KEY_A),
	MENU_DOWN (Keyboard.KEY_S),
	DEBUG (Keyboard.KEY_F3),
	EDIT (Keyboard.KEY_F4),
	SLOT1 (Keyboard.KEY_1),
	SLOT2 (Keyboard.KEY_2),
	SLOT3 (Keyboard.KEY_3),
	SLOT4 (Keyboard.KEY_4),
	SLOT5 (Keyboard.KEY_5),
	SLOT6 (Keyboard.KEY_6),
	SLOT7 (Keyboard.KEY_7),
	SLOT8 (Keyboard.KEY_8),
	SLOT9 (Keyboard.KEY_9),
	SLOT0 (Keyboard.KEY_0),
	CANCELTEXT (Keyboard.KEY_TAB),
	EXIT (Keyboard.KEY_ESCAPE);
	
	private Press key;
	
	Keybinds(int k) {
		this.key = new Press(k);
	}
	
	public static void update() {
		for(Keybinds keybinds : Keybinds.values()) {
			keybinds.key.setHeld(keybinds.key.isPressed());
			keybinds.key.update();
		}
	}
	
	/** Key has been pressed. */
	public boolean press() {
		return key.isPressed();
	}
	
	/** Key has been and is currently pressed. */
	public boolean held() {
		return key.isHeld();
	}
	
	/** Key was pressed and is no longer pressed. */
	public boolean clicked() {
		if(key.isPressed() && !key.isHeld())
			return true;
		else
			return false;
	}
	
	/** Key was released. */
	public boolean released() {
		return key.isReleased();
	}
	
	public static void inMenu() {
		Keybinds.ATTACK_MAIN.key.setDisabled(true);
		Keybinds.ATTACK_OFFHAND.key.setDisabled(true);
		Keybinds.DODGE.key.setDisabled(true);
		Keybinds.INTERACT.key.setDisabled(true);
		Keybinds.MENU.key.setDisabled(true);
		Keybinds.PAUSE.key.setDisabled(true);
		Keybinds.WEP_SPECIAL.key.setDisabled(true);
		Keybinds.ITEM_USE.key.setDisabled(true);
		Keybinds.ITEM_CYCLE.key.setDisabled(true);
		Keybinds.SLOT1.key.setDisabled(true);
		Keybinds.SLOT2.key.setDisabled(true);
		Keybinds.SLOT3.key.setDisabled(true);
		Keybinds.SLOT4.key.setDisabled(true);
		Keybinds.SLOT5.key.setDisabled(true);
		Keybinds.SLOT6.key.setDisabled(true);
		Keybinds.SLOT7.key.setDisabled(true);
		Keybinds.SLOT8.key.setDisabled(true);
	}
	
	public static void closeMenu() {
		Keybinds.ATTACK_MAIN.key.setDisabled(false);
		Keybinds.ATTACK_OFFHAND.key.setDisabled(false);
		Keybinds.DODGE.key.setDisabled(false);
		Keybinds.INTERACT.key.setDisabled(false);
		Keybinds.MENU.key.setDisabled(false);
		Keybinds.PAUSE.key.setDisabled(false);
		Keybinds.WEP_SPECIAL.key.setDisabled(false);
		Keybinds.ITEM_USE.key.setDisabled(false);
		Keybinds.ITEM_CYCLE.key.setDisabled(false);
		Keybinds.SLOT1.key.setDisabled(false);
		Keybinds.SLOT2.key.setDisabled(false);
		Keybinds.SLOT3.key.setDisabled(false);
		Keybinds.SLOT4.key.setDisabled(false);
		Keybinds.SLOT5.key.setDisabled(false);
		Keybinds.SLOT6.key.setDisabled(false);
		Keybinds.SLOT7.key.setDisabled(false);
		Keybinds.SLOT8.key.setDisabled(false);
	}

	public static void changeBind(String keybind) {
		String[] temp = keybind.split("=");
		
		Keybinds.valueOf(temp[0]).setKey(Keyboard.getKeyIndex(temp[1]));
	}
	
	public String getKey() {
		return Keyboard.getKeyName(this.key.getKey());
	}
	
	public void setKey(int k) {
		this.key = new Press(k);
	}
	
	public static void clear() {
		Keyboard.destroy();
		try {
			Keyboard.create();
		} catch (LWJGLException e1) {
			e1.printStackTrace();
		}
		for(Keybinds e : Keybinds.values()) {
			e.key.setPressed(false);
		}
	}
}
