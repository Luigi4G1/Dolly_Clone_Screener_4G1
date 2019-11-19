package controller;

import model.Commit_Node;
import persistence.System_Analysis;
import view.Commit_View;

public class Commit_View_Controller {
	
	private static Commit_View_Controller instance = null;
	
	public static Commit_View_Controller getInstance()
	{
		if(instance == null)
			instance = new Commit_View_Controller();
		return instance;
	}
	
	private Commit_View_Controller() {
	}
	
	public void showCommitView(Commit_View frame, Commit_Node commit, System_Analysis analysis_result)
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

}
