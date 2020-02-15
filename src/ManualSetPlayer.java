import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.AbstractListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

public class ManualSetPlayer extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JList<Player> listAvailable;
	private JButton btnAdd;
	private JButton btnDelete;
	private FinalStation station;
	private FinalStationMasterList stationList;
	private JButton okButton;
	private JButton btnShowInfo;
	private JLabel lblMinPlayers;
	private JLabel lblPrefPlayers;
	private JLabel lblMaxPlayers;
	private Frame3 frame;
	private JList<Player> listStation;
	/**
	 * Launch the application.
	 */

	/**
	 * Create the dialog.
	 */
	
	public ManualSetPlayer(FinalStation Fstation, FinalStationMasterList stationList, Frame3 frame) {
		this.stationList = stationList;
		this.station = Fstation;
		this.frame = frame;
		initComponents();
		updateModels();
		createEvents();
		UpdatePlayerNumberInfo();
		updatePlayerNumberInfoColors();
	}
	private void updateModels() {
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
		listStation.setModel(new AbstractListModel<Player>() {
			//private static final long serialVersionUID = 1L;
			ArrayList<Player> values = station.getCurrentPlayers();
			public int getSize() {
				return values.size();
			}
			public Player getElementAt(int index) {
				return values.get(index);
			}
		});
		updateLists();
	}	
	private void updateLists() {
		listStation.validate();
		listStation.repaint();
		listAvailable.validate();
		listAvailable.repaint();
	}
	private void addPlayer(Player player) {
		if(player != null) {
			station.manualAddPlayer(player);
			stationList.updateAvailablePlayers();
			updateModels();
			updatePlayerNumberInfoColors();
		}
	}
	private void removePlayer(Player player) {
		if(player!= null){
			station.manualRemovePlayer(player);
			stationList.updateAvailablePlayers();
			updateModels();
			updatePlayerNumberInfoColors();
		}
	}
	public void createEvents() {
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!station.isFull()) {
					addPlayer(listAvailable.getSelectedValue());
				} else {
					JOptionPane.showMessageDialog(null, "The station is already full!"
							+ " Please remove somebody first.");
				}
			}
		});
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removePlayer(listStation.getSelectedValue());
			}
		});
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.updatePlayersLeft();
				setVisible(false);
				
			}
		});
		btnShowInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StationInfo info = new StationInfo(station.getStation());
				info.setVisible(true);
			}
		});
	}
	
	public void UpdatePlayerNumberInfo() {
		lblMinPlayers.setText("Min Players: " + station.getStation().getMinPlayers());
		lblPrefPlayers.setText("Pref Players: " + station.getStation().getPreferredPlayers());
		lblMaxPlayers.setText("Max Players: " + station.getStation().getMaxPlayers());
	}
	
	public void updatePlayerNumberInfoColors() {
		int number = station.getCurrentPlayers().size();
		int min = station.getStation().getMinPlayers();
		int pref = station.getStation().getPreferredPlayers();
		int max = station.getStation().getMaxPlayers();
		if(number >= min) {
			if(number <= max) {
				lblMinPlayers.setForeground(Color.green);
			} else {
				lblMinPlayers.setForeground(Color.black);
			}
		} else {
			lblMinPlayers.setForeground(Color.red);
		}
		
		if(number <= max) {
			if(number >= min) {
				lblMaxPlayers.setForeground(Color.green);
			} else {
				lblMaxPlayers.setForeground(Color.black);
			}
		} else {
			lblMaxPlayers.setForeground(Color.red);
		}
		
		if(number == pref) {
			lblPrefPlayers.setForeground(Color.green);
		} else {
			lblPrefPlayers.setForeground(Color.black);
		}
	}
	public void initComponents() {
		setBounds(100, 100, 450, 342);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("Available Players");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblStation = new JLabel("Players in Station");
		lblStation.setHorizontalAlignment(SwingConstants.CENTER);
		
		btnAdd = new JButton("> Add >");
		
		btnDelete = new JButton("< Remove <");
		
		lblMinPlayers = new JLabel("Min Players:");
		
		lblPrefPlayers = new JLabel("Pref Players:");
		
		lblMaxPlayers = new JLabel("Max Players:");
		
		btnShowInfo = new JButton("Show Info");
		
		JScrollPane scrollPane = new JScrollPane();
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(lblMinPlayers, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
							.addGap(48))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
								.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
								.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
							.addGap(12)))
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(btnAdd, GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
										.addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE))
									.addGap(18))
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addComponent(btnShowInfo, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)))
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblStation, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
								.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(lblPrefPlayers, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
							.addComponent(lblMaxPlayers, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(10)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNewLabel)
								.addComponent(lblStation))
							.addGap(6)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 206, GroupLayout.PREFERRED_SIZE)
								.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 206, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED, 11, Short.MAX_VALUE))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(53)
							.addComponent(btnAdd)
							.addGap(35)
							.addComponent(btnShowInfo)
							.addPreferredGap(ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
							.addComponent(btnDelete)
							.addGap(37)))
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMinPlayers)
						.addComponent(lblPrefPlayers)
						.addComponent(lblMaxPlayers))
					.addContainerGap())
		);
		
		listStation = new JList<Player>();
		scrollPane_1.setViewportView(listStation);
		
		listAvailable = new JList<Player>();
		scrollPane.setViewportView(listAvailable);
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
