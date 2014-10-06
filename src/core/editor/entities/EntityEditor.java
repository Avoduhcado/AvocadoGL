package core.editor.entities;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import core.editor.actions.ActionEditor;
import core.editor.entities.ai.EntityFactionsList;
import core.editor.entities.sprites.EditSprite;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;
import javax.swing.JButton;

import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JList;
import java.awt.event.MouseAdapter;
import javax.swing.ListSelectionModel;
import core.entity.ai.Hostility;
import javax.swing.ScrollPaneConstants;

public class EntityEditor extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JCheckBox chckbxActions;
	private JList<String> list;
	private DefaultListModel<String> listModel = new DefaultListModel<String>();
	private DefaultListModel<String> copyModel = new DefaultListModel<String>();
	private JCheckBox lblSpeed;
	private JTextField textField;
	private JCheckBox lblAI;
	private JComboBox<Hostility> hostilityCombo;
	private JTextArea alliesField;
	private JTextArea enemiesField;
	private JCheckBox lblStats;
	private JTextArea textArea_1;
	private JButton btnNewButton;
	private JCheckBox lblEquipment;
	private JButton btnEditEquip;
	private JTextArea textArea_2;
	private JCheckBox lblSound;
	private JButton btnSetHit;
	private JTextField textField_10;
	private boolean dialogueOn;
	private boolean speedOn;
	private boolean hostilityOn;
	private boolean statsOn;
	private boolean equipmentOn;
	private boolean soundOn;
	
	private EntitySpritePanel entitySpritePanel;
	public EditSprite sprite;
	private JTextField textField_11;
	private JTextField textField_12;
	private JCheckBox chckbxSolid;
	private JTextField textField_13;
	private JTextField textField_14;
	private JTextField textField_15;
	private JTextField textField_16;
	
	private String attributes = "";
	private JScrollPane scrollPane_1;
	private JCheckBox switchA;
	private JCheckBox switchD;
	private JCheckBox switchC;
	private JCheckBox switchB;
	private JScrollPane scrollPane_2;
	private JScrollPane scrollPane_3;

	/**
	 * Create the frame.
	 */
	public EntityEditor(EditSprite sprite, boolean edit) {
		this.sprite = sprite;
		
		setResizable(false);
		setTitle("Entity Editor");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 375);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 494, 322);
		contentPane.add(tabbedPane);
		
		JPanel EntityPanel = new JPanel();
		tabbedPane.addTab("Entity", null, EntityPanel, null);
		EntityPanel.setLayout(null);
		
		JSeparator separator_5 = new JSeparator();
		separator_5.setBounds(6, 37, 473, 2);
		EntityPanel.add(separator_5);
		
		chckbxActions = new JCheckBox("Actions");
		chckbxActions.setBounds(6, 7, 97, 23);
		EntityPanel.add(chckbxActions);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 50, 374, 233);
		EntityPanel.add(scrollPane);
		
		list = new JList<String>();
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(list.getSelectedIndex() != -1 && e.getKeyCode() == KeyEvent.VK_DELETE) {
					if(list.getSelectedIndices().length == 1 && list.getSelectedValue().trim().matches("@")) {
						;
					} else {
						while(list.getSelectedIndices().length > 0) {
							if(list.getSelectedIndex() != listModel.size() - 1)
								listModel.remove(list.getSelectedIndex());
							else
								break;
						}
					}
				} else if(list.getSelectedIndex() != -1 && e.getKeyCode() == KeyEvent.VK_ENTER) {
					ActionEditor actionEditor = new ActionEditor();
					if(actionEditor.getLine() != null) {
						String[] temp = actionEditor.getLine().split("\n");
						String prefix = "";
						if(list.getSelectedValue().startsWith(" ")) {
							int indent = list.getSelectedValue().indexOf('@');
							for(int x = 0; x<indent; x++)
								prefix += " ";
						}
						for(int x = temp.length - 1; x>=0; x--) {
							listModel.add(list.getSelectedIndex(), prefix + temp[x]);
						}
						list.clearSelection();
					}
				}
			}
		});
		list.setEnabled(false);
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				list.setSelectedIndex(list.locationToIndex(e.getPoint()));
				checkBlockSelect();
				String prefix = "";
				for(int x = 0; list.getSelectedValue().startsWith(" ", x); x++) {
					prefix += " ";
				}
				if(listModel.get(list.getSelectedIndex()).startsWith(prefix + ">"))
					list.clearSelection();
				if(e.getClickCount() == 2 && !e.isConsumed() && list.getSelectedIndex() != -1) {
					ActionEditor actionEditor = new ActionEditor();
					if(actionEditor.getLine() != null) {
						String[] temp = actionEditor.getLine().split("\n");
						prefix = "";
						if(list.getSelectedValue().startsWith(" ")) {
							int indent = list.getSelectedValue().indexOf('@');
							for(int x = 0; x<indent; x++)
								prefix += " ";
						}
						for(int x = temp.length - 1; x>=0; x--) {
							listModel.add(list.getSelectedIndex(), prefix + temp[x]);
						}
						list.clearSelection();
					}
				} else if(e.getButton() == MouseEvent.BUTTON3 && list.getSelectedIndex() != -1) {
					addPopup(e.getX(), e.getY());
				}
			}
		});
		list.setModel(listModel);
		scrollPane.setViewportView(list);
		
		JLabel lblSwitches = new JLabel("Switches");
		lblSwitches.setBounds(390, 50, 46, 14);
		EntityPanel.add(lblSwitches);
		
		switchA = new JCheckBox("A");
		switchA.setEnabled(false);
		switchA.setBounds(386, 71, 97, 23);
		EntityPanel.add(switchA);
		
		switchB = new JCheckBox("B");
		switchB.setEnabled(false);
		switchB.setBounds(386, 97, 97, 23);
		EntityPanel.add(switchB);
		
		switchC = new JCheckBox("C");
		switchC.setEnabled(false);
		switchC.setBounds(386, 123, 97, 23);
		EntityPanel.add(switchC);
		
		switchD = new JCheckBox("D");
		switchD.setEnabled(false);
		switchD.setBounds(386, 149, 97, 23);
		EntityPanel.add(switchD);
		
		chckbxActions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialogueOn = !dialogueOn;
				if(dialogueOn) {
					list.setEnabled(true);
					switchA.setEnabled(true);
					switchB.setEnabled(true);
					switchC.setEnabled(true);
					switchD.setEnabled(true);
				} else {
					list.setEnabled(false);
					switchA.setEnabled(false);
					switchB.setEnabled(false);
					switchC.setEnabled(false);
					switchD.setEnabled(false);
				}
			}
		});
		
		JPanel ActorPanel = new JPanel();
		tabbedPane.addTab("Actor", null, ActorPanel, null);
		ActorPanel.setLayout(null);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(16, 32, 419, 2);
		ActorPanel.add(separator);
		
		lblSpeed = new JCheckBox("Speed");
		lblSpeed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				speedOn = !speedOn;
				if(speedOn)
					textField.setEnabled(true);
				else
					textField.setEnabled(false);
			}
		});
		lblSpeed.setBounds(6, 11, 72, 14);
		ActorPanel.add(lblSpeed);
		
		textField = new JTextField();
		textField.setEnabled(false);
		textField.setToolTipText("The Actor's speed. Decimals accepted.");
		textField.setBounds(6, 39, 68, 20);
		ActorPanel.add(textField);
		textField.setColumns(10);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setBounds(88, 23, 2, 260);
		ActorPanel.add(separator_1);
		
		lblAI = new JCheckBox("AI");
		lblAI.setBounds(104, 11, 80, 14);
		ActorPanel.add(lblAI);
		
		hostilityCombo = new JComboBox<Hostility>();
		hostilityCombo.setEnabled(false);
		hostilityCombo.setModel(new DefaultComboBoxModel<Hostility>(Hostility.values()));
		hostilityCombo.setBounds(100, 39, 80, 20);
		ActorPanel.add(hostilityCombo);
		
		JLabel lblAllies = new JLabel("Allies");
		lblAllies.setBounds(100, 70, 80, 14);
		ActorPanel.add(lblAllies);
		
		JLabel lblEnemies = new JLabel("Enemies");
		lblEnemies.setBounds(100, 178, 74, 14);
		ActorPanel.add(lblEnemies);
		
		scrollPane_2 = new JScrollPane();
		scrollPane_2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_2.setBounds(100, 89, 84, 80);
		ActorPanel.add(scrollPane_2);
		
		alliesField = new JTextArea();
		alliesField.setWrapStyleWord(true);
		alliesField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2  && !e.isConsumed() && alliesField.isEnabled()) {
					EntityFactionsList factionsList = new EntityFactionsList();
					if(factionsList.getSelection() != null) {
						if(alliesField.getText().isEmpty())
							alliesField.setText(factionsList.getSelection());
						else
							alliesField.setText(alliesField.getText() + ";" + factionsList.getSelection());
					}
				}
			}
		});
		scrollPane_2.setViewportView(alliesField);
		alliesField.setEditable(false);
		alliesField.setEnabled(false);
		alliesField.setToolTipText("");
		alliesField.setColumns(10);
		
		scrollPane_3 = new JScrollPane();
		scrollPane_3.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_3.setBounds(100, 203, 84, 80);
		ActorPanel.add(scrollPane_3);
		
		enemiesField = new JTextArea();
		enemiesField.setWrapStyleWord(true);
		enemiesField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2  && !e.isConsumed() && enemiesField.isEnabled()) {
					EntityFactionsList factionsList = new EntityFactionsList();
					if(factionsList.getSelection() != null) {
						if(enemiesField.getText().isEmpty())
							enemiesField.setText(factionsList.getSelection());
						else
							enemiesField.setText(enemiesField.getText() + ";" + factionsList.getSelection());
					}
				}
			}
		});
		scrollPane_3.setViewportView(enemiesField);
		enemiesField.setEditable(false);
		enemiesField.setEnabled(false);
		enemiesField.setToolTipText("");
		enemiesField.setColumns(10);
		
		lblAI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hostilityOn = !hostilityOn;
				if(hostilityOn) {
					hostilityCombo.setEnabled(true);
					alliesField.setEnabled(true);
					enemiesField.setEnabled(true);
				} else {
					hostilityCombo.setEnabled(false);
					alliesField.setEnabled(false);
					enemiesField.setEnabled(false);
				}
			}
		});
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setOrientation(SwingConstants.VERTICAL);
		separator_2.setBounds(190, 23, 2, 260);
		ActorPanel.add(separator_2);
		
		lblStats = new JCheckBox("Stats");
		lblStats.setBounds(210, 11, 80, 14);
		ActorPanel.add(lblStats);

		textArea_1 = new JTextArea();
		textArea_1.setLineWrap(true);
		textArea_1.setEditable(false);
		textArea_1.setBounds(202, 66, 88, 217);
		ActorPanel.add(textArea_1);
		
		btnNewButton = new JButton("Edit Stats");
		btnNewButton.setEnabled(false);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EntityStatsEditor entityStatsEditor = new EntityStatsEditor(textArea_1.getText());
				textArea_1.setText(entityStatsEditor.getStats());
			}
		});
		btnNewButton.setBounds(201, 38, 89, 23);
		ActorPanel.add(btnNewButton);
		
		lblStats.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				statsOn = !statsOn;
				if(statsOn) {
					btnNewButton.setEnabled(true);
				} else {
					btnNewButton.setEnabled(false);
				}
			}
		});
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setOrientation(SwingConstants.VERTICAL);
		separator_3.setBounds(296, 23, 2, 260);
		ActorPanel.add(separator_3);
		
		lblEquipment = new JCheckBox("Equipment");
		lblEquipment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				equipmentOn = !equipmentOn;
				if(equipmentOn) {
					btnEditEquip.setEnabled(true);
				} else {
					btnEditEquip.setEnabled(false);
				}
			}
		});
		lblEquipment.setBounds(312, 11, 86, 14);
		ActorPanel.add(lblEquipment);

		btnEditEquip = new JButton("Edit Equip");
		btnEditEquip.setEnabled(false);
		btnEditEquip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EntityEquipmentEditor entityEquipmentEditor = new EntityEquipmentEditor(textArea_2.getText());
				textArea_2.setText(entityEquipmentEditor.getEquipment());
			}
		});
		btnEditEquip.setBounds(310, 38, 89, 23);
		ActorPanel.add(btnEditEquip);
		
		textArea_2 = new JTextArea();
		textArea_2.setLineWrap(true);
		textArea_2.setEditable(false);
		textArea_2.setBounds(310, 66, 88, 217);
		ActorPanel.add(textArea_2);
		
		JSeparator separator_4 = new JSeparator();
		separator_4.setOrientation(SwingConstants.VERTICAL);
		separator_4.setBounds(404, 23, 2, 260);
		ActorPanel.add(separator_4);
		
		lblSound = new JCheckBox("Sound");
		lblSound.setBounds(412, 11, 71, 14);
		ActorPanel.add(lblSound);
		
		textField_10 = new JTextField();
		textField_10.setEditable(false);
		textField_10.setBounds(410, 36, 69, 20);
		ActorPanel.add(textField_10);
		textField_10.setColumns(10);
		
		btnSetHit = new JButton("Hit");
		btnSetHit.setEnabled(false);
		btnSetHit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					@SuppressWarnings("serial")
					JFileChooser fileChooser = new JFileChooser() {{ 
						setCurrentDirectory(new File(System.getProperty("resources") + "/sounds"));
						setFileFilter(new FileNameExtensionFilter("Sounds", "ogg"));
						setAcceptAllFileFilterUsed(false);
						setDialogTitle("Add Hit Sound Effect");
						showOpenDialog(null);
					}};
					
					File file = fileChooser.getSelectedFile();
					textField_10.setText(file.getName());
					contentPane.repaint();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnSetHit.setBounds(410, 63, 69, 23);
		ActorPanel.add(btnSetHit);

		lblSound.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				soundOn = !soundOn;
				if(soundOn) {
					btnSetHit.setEnabled(true);
				} else {
					btnSetHit.setEnabled(false);
				}
			}
		});
		
		JPanel PropPanel = new JPanel();
		tabbedPane.addTab("Prop", null, PropPanel, null);
		
		JPanel SpritePanel = new JPanel();
		tabbedPane.addTab("Sprite", null, SpritePanel, null);
		SpritePanel.setLayout(null);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setAutoscrolls(true);
		scrollPane_1.setBounds(10, 11, 350, 272);
		scrollPane_1.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				Rectangle r = new Rectangle(arg0.getX(), arg0.getY(), 1, 1);
				((JScrollPane)arg0.getSource()).scrollRectToVisible(r);
			}
		});
		SpritePanel.add(scrollPane_1);
		
		entitySpritePanel = new EntitySpritePanel(sprite);
		scrollPane_1.setViewportView(entitySpritePanel);
		entitySpritePanel.setLayout(null);
		
		JSeparator separator_6 = new JSeparator();
		separator_6.setOrientation(SwingConstants.VERTICAL);
		separator_6.setBounds(369, 11, 2, 272);
		SpritePanel.add(separator_6);
		
		JLabel lblMaxDirection = new JLabel("MaxDir");
		lblMaxDirection.setBounds(380, 11, 46, 14);
		SpritePanel.add(lblMaxDirection);
		
		textField_11 = new JTextField(sprite.getMaxDir());
		textField_11.setText("1");
		textField_11.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					entitySpritePanel.setMaxDir(Integer.parseInt(textField_11.getText()));
				} catch(NumberFormatException nfe) {
					entitySpritePanel.setMaxDir(1);
				}
				entitySpritePanel.repaint();
			}
		});
		textField_11.setBounds(381, 36, 45, 20);
		SpritePanel.add(textField_11);
		textField_11.setColumns(10);
		
		JLabel lblMaxFrame = new JLabel("MaxFrame");
		lblMaxFrame.setBounds(436, 11, 52, 14);
		SpritePanel.add(lblMaxFrame);
		
		textField_12 = new JTextField(sprite.getMaxFrame());
		textField_12.setText("1");
		textField_12.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					entitySpritePanel.setMaxFrame(Integer.parseInt(textField_12.getText()));
				} catch(NumberFormatException nfe) {
					entitySpritePanel.setMaxFrame(1);
				}
				entitySpritePanel.repaint();
			}
		});
		textField_12.setBounds(436, 36, 45, 20);
		SpritePanel.add(textField_12);
		textField_12.setColumns(10);
		
		JSeparator separator_7 = new JSeparator();
		separator_7.setBounds(381, 67, 98, 2);
		SpritePanel.add(separator_7);
		SpritePanel.repaint();
		
		chckbxSolid = new JCheckBox("Solid");
		chckbxSolid.setBounds(384, 76, 97, 23);
		SpritePanel.add(chckbxSolid);
		
		JLabel lblXOffset = new JLabel("X Offset");
		lblXOffset.setBounds(380, 106, 46, 14);
		SpritePanel.add(lblXOffset);
		
		textField_13 = new JTextField();
		textField_13.setText("0.0");
		textField_13.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					entitySpritePanel.setXOffset(Float.parseFloat(textField_13.getText()));
				} catch(NumberFormatException nfe) {
					entitySpritePanel.setXOffset(0.0f);
				}
				entitySpritePanel.repaint();
			}
		});
		textField_13.setBounds(381, 131, 45, 20);
		SpritePanel.add(textField_13);
		textField_13.setColumns(10);
		
		JLabel lblYOffset = new JLabel("Y Offset");
		lblYOffset.setBounds(436, 106, 46, 14);
		SpritePanel.add(lblYOffset);
		
		textField_14 = new JTextField();
		textField_14.setText("0.0");
		textField_14.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					entitySpritePanel.setYOffset(Float.parseFloat(textField_14.getText()));
				} catch(NumberFormatException nfe) {
					entitySpritePanel.setYOffset(0.0f);
				}
				entitySpritePanel.repaint();
			}
		});
		textField_14.setBounds(436, 131, 46, 20);
		SpritePanel.add(textField_14);
		textField_14.setColumns(10);
		
		JLabel lblWidth = new JLabel("Width");
		lblWidth.setBounds(380, 162, 46, 14);
		SpritePanel.add(lblWidth);
		
		JLabel lblHeight = new JLabel("Height");
		lblHeight.setBounds(436, 162, 46, 14);
		SpritePanel.add(lblHeight);
		
		textField_15 = new JTextField();
		textField_15.setText("1.0");
		textField_15.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					entitySpritePanel.setWidth(Float.parseFloat(textField_15.getText()));
				} catch(NumberFormatException nfe) {
					entitySpritePanel.setWidth(1.0f);
				}
				entitySpritePanel.repaint();
			}
		});
		textField_15.setBounds(381, 187, 45, 20);
		SpritePanel.add(textField_15);
		textField_15.setColumns(10);
		
		textField_16 = new JTextField();
		textField_16.setText("1.0");
		textField_16.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					entitySpritePanel.setHeight(Float.parseFloat(textField_16.getText()));
				} catch(NumberFormatException nfe) {
					entitySpritePanel.setHeight(1.0f);
				}
				entitySpritePanel.repaint();
			}
		});
		textField_16.setBounds(436, 187, 46, 20);
		SpritePanel.add(textField_16);
		textField_16.setColumns(10);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				attributes = null;
				dispose();
			}
		});
		btnCancel.setBounds(395, 324, 89, 23);
		contentPane.add(btnCancel);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(dialogueOn) {
					attributes += "<DIALOGUE>\n";
					attributes += switchA.isSelected() + ";" + switchB.isSelected() + ";" + switchC.isSelected() + ";" + switchD.isSelected() + "\n";
					for(int x = 0; x<listModel.size(); x++)
						attributes += listModel.get(x) + "\n";
					attributes += "<BREAK>\n";
				}
				if(speedOn)
					attributes += "<SPEED>\n" + textField.getText() + "\n<BREAK>\n";
				if(hostilityOn)
					attributes += "<AI>\n" + hostilityCombo.getSelectedItem().toString() + "\n" + alliesField.getText() + "\n" + enemiesField.getText() + "\n<BREAK>\n";
				if(statsOn)
					attributes += "<STATS>\n" + textArea_1.getText() + "\n<BREAK>\n";
				if(equipmentOn)
					attributes += "<EQUIPMENT>\n" + textArea_2.getText() + "\n<BREAK>\n";
				if(soundOn)
					attributes += "<SOUND>\n" + textField_10.getText() + "\n<BREAK>\n";
				
				saveAnimation();
				saveHitbox();
				
				dispose();
			}
		});
		btnSave.setBounds(296, 324, 89, 23);
		contentPane.add(btnSave);
		
		if(edit) {
			tabbedPane.setEnabledAt(0, false);
			tabbedPane.setEnabledAt(1, false);
			tabbedPane.setEnabledAt(2, false);
			tabbedPane.setSelectedIndex(3);
		}
		
		this.loadAttributes(sprite.getAttributes());
		if(listModel.isEmpty())
			listModel.addElement("@");
		
		setModalityType(ModalityType.APPLICATION_MODAL);
		setVisible(true);
	}
	
	public void loadAttributes(String attribs) { 
		String[] temp = attribs.split("\n");
		for(int x = 0; x<temp.length; x++) {
			if(temp[x].matches("<DIALOGUE>")) {
				dialogueOn = true;
				chckbxActions.setSelected(true);
				String[] switches = temp[x+=1].split(";");
				switchA.setEnabled(true);
				switchA.setSelected(Boolean.parseBoolean(switches[0]));
				switchB.setEnabled(true);
				switchB.setSelected(Boolean.parseBoolean(switches[1]));
				switchC.setEnabled(true);
				switchC.setSelected(Boolean.parseBoolean(switches[2]));
				switchD.setEnabled(true);
				switchD.setSelected(Boolean.parseBoolean(switches[3]));
				list.setEnabled(true);
				for(int y = x + 1; !temp[y].matches("<BREAK>"); y++)
					listModel.addElement(temp[y]);
				x += listModel.size();
			} else if(temp[x].matches("<SPEED>")) {
				speedOn = true;
				lblSpeed.setSelected(true);
				textField.setEnabled(true);
				textField.setText(temp[x+=1]);
			} else if(temp[x].matches("<AI>")) {
				hostilityOn = true;
				lblAI.setSelected(true);
				hostilityCombo.setEnabled(true);
				alliesField.setEnabled(true);
				enemiesField.setEnabled(true);
				hostilityCombo.setSelectedItem(Hostility.valueOf(temp[x+=1]));
				alliesField.setText(temp[x+=1]);
				enemiesField.setText(temp[x+=1]);
			} else if(temp[x].matches("<STATS>")) {
				statsOn = true;
				lblStats.setSelected(true);
				btnNewButton.setEnabled(true);
				textArea_1.setText(temp[x+=1]);
				String statsLine = temp[x+=1];
				while(!statsLine.matches("<BREAK>")) {
					textArea_1.setText(textArea_1.getText() + "\n" + statsLine);
					statsLine = temp[x+=1];
				}
			} else if(temp[x].matches("<EQUIPMENT>")) {
				equipmentOn = true;
				lblEquipment.setSelected(true);
				btnEditEquip.setEnabled(true);
				textArea_2.setText(temp[x+=1]);
				String equipLine = temp[x+=1];
				while(!equipLine.matches("<BREAK>")) {
					textArea_2.setText(textArea_2.getText() + "\n" + equipLine);
					equipLine = temp[x+=1];
				}
			} else if(temp[x].matches("<SOUND>")) {
				soundOn = true;
				lblSound.setSelected(true);
				btnSetHit.setEnabled(true);
				textField_10.setText(temp[x+=1]);
			}
		}
		
		textField_11.setText(entitySpritePanel.getMaxDir() + "");
		textField_12.setText(entitySpritePanel.getMaxFrame() + "");
		chckbxSolid.setSelected(entitySpritePanel.isSolid());
		textField_13.setText(entitySpritePanel.getXOffset() + "");
		textField_14.setText(entitySpritePanel.getYOffset() + "");
		textField_15.setText(entitySpritePanel.getSpriteWidth() + "");
		textField_16.setText(entitySpritePanel.getSpriteHeight() + "");
	}
	
	public void saveAnimation() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/database/sprites"));

			String line;
			StringBuilder fileContent = new StringBuilder();
			String newLine = null;
			while((line = reader.readLine()) != null) {
				String[] temp = line.split(";");

				if(temp[0].matches(sprite.getName())) {
					newLine = sprite.getName() + ";" + textField_11.getText() + ";" + textField_12.getText();
					fileContent.append(newLine);
					fileContent.append(System.getProperty("line.separator"));
				} else {
					fileContent.append(line);
					fileContent.append(System.getProperty("line.separator"));
				}
			}
			if(newLine == null) {
				newLine = sprite.getName() + ";" + textField_11.getText() + ";" + textField_12.getText();
				fileContent.append(newLine);
				fileContent.append(System.getProperty("line.separator"));
			}

			BufferedWriter out = new BufferedWriter(new FileWriter(System.getProperty("resources") + "/database/sprites"));
			out.write(fileContent.toString());
			out.close();
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("The sprite database has been misplaced!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Sprite database failed to load!");
			e.printStackTrace();
		}
	}
	
	public void saveHitbox() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/database/entities"));

			String line;
			StringBuilder fileContent = new StringBuilder();
			String newLine = null;
			while((line = reader.readLine()) != null) {
				String[] temp = line.split(";");

				if(temp[0].matches(sprite.getName())) {
					newLine = sprite.getName() + ";" + chckbxSolid.isSelected() + ";" + textField_13.getText() + ";" + textField_14.getText()
							+ ";" + textField_15.getText() + ";" + textField_16.getText();
					fileContent.append(newLine);
					fileContent.append(System.getProperty("line.separator"));
				} else {
					fileContent.append(line);
					fileContent.append(System.getProperty("line.separator"));
				}
			}
			if(newLine == null) {
				newLine = sprite.getName() + ";" + chckbxSolid.isSelected() + ";" + textField_13.getText() + ";" + textField_14.getText()
						+ ";" + textField_15.getText() + ";" + textField_16.getText();
				fileContent.append(newLine);
				fileContent.append(System.getProperty("line.separator"));
			}

			BufferedWriter out = new BufferedWriter(new FileWriter(System.getProperty("resources") + "/database/entities"));
			out.write(fileContent.toString());
			out.close();
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("The entity database has been misplaced!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Entity database failed to load!");
			e.printStackTrace();
		}
	}
	
	public void checkBlockSelect() {
		if(list.getSelectedValue().contains("@Choice:") || list.getSelectedValue().contains("@Condition:")) {
			String prefix = "";
			for(int x = 0; list.getSelectedValue().startsWith(" ", x); x++) {
				prefix += " ";
			}
			list.setSelectionInterval(list.getSelectedIndex(), listModel.indexOf(prefix + ">End", list.getSelectedIndex()));
		}
	}
	
	private void addPopup(int x, int y) {
		JMenuItem item = new JMenuItem();
		
		JPopupMenu menu = new JPopupMenu();
		item = new JMenuItem("Add - Enter");
		if(list.getSelectedIndex() == -1)
			item.setEnabled(false);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(list.getSelectedIndex() != -1) {
					ActionEditor actionEditor = new ActionEditor();
					if(actionEditor.getLine() != null) {
						String[] temp = actionEditor.getLine().split("\n");
						String prefix = "";
						if(list.getSelectedValue().startsWith(" ")) {
							int indent = list.getSelectedValue().indexOf('@');
							for(int x = 0; x<indent; x++)
								prefix += " ";
						}
						for(int x = temp.length - 1; x>=0; x--) {
							listModel.add(list.getSelectedIndex(), prefix + temp[x]);
						}
						list.clearSelection();
					}
				}
			}
		});
		menu.add(item);
		item = new JMenuItem("Copy");
		if(list.getSelectedIndex() == -1 || list.getSelectedValue().matches("@"))
			item.setEnabled(false);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				copyModel.clear();
				for(String line : list.getSelectedValuesList()) {
					copyModel.addElement(line);
				}
			}
		});
		menu.add(item);
		item = new JMenuItem("Cut");
		if(list.getSelectedIndex() == -1 || list.getSelectedValue().matches("@"))
			item.setEnabled(false);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				copyModel.clear();
				while(list.getSelectedIndices().length > 0) {
					if(list.getSelectedIndex() != listModel.size() - 1) {
						copyModel.addElement(list.getSelectedValue());
						listModel.remove(list.getSelectedIndex());
					} else
						break;
				}
			}
		});
		menu.add(item);
		// TODO Realign copied blocks to new alignments
		item = new JMenuItem("Paste");
		if(list.getSelectedIndex() == -1 || list.getSelectedValue().matches("@") || copyModel.isEmpty())
			item.setEnabled(false);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(int x = 0; x < copyModel.size(); x++)
					listModel.add(list.getSelectedIndex() + x, copyModel.get(x));
			}
		});
		menu.add(item);
		item = new JMenuItem("Remove - Delete");
		if(list.getSelectedIndex() == -1 || list.getSelectedValue().trim().matches("@"))
			item.setEnabled(false);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(list.getSelectedIndex() != -1) {
					while(list.getSelectedIndices().length > 0) {
						if(list.getSelectedIndex() != listModel.size() - 1)
							listModel.remove(list.getSelectedIndex());
						else
							break;
					}
				}
			}
		});
		menu.add(item);
		menu.show(list, x, y);
	}
	
	public String getAttributes() {
		return attributes;
	}
}
