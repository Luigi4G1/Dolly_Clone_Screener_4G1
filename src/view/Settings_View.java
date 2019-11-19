package view;

import java.io.File;

public interface Settings_View {
	
	public void showMessage(String message);
	public void closeForm();
	
	public void showMessagePopUp(String message, String title);
	public void showAlert(String message, String title);
	public boolean continueOperationAlert(String message, String title);
	
	public void highlightRepoURITextField(boolean lightOn);
	
	public File getCloneClassesFile();
	public void setCloneClassesFile(File cloneClassesFile);
	
	public File getCloneLinesFile();
	public void setCloneLinesFile(File cloneLinesFile);
	
	public File getClonePairsFile();
	public void setClonePairsFile(File clonePairsFile);
	
	public void setCommitlistFile(File commitlistFile);
	public File getCommitlistFile();

}
