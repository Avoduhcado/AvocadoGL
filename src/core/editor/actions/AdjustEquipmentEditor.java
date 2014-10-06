package core.editor.actions;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JSeparator;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;

import core.editor.entities.EntityEquipmentEditor;
import core.editor.map.MapPanel;

public class AdjustEquipmentEditor extends JDialog {

	// TODO Add Quiver and Spellbook adjustments, Pocket size?
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();
	private JRadioButton selectButton;
	private JRadioButton sourceButton;
	private JComboBox<String> selectCombo;
	private JRadioButton activatorButton;
	private final ButtonGroup buttonGroup_2 = new ButtonGroup();
	private JRadioButton pocketButton;
	private JComboBox<String> weaponCombo;
	private JRadioButton weaponButton;
	private JComboBox<String> pocketCombo;
	private JComboBox<String> armorCombo;
	private JRadioButton armorButton;
	private String adjust;
	private JRadioButton giveButton;
	private JRadioButton takeButton;

	/**
	 * Create the dialog.
	 */
	public AdjustEquipmentEditor() {
		setTitle("Adjust Equipment");
		setBounds(100, 100, 500, 300);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			giveButton = new JRadioButton("Give");
			giveButton.setSelected(true);
			buttonGroup.add(giveButton);
			giveButton.setBounds(8, 9, 60, 23);
			contentPanel.add(giveButton);
		}
		{
			takeButton = new JRadioButton("Take");
			buttonGroup.add(takeButton);
			takeButton.setBounds(70, 9, 60, 23);
			contentPanel.add(takeButton);
		}
		
		JSeparator separator = new JSeparator();
		separator.setBounds(8, 39, 464, 2);
		contentPanel.add(separator);
		
		activatorButton = new JRadioButton("Activator");
		buttonGroup_1.add(activatorButton);
		activatorButton.setSelected(true);
		activatorButton.setBounds(146, 9, 69, 23);
		contentPanel.add(activatorButton);
		
		sourceButton = new JRadioButton("Source");
		buttonGroup_1.add(sourceButton);
		sourceButton.setBounds(217, 9, 69, 23);
		contentPanel.add(sourceButton);
		
		selectButton = new JRadioButton("Select...");
		buttonGroup_1.add(selectButton);
		selectButton.setBounds(288, 9, 69, 23);
		contentPanel.add(selectButton);
		
		selectCombo = new JComboBox<String>();
		selectCombo.setModel(buildSelectCombo());
		selectCombo.setBounds(363, 9, 109, 22);
		contentPanel.add(selectCombo);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setBounds(138, 9, 2, 23);
		contentPanel.add(separator_1);
		
		armorCombo = new JComboBox<String>();
		armorCombo.setModel(buildArmorCombo());
		armorCombo.setBounds(8, 83, 109, 20);
		contentPanel.add(armorCombo);
		
		armorButton = new JRadioButton("Equip Armor");
		armorButton.setSelected(true);
		buttonGroup_2.add(armorButton);
		armorButton.setBounds(8, 52, 109, 23);
		contentPanel.add(armorButton);
		
		weaponButton = new JRadioButton("Equip Weapon");
		buttonGroup_2.add(weaponButton);
		weaponButton.setBounds(146, 52, 109, 23);
		contentPanel.add(weaponButton);
		
		pocketButton = new JRadioButton("Add to Pocket");
		buttonGroup_2.add(pocketButton);
		pocketButton.setBounds(288, 52, 109, 23);
		contentPanel.add(pocketButton);
		
		weaponCombo = new JComboBox<String>();
		weaponCombo.setModel(buildWeaponCombo());
		weaponCombo.setBounds(146, 84, 109, 20);
		contentPanel.add(weaponCombo);
		
		pocketCombo = new JComboBox<String>();
		pocketCombo.setModel(buildPocketCombo());
		pocketCombo.setBounds(288, 84, 109, 20);
		contentPanel.add(pocketCombo);
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
		
		setModalityType(ModalityType.APPLICATION_MODAL);
		setVisible(true);
	}
	
	public DefaultComboBoxModel<String> buildSelectCombo() {
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
		if(!MapPanel.actors.isEmpty()) {
			for(int x = 0; x<MapPanel.actors.size(); x++) {
				model.addElement(MapPanel.actors.get(x).getID());
			}
		}
		
		return model;
	}
	
	public DefaultComboBoxModel<String> buildPocketCombo() {
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
		for(int x = 0; x<EntityEquipmentEditor.itemDatabase.size(); x++) {
			String[] temp = EntityEquipmentEditor.itemDatabase.get(x).split(";");
			if(temp[0].startsWith("1"))
				model.addElement(temp[0] + ": " + temp[1]);
		}
		
		return model;
	}
	
	public DefaultComboBoxModel<String> buildArmorCombo() {
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
		for(int x = 0; x<EntityEquipmentEditor.itemDatabase.size(); x++) {
			String[] temp = EntityEquipmentEditor.itemDatabase.get(x).split(";");
			if(temp[0].startsWith("2"))
				model.addElement(temp[0] + ": " + temp[1]);
		}
		
		return model;
	}
	
	public DefaultComboBoxModel<String> buildWeaponCombo() {
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
		for(int x = 0; x<EntityEquipmentEditor.itemDatabase.size(); x++) {
			String[] temp = EntityEquipmentEditor.itemDatabase.get(x).split(";");
			if(Integer.parseInt(temp[0].substring(0, 1)) > 0)
				model.addElement(temp[0] + ": " + temp[1]);
		}
		
		return model;
	}
	
	public void save() {
		adjust = "@Adjust Equipment:";
		if(activatorButton.isSelected()) {
			adjust += "activator:";
		} else if(sourceButton.isSelected()) {
			adjust += "source:";
		} else if(selectButton.isSelected()) {
			adjust += selectCombo.getSelectedItem() + ":";
		}
		
		if(giveButton.isSelected()) {
			adjust += "give:";
		} else if(takeButton.isSelected()) {
			adjust += "take:";
		}
		
		if(armorButton.isSelected()) {
			adjust += "armor:" + ((String)armorCombo.getSelectedItem()).split(":")[0];
		} else if(weaponButton.isSelected()) {
			adjust += "weapon:" + ((String)weaponCombo.getSelectedItem()).split(":")[0];
		} else if(pocketButton.isSelected()) {
			adjust += "pocket:" + ((String)pocketCombo.getSelectedItem()).split(":")[0];
		}
	}

	public String getAdjust() {
		return adjust;
	}
}
