package core.utilities.menu;

import java.util.ArrayList;

import core.entity.character.Party;
import core.keyboard.Keybinds;
import core.scene.Stage;
import core.utilities.text.PlainText;

public class PartyMenu extends MenuPage {

	private Party party;
	private int selectedMember = 0;
	private ArrayList<PlainText> members = new ArrayList<PlainText>();
	private PlainText memberStats;
	
	public PartyMenu(Party party) {
		this.party = party;
		if(party == null || party.getMembers().isEmpty()) {
			PlainText temp = new PlainText(false, 0f, false);
			temp.buildText("Your party is empty.");
			temp.setStill(true);
			temp.setPositionOverTime(0 - temp.getFullWidth(), 15, 0 - temp.getFullHeight(), 15, 0.1f);	
		} else {
			for(int x = 0; x<party.getMembers().size(); x++) {
				PlainText temp = new PlainText(false, 0f, false);
				temp.setStill(true);
				temp.buildText(party.getMember(x).getName() + ";" + party.getMember(x).getStats().printSimpleStats());
				if(x > 0) {
					temp.setPositionOverTime(0 - temp.getFullWidth(), 15, 15 + (x * members.get(x - 1).getFullHeight()), 15 + (x * members.get(x - 1).getFullHeight()), 0.1f);
				} else {
					temp.setPositionOverTime(0 - temp.getFullWidth(), 15, 15, 15, 0.1f);
				}
				members.add(temp);
			}
		}
	}
	
	@Override
	public void update(Stage stage) {
		for(int x = 0; x<members.size(); x++)
			members.get(x).move();
		
		if(members.contains(focus)) {
			if(memberStats != null)
				memberStats.move();
			
			if(Keybinds.MENU_DOWN.clicked()) {
				resetPreviousFocus();
				if(selectedMember + 1 < party.getMembers().size())
					selectedMember++;
				else
					selectedMember = 0;
				cycleFocus();
			} else if(Keybinds.MENU_UP.clicked()) {
				resetPreviousFocus();
				if(selectedMember > 0)
					selectedMember--;
				else
					selectedMember = party.getMembers().size() - 1;
				cycleFocus();
			}
			
			if(Keybinds.CANCEL.clicked()) {
				resetPreviousFocus();
				focus = null;
				memberStats = null;
				selectedMember = 0;
			}
		} else {
			if(Keybinds.CANCEL.clicked()) {
				focus = null;
			}
		}
	}
	
	public void movePages() {
		for(int x = 0; x<members.size(); x++)
			members.get(x).move();
	}
	
	@Override
	public void draw() {
		for(int x = 0; x<members.size(); x++) {
			members.get(x).draw();
		}
		if(memberStats != null) {
			memberStats.draw();
		}
	}
	
	@Override
	public void focus() {
		cycleFocus();
	}
	
	@Override
	public void refresh() {
		for(int x = 0; x<members.size(); x++) {
			if(x == selectedMember && memberStats != null) {
				members.get(x).clear();
				members.get(x).buildText(party.getMember(x).getName());
			} else {
				members.get(x).clear();
				members.get(x).buildText(party.getMember(x).getName() + ";" + party.getMember(x).getStats().printSimpleStats());
			}
		}
		
		if(memberStats != null) {
			memberStats.clear();
			memberStats.buildText(party.getMember(selectedMember).getStats().print());
		}
	}
	
	@Override
	public void close() {
		for(int x = 0; x<party.getMembers().size(); x++) {
			members.get(x).setPositionOverTime(members.get(x).getX(), 0 - members.get(x).getFullWidth(), members.get(x).getY(), members.get(x).getY(), 0.15f);
		}
	}
	
	public void resetPreviousFocus() {
		focus.setPositionOverTime(50, 15, focus.getY(), focus.getY(), 0.1f);
		focus.clear();
		focus.buildText(party.getMember(selectedMember).getName() + ";" + party.getMember(selectedMember).getStats().printSimpleStats());
		
		if(selectedMember < party.getMembers().size()) {	// Reset all following party members
			for(int x = selectedMember + 1; x<party.getMembers().size(); x++) {
				members.get(x).setPosition(members.get(x).getX(), focus.getY() + focus.getFullHeight());
				members.get(x).setPositionOverTime(members.get(x).getX(), 15, members.get(x).getY(), members.get(x).getY(), 0.1f);
			}
		}
	}
	
	public void cycleFocus() {
		members.get(selectedMember).setPositionOverTime(15, 50, members.get(selectedMember).getY(), members.get(selectedMember).getY(), 0.1f);
		members.get(selectedMember).clear();
		members.get(selectedMember).buildText(party.getMember(selectedMember).getName());
		focus = members.get(selectedMember);
		
		memberStats = new PlainText(false, 0f, false);
		memberStats.setStill(true);
		memberStats.buildText(party.getMember(selectedMember).getStats().print());
		memberStats.setPositionOverTime(0 - memberStats.getFullWidth(), 75, focus.getY() + focus.getFullHeight(), focus.getY() + focus.getFullHeight(), 0.1f);
		
		if(selectedMember < party.getMembers().size()) {	// Adjust all following party members
			for(int x = selectedMember + 1; x<party.getMembers().size(); x++) {
				members.get(x).setPositionOverTime(15, 15, members.get(x).getY(), memberStats.getEventualY() + memberStats.getFullHeight(), 0.1f);
			}
		}
	}
	
}
