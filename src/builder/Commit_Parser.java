package builder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import model.Commit_parse;
import model.Diff_Range_Change;
import model.File_Change;

public class Commit_Parser {
	
	public static final String TAG_COMMIT_START = "###COMMIT:";
	
	//public static HashMap<String, Commit_parse> read_commit_history_file(Scanner sc_commit_text)
	public static HashMap<String, Commit_parse> read_commit_history_file(File commit_story_text)
	{
		int lines_counter=0;
		int commit_counter=0;
		ArrayList<String> commit_lines;
		HashMap<String, Commit_parse> commit_map = new HashMap<String, Commit_parse>();
		
		String line_to_analyze =null;
		//################################################################
		BufferedReader sc_commit_text = null;
		
		try
		{
			sc_commit_text = new BufferedReader(new FileReader(commit_story_text));
			line_to_analyze = sc_commit_text.readLine();
			while(line_to_analyze != null)
			{
				lines_counter++;
				if(line_to_analyze.startsWith(TAG_COMMIT_START))
				{
					commit_counter++;
					commit_lines = new ArrayList<String>();
					do
					{
						line_to_analyze = sc_commit_text.readLine();
						if(line_to_analyze != null)
						{
							if(!line_to_analyze.startsWith(TAG_COMMIT_START))
							{
								lines_counter++;
								commit_lines.add(line_to_analyze);
							}
						}
					}
					while((line_to_analyze != null)&&(!line_to_analyze.startsWith(TAG_COMMIT_START)));
					
					//PARSE the COMMIT lines as text -> function()
					Commit_parse commit = parse(commit_lines);
					if(commit != null)
						commit_map.put(commit.getId_commit(), commit);
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(sc_commit_text != null)
				try {
					sc_commit_text.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		//################################################################
		System.out.println("######LINES READ: "+lines_counter);
		System.out.println("######COMMIT HEADERS FOUND: "+commit_counter);
		System.out.println("-> LIST LENGHT: ["+commit_map.size()+"]");
		/*
		Scanner myObj = new Scanner(System.in);
	    System.out.println("END OF COMMIT -> press to continue");
	    myObj.nextLine();*/
		return commit_map;
	}
	
	public static Commit_parse parse(ArrayList<String> commit_lines)
	{
		System.out.println("######COMMIT LINES TO ANALYZE: "+commit_lines.size());
		if(commit_lines.size() < 6)
		{
			System.err.println("###### WRONG FORMAT - NO COMMIT PARSABLE ##");
			return null;
		}

		String id_commit = commit_lines.remove(0);
		String id_author = commit_lines.remove(0);
		String email_author = commit_lines.remove(0);
		//TO ELABORATE?!?
		/*
		int em = Math.abs(email_author.hashCode());
		email_author = Integer.toString(em);
		*/
		String date_string = commit_lines.remove(0);
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date commit_date;
		try
		{
			commit_date = sd.parse(date_string);
		}
		catch (ParseException e)
		{
			System.err.println("Catturata un'eccezione nella lettura della data [" + date_string+"]");
			System.err.println("nel committ con ID [" + id_commit+"]");
			
			System.err.println("Alla data verra assegnato il valore della data odierna;");
			commit_date = new Date();
			return null;
		}
		
		String commit_parents_string = commit_lines.remove(0);
		StringTokenizer parents_tokens = new StringTokenizer(commit_parents_string);
		ArrayList<String> commit_parents = new ArrayList<String>();
		while(parents_tokens.hasMoreTokens())
		{
			String id_parent = parents_tokens.nextToken();
			commit_parents.add(id_parent);
		}
		
		String description = commit_lines.remove(0);
		
		//new commit_node_text -> set
		Commit_parse commit_unconn = new Commit_parse(id_commit, id_author, email_author, commit_date, commit_parents, description);
		//WHILE
		while(!commit_lines.isEmpty())
		{
			System.err.println("###### SI");
			File_Change file_change = diff_analize(commit_lines);
			if(file_change != null)
				commit_unconn.addFileChange(file_change);
		}
		//TEST -> to REMOVE later
		commit_unconn.test_console_report();
		
		return commit_unconn;
	}
	
	public static File_Change diff_analize(ArrayList<String> commit_lines)
	{
		File_Change toReturn = null;
		
		if((commit_lines == null) || (commit_lines.isEmpty()))
			return null;
		
		String diff_line = commit_lines.remove(0);
		String initial_filename = null;
		String destination_filename = null;
		System.err.println("######DIFF LINE: "+diff_line);
		
		if(!diff_line.startsWith("diff --git a/"))
		{
			System.err.println("######PARSING ERROR! DIFF LINE: "+diff_line);
			return null;
		}
		else
		{
			int second_filename_index = diff_line.lastIndexOf(" b/");
			initial_filename = diff_line.substring("diff --git a/".length(),second_filename_index);
			destination_filename = diff_line.substring(second_filename_index+" b/".length());
			System.out.println("FIRST FILE: "+initial_filename);
			System.out.println("SECOND FILE: "+destination_filename);
			//NEXT
			toReturn = new File_Change(initial_filename, destination_filename);
		}
		
		try
		{
			//Using remove because diff --git MUST be followed by at least a line
			String header_line = commit_lines.get(0);
			
			boolean not_exit = true;
			while(not_exit && (!commit_lines.isEmpty()))
			{
				if(header_line.startsWith("diff --git a/"))
				{
					not_exit = false;
				}
				else
				{
					//REMOVE HEADER NOT USED
					//old mode <mode> -> NOT USED
					//new mode <mode> -> NOT USED
					//similarity % -> NOT USED
					//dissimilarity % -> NOT USED
					//copy from <path> -> NOT USED
					//copy to <path> -> NOT USED
					if(header_line.startsWith("old mode")||header_line.startsWith("new mode")||header_line.startsWith("similarity")||header_line.startsWith("dissimilarity")||header_line.startsWith("copy from")||header_line.startsWith("copy to"))
					{
						commit_lines.remove(0); 
						header_line = commit_lines.get(0); 	
					}
					else
					{
						if(header_line.startsWith("index "))
						{
							commit_lines.remove(0);
							//CHECK <MODE> after the checksums
							int separator_index = header_line.indexOf("..");
							String older_checksum = header_line.substring("index ".length(),separator_index);
							//############################################################
							int second_checksum_index = separator_index+2;
							boolean exit_checksum_bool = true;
							while(exit_checksum_bool)
							{
								if(second_checksum_index<header_line.length())
								{
									String end_checksum_finder = header_line.substring(second_checksum_index, second_checksum_index+1);
									if(end_checksum_finder.equalsIgnoreCase(" "))
										exit_checksum_bool = false;
									else
										second_checksum_index++;
								}
								else
									exit_checksum_bool = false;
							}
							String newer_checksum;
							if(second_checksum_index<header_line.length())
								newer_checksum = header_line.substring(separator_index+2,second_checksum_index);
							else
								newer_checksum = header_line.substring(separator_index+2);
							//############################################################
							toReturn.setOlder_checksum(older_checksum);
							toReturn.setNewer_checksum(newer_checksum);
							header_line = commit_lines.get(0);
						}
						else if(header_line.startsWith("--- "))
						{
							commit_lines.remove(0);
							if(header_line.startsWith("--- a/"))
							{
								initial_filename = header_line.substring("--- a/".length());
							}
							else if(header_line.startsWith("--- /dev/null"))
							{
								initial_filename = null;
							}
							toReturn.setInitial_filename(initial_filename);
							header_line = commit_lines.get(0);
						}
						else if(header_line.startsWith("+++ "))
						{
							commit_lines.remove(0);
							if(header_line.startsWith("+++ b/"))
							{
								destination_filename = header_line.substring("+++ b/".length());
							}
							else if(header_line.startsWith("+++ /dev/null"))
							{
								destination_filename = null;
							}
							toReturn.setDestination_filename(destination_filename);
							header_line = commit_lines.get(0);
						}
						else if(header_line.startsWith("rename from "))
						{
							commit_lines.remove(0);
							initial_filename = header_line.substring("rename from ".length());
							toReturn.setInitial_filename(initial_filename);
							header_line = commit_lines.get(0);
						}
						else if(header_line.startsWith("rename to "))
						{
							commit_lines.remove(0);
							destination_filename = header_line.substring("rename to ".length());
							toReturn.setDestination_filename(destination_filename);
							header_line = commit_lines.get(0);
						}
						else if(header_line.startsWith("deleted file mode"))
						{
							commit_lines.remove(0);
							toReturn.setDestination_filename(null);
							header_line = commit_lines.get(0);
						}
						else if(header_line.startsWith("new file mode"))
						{
							commit_lines.remove(0);
							toReturn.setInitial_filename(null);
							header_line = commit_lines.get(0);
						}
						else if(header_line.startsWith("@@ -"))
						{
							//EXAMPLE:
							//@@ -23,6 +23,7 @@ 
							ArrayList<Diff_Range_Change> line_changes = new ArrayList<Diff_Range_Change>();
							while(header_line.startsWith("@@ -"))
							{
								if(!commit_lines.isEmpty())
									commit_lines.remove(0);
								else
									header_line = "";
								Diff_Range_Change diff_range = Diff_Range_Change.parse_range(header_line);
								if(diff_range != null)
								{
									diff_range.parse_changes(commit_lines);
									line_changes.add(diff_range);
								}
								if(!commit_lines.isEmpty())
									header_line = commit_lines.get(0);
								else
									header_line = "";
							}
							//ADD CHANGES[] to FILEVERSION obj
							toReturn.getChanges().addAll(line_changes);
						}
						else
						{
							if(!commit_lines.isEmpty())
							{
								commit_lines.remove(0);
								if(!commit_lines.isEmpty())
									header_line = commit_lines.get(0);
							}
						}
					}
				}
			}
		}
		catch(IndexOutOfBoundsException e)
		{
			e.printStackTrace();
			System.err.println("######PARSING ERROR! DIFF LINE: "+diff_line);
			System.err.println("######PARSING ERROR! NO OTHER HEADER LINE -> NEEDED#");
			return null;
		}

		return toReturn;
	}

}
