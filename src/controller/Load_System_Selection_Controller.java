package controller;

import java.io.File;
import java.util.ArrayList;

import persistence.Settings;
import persistence.System_Analysis;
import persistence.System_Version;
import view.Home_System_Analysis_View;
import view.Load_System_Selection_View;
import view.Settings_Parser_View;

public class Load_System_Selection_Controller {

	private static Load_System_Selection_Controller instance = null;
	
	private ArrayList<System_Version> saved_system_versions = new ArrayList<System_Version>();
	private ArrayList<System_Version> parsing_system_versions = new ArrayList<System_Version>();
	
	private Load_System_Selection_Controller()
	{
		saved_system_versions = new ArrayList<System_Version>();
		parsing_system_versions = new ArrayList<System_Version>();
	}
	
	public static Load_System_Selection_Controller getInstance()
	{
		if(instance == null)
			instance = new Load_System_Selection_Controller();
		return instance;
	}

	public ArrayList<System_Version> getSaved_system_versions() {
		return saved_system_versions;
	}

	public void setSaved_system_versions(ArrayList<System_Version> saved_system_versions) {
		this.saved_system_versions = saved_system_versions;
	}

	public ArrayList<System_Version> getParsing_system_versions() {
		return parsing_system_versions;
	}

	public void setParsing_system_versions(ArrayList<System_Version> parsing_system_versions) {
		this.parsing_system_versions = parsing_system_versions;
	}

	public Object[][] savedSystemsRowsForTable2Col()
	{
		ArrayList<System_Version> saved_system_versions = new ArrayList<System_Version>();
		File saves_dir = new File(Settings.SAVED_SYSTEMS_FOLDER);
		if(saves_dir.isDirectory())
		{
			File[] save_dir_files = saves_dir.listFiles();
			for(File save_dir_file:save_dir_files)
			{
				if(save_dir_file.isDirectory())
				{
					String system_name = save_dir_file.getName();
					File[] system_name_dir_files = save_dir_file.listFiles();
					for(File system_name_dir_file:system_name_dir_files)
					{
						if(system_name_dir_file.isDirectory())
						{
							String version_name = system_name_dir_file.getName();
							saved_system_versions.add(new System_Version(system_name, version_name, system_name_dir_file,true));
						}
					}
				}
			}
		}
		Object[][] matrix = new Object[saved_system_versions.size()][2];
		for(int i=0; i<saved_system_versions.size(); i++)
		{
			System_Version row = saved_system_versions.get(i);
			matrix[i][0] = row.getSystem_name();
			matrix[i][1] = row.getSystem_version();
		}
		this.setSaved_system_versions(saved_system_versions);
		return matrix;
	}

	public Object[][] parsingSystemsRowsForTable3Col()
	{
		ArrayList<System_Version> parsing_system_versions = new ArrayList<System_Version>();
		File saves_dir = new File(Settings.PARSING_SYSTEMS_FOLDER);
		if(saves_dir.isDirectory())
		{
			File[] save_dir_files = saves_dir.listFiles();
			for(File save_dir_file:save_dir_files)
			{
				if(save_dir_file.isDirectory())
				{
					String system_name = save_dir_file.getName();
					File[] system_name_dir_files = save_dir_file.listFiles();
					for(File system_name_dir_file:system_name_dir_files)
					{
						if(system_name_dir_file.isDirectory())
						{
							String version_name = system_name_dir_file.getName();
							boolean saved = false;
							for(System_Version saved_version : this.saved_system_versions)
							{
								if((saved_version.getSystem_name().equalsIgnoreCase(system_name))&&(saved_version.getSystem_version().equalsIgnoreCase(version_name)))
								{
									saved = true;
									break;
								}
							}
							parsing_system_versions.add(new System_Version(system_name, version_name, system_name_dir_file, saved));
						}		
					}
				}
			}
		}
		Object[][] matrix = new Object[parsing_system_versions.size()][3];
		for(int i=0; i<parsing_system_versions.size(); i++)
		{
			System_Version row = parsing_system_versions.get(i);
			matrix[i][0] = row;
			matrix[i][1] = row;
			matrix[i][2] = row;
		}
		this.setParsing_system_versions(parsing_system_versions);
		return matrix;
	}
	
