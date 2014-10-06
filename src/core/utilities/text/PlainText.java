package core.utilities.text;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import core.Theater;
import core.keyboard.Keybinds;
import core.scene.Stage;

public class PlainText extends Textbox {

	private boolean borderless;
	private boolean hasTimer;
	private float timer;
	private ArrayList<TextLine> fillText = new ArrayList<TextLine>();
	private int fillPosition = 0;
	private float fillTimer = 0;
	private boolean filling;

	public PlainText(boolean borderless, float timer, boolean filling) {
		this.borderless = borderless;
		this.filling = filling;
		if(timer > 0f) {
			hasTimer = true;
			this.timer = timer;
		}
	}
	
	public void update(Stage stage) {
		if(source != null) {
			setCenterPosition(source.getX(), source.getY());
		}
		
		if(speed.x != 0 || speed.y != 0)
			move();
		
		if(filling) {
			fillTimer += Theater.getDeltaSpeed(0.025f);
			if(fillTimer > 0.075f) {
				fillPosition++;
				fillTimer = 0f;
			}
		}
		
		if(hasTimer) {
			if(timer > 0) {
				timer -= Theater.getDeltaSpeed(0.025f);
			} else {
				stage.removeText(this);
			}
		}
		
		if(action != null || filling) {
			if(Keybinds.CONFIRM.clicked()) {
				if(filling)
					filling = false;
				else
					action.readAction(stage);
			}
		}
	}
	
	public void draw() {
		if(!borderless) {
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
		}
		
		if(filling) {
			if(fillText.isEmpty() && !text.isEmpty()) {
				fillText.add(new TextLine(""));
			} else if(fillText.get(fillText.size() - 1).getLine().matches(text.get(fillText.size() - 1).getLine()) && fillText.size() < text.size()) {
				fillText.add(new TextLine(""));
			} else {
				fillText.set(fillText.size() - 1, new TextLine(text.get(fillText.size() - 1).getLine().substring(0, fillPosition)));
				if(text.get(fillText.size() - 1).getLine().length() <= fillPosition)
					fillPosition = 0;
			}
			
			if(still) {
				//unifont.drawString(this.x + 21, this.y + 21 + (x * lineheight), fillText.get(x), Color.black);
				//unifont.drawString(this.x + 20, this.y + 20 + (x * lineheight), fillText.get(x), defaultColor);
				for(int i = 0; i<fillText.size(); i++) {
					fillText.get(i).drawShadow(this.x + 21, this.y + 21 + (i * lineheight));
					fillText.get(i).draw(this.x + 20, this.y + 20 + (i * lineheight));
				}
			} else {
				/*unifont.drawString(this.x + 21 - (float)Math.floor(Theater.get().getScreen().camera.getX()),
							(this.y + 21 + (x * lineheight)) - (float)Math.floor(Theater.get().getScreen().camera.getY()), fillText.get(x), Color.black);
					unifont.drawString(this.x + 20 - (float)Math.floor(Theater.get().getScreen().camera.getX()), 
							(this.y + 20 + (x * lineheight)) - (float)Math.floor(Theater.get().getScreen().camera.getY()), fillText.get(x), defaultColor);*/
				for(int i = 0; i<fillText.size(); i++) {
					fillText.get(i).drawShadow(this.x + 21 - (float)Math.floor(Theater.get().getScreen().camera.getX()), 
							(this.y + 21 + (i * lineheight)) - (float)Math.floor(Theater.get().getScreen().camera.getY()));
					fillText.get(i).draw(this.x + 20 - (float)Math.floor(Theater.get().getScreen().camera.getX()), 
							(this.y + 20 + (i * lineheight)) - (float)Math.floor(Theater.get().getScreen().camera.getY()));
				}
			}
			if(fillText.get(fillText.size()-1).getLine().matches(text.get(text.size()-1).getLine()))
				filling = false;
		} else {
			if(still) {
				//unifont.drawString(this.x + 21, this.y + 21 + (x * lineheight), text.get(x), Color.black);
				//unifont.drawString(this.x + 20, this.y + 20 + (x * lineheight), text.get(x), defaultColor);
				for(int i = 0; i<text.size(); i++) {
					text.get(i).drawShadow(this.x + 21, this.y + 21 + (i * lineheight));
					text.get(i).draw(this.x + 20, this.y + 20 + (i * lineheight));
				}
			} else {
				/*unifont.drawString(this.x + 21 - (float)Math.floor(Theater.get().getScreen().camera.getX()),
							(this.y + 21 + (x * lineheight)) - (float)Math.floor(Theater.get().getScreen().camera.getY()), text.get(x), Color.black);
					unifont.drawString(this.x + 20 - (float)Math.floor(Theater.get().getScreen().camera.getX()), 
							(this.y + 20 + (x * lineheight)) - (float)Math.floor(Theater.get().getScreen().camera.getY()), text.get(x), defaultColor);*/
				for(int i = 0; i<text.size(); i++) {
					text.get(i).drawShadow(this.x + 21 - (float)Math.floor(Theater.get().getScreen().camera.getX()), 
							(this.y + 21 + (i * lineheight)) - (float)Math.floor(Theater.get().getScreen().camera.getY()));
					text.get(i).draw(this.x + 20 - (float)Math.floor(Theater.get().getScreen().camera.getX()), 
							(this.y + 20 + (i * lineheight)) - (float)Math.floor(Theater.get().getScreen().camera.getY()));
				}
			}
		}
		
		if(overflow) {
			drawScroll();
		}
	}

	public void resetFill() {
		filling = true;
		fillPosition = 0;
		fillTimer = 0;
	}
	
	public void cancelFill() {
		filling = false;
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
	public String input() {
		return null;
	}

	@Override
	public void showCursor(boolean showCursor) {		
	}
	
}
