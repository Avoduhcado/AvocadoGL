package core.editor.equipment;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JPanel;

import core.editor.entities.sprites.EditSprite;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JSeparator;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class WeaponPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EditSprite sprite;
	private int maxDir, maxFrame;
	private JTextField totalText;
	private JComboBox<String> timeComboBox;
	private JSpinner timeSpinner;
	private JComboBox<String> frameComboBox;
	private SpritePanel spritePanel;
	private JPanel panel;
	private JScrollPane scrollPane;
	
	private float[] swingTimes;
	private Rectangle[] hitboxes;
	private Rectangle rectToDraw = new Rectangle(0,0,0,0);

	/**
	 * Create the panel.
	 */
	public WeaponPanel(String name) {
		setSize(new Dimension(585, 300));
		setLayout(null);
		
		try {
			this.sprite = new EditSprite(System.getProperty("resources") + "/sprites/" + name + ".png");
			maxDir = this.sprite.getMaxDir();
			maxFrame = this.sprite.getMaxFrame();
		} catch(Exception e) {
			sprite = null;
		}
		
		panel = new JPanel();
		panel.setBounds(0, 0, 585, 300);
		add(panel);
		panel.setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 13, 325, 245);
		panel.add(scrollPane);
		
		spritePanel = new SpritePanel();
		spritePanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				rectToDraw.setFrame(e.getX(), e.getY(), rectToDraw.width, rectToDraw.height);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				rectToDraw.setFrameFromDiagonal(rectToDraw.x, rectToDraw.y, e.getX(), e.getY());
				hitboxes[frameComboBox.getSelectedIndex()] = new Rectangle(rectToDraw.x - ((frameComboBox.getSelectedIndex() % maxFrame) * sprite.getSprite().getWidth() / maxFrame), 
						rectToDraw.y - ((frameComboBox.getSelectedIndex() / maxDir) * sprite.getSprite().getHeight() / maxDir), rectToDraw.width, rectToDraw.height);
				spritePanel.repaint();
			}
		});
		spritePanel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				rectToDraw.setFrameFromDiagonal(rectToDraw.x, rectToDraw.y, e.getX(), e.getY());
				spritePanel.repaint();
			}
		});
		spritePanel.setPreferredSize(new Dimension(322, 242));
		scrollPane.setViewportView(spritePanel);
		
		JLabel label = new JLabel("Swing Time");
		label.setBounds(366, 16, 62, 16);
		panel.add(label);
		
		totalText = new JTextField();
		totalText.setEditable(false);
		totalText.setBounds(440, 13, 116, 22);
		panel.add(totalText);
		totalText.setColumns(10);
		
		timeComboBox = new JComboBox<String>();
		timeComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timeSpinner.setValue(swingTimes[timeComboBox.getSelectedIndex()]);
			}
		});
		timeComboBox.setModel(buildSwingBox());
		timeComboBox.setBounds(366, 45, 103, 22);
		panel.add(timeComboBox);
		
		timeSpinner = new JSpinner();
		timeSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				swingTimes[timeComboBox.getSelectedIndex()] = (Float) timeSpinner.getValue();
				float total = 0;
				for(int x = 0; x<swingTimes.length; x++) {
					total += swingTimes[x];
				}
				totalText.setText(total + " seconds");
			}
		});
		timeSpinner.setBounds(481, 47, 75, 20);
		panel.add(timeSpinner);
		timeSpinner.setModel(new SpinnerNumberModel(new Float(0), new Float(0), null, new Float(1)));
		if(swingTimes.length > 0)
			timeSpinner.setValue(swingTimes[0]);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(366, 80, 207, 1);
		panel.add(separator);
		
		JLabel label_1 = new JLabel("Frame Box");
		label_1.setBounds(366, 94, 55, 16);
		panel.add(label_1);
		
		frameComboBox = new JComboBox<String>();
		frameComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rectToDraw.setFrame(hitboxes[frameComboBox.getSelectedIndex()].x + ((frameComboBox.getSelectedIndex() % maxFrame) * sprite.getSprite().getWidth() / maxFrame),
						hitboxes[frameComboBox.getSelectedIndex()].y + ((frameComboBox.getSelectedIndex() / maxDir) * sprite.getSprite().getHeight() / maxDir), 
						hitboxes[frameComboBox.getSelectedIndex()].width, hitboxes[frameComboBox.getSelectedIndex()].height);
				spritePanel.repaint();
			}
		});
		frameComboBox.setModel(buildFrameBox());
		frameComboBox.setBounds(366, 123, 103, 22);
		panel.add(frameComboBox);
	}
	
	
	public DefaultComboBoxModel<String> buildSwingBox() {
		DefaultComboBoxModel<String> swingBoxModel = new DefaultComboBoxModel<String>();
		
		swingTimes = new float[maxFrame];
		for(int x = 0; x<swingTimes.length; x++) {
			swingBoxModel.addElement("Frame " + x);
			swingTimes[x] = 1.0f / maxFrame;
		}
		
		float total = 0;
		for(int x = 0; x<swingTimes.length; x++) {
			total += swingTimes[x];
		}
		totalText.setText(total + " seconds");
		
		return swingBoxModel;
	}
	
	public DefaultComboBoxModel<String> buildFrameBox() {
		DefaultComboBoxModel<String> frameBoxModel = new DefaultComboBoxModel<String>();
		
		hitboxes = new Rectangle[maxDir * maxFrame];
		for(int x = 0; x<hitboxes.length; x++) {
			frameBoxModel.addElement("Rect " + (x / maxDir) + ", " + (x % maxFrame));
			hitboxes[x] = new Rectangle(0,0,0,0);
		}
		
		return frameBoxModel;
	}
	
	public EditSprite getSprite() {
		return sprite;
	}
	
	public void updateSprite(String name) {
		try {
			this.sprite = new EditSprite(System.getProperty("resources") + "/sprites/" + name + ".png");
			maxDir = this.sprite.getMaxDir();
			maxFrame = this.sprite.getMaxFrame();
			
			
		} catch(Exception e) {
			sprite = null;
			maxDir = 1;
			maxFrame = 1;
		}
		
		timeComboBox.setModel(buildSwingBox());
		frameComboBox.setModel(buildFrameBox());
		if(swingTimes.length > 0)
			timeSpinner.setValue(swingTimes[0]);
	}
	
	class SpritePanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			
			g2.setColor(Color.LIGHT_GRAY);
			g2.fillRect(0, 0, getPreferredSize().width, getPreferredSize().height);
			
			if(sprite != null) {
				sprite.draw(g2, null);
				
				g2.setColor(Color.RED);
				for(int x = 0; x<maxFrame + 1; x++) {
					for(int y = 0; y<maxDir + 1; y++) {
						g2.drawLine(0, y * (sprite.getSprite().getHeight() / maxDir), sprite.getSprite().getWidth(), y * (sprite.getSprite().getHeight() / maxDir));
						g2.drawLine(x * (sprite.getSprite().getWidth() / maxFrame), 0, x * (sprite.getSprite().getWidth() / maxFrame), sprite.getSprite().getHeight());
					}
				}
				
				g2.setColor(Color.BLUE);
				g2.drawRect(rectToDraw.x, rectToDraw.y, rectToDraw.width, rectToDraw.height);
			}
		}
	}
	
	public StringBuilder save() {
		StringBuilder data = new StringBuilder();
		
		data.append(totalText.getText().substring(0, totalText.getText().indexOf(' ')));
		for(int x = 0; x<swingTimes.length; x++) {
			data.append(";" + swingTimes[x]);
		}
		data.append(System.getProperty("line.separator"));
		
		int i = 0;
		if(maxDir != 0 && maxFrame != 0) {
			for(int x = 0; x<hitboxes.length / maxDir; x++) {
				for(int y = 0; y<hitboxes.length / maxFrame; y++) {
					data.append(hitboxes[i].x + ";" + hitboxes[i].y + ";" + hitboxes[i].width + ";" + hitboxes[i].height + ";");
					i++;
				}
				data.deleteCharAt(data.lastIndexOf(";"));
				data.append(System.getProperty("line.separator"));
			}
		}
		data.append("<BREAK>" + System.getProperty("line.separator"));
		
		return data;
	}
	
	public void load(BufferedReader reader) {
		try {
			String line = reader.readLine();
			String[] temp = line.split(";");
			for(int x = 1; x<temp.length; x++) {
				swingTimes[x - 1] = Float.parseFloat(temp[x]);
			}
			if(swingTimes.length > 0)
				timeSpinner.setValue(swingTimes[0]);
			totalText.setText(temp[0] + " seconds");
			
			int i = 0;
			while(!(line = reader.readLine()).matches("<BREAK>")) {
				temp = line.split(";");
				for(int x = 0; x<temp.length; x++) {
					hitboxes[i] = new Rectangle(Integer.parseInt(temp[x]), Integer.parseInt(temp[x+=1]), Integer.parseInt(temp[x+=1]), Integer.parseInt(temp[x+=1]));
					i++;
				}
			}
			
			if(hitboxes.length > 0)
				rectToDraw.setFrame(hitboxes[0]);
		} catch (FileNotFoundException e) {
	    	System.out.println("The weapon database has been misplaced!");
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	System.out.println("Weapon database failed to load!");
	    	e.printStackTrace();
	    }
	}

}
