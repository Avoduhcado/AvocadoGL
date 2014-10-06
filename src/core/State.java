package core;

public enum State {

	NORMAL (0),
	COMBAT (1),
	MENU (2);
	
	private int type;
	
	State(int type) {
		this.setType(type);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
