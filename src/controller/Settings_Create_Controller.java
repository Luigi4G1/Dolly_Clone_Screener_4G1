package controller;

import java.io.File;

import persistence.Settings;
import utils.FileUtils4G1;
import utils.GitUtils4G1;
import view.Settings_Parser_View;
import view.Settings_View;

public class Settings_Create_Controller {

	private static Settings_Create_Controller instance = null;
	
	private Settings_Create_Controller() {}
	
	public static Settings_Create_Controller getInstance()
	{
		if(instance == null)
			instance = new Settings_Create_Controller();
		return instance;
	}
	
	public boolean testSettings(Settings_View frame, Settings settings_to_test, File cloneClassesFile, File cloneLinesFile, File clonePairsFile, String commitlist_filename, File commitlistFile)
	{
		//INPUT ERROR
		if(settings_to_test == null)
		{
			frame.showAlert("IMPOSSIBLE TO CREATE SETTINGS->INPUT ERROR!", "INPUT ERROR");
			frame.showMessage("IMPOSSIBLE TO CREATE SETTINGS->INPUT ERROR");
			return false;
		}
		if(settings_to_test.getRepo_info() == null)
		{
			frame.showAlert("IMPOSSIBLE TO CREATE SETTINGS->INPUT ERROR!\n->GIT REPO", "INPUT ERROR");
			frame.showMessage("IMPOSSIBLE TO CREATE SETTINGS->INPUT ERROR->GIT REPO");
			return false;
		}
		if(settings_to_test.getNicad_settings() == null)
		{
			frame.showAlert("IMPOSSIBLE TO CREATE SETTINGS->INPUT ERROR!\n->NICAD", "INPUT ERROR");
			frame.showMessage("IMPOSSIBLE TO CREATE SETTINGS->INPUT ERROR->NICAD");
			return false;
		}

		boolean isNew = true;
		//START TEST--------------------------
		boolean problems_found = false;
		String problems_found_message = "PROBLEMs FOUND:";
		//TEST GIT ATTRIBUTES
		//TESTiNG repo_URI
		String repo_path_uri = settings_to_test.getRepo_info().getRepo_path_uri();
		if(repo_path_uri == null)
		{
			frame.showAlert("Git REPO URI NOT SET!", "Git SETTINGS PROBLEM");
			return false;
		}
		else
		{
			repo_path_uri.trim();
			settings_to_test.getRepo_info().setRepo_path_uri(repo_path_uri);

			String repo_local_path = settings_to_test.getRepo_info().getRepo_local_path();
			repo_local_path = FileUtils4G1.filePathNormalize(repo_local_path.trim());
			if(!repo_local_path.endsWith(File.separator))
				repo_local_path = repo_local_path+ File.separator;
			settings_to_test.getRepo_info().setRepo_local_path(repo_local_path);
			
			//ONLY A SUGGESTION -> CHECK IF IS A VALID REPO URI
			boolean invalid_repo = !GitUtils4G1.isValidRepoUri(repo_path_uri);
			if(invalid_repo)
			{
				frame.highlightRepoURITextField(invalid_repo);
				frame.showAlert("INVALID REPO URI? -> CHECK IF IT IS A VALID GIT REPO URI", "Git SETTINGS PROBLEM");
				problems_found_message = problems_found_message + "\n- INVALID REPO URI?";
				problems_found = true;
			}
		}
		
		//TESTiNG SYSTEM NAME
		String system_name = settings_to_test.getRepo_info().getSystem_name();
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
			settings_to_test.getRepo_info().setSystem_name(system_name);
		}

		//TEST NICAD ATTRIBUTES
		//TESTiNG root_fullpath
		String root_fullpath = settings_to_test.getNicad_settings().getRoot_fullpath();
		if(root_fullpath == null)
		{
			frame.showAlert("NiCad ROOT FULLPATH NOT SET!", "NiCad SETTINGS PROBLEM");
			problems_found_message = problems_found_message + "\n- NiCad ROOT FULLPATH NOT SET";
			problems_found = true;
		}
		else
		{
			root_fullpath = FileUtils4G1.filePathNormalize(root_fullpath.trim());
			if(!root_fullpath.endsWith(File.separator))
				root_fullpath = root_fullpath + File.separator;
			settings_to_test.getNicad_settings().setRoot_fullpath(root_fullpath);
			File root_fullpath_dir = new File(root_fullpath);
			if(!(root_fullpath_dir.exists()&&root_fullpath_dir.isDirectory()))
			{
				frame.showAlert("NiCad ROOT FULLPATH\n->"+root_fullpath, "NiCad SETTINGS PROBLEM");
				problems_found_message = problems_found_message + "\n- NiCad ROOT FULLPATH DIR";
				problems_found = true;
			}
		}

