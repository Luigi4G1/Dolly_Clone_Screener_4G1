package view;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import controller.LoaderTemplateMethod;
import persistence.System_Analysis;

public class MenuBarDollyScreener extends JMenuBar{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7621965187314244770L;
	
	private JFrame window;
	private System_Analysis analysis_result = null;
	
	public MenuBarDollyScreener(JFrame window, System_Analysis analysis_result)
	{
		this.window = window;
		this.analysis_result = analysis_result;
		/**************************************************************/
		JMenu menuFile = new JMenu("File");
		menuFile.setMnemonic(KeyEvent.VK_F);

		JMenuItem itemSystemVersion = new JMenuItem("System Version");
		itemSystemVersion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickButtonSettings();
			}
		});
		itemSystemVersion.setMnemonic(KeyEvent.VK_V);
		KeyStroke keyStrokeToSystemVersion = KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.ALT_DOWN_MASK);
		itemSystemVersion.setAccelerator(keyStrokeToSystemVersion);
		menuFile.add(itemSystemVersion);
		
		JMenuItem itemDownloadSystemVersion = new JMenuItem("Download System Version");
		itemDownloadSystemVersion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickButtonDownload();
			}
		});
		itemDownloadSystemVersion.setMnemonic(KeyEvent.VK_D);
		KeyStroke keyStrokeToDownloadSystemVersion = KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.ALT_DOWN_MASK);
		itemDownloadSystemVersion.setAccelerator(keyStrokeToDownloadSystemVersion);
		menuFile.add(itemDownloadSystemVersion);
		
		JMenuItem itemLoadParse = new JMenuItem("Load/Parse");
		itemLoadParse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickButtonLoadParse();
			}
		});
		itemLoadParse.setMnemonic(KeyEvent.VK_L);
		KeyStroke keyStrokeToLoadParse = KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.ALT_DOWN_MASK);
		itemLoadParse.setAccelerator(keyStrokeToLoadParse);
		menuFile.add(itemLoadParse);

		JMenuItem itemSave = new JMenuItem("Save");
		itemSave.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		itemSave.setHorizontalTextPosition(JMenuItem.CENTER);
		itemSave.setMargin(new Insets(0,0,0,0));
		itemSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickButtonSave();
			}
		});
		itemSave.setMnemonic(KeyEvent.VK_S);
		KeyStroke keyStrokeToSave = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.ALT_DOWN_MASK);
		itemSave.setAccelerator(keyStrokeToSave);
		menuFile.add(itemSave);
		//super.add(itemSettings);
		super.add(menuFile);
		/**************************************************************/
		
		JMenu menuReports = new JMenu("Reports");
		menuReports.setMnemonic(KeyEvent.VK_R);

		JMenuItem itemCommitHistory = new JMenuItem("Commit History");
		itemCommitHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickButtonCommits();
			}
		});
		itemCommitHistory.setMnemonic(KeyEvent.VK_H);
		KeyStroke keyStrokeToCommitHistory = KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.ALT_DOWN_MASK);
		itemCommitHistory.setAccelerator(keyStrokeToCommitHistory);
		
		JMenuItem itemClonesReport = new JMenuItem("Clones - NiCad Report");
		itemClonesReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickButtonClones();
			}
		});
		itemClonesReport.setMnemonic(KeyEvent.VK_C);
		KeyStroke keyStrokeToClonesReport = KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.ALT_DOWN_MASK);
		itemClonesReport.setAccelerator(keyStrokeToClonesReport);
		
		JMenuItem itemAuthors = new JMenuItem("Authors");
		itemAuthors.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickButtonAuthors();
			}
		});
		itemAuthors.setMnemonic(KeyEvent.VK_A);
		KeyStroke keyStrokeToAuthors = KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.ALT_DOWN_MASK);
		itemAuthors.setAccelerator(keyStrokeToAuthors);
		
		menuReports.add(itemCommitHistory);
		menuReports.add(itemClonesReport);
		menuReports.add(itemAuthors);
		super.add(menuReports);
		/**************************************************************/
		JMenuItem info = new JMenuItem("Info");
		info.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickButtonInfo();
			}
		});
		ImageIcon imageIcon = null;
		try {
			imageIcon = new ImageIcon(getClass().getResource("/icons/" + "info_32x32" + ".png"));
			//Image image = imageIcon.getImage(); // transform it 
			//Image newimg = image.getScaledInstance(32, 32,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			//imageIcon = new ImageIcon(newimg);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		if (imageIcon != null)
			info.setIcon(imageIcon);
		else
			System.out.println("NO IMAGE FOUND!");
		info.setBounds(0, 0, 64, 32);
		info.setPreferredSize(new Dimension(64,32));
		info.setMargin(new Insets(0,0,0,0));
		info.setBorder(null);
		info.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		super.add(Box.createHorizontalGlue());
		super.add(Box.createHorizontalGlue());
		super.add(Box.createHorizontalGlue());
		super.add(Box.createHorizontalGlue());
		super.add(Box.createHorizontalGlue());
		super.add(Box.createHorizontalGlue());
		super.add(Box.createHorizontalGlue());
		super.add(Box.createHorizontalGlue());
		super.add(Box.createHorizontalGlue());
		super.add(Box.createHorizontalGlue());
		super.add(Box.createHorizontalGlue());
		super.add(Box.createHorizontalGlue());
		super.add(info);
	}
	
	public void clickButtonCommits()
	{
		if((analysis_result == null)||(analysis_result.getCommit_history_analysis() == null)||(analysis_result.getCommit_history_analysis().getCommit_nodes() == null))
		{
			showAlert("NO COMMIT DATA AVAILABLE", "ATTENTION");
		}
		else
		{
			this.window.dispose();
			new Commit_List_Report_View(analysis_result);
		}
	}
	
	public void clickButtonClones()
	{
		if((analysis_result.getReport_clone_analysis() == null)||(analysis_result.getReport_clone_analysis().getClones() == null))
		{
			showAlert("NO CLONE DATA AVAILABLE", "ATTENTION");
		}
		else
		{
			this.window.dispose();
			new Clone_List_View(analysis_result);
		}
	}
	
	public void clickButtonAuthors()
	{
		if((analysis_result == null)||(analysis_result.getCommit_history_analysis() == null)||(analysis_result.getCommit_history_analysis().getCommit_authors() == null))
		{
			showAlert("NO AUTHOR DATA AVAILABLE", "ATTENTION");
		}
		else
		{
			this.window.dispose();
			new Author_List_View(analysis_result);
		}
	}
	
	public void clickButtonSettings()
	{
		if(this.analysis_result.getSettings() == null)
		{
			showAlert("NO SETTINGS DATA AVAILABLE", "ATTENTION");
		}
		else
		{
			//COMMITPANEL inside Dialog FRAME
			JFrame f= new JFrame();
			JDialog d = new JDialog(f , "Settings", true);
			d.getContentPane().setLayout( new FlowLayout(FlowLayout.LEFT) );
	        d.getContentPane().add(Box.createVerticalGlue());
	        //CREATE SettingsPanel
	        int width = 670;
			int height = 500;
	        SettingsPanel panPane = new SettingsPanel(this.analysis_result.getSettings(), width, height);
	        d.getContentPane().add(panPane);

	        d.setSize(panPane.getWidth(), panPane.getHeight());
			d.setPreferredSize(new Dimension(panPane.getWidth(), panPane.getHeight()));
			d.setResizable(false);
	        d.setVisible(true);
		}
	}
	
	public void clickButtonInfo()
	{
		String message = "This tool was developed as part of the\r\n" + 
				"2019 thesis work by Luigi Forgione\r\n" + 
				"at the University of Sannio, Benevento, Italy.\r\n" + 
				"It is intended to show a possible method\r\n" + 
				"for linking a software clone,\r\n" + 
				"found in a open source system,\r\n" + 
				"to its introducer (developper, programmer, maintainer).\r\n" + 
				"\r\n" + 
				"For more info,\r\n" + 
				"please take a look at the readme.txt file\r\n" + 
				"or the relative documentation.\r\n" + 
				"\r\n" + 
				"All credits to Luigi -4G1- Forgione";
		JOptionPane.showMessageDialog(null, message, null, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void clickButtonDownload()
	{
		this.window.dispose();
		new Download_Sys_Version_View();
	}
	
	public void clickButtonLoadParse()
	{
		this.window.dispose();
		new Load_System_Selection_View();
	}
	
	public void clickButtonSave()
	{
		String message = "";
		String title ="";
		int messageType = JOptionPane.INFORMATION_MESSAGE;
		ImageIcon message_imageIcon = null;
		String message_icon_name = null;
		boolean success = LoaderTemplateMethod.saveAll(this.analysis_result.getSettings(), this.analysis_result.getCommit_history_analysis(), this.analysis_result.getReport_clone_analysis());
		
		if(success)
		{
			message_icon_name = "padlock_256x256";
			message = "SYSTEM ANALYSIS CORRECTLY SAVED!";
			title ="ANALYSIS SAVED!";
		}
		else
		{
			message_icon_name = "stop_x_256x256";
			message = "UNABLE TO SAVE SYSTEM ANALYSIS!";
			title ="ANALYSIS NOT SAVED!";
		}
		
		try {
			message_imageIcon = new ImageIcon(getClass().getResource("/icons/" + message_icon_name + ".png"));
			Image image = message_imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(32, 32,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			message_imageIcon = new ImageIcon(newimg);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		if(message_imageIcon != null)
			JOptionPane.showMessageDialog(null, message, title, messageType, message_imageIcon);
		else
			JOptionPane.showMessageDialog(null, message, title, messageType);
	}
	
	public void showAlert(String message, String title)
	{
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
	}

}
