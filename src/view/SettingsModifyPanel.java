package view;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import persistence.NiCad_Settings;
import persistence.Settings;
import utils.GitUtils4G1;

public class SettingsModifyPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4673630397340535278L;

	private Settings settings = null;
	private Settings_GitPanel gitPanel;
	private Settings_NiCadPanel nicadPanel;

	//600x400->670x500
	public SettingsModifyPanel(Settings settings, int width, int height) {
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
		gitPanel = new Settings_GitPanel(this.settings.getRepo_info(), width-70, height-100);
		nicadPanel = new Settings_NiCadPanel(this.settings.getNicad_settings(), width-70, height-100);
		gitPanel.unlockAllModifiableTextFields();
		nicadPanel.unlockAllModifiableTextFields();
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

	public Settings getModifiedSettings()
	{
		Settings toReturn = null;
		GitUtils4G1 new_repo_info = this.gitPanel.getModifiedGitUtils4G1();
		NiCad_Settings new_nicad_settings = this.nicadPanel.getModifiedNiCad_Settings();
		if((new_repo_info != null)&&(new_nicad_settings != null))
		{
			toReturn = new Settings(new_repo_info.getSystem_name(), new_nicad_settings.getVersion_commit(), new_repo_info, new_nicad_settings);
		}
		return toReturn;
	}
	
	public File getCloneClassesFile()
	{
		return this.nicadPanel.getCloneClassesFile();
	}
	
	public File getCloneLinesFile()
	{
		return this.nicadPanel.getCloneLinesFile();
	}
	
	public File getClonePairsFile()
	{
		return this.nicadPanel.getClonePairsFile();
	}
	
	public void setCloneClassesFile(File cloneClassesFile)
	{
		this.nicadPanel.setCloneClassesFile(cloneClassesFile);
	}
	
	public void setCloneLinesFile(File cloneLinesFile)
	{
		this.nicadPanel.setCloneLinesFile(cloneLinesFile);
	}
	
	public void setClonePairsFile(File clonePairsFile)
	{
		this.nicadPanel.setClonePairsFile(clonePairsFile);
	}
	
	public void highlightRepoURITextField(boolean lightOn)
	{
		this.gitPanel.highlightRepoURITextField(lightOn);
	}

	public SettingsModifyPanel() {
		// TODO Auto-generated constructor stub
	}

	public SettingsModifyPanel(LayoutManager arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public SettingsModifyPanel(boolean arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public SettingsModifyPanel(LayoutManager arg0, boolean arg1) {
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
