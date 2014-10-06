package core.editor.map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import core.editor.entities.sprites.EditSprite;
import core.utilities.loader.ImageLoader;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MapPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Map
	public int mouseX, mouseY, mouseXNew, mouseYNew;
	public MapDrawEnum drawEnum = MapDrawEnum.FREE;
	public boolean flat;
	public boolean erase;
	public boolean tile;
	public boolean drawing;
	public boolean copyTile;
	private BufferedImage bi;
	private BufferedImage selectedTile;
	private String tileName;
	private ArrayList<Point> solidBlocks = new ArrayList<Point>();
	private ArrayList<Point> flatBlocks = new ArrayList<Point>();
	private ArrayList<Rectangle> solidRects = new ArrayList<Rectangle>();
	private ArrayList<Rectangle> flatRects = new ArrayList<Rectangle>();
	private ArrayList<MapGridArray> mapGrid = new ArrayList<MapGridArray>();
	private int mapGridTile = -1;
	public int alignType = 0;
	public Point align = new Point();
	private boolean autotiling;
	private boolean saving;
	
	// Edit
	public EditSprite backdrop;
	public EditSprite player;
	public String backgroundMusic = "";
	public ArrayList<String> pathStrings = new ArrayList<String>();
	public ArrayList<EditSprite> props = new ArrayList<EditSprite>();
	public static ArrayList<EditSprite> actors = new ArrayList<EditSprite>();
	public ArrayList<EditSprite> paths = new ArrayList<EditSprite>();
	public EditSprite selection;
	public boolean editing = true;
	
	/**
	 * Create the panel.
	 */
	public MapPanel(ArrayList<Rectangle> solidRects, ArrayList<Rectangle> flatRects, ArrayList<MapGridArray> map, Dimension size) {
		setBackground(Color.BLACK);
		bi = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
		setPreferredSize(new Dimension(800, 600));
		
		if(size != null) {
			setPreferredSize(size);
			bi = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		}
		
		if(solidRects != null && !solidRects.isEmpty()) {
			this.solidRects = solidRects;
		}
		
		if(flatRects != null && !flatRects.isEmpty()) {
			this.flatRects = flatRects;
		}
		
		buildImage();
		
		Graphics g = bi.createGraphics();
		this.paint(g);
		g.dispose();
		if(map != null)
			mapGrid = map;
	}

	public void update(MouseEvent e) {
		if(tile && selectedTile != null && !editing) {
			if(erase) {
				if(!mapGrid.isEmpty()) {
					for(int x = mapGrid.size() - 1; x>=0; x--) {
						if(mapGrid.get(x).contains(mouseX, mouseY)) {
							mapGrid.get(x).erase(mouseX, mouseY);
							if(!e.isControlDown())
								break;
						}
					}
				}
			} else {
				switch(drawEnum) {
				case FREE:
					if(!mapGrid.isEmpty()) {
						boolean canDraw = true;
						for(int x = 0; x<mapGrid.size(); x++) {
							if(tileName == mapGrid.get(x).getTileName() && mapGrid.get(x).contains(mouseX, mouseY) && alignType == 0) {
								canDraw = false;
								mapGridTile = x;
								break;
							} else if(copyTile && alignType == 0) {
								if(mapGrid.get(x).contains(mouseX, mouseY)) {
									canDraw = false;
									copyTile = false;
									if(autotiling)
										mapGrid.add(mapGrid.get(x).copy(mapGrid.get(x).getCoordinate(mouseX, mouseY), tileName, true));
									else
										mapGrid.add(mapGrid.get(x).copy(mapGrid.get(x).getCoordinate(mouseX, mouseY), tileName, false));
									mapGridTile = mapGrid.size() - 1;
									break;
								}
							}
						}
						if(canDraw) {
							if(mapGridTile != -1) {
								mapGrid.get(mapGridTile).addPoint(mouseX, mouseY);
							} else {
								if(alignType > 0) {
									if(align == null)
										align = new Point(mouseX, mouseY);
									mapGrid.add(new MapGridArray(align.x, align.y, mouseX, mouseY, tileName, autotiling));
								} else {
									if(autotiling)
										mapGrid.add(new MapGridArray(mouseX, mouseY, tileName, true));
									else
										mapGrid.add(new MapGridArray(mouseX, mouseY, tileName, false));
								}
								mapGridTile = mapGrid.size() - 1;
								mapGrid.get(mapGridTile).addPoint(mouseX, mouseY);
							}
						}
					} else {
						if(alignType > 0) {
							if(align == null)
								align = new Point(mouseX, mouseY);
							if(autotiling)
								mapGrid.add(new MapGridArray(align.x, align.y, mouseX, mouseY, tileName, true));
							else
								mapGrid.add(new MapGridArray(align.x, align.y, mouseX, mouseY, tileName, false));
						} else {
							if(autotiling)
								mapGrid.add(new MapGridArray(mouseX, mouseY, tileName, true));
							else
								mapGrid.add(new MapGridArray(mouseX, mouseY, tileName, false));
						}
						mapGridTile = 0;
						mapGrid.get(mapGridTile).addPoint(mouseX, mouseY);
					}
					break;
				case LINE:
					if(mapGridTile == -1) {
						boolean canDraw = true;
						for(int x = 0; x<mapGrid.size(); x++) {
							if(tileName == mapGrid.get(x).getTileName() || copyTile && alignType == 0) {
								if(mapGrid.get(x).contains(mouseX, mouseY)) {
									copyTile = false;
									canDraw = false;
									if(autotiling)
										mapGrid.add(mapGrid.get(x).copy(mapGrid.get(x).getCoordinate(mouseX, mouseY), tileName, true));
									else
										mapGrid.add(mapGrid.get(x).copy(mapGrid.get(x).getCoordinate(mouseX, mouseY), tileName, false));
									mapGridTile = mapGrid.size() - 1;
									break;
								}
							}
						}
						if(canDraw) {
							if(alignType > 0) {
								if(align == null)
									align = new Point(mouseX, mouseY);
								if(autotiling)
									mapGrid.add(new MapGridArray(align.x, align.y, mouseX, mouseY, tileName, true));
								else
									mapGrid.add(new MapGridArray(align.x, align.y, mouseX, mouseY, tileName, false));
							} else {
								if(autotiling)
									mapGrid.add(new MapGridArray(mouseX, mouseY, tileName, true));
								else
									mapGrid.add(new MapGridArray(mouseX, mouseY, tileName, false));
							}
							mapGridTile = mapGrid.size() - 1;
							mapGrid.get(mapGridTile).addPoint(mouseX, mouseY);
						}
					} else {
						if(!mapGrid.get(mapGridTile).contains(mouseXNew, mouseYNew)) {
							mapGrid.get(mapGridTile).configureLine(mouseXNew, mouseYNew);
						}
					}
					break;
				case RECT:
					if(mapGridTile == -1) {
						boolean canDraw = true;
						for(int x = 0; x<mapGrid.size(); x++) {
							if(tileName == mapGrid.get(x).getTileName() || copyTile && alignType == 0) {
								if(mapGrid.get(x).contains(mouseX, mouseY)) {
									copyTile = false;
									canDraw = false;
									if(autotiling)
										mapGrid.add(mapGrid.get(x).copy(mapGrid.get(x).getCoordinate(mouseX, mouseY), tileName, true));
									else
										mapGrid.add(mapGrid.get(x).copy(mapGrid.get(x).getCoordinate(mouseX, mouseY), tileName, false));
									mapGridTile = mapGrid.size() - 1;
									break;
								}
							}
						}
						if(canDraw) {
							if(alignType > 0) {
								if(align == null)
									align = new Point(mouseX, mouseY);
								if(autotiling)
									mapGrid.add(new MapGridArray(align.x, align.y, mouseX, mouseY, tileName, true));
								else
									mapGrid.add(new MapGridArray(align.x, align.y, mouseX, mouseY, tileName, false));
							} else {
								if(autotiling)
									mapGrid.add(new MapGridArray(mouseX, mouseY, tileName, true));
								else
									mapGrid.add(new MapGridArray(mouseX, mouseY, tileName, false));
							}
							mapGridTile = mapGrid.size() - 1;
							mapGrid.get(mapGridTile).addPoint(mouseX, mouseY);
						}
					} else {
						if(!mapGrid.get(mapGridTile).contains(mouseXNew, mouseYNew)) {
							mapGrid.get(mapGridTile).configureRect(mouseXNew, mouseYNew);
						}
					}
					break;
				default:
					System.out.println("No such drawType as: " + drawEnum);
				}
			}
		} else {
			saving = true;
			Graphics g = bi.createGraphics();
			this.paint(g);
			g.dispose();
			saving = false;
		}
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.drawImage(bi, null, 0, 0);
		
		if(!tile && !editing) {
			if(erase)
				g2.setColor(Color.BLACK);
			else if(flat)
				g2.setColor(Color.RED);
			else
				g2.setColor(Color.WHITE);
			switch(drawEnum) {
			case FREE:
				g2.drawLine(mouseX, mouseY, mouseXNew, mouseYNew);
				break;
			case LINE:
				g2.drawLine(mouseX, mouseY, mouseXNew, mouseYNew);
				break;
			case RECT:
				if(mouseXNew > mouseX && mouseYNew > mouseY)
					g2.fillRect(mouseX, mouseY, mouseXNew - mouseX, mouseYNew - mouseY);
				if(mouseXNew > mouseX && mouseYNew < mouseY)
					g2.fillRect(mouseX, mouseYNew, mouseXNew - mouseX, mouseY - mouseYNew);
				if(mouseXNew < mouseX && mouseYNew > mouseY)
					g2.fillRect(mouseXNew, mouseY, mouseX - mouseXNew, mouseYNew - mouseY);
				if(mouseXNew < mouseX && mouseYNew < mouseY)
					g2.fillRect(mouseXNew, mouseYNew, mouseX - mouseXNew, mouseY - mouseYNew);
				break;
			case FILL:
				break;
			default:
				System.out.println("No such drawType as " + drawEnum);
			}
		}
		
		float[] scales = {1f, 1f, 1f, 0.75f};
		float[] offsets = new float[4];
		RescaleOp rop = new RescaleOp(scales, offsets, null);
		
		if(!saving) {
			if(editing) {
				if(backdrop != null) {
					backdrop.draw(g2, rop);
				}
			}
			if(!mapGrid.isEmpty()) {
				for(int x = 0; x<mapGrid.size(); x++) {
					mapGrid.get(x).draw(g2);
				}
			}
			if(selectedTile != null) {
				scales = new float[]{1f, 1f, 1f, 0.5f};
				offsets = new float[4];
				rop = new RescaleOp(scales, offsets, null);
				
				if(tile)
					g2.drawImage(selectedTile, rop, mouseX, mouseY);
				if(drawing && mapGridTile != -1) {
					g2.setColor(Color.GRAY);
					for(int x = mapGrid.get(mapGridTile).getGridX(); x<this.getWidth() + selectedTile.getWidth(); x += selectedTile.getWidth()) {
						for(int y = mapGrid.get(mapGridTile).getGridY(); y<this.getHeight() + selectedTile.getHeight(); y += selectedTile.getHeight()) {
							g2.drawLine(x, y, this.getHeight(), y);
							g2.drawLine(x, y, x, this.getHeight());
						}
					}
				}
			}
			g2.setColor(Color.CYAN);
			g2.drawRect(-1, -1, bi.getWidth() + 1, bi.getHeight() + 1);
		
			// Props/Actors
			scales = new float[]{1f, 1f, 1f, 1f};
			if(!editing)
				scales = new float[]{1f, 1f, 1f, 0.75f};
			offsets = new float[4];
			rop = new RescaleOp(scales, offsets, null);
			
			if(!props.isEmpty()) {
				for(int x = 0; x<props.size(); x++)
					props.get(x).draw(g2, rop);
			}
			if(!actors.isEmpty()) {
				for(int x = 0; x<actors.size(); x++)
					actors.get(x).draw(g2, rop);
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
		}
	}
	
	public void clearMap() {
		mouseX = -1;
		mouseXNew = -1;
		mouseY = -1;
		mouseYNew = -1;
		
		solidBlocks.clear();
		solidRects.clear();
		mapGrid.clear();
		
		bi = new BufferedImage(this.getSize().width, this.getSize().height, BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.createGraphics();
		this.paint(g);
		g.dispose();
	}
	
	public void clearScene() {
		backdrop = null;
		backgroundMusic = "";
		props = new ArrayList<EditSprite>();
		player = null;
		actors = new ArrayList<EditSprite>();
		paths = new ArrayList<EditSprite>();
		pathStrings = new ArrayList<String>();
		selection = null;
	}
	
	public void clearMouse() {
		mouseX = -1;
		mouseY = -1;
		mouseXNew = -1;
		mouseYNew = -1;
	}
	
	public void adjustSize(int width, int height) {
		saving = true;
		if(width > bi.getWidth() || height > bi.getHeight()) {
			BufferedImage tempBi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = tempBi.createGraphics();
			this.paint(g);
			g.dispose();
			bi = tempBi;
		} else {
			BufferedImage tempBi = bi.getSubimage(0, 0, width, height);
			bi = tempBi;
		}
		saving = false;
		this.setPreferredSize(new Dimension(width, height));
	}
	
	public void buildImage() {
		bi = new BufferedImage(getPreferredSize().width, getPreferredSize().height, BufferedImage.TYPE_INT_RGB);
		// Fill in flat rectangles
		for(int a = 0; a<flatRects.size(); a++) {
			for(int x = flatRects.get(a).x; x<flatRects.get(a).getMaxX(); x++) {
				for(int y = flatRects.get(a).y; y<flatRects.get(a).getMaxY(); y++) {
					bi.setRGB(x, y, Color.RED.getRGB());
				}
			}
		}
		
		// Fill in solid rectangles
		for(int a = 0; a<solidRects.size(); a++) {
			for(int x = solidRects.get(a).x; x<solidRects.get(a).getMaxX(); x++) {
				for(int y = solidRects.get(a).y; y<solidRects.get(a).getMaxY(); y++) {
					bi.setRGB(x, y, Color.WHITE.getRGB());
				}
			}
		}
	}
	
	public void saveImage() {
		BufferedImage bi = new BufferedImage(this.getSize().width, this.getSize().height, BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.createGraphics();
		this.paint(g);
		g.dispose();
		
		try {
			ImageIO.write(bi, "png", new File(System.getProperty("resources") + "/TempCol.png"));
		} catch(IOException e) {}
	}
	
	public void saveBlocks() {
		solidBlocks.clear();
		flatBlocks.clear();
		for(int x = 0; x<bi.getWidth(); x++) {
			for(int y = 0; y<bi.getHeight(); y++) {
				if(bi.getRGB(x, y) == Color.WHITE.getRGB())
					solidBlocks.add(new Point(x, y));
				else if(bi.getRGB(x, y) == Color.RED.getRGB())
					flatBlocks.add(new Point(x, y));
			}
		}
	}
	
	public void buildRects() {
		saveBlocks();
		solidRects.clear();
		flatRects.clear();
		
		Rectangle rect = null;
		for(int x = 0; x<solidBlocks.size(); x++) {
			if(rect == null) {
				rect = new Rectangle(solidBlocks.get(x).x, solidBlocks.get(x).y, 1, 1);
			} else {
				if(solidBlocks.get(x).x == rect.x && solidBlocks.get(x).y == rect.y + rect.height) {
					rect.setBounds(rect.x, rect.y, rect.width, rect.height + 1);
				} else {
					solidRects.add(rect);
					rect = new Rectangle(solidBlocks.get(x).x, solidBlocks.get(x).y, 1, 1);
				}
			}
		}
		if(rect != null && !solidRects.contains(rect))
			solidRects.add(rect);
		for(int x = 0; x<solidRects.size(); x++) {
			rect = solidRects.get(x);
			if(x < solidRects.size()) {
				for(int y = x + 1; y<solidRects.size(); y++) {
					if(solidRects.get(y).y == rect.y && solidRects.get(y).height == rect.height) {
						solidRects.set(x, new Rectangle(rect.x, rect.y, rect.width + 1, rect.height));
						solidRects.remove(y);
						x--;
						break;
					} else if(solidRects.get(y).x > rect.getMaxX() + 1)
						break;
				}
			}
		}
		
		rect = null;
		for(int x = 0; x<flatBlocks.size(); x++) {
			if(rect == null) {
				rect = new Rectangle(flatBlocks.get(x).x, flatBlocks.get(x).y, 1, 1);
			} else {
				if(flatBlocks.get(x).x == rect.x && flatBlocks.get(x).y == rect.y + rect.height) {
					rect.setBounds(rect.x, rect.y, rect.width, rect.height + 1);
				} else {
					flatRects.add(rect);
					rect = new Rectangle(flatBlocks.get(x).x, flatBlocks.get(x).y, 1, 1);
				}
			}
		}
		if(rect != null && !flatRects.contains(rect))
			flatRects.add(rect);
		for(int x = 0; x<flatRects.size(); x++) {
			rect = flatRects.get(x);
			if(x < flatRects.size()) {
				for(int y = x + 1; y<flatRects.size(); y++) {
					if(flatRects.get(y).y == rect.y && flatRects.get(y).height == rect.height) {
						flatRects.set(x, new Rectangle(rect.x, rect.y, rect.width + 1, rect.height));
						flatRects.remove(y);
						x--;
						break;
					} else if(flatRects.get(y).x > rect.getMaxX() + 1)
						break;
				}
			}
		}
	}
	
	public boolean isBlack(BufferedImage image, int posX, int posY) {
		if(erase) {
			if(flat) {
				if(image.getRGB(posX, posY) == Color.RED.getRGB())
					return true;
			} else {
				if(image.getRGB(posX, posY) == Color.WHITE.getRGB())
					return true;
			}
		} else {
			if(image.getRGB(posX, posY) == Color.BLACK.getRGB())
				return true;
		}
		
		return false;
    }
	
	public void fill(int mouseX, int mouseY) {
		boolean[][] painted = new boolean[bi.getHeight()][bi.getWidth()];

		for (int i = 0; i < bi.getHeight(); i++) {
			for (int j = 0; j < bi.getWidth(); j++) {
				if (isBlack(bi, j, i) && !painted[i][j]) {
					Queue<Point> queue = new LinkedList<Point>();
					queue.add(new Point(j, i));
					Queue<Point> toFill = new LinkedList<Point>();

					while (!queue.isEmpty()) {
						Point p = queue.remove();

						if ((p.x >= 0) && (p.x < bi.getWidth() && (p.y >= 0) && (p.y < bi.getHeight()))) {
							if (!painted[p.y][p.x] && isBlack(bi, p.x, p.y)) {
								painted[p.y][p.x] = true;
								toFill.add(new Point(p.x, p.y));

								queue.add(new Point(p.x + 1, p.y));
								queue.add(new Point(p.x - 1, p.y));
								queue.add(new Point(p.x, p.y + 1));
								queue.add(new Point(p.x, p.y - 1));
							}
						}
					}
					if(toFill.contains(new Point(mouseX, mouseY))) {
						while(!toFill.isEmpty()) {
							Point p = toFill.remove();
							if(erase) {
								bi.setRGB(p.x, p.y, Color.BLACK.getRGB());
							} else if(flat) {
								bi.setRGB(p.x, p.y, Color.RED.getRGB());
							} else {
								bi.setRGB(p.x, p.y, Color.WHITE.getRGB());
							}
						}
						j = bi.getWidth() + 1;
						i = bi.getHeight() + 1;
					}
				}
			}
		}
	}
	
	public void setMapGridTile(int x) {
		mapGridTile = x;
	}
	
	public void setSelectedTile(File selectedTile) throws IOException {
		if(selectedTile.getParent().endsWith("autotiles")) {
			MapAutotile auto = new MapAutotile(ImageLoader.get().getSprite(selectedTile.getAbsolutePath()));
			tileName = selectedTile.getName();
			int frames = MapGridArray.isAnimatedTile(tileName.replaceFirst(".png", ""));
			if(frames > 1) {
				auto.setFrames(frames);
			}
			int w = auto.getDisplayTile().getWidth();
			int h = auto.getDisplayTile().getHeight();
			this.selectedTile = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	        Graphics g = this.selectedTile.getGraphics();
	        g.drawImage(auto.getDisplayTile(), 0, 0, null);
	        autotiling = true;
		} else {
			BufferedImage img = ImageLoader.get().getSprite(selectedTile.getAbsolutePath());
			tileName = selectedTile.getName();
			int frames = MapGridArray.isAnimatedTile(tileName.replaceFirst(".png", ""));
			if(frames > 1) {
				img = img.getSubimage(0, 0, img.getWidth() / frames, img.getHeight());
			}
			int w = img.getWidth();
	        int h = img.getHeight();
	        this.selectedTile = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	        Graphics g = this.selectedTile.getGraphics();
	        g.drawImage(img, 0, 0, null);
	        autotiling = false;
		}
	}
	
	public BufferedImage getSelectedTile() {
		return selectedTile;
	}
	
	public ArrayList<Point> getSolidBlocks() {
		return solidBlocks;
	}
	
	public ArrayList<Point> getFlatBlocks() {
		return flatBlocks;
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
		}
		if(!backgroundMusic.matches("") && select.matches("BGM-" + backgroundMusic)) {
			backgroundMusic = "";
		}
		if(!props.isEmpty()) {
        	for(int x = 0; x<props.size(); x++) {
            	if(props.get(x).toString().matches(select)) {
            		props.remove(x);
            	}
        	}
    	}
		if(!actors.isEmpty()) {
        	for(int x = 0; x<actors.size(); x++) {
            	if(actors.get(x).toString().matches(select)) {
            		actors.remove(x);
            	}
        	}
    	}
		if(!paths.isEmpty()) {
    		for(int x = 0; x<paths.size(); x++) {
    			if(paths.get(x).toString().matches(select)) {
    				paths.remove(x);
    				pathStrings.remove(x);
    			}
    		}
    	}
		if(player != null) {
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
	
	public Dimension getMapSize() {
		return new Dimension(bi.getWidth(), bi.getHeight());
	}
	
	public ArrayList<Rectangle> getSolidRects() {
		return solidRects;
	}
	
	public ArrayList<Rectangle> getFlatRects() {
		return flatRects;
	}
	
	public ArrayList<MapGridArray> getMapGrid() {
		return mapGrid;
	}
	
}
