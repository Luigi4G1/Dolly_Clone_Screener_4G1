package persistence;

import java.io.IOException;
import java.io.Serializable;

public class Link_Clone_Class implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8458473967738372977L;

	public Link_Clone_Class(int clone_id, int class_id) {
		this.clone_id = clone_id;
		this.class_id = class_id;
	}

	public int getClone_id() {
		return clone_id;
	}
	public void setClone_id(int clone_id) {
		this.clone_id = clone_id;
	}
	public int getClass_id() {
		return class_id;
	}
	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}
	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeInt(clone_id);
		out.writeInt(class_id);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		clone_id = in.readInt();
		class_id = in.readInt();
	}

	private int clone_id;
	private int class_id;
}
