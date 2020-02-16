import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.AbstractListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class AdvancedPlayerBox extends JDialog {
	private ArrayList<PartneredPlayer>  partneredPlayers;
	private JList<PartneredPlayer> partneredPlayersList;
	private Player player;
	private PartneredPlayer partneredPlayer;
	private MasterPlayerList playerList;
	
	private final JPanel contentPanel = new JPanel();
	private JLabel lblPartner;
	private JButton btnSet;
	private JButton btnunset;
	
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		try {
//			AdvancedPlayerBox dialog = new AdvancedPlayerBox();
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Create the dialog.
	 */
	public AdvancedPlayerBox(Player target, MasterPlayerList playerList) {
		initComponents();
		createEvents();
		this.player = target;
		this.partneredPlayer = new PartneredPlayer(target);
		this.playerList = playerList;
		partneredPlayers = new ArrayList<PartneredPlayer>();
		for(Player player : playerList.getPlayerList()) {
			if(player.equals(this.player)) {
				continue;
			}
			if(this.partneredPlayer.hasPartner()) {
				if(this.partneredPlayer.player.getPartner().equals(player))
				{
					continue;
				}
			}
			partneredPlayers.add(new PartneredPlayer(player));
		}
		partneredPlayersList.setModel(new AbstractListModel<PartneredPlayer>() {
			ArrayList<PartneredPlayer> values = partneredPlayers;
			public int getSize() {
				return values.size();
			}
			public PartneredPlayer getElementAt(int index) {
				return values.get(index);
			}
		});
		
		if(this.partneredPlayer.hasPartner()) {
			lblPartner.setText(partneredPlayer.getPlayer().getPartner().getName());
		} else {
			lblPartner.setText("none");
		}
		
		for(Player player : playerList.getPlayerList()) {
			if(target.getPartner() != null) {

				if(target.getPartner().equals(player)) {
					System.out.println("true: " + target.getPartner().getName());
				} else {
					System.out.println("no");
				}
			}
		}
	}
	void setPartner(PartneredPlayer newPartner) {
		if(this.partneredPlayer.hasPartner()) {
			int option = JOptionPane.showConfirmDialog(null, "this player already has a partner. Overwrite?");
			if(!(option == 0)) {//didn't put yes
				return;
			}
		}
		if(newPartner.hasPartner()) {
			int option = JOptionPane.showConfirmDialog(null, "the selected partner already has their own partner. Overwrite?");
			if(!(option == 0)) {//didn't put yes
				return;
			}
		}
		if(this.partneredPlayer.hasPartner()) { 
			this.partneredPlayer.getPlayer().getPartner().deletePartner();
			this.partneredPlayer.removePartner();
		}
		if(newPartner.hasPartner()) {
			newPartner.getPlayer().getPartner().deletePartner();
			newPartner.removePartner();
		}
		this.partneredPlayer.setPartner(newPartner.getPlayer());
		newPartner.setPartner(this.partneredPlayer.getPlayer());
		this.lblPartner.setText(newPartner.getPlayer().getName());
		updatePartneredPlayers();
		updatePartnerList();
	}
	void updatePartneredPlayers() {
		partneredPlayers = new ArrayList<PartneredPlayer>();
		for(Player player : playerList.getPlayerList()) {
			if(player.equals(this.player)) {
				continue;
			}
			if(this.partneredPlayer.hasPartner()) {
				if(this.partneredPlayer.player.getPartner().equals(player))
				{
					continue;
				}
			}
			partneredPlayers.add(new PartneredPlayer(player));
		}
	}
	void updatePartnerList() {
		partneredPlayersList.setModel(new AbstractListModel<PartneredPlayer>() {
			ArrayList<PartneredPlayer> values = partneredPlayers;
			public int getSize() {
				return values.size();
			}
			public PartneredPlayer getElementAt(int index) {
				return values.get(index);
			}
		});
		partneredPlayersList.repaint();
		partneredPlayersList.revalidate();
	}
	void unsetPartner() {
		this.partneredPlayer.getPlayer().getPartner().deletePartner();
		this.partneredPlayer.removePartner();
		this.lblPartner.setText("none");
		updatePartneredPlayers();
		updatePartnerList();
	}
	void createEvents() {
		btnSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PartneredPlayer target = partneredPlayersList.getSelectedValue();
				if(target == null) {
					return;
				}
				if(partneredPlayer.getPlayer().getPartner() != null && target != null) {
					if(partneredPlayer.getPlayer().getPartner().equals(target.getPlayer())) {
						JOptionPane.showMessageDialog(null, "they're already partners!");
						return;
					}
				}
				setPartner(target);
			}
			
		});
		btnunset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(partneredPlayer.hasPartner()) {
					unsetPartner();
				}
			}
		});
		
	}
	void initComponents() {
		setBounds(100, 100, 450, 230);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel lblSetDoublesPartner = new JLabel("Set Doubles Partner");
		
		btnSet = new JButton("<Set<");
		
		btnunset = new JButton(">Unset>");
		
		JLabel lblCurrentPartner = new JLabel("Current Partner");
		
		lblPartner = new JLabel("None");
		lblPartner.setHorizontalAlignment(SwingConstants.CENTER);
		
		JScrollPane scrollPane = new JScrollPane();
		
		JLabel lblAverageRanking = new JLabel("Average Ranking:");
		
		JLabel label = new JLabel("00.00");
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(47)
							.addComponent(lblCurrentPartner)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(lblSetDoublesPartner)
							.addGap(18))
						.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblPartner, GroupLayout.PREFERRED_SIZE, 172, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(btnunset, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnSet)))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 161, GroupLayout.PREFERRED_SIZE)))
					.addGap(148))
				.addGroup(Alignment.LEADING, gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblAverageRanking)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(label, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(352, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCurrentPartner)
						.addComponent(lblSetDoublesPartner))
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(12)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
								.addGroup(Alignment.LEADING, gl_contentPanel.createParallelGroup(Alignment.BASELINE)
									.addComponent(btnSet)
									.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addComponent(btnunset)
									.addPreferredGap(ComponentPlacement.RELATED))))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(37)
							.addComponent(lblPartner)))
					.addGap(31)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAverageRanking)
						.addComponent(label))
					.addGap(78))
		);
		
		partneredPlayersList = new JList();
		scrollPane.setViewportView(partneredPlayersList);
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
