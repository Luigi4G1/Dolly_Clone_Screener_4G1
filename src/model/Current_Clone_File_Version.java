package model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Current_Clone_File_Version implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6180106621264441664L;

	public Current_Clone_File_Version(Clone clone, File_Version current_file_version,
			HashMap<Integer, String> current_clone_lines, int modified_clone_lines_counter) {
		this.clone = clone;
		this.current_file_version = current_file_version;
		this.current_clone_lines = current_clone_lines;
		this.modified_clone_lines_counter = modified_clone_lines_counter;
	}

	public Clone getClone() {
		return clone;
	}
	public void setClone(Clone clone) {
		this.clone = clone;
	}
	public File_Version getCurrent_file_version() {
		return current_file_version;
	}
	public void setCurrent_file_version(File_Version current_file_version) {
		this.current_file_version = current_file_version;
	}
	public HashMap<Integer, String> getCurrent_clone_lines() {
		return current_clone_lines;
	}
	public void setCurrent_clone_lines(HashMap<Integer, String> current_clone_lines) {
		this.current_clone_lines = current_clone_lines;
	}
	
	public String getCurrent_clone_file_as_string()
	{
		if(this.current_clone_lines == null)
			return null;
		else
		{
			String toReturn = "";
			ArrayList<Integer> line_numbers = new ArrayList<Integer>(this.current_clone_lines.keySet());
			Collections.sort(line_numbers);
			//TEST->TO REMOVE
			System.out.println("$$$->LINES:"+line_numbers.size());
			
			for(Integer line_number : line_numbers)
			{
				toReturn = toReturn + this.current_clone_lines.get(line_number) + System.lineSeparator();
			}
			return toReturn;
		}
	}
	
	public int getInitialLineNumber()
	{
		int result = 0;
		if(this.current_clone_lines != null)
		{
			for( Entry<Integer, String> entry : this.current_clone_lines.entrySet())
			{
				if(entry.getValue() != null)
				{
					if(entry.getValue().startsWith(Clone_File_History.START_CLONE_TAG))
					{
						result = entry.getKey().intValue();
						break;
					}
				}
			}
		}
		return result;
	}
	
	public int getFinalLineNumber()
	{
		int result = 0;
		if(this.current_clone_lines != null)
		{
			for( Entry<Integer, String> entry : this.current_clone_lines.entrySet())
			{
				if(entry.getValue() != null)
				{
					if(entry.getValue().endsWith(Clone_File_History.END_CLONE_TAG))
					{
						result = entry.getKey().intValue();
						break;
					}
				}
			}
		}
		return result;
	}
	
	public Current_Clone_File_Version getPrevious()
	{
		if(current_file_version.getPrevious() == null)
		{
			System.out.println("NO PREVOIUS VERSION AVAILABLE");
			return null;
		}
		else
		{
			int start_clone = this.getInitialLineNumber();
			int end_clone = this.getFinalLineNumber();
			int modified_clone_lines = 0;
			/*
			if((start_clone <=0 )||(end_clone <= 0))
			{
				System.out.println("UNABLE TO FIND CLONE TAG");
				return null;
			}*/
			HashMap<Integer, String> older_lines_to_return = new HashMap<Integer, String>();
			//Copy all current values -> curr_lines
			HashMap<Integer, String> curr_lines = new HashMap<Integer, String>();
			if(current_clone_lines != null)
			{
				for(Map.Entry<Integer, String> entry : current_clone_lines.entrySet())
					curr_lines.put(entry.getKey(), entry.getValue());
			}
			
			//older version file lines
			HashMap<Integer, String> older_version_file_lines_substitute = current_file_version.getPrevious().getLines();
			
			ArrayList<Diff_Range_Change> changes = current_file_version.getDiff().getChanges();
			for(Diff_Range_Change change : changes)
			{
				//READD ALL REMOVED LINES -> "-"
				HashMap<Integer, String> removed_lines = change.getRemoved_lines();
				for(Map.Entry<Integer, String> entry :removed_lines.entrySet())
				{
					/*
					String line_removed = entry.getValue();
					if((line_removed != null)&&(line_removed.startsWith("-")))
					{
						older_lines_to_return.put(entry.getKey(),line_removed.substring(1));
					}*/
					
					String line_removed = entry.getValue();
					if((line_removed != null)&&(line_removed.startsWith("-")))
					{
						String to_readd = null;
						if(older_version_file_lines_substitute != null)
							to_readd = older_version_file_lines_substitute.get(entry.getKey());
						//FIX - If  not able to find the line, use the one extracted from DIFF PATCH
						if(to_readd == null)
							to_readd = line_removed.substring(1);
						
						older_lines_to_return.put(entry.getKey(),to_readd);
					}
				}
				//REMOVE ALL ADDED LINES -> "+"
				for(Map.Entry<Integer, String> entry :change.getAdded_lines().entrySet())
				{
					String line_added = entry.getValue();
					if((line_added != null)&&(line_added.startsWith("+")))
					{
						curr_lines.remove(entry.getKey());
						//LOC changes counter
						if((start_clone >=0)&&(end_clone >= 0))
						{
							int num_line = entry.getKey().intValue();
							if((num_line >= start_clone)&&(num_line <= end_clone))
								modified_clone_lines++;
						}
					}
				}
				//NEXT
			}
			//MODs
			ArrayList<Integer> keys_for_fill = new ArrayList<Integer>(curr_lines.keySet());
			//SORTING in ASCENDING ORDER the remaining lines
			Collections.sort(keys_for_fill);
			//ADDING all the remaining lines
			int line_num = 1;
			while(!keys_for_fill.isEmpty())
			{
				Integer key_to_add = keys_for_fill.remove(0);
				//FIND the hole between lines
				while(older_lines_to_return.get(new Integer(line_num))!=null)
				{
					line_num++;
				}
				older_lines_to_return.put(new Integer(line_num),curr_lines.get(key_to_add));
			}
			
			//If still holes, uses file_version obj
			if(older_version_file_lines_substitute != null)
			{
				for(Map.Entry<Integer, String> entry : older_version_file_lines_substitute.entrySet())
				{
					String test_line = older_lines_to_return.get(entry.getKey());
					if(test_line == null)
					{
						String filler_line = entry.getValue();
						if(filler_line != null)
							older_lines_to_return.put(entry.getKey(),filler_line);
					}
				}
			}
			
			return new Current_Clone_File_Version(clone, current_file_version.getPrevious(), older_lines_to_return,modified_clone_lines);
		}
	}
	
	//CLONE CHRONOLOGY
	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(clone);
		out.writeObject(current_file_version);
		out.writeObject(current_clone_lines);
		out.writeInt(modified_clone_lines_counter);
	}

	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		clone = (Clone) in.readObject();
		current_file_version = (File_Version) in.readObject();
		current_clone_lines = (HashMap<Integer,String>) in.readObject();
		modified_clone_lines_counter = in.readInt();
	}
	
	public int getModified_clone_lines_counter() {
		return modified_clone_lines_counter;
	}

	public void setModified_clone_lines_counter(int modified_clone_lines_counter) {
		this.modified_clone_lines_counter = modified_clone_lines_counter;
	}

	public String shortDescription()
	{
		StringBuilder builder = new StringBuilder();
		String date_version ="UNKNOWN";
		if(this.current_file_version != null)
			if(this.current_file_version.getDiff() != null)
				if(this.current_file_version.getDiff().getCommit()!= null)
					date_version = this.current_file_version.getDiff().getCommit().dateString();
				else
					System.out.println("->NO COMMIT");
			else
				System.out.println("->NO DIFF");
		else
			System.out.println("->NO FILEVERSION");
		builder.append("["+date_version+"]");
		builder.append("-{FILE:"+this.current_file_version.getFilename()+"}");
		builder.append("|"+this.current_file_version.getChecksum()+"|");
		return builder.toString();
	}

	private Clone clone = null;
	private File_Version current_file_version = null;
	HashMap<Integer, String> current_clone_lines = new HashMap<Integer, String>();
	private int modified_clone_lines_counter;

}
