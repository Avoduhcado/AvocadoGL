package core.entity.character.stats;

public class Vitality extends Attribute {
		
	private float average = 0f;
	private float tallie = 0f;
	private float threshold;
	
	public Vitality() {
		maxStat = 15f;
		stat = maxStat;
		threshold = (float) Math.log(maxStat);
	}
	
	public void update() {
		super.update();
	}
	
	public void takeDamage(float damage) {
		// Subtract damage from current health
		stat -= damage;
		
		System.out.println("Vit damage " + damage + " " + threshold);
		// Check for increasing level up counters
		if(damage > maxStat / threshold) {
			// Current average damage taken exceeding threshold of max HP
			average = (average + (damage - (maxStat / threshold))) / 2f;
			// Current total damage taken exceeding threshold of max HP
			tallie += damage - (maxStat / threshold);
			System.out.println("Vitality " + average + " " + tallie);
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
		String temp = "Vitality: ";
		if(getPercentRemaining() > 1f) {
			temp += "<c#78FF00>Rosy-Cheeked";
		} else if(getPercentRemaining() <= 1f && getPercentRemaining() > 0.8f) {
			temp += "<c#00FF00>Just Peachy";
		} else if(getPercentRemaining() <= 0.8f && getPercentRemaining() > 0.6f) {
			temp += "<c#4BC800>A Little Tired";
		} else if(getPercentRemaining() <= 0.6f && getPercentRemaining() > 0.4f) {
			temp += "<c#647800>Exhausted";
		} else if(getPercentRemaining() <= 0.4f && getPercentRemaining() > 0.2f) {
			temp += "<c#AF3200>Heavily Wounded";
		} else if(getPercentRemaining() <= 0.2f && getPercentRemaining() > 0f) {
			temp += "<cred>Dying";
		} else {
			temp += "<cblack>You Died";
		}
		
		return temp;
	}
	
	@Override
	public String save() {
		String save = "VITALITY;";
		save += maxStat + "-" + maxStatBuff + "-" + maxStatBuffTime + ";";
		save += stat + "-" + statBuff + "-" + statBuffTime + ";";
		save += average + "-" + tallie;
		
		return save;
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

}
