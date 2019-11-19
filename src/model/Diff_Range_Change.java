package model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Diff_Range_Change implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2814639397389765050L;

	public Diff_Range_Change()
	{
		removed_lines = new HashMap<Integer,String> ();
		added_lines = new HashMap<Integer,String> ();
		candidates_lines = new ArrayList<Integer> ();
	}

	public int getRemove_initial_line() {
		return remove_initial_line;
	}
	public void setRemove_initial_line(int remove_initial_line) {
		this.remove_initial_line = remove_initial_line;
	}
	public int getRemove_lenght() {
		return remove_lenght;
	}
	public void setRemove_lenght(int remove_lenght) {
		this.remove_lenght = remove_lenght;
	}
	public int getAdd_initial_line() {
		return add_initial_line;
	}
	public void setAdd_initial_line(int add_initial_line) {
		this.add_initial_line = add_initial_line;
	}
	public int getAdd_lenght() {
		return add_lenght;
	}
	public void setAdd_lenght(int add_lenght) {
		this.add_lenght = add_lenght;
	}

	public HashMap<Integer, String> getRemoved_lines() {
		if(this.removed_lines == null)
			this.removed_lines = new HashMap<Integer, String>();
		return removed_lines;
	}

	public void setRemoved_lines(HashMap<Integer, String> removed_lines) {
		this.removed_lines = removed_lines;
	}

	public HashMap<Integer, String> getAdded_lines() {
		if(this.added_lines == null)
			this.added_lines = new HashMap<Integer, String>();
		return added_lines;
	}

	public void setAdded_lines(HashMap<Integer, String> added_lines) {
		this.added_lines = added_lines;
	}

	public ArrayList<Integer> getCandidates_lines() {
		if(this.candidates_lines == null)
			this.candidates_lines = new ArrayList<Integer>();
		return candidates_lines;
	}

	public void setCandidates_lines(ArrayList<Integer> candidates_lines) {
		this.candidates_lines = candidates_lines;
	}

	//EXAMPLE:
	//@@ -28,7 +28,7 @@ the branch named `upstream`.
	public static Diff_Range_Change parse_range(String header_line)
	{
		if((header_line == null)||(!header_line.startsWith("@@ ")))
			return null;
		
		Diff_Range_Change toReturn = new Diff_Range_Change();
		
		int minus_index = header_line.indexOf("-");
		int plus_index = header_line.indexOf("+");
		
		int first_comma_index = header_line.indexOf(",");
		int end_first_range_index = header_line.indexOf(" +");
		
		int end_second_range_index = header_line.indexOf(" @@");
				
		try
		{
			//IndexOutOfBound Exception possibility -> CATCH EXCEPTION
			String first_line_number_string = header_line.substring(minus_index+1, first_comma_index);
			String first_length_string = header_line.substring(first_comma_index+1, end_first_range_index);
			
			int second_comma_index = plus_index+1;
			String comma_finder = header_line.substring(second_comma_index, second_comma_index+1);
			
			while(!comma_finder.equalsIgnoreCase(","))
			{
				second_comma_index++;
				comma_finder = header_line.substring(second_comma_index, second_comma_index+1);
			}
			
			String second_line_number_string = header_line.substring(plus_index+1, second_comma_index);
			String second_length_string = header_line.substring(second_comma_index+1, end_second_range_index);

			toReturn.setRemove_initial_line(Integer.parseInt(first_line_number_string));
			toReturn.setRemove_lenght(Integer.parseInt(first_length_string));
			toReturn.setAdd_initial_line(Integer.parseInt(second_line_number_string));
			toReturn.setAdd_lenght(Integer.parseInt(second_length_string));
		}
		catch(NumberFormatException e1)
		{
			e1.printStackTrace();
			return null;
		}
		catch(Exception e2)
		{
			e2.printStackTrace();
			return null;
		}
		
		return toReturn;
	}
	
	//HUNGRY function -> consumes the LIST
	//NOTE: + - or BLANK -> NOT removed from strings -> TEST if parsing is CORRECT
	//NEXT -> use toAnalyze.substring(1)
	public void parse_changes(ArrayList<String> commit_lines)
	{
		int removed_lines_index = remove_initial_line;
		int added_lines_index = add_initial_line;
		
		try
		{
			while((!commit_lines.isEmpty())&&(!((removed_lines_index == remove_initial_line+remove_lenght)&&(added_lines_index == add_initial_line+add_lenght))))
			{
				//USING GET here and REMOVE only after, trying to recover/avoid misalignments
				String toAnalyze = commit_lines.get(0);
				if(toAnalyze.startsWith(" "))
				{
					int max_value_rem = remove_initial_line+remove_lenght-1;
					if((removed_lines_index > max_value_rem))
						throw new Exception("INCONSISTENCY in the LENGTH -> INDEX REMOVE LINE: "+removed_lines_index+"[MAX "+max_value_rem+"]");
					
					int max_value_add = add_initial_line+add_lenght-1;
					if((added_lines_index > max_value_add))
						throw new Exception("INCONSISTENCY in the LENGTH -> INDEX ADDED LINE: "+added_lines_index+"[MAX "+max_value_add+"]");
					
					removed_lines.put(new Integer(removed_lines_index), toAnalyze);
					removed_lines_index++;
					added_lines.put(new Integer(added_lines_index), toAnalyze);
					added_lines_index++;
					commit_lines.remove(0);
				}
				else if(toAnalyze.startsWith("-"))
				{
					int max_value_rem = remove_initial_line+remove_lenght-1;
					if((removed_lines_index > max_value_rem))
						throw new Exception("INCONSISTENCY in the LENGTH -> INDEX REMOVE LINE: "+removed_lines_index+"[MAX "+max_value_rem+"]");
					removed_lines.put(new Integer(removed_lines_index), toAnalyze);
					removed_lines_index++;
					commit_lines.remove(0);
				}
				else if(toAnalyze.startsWith("+"))
				{
					int max_value_add = add_initial_line+add_lenght-1;
					if((added_lines_index > max_value_add))
						throw new Exception("INCONSISTENCY in the LENGTH -> INDEX ADDED LINE: "+added_lines_index+"[MAX "+max_value_add+"]");
					added_lines.put(new Integer(added_lines_index), toAnalyze);
					//TEST CLONE DETECTION
					candidates_lines.add(new Integer(added_lines_index));
					//TEST END#
					added_lines_index++;
					commit_lines.remove(0);
				}
				else
					throw new Exception("UNABLE TO PARSE THE STRING: "+toAnalyze);
			}
			
			if((removed_lines_index-remove_initial_line)!=remove_lenght)
				throw new Exception("INCONSISTENCY in the LENGTH -> REMOVED: "+removed_lines.size());
			else if((added_lines_index-add_initial_line)!=add_lenght)
				throw new Exception("INCONSISTENCY in the LENGTH -> ADDED: "+added_lines.size());

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "Diff_Range_Change [remove_initial_line=" + remove_initial_line + ", remove_lenght=" + remove_lenght
				+ ", add_initial_line=" + add_initial_line + ", add_lenght=" + add_lenght + "]";
	}
	
	public void console_print_range()
	{
		System.out.println("____CHANGED LINES_____");
		System.out.println("[-]->START LINE INDEX: "+this.remove_initial_line);
		System.out.println(" - TOT LINE #: "+this.remove_lenght);
		if(this.removed_lines != null)
			System.out.println("--["+removed_lines.size()+"] MEMORIZED!");
		System.out.println("[+]->START LINE INDEX: "+this.add_initial_line);
		System.out.println(" + TOT LINE #: "+this.add_lenght);
		if(this.added_lines != null)
			System.out.println("++["+added_lines.size()+"] MEMORIZED!");
		System.out.println("______________________");
	}
	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeInt(remove_initial_line);
		out.writeInt(remove_lenght);
		out.writeInt(add_initial_line);
		out.writeInt(add_lenght);
		
		out.writeObject(removed_lines);
		out.writeObject(added_lines);
		out.writeObject(candidates_lines);
		//SAVER
	}
	
	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		remove_initial_line = (int) in.readInt();
		remove_lenght = (int) in.readInt();
		add_initial_line = (int) in.readInt();
		add_lenght = (int) in.readInt();
		
		removed_lines = (HashMap<Integer,String>) in.readObject();
		added_lines = (HashMap<Integer,String>) in.readObject();
		candidates_lines = (ArrayList<Integer>) in.readObject();
		//LINKER
	}

	private int remove_initial_line;
	private int remove_lenght;
	private int add_initial_line;
	private int add_lenght;
	
	//File identified by - SIGN(first)
	//File identified by + SIGN(second)
	private HashMap<Integer,String> removed_lines = new HashMap<Integer,String> ();
	private HashMap<Integer,String> added_lines = new HashMap<Integer,String> ();
	private ArrayList<Integer> candidates_lines = new ArrayList<Integer> ();

}
