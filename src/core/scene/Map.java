package core.scene;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import core.render.Tile;

public class Map {
	
	// TODO Initialize a blank map?

	private ArrayList<Tile> mapTiles = new ArrayList<Tile>();
	private boolean town;
	
	public Map(ArrayList<Tile> mapTiles, boolean town) {
		this.mapTiles = mapTiles;
		this.town = town;
	}
	
	public static Map loadMap(BufferedReader reader) {
		try {
			String line;
			String tileName;
			Point start = new Point();
			boolean auto;
			ArrayList<Tile> tiles = new ArrayList<Tile>();
			line = reader.readLine();
			Boolean tempTown = Boolean.parseBoolean(line);
			while(!(line = reader.readLine()).matches("<END>")) {
				if(line.matches("<TILE>")) {
					String[] temp = reader.readLine().split(";");
					tileName = temp[0];
					start = new Point(Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
					auto = Boolean.parseBoolean(temp[3]);
					Tile tempTile = new Tile(tileName, 0, 0, auto);
					while(!(line = reader.readLine()).matches("<BREAK>")) {
						temp = line.split(";");
						tiles.add(new Tile(tileName, start.x + (Integer.parseInt(temp[0]) * tempTile.getWidth()), start.y + (Integer.parseInt(temp[1]) * tempTile.getHeight()), auto));
						if(auto) {
							tiles.get(tiles.size() - 1).setCorners(new Point[]{new Point(Integer.parseInt(temp[2]), Integer.parseInt(temp[3])), 
								new Point(Integer.parseInt(temp[4]), Integer.parseInt(temp[5])), new Point(Integer.parseInt(temp[6]), Integer.parseInt(temp[7])),
								new Point(Integer.parseInt(temp[8]), Integer.parseInt(temp[9]))});
						}
					}
				}
			}
			
			return new Map(tiles, tempTown);
		} catch (IOException e) {
			System.err.println("The scene file has been misplaced!");
	    	e.printStackTrace();
		}
		
		System.out.println("Error loading map");
		return null;
	}
	
	public void clear() {
		mapTiles.clear();
	}
	
	public void draw() {
		for(int x = 0; x<mapTiles.size(); x++) {
			mapTiles.get(x).draw();
		}
	}
	
	public void animate() {
		for(int x = 0; x<mapTiles.size(); x++) {
			mapTiles.get(x).animate();
		}
	}
	
	public boolean isTown() {
		return town;
	}
	
}
