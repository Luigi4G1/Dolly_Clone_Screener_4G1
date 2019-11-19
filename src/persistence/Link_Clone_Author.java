package persistence;

import java.io.IOException;
import java.io.Serializable;

public class Link_Clone_Author implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8497420638582868319L;

	public Link_Clone_Author(int clone_id, String author_id) {
		this.clone_id = clone_id;
		this.author_id = author_id;
	}

	public int getClone_id() {
		return clone_id;
	}
	public void setClone_id(int clone_id) {
		this.clone_id = clone_id;
	}
	public String getAuthor_id() {
		return author_id;
	}

	public void setAuthor_id(String author_id) {
		this.author_id = author_id;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeInt(clone_id);
		out.writeObject(author_id);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		clone_id = in.readInt();
		author_id = (String) in.readObject();
	}

	private int clone_id;
	private String author_id;
}
