import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class StationHelpBox extends JDialog {

	private final JPanel contentPanel = new JPanel();
	AddStation parent;

	/**
	 * Create the dialog.
	 */
	public StationHelpBox(AddStation parent) {
		this.parent = parent;
		setTitle("Help");
		setBounds(100, 100, 700, 605);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		JLabel lblNewLabel = new JLabel("<html>\n<b>Station Name</b>: the name of the station<br>\n<b>Station tag </b>: the tag of the station. Stations with the exact same tag are deemed similar, and the algorithm will try to prevent people in stations with the exact same tag from being assigned any of those stations consecutively. <br>\nExample: If tables, Singles1, Singles2, and Singles3 all have the tag \"singles\", the algorithm will try to prevent every player in Singles1, Singles2, and Singles3 from getting into any one of Singles1, Singles2, and Singles3 in the next few configurations. <br> <br>\n<b>Minimum Number of Players</b>: the minimum number of players required for this station. This station will never have less players than this number<br>\n<b>Preferred Number of Players </b>: the preferred number of players for this station. The algorithm will try its best to reach this number.<br>\n<b>Maximum Number of Players </b>: the maximum number of players for this station. This station will never have more players than this number.<br>\n<br>\n<b>Must Be Evens</b>: The number of players in the station will always be even (TBI)<br>\n<b>Is competitive (only shows up if must be evens is ticked)</b> If ticked, will allow for the selection for Singles or Doubles<br>\n<b>Singles</b>: Automatically sets the min, pref, and max number of players to 2<br>\n<b>Doubles</b>: Automatically sets the min, pref, and max number of players to 4, and enables the partner system for this configuration.<br>\n<br>\n\n<b>Take Ranks into Account</b>: Adjust the number spinner here for the maximum rank difference between players in a station. Example: If set to 3 and one player in the station is rank 6, the next player must be rank 3-9. <br>\nIf, for example, more than 2 players are required for this station, the algorithm will try its best to put ONLY players from rank 3-9, rather than from the rank range of the second player.<br>\n<b>Gender Preference</b>: Adjust the setting here to indicate if gender mixing should be encouraged or discouraged by the algorithm.<br>\n<b>Rank Preference</b>: Adjust the setting here to indicate whether to prioritize or avoid players in the given rank range by the two spinners on the right (only shows up if \"Allow\" isn't chosen)<br>\n<b>Tables</b>: Number of tables needed by this station. Singles require 2. ");
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addContainerGap(373, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addContainerGap(207, Short.MAX_VALUE))
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
