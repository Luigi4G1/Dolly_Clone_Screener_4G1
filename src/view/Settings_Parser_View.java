package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.Settings_Parser_Controller;
import persistence.Settings;

public class Settings_Parser_View extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8750790273354561753L;

	private Settings settings;
	
	private final static int WINDOW_WIDTH = 700;
	private final static int WINDOW_HEIGHT = 600;
	
	private final static int NORTH_PANEL_HEIGHT = 0;
	private final static int SOUTH_PANEL_HEIGHT = 20;

	private final static int BUTTON_WIDTH = 100;
	private final static int BUTTON_HEIGHT = 40;
	
	private JLabel errorDialogLabel;
	private JButton modifySettingsButton;
	private JButton systemParseButton;
	
	public Settings_Parser_View(Settings settings)
	{
		this.settings = settings;
		
		setTitle("SETTINGS - PARSE");
		setResizable(false);
		setBounds(100, 100, WINDOW_WIDTH, WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		
		//SOUTH PANEL -> error label
		errorDialogLabel = new JLabel("Questo e' un messaggio di errore!");
		errorDialogLabel.setBounds(0, 0, WINDOW_WIDTH, SOUTH_PANEL_HEIGHT-4);
		errorDialogLabel.setForeground(Color.red);

		JPanel panelSouth = new JPanel();
		panelSouth.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelSouth.setBounds(0, 0, WINDOW_WIDTH-20, SOUTH_PANEL_HEIGHT);//prev->690
		panelSouth.add(errorDialogLabel);
		getContentPane().add(panelSouth, BorderLayout.SOUTH);
		errorDialogLabel.setVisible(false);//TEST->true
		panelSouth.setVisible(true);
		//SOUTH PANEL -> END#
		
		//CENTER PANEL -> list
		//670x500
		int width = 670;
		int height = 500;
		JPanel panelCenter = new SettingsPanel(this.settings, width, height);
		panelCenter.setBounds(0, 0, WINDOW_WIDTH-20, WINDOW_HEIGHT-SOUTH_PANEL_HEIGHT-NORTH_PANEL_HEIGHT);//620 MIN->700
		panelCenter.setLayout(null);
		getContentPane().add(panelCenter, BorderLayout.CENTER);

		int center_panel_element_height = height;
		
		JLabel modifySettingsButtonLabel = new JLabel("MODIFY");
		modifySettingsButtonLabel.setBounds(WINDOW_WIDTH-52-2*BUTTON_WIDTH-60, center_panel_element_height-2-BUTTON_HEIGHT+7, 60, BUTTON_HEIGHT-14);
		modifySettingsButtonLabel.setForeground(Color.red);
		panelCenter.add(modifySettingsButtonLabel);
		
		modifySettingsButton = new JButton();
		modifySettingsButton.setBounds(WINDOW_WIDTH-52-2*BUTTON_WIDTH, center_panel_element_height-2-BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
		String modify_button_icon_name = "modify_settings_256x256";
		ImageIcon modify_button_imageIcon = null;
		try {
			modify_button_imageIcon = new ImageIcon(getClass().getResource("/icons/" + modify_button_icon_name + ".png"));
			Image image = modify_button_imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(32, 32,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			modify_button_imageIcon = new ImageIcon(newimg);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		if (modify_button_imageIcon != null)
			modifySettingsButton.setIcon(modify_button_imageIcon);
		panelCenter.add(modifySettingsButton);

		///////////////////////////////////////////////////
		modifySettingsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				clickButtonModify();
			}
		});
		///////////////////////////////////////////////////
		
		systemParseButton = new JButton();
		systemParseButton.setBounds(WINDOW_WIDTH-50-BUTTON_WIDTH, center_panel_element_height, BUTTON_WIDTH, BUTTON_HEIGHT);
		String parse_button_icon_name = "parse_button_icon_256x256";
		ImageIcon parse_button_imageIcon = null;
		try {
			parse_button_imageIcon = new ImageIcon(getClass().getResource("/icons/" + parse_button_icon_name + ".png"));
			Image image = parse_button_imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(32, 32,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			parse_button_imageIcon = new ImageIcon(newimg);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		if (parse_button_imageIcon != null)
			systemParseButton.setIcon(parse_button_imageIcon);
		panelCenter.add(systemParseButton);
		///////////////////////////////////////////////////
		systemParseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				clickButtonParse();
			}
		});
		///////////////////////////////////////////////////
		//CENTER PANEL -> END#
		setVisible(true);
	}
	
	public void showMessage(String message) {
		this.errorDialogLabel.setText(message);
		this.errorDialogLabel.setVisible(true);
	}

	public void closeForm() {
		this.dispose();
	}
	
	public void showMessagePopUp(String message, String title)
	{
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void showAlert(String message, String title)
	{
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
	}
	
	public boolean continueOperationAlert(String message, String title)
	{
		int result = JOptionPane.DEFAULT_OPTION;
		while((result!=JOptionPane.YES_OPTION)&&(result!=JOptionPane.NO_OPTION))
			result = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
		
		if(result == JOptionPane.YES_OPTION)
			return true;
		else
			return false;
	}
	
	public void clickButtonModify()
	{
		Settings_Parser_Controller.getInstance().goToSettingsModify(this, settings);
	}
	
	public void clickButtonParse()
	{
		systemParseButton.getModel().setPressed(true);
		systemParseButton.setEnabled(false);
		
		//Settings_Parser_Controller.getInstance().parseSettingsSystem(this, settings);
		Thread splash_thread = new Thread(new Runnable() {
			public void run() {
				try 
				{
					showParsingSplash();

				}catch (Exception e) {
					e.printStackTrace();
					systemParseButton.setEnabled(true);
					systemParseButton.getModel().setPressed(false);
				}
			}
		});
		
		Thread function_thread = new Thread(new Runnable() {
			@SuppressWarnings("static-access")
			public void run() {
				try 
				{
					Thread.currentThread().yield();
					Settings_Parser_Controller.getInstance().parseSettingsSystem(Settings_Parser_View.this, settings);

					closeParsingSplash();
					
				}catch (Exception e) {
					e.printStackTrace();
					systemParseButton.setEnabled(true);
					systemParseButton.getModel().setPressed(false);
				}
				finally
				{
					systemParseButton.setEnabled(true);
					systemParseButton.getModel().setPressed(false);
				}
			}
		});
		splash_thread.start();
		function_thread.start();
	}
	
	public void showParsingSplash()
	{
		splash_frame = new JFrame();
		JDialog d = new JDialog(splash_frame , "", false);
		d.setUndecorated(true);
		//Standard window bounds
		int width = 450;
		int height =115;
		JLabel labelSplashImage = new JLabel();
		
		ImageIcon backgroundImageIcon = null;
		String filename = "alambicchi_rasp.gif";
		try {
			backgroundImageIcon = new ImageIcon(getClass().getResource("/splash_images/"+filename));
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		if(backgroundImageIcon != null)
		{
			labelSplashImage.setIcon(backgroundImageIcon);
			width = backgroundImageIcon.getIconWidth();
			height = backgroundImageIcon.getIconHeight();
			labelSplashImage.setOpaque(true);
			d.getContentPane().add(labelSplashImage);
		}

		//Center the window
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width-width)/2;
		int y = (screen.height-height)/2;
		d.setBounds(x,y,width,height);
		
        d.setSize(width, height);
		d.setPreferredSize(new Dimension(width, height));
		d.setResizable(false);
		d.setAlwaysOnTop(true);
		d.setVisible(true);
	}
	
	public void closeParsingSplash()
	{
		if(splash_frame != null)
			splash_frame.dispose();
	}
	
	private JFrame splash_frame = null;
}
