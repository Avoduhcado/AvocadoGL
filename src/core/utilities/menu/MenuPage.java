package core.utilities.menu;

import core.scene.Stage;
import core.utilities.text.Textbox;

public abstract class MenuPage {

	protected Textbox focus;
	
	public abstract void update(Stage stage);
	public abstract void movePages();
	public abstract void draw();
	
	public abstract void focus();
	public abstract void refresh();
	public abstract void close();
	
	public Textbox getFocus() {
		return focus;
	}
	
}
