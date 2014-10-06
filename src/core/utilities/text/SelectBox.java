package core.utilities.text;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import core.Theater;
import core.keyboard.Keybinds;
import core.scene.Stage;
import core.utilities.exceptions.FailedTextureException;
import core.utilities.loader.TextureLoader;

public class SelectBox extends Textbox {

	private Texture select;
	private int selectable;
	private boolean hasTitle = false;
	private boolean showCursor = true;
	
	public SelectBox(boolean hasTitle) {
		try {
			select = TextureLoader.get().getSlickTexture(System.getProperty("resources") + "/ui/menuSelect.png");
		} catch (FailedTextureException e) {
			select = e.loadErrorTexture();
		}
		this.hasTitle = hasTitle;
		if(hasTitle)
			selectable = 1;
		else
			selectable = 0;
		Keybinds.inMenu();
		//TODO Might cause errors?
		//clearKeyboard();
	}
	
	public void update(Stage stage) {
		if(source != null) {
			setCenterPosition(source.getX(), source.getY());
		}
		
		switch(select()) {
		case(-1):
			break;
		default:
			if(action != null) {
				action.readAction(stage);
			}
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
			for(int i = 0; i<text.size(); i++) {
				text.get(i).drawShadow(this.x + 21, this.y + 21 + (i * lineheight));
				text.get(i).draw(this.x + 20, this.y + 20 + (i * lineheight));
			}
		} else {
			for(int i = 0; i<text.size(); i++) {
				text.get(i).drawShadow(this.x + 21 - (float)Math.floor(Theater.get().getScreen().camera.getX()), 
						(this.y + 21 + (i * lineheight)) - (float)Math.floor(Theater.get().getScreen().camera.getY()));
				text.get(i).draw(this.x + 20 - (float)Math.floor(Theater.get().getScreen().camera.getX()), 
						(this.y + 20 + (i * lineheight)) - (float)Math.floor(Theater.get().getScreen().camera.getY()));
			}
		}
		
		if(showCursor)
			drawSelect();
		if(overflow) {
			drawScroll();
		}
	}
	
	public void drawSelect() {
		GL11.glPushMatrix();
		select.bind();

		if(still)
			GL11.glTranslatef(x, (y + 23) + (selectable * lineheight), 0);
		else
			GL11.glTranslatef(x - (float)Theater.get().getScreen().camera.getX(), ((y + 23) + (selectable * lineheight)) - (float)Theater.get().getScreen().camera.getY(), 0.0f);
		GL11.glColor3d(1,1,1);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2f(0, 0);
			GL11.glTexCoord2f(select.getWidth(), 0);
			GL11.glVertex2f(select.getImageWidth(), 0);
			GL11.glTexCoord2f(select.getWidth(), select.getHeight());
			GL11.glVertex2f(select.getImageWidth(), select.getImageHeight());
			GL11.glTexCoord2f(0, select.getHeight());
			GL11.glVertex2f(0, select.getImageHeight());
		}
		GL11.glEnd();
		GL11.glPopMatrix();
		/*
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glPushMatrix();
		GL11.glTranslatef(x, (y + 20) + (selectable * lineheight), 0);
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glLineWidth(1.0f);
		GL11.glBegin(GL11.GL_LINE_LOOP);
		{
			GL11.glVertex2f(0, 0);
			GL11.glVertex2f(500, 0);
			GL11.glVertex2f(0, 0);
			GL11.glVertex2f(500, 0);
		}
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		*/
	}
	
	public int select() {
		if(Keybinds.MENU_UP.clicked()) {
			if(selectable == 0 && !hasTitle) {
				if(overflow) {
					TextLine temp = text.get(text.size()-1);
					for(int x = text.size()-1; x>0; x--) {
						text.set(x, text.get(x-1));
					}
					text.set(0, temp);
				} else
					selectable = text.size() - 1;
			} else if(selectable == 1 && hasTitle) {
				if(overflow) {
					TextLine temp = text.get(text.size()-1);
					for(int x = text.size()-1; x>1; x--) {
						text.set(x, text.get(x-1));
					}
					text.set(1, temp);
				} else
					selectable = text.size() - 1;
			} else {
				selectable--;
			}
		} else if(Keybinds.MENU_DOWN.clicked()) {
			if(selectable == text.size() - 1 && !hasTitle) {
				selectable = 0;
			} else if(selectable == text.size() - 1 && hasTitle) {
				selectable = 1;
			} else {
				if(overflow && (this.y + 40 + ((selectable+2) * lineheight)) >= Theater.get().getScreen().camera.getHeight()) {
					TextLine temp = text.get(0);
					for(int x = 1; x<text.size(); x++) {
						text.set(x - 1, text.get(x));
					}
					text.set(text.size()-1, temp);
				} else
					selectable++;
			}
		}
		if(Keybinds.CONFIRM.clicked()) {
			Keyboard.enableRepeatEvents(false);
			Keybinds.closeMenu();
			if(hasTitle) {
				return selectable - 1;
			} else {
				return selectable;
			}
		}
		
		return -1;
	}
	
	public int getSelected() {
		if(hasTitle) {
			return selectable - 1;
		} else {
			return selectable;
		}
	}
	
	public void setSelected(int selected) {
		if(selected == 0 && hasTitle)
			selectable = 1;
		else
			selectable = selected;
	}

	public void showCursor(boolean showCursor) {
		this.showCursor = showCursor;
	}
	
	@Override
	public String input() {
		return null;
	}

	@Override
	public void cancelFill() {
	}
	
}
