package core.utilities.text;

import java.util.ArrayList;

import org.newdawn.slick.Color;

public class TextLine {

	private ArrayList<Text> line = new ArrayList<Text>();
	
	public TextLine(String line) {
		for(int x = 0; x<line.split("<").length; x++) {
			if(!line.split("<")[x].matches("")) {
				this.line.add(new Text(line.split("<")[x]));
			}
		}		
	}
	
	public void draw(float x, float y) {
		float offset = 0;
		for(int i = 0; i<line.size(); i++) {
			if(i > 0) {
				offset += line.get(i - 1).getFont().getWidth(line.get(i - 1).getText());
				if(line.get(i - 1).getIcon() != null) {
					offset += line.get(i - 1).getIcon().getWidth();
				}
			}
			line.get(i).getFont().drawString(x + offset, y, line.get(i).getText(), line.get(i).getColor());
			if(line.get(i).getIcon() != null) {
				line.get(i).getIcon().draw(x + offset + line.get(i).getFont().getWidth(line.get(i).getText()), y);
			}
		}
	}
	
	public void drawShadow(float x, float y) {
		float offset = 0;
		for(int i = 0; i<line.size(); i++) {
			if(i > 0) {
				offset += line.get(i - 1).getFont().getWidth(line.get(i - 1).getText());
				if(line.get(i - 1).getIcon() != null) {
					offset += line.get(i - 1).getIcon().getWidth();
				}
			}
			line.get(i).getFont().drawString(x + offset, y, line.get(i).getText(), Color.black);
		}
	}
	
	public String getLine() {
		String temp = "";
		for(int x = 0; x<line.size(); x++) 
			temp += line.get(x).getText();
		
		return temp;
	}
	
	public int getWidth() {
		int width = Text.defaultUnifont.getWidth(getLine());
		for(int x = 0; x<line.size(); x++) {
			if(line.get(x).getIcon() != null)
				width += line.get(x).getIcon().getWidth();
		}
		
		return width;
	}
	
	public int getHeight() {
		int height = Text.defaultUnifont.getHeight(getLine());
		for(int x = 0; x<line.size(); x++) {
			if(line.get(x).getIcon() != null)
				if(line.get(x).getIcon().getHeight() - 15 > height)
					height = line.get(x).getIcon().getHeight() - 15;
		}
		
		return height;
	}
	
}
