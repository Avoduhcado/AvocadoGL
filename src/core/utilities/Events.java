package core.utilities;

import core.Theater;
import core.entity.Actor;
import core.entity.Prop;
import core.items.Item;
import core.scene.Stage;

public class Events {

	private Stage stage;
	private int inputHolder = 0;
	
	private static Events events = new Events();
	
	public static Events get() {
		return events;
	}
	
	public Events() {
		this.stage = Theater.get().getStage();
	}
	
	public void updateEvents(Stage stage) {
	}
	
	public boolean canRemoveMoney(int value) {
		if(stage.player.getEquipment().getGold() >= value) {
			return true;
		}
		return false;
	}
	
	public void removeMoney(int value) {
		stage.player.getEquipment().setGold(stage.player.getEquipment().getGold() - value);		
	}
	
	public void addMoney(int value) {
		stage.player.getEquipment().setGold(stage.player.getEquipment().getGold() - value);
	}
	
	public int getMoney() {
		return stage.player.getEquipment().getGold();
	}
	
	public void changeScene(String dest, int x, int y) {
		stage.changeStage(dest, x, y);
	}
	
	public void reviveToPrevious() {
		stage.player.revive();
		stage.changeStage(stage.paths.get(0).getDestination(), stage.paths.get(0).getDestX(), stage.paths.get(0).getDestY());
	}
	
	public void raiseDeathDebt() {
		stage.player.setDeathDebt(stage.player.getDeathDebt() + 1000000L);
	}
	
	public void reduceDeathDebt(int value) {
		stage.player.setDeathDebt(stage.player.getDeathDebt() - value);
	}
	
	public String getDeathDebt() {
		return stage.player.getDeathDebt() + " Bone Splinters";
	}

	public int getInputHolder() {
		return inputHolder;
	}

	public void setInputHolder(int inputHolder) {
		this.inputHolder = inputHolder;
	}
	
	public Prop getProp(String ID) {
		for(int x = 0; x<stage.props.size(); x++) {
			if(ID.matches(stage.props.get(x).getID()))
				return stage.props.get(x);
		}
		
		return null;
	}
	
	public Actor getActor(String ID) {
		for(int x = 0; x<stage.cast.size(); x++) {
			if(ID.matches(stage.cast.get(x).getID()))
				return stage.cast.get(x);
		}
		
		return null;
	}
	
	public void removeEntity(String ID) {
		for(int x = 0; x<stage.cast.size(); x++) {
			if(ID.matches(stage.cast.get(x).getID()))
				stage.remove(stage.cast.get(x));
		}
		for(int x = 0; x<stage.props.size(); x++) {
			if(ID.matches(stage.props.get(x).getID()))
				stage.remove(stage.props.get(x));
		}
	}

	public void holdItem(Item item) {
		stage.player.getEquipment().getWeapons().setGear(stage.player.getEquipment().getWeapons().getGear(0).getID() == 0 ? 0 : 1, item);
	}
	
}
