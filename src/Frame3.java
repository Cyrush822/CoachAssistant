import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
import java.time.*;
public class Frame3 extends JFrame {

	private JPanel contentPane;
	MasterPlayerList playerList;
	MasterStationList stationList;
	FinalStationMasterList finalStationList;
	private SavedSettings settings;
	private JList listStations;
	private JLabel lblPlayersLeft;
	private JButton btnBack;
	private JButton btnGenerate;
	private JButton btnClear;
	private JButton btnHistory;
	private JButton btnNext;
	private JButton btnAdvanced;
	private File settingsFile;
	public static String settingsFileName = "AdvSettings";
	public static String dirName = "savedConfigs";
	private final String rDConfigDirName = "recentlyDeletedConfig";
	private final String rDWordDirName = "recentlyDeletedWordDoc";
	public String docDirName = "ConvertedDocuments";
	/**
	 * 
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */
	public Frame3(MasterPlayerList playerList, MasterStationList stationList) {
		initComponents();
		createEvents();
		settingsFile = new File(settingsFileName);
		settings = this.deserializeSettings(settingsFile);
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
		System.out.println("testing:...");
		ArrayList<Player> players = new ArrayList<Player>();
		players.add(finalStationList.getPlayerList().getPlayerList().get(0));
		players.add(finalStationList.getPlayerList().getPlayerList().get(1));
		System.out.println("normal: " + players);
		System.out.println("invert: " + finalStationList.invertPlayerList(players));
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
	public static LocalTime extractTimeFromWordDoc(File wordDoc) {
		String str = wordDoc.getName();
		if(str == null) {
			return null;
		}
		int firstNumber = str.length();
		for(int i = 0; i < str.length(); i++) {
			if(isNumeric(str.substring(i,i+1))) {
				firstNumber = i;
				break;
			}
		}
		int firstT = str.length();
		for(int i = firstNumber; i < str.length(); i++) {
			if(str.substring(i,i+1).equals("T")) {
				firstT = i;
				break;
			}
		}
		int dot = str.length();
		for(int i = firstT; i < str.length();i++) {
			if(str.substring(i,i+5).equals(".docx")) {
				dot = i;
				break;
			}
		}
		String timeString = str.substring(firstT + 1, dot);
		try {
			return LocalTime.parse(timeString);
		}
		catch(Exception e) {
			System.out.println(timeString);
			System.out.println("error in parsing localTime from word doc");
			return null;
		}
	}
	public static LocalDate extractDateFromWordDoc(File wordDoc) {
		String str = wordDoc.getName();
		if(str == null) {
			return null;
		}
		int firstNumber = str.length();
		for(int i = 0; i < str.length(); i++) {
			if(isNumeric(str.substring(i,i+1))) {
				firstNumber = i;
				System.out.println(i);
				break;
			}
		}
		int firstT = str.length();
		for(int i = firstNumber; i < str.length(); i++) {
			if(str.substring(i,i+1).equals("T")) {
				firstT = i;
				break;
			}
		}
		String dateString = str.substring(firstNumber, firstT);
		try {
			return LocalDate.parse(dateString);
		}
		catch(Exception e) {
			System.out.println(dateString);
			System.out.println("error in parsing localDate from word doc");
			return null;
		}
	}
	public static String extractNameFromWordDoc(File wordDoc) {
		String str = wordDoc.getName();
		if(str == null) {
			return "";
		}
		int firstNumber = str.length();
		for(int i = 0; i < str.length(); i++) {
			if(isNumeric(str.substring(i,i+1))) {
				firstNumber = i;
				break;
			}
		}
		return str.substring(0,firstNumber);
	}
	public static boolean isNumeric(String str) {
		if(str == null) {
			return false;
		}
		try {
			Integer.parseInt(str);
		}
		catch(Exception e) {
			return false;
		}
		return true;
	}
	void sortWordDocsByTime(ArrayList<File> docs) {
		//sort by date
		for(int i = docs.size(); i > 0; i--) {
			for(int a = 0; a < i - 1; a++) {
				if(extractDateFromWordDoc(docs.get(a)).isBefore
						(extractDateFromWordDoc(docs.get(a+1)))) {
					File holder = docs.get(a);
					docs.set(a, docs.get(a+1));
					docs.set(a+1, holder);
				}
			}
		}
//		sort by time
		for(int i = docs.size(); i > 0; i--) {
			for(int a = 0; a < i - 1; a++) {
				if(extractDateFromWordDoc(docs.get(a)).isEqual
						(extractDateFromWordDoc(docs.get(a+1)))) {
					if(extractTimeFromWordDoc(docs.get(a)).isBefore
							(extractTimeFromWordDoc(docs.get(a+1)))) {
						File holder = docs.get(a);
						docs.set(a, docs.get(a+1));
						docs.set(a+1, holder);
					}
				}
			}
		}
	}
	void deleteOldestWordDoc(ArrayList<File> stationLists) {
		sortWordDocsByTime(stationLists);
		stationLists.get(stationLists.size() - 1).delete();
	}
	/**
	 * the method with the best naming in the world
	 */
	void checkWordDocsToMakeSureItDoesntOverFlow(File dir) {
		if(dir.listFiles().length <= settings.configsSaved) {
			return;
		}
		while(dir.listFiles().length > settings.configsSaved) {
			ArrayList<File> stationLists = new ArrayList<File>();
			for(File file : dir.listFiles()) {
				stationLists.add(file);
			}
			deleteOldestWordDoc(stationLists);
		}
	}
	void makeSpaceForNewConfig() {
		File configsFolder = new File(dirName);
		if(!configsFolder.exists()) {
			return;
		}
		File[] configs = configsFolder.listFiles();
		if(configs.length < settings.getConfigsSaved()) {
			return;
		}
		while(configs.length >= settings.getConfigsSaved()) {
			ArrayList<FinalStationMasterList> stationLists = new ArrayList<FinalStationMasterList>();
			for(File config : configs) {
				stationLists.add(deserializeFinalStationMasterList(config));
			}
			FinalStationMasterList oldestStationList = stationLists.get(0);
			for(FinalStationMasterList stationList : stationLists) {
				if(stationList.getSavedTime().isBefore(oldestStationList.getSavedTime())) {
					oldestStationList = stationList;
				}
			}
			File recentlyDeletedWordDocs = new File(this.rDWordDirName);
			
			if(recentlyDeletedWordDocs.exists()) {
				for(File file : recentlyDeletedWordDocs.listFiles()) {
					file.delete();
				}
			} else {
				recentlyDeletedWordDocs.mkdir();
			}
			File recentlyDeletedConfigs = new File(this.rDConfigDirName);
			if(recentlyDeletedConfigs.exists()) {
				for(File file : recentlyDeletedConfigs.listFiles()) {
					file.delete();
				}
			}else {
				recentlyDeletedConfigs.mkdir();
			}
			System.out.println(oldestStationList.getFileName());
			if(oldestStationList.getAssociatedWordDoc().exists()) {
				oldestStationList.getAssociatedWordDoc().renameTo(new File(this.rDWordDirName + "/" + 
						oldestStationList.getAssociatedWordDoc().getName()));
			}
			oldestStationList.getThisFile().renameTo(new File(this.rDConfigDirName + "/" + oldestStationList.getFileName()));
			configsFolder = new File(dirName);
			configs = configsFolder.listFiles();
		}
		
	}
	void updateAvgRanks() {
		for(Player player: playerList.getPlayerList()) {
			if(!player.getIsAbsent()) {
				player.updateAvgRanking();
			}
		}
	}
	void createEvents() {
		btnHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				configHistory frame = new configHistory(new File(dirName));
				frame.setVisible(true);
			}
		});
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(settings.isConfirmDialogue()) {
					int option = JOptionPane.showConfirmDialog(null, "<HTML>Are you sure?\n"
							+ "Pressing \"YES\" will automatically add this configuration to saved"
							+ "\nconfigurations, and subsequent configurations will avoid putting players"
							+ "\ninto the same stations as in this configuration. Ensure that this "
							+ "\nconfiguration is finalized and complete.");
					if(option != JOptionPane.YES_OPTION) {
						return;
					}
				}
				makeSpaceForNewConfig();
				convertToWord wordDoc = new convertToWord(finalStationList, java.time.LocalDateTime.now(), docDirName);
				System.out.println(wordDoc.getDoc().getName());
				System.out.println(extractNameFromWordDoc(wordDoc.getDoc()));
				System.out.println(extractDateFromWordDoc(wordDoc.getDoc()));
				System.out.println(extractTimeFromWordDoc(wordDoc.getDoc()));
				finalStationList.setAssociatedWordDoc(wordDoc.getDoc());
				finalStationList.setTimeAndName();
				finalStationList.save();
				ArrayList<File> docs = new ArrayList<File>();
				for(File file : new File("ConvertedDocuments").listFiles()) {
					docs.add(file);
				}
				sortWordDocsByTime(docs);
				for(File file : docs) {
					System.out.println(file.getName());
				}
				File configFile = new File(finalStationList.getDir() + "/" + finalStationList.getFileName());
				ConvertedFrame frame = new ConvertedFrame(configFile, 
						wordDoc.getDoc(), new File(rDWordDirName), new File(rDConfigDirName), 
						new File(docDirName), new File(dirName));
				frame.setVisible(true);
				updateAvgRanks();
			}
		});
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
		btnAdvanced.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Frame3Advanced frame = new Frame3Advanced(settings);
				frame.setVisible(true);
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
	/**
	 * shifts all saved stationslists up
	 * savedConfig -> savedConfig1
	 * savedConfig1 -> savedConfig2
	 * etc.
	 * Used for prepping for a new savedConfig
	 * Always saves one more than what user specifies, in case they delete. 
	 * (doesn't get used, however, because of generateStations() only using
	 * what the user specifies
	 */
	public void shiftAllSavedStationList() {
		ArrayList<FinalStationMasterList> stationListList = deserializeAllStationLists();
		for(FinalStationMasterList stationList : stationListList) {
			
		}
	}
	public ArrayList<FinalStationMasterList> deserializeAllStationLists() {
		ArrayList<FinalStationMasterList> stationListList = new ArrayList<FinalStationMasterList>();
		for(File file: new File(settingsFileName).listFiles()) {
			stationListList.add(deserializeFinalStationMasterList(file));
		}
		return stationListList;
	}
	public FinalStationMasterList deserializeFinalStationMasterList(File file) {
		try
        {    
            // Reading the object from a file 
            FileInputStream fileInput = new FileInputStream(file); 
            ObjectInputStream in = new ObjectInputStream(fileInput); 
              
            // Method for deserialization of object 
            FinalStationMasterList stationList = (FinalStationMasterList)in.readObject(); 
              
            in.close(); 
            fileInput.close(); 
              
            System.out.println("Object has been deserialized "); 
            return stationList;
        } 
          
        catch(IOException ex) 
        { 
            System.out.println("IOException is caught"); 
            return null;
        } 
          
        catch(ClassNotFoundException ex) 
        { 
            System.out.println("ClassNotFoundException is caught"); 
            return null;
        } 
	}
	public SavedSettings deserializeSettings(File file) {
		if(!file.exists()) {
			return new SavedSettings();
		}
		try
        {    
            // Reading the object from a file 
            FileInputStream fileIn = new FileInputStream(file); 
            ObjectInputStream in = new ObjectInputStream(fileIn); 
              
            // Method for deserialization of object 
            SavedSettings settings = (SavedSettings)in.readObject(); 
              
            in.close(); 
            fileIn.close(); 
              
            System.out.println("Object has been deserialized "); 
            return settings;
        } 
          
        catch(IOException ex) 
        { 
            System.out.println("IOException is caught"); 
        	file.delete();
        	System.out.println("settings deleted");
        	System.out.println("retrying...");
            return deserializeSettings(file);
        } 
          
        catch(ClassNotFoundException ex) 
        { 
            System.out.println("ClassNotFoundException is caught"); 
        	settings.delete();
            return null;
        } 
	}
	void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 575, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		listStations = new JList<FinalStation>();
		
		btnGenerate = new JButton("Generate");
		
		btnNext = new JButton("Done");
		
		lblPlayersLeft = new JLabel("Players left: ");
		
		btnBack = new JButton("Back");
		
		btnClear = new JButton("Clear");
		
		btnHistory = new JButton("History");
		
		btnAdvanced = new JButton("Advanced");
		
		JButton btnHelp = new JButton("Help");
		btnHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Frame3Help frame = new Frame3Help();
				frame.setVisible(true);
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(5)
							.addComponent(btnGenerate)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblPlayersLeft)
							.addPreferredGap(ComponentPlacement.RELATED, 192, Short.MAX_VALUE)
							.addComponent(btnClear)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnHistory))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnHelp)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnAdvanced)
							.addPreferredGap(ComponentPlacement.RELATED)
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
							.addComponent(btnHistory)
							.addComponent(btnClear))
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnGenerate)
							.addComponent(lblPlayersLeft)))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNext)
						.addComponent(btnBack)
						.addComponent(btnAdvanced)
						.addComponent(btnHelp)))
		);
		contentPane.setLayout(gl_contentPane);
	}
}
