package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import builder.File_History_Builder;
import model.Clone;
import model.Clone_File_History;
import model.Commit_Author;
import model.Commit_History;
import model.Current_Clone_File_Version;
import model.File_Change;
import model.File_History;
import persistence.Settings;
import utils.FileUtils4G1;
import utils.GitUtils4G1;

public class CloneAuthorFinderTemplateMethod {
	
	public static Commit_Author findCloneAuthor(Clone clone, GitUtils4G1 repo_info, Commit_History commit_history)
	{
		//INPUT ERROR CONTROL
		if(clone == null)
		{
			System.out.println("CLONE NOT SET -> NULL");
			return null;
		}
		if(repo_info == null)
		{
			System.out.println("GIT REPO NOT SET -> NULL");
			return null;
		}
		if(commit_history == null)
		{
			System.out.println("COMMIT HISTORY NOT SET -> NULL");
			return null;
		}
		//BUILD THE FILE HISTORY using the CLONE FILENAME
		String filepath = FileUtils4G1.filePathNormalize(clone.getFilename().trim());
		File_History file_history = File_History_Builder.file_history_builder(repo_info, commit_history, filepath);
		//CORRECTION -> FOR TESTING FH-BUILDER
		//if((file_history == null)||(file_history.getNewest_version() == null))
		if((file_history == null))
		{
			System.out.println("FILE HISTORY NULL ->"+filepath);
			return null;
		}
		else
		{
			file_history.recap_history();
			//BUILD CLONE HISTORY
			Clone_File_History clone_history = new Clone_File_History(file_history, clone);
			//TEST HARD WIRED
			int index = 1;
			Current_Clone_File_Version current_search = clone_history.getCurrent_clone_file_version();
			boolean isPresent = false;
			//INITIAL TEST -> FIND CLONE inside its file most recent version -> GET MOST RECENT VERSION
			if(current_search == null)
			{
				System.out.println("["+index+" of "+clone_history.getFile_history().getVersions().size()+"]ERROR->CURRENT CLONE FILE VERSION is NULL");
				return null;
			}
			else
			{
				//INITIAL TEST -> FIND CLONE inside its file most recent version
				isPresent = clone_history.isCloneStillSame(current_search);
				if(isPresent)
				{
					System.out.println("["+index+" of "+clone_history.getFile_history().getVersions().size()+"]CURRENT CLONE VERSION -> CLONE CORRECTLY FOUND!");
					Current_Clone_File_Version version_clone_found = current_search;
					current_search = version_clone_found.getPrevious();
					
					while(isPresent)
					{
						if(current_search == null)
						{
							System.out.println("["+index+" of "+clone_history.getFile_history().getVersions().size()+"]CURRENT CLONE VERSION -> NO MORE CLONE VERSIONS FOUND!");
							isPresent = false;
						}
						else
						{
							index++;
							isPresent = clone_history.isCloneStillSame(current_search);
							System.out.println("["+index+" of "+clone_history.getFile_history().getVersions().size()+"]CURRENT CLONE VERSION ->"+isPresent);
							if(isPresent)
							{
								version_clone_found = current_search;
								current_search = version_clone_found.getPrevious();
							}
						}
						//TEST
						/*
						Scanner myObj = new Scanner(System.in);
						System.out.println(">>>PRESS ENTER TO CONTINUE>>>");
						myObj.nextLine();
						*/
					}
					//RETURN THE AUTHOR FOUND
					System.out.println("AUTHOR of the CLONE->"+version_clone_found.getCurrent_file_version().getDiff().getCommit().getAuthor().getId_author());
					return version_clone_found.getCurrent_file_version().getDiff().getCommit().getAuthor();
				}
				else
				{
					System.out.println("["+index+" of "+clone_history.getFile_history().getVersions().size()+"]ERROR -> CLONE NOT FOUND!->check files-clone");
					return null;
				}
			}
		}
	}
	
