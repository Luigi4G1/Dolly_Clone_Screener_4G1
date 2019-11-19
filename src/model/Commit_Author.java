package model;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

public class Commit_Author implements Serializable, Comparable<Commit_Author> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8317939001112361594L;

	public Commit_Author(String id_author, String email_author) {
		this.id_author = id_author;
		this.email_author = email_author;
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
	
	public HashMap<String, Commit_Node> getCommit_done_map() {
		if(this.commit_done_map == null)
			this.commit_done_map = new HashMap<String, Commit_Node>();
		return commit_done_map;
	}

	public void setCommit_done_map(HashMap<String, Commit_Node> commit_done_map) {
		this.commit_done_map = commit_done_map;
	}
	
	public void addCommit_Node(Commit_Node commit_node)
	{
		if(this.commit_done_map == null)
			this.commit_done_map = new HashMap<String, Commit_Node>();
		this.commit_done_map.put(commit_node.getId_commit(), commit_node);
	}
	
	public Commit_Node getCommit_NodeByID(String id_commit)
	{
		if(this.commit_done_map == null)
			return null;
		return this.commit_done_map.get(id_commit);
	}

	public HashMap<Integer, Clone> getCreated_clones() {
		if(this.created_clones == null)
			this.created_clones = new HashMap<Integer, Clone>();
		return created_clones;
	}

	public void setCreated_clones(HashMap<Integer, Clone> created_clones) {
		this.created_clones = created_clones;
	}
	
	public void addClone(Clone clone)
	{
		if(this.created_clones == null)
			this.created_clones = new HashMap<Integer, Clone>();
		this.created_clones.put(new Integer(clone.getId_clone()), clone);
	}

	@Override
	public int compareTo(Commit_Author arg0) {
		int result = this.id_author.compareTo(arg0.getId_author());
		if(result == 0)
		{
			result = this.email_author.compareTo(arg0.getEmail_author());
		}
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email_author == null) ? 0 : email_author.hashCode());
		result = prime * result + ((id_author == null) ? 0 : id_author.hashCode());
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
		Commit_Author other = (Commit_Author) obj;
		if (email_author == null) {
			if (other.email_author != null)
				return false;
		} else if (!email_author.equals(other.email_author))
			return false;
		if (id_author == null) {
			if (other.id_author != null)
				return false;
		} else if (!id_author.equals(other.id_author))
			return false;
		return true;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id_author);
		out.writeObject(email_author);
		//SAVER
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		id_author = (String) in.readObject();
		email_author = (String) in.readObject();
		//LINKER
	}

	private String id_author;
	private String email_author;
	private HashMap<String, Commit_Node> commit_done_map = new HashMap<String, Commit_Node>();
	private HashMap<Integer, Clone> created_clones = new HashMap<Integer, Clone>();
}
