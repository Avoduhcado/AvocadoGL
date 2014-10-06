package core.entity.ai;

import java.awt.geom.Rectangle2D;

import core.Theater;
import core.entity.Actor;
import core.scene.PropQuad;
import core.scene.Stage;
import core.utilities.pathfinding.PathQuadStar;

public class IdleWander extends AIComponent {

	private boolean wandering;
	private float wait;
	private Rectangle2D range;
	
	public IdleWander(Actor source) {
		super(source);
	}

	@Override
	public void update(Stage stage) {
		if(source.getSpeed() > 0) {
			if(!wandering) {
				float roll = (float) Math.random();
				if(roll < 0.75f) {
					float x;
					float y;
					if(range == null) {
						x = (float) ((Math.random()*400) - 200) + getSource().getX();
						y = (float) ((Math.random()*300) - 150) + getSource().getY();
					} else {
						x = (float) ((Math.random()*range.getWidth()) + range.getX());
						y = (float) ((Math.random()*range.getHeight()) + range.getY());
					}
					
					if(x <= 0)
						x = 1;
					if(y <= 0)
						y = 1;
					if(x >= PropQuad.get().getTopQuad().getMaxX())
						x = (float) (PropQuad.get().getTopQuad().getMaxX() - 1f);
					if(y >= PropQuad.get().getTopQuad().getMaxY())
						y = (float) (PropQuad.get().getTopQuad().getMaxY() - 1f);
					
					source.setCourse(PathQuadStar.get().findPath(getSource(), x, y));
				} else {
					wait = (float) (Math.random()*5) + 2;
				}
				wandering = true;
			} else {
				if(source.getCourse().isEmpty() && wait <= 0) {
					wandering = false;
				} else if(wait >= 0) {
					wait -= Theater.getDeltaSpeed(0.025f);
				}
			}
		}
	}

	public void setRange(Rectangle2D range) {
		this.range = range;
	}
	
	@Override
	public void receiveAlert(AIAlert alert, float urgency, Hostility hostility) {
		// TODO Auto-generated method stub
		
	}
	
}
