package core.editor.equipment;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import core.entity.projectiles.ProjectileType;
import core.equipment.spells.Spell;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JSeparator;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SpellEditor extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField IDText;
	private JTextField nameText;
	private JTextField areaTextureText;
	private JCheckBox multiCheck;
	private JSpinner durationSpinner;
	private JSpinner damageSpinner;
	private JComboBox<ProjectileType> spellTypeCombo;
	private JCheckBox piercingCheck;
	private JSpinner distanceSpinner;
	private JSpinner costSpinner;
	private JCheckBox movingCheck;
	private JSpinner speedSpinner;
	private JSpinner diameterSpinner;
	
	private int ID;

	/**
	 * Create the dialog.
	 */
	public SpellEditor(int ID) {
		this.ID = ID;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Spell Editor");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblId = new JLabel("ID:");
			lblId.setBounds(12, 13, 46, 14);
			contentPanel.add(lblId);
		}
		{
			IDText = new JTextField(ID + "");
			IDText.setEditable(false);
			IDText.setBounds(70, 11, 86, 20);
			contentPanel.add(IDText);
			IDText.setColumns(10);
		}
		{
			JLabel lblName = new JLabel("Name:");
			lblName.setBounds(12, 40, 46, 14);
			contentPanel.add(lblName);
		}
		{
			nameText = new JTextField();
			nameText.setBounds(70, 38, 86, 20);
			contentPanel.add(nameText);
			nameText.setColumns(10);
		}
		{
			JLabel lblSpellType = new JLabel("Spell Type:");
			lblSpellType.setBounds(168, 14, 57, 14);
			contentPanel.add(lblSpellType);
		}
		{
			spellTypeCombo = new JComboBox<ProjectileType>();
			spellTypeCombo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					toggleType();
				}
			});
			spellTypeCombo.setModel(new DefaultComboBoxModel<ProjectileType>(ProjectileType.values()));
			spellTypeCombo.setBounds(237, 9, 86, 22);
			contentPanel.add(spellTypeCombo);
		}
		{
			JLabel lblCost = new JLabel("Cost:");
			lblCost.setBounds(168, 41, 46, 14);
			contentPanel.add(lblCost);
		}
		{
			costSpinner = new JSpinner();
			costSpinner.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
			costSpinner.setBounds(237, 38, 57, 20);
			contentPanel.add(costSpinner);
		}
		{
			JSeparator separator = new JSeparator();
			separator.setBounds(12, 67, 410, 2);
			contentPanel.add(separator);
		}
		{
			JLabel lblSpeed = new JLabel("Speed:");
			lblSpeed.setBounds(12, 82, 46, 14);
			contentPanel.add(lblSpeed);
		}
		{
			speedSpinner = new JSpinner();
			speedSpinner.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
			speedSpinner.setBounds(70, 80, 57, 20);
			contentPanel.add(speedSpinner);
		}
		{
			piercingCheck = new JCheckBox("Piercing");
			piercingCheck.setBounds(12, 105, 97, 23);
			contentPanel.add(piercingCheck);
		}
		{
			JLabel lblDistance = new JLabel("Distance:");
			lblDistance.setBounds(12, 137, 57, 14);
			contentPanel.add(lblDistance);
		}
		{
			distanceSpinner = new JSpinner();
			distanceSpinner.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
			distanceSpinner.setBounds(70, 135, 57, 20);
			contentPanel.add(distanceSpinner);
		}
		{
			JLabel lblDamage = new JLabel("Damage:");
			lblDamage.setBounds(12, 164, 57, 14);
			contentPanel.add(lblDamage);
		}
		{
			damageSpinner = new JSpinner();
			damageSpinner.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
			damageSpinner.setBounds(70, 162, 57, 20);
			contentPanel.add(damageSpinner);
		}
		{
			JLabel lblRadius = new JLabel("Diameter:");
			lblRadius.setBounds(12, 191, 57, 14);
			contentPanel.add(lblRadius);
		}
		{
			diameterSpinner = new JSpinner();
			diameterSpinner.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
			diameterSpinner.setBounds(70, 189, 57, 20);
			contentPanel.add(diameterSpinner);
		}
		{
			multiCheck = new JCheckBox("Multi");
			multiCheck.setBounds(168, 78, 97, 23);
			contentPanel.add(multiCheck);
		}
		{
			movingCheck = new JCheckBox("Moving");
			movingCheck.setBounds(168, 106, 97, 23);
			contentPanel.add(movingCheck);
		}
		{
			JLabel lblDuration = new JLabel("Duration:");
			lblDuration.setBounds(168, 138, 57, 14);
			contentPanel.add(lblDuration);
		}
		{
			durationSpinner = new JSpinner();
			durationSpinner.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
			durationSpinner.setBounds(237, 135, 57, 20);
			contentPanel.add(durationSpinner);
		}
		{
			JLabel lblAreaTexture = new JLabel("Area Texture:");
			lblAreaTexture.setBounds(168, 165, 69, 14);
			contentPanel.add(lblAreaTexture);
		}
		{
			areaTextureText = new JTextField();
			areaTextureText.setBounds(237, 162, 86, 20);
			contentPanel.add(areaTextureText);
			areaTextureText.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
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
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		if(ID != 0)
			load();
		else {
			updateID();
			toggleType();
		}
		setModalityType(ModalityType.APPLICATION_MODAL);
		setVisible(true);
	}
	
	public void save() {
		String typeLine = "";
		switch(spellTypeCombo.getSelectedIndex()) {
		case(0):
			typeLine = ";" + speedSpinner.getValue() + ";" + piercingCheck.isSelected() + ";" + distanceSpinner.getValue() + ";" + damageSpinner.getValue();
			break;
		case(1):
			typeLine = ";" + speedSpinner.getValue() + ";" + piercingCheck.isSelected() + ";" + damageSpinner.getValue() + ";" + diameterSpinner.getValue()
				+ ";" + multiCheck.isSelected() + ";" + areaTextureText.getText();
			break;
		case(2):
			typeLine = ";" + damageSpinner.getValue() + ";" + diameterSpinner.getValue() + ";" + movingCheck.isSelected() + ";" + durationSpinner.getValue()
				+ ";" + areaTextureText.getText();
			break;
		case(3):
			break;
		}
		
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/database/spells"));

			String line;
			int pos = 0;
			StringBuilder fileContent = new StringBuilder();
			String newLine = null;
			while((line = reader.readLine()) != null) {
				String[] temp = line.split(";");

				if(temp[0].matches(IDText.getText())) {
					newLine = IDText.getText() + ";" + spellTypeCombo.getSelectedItem() + ";" + nameText.getText() + ";" + costSpinner.getValue() + typeLine;
					fileContent.append(newLine);
					fileContent.append(System.getProperty("line.separator"));
				} else if(Integer.parseInt(IDText.getText()) > pos && Integer.parseInt(IDText.getText()) < Integer.parseInt(temp[0]) && 
						Integer.parseInt("" + IDText.getText().charAt(0)) <= Integer.parseInt("" + temp[0].charAt(0))) {
					newLine = IDText.getText() + ";" + spellTypeCombo.getSelectedItem() + ";" + nameText.getText() + ";" + costSpinner.getValue() + typeLine;
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
				newLine = IDText.getText() + ";" + spellTypeCombo.getSelectedItem() + ";" + nameText.getText() + ";" + costSpinner.getValue() + typeLine;
				fileContent.append(newLine);
				fileContent.append(System.getProperty("line.separator"));
			}

			BufferedWriter out = new BufferedWriter(new FileWriter(System.getProperty("resources") + "/database/spells"));
			out.write(fileContent.toString());
			out.close();
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("The spell database has been misplaced!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Spell database failed to load!");
			e.printStackTrace();
		}
	}
	
	public void load() {
		Spell spell = Spell.loadSpell(ID + "");
		nameText.setText(spell.getName());
		spellTypeCombo.setSelectedItem(spell.getSpellType());
		costSpinner.setValue(spell.getCost());
		
		toggleType();
		switch(spellTypeCombo.getSelectedIndex()) {
		case(0):
			speedSpinner.setValue(spell.getSpeed());
			piercingCheck.setSelected(spell.getPiercing());
			distanceSpinner.setValue(spell.getDistance());
			damageSpinner.setValue(spell.getDamage());
			break;
		case(1):
			speedSpinner.setValue(spell.getSpeed());
			piercingCheck.setSelected(spell.getPiercing());
			damageSpinner.setValue(spell.getDamage());
			diameterSpinner.setValue(spell.getDiameter());
			multiCheck.setSelected(spell.getMulti());
			areaTextureText.setText(spell.getAreaTexture());
			break;
		case(2):
			damageSpinner.setValue(spell.getDamage());
			diameterSpinner.setValue(spell.getDiameter());
			movingCheck.setSelected(spell.getMoving());
			durationSpinner.setValue(spell.getDuration());
			areaTextureText.setText(spell.getAreaTexture());
			break;
		case(3):
			break;
		}
	}

	public void updateID() {
		if(ID == 0) {
			int x = 1;
			while(Spell.validID(x)) {
				x++;
			}
			IDText.setText(x + "");
		}
	}
	
	public void toggleType() {
		speedSpinner.setEnabled(false);
		piercingCheck.setEnabled(false);
		distanceSpinner.setEnabled(false);
		damageSpinner.setEnabled(false);
		diameterSpinner.setEnabled(false);
		multiCheck.setEnabled(false);
		movingCheck.setEnabled(false);
		durationSpinner.setEnabled(false);
		areaTextureText.setEnabled(false);
		
		switch(spellTypeCombo.getSelectedIndex()) {
		case(0):
			speedSpinner.setEnabled(true);
			piercingCheck.setEnabled(true);
			distanceSpinner.setEnabled(true);
			damageSpinner.setEnabled(true);
			break;
		case(1):
			speedSpinner.setEnabled(true);
			piercingCheck.setEnabled(true);
			damageSpinner.setEnabled(true);
			diameterSpinner.setEnabled(true);
			multiCheck.setEnabled(true);
			areaTextureText.setEnabled(true);
			break;
		case(2):
			damageSpinner.setEnabled(true);
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
