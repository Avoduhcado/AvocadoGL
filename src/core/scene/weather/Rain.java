package core.scene.weather;

import core.Theater;

public class Rain extends Weather {

	public Rain(int intensity) {
		super(intensity);
		setTint(0.15f, 0.15f, 0.175f, 0.2f);
	}

	public void update() {
		switch(intensity) {
		case(0):
			if(Math.random()*100 < 45 && downfall.size() < 5000)
				downfall.add(new Raindrop((int)(Math.random() * (Theater.get().getScreen().camera.getY() + Theater.get().getScreen().camera.getHeight())),
						(float) ((Math.random()*8) + 6)));
			break;
		case(1):
			if(Math.random()*100 < 60 && downfall.size() < 10000)
				downfall.add(new Raindrop((int)(Math.random() * (Theater.get().getScreen().camera.getY() + Theater.get().getScreen().camera.getHeight())),
						(float) ((Math.random()*10) + 8)));
			break;
		case(2):
			if(Math.random()*100 < 98 && downfall.size() < 20000)
				downfall.add(new Raindrop((int)(Math.random() * (Theater.get().getScreen().camera.getY() + Theater.get().getScreen().camera.getHeight())),
						(float) ((Math.random()*12) + 10)));
			break;
		default:
			System.out.println("Not particularly rainy out today.");
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
	
}
