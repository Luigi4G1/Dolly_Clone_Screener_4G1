package controller;

import model.Clone;
import model.File_Change;
import persistence.System_Analysis;
import view.Clone_List_View;
import view.Clone_View;

public class Clone_View_Controller {
	
	private static Clone_View_Controller instance = null;
	
	public static Clone_View_Controller getInstance()
	{
		if(instance == null)
			instance = new Clone_View_Controller();
		return instance;
	}
	
	private Clone_View_Controller() {
	}
	
	public void findCloneAuthor(Clone_View frame, Clone clone, System_Analysis analysis_result)
	{
		//INPUT ERROR CONTROL
		if(clone == null)
		{
			frame.showAlert("ERROR! NULL VALUE FOUND.\nUNABLE TO START SEARCHING!","NO DATA");
			return;
		}
		else if(analysis_result == null)
		{
			frame.showAlert("ERROR! NULL VALUE FOUND.\nNO SYSTEM DATA!","NO DATA");
			return;
		}
		else
		{
			//FIND AUTHOR
			//Commit_Author author_found = CloneAuthorFinderTemplateMethod.findCloneAuthor(clone, analysis_result.getSettings().getRepo_info(), analysis_result.getCommit_history_analysis());
			//FIND DIFF & AUTHOR
			File_Change change = CloneAuthorFinderTemplateMethod.findCloneAuthorDiff(clone, analysis_result.getSettings().getRepo_info(), analysis_result.getCommit_history_analysis(), analysis_result.getSettings().getNicad_settings().getVersion_commit());
			if(change == null)
			{
				frame.showAlert("UNABLE TO FIND AUTHOR!", "SEARCH ERROR");
			}
			else
			{
				change.addClone(clone);
				clone.setChange_modify_clone(change);
				clone.setAuthor(change.getCommit().getAuthor());
				change.getCommit().getAuthor().addClone(clone);
				frame.closeForm();
				new Clone_View(clone, analysis_result);
			}
		}
	}
	
	public void findCloneAuthor(Clone_List_View frame, Clone clone, System_Analysis analysis_result)
	{
		//INPUT ERROR CONTROL
		if(clone == null)
		{
			frame.showAlert("ERROR! NULL VALUE FOUND.\nUNABLE TO START SEARCHING!","NO DATA");
			return;
		}
		else if(analysis_result == null)
		{
			frame.showAlert("ERROR! NULL VALUE FOUND.\nNO SYSTEM DATA!","NO DATA");
			return;
		}
		else
		{
			//FIND AUTHOR
			//Commit_Author author_found = CloneAuthorFinderTemplateMethod.findCloneAuthor(clone, analysis_result.getSettings().getRepo_info(), analysis_result.getCommit_history_analysis());
			//FIND DIFF & AUTHOR
			File_Change change = CloneAuthorFinderTemplateMethod.findCloneAuthorDiff(clone, analysis_result.getSettings().getRepo_info(), analysis_result.getCommit_history_analysis(), analysis_result.getSettings().getNicad_settings().getVersion_commit());
			if(change == null)
			{
				frame.showAlert("UNABLE TO FIND AUTHOR!", "SEARCH ERROR");
			}
			else
			{
				change.addClone(clone);
				clone.setChange_modify_clone(change);
				clone.setAuthor(change.getCommit().getAuthor());
				change.getCommit().getAuthor().addClone(clone);
				frame.closeForm();
				new Clone_View(clone, analysis_result);
			}
		}
	}

}
