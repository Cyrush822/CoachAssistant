import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import net.miginfocom.swing.MigLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.LayoutStyle.ComponentPlacement;
public class ConvertedFrame extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JLabel lblSuccess;
	private JButton btnDelete;
	private JButton btnRevealInFinder;
	private JButton btnOpenDoc;
	private JButton okButton;
	private File doc;
	private File stationListFile;
	private File rdDocDir;
	private File rdConfigDir;
	private File docDir;
	private File configDir;
	private JLabel lblDocName;
	/**
	 * Create the dialog.
	 */
	public ConvertedFrame(File stationListFile, File doc, File rdDocDir, File rdConfigDir, File docDir, File configDir) {
		this.stationListFile = stationListFile;
		this.doc = doc;
		this.rdConfigDir = rdConfigDir;
		this.rdDocDir = rdDocDir;
		this.docDir = docDir;
		this.configDir = configDir;
		initComponents();
		createEvents();
		lblDocName.setText("<HTML><div text-align: center>word doc name:<br>" + doc.getName() + "</div>");
	}
	public void initComponents() {
		setBounds(100, 100, 436, 171);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			lblSuccess = new JLabel("<HTML>\nSuccess! File converted to Word <br>\n<div text-align: center>\n<p style=\"font-size:12px;\">\nConfiguration saved to \"history\"\n</p>\n</div>");
			lblSuccess.setHorizontalAlignment(SwingConstants.CENTER);
			lblSuccess.setHorizontalTextPosition(SwingConstants.CENTER);
			lblSuccess.setFont(new Font("Lucida Grande", Font.PLAIN, 24));
		}
		
		lblDocName = new JLabel("docName");
		lblDocName.setHorizontalAlignment(SwingConstants.CENTER);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblSuccess, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
					.addContainerGap())
				.addComponent(lblDocName, GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblSuccess)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblDocName)
					.addContainerGap(22, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				btnDelete = new JButton("Undo");
				btnDelete.setForeground(Color.RED);
				btnDelete.setActionCommand("OK");
				buttonPane.add(btnDelete);
			}
			{
				btnRevealInFinder = new JButton("Reveal in finder");
				btnRevealInFinder.setActionCommand("OK");
				buttonPane.add(btnRevealInFinder);
			}
			{
				btnOpenDoc = new JButton("Open Doc");
				btnOpenDoc.setActionCommand("OK");
				buttonPane.add(btnOpenDoc);
			}
			{
				okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
	public void createEvents() {
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to "
						+ "undo this configuration? \n\n"
						+ "Both the word document and the configuration in history "
						+ "will be deleted \n(this configuration will not be factored "
						+ "\ninto subsequent configurations) \n\n"
						+ "the oldest configuration that was deleted to make space for this \n"
						+ "configuration will also be restored automagically. \n\n"
						+ "Basically, it will be as if the done button was never pressed. \n\n"
						+ "Use ONLY when a misclick or mistake occurred.");
				if(option != JOptionPane.YES_OPTION) {
					return;
				}
				stationListFile.delete();
				doc.delete();
				File rdDoc = null;
				File rdConfig = null;
				if(rdDocDir.exists() && rdDocDir.listFiles().length > 0) {
					rdDoc = rdDocDir.listFiles()[0];
				}
				if(rdConfigDir.exists() && rdConfigDir.listFiles().length > 0) {
					rdConfig = rdConfigDir.listFiles()[0];
				}
				if(!configDir.exists()) configDir.mkdir();
				if(!docDir.exists()) docDir.mkdir();
				if(rdConfig != null) {
					rdConfig.renameTo(new File(configDir.getPath() + "/" + rdConfig.getName()));
				}
				if(rdDoc != null) {
					rdDoc.renameTo(new File(docDir.getPath() + "/" + rdDoc.getName()));
				}
				rdDocDir.delete();
				rdConfigDir.delete();
				setVisible(false);
			}
		});
		btnRevealInFinder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					System.out.println(doc.getCanonicalPath());
					System.out.println(doc.getParentFile());
					Runtime.getRuntime().exec("open "+ doc.getParentFile().getCanonicalPath()).waitFor();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "error! File not found."
							+ " Please ensure the file was not deleted");
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnOpenDoc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!Desktop.isDesktopSupported()){
		            System.out.println("Desktop is not supported");
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
		});
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
	}
}
