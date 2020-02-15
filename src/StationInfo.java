import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class StationInfo extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JLabel lblName;
	private JLabel lblMustBeEvens;
	private JLabel lblIsranked;
	private JLabel lblMaximumRankDifference;
	private JLabel lblGenderPref;
	private JLabel lblDisabled;
	private Station station;
	private JLabel lblDesc;
	private JLabel lblRankPref;
	private JLabel lblTables;
	private JLabel lblPlayers;
	private JPanel buttonPane;
	/**
	 * Launch the application.
	 */
	/**
	 * Create the dialog.
	 */
	
	public void updateAll() {
		lblName.setText(station.getStationName());
		lblDesc.setText(station.getStationDesc());
		if(station.getCompType() == MasterStationList.competitiveType.notCompetitive) {
			lblPlayers.setText("Number Of Players: min: " + station.getMinPlayers() + " pref: " + station.getPreferredPlayers() + " max: " + station.getMaxPlayers());
		} else {
			if(station.getCompType() == MasterStationList.competitiveType.doubles) {
				lblPlayers.setText("Number Of Players: Doubles (4)");
			} else {
				lblPlayers.setText("Number Of Players: Singles (2)");
			}
		}
		lblMustBeEvens.setText("Must Be Evens: " + Boolean.toString(station.isMustBeEven()).toUpperCase());
		lblIsranked.setText("Is Ranked: " + Boolean.toString(station.isRanked()).toUpperCase());
		if(station.isRanked()) {
			lblMaximumRankDifference.setText("Maximum Rank Difference: " + station.getMaxRankDifference());
		} else {
			lblMaximumRankDifference.setVisible(false);
		}
		lblDisabled.setText("Disabled: " + Boolean.toString(station.isDisabled()).toUpperCase());
		lblTables.setText(Integer.toString(station.getNumberOfTables()));
		lblRankPref.setText(station.getRankPreferenceStr());
		lblGenderPref.setText(station.getGenderPreferenceStr());
	}
	public StationInfo(Station station) {
		setModal(true);
		this.station = station;
		setTitle("Station Info ");
		setBounds(100, 100, 550, 235);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		JLabel lblTableName = new JLabel("Table Name:");
		
		JLabel lblTableDescription = new JLabel("Table Description:");
		
		lblPlayers = new JLabel("Number Of Players: Singles");
		
		lblIsranked = new JLabel("isRanked:");
		
		lblMaximumRankDifference = new JLabel("Maximum Rank Difference: ");
		
		JLabel lblGenderPreference = new JLabel("Gender Preference: ");
		
		JLabel lblRankingPreference = new JLabel("Ranking Preference:");
		
		JLabel lblTablesRequired = new JLabel("Tables Required:");
		
		lblDisabled = new JLabel("Disabled:");
		
		lblName = new JLabel("name here");
		
		lblDesc = new JLabel("desc here");
		
		lblGenderPref = new JLabel("genderPref");
		
		lblRankPref = new JLabel("rankPref");
		
		lblTables = new JLabel("tables");
		
		lblMustBeEvens = new JLabel("Must Be Evens:");
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblDisabled, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 134, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(lblTableName)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblName, GroupLayout.PREFERRED_SIZE, 189, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(267, Short.MAX_VALUE))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(lblTableDescription)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblDesc, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addComponent(lblTablesRequired)
									.addGap(12)
									.addComponent(lblTables, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPanel.createSequentialGroup()
											.addComponent(lblRankingPreference)
											.addPreferredGap(ComponentPlacement.UNRELATED))
										.addComponent(lblIsranked, GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
										.addComponent(lblGenderPreference, GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(lblGenderPref, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblRankPref, GroupLayout.PREFERRED_SIZE, 192, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblMaximumRankDifference, GroupLayout.PREFERRED_SIZE, 271, GroupLayout.PREFERRED_SIZE))
									.addGap(1)))
							.addGap(1533))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(lblPlayers, GroupLayout.PREFERRED_SIZE, 381, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblMustBeEvens)
							.addGap(732))))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTableName)
						.addComponent(lblName))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTableDescription)
						.addComponent(lblDesc))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPlayers)
						.addComponent(lblMustBeEvens))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblIsranked)
						.addComponent(lblMaximumRankDifference))
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblGenderPreference)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblRankingPreference))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(6)
							.addComponent(lblGenderPref)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblRankPref)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTables)
						.addComponent(lblTablesRequired))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblDisabled)
					.addGap(13))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						close();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(contentPanel, GroupLayout.PREFERRED_SIZE, 550, Short.MAX_VALUE)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(buttonPane, GroupLayout.PREFERRED_SIZE, 550, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(contentPanel, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(buttonPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18))
		);
		getContentPane().setLayout(groupLayout);
		updateAll();
	}
	public void close() {
		this.setVisible(false);
	}
}
