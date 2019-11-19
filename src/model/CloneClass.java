package model;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

public class CloneClass implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8735977895971312155L;

	public CloneClass(int id_class, int clone_counter, int similarity, int number_of_lines) {
		this.id_class = id_class;
		this.clone_counter = clone_counter;
		this.similarity = similarity;
		this.number_of_lines = number_of_lines;
		this.clones = new HashMap<Integer,Clone>();
	}
	
	public int getId_class() {
		return id_class;
	}

	public void setId_class(int id_class) {
		this.id_class = id_class;
	}

	public int getClone_counter() {
		return clone_counter;
	}

	public void setClone_counter(int clone_counter) {
		this.clone_counter = clone_counter;
	}

	public int getSimilarity() {
		return similarity;
	}

	public void setSimilarity(int similarity) {
		this.similarity = similarity;
	}

	public int getNumber_of_lines() {
		return number_of_lines;
	}

	public void setNumber_of_lines(int number_of_lines) {
		this.number_of_lines = number_of_lines;
	}

	public HashMap<Integer, Clone> getClones() {
		if(this.clones == null)
			this.clones = new HashMap<Integer,Clone>();
		return clones;
	}

	public void setClones(HashMap<Integer, Clone> clones) {
		this.clones = clones;
	}

	public void addClone(Clone clone)
	{
		if(this.clones == null)
			this.clones = new HashMap<Integer,Clone>();
		this.clones.put(new Integer(clone.getId_clone()), clone);
	}
	
	@Override
	public String toString() {
		return "CloneClass [id_class=" + id_class + ", clone_counter=" + clone_counter + ", similarity=" + similarity
				+ ", number_of_lines=" + number_of_lines + ", clones=" + clones.size() + "]";
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeInt(id_class);
		out.writeInt(clone_counter);
		out.writeInt(similarity);
		out.writeInt(number_of_lines);

		//SAVER
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		id_class = (int) in.readInt();
		clone_counter = (int) in.readInt();
		similarity = (int) in.readInt();
		number_of_lines = (int) in.readInt();

		//LINKER
	}

	private int id_class;
	private int clone_counter;
	private int similarity;
	private int number_of_lines;
	
	private HashMap<Integer,Clone> clones;

}
