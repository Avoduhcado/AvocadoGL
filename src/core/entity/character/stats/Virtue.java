package core.entity.character.stats;

public class Virtue extends Attribute {
	
	private float average = 0f;
	private float tallie = 0f;
	private float threshold;
	
	public Virtue() {
		maxStat = 50f;
		stat = maxStat;
		threshold = (float) Math.log(maxStat);
	}
	
	public void update() {
		super.update();
	}
	
	public void expelEssence(float cost) {
		// Reduce current mana by cost
		stat -= cost;
		if(stat < 0f)
			stat = 0f;
		
		// Check for increasing level up counters
		if(cost > maxStat / threshold) {
			// Current average damage taken exceeding threshold of max HP
			average = (average + (cost - (maxStat / threshold))) / 2f;
			// Current total damage taken exceeding threshold of max HP
			tallie += cost - (maxStat / threshold);
			System.out.println("Virtue " + average + " " + tallie);
		}
		
		// Check for level up conditions
		if(tallie >= maxStat * Math.log(maxStat)) {
			// Apply new max HP and reset counters
			maxStat = maxStat * average;
			threshold = (float) Math.log(maxStat);
			average = 0f;
			tallie = 0f;
		}
	}
	
	@Override
	public String print() {
		String temp = "Virtue: ";
		if(getPercentRemaining() > 1f) {
			temp += "Burning";
		} else if(getPercentRemaining() <= 1f && getPercentRemaining() > 0.8f) {
			temp += "Flooded";
		} else if(getPercentRemaining() <= 0.8f && getPercentRemaining() > 0.6f) {
			temp += "Enlightened";
		} else if(getPercentRemaining() <= 0.6f && getPercentRemaining() > 0.4f) {
			temp += "Stable";
		} else if(getPercentRemaining() <= 0.4f && getPercentRemaining() > 0.2f) {
			temp += "Waning";
		} else if(getPercentRemaining() <= 0.2f && getPercentRemaining() > 0f) {
			temp += "Normal";
		} else {
			temp += "OOM";
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
		average = Float.parseFloat(secondaryTemp[0]);
		tallie = Float.parseFloat(secondaryTemp[1]);
	}

	@Override
	public String save() {
		String save = "VIRTUE;";
		save += maxStat + "-" + maxStatBuff + "-" + maxStatBuffTime + ";";
		save += stat + "-" + statBuff + "-" + statBuffTime + ";";
		save += average + "-" + tallie;
		
		return save;
	}

}
