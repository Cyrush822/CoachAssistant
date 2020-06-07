import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
public class AddStation extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private MasterStationList stationList;
	public MasterStationList.rankPreference rankPref;
	private JCheckBox chckbxTakeRanksInto;
	private JLabel lblMaximumRankDifference;
	private JSpinner spinnerRankDifference;
	private JSpinner spinnerRankMax;
	private JSpinner spinnerRankMin;
	private JComboBox<MasterStationList.rankPreference> comboBoxRankPref;
	private JSpinner spinnerMin;
	private JSpinner spinnerPref;
	private JSpinner spinnerMax;
	private JButton okButton;
	private JTextField txtName;
	private JTextField txtDesc;
	private JComboBox<MasterStationList.genderPreference> comboBoxGenderPref;
	private Frame2 frame;
	private JLabel lblMin;
	private JLabel lblMax;
	private JLabel lblDash;
	private JButton cancelButton;
	private final int nameMaxChars = 15;
	private final int descMaxChars = 20;
	private Station exception;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JLabel lblMinimumNumberOf;
	private JLabel lblPreferredNumberOf;
	private JLabel lblMaximumNumberOf;
	private JLabel lblRankPref;
	private JButton btnHelp;
	private JRadioButton rdbtnDoubles;
	public enum rankingPref {
	};
	/**
	 * Launch the application.
	 */

	/**
	 * Create the dialog.
	 * @wbp.parser.constructor
	 */
	public AddStation(MasterStationList stationList, Frame2 frame) {
		this.frame = frame;
		this.stationList = stationList;
		
		initComponents();
		createEvents();
		
	}
	/**
	 * for editStation
	 * @param stationList
	 * @param frame
	 * @param exception
	 */
	public AddStation(MasterStationList stationList, Frame2 frame, Station exception) {
		this.frame = frame;
		this.stationList = stationList;
		this.exception = exception;
		initComponents();
		createEvents();
		txtName.setText(exception.getStationName());
		txtDesc.setText(exception.getStationDesc());
		spinnerMin.setValue(exception.getMinPlayers());
		spinnerPref.setValue(exception.getPreferredPlayers());
		spinnerMax.setValue(exception.getMaxPlayers());
		chckbxTakeRanksInto.setSelected(exception.isRanked());
		spinnerRankDifference.setValue(exception.getMaxRankDifference());
		comboBoxRankPref.setSelectedItem(exception.getRankPref());
		comboBoxGenderPref.setSelectedItem(exception.getGenderPref());
		spinnerRankMin.setValue(exception.getRankMin());
		spinnerRankMax.setValue(exception.getRankMax());
		rdbtnDoubles.setSelected(exception.getCompType().equals(MasterStationList.competitiveType.doubles));
		setTitle("Edit Existing Station - " + exception.getStationName());
		updateAllUIVisibilities();
	}
	void createEvents() {
		chckbxTakeRanksInto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				lblMaximumRankDifference.setVisible(chckbxTakeRanksInto.isSelected());
//				spinnerRankDifference.setVisible(chckbxTakeRanksInto.isSelected());
				updateAllUIVisibilities();
			}
		});
		
		comboBoxRankPref.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateAllUIVisibilities();
			}

		});
		
		spinnerMin.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if((int)spinnerMin.getValue() > (int)spinnerPref.getValue()) {
					spinnerPref.setValue(spinnerMin.getValue());
				}
				if((int)spinnerMin.getValue() > (int)spinnerMax.getValue()) {
					spinnerMax.setValue(spinnerMin.getValue());
				}
			}
		});
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applyAndQuit();
			}
		});
		
		spinnerRankMin.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if((int) spinnerRankMin.getValue() > (int) spinnerRankMax.getValue()) {
					spinnerRankMax.setValue(spinnerRankMin.getValue());
				}
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quit();
			}
		});
		txtName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(txtName.getText().length() >= nameMaxChars) {
					txtName.setText(txtName.getText().substring(0,nameMaxChars-1));
				}
			}
		});
		txtName.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(txtName.getText().length() >= nameMaxChars) {
					txtName.setText(txtName.getText().substring(0,nameMaxChars-1));
				}
			}
		});
		txtDesc.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(txtDesc.getText().length() >= descMaxChars) {
					txtDesc.setText(txtDesc.getText().substring(0,descMaxChars - 1));
				}
			}
		});
		txtDesc.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(txtDesc.getText().length() >= descMaxChars) {
					txtDesc.setText(txtDesc.getText().substring(0, descMaxChars - 1));
				}
			}
		});
	}
	private void updateAllUIVisibilities() {
		updateRankPrefUI();
		lblMaximumRankDifference.setVisible(chckbxTakeRanksInto.isSelected());
		spinnerRankDifference.setVisible(chckbxTakeRanksInto.isSelected());
	}
	void setAllPlayerSpinners(int num) {
		spinnerMin.setValue(num);
		spinnerPref.setValue(num);
		spinnerMax.setValue(num);
	}
	void quit() {
		this.setVisible(false);
	}
	
	private void updateRankPrefUI() {
		boolean flag = !(comboBoxRankPref.getSelectedItem() == MasterStationList.rankPreference.allow);
		spinnerRankMin.setVisible(flag);
		spinnerRankMax.setVisible(flag);
		lblMin.setVisible(flag);
		lblMax.setVisible(flag);
		lblDash.setVisible(flag);
		if(flag) {
			lblRankPref.setText(" Players of these ranks:");
		} else {
			lblRankPref.setText(" Players of any rank.");
		}
	}
	boolean checkEvens() {
		return true;
	}
	/**
	 * checks that all values are good, and then creates a new Station and calls addStation
	 * from Frame2, and proceeds to quit. 
	 */
	void applyAndQuit() {
		if(exception != null) {
			if(!(stationList.isNameOk(txtName.getText(), exception))) {
				return;
			}
		} else {
			if(!stationList.isNameOk(txtName.getText())) {
				return;
		}
		}if(!checkNumOfPlayers()) {
			return;
		}if(!checkMaxRankDifference()) {
			return;
		}if(!checkRankRequirements()) {
			return;
		}
		int maxRank = (int)this.spinnerRankDifference.getValue();
		if(!chckbxTakeRanksInto.isSelected()) {
			maxRank = 0;
		}
		MasterStationList.competitiveType compType;
		if(rdbtnDoubles.isSelected()) {
			compType = MasterStationList.competitiveType.doubles;
		} else {
			compType = MasterStationList.competitiveType.notCompetitive;
		}
		Station newStation = new Station(txtName.getText(), txtDesc.getText(), 
				(int)spinnerMin.getValue(), (int)spinnerMax.getValue(), 
				(int)spinnerPref.getValue(), rdbtnDoubles.isSelected(), 
				chckbxTakeRanksInto.isSelected(), (int)spinnerRankDifference.getValue(),
				(MasterStationList.genderPreference) comboBoxGenderPref.getSelectedItem(), 
				(MasterStationList.rankPreference) comboBoxRankPref.getSelectedItem(),
				(int)spinnerRankMin.getValue(), (int)spinnerRankMax.getValue(),
				0, compType);
		if(exception != null) {
			frame.editStation(exception, newStation);
		} else {
			frame.addStation(newStation);
		}
		this.setVisible(false);
	}
	/**
	 * checks if the minRank is less than maxRank (only if the comboBox for rankPref is selected
	 * Because if not it doesn't matter.
	 * @return boolean ok or not
	 */
	boolean checkRankRequirements() {
		if((int)spinnerRankMin.getValue() > (int)spinnerRankMax.getValue() && !comboBoxRankPref.getSelectedItem().equals(MasterStationList.rankPreference.allow)) {
			JOptionPane.showMessageDialog(null, "Please ensure min rank is not higher than max rank");
			return false;
		}
		if((int)spinnerRankMin.getValue() < 1 && !comboBoxRankPref.getSelectedItem().equals(MasterStationList.rankPreference.allow)) {
			JOptionPane.showMessageDialog(null, "Please ensure min rank is not lower than 0");
			return false;
		}
		return true;
	 }
	/**
	 * checks if the max rank difference is higher than -1 (only if taking ranks into account)
	 * @return boolean ok or not
	 */
	boolean checkMaxRankDifference() {
		if((int)spinnerRankDifference.getValue() <= 0 && chckbxTakeRanksInto.isSelected()) {
			JOptionPane.showMessageDialog(null, "Please input a rank difference of "
					+ "more than 0!");
			return false;
		}
		return true;
	}
	/**
	 * checks if the number of players in min, pref, and max all match up and do not
	 * go below 1. 
	 * @return ok or not
	 */
	boolean checkNumOfPlayers() {
		if((int)spinnerMin.getValue() > (int) spinnerPref.getValue()) {
			JOptionPane.showMessageDialog(null, "please make sure the minimum "
					+ "number of players is not higher than the preferred number "
					+ "of players!");
			return false;
		}
		if((int)spinnerPref.getValue() > (int) spinnerMax.getValue()) {
			JOptionPane.showMessageDialog(null, "please make sure the preferred number "
					+ "of players is not higher than the maximum number of players!");
			return false;
		}
		if((int)spinnerMin.getValue() <= 0) {
			JOptionPane.showMessageDialog(null, "please make sure the number of "
					+ "minimum players is not less than 1!");
			return false;
		}
		if((int)spinnerPref.getValue() <= 0) {
			JOptionPane.showMessageDialog(null, "please make sure the number of "
					+ "preferred players is not less than 1!");
			return false;
		}
		if((int)spinnerMax.getValue() <= 0) {
			JOptionPane.showMessageDialog(null, "please make sure the number of "
					+ "maximum players is not less than 1!");
			return false;
		}
		return true;
	}
	void initComponents() {
		setTitle("Add New Station");
		setBounds(100, 100, 475, 375);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		JLabel lblStationName = new JLabel("Station name:");
		lblStationName.setToolTipText("name of the station for refernece");
		JLabel lblStationDescription = new JLabel("Station tag:");
		lblStationDescription.setToolTipText("<HTML>\nA short description of the station. <br>\nTables with the EXACT same description are treated as the same and will avoid repeating players<br>\n(e.g. 3 singles tables all with the desc. \"singles\" will all try to prevent having the same <br> player in each of the stations 2 weeks in a row.)");
		lblMinimumNumberOf = new JLabel("Minimum number of players:");
		lblMinimumNumberOf.setToolTipText("<HTML>\nThe minimum number of players in this station<br>\nEnsure that the total # of min. players in all tables is less than or equal to # of players <br>\nAlso make sure min player is less than or equal to preferred/maximum number of players");
		lblPreferredNumberOf = new JLabel("Preferred number of players:");
		lblPreferredNumberOf.setToolTipText("<HTML>\n\nThe preferred number of players in this station<br>\nThe algorithm will try to achieve this number of players <br>\nensure it is less than or equal to max players\n");
		lblMaximumNumberOf = new JLabel("Maximum number of players:");
		lblMaximumNumberOf.setToolTipText("<HTML>\nMaximum number of players in this station <br>\nnumber of players will never exceed this number.\n");
		
		txtName = new JTextField("");
		txtName.setColumns(10);
		
		txtDesc = new JTextField("");
		txtDesc.setColumns(10);
		
		chckbxTakeRanksInto = new JCheckBox("Take ranks into account");
		chckbxTakeRanksInto.setToolTipText("<HTML>\nShould the station take ranks into account?\nUsually marked true for ranked matches. If not marked, will disregard rank\n");
		
		spinnerMin = new JSpinner();
		
		spinnerPref = new JSpinner();
		
		spinnerMax = new JSpinner();
		
		lblMaximumRankDifference = new JLabel("Maximum rank difference:");
		lblMaximumRankDifference.setVisible(false);
		lblMaximumRankDifference.setToolTipText("<HTML>\nMaximum rank difference between players in this station. <br>\nexample: if set to 3, then rank 6 might be paired with rank 3-9. ");
		
		spinnerRankDifference = new JSpinner();
		spinnerRankDifference.setVisible(false);
		
		comboBoxGenderPref = new JComboBox<MasterStationList.genderPreference>();
		for(MasterStationList.genderPreference pref:  MasterStationList.genderPreference.values()) {
			comboBoxGenderPref.addItem(pref);
		}
		JLabel lblNewLabel = new JLabel("Players of differing genders");
		lblNewLabel.setToolTipText("<HTML>\nLevel of sexism (haha just kidding) <br>\nAllow means that it doesn't care and will pair men and women indiscriminately. ");
		
		comboBoxRankPref = new JComboBox<MasterStationList.rankPreference>();
		for(MasterStationList.rankPreference pref: MasterStationList.rankPreference.values()) {
			comboBoxRankPref.addItem(pref);
		}
		comboBoxRankPref.setSelectedItem(MasterStationList.rankPreference.allow);
		lblRankPref = new JLabel("Players of any rank:");
		lblRankPref.setToolTipText("<HTML>\nWhat should be done with players in the ranks to the left. <br>\nExample: AVOID 3 - 6 means to try preventing the ranks 3-6 from being chosen. <br>\nRestrict: will never be these ranks. <br>\nStrongly avoid: will almost never be these ranks<br> \nAvoid: will avoid these ranks<br>\nAllow: doesn't care. Can leave the numbers at 0.<br>\nPrioritize: will prioritize these ranks<br>\nStrongly prior: will strongly prioritize these ranks<br>\nRequire: will always be these ranks. <br>");
		
		spinnerRankMin = new JSpinner();
		spinnerRankMin.setValue(1);
		spinnerRankMax = new JSpinner();
		spinnerRankMax.setValue(1);
		lblMin = new JLabel("Min\n");
		lblMin.setHorizontalAlignment(SwingConstants.CENTER);
		
		lblMax = new JLabel("Max");
		lblMax.setHorizontalAlignment(SwingConstants.CENTER);
		
		lblDash = new JLabel("-");
		
		rdbtnDoubles = new JRadioButton("Partners System");
		
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(lblStationName)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtName, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(lblStationDescription)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtDesc, GroupLayout.PREFERRED_SIZE, 234, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(comboBoxRankPref, Alignment.LEADING, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(comboBoxGenderPref, Alignment.LEADING, 0, 127, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel)
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addComponent(lblRankPref, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(spinnerRankMin, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
										.addGroup(gl_contentPanel.createSequentialGroup()
											.addGap(6)
											.addComponent(lblMin)
											.addPreferredGap(ComponentPlacement.UNRELATED)
											.addComponent(lblDash)))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
										.addComponent(lblMax, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(spinnerRankMax, GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)))))
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING, false)
							.addGroup(gl_contentPanel.createSequentialGroup()
								.addGap(6)
								.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
									.addGroup(gl_contentPanel.createSequentialGroup()
										.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
											.addComponent(lblMinimumNumberOf)
											.addComponent(lblPreferredNumberOf))
										.addGap(15)
										.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
											.addComponent(spinnerMin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
											.addGroup(gl_contentPanel.createSequentialGroup()
												.addComponent(spinnerPref, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
												.addComponent(rdbtnDoubles))))
									.addGroup(gl_contentPanel.createSequentialGroup()
										.addComponent(lblMaximumNumberOf)
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(spinnerMax, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
								.addPreferredGap(ComponentPlacement.RELATED))
							.addGroup(Alignment.LEADING, gl_contentPanel.createSequentialGroup()
								.addComponent(chckbxTakeRanksInto)
								.addGap(14)
								.addComponent(lblMaximumRankDifference)
								.addGap(10)
								.addComponent(spinnerRankDifference, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap(39, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblStationName)
						.addComponent(txtName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblStationDescription)
						.addComponent(txtDesc, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(9)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(rdbtnDoubles)
							.addGap(40))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblMinimumNumberOf)
								.addComponent(spinnerMin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblPreferredNumberOf)
								.addComponent(spinnerPref, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(7)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblMaximumNumberOf)
								.addComponent(spinnerMax, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(chckbxTakeRanksInto)
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addGap(5)
									.addComponent(lblMaximumRankDifference)))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(comboBoxGenderPref, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel)))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(4)
							.addComponent(spinnerRankDifference, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(comboBoxRankPref, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblRankPref)))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(6)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(spinnerRankMin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(spinnerRankMax, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMin)
						.addComponent(lblDash)
						.addComponent(lblMax))
					.addContainerGap(27, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			
			btnHelp = new JButton("Help");
			btnHelp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					openHelp();
				}

			});
			buttonPane.add(btnHelp);
			{
				okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		updateRankPrefUI();
	}
	private void openHelp() {
		StationHelpBox newFrame = new StationHelpBox(this);
		newFrame.setVisible(true);
	}
}
