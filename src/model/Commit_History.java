package model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Commit_History implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -474185669015771238L;

	public Commit_History(String system) {
		this.system = system;
		this.commit_nodes = new HashMap<String, Commit_Node>();
		this.commit_authors = new HashMap<String, Commit_Author>();
	}
	
	public Commit_History(String system, HashMap<String, Commit_Node> commit_nodes, HashMap<String, Commit_Author> commit_authors) {
		this.system = system;
		this.commit_nodes = commit_nodes;
		this.commit_authors = commit_authors;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public HashMap<String, Commit_Node> getCommit_nodes() {
		return commit_nodes;
	}

	public void setCommit_nodes(HashMap<String, Commit_Node> commit_nodes) {
		this.commit_nodes = commit_nodes;
	}

	public HashMap<String, Commit_Author> getCommit_authors() {
		return commit_authors;
	}

	public void setCommit_authors(HashMap<String, Commit_Author> commit_authors) {
		this.commit_authors = commit_authors;
	}

	public void addAuthor(Commit_Author author)
	{
		if(this.commit_authors == null)
			this.commit_authors = new HashMap<String, Commit_Author>();
		this.commit_authors.put(author.getId_author(), author);
		//COULD ADD HIS COMMITS TOO?
	}
	
	public void addCommit(Commit_Node commit)
	{
		if(this.commit_nodes == null)
			this.commit_nodes = new HashMap<String, Commit_Node>();
		this.commit_nodes.put(commit.getId_commit(), commit);
		//COULD ADD ITS AUTHOR TOO?
	}
	
	public Commit_Author getAuthorByID(String id_author)
	{
		if(this.commit_authors == null)
			return null;
		return commit_authors.get(id_author);
	}
	
	public Commit_Node getCommitByID(String id_commit)
	{
		if(this.commit_nodes == null)
			return null;
		return commit_nodes.get(id_commit);
	}
	
	public ArrayList<Commit_Node> filterCommits_ByAuthorID(String author_id)
	{
		ArrayList<Commit_Node> toReturn = new ArrayList<Commit_Node>();
		if((author_id != null)&&(!author_id.isEmpty()))
		{
			if(this.commit_nodes == null)
				return toReturn;
			for(Map.Entry<String, Commit_Node> entry : this.commit_nodes.entrySet())
			{
				Commit_Node commit = entry.getValue();
				Commit_Author author = commit.getAuthor();
				if(author != null)
				{
					if(author.getId_author().equalsIgnoreCase(author_id))
						toReturn.add(commit);
				}
			}
		}
		return toReturn;
	}
	
	public ArrayList<Commit_Node> getCommits_ByDestinationFilename(String dest_filename)
	{
		ArrayList<Commit_Node> toReturn = new ArrayList<Commit_Node>();
		if(this.commit_nodes == null)
			return toReturn;
		for(Map.Entry<String, Commit_Node> entry : this.commit_nodes.entrySet())
		{
			Commit_Node commit = entry.getValue();
			ArrayList<File_Change> changes = commit.getFile_changes();
			boolean found = false;
			for(File_Change file_change : changes)
			{
				if((file_change.getDestination_filename() != null)&&(file_change.getDestination_filename().equalsIgnoreCase(dest_filename)))
				{
					found = true;
					break;
				}
			}
			if(found)
				toReturn.add(commit);
		}
		return toReturn;
	}
	
	public ArrayList<Commit_Node> getCommits_NotAfterDate(Date date)
	{
		ArrayList<Commit_Node> toReturn = new ArrayList<Commit_Node>();
		if(this.commit_nodes == null)
			return toReturn;
		for(Map.Entry<String, Commit_Node> entry : this.commit_nodes.entrySet())
		{
			Commit_Node commit = entry.getValue();
			if(!commit.getData_commit().after(date))
				toReturn.add(commit);
		}
		return toReturn;
	}
	
	//GET the MOST RECENT COMMIT
	public Commit_Node getLastCommit()
	{
		if(this.commit_nodes == null)
			return null;
		ArrayList<Commit_Node> commits_to_test_list = new ArrayList<Commit_Node>(commit_nodes.values());
		Collections.sort(commits_to_test_list, Commit_Node.Commit_DateDESC_Comparator);
		return commits_to_test_list.get(0);
	}
	
	//GET the LEAST RECENT COMMIT
	public Commit_Node getOldestCommit()
	{
		if(this.commit_nodes == null)
			return null;
		ArrayList<Commit_Node> commits_to_test_list = new ArrayList<Commit_Node>(commit_nodes.values());
		Collections.sort(commits_to_test_list, Commit_Node.Commit_DateASC_Comparator);
		return commits_to_test_list.get(0);
	}

	//GET the MOST RECENT (in relation to the COMMIT of the File_Change argument) COMMIT with the same:
	//->DESTINATION_FILENAME
	//->NEWER_CHCKSUM
	public File_Change getFileChangeAnchestor(File_Change file_change)
	{
		if(file_change == null)
			return null;
		if(file_change.getCommit() == null)
			return null;
		if(file_change.getCommit().getData_commit() == null)
			return null;
		if(file_change.getInitial_filename() == null)
			return null;
		
		String previousFilename = file_change.getInitial_filename();
		String previousChecksum = file_change.getOlder_checksum();
		Date diffDate = file_change.getCommit().getData_commit();
		File_Change toReturn = null;
		if(this.commit_nodes == null)
			return toReturn;
		//DESC arraylist
		ArrayList<Commit_Node> commits_to_test_list = new ArrayList<Commit_Node>(commit_nodes.values());
		Collections.sort(commits_to_test_list, Commit_Node.Commit_DateDESC_Comparator);
		for(Commit_Node commit_to_test : commits_to_test_list)
		{
			if(!commit_to_test.getData_commit().after(diffDate))
			{
				ArrayList<File_Change> changes = commit_to_test.getFile_changes();
				for(File_Change file_change_to_test : changes)
				{
					//###########################3
					if((file_change_to_test.getDestination_filename() != null)&&(file_change_to_test.getDestination_filename().equalsIgnoreCase(previousFilename)))
					{
						//NOTE:
						//FIX CHECKSUM in the BUILDER
						if((file_change_to_test.getNewer_checksum() != null)&&(file_change_to_test.getNewer_checksum().equalsIgnoreCase(previousChecksum)))
						{
							toReturn = file_change_to_test;
							break;
						}
					}
					//###########################3
				}
			}
		}
		return toReturn;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(system);
		out.writeObject(commit_nodes);
		out.writeObject(commit_authors);
		//SAVER
	}

	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		system = (String) in.readObject();
		commit_nodes = (HashMap<String, Commit_Node>) in.readObject();
		commit_authors = (HashMap<String, Commit_Author>) in.readObject();
		//LINKER
	}

	private String system;
	private HashMap<String, Commit_Node> commit_nodes;
	private HashMap<String, Commit_Author> commit_authors;
}
