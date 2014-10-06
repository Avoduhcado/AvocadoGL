package core.entity.ai;

import java.awt.geom.Rectangle2D;

import core.entity.Actor;
import core.scene.PropQuad;
import core.scene.Stage;
import core.utilities.pathfinding.PathQuadStar;

public abstract class AIComponent {

	protected Actor source;
	protected float priority;
	
	public AIComponent(Actor source) {
		this.source = source;
	}

	public static AIComponent loadComponent(String ref, Actor source) {
		AIComponent component = null;
		String[] temp = ref.split(";");
		
		if(ref.matches("ActiveAttack")) {
			component = new ActiveAttack(source);
		} else if(ref.matches("ActiveFlee")) {
			component = new ActiveFlee(source);
		} else if(ref.matches("ActiveRetaliate")) {
			component = new ActiveRetaliate(source);
		} else if(ref.matches("IdleFlee")) {
			component = new IdleFlee(source);
		} else if(ref.matches("IdlePatrol")) {
			component = new IdlePatrol(source);
		} else if(ref.matches("IdleWander")) {
			component = new IdleWander(source);
			((IdleWander) component).setRange(new Rectangle2D.Float(Float.parseFloat(temp[1]), Float.parseFloat(temp[2]), Float.parseFloat(temp[3]), Float.parseFloat(temp[4])));
		}
		
		return component;
	}
	
	public abstract void update(Stage stage);
	
	public void chaseTarget() {
		float x;
		float y;

		Rectangle2D range = new Rectangle2D.Double(source.getAI().getTarget().getX() - source.getAttackRange(1), source.getAI().getTarget().getY() - source.getAttackRange(0),
				source.getAI().getTarget().getBox().getWidth() + (source.getAttackRange(1) * 2), source.getAI().getTarget().getBox().getHeight() + (source.getAttackRange(0) * 2));
		x = (float) ((Math.random() * range.getWidth()) + range.getX());
		y = (float) ((Math.random() * range.getHeight()) + range.getY());
		
		/*if(source.getX() < source.getAI().getTarget().getX())
			x = (float) (source.getAI().getTarget().getBox().getMaxX() + source.getAttackRange(2) + (Math.random()*(source.getAI().getTarget().getBox().getWidth() / 2)));
		else
			x = (float) (source.getAI().getTarget().getBox().getX() - source.getAttackRange(1) + (Math.random()*(source.getAI().getTarget().getBox().getWidth() / 2)));
		if(source.getY() < source.getAI().getTarget().getY())
			y = (float) (source.getAI().getTarget().getBox().getMaxY() + source.getAttackRange(3) + (Math.random()*(source.getAI().getTarget().getBox().getHeight() / 2)));
		else
			y = (float) (source.getAI().getTarget().getBox().getY() - source.getAttackRange(0)  + (Math.random()*(source.getAI().getTarget().getBox().getHeight() / 2)));
		*/
		if(x <= 0)
			x = 1;
		if(y <= 0)
			y = 1;
		if(x >= PropQuad.get().getTopQuad().getMaxX())
			x = (float) (PropQuad.get().getTopQuad().getMaxX() - 1f);
		if(y >= PropQuad.get().getTopQuad().getMaxY())
			y = (float) (PropQuad.get().getTopQuad().getMaxY() - 1f);

		source.setCourse(PathQuadStar.get().findPath(source, x, y));
	}
	
	public Actor getSource() {
		return source;
	}

	public void setSource(Actor source) {
		this.source = source;
	}
	
	public float getPriority() {
		return priority;
	}

	public void setPriority(float priority) {
		this.priority = priority;
	}
	
	public abstract void receiveAlert(AIAlert alert, float urgency, Hostility hostility);
	
}
