package core.editor;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class EditScrollPane extends JScrollPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public EditPanel panel;
	
	/**
	 * Create the panel.
	 */
	public EditScrollPane(Dimension size) {
		setPreferredSize(size);
		setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		setAutoscrolls(true);
		
		panel = new EditPanel(size);
		add(panel);
		setViewportView(panel);
		panel.setLayout(null);
				
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				Rectangle r = new Rectangle(arg0.getX(), arg0.getY(), 1, 1);
				((JScrollPane)arg0.getSource()).scrollRectToVisible(r);
			}
		});
	}
}
