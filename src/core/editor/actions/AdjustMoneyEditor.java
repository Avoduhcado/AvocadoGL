package core.editor.actions;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.ButtonGroup;

import core.editor.map.MapPanel;
import javax.swing.SpinnerNumberModel;

public class AdjustMoneyEditor extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private String adjust;
	private JSpinner adjustSpinner;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JComboBox<String> selectCombo;
	private JRadioButton rdbtnSource;
	private JRadioButton rdbtnSelect;
	private JRadioButton rdbtnActivator;
	private JLabel lblTarget;
	private JRadioButton allButton;
	private JRadioButton percentButton;
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();
	private JSpinner percentSpinner;
	private JRadioButton staticButton;

	/**
	 * Create the dialog.
	 */
	public AdjustMoneyEditor() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Adjust Money Editor");
		setBounds(100, 100, 343, 234);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblValueToAdjust = new JLabel("Value to Adjust");
		lblValueToAdjust.setBounds(10, 11, 125, 14);
		contentPanel.add(lblValueToAdjust);
		
		adjustSpinner = new JSpinner();
		adjustSpinner.setBounds(10, 36, 50, 20);
		contentPanel.add(adjustSpinner);
		
		rdbtnActivator = new JRadioButton("Activator");
		rdbtnActivator.setSelected(true);
		buttonGroup.add(rdbtnActivator);
		rdbtnActivator.setBounds(10, 97, 89, 23);
		contentPanel.add(rdbtnActivator);
		
		rdbtnSource = new JRadioButton("Source");
		buttonGroup.add(rdbtnSource);
		rdbtnSource.setBounds(103, 97, 61, 23);
		contentPanel.add(rdbtnSource);
		
		rdbtnSelect = new JRadioButton("Select...");
		buttonGroup.add(rdbtnSelect);
		rdbtnSelect.setBounds(168, 97, 74, 23);
		contentPanel.add(rdbtnSelect);
		
		selectCombo = new JComboBox<String>();
		selectCombo.setModel(buildSelectCombo());
		selectCombo.setBounds(168, 129, 109, 22);
		contentPanel.add(selectCombo);
		
		lblTarget = new JLabel("Target");
		lblTarget.setBounds(10, 74, 46, 14);
		contentPanel.add(lblTarget);
		
		allButton = new JRadioButton("All");
		allButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(allButton.isSelected()) {
					percentSpinner.setEnabled(false);
				}
			}
		});
		buttonGroup_1.add(allButton);
		allButton.setBounds(127, 34, 53, 23);
		contentPanel.add(allButton);
		
		percentButton = new JRadioButton("%");
		buttonGroup_1.add(percentButton);
		percentButton.setBounds(184, 34, 46, 23);
		contentPanel.add(percentButton);
		
		percentSpinner = new JSpinner();
		percentSpinner.setModel(new SpinnerNumberModel(new Float(0), new Float(-1), new Float(1), new Float(1)));
		percentSpinner.setBounds(238, 36, 37, 20);
		contentPanel.add(percentSpinner);
		
		staticButton = new JRadioButton("Static");
		staticButton.setSelected(true);
		buttonGroup_1.add(staticButton);
		staticButton.setBounds(68, 34, 55, 23);
		contentPanel.add(staticButton);
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
	
	public void save() {
		adjust = "@Adjust Money:";
		if(rdbtnActivator.isSelected()) {
			adjust += "activator:";
		} else if(rdbtnSource.isSelected()) {
			adjust += "source:";
		} else if(rdbtnSelect.isSelected()) {
			adjust += selectCombo.getSelectedItem() + ":";
		}
		
		if(allButton.isSelected()) {
			adjust += "all";
		} else if(percentButton.isSelected()) {
			adjust += "percent:" + percentSpinner.getValue();
		} else {
			adjust += "static:" + adjustSpinner.getValue();
		}
	}

	public String getAdjust() {
		return adjust;
	}
}
