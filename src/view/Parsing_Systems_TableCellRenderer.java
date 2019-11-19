package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import persistence.System_Version;

public class Parsing_Systems_TableCellRenderer extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7230467322993859278L;

	public Parsing_Systems_TableCellRenderer() {
		super();
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col)
	{
		JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
		
		System_Version version = (System_Version) value;
		
		if(version.isSaved())
			l.setBackground(Color.YELLOW);
		else
			l.setBackground(Color.GREEN);
		
		if(col==0)
		{
			String line = version.getSystem_name();
			l.setText(line);
			l.setIcon(null);
		}
		else if(col==1)
		{
			String line = version.getSystem_version();
			l.setText(line);
			l.setIcon(null);
		}
		else
		{
			//String icon_name = "system_to_parse_256x256";
			//String icon_name = "magnifying_glass_256x256";
			String icon_name = "parse_document_256x256";
			if(version.isSaved())
			{
				icon_name = "floppy_disk_save_256x256";
				l.setText("SAVED");
			}
			else
			{
				l.setText("PARSE");
			}
				
			
			l.setIcon(null);
			ImageIcon imageIcon = null;
			try {
				imageIcon = new ImageIcon(getClass().getResource("/icons/" + icon_name + ".png"));
				Image image = imageIcon.getImage(); // transform it 
				Image newimg = image.getScaledInstance(32, 32,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
				imageIcon = new ImageIcon(newimg);
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			
			if (imageIcon != null)
				l.setIcon(imageIcon);
		}
		//Return the JLabel which renders the cell.
		return l;
	}
	
	
	
}
