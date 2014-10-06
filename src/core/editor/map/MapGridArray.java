package core.editor.map;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import core.utilities.loader.ImageLoader;

public class MapGridArray implements Cloneable {

	private int startX;
	private int startY;
	private int tileWidth;
	private int tileHeight;
	private ArrayList<Point> coordinates = new ArrayList<Point>();
	private String tileName;
	private BufferedImage tile;
	private boolean auto = false;
	private HashMap<Point, MapAutotile> autotiles = new HashMap<Point, MapAutotile>();
	
	public MapGridArray(int x, int y, String tileName, boolean auto) {
		startX = x;
		startY = y;
		this.tileName = tileName;
		this.auto = auto;
		setTile(tileName);
		tileWidth = tile.getWidth();
		tileHeight = tile.getHeight();
		coordinates.add(new Point(0,0));
		if(this.auto) {
			autotiles.put(new Point(0, 0), new MapAutotile(tile));
			tileWidth = autotiles.get(new Point(0,0)).getDisplayTile().getWidth();
			tileHeight = autotiles.get(new Point(0,0)).getDisplayTile().getHeight();
		}
	}

	public MapGridArray(int startX, int startY, int x, int y, String tileName, boolean auto) {
		this.startX = startX;
		this.startY = startY;
		this.tileName = tileName;
		this.auto = auto;
		setTile(tileName);
		tileWidth = tile.getWidth();
		tileHeight = tile.getHeight();
		Point first = new Point((int)Math.floor((x - startX) / tileWidth), (int)Math.floor((y - startY) / tileHeight));
		coordinates.add(first);
		if(this.auto) {
			autotiles.put(first, new MapAutotile(tile));
			tileWidth = autotiles.get(first).getDisplayTile().getWidth();
			tileHeight = autotiles.get(first).getDisplayTile().getHeight();
		}
	}
	
	public MapGridArray copy(int coordX, String tileName, boolean auto) {
		MapGridArray temp = new MapGridArray(this.getX(), this.getY(), tileName, auto);
		temp.startX = temp.startX + (this.coordinates.get(coordX).x * temp.tileWidth);
		temp.startY = temp.startY + (this.coordinates.get(coordX).y * temp.tileHeight);
		
		return temp;
	}
	
