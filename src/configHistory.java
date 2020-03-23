import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.*;
import java.util.*;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.AbstractListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Color;
import java.awt.Desktop;

import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class configHistory extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JButton btnViewWordDoc;
	private JButton btnDelete;
	private JButton btnViewDetails;
	private JList<FinalStationMasterList> list;
	private File savedConfigDir;
	private JScrollPane scrollPane;
	private JLabel lblEarliest;
	private JLabel lblLatest;
	private JButton okButton;

	/**
	 * Create the dialog.
	 */
	public configHistory(File savedConfigDir) {
		setTitle("Configuration History\n");
		this.savedConfigDir = savedConfigDir;
		initComponents();
		createEvents();
		updateJList(savedConfigDir);
	}
	private void sortByTime(ArrayList<FinalStationMasterList> stationLists) {
		for(int i = stationLists.size() - 1; i > 0; i--) {
			for(int a = 0; a < i; a++) {
				if(stationLists.get(a).getSavedDate().isAfter(stationLists.get(a+1).getSavedDate())) {
					FinalStationMasterList holder = stationLists.get(a);
					stationLists.set(a, stationLists.get(a+1));
					stationLists.set(a+1, holder);
				}
			}
		}
		for(int i = stationLists.size() - 1; i > 0; i--) {
			for(int a = 0; a < i; a++) {
				if(stationLists.get(a).getSavedDate().isEqual(stationLists.get(a+1).getSavedDate()) && stationLists.get(a).getSavedTime().isAfter(stationLists.get(a+1).getSavedTime())) {
					FinalStationMasterList holder = stationLists.get(a);
					stationLists.set(a, stationLists.get(a+1));
					stationLists.set(a+1, holder);
				}
			}
		}
	}
	private void updateJList(File dir) {
		if(!dir.exists()) {
			return;
		}
		File[] files = dir.listFiles();
		ArrayList<FinalStationMasterList> stationLists = new ArrayList<FinalStationMasterList>();
		for(File file:files) {
			stationLists.add(this.deserializeFinalStationMasterList(file));
		}
		sortByTime(stationLists);
		list.setModel(new AbstractListModel<FinalStationMasterList>() {
			ArrayList<FinalStationMasterList> values = stationLists;
			public int getSize() {
				return values.size();
			}
			public FinalStationMasterList getElementAt(int index) {
				return values.get(index);
			}
		});
	}
	private void openWordDoc(File doc) {
		if(!Desktop.isDesktopSupported()){
            JOptionPane.showMessageDialog(null, "desktop not supported.");
            return;
        }
        
        Desktop desktop = Desktop.getDesktop();
        if(doc.exists())
			try {
				desktop.open(doc);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "error!");
				e1.printStackTrace();
			}
        
        //let's try to open PDF file
	}
	private void createEvents() {
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnViewWordDoc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(list.getSelectedValue() == null) {
					return;
				}
				if(!list.getSelectedValue().getAssociatedWordDoc().exists()) {
					JOptionPane.showMessageDialog(null, "the word document for this "
							+ "configuration doesn't exist! Was it deleted or moved?");
					return;
				}
				openWordDoc(list.getSelectedValue().getAssociatedWordDoc());
			}
		});
		btnViewDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				configDetailsFrame frame = new configDetailsFrame(list.getSelectedValue());
				frame.setVisible(true);
			}
		});
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int option = JOptionPane.showConfirmDialog(null, "Are you sure you want "
						+ "to delete this past configuration?"
						+ "\nit's corresponding word document will also be deleted"
						+ "\nand the configuration will be lost forever"
						+ "\n(hence it will not be factored into future configurations.)");
				if(option != JOptionPane.YES_OPTION) {
					return;
				}
				if(list.getSelectedValue().getAssociatedWordDoc().exists())
					list.getSelectedValue().getAssociatedWordDoc().delete();
				list.getSelectedValue().delete();
				updateJList(savedConfigDir);
			}
		});
		
	}
	private void initComponents() {
		setBounds(100, 100, 450, 260);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		btnViewDetails = new JButton("View Details");
		btnViewWordDoc = new JButton("View Word Doc");
		btnDelete = new JButton("Delete");
		btnDelete.setForeground(Color.RED);
		
		scrollPane = new JScrollPane();
		
		lblEarliest = new JLabel("Earliest");
		
		lblLatest = new JLabel("Latest");
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
						.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
							.addContainerGap(79, Short.MAX_VALUE)
							.addComponent(btnDelete)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnViewWordDoc)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnViewDetails))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(194)
							.addComponent(lblEarliest))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(194)
							.addComponent(lblLatest, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addComponent(lblEarliest)
					.addGap(8)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblLatest)
					.addGap(7)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnViewDetails)
						.addComponent(btnViewWordDoc)
						.addComponent(btnDelete))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		
		list = new JList<FinalStationMasterList>();
		scrollPane.setViewportView(list);
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
}
