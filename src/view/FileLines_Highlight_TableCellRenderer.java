package view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class FileLines_Highlight_TableCellRenderer extends DefaultTableCellRenderer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 770626024189147808L;

	public FileLines_Highlight_TableCellRenderer(int start_line, int end_line)
	{
		super();
		this.start_line = start_line;
		this.end_line = end_line;
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col)
	{
		//Cells are by default rendered as a JLabel.
		JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
		l.setBackground(Color.WHITE);
		l.setEnabled(true);
		
		if((this.start_line >0)&&(this.end_line >0))
		{
			if(col==0)
			{
				try
				{
					int line = ((Integer) value).intValue();
					if((line>=start_line)&&(line<=end_line))
						l.setBackground(Color.GREEN);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		else
		{
			if(this.start_line >0)
			{
				if(col==0)
				{
					try
					{
						int line = ((Integer) value).intValue();
						if(line==start_line)
							l.setBackground(Color.RED);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
			
			if(this.end_line >0)
			{
				if(col==0)
				{
					try
					{
						int line = ((Integer) value).intValue();
						if(line==end_line)
							l.setBackground(Color.RED);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		//Return the JLabel which renders the cell.
		return l;
	}
	
	private int start_line;
	private int end_line;
}
