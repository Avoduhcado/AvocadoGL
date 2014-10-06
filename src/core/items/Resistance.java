package core.items;

public class Resistance {

	private float crush;
	private float slash;
	private float thrust;
	private float magic;
	private float head;
	private float body;
	private float cripple;

	public Resistance(String resist) {
		if(resist != null) {
			String[] temp = resist.split(":");
			this.crush = Float.parseFloat(temp[0]);
			this.slash = Float.parseFloat(temp[1]);
			this.thrust = Float.parseFloat(temp[2]);
			this.magic = Float.parseFloat(temp[3]);
			this.head = Float.parseFloat(temp[4]);
			this.body = Float.parseFloat(temp[5]);
			this.cripple = Float.parseFloat(temp[6]);
		}
	}

	public float getCrush() {
		return crush;
	}

	public void setCrush(float crush) {
		this.crush = crush;
	}
	
	public float getSlash() {
		return slash;
	}

	public void setSlash(float slash) {
		this.slash = slash;
	}

	public float getThrust() {
		return thrust;
	}

	public void setThrust(float thrust) {
		this.thrust = thrust;
	}

	public float getMagic() {
		return magic;
	}

	public void setMagic(float magic) {
		this.magic = magic;
	}

	public float getHead() {
		return head;
	}

	public void setHead(float head) {
		this.head = head;
	}

	public float getBody() {
		return body;
	}

	public void setBody(float body) {
		this.body = body;
	}

	public float getCripple() {
		return cripple;
	}

	public void setCripple(float cripple) {
		this.cripple = cripple;
	}
	
}
