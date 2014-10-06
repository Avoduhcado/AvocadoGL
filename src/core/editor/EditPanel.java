package core.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.JPanel;

import core.editor.entities.sprites.EditSprite;
import core.editor.map.MapGridArray;

public class EditPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EditSprite backdrop;
	public EditSprite player;
	public String backgroundMusic = "";
	public ArrayList<String> pathStrings = new ArrayList<String>();
	public ArrayList<Rectangle> hitmap = new ArrayList<Rectangle>();
	public ArrayList<MapGridArray> map = new ArrayList<MapGridArray>();
	public ArrayList<EditSprite> props = new ArrayList<EditSprite>();
	public ArrayList<EditSprite> actors = new ArrayList<EditSprite>();
	public ArrayList<EditSprite> paths = new ArrayList<EditSprite>();
	private Dimension size = new Dimension(800, 600);
	public EditSprite selection;
	
	/**
	 * Create the panel.
	 */
	public EditPanel(Dimension size) {
		setBackground(Color.MAGENTA);
		setAutoscrolls(true);
		setPreferredSize(this.size);
		if(size != null) {
			this.size = size;
			setPreferredSize(size);
		}
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.MAGENTA);
		g2.fillRect(0, 0, size.width, size.height);
		
		if(backdrop != null) {
			backdrop.draw(g2, null);
		}
		if(!map.isEmpty()) {
			for(int x = 0; x<map.size(); x++)
				map.get(x).draw(g2);
		}
		if(!props.isEmpty()) {
			for(int x = 0; x<props.size(); x++)
				props.get(x).draw(g2, null);
		}
		if(!actors.isEmpty()) {
			for(int x = 0; x<actors.size(); x++)
				actors.get(x).draw(g2, null);
		}
		if(player != null) {
			g2.setColor(Color.BLUE);
			g2.fillRect(player.getX(), player.getY(), player.getSprite().getWidth(), player.getSprite().getHeight());
		}
		if(!paths.isEmpty()) {
			for(int x = 0; x<paths.size(); x++) {
				g2.setColor(Color.RED);
				g2.drawRect(paths.get(x).getX(), paths.get(x).getY(), paths.get(x).getSprite().getWidth(), paths.get(x).getSprite().getHeight());
			}
		}
		if(selection != null) {
			g2.setColor(Color.GREEN);
			g2.drawRect(selection.getX(), selection.getY(), selection.getSprite().getWidth(), selection.getSprite().getHeight());
		}
		g2.setColor(Color.MAGENTA);
		g2.drawRect(0, 0, size.width, size.height);
	}
	
	public void clearScene() {
		backdrop = null;
		backgroundMusic = "";
		hitmap = new ArrayList<Rectangle>();
		map = new ArrayList<MapGridArray>();
		props = new ArrayList<EditSprite>();
		player = null;
		actors = new ArrayList<EditSprite>();
		paths = new ArrayList<EditSprite>();
		pathStrings = new ArrayList<String>();
		selection = null;
	}
	
	public void setSelection(String select) {
		if(backdrop != null && backdrop.toString().matches(select)) {
    		selection = backdrop;
    	} if(!props.isEmpty()) {
        	for(int x = 0; x<props.size(); x++) {
            	if(props.get(x).toString().matches(select)) {
            		selection = props.get(x);
            	}
        	}
    	} if(!actors.isEmpty()) {
        	for(int x = 0; x<actors.size(); x++) {
            	if(actors.get(x).toString().matches(select)) {
            		selection = actors.get(x);
            	}
        	}
    	} if(!paths.isEmpty()) {
    		for(int x = 0; x<paths.size(); x++) {
    			if(paths.get(x).toString().matches(select)) {
    				selection = paths.get(x);
    			}
    		}
    	} if(player != null && player.toString().matches(select)) {
    		selection = player;
    	}
	}
	
	public void setSelection(Point e) {
		if(backdrop != null && backdrop.contains(e)) {
			selection = backdrop;
		}
		for(int x = 0; x<props.size(); x++) {
			if(props.get(x) != null && props.get(x).contains(e)) {
				selection = props.get(x);
				break;
			}
		}
		for(int x = 0; x<actors.size(); x++) {
			if(actors.get(x) != null && actors.get(x).contains(e)) {
				selection = actors.get(x);
				break;
			}
		} if(!paths.isEmpty()) {
    		for(int x = 0; x<paths.size(); x++) {
    			if(paths.get(x) != null && paths.get(x).contains(e)) {
    				selection = paths.get(x);
    			}
    		}
    	} if(player != null && player.contains(e)) {
    		selection = player;
    	}
	}
	
	public void removeSelection(String select) {
		if(backdrop != null && backdrop.toString().matches(select)) {
    		backdrop = null;
		} if(!hitmap.isEmpty() && select.matches("Hitmap")) {
			hitmap.clear();
		} if(!backgroundMusic.matches("") && select.matches("BGM-" + backgroundMusic)) {
			backgroundMusic = "";
		} if(!map.isEmpty() && select.matches("Map")) {
			map.clear();
    	} if(!props.isEmpty()) {
        	for(int x = 0; x<props.size(); x++) {
            	if(props.get(x).toString().matches(select)) {
            		props.remove(x);
            	}
        	}
    	} if(!actors.isEmpty()) {
        	for(int x = 0; x<actors.size(); x++) {
            	if(actors.get(x).toString().matches(select)) {
            		actors.remove(x);
            	}
        	}
    	} if(!paths.isEmpty()) {
    		for(int x = 0; x<paths.size(); x++) {
    			if(paths.get(x).toString().matches(select)) {
    				paths.remove(x);
    				pathStrings.remove(x);
    			}
    		}
    	} if(player != null) {
    		if(player.toString().matches(select)) {
    			player = null;
    		}
    	}
    	selection = null;
	}
	
	public void updateAttributes(String select, String attribs) {
		if(backdrop != null && backdrop.toString().matches(select)) {
    		backdrop.setAttributes(attribs);
    	} if(!props.isEmpty()) {
        	for(int x = 0; x<props.size(); x++) {
            	if(props.get(x).toString().matches(select)) {
            		props.get(x).setAttributes(attribs);
            	}
        	}
    	} if(!actors.isEmpty()) {
        	for(int x = 0; x<actors.size(); x++) {
            	if(actors.get(x).toString().matches(select)) {
            		actors.get(x).setAttributes(attribs);
            	}
        	}
    	} if(player != null) {
    		if(player.toString().matches(select)) {
    			player.setAttributes(attribs);
    		}
    	}
	}
	
}
