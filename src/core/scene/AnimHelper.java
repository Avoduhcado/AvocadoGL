package core.scene;

public class AnimHelper {

	private int idleFrames;
	private int walkFrames;
	private int runFrames;
	private int attackFrames;
	private int hurtFrames;
	
	public AnimHelper(int idle, int walk, int run, int attack, int hurt) {
		idleFrames = idle;
		walkFrames = walk;
		runFrames = run;
		attackFrames = attack;
		hurtFrames = hurt;
	}

	public int getIdleFrames() {
		return idleFrames;
	}

	public void setIdleFrames(int idleFrames) {
		this.idleFrames = idleFrames;
	}

	public int getWalkFrames() {
		return walkFrames;
	}

	public void setWalkFrames(int walkFrames) {
		this.walkFrames = walkFrames;
	}

	public int getRunFrames() {
		return runFrames;
	}

	public void setRunFrames(int runFrames) {
		this.runFrames = runFrames;
	}

	public int getAttackFrames() {
		return attackFrames;
	}

	public void setAttackFrames(int attackFrames) {
		this.attackFrames = attackFrames;
	}

	public int getHurtFrames() {
		return hurtFrames;
	}

	public void setHurtFrames(int hurtFrames) {
		this.hurtFrames = hurtFrames;
	}
	
}
