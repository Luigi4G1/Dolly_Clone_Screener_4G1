package controller;

import utils.GitUtils4G1;
import view.Download_Sys_Version_View;

public class Download_Sys_Version_Controller {
	
	private static Download_Sys_Version_Controller instance = null;
	
	private Download_Sys_Version_Controller() {}
	
	public static Download_Sys_Version_Controller getInstance()
	{
		if(instance == null)
			instance = new Download_Sys_Version_Controller();
		return instance;
	}
	
	public void downloadSystemVersion(Download_Sys_Version_View frame, GitUtils4G1 repo, String version_commit_id)
	{
		//TEST INPUT
		if(frame == null)
		{
			System.out.println("ATTENTION! GUI NOT SET->NULL");
			return;
		}
		if(repo == null)
		{
			String message = "ATTENTION!\nERROR DURING OPERATION:\nGIT REPO->NULL";
			String title ="ERROR";
			frame.showAlert(message, title);
			return;
		}
		if((version_commit_id == null)||(version_commit_id.trim().isEmpty()))
		{
			String message = "ATTENTION!\nERROR DURING OPERATION:\nVERSION NOT SET";
			String title ="ERROR";
			frame.showAlert(message, title);
			return;
		}

		frame.highlightRepoURITextField(false);

		if(GitUtils4G1.isValidRepoUri(repo.getRepo_path_uri()))
		{
			String save_folder_path = repo.downloadRepoByCommitIDToFolderNoGit(version_commit_id, repo.getRepo_local_path());
			
			if(save_folder_path == null)
			{
				String message = "ATTENTION!\nERROR DURING OPERATION";
				String title ="ERROR";
				frame.showAlert(message, title);
			}
			else
			{
				String message = "DOWNLOAD COMPLETED!\nCHECK FOLDER:\n"+save_folder_path;
				String title ="DOWNLOAD COMPLETED";
				frame.showMessagePopUp(message, title);
			}
		}
		else
		{
			frame.highlightRepoURITextField(true);
			String message = "NOT A VALID URI!";
			String title ="TEST NOT PASSED!";
			frame.showAlert(message, title);
		}
		
	}

}
