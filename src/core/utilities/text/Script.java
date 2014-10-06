package core.utilities.text;

import java.util.ArrayList;

import core.scene.Stage;
import core.utilities.actions.Action;

public class Script {

	private ArrayList<Action> actions = new ArrayList<Action>();
	private ArrayList<Textbox> textboxes = new ArrayList<Textbox>();
	private boolean interactPrompt;
	
	public void update(Stage stage) {
		for(int x = 0; x<actions.size(); x++) {
			actions.get(x).update(stage);
		}
		for(int x = 0; x<textboxes.size(); x++) {
			textboxes.get(x).update(stage);
		}
	}
	
	public void draw() {
		for(int x = 0; x<actions.size(); x++) {
			if(actions.get(x).hasText())
				actions.get(x).getText().draw();
		}
		for(int x = 0; x<textboxes.size(); x++) {
			textboxes.get(x).draw();
		}
	}
	
	public void clear() {
		setInteractPrompt(false);
		actions.clear();
		textboxes.clear();
	}
	
	public boolean interactActive() {
		return interactPrompt;
	}
	
	public void setInteractPrompt(boolean prompt) {
		this.interactPrompt = prompt;
	}
	
	public void addAction(Action action) {
		actions.add(action);
	}
	
	public void removeAction(Action action) {
		actions.remove(action);
	}
	
	public void addText(Textbox text) {
		textboxes.add(text);
	}
	
	public void removeText(Textbox text) {
		textboxes.remove(text);
	}
	
}
