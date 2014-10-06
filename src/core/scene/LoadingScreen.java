package core.scene;

import core.entity.Prop;

public class LoadingScreen {

	private Prop background;
	private int fadeTimer = 245;
	private boolean fadeOut = true;
	//private String floatText;
	
	public LoadingScreen() {
		background = new Prop(System.getProperty("resources") + "/backdrops/load.png", 0, 0, false);
		//floatText = "Loading...";
		//Text.setFont("Skwirl.ttf");
	}
	
	public LoadingScreen(String ref, String text) {
		background = new Prop(System.getProperty("resources") + "/backdrops/" + ref + ".png", 0, 0, false);
		//floatText = text;
		//Text.setFont("Skwirl.ttf");
	}
	
	public void draw() {
		background.draw();
		//Text.drawString(floatText, 385, 300, new Color(255, 255, 255, fadeTimer));
		if(fadeTimer <= 10) {
			fadeOut = false;
		} else if(fadeTimer >= 245) {
			fadeOut = true;
		}
		if(fadeOut) {
			fadeTimer -= 5;
		} else {
			fadeTimer += 5;
		}
	}
}
