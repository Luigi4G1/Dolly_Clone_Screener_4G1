package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import model.Current_Clone_File_Version;

public class Current_Clone_File_Version_Renderer extends JLabel implements ListCellRenderer<Current_Clone_File_Version>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6255429243697971184L;

	public Current_Clone_File_Version_Renderer() {
		setOpaque(true);
	}
	
	@Override
	public Component getListCellRendererComponent(JList<? extends Current_Clone_File_Version> list, Current_Clone_File_Version version, int index, 
			boolean isSelected, boolean cellHasFocus)
	{
		if(version == null)
			return this;
		
		String description = version.shortDescription();
		this.setIcon(null);
		ImageIcon imageIcon = null;
		try {
			imageIcon = new ImageIcon(getClass().getResource("/icons/" + "back_to_the_future_256x256" + ".png"));
			Image image = imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(32, 32,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			imageIcon = new ImageIcon(newimg);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		if (imageIcon != null)
			setIcon(imageIcon);
		setText(description);
		
		try
		{
			if(version.getCurrent_file_version().getDiff().getCommit().getData_commit().before(version.getClone().getChange_modify_clone().getCommit().getData_commit()))
			{
				this.setForeground(Color.RED);
				//this.setBackground(Color.RED);
				//setForeground(Color.RED);
			}
			else
			{
				this.setForeground(Color.BLACK);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		if (isSelected)
		{
			setBackground(list.getSelectionBackground());
			//setForeground(list.getSelectionForeground());
		}
		else
		{
			setBackground(list.getBackground());
			//setForeground(list.getForeground());
		}
		
		return this;
	}

}
