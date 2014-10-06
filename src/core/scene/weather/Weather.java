package core.scene.weather;

import java.util.ArrayList;

public class Weather {

	protected ArrayList<Downfall> downfall = new ArrayList<Downfall>();
	protected int intensity;
	protected float red, green, blue, opacity;
	
	public Weather(int intensity) {
		this.intensity = intensity;
	}
	
	public void update() {
		
	}
	
	public void draw() {
		for(int x = 0; x<downfall.size(); x++) {
			downfall.get(x).draw();
		}
	}
	
	public void setTint(float r, float g, float b, float o) {
		red = r;
		green = g;
		blue = b;
		opacity = o;
	}
	
	public float getRed() {
		return red;
	}
	
	public float getGreen() {
		return green;
	}
	
	public float getBlue() {
		return blue;
	}
	
	public float getOpacity() {
		return opacity;
	}
	
}
