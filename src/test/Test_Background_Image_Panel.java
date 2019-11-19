package test;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import view.ImagePanel;

public class Test_Background_Image_Panel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try 
				{
					String background_image_name = "floppy_disk_256x256";
					///////////////////////////////////////////////
					Image backgroundImage = null;
					try {
						ImageIcon backgroundImageIcon = new ImageIcon(getClass().getResource("/icons/" + background_image_name + ".png"));
						backgroundImage = backgroundImageIcon.getImage();
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
							JFrame f = new JFrame(""); 
							JPanel j = new ImagePanel(backgroundImage); 
							j.setLayout(new FlowLayout()); 
							j.add(new JButton("YoYo")); 
							j.add(new JButton("MaMa")); 
							f.add(j); 
							f.setVisible(true); 
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

}
