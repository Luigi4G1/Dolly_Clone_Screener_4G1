package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

public class Splash_Screen extends JWindow{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6693423808208153917L;

	public Splash_Screen(int duration_milliseconds, String filename) {
		this.duration_milliseconds = duration_milliseconds;
		this.filename = filename;
	}

	public void showSplash()
	{
		JPanel content = (JPanel)getContentPane();
		content.setBackground(Color.white);
		//Standard window bounds
		int width = 450;
		int height =115;

		//Build the splash screen image
		//JLabel labelSplashImage = new JLabel(new ImageIcon("filename.gif"));
		labelSplashImage = new JLabel();

		ImageIcon backgroundImageIcon = null;
		try {
			backgroundImageIcon = new ImageIcon(getClass().getResource("/splash_images/" + this.filename));
			//Image image = backgroundImageIcon.getImage(); // transform it 
			//Image newimg = image.getScaledInstance(32, 32,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			//backgroundImageIcon = new ImageIcon(newimg);
			//FOR RESIZING PURPOSE -> PROBLEM WITH GIF
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		if(backgroundImageIcon != null)
		{
			labelSplashImage.setIcon(backgroundImageIcon);
			width = backgroundImageIcon.getIconWidth();
			height = backgroundImageIcon.getIconHeight();
			labelSplashImage.setOpaque(true);
			content.add(labelSplashImage, BorderLayout.CENTER);
		}

		if(copyrightLabel == null)
		{
			copyrightLabel = new JLabel("\u00a9 Copyright \u00a9 Luigi -4G1- Forgione 2019 \u00a9", JLabel.CENTER);
			content.add(copyrightLabel, BorderLayout.SOUTH);
		}
		copyrightLabel.setFont(new Font("Sans-Serif", Font.BOLD, 14));
		copyrightLabel.setOpaque(true);
		copyrightLabel.setBackground(Color.BLACK);
		copyrightLabel.setForeground(Color.YELLOW);

		height += copyrightLabel.getHeight();
		if(width < copyrightLabel.getWidth())
			width = copyrightLabel.getWidth();

		Color oraRed = new Color(156, 20, 20,  255);
		int thickness = 10;
		content.setBorder(BorderFactory.createLineBorder(oraRed, thickness));
		width += (thickness*2);
		height += (thickness*2);

		//Center the window
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width-width)/2;
		int y = (screen.height-height)/2;

		setBounds(x,y,width,height);
		//Display it
		setVisible(true);
		try
		{
			Thread.sleep(duration_milliseconds);
		}catch(Exception e){}
		
		setVisible(false);
		dispose();
	}
	
	public void showSplashMessage(String message)
	{
		JPanel content = (JPanel)getContentPane();
		content.setBackground(Color.white);
		//Standard window bounds
		int width = 450;
		int height =115;

		//Build the splash screen image
		//JLabel labelSplashImage = new JLabel(new ImageIcon("filename.gif"));
		labelSplashImage = new JLabel();

		ImageIcon backgroundImageIcon = null;
		try {
			backgroundImageIcon = new ImageIcon(getClass().getResource("/splash_images/" + this.filename));
			//Image image = backgroundImageIcon.getImage(); // transform it 
			//Image newimg = image.getScaledInstance(32, 32,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			//backgroundImageIcon = new ImageIcon(newimg);
			//FOR RESIZING PURPOSE -> PROBLEM WITH GIF
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		if(backgroundImageIcon != null)
		{
			labelSplashImage.setIcon(backgroundImageIcon);
			width = backgroundImageIcon.getIconWidth();
			height = backgroundImageIcon.getIconHeight();
			labelSplashImage.setOpaque(true);
			content.add(labelSplashImage, BorderLayout.CENTER);
		}

		if(copyrightLabel == null)
		{
			copyrightLabel = new JLabel("", JLabel.CENTER);
			content.add(copyrightLabel, BorderLayout.SOUTH);
		}
		copyrightLabel.setFont(new Font("Sans-Serif", Font.BOLD, 14));
		copyrightLabel.setOpaque(true);
		copyrightLabel.setBackground(Color.BLACK);
		copyrightLabel.setForeground(Color.YELLOW);
		copyrightLabel.setText(message);

		height += copyrightLabel.getHeight();
		if(width < copyrightLabel.getWidth())
			width = copyrightLabel.getWidth();

		Color oraRed = new Color(156, 20, 20,  255);
		int thickness = 10;
		content.setBorder(BorderFactory.createLineBorder(oraRed, thickness));
		width += (thickness*2);
		height += (thickness*2);

		//Center the window
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width-width)/2;
		int y = (screen.height-height)/2;

		setBounds(x,y,width,height);
		//Display it
		setVisible(true);
		try
		{
			Thread.sleep(duration_milliseconds);
		}catch(Exception e){}
		
		setVisible(false);
		dispose();
	}

