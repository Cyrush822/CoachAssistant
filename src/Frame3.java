import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.AbstractListModel;
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
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import javax.swing.JList;
import javax.swing.JSpinner;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Component;
import javax.swing.Box;

public class Frame3 extends JFrame {

	private JPanel contentPane;
	MasterPlayerList playerList;
	MasterStationList stationList;
	FinalStationMasterList finalStationList;
	private JList listStations;
	private JLabel lblPlayersLeft;
	private JButton btnBack;
	private JButton btnGenerate;
	private JButton btnClear;
	private JButton btnHistory;
	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */
	public Frame3(MasterPlayerList playerList, MasterStationList stationList) {
		initComponents();
		createEvents();
		this.playerList = playerList;
		this.stationList = stationList;
		finalStationList = new FinalStationMasterList(playerList);
		for(int i = 0; i < stationList.getStations().size(); i++) {
			if(!stationList.getStations().get(i).isDisabled())
				finalStationList.addStation(new FinalStation(stationList.getStations().get(i), playerList, finalStationList));
		}
		System.out.println(finalStationList.getAvailablePlayers());
		updateFinalStationList();
		updatePlayersLeft();
	}
	void updateFinalStationList() {
		listStations.setModel(new AbstractListModel<FinalStation>() {
			ArrayList<FinalStation> values = finalStationList.getFinalStations();
			public int getSize() {
				return values.size();
			}
			public FinalStation getElementAt(int index) {
				return values.get(index);
			}
		});
	}
	void createEvents() {
		listStations.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("detected");
		        if (e.getClickCount() == 2) {
		            // Double-click detected
		        	Rectangle r = listStations.getCellBounds(0, listStations.getLastVisibleIndex()); 
					if (r != null && r.contains(e.getPoint())) {
						int index = listStations.locationToIndex(e.getPoint());
						openManualWindow(index);
					}
		      
		        } 
			}
		});
		
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Frame2 frame = new Frame2(playerList);
				frame.setVisible(true);
				setVisible(false);
			}
		});
		
		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generateStations();
			}
		});
		
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean allSame = true;
				for(FinalStation station : finalStationList.getFinalStations()) {
					if(!station.getCurrentPlayers().equals(station.getManuallyAddedPlayers())) {
						allSame = false;
					}
				}
				for(int i = 0; i < finalStationList.getFinalStations().size(); i++) {
					if(allSame) {
						finalStationList.getFinalStations().get(i).trueClearPlayers();
					} else {
						finalStationList.getFinalStations().get(i).clearPlayers();
					}
					updateFinalStationList();
					updatePlayersLeft();
				}
			}
		});
	}
	public void generateStations() {
		finalStationList.generateStations();
//		for(int i = 0; i < finalStationList.getFinalStations().size(); i++) {
////			finalStationList.getFinalStations().get(i).calculateLists();
////			finalStationList.getFinalStations().get(i).printLists();
//			finalStationList.getFinalStations().get(i).clearPlayers();
//			finalStationList.getFinalStations().get(i).calculatePriority();
//		}
		this.updateFinalStationList();
		this.updatePlayersLeft();
		
	}
	public void openManualWindow(int stationIndex) {
		ManualSetPlayer manualWindow = new ManualSetPlayer((FinalStation)listStations.getModel().getElementAt(stationIndex), finalStationList, this);
		manualWindow.setVisible(true);
	}
	public void updatePlayersLeft() {
		this.lblPlayersLeft.setText("Players left: " + this.finalStationList.getAvailablePlayers().size());
	}
	void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 575, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		listStations = new JList<FinalStation>();
		
		btnGenerate = new JButton("Generate");
		
		JButton btnNext = new JButton("Next");
		
		JButton btnSave = new JButton("Save");
		
		lblPlayersLeft = new JLabel("Players left: ");
		
		btnBack = new JButton("Back");
		
		btnClear = new JButton("Clear");
		
		btnHistory = new JButton("History");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(5)
							.addComponent(btnGenerate)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblPlayersLeft)
							.addPreferredGap(ComponentPlacement.RELATED, 111, Short.MAX_VALUE)
							.addComponent(btnClear)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnHistory)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSave))
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(btnBack)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNext))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(listStations, GroupLayout.PREFERRED_SIZE, 551, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(listStations, GroupLayout.PREFERRED_SIZE, 191, GroupLayout.PREFERRED_SIZE)
					.addGap(8)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnSave)
							.addComponent(btnHistory)
							.addComponent(btnClear))
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnGenerate)
							.addComponent(lblPlayersLeft)))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNext)
						.addComponent(btnBack)))
		);
		contentPane.setLayout(gl_contentPane);
	}
}
