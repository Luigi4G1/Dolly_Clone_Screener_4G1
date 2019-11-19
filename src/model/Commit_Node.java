package model;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class Commit_Node implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8883113251937338206L;

	public Commit_Node(String id_commit, Date data_commit, String description, ArrayList<File_Change> file_changes) {
		this.id_commit = id_commit;
		this.data_commit = data_commit;
		this.description = description;
		this.file_changes = file_changes;
	}

	public String getId_commit() {
		return id_commit;
	}

	public void setId_commit(String id_commit) {
		this.id_commit = id_commit;
	}

	public Commit_Author getAuthor() {
		return author;
	}

	public void setAuthor(Commit_Author author) {
		this.author = author;
	}

	public Date getData_commit() {
		return data_commit;
	}

	public void setData_commit(Date data_commit) {
		this.data_commit = data_commit;
	}

	public ArrayList<Commit_Node> getParents() {
		if(this.parents == null)
			this.parents = new ArrayList<Commit_Node>();
		return parents;
	}

	public void setParents(ArrayList<Commit_Node> parents) {
		this.parents = parents;
	}

	public void addParent(Commit_Node parent) {
		if(this.parents == null)
			this.parents = new ArrayList<Commit_Node>();
		if(!this.parents.contains(parent))
			this.parents.add(parent);
	}
	
	public ArrayList<Commit_Node> getChilds() {
		if(this.childs == null)
			this.childs = new ArrayList<Commit_Node>();
		return childs;
	}

	public void setChilds(ArrayList<Commit_Node> childs) {
		this.childs = childs;
	}

	public void addChild(Commit_Node child) {
		if(this.childs == null)
			this.childs = new ArrayList<Commit_Node>();
		if(!this.childs.contains(child))
			this.childs.add(child);
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<File_Change> getFile_changes() {
		if(this.file_changes == null)
			this.file_changes = new ArrayList<File_Change>();
		return file_changes;
	}

	public void setFile_changes(ArrayList<File_Change> file_changes) {
		this.file_changes = file_changes;
	}
	
	public void addFile_change(File_Change file_change) {
		if(this.file_changes == null)
			this.file_changes = new ArrayList<File_Change>();
		this.file_changes.add(file_change);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id_commit == null) ? 0 : id_commit.hashCode());
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
		Commit_Node other = (Commit_Node) obj;
		if (id_commit == null) {
			if (other.id_commit != null)
				return false;
		} else if (!id_commit.equals(other.id_commit))
			return false;
		return true;
	}

	public static Comparator<Commit_Node> Commit_DateASC_Comparator = new Comparator<Commit_Node>()
	{
		public int compare(Commit_Node s1, Commit_Node s2)
		{
			//ascending order
			return s1.getData_commit().compareTo(s2.getData_commit());
	    }
	};
	
	public static Comparator<Commit_Node> Commit_DateDESC_Comparator = new Comparator<Commit_Node>()
	{
		public int compare(Commit_Node s1, Commit_Node s2)
		{
			//descending order
			return s2.getData_commit().compareTo(s1.getData_commit());
	    }
	};
	
	public String shortDescription()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
        String strDate = dateFormat.format(this.data_commit);
        builder.append(strDate);
        builder.append("]{AUTHOR:"+this.getAuthor().getId_author()+"}-#ID->["+this.id_commit+"]");
		return builder.toString();
	}
	
	public String dateString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
        String strDate = dateFormat.format(this.data_commit);
        builder.append(strDate);
        builder.append("]");
		return builder.toString();
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id_commit);
		out.writeObject(data_commit);
		
		out.writeObject(description);
		
		//NEW->It's done in the COMMIT_SAVE -> DIFF_SAVE
		//out.writeObject(file_changes);
		//SAVER
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		id_commit = (String) in.readObject();
		data_commit = (Date) in.readObject();
		
		description = (String) in.readObject();
		
		//NEW->It's done in the COMMIT_SAVE -> DIFF_SAVE
		//file_changes = (ArrayList<File_Change>) in.readObject();
		//LINKER
	}
	
	private String id_commit;
	private Commit_Author author = null;
	private Date data_commit;
	private ArrayList<Commit_Node> parents = new ArrayList<Commit_Node>();
	private String description;
	
	private ArrayList<File_Change> file_changes = new ArrayList<File_Change>();
	
	private ArrayList<Commit_Node> childs = new ArrayList<Commit_Node>();

}
