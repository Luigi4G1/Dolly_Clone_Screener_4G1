package view;

import java.awt.Component;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import model.Clone;
/** 
 * Custom renderer to display a the profile of a CLONE with an icon alongside its description
 *
 * @author Luigi -4G1- Forgione
 */ 
public class Clone_Renderer extends JLabel implements ListCellRenderer<Clone> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 34976439347031657L;

	public Clone_Renderer() {
		setOpaque(true);
	}
	
	@Override
	public Component getListCellRendererComponent(JList<? extends Clone> list, Clone clone, int index, 
			boolean isSelected, boolean cellHasFocus)
	{
		if(clone == null)
			return this;
		
		String description = clone.shortDescription();
		this.setIcon(null);
		ImageIcon imageIcon = null;
		try {
			imageIcon = new ImageIcon(getClass().getResource("/icons/" + "clone_sheep_256x256" + ".png"));
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
