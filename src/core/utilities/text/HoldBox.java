package core.utilities.text;

import core.items.Item;
import core.scene.Stage;

public class HoldBox extends SelectBox {

	private Item item;
	
	public HoldBox(boolean hasTitle, String message, Item item) {
		super(hasTitle);
		buildText(message + " Hold Item?;Yes;No");
		this.item = item;
	}
	
	public void update(Stage stage) {
		if(source != null) {
			setCenterPosition(source.getX(), source.getY());
		}
	}
	
	public Item getItem() {
		return item;
	}

}
