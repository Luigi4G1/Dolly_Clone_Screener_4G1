package builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import model.Commit_Author;
import model.Commit_History;
import model.Commit_Node;
import model.Commit_parse;
import model.File_Change;
import utils.GitUtils4G1;

public class Commit_History_Builder {

	public static Commit_History commit_history_builder(GitUtils4G1 repo_info, HashMap<String, Commit_parse> commit_map)
	{
		//INPUT ERROR CONTROL
		if((repo_info == null)||(repo_info.getSystem_name() == null)||(repo_info.getSystem_name().isEmpty()))
		{
			String response = "REPO NOT SET->";
			if(repo_info == null)
				response = response + "NULL";
			else
				response = response + repo_info.toString();
			System.out.println(response);
			return null;
		}
		Collection<Commit_parse> map_values = commit_map.values();
		ArrayList<Commit_parse> commit_list = new ArrayList<Commit_parse>(map_values);
		Collections.sort(commit_list, Commit_parse.Commit_parse_DateASC_Comparator);
		
		Commit_History commit_history = new Commit_History(repo_info.getSystem_name());
		for(Commit_parse commit_parse : commit_list)
		{
			Commit_Author author = commit_history.getAuthorByID(commit_parse.getId_author());
			if(author == null)
			{
				author = new Commit_Author(commit_parse.getId_author(), commit_parse.getEmail_author());
				commit_history.addAuthor(author);
			}
			
			Commit_Node commit_node = commit_history.getCommitByID(commit_parse.getId_commit());
			if(commit_node == null)
			{
				commit_node = new Commit_Node(commit_parse.getId_commit(), commit_parse.getData_commit(), commit_parse.getDescription(), commit_parse.getFile_changes());
				commit_node.setAuthor(author);
				commit_history.addCommit(commit_node);
				//ADD PARENTS
				for(String parent_id:commit_parse.getId_parents())
				{
					Commit_Node commit_parent = commit_history.getCommitByID(parent_id);
					if(commit_parent != null)
					{
						commit_node.addParent(commit_parent);
						commit_parent.addChild(commit_node);
					}
				}
			}
			
			for(File_Change change : commit_node.getFile_changes())
			{
				change.setCommit(commit_node);
				if(change.getNewer_checksum()==null)
				{
					System.out.println("#NO CHECKSUM READ[dest file="+change.getDestination_filename()+"]:"+change.getNewer_checksum());
					System.out.println("->COMMIT-ID:"+commit_node.getId_commit());
					//String sha1 = repo_info.getFileChecksumByCommitID(change.getDestination_filename(), commit_node.getId_commit());
					//NOTE: faster
					String sha1 = null;
					System.out.println("->RETRIEVED:"+sha1);
					if(!change.getInitial_filename().equalsIgnoreCase(change.getDestination_filename()))
					{
						if(change.getChanges().isEmpty())
						{
							System.out.println("RENAME FROM->TO with SIMILARITY 100%");
							if(change.getOlder_checksum()==null)
								change.setOlder_checksum(sha1);
						}
						else
							System.out.println("RENAME FROM->TO");
						
						System.out.println("->RENAME FROM:"+change.getInitial_filename());
						System.out.println("->RENAME TO:"+change.getDestination_filename());
					}
					else
						System.out.println("->FILEPATH:"+change.getDestination_filename());
					
					change.setNewer_checksum(sha1);
				}
			}
			author.addCommit_Node(commit_node);	
		}
		//NEXT
		System.out.println("######COMMITS PARSED -> NODES: "+commit_history.getCommit_nodes().size());
		System.out.println("######AUTHORS PARSED: "+commit_history.getCommit_authors().size());
		return commit_history;
	}
}
