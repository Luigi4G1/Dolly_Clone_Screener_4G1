package view;

import java.awt.Component;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import persistence.System_Version;

public class System_Version_JComboBoxRenderer extends JLabel implements ListCellRenderer<System_Version>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 465717400721612013L;

	@Override
	public Component getListCellRendererComponent(JList<? extends System_Version> list, System_Version value, int index, boolean isSelected, boolean hasFocus) {
		
		if(value == null)
			return this;
		
		String icon_name = "system_analyzed_icon_256x256";
		if(!value.isSaved())
			icon_name = "system_to_parse_256x256";
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
		setText(value.getSystem_name()+" | "+value.getSystem_version());
		return this;
	}
}
