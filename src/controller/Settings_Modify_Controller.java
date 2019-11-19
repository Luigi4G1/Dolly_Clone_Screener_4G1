package controller;

import java.io.File;

import persistence.Settings;
import utils.FileUtils4G1;
import view.Settings_Modify_View;
import view.Settings_Parser_View;
import view.Settings_View;

public class Settings_Modify_Controller {

	private static Settings_Modify_Controller instance = null;
	
	public static Settings_Modify_Controller getInstance()
	{
		if(instance == null)
			instance = new Settings_Modify_Controller();
		return instance;
	}
	
	public boolean saveSettings(Settings_View frame, Settings original_settings, Settings settings_to_save, File cloneClassesFile, File cloneLinesFile, File clonePairsFile, String commitlist_filename, File commitlistFile)
	{
		//INPUT ERROR
		if(settings_to_save == null)
		{
			frame.showAlert("IMPOSSIBLE TO CREATE SETTINGS->INPUT ERROR!", "INPUT ERROR");
			frame.showMessage("IMPOSSIBLE TO CREATE SETTINGS->INPUT ERROR");
			return false;
		}
		if(settings_to_save.getRepo_info() == null)
		{
			frame.showAlert("IMPOSSIBLE TO CREATE SETTINGS->INPUT ERROR!\n->GIT REPO", "INPUT ERROR");
			frame.showMessage("IMPOSSIBLE TO CREATE SETTINGS->INPUT ERROR->GIT REPO");
			return false;
		}
		if(settings_to_save.getNicad_settings() == null)
		{
			frame.showAlert("IMPOSSIBLE TO CREATE SETTINGS->INPUT ERROR!\n->NICAD", "INPUT ERROR");
			frame.showMessage("IMPOSSIBLE TO CREATE SETTINGS->INPUT ERROR->NICAD");
			return false;
		}
		if(original_settings == null)
		{
			frame.showAlert("IMPOSSIBLE TO SAVE SETTINGS->ORIGINAL SETTINGS MISSING!", "ORIGINAL SETTINGS MISSING");
			frame.showMessage("IMPOSSIBLE TO SAVE SETTINGS->ORIGINAL SETTINGS MISSING");
			return false;
		}
		if(original_settings.getRepo_info() == null)
		{
			frame.showAlert("IMPOSSIBLE TO SAVE SETTINGS->ORIGINAL SETTINGS ERROR!\n->GIT REPO", "ORIGINAL SETTINGS ERROR");
			frame.showMessage("IMPOSSIBLE TO SAVE SETTINGS->INPUT ERROR->GIT REPO");
			return false;
		}
		if(original_settings.getNicad_settings() == null)
		{
			frame.showAlert("IMPOSSIBLE TO SAVE SETTINGS->ORIGINAL SETTINGS ERROR!\n->NICAD", "ORIGINAL SETTINGS ERROR");
			frame.showMessage("IMPOSSIBLE TO SAVE SETTINGS->ORIGINAL SETTINGS ERROR->NICAD");
			return false;
		}
		
		//TESTiNG SYSTEM NAME
		String system_name = settings_to_save.getRepo_info().getSystem_name();
		if(system_name == null)
		{
			frame.showAlert("SYSTEM NAME NOT SET!", "Git SETTINGS PROBLEM");
			return false;
		}
		else
		{
			system_name = FileUtils4G1.filePathNormalize(system_name.trim());
			/*
			while(system_name.startsWith(File.separator))
				system_name = system_name.substring(1);
			while(system_name.endsWith(File.separator))
				system_name = system_name.substring(0, system_name.length()-1);
			*/
			system_name = FileUtils4G1.removeStartingSeparator(system_name);
			system_name = FileUtils4G1.removeTrailingSeparator(system_name);
			settings_to_save.getRepo_info().setSystem_name(system_name);
		}

		//TESTiNG systemVersion
		String systemVersion_string = settings_to_save.getNicad_settings().getVersion_commit();
		
		if(systemVersion_string == null)
		{
			frame.showAlert("NiCad ANALYZED SYSTEM VERSION NOT SET!", "NiCad SETTINGS PROBLEM");
			return false;
		}
		else
		{
			systemVersion_string = FileUtils4G1.filePathNormalize(systemVersion_string.trim());
			/*
			while(systemVersion_string.startsWith(File.separator))
				systemVersion_string = systemVersion_string.substring(1);
			while(systemVersion_string.endsWith(File.separator))
				systemVersion_string = systemVersion_string.substring(0, systemVersion_string.length()-1);
			*/
			systemVersion_string = FileUtils4G1.removeStartingSeparator(systemVersion_string);
			systemVersion_string = FileUtils4G1.removeTrailingSeparator(systemVersion_string);
			settings_to_save.getNicad_settings().setVersion_commit(systemVersion_string);
		}

		//COMPARE SETTINGS
		//NOTE: also tests NiCad filenames but NOT commit_list.txt
		if(original_settings.equals(settings_to_save))
		{
			//NEED to CHECK IF ADDED FILES by CHOOSERs
			//with the SAME NAME -> maybe from DIFFERENT FOLDERS -> check FULLPATH
			//File cloneClassesFile, File cloneLinesFile, File clonePairsFile
			//NOTE:String commitlist_filename, File commitlistFile -> CAN HAVE DIFFERENT NAMES instead
			
			//TESTiNG commitlist.txt
			//String commitlist_filename
			//File commitlistFile
			if(commitlist_filename == null)
			{
				frame.showAlert("COMMIT LIST HISTORY FILE NOT SET!", "SETTINGS PROBLEM");
				return false;
			}
			else
			{
				commitlist_filename = FileUtils4G1.filePathNormalize(commitlist_filename.trim());
				/*
				while(commitlist_filename.startsWith(File.separator))
					commitlist_filename = commitlist_filename.substring(1);
				*/
				commitlist_filename = FileUtils4G1.removeStartingSeparator(commitlist_filename);

				if(!commitlist_filename.equalsIgnoreCase(Settings.COMMIT_LIST_FILE))
				{
					return Settings_Create_Controller.getInstance().saveSettings(frame, settings_to_save, cloneClassesFile, cloneLinesFile, clonePairsFile, commitlist_filename, commitlistFile);
				}
				else
				{
					//maybe from DIFFERENT FOLDERS -> check FULLPATH
					if(commitlistFile == null)
						return Settings_Create_Controller.getInstance().saveSettings(frame, settings_to_save, cloneClassesFile, cloneLinesFile, clonePairsFile, commitlist_filename, commitlistFile);
					else
					{
						File commitlistFile_to_test = new File(Settings.PARSING_SYSTEMS_FOLDER+system_name+File.separator+systemVersion_string+File.separator+commitlist_filename);
						if(!commitlistFile_to_test.getAbsolutePath().equalsIgnoreCase(commitlistFile.getAbsolutePath()))
							return Settings_Create_Controller.getInstance().saveSettings(frame, settings_to_save, cloneClassesFile, cloneLinesFile, clonePairsFile, commitlist_filename, commitlistFile);
					}
				}
			}
			
			//TESTiNG clone_classes_file
			//File cloneClassesFile
			//SAME NAME (or CHECK NULL)
			String clone_classes_file_string = settings_to_save.getNicad_settings().getClone_classes_file();
			if(clone_classes_file_string == null)
			{
				frame.showAlert("NiCad CLONE CLASSES FILE NOT SET!", "NiCad SETTINGS PROBLEM");
				return false;
			}
			else
			{
				clone_classes_file_string = FileUtils4G1.filePathNormalize(clone_classes_file_string.trim());
				/*
				while(clone_classes_file_string.startsWith(File.separator))
					clone_classes_file_string = clone_classes_file_string.substring(1);
				*/
				clone_classes_file_string = FileUtils4G1.removeStartingSeparator(clone_classes_file_string);
				settings_to_save.getNicad_settings().setClone_classes_file(clone_classes_file_string);
				
				//maybe from DIFFERENT FOLDERS -> check FULLPATH
				if(cloneClassesFile == null)
					return Settings_Create_Controller.getInstance().saveSettings(frame, settings_to_save, cloneClassesFile, cloneLinesFile, clonePairsFile, commitlist_filename, commitlistFile);
				else
				{
					File cloneClassesFile_to_test = new File(Settings.PARSING_SYSTEMS_FOLDER+system_name+File.separator+systemVersion_string+File.separator+clone_classes_file_string);
					if(!cloneClassesFile_to_test.getAbsolutePath().equalsIgnoreCase(cloneClassesFile.getAbsolutePath()))
						return Settings_Create_Controller.getInstance().saveSettings(frame, settings_to_save, cloneClassesFile, cloneLinesFile, clonePairsFile, commitlist_filename, commitlistFile);
				}
			}
			
			//TESTiNG clone_lines_file
			//File cloneLinesFile,
			//NOTE: not used if you want to use Report_NiCad BUILDER METHODS  like
			//-> getCloneLinesFromOriginFileXXXXX();
			String clone_lines_file_string = settings_to_save.getNicad_settings().getClone_lines_file();
			if(clone_lines_file_string != null)
			{
				clone_lines_file_string = FileUtils4G1.filePathNormalize(clone_lines_file_string.trim());
				/*
				while(clone_lines_file_string.startsWith(File.separator))
					clone_lines_file_string = clone_lines_file_string.substring(1);
				*/
				clone_lines_file_string = FileUtils4G1.removeStartingSeparator(clone_lines_file_string);
				settings_to_save.getNicad_settings().setClone_lines_file(clone_lines_file_string);

				//maybe from DIFFERENT FOLDERS -> check FULLPATH
				if(cloneLinesFile == null)
					return Settings_Create_Controller.getInstance().saveSettings(frame, settings_to_save, cloneClassesFile, cloneLinesFile, clonePairsFile, commitlist_filename, commitlistFile);
				else
				{
					File cloneLinesFile_to_test = new File(Settings.PARSING_SYSTEMS_FOLDER+system_name+File.separator+systemVersion_string+File.separator+clone_lines_file_string);
					if(!cloneLinesFile_to_test.getAbsolutePath().equalsIgnoreCase(cloneLinesFile.getAbsolutePath()))
						return Settings_Create_Controller.getInstance().saveSettings(frame, settings_to_save, cloneClassesFile, cloneLinesFile, clonePairsFile, commitlist_filename, commitlistFile);
				}
			}
			
			//TESTiNG clone_pairs_file
			//File clonePairsFile
			String clone_pairs_file_string = settings_to_save.getNicad_settings().getClone_pairs_file();
			if(clone_pairs_file_string == null)
			{
				frame.showAlert("NiCad CLONE PAIRS FILE NOT SET!", "NiCad SETTINGS PROBLEM");
				return false;
			}
			else
			{
				clone_pairs_file_string = FileUtils4G1.filePathNormalize(clone_pairs_file_string.trim());
				/*
				while(clone_pairs_file_string.startsWith(File.separator))
					clone_pairs_file_string = clone_pairs_file_string.substring(1);
				*/
				clone_pairs_file_string = FileUtils4G1.removeStartingSeparator(clone_pairs_file_string);
				settings_to_save.getNicad_settings().setClone_pairs_file(clone_pairs_file_string);

				//maybe from DIFFERENT FOLDERS -> check FULLPATH
				if(clonePairsFile == null)
					return Settings_Create_Controller.getInstance().saveSettings(frame, settings_to_save, cloneClassesFile, cloneLinesFile, clonePairsFile, commitlist_filename, commitlistFile);
				else
				{
					File clonePairsFile_to_test = new File(Settings.PARSING_SYSTEMS_FOLDER+system_name+File.separator+systemVersion_string+File.separator+clone_pairs_file_string);
					if(!clonePairsFile_to_test.getAbsolutePath().equalsIgnoreCase(clonePairsFile.getAbsolutePath()))
						return Settings_Create_Controller.getInstance().saveSettings(frame, settings_to_save, cloneClassesFile, cloneLinesFile, clonePairsFile, commitlist_filename, commitlistFile);
				}
			}
			//ELSE
			////////////////////////////////////////////////
			frame.showMessage("NOTHING MODIFIED!");
			return false;
		}
		else
		{
			String old_sys_name = original_settings.getRepo_info().getSystem_name();
			String new_sys_name = settings_to_save.getRepo_info().getSystem_name();
			String old_sys_vers = original_settings.getNicad_settings().getVersion_commit();
			String new_sys_vers = settings_to_save.getNicad_settings().getVersion_commit();
			if((new_sys_name.equalsIgnoreCase(old_sys_name))&&(new_sys_vers.equalsIgnoreCase(old_sys_vers)))
			{
				//SAVE NEW SETTINGS -> IF passed TESTs()
				return Settings_Create_Controller.getInstance().saveSettings(frame, settings_to_save, cloneClassesFile, cloneLinesFile, clonePairsFile, commitlist_filename, commitlistFile);
				//SHOW NEW SETTINGS
				/*
				frame.closeForm();
				new Settings_Parser_View(settings_to_save);*/
			}
			else
			{
				frame.showMessage("IMPOSSIBLE TO MODIFY SETTINGS->DIFFERENT SYSNAME|SYSVERS");
				System.out.println("old_sys_name - old_sys_vers | new_sys_name - new_sys_vers\n"+old_sys_name+" - " +old_sys_vers+"\n"+new_sys_name+" - " +new_sys_vers);
				return false;
			}
		}
	}
	
	public void discardChanges(Settings_Modify_View frame, Settings settings_to_show)
	{
		frame.closeForm();
		new Settings_Parser_View(settings_to_show);
	}
	
	public void showCreatedSettings(Settings_View frame, Settings settings_to_show)
	{
		frame.closeForm();
		new Settings_Parser_View(settings_to_show);
	}
}