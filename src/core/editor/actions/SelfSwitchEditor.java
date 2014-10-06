package core.editor.actions;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

public class SelfSwitchEditor extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();
	private JRadioButton rdbtnA;
	private JRadioButton rdbtnB;
	private JRadioButton rdbtnDisable;
	private JRadioButton rdbtnEnable;
	private JRadioButton rdbtnC;
	private JRadioButton rdbtnD;
	private String switches;

	/**
	 * Create the dialog.
	 */
	public SelfSwitchEditor() {
		setTitle("Self Switch Editor");
		setBounds(100, 100, 293, 213);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		rdbtnA = new JRadioButton("A");
		rdbtnA.setSelected(true);
		buttonGroup.add(rdbtnA);
		rdbtnA.setBounds(8, 9, 109, 23);
		contentPanel.add(rdbtnA);
		
		rdbtnB = new JRadioButton("B");
		buttonGroup.add(rdbtnB);
		rdbtnB.setBounds(8, 37, 109, 23);
		contentPanel.add(rdbtnB);
		
		rdbtnC = new JRadioButton("C");
		buttonGroup.add(rdbtnC);
		rdbtnC.setBounds(8, 65, 109, 23);
		contentPanel.add(rdbtnC);
		
		rdbtnD = new JRadioButton("D");
		buttonGroup.add(rdbtnD);
		rdbtnD.setBounds(8, 93, 109, 23);
		contentPanel.add(rdbtnD);
		
		rdbtnEnable = new JRadioButton("Enable");
		rdbtnEnable.setSelected(true);
		buttonGroup_1.add(rdbtnEnable);
		rdbtnEnable.setBounds(121, 10, 109, 23);
		contentPanel.add(rdbtnEnable);
		
		rdbtnDisable = new JRadioButton("Disable");
		buttonGroup_1.add(rdbtnDisable);
		rdbtnDisable.setBounds(121, 38, 109, 23);
		contentPanel.add(rdbtnDisable);
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
		switches = "@Switch:";
		if(rdbtnA.isSelected()) {
			switches += "a:";
		} else if(rdbtnB.isSelected()) {
			switches += "b:";
		} else if(rdbtnC.isSelected()) {
			switches += "c:";
		} else if(rdbtnD.isSelected()) {
			switches += "d:";
		}
		
		if(rdbtnEnable.isSelected()) {
			switches += "true";
		} else if(rdbtnDisable.isSelected()) {
			switches += "false";
		}
	}
	
	public String getSwitches() {
		return switches;
	}
	
}