	@SuppressWarnings("unchecked")
	public MapGridArray makeClone() {
		try {
			MapGridArray temp = (MapGridArray) super.clone();
			temp.coordinates = (ArrayList<Point>) this.coordinates.clone();
			temp.autotiles = (HashMap<Point, MapAutotile>) this.autotiles.clone();
			return temp;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void addCoordinate(int x, int y) {
		if(!coordinates.contains(new Point(x, y))) {
			coordinates.add(new Point(x, y));
			if(this.auto) {
				autotiles.put(new Point(x, y), new MapAutotile(tile));
				buildNeighbors(x, y);
			}
		}
	}
	
	public void addPoint(int x, int y) {
		Rectangle temp = new Rectangle(startX, startY, tileWidth, tileHeight);
		int pX = 0;
		Point closest = new Point((int)temp.getCenterX(), (int)temp.getCenterY());
		double dist = closest.distance(x, y);
		int pY = 0;
		boolean uncharted = true;
		for(int a = 0; a<coordinates.size(); a++) {
			temp = new Rectangle(startX + (coordinates.get(a).x * tileWidth), startY + (coordinates.get(a).y * tileHeight), tileWidth, tileHeight);
			if(temp.contains(x, y)) {
				uncharted = false;
			} else {
				closest = new Point((int)temp.getCenterX(), (int)temp.getCenterY());
				if(dist >= closest.distance(x, y)) {
					dist = closest.distance(x, y);
					if(x < temp.x) {
						pX = coordinates.get(a).x - 1;
					} else if(x > temp.getMaxX()) {
						pX = coordinates.get(a).x + 1;
					}
					if(y < temp.y) {
						pY = coordinates.get(a).y - 1;
					} else if(y > temp.getMaxY()) {
						pY = coordinates.get(a).y + 1;
					}
				}
			}
		}
		if(uncharted && !coordinates.contains(new Point(pX, pY))) {
			coordinates.add(new Point(pX, pY));
			if(auto) {
				autotiles.put(new Point(pX, pY), new MapAutotile(tile));
				buildNeighbors(pX, pY);
			}
		}
	}
	
	public boolean contains(int x, int y) {
		Rectangle temp = new Rectangle(startX, startY, tileWidth, tileHeight);
		for(int a = 0; a<coordinates.size(); a++) {
			temp = new Rectangle(startX + (coordinates.get(a).x * tileWidth), startY + (coordinates.get(a).y * tileHeight), tileWidth, tileHeight);
			if(temp.contains(x, y)) {
				return true;
			}
		}
		
		return false;
	}
	
	public void configureLine(int i, int j) {
		coordinates.clear();
		coordinates.add(new Point(0,0));
		if(auto) {
			autotiles.clear();
			autotiles.put(new Point(0, 0), new MapAutotile(tile));
		}
		double xSlope;
		double ySlope;
		if((i - startX) / (double)tileWidth < 0)
			xSlope = Math.floor((i - startX) / (double)tileWidth);
		else
			xSlope = Math.ceil((i - startX) / (double)tileWidth);
		if((j - startY) / (double)tileHeight < 0)
			ySlope = Math.floor((j - startY) / (double)tileHeight);
		else
			ySlope = Math.ceil((j - startY) / (double)tileHeight);
		if(!(startX <= i && startX + tileWidth >= i ) && !(startY <= j && startY + tileHeight >= j)) {
			double m = (double)(0 - ySlope) / (double)(0 - xSlope);
			for(double y = 0; 0 < ySlope ? y <= ySlope : y >= ySlope; y = 0 < ySlope ? (m > 0 ? y + m : y - m) : (m > 0 ? y - m : y + m)) {
				if(Math.abs(m) >= 2) {
					for(int a = (int)((1 * m) / Math.abs(m)); Math.abs(a)<Math.abs(m); a = m > 0 ? a + 1 : a - 1) {
						if(!coordinates.contains(new Point((int)Math.round(y / m), (int)Math.round(y) + a))) {
							coordinates.add(new Point((int)Math.round(y / m), (int)Math.round(y) + a));
							if(auto) {
								autotiles.put(new Point((int)Math.round(y / m), (int)Math.round(y) + a), new MapAutotile(tile));
								buildNeighbors((int)Math.round(y / m), (int)Math.round(y) + a);
							}
						}
					}
				} else {
					if(!coordinates.contains(new Point((int)Math.round(y / m), (int)Math.round(y)))) {
						coordinates.add(new Point((int)Math.round(y / m), (int)Math.round(y)));
						if(auto) {
							autotiles.put(new Point((int)Math.round(y / m), (int)Math.round(y)), new MapAutotile(tile));
							buildNeighbors((int)Math.round(y / m), (int)Math.round(y));
						}
					}
				}
			}
		} else if(!(startX <= i && startX + tileWidth >= i )) {
			for(int x = 0; x != xSlope; x = 0 < xSlope ? x + 1 : x - 1) {
				if(!coordinates.contains(new Point(x, 0))) {
					coordinates.add(new Point(x, 0));
					if(auto) {
						autotiles.put(new Point(x, 0), new MapAutotile(tile));
						buildNeighbors(x, 0);
					}
				}
			}
		} else {
			for(int y = 0; y != ySlope; y = 0 < ySlope ? y + 1 : y - 1) {
				if(!coordinates.contains(new Point(0, y))) {
					coordinates.add(new Point(0, y));
					if(auto) {
						autotiles.put(new Point(0, y), new MapAutotile(tile));
						buildNeighbors(0, y);
					}
				}
			}
		}
	}
	
	public void configureRect(int x, int y) {
		coordinates.clear();
		coordinates.add(new Point(0,0));
		if(auto) {
			autotiles.clear();
			autotiles.put(new Point(0, 0), new MapAutotile(tile));
		}
		int endX = (x - startX) / tileWidth;
		int endY = (y - startY) / tileHeight;
		int eX = 0;
		int eY = 0;
		for(int i = 0; i<Math.abs(endX); i++) {
			eY = 0;
			for(int j = 0; j<Math.abs(endY); j++) {
				coordinates.add(new Point(eX, eY));
				if(auto) {
					autotiles.put(new Point(eX, eY), new MapAutotile(tile));
					buildNeighbors(eX, eY);
				}
				if(endY > 0 && eY < endY)
					eY++;
				else if(eY > endY)
					eY--;
			}
			if(endX > 0 && eX < endX)
				eX++;
			else if(eX > endX)
				eX--;
		}
	}
	
	public void draw(Graphics2D g2) {		
		float[] scales = {1f, 1f, 1f, 0.85f};
		float[] offsets = new float[4];
		RescaleOp rop = new RescaleOp(scales, offsets, null);
		
		for(int x = 0; x<coordinates.size(); x++) {
			if(auto)
				g2.drawImage(autotiles.get(coordinates.get(x)).getDisplayTile(), rop, startX + (coordinates.get(x).x * tileWidth), startY + (coordinates.get(x).y * tileHeight));
			else
				g2.drawImage(tile, rop, startX + (coordinates.get(x).x * tileWidth), startY + (coordinates.get(x).y * tileHeight));
		}
	}
	
	public void erase(int x, int y) {
		Rectangle temp = new Rectangle(startX, startY, tileWidth, tileHeight);
		for(int a = 0; a<coordinates.size(); a++) {
			temp = new Rectangle(startX + (coordinates.get(a).x * tileWidth), startY + (coordinates.get(a).y * tileHeight), tileWidth, tileHeight);
			if(temp.contains(x, y)) {
				if(auto) {
					for(int i = 0; i<autotiles.get(new Point(coordinates.get(a).x, coordinates.get(a).y)).getNeighbors().length; i++) {
						try {
							autotiles.get(new Point(new Point(coordinates.get(a).x, coordinates.get(a).y))).getNeighbor(i).updateNeighbor(null, Math.abs(i - 7));
						} catch(NullPointerException npe) {};
					}
				}
				coordinates.remove(a);
			}
		}
	}
	
	public void buildNeighbors(int pX, int pY) {
		MapAutotile[] neighbors = new MapAutotile[8];
		neighbors[0] = autotiles.get(new Point(pX - 1, pY - 1));
		if(neighbors[0] != null)
			autotiles.get(new Point(pX - 1, pY - 1)).updateNeighbor(autotiles.get(new Point(pX, pY)), 7);
		neighbors[1] = autotiles.get(new Point(pX, pY - 1));
		if(neighbors[1] != null)
			autotiles.get(new Point(pX, pY - 1)).updateNeighbor(autotiles.get(new Point(pX, pY)), 6);
		neighbors[2] = autotiles.get(new Point(pX + 1, pY - 1));
		if(neighbors[2] != null)
			autotiles.get(new Point(pX + 1, pY - 1)).updateNeighbor(autotiles.get(new Point(pX, pY)), 5);
		neighbors[3] = autotiles.get(new Point(pX - 1, pY));
		if(neighbors[3] != null)
			autotiles.get(new Point(pX - 1, pY)).updateNeighbor(autotiles.get(new Point(pX, pY)), 4);
		neighbors[4] = autotiles.get(new Point(pX + 1, pY));
		if(neighbors[4] != null)
			autotiles.get(new Point(pX + 1, pY)).updateNeighbor(autotiles.get(new Point(pX, pY)), 3);
		neighbors[5] = autotiles.get(new Point(pX - 1, pY + 1));
		if(neighbors[5] != null)
			autotiles.get(new Point(pX - 1, pY + 1)).updateNeighbor(autotiles.get(new Point(pX, pY)), 2);
		neighbors[6] = autotiles.get(new Point(pX, pY + 1));
		if(neighbors[6] != null)
			autotiles.get(new Point(pX, pY + 1)).updateNeighbor(autotiles.get(new Point(pX, pY)), 1);
		neighbors[7] = autotiles.get(new Point(pX + 1, pY + 1));
		if(neighbors[7] != null)
			autotiles.get(new Point(pX + 1, pY + 1)).updateNeighbor(autotiles.get(new Point(pX, pY)), 0);
		autotiles.get(new Point(pX, pY)).updateNeighbors(neighbors);
	}
	
	public void save(BufferedWriter writer) throws IOException {
		writer.write("<TILE>");
		writer.newLine();
		writer.write(tileName + ";" + startX + ";" + startY + ";" + auto);
		writer.newLine();
		for(int x = 0; x<coordinates.size(); x++) {
			writer.write(coordinates.get(x).x + ";" + coordinates.get(x).y);
			if(auto)
				writer.write(autotiles.get(coordinates.get(x)).saveCorners());
			writer.newLine();
		}
		writer.write("<BREAK>");
		writer.newLine();
	}
	
	public int getCoordinate(int x, int y) {
		Rectangle temp = new Rectangle(startX, startY, tileWidth, tileHeight);
		for(int a = 0; a<coordinates.size(); a++) {
			temp = new Rectangle(startX + (coordinates.get(a).x * tileWidth), startY + (coordinates.get(a).y * tileHeight), tileWidth, tileHeight);
			if(temp.contains(x, y)) {
				return a;
			}
		}
		
		return 0;
	}
	
	public static int isAnimatedTile(String ref) {
		BufferedReader reader;
		
		try {
			reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/database/sprites"));

	    	String line;
	    	while((line = reader.readLine()) != null) {
	    		String[] temp = line.split(";");
	    		
	    		if(temp[0].matches(ref)) {
	    			reader.close();
	    			return Integer.parseInt(temp[1]);
	    		}
	    	}

	    	reader.close();
	    } catch (FileNotFoundException e) {
	    	System.out.println("The sprite database has been misplaced!");
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	System.out.println("Sprite database failed to load!");
	    	e.printStackTrace();
	    }
		
		return 1;
	}
	
	public void setTile(String ref) {
		BufferedImage temp;
		if(auto) {
			temp = ImageLoader.get().getSprite(System.getProperty("resources") + "/tiles/autotiles/" + ref);
		} else {
			temp = ImageLoader.get().getSprite(System.getProperty("resources") + "/tiles/" + ref);
		}
		this.tile = new BufferedImage(temp.getWidth(), temp.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = tile.getGraphics();
        g.drawImage(temp, 0, 0, null);
        g.dispose();
        
		int frames = isAnimatedTile(ref.replaceFirst(".png", ""));
		if(frames > 1) {
			tile = tile.getSubimage(0, 0, tile.getWidth() / frames, tile.getHeight());
		}
	}
	
	public void removeCoordinate(int x, int y) {
		coordinates.remove(new Point(x, y));
		if(auto)
			autotiles.remove(new Point(x, y));
	}
	
	public int getX() {
		return startX;
	}
	
	public int getY() {
		return startY;
	}
	
	public int getGridX() {
		return startX - ((int)Math.ceil(startX / tileWidth) * (tileWidth * 2));
	}
	
	public int getGridY() {
		return startY - ((int)Math.ceil(startY / tileHeight) * (tileHeight * 2));
	}
	
	public BufferedImage getTile() {
		return tile;
	}
	
	public String getTileName() {
		return tileName;
	}
	
	public void setAuto(boolean auto) {
		this.auto = auto;
	}
}
