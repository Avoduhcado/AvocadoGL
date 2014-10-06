package core.entity.character.stats;

import core.Theater;

public abstract class Attribute {

	protected float maxStat;
	protected float maxStatBuff;
	protected float maxStatBuffTime;
	protected float stat;
	protected float statBuff;
	protected float statBuffTime;
	
	/**
	 * Reduce counter on buffered stat.
	 */
	public void update() {
		if(maxStatBuffTime > 0.0f) {
			maxStatBuffTime -= Theater.getDeltaSpeed(0.025f);
		}
		if(statBuffTime > 0.0f) {
			statBuffTime -= Theater.getDeltaSpeed(0.025f);
		}
	}
	
	/**
	 * Restore stat to max.
	 */
	public void restore() {
		stat = maxStat;
	}
	
	/**
	 * Buff the max of this stat.
	 * @param buff The amount to be buffered.
	 * @param duration The duration to stay buffered. Set to 0 for one off boosts.
	 */
	public void buffMax(float buff, float duration) {
		if(duration != 0) {
			maxStatBuff = maxStat + buff;
			maxStatBuffTime = duration;
		} else {
			maxStat += buff;
		}
	}
	
	/**
	 * Buff the current stat.
	 * @param buff The amount to be buffered.
	 * @param duration The duration to stay buffered. Set to 0 for one off boosts.
	 */
	public void buffStat(float buff, float duration) {
		if(duration != 0) {
			statBuff = stat + buff;
			statBuffTime = duration;
		} else {
			stat += buff;
		}
	}
	
	/**
	 * @return Percentage of stat remaining.
	 */
	public float getPercentRemaining() {
		return stat / maxStat;
	}
	
	public float getMax() {
		if(maxStatBuffTime > 0.0f)
			return maxStatBuff;

		return maxStat;
	}
	
	public void setMax(float max) {
		this.maxStat = max;
		this.maxStatBuff = max;
	}
	
	public float getStat() {
		if(statBuffTime > 0.0f)
			return statBuff;
			
		return stat;
	}
	
	public void setStat(float stat) {
		this.stat = stat;
		this.statBuff = stat;
	}
	
	public abstract String print();
	public abstract void load(String[] temp);
	public abstract String save();
	
}
