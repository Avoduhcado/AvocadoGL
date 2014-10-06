package core.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

import javax.swing.JPopupMenu;

import core.editor.entities.EntityEditor;
import core.editor.entities.sprites.EditSprite;
import core.editor.entities.sprites.SpriteList;

import javax.swing.JTabbedPane;
import javax.swing.JComboBox;
import core.editor.map.MapDrawEnum;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JCheckBox;
import java.io.File;
import java.io.IOException;

import core.editor.map.MapPanel;

public class EditSplitPane extends JPanel implements ListSelectionListener {

	private static final long serialVersionUID = 1L;
	private JSplitPane splitPane;

	private JList<String> list;
	public DefaultListModel<String> listModel;

	private JTabbedPane tabbedPane;
	private JScrollPane listScroller;
	private JSplitPane mapSplitPane;
	private JPanel panelMapEdit;
	private JComboBox<MapDrawEnum> comboBox;
	private JSpinner sizeXSpin;
	private JSpinner sizeYSpin;
	private JCheckBox chckbxErase;
	private JCheckBox chckbxPaint;
	private JComboBox<String> comboBox_1;
	public JCheckBox chckbxTown;
	private JScrollPane scrollTilePane;
	public MapPanel mapPanel;
	private JScrollPane scrollMapPanel;
	private JTree tree;
	
	public boolean cleared = false;
	private JPanel panel;
	private JCheckBox flatCheck;

	/**
	 * Create the panel.
	 */
	public EditSplitPane(Dimension size) {		
		super(new BorderLayout());
		splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.15);

		listModel = new DefaultListModel<String>();

