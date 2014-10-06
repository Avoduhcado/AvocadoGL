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
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.SpinnerNumberModel;

import core.editor.map.MapPanel;
import javax.swing.JSeparator;

public class ConditionalEditor extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private String condition;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rdbtnA;
	private JRadioButton rdbtnC;
	private JRadioButton rdbtnB;
	private JRadioButton rdbtnD;
	private boolean selfSwitch;
	private boolean variable;
	private boolean equipment;
	private JTabbedPane tabbedPane;
	private JTextField matchText;
	private JSpinner lessThanSpinner;
	private JSpinner equalToSpinner;
	private JSpinner greaterSpinner;
	private JComboBox<String> variableCombo;
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();
	private JRadioButton equalSpinner;
	private JRadioButton lblGreaterThan;
	private JRadioButton lessSpinner;
	private final ButtonGroup buttonGroup_2 = new ButtonGroup();
	private JSpinner goldGreaterSpinner;
	private JSpinner goldEqualSpinner;
	private JSpinner goldLessSpinner;
	private JRadioButton goldEqual;
	private JRadioButton goldGreater;
	private JRadioButton goldLess;
	private JRadioButton goldRadio;
	private final ButtonGroup buttonGroup_3 = new ButtonGroup();
	private JRadioButton equipmentActivator;
	private JComboBox<String> selectBox;
	private JRadioButton equipmentSelect;
	private JRadioButton equipmentSource;
	
	/**
	 * Create the dialog.
	 */
	public ConditionalEditor() {
		setTitle("Conditional Editor");
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				switch(tabbedPane.getSelectedIndex()) {
				case(0):
					selfSwitch = true;
					variable = false;
					equipment = false;
					break;
				case(1):
					selfSwitch = false;
					variable = true;
					equipment = false;
					break;
				case(2):
					selfSwitch = false;
					variable = false;
					equipment = true;
					break;
				}
			}
		});
		tabbedPane.setBounds(0, 0, 434, 227);
		contentPanel.add(tabbedPane);
		
		JPanel selfSwitchTab = new JPanel();
		tabbedPane.addTab("Self Switch", null, selfSwitchTab, null);
		selfSwitchTab.setLayout(null);
		
		JLabel lblIf = new JLabel("If");
		lblIf.setBounds(12, 13, 46, 14);
		selfSwitchTab.add(lblIf);
		
		rdbtnA = new JRadioButton("A");
		rdbtnA.setSelected(true);
		buttonGroup.add(rdbtnA);
		rdbtnA.setBounds(22, 36, 50, 23);
		selfSwitchTab.add(rdbtnA);
		
		rdbtnB = new JRadioButton("B");
		buttonGroup.add(rdbtnB);
		rdbtnB.setBounds(76, 36, 50, 23);
		selfSwitchTab.add(rdbtnB);
		
		rdbtnC = new JRadioButton("C");
		buttonGroup.add(rdbtnC);
		rdbtnC.setBounds(130, 36, 50, 23);
		selfSwitchTab.add(rdbtnC);
		
		rdbtnD = new JRadioButton("D");
		buttonGroup.add(rdbtnD);
		rdbtnD.setBounds(184, 36, 50, 23);
		selfSwitchTab.add(rdbtnD);
		
		JLabel lblIsTrue = new JLabel("Is true");
		lblIsTrue.setBounds(12, 68, 46, 14);
		selfSwitchTab.add(lblIsTrue);
		
		JPanel variableTab = new JPanel();
		tabbedPane.addTab("Variable", null, variableTab, null);
		variableTab.setLayout(null);
		
		JLabel lblIf_1 = new JLabel("If");
		lblIf_1.setBounds(12, 13, 46, 14);
		variableTab.add(lblIf_1);
		
		variableCombo = new JComboBox<String>();
		variableCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				greaterSpinner.setEnabled(false);
				lessThanSpinner.setEnabled(false);
				equalToSpinner.setEnabled(false);
				matchText.setEnabled(false);
				
				char type = ((String)variableCombo.getSelectedItem()).charAt(((String)variableCombo.getSelectedItem()).indexOf(';') + 1);
				switch(type) {
				case 'I':
					greaterSpinner.setEnabled(true);
					greaterSpinner.setModel(new SpinnerNumberModel(new Integer(0), null, null, new Integer(1)));
					lessThanSpinner.setEnabled(true);
					lessThanSpinner.setModel(new SpinnerNumberModel(new Integer(0), null, null, new Integer(1)));
					equalToSpinner.setEnabled(true);
					equalToSpinner.setModel(new SpinnerNumberModel(new Integer(0), null, null, new Integer(1)));
					break;
				case 'B':
					break;
				case 'F':
					greaterSpinner.setEnabled(true);
					greaterSpinner.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
					lessThanSpinner.setEnabled(true);
					lessThanSpinner.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
					equalToSpinner.setEnabled(true);
					equalToSpinner.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
					break;
				case 'S':
					matchText.setEnabled(true);
					break;
				}
			}
		});
		variableCombo.setModel(buildVariableModel());
		variableCombo.setBounds(22, 40, 115, 22);
		variableTab.add(variableCombo);
		
		lblGreaterThan = new JRadioButton(">");
		lblGreaterThan.setSelected(true);
		buttonGroup_1.add(lblGreaterThan);
		lblGreaterThan.setBounds(32, 75, 46, 14);
		variableTab.add(lblGreaterThan);
		
		greaterSpinner = new JSpinner();
		greaterSpinner.setModel(new SpinnerNumberModel(new Integer(0), null, null, new Integer(1)));
		greaterSpinner.setEnabled(false);
		greaterSpinner.setBounds(91, 73, 46, 20);
		variableTab.add(greaterSpinner);
		
		lessSpinner = new JRadioButton("<");
		buttonGroup_1.add(lessSpinner);
		lessSpinner.setBounds(145, 75, 56, 14);
		variableTab.add(lessSpinner);
		
		lessThanSpinner = new JSpinner();
		lessThanSpinner.setEnabled(false);
		lessThanSpinner.setBounds(204, 73, 46, 20);
		variableTab.add(lessThanSpinner);
		
		equalSpinner = new JRadioButton("==");
		buttonGroup_1.add(equalSpinner);
		equalSpinner.setBounds(258, 75, 46, 14);
		variableTab.add(equalSpinner);
		
		equalToSpinner = new JSpinner();
		equalToSpinner.setEnabled(false);
		equalToSpinner.setBounds(314, 73, 46, 20);
		variableTab.add(equalToSpinner);
		
		JLabel lblMatches = new JLabel("Matches");
		lblMatches.setBounds(42, 110, 46, 14);
		variableTab.add(lblMatches);
		
		matchText = new JTextField();
		matchText.setEnabled(false);
		matchText.setBounds(107, 106, 116, 22);
		variableTab.add(matchText);
		matchText.setColumns(10);
		
		JPanel equipmentTab = new JPanel();
		tabbedPane.addTab("Equipment", null, equipmentTab, null);
		equipmentTab.setLayout(null);
		
		goldRadio = new JRadioButton("Gold");
		goldRadio.setSelected(true);
		goldRadio.setBounds(6, 50, 109, 23);
		equipmentTab.add(goldRadio);
		
		goldGreater = new JRadioButton(">");
		goldGreater.setSelected(true);
		buttonGroup_2.add(goldGreater);
		goldGreater.setBounds(16, 76, 33, 23);
		equipmentTab.add(goldGreater);
		
		goldGreaterSpinner = new JSpinner();
		goldGreaterSpinner.setBounds(55, 77, 42, 20);
		equipmentTab.add(goldGreaterSpinner);
		
		goldLess = new JRadioButton("<");
		buttonGroup_2.add(goldLess);
		goldLess.setBounds(105, 76, 33, 23);
		equipmentTab.add(goldLess);
		
		goldLessSpinner = new JSpinner();
		goldLessSpinner.setBounds(144, 77, 42, 20);
		equipmentTab.add(goldLessSpinner);
		
		goldEqual = new JRadioButton("=");
		buttonGroup_2.add(goldEqual);
		goldEqual.setBounds(192, 76, 33, 23);
		equipmentTab.add(goldEqual);
		
		goldEqualSpinner = new JSpinner();
		goldEqualSpinner.setBounds(231, 77, 42, 20);
		equipmentTab.add(goldEqualSpinner);
		
		equipmentActivator = new JRadioButton("Activator");
		buttonGroup_3.add(equipmentActivator);
		equipmentActivator.setSelected(true);
		equipmentActivator.setBounds(6, 11, 69, 23);
		equipmentTab.add(equipmentActivator);
		
		equipmentSource = new JRadioButton("Source");
		buttonGroup_3.add(equipmentSource);
		equipmentSource.setBounds(77, 11, 69, 23);
		equipmentTab.add(equipmentSource);
		
		equipmentSelect = new JRadioButton("Select...");
		buttonGroup_3.add(equipmentSelect);
		equipmentSelect.setBounds(144, 11, 69, 23);
		equipmentTab.add(equipmentSelect);
		
		selectBox = new JComboBox<String>();
		selectBox.setModel(buildSelectCombo());
		selectBox.setBounds(219, 11, 109, 22);
		equipmentTab.add(selectBox);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(6, 41, 413, 2);
		equipmentTab.add(separator);
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
	
	public DefaultComboBoxModel<String> buildVariableModel() {
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
		BufferedReader reader;
		
		try {
			reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/database/variables"));
			
			String line;
			while((line = reader.readLine()) != null) {
				model.addElement(line);
			}
			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return model;
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
	
	public void save() {
		condition = "@Condition:";
		
		if(selfSwitch) {
			condition += "Switch:";
			if(rdbtnA.isSelected())
				condition += "a";
			else if(rdbtnB.isSelected())
				condition += "b";
			else if(rdbtnC.isSelected())
				condition += "c";
			else if(rdbtnD.isSelected())
				condition += "d";
		} else if(variable) {
			condition += "Variable:";
			String temp = (String) variableCombo.getSelectedItem();
			condition += temp.substring(0, temp.indexOf(';') + 2) + ":";
			
			switch(temp.charAt(temp.indexOf(';') + 1)) {
			case 'I':
				if(lblGreaterThan.isSelected()) {
					condition += ">" + greaterSpinner.getValue();
				} else if(lessSpinner.isSelected()) {
					condition += "<" + lessThanSpinner.getValue();
				} else if(equalSpinner.isSelected()) {
					condition += "=" + equalToSpinner.getValue();
				}
				break;
			case 'S':
				condition += matchText.getText();
				break;
			case 'F':
				if(lblGreaterThan.isSelected()) {
					condition += ">" + greaterSpinner.getValue();
				} else if(lessSpinner.isSelected()) {
					condition += "<" + lessThanSpinner.getValue();
				} else if(equalSpinner.isSelected()) {
					condition += "=" + equalToSpinner.getValue();
				}
				break;
			}
		} else if(equipment) {
			condition += "Equipment:";
			
			if(equipmentActivator.isSelected()) {
				condition += "activator:";
			} else if(equipmentSource.isSelected()) {
				condition += "source:";
			} else if(equipmentSelect.isSelected()) {
				condition += selectBox.getSelectedItem() + ":";
			}
			
			if(goldRadio.isSelected()) {
				condition += "Gold:";
				if(goldGreater.isSelected()) {
					condition += ">" + goldGreaterSpinner.getValue();
				} else if(goldLess.isSelected()) {
					condition += "<" + goldLessSpinner.getValue();
				} else if(goldEqual.isSelected()) {
					condition += "=" + goldEqualSpinner.getValue();
				}
			}
		}
		
		condition += "\n>True\n @\n>False\n @\n>End";
	}

	public String getCondition() {
		return condition;
	}
}
