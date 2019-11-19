package model;

import java.util.HashMap;
import java.util.Map;

import utils.CommentRemover;

public class Clone_File_History {
	
	public final static String START_CLONE_TAG = "[CLST4G1]";
	public final static String END_CLONE_TAG = "[CLEND4G1]";
	
	public Clone_File_History(File_History file_history, Clone clone) {
		this.file_history = file_history;
		this.clone = clone;

		HashMap<Integer, String> clone_lines = new HashMap<Integer, String>();
		for(Map.Entry<Integer, String> entry : file_history.getNewest_version().getLines().entrySet())
		{
			clone_lines.put(entry.getKey(), entry.getValue());
		}
		//HashMap<Integer, String> clone_lines = new HashMap<Integer, String>(file_history.getNewest_version().getLines());
		//ADD TAGs to follow CLONE evolution
		String line_tag_start ="";
		if(clone_lines.get(new Integer(clone.getStart_line()))!=null)
			line_tag_start = START_CLONE_TAG + clone_lines.get(new Integer(clone.getStart_line()));
		clone_lines.put(new Integer(clone.getStart_line()), line_tag_start);
		
		String line_tag_end ="";
		if(clone_lines.get(new Integer(clone.getEnd_line()))!=null)
			line_tag_end = clone_lines.get(new Integer(clone.getEnd_line())) + END_CLONE_TAG;
		clone_lines.put(new Integer(clone.getEnd_line()), line_tag_end);

		this.current_clone_file_version = new Current_Clone_File_Version(clone,file_history.getNewest_version(),clone_lines,0);
	}

	public File_History getFile_history() {
		return file_history;
	}
	public void setFile_history(File_History file_history) {
		this.file_history = file_history;
	}
	public Clone getClone() {
		return clone;
	}
	public void setClone(Clone clone) {
		this.clone = clone;
	}

	public Current_Clone_File_Version getCurrent_clone_file_version() {
		return current_clone_file_version;
	}

	public void setCurrent_clone_file_version(Current_Clone_File_Version current_clone_file_version) {
		this.current_clone_file_version = current_clone_file_version;
	}

	public String getClone_search_string() {
		if(this.clone_search_string == null)
			this.clone_search_string = calculateClone_search_string();
		return clone_search_string;
	}

	public String calculateClone_search_string() {
		String toReturn = "";
		if(this.clone == null)
			return null;
		else
		{
			toReturn = toReturn + START_CLONE_TAG;
			for(int line_num = this.clone.getStart_line(); line_num < this.clone.getEnd_line(); line_num++)
			{
				String line = this.clone.getLines().get(new Integer(line_num));
				if(line == null)
				{
					System.out.println("->CLONE["+this.clone.getFilename()+"]-LINE["+line_num+"] NOT FOUND!");
					toReturn = toReturn + System.lineSeparator();
				}
				else
					toReturn = toReturn + line + System.lineSeparator();
			}
			
			String line = this.clone.getLines().get(new Integer(this.clone.getEnd_line()));
			if(line == null)
			{
				System.out.println("->CLONE["+this.clone.getFilename()+"]-LINE["+this.clone.getEnd_line()+"] NOT FOUND!");
				toReturn = toReturn + END_CLONE_TAG + System.lineSeparator();
			}
			else
				toReturn = toReturn + line + END_CLONE_TAG + System.lineSeparator();

			toReturn = CommentRemover.removeCommentsFromFileAsString(toReturn);
			return toReturn;
		}
	}
	
	public boolean isCloneStillSame(Current_Clone_File_Version current_clone_file_version_to_check)
	{
		String file_as_string = CommentRemover.removeCommentsFromFileAsString(current_clone_file_version_to_check.getCurrent_clone_file_as_string());
		file_as_string = CommentRemover.removeSpaces(file_as_string);
		String clone_to_find = this.getClone_search_string();
		clone_to_find = CommentRemover.removeSpaces(clone_to_find);
		System.out.println("+++++++CLONE TO FIND ->\n"+clone_to_find);
		System.out.println("+++++++FILE TO SEARCH ->\n"+file_as_string);
		if(file_as_string.contains(clone_to_find))
			return true;
		else
		{
			if(!file_as_string.contains(START_CLONE_TAG))
			{
				System.out.println("->CLONE START TAG NOT FOUND ->");
			}
			if(!file_as_string.contains(END_CLONE_TAG))
			{
				System.out.println("->CLONE END TAG NOT FOUND ->");
			}
			return false;
		}
	}


	private File_History file_history = null;
	private Clone clone = null;
	private String clone_search_string = null;
	
	private Current_Clone_File_Version current_clone_file_version = null;

}
