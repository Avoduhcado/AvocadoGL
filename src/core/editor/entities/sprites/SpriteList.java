package core.editor.entities.sprites;

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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.JTree;

public class SpriteList extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	
	private JTree tree;
	private String selection;

	/**
	 * Create the dialog.
	 */
	public SpriteList() {
		setTitle("Sprite List");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 325, 500);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			tree = new JTree(buildTree());
			tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			tree.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount() == 2 && !e.isConsumed() && tree.getSelectionPath() != null) {
						makeSelection();
					}
				}
			});
			contentPanel.add(tree, BorderLayout.CENTER);
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
	
	public DefaultMutableTreeNode buildTree() {
		DefaultMutableTreeNode root;
		DefaultMutableTreeNode grandparent;
		DefaultMutableTreeNode parent;
		
		root = new DefaultMutableTreeNode("Sprites");
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/database/sprites"));
			String line;
			while((line = reader.readLine()) != null) {
				String[] temp = line.split(";");
				if(temp[0].startsWith("-") && temp[0].endsWith("-") && !temp[0].matches("-TILES-")) {
					grandparent = new DefaultMutableTreeNode(temp[0]);
					while((line = reader.readLine()) != null && !line.matches("")) {
						temp = line.split(";");
						parent = new DefaultMutableTreeNode(temp[0]);
						grandparent.add(parent);
					}
					root.add(grandparent);
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
		
		return root;
	}
	
	public void makeSelection() {
		if(tree.getSelectionPath().getPathCount() > 2) {
			selection = tree.getSelectionPath().getLastPathComponent().toString();
			dispose();
		}
	}
	
	public String getSelection() {
		return selection;
	}

}
