package core.editor.map;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class MapAutotile {

	private int frames = 1;
	private MapAutotile[] neighbors = new MapAutotile[8]; // Clockwise from top left
	private Point[] corners = {new Point(0,0), new Point(1,0), new Point(0,1), new Point(1,1)}; // 6 per row, 8 per column
	private BufferedImage tile;
	private BufferedImage displayTile;
	
	public MapAutotile(BufferedImage img) {
		int w = img.getWidth() / frames;
		int h = img.getHeight();
		this.tile = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics g = this.tile.getGraphics();
        g.drawImage(img, 0, 0, null);
		setDisplayTile();
	}
	
	public void updateNeighbor(MapAutotile neighbor, int side) {
		this.neighbors[side] = neighbor;
		updateCorners();
	}
	
	public void updateNeighbors(MapAutotile[] neighbors) {
		this.neighbors = neighbors;
		updateCorners();
	}
	
	public void updateCorners() {
		if(neighbors[1] != null && neighbors[3] != null && neighbors[4] != null && neighbors[6] != null) {
			if(neighbors[0] != null)
				corners[0] = new Point(2,4);
			else
				corners[0] = new Point(4,0);
			if(neighbors[2] != null)
				corners[1] = new Point(3,4);
			else
				corners[1] = new Point(5,0);
			if(neighbors[5] != null)
				corners[2] = new Point(2,5);
			else
				corners[2] = new Point(4,1);
			if(neighbors[7] != null)
				corners[3] = new Point(3,5);
			else
				corners[3] = new Point(5,1);
		} else if(neighbors[1] == null && neighbors[3] != null && neighbors[4] != null && neighbors[6] != null) {
			corners[0] = new Point(2,2);
			corners[1] = new Point(3,2);
			if(neighbors[5] != null)
				corners[2] = new Point(2,3);
			else
				corners[2] = new Point(4,1);
			if(neighbors[7] != null)
				corners[3] = new Point(3,3);
			else
				corners[3] = new Point(5,1);
		} else if(neighbors[3] == null && neighbors[1] != null && neighbors[6] != null && neighbors[4] != null) {
			corners[0] = new Point(0,4);
			corners[2] = new Point(0,5);
			if(neighbors[2] != null)
				corners[1] = new Point(1,4);
			else
				corners[1] = new Point(5,0);
			if(neighbors[7] != null)
				corners[3] = new Point(1,5);
			else
				corners[3] = new Point(5,1);
		} else if(neighbors[4] == null && neighbors[1] != null && neighbors[6] != null && neighbors[3] != null) {
			corners[1] = new Point(5,4);
			corners[3] = new Point(5,5);
			if(neighbors[0] != null)
				corners[0] = new Point(4,4);
			else
				corners[0] = new Point(4,0);
			if(neighbors[5] != null)
				corners[2] = new Point(4,5);
			else
				corners[2] = new Point(4,1);
		} else if(neighbors[6] == null && neighbors[3] != null && neighbors[4] != null && neighbors[1] != null) {
			corners[2] = new Point(2,7);
			corners[3] = new Point(3,7);
			if(neighbors[0] != null)
				corners[0] = new Point(2,6);
			else
				corners[0] = new Point(4,0);
			if(neighbors[2] != null)
				corners[1] = new Point(3,6);
			else
				corners[1] = new Point(5, 0);
		} else if(neighbors[1] == null && neighbors[3] != null && neighbors[4] != null && neighbors[6] == null) {
			corners[0] = new Point(2,2);
			corners[1] = new Point(3,2);
			corners[2] = new Point(2,7);
			corners[3] = new Point(3,7);
		} else if(neighbors[1] != null && neighbors[3] == null && neighbors[4] == null && neighbors[6] != null) {
			corners[0] = new Point(0,4);
			corners[1] = new Point(5,4);
			corners[2] = new Point(0,5);
			corners[3] = new Point(5,5);
		} else if(neighbors[1] != null && neighbors[3] != null && neighbors[4] == null && neighbors[6] == null) {
			if(neighbors[0] != null)
				corners[0] = new Point(4,6);
			else
				corners[0] = new Point(4,0);
			corners[1] = new Point(5,6);
			corners[2] = new Point(4,7);
			corners[3] = new Point(5,7);
		} else if(neighbors[1] != null && neighbors[3] == null && neighbors[4] != null && neighbors[6] == null) {
			corners[0] = new Point(0,6);
			if(neighbors[2] != null)
				corners[1] = new Point(1,6);
			else
				corners[1] = new Point(5,0);
			corners[2] = new Point(0,7);
			corners[3] = new Point(1,7);
		} else if(neighbors[1] == null && neighbors[3] != null && neighbors[4] == null && neighbors[6] != null) {
			corners[0] = new Point(4,2);
			corners[1] = new Point(5,2);
			if(neighbors[5] != null)
				corners[2] = new Point(4,3);
			else
				corners[2] = new Point(4,1);
			corners[3] = new Point(5,3);
		} else if(neighbors[1] == null && neighbors[3] == null && neighbors[4] != null && neighbors[6] != null) {
			corners[0] = new Point(0,2);
			corners[1] = new Point(1,2);
			corners[2] = new Point(0,3);
			if(neighbors[7] != null)
				corners[3] = new Point(1,3);
			else
				corners[3] = new Point(5,1);
		} else if(neighbors[1] != null) {
			corners[0] = new Point(0,6);
			corners[1] = new Point(5,6);
			corners[2] = new Point(0,7);
			corners[3] = new Point(5,7);
		} else if(neighbors[3] != null) {
			corners[0] = new Point(4,2);
			corners[1] = new Point(5,2);
			corners[2] = new Point(4,7);
			corners[3] = new Point(5,7);
		} else if(neighbors[4] != null) {
			corners[0] = new Point(0,2);
			corners[1] = new Point(1,2);
			corners[2] = new Point(0,7);
			corners[3] = new Point(1,7);
		} else if(neighbors[6] != null) {
			corners[0] = new Point(0,2);
			corners[1] = new Point(5,2);
			corners[2] = new Point(0,3);
			corners[3] = new Point(5,3);
		}
		
		buildTile();
	}
	
	public void buildTile() {
		int w = tile.getWidth() / (3 * frames);
		int h = tile.getHeight() / 4;
		this.displayTile = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Raster[] r = new Raster[4];
		r[0] = tile.getData(new Rectangle(corners[0].x * (w/2), corners[0].y * (h/2), w/2, h/2));
		displayTile.setData(r[0].createTranslatedChild(0, 0));
		r[1] = tile.getData(new Rectangle(corners[1].x * (w/2), corners[1].y * (h/2), w/2, h/2));
		displayTile.setData(r[1].createTranslatedChild(w/2, 0));
		r[2] = tile.getData(new Rectangle(corners[2].x * (w/2), corners[2].y * (h/2), w/2, h/2));
		displayTile.setData(r[2].createTranslatedChild(0, h/2));
		r[3] = tile.getData(new Rectangle(corners[3].x * (w/2), corners[3].y * (h/2), w/2, h/2));
		displayTile.setData(r[3].createTranslatedChild(w/2, h/2));
	}
	
	public String saveCorners() {
		return ";" + corners[2].x + ";" + corners[2].y + ";" + corners[3].x + ";" + corners[3].y + ";" + corners[1].x + ";" + corners[1].y + ";" + corners[0].x + ";" + corners[0].y;
	}
	
	public MapAutotile[] getNeighbors() {
		return neighbors;
	}
	
	public MapAutotile getNeighbor(int neighbor) {
		return neighbors[neighbor];
	}
	
	public void setFrames(int frames) {
		this.frames = frames;
		
		BufferedImage img = this.tile.getSubimage(0, 0, tile.getWidth() / frames, tile.getHeight());
		int w = img.getWidth();
		int h = img.getHeight();
		this.tile = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics g = this.tile.getGraphics();
        g.drawImage(img, 0, 0, null);
		setDisplayTile();
	}
	
	public BufferedImage getTile() {
		return tile;
	}
	
	public void setDisplayTile() {
		BufferedImage img = tile;
		int w = (tile.getWidth()/3);
        int h = tile.getHeight()/4;
        this.displayTile = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics g = this.displayTile.getGraphics();
        g.drawImage(img, 0, 0, null);
	}
	
	public BufferedImage getDisplayTile() {
		return displayTile;
	}
	
}
