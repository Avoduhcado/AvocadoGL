package core.editor.entities.ai;

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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class RenameFactionEditor extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private String toChange;

	/**
	 * Create the dialog.
	 */
	public RenameFactionEditor(String toChange) {
		this.toChange = toChange;
		
		setTitle("Rename Faction");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 300, 150);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(10, 36, 138, 20);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		JLabel lblFactionName = new JLabel("Faction name:");
		lblFactionName.setBounds(10, 11, 68, 14);
		contentPanel.add(lblFactionName);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Save");
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
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/database/factions"));

			String line;
			StringBuilder fileContent = new StringBuilder();
			String newLine = null;
			while((line = reader.readLine()) != null) {
				String[] temp = line.split(";");

				if(temp[0].matches(toChange)) {
					newLine = textField.getText();
					fileContent.append(newLine);
					fileContent.append(System.getProperty("line.separator"));
				} else {
					fileContent.append(line);
					fileContent.append(System.getProperty("line.separator"));
				}
			}

			BufferedWriter out = new BufferedWriter(new FileWriter(System.getProperty("resources") + "/database/factions"));
			out.write(fileContent.toString());
			out.close();
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("The factions database has been misplaced!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Factions database failed to load!");
			e.printStackTrace();
		}
	}

}