	public void showSplashTitle(String filenameTitle)
	{
		JPanel content = (JPanel)getContentPane();
		content.setBackground(Color.white);
		//Standard window bounds
		int width = 450;
		int height =115;

		//Build the splash screen image
		//JLabel labelSplashImage = new JLabel(new ImageIcon("filename.gif"));
		labelSplashImage = new JLabel();

		ImageIcon backgroundImageIcon = null;
		try {
			backgroundImageIcon = new ImageIcon(getClass().getResource("/splash_images/" + this.filename));
			//Image image = backgroundImageIcon.getImage(); // transform it 
			//Image newimg = image.getScaledInstance(32, 32,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			//backgroundImageIcon = new ImageIcon(newimg);
			//FOR RESIZING PURPOSE -> PROBLEM WITH GIF
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		if(backgroundImageIcon != null)
		{
			labelSplashImage.setIcon(backgroundImageIcon);
			width = backgroundImageIcon.getIconWidth();
			height = backgroundImageIcon.getIconHeight();
			labelSplashImage.setOpaque(true);
			content.add(labelSplashImage, BorderLayout.CENTER);
		}
		
		titleLabel = new JLabel();

		ImageIcon titleImageIcon = null;
		try {
			titleImageIcon = new ImageIcon(getClass().getResource("/splash_images/" + filenameTitle));
			//Image image = backgroundImageIcon.getImage(); // transform it 
			//Image newimg = image.getScaledInstance(32, 32,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			//backgroundImageIcon = new ImageIcon(newimg);
			//FOR RESIZING PURPOSE -> PROBLEM WITH GIF
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		if(titleImageIcon != null)
		{
			titleLabel.setIcon(titleImageIcon);
			
			if(width < titleLabel.getWidth())
				width = titleLabel.getWidth();

			height += titleImageIcon.getIconHeight();
			titleLabel.setOpaque(true);
			content.add(titleLabel, BorderLayout.NORTH);
		}
		/////////////////////////////////////////////////////////
		Color oraRed = new Color(156, 20, 20,  255);
		int thickness = 10;
		content.setBorder(BorderFactory.createLineBorder(oraRed, thickness));
		width += (thickness*2);
		height += (thickness*2);

		//Center the window
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width-width)/2;
		int y = (screen.height-height)/2;

		setBounds(x,y,width,height);
		//Display it
		setVisible(true);
		try
		{
			Thread.sleep(duration_milliseconds);
		}catch(Exception e){}
		
		setVisible(false);
		dispose();
	}
	
	public static void main(String[] args)
	{
		String splashImageFilename = "4G1_logo_rotating_bmf_320x240.gif";
		//String splashImageFilename1 = "alambicchi_480x360.gif";
		//String splashImageFilename2 = "alambicchi_rasp.gif";
		//String splashImageFilename3 = "fingerprint_loading_rotating_64x64.gif";
		// Throw a nice little title page up on the screen first
		Splash_Screen splash = new Splash_Screen(5000,splashImageFilename);
		// Normally, call splash.showSplash() and get on with the program.
		splash.showSplash();
		
		splash.showSplashMessage("loading...");
		
		String splashImageTitleFilename = "Dolly_Logo_Pasadena_495x100.gif";
		//String splashImageTitleFilename = "ezgif.com-gif-maker.gif";
		String splashImageCenterFilename = "cloned_sheeps_495x330.gif";
		Splash_Screen splash_title = new Splash_Screen(10000,splashImageCenterFilename);
		splash_title.showSplashTitle(splashImageTitleFilename);
		
		Load_Last_Saved_System_View frame = new Load_Last_Saved_System_View();
		frame.setVisible(true);
    }

	//milliseconds
    private int duration_milliseconds;
    private String filename;
    
    private JLabel titleLabel = null;
    private JLabel labelSplashImage = null;
    private JLabel copyrightLabel = null;
}
