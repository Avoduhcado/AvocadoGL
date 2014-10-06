package core.scene;

import core.Theater;
import core.entity.Prop;

public class DeathScreen {

	private Prop background;
	private float timer = 3;
	
	public DeathScreen() {
		background = new Prop(System.getProperty("resources") + "/backdrops/Gameover.png", 0, 0, false);
	}
	
	public void draw() {
		background.draw();
		setTimer(getTimer() - Theater.getDeltaSpeed(0.025f));
	}

	public float getTimer() {
		return timer;
	}

	public void setTimer(float timer) {
		this.timer = timer;
	}
	
}
