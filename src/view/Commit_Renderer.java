package view;

import java.awt.Component;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import model.Commit_Node;

/** 
 * Custom renderer to display a the profile of a COMMIT with an icon alongside its description
 *
 * @author Luigi -4G1- Forgione
 */ 
public class Commit_Renderer extends JLabel implements ListCellRenderer<Commit_Node>{

	private static final long serialVersionUID = 1L;

	public Commit_Renderer() {
		setOpaque(true);
	}
	
	@Override
	public Component getListCellRendererComponent(JList<? extends Commit_Node> list, Commit_Node commit, int index,
			boolean isSelected, boolean cellHasFocus) {
		
		if(commit == null)
			return this;
		
		String description = commit.shortDescription();
		
		this.setIcon(null);
		ImageIcon imageIcon = null;
		try {
			imageIcon = new ImageIcon(getClass().getResource("/icons/" + "commit_icon_256x256" + ".png"));
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
