package core.editor.actions;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class ActionEditor extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private String line;
	private JTextField textField;

	/**
	 * Create the dialog.
	 */
	public ActionEditor() {
		setTitle("Action Editor");
		setBounds(100, 100, 290, 500);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JButton btnDialogue = new JButton("Dialogue");
		btnDialogue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DialogueEditor dialogueEditor = new DialogueEditor();
				line = dialogueEditor.getDialogue();
				dispose();
			}
		});
		btnDialogue.setBounds(10, 11, 120, 23);
		contentPanel.add(btnDialogue);
		
		JButton btnChoice = new JButton("Choice");
		btnChoice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChoiceEditor choiceEditor = new ChoiceEditor();
				line = choiceEditor.getChoice();
				dispose();
			}
		});
		btnChoice.setBounds(10, 47, 120, 23);
		contentPanel.add(btnChoice);
		
		JButton btnInput = new JButton("Input");
		btnInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InputEditor inputEditor = new InputEditor();
				line = inputEditor.getInput();
				dispose();
			}
		});
		btnInput.setBounds(10, 83, 120, 23);
		contentPanel.add(btnInput);
		
		JButton btnTakeMoney = new JButton("Adjust Money");
		btnTakeMoney.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdjustMoneyEditor adjustMoneyEditor = new AdjustMoneyEditor();
				line = adjustMoneyEditor.getAdjust();
				dispose();
			}
		});
		btnTakeMoney.setBounds(10, 111, 120, 23);
		contentPanel.add(btnTakeMoney);
		
		JButton btnRevive = new JButton("Revive");
		btnRevive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				line = "@Revive:";
				dispose();
			}
		});
		btnRevive.setBounds(10, 147, 120, 23);
		contentPanel.add(btnRevive);
		
		JButton btnShowDebt = new JButton("Show Debt");
		btnShowDebt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				line = "@Show Debt:";
				dispose();
			}
		});
		btnShowDebt.setBounds(10, 181, 120, 23);
		contentPanel.add(btnShowDebt);
		
		JButton btnReduceDebt = new JButton("Reduce Debt");
		btnReduceDebt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				line = "@Reduce Debt:";
				dispose();
			}
		});
		btnReduceDebt.setBounds(10, 215, 120, 23);
		contentPanel.add(btnReduceDebt);
		
		JButton btnIncreaseDebt = new JButton("Raise Debt");
		btnIncreaseDebt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				line = "@Raise Debt:";
				dispose();
			}
		});
		btnIncreaseDebt.setBounds(10, 249, 120, 23);
		contentPanel.add(btnIncreaseDebt);
		
		JButton btnNewButton = new JButton("Adjust Variable");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VariableEditor variableEditor = new VariableEditor();
				line = variableEditor.getVariable();
				dispose();
			}
		});
		btnNewButton.setBounds(10, 285, 120, 25);
		contentPanel.add(btnNewButton);
		
		JButton btnAdjustStats = new JButton("Adjust Stats");
		btnAdjustStats.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AdjustStatEditor adjustStat = new AdjustStatEditor();
				line = adjustStat.getAdjust();
				dispose();
			}
		});
		btnAdjustStats.setBounds(10, 323, 120, 25);
		contentPanel.add(btnAdjustStats);
		
		JButton btnWait = new JButton("Wait");
		btnWait.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WaitEditor waitEditor = new WaitEditor();
				line = waitEditor.getWait();
				dispose();
			}
		});
		btnWait.setBounds(10, 361, 120, 25);
		contentPanel.add(btnWait);
		
		JButton btnConditional = new JButton("Conditional");
		btnConditional.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConditionalEditor conditionalEditor = new ConditionalEditor();
				line = conditionalEditor.getCondition();
				dispose();
			}
		});
		btnConditional.setBounds(10, 389, 120, 25);
		contentPanel.add(btnConditional);
		
		JButton btnBindSpells = new JButton("Bind Spells");
		btnBindSpells.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				line = "@Spell Bind:";
				dispose();
			}
		});
		btnBindSpells.setBounds(142, 10, 120, 25);
		contentPanel.add(btnBindSpells);
		
		JButton btnSelfSwitch = new JButton("Self Switches");
		btnSelfSwitch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SelfSwitchEditor selfSwitchEditor = new SelfSwitchEditor();
				line = selfSwitchEditor.getSwitches();
				dispose();
			}
		});
		btnSelfSwitch.setBounds(142, 46, 120, 25);
		contentPanel.add(btnSelfSwitch);
		
		JButton btnAdjustequipment = new JButton("Adjust Equipment");
		btnAdjustequipment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AdjustEquipmentEditor adjustEquipment = new AdjustEquipmentEditor();
				line = adjustEquipment.getAdjust();
				dispose();
			}
		});
		btnAdjustequipment.setBounds(142, 82, 120, 25);
		contentPanel.add(btnAdjustequipment);
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		setModalityType(ModalityType.APPLICATION_MODAL);
		setVisible(true);
	}
	
	public String getBranch() {
		return textField.getText();
	}
	
	public String getLine() {
		return line;
	}
}
