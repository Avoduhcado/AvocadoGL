package core.entity.ai;

import core.Theater;
import core.entity.Actor;
import core.entity.CharState;
import core.entity.weapons.MagicWeapon;
import core.entity.weapons.RangedWeapon;
import core.scene.Stage;

public class ActiveAttack extends AIComponent {

	private boolean movingIn;
	private boolean inRange;
	private float charge;
	private float chaseTimer = 0;
	
	public ActiveAttack(Actor source) {
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
					source.setSpeedMod(2f);
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
			} else if((source.getEquipment().getWeapons().getSwingingWeapon() instanceof MagicWeapon 
					|| source.getEquipment().getWeapons().getSwingingWeapon() instanceof RangedWeapon) && source.getEquipment().getWeapons().getHeldRight().isSwinging()) {
				
				charge += Theater.getDeltaSpeed(0.025f);
				if(charge >= 3) {
					charge = 0;
					source.releaseAttack(true, stage);
					System.out.println("SDfsdfsd");
				}
					
				/*if(source.canHit(source.getAI().getTarget())) {
					source.releaseAttack(true, stage);
					System.out.println("SDfsdfsd");
					priority = 0f;
				}*/
			}
			
			if(!source.canHit(source.getAI().getTarget())) {
				inRange = false;
				source.cancelCharge(true, stage);
			}
		}

		if(!movingIn && !inRange && !source.inBusyState()) {
			chaseTarget();
			movingIn = true;
		}
		
		if(!source.getAI().getTarget().isAlive()) {
			source.getAI().setTarget(null);
			priority = 0f;
		}
	}

	@Override
	public void receiveAlert(AIAlert alert, float urgency, Hostility hostility) {
		switch(alert) {
		case THREATENED:
		case ATTACKED:
			priority = 1f;
			break;
		}
	}

}
