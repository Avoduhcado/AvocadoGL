package core.editor;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import core.editor.entities.EntityEditor;
import core.editor.entities.EntityEquipmentEditor;
import core.editor.entities.sprites.EditSprite;
import core.editor.entities.sprites.EditSpriteList;
import core.editor.entities.sprites.SpriteList;
import core.editor.equipment.SpellEditor;
import core.editor.equipment.SpellList;
import core.editor.equipment.WeaponEditor;
import core.editor.equipment.WeaponList;
import core.editor.items.ItemList;
import core.editor.items.ItemEditor;
import core.editor.map.MapGridArray;
import core.editor.map.MapPanel;
import core.editor.variables.VariableCreator;
import core.scene.Stage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Editor extends JFrame {

	/* TODO 
	 * Add Project editing to manage multiple maps at once
	 * Add Variable editing
	 */
	
	private static final long serialVersionUID = 1L;
	private EditSplitPane contentPane;
	public static ArrayList<String> IDList = new ArrayList<String>();
	private ArrayList<String> editList = new ArrayList<String>();
		
	private String sceneName;
	private Stage stage;
	
	private MouseAdapter pathAdapter = pathMouseListener();
	private MouseAdapter playerAdapter = playerMouseListener();

	private Dimension mapSize = new Dimension(800, 600);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/natives");
					System.setProperty("resources", System.getProperty("user.dir") + "/resources");
					System.setProperty("scenes", System.getProperty("resources") + "/database/scenes/");
					
					Editor frame = new Editor();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void open(final Stage stage) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {					
					Editor frame = new Editor();
					frame.setVisible(true);
					frame.stage = stage;
					frame.sceneName = stage.scene;
					frame.load(new File(System.getProperty("resources") + "/database/scenes/" + frame.sceneName));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Editor() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				if(stage != null) {
					if(editList.contains(stage.scene))
						stage.reload = true;
				}
			}
		});
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		EntityEquipmentEditor.fillDatabase();
		setVisible(true);
		setTitle("Super Avocado Builder Deluxe");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 857, 673);
				
		IDList.clear();
		contentPane = new EditSplitPane(mapSize);
		setContentPane(contentPane);
				
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contentPane.clear();
				contentPane.repaint();
			}
		});
		mnFile.add(mntmNew);
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File file = null;
				try {
					@SuppressWarnings("serial")
					JFileChooser fileChooser = new JFileChooser() {{ 
						setCurrentDirectory(new File(System.getProperty("resources") + "/database/scenes/"));
						setDialogTitle("Open a Scene file");
						showOpenDialog(null);
					}};
					
					file = fileChooser.getSelectedFile();
				} catch(Exception e) {
					e.printStackTrace();
				}
				if(file != null) {
					sceneName = file.getName();
					load(file);
				}
			}
		});
		mnFile.add(mntmOpen);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File file = null;
				try {
					@SuppressWarnings("serial")
					JFileChooser fileChooser = new JFileChooser() {{
						setCurrentDirectory(new File(System.getProperty("resources") + "/database/scenes/"));
						if(sceneName != null)
							setSelectedFile(new File(getCurrentDirectory() + "/" + sceneName));
						setDialogTitle("Save Scene as...");
						showSaveDialog(null);
					}};
					
					file = fileChooser.getSelectedFile();
				} catch(Exception e) {
					e.printStackTrace();
				}
				if(file != null) {
					sceneName = file.getName();
					contentPane.mapPanel.buildRects();
					mapSize = contentPane.mapPanel.getMapSize();
					save(file);
					if(!editList.contains(sceneName))
						editList.add(sceneName);
					if(stage != null) {
						if(editList.contains(stage.scene))
							stage.reload = true;
					}
				}
			}
		});
		mnFile.add(mntmSave);
		
		JMenu mnAdd = new JMenu("Add");
		menuBar.add(mnAdd);
		
		JMenuItem mntmBackdrop = new JMenuItem("Backdrop");
		mntmBackdrop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SpriteList spriteList = new SpriteList();
				File file = new File(System.getProperty("resources") + "/sprites/" + spriteList.getSelection() + ".png");
				if(file.exists()) {
					if(contentPane.mapPanel.backdrop != null)
						contentPane.listModel.removeElement(contentPane.mapPanel.backdrop.toString());
					contentPane.mapPanel.backdrop = new EditSprite(file);
					contentPane.mapPanel.backdrop.setType("Backdrop");
					contentPane.listModel.addElement(contentPane.mapPanel.backdrop.toString());
					contentPane.clearSelection();
					contentPane.repaint();
				}
			}
		});
		mnAdd.add(mntmBackdrop);
		
		JMenuItem mntmBackgroundMusic = new JMenuItem("Background Music");
		mntmBackgroundMusic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File file = null;
				try {
					@SuppressWarnings("serial")
					JFileChooser fileChooser = new JFileChooser() {{ 
						setCurrentDirectory(new File(System.getProperty("resources") + "/sounds"));
						setFileFilter(new FileNameExtensionFilter("Sounds", "ogg"));
						setAcceptAllFileFilterUsed(false);
						setDialogTitle("Add Background Music");
						showOpenDialog(null);
					}};
					
					file = fileChooser.getSelectedFile();
				} catch(Exception e) {
					e.printStackTrace();
				}
				if(file.exists()) {
					if(!contentPane.mapPanel.backgroundMusic.matches(""))
						contentPane.listModel.removeElement("BGM-" + contentPane.mapPanel.backgroundMusic);
					contentPane.mapPanel.backgroundMusic = file.getName();
					contentPane.listModel.addElement("BGM-" + contentPane.mapPanel.backgroundMusic);
					contentPane.clearSelection();
					contentPane.repaint();
				}
			}
		});
		mnAdd.add(mntmBackgroundMusic);
		
		JSeparator separator = new JSeparator();
		mnAdd.add(separator);
		
		JMenuItem mntmPlayerSpawn = new JMenuItem("Player Spawn");
		mntmPlayerSpawn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				contentPane.mapPanel.addMouseListener(playerAdapter);
				contentPane.clearSelection();
			}
		});
		mnAdd.add(mntmPlayerSpawn);
		
		JMenuItem mntmProp = new JMenuItem("Prop");
		mntmProp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SpriteList spriteList = new SpriteList();
				File file = new File(System.getProperty("resources") + "/sprites/" + spriteList.getSelection() + ".png");
				if(file.exists()) {
					contentPane.mapPanel.props.add(new EditSprite(file));
					contentPane.mapPanel.props.get(contentPane.mapPanel.props.size() - 1).setType("Prop");
					contentPane.mapPanel.props.get(contentPane.mapPanel.props.size() - 1).setX(Math.abs(contentPane.mapPanel.getX()));
					contentPane.mapPanel.props.get(contentPane.mapPanel.props.size() - 1).setY(Math.abs(contentPane.mapPanel.getY()));
					contentPane.listModel.addElement(contentPane.mapPanel.props.get(contentPane.mapPanel.props.size() - 1).toString());
					contentPane.clearSelection();
					contentPane.repaint();
				}
			}
		});
		mnAdd.add(mntmProp);
		
		JMenuItem mntmActor = new JMenuItem("Actor");
		mntmActor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SpriteList spriteList = new SpriteList();
				File file = new File(System.getProperty("resources") + "/sprites/" + spriteList.getSelection() + ".png");
				if(file.exists()) {
					MapPanel.actors.add(new EditSprite(file));
					MapPanel.actors.get(MapPanel.actors.size() - 1).setType("Actor");
					MapPanel.actors.get(MapPanel.actors.size() - 1).setX(Math.abs(contentPane.mapPanel.getX()));
					MapPanel.actors.get(MapPanel.actors.size() - 1).setY(Math.abs(contentPane.mapPanel.getY()));
					contentPane.listModel.addElement(MapPanel.actors.get(MapPanel.actors.size() - 1).toString());
					contentPane.clearSelection();
					contentPane.repaint();
				}
			}
		});
		mnAdd.add(mntmActor);
		
		JSeparator separator_1 = new JSeparator();
		mnAdd.add(separator_1);
		
		JMenuItem mntmPathway = new JMenuItem("Pathway");
		mntmPathway.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				contentPane.mapPanel.addMouseListener(pathAdapter);
				contentPane.clearSelection();
			}
		});
		mnAdd.add(mntmPathway);
		
		JMenu mnCreate = new JMenu("Create");
		menuBar.add(mnCreate);
		
		JMenuItem mntmItem = new JMenuItem("Item");
		mntmItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				@SuppressWarnings("unused")
				ItemEditor itemEditor = new ItemEditor(0);
				EntityEquipmentEditor.fillDatabase();
			}
		});
		mnCreate.add(mntmItem);
		
		JMenuItem mntmSprite = new JMenuItem("Sprite");
		mntmSprite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File file = null;
				try {
					@SuppressWarnings("serial")
					JFileChooser fileChooser = new JFileChooser() {{ 
						setCurrentDirectory(new File(System.getProperty("resources") + "/sprites"));
						setFileFilter(new FileNameExtensionFilter("Images", "png"));
						setAcceptAllFileFilterUsed(false);
						setDialogTitle("Create a Sprite");
						showOpenDialog(null);
					}};
					
					file = fileChooser.getSelectedFile();
				} catch(Exception e) {
					e.printStackTrace();
				}
				if(file != null) {
					@SuppressWarnings("unused")
					EntityEditor entityEditor = new EntityEditor(new EditSprite(file), true);
				}
			}
		});
		mnCreate.add(mntmSprite);
		
		JMenuItem mntmSpell = new JMenuItem("Spell");
		mntmSpell.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unused")
				SpellEditor spellEditor = new SpellEditor(0);
			}
		});
		
		JMenuItem mntmWeapon_1 = new JMenuItem("Weapon");
		mntmWeapon_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File file = null;
				try {
					@SuppressWarnings("serial")
					JFileChooser fileChooser = new JFileChooser() {{ 
						setCurrentDirectory(new File(System.getProperty("resources") + "/sprites"));
						setFileFilter(new FileNameExtensionFilter("Images", "png"));
						setAcceptAllFileFilterUsed(false);
						setDialogTitle("Create a Weapon");
						showOpenDialog(null);
					}};
					
					file = fileChooser.getSelectedFile();
				} catch(Exception e2) {
					e2.printStackTrace();
				}
				if(file != null) {
					@SuppressWarnings("unused")
					WeaponEditor weaponEditor = new WeaponEditor(file.getName().replace(".png", ""));
				}
			}
		});
		mnCreate.add(mntmWeapon_1);
		mnCreate.add(mntmSpell);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenuItem mntmItem_1 = new JMenuItem("Item");
		mntmItem_1.addActionListener(new ActionListener() {
			@SuppressWarnings("unused")
			public void actionPerformed(ActionEvent e) {
				ItemList itemList = new ItemList();
				ItemEditor itemEditor;
				if(itemList.getSelection() != null)
					itemEditor = new ItemEditor(Integer.parseInt(itemList.getSelection()));
			}
		});
		mnEdit.add(mntmItem_1);
		
		JMenuItem mntmSpriteList = new JMenuItem("Sprite List");
		mntmSpriteList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unused")
				EditSpriteList editSpriteList = new EditSpriteList();
			}
		});
		mnEdit.add(mntmSpriteList);
		
		JMenuItem mntmVariable = new JMenuItem("Variable");
		mntmVariable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unused")
				VariableCreator variableCreator = new VariableCreator();
			}
		});
		mnEdit.add(mntmVariable);
		
		JMenuItem mntmWeapon = new JMenuItem("Weapon");
		mntmWeapon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WeaponList weaponList = new WeaponList();
				@SuppressWarnings("unused")
				WeaponEditor weaponEditor;
				if(weaponList.getSelection() != null)
					weaponEditor = new WeaponEditor(weaponList.getSelection());
			}
		});
		mnEdit.add(mntmWeapon);
		
		JMenuItem mntmSpell_1 = new JMenuItem("Spell");
		mntmSpell_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SpellList spellList = new SpellList();
				@SuppressWarnings("unused")
				SpellEditor spellEditor;
				if(spellList.getSelection() != null)
					spellEditor = new SpellEditor(Integer.parseInt(spellList.getSelection()));
			}
		});
		mnEdit.add(mntmSpell_1);
	}
	
	public MouseAdapter playerMouseListener() {
		return new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(contentPane.mapPanel.player != null)
					contentPane.listModel.removeElement(contentPane.mapPanel.player.toString());
				contentPane.mapPanel.player = new EditSprite(new Rectangle(e.getX(), e.getY(), 15, 15), "Player");
				contentPane.mapPanel.player.setType("Player");
				contentPane.listModel.addElement(contentPane.mapPanel.player.toString());
				contentPane.repaint();
				contentPane.mapPanel.removeMouseListener(playerAdapter);
			}
		};
	}
	
	public MouseAdapter pathMouseListener() {
		return new MouseAdapter() {
			File tempFile = new File(System.getProperty("scenes") + "temp");
			File destFile;
			Point firstClick;
			Point lastClick;
			Point destClick;
			boolean inDestFile;
			@Override
			public void mousePressed(MouseEvent e) {
				if(!inDestFile) {
					firstClick = e.getPoint();
					
					if(firstClick.x < 0) {
						firstClick.x = 0;
					} else if(firstClick.x > contentPane.mapPanel.getBounds().getMaxX()) {
						firstClick.x = (int) contentPane.mapPanel.getBounds().getMaxX();
					}
					if(firstClick.y < 0) {
						firstClick.y = 0;
					} else if(firstClick.y > contentPane.mapPanel.getBounds().getMaxY()) {
						firstClick.y = (int) contentPane.mapPanel.getBounds().getMaxY();
					}
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if(!inDestFile) {
					lastClick = e.getPoint();
					
					if(lastClick.x < 0) {
						lastClick.x = 0;
					} else if(lastClick.x > contentPane.mapPanel.getBounds().getMaxX()) {
						lastClick.x = (int) contentPane.mapPanel.getBounds().getMaxX();
					}
					if(lastClick.y < 0) {
						lastClick.y = 0;
					} else if(lastClick.y > contentPane.mapPanel.getBounds().getMaxY()) {
						lastClick.y = (int) contentPane.mapPanel.getBounds().getMaxY();
					}
					
					if(lastClick.x < firstClick.x) {
						int x = firstClick.x;
						firstClick.x = lastClick.x;
						lastClick.x = x;
					}
					if(lastClick.y < firstClick.y) {
						int y = firstClick.y;
						firstClick.y = lastClick.y;
						lastClick.y = y;
					}
					
					@SuppressWarnings("serial")
					JFileChooser fileChooser = new JFileChooser() {{
						setCurrentDirectory(new File(System.getProperty("resources") + "/database/scenes/"));
						setDialogTitle("Link a Scene");
						showOpenDialog(null);
					}};
					
					destFile = fileChooser.getSelectedFile();
					save(tempFile);
					inDestFile = true;
					load(destFile);
					contentPane.repaint();
				} else {
					destClick = e.getPoint();
					load(tempFile);
					tempFile.delete();
					contentPane.mapPanel.paths.add(new EditSprite(new Rectangle(firstClick.x, firstClick.y, lastClick.x - firstClick.x, lastClick.y - firstClick.y), destFile.getName()));
					contentPane.mapPanel.paths.get(contentPane.mapPanel.paths.size() - 1).setType("Path");
					contentPane.listModel.addElement(contentPane.mapPanel.paths.get(contentPane.mapPanel.paths.size() - 1).toString());
					contentPane.repaint();
					contentPane.mapPanel.pathStrings.add(firstClick.x + ";" + firstClick.y + ";" + (lastClick.x - firstClick.x) + ";" + (lastClick.y - firstClick.y) + ";" +
							destFile.getName() + ";" + destClick.x + ";" + destClick.y);
					inDestFile = false;
					contentPane.mapPanel.removeMouseListener(pathAdapter);
				}
			}
		};
	}
	
	public void clear() {
		contentPane.clear();
		mapSize = new Dimension(800, 600);
	}
	
	public void load(File file) {
		clear();
		BufferedReader reader;
		
		try {
	    	reader = new BufferedReader(new FileReader(file));

	    	String line;
	    	while((line = reader.readLine()) != null) {
	    		String[] temp = line.split(";");
	    		
	    		if(line.matches("<HITMAP>")) {
	    			loadHitmap(reader);
	    		} else if(line.matches("<MAP>")) {
	    			loadMap(reader);
	    		} else if(line.matches("<BGM>")) {
	    			line = reader.readLine();
	    			contentPane.mapPanel.backgroundMusic = "BGM-" + line;
	    		} else if(line.matches("<BACKDROP>")) {
	    			line = reader.readLine();
	    			temp = line.split(";");
	    			contentPane.mapPanel.backdrop = new EditSprite(System.getProperty("resources") + "/sprites/" + temp[0] + ".png", Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
					contentPane.mapPanel.backdrop.setType("Backdrop");
	    		} else if(line.matches("<PLAYER>")) {
	    			line = reader.readLine();
	    			temp = line.split(";");
	    			contentPane.mapPanel.player = new EditSprite(System.getProperty("resources") + "/sprites/" + temp[0] + ".png", Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
	    			contentPane.mapPanel.player.setType("Player");
	    			contentPane.mapPanel.player.setID("Player");
	    			contentPane.mapPanel.player.loadAttributes(reader);
	    		} else if(line.matches("<ACTOR>")) {
	    			line = reader.readLine();
	    			temp = line.split(";");
	    			MapPanel.actors.add(new EditSprite(System.getProperty("resources") + "/sprites/" + temp[0] + ".png", Integer.parseInt(temp[1]), Integer.parseInt(temp[2])));
	    			MapPanel.actors.get(MapPanel.actors.size() - 1).setType("Actor");
	    			MapPanel.actors.get(MapPanel.actors.size() - 1).setID(temp[3]);
	    			MapPanel.actors.get(MapPanel.actors.size() - 1).loadAttributes(reader);
	    		} else if(line.matches("<PROP>")) {
	    			line = reader.readLine();
	    			temp = line.split(";");
	    			contentPane.mapPanel.props.add(new EditSprite(System.getProperty("resources") + "/sprites/" + temp[0] + ".png", Integer.parseInt(temp[1]), Integer.parseInt(temp[2])));
	    			contentPane.mapPanel.props.get(contentPane.mapPanel.props.size() - 1).setType("Prop");
	    			contentPane.mapPanel.props.get(contentPane.mapPanel.props.size() - 1).setID(temp[3]);
	    			contentPane.mapPanel.props.get(contentPane.mapPanel.props.size() - 1).loadAttributes(reader);
	    		} else if(line.matches("<PATHWAY>")) {
	    			line = reader.readLine();
	    			temp = line.split(";");
	    			contentPane.mapPanel.pathStrings.add(temp[0] + ";" + temp[1]  + ";" + temp[2] + ";" + temp[3] + ";" + temp[4] + ";" + temp[5] + ";" + temp[6]);
	    			contentPane.mapPanel.paths.add(new EditSprite(new Rectangle(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]),
	    					Integer.parseInt(temp[2]), Integer.parseInt(temp[3])), temp[4]));
	    			contentPane.mapPanel.paths.get(contentPane.mapPanel.paths.size() - 1).setType("Path");
	    		}
	    	}
	    	
	    	reader.close();
	    } catch (FileNotFoundException e) {
	    	System.out.println("Could not find save file: " + file.getName());
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	System.out.println("Save file failed to load: " + file.getName());
	    	e.printStackTrace();
	    }
		
		contentPane.mapPanel.setPreferredSize(mapSize);
		contentPane.buildListModel();
		contentPane.repaint();
	}
	
	public void loadHitmap(BufferedReader reader) {
		String line;
		try {
			String[] temp = reader.readLine().split(";");
			mapSize = new Dimension(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
			contentPane.mapPanel.setPreferredSize(mapSize);
			boolean flat = false;
			while((line = reader.readLine()) != null && !line.matches("<END>")) {
				if(line.matches("<FLAT>")) {
					flat = true;
				} else {
					if(flat) {
						temp = line.split(";");
						contentPane.mapPanel.getFlatRects().add(new Rectangle(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), Integer.parseInt(temp[3])));
					} else {
						temp = line.split(";");
						contentPane.mapPanel.getSolidRects().add(new Rectangle(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), Integer.parseInt(temp[3])));
					}
				}
			}
			contentPane.mapPanel.buildImage();
		} catch (IOException e) {
			System.err.println("The scene file has been misplaced!");
	    	e.printStackTrace();
		}
	}
	
	public void loadMap(BufferedReader reader) {
		String line;
		try {
			line = reader.readLine();
			contentPane.chckbxTown.setSelected(Boolean.parseBoolean(line));
			while((line = reader.readLine()) != null && !line.matches("<END>")) {
				if(line.matches("<TILE>")) {
					line = reader.readLine();
					String[] temp = line.split(";");
					contentPane.mapPanel.getMapGrid().add(new MapGridArray(Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), temp[0], Boolean.parseBoolean(temp[3])));
					contentPane.mapPanel.getMapGrid().get(contentPane.mapPanel.getMapGrid().size() - 1).removeCoordinate(0, 0);
					while((line = reader.readLine()) != null && !line.matches("<BREAK>")) {
						temp = line.split(";");
						contentPane.mapPanel.getMapGrid().get(contentPane.mapPanel.getMapGrid().size() - 1).addCoordinate(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
					}
				}
			}
		} catch (IOException e) {
			System.err.println("The scene file has been misplaced!");
	    	e.printStackTrace();
		}
	}
	
	public void save(File file) {
		if(!new File(System.getProperty("resources") + "/database/scenes/").exists()) {
			if(new File(System.getProperty("resources") + "/database/scenes/").mkdir()) {
				System.out.println("Directory Created");
			} else {
				System.err.println("Directory was not created");
				return;
			}
		} else {
			try {
				writeFile(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void writeFile(File file) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));

		file.setWritable(true);
		if(contentPane.mapPanel.backdrop != null) {
			writer.write("<BACKDROP>");
			writer.newLine();
			writer.write(contentPane.mapPanel.backdrop.getName() + ";" + contentPane.mapPanel.backdrop.getX() + ";" + contentPane.mapPanel.backdrop.getY());
			writer.newLine();
			writer.write("<END>");
			writer.newLine();
			writer.newLine();
		}
		if(!contentPane.mapPanel.backgroundMusic.matches("")) {
			writer.write("<BGM>");
			writer.newLine();
			writer.write(contentPane.mapPanel.backgroundMusic.replaceFirst("BGM-", ""));
			writer.newLine();
			writer.write("<END>");
			writer.newLine();
			writer.newLine();
		}
		if(!contentPane.mapPanel.props.isEmpty()) {
			for(int x = 0; x<contentPane.mapPanel.props.size(); x++) {
				writer.write("<PROP>");
				writer.newLine();
				writer.write(contentPane.mapPanel.props.get(x).getName().replace(".png", "") + ";" + contentPane.mapPanel.props.get(x).getX() + ";" + contentPane.mapPanel.props.get(x).getY()
						+ ";" + contentPane.mapPanel.props.get(x).getID());
				writer.newLine();
				if(!contentPane.mapPanel.props.get(x).getAttributes().matches("")) {
					String[] temp = contentPane.mapPanel.props.get(x).getAttributes().split("\n");
					for(int a = 0; a<temp.length; a++) {
						writer.write(temp[a]);
						writer.newLine();
					}
				}
				writer.write("<END>");
				writer.newLine();
				writer.newLine();
			}
		}
		if(contentPane.mapPanel.player != null) {
			writer.write("<PLAYER>");
			writer.newLine();
			writer.write(contentPane.mapPanel.player.getName().replace(".png", "") + ";" + contentPane.mapPanel.player.getX() + ";" + contentPane.mapPanel.player.getY());
			writer.newLine();
			if(!contentPane.mapPanel.player.getAttributes().matches("")) {
				String[] temp = contentPane.mapPanel.player.getAttributes().split("\n");
				for(int a = 0; a<temp.length; a++) {
					writer.write(temp[a]);
					writer.newLine();
				}
			}
			writer.write("<END>");
			writer.newLine();
			writer.newLine();
		}
		if(!MapPanel.actors.isEmpty()) {
			for(int x = 0; x<MapPanel.actors.size(); x++) {
				writer.write("<ACTOR>");
				writer.newLine();
				writer.write(MapPanel.actors.get(x).getName().replace(".png", "") + ";" + MapPanel.actors.get(x).getX() + ";" + MapPanel.actors.get(x).getY()
						+ ";" + MapPanel.actors.get(x).getID());
				writer.newLine();
				if(!MapPanel.actors.get(x).getAttributes().matches("")) {
					String[] temp = MapPanel.actors.get(x).getAttributes().split("\n");
					for(int a = 0; a<temp.length; a++) {
						writer.write(temp[a]);
						writer.newLine();
					}
				}
				writer.write("<END>");
				writer.newLine();
				writer.newLine();
			}
		}
		if(!contentPane.mapPanel.pathStrings.isEmpty()) {
			for(int x = 0; x<contentPane.mapPanel.pathStrings.size(); x++) {
				writer.write("<PATHWAY>");
				writer.newLine();
				writer.write(contentPane.mapPanel.pathStrings.get(x));
				writer.newLine();
				writer.write("<END>");
				writer.newLine();
				writer.newLine();
			}
		}
		if(!contentPane.mapPanel.getSolidRects().isEmpty() || !contentPane.mapPanel.getFlatRects().isEmpty() || contentPane.mapPanel.getMapSize() != new Dimension(800, 600)) {
			writer.write("<HITMAP>");
			writer.newLine();
			writer.write(contentPane.mapPanel.getMapSize().width + ";" + contentPane.mapPanel.getMapSize().height);
			writer.newLine();
			for(int x = 0; x<contentPane.mapPanel.getSolidRects().size(); x++) {
				writer.write(contentPane.mapPanel.getSolidRects().get(x).x + ";" + contentPane.mapPanel.getSolidRects().get(x).y + ";" 
						+ contentPane.mapPanel.getSolidRects().get(x).width + ";" + contentPane.mapPanel.getSolidRects().get(x).height);
				writer.newLine();
			}
			if(!contentPane.mapPanel.getFlatRects().isEmpty()) {
				writer.write("<FLAT>");
				writer.newLine();
				for(int x = 0; x<contentPane.mapPanel.getFlatRects().size(); x++) {
					writer.write(contentPane.mapPanel.getFlatRects().get(x).x + ";" + contentPane.mapPanel.getFlatRects().get(x).y + ";" 
							+ contentPane.mapPanel.getFlatRects().get(x).width + ";" + contentPane.mapPanel.getFlatRects().get(x).height);
					writer.newLine();
				}
			}
			writer.write("<END>");
			writer.newLine();
			writer.newLine();
		}
		if(!contentPane.mapPanel.getMapGrid().isEmpty()) {
			writer.write("<MAP>");
			writer.newLine();
			writer.write(contentPane.chckbxTown.isSelected() + "");
			writer.newLine();
			for(int x = 0; x<contentPane.mapPanel.getMapGrid().size(); x++) {
				contentPane.mapPanel.getMapGrid().get(x).save(writer);
				writer.newLine();
			}
			writer.write("<END>");
			writer.newLine();
			writer.newLine();
		}

		writer.close();
	}
}
