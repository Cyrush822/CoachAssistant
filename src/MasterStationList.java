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
public class MasterStationList implements Serializable{
	ArrayList<Station> stations;
	File directory;
	int lastTableNumber;
	
	public static enum genderPreference {
		restrict, stronglyavoid, avoid, allow, prioritize, stronglyprioritize, require;
	}
	public static enum rankPreference {
		restrict, stronglyavoid, avoid, allow, prioritize, stronglyprioritize, require;
	}
	public static enum competitiveType {
		notCompetitive, singles, doubles;
	}
	
	
	public MasterStationList(File directory) {
		if(!directory.exists()) {
			directory.mkdir();
		}
		this.directory = directory;
		stations = new ArrayList<Station>();
		File[] allFiles = directory.listFiles();
		for(int i = 0; i < allFiles.length; i++) {
			stations.add(deserializeStation(allFiles[i]));
			lastTableNumber++;
		}
	}
	/**
	 * updates the list with the same directory
	 */
	public void updateDir() {
		if(!directory.exists()) {
			directory.mkdir();
		}
		stations = new ArrayList<Station>();
		File[] allFiles = directory.listFiles();
		for(int i = 0; i < allFiles.length; i++) {
			stations.add(deserializeStation(allFiles[i]));
			lastTableNumber++;
		}
	}
	/**
	 * returns the list of stations.
	 * @return
	 */
	public void deleteAll() {
		for(File file : directory.listFiles()) {
			file.delete();
		}
//		directory.delete();
		stations = new ArrayList<Station>();
	}
	public ArrayList<Station> getStations() {
		return stations;
	}

	public int getNumberOfMinPlayers() {
		int num = 0;
		for(Station station: stations) {
			if(!station.isDisabled()) {
				num += station.getMinPlayers();
			}
		}
		return num;
	}
	
	public int getNumberOfPrefPlayers() {
		int num = 0;
		for(Station station: stations) {
			if(!station.isDisabled()) {
				num += station.getPreferredPlayers();
			}
		}
		return num;
	}
	
	public int getNumberOfMaxPlayers() {
		int num = 0;
		for(Station station: stations) {
			if(!station.isDisabled()) {
				num += station.getMaxPlayers();
			}
		}
		return num;
	}
	/**
	 * checks if the name is ok. Returns false if it already exists or if the 
	 * name is blank (or only has spaces)
	 * @param name
	 * @return
	 */
	public boolean isNameOk(String name) {
		if(name.contains("/")) {
			JOptionPane.showMessageDialog(null, "Please do not use /, as it will crash the program.");
			return false;
		}
		if(name.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Please input a name!");
			return false;
		}
		for(Station station : stations) {
			if(station.getStationName().equals(name)) {
				JOptionPane.showMessageDialog(null, "That name already exists! Please add a "
						+ "number or choose another name.");
				return false;
			}
		}
		return true;
	}
	/**
	 * checks if the name is ok. Returns false if it already exists
	 * UNLESS it's exception or if the name is blank (or only has spaces)
	 * @param name
	 * @param exception
	 * @return
	 */
	public boolean isNameOk(String name, Station exception) {
		if(name.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Please input a name!");
			return false;
		} 
		if(name.contains("/")) {
			JOptionPane.showMessageDialog(null, "Please do not use /, as it will upset the program.");
			return false;
		}
		for(Station station : stations) {
			if(station.getStationName().equals(name) && !(station.equals(exception))) {
				JOptionPane.showMessageDialog(null, "That name already exists! Please add a "
						+ "number or choose another name.");
				return false;
			}
		}
		return true;
	}
	/**
	 * adds a station to the list. 
	 * @param table
	 */
	public void addStation(Station table) {
		stations.add(table);
		
	}
	/**
	 * removes the station from the list.
	 * @param table
	 */
	public void removeStation(Station station) {
		int n = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this station: " + station.getStationName());
		if(n == JOptionPane.YES_OPTION) {
			stations.get(stations.indexOf(station)).deleteTable();;
			stations.remove(stations.indexOf(station));
		}
	}
	public void removeStationWithoutWarning(Station station) {
		stations.remove(stations.indexOf(station));
	}
	/**
	 * deserializes the file and casts it into a station.
	 * @param filename
	 * @return
	 */
	public Station deserializeStation(File filename) {
		try
        {    
            // Reading the object from a file 
            FileInputStream file = new FileInputStream(filename); 
            ObjectInputStream in = new ObjectInputStream(file); 
              
            // Method for deserialization of object 
            Station table = (Station)in.readObject(); 
              
            in.close(); 
            file.close(); 
              
            System.out.println("Object has been deserialized "); 
            return table;
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
	public void addStation(Station newStation, int index) {
		// TODO Auto-generated method stub
		stations.add(index, newStation);
		
	}
}