	public void searchSavedSystems()
	{
		ArrayList<System_Version> saved_system_versions = new ArrayList<System_Version>();
		File saves_dir = new File(Settings.SAVED_SYSTEMS_FOLDER);
		if(saves_dir.isDirectory())
		{
			File[] save_dir_files = saves_dir.listFiles();
			for(File save_dir_file:save_dir_files)
			{
				if(save_dir_file.isDirectory())
				{
					String system_name = save_dir_file.getName();
					File[] system_name_dir_files = save_dir_file.listFiles();
					for(File system_name_dir_file:system_name_dir_files)
					{
						if(system_name_dir_file.isDirectory())
						{
							String version_name = system_name_dir_file.getName();
							saved_system_versions.add(new System_Version(system_name, version_name, system_name_dir_file,true));
						}
					}
				}
			}
		}
		this.setSaved_system_versions(saved_system_versions);
	}
	
	public void searchParsingSystems()
	{
		ArrayList<System_Version> parsing_system_versions = new ArrayList<System_Version>();
		File saves_dir = new File(Settings.PARSING_SYSTEMS_FOLDER);
		if(saves_dir.isDirectory())
		{
			File[] save_dir_files = saves_dir.listFiles();
			for(File save_dir_file:save_dir_files)
			{
				if(save_dir_file.isDirectory())
				{
					String system_name = save_dir_file.getName();
					File[] system_name_dir_files = save_dir_file.listFiles();
					for(File system_name_dir_file:system_name_dir_files)
					{
						if(system_name_dir_file.isDirectory())
						{
							String version_name = system_name_dir_file.getName();
							boolean saved = false;
							for(System_Version saved_version : this.saved_system_versions)
							{
								if((saved_version.getSystem_name().equalsIgnoreCase(system_name))&&(saved_version.getSystem_version().equalsIgnoreCase(version_name)))
								{
									saved = true;
									break;
								}
							}
							parsing_system_versions.add(new System_Version(system_name, version_name, system_name_dir_file, saved));
						}		
					}
				}
			}
		}
		this.setParsing_system_versions(parsing_system_versions);
	}
	
	public void loadSavedSystem(Load_System_Selection_View frame, System_Version version_to_load)
	{
		searchSavedSystems();
		if(this.saved_system_versions.contains(version_to_load))
		{
			Settings version_settings = Settings.loadSettingsVersion(version_to_load.getSystem_name(), version_to_load.getSystem_version());
			if(version_settings != null)
			{
				//CALL LOADER
				frame.showMessage("CALLING SAVE LOADER->SYSNAME:"+version_to_load.getSystem_name()+" - VERS:"+version_to_load.getSystem_version());

				System_Analysis app_data = LoaderTemplateMethod.loader_linker(version_settings);
				//close WIN
				frame.closeForm();
				new Home_System_Analysis_View(app_data);
			}
			else
			{
				frame.showMessage("ERROR! Can't find SAVED VERS SETTINGS->SYSNAME:"+version_to_load.getSystem_name()+" - VERS:"+version_to_load.getSystem_version());
			}
		}
		else
		{
			frame.showMessage("ERROR! Can't find SAVED VERS FOLDER->SYSNAME:"+version_to_load.getSystem_name()+" - VERS:"+version_to_load.getSystem_version());
		}
	}
	
	public void parseSystem(Load_System_Selection_View frame, System_Version version_to_parse)
	{
		searchParsingSystems();
		if(this.parsing_system_versions.contains(version_to_parse))
		{
			Settings version_settings = Settings.loadSettingsVersion(version_to_parse.getSystem_name(), version_to_parse.getSystem_version());
			if(version_settings != null)
			{
				//CALL PARSER
				frame.showMessage("CALLING PARSER->SYSNAME:"+version_to_parse.getSystem_name()+" - VERS:"+version_to_parse.getSystem_version());
				frame.closeForm();
				new Settings_Parser_View(version_settings);
			}
			else
			{
				frame.showMessage("ERROR! Can't find VERS SETTINGS->SYSNAME:"+version_to_parse.getSystem_name()+" - VERS:"+version_to_parse.getSystem_version());
			}
		}
		else
		{
			frame.showMessage("ERROR! Can't find VERS FOLDER->SYSNAME:"+version_to_parse.getSystem_name()+" - VERS:"+version_to_parse.getSystem_version());
		}
	}
}
