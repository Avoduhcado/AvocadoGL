package core.editor.entities;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import core.equipment.Equipment;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import java.awt.Dimension;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class EntityEquipmentEditor extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static ArrayList<String> itemDatabase = new ArrayList<String>();
	
	private JComboBox<String> comboBox;
	private JComboBox<String> comboBox_1;
	private JComboBox<String> comboBox_2;
	private JComboBox<String> comboBox_3;
	private JComboBox<String> comboBox_4;
	private JComboBox<String> comboBox_5;
	private JComboBox<String> comboBox_6;
	private JComboBox<String> comboBox_7;
	private JComboBox<String> comboBox_8;
	private JComboBox<String> comboBox_9;
	private JComboBox<String> comboBox_10;
	private JComboBox<String> comboBox_11;
	private JComboBox<String> comboBox_12;
	private JComboBox<String> comboBox_13;
	private JComboBox<String> comboBox_14;
	private JComboBox<String> comboBox_15;
	private JComboBox<String> comboBox_16;
	private JComboBox<String> comboBox_17;
	private JComboBox<String> comboBox_18;
	private JComboBox<String> comboBox_19;
	private JComboBox<String> comboBox_20;
	private JComboBox<String> comboBox_21;
	private JComboBox<String> comboBox_22;
	private JComboBox<String> comboBox_23;
	private JComboBox<String> comboBox_24;
	private JComboBox<String> comboBox_25;
	private JSpinner spinner;
	private JSpinner spinner_1;
	private JList<String> list_1;
	private DefaultListModel<String> itemListModel;
	private DefaultListModel<String> ammoListModel;
	private JList<String> list;
	
	private String equipment = "";
	private JPanel quiverTab;
	private JPanel spellbookTab;
	private JSplitPane splitPane_1;
	private JPanel panel;
	private JScrollPane scrollPane_2;
	private JList<String> list_2;
	private JLabel lblQuiverSize;
	private JSpinner spinQuiverSize;
	private JButton btnRemoveAmmo;
	private JList<String> list_3;
	
	/**
	 * Create the dialog.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public EntityEquipmentEditor(String savedEquipment) {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Equipment Editor");
		setResizable(false);
		setBounds(100, 100, 450, 350);
		getContentPane().setLayout(new BorderLayout());
		{
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			getContentPane().add(tabbedPane, BorderLayout.CENTER);
			{	
				JPanel armorTab = new JPanel();
				tabbedPane.addTab("Armor", null, armorTab, null);
				armorTab.setLayout(null);
				
				comboBox = new JComboBox();
				comboBox.setModel(buildList(1, 0));
				comboBox.setBounds(10, 11, 130, 20);
				armorTab.add(comboBox);
				
				comboBox_1 = new JComboBox();
				comboBox_1.setModel(buildList(1, 1));
				comboBox_1.setBounds(150, 11, 130, 20);
				armorTab.add(comboBox_1);
				
				comboBox_2 = new JComboBox();
				comboBox_2.setModel(buildList(1, 2));
				comboBox_2.setBounds(290, 11, 130, 20);
				armorTab.add(comboBox_2);
				
				comboBox_3 = new JComboBox();
				comboBox_3.setModel(buildList(1, 3));
				comboBox_3.setBounds(10, 42, 130, 20);
				armorTab.add(comboBox_3);
				
				comboBox_4 = new JComboBox();
				comboBox_4.setModel(buildList(1, 4));
				comboBox_4.setBounds(150, 42, 130, 20);
				armorTab.add(comboBox_4);
				
				comboBox_5 = new JComboBox();
				comboBox_5.setModel(buildList(1, 5));
				comboBox_5.setBounds(290, 42, 130, 20);
				armorTab.add(comboBox_5);
				
				comboBox_6 = new JComboBox();
				comboBox_6.setModel(buildList(1, 6));
				comboBox_6.setBounds(10, 73, 130, 20);
				armorTab.add(comboBox_6);
				
				comboBox_7 = new JComboBox();
				comboBox_7.setModel(buildList(1, 7));
				comboBox_7.setBounds(150, 73, 130, 20);
				armorTab.add(comboBox_7);
				
				comboBox_8 = new JComboBox();
				comboBox_8.setModel(buildList(1, 8));
				comboBox_8.setBounds(290, 73, 130, 20);
				armorTab.add(comboBox_8);
				
				comboBox_9 = new JComboBox();
				comboBox_9.setModel(buildList(1, 9));
				comboBox_9.setBounds(10, 104, 130, 20);
				armorTab.add(comboBox_9);
				
				comboBox_10 = new JComboBox();
				comboBox_10.setModel(buildList(1, 10));
				comboBox_10.setBounds(150, 104, 130, 20);
				armorTab.add(comboBox_10);
				
				comboBox_11 = new JComboBox();
				comboBox_11.setModel(buildList(1, 11));
				comboBox_11.setBounds(290, 104, 130, 20);
				armorTab.add(comboBox_11);
				
				comboBox_12 = new JComboBox();
				comboBox_12.setModel(buildList(1, 12));
				comboBox_12.setBounds(10, 135, 130, 20);
				armorTab.add(comboBox_12);
				
				comboBox_13 = new JComboBox();
				comboBox_13.setModel(buildList(1, 13));
				comboBox_13.setBounds(150, 135, 130, 20);
				armorTab.add(comboBox_13);
				
				comboBox_14 = new JComboBox();
				comboBox_14.setModel(buildList(1, 14));
				comboBox_14.setBounds(290, 135, 130, 20);
				armorTab.add(comboBox_14);
				
				comboBox_15 = new JComboBox();
				comboBox_15.setModel(buildList(1, 15));
				comboBox_15.setBounds(10, 166, 130, 20);
				armorTab.add(comboBox_15);
				
				comboBox_16 = new JComboBox();
				comboBox_16.setModel(buildList(1, 16));
				comboBox_16.setBounds(150, 166, 130, 20);
				armorTab.add(comboBox_16);
				
				comboBox_17 = new JComboBox();
				comboBox_17.setModel(buildList(1, 17));
				comboBox_17.setBounds(290, 166, 130, 20);
				armorTab.add(comboBox_17);
			}
			{
				JPanel weaponTab = new JPanel();
				tabbedPane.addTab("Weapons", null, weaponTab, null);
				weaponTab.setLayout(null);
				
				comboBox_18 = new JComboBox();
				comboBox_18.setModel(buildList(2, 0));
				comboBox_18.setBounds(10, 11, 130, 20);
				weaponTab.add(comboBox_18);
				
				comboBox_19 = new JComboBox();
				comboBox_19.setModel(buildList(2, 1));
				comboBox_19.setBounds(150, 11, 130, 20);
				weaponTab.add(comboBox_19);
				
				comboBox_20 = new JComboBox();
				comboBox_20.setModel(buildList(2, 2));
				comboBox_20.setBounds(290, 11, 130, 20);
				weaponTab.add(comboBox_20);
				
				comboBox_21 = new JComboBox();
				comboBox_21.setModel(buildList(2, 3));
				comboBox_21.setBounds(10, 42, 130, 20);
				weaponTab.add(comboBox_21);
				
				comboBox_22 = new JComboBox();
				comboBox_22.setModel(buildList(2, 4));
				comboBox_22.setBounds(150, 42, 130, 20);
				weaponTab.add(comboBox_22);
				
				comboBox_23 = new JComboBox();
				comboBox_23.setModel(buildList(2, 5));
				comboBox_23.setBounds(290, 42, 130, 20);
				weaponTab.add(comboBox_23);
				
				comboBox_24 = new JComboBox();
				comboBox_24.setModel(buildList(2, 6));
				comboBox_24.setBounds(10, 73, 130, 20);
				weaponTab.add(comboBox_24);
				
				comboBox_25 = new JComboBox();
				comboBox_25.setModel(buildList(2, 7));
				comboBox_25.setBounds(150, 73, 130, 20);
				weaponTab.add(comboBox_25);
			}
			{
				JPanel pocketTab = new JPanel();
				tabbedPane.addTab("Pockets", null, pocketTab, null);
				pocketTab.setLayout(null);
				
				itemListModel = new DefaultListModel();
				
				JSplitPane splitPane = new JSplitPane();
				splitPane.setBounds(0, 0, 439, 261);
				pocketTab.add(splitPane);
				
				JPanel panel_1 = new JPanel();
				splitPane.setRightComponent(panel_1);
				panel_1.setLayout(null);
				
				JLabel lblGold = new JLabel("Gold");
				lblGold.setBounds(10, 8, 42, 14);
				panel_1.add(lblGold);
				
				spinner_1 = new JSpinner();
				spinner_1.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
				spinner_1.setBounds(62, 6, 54, 20);
				panel_1.add(spinner_1);

				JScrollPane scrollPane_1 = new JScrollPane();
				scrollPane_1.setBounds(10, 71, 310, 177);
				panel_1.add(scrollPane_1);

				list_1 = new JList();
				list_1.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
				list_1.setModel(itemListModel);
				scrollPane_1.setViewportView(list_1);
				
				JLabel lblPocketSize = new JLabel("Pocket Size");
				lblPocketSize.setBounds(126, 8, 86, 14);
				panel_1.add(lblPocketSize);
				
				spinner = new JSpinner();
				spinner.addFocusListener(new FocusAdapter() {
					@Override
					public void focusLost(FocusEvent arg0) {
						if(list_1.getModel().getSize() > (Integer)spinner.getModel().getValue()) {
							itemListModel.removeRange((Integer)spinner.getModel().getValue(), itemListModel.getSize() - 1);
						}
					}
				});
				spinner.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent arg0) {
						if(list_1.getModel().getSize() > (Integer)spinner.getModel().getValue()) {
							itemListModel.removeRange((Integer)spinner.getModel().getValue(), itemListModel.getSize() - 1);
						}
					}
				});
				spinner.setModel(new SpinnerNumberModel(new Integer(6), new Integer(1), null, new Integer(1)));
				spinner.setBounds(222, 6, 54, 20);
				panel_1.add(spinner);

				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setPreferredSize(new Dimension(100, 2));
				splitPane.setLeftComponent(scrollPane);
				
				list = new JList();
				list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				list.setModel(buildItemList());
				scrollPane.setViewportView(list);
				
				JButton btnAddItem = new JButton("Add Item");
				btnAddItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(list.getSelectedIndex() != -1 && list_1.getModel().getSize() < (Integer)spinner.getModel().getValue()) {
							itemListModel.addElement(list.getSelectedValue());
						}
					}
				});
				btnAddItem.setBounds(10, 35, 89, 23);
				panel_1.add(btnAddItem);
				
				JButton btnRemoveItem = new JButton("Remove Item");
				btnRemoveItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(list_1.getSelectedIndex() != -1) {
							if(list_1.getSelectedValuesList().size() > 1)
								itemListModel.removeRange(list_1.getSelectedIndex(), list_1.getSelectedIndices()[list_1.getSelectedIndices().length - 1]);
							else
								itemListModel.remove(list_1.getSelectedIndex());
						}
					}
				});
				btnRemoveItem.setBounds(136, 35, 110, 23);
				panel_1.add(btnRemoveItem);
				
			}
			{
				quiverTab = new JPanel();
				tabbedPane.addTab("Quiver", null, quiverTab, null);
				quiverTab.setLayout(null);
				
				ammoListModel = new DefaultListModel();
				
				splitPane_1 = new JSplitPane();
				splitPane_1.setBounds(0, 0, 439, 257);
				quiverTab.add(splitPane_1);
				
				panel = new JPanel();
				splitPane_1.setRightComponent(panel);
				panel.setLayout(null);
				
				lblQuiverSize = new JLabel("Quiver Size");
				lblQuiverSize.setBounds(12, 13, 58, 14);
				panel.add(lblQuiverSize);
				
				spinQuiverSize = new JSpinner();
				spinQuiverSize.setModel(new SpinnerNumberModel(new Integer(4), new Integer(0), null, new Integer(1)));
				spinQuiverSize.setBounds(82, 11, 45, 20);
				panel.add(spinQuiverSize);
				
				JButton btnAddAmmo = new JButton("Add Ammo");
				btnAddAmmo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(list_2.getSelectedIndex() != -1 && list_3.getModel().getSize() < (Integer)spinQuiverSize.getModel().getValue()) {
							ammoListModel.addElement(list_2.getSelectedValue());
						}
					}
				});
				btnAddAmmo.setBounds(12, 40, 95, 25);
				panel.add(btnAddAmmo);
				
				btnRemoveAmmo = new JButton("Remove Ammo");
				btnRemoveAmmo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(list_3.getSelectedIndex() != -1) {
							if(list_3.getSelectedValuesList().size() > 1)
								ammoListModel.removeRange(list_3.getSelectedIndex(), list_3.getSelectedIndices()[list_3.getSelectedIndices().length - 1]);
							else
								ammoListModel.remove(list_3.getSelectedIndex());
						}
					}
				});
				btnRemoveAmmo.setBounds(119, 40, 115, 25);
				panel.add(btnRemoveAmmo);
				
				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setBounds(12, 78, 301, 164);
				panel.add(scrollPane);
				
				list_3 = new JList();
				list_3.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
				list_3.setModel(ammoListModel);
				scrollPane.setViewportView(list_3);
				
				scrollPane_2 = new JScrollPane();
				splitPane_1.setLeftComponent(scrollPane_2);
				
				list_2 = new JList();
				list_2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				list_2.setModel(buildAmmoList());
				scrollPane_2.setViewportView(list_2);
				splitPane_1.setDividerLocation(0.25);
			}
			spellbookTab = new JPanel();
			tabbedPane.addTab("Spellbook", null, spellbookTab, null);
		}
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						equipment += "0;" + (Integer)spinner_1.getModel().getValue() + "\n";
						equipment += "1;" + comboBox.getSelectedItem().toString().substring(0, comboBox.getSelectedItem().toString().indexOf(':'))
								+ ";" + comboBox_1.getSelectedItem().toString().substring(0, comboBox_1.getSelectedItem().toString().indexOf(':'))
								+ ";" + comboBox_2.getSelectedItem().toString().substring(0, comboBox_2.getSelectedItem().toString().indexOf(':'))
								+ ";" + comboBox_3.getSelectedItem().toString().substring(0, comboBox_3.getSelectedItem().toString().indexOf(':'))
								+ ";" + comboBox_4.getSelectedItem().toString().substring(0, comboBox_4.getSelectedItem().toString().indexOf(':'))
								+ ";" + comboBox_5.getSelectedItem().toString().substring(0, comboBox_5.getSelectedItem().toString().indexOf(':'))
								+ ";" + comboBox_6.getSelectedItem().toString().substring(0, comboBox_6.getSelectedItem().toString().indexOf(':'))
								+ ";" + comboBox_7.getSelectedItem().toString().substring(0, comboBox_7.getSelectedItem().toString().indexOf(':'))
								+ ";" + comboBox_8.getSelectedItem().toString().substring(0, comboBox_8.getSelectedItem().toString().indexOf(':'))
								+ ";" + comboBox_9.getSelectedItem().toString().substring(0, comboBox_9.getSelectedItem().toString().indexOf(':'))
								+ ";" + comboBox_10.getSelectedItem().toString().substring(0, comboBox_10.getSelectedItem().toString().indexOf(':'))
								+ ";" + comboBox_11.getSelectedItem().toString().substring(0, comboBox_11.getSelectedItem().toString().indexOf(':'))
								+ ";" + comboBox_12.getSelectedItem().toString().substring(0, comboBox_12.getSelectedItem().toString().indexOf(':'))
								+ ";" + comboBox_13.getSelectedItem().toString().substring(0, comboBox_13.getSelectedItem().toString().indexOf(':'))
								+ ";" + comboBox_14.getSelectedItem().toString().substring(0, comboBox_14.getSelectedItem().toString().indexOf(':'))
								+ ";" + comboBox_15.getSelectedItem().toString().substring(0, comboBox_15.getSelectedItem().toString().indexOf(':'))
								+ ";" + comboBox_16.getSelectedItem().toString().substring(0, comboBox_16.getSelectedItem().toString().indexOf(':'))
								+ ";" + comboBox_17.getSelectedItem().toString().substring(0, comboBox_17.getSelectedItem().toString().indexOf(':')) + "\n";
						equipment += "2;" + comboBox_18.getSelectedItem().toString().substring(0, comboBox_18.getSelectedItem().toString().indexOf(':'))
								+ ";" + comboBox_19.getSelectedItem().toString().substring(0, comboBox_19.getSelectedItem().toString().indexOf(':'))
								+ ";" + comboBox_20.getSelectedItem().toString().substring(0, comboBox_20.getSelectedItem().toString().indexOf(':'))
								+ ";" + comboBox_21.getSelectedItem().toString().substring(0, comboBox_21.getSelectedItem().toString().indexOf(':'))
								+ ";" + comboBox_22.getSelectedItem().toString().substring(0, comboBox_22.getSelectedItem().toString().indexOf(':'))
								+ ";" + comboBox_23.getSelectedItem().toString().substring(0, comboBox_23.getSelectedItem().toString().indexOf(':'))
								+ ";" + comboBox_24.getSelectedItem().toString().substring(0, comboBox_24.getSelectedItem().toString().indexOf(':'))
								+ ";" + comboBox_25.getSelectedItem().toString().substring(0, comboBox_25.getSelectedItem().toString().indexOf(':'))
								+ ";" + "R0;L1" + "\n";
						equipment += "3;" + (Integer)spinner.getValue();
						for(int x = 0; x<list_1.getModel().getSize(); x++) {
							equipment += ";" + list_1.getModel().getElementAt(x).substring(0, list_1.getModel().getElementAt(x).indexOf(':'));
						}
						if(list_1.getModel().getSize() < (Integer)spinner.getValue()) {
							for(int x = 0; x<(Integer)spinner.getValue() - list_1.getModel().getSize(); x++)
								equipment += ";0";
						}
						System.out.println(equipment);
						
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
		if(!savedEquipment.matches(""))
			loadEquipment(savedEquipment);
		
		setModalityType(ModalityType.APPLICATION_MODAL);
		setVisible(true);
	}
	
	public static void fillDatabase() {
		itemDatabase = new ArrayList<String>();
		
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/database/items"));

	    	String line;
	    	while((line = reader.readLine()) != null) {
	    		String[] temp = line.split(";");
	    		
	    		if(temp[0].startsWith("2") || temp[0].startsWith("3")) {
	    			itemDatabase.add(temp[0] + ";" + temp[1] + ";" + temp[11]);
	    		} else {
	    			itemDatabase.add(temp[0] + ";" + temp[1]);
	    		}
	    	}
	    	reader.close();
	    } catch (FileNotFoundException e) {
	    	System.out.println("The item database has been misplaced!");
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	System.out.println("Item database failed to load!");
	    	e.printStackTrace();
	    }
	}
	
	public DefaultListModel<String> buildItemList() {
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		for(int x = 0; x<itemDatabase.size(); x++) {
			String[] temp = itemDatabase.get(x).split(";");
			if(temp[0].startsWith("1"))
				listModel.addElement(temp[0] + ": " + temp[1]);
		}
		
		return listModel;
	}
	
	public DefaultComboBoxModel<String> buildList(int type, int slot) {
		Equipment equipment = new Equipment();
		DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<String>();
		switch(type) {
		case(1):
			comboBoxModel.addElement("0: " + equipment.getArmor().getSlotName(slot));
			if(slot == 12 || slot == 13) {
				for(int x = 0; x<itemDatabase.size(); x++) {
					String[] temp = itemDatabase.get(x).split(";");
					if(temp[0].startsWith("2") && (Integer.parseInt(temp[2]) == 12 || Integer.parseInt(temp[2]) == 13))
						comboBoxModel.addElement(temp[0] + ": " + temp[1]);
				}
			} else {
				for(int x = 0; x<itemDatabase.size(); x++) {
					String[] temp = itemDatabase.get(x).split(";");
					if(temp[0].startsWith("2") && Integer.parseInt(temp[2]) == slot)
						comboBoxModel.addElement(temp[0] + ": " + temp[1]);
				}
			}
			break;
		case(2):
			comboBoxModel.addElement("0: " + equipment.getWeapons().getSlotName(slot));
			if(slot == 0 || slot == 1) {
				for(int x = 0; x<itemDatabase.size(); x++) {
					String[] temp = itemDatabase.get(x).split(";");
					comboBoxModel.addElement(temp[0] + ": " + temp[1]);
				}
			} else if(slot == 2 || slot == 3) {
				for(int x = 0; x<itemDatabase.size(); x++) {
					String[] temp = itemDatabase.get(x).split(";");
					if(temp[0].startsWith("3") && (Integer.parseInt(temp[2]) == 0 || Integer.parseInt(temp[2]) == 1 
							|| Integer.parseInt(temp[2]) == 2 || Integer.parseInt(temp[2]) == 3))
						comboBoxModel.addElement(temp[0] + ": " + temp[1]);
				}
			} else {
				for(int x = 0; x<itemDatabase.size(); x++) {
					String[] temp = itemDatabase.get(x).split(";");
					if(temp[0].startsWith("3") && Integer.parseInt(temp[2]) == slot)
						comboBoxModel.addElement(temp[0] + ": " + temp[1]);
				}
			}
			break;
		default:
			System.out.println("There's no item of type: " + type);
		}
		
		return comboBoxModel;
	}
	
	public DefaultListModel<String> buildAmmoList() {
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		for(int x = 0; x<itemDatabase.size(); x++) {
			String[] temp = itemDatabase.get(x).split(";");
			if(temp[0].startsWith("3"))
				listModel.addElement(temp[0] + ": " + temp[1]);
		}
		
		return listModel;
	}
	
	public void loadEquipment(String equipment) {
		String[] temp = equipment.split("\n");
		// Gold
		String[] singleTemp = temp[0].split(";");
		spinner_1.setValue(Integer.parseInt(singleTemp[1]));
		// Armor
		singleTemp = temp[1].split(";");
		for(int y = 0; y<comboBox.getModel().getSize(); y++)
			if(comboBox.getModel().getElementAt(y).substring(0, comboBox.getModel().getElementAt(y).indexOf(":")).matches(singleTemp[1]))
				comboBox.setSelectedIndex(y);
		for(int y = 0; y<comboBox_1.getModel().getSize(); y++)
			if(comboBox_1.getModel().getElementAt(y).substring(0, comboBox_1.getModel().getElementAt(y).indexOf(":")).matches(singleTemp[2]))
				comboBox_1.setSelectedIndex(y);
		for(int y = 0; y<comboBox_2.getModel().getSize(); y++)
			if(comboBox_2.getModel().getElementAt(y).substring(0, comboBox_2.getModel().getElementAt(y).indexOf(":")).matches(singleTemp[3]))
				comboBox_2.setSelectedIndex(y);
		for(int y = 0; y<comboBox_3.getModel().getSize(); y++)
			if(comboBox_3.getModel().getElementAt(y).substring(0, comboBox_3.getModel().getElementAt(y).indexOf(":")).matches(singleTemp[4]))
				comboBox_3.setSelectedIndex(y);
		for(int y = 0; y<comboBox_4.getModel().getSize(); y++)
			if(comboBox_4.getModel().getElementAt(y).substring(0, comboBox_4.getModel().getElementAt(y).indexOf(":")).matches(singleTemp[5]))
				comboBox_4.setSelectedIndex(y);
		for(int y = 0; y<comboBox_5.getModel().getSize(); y++)
			if(comboBox_5.getModel().getElementAt(y).substring(0, comboBox_5.getModel().getElementAt(y).indexOf(":")).matches(singleTemp[6]))
				comboBox_5.setSelectedIndex(y);
		for(int y = 0; y<comboBox_6.getModel().getSize(); y++)
			if(comboBox_6.getModel().getElementAt(y).substring(0, comboBox_6.getModel().getElementAt(y).indexOf(":")).matches(singleTemp[7]))
				comboBox_6.setSelectedIndex(y);
		for(int y = 0; y<comboBox_7.getModel().getSize(); y++)
			if(comboBox_7.getModel().getElementAt(y).substring(0, comboBox_7.getModel().getElementAt(y).indexOf(":")).matches(singleTemp[8]))
				comboBox_7.setSelectedIndex(y);
		for(int y = 0; y<comboBox_8.getModel().getSize(); y++)
			if(comboBox_8.getModel().getElementAt(y).substring(0, comboBox_8.getModel().getElementAt(y).indexOf(":")).matches(singleTemp[9]))
				comboBox_8.setSelectedIndex(y);
		for(int y = 0; y<comboBox_9.getModel().getSize(); y++)
			if(comboBox_9.getModel().getElementAt(y).substring(0, comboBox_9.getModel().getElementAt(y).indexOf(":")).matches(singleTemp[10]))
				comboBox_9.setSelectedIndex(y);
		for(int y = 0; y<comboBox_10.getModel().getSize(); y++)
			if(comboBox_10.getModel().getElementAt(y).substring(0, comboBox_10.getModel().getElementAt(y).indexOf(":")).matches(singleTemp[11]))
				comboBox_10.setSelectedIndex(y);
		for(int y = 0; y<comboBox_11.getModel().getSize(); y++)
			if(comboBox_11.getModel().getElementAt(y).substring(0, comboBox_11.getModel().getElementAt(y).indexOf(":")).matches(singleTemp[12]))
				comboBox_11.setSelectedIndex(y);
		for(int y = 0; y<comboBox_12.getModel().getSize(); y++)
			if(comboBox_12.getModel().getElementAt(y).substring(0, comboBox_12.getModel().getElementAt(y).indexOf(":")).matches(singleTemp[13]))
				comboBox_12.setSelectedIndex(y);
		for(int y = 0; y<comboBox_13.getModel().getSize(); y++)
			if(comboBox_13.getModel().getElementAt(y).substring(0, comboBox_13.getModel().getElementAt(y).indexOf(":")).matches(singleTemp[14]))
				comboBox_13.setSelectedIndex(y);
		for(int y = 0; y<comboBox_14.getModel().getSize(); y++)
			if(comboBox_14.getModel().getElementAt(y).substring(0, comboBox_14.getModel().getElementAt(y).indexOf(":")).matches(singleTemp[15]))
				comboBox_14.setSelectedIndex(y);
		for(int y = 0; y<comboBox_15.getModel().getSize(); y++)
			if(comboBox_15.getModel().getElementAt(y).substring(0, comboBox_15.getModel().getElementAt(y).indexOf(":")).matches(singleTemp[16]))
				comboBox_15.setSelectedIndex(y);
		for(int y = 0; y<comboBox_16.getModel().getSize(); y++)
			if(comboBox_16.getModel().getElementAt(y).substring(0, comboBox_16.getModel().getElementAt(y).indexOf(":")).matches(singleTemp[17]))
				comboBox_16.setSelectedIndex(y);
		for(int y = 0; y<comboBox_17.getModel().getSize(); y++)
			if(comboBox_17.getModel().getElementAt(y).substring(0, comboBox_17.getModel().getElementAt(y).indexOf(":")).matches(singleTemp[18]))
				comboBox_17.setSelectedIndex(y);
		// Weapons
		singleTemp = temp[2].split(";");
		for(int y = 0; y<comboBox_18.getModel().getSize(); y++)
			if(comboBox_18.getModel().getElementAt(y).substring(0, comboBox_18.getModel().getElementAt(y).indexOf(":")).matches(singleTemp[1]))
				comboBox_18.setSelectedIndex(y);
		for(int y = 0; y<comboBox_19.getModel().getSize(); y++)
			if(comboBox_19.getModel().getElementAt(y).substring(0, comboBox_19.getModel().getElementAt(y).indexOf(":")).matches(singleTemp[2]))
				comboBox_19.setSelectedIndex(y);
		for(int y = 0; y<comboBox_20.getModel().getSize(); y++)
			if(comboBox_20.getModel().getElementAt(y).substring(0, comboBox_20.getModel().getElementAt(y).indexOf(":")).matches(singleTemp[3]))
				comboBox_20.setSelectedIndex(y);
		for(int y = 0; y<comboBox_21.getModel().getSize(); y++)
			if(comboBox_21.getModel().getElementAt(y).substring(0, comboBox_21.getModel().getElementAt(y).indexOf(":")).matches(singleTemp[4]))
				comboBox_21.setSelectedIndex(y);
		for(int y = 0; y<comboBox_22.getModel().getSize(); y++)
			if(comboBox_22.getModel().getElementAt(y).substring(0, comboBox_22.getModel().getElementAt(y).indexOf(":")).matches(singleTemp[5]))
				comboBox_22.setSelectedIndex(y);
		for(int y = 0; y<comboBox_23.getModel().getSize(); y++)
			if(comboBox_23.getModel().getElementAt(y).substring(0, comboBox_23.getModel().getElementAt(y).indexOf(":")).matches(singleTemp[6]))
				comboBox_23.setSelectedIndex(y);
		for(int y = 0; y<comboBox_24.getModel().getSize(); y++)
			if(comboBox_24.getModel().getElementAt(y).substring(0, comboBox_24.getModel().getElementAt(y).indexOf(":")).matches(singleTemp[7]))
				comboBox_24.setSelectedIndex(y);
		for(int y = 0; y<comboBox_25.getModel().getSize(); y++)
			if(comboBox_25.getModel().getElementAt(y).substring(0, comboBox_25.getModel().getElementAt(y).indexOf(":")).matches(singleTemp[8]))
				comboBox_25.setSelectedIndex(y);
		// Pockets
		singleTemp = temp[3].split(";");
		spinner.setValue(Integer.parseInt(singleTemp[1]));
		for(int x = 0; x<Integer.parseInt(singleTemp[1]); x++) {
			try {
				for(int y = 0; y<list.getModel().getSize(); y++)
					if(list.getModel().getElementAt(y).substring(0, list.getModel().getElementAt(y).indexOf(":")).matches(singleTemp[x+2]))
						itemListModel.addElement(list.getModel().getElementAt(y));
			} catch(ArrayIndexOutOfBoundsException e) {
				break;
			}
		}
	}
	
	public String getEquipment() {
		return equipment;
	}
}
