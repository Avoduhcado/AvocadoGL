package core.editor.actions;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;

import core.editor.map.MapPanel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SpinnerNumberModel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class AdjustStatEditor extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JComboBox<String> selectCombo;
	private JRadioButton rdbtnSource;
	private JSpinner vitalitySpinner;
	private JSpinner virtueSpinner;
	private JSpinner vigorSpinner;
	private JRadioButton rdbtnSelect;
	private JRadioButton rdbtnActivator;
	private JSpinner vivacitySpinner;
	private String adjust;
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();
	private final ButtonGroup buttonGroup_2 = new ButtonGroup();
	private final ButtonGroup buttonGroup_3 = new ButtonGroup();
	private final ButtonGroup buttonGroup_4 = new ButtonGroup();
	private JRadioButton vigorMaxCheck;
	private JRadioButton vivacityMaxCheck;
	private JRadioButton vigorEmptyCheck;
	private JRadioButton vivacityEmptyCheck;
	private JRadioButton vitalityEmptyCheck;
	private JRadioButton virtueMaxCheck;
	private JRadioButton virtueEmptyCheck;
	private JRadioButton vitalityMaxCheck;
	private JSeparator separator;
	private JRadioButton vitalityStaticCheck;
	private JRadioButton vigorStaticCheck;
	private JRadioButton virtueStaticCheck;
	private JRadioButton vivacityStaticCheck;

	/**
	 * Create the dialog.
	 */
	public AdjustStatEditor() {
		setTitle("Adjust Stats");
		setBounds(100, 100, 400, 300);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblVitality = new JLabel("Vitality");
		lblVitality.setBounds(10, 11, 46, 14);
		contentPanel.add(lblVitality);
		
		vitalitySpinner = new JSpinner();
		vitalitySpinner.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		vitalitySpinner.setBounds(10, 36, 46, 20);
		contentPanel.add(vitalitySpinner);
		
		JLabel lblVigor = new JLabel("Vigor");
		lblVigor.setBounds(10, 67, 46, 14);
		contentPanel.add(lblVigor);
		
		vigorSpinner = new JSpinner();
		vigorSpinner.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		vigorSpinner.setBounds(10, 92, 46, 20);
		contentPanel.add(vigorSpinner);
		
		JLabel lblVirtue = new JLabel("Virtue");
		lblVirtue.setBounds(10, 123, 46, 14);
		contentPanel.add(lblVirtue);
		
		virtueSpinner = new JSpinner();
		virtueSpinner.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		virtueSpinner.setBounds(10, 148, 46, 20);
		contentPanel.add(virtueSpinner);
		
		JLabel lblVivacity = new JLabel("Vivacity");
		lblVivacity.setBounds(10, 179, 46, 14);
		contentPanel.add(lblVivacity);
		
		vivacitySpinner = new JSpinner();
		vivacitySpinner.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		vivacitySpinner.setBounds(10, 204, 46, 20);
		contentPanel.add(vivacitySpinner);
		
		rdbtnActivator = new JRadioButton("Activator");
		rdbtnActivator.setSelected(true);
		buttonGroup.add(rdbtnActivator);
		rdbtnActivator.setBounds(269, 7, 109, 23);
		contentPanel.add(rdbtnActivator);
		
		rdbtnSource = new JRadioButton("Source");
		buttonGroup.add(rdbtnSource);
		rdbtnSource.setBounds(269, 35, 109, 23);
		contentPanel.add(rdbtnSource);
		
		rdbtnSelect = new JRadioButton("Select...");
		rdbtnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(rdbtnSelect.isSelected()) {
					selectCombo.setEnabled(true);
				} else {
					selectCombo.setEnabled(false);
				}
			}
		});
		buttonGroup.add(rdbtnSelect);
		rdbtnSelect.setBounds(269, 63, 109, 23);
		contentPanel.add(rdbtnSelect);
		
		selectCombo = new JComboBox<String>();
		selectCombo.setModel(buildSelectCombo());
		selectCombo.setBounds(269, 91, 109, 22);
		contentPanel.add(selectCombo);
		
		vitalityMaxCheck = new JRadioButton("Max");
		buttonGroup_1.add(vitalityMaxCheck);
		vitalityMaxCheck.setBounds(123, 34, 55, 23);
		contentPanel.add(vitalityMaxCheck);
		
		vigorMaxCheck = new JRadioButton("Max");
		buttonGroup_2.add(vigorMaxCheck);
		vigorMaxCheck.setBounds(123, 90, 55, 23);
		contentPanel.add(vigorMaxCheck);
		
		virtueMaxCheck = new JRadioButton("Max");
		buttonGroup_3.add(virtueMaxCheck);
		virtueMaxCheck.setBounds(123, 146, 55, 23);
		contentPanel.add(virtueMaxCheck);
		
		vivacityMaxCheck = new JRadioButton("Max");
		buttonGroup_4.add(vivacityMaxCheck);
		vivacityMaxCheck.setBounds(123, 202, 55, 23);
		contentPanel.add(vivacityMaxCheck);
		
		vitalityEmptyCheck = new JRadioButton("Empty");
		buttonGroup_1.add(vitalityEmptyCheck);
		vitalityEmptyCheck.setBounds(182, 34, 67, 23);
		contentPanel.add(vitalityEmptyCheck);
		
		vigorEmptyCheck = new JRadioButton("Empty");
		buttonGroup_2.add(vigorEmptyCheck);
		vigorEmptyCheck.setBounds(182, 90, 67, 23);
		contentPanel.add(vigorEmptyCheck);
		
		virtueEmptyCheck = new JRadioButton("Empty");
		buttonGroup_3.add(virtueEmptyCheck);
		virtueEmptyCheck.setBounds(182, 146, 67, 23);
		contentPanel.add(virtueEmptyCheck);
		
		vivacityEmptyCheck = new JRadioButton("Empty");
		buttonGroup_4.add(vivacityEmptyCheck);
		vivacityEmptyCheck.setBounds(182, 202, 67, 23);
		contentPanel.add(vivacityEmptyCheck);
		
		separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(257, 11, 2, 213);
		contentPanel.add(separator);
		
		vitalityStaticCheck = new JRadioButton("Static");
		vitalityStaticCheck.setSelected(true);
		buttonGroup_1.add(vitalityStaticCheck);
		vitalityStaticCheck.setBounds(64, 35, 55, 23);
		contentPanel.add(vitalityStaticCheck);
		
		vigorStaticCheck = new JRadioButton("Static");
		vigorStaticCheck.setSelected(true);
		buttonGroup_2.add(vigorStaticCheck);
		vigorStaticCheck.setBounds(64, 91, 55, 23);
		contentPanel.add(vigorStaticCheck);
		
		virtueStaticCheck = new JRadioButton("Static");
		virtueStaticCheck.setSelected(true);
		buttonGroup_3.add(virtueStaticCheck);
		virtueStaticCheck.setBounds(64, 147, 55, 23);
		contentPanel.add(virtueStaticCheck);
		
		vivacityStaticCheck = new JRadioButton("Static");
		vivacityStaticCheck.setSelected(true);
		buttonGroup_4.add(vivacityStaticCheck);
		vivacityStaticCheck.setBounds(64, 203, 55, 23);
		contentPanel.add(vivacityStaticCheck);
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
		adjust = "@Adjust Stats:";
		if(rdbtnActivator.isSelected()) {
			adjust += "activator:";
		} else if(rdbtnSource.isSelected()) {
			adjust += "source:";
		} else if(rdbtnSelect.isSelected()) {
			adjust += selectCombo.getSelectedItem() + ":";
		}
		
		if(vitalityMaxCheck.isSelected()) {
			adjust += "max:";
		} else if(vitalityEmptyCheck.isSelected()) {
			adjust += "empty:";
		} else {
			adjust += vitalitySpinner.getValue() + ":";
		}
		
		if(vigorMaxCheck.isSelected()) {
			adjust += "max:";
		} else if(vigorEmptyCheck.isSelected()) {
			adjust += "empty:";
		} else {
			adjust += vigorSpinner.getValue() + ":";
		}
		
		if(virtueMaxCheck.isSelected()) {
			adjust += "max:";
		} else if(virtueEmptyCheck.isSelected()) {
			adjust += "empty:";
		} else {
			adjust += virtueSpinner.getValue() + ":";
		}
		
		if(vivacityMaxCheck.isSelected()) {
			adjust += "max:";
		} else if(vivacityEmptyCheck.isSelected()) {
			adjust += "empty:";
		} else {
			adjust += vivacitySpinner.getValue() + ":";
		}
	}
	
	public String getAdjust() {
		return adjust;
	}
}
