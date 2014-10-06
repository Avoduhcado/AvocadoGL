package core.editor.equipment;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

public class WeaponEditor extends JDialog {

	/** TODO saving */
	private static final long serialVersionUID = 1L;
	private final JSplitPane contentPanel = new JSplitPane();
	private JTextField nameText;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton magicButton;
	private JRadioButton fistButton;
	private JRadioButton rangedButton;
	private JRadioButton meleeButton;
	private JTabbedPane tabbedPane;
	private WeaponPanel attackPanel;
	private WeaponPanel slashPanel;
	private WeaponPanel thrustPanel;
	private WeaponPanel crushPanel;

	/**
	 * Create the dialog.
	 */
	public WeaponEditor(String name) {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Weapon Editor");
		setBounds(100, 100, 600, 475);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		contentPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPanel.setBounds(0, 0, 584, 402);
		
		JPanel panel = new JPanel();
		contentPanel.setLeftComponent(panel);
		panel.setLayout(null);
		{
			JLabel nameLabel = new JLabel("Weapon Name");
			nameLabel.setBounds(12, 9, 79, 16);
			panel.add(nameLabel);
		}
		
		JLabel lblWeaponType = new JLabel("Weapon Type");
		lblWeaponType.setBounds(145, 9, 73, 16);
		panel.add(lblWeaponType);
		
		meleeButton = new JRadioButton("Melee");
		meleeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(meleeButton.isSelected()) {
					tabbedPane.setEnabledAt(0, true);
					tabbedPane.setEnabledAt(1, true);
					tabbedPane.setEnabledAt(2, true);
					tabbedPane.setEnabledAt(3, false);
					tabbedPane.setSelectedIndex(0);
				}
			}
		});
		meleeButton.setBounds(145, 34, 57, 25);
		panel.add(meleeButton);
		meleeButton.setSelected(true);
		buttonGroup.add(meleeButton);
		
		magicButton = new JRadioButton("Magic");
		magicButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(magicButton.isSelected()) {
					tabbedPane.setEnabledAt(0, false);
					tabbedPane.setEnabledAt(1, false);
					tabbedPane.setEnabledAt(2, false);
					tabbedPane.setEnabledAt(3, true);
					tabbedPane.setSelectedIndex(3);
				}
			}
		});
		magicButton.setBounds(206, 34, 59, 25);
		panel.add(magicButton);
		buttonGroup.add(magicButton);
		
		rangedButton = new JRadioButton("Ranged");
		rangedButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rangedButton.isSelected()) {
					tabbedPane.setEnabledAt(0, false);
					tabbedPane.setEnabledAt(1, false);
					tabbedPane.setEnabledAt(2, false);
					tabbedPane.setEnabledAt(3, true);
					tabbedPane.setSelectedIndex(3);
				}
			}
		});
		rangedButton.setBounds(269, 34, 65, 25);
		panel.add(rangedButton);
		buttonGroup.add(rangedButton);
		
		fistButton = new JRadioButton("Fist");
		fistButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(fistButton.isSelected()) {
					tabbedPane.setEnabledAt(0, false);
					tabbedPane.setEnabledAt(1, false);
					tabbedPane.setEnabledAt(2, false);
					tabbedPane.setEnabledAt(3, true);
					tabbedPane.setSelectedIndex(3);
				}
			}
		});
		fistButton.setBounds(338, 34, 43, 25);
		panel.add(fistButton);
		buttonGroup.add(fistButton);
		
		nameText = new JTextField(name);
		nameText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				thrustPanel.updateSprite(nameText.getText() + "_thrust");
				crushPanel.updateSprite(nameText.getText() + "_crush");
				slashPanel.updateSprite(nameText.getText() + "_slash");
				attackPanel.updateSprite(nameText.getText() + "_attack");
				tabbedPane.getSelectedComponent().repaint();
			}
		});
		nameText.setBounds(12, 38, 116, 22);
		panel.add(nameText);
		nameText.setColumns(10);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPanel.setRightComponent(tabbedPane);
		
		thrustPanel = new WeaponPanel(nameText.getText() + "_thrust");
		tabbedPane.addTab("Thrust", null, thrustPanel, null);
		thrustPanel.setLayout(null);
		
		crushPanel = new WeaponPanel(nameText.getText() + "_crush");
		tabbedPane.addTab("Crush", null, crushPanel, null);
		crushPanel.setLayout(null);
		
		slashPanel = new WeaponPanel(nameText.getText() + "_slash");
		tabbedPane.addTab("Slash", null, slashPanel, null);
		slashPanel.setLayout(null);
		
		attackPanel = new WeaponPanel(nameText.getText() + "_attack");
		tabbedPane.addTab("Attack", null, attackPanel, null);
		tabbedPane.setEnabledAt(3, false);
		attackPanel.setLayout(null);
		contentPanel.setDividerLocation(75);
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
		
		if(name != null)
			load();
		setModalityType(ModalityType.APPLICATION_MODAL);
		setVisible(true);
	}
	
	public void save() {
		StringBuilder data = new StringBuilder();
		data.append("-" + nameText.getText() + "-" + System.getProperty("line.separator"));
		if(meleeButton.isSelected()) {
			data.append("<THRUST>" + System.getProperty("line.separator"));
			data.append(thrustPanel.save());
			data.append("<CRUSH>" + System.getProperty("line.separator"));
			data.append(crushPanel.save());
			data.append("<SLASH>" + System.getProperty("line.separator"));
			data.append(slashPanel.save());
			data.append("<PARRY>" + System.getProperty("line.separator"));
			data.append("0.55" + System.getProperty("line.separator"));
			data.append("<BREAK>" + System.getProperty("line.separator"));
			data.append("<DEFEND>" + System.getProperty("line.separator"));
			data.append("-1.0" + System.getProperty("line.separator"));
			data.append("<BREAK>" + System.getProperty("line.separator"));
			data.append("<END>" + System.getProperty("line.separator"));
		} else if(magicButton.isSelected()) {
			data.append("<SPELL1>" + System.getProperty("line.separator"));
			data.append("-1.0" + System.getProperty("line.separator"));
			data.append("<BREAK>" + System.getProperty("line.separator"));
			data.append("<SPELL2>" + System.getProperty("line.separator"));
			data.append("-1.0" + System.getProperty("line.separator"));
			data.append("<BREAK>" + System.getProperty("line.separator"));
			data.append("<SPELL3>" + System.getProperty("line.separator"));
			data.append("-1.0" + System.getProperty("line.separator"));
			data.append("<BREAK>" + System.getProperty("line.separator"));
			data.append("<ATTACK>" + System.getProperty("line.separator"));
			data.append(attackPanel.save());
			data.append("<DEFEND>" + System.getProperty("line.separator"));
			data.append("-1.0" + System.getProperty("line.separator"));
			data.append("<BREAK>" + System.getProperty("line.separator"));
			data.append("<END>" + System.getProperty("line.separator"));
		} else if(rangedButton.isSelected()) {
			data.append("<HEADSHOT>" + System.getProperty("line.separator"));
			data.append("-1.0" + System.getProperty("line.separator"));
			data.append("<BREAK>" + System.getProperty("line.separator"));
			data.append("<BODYSHOT>" + System.getProperty("line.separator"));
			data.append("-1.0" + System.getProperty("line.separator"));
			data.append("<BREAK>" + System.getProperty("line.separator"));
			data.append("<CRIPPLE>" + System.getProperty("line.separator"));
			data.append("-1.0" + System.getProperty("line.separator"));
			data.append("<BREAK>" + System.getProperty("line.separator"));
			data.append("<ATTACK>" + System.getProperty("line.separator"));
			data.append(attackPanel.save());
			data.append("<DEFEND>" + System.getProperty("line.separator"));
			data.append("-1.0" + System.getProperty("line.separator"));
			data.append("<BREAK>" + System.getProperty("line.separator"));
			data.append("<END>" + System.getProperty("line.separator"));
		} else {
			data.append("<THRUST>" + System.getProperty("line.separator"));
			data.append("0.45" + System.getProperty("line.separator"));
			data.append("<BREAK>" + System.getProperty("line.separator"));
			data.append("<END>" + System.getProperty("line.separator"));
		}
		
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/database/weapons"));

			String line;
			StringBuilder fileContent = new StringBuilder();
			boolean makeNew = true;
			while((line = reader.readLine()) != null) {
				if(line.matches("-" + nameText.getText() + "-")) {
					fileContent.append(data);
					makeNew = false;
					
					while(!(line = reader.readLine()).matches("<END>")) {
						
					}
				} else {
					fileContent.append(line);
					fileContent.append(System.getProperty("line.separator"));
				}
			}
			if(makeNew) {
				fileContent.append(System.getProperty("line.separator"));
				fileContent.append(data);
			}

			BufferedWriter out = new BufferedWriter(new FileWriter(System.getProperty("resources") + "/database/weapons"));
			out.write(fileContent.toString());
			out.close();
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("The weapon database has been misplaced!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Weapon database failed to load!");
			e.printStackTrace();
		}
	}
	
	public void load() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/database/weapons"));
			
			String line;
			while((line = reader.readLine()) != null) {
				if(line.matches("-" + nameText.getText() + "-")) {
					while(!(line = reader.readLine()).matches("<END>")) {
						if(line.matches("<THRUST>")) {
							thrustPanel.load(reader);
						} else if(line.matches("<CRUSH>")) {
							crushPanel.load(reader);
						} else if(line.matches("<SLASH>")) {
							slashPanel.load(reader);
						} else if(line.matches("<ATTACK>")) {
							attackPanel.load(reader);
						}
					}
				}
				repaint();
			}
			
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("The weapon database has been misplaced!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Weapon database failed to load!");
			e.printStackTrace();
		}
	}
}
