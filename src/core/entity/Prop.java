package core.entity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import core.entity.Entity;
import core.equipment.ItemContainer;
import core.render.Shadow;
import core.scene.Stage;
import core.utilities.actions.Action;
import core.utilities.text.LootWindow;

public class Prop extends Entity {

	private ItemContainer container;
	private boolean unique;
	
	public Prop(String ref, float x, float y, boolean solid) {
		super(ref, x, y, solid);
	}
	
	public static Prop loadProp(String ID, float x, float y) {
		BufferedReader reader;
		
		try {
	    	reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/database/entities"));

	    	String line;
	    	while((line = reader.readLine()) != null) {
	    		final String[] temp = line.split(";");
	    		
	    		if(temp[0].matches(ID)) {
	    			Prop prop = new Prop(temp[0], x, y, Boolean.parseBoolean(temp[1])) {{
		    			resizeBox((int)(sprite.getWidth()*Float.parseFloat(temp[2])), 
		    					(int)(sprite.getHeight()*Float.parseFloat(temp[3])),
			    				Float.parseFloat(temp[4]), Float.parseFloat(temp[5]));
		    			sprite.setShadow(new Shadow());
	    			}};
	    			
	    			reader.close();
	    			return prop;
	    		}
	    	}

	    	reader.close();
	    } catch (FileNotFoundException e) {
	    	System.out.println("The prop database has been misplaced!");
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	System.out.println("Prop database failed to load!");
	    	e.printStackTrace();
	    }
		return new Prop(null, 0, 0, false);
	}
	
	public static Prop loadProp(BufferedReader reader) {
		try {

	    	String line = reader.readLine();
    		String[] start = line.split(";");
			Prop prop = Prop.loadProp(start[0], Float.parseFloat(start[1]), Float.parseFloat(start[2]));
			if(start.length > 3)
				prop.setID(start[3]);
			if(start.length > 4) {
				prop.setFlat(Boolean.parseBoolean(start[4]));
				if(prop.isFlat())
					prop.sprite.shadow = null;
			}
			
			while((line = reader.readLine()) != null && !line.matches("<END>")) {
				if(line.matches("<DIALOGUE>")) {
					String switches = reader.readLine();
					ArrayList<String> temp = new ArrayList<String>();
					while((line = reader.readLine()) != null && !line.matches("<BREAK>")) {
						temp.add(line);
					}
					prop.setAction(new Action(switches, temp.toArray(new String[temp.size()])));
					prop.setUnique(true);
				} else if(line.matches("<CONTAINER>")) {
					prop.container = new ItemContainer(null);
					while((line = reader.readLine()) != null && !line.matches("<BREAK>")) {
						prop.container.fillItems(line.split(";"));
					}
					prop.setUnique(true);
				}
			}
			
			return prop;
	    } catch (FileNotFoundException e) {
	    	System.out.println("The prop database has been misplaced!");
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	System.out.println("Prop database failed to load!");
	    	e.printStackTrace();
	    }
		
		return new Prop(null, 0, 0, false);
	}
	
	public void buildLootWindow(Stage stage) {
		stage.script.addText(new LootWindow(this));
	}
	
	public ItemContainer getContainer() {
		return container;
	}
	
	public void setContainer(ItemContainer container) {
		this.container = container;
		this.setUnique(true);
	}
	
	public boolean isUnique() {
		return unique;
	}
	
	public void setUnique(boolean unique) {
		this.unique = unique;
	}
	
}
