package persistence;

import java.io.IOException;
import java.io.Serializable;

public class Link_Clone_Pair implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1818184189775136275L;
	
	public Link_Clone_Pair(int similarity, int number_of_lines, int first_clone, int second_clone) {
		this.similarity = similarity;
		this.number_of_lines = number_of_lines;
		this.first_clone = first_clone;
		this.second_clone = second_clone;
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
	public int getFirst_clone() {
		return first_clone;
	}
	public void setFirst_clone(int first_clone) {
		this.first_clone = first_clone;
	}
	public int getSecond_clone() {
		return second_clone;
	}
	public void setSecond_clone(int second_clone) {
		this.second_clone = second_clone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + first_clone;
		result = prime * result + number_of_lines;
		result = prime * result + second_clone;
		result = prime * result + similarity;
		return result;
	}

	//RETURN TRUE if:
	//-	CLONEPAIR1 == CLONEPAIR2(SAME OBJECT)
	//-	if (CLONEPAIR1.CLONE1 == CLONEPAIR2.CLONE1) && (CLONEPAIR1.CLONE2 == CLONEPAIR2.CLONE2) + ->with SIMILARITY & NUMLINES
	//-	if (CLONEPAIR1.CLONE1 == CLONEPAIR2.CLONE2) && (CLONEPAIR1.CLONE2 == CLONEPAIR2.CLONE1) + ->with SIMILARITY & NUMLINES
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Link_Clone_Pair other = (Link_Clone_Pair) obj;

		if(similarity != other.similarity)
			return false;
		if(number_of_lines != other.number_of_lines)
			return false;

		if (first_clone != other.first_clone)
		{
			if (first_clone != other.second_clone)
				return false;
			else
				return (second_clone == other.first_clone);
		}
		else
		{
			return (second_clone == other.second_clone);
		}
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeInt(similarity);
		out.writeInt(number_of_lines);
		out.writeInt(first_clone);
		out.writeInt(second_clone);

		//SAVER
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		similarity = (int) in.readInt();
		number_of_lines = (int) in.readInt();
		first_clone = (int) in.readInt();
		second_clone = (int) in.readInt();

		//LINKER
	}
	
	private int similarity;
	private int number_of_lines;
	private int first_clone;
	private int second_clone;
}
