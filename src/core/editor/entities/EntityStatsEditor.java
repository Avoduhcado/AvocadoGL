package core.editor.entities;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class EntityStatsEditor extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String stats = "";
	private JSpinner spinMaxHp;
	private JSpinner spinMaxHpBuff;
	private JSpinner spinMaxHpBuffTime;
	private JSpinner spinHp;
	private JSpinner spinHpBuff;
	private JSpinner spinHpBuffTime;
	private JSpinner spinHpTallie;
	private JSpinner spinHpAvg;
	private JSpinner spinMaxVig;
	private JSpinner spinMaxVigBuff;
	private JSpinner spinMaxVigBuffTime;
	private JSpinner spinVig;
	private JSpinner spinVigBuff;
	private JSpinner spinVigBuffTime;
	private JSpinner spinVigTallie;
	private JSpinner spinVigAvg;
	private JSpinner spinEssBuffTime;
	private JSpinner spinMaxEss;
	private JSpinner spinEssBuff;
	private JSpinner spinMaxEssBuffTime;
	private JSpinner spinMaxEssBuff;
	private JSpinner spinEss;
	private JSpinner spinEssAvg;
	private JSpinner spinEssTallie;
	private JSpinner spinVivTime;
	private JSpinner spinVivActive;
	private JSpinner spinMaxVivBuff;
	private JSpinner spinMaxVivBuffTime;
	private JSpinner spinVivBuffTime;
	private JSpinner spinVivBuff;
	private JSpinner spinViv;
	private JSpinner spinMaxViv;

	/**
	 * Create the dialog.
	 */
	public EntityStatsEditor(String savedStats) {
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Stat Editor");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 434, 237);
		getContentPane().add(tabbedPane);
		
		JPanel vitTab = new JPanel();
		tabbedPane.addTab("Vitality", null, vitTab, null);
		tabbedPane.setEnabledAt(0, true);
		vitTab.setLayout(null);

		JLabel lblMaxHp = new JLabel("Max HP");
		lblMaxHp.setBounds(10, 11, 86, 14);
		vitTab.add(lblMaxHp);
		
		spinMaxHp = new JSpinner();
		spinMaxHp.setModel(new SpinnerNumberModel(new Float(15), new Float(0), null, new Float(1)));
		lblMaxHp.setLabelFor(spinMaxHp);
		spinMaxHp.setBounds(106, 8, 70, 20);
		vitTab.add(spinMaxHp);

		JLabel lblMaxBuffHp = new JLabel("Max Buff HP");
		lblMaxBuffHp.setBounds(10, 36, 86, 14);
		vitTab.add(lblMaxBuffHp);
		
		spinMaxHpBuff = new JSpinner();
		spinMaxHpBuff.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		lblMaxBuffHp.setLabelFor(spinMaxHpBuff);
		spinMaxHpBuff.setBounds(106, 33, 70, 20);
		vitTab.add(spinMaxHpBuff);

		JLabel lblMaxBuffTime = new JLabel("Max Buff Time");
		lblMaxBuffTime.setBounds(10, 61, 86, 14);
		vitTab.add(lblMaxBuffTime);
		
		spinMaxHpBuffTime = new JSpinner();
		spinMaxHpBuffTime.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		lblMaxBuffTime.setLabelFor(spinMaxHpBuffTime);
		spinMaxHpBuffTime.setBounds(106, 58, 70, 20);
		vitTab.add(spinMaxHpBuffTime);

		JLabel lblHp = new JLabel("HP");
		lblHp.setBounds(10, 86, 70, 14);
		vitTab.add(lblHp);
		
		spinHp = new JSpinner();
		spinHp.setModel(new SpinnerNumberModel(new Float(15), new Float(0), null, new Float(1)));
		spinHp.setBounds(106, 83, 70, 20);
		vitTab.add(spinHp);
		
		JLabel lblBuffHp = new JLabel("HP Buff");
		lblBuffHp.setBounds(10, 111, 86, 14);
		vitTab.add(lblBuffHp);
		
		spinHpBuff = new JSpinner();
		spinHpBuff.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		spinHpBuff.setBounds(106, 108, 70, 20);
		vitTab.add(spinHpBuff);
		
		JLabel lblHpBuffTime = new JLabel("HP Buff Time");
		lblHpBuffTime.setBounds(10, 136, 86, 14);
		vitTab.add(lblHpBuffTime);
		
		spinHpBuffTime = new JSpinner();
		spinHpBuffTime.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		spinHpBuffTime.setBounds(106, 133, 70, 20);
		vitTab.add(spinHpBuffTime);
		
		JLabel lblAverage = new JLabel("Average");
		lblAverage.setBounds(186, 11, 46, 14);
		vitTab.add(lblAverage);
		
		JLabel lblNewLabel = new JLabel("Tallie");
		lblNewLabel.setBounds(186, 36, 46, 14);
		vitTab.add(lblNewLabel);
		
		spinHpAvg = new JSpinner();
		spinHpAvg.setModel(new SpinnerNumberModel(new Float(0), new Float(0), null, new Float(1)));
		spinHpAvg.setBounds(242, 8, 70, 20);
		vitTab.add(spinHpAvg);
		
		spinHpTallie = new JSpinner();
		spinHpTallie.setModel(new SpinnerNumberModel(new Float(0), new Float(0), null, new Float(1)));
		spinHpTallie.setBounds(242, 33, 70, 20);
		vitTab.add(spinHpTallie);
		
		JPanel vigTab = new JPanel();
		tabbedPane.addTab("Vigor", null, vigTab, null);
		vigTab.setLayout(null);

		JLabel lblMaxStr = new JLabel("Max STR");
		lblMaxStr.setBounds(10, 11, 86, 14);
		vigTab.add(lblMaxStr);
		
		spinMaxVig = new JSpinner();
		spinMaxVig.setModel(new SpinnerNumberModel(new Float(30), new Float(0), null, new Float(1)));
		spinMaxVig.setBounds(106, 8, 70, 20);
		vigTab.add(spinMaxVig);
		
		JLabel lblMaxBuffStr = new JLabel("Max Buff STR");
		lblMaxBuffStr.setBounds(10, 36, 86, 14);
		vigTab.add(lblMaxBuffStr);
		
		spinMaxVigBuff = new JSpinner();
		spinMaxVigBuff.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		spinMaxVigBuff.setBounds(106, 33, 70, 20);
		vigTab.add(spinMaxVigBuff);

		JLabel lblMaxBuffTime_1 = new JLabel("Max Buff Time");
		lblMaxBuffTime_1.setBounds(10, 61, 86, 14);
		vigTab.add(lblMaxBuffTime_1);
		
		spinMaxVigBuffTime = new JSpinner();
		spinMaxVigBuffTime.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		spinMaxVigBuffTime.setBounds(106, 58, 70, 20);
		vigTab.add(spinMaxVigBuffTime);

		JLabel lblStr = new JLabel("STR");
		lblStr.setBounds(10, 86, 86, 14);
		vigTab.add(lblStr);
		
		spinVig = new JSpinner();
		spinVig.setModel(new SpinnerNumberModel(new Float(30), new Float(0), null, new Float(1)));
		spinVig.setBounds(106, 83, 70, 20);
		vigTab.add(spinVig);

		JLabel lblStrBuff = new JLabel("STR Buff");
		lblStrBuff.setBounds(10, 111, 86, 14);
		vigTab.add(lblStrBuff);
		
		spinVigBuff = new JSpinner();
		spinVigBuff.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		spinVigBuff.setBounds(106, 108, 70, 20);
		vigTab.add(spinVigBuff);

		JLabel lblStrBuffTime = new JLabel("STR Buff Time");
		lblStrBuffTime.setBounds(10, 136, 86, 14);
		vigTab.add(lblStrBuffTime);
		
		spinVigBuffTime = new JSpinner();
		spinVigBuffTime.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		spinVigBuffTime.setBounds(106, 133, 70, 20);
		vigTab.add(spinVigBuffTime);
		
		JLabel lblAverage_1 = new JLabel("Average");
		lblAverage_1.setBounds(188, 12, 46, 14);
		vigTab.add(lblAverage_1);
		
		JLabel lblTallie = new JLabel("Tallie");
		lblTallie.setBounds(188, 37, 46, 14);
		vigTab.add(lblTallie);
		
		spinVigAvg = new JSpinner();
		spinVigAvg.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		spinVigAvg.setBounds(246, 9, 70, 20);
		vigTab.add(spinVigAvg);
		
		spinVigTallie = new JSpinner();
		spinVigTallie.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		spinVigTallie.setBounds(246, 34, 70, 20);
		vigTab.add(spinVigTallie);
		
		JPanel essTab = new JPanel();
		essTab.setName("");
		tabbedPane.addTab("Essence", null, essTab, null);
		essTab.setLayout(null);
		
		JLabel lblMaxEssence = new JLabel("Max Essence");
		lblMaxEssence.setBounds(12, 13, 86, 14);
		essTab.add(lblMaxEssence);
		
		JLabel lblMaxBuffEss = new JLabel("Max Buff Ess");
		lblMaxBuffEss.setBounds(12, 40, 86, 14);
		essTab.add(lblMaxBuffEss);
		
		JLabel lblMaxBuffTime_2 = new JLabel("Max Buff Time");
		lblMaxBuffTime_2.setBounds(12, 67, 86, 14);
		essTab.add(lblMaxBuffTime_2);
		
		JLabel lblEssence = new JLabel("Essence");
		lblEssence.setBounds(12, 94, 86, 14);
		essTab.add(lblEssence);
		
		JLabel lblBuffEssence = new JLabel("Essence Buff");
		lblBuffEssence.setBounds(12, 121, 86, 14);
		essTab.add(lblBuffEssence);
		
		JLabel lblEssenceBuffTime = new JLabel("Essence Buff Time");
		lblEssenceBuffTime.setBounds(12, 148, 86, 14);
		essTab.add(lblEssenceBuffTime);
		
		spinMaxEss = new JSpinner();
		spinMaxEss.setModel(new SpinnerNumberModel(new Float(30), new Float(0), null, new Float(1)));
		spinMaxEss.setBounds(110, 11, 70, 20);
		essTab.add(spinMaxEss);
		
		spinMaxEssBuff = new JSpinner();
		spinMaxEssBuff.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		spinMaxEssBuff.setBounds(110, 38, 70, 20);
		essTab.add(spinMaxEssBuff);
		
		spinMaxEssBuffTime = new JSpinner();
		spinMaxEssBuffTime.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		spinMaxEssBuffTime.setBounds(110, 65, 70, 20);
		essTab.add(spinMaxEssBuffTime);
		
		spinEss = new JSpinner();
		spinEss.setModel(new SpinnerNumberModel(new Float(30), new Float(0), null, new Float(1)));
		spinEss.setBounds(110, 92, 70, 20);
		essTab.add(spinEss);
		
		spinEssBuff = new JSpinner();
		spinEssBuff.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		spinEssBuff.setBounds(110, 119, 70, 20);
		essTab.add(spinEssBuff);
		
		spinEssBuffTime = new JSpinner();
		spinEssBuffTime.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		spinEssBuffTime.setBounds(110, 146, 70, 20);
		essTab.add(spinEssBuffTime);
		
		JLabel lblAverage_2 = new JLabel("Average");
		lblAverage_2.setBounds(192, 14, 46, 14);
		essTab.add(lblAverage_2);
		
		JLabel lblTallie_1 = new JLabel("Tallie");
		lblTallie_1.setBounds(192, 41, 46, 14);
		essTab.add(lblTallie_1);
		
		spinEssAvg = new JSpinner();
		spinEssAvg.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		spinEssAvg.setBounds(250, 11, 70, 20);
		essTab.add(spinEssAvg);
		
		spinEssTallie = new JSpinner();
		spinEssTallie.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		spinEssTallie.setBounds(250, 38, 70, 20);
		essTab.add(spinEssTallie);
		
		JPanel vivTab = new JPanel();
		tabbedPane.addTab("Vivacity", null, vivTab, null);
		vivTab.setLayout(null);
		
		JLabel lblMaxStamina = new JLabel("Max Stamina");
		lblMaxStamina.setBounds(12, 13, 86, 14);
		vivTab.add(lblMaxStamina);
		
		JLabel lblMaxBuffStamina = new JLabel("Max Buff Stam");
		lblMaxBuffStamina.setBounds(12, 40, 86, 14);
		vivTab.add(lblMaxBuffStamina);
		
		JLabel lblMaxBuffTime_3 = new JLabel("Max Buff Time");
		lblMaxBuffTime_3.setBounds(12, 67, 86, 14);
		vivTab.add(lblMaxBuffTime_3);
		
		JLabel lblStamina = new JLabel("Stamina");
		lblStamina.setBounds(12, 94, 86, 14);
		vivTab.add(lblStamina);
		
		JLabel lblStaminaBuff = new JLabel("Stamina Buff");
		lblStaminaBuff.setBounds(12, 121, 86, 14);
		vivTab.add(lblStaminaBuff);
		
		JLabel lblStaminaBuffTime = new JLabel("Stamina Buff Time");
		lblStaminaBuffTime.setBounds(12, 148, 86, 14);
		vivTab.add(lblStaminaBuffTime);
		
		spinMaxViv = new JSpinner();
		spinMaxViv.setModel(new SpinnerNumberModel(new Float(30), new Float(0), null, new Float(1)));
		spinMaxViv.setBounds(110, 11, 70, 20);
		vivTab.add(spinMaxViv);
		
		spinMaxVivBuff = new JSpinner();
		spinMaxVivBuff.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		spinMaxVivBuff.setBounds(110, 38, 70, 20);
		vivTab.add(spinMaxVivBuff);
		
		spinMaxVivBuffTime = new JSpinner();
		spinMaxVivBuffTime.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		spinMaxVivBuffTime.setBounds(110, 65, 70, 20);
		vivTab.add(spinMaxVivBuffTime);
		
		spinViv = new JSpinner();
		spinViv.setModel(new SpinnerNumberModel(new Float(30), new Float(0), null, new Float(1)));
		spinViv.setBounds(110, 92, 70, 20);
		vivTab.add(spinViv);
		
		spinVivBuff = new JSpinner();
		spinVivBuff.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		spinVivBuff.setBounds(110, 119, 70, 20);
		vivTab.add(spinVivBuff);
		
		spinVivBuffTime = new JSpinner();
		spinVivBuffTime.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		spinVivBuffTime.setBounds(110, 146, 70, 20);
		vivTab.add(spinVivBuffTime);
		
		JLabel lblActiveCount = new JLabel("Active Count");
		lblActiveCount.setBounds(192, 14, 70, 14);
		vivTab.add(lblActiveCount);
		
		spinVivActive = new JSpinner();
		spinVivActive.setBounds(274, 11, 70, 20);
		vivTab.add(spinVivActive);
		
		JLabel lblAvgTime = new JLabel("Avg Time");
		lblAvgTime.setBounds(192, 41, 70, 14);
		vivTab.add(lblAvgTime);
		
		spinVivTime = new JSpinner();
		spinVivTime.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		spinVivTime.setBounds(274, 38, 70, 20);
		vivTab.add(spinVivTime);
		
		{
			JButton okButton = new JButton("Save");
			okButton.setBounds(252, 239, 81, 23);
			getContentPane().add(okButton);
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						stats += "VITALITY;" + spinMaxHp.getValue() + "-" + spinMaxHpBuff.getValue() + "-" + spinMaxHpBuffTime.getValue()
								+ ";" + spinHp.getValue() + "-" + spinHpBuff.getValue() + "-" + spinHpBuffTime.getValue()
								+ ";" + spinHpAvg.getValue() + "-" + spinHpTallie.getValue();
						stats += "VIGOR;" + spinMaxVig.getValue() + "-" + spinMaxVigBuff.getValue() + "-" + spinMaxVigBuffTime.getValue()
								+ ";" + spinVig.getValue() + "-" + spinVigBuff.getValue() + "-" + spinVigBuffTime.getValue()
								+ ";" + spinVigAvg.getValue() + "-" + spinVigTallie.getValue();
						stats += "ESSENCE;" + spinMaxEss.getValue() + "-" + spinMaxEssBuff.getValue() + "-" + spinMaxEssBuffTime.getValue()
								+ ";" + spinEss.getValue() + "-" + spinEssBuff.getValue() + "-" + spinEssBuffTime.getValue()
								+ ";" + spinEssAvg.getValue() + "-" + spinEssTallie.getValue();
						stats += "VIVACITY;" + spinMaxViv.getValue() + "-" + spinMaxVivBuff.getValue() + "-" + spinMaxVivBuffTime.getValue()
								+ ";" + spinViv.getValue() + "-" + spinVivBuff.getValue() + "-" + spinVivBuffTime.getValue()
								+ ";" + spinVivActive.getValue() + "-" + spinVivTime.getValue();
						dispose();
					} catch(NumberFormatException nfe) {
						JOptionPane.showMessageDialog(null, "Values must be integers or floats");
					}
				}
			});
			okButton.setActionCommand("OK");
			getRootPane().setDefaultButton(okButton);
		}
		{
			JButton cancelButton = new JButton("Cancel");
			cancelButton.setBounds(343, 239, 81, 23);
			getContentPane().add(cancelButton);
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			cancelButton.setActionCommand("Cancel");
		}
		if(!savedStats.matches(""))
			loadStats(savedStats);
		
		setModalityType(ModalityType.APPLICATION_MODAL);
		setVisible(true);
	}
	
	public void loadStats(String stats) {
		String[] temp = stats.split("\n");
		// Vitality
		String[] singleTemp = temp[0].split(";");
		spinMaxHp.setValue(singleTemp[1]);
		spinMaxHpBuff.setValue(singleTemp[2]);
		spinMaxHpBuffTime.setValue(singleTemp[3]);
		spinHp.setValue(singleTemp[4]);
		spinHpBuff.setValue(singleTemp[5]);
		spinHpBuffTime.setValue(singleTemp[6]);
		spinHpAvg.setValue(singleTemp[7]);
		spinHpTallie.setValue(singleTemp[8]);
		// Vigor
		singleTemp = temp[1].split(";");
		spinMaxVig.setValue(singleTemp[1]);
		spinMaxVigBuff.setValue(singleTemp[2]);
		spinMaxVigBuffTime.setValue(singleTemp[3]);
		spinVig.setValue(singleTemp[4]);
		spinVigBuff.setValue(singleTemp[5]);
		spinVigBuffTime.setValue(singleTemp[6]);
		spinVigAvg.setValue(singleTemp[7]);
		spinVigTallie.setValue(singleTemp[8]);
		// Essence
		singleTemp = temp[2].split(";");
		spinMaxEss.setValue(singleTemp[1]);
		spinMaxEssBuff.setValue(singleTemp[2]);
		spinMaxEssBuffTime.setValue(singleTemp[3]);
		spinEss.setValue(singleTemp[4]);
		spinEssBuff.setValue(singleTemp[5]);
		spinEssBuffTime.setValue(singleTemp[6]);
		spinEssAvg.setValue(singleTemp[7]);
		spinEssTallie.setValue(singleTemp[8]);
		// Vivacity
		singleTemp = temp[3].split(";");
		spinMaxViv.setValue(singleTemp[1]);
		spinMaxVivBuff.setValue(singleTemp[2]);
		spinMaxVivBuffTime.setValue(singleTemp[3]);
		spinViv.setValue(singleTemp[4]);
		spinVivBuff.setValue(singleTemp[5]);
		spinVivBuffTime.setValue(singleTemp[6]);
		spinVivActive.setValue(singleTemp[7]);
		spinVivTime.setValue(singleTemp[8]);
	}
	
	public String getStats() {
		return stats;
	}
}
