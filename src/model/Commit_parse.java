package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class Commit_parse {

	
	public Commit_parse(String id_commit, String id_author, String email_author, Date data_commit,
			ArrayList<String> id_parents, String description) {
		this.id_commit = id_commit;
		this.id_author = id_author;
		this.email_author = email_author;
		this.data_commit = data_commit;
		this.id_parents = id_parents;
		this.description = description;
		this.file_changes = new ArrayList<File_Change>();
	}

	public String getId_commit() {
		return id_commit;
	}

	public void setId_commit(String id_commit) {
		this.id_commit = id_commit;
	}
	
	public String getId_author() {
		return id_author;
	}

	public void setId_author(String id_author) {
		this.id_author = id_author;
	}

	public String getEmail_author() {
		return email_author;
	}

	public void setEmail_author(String email_author) {
		this.email_author = email_author;
	}

	public Date getData_commit() {
		return data_commit;
	}

	public void setData_commit(Date data_commit) {
		this.data_commit = data_commit;
	}

	public ArrayList<String> getId_parents() {
		return id_parents;
	}

	public void setId_parents(ArrayList<String> id_parents) {
		this.id_parents = id_parents;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<File_Change> getFile_changes() {
		return file_changes;
	}

	public void setFile_changes(ArrayList<File_Change> file_changes) {
		this.file_changes = file_changes;
	}

	public void addFileChange(File_Change file_change)
	{
		if(this.file_changes == null)
			this.file_changes = new ArrayList<File_Change>();
		this.file_changes.add(file_change);
	}
	
	public void test_console_report()
	{
		System.out.println("####COMMIT####");
		System.out.println("#ID: "+this.id_commit);
		System.out.println("#AUTHOR: "+this.id_author);
		System.out.println("#EMAIL: "+this.email_author);
		System.out.println("#DATE: "+this.data_commit);
		System.out.println("#PARENTS: ");
		if(this.id_parents == null)
		{
			System.out.println("NOT SET -> NULL");
		}
		else
		{
			for(int index = 0; index<this.id_parents.size(); index++)
			{
				System.out.println("#"+(index+1)+"): "+this.id_parents.get(index));
			}
		}
		System.out.println("#DESCRIPTION: "+this.description);
		if(this.file_changes != null)
		{
			for(int index = 0; index<this.file_changes.size(); index++)
			{
				this.file_changes.get(index).test_console_report();
			}
		}
		System.out.println("##############");
	}
	
	public static Comparator<Commit_parse> Commit_parse_DateASC_Comparator = new Comparator<Commit_parse>()
	{
		public int compare(Commit_parse s1, Commit_parse s2)
		{
			//ascending order
			return s1.getData_commit().compareTo(s2.getData_commit());
	    }
	};
	
	public static Comparator<Commit_parse> Commit_parse_DateDESC_Comparator = new Comparator<Commit_parse>()
	{
		public int compare(Commit_parse s1, Commit_parse s2)
		{
			//descending order
			return s2.getData_commit().compareTo(s1.getData_commit());
	    }
	};

	private String id_commit;
	private String id_author;
	private String email_author;
	private Date data_commit;
	private ArrayList<String> id_parents = new ArrayList<String>();
	private String description;
	
	private ArrayList<File_Change> file_changes = new ArrayList<File_Change>();
}