	public static File_Change findCloneAuthorDiff(Clone clone, GitUtils4G1 repo_info, Commit_History commit_history, String commit_id_version)
	{
		//INPUT ERROR CONTROL
		if(clone == null)
		{
			System.out.println("CLONE NOT SET -> NULL");
			return null;
		}
		if(repo_info == null)
		{
			System.out.println("GIT REPO NOT SET -> NULL");
			return null;
		}
		if(commit_history == null)
		{
			System.out.println("COMMIT HISTORY NOT SET -> NULL");
			return null;
		}
		//BUILD THE FILE HISTORY using the CLONE FILENAME
		String filepath = FileUtils4G1.filePathNormalize(clone.getFilename().trim());
		File_History file_history = File_History_Builder.file_history_builder(repo_info, commit_history, filepath);
		//CORRECTION -> FOR TESTING FH-BUILDER -> NewestVersion NEEDED by Clone_File_History() CONSTRUCTOR
		if((file_history == null)||(file_history.getNewest_version() == null))
		{
			System.out.println("FILE HISTORY NULL ->"+filepath);
			return null;
		}
		else
		{
			file_history.recap_history();
			//BUILD CLONE HISTORY
			Clone_File_History clone_history = new Clone_File_History(file_history, clone);
			//TEST HARD WIRED
			int index = 1;
			Current_Clone_File_Version current_search = clone_history.getCurrent_clone_file_version();
			boolean isPresent = false;
			//INITIAL TEST -> FIND CLONE inside its file most recent version -> GET MOST RECENT VERSION
			if(current_search == null)
			{
				System.out.println("["+index+" of "+clone_history.getFile_history().getVersions().size()+"]ERROR->CURRENT CLONE FILE VERSION is NULL");
				return null;
			}
			else
			{
				//INITIAL TEST -> FIND CLONE inside its file most recent version
				isPresent = clone_history.isCloneStillSame(current_search);
				if(isPresent)
				{
					//CLONE CHRONOLOGY
					ArrayList<Current_Clone_File_Version> clone_backward_evolution = new ArrayList<Current_Clone_File_Version>();
					clone_backward_evolution.add(current_search);
					
					System.out.println("["+index+" of "+clone_history.getFile_history().getVersions().size()+"]CURRENT CLONE VERSION -> CLONE CORRECTLY FOUND!");
					Current_Clone_File_Version version_clone_found = current_search;
					current_search = version_clone_found.getPrevious();
					
					while(isPresent)
					{
						if(current_search == null)
						{
							System.out.println("["+index+" of "+clone_history.getFile_history().getVersions().size()+"]CURRENT CLONE VERSION -> NO MORE CLONE VERSIONS FOUND!");
							isPresent = false;
						}
						else
						{
							//CLONE CHRONOLOGY
							clone_backward_evolution.add(current_search);
							
							index++;
							isPresent = clone_history.isCloneStillSame(current_search);
							System.out.println("["+index+" of "+clone_history.getFile_history().getVersions().size()+"]CURRENT CLONE VERSION ->"+isPresent);
							if(isPresent)
							{
								version_clone_found = current_search;
								current_search = version_clone_found.getPrevious();
							}
						}
						//TEST
						/*
						Scanner myObj = new Scanner(System.in);
						System.out.println(">>>PRESS ENTER TO CONTINUE>>>");
						myObj.nextLine();
						*/
					}
					//CLONE CHRONOLOGY
					//SAVE
					boolean saved_evolution = saveCloneBackwardEvolution(clone, repo_info, commit_id_version, clone_backward_evolution);
					String system_saves_folder_name = Settings.SAVED_SYSTEMS_FOLDER+repo_info.getSystem_name()+File.separatorChar+commit_id_version+File.separatorChar+Settings.CLONE_BW_EVOLUTION_FOLDER+File.separatorChar;
					String clone_bw_evolution_filename = system_saves_folder_name+Settings.CLONE_BW_EVOLUTION_FILE+clone.getId_clone()+Settings.SAVE_FILE_EXT;
					if(saved_evolution)
						System.out.println("CLONE ["+clone.getId_clone()+"] EVOLUTION SAVED->"+clone_bw_evolution_filename);
					else
						System.out.println("UNABLE TO SAVE CLONE ["+clone.getId_clone()+"] EVOLUTION->"+clone_bw_evolution_filename);

					//RETURN THE AUTHOR FOUND
					System.out.println("AUTHOR of the CLONE->"+version_clone_found.getCurrent_file_version().getDiff().getCommit().getAuthor().getId_author());
					return version_clone_found.getCurrent_file_version().getDiff();
				}
				else
				{
					System.out.println("["+index+" of "+clone_history.getFile_history().getVersions().size()+"]ERROR -> CLONE NOT FOUND!->check files-clone");
					return null;
				}
			}
		}
	}
	
	public static boolean saveCloneBackwardEvolution(Clone clone, GitUtils4G1 repo_info, String commit_id_version, ArrayList<Current_Clone_File_Version> clone_backward_evolution) {

		String system_saves_folder_name = Settings.SAVED_SYSTEMS_FOLDER+repo_info.getSystem_name()+File.separatorChar+commit_id_version+File.separatorChar+Settings.CLONE_BW_EVOLUTION_FOLDER+File.separatorChar;
		System.out.println("->"+system_saves_folder_name);
		String clone_bw_evolution_filename = system_saves_folder_name+Settings.CLONE_BW_EVOLUTION_FILE+clone.getId_clone()+Settings.SAVE_FILE_EXT;
		System.out.println("-> ->"+clone_bw_evolution_filename);

		try
		{
			File system_saves_folder = new File(system_saves_folder_name);
			if(!system_saves_folder.exists())
				system_saves_folder.mkdirs();
			
			File clone_bw_evolution_file = new File(clone_bw_evolution_filename);
			if(clone_bw_evolution_file.exists())
				clone_bw_evolution_file.delete();
			
			clone_bw_evolution_file.createNewFile();
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(clone_bw_evolution_file));
			out.writeObject(clone_backward_evolution);
			out.close();
			return true;
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
