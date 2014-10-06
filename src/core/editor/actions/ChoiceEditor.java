package core.editor.actions;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ChoiceEditor extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JTextField txtYes;
	private JTextField txtNo;
	private JTextField textField_3;
	private JTextField textField_4;
	private String choice;

	/**
	 * Create the dialog.
	 */
	public ChoiceEditor() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Choice Editor");
		setResizable(false);
		setBounds(100, 100, 335, 371);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblHeader = new JLabel("Header");
			lblHeader.setBounds(10, 11, 46, 14);
			contentPanel.add(lblHeader);
		}
		{
			textField = new JTextField();
			textField.setBounds(10, 36, 299, 20);
			contentPanel.add(textField);
			textField.setColumns(10);
		}
		{
			JLabel lblChoiceOne = new JLabel("Choice One");
			lblChoiceOne.setBounds(10, 67, 86, 14);
			contentPanel.add(lblChoiceOne);
		}
		{
			txtYes = new JTextField();
			txtYes.setText("Yes");
			txtYes.setBounds(10, 92, 299, 20);
			contentPanel.add(txtYes);
			txtYes.setColumns(10);
		}
		{
			JLabel lblChoiceTwo = new JLabel("Choice Two");
			lblChoiceTwo.setBounds(10, 123, 86, 14);
			contentPanel.add(lblChoiceTwo);
		}
		{
			txtNo = new JTextField();
			txtNo.setText("No");
			txtNo.setBounds(10, 148, 299, 20);
			contentPanel.add(txtNo);
			txtNo.setColumns(10);
		}
		{
			JLabel lblChoiceThree = new JLabel("Choice Three");
			lblChoiceThree.setBounds(10, 179, 86, 14);
			contentPanel.add(lblChoiceThree);
		}
		{
			textField_3 = new JTextField();
			textField_3.setBounds(10, 204, 299, 20);
			contentPanel.add(textField_3);
			textField_3.setColumns(10);
		}
		{
			JLabel lblChoiceFour = new JLabel("Choice Four");
			lblChoiceFour.setBounds(10, 235, 86, 14);
			contentPanel.add(lblChoiceFour);
		}
		{
			textField_4 = new JTextField();
			textField_4.setBounds(10, 260, 299, 20);
			contentPanel.add(textField_4);
			textField_4.setColumns(10);
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
		
		setModalityType(ModalityType.APPLICATION_MODAL);
		setVisible(true);
	}

	public void save() {
		choice = "@Choice:";
		if(!textField.getText().isEmpty())
			choice += "true:" + textField.getText() + ";";
		else
			choice += "false:";
		if(!txtYes.getText().isEmpty())
			choice += txtYes.getText() + ";";
		if(!txtNo.getText().isEmpty())
			choice += txtNo.getText() + ";";
		if(!textField_3.getText().isEmpty())
			choice += textField_3.getText() + ";";
		if(!textField_4.getText().isEmpty())
			choice += textField_4.getText() + ";";
		choice += "\n";
		if(!txtYes.getText().isEmpty())
			choice += ">" + txtYes.getText() + "\n @\n";
		if(!txtNo.getText().isEmpty())
			choice += ">" + txtNo.getText() + "\n @\n";
		if(!textField_3.getText().isEmpty())
			choice += ">" + textField_3.getText() + "\n @\n";
		if(!textField_4.getText().isEmpty())
			choice += ">" + textField_4.getText() + "\n @\n";
		choice += ">End";
	}
	
	public String getChoice() {
		return choice;
	}
	
}
