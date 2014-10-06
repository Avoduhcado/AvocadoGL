package core.entity.ai;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import core.entity.Actor;
import core.scene.PropQuad;
import core.scene.Stage;

public class IdlePatrol extends AIComponent {

	private Rectangle2D agroRange;
	
	public IdlePatrol(Actor source) {
		super(source);
		agroRange = new Rectangle2D.Float((float)source.getBox().getCenterX() - 100, (float)source.getBox().getCenterY() - 100, 200, 200);
	}

	@Override
	public void update(Stage stage) {
		updateAgroRange();
		ArrayList<Actor> actors = new ArrayList<Actor>();
		actors = PropQuad.get().getActors(agroRange);
		
		if(!actors.isEmpty()) {
			if(actors.size() > 1) {
				Actor target = getClosestTarget(actors);
				if(target != null) {
					source.getAI().setTarget(target);
					source.getAI().sendAlert(AIAlert.THREATENED, 1f);
				}
			}
			actors = null;
		}
	}

	public void updateAgroRange() {
		agroRange.setFrame((float)source.getBox().getCenterX() - 100, (float)source.getBox().getCenterY() - 100, 200, 200);
	}
	
	public Actor getClosestTarget(ArrayList<Actor> targets) {
		Actor closest = targets.get(0);
		float distance = -1;
		if(agroRange.intersects(closest.getBox()) && closest != source && !source.getAI().isAllied(closest))
			distance = closest.getDistance(source.getX(), source.getY());
		
		for(int x = 1; x<targets.size(); x++) {
			if((targets.get(x).getDistance(source.getX(), source.getY()) < distance || distance == -1) && agroRange.intersects(targets.get(x).getBox()) 
					&& targets.get(x) != source && !source.getAI().isAllied(targets.get(x))) {
				closest = targets.get(x);
				distance = targets.get(x).getDistance(source.getX(), source.getY());
			}
		}
		
		if(!agroRange.intersects(closest.getBox()) || closest == source || source.getAI().isAllied(closest))
			closest = null;
		
		return closest;
	}
	
	@Override
	public void receiveAlert(AIAlert alert, float urgency, Hostility hostility) {
		// TODO Auto-generated method stub
		
	}

}