		//TESTiNG analyzedsystems_root_folder
		String analyzedsystems_root_folder = settings_to_test.getNicad_settings().getAnalyzedsystems_root_folder();
		if(analyzedsystems_root_folder == null)
		{
			frame.showAlert("NiCad ANALYZED SYSTEMS FOLDER NOT SET!", "NiCad SETTINGS PROBLEM");
			problems_found_message = problems_found_message + "\n- NiCad ANALYZED SYSTEMS FOLDER NOT SET";
			problems_found = true;
		}
		else
		{
			analyzedsystems_root_folder = FileUtils4G1.filePathNormalize(analyzedsystems_root_folder.trim());
			/*
			while(analyzedsystems_root_folder.startsWith(File.separator))
				analyzedsystems_root_folder = analyzedsystems_root_folder.substring(1);
			while(analyzedsystems_root_folder.endsWith(File.separator))
				analyzedsystems_root_folder = analyzedsystems_root_folder.substring(0, analyzedsystems_root_folder.length()-1);
			*/
			analyzedsystems_root_folder = FileUtils4G1.removeStartingSeparator(analyzedsystems_root_folder);
			analyzedsystems_root_folder = FileUtils4G1.removeTrailingSeparator(analyzedsystems_root_folder);
			settings_to_test.getNicad_settings().setAnalyzedsystems_root_folder(analyzedsystems_root_folder);
			
			File analyzedsystems_root_folder_dir = new File(root_fullpath+analyzedsystems_root_folder);
			if(!(analyzedsystems_root_folder_dir.exists()&&analyzedsystems_root_folder_dir.isDirectory()))
			{
				frame.showAlert("NiCad ANALYZED SYSTEMS FOLDER\n->"+analyzedsystems_root_folder, "NiCad SETTINGS PROBLEM");
				problems_found_message = problems_found_message + "\n- NiCad ANALYZED SYSTEMS FOLDER DIR";
				problems_found = true;
			}
		}

		//TESTiNG systemVersion
		String systemVersion_string = settings_to_test.getNicad_settings().getVersion_commit();
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
			
