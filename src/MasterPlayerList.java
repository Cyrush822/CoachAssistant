import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.*;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JToolBar;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.GridLayout;
import net.miginfocom.swing.MigLayout;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import java.util.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.xml.*;
/**
Stores, organises, and performs operations on each Player Object.
@param either an array of player, or the directory of serialized players
*/
public class MasterPlayerList implements Serializable{
	ArrayList<Player> players;
	File directory;
	int lastRank;
	public MasterPlayerList(Player[] players) {
		this.players = new ArrayList<Player>();
		for(int i = 0; i < players.length; i++) {
			this.players.set(i, players[i]);
			lastRank++;
		}
	}
	public MasterPlayerList(File directory) {
		if(!directory.exists()) {
			directory.mkdir();
		}
		players = new ArrayList<Player>();
		this.directory = directory;
		File[] allFiles = directory.listFiles();
		for(int i = 0; i < allFiles.length; i++) {
			players.add(deserializePlayer(allFiles[i]));
			lastRank++;
		}
	}
	
	public int getNumOfPlayers() {
		int num = 0;
		for(Player player : players) {
			if(!player.getIsAbsent()) {
				num++;
			}
		}
		return num;
	}
	/**
	 * updates everyone's rank to their position in the list + 1.
	 * Use after deleting or moving anyone. 
	 * Requires the list to be sorted. 
	 */
	public void updateRanks() {
		for(int i = 0; i < players.size(); i++) {
			players.get(i).setRank(i + 1);
		}
	}
	/**
	Returns a player according to index (ranking - 1), assuming the list is sorted.
	@return Player
	@exception IndexOutOfBoundsException
	*/
	public Player getPlayer(int ranking) throws IndexOutOfBoundsException{
		return players.get(ranking - 1);
	}
	
	/**
	Adds a player to the list stored in this object, and then sorts it.
	@param Player object
	@exception NullPointerException
	*/
	public void addPlayer(Player player) throws NullPointerException {
		players.add(player);
		recalculateLastRank();
		sort();
	}
	/**
	Adds a player to the list stored in this object at the given index, and then sorts it.
	@param Player object
	@param int index to add in
	@exception NullPointerException
	*/
	public void addPlayer(Player player, int index) throws NullPointerException {
		players.add(index, player);
		recalculateLastRank();
		sort();
	}
	/**
	 * changes information of a player. 
	 * TBI: changing partner, and other stuff .
	 * PRE CONDITION: NAME MUST NOT HAVE OVERLAP.  
	 * @param ranking
	 * @param newPlayer
	 */
	public void editPlayer(int ranking, Player newPlayer) {
		sort();
		Player playerToRemove = players.get(ranking - 1);
		boolean isAbsent = playerToRemove.getIsAbsent();
		deletePlayer(ranking);
		newPlayer.setAbsent(isAbsent);
		addPlayer(newPlayer, ranking - 1);//should add player to that particular index. 
		recalculateLastRank();
		newPlayer.save();
		sort();
		updateRanks();
	}
	/**
	 * updates the last rank according to the size of the players list. 
	 */
	
	/**
	Deletes a player to the list stored in this object, and also it's associated
	Serialised object. 
	@param int ranking
	@exception IndexOutOfBoundsExceptio[n
	*/
	public void deletePlayer(int ranking){
		sort();
		players.get(ranking - 1).delete();
		players.remove(ranking - 1);
		updateRanks();
		sort();
		recalculateLastRank();
		System.out.println(toString());
	}
	/**
	 * actually swaps two players within the ranks. Adjusts ranks as necessary. 
	 * @param rank1
	 * @param rank2
	 */
	public void swapPlayers(int rank1, int rank2) {
		this.swapInList(rank1, rank2);
		this.updateRanks();
		players.get(rank1 - 1).save();
		players.get(rank2 - 1).save();
	}
	/**
	 * moves rank2 to rank1, and shifts everything down.
	 * @param rank1
	 * @param rank2
	 */
	public void beatPlayer(int rank1, int rank2) {//rank1 is lower (better and got beat)
		Player player1 = players.get(rank1 - 1);
		Player player2 = players.get(rank2 - 1);
		players.remove(rank2 - 1);
		players.add(rank1 - 1, player2);
		this.updateRanks();
		player1.save();
		player2.save();
	}
	public void recalculateLastRank() {
		lastRank = players.size();
	}
	
	public int getLastRank() {
		return lastRank;
	}
	public String toString() {
		String str = "";
		for(Player P: players) {
			str += P.getName() + " rank" + P.getRank();
			str += "\n";
		}
		return str;
	}
	public boolean isNameOk(String name) {
		for(int i = 0; i < players.size(); i++) {
			if(players.get(i).getName().equals(name)) {
				JOptionPane.showMessageDialog(null, "this name already exists! "
						+ "Please use another name or include a last name.");
				return false;
			}
		}
		if(name.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Nobody has a blank name come on");
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param name to be checked
	 * @param exception (ranking of the player that is the exception). 
	 * @return
	 */
	public boolean isNameOk(String name, int exception) {
		for(int i = 0; i < players.size(); i++) {
			if(players.get(i).getName().equals(name)) {
				if(i != exception - 1) {
					JOptionPane.showMessageDialog(null, "this name already exists! "
							+ "Please use another name or include a last name.");
					return false;
				}
			}
		}
		if(name.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Nobody has a blank name come on");
			return false;
		}
		return true;
	}
	/**
	sorts the players in the list. Currently uses bubble sort. 
	*/
	public void sort() {
		for(int i = players.size(); i > 0; i--) {
			for(int a = 0; a < i - 1; a++) {
				int rank1 = players.get(a).getRank();
				int rank2 = players.get(a + 1).getRank();
				if(rank1 > rank2) {
					swapInList(rank1, rank2);
				}
			}
		}
	}
	
	/**
	returns the playerList within this object, already sorted.
	@return ArrayList<Player> players
	*/
	public ArrayList<Player> getPlayerList() {
		sort();
		return players;
	}
	
	/**
	swaps two players in the list (not their actual rankings)
	@param int firstRanking, int secondRanking.
	*/
	private void swapInList(int ranking1, int ranking2) {
		//precondition: must be within bounds
		Player holder = players.get(ranking1 - 1);
		players.set(ranking1 - 1, players.get(ranking2 - 1));
		players.set(ranking2 - 1,  holder);
	}
	
	/**
	Takes a file and deserializes it, and then casts it into a player, returning it.
	@param File file
	@return Player
	@throws IOException, ClassNotFoundException
	*/
	public Player deserializePlayer(File filename) {
		try
        {    
            // Reading the object from a file 
            FileInputStream file = new FileInputStream(filename); 
            ObjectInputStream in = new ObjectInputStream(file); 
              
            // Method for deserialization of object 
            Player player = (Player)in.readObject(); 
              
            in.close(); 
            file.close(); 
              
            System.out.println("Object has been deserialized "); 
            return player;
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
