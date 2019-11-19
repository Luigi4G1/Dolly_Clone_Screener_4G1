package persistence;

import java.io.IOException;
import java.io.Serializable;

public class Link_Commit_Parent implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4582085353463953900L;

	public Link_Commit_Parent(String commit_id, String parent_id) {
		this.commit_id = commit_id;
		this.parent_id = parent_id;
	}

	public String getCommit_id() {
		return commit_id;
	}
	public void setCommit_id(String commit_id) {
		this.commit_id = commit_id;
	}
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(commit_id);
		out.writeObject(parent_id);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		commit_id = (String) in.readObject();
		parent_id = (String) in.readObject();
	}

	private String commit_id;
	private String parent_id;
}
