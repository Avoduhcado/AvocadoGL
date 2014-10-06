package core.editor.items;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JCheckBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JTextArea;

import core.items.Consumable;

public class ConsumableEditor extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextArea textPane;
	private JLabel lblNewLabel;
	private JComboBox<String> comboBox;
	private JComboBox<String> comboBox_1;
	private JList<String> list;
	private JSpinner spinner;
	private JSpinner spinner_1;
	private JCheckBox chckbxPercentage;
	
	private DefaultListModel<String> listModel = new DefaultListModel<String>();

	/**
	 * Create the dialog.
	 */
	public ConsumableEditor(int ID) {
		setTitle("Consumable Editor");
		setBounds(100, 100, 375, 300);
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblId = new JLabel("ID:");
		lblId.setBounds(10, 11, 15, 14);
		contentPanel.add(lblId);
		
		lblNewLabel = new JLabel("" + ID);
		lblNewLabel.setBounds(35, 11, 46, 14);
		contentPanel.add(lblNewLabel);
		
		JLabel lblFlavorText = new JLabel("Flavor text:");
		lblFlavorText.setBounds(10, 36, 71, 14);
		contentPanel.add(lblFlavorText);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 61, 125, 45);
		contentPanel.add(scrollPane_1);
		
		textPane = new JTextArea();
		scrollPane_1.setViewportView(textPane);
		textPane.setLineWrap(true);

		JLabel lblEffects = new JLabel("Effects:");
		lblEffects.setBounds(10, 117, 46, 14);
		contentPanel.add(lblEffects);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 142, 125, 76);
		contentPanel.add(scrollPane);
		
		list = new JList<String>();
		list.setModel(listModel);
		scrollPane.setViewportView(list);

		JLabel lblStat = new JLabel("Stat:");
		lblStat.setBounds(156, 11, 46, 14);
		contentPanel.add(lblStat);
		
		comboBox = new JComboBox<String>();
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				comboBox_1.setModel(buildList());
			}
		});
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Vitality", "Vigor", "Essence", "Vivacity"}));
		comboBox.setBounds(156, 33, 71, 20);
		contentPanel.add(comboBox);

		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(144, 11, 2, 207);
		contentPanel.add(separator);

		JLabel lblEffect = new JLabel("Effect:");
		lblEffect.setBounds(156, 61, 46, 14);
		contentPanel.add(lblEffect);
		
		comboBox_1 = new JComboBox<String>();
		comboBox_1.setModel(buildList());
		comboBox_1.setBounds(156, 86, 71, 20);
		contentPanel.add(comboBox_1);
		
		JLabel lblAdjustValue = new JLabel("Adjust value:");
		lblAdjustValue.setBounds(156, 117, 71, 14);
		contentPanel.add(lblAdjustValue);
		
		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		spinner.setBounds(156, 141, 46, 20);
		contentPanel.add(spinner);
		
		chckbxPercentage = new JCheckBox("Percentage");
		chckbxPercentage.setBounds(156, 168, 105, 25);
		contentPanel.add(chckbxPercentage);
		
		JLabel lblDuration = new JLabel("Duration:");
		lblDuration.setBounds(276, 11, 71, 14);
		contentPanel.add(lblDuration);
		
		spinner_1 = new JSpinner();
		spinner_1.setModel(new SpinnerNumberModel(new Float(0), new Float(0), null, new Float(1)));
		spinner_1.setBounds(276, 34, 46, 20);
		contentPanel.add(spinner_1);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listModel.addElement(comboBox.getSelectedIndex() + ";" + (comboBox_1.getSelectedIndex() + 1) + ";" + spinner.getValue() + ";"
						+ chckbxPercentage.isSelected() + ";" + spinner_1.getValue());
			}
		});
		btnAdd.setBounds(272, 169, 75, 23);
		contentPanel.add(btnAdd);
		
		JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				while(list.getSelectedIndices().length > 0) {
					listModel.remove(list.getSelectedIndex());
				}
			}
		});
		btnRemove.setBounds(272, 195, 75, 23);
		contentPanel.add(btnRemove);
		
		JLabel lblEditEffects = new JLabel("Edit Effects");
		lblEditEffects.setBounds(272, 145, 75, 14);
		contentPanel.add(lblEditEffects);

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
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
		
		load(ID);
		
		setModalityType(ModalityType.APPLICATION_MODAL);
		setVisible(true);
	}
	
	public void load(int ID) {
		if(Consumable.loadConsumable(ID) != null) {
			Consumable consumable = Consumable.loadConsumable(ID);
			textPane.setText(consumable.getFlavorText());
			for(int x = 0; x<consumable.getEffectsTotal(); x++) {
				listModel.addElement(consumable.getStat(x) + ";" + consumable.getEffect(x) + ";" + consumable.getStat(x) + ";" + 
						consumable.getPercent(x) + ";" + consumable.getDuration(x));
			}
		}
	}
	
	public DefaultComboBoxModel<String> buildList() {
		DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<String>();
		switch(comboBox.getSelectedIndex()) {
		case(0):
			comboBoxModel.addElement("Buff Max");
			comboBoxModel.addElement("Heal");
			break;
		case(1):
			comboBoxModel.addElement("Buff Max");
			comboBoxModel.addElement("Envigor");
			break;
		case(2):
			comboBoxModel.addElement("Buff Max");
			comboBoxModel.addElement("Restore");
			break;
		case(3):
			comboBoxModel.addElement("Buff Max");
			comboBoxModel.addElement("Rejuv");
			break;
		}
		
		return comboBoxModel;
	}
	
	public String getEffects() {
		String effects = "";
		for(int x = 0; x<listModel.getSize(); x++)
			effects += ";" + listModel.get(x);
		
		if(!effects.isEmpty())
			return textPane.getText() + effects;
		
		return null;
	}
}
