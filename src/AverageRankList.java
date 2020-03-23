import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.AbstractListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AverageRankList extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JList<playerAvgRankWrapper> Playerlist;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the dialog.
	 */
	
	public AverageRankList(MasterPlayerList playerList) {
		init();
		ArrayList<Player> players = playerList.getPlayerList();
		sortByAvgRank(players);
		ArrayList<playerAvgRankWrapper> wrappedPlayers = new ArrayList<playerAvgRankWrapper>();
		for(Player player : players) {
			wrappedPlayers.add(new playerAvgRankWrapper(player));
		}
		Playerlist.setModel(new AbstractListModel<playerAvgRankWrapper>() {
			ArrayList<playerAvgRankWrapper> values = wrappedPlayers;
			public int getSize() {
				return values.size();
			}
			public playerAvgRankWrapper getElementAt(int index) {
				return values.get(index);
			}
		});
	}
	public void sortByAvgRank(ArrayList<Player> players) {
		for(int i = players.size() - 1; i > 0; i--) {
			for(int a = 0; a < i; a++) {
				if(players.get(a).getAvgRank() > players.get(a+1).getAvgRank()) {
					Player holder = players.get(a);
					players.set(a, players.get(a+1));
					players.set(a+1, holder);
				}
			}
		}
	}
	private void init() {
		setBounds(100, 100, 450, 200);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 424, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(10, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(9, Short.MAX_VALUE))
		);
		
		Playerlist = new JList<playerAvgRankWrapper>();
		scrollPane.setViewportView(Playerlist);
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
