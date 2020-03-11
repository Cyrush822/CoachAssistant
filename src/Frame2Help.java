import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Frame2Help extends JDialog {

	private final JPanel contentPanel = new JPanel();
	Frame2 frame;
	/**
	 * Launch the application.
	 */

	/**
	 * Create the dialog.
	 */
	public Frame2Help() {
		setModal(true);
		setTitle("Help");
		setBounds(100, 100, 575, 430);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		JLabel lblNewLabel = new JLabel("<html>\n<b>Add Station</b>: Adds a new station.<br>\n<b>Edit Station</b>: Select a station from the list, edit the configurations of that station.<br>\n<b>Delete Station</b>: Select a station from the list. Delete that station. <br>\n<br>\n<b>Disable</b>: Disables a station. It is no longer put into consideration for the algorithm. Useful for when players are absent.<br>\n<b><i>Temp</i></b>: Toggles temporary mode<br>\nWhen temporary mode is on, all changes are temporary. Any changes to the stations while temp mode is on will be wiped back to before the button was pressed when temporary mode is turned off. The changed settings and original settings are preserved even if the program is closed. Useful for when drastic changes need to be made but only for a short while. <br>\n<b><i> - WARNING - Once temporary mode is off, any changes while temporary mode was on will be permanently wiped. </i></b> <br><br>\n<b>Delete All</b>: Deletes all stations<br>\n<b>\"player: n\" label</b>: simply an indicator of the current number of non-absent players.<br>\n<b>min, pref (preferred), and max labels </b>: an indicator of the minimum, preferred and maximum number of players needed by every non-disabled station. Ensure that the number of present players are more than the minimum and lower than the maximum. <br>\n<b>for viewing a station's information:</b> double tap the station in the list.");
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
