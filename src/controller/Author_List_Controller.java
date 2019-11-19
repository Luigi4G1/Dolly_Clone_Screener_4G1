package controller;

import model.Commit_Author;
import persistence.System_Analysis;
import view.Author_List_View;
import view.Author_View;

public class Author_List_Controller {
	
	private static Author_List_Controller instance = null;
	
	public static Author_List_Controller getInstance()
	{
		if(instance == null)
			instance = new Author_List_Controller();
		return instance;
	}
	
	private Author_List_Controller() {
	}
	
	public void showAuthorView(Author_List_View frame, Commit_Author author, System_Analysis analysis_result)
	{
		//INPUT ERROR CONTROL
		if(author == null)
		{
			frame.showAlert("ERROR! NULL VALUE FOUND.\nUNABLE TO SHOW AUTHOR DATA!","NO DATA");
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
			new Author_View(author, analysis_result);
		}
	}
	/*
	public void showCommitView(Author_List_View frame, Commit_Node commit, System_Analysis analysis_result)
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

	public void showCloneView(Author_List_View frame, Clone clone, System_Analysis analysis_result)
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
	}*/

}
