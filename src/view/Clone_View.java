package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.Clone_View_Controller;
import model.Clone;
import model.File_Change;
import persistence.System_Analysis;

public class Clone_View extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6393682039716977793L;

	private Clone clone = null;
	private System_Analysis analysis_result = null;
	
	private final static int NORTH_PANEL_HEIGHT = 0;
	private final static int SOUTH_PANEL_HEIGHT = 20;
	
	private final static int MENUBAR_HEIGHT = 32;
	
	private final static int SPACE = 10;
	private final static int LABEL_WIDTH = 90;
	private final static int LABEL_HEIGHT = 16;
	private final static int BUTTON_HEIGHT = 40;
	private final static int IMAGE_HEIGHT = 64;
	
	private JLabel errorDialogLabel;
	private JLabel findButtonLabel;
	
	public Clone_View(Clone clone, System_Analysis analysis_result)
	{
		this.clone = clone;
		this.analysis_result = analysis_result;
		
		setTitle("CLONE VIEWER");
		setResizable(false);
		//setBounds(10, 10, WINDOW_WIDTH, WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		
		//SOUTH PANEL -> error label
		errorDialogLabel = new JLabel("Questo e' un messaggio di errore!");
		errorDialogLabel.setBounds(0, 0, 325, 16);
		errorDialogLabel.setForeground(Color.red);
		
		super.setJMenuBar(new MenuBarDollyScreener(this, this.analysis_result));

		JPanel panelSouth = new JPanel();
		panelSouth.setLayout(new FlowLayout(FlowLayout.CENTER));
		//panelSouth.setBounds(0, 0, WINDOW_WIDTH, SOUTH_PANEL_HEIGHT);
		panelSouth.add(errorDialogLabel);
		getContentPane().add(panelSouth, BorderLayout.SOUTH);
		errorDialogLabel.setVisible(false);
		panelSouth.setVisible(true);
		//SOUTH PANEL -> END#
		
		//CENTER PANEL -> list
		ClonePanel panelCenter = new ClonePanel(this.clone);
		panelCenter.setLayout(null);
		getContentPane().add(panelCenter, BorderLayout.CENTER);
		
		int center_panel_height = panelCenter.getHeight();
		int center_panel_width = panelCenter.getWidth();
		
		if(this.clone.getAuthor() == null)
		{
			//JButton findAuthorButton = new JButton();
			findAuthorButton = new JButton();
			findAuthorButton.setBounds(center_panel_width-IMAGE_HEIGHT-50+5, center_panel_height-70, IMAGE_HEIGHT, IMAGE_HEIGHT);
			panelCenter.add(findAuthorButton);
			
			findAuthorButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					clickButtonFindAuthor();
				}
			});
			String search_image_name = "search_author_256x256";
			//ImageIcon search_imageIcon = null;
			search_imageIcon = null;
			try {
				search_imageIcon = new ImageIcon(getClass().getResource("/icons/" + search_image_name + ".png"));
				Image image = search_imageIcon.getImage(); // transform it 
				Image newimg = image.getScaledInstance(IMAGE_HEIGHT, IMAGE_HEIGHT,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
				search_imageIcon = new ImageIcon(newimg);
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			if (search_imageIcon != null)
				findAuthorButton.setIcon(search_imageIcon);
			
			String loading_search_image_name = "fingerprint_loading_256x256";
			//ImageIcon loading_search_imageIcon = null;
			loading_search_imageIcon = null;
			search_imageIcon = null;
			try {
				loading_search_imageIcon = new ImageIcon(getClass().getResource("/icons/" + loading_search_image_name + ".png"));
				Image image = loading_search_imageIcon.getImage(); // transform it 
				Image newimg = image.getScaledInstance(IMAGE_HEIGHT, IMAGE_HEIGHT,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
				loading_search_imageIcon = new ImageIcon(newimg);
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			if (loading_search_imageIcon != null)
				findAuthorButton.setPressedIcon(loading_search_imageIcon);

			findButtonLabel = new JLabel();
			findButtonLabel.setBounds(center_panel_width-IMAGE_HEIGHT-50+5-LABEL_WIDTH, center_panel_height-70+(IMAGE_HEIGHT-LABEL_HEIGHT)/2, LABEL_WIDTH, LABEL_HEIGHT);
			findButtonLabel.setPreferredSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
			panelCenter.add(findButtonLabel);
			//COLOR TEXT HTML WAY
			//findButtonLabel.setText("<html><font color='red'>FIND AUTHOR</font></html>");
			findButtonLabel.setText("FIND AUTHOR");
			findButtonLabel.setForeground(Color.RED);
			/*
			 * IF DON'T USE THE SPLASH PAGE
			 * the BUTTON (findAuthorButton) remains PRESSED until the METHOD invoked ends.
			 * SO, the BUTTON PRESSED/ARMED STATE CAN BE USED.
			findAuthorButton.getModel().addChangeListener(new ChangeListener(){
				public void stateChanged(ChangeEvent e)
				{
					if(findAuthorButton.getModel().isPressed())
					{
						if(findButtonLabel != null)
						{
							findButtonLabel.setText("SEARCHING...");
							findButtonLabel.setForeground(Color.GRAY);
						}
					}
					else
					{
						if(findButtonLabel != null)
						{
							findButtonLabel.setText("FIND AUTHOR");
							findButtonLabel.setForeground(Color.RED);
						}
					}
				}
			});
			*/
		}
		
		if(this.clone.getChange_modify_clone() != null)
		{
			JButton showDiffButton = new JButton("SHOW DIFF");
			showDiffButton.setBounds(SPACE, center_panel_height-70, LABEL_WIDTH*2, BUTTON_HEIGHT);
			panelCenter.add(showDiffButton);
			
			showDiffButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					showInfoDiff(Clone_View.this.clone.getChange_modify_clone());
				}
			});
		}

		center_panel_height += BUTTON_HEIGHT+SPACE;
		//CENTER PANEL -> END#
		panelCenter.setBounds(0, 0, center_panel_width, center_panel_height);
		panelSouth.setBounds(0, 0, center_panel_width, SOUTH_PANEL_HEIGHT);
		setBounds(SPACE, SPACE, center_panel_width, center_panel_height+SOUTH_PANEL_HEIGHT+NORTH_PANEL_HEIGHT);
		setSize(center_panel_width, center_panel_height+SOUTH_PANEL_HEIGHT+NORTH_PANEL_HEIGHT+MENUBAR_HEIGHT);

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
	
	public void showInfoDiff(File_Change change)
	{
		if (this.errorDialogLabel.isVisible())
			this.errorDialogLabel.setVisible(false);
		if (change == null)
			this.showAlert("ERROR! NULL VALUE FOUND.\nUNABLE TO SHOW DATA!","NO DATA");
		else
		{
			//COMMITPANEL inside Dialog FRAME
			JFrame f= new JFrame();
			JDialog d = new JDialog(f , "Diff Info", true);
			d.getContentPane().setLayout( new FlowLayout(FlowLayout.LEFT) );
	        d.getContentPane().add(Box.createVerticalGlue());
	        //CREATE ClonePanel
	        FileChangePanel panPane = new FileChangePanel(change);
	        d.getContentPane().add(panPane);

	        d.setSize(panPane.getWidth(), panPane.getHeight());
			d.setPreferredSize(new Dimension(panPane.getWidth(), panPane.getHeight()));
			d.setResizable(false);
	        d.setVisible(true);
		}
	}
	
	public void showFindAuthorImageButton()
	{
		if(findAuthorButton != null)
			if(search_imageIcon != null)
				findAuthorButton.setIcon(search_imageIcon);

		if(findButtonLabel != null)
		{
			findButtonLabel.setText("FIND AUTHOR");
			findButtonLabel.setForeground(Color.RED);
		}
	}
	
	public void showSearchingImageButton()
	{
		if(findAuthorButton != null)
			if(loading_search_imageIcon != null)
				findAuthorButton.setIcon(loading_search_imageIcon);

		if(findButtonLabel != null)
		{
			findButtonLabel.setText("SEARCHING...");
			findButtonLabel.setForeground(Color.GRAY);
		}
	}

	public void clickButtonFindAuthor()
	{
		findAuthorButton.getModel().setPressed(true);
		findAuthorButton.setEnabled(false);
		showSearchingImageButton();
		//Clone_View_Controller.getInstance().findCloneAuthor(this, this.clone, this.analysis_result);
		Thread splash_thread = new Thread(new Runnable() {
			public void run() {
				try 
				{
					showSearchingSplash();

				}catch (Exception e) {
					e.printStackTrace();
					showFindAuthorImageButton();
					findAuthorButton.setEnabled(true);
					findAuthorButton.getModel().setPressed(false);
				}
			}
		});
		
		Thread function_thread = new Thread(new Runnable() {
			@SuppressWarnings("static-access")
			public void run() {
				try 
				{
					Thread.currentThread().yield();
					Clone_View_Controller.getInstance().findCloneAuthor(Clone_View.this, clone, analysis_result);
					//showFindAuthorImageButton();
					closeSearchingSplash();
					
				}catch (Exception e) {
					e.printStackTrace();
					showFindAuthorImageButton();
					findAuthorButton.setEnabled(true);
					findAuthorButton.getModel().setPressed(false);
				}
				finally
				{
					findAuthorButton.setEnabled(true);
					findAuthorButton.getModel().setPressed(false);
					showFindAuthorImageButton();
				}
			}
		});
		splash_thread.start();
		function_thread.start();
	}
	
	public void showSearchingSplash()
	{
		splash_frame = new JFrame();
		JDialog d = new JDialog(splash_frame , "", false);
		d.setUndecorated(true);
		//Standard window bounds
		int width = 450;
		int height =115;
		JLabel labelSplashImage = new JLabel();
		
		ImageIcon backgroundImageIcon = null;
		String filename = "fingerprint_loading_rotating_speedx4_256x256.gif";
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
	
	public void closeSearchingSplash()
	{
		if(splash_frame != null)
			splash_frame.dispose();
	}
	
	private JFrame splash_frame = null;
	private JButton findAuthorButton = null;
	private ImageIcon loading_search_imageIcon = null;
	private ImageIcon search_imageIcon = null;
}
