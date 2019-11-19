package view;

import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import persistence.Settings;

public class SettingsPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5896291906881110910L;

	private Settings settings = null;

	//600x400->670x500
	public SettingsPanel(Settings settings, int width, int height) {
		super();
		this.settings = settings;
		
		setLayout(null);
		int space = 5;/*
		int width = 600;
		int height = 400;*/
		
		JTabbedPane settingsFull = new JTabbedPane();
		settingsFull.setTabPlacement(JTabbedPane.BOTTOM);
		//settingsFull.setBounds(space, 0, width, height);
		settingsFull.setBounds(space, 0, width-70, height-100);
		//settingsFull.setPreferredSize(new Dimension(width,height));
		settingsFull.setPreferredSize(new Dimension(width-70,height-100));
		
		//String system_name, String system_version_commit_id
		Settings_GitPanel gitPanel = new Settings_GitPanel(this.settings.getRepo_info(), width-70, height-100);
		Settings_NiCadPanel nicadPanel = new Settings_NiCadPanel(this.settings.getNicad_settings(), width-70, height-100);
		gitPanel.lockAllTextFields();
		nicadPanel.lockAllTextFields();
		
		settingsFull.addTab("GIT REPO", gitPanel);
		settingsFull.addTab("NICAD", nicadPanel);
		//settingsFull.setSize(width+50, height+50);
		settingsFull.setSize(width-20, height-50);
		//settingsFull.setPreferredSize(new Dimension(width+50, height+50));
		settingsFull.setPreferredSize(new Dimension(width-20, height-50));
		
		add(settingsFull);
		//setSize(width+70, height+100);
		setSize(width, height);
		//setPreferredSize(new Dimension(width+70, height+100));
		setPreferredSize(new Dimension(width, height));
		System.out.println("PANEL SETTINGS FULL: "+getWidth()+"x"+getHeight());
		setVisible(true);
	}

	public SettingsPanel() {
		// TODO Auto-generated constructor stub
	}

	public SettingsPanel(LayoutManager arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public SettingsPanel(boolean arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public SettingsPanel(LayoutManager arg0, boolean arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
	
	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}
}
