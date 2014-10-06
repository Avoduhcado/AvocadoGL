package core.editor.entities.sprites;

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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.JFileChooser;
import javax.swing.JTree;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class EditSpriteList extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	private JTree tree;
	private JTextField textField;
	private JComboBox<String> comboBox;
	private JSpinner spinner;
	private JSpinner spinner_1;

	/**
	 * Create the dialog.
	 */
	public EditSpriteList() {
		setTitle("Sprite List Editor");
		setBounds(100, 100, 450, 500);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JSplitPane splitPane = new JSplitPane();
			splitPane.setResizeWeight(0.65);
			contentPanel.add(splitPane, BorderLayout.CENTER);
			{
				JScrollPane scrollPane = new JScrollPane();
				splitPane.setLeftComponent(scrollPane);
				{
					tree = new JTree(buildTree());
					tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
					tree.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							if(e.getButton() == MouseEvent.BUTTON3) {
								tree.setSelectionPath(tree.getClosestPathForLocation(e.getX(), e.getY()));
								System.out.println("Popup");
							}
						}
					});
					scrollPane.setViewportView(tree);
				}
			}
			{
				JPanel panel = new JPanel();
				splitPane.setRightComponent(panel);
				panel.setLayout(null);
				
				JButton btnAdd = new JButton("Add");
				btnAdd.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if(!textField.getText().isEmpty()) {
							String temp = textField.getText().replaceFirst(".png", "") + ";" + spinner.getValue() + ";" + spinner_1.getValue();
							DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
							TreePath path;
							MutableTreeNode node = null;
							MutableTreeNode newNode = null;
							switch(comboBox.getSelectedIndex()) {
							case(0):
								path = tree.getNextMatch("-BACKDROPS-", 0, Position.Bias.Forward);
								node = (MutableTreeNode)path.getLastPathComponent();
								newNode = new DefaultMutableTreeNode(temp);
								break;
							case(1):
								path = tree.getNextMatch("-MAPS-", 0, Position.Bias.Forward);
								node = (MutableTreeNode)path.getLastPathComponent();
								newNode = new DefaultMutableTreeNode(temp);
								break;
							case(2):
								path = tree.getNextMatch("-PROPS-", 0, Position.Bias.Forward);
								node = (MutableTreeNode)path.getLastPathComponent();
								newNode = new DefaultMutableTreeNode(temp);
								break;
							case(3):
								path = tree.getNextMatch("-ACTORS-", 0, Position.Bias.Forward);
								node = (MutableTreeNode)path.getLastPathComponent();
								newNode = new DefaultMutableTreeNode(temp);
								break;
							case(4):
								path = tree.getNextMatch("-TILES-", 0, Position.Bias.Forward);
								node = (MutableTreeNode)path.getLastPathComponent();
								newNode = new DefaultMutableTreeNode(temp);
								break;
							}
							model.insertNodeInto(newNode, node, node.getChildCount());
							model.reload();
						}
					}
				});
				btnAdd.setBounds(10, 343, 89, 23);
				panel.add(btnAdd);
				
				JButton btnRemove = new JButton("Remove");
				btnRemove.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(tree.getSelectionPath() != null) {
							if(tree.getSelectionPath().getPathCount() > 2) {
								DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
								model.removeNodeFromParent((DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent());
							}
						}
					}
				});
				btnRemove.setBounds(10, 379, 89, 23);
				panel.add(btnRemove);
				
				textField = new JTextField();
				textField.setEnabled(false);
				textField.setBounds(10, 76, 89, 20);
				panel.add(textField);
				textField.setColumns(10);
				
				JLabel lblSpriteName = new JLabel("Sprite name");
				lblSpriteName.setBounds(10, 49, 85, 14);
				panel.add(lblSpriteName);
				
				comboBox = new JComboBox<String>();
				comboBox.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent arg0) {
						if(comboBox.getSelectedIndex() == 4) {
							spinner_1.setValue(1);
							spinner_1.setEnabled(false);
						} else {
							if(!spinner_1.isEnabled())
								spinner_1.setEnabled(true);
						}
					}
				});
				comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"BACKDROP", "MAP", "PROP", "ACTOR", "TILE"}));
				comboBox.setBounds(10, 136, 89, 20);
				panel.add(comboBox);
				
				JLabel lblSpriteType = new JLabel("Sprite Type");
				lblSpriteType.setBounds(10, 109, 85, 14);
				panel.add(lblSpriteType);
				{
					JLabel lblMaxFrame = new JLabel("Max Frame");
					lblMaxFrame.setBounds(10, 169, 89, 14);
					panel.add(lblMaxFrame);
				}
				
				spinner = new JSpinner();
				spinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
				spinner.setBounds(10, 196, 45, 20);
				panel.add(spinner);
				
				JLabel lblMaxDirection = new JLabel("Max Direction");
				lblMaxDirection.setBounds(10, 229, 90, 14);
				panel.add(lblMaxDirection);
				
				spinner_1 = new JSpinner();
				spinner_1.setModel(new SpinnerNumberModel(1, 1, 4, 1));
				spinner_1.setBounds(10, 256, 45, 20);
				panel.add(spinner_1);
				
				JButton btnLoadNew = new JButton("Load New");
				btnLoadNew.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						File file = null;
						try {
							@SuppressWarnings("serial")
							JFileChooser fileChooser = new JFileChooser() {{ 
								setCurrentDirectory(new File(System.getProperty("resources")));
								setFileFilter(new FileNameExtensionFilter("Images", "png"));
								setAcceptAllFileFilterUsed(false);
								setDialogTitle("Load new Sprite");
								showOpenDialog(null);
							}};
							
							file = fileChooser.getSelectedFile();
						} catch(Exception e) {
							e.printStackTrace();
						}
						if(file.exists()) {
							textField.setText(file.getName());
						}
					}
				});
				btnLoadNew.setBounds(10, 13, 89, 23);
				panel.add(btnLoadNew);
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
		try {
			File file = new File(System.getProperty("resources") + "/database/sprites");
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			
			file.setWritable(true);
			DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
			for(int x = 0; x<model.getChildCount(tree.getModel().getRoot()); x++) {
				writer.write(model.getChild(tree.getModel().getRoot(), x).toString());
				writer.newLine();
				Object child = model.getChild(model.getRoot(), x);
				for(int y = 0; y<model.getChildCount(child); y++) {
					writer.write(model.getChild(child, y).toString());
					writer.newLine();
				}
				writer.newLine();
			}
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
				if(line.startsWith("-") && line.endsWith("-")) {
					grandparent = new DefaultMutableTreeNode(line);
					while((line = reader.readLine()) != null && !line.matches("")) {
						parent = new DefaultMutableTreeNode(line);
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
}