		list = new JList<String>(listModel);
		list.setPreferredSize(new Dimension(100, 0));
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				list.setSelectedIndex(list.locationToIndex(e.getPoint()));
				if(e.getButton() == MouseEvent.BUTTON3 || e.getClickCount() == 2)
					addPopup(e.getX(), e.getY());
			}
		});
		list.addListSelectionListener(this);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		listScroller = new JScrollPane(list);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.add(listScroller);
		tabbedPane.setTitleAt(0, "Entities");
		tabbedPane.setEnabledAt(0, true);
		
		splitPane.setLeftComponent(tabbedPane);
		
		mapSplitPane = new JSplitPane();
		tabbedPane.addTab("Map", null, mapSplitPane, null);
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if(tabbedPane.getSelectedComponent() == mapSplitPane)
					mapPanel.editing = false;
				else
					mapPanel.editing = true;
				
				mapPanel.clearMouse();
				panel.repaint();
			}
		});
		mapSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		panelMapEdit = new JPanel();
		panelMapEdit.setLayout(null);
		panelMapEdit.setPreferredSize(new Dimension(10, 200));
		mapSplitPane.setLeftComponent(panelMapEdit);
		
		comboBox = new JComboBox<MapDrawEnum>();
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				mapPanel.drawEnum = (MapDrawEnum) comboBox.getSelectedItem();
			}
		});
		comboBox.setModel(new DefaultComboBoxModel<MapDrawEnum>(MapDrawEnum.values()));
		comboBox.setBounds(10, 0, 88, 20);
		panelMapEdit.add(comboBox);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(JOptionPane.showConfirmDialog(null, "Are you sure you want to clear the image?") == JOptionPane.OK_OPTION) {
					mapPanel.clearMap();
					mapPanel.repaint();
					cleared = true;
				}
			}
		});
		btnClear.setBounds(10, 203, 89, 23);
		panelMapEdit.add(btnClear);
		
		JLabel lblSize = new JLabel("SizeX");
		lblSize.setBounds(10, 59, 46, 14);
		panelMapEdit.add(lblSize);
		
		sizeXSpin = new JSpinner();
		sizeXSpin.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				mapPanel.adjustSize((Integer)sizeXSpin.getValue(), mapPanel.getMapSize().height);
				scrollMapPanel.setPreferredSize(mapPanel.getMapSize());
				mapPanel.repaint();
			}
		});
		sizeXSpin.setModel(new SpinnerNumberModel(size.width, 800, 2000, 1));
		sizeXSpin.setBounds(52, 56, 46, 20);
		panelMapEdit.add(sizeXSpin);
		
		JLabel lblSizey = new JLabel("SizeY");
		lblSizey.setBounds(10, 81, 46, 14);
		panelMapEdit.add(lblSizey);
		
		sizeYSpin = new JSpinner();
		sizeYSpin.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				mapPanel.adjustSize(mapPanel.getMapSize().width, (Integer)sizeYSpin.getValue());
				scrollMapPanel.setPreferredSize(mapPanel.getMapSize());
				mapPanel.repaint();
			}
		});
		sizeYSpin.setModel(new SpinnerNumberModel(size.height, 600, 2000, 1));
		sizeYSpin.setBounds(52, 78, 46, 20);
		panelMapEdit.add(sizeYSpin);
		
		chckbxErase = new JCheckBox("Erase");
		chckbxErase.setToolTipText("Hold ctrl to erase all tiles under cursor.");
		chckbxErase.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				mapPanel.erase = chckbxErase.isSelected();
				mapPanel.clearMouse();
			}
		});
		chckbxErase.setBounds(10, 92, 88, 25);
		panelMapEdit.add(chckbxErase);
		
		chckbxPaint = new JCheckBox("Paint");
		chckbxPaint.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				mapPanel.tile = chckbxPaint.isSelected();
				mapPanel.clearMouse();
				mapPanel.repaint();
			}
		});
		chckbxPaint.setBounds(10, 120, 88, 23);
		panelMapEdit.add(chckbxPaint);
		
		comboBox_1 = new JComboBox<String>();
		comboBox_1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				mapPanel.alignType = comboBox_1.getSelectedIndex();
				switch(mapPanel.alignType) {
				case(0):
				case(1):
					mapPanel.align = null;
					break;
				case(2):
					mapPanel.align = new Point();
					mapPanel.align.x = 0;
					mapPanel.align.y = 0;
					break;
				default:
					System.out.println("No alignType of type " + mapPanel.alignType);
				}
			}
		});
		comboBox_1.setModel(new DefaultComboBoxModel<String>(new String[] {"Free", "Align Click", "Align 0, 0"}));
		comboBox_1.setBounds(10, 172, 88, 20);
		panelMapEdit.add(comboBox_1);
		
		JLabel lblGridStyle = new JLabel("Grid Style");
		lblGridStyle.setBounds(10, 150, 46, 14);
		panelMapEdit.add(lblGridStyle);
		
		chckbxTown = new JCheckBox("Town");
		chckbxTown.setBounds(10, 233, 97, 23);
		panelMapEdit.add(chckbxTown);
		
		flatCheck = new JCheckBox("Flat");
		flatCheck.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if(flatCheck.isSelected())
					mapPanel.flat = true;
				else
					mapPanel.flat = false;
			}
		});
		flatCheck.setBounds(10, 28, 88, 23);
		panelMapEdit.add(flatCheck);
		
		scrollTilePane = new JScrollPane();
		mapSplitPane.setRightComponent(scrollTilePane);
		
		tree = new JTree(buildTree());
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent arg0) {
				if(tree.getSelectionPath() != null) {
					String path = "";
					for(int x = 0; x<tree.getSelectionPath().getPathCount(); x++) {
						path += "/" + tree.getSelectionPath().getPath()[x];
					}
					File file = new File(System.getProperty("resources") + path);
					if(!file.isDirectory()) {
						try {
							mapPanel.setSelectedTile(file);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} else {
					tree.setSelectionPath(null);
				}
			}
		});
		scrollTilePane.setViewportView(tree);
		mapSplitPane.setDividerLocation(280);

		panel = new JPanel();
		
		scrollMapPanel = new JScrollPane();
		scrollMapPanel.setBounds(0, 0, 800, 600);
		scrollMapPanel.setPreferredSize(new Dimension(800, 600));
		scrollMapPanel.setAutoscrolls(true);
		
		mapPanel = new MapPanel(null, null, null, null);
		scrollMapPanel.add(mapPanel);
		scrollMapPanel.setViewportView(mapPanel);
		mapPanel.setAutoscrolls(true);
		mapPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(mapPanel.editing) {
					if(mapPanel.selection == null) {
						mapPanel.setSelection(e.getPoint());
						if(mapPanel.selection != null) {
							list.setSelectedValue(mapPanel.selection.toString(), true);
						}
					} else {
						if(e.getClickCount() == 2 && !e.isConsumed()) {
							addPopup(e.getX() + list.getWidth(), e.getY());
						} else if(e.getButton() == MouseEvent.BUTTON1) {						
							if(e.isControlDown()) {
								if(mapPanel.props.contains(mapPanel.selection)) {
									mapPanel.props.add(mapPanel.selection.getClone());
									mapPanel.props.get(mapPanel.props.size() - 1).setX(e.getX());
									mapPanel.props.get(mapPanel.props.size() - 1).setY(e.getY());
									listModel.addElement(mapPanel.props.get(mapPanel.props.size() - 1).toString());
									repaint();
								} else if(MapPanel.actors.contains(mapPanel.selection)) {
									MapPanel.actors.add(mapPanel.selection.getClone());
									MapPanel.actors.get(MapPanel.actors.size() - 1).setX(e.getX());
									MapPanel.actors.get(MapPanel.actors.size() - 1).setY(e.getY());
									listModel.addElement(MapPanel.actors.get(MapPanel.actors.size() - 1).toString());
									repaint();
								}
							} /*else {
								mapPanel.selection.setX(e.getX());
								if(mapPanel.selection.getX() + mapPanel.selection.getSprite().getWidth() > mapPanel.getSize().width) {
									mapPanel.selection.setX(mapPanel.selection.getX() - 
											((mapPanel.selection.getX() + mapPanel.selection.getSprite().getWidth()) - mapPanel.getSize().width));
								}
								mapPanel.selection.setY(e.getY());
								if(mapPanel.selection.getY() + mapPanel.selection.getSprite().getHeight() > mapPanel.getSize().height) {
									mapPanel.selection.setY(mapPanel.selection.getY() - 
											((mapPanel.selection.getY() + mapPanel.selection.getSprite().getHeight()) - mapPanel.getSize().height));
								}
								mapPanel.selection = null;
								list.clearSelection();
							}*/
						} else {
							mapPanel.selection = null;
							list.clearSelection();
						}

					}
					repaint();
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				mapPanel.mouseX = e.getX();
				mapPanel.mouseY = e.getY();
				mapPanel.mouseXNew = e.getX();
				mapPanel.mouseYNew = e.getY();
				mapPanel.setMapGridTile(-1);
				mapPanel.drawing = true;
				if(e.isControlDown()) {
					mapPanel.copyTile = true;
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				switch(mapPanel.drawEnum) {
				case FREE:
					if(mapPanel.tile) {
						mapPanel.update(e);
						mapPanel.repaint();
					}
					break;
				case LINE:
				case RECT:
					mapPanel.mouseXNew = e.getX();
					mapPanel.mouseYNew = e.getY();
					mapPanel.update(e);
					mapPanel.repaint();
					break;
				case FILL:
					mapPanel.fill(e.getX(), e.getY());
					mapPanel.update(e);
					mapPanel.repaint();
					break;
				}
				mapPanel.drawing = false;
				mapPanel.copyTile = false;
				panel.repaint();
			}
		});
		mapPanel.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if(mapPanel.editing) {
					if(mapPanel.selection != null) {
						mapPanel.mouseX = mapPanel.mouseXNew;
						mapPanel.mouseY = mapPanel.mouseYNew;
						mapPanel.mouseXNew = e.getX();
						mapPanel.mouseYNew = e.getY();
						mapPanel.selection.setX(mapPanel.selection.getX() + (mapPanel.mouseXNew - mapPanel.mouseX));
						mapPanel.selection.setY(mapPanel.selection.getY() + (mapPanel.mouseYNew - mapPanel.mouseY));
					}
				} else {
					switch(mapPanel.drawEnum) {
					case FREE:
						mapPanel.mouseX = mapPanel.mouseXNew;
						mapPanel.mouseY = mapPanel.mouseYNew;
						mapPanel.mouseXNew = e.getX();
						mapPanel.mouseYNew = e.getY();
						mapPanel.update(e);
						mapPanel.repaint();
						break;
					case LINE:
					case RECT:
						mapPanel.mouseXNew = e.getX();
						mapPanel.mouseYNew = e.getY();
						if(mapPanel.tile) {
							mapPanel.update(e);
						}
						mapPanel.repaint();
						break;
					case FILL:
						break;
					default:
						System.out.println("DrawType: " + mapPanel.drawEnum + " is not valid");
					}
				}
				panel.repaint();
			}
			
			@Override
			public void mouseMoved(MouseEvent e) {
				if(mapPanel.tile) {
					mapPanel.mouseX = e.getX();
					mapPanel.mouseY = e.getY();
					mapPanel.repaint();
				}
			}
		});
		scrollMapPanel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				Rectangle r = new Rectangle(arg0.getX(), arg0.getY(), 1, 1);
				((JScrollPane)arg0.getSource()).scrollRectToVisible(r);
			}
		});
		
		buildListModel();
		panel.setLayout(new BorderLayout(0, 0));
		
		panel.add(scrollMapPanel);
		splitPane.setRightComponent(panel);

		add(splitPane, BorderLayout.CENTER);
	}

	public void buildListModel() {
		if(mapPanel.backdrop != null) {
			listModel.addElement(mapPanel.backdrop.toString());
		}
		if(!mapPanel.backgroundMusic.matches("")) {
			listModel.addElement(mapPanel.backgroundMusic);
		}
		if(mapPanel.player != null) {
			listModel.addElement(mapPanel.player.toString());
		}
		if(!mapPanel.props.isEmpty()) {
			for(int x = 0; x<mapPanel.props.size(); x++)
				listModel.addElement(mapPanel.props.get(x).toString());
		}
		if(!MapPanel.actors.isEmpty()) {
			for(int x = 0; x<MapPanel.actors.size(); x++)
				listModel.addElement(MapPanel.actors.get(x).toString());
		}
		if(!mapPanel.paths.isEmpty()) {
			for(int x = 0; x<mapPanel.paths.size(); x++)
				listModel.addElement(mapPanel.paths.get(x).toString());
		}
	}

	public void clear() {
		mapPanel.clearScene();
		mapPanel.clearMap();
		listModel.clear();
	}
	
	public void clearSelection() {
		mapPanel.selection = null;
		list.clearSelection();
	}
	
	public DefaultMutableTreeNode buildTree() {
		DefaultMutableTreeNode root;
		DefaultMutableTreeNode grandparent;
		DefaultMutableTreeNode parent;
		File fileRoot = new File(System.getProperty("resources") + "/tiles");
		File[] fileGrandparent = fileRoot.listFiles();
		File[] fileParent;
        
        root = new DefaultMutableTreeNode(fileRoot.getName());

        if(fileGrandparent.length > 0) {
        	for(int x = 0; x<fileGrandparent.length; x++) {
	        	if(fileGrandparent[x].isDirectory()) {
	        		grandparent = new DefaultMutableTreeNode(fileGrandparent[x].getName());
	        		root.add(grandparent);
	        		fileParent = fileGrandparent[x].listFiles();
	        		if(fileParent.length > 0) {
		        		for(int y = 0; y<fileParent.length; y++) {
		        			parent = new DefaultMutableTreeNode(fileParent[y].getName());
		        			grandparent.add(parent);
		        		}
	        		}
	        	} else {
	        		grandparent = new DefaultMutableTreeNode(fileGrandparent[x].getName());
	        		root.add(grandparent);
	        	}
	        }
        }
        
        return root;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {
			if (list.getSelectedIndex() != -1) {
				mapPanel.setSelection(list.getSelectedValue());
			}
			mapPanel.clearMouse();
			repaint();
		}
	}

	private void addPopup(int x, int y) {
		JMenuItem item = new JMenuItem();

		JPopupMenu menu = new JPopupMenu();
		item = new JMenuItem("Edit");
		if(list.getSelectedIndex() == -1)
			item.setEnabled(false);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(list.getSelectedIndex() != -1 && mapPanel.selection != null) {
					EntityEditor entityEditor = new EntityEditor(mapPanel.selection, false);
					String attribs = entityEditor.getAttributes();
					if(attribs != null && !attribs.matches(""))
						mapPanel.updateAttributes(list.getSelectedValue(), attribs);
					clearSelection();
				}
			}
		});
		menu.add(item);
		item = new JMenuItem("Remove");
		if(list.getSelectedIndex() == -1)
			item.setEnabled(false);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(list.getSelectedIndex() != -1) {
					mapPanel.removeSelection(list.getSelectedValue());
					listModel.remove(list.getSelectedIndex());
					list.clearSelection();
					mapPanel.repaint();
				}
			}
		});
		menu.add(item);
		
		menu.add(new JSeparator());
		item = new JMenuItem("Add Backdrop");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SpriteList spriteList = new SpriteList();
				File file = new File(System.getProperty("resources") + "/sprites/" + spriteList.getSelection() + ".png");
				if(file.exists()) {
					if(mapPanel.backdrop != null)
						listModel.removeElement(mapPanel.backdrop.toString());
					mapPanel.backdrop = new EditSprite(file);
					mapPanel.backdrop.setType("Backdrop");
					listModel.addElement(mapPanel.backdrop.toString());
					clearSelection();
					repaint();
				}
			}
		});
		menu.add(item);
		item = new JMenuItem("Add BGM");
		item.addActionListener(new ActionListener() {
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
					if(!mapPanel.backgroundMusic.matches(""))
						listModel.removeElement("BGM-" + mapPanel.backgroundMusic);
					mapPanel.backgroundMusic = file.getName();
					listModel.addElement("BGM-" + mapPanel.backgroundMusic);
					clearSelection();
					repaint();
				}
			}
		});
		menu.add(item);
		item = new JMenuItem("Add Prop");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SpriteList spriteList = new SpriteList();
				File file = new File(System.getProperty("resources") + "/sprites/" + spriteList.getSelection() + ".png");
				if(file.exists()) {
					mapPanel.props.add(new EditSprite(file));
					mapPanel.props.get(mapPanel.props.size() - 1).setType("Prop");
					mapPanel.props.get(mapPanel.props.size() - 1).setX(Math.abs(mapPanel.getX()));
					mapPanel.props.get(mapPanel.props.size() - 1).setY(Math.abs(mapPanel.getY()));
					listModel.addElement(mapPanel.props.get(mapPanel.props.size() - 1).toString());
					clearSelection();
					repaint();
				}
			}
		});
		menu.add(item);
		item = new JMenuItem("Add Actor");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SpriteList spriteList = new SpriteList();
				File file = new File(System.getProperty("resources") + "/sprites/" + spriteList.getSelection() + ".png");
				if(file.exists()) {
					MapPanel.actors.add(new EditSprite(file));
					MapPanel.actors.get(MapPanel.actors.size() - 1).setType("Actor");
					MapPanel.actors.get(MapPanel.actors.size() - 1).setX(Math.abs(mapPanel.getX()));
					MapPanel.actors.get(MapPanel.actors.size() - 1).setY(Math.abs(mapPanel.getY()));
					listModel.addElement(MapPanel.actors.get(MapPanel.actors.size() - 1).toString());
					clearSelection();
					repaint();
				}
			}
		});
		menu.add(item);
		item = new JMenuItem("Add Player Spawn");
		item.setEnabled(false);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//mapPanel.addMouseListener(playerAdapter);
				clearSelection();
			}
		});
		menu.add(item);
		item = new JMenuItem("Add Pathway");
		item.setEnabled(false);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//mapPanel.addMouseListener(pathAdapter);
				clearSelection();
			}
		});
		menu.add(item);
		
		menu.show(list, x, y);
	}
}
