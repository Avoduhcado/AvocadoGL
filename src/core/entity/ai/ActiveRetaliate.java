package core.entity.ai;

import core.Theater;
import core.entity.Actor;
import core.entity.CharState;
import core.scene.Stage;

public class ActiveRetaliate extends AIComponent {

	private boolean movingIn;
	private boolean inRange;
	private float chaseTimer = 0f;
	
	public ActiveRetaliate(Actor source) {
		super(source);
	}

	@Override
	public void update(Stage stage) {
		if(!inRange) {
			if(source.canHit(source.getAI().getTarget())) {
				inRange = true;
				movingIn = false;
				source.getCourse().clear();
			} else {
				if(source.getStats().canRun()) {
					if(source.getState() != CharState.RUNNING) {
						source.setState(CharState.RUNNING);
					}
					source.setSpeedMod(2.2f);
				} else {
					if(source.getState() == CharState.RUNNING) {
						source.setState(CharState.WALKING);
					}
					source.setSpeedMod(1f);
				}
				
				chaseTimer += Theater.getDeltaSpeed(0.025f);
				if(source.getCourse().isEmpty() && movingIn) {
					movingIn = false;
				} else if(chaseTimer >= 0.5f) {
					chaseTimer = 0f;
					source.getCourse().clear();
					chaseTarget();
				}
			}
		} else {
			if(source.canAttack()) {
				source.faceTarget(source.getAI().getTarget());
				source.startAttack(true);
			}
			if(!source.canHit(source.getAI().getTarget())) {
				inRange = false;
			}
		}

		if(!movingIn && !inRange && !source.inBusyState()) {
			chaseTarget();
			movingIn = true;
		}
		
		// TODO Cancel retaliation to yielding
		if(!source.getAI().getTarget().isAlive()) {
			source.getAI().setTarget(null);
			priority = 0f;
		}
	}

	@Override
	public void receiveAlert(AIAlert alert, float urgency, Hostility hostility) {
		switch(alert) {
		case ATTACKED:
			priority = 1f;
			break;
		}
	}

}
