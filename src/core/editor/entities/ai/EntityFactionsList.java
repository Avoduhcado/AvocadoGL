package core.editor.entities.ai;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import javax.swing.ListSelectionModel;

public class EntityFactionsList extends JDialog {

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
	public EntityFactionsList() {
		setTitle("Factions List");
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
				list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				list.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						list.setSelectedIndex(list.locationToIndex(e.getPoint()));
						if(e.getClickCount() == 2) {
							makeSelection();
						} else if(e.getButton() == MouseEvent.BUTTON3 && list.getSelectedIndex() != -1) {
							addPopup(e.getX(), e.getY());
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
				JButton btnNew = new JButton("New");
				btnNew.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						@SuppressWarnings("unused")
						NewFactionEditor factionEditor = new NewFactionEditor();
						list.setModel(buildList());
					}
				});
				buttonPane.add(btnNew);
			}
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
	
	public DefaultListModel<String> buildList() {
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		String line;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/database/factions"));
			
			while((line = reader.readLine()) != null) {
				listModel.addElement(line);
			}
			
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("The factions database has been misplaced!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Factions database failed to load!");
			e.printStackTrace();
		}
		
		return listModel;
	}
	
	public void makeSelection() {
		if(list.getSelectedIndex() != -1) {
			selection = list.getSelectedValue();
			
			dispose();
		}
	}
	
	public String getSelection() {
		return selection;
	}
	
	public void save() throws IOException {
		File file = new File(System.getProperty("resources") + "/database/factions");
		if(file.exists()) {
			file.delete();
			file.createNewFile();
		} else {
			file.createNewFile();
		}
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		
		for(int x = 0; x<list.getModel().getSize(); x++) {
			writer.write(list.getModel().getElementAt(x));
			writer.newLine();
		}
		
		writer.close();
	}

	public void addPopup(int x, int y) {
		JMenuItem item = new JMenuItem();
		
		JPopupMenu menu = new JPopupMenu();
		item = new JMenuItem("Rename");
		if(list.getSelectedIndex() == -1)
			item.setEnabled(false);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				@SuppressWarnings("unused")
				RenameFactionEditor factionEditor = new RenameFactionEditor(list.getSelectedValue());
				list.setModel(buildList());
			}
		});
		menu.add(item);
		
		item = new JMenuItem("Remove");
		if(list.getSelectedIndex() == -1)
			item.setEnabled(false);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				((DefaultListModel<String>) list.getModel()).removeElementAt(list.getSelectedIndex());
				try {
					save();
				} catch (IOException e) {
					e.printStackTrace();
				}
				list.setModel(buildList());
			}
		});
		menu.add(item);
		menu.show(list, x, y);
	}

}
