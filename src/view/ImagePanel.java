package view;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

public class ImagePanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6910646737326608828L;
	
	private Image imageOrg = null;
	private Image image = null;

	{
		addComponentListener(new ComponentAdapter() {
		    public void componentResized(ComponentEvent e) {
		    	int width = ImagePanel.this.getWidth();
		    	int height = ImagePanel.this.getHeight();
		    	System.out.println(width+"x"+height);
		    	if(width>0&&height>0)
		    		image = imageOrg.getScaledInstance(width,height,java.awt.Image.SCALE_SMOOTH);
		    	else
		    		image = imageOrg;
		    }
		});
	}

	public ImagePanel(Image i) {
		imageOrg=i;
		image=i;
		setOpaque(false);
		//setOpaque(true);
	}
	
	public void paint(Graphics g) {
		if (image!=null)
			g.drawImage(image, 0, 0, null);
		
		super.revalidate();
		super.repaint();
		super.paint(g);
	}
}
