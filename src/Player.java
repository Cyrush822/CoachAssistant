import java.io.File;
import java.io.Serializable;

import javax.swing.JOptionPane;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
public class Player implements Serializable {
	private String dirName = "players";
	private String name;
	private int rank;
	private boolean isMale;
	private boolean isAbsent;
	private int numOfPractice;
	private float avgRank;//avgRank = rank sum / numOfPractice
	private Player partner;
	private int playerID;
	/**
	makes a new Player object, which stores all the information about one player.
	@param String name, int rank, boolean isMale, boolean isAbsent
	@return Player
	*/
	public Player(String setName, int setRank, boolean setIsMale, boolean setIsAbsent, int playerID) {
		name = setName;
		rank = setRank;
		isMale = setIsMale;
		isAbsent = setIsAbsent;
		avgRank = 0;
		partner = null;
		this.playerID = playerID;
	}
	/**
	makes a new Player object, which stores all the information about one player.
	@param String name, int rank, boolean isMale, boolean isAbsent, Player partner
	@return Player
	*/
	public Player(String setName, int setRank, boolean setIsMale, boolean setIsAbsent, Player partner, int playerID) {
		name = setName;
		rank = setRank;
		isMale = setIsMale;
		isAbsent = setIsAbsent;
		avgRank = 0;
		this.playerID = playerID;
		this.partner = partner;
	}
	
	public String toString() {
		return this.name + "(" + this.rank + ")";
	}
	
	public String genderToString(boolean isMale) {
		if(isMale)
			return "M";
		return "F";
	}
	public void updateAvgRanking() {
		avgRank = ((avgRank * numOfPractice) + rank) /(numOfPractice + 1);
		numOfPractice++;
		save();
	}
	/**
	saves this player. Deletes the old version and serializes the current version. 
	Saves to a folder called "Player"
	WARNING: if the name has been changed, would delete the wrong version. 
	*/
	public void save() {
		File directory = new File(dirName);
	    if (! directory.exists()){
	        directory.mkdir();
	    }
	    File oldTable = new File(dirName + "/" + name);
	    if(oldTable.exists()) {
	    	oldTable.delete();
	    }
	    try {
            FileOutputStream fileOut = new FileOutputStream(directory + "/" + name);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(this);
            objectOut.close();
            System.out.println("The Object was succesfully written to a file");
 
        } catch (Exception ex) {
        	int option = JOptionPane.showConfirmDialog(null, "an error has occurred while loading a player"
        			+ " (or perhaps all of them). This usually happens if there was an update "
        			+ "or if a player file was corrupted. The only option is to try"
        			+ "deleting all players, or contact Cyrus. Should the player folder be deleted?. ");
        	if(option == 0) {
        		File dir = new File(dirName);
        		dir.delete();
        	}
            ex.printStackTrace();
        }
	}
	/**
	deletes the saved version of the player. 
	WARNING: if the name has been changed, would delete the wrong version. 
	*/
	public void delete() {
		File thisTable = new File(dirName + "/" + name);
		if(thisTable.exists()) {
	    	thisTable.delete();
	    }
	}
	/**
	returns the path to the serialized object.
	*/
	public String getPath() {
		return dirName + "/" + name;
	}
	
	public String getName() {
		return name;
	}
	/**
	changes the name of the object. Automatically deletes the old version (which is 
	under a different name), and saves it immediately afterwards.
	*/
	public void setName(String newName) {//dangerous. may lead to previous object still persisting
		delete();
		name = newName;
		save();
	}
	
	public int getRank() {
		return rank;
	}
	
	public void setRank(int newRank) {
		rank = newRank;
		save();
	}
	
	public boolean getIsMale() {
		return isMale;
	}
	
	public void setIsMale(boolean isMale) {
		this.isMale = isMale;
		save();
	}

	public boolean getIsAbsent() {
		return isAbsent;
	}

	public void setIsAbsent(boolean isAbsent) {
		this.isAbsent = isAbsent;
		save();
	}

	public float getAvgRank() {
		return avgRank;
	}

	public void setAvgRank(float avgRank) {
		this.avgRank = avgRank;
		save();
	}

	public Player getPartner() {
		return partner;
	}
	public boolean hasPartner() {
		if(partner == null) {
			return false;
		}
		return true;
	}
	public void deletePartner() {
		partner = null;
		save();
	}
	public void setPartner(Player partner) {
		this.partner = partner;
		save();
	}

	public void setMale(boolean isMale) {
		this.isMale = isMale;
		save();
	}

	public void setAbsent(boolean isAbsent) {
		this.isAbsent = isAbsent;
		save();
	}
	public int getPlayerID() {
		return playerID;
	}
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	public boolean compareID(int playerID) {
		return this.playerID == playerID;
	}
}
