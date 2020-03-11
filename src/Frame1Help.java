import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;

public class Frame1Help extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */

	/**
	 * Create the dialog.
	 */
	public Frame1Help() {
		setTitle("Help");
		setBounds(100, 100, 600, 474);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel label = new JLabel("");
		
		JLabel lblAwefawef = new JLabel("<HTML>\n<b>Add Player</b>: Adds a new player to the roster <br>\n<b>Edit Player</b>: Select two player from the roster and edit their info <br>\n<b>Delete Player</b>: Select one player from the roster and delete them <br>\n<b>- WARNING - THIS WILL DELETE THE PLAYER'S AVERAGE RANKING, WHICH IS UNRECOVERABLE</b><br> <br>\n<b>Swap</b>: Select two players, their rankings are swapped <br>\n<b>Beat</b>: Select two players, simulates if one player beats another. The lower player gets the higher player's rank and everyone else moves down. <br>\n<b>Absent</b>: Toggles if a player is absent. If they are marked absent, they will not be taken into consideration for the algorithm<br> <br>\n<b>- Advanced - </b><br>\nSelect a player, and press the advanced button. This will show the advanced settings and information for a player. <br>\n<b>Partner</b>: Select a player from the list. \"Set\" will set the selected player as the partner. \"Unset\" will remove the current partner (if applicable). Players may only have 1 partner. <br>\nWhen two players are partners, the algorithm will try it's best putting them into the same station when it is marked as doubles. <br>\n<b>Average Ranking</b>: Every time stations are generated and it is converted to word, it will automatically update every player's average ranking by saving their current ranking.\n<b>PlayerID </b>: Simply a mechanism so that the system can recognize a player even if their information is changed. Each player is assigned a unique PlayerID whenever they are created, and it can not be changed.\n\n");
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(185)
							.addComponent(label))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblAwefawef, GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(label)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblAwefawef)
					.addContainerGap(105, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
}
