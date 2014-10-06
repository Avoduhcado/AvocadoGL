package core.editor.actions;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

public class VariableEditor extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JList<String> list;
	public DefaultListModel<String> listModel;
	private JTextField nameField;
	private JTextField stringField;
	private JSpinner intSpinner;
	private JCheckBox booleanBox;
	private JSpinner floatSpinner;
	private String variable;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton adjustSet;
	private JRadioButton adjustAdd;
	private JRadioButton adjustSubtract;

	/**
	 * Create the dialog.
	 */
	public VariableEditor() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		if(System.getProperty("resources") == null)
			System.setProperty("resources", System.getProperty("user.dir") + "/resources");
		
		setBounds(100, 100, 450, 350);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JSplitPane splitPane = new JSplitPane();
			splitPane.setBounds(0, 0, 434, 277);
			contentPanel.add(splitPane);
			{
				JScrollPane scrollPane = new JScrollPane();
				splitPane.setLeftComponent(scrollPane);
				{
					listModel = new DefaultListModel<String>();
					fillList();
					
					list = new JList<String>(listModel);
					list.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							if(list.getSelectedIndex() != -1 && list.getSelectedValue() != null) {
								enableEdit();
							} else
								disableEdit();
						}
					});
					list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					scrollPane.setViewportView(list);
				}
			}
			{
				JPanel panel = new JPanel();
				splitPane.setRightComponent(panel);
				panel.setLayout(null);
				
				JLabel lblName = new JLabel("Name:");
				lblName.setBounds(10, 11, 46, 14);
				panel.add(lblName);
				
				nameField = new JTextField();
				nameField.setEnabled(false);
				nameField.setBounds(66, 8, 86, 20);
				panel.add(nameField);
				nameField.setColumns(10);
				
				JSeparator separator = new JSeparator();
				separator.setBounds(10, 36, 258, 2);
				panel.add(separator);
				
				JLabel lblString = new JLabel("String:");
				lblString.setBounds(10, 51, 46, 14);
				panel.add(lblString);
				
				stringField = new JTextField();
				stringField.setEnabled(false);
				stringField.setBounds(10, 78, 86, 20);
				panel.add(stringField);
				stringField.setColumns(10);
				
				JLabel lblInteger = new JLabel("Integer:");
				lblInteger.setBounds(106, 51, 46, 14);
				panel.add(lblInteger);
				
				intSpinner = new JSpinner();
				intSpinner.setEnabled(false);
				intSpinner.setBounds(106, 78, 86, 20);
				panel.add(intSpinner);
				
				JLabel lblBoolean = new JLabel("Boolean:");
				lblBoolean.setBounds(10, 109, 46, 14);
				panel.add(lblBoolean);
				
				booleanBox = new JCheckBox("True");
				booleanBox.setEnabled(false);
				booleanBox.setBounds(10, 130, 86, 23);
				panel.add(booleanBox);
				
				JLabel lblFloat = new JLabel("Float:");
				lblFloat.setBounds(106, 109, 46, 14);
				panel.add(lblFloat);
				
				floatSpinner = new JSpinner();
				floatSpinner.setEnabled(false);
				floatSpinner.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
				floatSpinner.setBounds(106, 131, 86, 20);
				panel.add(floatSpinner);
				
				JSeparator separator_1 = new JSeparator();
				separator_1.setBounds(10, 162, 258, 2);
				panel.add(separator_1);
				
				adjustAdd = new JRadioButton("Add");
				buttonGroup.add(adjustAdd);
				adjustAdd.setBounds(106, 173, 119, 25);
				panel.add(adjustAdd);
				
				adjustSet = new JRadioButton("Set");
				buttonGroup.add(adjustSet);
				adjustSet.setBounds(10, 173, 86, 25);
				panel.add(adjustSet);
				
				adjustSubtract = new JRadioButton("Subtract");
				buttonGroup.add(adjustSubtract);
				adjustSubtract.setBounds(106, 203, 119, 25);
				panel.add(adjustSubtract);
			}
			splitPane.setDividerLocation(0.35);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
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
		
		setModalityType(ModalityType.APPLICATION_MODAL);
		setVisible(true);
	}
	
	public void fillList() {
		BufferedReader reader;
		
		try {
			reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/database/variables"));
			
			String line;
			while((line = reader.readLine()) != null) {
				listModel.addElement(line);
			}
			listModel.addElement(null);
			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void enableEdit() {
		disableEdit();
		
		nameField.setEnabled(true);
		nameField.setText(list.getSelectedValue().substring(0, list.getSelectedValue().indexOf(';')));
		switch(list.getSelectedValue().charAt(list.getSelectedValue().indexOf(';') + 1)) {
		case 'S':
			stringField.setEnabled(true);
			stringField.setText(list.getSelectedValue().substring(list.getSelectedValue().lastIndexOf(';') + 1, list.getSelectedValue().length()));
			break;
		case 'I':
			intSpinner.setEnabled(true);
			intSpinner.setValue(Integer.parseInt(list.getSelectedValue().substring(list.getSelectedValue().lastIndexOf(';') + 1, list.getSelectedValue().length())));
			adjustSet.setEnabled(true);
			adjustAdd.setEnabled(true);
			adjustSubtract.setEnabled(true);
			break;
		case 'B':
			booleanBox.setEnabled(true);
			booleanBox.setSelected(Boolean.parseBoolean(list.getSelectedValue().substring(list.getSelectedValue().lastIndexOf(';') + 1, list.getSelectedValue().length())));
			break;
		case 'F':
			floatSpinner.setEnabled(true);
			floatSpinner.setValue(Float.parseFloat(list.getSelectedValue().substring(list.getSelectedValue().lastIndexOf(';') + 1, list.getSelectedValue().length())));
			adjustSet.setEnabled(true);
			adjustAdd.setEnabled(true);
			adjustSubtract.setEnabled(true);
			break;
		}
	}
	
	public void disableEdit() {
		nameField.setText(null);
		nameField.setEnabled(false);
		stringField.setText(null);
		stringField.setEnabled(false);
		intSpinner.setValue(0);
		intSpinner.setEnabled(false);
		booleanBox.setSelected(false);
		booleanBox.setEnabled(false);
		floatSpinner.setValue(0);
		floatSpinner.setEnabled(false);
		buttonGroup.clearSelection();
		adjustSet.setEnabled(false);
		adjustAdd.setEnabled(false);
		adjustSubtract.setEnabled(false);
	}
	
	public String getAdjustType() {
		String adjust = "";
		
		if(adjustSet.isSelected())
			adjust = "0";
		else if(adjustAdd.isSelected())
			adjust = "1";
		else if(adjustSubtract.isSelected())
			adjust = "2";
		
		return adjust;
	}
	
	public void save() {
		variable = "@Variable:" + nameField.getText() + ":";
		if(stringField.isEnabled()) {
			variable += stringField.getText();
		} else if(intSpinner.isEnabled()) {
			variable += getAdjustType() + ":" + intSpinner.getValue();
		} else if(booleanBox.isEnabled()) {
			variable += booleanBox.isSelected();
		} else if(floatSpinner.isEnabled()) {
			variable += getAdjustType() + ":" + floatSpinner.getValue();
		}
	}
	
	public String getVariable() {
		return variable;
	}
}
