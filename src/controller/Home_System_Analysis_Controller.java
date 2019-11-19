package controller;

import persistence.System_Analysis;
import view.Author_List_View;
import view.Clone_List_View;
import view.Commit_List_Report_View;
import view.Home_System_Analysis_View;

public class Home_System_Analysis_Controller {
	
private static Home_System_Analysis_Controller instance = null;
	
	public static Home_System_Analysis_Controller getInstance()
	{
		if(instance == null)
			instance = new Home_System_Analysis_Controller();
		return instance;
	}
	
	public void goToCommitsList(Home_System_Analysis_View frame, System_Analysis analysis_result)
	{
		if((analysis_result == null)||(analysis_result.getCommit_history_analysis() == null)||(analysis_result.getCommit_history_analysis().getCommit_nodes() == null))
		{
			frame.enableCommitHistoryButton(false);
			frame.showAlert("NO COMMIT DATA AVAILABLE", "ATTENTION");
		}
		else
		{
			frame.closeForm();
			new Commit_List_Report_View(analysis_result);
		}
	}
	
	public void goToClonesList(Home_System_Analysis_View frame, System_Analysis analysis_result)
	{
		if((analysis_result.getReport_clone_analysis() == null)||(analysis_result.getReport_clone_analysis().getClones() == null))
		{
			frame.enableCloneNiCadReportButton(false);
			frame.showAlert("NO CLONE DATA AVAILABLE", "ATTENTION");
		}
		else
		{
			frame.closeForm();
			//NEXT -> CLONES LIST VIEW
			new Clone_List_View(analysis_result);
		}
	}
	
	public void goToAuthorsList(Home_System_Analysis_View frame, System_Analysis analysis_result)
	{
		if((analysis_result == null)||(analysis_result.getCommit_history_analysis() == null)||(analysis_result.getCommit_history_analysis().getCommit_authors() == null))
		{
			frame.enabledAuthorsButton(false);
			frame.showAlert("NO AUTHOR DATA AVAILABLE", "ATTENTION");
		}
		else
		{
			frame.closeForm();
			//NEXT -> AUTHORS LIST VIEW
			new Author_List_View(analysis_result);
		}
	}
}
