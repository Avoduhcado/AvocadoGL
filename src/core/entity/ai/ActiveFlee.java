package core.entity.ai;

import java.awt.geom.Rectangle2D;
import core.entity.Actor;
import core.scene.PropQuad;
import core.scene.Stage;
import core.utilities.pathfinding.PathQuadStar;

public class ActiveFlee extends AIComponent {

	// TODO Add a radius for differing fear amounts
	private boolean fleeing;
	
	public ActiveFlee(Actor source) {
		super(source);
	}

	@Override
	public void update(Stage stage) {
		if(source.getAI().getTarget() != null && source.getSpeed() > 0) {
			if(!fleeing) {
				Rectangle2D range = new Rectangle2D.Float();
				range.setFrame(source.getAI().getTarget().getBox().getCenterX() - 100, source.getAI().getTarget().getBox().getCenterY() - 100, 200, 200);

				float x = 0;
				float y = 0;
				
				do {
					if(source.getAI().getTarget().getBox().getX() >= source.getBox().getX() && source.getBox().getX() > 1)
						x = getSource().getX() - (float) ((Math.random()*200) + 200);
					else
						x = getSource().getX() + (float) ((Math.random()*200) + 200);
					if(source.getAI().getTarget().getBox().getY() >= source.getBox().getY() && source.getBox().getY() > 1)
						y = getSource().getY() - (float) ((Math.random()*200) + 200);
					else
						y = getSource().getY() + (float) ((Math.random()*200) + 200);
				} while(range.contains(x, y));
				
				if(x <= 0)
					x = 1;
				if(y <= 0)
					y = 1;
				if(x >= PropQuad.get().getTopQuad().getMaxX())
					x = (float) (PropQuad.get().getTopQuad().getMaxX() - 1f);
				if(y >= PropQuad.get().getTopQuad().getMaxY())
					y = (float) (PropQuad.get().getTopQuad().getMaxY() - 1f);
				
				source.setCourse(PathQuadStar.get().findPath(getSource(), x, y));
				fleeing = true;
			} else {
				if(source.getCourse().isEmpty()) {
					fleeing = false;
					if(!PropQuad.get().getActors(new Rectangle2D.Double(source.getAI().getTarget().getBox().getCenterX() - 100,
							source.getAI().getTarget().getBox().getCenterY() - 100, 200, 200)).contains(source)) {
						priority -= 0.2f;
					}
				}
			}
		}
	}

	@Override
	public void receiveAlert(AIAlert alert, float urgency, Hostility hostility) {
		switch(alert) {
		case THREATENED:
			priority = 0.75f;
			break;
		case ATTACKED:
			priority = 1f;
			break;
		}
	}
	
}
