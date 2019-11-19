package view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class File_Change_TableCellRenderer extends DefaultTableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1372041839957501828L;

	public File_Change_TableCellRenderer() {
		super();
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col)
	{
		//Cells are by default rendered as a JLabel.
		JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
		l.setBackground(Color.WHITE);
		l.setEnabled(true);
		if(col==1)
		{
			try
			{
				String line = (String) value;
				if(line.startsWith("+"))
					l.setBackground(Color.GREEN);
				else if(line.startsWith("-"))
					l.setBackground(Color.RED);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			l.setEnabled(false);
		}
		//Return the JLabel which renders the cell.
		return l;
	}
}
