import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
public class Frame2 extends JFrame {
	private final String stationDirectoryName = "stations";
	private final String stationOrigDirectoryName = "originalstations";
	private File stationDirectory;
	private File originalDirectory;
	private JPanel contentPane;
	
	private MasterPlayerList playerList;
	private MasterStationList stationList;
	private JList list;

	private JList<Station> JlistStations;
	private JLabel lblNewLabel;
	private JButton btnNewStation;
	private JButton btnEditStation;
	private JButton btnDeleteStation;
	private JButton btnNewButton;
	private JButton btnBack;
	private JButton btnNext;
	private JButton btnTemp;
	private JButton btnDeleteAll;
	private JLabel lblNumberOfPresent;
	private JLabel lblNewLabel_2;
	private JLabel lblPref;
	private JLabel lblMax;
	private JLabel lblPresentPlayers;
	private JLabel lblRequiredMax;
	private JLabel lblRequiredMin;
	private JLabel lblRequiredPref;

	private boolean tempOn;
	private JLabel lblTemp;
	/**
	 * Create the frame.
	 */
	public Frame2(MasterPlayerList playerList) {
		setTitle("Stations");
		stationDirectory = new File(stationDirectoryName);
		stationList = new MasterStationList(stationDirectory);
		originalDirectory = new File(this.stationOrigDirectoryName);
		this.playerList = playerList;
		initComponents();
		createEvents();
		if(originalDirectory.exists()) {//temp mode was on
			tempOn = true;
			btnTemp.setText("Temp: On");
			lblTemp.setVisible(true);
			btnTemp.setForeground(Color.green);
		} 
		updateList();
		
	}
	
