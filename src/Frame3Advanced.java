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
import javax.swing.JSpinner;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.awt.event.ActionEvent;

public class Frame3Advanced extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JRadioButton rdbtnEnablePartnersSystem;
	private JSpinner spinner;
	private JButton cancelButton;
	private JButton okButton;
	private SavedSettings settings;
	private JRadioButton rdbtnShowConfirmDialogue;
	private JSpinner spinner_TriesMultiplier;
	/**
	 * Create the dialog.
	 * @param savedSettings 
	 */
	public Frame3Advanced(SavedSettings settings) {
		this.settings = settings;
		initComponents();
		createEvents();
		spinner.setValue(settings.getConfigsSaved());
		spinner_TriesMultiplier.setModel(new SpinnerNumberModel(settings.getTriesMultiplier(), 0.25, 25, 0.25));
		rdbtnEnablePartnersSystem.setSelected(settings.isPartnerSystemOn());
		rdbtnShowConfirmDialogue.setSelected(settings.isConfirmDialogue());
		
	}
	public void createEvents() {
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int configsSaved = (int)spinner.getValue();
				if(configsSaved > 9 || configsSaved < 0) {
					JOptionPane.showMessageDialog(null, "please make sure the number of "
							+ "past configs to avoid is below 9 and above 0.");
					return;
				}
				settings.setConfigsSaved((int)spinner.getValue());
				settings.setPartnerSystemOn(rdbtnEnablePartnersSystem.isSelected());
				settings.setConfirmDialogue(rdbtnShowConfirmDialogue.isSelected());
				settings.setTriesMultiplier((float)(double)spinner_TriesMultiplier.getValue());
				settings.save();
				setVisible(false);
			}
		});
	}
	public void initComponents() {
		setBounds(100, 100, 302, 221);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel lblRememberTheLast = new JLabel("Avoid the last");
		
		spinner = new JSpinner();
		
		JLabel lblConfigurations = new JLabel("generations");
		
		rdbtnEnablePartnersSystem = new JRadioButton("Enable Partners System");
		rdbtnEnablePartnersSystem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		rdbtnShowConfirmDialogue = new JRadioButton("<HTML>\nShow Confirm Dialogue when <br>pressing \"done\"");
		
		JLabel lblOfTries = new JLabel("# of tries multiplier:");
		
		spinner_TriesMultiplier = new JSpinner();
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(7)
							.addComponent(lblRememberTheLast)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(spinner, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblConfigurations))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(rdbtnEnablePartnersSystem))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(rdbtnShowConfirmDialogue))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblOfTries, GroupLayout.PREFERRED_SIZE, 134, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(spinner_TriesMultiplier, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(63, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(10)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblRememberTheLast)
						.addComponent(lblConfigurations)
						.addComponent(spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(rdbtnEnablePartnersSystem)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(rdbtnShowConfirmDialogue)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblOfTries)
						.addComponent(spinner_TriesMultiplier, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
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
}
