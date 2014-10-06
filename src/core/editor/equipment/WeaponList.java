package core.editor.equipment;

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

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class WeaponList extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	private JList<String> list;
	private String selection;

	/**
	 * Create the dialog.
	 */
	public WeaponList() {
		setTitle("Weapon List");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 325, 500);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			{
				list = new JList<String>();
				list.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if(e.getClickCount() == 2 && !e.isConsumed()) {
							makeSelection();
						}
					}
				});
				list.setModel(buildList());
				scrollPane.setViewportView(list);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						makeSelection();
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
	
	public DefaultComboBoxModel<String> buildList() {
		DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<String>();
		String line;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/database/weapons"));
			while((line = reader.readLine()) != null) {
				if(line.startsWith("-") && line.endsWith("-"))
					comboBoxModel.addElement(line.substring(1, line.length() - 1));
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("The item database has been misplaced!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Item database failed to load!");
			e.printStackTrace();
		}
		
		return comboBoxModel;
	}
	
	public void makeSelection() {
		if(!list.getSelectedValue().startsWith("-1")) {
			selection = list.getSelectedValue();
			
			dispose();
		}
	}
	
	public String getSelection() {
		return selection;
	}

}
