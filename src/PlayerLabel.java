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
import java.io.File;
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
public class PlayerLabel {
	Player player;
	int row;
	int rank;
	String name;
	boolean isMale;
	boolean isAbsent;
	JLabel lblrank;
	JLabel lblname;
	JLabel lblGender;
	JPanel parent;
	JRadioButton button;
	JPanel[] parents;
	/**
	 * The label representing the player's data in frame1. Automatically sets the row
	 * according to the players ranking.
	 * @param player
	 * @param parentsPanels
	 */
	public PlayerLabel(Player player, JPanel[] parents)  {
		this.player = player;
		this.parents = parents;
		if (rank > 12) {
			parent = parents[1];
		} else {
			parent =  parents[0];
		}
		Update(player);
	}
	public PlayerLabel(JPanel[] parents) {
		if (rank > 12) {
			parent = parents[1];
		} else {
			parent =  parents[0];
		}
	}
	/**
	 * Updates the information so a new player is it's info source.
	 * Deletes all previous labels and buttons and makes new ones. Use whenever
	 * the information of a player is changed. Automatically validates information
	 * @param player
	 */
	public void Update(Player player) {
		//will automatically put it in the right row. 
		remove();
		this.player = player;
		rank = player.getRank();
		name = player.getName();
		isMale = player.getIsMale();
		isAbsent = player.getIsAbsent();
		if (rank > 12) {
			parent = parents[1];
		} else {
			parent =  parents[0];
		}
		row = (rank - 1)% 12;
		lblrank = new JLabel(Integer.toString(rank));
		lblname = new JLabel(name);
		if(isMale) {
			lblGender = new JLabel("M");
		} else {
			lblGender = new JLabel("F");
		}
		parent.add(lblrank, "cell 0 " + row);
		parent.add(lblname, "cell 1 "  + row);
		parent.add(lblGender, "cell 2 " +  row);
		if(isAbsent) {
			lblname.setForeground(Color.red);
		} else {
			lblname.setForeground(Color.black);
		}
		button = new JRadioButton("");
		parent.add(button,  "cell 3 " + row);
		parent.validate();
		parent.setVisible(true);
	}
	/**
	 * removes all components in the panel (essentially disappearing)
	 */
	public void remove() {
		if(lblrank != null) {
			parent.remove(lblrank);
		}
		if(lblname != null) {
			parent.remove(lblname);
		}
		if(lblGender != null) {
			parent.remove(lblGender);
		}
		if(lblGender != null) {
			parent.remove(button);
		}
//		parent.remove(lblrank);
//		parent.remove(lblname);
//		parent.remove(lblGender);
//		parent.remove(button);
		parent.validate();
		parent.repaint();
	}
	/**
	 * returns if current label is selected. Use in conjunction with getPlayer()
	 * @return isSelected (radio button)
	 */
	public boolean getIsSelected() {
		return button.isSelected();
	}
	public void setIsSelected(boolean selected) {
		button.setSelected(selected);
	}
	public Player getPlayer() {
		return player;
	}
}
