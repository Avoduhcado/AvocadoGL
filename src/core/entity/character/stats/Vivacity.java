package core.entity.character.stats;

import core.Theater;

public class Vivacity extends Attribute {

	private boolean active = false;
	private int activeCount;
	private float tallieTimer;
	private float activeTime;
	private float exhaustedTime;
	private float bufferTime;
	private float restoreRate = 2.0f;
	
	public Vivacity() {
		maxStat = 30f;
		stat = maxStat;
	}

	// TODO Deplete stat from dodges based on level of vivacity
	public void update() {
		super.update();
		
		// Check if stat should be regenerating
		if(!active && stat < maxStat) {
			// Only regenerate when not in buffer period
			if(bufferTime > 0) {
				bufferTime -= Theater.getDeltaSpeed(0.025f);
			} else {
				regen();
			}
			
			// Exhausted timer after for run cap
			if(exhaustedTime > 0f) {
				exhaustedTime -= Theater.getDeltaSpeed(0.025f);
			}
		} else {
			// Active while running
			if(active) {
				// Decrease stat and reset active marker
				stat -= Theater.getDeltaSpeed(0.025f);
				active = false;
				// Hitting run cap
				if(getPercentRemaining() <= 0.4f) {
					exhaustedTime = 2.0f;
				}
			}
			// Bottom out conditions
			if(stat <= 0.0f) {
				stat = 0f;
				// Set recovery timer
				deplete();
				active = false;
			}
		}
		
		// Building towards level up while under 30% and above 0
		if(getPercentRemaining() < 0.3f && stat != 0f) {
			activeTime += Theater.getDeltaSpeed(0.025f);
			System.out.println(activeTime + " Active time");
		} else if(activeTime > 0f) {
			// Increment counter
			activeCount++;
			// Penalty for bottoming out
			if(stat == 0f)
				activeTime = activeTime / 2f;
			// Add to tallie
			tallieTimer += activeTime;
			activeTime = 0f;
			System.out.println("Active count " + activeCount + " Tallie " + tallieTimer);
		}

		// Level up conditions
		if(tallieTimer >= maxStat * 2.5f) {
			// Increase max and reset counters
			maxStat += maxStat / (activeCount * 2.5f);
			tallieTimer = 0;
			activeCount = 0;
		}
	}
	
	public void regen() {
		stat += restoreRate * Theater.getDeltaSpeed(0.025f);
		if(stat > maxStat) {
			stat = maxStat;
		}
	}
	
	public void deplete() {
		bufferTime = 2.0f;
	}
	
	public void reduce() {
		active = true;
	}
	
	public boolean isExhausted() {
		if(exhaustedTime > 0f)
			return true;
		
		return false;
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getActiveCount() {
		return activeCount;
	}

	public void setActiveCount(int activeCount) {
		this.activeCount = activeCount;
	}

	public float getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(float activeTime) {
		this.activeTime = activeTime;
	}

	public float getBufferTime() {
		return bufferTime;
	}

	public void setBufferTime(float bufferTime) {
		this.bufferTime = bufferTime;
	}
	
	public void setRestoreRate(float restoreRate) {
		this.restoreRate = restoreRate;
	}

	@Override
	public String print() {
		String temp = "Vivacity: ";
		if(getPercentRemaining() > 1f) {
			temp += "Hyperactive";
		} else if(getPercentRemaining() <= 1f && getPercentRemaining() > 0.8f) {
			temp += "Ready";
		} else if(getPercentRemaining() <= 0.8f && getPercentRemaining() > 0.6f) {
			temp += "Energetic";
		} else if(getPercentRemaining() <= 0.6f && getPercentRemaining() > 0.4f) {
			temp += "Wearied";
		} else if(getPercentRemaining() <= 0.4f && getPercentRemaining() > 0.2f) {
			temp += "Slow";
		} else if(getPercentRemaining() <= 0.2f && getPercentRemaining() > 0f) {
			temp += "Wheezing";
		} else {
			temp += "Out Of Breath";
		}
		
		return temp;
	}

	@Override
	public void load(String[] temp) {
		String[] maxTemp = temp[1].split("-");
		maxStat = Float.parseFloat(maxTemp[0]);
		maxStatBuff = Float.parseFloat(maxTemp[1]);
		maxStatBuffTime = Float.parseFloat(maxTemp[2]);
		
		String[] statTemp = temp[2].split("-");
		stat = Float.parseFloat(statTemp[0]);
		statBuff = Float.parseFloat(statTemp[1]);
		statBuffTime = Float.parseFloat(statTemp[2]);
		
		String[] secondaryTemp = temp[3].split("-");
		activeCount = Integer.parseInt(secondaryTemp[0]);
		tallieTimer = Float.parseFloat(secondaryTemp[1]);
	}

	@Override
	public String save() {
		String save = "VIVACITY;";
		save += maxStat + "-" + maxStatBuff + "-" + maxStatBuffTime + ";";
		save += stat + "-" + statBuff + "-" + statBuffTime + ";";
		save += activeCount + "-" + tallieTimer;
		
		return save;
	}
	
}
