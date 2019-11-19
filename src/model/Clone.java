package model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Clone implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6482291173843139270L;

	public Clone()
	{
		this.lines = new HashMap<Integer,String>();
		this.clone_class = null;
		this.author = null;
	}

	public Clone(int id_clone, String filename, int start_line, int end_line) {
		this.id_clone = id_clone;
		this.filename = filename;
		this.start_line = start_line;
		this.end_line = end_line;
		this.lines = new HashMap<Integer,String>();
		this.clone_class = null;
		this.author = null;
	}

	public int getId_clone() {
		return id_clone;
	}
	public void setId_clone(int id_clone) {
		this.id_clone = id_clone;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public int getStart_line() {
		return start_line;
	}
	public void setStart_line(int start_line) {
		this.start_line = start_line;
	}
	public int getEnd_line() {
		return end_line;
	}
	public void setEnd_line(int end_line) {
		this.end_line = end_line;
	}
	public HashMap<Integer, String> getLines() {
		if(this.lines == null)
			this.lines = new HashMap<Integer,String>();
		return lines;
	}
	public void setLines(HashMap<Integer, String> lines) {
		this.lines = lines;
	}
	public CloneClass getClone_class() {
		return clone_class;
	}
	public void setClone_class(CloneClass clone_class) {
		this.clone_class = clone_class;
	}

	public void addLine(int line_number, String line)
	{
		if(this.lines == null)
			this.lines = new HashMap<Integer,String>();
		this.lines.put(new Integer(line_number), line);
	}

	public Commit_Author getAuthor() {
		return author;
	}

	public void setAuthor(Commit_Author author) {
		this.author = author;
	}

	public File_Change getChange_modify_clone() {
		return change_modify_clone;
	}

	public void setChange_modify_clone(File_Change change_modify_clone) {
		this.change_modify_clone = change_modify_clone;
	}

	public ArrayList<ClonePair> getPairs() {
		if(this.pairs == null)
			this.pairs = new ArrayList<ClonePair>();
		return pairs;
	}

	public void setPairs(ArrayList<ClonePair> pairs) {
		this.pairs = pairs;
	}
	
	public void addPair(ClonePair pair) {
		if(this.pairs == null)
			this.pairs = new ArrayList<ClonePair>();
		if(!this.pairs.contains(pair))
			this.pairs.add(pair);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id_clone;
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
		Clone other = (Clone) obj;
		if (id_clone != other.id_clone)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Clone [id_clone=" + id_clone + ", filename=" + filename + ", start_line=" + start_line + ", end_line="
				+ end_line + ", lines=" + lines.size() + ", clone_class=" + clone_class + "]";
	}

	public String shortDescription()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("ID["+this.id_clone+"]-");
		builder.append("[LINES("+this.getLines().size()+"):"+this.getStart_line()+"->"+this.getEnd_line()+"]");
		builder.append("-{FILE:"+this.getFilename()+"}");
		builder.append("-|CLASS:"+this.getClone_class().getId_class()+"|");
		builder.append("-/AUTHOR:->");
		if(this.getAuthor()==null)
			builder.append("UNKNOWN/");
		else
			builder.append(this.getAuthor().getId_author()+"/");
		return builder.toString();
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeInt(id_clone);
		out.writeObject(filename);
		out.writeInt(start_line);
		out.writeInt(end_line);
		out.writeObject(lines);
		//SAVER
	}

	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		id_clone = (int) in.readInt();
		filename = (String) in.readObject();
		start_line = (int) in.readInt();
		end_line = (int) in.readInt();
		lines = (HashMap<Integer,String>) in.readObject();
		//LINKER
	}

	private int id_clone;
	private String filename;
	private int start_line;
	private int end_line;
	private HashMap<Integer,String> lines;
	
	private CloneClass clone_class;
	private Commit_Author author;

	private File_Change change_modify_clone = null;
	
	private ArrayList<ClonePair> pairs = new ArrayList<ClonePair>();

}
