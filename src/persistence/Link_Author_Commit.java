package persistence;

import java.io.IOException;
import java.io.Serializable;

public class Link_Author_Commit implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8304330142956038464L;

	public Link_Author_Commit(String author_id, String commit_id) {
		this.author_id = author_id;
		this.commit_id = commit_id;
	}

	public String getAuthor_id() {
		return author_id;
	}
	public void setAuthor_id(String author_id) {
		this.author_id = author_id;
	}
	public String getCommit_id() {
		return commit_id;
	}
	public void setCommit_id(String commit_id) {
		this.commit_id = commit_id;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(author_id);
		out.writeObject(commit_id);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		author_id = (String) in.readObject();
		commit_id = (String) in.readObject();
	}

	private String author_id;
	private String commit_id;
}
