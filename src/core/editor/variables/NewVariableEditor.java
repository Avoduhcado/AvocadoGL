package core.editor.variables;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

public class NewVariableEditor extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField nameField;
	private JTextField textField_1;
	private JRadioButton rdbtnString;
	private JRadioButton rdbtnInteger;
	private JSpinner spinner;
	private JRadioButton rdbtnBoolean;
	private JCheckBox chckbxTrue;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rdbtnFloat;
	private JSpinner spinner_1;
	
	private String var;

	/**
	 * Create the dialog.
	 */
	public NewVariableEditor() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Create new variable");
		setBounds(100, 100, 450, 235);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(10, 11, 46, 14);
		contentPanel.add(lblName);
		
		nameField = new JTextField();
		nameField.setBounds(10, 36, 86, 20);
		contentPanel.add(nameField);
		nameField.setColumns(10);
		
		rdbtnString = new JRadioButton("String");
		rdbtnString.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(rdbtnString.isSelected()) {
					spinner.setEnabled(false);
					textField_1.setEnabled(true);
					chckbxTrue.setEnabled(false);
					spinner_1.setEnabled(false);
				} else
					textField_1.setEnabled(false);
			}
		});
		buttonGroup.add(rdbtnString);
		rdbtnString.setSelected(true);
		rdbtnString.setBounds(10, 63, 97, 23);
		contentPanel.add(rdbtnString);
		
		JLabel lblValue = new JLabel("Value:");
		lblValue.setBounds(10, 93, 46, 14);
		contentPanel.add(lblValue);
		
		textField_1 = new JTextField();
		textField_1.setBounds(10, 118, 86, 20);
		contentPanel.add(textField_1);
		textField_1.setColumns(10);
		
		rdbtnInteger = new JRadioButton("Integer");
		rdbtnInteger.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(rdbtnInteger.isSelected()) {
					spinner.setEnabled(true);
					textField_1.setEnabled(false);
					chckbxTrue.setEnabled(false);
					spinner_1.setEnabled(false);
				} else
					spinner.setEnabled(false);
			}
		});
		buttonGroup.add(rdbtnInteger);
		rdbtnInteger.setBounds(109, 63, 97, 23);
		contentPanel.add(rdbtnInteger);
		
		spinner = new JSpinner();
		spinner.setEnabled(false);
		spinner.setModel(new SpinnerNumberModel(new Integer(0), null, null, new Integer(1)));
		spinner.setBounds(109, 118, 86, 20);
		contentPanel.add(spinner);
		
		JLabel lblValue_1 = new JLabel("Value:");
		lblValue_1.setBounds(109, 93, 46, 14);
		contentPanel.add(lblValue_1);
		
		rdbtnBoolean = new JRadioButton("Boolean");
		rdbtnBoolean.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(rdbtnBoolean.isSelected()) {
					spinner.setEnabled(false);
					textField_1.setEnabled(false);
					chckbxTrue.setEnabled(true);
					spinner_1.setEnabled(false);
				} else
					chckbxTrue.setEnabled(false);
			}
		});
		buttonGroup.add(rdbtnBoolean);
		rdbtnBoolean.setBounds(208, 64, 97, 23);
		contentPanel.add(rdbtnBoolean);
		
		chckbxTrue = new JCheckBox("True");
		chckbxTrue.setEnabled(false);
		chckbxTrue.setBounds(208, 89, 97, 23);
		contentPanel.add(chckbxTrue);
		
		rdbtnFloat = new JRadioButton("Float");
		rdbtnFloat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnFloat.isSelected()) {
					spinner.setEnabled(false);
					textField_1.setEnabled(false);
					chckbxTrue.setEnabled(true);
					spinner_1.setEnabled(true);
				} else
					spinner_1.setEnabled(false);
			}
		});
		buttonGroup.add(rdbtnFloat);
		rdbtnFloat.setBounds(309, 62, 86, 25);
		contentPanel.add(rdbtnFloat);
		
		JLabel lblValue_2 = new JLabel("Value:");
		lblValue_2.setBounds(309, 94, 46, 14);
		contentPanel.add(lblValue_2);
		
		spinner_1 = new JSpinner();
		spinner_1.setEnabled(false);
		spinner_1.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		spinner_1.setBounds(309, 119, 86, 20);
		contentPanel.add(spinner_1);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						saveVariable();
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
	
	public void saveVariable() {
		var = nameField.getText() + ";";
		if(rdbtnString.isSelected()) {
			var += "S;" + textField_1.getText();
		} else if(rdbtnInteger.isSelected()) {
			var += "I;" + spinner.getValue();
		} else if(rdbtnBoolean.isSelected()) {
			var += "B;" + chckbxTrue.isSelected();
		} else if(rdbtnFloat.isSelected()) {
			var += "F;" + spinner_1.getValue();
		}
	}
	
	public String getVariable() {
		return var;
	}
}