			settings_to_test.getNicad_settings().setVersion_commit(systemVersion_string);
			String original_files_folder = root_fullpath+analyzedsystems_root_folder+File.separator+system_name+File.separator+systemVersion_string+File.separator+system_name;
			File analyzedsystem_version_dir = new File(original_files_folder);
			System.out.println("NiCad ANALYZED SYSTEM VERSION FOLDER\n"+original_files_folder);
			if(!(analyzedsystem_version_dir.exists()&&analyzedsystem_version_dir.isDirectory()))
			{
				frame.showAlert("NiCad ANALYZED SYSTEM VERSION FOLDER\n->"+original_files_folder, "NiCad SETTINGS PROBLEM");
				problems_found_message = problems_found_message + "\n- NiCad ANALYZED SYSTEM VERSION FOLDER DIR";
				problems_found = true;
			}
		}

		//TESTiNG clone_classes_file
		//File cloneClassesFile
		String clone_classes_file_string = settings_to_test.getNicad_settings().getClone_classes_file();
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

			settings_to_test.getNicad_settings().setClone_classes_file(clone_classes_file_string);
			
			File cloneClassesFile_to_test = null;
			String clone_classes_filepath_string = Settings.PARSING_SYSTEMS_FOLDER+system_name+File.separator+systemVersion_string+File.separator+clone_classes_file_string;
			if(cloneClassesFile != null)
			{
				if(clone_classes_file_string.equalsIgnoreCase(cloneClassesFile.getName()))
					cloneClassesFile_to_test = cloneClassesFile;
				else
					cloneClassesFile_to_test = new File(clone_classes_filepath_string);
			}
			else
				cloneClassesFile_to_test = new File(clone_classes_filepath_string);
			frame.setCloneClassesFile(cloneClassesFile_to_test);

			if(!(cloneClassesFile_to_test.exists()&&cloneClassesFile_to_test.isFile()&&(FileUtils4G1.getExtension(cloneClassesFile_to_test).equalsIgnoreCase(FileUtils4G1.xml))))
			{
				frame.showAlert("NiCad CLONE CLASSES FILE NOT FOUND!\n"+cloneClassesFile_to_test.getAbsolutePath(), "NiCad SETTINGS PROBLEM");
				return false;
			}
		}

		//TESTiNG clone_lines_file
		//File cloneLinesFile,
		//NOTE: not used if you want to use Report_NiCad BUILDER METHODS  like
		//-> getCloneLinesFromOriginFileXXXXX();
		String clone_lines_file_string = settings_to_test.getNicad_settings().getClone_lines_file();
		if(clone_lines_file_string == null)
		{
			frame.showAlert("NiCad CLONE LINES FILE NOT SET!", "NiCad SETTINGS PROBLEM");
			problems_found_message = problems_found_message + "\n- NiCad CLONE LINES FILE NOT SET";
			problems_found = true;
		}
		else
		{
			clone_lines_file_string = FileUtils4G1.filePathNormalize(clone_lines_file_string.trim());
			/*
			while(clone_lines_file_string.startsWith(File.separator))
				clone_lines_file_string = clone_lines_file_string.substring(1);
			*/
			clone_lines_file_string = FileUtils4G1.removeStartingSeparator(clone_lines_file_string);

			settings_to_test.getNicad_settings().setClone_lines_file(clone_lines_file_string);

			File cloneLinesFile_to_test = null;
			String clone_lines_filepath_string = Settings.PARSING_SYSTEMS_FOLDER+system_name+File.separator+systemVersion_string+File.separator+clone_lines_file_string;
			if(cloneLinesFile != null)
			{
				if(clone_lines_file_string.equalsIgnoreCase(cloneLinesFile.getName()))
					cloneLinesFile_to_test = cloneLinesFile;
				else
					cloneLinesFile_to_test = new File(clone_lines_filepath_string);
			}
			else
				cloneLinesFile_to_test = new File(clone_lines_filepath_string);
			frame.setCloneLinesFile(cloneLinesFile_to_test);

			if(!(cloneLinesFile_to_test.exists()&&cloneLinesFile_to_test.isFile()&&(FileUtils4G1.getExtension(cloneLinesFile_to_test).equalsIgnoreCase(FileUtils4G1.html))))
			{
				frame.showAlert("NiCad CLONE LINES FILE NOT FOUND!\n"+cloneLinesFile_to_test.getAbsolutePath(), "NiCad SETTINGS PROBLEM");
				problems_found_message = problems_found_message + "\n- NiCad CLONE LINES FILE NOT FOUND";
				problems_found = true;
			}
		}

		//TESTiNG clone_pairs_file
		//File clonePairsFile
		String clone_pairs_file_string = settings_to_test.getNicad_settings().getClone_pairs_file();
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

			settings_to_test.getNicad_settings().setClone_pairs_file(clone_pairs_file_string);
			
			File clonePairsFile_to_test = null;
			String clone_pairs_filepath_string = Settings.PARSING_SYSTEMS_FOLDER+system_name+File.separator+systemVersion_string+File.separator+clone_pairs_file_string;
			if(clonePairsFile != null)
			{
				if(clone_pairs_file_string.equalsIgnoreCase(clonePairsFile.getName()))
					clonePairsFile_to_test = clonePairsFile;
				else
					clonePairsFile_to_test = new File(clone_pairs_filepath_string);
			}
			else
				clonePairsFile_to_test = new File(clone_pairs_filepath_string);
			frame.setClonePairsFile(clonePairsFile_to_test);
			
			if(!(clonePairsFile_to_test.exists()&&clonePairsFile_to_test.isFile()&&(FileUtils4G1.getExtension(clonePairsFile_to_test).equalsIgnoreCase(FileUtils4G1.xml))))
			{
				frame.showAlert("NiCad CLONE PAIRS FILE NOT FOUND!\n"+clonePairsFile_to_test.getAbsolutePath(), "NiCad SETTINGS PROBLEM");
				return false;
			}
		}

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

			File commitlistFile_to_test = null;
			String commitlist_filename_filepath_string = Settings.PARSING_SYSTEMS_FOLDER+system_name+File.separator+systemVersion_string+File.separator+commitlist_filename;
			if(commitlistFile != null)
			{
				if(commitlist_filename.equalsIgnoreCase(commitlistFile.getName()))
					commitlistFile_to_test = commitlistFile;
				else
					commitlistFile_to_test = new File(commitlist_filename_filepath_string);
			}
			else
				commitlistFile_to_test = new File(commitlist_filename_filepath_string);
			frame.setCommitlistFile(commitlistFile_to_test);
			
			if(!(commitlistFile_to_test.exists()&&commitlistFile_to_test.isFile()&&(FileUtils4G1.getExtension(commitlistFile_to_test).equalsIgnoreCase(FileUtils4G1.txt))))
			{
				frame.showAlert("COMMIT LIST HISTORY FILE NOT FOUND!\n"+commitlistFile_to_test.getAbsolutePath(), "SETTINGS PROBLEM");
				return false;
			}
		}
		
		//-> END OF FIELDS TEST
		if(problems_found)
		{
			isNew = frame.continueOperationAlert(problems_found_message+"\n\nDO YOU WANT TO SAVE IT ANYWAY?", "SETTINGS PROBLEMS FOUND");
		}
		return isNew;
	}
	
	public boolean saveSettings(Settings_View frame, Settings settings_to_save, File cloneClassesFile, File cloneLinesFile, File clonePairsFile, String commitlist_filename, File commitlistFile)
	{
		boolean create_settings_file = true;
		//COMPARE SETTINGS
		if(testSettings(frame, settings_to_save, cloneClassesFile, cloneLinesFile, clonePairsFile, commitlist_filename, commitlistFile))
		{
			//UPDATE VALUES -> because TEST() SET NEW VALUES
			cloneClassesFile = frame.getCloneClassesFile();
			cloneLinesFile = frame.getCloneLinesFile();
			clonePairsFile = frame.getClonePairsFile();
			commitlistFile = frame.getCommitlistFile();
			
			//SAVE NEW SETTINGS
			
			//OPTIONAL
			//CREATE NiCad Analyzed System Version folder -> if you want to use some of the Report_NiCad BUILDER METHODS like:
			//- getCloneLinesFromOriginFileAnalyzedByNiCad();
			//- getCloneLinesFromOriginFileAnalyzedByNiCadWithoutComments();
			//It's not strictly necessary if you use, for example, this other METHOD:
			//- getCloneLinesFromOriginFileDLDByCheckoutCommand();
			String nicad_repo_system_version_path = settings_to_save.getNicad_settings().getRoot_fullpath()+settings_to_save.getNicad_settings().getAnalyzedsystems_root_folder()+File.separator+settings_to_save.getSystem_name()+File.separator+settings_to_save.getSystem_version_commit_id()+File.separator+settings_to_save.getSystem_name();
			System.out.println("NiCad AnSysFolder->"+nicad_repo_system_version_path);
			File folder_for_saves = new File(nicad_repo_system_version_path);
			if(!folder_for_saves.exists())
			{
				String message = "CREATE NiCad Analyzed System Version folder ->\n"
						+"if you want to use some of the Report_NiCad BUILDER METHODS like:\n"
						+"- getCloneLinesFromOriginFileAnalyzedByNiCad();\n"
						+"- getCloneLinesFromOriginFileAnalyzedByNiCadWithoutComments();\n"
						+"It's not strictly necessary if you use, for example, this other METHOD:\n"
						+"- getCloneLinesFromOriginFileDLDByCheckoutCommand();\n\n"
						+"DO YOU WANT to DOWNLOAD THIS VERSION?\n"
						+"["+settings_to_save.getSystem_name()+" - Vers.COMMITID:"+settings_to_save.getSystem_version_commit_id()+"]\n"
						+"INSIDE FOLDER->["+nicad_repo_system_version_path+"]"
						;
				String title = "DOWNLOAD SYSTEM VERSION for NiCad";
				boolean download_sys_vers_for_nicad = frame.continueOperationAlert(message, title);
				if(download_sys_vers_for_nicad)
				{
					String folder_for_saves_string = settings_to_save.getNicad_settings().getRoot_fullpath()+settings_to_save.getNicad_settings().getAnalyzedsystems_root_folder()+File.separator;
					String folder_created = settings_to_save.getRepo_info().downloadRepoByCommitIDToFolderNoGit(settings_to_save.getSystem_version_commit_id(), folder_for_saves_string);
					if(folder_created != null)
						frame.showMessagePopUp("VERSION DOWNLOADED!->\n"+settings_to_save.getSystem_version_commit_id()+"\nto FOLDER->\n"+nicad_repo_system_version_path, "SYSTEM VERSION DOWNLOADED");
					else
						frame.showAlert("UNABLE TO DOWNLOAD VERSION to the selected folder\n"+nicad_repo_system_version_path, "DOWNLOAD PROBLEM");
				}
			}

			//COPY FILES to PARSING FOLDER
			String parsing_system_folder_string = Settings.PARSING_SYSTEMS_FOLDER+settings_to_save.getSystem_name()+File.separator+settings_to_save.getSystem_version_commit_id()+File.separator;
			File parsing_system_folder = new File(parsing_system_folder_string);
			
			String warning_message = "BEFORE PARSING, REMEMBER:\n";
			boolean warning_message_necessity = false;
			
			try
			{
				//CREATE PARSING FOLDER if not exists
				if(!parsing_system_folder.exists())
					parsing_system_folder.mkdirs();

				if(parsing_system_folder.exists())
				{
					String message ="DO YOU WANT copy this file (NEEDED FOR PARSING OP) to PARSING DIRECTORY NOW?";
					String title = "FILE COPY";

					//->CLONECLASSES file
					//NOTE: cloneClassesFile EXISTS -> verified by testSettings();
					try
					{
						File clone_classes_file = new File(parsing_system_folder_string+settings_to_save.getNicad_settings().getClone_classes_file());
						String is_present = null;
						String popup_message = null;
						String popup_title = null;
						
						if(clone_classes_file.exists())
						{
							is_present = "ATTENTION: this file ALREADY EXISTS in the PARSING FOLDER!";
							popup_message = "\nNO NEED TO COPY AGAIN the SAME FILE";
							popup_title = "FILE ALREADY PRESENT";
						}
						else
						{
							is_present = "ATTENTION: this file NOT EXISTS in the PARSING FOLDER!";
							popup_message = "\nTHE SELECTED FILE HAS NOT BEEN FOUND.\nCREATE AND COPY IT inside the PARSING FOLDER";
							popup_title = "FILE NOT PRESENT";
						}
						
						if(!clone_classes_file.getAbsolutePath().equalsIgnoreCase(cloneClassesFile.getAbsolutePath()))
						{
							boolean copy_file_to_folder = frame.continueOperationAlert("FILE:\n"+settings_to_save.getNicad_settings().getClone_classes_file()+"\n"+is_present+"\n\n"+message, title);
							if(copy_file_to_folder)
							{
								if(FileUtils4G1.copyRenameFile(cloneClassesFile, clone_classes_file))
								{
									frame.showMessagePopUp("FILE:\n"+settings_to_save.getNicad_settings().getClone_classes_file()+"\nSUCCESSFULLY COPIED!", "FILE COPIED");
								}
								else
								{
									frame.showAlert("ERROR DURING COPY OPERATION of the FILE:\n"+settings_to_save.getNicad_settings().getClone_classes_file()+"\nUNABLE TO COPY IT!", "FILE NOT COPIED");
									warning_message = warning_message + "\n- copy "+settings_to_save.getNicad_settings().getClone_classes_file()+" -> to FOLDER\n\t"+parsing_system_folder_string+"\n\t["+is_present+"]";
									warning_message_necessity = true;
								}
							}
							else
							{
								warning_message = warning_message + "\n- copy "+settings_to_save.getNicad_settings().getClone_classes_file()+" -> to FOLDER\n\t"+parsing_system_folder_string+"\n\t["+is_present+"]";
								warning_message_necessity = true;
							}
						}
						else
							frame.showMessagePopUp("FILE:\n"+settings_to_save.getNicad_settings().getClone_classes_file()+"\n"+is_present+popup_message, popup_title);
					}
					catch(SecurityException e)
					{
						e.printStackTrace();
						frame.showAlert("UNABLE TO COPY the selected file:\n"+cloneClassesFile.getAbsolutePath()+"\nto the PARSING FOLDER:\n"+parsing_system_folder.getAbsolutePath(), "SAVING PROBLEM");
						warning_message = warning_message + "\n- copy "+settings_to_save.getNicad_settings().getClone_classes_file()+" -> to FOLDER\n\t"+parsing_system_folder_string;
					}

					//->CLONELINES file
					//NOTE: cloneLinesFile EXISTENCE NOT SURE -> verified but not NECESSARY to get TRUE by testSettings();
					//THIS FILE IS NOT NECESSARY if you use BUILDER METHODS getCloneLinesFromOriginFileXXX() like:
					//- getCloneLinesFromOriginFileAnalyzedByNiCad();
					//- getCloneLinesFromOriginFileAnalyzedByNiCadWithoutComments();
					//- getCloneLinesFromOriginFileDLDByCheckoutCommand();
					try
					{
						File clone_lines_file = new File(parsing_system_folder_string+settings_to_save.getNicad_settings().getClone_lines_file());
						String is_present = null;
						String popup_message = null;
						String popup_title = null;
						
						if(clone_lines_file.exists())
						{
							is_present = "ATTENTION: this file ALREADY EXISTS in the PARSING FOLDER!";
							popup_message = "\nNO NEED TO COPY AGAIN the SAME FILE";
							popup_title = "FILE ALREADY PRESENT";
						}
						else
						{
							is_present = "ATTENTION: this file NOT EXISTS in the PARSING FOLDER!";
							popup_message = "\nTHE SELECTED FILE HAS NOT BEEN FOUND.\nCREATE AND COPY IT inside the PARSING FOLDER";
							popup_title = "FILE NOT PRESENT";
						}
						
						if(!clone_lines_file.getAbsolutePath().equalsIgnoreCase(cloneLinesFile.getAbsolutePath()))
						{
							boolean copy_file_to_folder = frame.continueOperationAlert("FILE:\n"+settings_to_save.getNicad_settings().getClone_lines_file()+"\n"+is_present+"\n\n"+message, title);
							if(copy_file_to_folder)
							{
								if(FileUtils4G1.copyRenameFile(cloneLinesFile, clone_lines_file))
								{
									frame.showMessagePopUp("FILE:\n"+settings_to_save.getNicad_settings().getClone_lines_file()+"\nSUCCESSFULLY COPIED!", "FILE COPIED");
								}
								else
								{
									frame.showAlert("ERROR DURING COPY OPERATION of the FILE:\n"+settings_to_save.getNicad_settings().getClone_lines_file()+"\nUNABLE TO COPY IT!", "FILE NOT COPIED");
									warning_message = warning_message + "\n- copy "+settings_to_save.getNicad_settings().getClone_lines_file()+" -> to FOLDER\n\t"+parsing_system_folder_string+"\n\t["+is_present+"]";
									warning_message_necessity = true;
								}
							}
							else
							{
								warning_message = warning_message + "\n- copy "+settings_to_save.getNicad_settings().getClone_lines_file()+" -> to FOLDER\n\t"+parsing_system_folder_string+"\n\t["+is_present+"]";
								warning_message_necessity = true;
							}
						}
						else
							frame.showMessagePopUp("FILE:\n"+settings_to_save.getNicad_settings().getClone_lines_file()+"\n"+is_present+popup_message, popup_title);
					}
					catch(SecurityException e)
					{
						e.printStackTrace();
						frame.showAlert("UNABLE TO COPY the selected file:\n"+cloneLinesFile.getAbsolutePath()+"\nto the PARSING FOLDER:\n"+parsing_system_folder.getAbsolutePath(), "SAVING PROBLEM");
						warning_message = warning_message + "\n- copy "+settings_to_save.getNicad_settings().getClone_lines_file()+" -> to FOLDER\n\t"+parsing_system_folder_string;
					}

					//->CLONEPAIRS file
					//NOTE: clonePairsFile EXISTS -> verified by testSettings();
					try
					{
						File clone_pairs_file = new File(parsing_system_folder_string+settings_to_save.getNicad_settings().getClone_pairs_file());
						String is_present = null;
						String popup_message = null;
						String popup_title = null;
						
						if(clone_pairs_file.exists())
						{
							is_present = "ATTENTION: this file ALREADY EXISTS in the PARSING FOLDER!";
							popup_message = "\nNO NEED TO COPY AGAIN the SAME FILE";
							popup_title = "FILE ALREADY PRESENT";
						}
						else
						{
							is_present = "ATTENTION: this file NOT EXISTS in the PARSING FOLDER!";
							popup_message = "\nTHE SELECTED FILE HAS NOT BEEN FOUND.\nCREATE AND COPY IT inside the PARSING FOLDER";
							popup_title = "FILE NOT PRESENT";
						}

						if(!clone_pairs_file.getAbsolutePath().equalsIgnoreCase(clonePairsFile.getAbsolutePath()))
						{
							boolean copy_file_to_folder = frame.continueOperationAlert("FILE:\n"+settings_to_save.getNicad_settings().getClone_pairs_file()+"\n"+is_present+"\n\n"+message, title);
							if(copy_file_to_folder)
							{
								if(FileUtils4G1.copyRenameFile(clonePairsFile, clone_pairs_file))
								{
									frame.showMessagePopUp("FILE:\n"+settings_to_save.getNicad_settings().getClone_pairs_file()+"\nSUCCESSFULLY COPIED!", "FILE COPIED");
								}
								else
								{
									frame.showAlert("ERROR DURING COPY OPERATION of the FILE:\n"+settings_to_save.getNicad_settings().getClone_pairs_file()+"\nUNABLE TO COPY IT!", "FILE NOT COPIED");
									warning_message = warning_message + "\n- copy "+settings_to_save.getNicad_settings().getClone_pairs_file()+" -> to FOLDER\n\t"+parsing_system_folder_string+"\n\t["+is_present+"]";
									warning_message_necessity = true;
								}
							}
							else
							{
								warning_message = warning_message + "\n- copy "+settings_to_save.getNicad_settings().getClone_pairs_file()+" -> to FOLDER\n\t"+parsing_system_folder_string+"\n\t["+is_present+"]";
								warning_message_necessity = true;
							}
						}
						else
							frame.showMessagePopUp("FILE:\n"+settings_to_save.getNicad_settings().getClone_pairs_file()+"\n"+is_present+popup_message, popup_title);
					}
					catch(SecurityException e)
					{
						e.printStackTrace();
						frame.showAlert("UNABLE TO COPY the selected file:\n"+clonePairsFile.getAbsolutePath()+"\nto the PARSING FOLDER:\n"+parsing_system_folder.getAbsolutePath(), "SAVING PROBLEM");
						warning_message = warning_message + "\n- copy "+settings_to_save.getNicad_settings().getClone_pairs_file()+" -> to FOLDER\n\t"+parsing_system_folder_string;
					}

					//->COMMITLIST file
					//NOTE: commitlistFile EXISTS -> verified by testSettings();
					try
					{
						File commitlist_file = new File(parsing_system_folder_string+Settings.COMMIT_LIST_FILE);
						String is_present = null;
						String popup_message = null;
						String popup_title = null;
						
						if(commitlist_file.exists())
						{
							is_present = "ATTENTION: this file ALREADY EXISTS in the PARSING FOLDER!";
							popup_message = "\nNO NEED TO COPY AGAIN the SAME FILE";
							popup_title = "FILE ALREADY PRESENT";
						}
						else
						{
							is_present = "ATTENTION: this file NOT EXISTS in the PARSING FOLDER!";
							popup_message = "\nTHE SELECTED FILE HAS NOT BEEN FOUND.\nCREATE AND COPY IT inside the PARSING FOLDER";
							popup_title = "FILE NOT PRESENT";
						}

						if(!commitlist_file.getAbsolutePath().equalsIgnoreCase(frame.getCommitlistFile().getAbsolutePath()))
						{
							boolean copy_file_to_folder = frame.continueOperationAlert("FILE:\n"+frame.getCommitlistFile().getAbsolutePath()+"\nAS->"+Settings.COMMIT_LIST_FILE+"\n"+is_present+"\n\n"+message, title);
							if(copy_file_to_folder)
							{
								if(FileUtils4G1.copyRenameFile(frame.getCommitlistFile(), commitlist_file))
								{
									frame.showMessagePopUp("FILE:\n"+frame.getCommitlistFile().getAbsolutePath()+"\nAS->"+Settings.COMMIT_LIST_FILE+"\nSUCCESSFULLY COPIED!", "FILE COPIED");
								}
								else
								{
									frame.showAlert("ERROR DURING COPY OPERATION of the FILE:\n"+frame.getCommitlistFile().getAbsolutePath()+"\nAS->"+Settings.COMMIT_LIST_FILE+"\nUNABLE TO COPY IT!", "FILE NOT COPIED");
									warning_message = warning_message + "\n- copy "+frame.getCommitlistFile().getAbsolutePath()+" -> to FOLDER\n\t"+parsing_system_folder_string+"\n\tand rename as->"+Settings.COMMIT_LIST_FILE+"\n\t["+is_present+"]";
									warning_message_necessity = true;
								}
							}
							else
							{
								warning_message = warning_message + "\n- copy "+frame.getCommitlistFile().getAbsolutePath()+" -> to FOLDER\n\t"+parsing_system_folder_string+"\n\tand rename as->"+Settings.COMMIT_LIST_FILE+"\n\t["+is_present+"]";
								warning_message_necessity = true;
							}
						}
						else
							frame.showMessagePopUp("FILE:\n"+frame.getCommitlistFile().getAbsolutePath()+"\n"+is_present+popup_message, popup_title);
					}
					catch(SecurityException e)
					{
						e.printStackTrace();
						frame.showAlert("UNABLE TO COPY the selected file:\n"+frame.getCommitlistFile().getAbsolutePath()+"\nto the PARSING FOLDER:\n"+parsing_system_folder.getAbsolutePath()+"\nAS->"+Settings.COMMIT_LIST_FILE, "SAVING PROBLEM");
						warning_message = warning_message + "\n- copy "+frame.getCommitlistFile().getAbsolutePath()+" -> to FOLDER\n\t"+parsing_system_folder_string+"\n\tand rename as->"+Settings.COMMIT_LIST_FILE;
					}
					
					//FROM HERE -> SAVE SETTINGS FILES TXT
					//INSIDE parsing_system_folder_string
					//createSettingsFiles
					String setting_is_present = "ATTENTION: some settings files ALREADY EXIST in the PARSING FOLDER!";
					String replace_message ="DO YOU WANT overwrite the files FOUND into PARSING DIRECTORY NOW?";
					String replace_title = "REPLACE FILEs";
					//SETTINGS_GIT.TXT
					File git_settings_to_test = new File(parsing_system_folder_string+Settings.GIT_SETTINGS_FILE);
					//SETTINGS_NICAD.TXT
					File nicad_settings_to_test = new File(parsing_system_folder_string+Settings.NICAD_SETTINGS_FILE);
					
					if(git_settings_to_test.exists()||nicad_settings_to_test.exists())
					{
						create_settings_file = false;
						if(git_settings_to_test.exists())
							setting_is_present = setting_is_present + "\n->"+Settings.GIT_SETTINGS_FILE;
						if(nicad_settings_to_test.exists())
							setting_is_present = setting_is_present + "\n->"+Settings.NICAD_SETTINGS_FILE;
					}

					//CHOICE NEEDED -> OVERWRITE FILES?
					if(!create_settings_file)
						create_settings_file = frame.continueOperationAlert(setting_is_present+"\n\n"+replace_message, replace_title);
					
					if(create_settings_file)
						create_settings_file = settings_to_save.createSettingsFiles();
				}
				else
				{
					frame.showAlert("\nUNABLE TO FIND|CREATE the selected folder:\n"+parsing_system_folder.getAbsolutePath(), "SAVING PROBLEM");
					return false;
				}
			}
			catch(SecurityException e)
			{
				e.printStackTrace();
				frame.showAlert("UNABLE TO FIND|CREATE the selected folder:\n"+parsing_system_folder.getAbsolutePath(), "SAVING PROBLEM");
				return false;
			}

			if(warning_message_necessity)
			{
				frame.showAlert(warning_message, "REMINDER");
			}
			return create_settings_file;
			//SHOW NEW SETTINGS
			/*
			frame.closeForm();
			new Settings_Parser_View(settings_to_save);*/
		}
		else
		{
			frame.showMessage("IMPOSSIBLE TO CREATE SETTINGS->PROBLEMS FOUND");
			return false;
		}
	}
	
	public void showCreatedSettings(Settings_View frame, Settings settings_to_show)
	{
		frame.closeForm();
		new Settings_Parser_View(settings_to_show);
	}
}