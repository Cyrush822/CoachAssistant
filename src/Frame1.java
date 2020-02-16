import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.LayoutStyle.ComponentPlacement;

import net.miginfocom.swing.MigLayout;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
public class Frame1 {

	private JFrame frmCissTableTennis;
	private JLabel FinalNum;
	private JPanel pnlLeftPlayers;
	private JRadioButton rdbtnNewRadioButton;
	private JPanel pnlRightPlayers;
	private JPanel[] playerLabelPanels;
	private JButton btnAdd;
	final static String playersFilePath = "players";
	static File file = new File(playersFilePath + ".txt");
	private JButton btnEdit;
	Player[] players;
	private JButton btnDelete;
	private JButton btnSwap;
	private JButton btnWon;
	private JButton btnAbsent;
	private JButton btnNext;

	MasterPlayerList playerList;
	MasterPlayerLabelList labelList;
	private JButton btnAdvanced;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame1 window = new Frame1();
					window.getFrmCissTableTennis().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create the application.
	 */
	public Frame1() {
		initComponents();
		createEvents();
		assignLists();
		recreatePartnerConnections();
	}
	/**
	 * initializes playerList and labelList. 
	 */
	public void assignLists() {
		playerList = new MasterPlayerList(new File(playersFilePath));
		labelList = new MasterPlayerLabelList(playerList, playerLabelPanels);
		updatePlayerLabels();
	}
	/**
	 * adds a new player. Automatically creates the player, adds it to the playerList,
	 * updates it in the labelList, and shows the change in information within the labels
	 * in frame1. Also saves the player. Rank is automatically determined by MasterPlayerList
	 * (last rank + 1)
	 * precondition: name must not already exist in dir. Check beforehand.
	 * @param name
	 * @param isMale
	 */
	public void addPlayer(String name, boolean isMale) {
		int rank = playerList.getLastRank() + 1;
		Player player = new Player(name, rank, isMale, false);
		player.save();
		playerList.addPlayer(player);
		updatePlayerLabels();
	}

	public void editPlayer(int ranking, String name, boolean isMale) {

		Player player = new Player(name, ranking, isMale, false);
		playerList.editPlayer(ranking, player);
		updatePlayerLabels();
	}
	/**
	 * updates the label list with the most current playerList and updates the labels. 
	 * Call after every change to player information.
	 */

