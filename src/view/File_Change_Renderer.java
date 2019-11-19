package view;

import java.awt.Component;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import model.File_Change;

public class File_Change_Renderer extends JLabel implements ListCellRenderer<File_Change> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8574596522532350384L;

	public File_Change_Renderer() {
		setOpaque(true);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends File_Change> list, File_Change change, int index,
			boolean isSelected, boolean cellHasFocus) {

		if(change == null)
			return this;
		
		String type = change.changeType();
		String icon_name = "file_change_modify_256x256";
		switch(type)
		{
			case "CREATE":
				icon_name = "file_change_create_256x256";
				break;
			case "DELETE":
				icon_name = "file_change_delete_256x256";
				break;
			case "RENAME":
				icon_name = "file_change_rename_256x256";
				break;
			case "CHANGE":
				icon_name = "file_change_modify_256x256";
				break;
			default:
				icon_name = "file_change_modify_256x256";
				break;
		}
		
		String description = change.shortDescription();
		
		this.setIcon(null);
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
			setIcon(imageIcon);
		setText(description);
		
		if (isSelected)
		{
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		}
		else
		{
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		return this;
	}

}
