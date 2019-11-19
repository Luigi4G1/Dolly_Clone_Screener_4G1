package controller;

import java.io.File;

import persistence.Settings;
import persistence.System_Analysis;
import persistence.System_Version;
import view.Download_Sys_Version_View;
import view.Home_System_Analysis_View;
import view.Load_Last_Saved_System_View;
import view.Load_System_Selection_View;
import view.Settings_Create_View;

public class Load_Saved_System_Controller {

	
	
	private static Load_Saved_System_Controller instance = null;
	
	private Load_Saved_System_Controller()
	{}
	
	public static Load_Saved_System_Controller getInstance()
	{
		if(instance == null)
			instance = new Load_Saved_System_Controller();
		return instance;
	}
	
	public System_Version getLastSavedSystem(Load_Last_Saved_System_View frame)
	{
		System_Version toReturn = null;
		
		File settings_file = new File(Settings.SETTINGS_FILE);
		if(settings_file.exists())
		{
			Settings saved_version_settings_file = Settings.loadSettingsFile();
			if(saved_version_settings_file != null)
			{
				
				File save_dir = new File(Settings.SAVED_SYSTEMS_FOLDER);
				if(save_dir.isDirectory())
				{
					String system_name = saved_version_settings_file.getSystem_name();
					if((system_name != null)&&(!system_name.isEmpty()))
					{
						File system_name_save_dir = new File(Settings.SAVED_SYSTEMS_FOLDER+system_name);
						if(system_name_save_dir.exists()&&system_name_save_dir.isDirectory())
						{
							String system_version_commit_id = saved_version_settings_file.getSystem_version_commit_id();

							if((system_version_commit_id != null)&&(!system_version_commit_id.isEmpty()))
							{
								File system_version_save_dir = new File(Settings.SAVED_SYSTEMS_FOLDER+system_name+File.separator+system_version_commit_id);
								if(system_version_save_dir.exists()&&system_version_save_dir.isDirectory())
								{
									toReturn = new System_Version(system_name, system_version_commit_id, system_version_save_dir, true);
								}
								else
								{
									frame.showMessage("ERROR: Can't find SYSTEM VERSION SAVE FOLDER:"+system_version_save_dir.getAbsolutePath());
								}
							}
							else
							{
								frame.showMessage("ERROR: SYSTEM VERSION NOT FOUND:"+system_version_commit_id);
							}
							
						}
						else
						{
							frame.showMessage("ERROR: Can't find SYSTEM SAVE FOLDER:"+system_name_save_dir.getAbsolutePath());
						}
					}
					else
					{
						frame.showMessage("ERROR: SYSTEM NAME NOT FOUND:"+system_name);
					}

				}
				else
				{
					frame.showMessage("ERROR: Can't find SAVE FOLDER:"+Settings.SAVED_SYSTEMS_FOLDER);
				}
			}
			else
			{
				frame.showMessage("ERROR: Can't read SETTINGS FILE:"+Settings.SETTINGS_FILE);
			}
		}
		else
		{
			frame.showMessage("ERROR: Can't find SETTINGS FILE:"+Settings.SETTINGS_FILE);
		}

		return toReturn;
	}
	
	public void loadSavedSystem(Load_Last_Saved_System_View frame, System_Version system_to_load)
	{
		if(system_to_load == null)
		{
			frame.showMessage("ERROR: Version to load NOT SET -> NULL");
		}
		else
		{
			//CALL LOADER
			Settings version_settings = Settings.loadSettingsVersion(system_to_load.getSystem_name(), system_to_load.getSystem_version());
			if(version_settings != null)
			{
				//CALL LOADER->
				frame.showMessage("CALLING LOADER->SYSNAME:"+system_to_load.getSystem_name()+" - VERS:"+system_to_load.getSystem_version());
				
				System_Analysis app_data = LoaderTemplateMethod.loader_linker(version_settings);
				//close WIN
				frame.closeForm();
				new Home_System_Analysis_View(app_data);
			}
			else
			{
				frame.showMessage("ERROR! Can't find VERS SETTINGS->SYSNAME:"+system_to_load.getSystem_name()+" - VERS:"+system_to_load.getSystem_version());
			}
			
		}
	}
	
	public void goToSystemSelection(Load_Last_Saved_System_View frame)
	{
		frame.closeForm();
		new Load_System_Selection_View();
	}
	
	public void goToCreateSettings(Load_Last_Saved_System_View frame)
	{
		frame.closeForm();
		new Settings_Create_View();
	}
	
	public void goToDownloadSysVersion(Load_Last_Saved_System_View frame)
	{
		frame.closeForm();
		new Download_Sys_Version_View();
	}
	
}