	public void addStation(Station newStation) {
		stationList.addStation(newStation);
		updateList();
	}
	public void editStation(Station station, Station newStation) {
		int index = stationList.getStations().indexOf(station);
		station.deleteTable();
		stationList.removeStationWithoutWarning(station);
		stationList.addStation(newStation, index);
		newStation.saveTable();
		updateList();
	}
	public void updateList() {
		JlistStations.setModel(new AbstractListModel<Station>() {
			//private static final long serialVersionUID = 1L;
			ArrayList<Station> values = stationList.getStations();
			
			public int getSize() {
				return values.size();
			}
			public Station getElementAt(int index) {
				return values.get(index);
			}
		});
		updateAvailableAndRequiredPlayersLabels();
	}
	public void createEvents() {
		btnNewStation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openAddStationsBox();
			}
		});
		btnDeleteStation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(JlistStations.getSelectedValue() != null) {
					stationList.removeStation(JlistStations.getSelectedValue());
					updateList();
				}
			}
		});
		btnEditStation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(JlistStations.getSelectedValue() != null) {
					openEditStationsBox(JlistStations.getSelectedValue());
				}
			}
		});
		btnNewButton.addActionListener(new ActionListener() {//sets absents
			public void actionPerformed(ActionEvent e) {
				if(JlistStations.getSelectedValue() != null) {
					JlistStations.getSelectedValue().setDisabled(!JlistStations.getSelectedValue().isDisabled());
					updateList();
				}
			}
		});
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goBack();
			}
		});
		JlistStations.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("detected");
		        if (e.getClickCount() == 2) {
		            // Double-click detected
		        	Rectangle r = JlistStations.getCellBounds(0, JlistStations.getLastVisibleIndex()); 
					if (r != null && r.contains(e.getPoint())) {
						int index = JlistStations.locationToIndex(e.getPoint());
						StationInfo info = new StationInfo(JlistStations.getModel().getElementAt(index));
						info.setVisible(true);
					}
		      
		        } 
			}
		});
		btnDeleteAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteAll();
			}
		});
		btnTemp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tempToggle();
			}
		});
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nextFrame();
			}
		});
	}
	public void nextFrame() {
		if(playerList.getNumOfPlayers() > stationList.getNumberOfMaxPlayers()) {
			JOptionPane.showMessageDialog(null, "Error! There are more players than "
					+ " player slots within stations. Please add more slots in stations"
					+ " or enable some of them.");
			return;
		}
		if(playerList.getNumOfPlayers() < stationList.getNumberOfMinPlayers()) {
			JOptionPane.showMessageDialog(null, "Error! There are too few players to satisfy "
					+ "every player slot! Please disable some stations or double check the "
					+ "number of players ");
			return;
		}
		Frame3 frame3 = new Frame3(playerList, stationList);
		frame3.setVisible(true);
		this.setVisible(false);
	}
	public void tempToggle() {
		tempOn = !tempOn;
		if(tempOn) {
			btnTemp.setText("Temp: On");
			btnTemp.setForeground(Color.green);
			lblTemp.setVisible(true);
			copyDir(stationDirectory, originalDirectory);
		} else {
			btnTemp.setText("Temp: Off");
			btnTemp.setForeground(Color.black);
			lblTemp.setVisible(false);
			deleteDir(stationDirectory);
			copyDir(originalDirectory, stationDirectory);
			deleteDir(originalDirectory);
		}
		stationList.updateDir();
		updateList();
	}
	/**
	 * copies directory1 to directory2
	 * @param directory1
	 * @param directory2
	 */
	public void copyDir(File directory1, File directory2) {
		if(!directory1.exists()) {
			directory1.mkdir();
		}
		if(!directory2.exists()) {
			directory2.mkdir();
		}
		for(File file : directory1.listFiles()) {
			try {//write old back
				OutputStream fileOut = new FileOutputStream(directory2.getPath() + "/" + file.getName());
				Path path = Paths.get(file.getPath());
				Files.copy(path, fileOut);
				fileOut.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void deleteDir(File directory) {
		for(File file : directory.listFiles()) {
			file.delete();
		}
		directory.delete();
	}
	public void deleteAll() {
		if((JOptionPane.showConfirmDialog(null, "ARE YOU ABSOLUTELY SURE YOU WANT TO DELETE ALL STATIONS?")) == JOptionPane.YES_OPTION) {
				stationList.deleteAll();
				updateList();
		}
	}
	public void goBack() {
		this.setVisible(false);
			try {
				Frame1 window = new Frame1();
				window.getFrmCissTableTennis().setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	public void openEditStationsBox(Station editingStation) {
		AddStation edit = new AddStation(stationList, this, editingStation);
		edit.setVisible(true);
	}
	public void openAddStationsBox() {
		AddStation add = new AddStation(stationList, this);
		add.setVisible(true);
	}
	public void updateAvailableAndRequiredPlayersLabels() {
		int players = playerList.getNumOfPlayers();
		int max = stationList.getNumberOfMaxPlayers();
		int min = stationList.getNumberOfMinPlayers();
		lblPresentPlayers.setText(Integer.toString(players));
		lblRequiredMax.setText(Integer.toString(max));
		lblRequiredPref.setText(Integer.toString(stationList.getNumberOfPrefPlayers()));
		lblRequiredMin.setText(Integer.toString(min));
		if(players > max) {
			lblRequiredMax.setForeground(Color.red);
		} else {
			lblRequiredMax.setForeground(Color.green);
		}
		if(players == stationList.getNumberOfPrefPlayers()) {
			lblRequiredPref.setForeground(Color.green);
		} else {
			lblRequiredPref.setForeground(Color.black);
		}
		if(players < min) {
			lblRequiredMin.setForeground(Color.red);
		} else {
			lblRequiredMin.setForeground(Color.green);
		}
	}
	public void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 515, 375);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		
		btnNewStation = new JButton("Add Station");
		
		btnEditStation = new JButton("Edit Station");
		btnDeleteStation = new JButton("Delete Station");
		
		btnNewButton = new JButton("Disable");
		
		btnNext = new JButton("Next");
		
		btnBack = new JButton("Back");
		
		btnTemp = new JButton("Temp: Off");
		
		btnTemp.setToolTipText("When Temp (temporary) is on, all changes will be temporary. Turn it off to revert back to the permanent versions (and the temp versions will be lost.)");
		
		btnDeleteAll = new JButton("DELETE ALL");
		
		btnDeleteAll.setBackground(Color.RED);
		btnDeleteAll.setForeground(Color.RED);
		btnDeleteAll.setToolTipText("DELETES ALL STATIONS. ONLY USE WHEN APP IS BEYOND SAVING.");
		
		lblTemp = new JLabel("All changes are temporary");
		lblTemp.setVisible(false);
		
		lblNumberOfPresent = new JLabel("players:");
		
		lblPresentPlayers = new JLabel("99");
		
		lblNewLabel_2 = new JLabel("Min:");
		
		lblRequiredMin = new JLabel("99");
		
		lblPref = new JLabel("Pref:");
		
		lblRequiredPref = new JLabel("99");
		
		lblMax = new JLabel("Max:");
		
		lblRequiredMax = new JLabel("99");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(6)
							.addComponent(lblTemp)
							.addPreferredGap(ComponentPlacement.RELATED, 159, Short.MAX_VALUE)
							.addComponent(btnBack, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnNext)
							.addGap(8))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(btnEditStation, Alignment.LEADING)
								.addComponent(btnNewStation, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnDeleteAll, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
								.addGroup(Alignment.LEADING, gl_contentPane.createParallelGroup(Alignment.TRAILING)
									.addComponent(btnNewButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(btnDeleteStation, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 116, Short.MAX_VALUE)
									.addComponent(btnTemp, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)))
							.addGap(15)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
									.addComponent(lblNumberOfPresent)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblPresentPlayers)
									.addGap(37)
									.addComponent(lblNewLabel_2)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblRequiredMin, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(lblPref)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblRequiredPref, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblMax)
									.addGap(4)
									.addComponent(lblRequiredMax, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
									.addGap(7))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE)
									.addContainerGap())))))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 266, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
							.addGap(16)
							.addComponent(btnNewStation)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnEditStation)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnDeleteStation)
							.addGap(47)
							.addComponent(btnNewButton)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnTemp)
							.addGap(46)))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(11)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
									.addComponent(lblRequiredMin)
									.addComponent(lblNewLabel_2))
								.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
									.addComponent(lblPref)
									.addComponent(lblRequiredPref)
									.addComponent(lblMax)
									.addComponent(lblRequiredMax))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblNumberOfPresent)
										.addComponent(lblPresentPlayers))
									.addGap(1)))
							.addGap(8)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnNext)
								.addComponent(btnBack)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnDeleteAll)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblTemp)))
					.addContainerGap())
		);
		JlistStations = new JList<Station>();
		scrollPane.setViewportView(JlistStations);
		
		lblNewLabel = new JLabel("Stations");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPane.setColumnHeaderView(lblNewLabel);
		contentPane.setLayout(gl_contentPane);
	}
}
