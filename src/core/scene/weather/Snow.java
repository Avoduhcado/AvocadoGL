package core.scene.weather;

import core.Theater;

public class Snow extends Weather {

	private float wind;
	
	public Snow(int intensity, float wind) {
		super(intensity);
		setTint(0.1f, 0.1f, 0.1f, 0.2f);
		this.setWind(wind);
	}
	
	public void update() {
		switch(intensity) {
		case(0):
			if(Math.random()*100 < 30 && downfall.size() < 2500)
				//downfall.add(new Snowflake((int)(Math.random()*(Screen.camera.getHeight() + 500)) - 250, (float) ((Math.random()*1) + 0.5)));
				downfall.add(new Snowflake((int)(Math.random() * (Theater.get().getScreen().camera.getY() + Theater.get().getScreen().camera.getHeight())), 
						(float) ((Math.random()*1) + 0.5)));
			break;
		case(1):
			if(Math.random()*100 < 45 && downfall.size() < 5000)
				downfall.add(new Snowflake((int)(Math.random() * (Theater.get().getScreen().camera.getY() + Theater.get().getScreen().camera.getHeight())), 
						(float) ((Math.random()*1) + 0.65)));
			break;
		case(2):
			if(Math.random()*100 < 80 && downfall.size() < 10000)
				downfall.add(new Snowflake((int)(Math.random() * (Theater.get().getScreen().camera.getY() + Theater.get().getScreen().camera.getHeight())),
						(float) ((Math.random()*1) + 0.75)));
			break;
		default:
			System.out.println("Not particularly snowy out today.");
			break;
		}
		
		for(int x = 0; x<downfall.size(); x++) {
			downfall.get(x).update();
			if(downfall.get(x).isLanded()) {
				downfall.remove(x);
				x--;
			}
		}
		//System.out.println(downfall.size());
	}
	
	public float getWind() {
		return wind;
	}

	public void setWind(float wind) {
		this.wind = wind;
	}
	
}
