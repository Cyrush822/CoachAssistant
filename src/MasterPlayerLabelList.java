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
public class MasterPlayerLabelList {

	MasterPlayerList masterList;
	ArrayList<PlayerLabel> labels;
	JPanel[] parents;
	public MasterPlayerLabelList(MasterPlayerList masterList, JPanel[] parents) {
		labels = new ArrayList<PlayerLabel>();
		this.masterList = masterList;
		this.parents = parents;
		
	}
	/**
	 * takes all the current players in the masterList it is tied to, and creates new 
	 * labels for them. Deletes all labels before, so it auto updates.
	 */
	public void updateLabels() {
		ArrayList<Player> players = masterList.getPlayerList();
		eraseAll();
		for(int i = 0; i < players.size(); i++) {
			PlayerLabel label = new PlayerLabel(players.get(i), parents);
			labels.add(label);
		}
	}
	
	public void eraseAll() {
		while(labels.size() > 0) {
			labels.get(0).remove();
			labels.remove(0);
		}
		parents[0].validate();
		parents[1].validate();
	}
	/**
	 * is this even necessary considering masterList is passed by reference, and 
	 * thus will auto update?
	 * @param masterList
	 */
	public void updatePlayerList(MasterPlayerList masterList) {
		//precondition: masterList should be sorted
		this.masterList = masterList;
	}
	/**
	 * returns the list of PlayerLabels.
	 * @return
	 */
	public ArrayList<PlayerLabel> getPlayerLabelList() {
		return this.labels;
	}
}
