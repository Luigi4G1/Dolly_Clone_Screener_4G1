package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import model.Clone;
import model.Commit_Node;
import model.Current_Clone_File_Version;
import model.File_Change;
import model.File_Version;
import persistence.Settings;
import persistence.System_Analysis;
import view.Clone_Evolution_View;
import view.Clone_List_View;
import view.Clone_View;

public class Clone_List_Controller {

private static Clone_List_Controller instance = null;
	
	public static Clone_List_Controller getInstance()
	{
		if(instance == null)
			instance = new Clone_List_Controller();
		return instance;
	}
	
	private Clone_List_Controller()
	{
	}
	
	public void showCloneView(Clone_List_View frame, Clone clone, System_Analysis analysis_result)
	{
		//INPUT ERROR CONTROL
		if(clone == null)
		{
			frame.showAlert("ERROR! NULL VALUE FOUND.\nUNABLE TO SHOW CLONE DATA!","NO DATA");
			return;
		}
		else if(analysis_result == null)
		{
			frame.showAlert("ERROR! NULL VALUE FOUND.\nNO SYSTEM DATA!","NO DATA");
			return;
		}
		else
		{
			frame.closeForm();
			new Clone_View(clone, analysis_result);
		}
	}
	
	public void showCloneBWEvolution(Clone_List_View frame, Clone clone, System_Analysis analysis_result)
	{
		//INPUT ERROR CONTROL
		if(clone == null)
		{
			frame.showAlert("ERROR! NULL VALUE FOUND.\nUNABLE TO SHOW CLONE DATA!","NO DATA");
			return;
		}
		else if(analysis_result == null)
		{
			frame.showAlert("ERROR! NULL VALUE FOUND.\nNO SYSTEM DATA!","NO DATA");
			return;
		}
		else
		{
			String system_saves_folder_name = Settings.SAVED_SYSTEMS_FOLDER+analysis_result.getSettings().getRepo_info().getSystem_name()+File.separatorChar+analysis_result.getSettings().getSystem_version_commit_id()+File.separatorChar+Settings.CLONE_BW_EVOLUTION_FOLDER+File.separatorChar;
			String clone_bw_evolution_filename = system_saves_folder_name+Settings.CLONE_BW_EVOLUTION_FILE+clone.getId_clone()+Settings.SAVE_FILE_EXT;
			System.out.println("-> ->"+clone_bw_evolution_filename);
			
			try
			{
				File clone_bw_evolution_file = new File(clone_bw_evolution_filename);
				if(!clone_bw_evolution_file.exists())
				{
					//frame.showAlert("FILE NOT FOUND.\nNO SYSTEM DATA!","NO DATA");
					
					String message = "CHRONOLOGY FILE NOT FOUND.\n"
							+ "Do you want to CREATE IT?";
					String title = "FILE NOT FOUND";
					boolean continue_op = frame.continueOperationAlert(message, title);
					
					if(continue_op)
					{
						Clone_View_Controller.getInstance().findCloneAuthor(frame, clone, analysis_result);
					}
					return;
				}

				ObjectInputStream in = new ObjectInputStream(new FileInputStream(clone_bw_evolution_file));
				@SuppressWarnings("unchecked")
				ArrayList<Current_Clone_File_Version> clone_backward_evolution = (ArrayList<Current_Clone_File_Version>) in.readObject();
				in.close();

				//RE-LINK serialized objects
				for(Current_Clone_File_Version version : clone_backward_evolution)
				{
					version.setClone(clone);
					File_Version file_version = version.getCurrent_file_version();
					if(file_version != null)
					{
						if(file_version.getDiff() != null)
						{
							if(file_version.getId_commit()!=null)
							{
								Commit_Node commit = analysis_result.getCommit_history_analysis().getCommitByID(file_version.getId_commit());
								if(commit != null)
									file_version.getDiff().setCommit(commit);
							}
						}
					}
				}
				frame.closeForm();
				new Clone_Evolution_View(clone, clone_backward_evolution, analysis_result);
				
			} catch (IOException e) {
				e.printStackTrace();
				frame.showAlert("ERROR!\n"+e.getMessage(),"ERROR");
			} catch (Exception e) {
				e.printStackTrace();
				frame.showAlert("ERROR!\n"+e.getMessage(),"ERROR");
			}
		}
	}
	
	public void findAllClonesAuthors(Clone_List_View frame, System_Analysis analysis_result)
	{
		//INPUT ERROR CONTROL
		if(analysis_result == null)
		{
			frame.showAlert("ERROR! NULL VALUE FOUND.\nNO SYSTEM DATA!","NO DATA");
			return;
		}
		else
		{
			int to_do_clones = analysis_result.getReport_clone_analysis().getClones().size();
			for(Clone clone : analysis_result.getReport_clone_analysis().getClones().values())
			{
				to_do_clones--;
				if(clone.getAuthor() == null)
				{
					try
					{
						File_Change change = CloneAuthorFinderTemplateMethod.findCloneAuthorDiff(clone, analysis_result.getSettings().getRepo_info(), analysis_result.getCommit_history_analysis(), analysis_result.getSettings().getNicad_settings().getVersion_commit());
						if(change == null)
						{
							System.out.println("-CLONE["+clone.getId_clone()+"] AUTHOR NOT FOUND![TO DO:"+to_do_clones+"]");
						}
						else
						{
							change.addClone(clone);
							clone.setChange_modify_clone(change);
							clone.setAuthor(change.getCommit().getAuthor());
							change.getCommit().getAuthor().addClone(clone);
							System.out.println("+CLONE["+clone.getId_clone()+"] AUTHOR FOUND![TO DO:"+to_do_clones+"]");
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					
				}
				else
					System.out.println("=CLONE["+clone.getId_clone()+"] AUTHOR ALREADY FOUND![TO DO:"+to_do_clones+"]");
			}
		}
	}
}
