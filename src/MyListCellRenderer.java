import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

public class MyListCellRenderer extends DefaultListCellRenderer{
	MasterStationList stationList;
	public MyListCellRenderer(MasterStationList stationList) {
		this.stationList = stationList;
	}
	public Component getListCellRendererComponent(
	        JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	    {
	        // I know DefaultListCellRenderer always returns a JLabel
	        // super setups up all the defaults
	        JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

	        // "value" is whatever object you put into the list, you can use it however you want here

	        // I'm going to prefix the label text to demonstrate the point
	       if(stationList.isTagDuplicate((Station)value)) {
	    	   label.setText("<html> <p> <font color=" + stationList.getNewTagColor((Station)value) + ">" + label.getText() + "</font> </p> </html>");
	       } else {
	    	   label.setText(label.getText());
	       }
	       return label;

	    }
}
