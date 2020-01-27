import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DeletePlayerBox extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private MasterPlayerList playerList;
	private JTextField txtIndex;
	private JButton btnOK;
	Frame1 Frame;
	enum gender
	{
		male, female;
	}
	/**
	 * Launch the application.
	 */
	/**
	 * Create the dialog.
	 */
//	try {
//		EditPlayerBox dialog = new EditPlayerBox();
//		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//		dialog.setVisible(true);
//	} catch (Exception e) {
//		e.printStackTrace();
//	}
	public DeletePlayerBox(MasterPlayerList playerList, Frame1 F1) {
		this.playerList = playerList;
		Frame = F1;
		setTitle("Delete Player");
		setResizable(false);
		setModal(true);
		setAlwaysOnTop(true);
		setBounds(100, 100, 450, 130);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel lblDeletePlayer = new JLabel("Delete Player");
		lblDeletePlayer.setHorizontalAlignment(SwingConstants.CENTER);
		
		txtIndex = new JTextField();
		txtIndex.setColumns(10);
		
		JLabel lblIndex = new JLabel("Ranking");
		lblIndex.setHorizontalAlignment(SwingConstants.TRAILING);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(146)
							.addComponent(lblIndex, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtIndex, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE))
						.addComponent(lblDeletePlayer, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
					.addComponent(lblDeletePlayer)
					.addPreferredGap(ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblIndex)
						.addComponent(txtIndex, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				btnOK = new JButton("OK");
				btnOK.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
							int ranking;
							try {
								ranking = Integer.parseInt(txtIndex.getText());
							} 
							catch(Exception a){
								JOptionPane.showMessageDialog(null, "ERROR! Please input a number");
								return;
							}
							if(ranking > playerList.getLastRank() || ranking < 1) {
								JOptionPane.showMessageDialog(null, "ERROR! Please input a valid rank");
								return;
							}
							Frame.deletePlayer(ranking);
							setVisible(false);
							return;
					}
				});
				btnOK.setActionCommand("OK");
				buttonPane.add(btnOK);
				getRootPane().setDefaultButton(btnOK);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
