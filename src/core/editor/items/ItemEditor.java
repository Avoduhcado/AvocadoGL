package core.editor.items;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import core.entity.projectiles.ProjectileType;
import core.entity.weapons.Weapon;
import core.equipment.Equipment;
import core.items.AmmoItem;
import core.items.ArmorItem;
import core.items.Consumable;
import core.items.Item;
import core.items.PocketItem;
import core.items.WeaponItem;

import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class ItemEditor extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField nameText;
	private JTextField IDText;
	private JCheckBox stackableCheck;
	private JSpinner priceSpinner;
	private JSpinner vigorSpinner;
	private JComboBox<String> wepTypeCombo;
	private JComboBox<String> weaponNameCombo;
	private JSpinner damageSpinner;
	private JSpinner bodySpinner;
	private JSpinner headSpinner;
	private JSpinner slashSpinner;
	private JSpinner magicSpinner;
	private JSpinner crippleSpinner;
	private JSpinner thrustSpinner;
	private JSpinner crushSpinner;
	private JCheckBox usableCheck;
	private JComboBox<String> itemTypeCombo;
	private JCheckBox consumableCheck;
	private JComboBox<String> weaponSlotCombo;
	private JComboBox<String> armorSlotCombo;
	private JSpinner stabilitySpinner;
	private JSpinner skillSpinner;
	private JTextField ammoText;
	private JTextField areaTextureText;
	private JCheckBox piercingCheck;
	private JSpinner ammoDamageSpinner;
	private JSpinner breakSpinner;
	private JCheckBox multiCheck;
	private JCheckBox movingCheck;
	private JSpinner durationSpinner;
	private JSpinner speedSpinner;
	private JComboBox<ProjectileType> ammoTypeCombo;
	private JSpinner diameterSpinner;
	private JSpinner distanceSpinner;
	private JTabbedPane tabbedPane;

	private int ID;
	private JTextArea effectTextArea;
	private JSpinner ammoWeaponSpinner;

	/**
	 * Create the dialog.
	 */
	public ItemEditor(int ID) {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		System.setProperty("resources", System.getProperty("user.dir") + "/resources");
		this.ID = ID;
		setTitle("Item Editor");
		setBounds(100, 100, 600, 500);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setBounds(0, 0, 584, 429);
		contentPanel.add(splitPane);
		
		JPanel panel = new JPanel();
		splitPane.setLeftComponent(panel);
		panel.setLayout(null);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(10, 38, 46, 14);
		panel.add(lblName);
		
		nameText = new JTextField();
		nameText.setBounds(66, 35, 86, 20);
		panel.add(nameText);
		nameText.setColumns(10);
		
		IDText = new JTextField(ID + "");
		IDText.setEditable(false);
		IDText.setBounds(66, 11, 86, 20);
		panel.add(IDText);
		IDText.setColumns(10);
		
		stackableCheck = new JCheckBox("Stackable");
		stackableCheck.setBounds(10, 59, 91, 23);
		panel.add(stackableCheck);
		
		JLabel lblPrice = new JLabel("Price:");
		lblPrice.setBounds(10, 115, 46, 14);
		panel.add(lblPrice);
		
		priceSpinner = new JSpinner();
		priceSpinner.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
		priceSpinner.setBounds(76, 113, 59, 20);
		panel.add(priceSpinner);
		
		usableCheck = new JCheckBox("Usable");
		usableCheck.setBounds(10, 85, 86, 23);
		panel.add(usableCheck);
		
		JLabel lblWeaponType = new JLabel("Weapon Type:");
		lblWeaponType.setBounds(162, 38, 86, 14);
		panel.add(lblWeaponType);
		
		wepTypeCombo = new JComboBox<String>();
		wepTypeCombo.setModel(new DefaultComboBoxModel<String>(new String[] {"Melee", "Magic", "Ranged"}));
		wepTypeCombo.setBounds(258, 34, 86, 22);
		panel.add(wepTypeCombo);
		
		JLabel lblVigorCost = new JLabel("Vigor Cost:");
		lblVigorCost.setBounds(10, 140, 71, 14);
		panel.add(lblVigorCost);
		
		vigorSpinner = new JSpinner();
		vigorSpinner.setModel(new SpinnerNumberModel(new Float(0), new Float(0), null, new Float(1)));
		vigorSpinner.setBounds(76, 138, 59, 20);
		panel.add(vigorSpinner);
		
		JLabel lblWeaponName = new JLabel("Weapon Name:");
		lblWeaponName.setBounds(162, 63, 86, 14);
		panel.add(lblWeaponName);
		
		JLabel lblDamage = new JLabel("Damage:");
		lblDamage.setBounds(10, 165, 53, 14);
		panel.add(lblDamage);
		
		damageSpinner = new JSpinner();
		damageSpinner.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		damageSpinner.setBounds(76, 163, 59, 20);
		panel.add(damageSpinner);
		
		JLabel lblResistance = new JLabel("Resistance:");
		lblResistance.setBounds(354, 38, 71, 14);
		panel.add(lblResistance);
		
		JLabel lblCrush = new JLabel("Crush:");
		lblCrush.setBounds(354, 64, 46, 14);
		panel.add(lblCrush);
		
		crushSpinner = new JSpinner();
		crushSpinner.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		crushSpinner.setBounds(410, 61, 46, 20);
		panel.add(crushSpinner);
		
		JLabel lblThrust = new JLabel("Thrust:");
		lblThrust.setBounds(354, 90, 46, 14);
		panel.add(lblThrust);
		
		thrustSpinner = new JSpinner();
		thrustSpinner.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		thrustSpinner.setBounds(410, 87, 46, 20);
		panel.add(thrustSpinner);
		
		JLabel lblSlash = new JLabel("Slash:");
		lblSlash.setBounds(354, 116, 46, 14);
		panel.add(lblSlash);
		
		slashSpinner = new JSpinner();
		slashSpinner.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		slashSpinner.setBounds(410, 113, 46, 20);
		panel.add(slashSpinner);
		
		JLabel lblMagic = new JLabel("Magic:");
		lblMagic.setBounds(354, 165, 46, 14);
		panel.add(lblMagic);
		
		magicSpinner = new JSpinner();
		magicSpinner.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		magicSpinner.setBounds(410, 163, 46, 20);
		panel.add(magicSpinner);
		
		JLabel lblHead = new JLabel("Head:");
		lblHead.setBounds(466, 64, 46, 14);
		panel.add(lblHead);
		
		headSpinner = new JSpinner();
		headSpinner.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		headSpinner.setBounds(522, 61, 46, 20);
		panel.add(headSpinner);
		
		JLabel lblBody = new JLabel("Body:");
		lblBody.setBounds(466, 90, 46, 14);
		panel.add(lblBody);
		
		bodySpinner = new JSpinner();
		bodySpinner.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		bodySpinner.setBounds(522, 87, 46, 20);
		panel.add(bodySpinner);
		
		crippleSpinner = new JSpinner();
		crippleSpinner.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		crippleSpinner.setBounds(522, 113, 46, 20);
		panel.add(crippleSpinner);
		
		JLabel lblCripple = new JLabel("Cripple:");
		lblCripple.setBounds(466, 116, 46, 14);
		panel.add(lblCripple);
		
		JLabel lblSpells = new JLabel("Spells:");
		lblSpells.setBounds(162, 90, 46, 14);
		panel.add(lblSpells);
		
		JLabel lblId = new JLabel("ID:");
		lblId.setBounds(10, 15, 46, 14);
		panel.add(lblId);
		
		JLabel lblItemType = new JLabel("Item Type:");
		lblItemType.setBounds(164, 15, 84, 14);
		panel.add(lblItemType);
		
		itemTypeCombo = new JComboBox<String>();
		itemTypeCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setEnabledAt(0, false);
				tabbedPane.setEnabledAt(1, false);
				tabbedPane.setEnabledAt(2, false);
				tabbedPane.setEnabledAt(3, false);
				switch(itemTypeCombo.getSelectedIndex()) {
				case(0):
					tabbedPane.setEnabledAt(0, true);
					tabbedPane.setSelectedIndex(0);
					break;
				case(1):
					tabbedPane.setEnabledAt(1, true);
					tabbedPane.setSelectedIndex(1);
					break;
				case(2):
					tabbedPane.setEnabledAt(2, true);
					tabbedPane.setSelectedIndex(2);
					break;
				case(3):
					tabbedPane.setEnabledAt(3, true);
					tabbedPane.setSelectedIndex(3);
					toggleAmmo();
					break;
				}
				updateID();
			}
		});
		itemTypeCombo.setModel(new DefaultComboBoxModel<String>(new String[] {"Pocket", "Armor", "Weapon", "Ammo"}));
		itemTypeCombo.setBounds(258, 10, 86, 22);
		panel.add(itemTypeCombo);
		
		weaponNameCombo = new JComboBox<String>();
		weaponNameCombo.setModel(buildWeaponList());
		weaponNameCombo.setBounds(258, 59, 86, 22);
		panel.add(weaponNameCombo);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		splitPane.setRightComponent(tabbedPane);
		
		JPanel pocketPanel = new JPanel();
		tabbedPane.addTab("Pocket", null, pocketPanel, null);
		pocketPanel.setLayout(null);
		
		consumableCheck = new JCheckBox("Consumable");
		consumableCheck.setBounds(8, 9, 97, 23);
		pocketPanel.add(consumableCheck);
		
		JButton effectButton = new JButton("Add Effect");
		effectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConsumableEditor consumableEditor = new ConsumableEditor(Integer.parseInt(IDText.getText()));
				if(consumableEditor.getEffects() != null)
					effectTextArea.setText(consumableEditor.getEffects());
			}
		});
		effectButton.setBounds(8, 41, 95, 25);
		pocketPanel.add(effectButton);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(8, 73, 213, 75);
		pocketPanel.add(scrollPane);
		
		effectTextArea = new JTextArea();
		scrollPane.setViewportView(effectTextArea);
		tabbedPane.setEnabledAt(0, true);
		
		JPanel armorPanel = new JPanel();
		tabbedPane.addTab("Armor", null, armorPanel, null);
		armorPanel.setLayout(null);
		
		JLabel lblArmorSlot = new JLabel("Armor Slot:");
		lblArmorSlot.setBounds(12, 13, 60, 14);
		armorPanel.add(lblArmorSlot);
		
		armorSlotCombo = new JComboBox<String>();
		armorSlotCombo.setModel(buildSlotList(1));
		armorSlotCombo.setBounds(84, 11, 86, 20);
		armorPanel.add(armorSlotCombo);
		
		JLabel lblStability = new JLabel("Stability:");
		lblStability.setBounds(12, 40, 46, 14);
		armorPanel.add(lblStability);
		
		stabilitySpinner = new JSpinner();
		stabilitySpinner.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		stabilitySpinner.setBounds(84, 38, 39, 20);
		armorPanel.add(stabilitySpinner);
		tabbedPane.setEnabledAt(1, false);
		
		JPanel weaponPanel = new JPanel();
		tabbedPane.addTab("Weapon", null, weaponPanel, null);
		weaponPanel.setLayout(null);
		
		JLabel lblWeaponSlot = new JLabel("Weapon Slot:");
		lblWeaponSlot.setBounds(12, 13, 70, 14);
		weaponPanel.add(lblWeaponSlot);
		
		weaponSlotCombo = new JComboBox<String>();
		weaponSlotCombo.setModel(buildSlotList(2));
		weaponSlotCombo.setBounds(94, 11, 86, 20);
		weaponPanel.add(weaponSlotCombo);
		
		JLabel lblSkill = new JLabel("Skill:");
		lblSkill.setBounds(12, 40, 46, 14);
		weaponPanel.add(lblSkill);
		
		skillSpinner = new JSpinner();
		skillSpinner.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		skillSpinner.setBounds(94, 38, 39, 20);
		weaponPanel.add(skillSpinner);
		tabbedPane.setEnabledAt(2, false);
		
		JPanel ammoPanel = new JPanel();
		tabbedPane.addTab("Ammo", null, ammoPanel, null);
		ammoPanel.setLayout(null);
		
		JLabel lblBreakChance = new JLabel("Break Chance:");
		lblBreakChance.setBounds(8, 41, 75, 14);
		ammoPanel.add(lblBreakChance);
		
		breakSpinner = new JSpinner();
		breakSpinner.setModel(new SpinnerNumberModel(new Float(0), new Float(0), new Float(1), new Float(1)));
		breakSpinner.setBounds(95, 39, 41, 20);
		ammoPanel.add(breakSpinner);
		
		JLabel lblAmmoType = new JLabel("Ammo Type:");
		lblAmmoType.setBounds(8, 68, 75, 14);
		ammoPanel.add(lblAmmoType);
		
		ammoTypeCombo = new JComboBox<ProjectileType>();
		ammoTypeCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleAmmo();
			}
		});
		ammoTypeCombo.setModel(new DefaultComboBoxModel<ProjectileType>(ProjectileType.values()));
		ammoTypeCombo.setBounds(95, 64, 97, 22);
		ammoPanel.add(ammoTypeCombo);
		
		JLabel lblAmmoName = new JLabel("Ammo Name:");
		lblAmmoName.setBounds(8, 95, 75, 14);
		ammoPanel.add(lblAmmoName);
		
		ammoText = new JTextField();
		ammoText.setBounds(95, 93, 86, 20);
		ammoPanel.add(ammoText);
		ammoText.setColumns(10);
		
		JLabel lblSpeed = new JLabel("Speed:");
		lblSpeed.setBounds(8, 122, 46, 14);
		ammoPanel.add(lblSpeed);
		
		speedSpinner = new JSpinner();
		speedSpinner.setModel(new SpinnerNumberModel(new Float(0), new Float(0), null, new Float(1)));
		speedSpinner.setBounds(95, 120, 41, 20);
		ammoPanel.add(speedSpinner);
		
		piercingCheck = new JCheckBox("Piercing");
		piercingCheck.setBounds(8, 145, 97, 23);
		ammoPanel.add(piercingCheck);
		
		JLabel lblDistance = new JLabel("Distance:");
		lblDistance.setBounds(200, 14, 54, 14);
		ammoPanel.add(lblDistance);
		
		distanceSpinner = new JSpinner();
		distanceSpinner.setModel(new SpinnerNumberModel(new Float(0), new Float(0), null, new Float(1)));
		distanceSpinner.setBounds(266, 11, 41, 20);
		ammoPanel.add(distanceSpinner);
		
		JLabel lblDamage_1 = new JLabel("Damage:");
		lblDamage_1.setBounds(200, 42, 54, 14);
		ammoPanel.add(lblDamage_1);
		
		ammoDamageSpinner = new JSpinner();
		ammoDamageSpinner.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		ammoDamageSpinner.setBounds(266, 39, 41, 20);
		ammoPanel.add(ammoDamageSpinner);
		
		JLabel lblRadius = new JLabel("Diameter:");
		lblRadius.setBounds(200, 69, 54, 14);
		ammoPanel.add(lblRadius);
		
		diameterSpinner = new JSpinner();
		diameterSpinner.setModel(new SpinnerNumberModel(new Float(0), new Float(0), null, new Float(1)));
		diameterSpinner.setBounds(266, 66, 41, 20);
		ammoPanel.add(diameterSpinner);
		
		multiCheck = new JCheckBox("Multi");
		multiCheck.setBounds(200, 92, 97, 23);
		ammoPanel.add(multiCheck);
		
		movingCheck = new JCheckBox("Moving");
		movingCheck.setBounds(200, 119, 97, 23);
		ammoPanel.add(movingCheck);
		
		JLabel lblDuration = new JLabel("Duration:");
		lblDuration.setBounds(200, 150, 54, 14);
		ammoPanel.add(lblDuration);
		
		durationSpinner = new JSpinner();
		durationSpinner.setModel(new SpinnerNumberModel(new Float(0), new Float(0), null, new Float(1)));
		durationSpinner.setBounds(266, 147, 41, 20);
		ammoPanel.add(durationSpinner);
		
		JLabel lblAreaTexture = new JLabel("Area Texture:");
		lblAreaTexture.setBounds(319, 14, 69, 14);
		ammoPanel.add(lblAreaTexture);
		
		areaTextureText = new JTextField();
		areaTextureText.setBounds(400, 11, 86, 20);
		ammoPanel.add(areaTextureText);
		areaTextureText.setColumns(10);
		
		JLabel lblWeaponSlot_1 = new JLabel("Weapon Slot:");
		lblWeaponSlot_1.setBounds(8, 14, 75, 14);
		ammoPanel.add(lblWeaponSlot_1);
		
		ammoWeaponSpinner = new JSpinner();
		ammoWeaponSpinner.setModel(new SpinnerNumberModel(0, 0, 7, 1));
		ammoWeaponSpinner.setBounds(95, 12, 41, 20);
		ammoPanel.add(ammoWeaponSpinner);
		tabbedPane.setEnabledAt(3, false);
		splitPane.setDividerLocation(200);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						updateID();
						save();
						
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		if(ID != 0)
			load();
		setModalityType(ModalityType.APPLICATION_MODAL);
		setVisible(true);
	}
	
	public void save() {
		String typeLine = "";
		switch(itemTypeCombo.getSelectedIndex()) {
		case(0):
			typeLine = ";" + consumableCheck.isSelected();
			if(effectTextArea.getText() != null)
				saveConsumable();
			break;
		case(1):
			typeLine = ";" + armorSlotCombo.getSelectedIndex() + ";" + stabilitySpinner.getValue();
			break;
		case(2):
			typeLine = ";" + weaponSlotCombo.getSelectedIndex() + ";" + skillSpinner.getValue();
			break;
		case(3):
			typeLine = ";" + breakSpinner.getValue() + ";" + ammoWeaponSpinner.getValue() + ";" + ammoTypeCombo.getSelectedItem() + ";" + ammoText.getText();
			switch(ammoTypeCombo.getSelectedIndex()) {
			case(0):
				typeLine += ";" + speedSpinner.getValue() + ";" + distanceSpinner.getValue() + ";" + piercingCheck.isSelected() + ";" + ammoDamageSpinner.getValue();
				break;
			case(1):
				typeLine += ";" + speedSpinner.getValue() + ";" + piercingCheck.isSelected() + ";" + ammoDamageSpinner.getValue() + ";" + diameterSpinner.getValue()
					+ ";" + multiCheck.isSelected() + ";" + areaTextureText.getText();
				break;
			case(2):
				typeLine += ";" + ammoDamageSpinner.getValue() + ";" + diameterSpinner.getValue() + ";" + movingCheck.isSelected() + ";" + durationSpinner.getValue()
					+ ";" + areaTextureText.getText();
				break;
			case(3):
				break;
			}
			break;
		}
		
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/database/items"));

			String line;
			int pos = 0;
			StringBuilder fileContent = new StringBuilder();
			String newLine = null;
			while((line = reader.readLine()) != null) {
				String[] temp = line.split(";");

				if(temp[0].matches(IDText.getText())) {
					newLine = IDText.getText() + ";" + nameText.getText() + ";" + stackableCheck.isSelected() + ";" + 1 + ";" + priceSpinner.getValue() + ";"
							+ usableCheck.isSelected() + ";" + wepTypeCombo.getSelectedIndex() + ";" + vigorSpinner.getValue() + ";" + weaponNameCombo.getSelectedItem()
							+ ";" + damageSpinner.getValue() + ";" + crushSpinner.getValue() + ":" + slashSpinner.getValue() + ":" + thrustSpinner.getValue() 
							+ ":" + magicSpinner.getValue() + ":" + headSpinner.getValue() + ":" + bodySpinner.getValue() + ":" + crippleSpinner.getValue() + typeLine;
					fileContent.append(newLine);
					fileContent.append(System.getProperty("line.separator"));
				} else if(Integer.parseInt(IDText.getText()) > pos && Integer.parseInt(IDText.getText()) < Integer.parseInt(temp[0]) && 
						Integer.parseInt("" + IDText.getText().charAt(0)) <= Integer.parseInt("" + temp[0].charAt(0))) {
					newLine = IDText.getText() + ";" + nameText.getText() + ";" + stackableCheck.isSelected() + ";" + 1 + ";" + priceSpinner.getValue() + ";"
							+ usableCheck.isSelected() + ";" + wepTypeCombo.getSelectedIndex() + ";" + vigorSpinner.getValue() + ";" + weaponNameCombo.getSelectedItem()
							+ ";" + damageSpinner.getValue() + ";" + crushSpinner.getValue() + ":" + slashSpinner.getValue() + ":" + thrustSpinner.getValue() 
							+ ":" + magicSpinner.getValue() + ":" + headSpinner.getValue() + ":" + bodySpinner.getValue() + ":" + crippleSpinner.getValue() + typeLine;
					fileContent.append(newLine);
					fileContent.append(System.getProperty("line.separator"));
					fileContent.append(line);
					fileContent.append(System.getProperty("line.separator"));
				} else {
					fileContent.append(line);
					fileContent.append(System.getProperty("line.separator"));
				}
				pos = Integer.parseInt(temp[0]);
			}
			if(newLine == null) {
				newLine = IDText.getText() + ";" + nameText.getText() + ";" + stackableCheck.isSelected() + ";" + 1 + ";" + priceSpinner.getValue() + ";"
						+ usableCheck.isSelected() + ";" + wepTypeCombo.getSelectedIndex() + ";" + vigorSpinner.getValue() + ";" + weaponNameCombo.getSelectedItem()
						+ ";" + damageSpinner.getValue() + ";" + crushSpinner.getValue() + ":" + slashSpinner.getValue() + ":" + thrustSpinner.getValue() 
						+ ":" + magicSpinner.getValue() + ":" + headSpinner.getValue() + ":" + bodySpinner.getValue() + ":" + crippleSpinner.getValue() + typeLine;
				fileContent.append(newLine);
				fileContent.append(System.getProperty("line.separator"));
			}

			BufferedWriter out = new BufferedWriter(new FileWriter(System.getProperty("resources") + "/database/items"));
			out.write(fileContent.toString());
			out.close();
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("The item database has been misplaced!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Item database failed to load!");
			e.printStackTrace();
		}
	}
	
	public void saveConsumable() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/database/consumables"));

			String line;
			int pos = 0;
			StringBuilder fileContent = new StringBuilder();
			String newLine = null;
			while((line = reader.readLine()) != null) {
				String[] temp = line.split(";");

				if(temp[0].matches(IDText.getText())) {
					newLine = IDText.getText() + ";" + effectTextArea.getText();
					fileContent.append(newLine);
					fileContent.append(System.getProperty("line.separator"));
				} else if(Integer.parseInt(IDText.getText()) > pos && Integer.parseInt(IDText.getText()) < Integer.parseInt(temp[0]) && 
						Integer.parseInt("" + IDText.getText().charAt(0)) <= Integer.parseInt("" + temp[0].charAt(0))) {
					newLine = IDText.getText() + ";" + effectTextArea.getText();
					fileContent.append(newLine);
					fileContent.append(System.getProperty("line.separator"));
					fileContent.append(line);
					fileContent.append(System.getProperty("line.separator"));
				} else {
					fileContent.append(line);
					fileContent.append(System.getProperty("line.separator"));
				}
				pos = Integer.parseInt(temp[0]);
			}
			if(newLine == null) {
				newLine = IDText.getText() + ";" + effectTextArea.getText();
				fileContent.append(newLine);
				fileContent.append(System.getProperty("line.separator"));
			}

			BufferedWriter out = new BufferedWriter(new FileWriter(System.getProperty("resources") + "/database/consumables"));
			out.write(fileContent.toString());
			out.close();
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("The consumable database has been misplaced!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Consumable database failed to load!");
			e.printStackTrace();
		}
	}
	
	public void load() {
		Item item = Item.loadItem(ID + "");
		nameText.setText(item.getName());
		stackableCheck.setSelected(item.isStackable());
		usableCheck.setSelected(item.isUsable());
		priceSpinner.setValue(item.getPrice());
		vigorSpinner.setValue(item.getVigorCost());
		damageSpinner.setValue(item.getWeaponDamage());
		if(item instanceof PocketItem)
			itemTypeCombo.setSelectedIndex(0);
		else if(item instanceof ArmorItem)
			itemTypeCombo.setSelectedIndex(1);
		else if(item instanceof WeaponItem)
			itemTypeCombo.setSelectedIndex(2);
		else if(item instanceof AmmoItem)
			itemTypeCombo.setSelectedIndex(3);
		wepTypeCombo.setSelectedIndex(item.getWeaponType());
		weaponNameCombo.setSelectedItem(item.getWeaponName());
		crushSpinner.setValue(item.getResistance().getCrush());
		thrustSpinner.setValue(item.getResistance().getThrust());
		slashSpinner.setValue(item.getResistance().getSlash());
		magicSpinner.setValue(item.getResistance().getMagic());
		headSpinner.setValue(item.getResistance().getHead());
		bodySpinner.setValue(item.getResistance().getBody());
		crippleSpinner.setValue(item.getResistance().getCripple());
		
		tabbedPane.setEnabledAt(0, false);
		switch(itemTypeCombo.getSelectedIndex()) {
		case(0):
			tabbedPane.setEnabledAt(0, true);
			tabbedPane.setSelectedIndex(0);
			consumableCheck.setSelected(item.isConsumable());
			if(item.isConsumable())
				effectTextArea.setText(Consumable.loadConsumable(ID).toString());
			break;
		case(1):
			tabbedPane.setEnabledAt(1, true);
			tabbedPane.setSelectedIndex(1);
			armorSlotCombo.setSelectedIndex(item.getArmorSlot());
			stabilitySpinner.setValue(item.getStability());
			break;
		case(2):
			tabbedPane.setEnabledAt(2, true);
			tabbedPane.setSelectedIndex(2);
			System.out.println(item.getWeaponSlot());
			weaponSlotCombo.setSelectedIndex(item.getWeaponSlot());
			skillSpinner.setValue(item.getSkill());
			break;
		case(3):
			tabbedPane.setEnabledAt(3, true);
			tabbedPane.setSelectedIndex(3);
			ammoWeaponSpinner.setValue(item.getWeaponSlot());
			breakSpinner.setValue(item.getBreakChance());
			ammoTypeCombo.setSelectedItem(item.getAmmoType());
			toggleAmmo();
			switch(ammoTypeCombo.getSelectedIndex()) {
			case(0):
				speedSpinner.setValue(item.getSpeed());
				piercingCheck.setSelected(item.getPiercing());
				distanceSpinner.setValue(item.getDistance());
				ammoDamageSpinner.setValue(item.getDamage());
				break;
			case(1):
				speedSpinner.setValue(item.getSpeed());
				piercingCheck.setSelected(item.getPiercing());
				ammoDamageSpinner.setValue(item.getDamage());
				diameterSpinner.setValue(item.getDiameter());
				multiCheck.setSelected(item.getMulti());
				areaTextureText.setText(item.getAreaTexture());
				break;
			case(2):
				ammoDamageSpinner.setValue(item.getDamage());
				diameterSpinner.setValue(item.getDiameter());
				movingCheck.setSelected(item.getMoving());
				durationSpinner.setValue(item.getDuration());
				areaTextureText.setText(item.getAreaTexture());
				break;
			case(3):
				break;
			}
			break;
		}
		
	}
	
	public void updateID() {
		if(ID == 0) {
			int x = 1;
			while(Item.validID(Integer.parseInt((itemTypeCombo.getSelectedIndex() + 1) + "" + x))) {
				x++;
			}
			IDText.setText((itemTypeCombo.getSelectedIndex() + 1) + "" + x);
		}
	}
	
	public DefaultComboBoxModel<String> buildSlotList(int type) {
		Equipment equipment = new Equipment();
		DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<String>();
		switch(type) {
		case(1):
			for(int x = 0; x<equipment.getArmor().getSlotNames().length; x++) {
				comboBoxModel.addElement(equipment.getArmor().getSlotName(x));
			}
			break;
		case(2):
			for(int x = 0; x<equipment.getWeapons().getSlotNames().length; x++) {
				comboBoxModel.addElement(equipment.getWeapons().getSlotName(x));
			}
			break;
		default:
			System.out.println("No slot of type: " + type);
		}
		
		return comboBoxModel;
	}
	
	public DefaultComboBoxModel<String> buildWeaponList() {
		DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<String>();
		String[] names = Weapon.getWeaponNames().split(";");
		
		for(int x = 0; x<names.length; x++) {
			comboBoxModel.addElement(names[x]);
		}
		
		return comboBoxModel;
	}
	
	public void toggleAmmo() {
		speedSpinner.setEnabled(false);
		piercingCheck.setEnabled(false);
		distanceSpinner.setEnabled(false);
		ammoDamageSpinner.setEnabled(false);
		diameterSpinner.setEnabled(false);
		multiCheck.setEnabled(false);
		areaTextureText.setEnabled(false);
		movingCheck.setEnabled(false);
		durationSpinner.setEnabled(false);
		switch(ammoTypeCombo.getSelectedIndex()) {
		case(0):
			speedSpinner.setEnabled(true);
			piercingCheck.setEnabled(true);
			distanceSpinner.setEnabled(true);
			ammoDamageSpinner.setEnabled(true);
			break;
		case(1):
			speedSpinner.setEnabled(true);
			piercingCheck.setEnabled(true);
			ammoDamageSpinner.setEnabled(true);
			diameterSpinner.setEnabled(true);
			multiCheck.setEnabled(true);
			areaTextureText.setEnabled(true);
			break;
		case(2):
			ammoDamageSpinner.setEnabled(true);
			diameterSpinner.setEnabled(true);
			movingCheck.setEnabled(true);
			durationSpinner.setEnabled(true);
			areaTextureText.setEnabled(true);
			break;
		case(3):
			break;
		}
	}
}
