package core.entity.character;

import java.util.ArrayList;

import core.entity.Actor;
import core.entity.Prop;
import core.scene.Stage;
import core.utilities.pathfinding.PathQuadStar;
import core.utilities.text.PlainText;
import core.utilities.text.Textbox;

public class Party {

	private int maxPartySize = 4;
	private ArrayList<Actor> members = new ArrayList<Actor>();
	
	public Party() {
	}
	
	public Party(Actor player) {
		if(!members.contains(player)) {
			members.add(player);
			player.setInParty(1);
		}
	}
	
	public Party(ArrayList<Actor> members) {
		for(int x = 0; x<maxPartySize; x++) {
			if(!this.members.contains(members.get(x))) {
				this.members.add(members.get(x));
				members.get(x).setInParty(1);
			}
		}
	}
	
	public void regroup(Prop foreground) {
		for(int x = 1; x<members.size(); x++) {
			int tempX = (int)((Math.random()*200)-100) + (int)members.get(0).getX();
			int tempY = (int)((Math.random()*200)-100) + (int)members.get(0).getY();
			members.get(x).setCourse(PathQuadStar.get().findPath(members.get(x), tempX, tempY));
		}
	}
	
	public void removeMember(Actor member) {
		if(members.contains(member))
			members.remove(member);
	}
	
	public void addMember(Actor member, Stage stage) {
		if(members.size() < maxPartySize && !members.contains(member)) {
			members.add(member);
			member.setInParty(1);
		} else if(members.size() >= maxPartySize) {
			Textbox text = new PlainText(false, 3f, false);
			text.setSource(members.get(0));
			text.buildText("Party is full!");
			stage.addText(text);
		}
	}
	
	public ArrayList<Actor> getMembers() {
		return members;
	}
	
	public Actor getMember(int x) {
		return members.get(x);
	}
	
	public Actor getMember(Actor member) {
		for(int x = 0; x<members.size(); x++) {
			if(members.get(x) == member) {
				return members.get(x);
			}
		}
		
		return null;
	}
}
