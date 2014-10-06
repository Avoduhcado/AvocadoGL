package core.utilities.text;

import java.awt.geom.Rectangle2D;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

import core.Theater;
import core.keyboard.Keybinds;
import core.scene.Stage;
import core.utilities.Events;

public class InputBox extends Textbox {

	private boolean hasInputNumbers = false;
	private String text;
	private float flash = 0.0f;
	
	public InputBox(boolean hasInputNumbers) {
		clearKeyboard();
		this.hasInputNumbers = hasInputNumbers;
		Keyboard.enableRepeatEvents(true);
		Keybinds.inMenu();
		this.text = "";
	}
	
	public void update(Stage stage) {
		if(source != null) {
			setCenterPosition(source.getX(), source.getY());
		}
				
		if(input() != null) {
			if(hasInputNumbers) {
				if(!this.getText().get(0).getLine().isEmpty())
					Events.get().setInputHolder(Integer.parseInt(this.text));
				else
					Events.get().setInputHolder(0);
			}
		}
				
		if(flash < 1.0f)
			flash += Theater.getDeltaSpeed(0.025f);
		else
			flash = 0;
		
		if(action != null) {
			action.readAction(stage);
		}
	}
	
	public void draw() { 
		GL11.glPushMatrix();
		background.bind();
		
		if(still)
			GL11.glTranslatef(x, y, 0);
		else
			GL11.glTranslated(x - Math.floor(Theater.get().getScreen().camera.getX()), y - Math.floor(Theater.get().getScreen().camera.getY()), 0);
	    GL11.glColor4f(1, 1, 1, 0.8f);
	    GL11.glEnable(GL11.GL_BLEND);
	    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		
	    for(int x = 0; x<3; x++) {
	    	for(int y = 0; y<3; y++) {
	    		if((x + 1)%2 != 0 && (y+1)%2 != 0) {
	    			setCornerQuads(x, y);
	    		} else if((x + 1)%2 == 0 && (y+1)%2 == 0) {
	    			setInnerQuads(x, y);
	    		} else if ((x + 1)%2 != 0){
	    			setVertQuads(x, y);
	    		} else {
	    			setHorizQuads(x, y);
	    		}
	    	}
	    }	    
		GL11.glEnd();
		GL11.glPopMatrix();
		
		if(still) {
			Text.drawString(flash > 0.5f ? text + "|" : text, this.x + 21, this.y + 11, Color.black);
			Text.drawString(flash > 0.5f ? text + "|" : text, this.x + 20, this.y + 10, Text.defaultColor);
		} else {
			Text.drawString(flash > 0.5f ? text + "|" : text, this.x + 21 - (float)Math.floor(Theater.get().getScreen().camera.getX()), 
					this.y + 11 - (float)Math.floor(Theater.get().getScreen().camera.getY()), Color.black);
			Text.drawString(flash > 0.5f ? text + "|" : text, this.x + 20 - (float)Math.floor(Theater.get().getScreen().camera.getX()), 
					this.y + 10 - (float)Math.floor(Theater.get().getScreen().camera.getY()), Text.defaultColor);
		}
	}
	
	public void buildText(String message) {
		lineheight = Text.defaultUnifont.getHeight(message);
		int adjust = width/2;
		
		bounds = new Rectangle2D.Double(0, 0, Text.defaultUnifont.getWidth(message) - adjust, 0);
		text = message;
	}
	
	public String input() {
		while(Keyboard.next()) {
			if(Keyboard.getEventKeyState()) {
				if(Keyboard.getEventKey() != Keyboard.KEY_LSHIFT && Keyboard.getEventKey() != Keyboard.KEY_RSHIFT && Keyboard.getEventKey() != Keyboard.KEY_RIGHT
						 && Keyboard.getEventKey() != Keyboard.KEY_LEFT && Keyboard.getEventKey() != Keyboard.KEY_DOWN && Keyboard.getEventKey() != Keyboard.KEY_UP) {
					if(Keyboard.getEventKey() == Keyboard.KEY_BACK && text.length() > 0) {
						text = (String)text.subSequence(0, text.length() - 1);
					} else if(Keyboard.getEventKey() != Keyboard.KEY_BACK && text.length() <= 25) {
						if(hasInputNumbers && text.length() < 9) {
							if(Keyboard.getEventKey() == Keyboard.KEY_0 || Keyboard.getEventKey() == Keyboard.KEY_1 || Keyboard.getEventKey() == Keyboard.KEY_2 || 
									Keyboard.getEventKey() == Keyboard.KEY_3 || Keyboard.getEventKey() == Keyboard.KEY_4 || Keyboard.getEventKey() == Keyboard.KEY_5 || 
									Keyboard.getEventKey() == Keyboard.KEY_6 || Keyboard.getEventKey() == Keyboard.KEY_7 || Keyboard.getEventKey() == Keyboard.KEY_8 || 
									Keyboard.getEventKey() == Keyboard.KEY_9 || Keyboard.getEventKey() == Keyboard.KEY_NUMPAD0 || Keyboard.getEventKey() == Keyboard.KEY_NUMPAD1 || 
									Keyboard.getEventKey() == Keyboard.KEY_NUMPAD2 || Keyboard.getEventKey() == Keyboard.KEY_NUMPAD3 || Keyboard.getEventKey() == Keyboard.KEY_NUMPAD4 || 
									Keyboard.getEventKey() == Keyboard.KEY_NUMPAD5 || Keyboard.getEventKey() == Keyboard.KEY_NUMPAD6 || Keyboard.getEventKey() == Keyboard.KEY_NUMPAD7 || 
									Keyboard.getEventKey() == Keyboard.KEY_NUMPAD8 || Keyboard.getEventKey() == Keyboard.KEY_NUMPAD9) {
								text = text + Keyboard.getEventCharacter();
							}
						} else if(!hasInputNumbers) {
							text = text + Keyboard.getEventCharacter();
						}
					}
					buildText(text);
				}
			}
		}
		if(Keybinds.CANCELTEXT.clicked()) {
			Keyboard.enableRepeatEvents(false);
			Keybinds.closeMenu();
			Theater.get().getStage().removeText(this);
		}
		if(Keybinds.CONFIRM.clicked()) {
			Keyboard.enableRepeatEvents(false);
			Keybinds.closeMenu();
			return text;
		}
		
		return null;
	}

	@Override
	public int select() {
		return 0;
	}

	@Override
	public int getSelected() {
		return 0;
	}

	@Override
	public void cancelFill() {		
	}

	@Override
	public void showCursor(boolean showCursor) {		
	}
	
}
