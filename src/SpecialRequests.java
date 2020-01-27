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
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JList;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class SpecialRequests extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private MasterPlayerList playerList;
	private MasterStationList stationList;
	private Station station;
	private JList<Player> listAvailable;
	private JLabel lblPlayers;
	private JList<Player> listStationPlayers;
	private JLabel lblMax;
	private JButton btnAdd;
	private JButton btnRemove;
	private JButton okButton;
	/**
	 * Launch the application.
	 */

	/**
	 * Create the dialog.
	 */
	public SpecialRequests(MasterPlayerList playerList, Station station, MasterStationList stationList) {
		this.playerList = playerList;
		this.station = station;
		this.stationList = stationList;
		initComponents();
		createEvents();
		updateList();
	}
	
	
	private void updateList() {
		listAvailable.setModel(new AbstractListModel<Player>() {
			//private static final long serialVersionUID = 1L;
			ArrayList<Player> values = stationList.getAvailablePlayers();
			
			public int getSize() {
				return values.size();
			}
			public Player getElementAt(int index) {
				return values.get(index);
			}
		});
		listStationPlayers.setModel(new AbstractListModel<Player>() {
			//private static final long serialVersionUID = 1L;
			ArrayList<Player> values = station.getPlayersList();//should have no absences
			
			public int getSize() {
				return values.size();
			}
			public Player getElementAt(int index) {
				return values.get(index);
			}
		});
	}
	private void createEvents() {
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(listAvailable.getSelectedValue() != null) {
					Player selected = listAvailable.getSelectedValue();
					station.addPlayer(selected);
					stationList.updatePlayerLists();
					updateList();
					station.saveTable();
				}
			}
		});
		
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(listStationPlayers.getSelectedValue() != null) {
					Player selected = listStationPlayers.getSelectedValue();
					station.deletePlayer(selected);
					stationList.updatePlayerLists();
					updateList();
					station.saveTable();
				}
			}
		});
		
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
	}
	private void initComponents() {
		setBounds(100, 100, 450, 325);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		JLabel lblAvailablePlayers = new JLabel("Available Players");
		lblAvailablePlayers.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel lblPlayersWithinStation = new JLabel("Players In Station");
		lblPlayersWithinStation.setHorizontalAlignment(SwingConstants.CENTER);
		listAvailable = new JList<Player>();
		
		listStationPlayers = new JList<Player>();
		
		btnAdd = new JButton("Add >");
		
		
		btnRemove = new JButton("< Remove");
		
		lblPlayers = new JLabel("Players:");
		lblPlayers.setHorizontalAlignment(SwingConstants.CENTER);
		
		lblMax = new JLabel("Maximum:");
		lblMax.setHorizontalAlignment(SwingConstants.CENTER);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblPlayers, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
						.addComponent(lblAvailablePlayers, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
						.addComponent(listAvailable, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(btnRemove, GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
						.addComponent(btnAdd, GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(lblMax, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(listStationPlayers, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
						.addComponent(lblPlayersWithinStation, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblPlayersWithinStation)
								.addComponent(lblAvailablePlayers))
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
								.addComponent(listStationPlayers, GroupLayout.PREFERRED_SIZE, 201, GroupLayout.PREFERRED_SIZE)
								.addComponent(listAvailable, GroupLayout.PREFERRED_SIZE, 201, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(59)
							.addComponent(btnAdd)
							.addPreferredGap(ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
							.addComponent(btnRemove)
							.addGap(36)))
					.addGap(9)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPlayers)
						.addComponent(lblMax)))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

}