	public void deletePlayer(int ranking) {
		playerList.deletePlayer(ranking);
		updatePlayerLabels();
	}
	public void updatePlayerLabels() {
		System.out.println(playerList);
		labelList.updatePlayerList(playerList);
		labelList.updateLabels();
	}
	//Create all components
	private void initComponents()
	{
		setFrmCissTableTennis(new JFrame());
		getFrmCissTableTennis().setTitle("CISS Table Tennis Stations Generator");
		getFrmCissTableTennis().setBounds(100, 100, 400, 425);
		getFrmCissTableTennis().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();

		JLabel lblNewLabel = new JLabel("Players");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		panel.add(lblNewLabel);

		pnlLeftPlayers = new JPanel();

		pnlRightPlayers = new JPanel();
		pnlRightPlayers.setLayout(new MigLayout("", "[15:n:15][100:n:100][12:n:12][12:n:12]", "[18:n:18][18:n:18][18:n:18][18:n:18][18:n:18][18:n:18][18:n:18][18:n:18][18:n:18][18:n:18][18:n:18][18:n:18][18:n:18]"));

		btnAdd = new JButton("Add");
		btnEdit = new JButton("Edit");
		btnDelete = new JButton("Del");

		btnSwap = new JButton("Swap");

		btnWon = new JButton("Beat");


		btnAbsent = new JButton("Absent");

		btnNext = new JButton("Next");

		btnNext.setBackground(Color.WHITE);
		btnNext.setForeground(Color.BLUE);
		
		btnAdvanced = new JButton("Advanced");
		GroupLayout groupLayout = new GroupLayout(getFrmCissTableTennis().getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE)
					.addGap(1))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(8)
					.addComponent(pnlLeftPlayers, GroupLayout.PREFERRED_SIZE, 185, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(pnlRightPlayers, GroupLayout.PREFERRED_SIZE, 185, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(10, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnSwap, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnWon, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnAbsent, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(53)
							.addComponent(btnAdvanced)))
					.addPreferredGap(ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnNext)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnEdit, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(pnlRightPlayers, 0, 0, Short.MAX_VALUE)
						.addComponent(pnlLeftPlayers, GroupLayout.PREFERRED_SIZE, 292, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(1)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnAdd)
								.addComponent(btnEdit)
								.addComponent(btnDelete)))
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnSwap)
							.addComponent(btnWon)
							.addComponent(btnAbsent)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnNext)
						.addComponent(btnAdvanced))
					.addGap(30))
		);
		pnlLeftPlayers.setLayout(new MigLayout("", "[15:n:15][100:n:100,grow][12:n:12][12:n:12]", "[18:n:18][18:n:18][18:n:18][18:n:18,grow][18:n:18][18:n:18][18:n:18][18:n:18][18:n:18][18:n:18][18:n:18][18:n:18][18:n:18]"));
		getFrmCissTableTennis().getContentPane().setLayout(groupLayout);
		playerLabelPanels = new JPanel[]{pnlLeftPlayers, pnlRightPlayers};
	}
	/**
	 * after deserialization, all the references to players are lost (deserialized version of 
	 * partner for a player != the actual partner player. .equals() doesn't work). Thus, this 
	 * simply takes the deserialized version's name and checks it with each actual version.
	 * If it is the same, it will set the actual to replace the deserialized version. If there are
	 * no actual verison that fits the deserialized version, that means an error has occurred. Perhaps
	 * the name changed (which shouldn't actually work as it should have saved... although maybe not
	 * in the partner's reference) or it has been deleted (externally or internally).
	 */
	private void recreatePartnerConnections() {
		for(Player player : playerList.getPlayerList()) {
			if(player.getPartner()!= null) {
				boolean set = false;
				for(Player comparedPlayer: playerList.getPlayerList()) {
					if(player.getPartner().getName().equals(comparedPlayer.getName())) {
						if(set) {
							System.out.println("ERROR ERROR two valid players fit the "
									+ "deserialized version of partner");
						}
						player.setPartner(comparedPlayer);
						set = true;
					}
				}
				if(!set) {
					System.out.println("ERROR ERROR valid player not found for deserialized partner");
				}
			}
		}
	}
	//create all events
	private void createEvents()
	{
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(playerList.getLastRank() == 24) {
					JOptionPane.showMessageDialog(null, "There are already 24 players! Please "
							+ "delete one to proceed. (or contact cyrus to add more slots somehow "
							+ "idk)");
					return;
				}
				openAddPlayerBox();
			}
		});
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openEditPlayerBox();
			}
		});
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openDeletePlayerBox();
			}
		});
		btnSwap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				swapPlayer();
			}
		});
		btnWon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				beatPlayer();
			}
		});
		btnAbsent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setAbsents();
			}
		});
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getFrmCissTableTennis().setVisible(false);
				Frame2 frame2 = new Frame2(playerList);
				frame2.setVisible(true);
			}
		});
		btnAdvanced.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openAdvancedPlayerBox();
			}
		});
	}
	
	public void openAdvancedPlayerBox() {
		int numberOfSelected = 0;
		PlayerLabel selected = null;
		for(PlayerLabel PL : labelList.getPlayerLabelList()) {
			if(PL.getIsSelected()) {
				selected = PL;
				numberOfSelected++;
			}
		}
		if(numberOfSelected != 1) {
			JOptionPane.showMessageDialog(null, "Please select one and only one player!");
			return;
		} 
		try {
			AdvancedPlayerBox dialog = new AdvancedPlayerBox(selected.getPlayer(), playerList);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			selected.setIsSelected(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * opens up the add player box and initializes it with necessary information. 
	 */
	public void openAddPlayerBox() {
		try {
			AddPlayerBox dialog = new AddPlayerBox(playerList, this);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * opens up the edit player box and initializes it with necessary info. 
	 */
	public void openEditPlayerBox() {
		try {
			EditPlayerBox dialog = new EditPlayerBox(playerList, this);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception a) {
			a.printStackTrace();
		}
	}
	/**
	 * opens up the delete player box and initializes it with necessary info. 
	 */
	private void openDeletePlayerBox() {
		try {
			DeletePlayerBox dialog = new DeletePlayerBox(playerList, this);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * swaps the two players that are selected right now. Updates labels, playerList, and saves. 
	 */
	private void swapPlayer() {
		int rank1 = 0;//lower one
		int rank2 = 0;
		int numOfSelected = 0;
		for(PlayerLabel PL : labelList.getPlayerLabelList()) {
			if(PL.getIsSelected()) {
				if(rank1 <= PL.getPlayer().getRank()) {
					rank2 = rank1;
					rank1 = PL.getPlayer().getRank();
				}
				else {
					rank2 = PL.getPlayer().getRank();
				}
				numOfSelected++;
			}
		}
		if (numOfSelected != 2) {
			JOptionPane.showMessageDialog(null, "ERROR! Please select 2 and only 2 players.");
			return;
		} else {
			playerList.swapPlayers(rank1, rank2);
		}
		this.updatePlayerLabels();
	}
	/**
	 * simulates what happens if 1 player beats the other, updates labels, playerLists, and saves.
	 */
	private void beatPlayer() {
		int rank1 = 100;//lower one
		int rank2 = 100;
		int numOfSelected = 0;
		for(PlayerLabel PL : labelList.getPlayerLabelList()) {
			if(PL.getIsSelected()) {
				if(rank1 != 100) {
					rank2 = PL.getPlayer().getRank();
				} else {
					rank1 = PL.getPlayer().getRank();
				}
				numOfSelected++;
			}
		}
		if(rank1 > rank2) {
			int holder = rank1;
			rank1 = rank2;
			rank2 = holder;
		}
		if (numOfSelected != 2) {
			JOptionPane.showMessageDialog(null, "ERROR! Please select 2 and only 2 players.");
			return;
		} else {
			playerList.beatPlayer(rank1, rank2);
		}
		this.updatePlayerLabels();
	}
	
	private void setAbsents() {
		for(PlayerLabel PL : labelList.getPlayerLabelList()) {
			if (PL.getIsSelected()) {
				PL.getPlayer().setAbsent(!PL.getPlayer().getIsAbsent());
				PL.Update(PL.getPlayer());
			}
		}
		this.updatePlayerLabels();
	}
	//	private void goToNext(PlayerInformationHolder PIH) {
	//		List<String> infos = PIH.getAllExistingInformationArray();
	//		for(int i = 0; i < infos.size(); i++) {
	//			String[] parts = infos.get(i).split(":");
	//			
	//		}
	//	}
	//	private void addPlayerLabel(int ranking, String name, JPanel group)
	//	{
	//		
	//		JLabel lblrank = new JLabel(Integer.toString(ranking));
	//		group.add(lblrank, "cell 0 " + ranking);
	//		
	//		JLabel lblname = new JLabel(name);
	//		group.add(lblname, "cell 1 " + ranking);
	//		
	//		rdbtnNewRadioButton = new JRadioButton("");
	//		pnlLeftPlayers.add(rdbtnNewRadioButton, "cell 2 " + ranking);
	//	}
	//	
	//	public void printse()
	//	{
	//		System.out.println("se");
	//	}
	public JFrame getFrmCissTableTennis() {
		return frmCissTableTennis;
	}
	public void setFrmCissTableTennis(JFrame frmCissTableTennis) {
		this.frmCissTableTennis = frmCissTableTennis;
	}
}

