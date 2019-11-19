package model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class File_Change implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2730283886407498399L;

	public File_Change(String initial_filename, String destination_filename) {
		this.initial_filename = initial_filename;
		this.destination_filename = destination_filename;
		this.changes = new ArrayList<Diff_Range_Change>();
	}
	
	public String getInitial_filename() {
		return initial_filename;
	}

	public void setInitial_filename(String initial_filename) {
		this.initial_filename = initial_filename;
	}

	public String getOlder_checksum() {
		return older_checksum;
	}

	public void setOlder_checksum(String older_checksum) {
		this.older_checksum = older_checksum;
	}

	public String getDestination_filename() {
		return destination_filename;
	}

	public void setDestination_filename(String destination_filename) {
		this.destination_filename = destination_filename;
	}

	public String getNewer_checksum() {
		return newer_checksum;
	}

	public void setNewer_checksum(String newer_checksum) {
		this.newer_checksum = newer_checksum;
	}
	
	public ArrayList<Diff_Range_Change> getChanges() {
		if(this.changes == null)
			this.changes = new ArrayList<Diff_Range_Change>();
		return changes;
	}

	public void setChanges(ArrayList<Diff_Range_Change> changes) {
		this.changes = changes;
	}

	public void addFileChange(Diff_Range_Change change)
	{
		if(this.changes == null)
			this.changes = new ArrayList<Diff_Range_Change>();
		this.changes.add(change);
	}
	
	public Commit_Node getCommit() {
		return commit;
	}

	public void setCommit(Commit_Node commit) {
		this.commit = commit;
	}

	public ArrayList<Clone> getAdded_clones() {
		if(this.added_clones == null)
			this.added_clones = new ArrayList<Clone>();
		return added_clones;
	}

	public void setAdded_clones(ArrayList<Clone> added_clones) {
		this.added_clones = added_clones;
	}

	public void addClone(Clone clone)
	{
		if(this.added_clones == null)
			this.added_clones = new ArrayList<Clone>();
		if(!this.added_clones.contains(clone))
			this.added_clones.add(clone);
	}
	
	public void test_console_report()
	{
		System.out.println(">>>>CHANGE>>>>>");
		System.out.println("##INITIAL FILENAME: ");
		System.out.println(this.initial_filename);
		System.out.println("##OLDER CHECKSUM: ");
		System.out.println(this.older_checksum);
		
		System.out.println("##DESTINATION FILENAME: ");
		System.out.println(this.destination_filename);
		System.out.println("##NEWER CHECKSUM: ");
		System.out.println(this.newer_checksum);
		System.out.println("~~DIFFERENCES~~");
		if(this.changes == null)
		{
			System.out.println("NOT SET -> NULL");
		}
		else
		{
			for(int index = 0; index<this.changes.size(); index++)
			{
				this.changes.get(index).console_print_range();
			}
		}
		System.out.println("~~~~~~~~~~~~~~~");
		System.out.println(">>>>>>>>>>>>>>>");
	}
	
	public String changeType()
	{
		if(initial_filename == null)
			return "CREATE";
		else if(destination_filename == null)
			return "DELETE";
		else
		{
			if(!initial_filename.equalsIgnoreCase(destination_filename))
				return "RENAME";
			else
				return "CHANGE";
		}
	}
	
	public String shortDescription()
	{
		if(initial_filename == null)
			return "[/dev/null]->["+destination_filename+"]";
		else if(destination_filename == null)
			return "["+initial_filename+"]->[/dev/null]";
		else
		{
			if(!initial_filename.equalsIgnoreCase(destination_filename))
				return "["+initial_filename+"]->["+destination_filename+"]";
			else
				return "["+initial_filename+"]~>{"+newer_checksum+"}";
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((destination_filename == null) ? 0 : destination_filename.hashCode());
		result = prime * result + ((initial_filename == null) ? 0 : initial_filename.hashCode());
		result = prime * result + ((newer_checksum == null) ? 0 : newer_checksum.hashCode());
		result = prime * result + ((older_checksum == null) ? 0 : older_checksum.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		File_Change other = (File_Change) obj;
		if (destination_filename == null) {
			if (other.destination_filename != null)
				return false;
		} else if (!destination_filename.equals(other.destination_filename))
			return false;
		if (initial_filename == null) {
			if (other.initial_filename != null)
				return false;
		} else if (!initial_filename.equals(other.initial_filename))
			return false;
		if (newer_checksum == null) {
			if (other.newer_checksum != null)
				return false;
		} else if (!newer_checksum.equals(other.newer_checksum))
			return false;
		if (older_checksum == null) {
			if (other.older_checksum != null)
				return false;
		} else if (!older_checksum.equals(other.older_checksum))
			return false;
		return true;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(initial_filename);
		out.writeObject(older_checksum);
		out.writeObject(destination_filename);
		out.writeObject(newer_checksum);

		out.writeObject(changes);
		//SAVER
	}
	
	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		initial_filename = (String) in.readObject();
		older_checksum = (String) in.readObject();
		destination_filename = (String) in.readObject();
		newer_checksum = (String) in.readObject();

		changes = (ArrayList<Diff_Range_Change>) in.readObject();
		//LINKER
	}
	
	private String initial_filename;
	private String older_checksum;
	
	private String destination_filename;
	private String newer_checksum;
	
	private ArrayList<Diff_Range_Change> changes = new ArrayList<Diff_Range_Change>();
	
	private Commit_Node commit = null;
	
	private ArrayList<Clone> added_clones = new ArrayList<Clone>();
}
