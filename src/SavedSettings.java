import java.util.*;

import javax.swing.JOptionPane;

import java.io.*;
public class SavedSettings implements Serializable{
	
	int configsSaved = 2;
	boolean partnerSystemOn = true;
	boolean confirmDialogue = true;
	public String fileName = "AdvSettings";
	
	public void SavedSettings() {
		
	}
	
	public void save() {
	    File oldFile = new File(fileName);
	    if(oldFile.exists()) {
	    	oldFile.delete();
	    }
	    try {
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(this);
            objectOut.close();
            System.out.println("The Object was succesfully written to a file");
 
        } catch (Exception ex) {
        	int option = JOptionPane.showConfirmDialog(null, "error while trying to save"
        			+ " advanced settings. Please restart and try again.");
            ex.printStackTrace();
        }
	}
	public void delete() {
		File thisFile = new File(fileName);
		if(thisFile.exists()) {
	    	thisFile.delete();
	    }
	}
	public int getConfigsSaved() {
		return configsSaved;
	}

	public void setConfigsSaved(int configsSaved) {
		this.configsSaved = configsSaved;
	}

	public boolean isPartnerSystemOn() {
		return partnerSystemOn;
	}

	public void setPartnerSystemOn(boolean partnerSystemOn) {
		this.partnerSystemOn = partnerSystemOn;
	}

	public boolean isConfirmDialogue() {
		return confirmDialogue;
	}

	public void setConfirmDialogue(boolean confirmDialogue) {
		this.confirmDialogue = confirmDialogue;
	}
}
