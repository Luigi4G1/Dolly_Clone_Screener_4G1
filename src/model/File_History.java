package model;

import java.util.ArrayList;

public class File_History {
	
	public File_History() {
	}

	public ArrayList<File_Version> getVersions() {
		return versions;
	}
	public void setVersions(ArrayList<File_Version> versions) {
		this.versions = versions;
	}
	
	public File_Version getNewest_version() {
		return newest_version;
	}
	public void setNewest_version(File_Version newest_version) {
		this.newest_version = newest_version;
	}
	
	public File_Version getOldest_version() {
		return oldest_version;
	}
	public void setOldest_version(File_Version oldest_version) {
		this.oldest_version = oldest_version;
	}
	
	public ArrayList<String> getFilenames() {
		return filenames;
	}
	public void setFilenames(ArrayList<String> filenames) {
		this.filenames = filenames;
	}

	public void addFilename(String filename) {
		if(this.filenames == null)
			this.filenames = new ArrayList<String>();
		if(!this.filenames.contains(filename))
			this.filenames.add(filename);
	}
	
	public void addVersion(File_Version version) {
		if(this.versions == null)
			this.versions = new ArrayList<File_Version>();
		if(!this.versions.contains(version))
			this.versions.add(version);
	}
	
	public void recap_history()
	{
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println(">>>FILE HISTORY>>>>");
		File_Version curr_version = newest_version;
		int index = 0;
		while(curr_version != null)
		{
			index++;
			System.out.println(">["+index+"]NODE>");
			curr_version.recap_version();
			curr_version = curr_version.getPrevious();
		}
		System.out.println(">>>END RECAP HISTORY>>>>["+index+" NODES-LINKEDLIST]");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}
	
	ArrayList<File_Version> versions = new ArrayList<File_Version>();
	File_Version newest_version = null;
	File_Version oldest_version = null;
	ArrayList<String> filenames = new ArrayList<String>();

}
