package controller;

import model.Clone;
import model.Commit_Node;
import persistence.System_Analysis;
import view.Author_View;
import view.Clone_View;
import view.Commit_View;

public class Author_View_Controller {
	
	private static Author_View_Controller instance = null;
	
	public static Author_View_Controller getInstance()
	{
		if(instance == null)
			instance = new Author_View_Controller();
		return instance;
	}
	
	private Author_View_Controller() {
	}
	
	public void showCommitView(Author_View frame, Commit_Node commit, System_Analysis analysis_result)
	{
		//INPUT ERROR CONTROL
		if(commit == null)
		{
			frame.showAlert("ERROR! NULL VALUE FOUND.\nUNABLE TO SHOW COMMIT DATA!","NO DATA");
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
			new Commit_View(commit, analysis_result);
		}
	}
	
	public void showCloneView(Author_View frame, Clone clone, System_Analysis analysis_result)
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

}
