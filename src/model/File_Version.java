package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Scanner;

public class File_Version implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7214380889136829790L;

	public File_Version(String filename) {
		this.filename = filename;
	}
	
	public File_Version(String filename, String checksum) {
		this.filename = filename;
		this.checksum = checksum;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public File_Change getDiff() {
		return diff;
	}

	public void setDiff(File_Change diff) {
		this.diff = diff;
		
		if(this.diff != null)
			if(this.diff.getCommit()!= null)
				this.id_commit = this.diff.getCommit().getId_commit();
	}

	public HashMap<Integer, String> getLines() {
		return lines;
	}
	
	public void setLines(HashMap<Integer, String> lines) {
		this.lines = lines;
	}
	
	public void addLine(int linenumber, String line) {
		if(this.lines == null)
			this.lines = new HashMap<Integer,String>();
		this.lines.put(new Integer(linenumber), line);
	}
	
	public File_Version getNext() {
		return next;
	}

	public void setNext(File_Version next) {
		this.next = next;
	}

	public File_Version getPrevious() {
		return previous;
	}

	public void setPrevious(File_Version previous) {
		this.previous = previous;
	}

	public static File_Version parse(String filename, File file)
	{
		if((file == null)||(filename == null)||(filename.isEmpty()))
			return null;

		Scanner sc_file_version = null;
		try
		{
			if(!file.exists())
				return null;
			sc_file_version = new Scanner(file);
			int indexline = 1;
			File_Version toReturn = new File_Version(filename);
			while(sc_file_version.hasNextLine())
			{
				toReturn.lines.put(new Integer(indexline), sc_file_version.nextLine());
				indexline++;
			}
			return toReturn;
		}
		catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
			return null;
		}
		catch (Exception e2)
		{
			e2.printStackTrace();
			return null;
		}
		finally
		{
			if(sc_file_version != null)
				sc_file_version.close();
		}
	}
	
	public void recap_version()
	{
		System.out.println("<<<FILE VERSION<<<<");
		String line_num = null;
		if(this.lines == null)
			line_num = "UNABLE TO READ";
		else
			line_num = ""+lines.size();
		System.out.println("->FILENAME ["+line_num+" LINES]:\n"+filename);
		System.out.println(diff.shortDescription());
		System.out.println("->TYPE:["+diff.changeType()+"]");
		String commit_id = null;
		if(diff.getCommit() == null)
			commit_id = "COMMIT NOT SET -> NULL";
		else
			commit_id = diff.getCommit().getId_commit();
		System.out.println("from COMMIT:\n"+commit_id);
		System.out.println("<<<END RECAP VERSION<<<<");
	}
	
	//CLONE CHRONOLOGY
	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(filename);
		out.writeObject(checksum);
		out.writeObject(diff);
		out.writeObject(lines);

		if(diff != null)
			if (diff.getCommit()!=null)
				id_commit = diff.getCommit().getId_commit();
		out.writeObject(id_commit);
	}

	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		filename = (String) in.readObject();
		checksum = (String) in.readObject();
		diff = (File_Change) in.readObject();
		lines = (HashMap<Integer,String>) in.readObject();
		
		id_commit = (String) in.readObject();
	}
	//NOTE: File_Change writeObject() DOES NOT preserve LINK with Commit_Node -> NEEDS to be RE-LINKED again after readObject() invocation.
	
	public String getId_commit() {
		return id_commit;
	}
	
	private String filename;
	private String checksum = null;
	private File_Change diff = null;
	private HashMap<Integer,String> lines = new HashMap<Integer,String>();
	private File_Version next = null;
	private File_Version previous = null;
	
	private String id_commit = null;
	//TO DO->GETTER - SETTER NODES
}
