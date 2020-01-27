import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class EditPlayerBox extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtIndex;
	private JTextField txtName;
	private JButton btnOK;
	private JComboBox cbGender;
	private MasterPlayerList playerList;
	Frame1 Frame;
	enum gender
	{
		male, female;
	}
	/**
	 * Launch the application.
	 */
	/**
	 * Create the dialog.
	 */
//	try {
//		EditPlayerBox dialog = new EditPlayerBox();
//		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//		dialog.setVisible(true);
//	} catch (Exception e) {
//		e.printStackTrace();
//	}
	public EditPlayerBox(MasterPlayerList playerList, Frame1 F1) {
		this.playerList = playerList;
		Frame = F1;
		setTitle("Edit Player");
		setResizable(false);
		setModal(true);
		setAlwaysOnTop(true);
		setBounds(100, 100, 450, 175);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel lblEditPlayer = new JLabel("Edit Existing Player");
		
		txtIndex = new JTextField();
		txtIndex.setColumns(10);
		
		JLabel lblName = new JLabel("New name");
		
		txtName = new JTextField();
		txtName.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(txtName.getText().length() > 11) {
					txtName.setText(txtName.getText().substring(0,11));
				}
			}
		});
		txtName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(txtName.getText().length() > 11) {
					txtName.setText(txtName.getText().substring(0,11));
				}
			}
		});
		txtName.setColumns(10);
		
		JLabel lblIndex = new JLabel("Ranking");
		lblIndex.setHorizontalAlignment(SwingConstants.TRAILING);
		
		JLabel lblGender = new JLabel("Gender");
		lblGender.setHorizontalAlignment(SwingConstants.TRAILING);
		
		cbGender = new JComboBox<MasterStationList.genderPreference>();
		cbGender.setMaximumRowCount(2);
		cbGender.setModel(new DefaultComboBoxModel(gender.values()));
		cbGender.setSelectedIndex(0);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblName)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtName, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblGender, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(cbGender, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblIndex, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtIndex, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(153)
							.addComponent(lblEditPlayer)))
					.addContainerGap(28, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addComponent(lblEditPlayer)
					.addGap(16)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblIndex)
						.addComponent(txtIndex, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblName)
						.addComponent(txtName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblGender)
						.addComponent(cbGender, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(14, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				btnOK = new JButton("OK");
				btnOK.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						while(true)
						{
							int ranking;
							try {
								ranking = Integer.parseInt(txtIndex.getText());
							} 
							catch(Exception a){
								JOptionPane.showMessageDialog(null, "ERROR! Please input a number");
								break;
							}
							if(playerList.getLastRank() < ranking || ranking < 1) {
								JOptionPane.showMessageDialog(null, "ERROR! Please input a valid rank.");
								return;
							}
							if(!playerList.isNameOk(txtName.getText(), ranking)) {
								return;
							}
							Frame.editPlayer(ranking, txtName.getText(), cbGender.getSelectedIndex() == 0);
							setVisible(false);
							break;
						}
					}
				});
				btnOK.setActionCommand("OK");
				buttonPane.add(btnOK);
				getRootPane().setDefaultButton(btnOK);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
