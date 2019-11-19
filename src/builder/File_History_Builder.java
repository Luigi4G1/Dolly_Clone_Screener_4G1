package builder;

import java.util.ArrayList;
import java.util.HashMap;

import model.Commit_History;
import model.Commit_Node;
import model.File_Change;
import model.File_History;
import model.File_Version;
import utils.FileUtils4G1;
import utils.GitUtils4G1;

public class File_History_Builder {

	
	//String filepath = "org/xbill/DNS/SingleNameBase.java";
	public static File_History file_history_builder(GitUtils4G1 repo_info, Commit_History commit_history, String filepath)
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
		if((commit_history == null)||(commit_history.getCommit_nodes().size() == 0))
		{
			String response = "COMMIT HISTORY NOT SET->";
			if(commit_history == null)
				response = response + "NULL";
			else
				response = response + "EMPTY["+commit_history.getCommit_nodes().size()+"]";
			System.out.println(response);
			return null;
		}
		if((filepath == null)||(filepath.isEmpty()))
		{
			String response = "FILEPATH NOT SET->";
			if(filepath == null)
				response = response + "NULL";
			else
				response = response + "EMPTY";
			System.out.println(response);
			return null;
		}
		
		//NEED the MOST RECENT COMMIT ID for setting the STARTING point of the RESEARCH
		String commit_id_string = commit_history.getLastCommit().getId_commit();
		System.out.println("LAST COMMIT ID->"+commit_id_string);
		ArrayList<String> ids=repo_info.getFilenameHistoryOfCommitsID(filepath,commit_id_string);
		System.out.println("COMMIT IDs FOUND->"+ids.size());
		//NO COMMIT FOUND for the filepath -> NULL
		if(ids.isEmpty())
		{
			System.out.println("NO COMMIT FOUND for the file->"+filepath);
			return null;
		}
		
		ArrayList<Commit_Node> file_history_commits = new ArrayList<Commit_Node>();
		for(String id:ids)
		{
			Commit_Node commit = commit_history.getCommitByID(id);
			if(commit != null)
				file_history_commits.add(commit);
		}
		System.out.println("["+file_history_commits.size()+"]COMMITs FOUND inside COMMIT_HISTORY for the file->"+filepath);
		//NO COMMIT FOUND in the COMMIT HISTORY for the filepath -> NULL
		if(file_history_commits.isEmpty())
		{
			System.out.println("NO COMMIT FOUND in the COMMIT HISTORY for the file->"+filepath);
			return null;
		}
		
		String filepath_for_backtracking = filepath;
		//NORMALIZE FILEPATH
		filepath_for_backtracking = FileUtils4G1.filePathNormalize(filepath_for_backtracking);
		String checksum_for_backtracking = repo_info.getFileChecksumByCommitID(filepath_for_backtracking, commit_id_string);
		//NOTE: faster
		//String checksum_for_backtracking = null;
		
		File_History file_history = new File_History();
		boolean continue_versions = true;
		File_Change search_node = null;
		File_Version search_version=null;
		//TEST -> TO REMOVE
		int vers = 0;
		while(continue_versions)
		{
			if(checksum_for_backtracking == null)
			{
				System.out.println("Unable to continue: CHECKSUM -> NULL");
				break;
			}
			else
			{
				vers++;//TEST -> TO REMOVE
				System.out.println("#VERS["+vers+"]CHECKSUM -> "+checksum_for_backtracking);
			}
			File_Version newer_version = search_version;
			search_version = null;
			search_node = null;
			
			for(int index_file_history_commits=0;index_file_history_commits<file_history_commits.size();index_file_history_commits++)
			{
				Commit_Node curr_commit = file_history_commits.get(index_file_history_commits);
				//NOTE:INITIAL NODE -> newer_version == NULL
				if(newer_version != null)
				{
					//NOTE:
					//DIFF shows the differencies between a/file1 AND b/file2 with:
					//-file1->file status BEFORE the COMMIT
					//-file2->file status AFTER the COMMIT
					//---->AVOID infinite CICLES for RENAMES like the following example:
					//#COMMIT#(with 2 DIFFs)
					//(#1)DIFF a/file1 b/file2 -> RENAME file1 TO file2
					//(#2)DIFF a/file2 b/file1 -> RENAME file2 TO file1
					if(curr_commit.getData_commit().before(newer_version.getDiff().getCommit().getData_commit()))
					{
						ArrayList<File_Change> curr_commit_changes = curr_commit.getFile_changes();
						for(File_Change c:curr_commit_changes)
						{
							String curr_filename = c.getDestination_filename();
							//NORMALIZE FILEPATH
							curr_filename = FileUtils4G1.filePathNormalize(curr_filename);
							if(curr_filename != null)
							{
								if(curr_filename.equalsIgnoreCase(filepath_for_backtracking))
								{
									String curr_checksum = c.getNewer_checksum();
									//TRYING to FIX possible ERROR during BUILDING process
									//NOTE: faster
									
									if(curr_checksum == null)
									{
										curr_checksum = repo_info.getFileChecksumByCommitID(curr_filename, curr_commit.getId_commit());
										c.setNewer_checksum(curr_checksum);
									}
									if((curr_checksum != null)&&(curr_checksum.equalsIgnoreCase(checksum_for_backtracking)))
									{
										search_node = c;
										break;
									}
								}
							}
						}
					}
					else
					{
						//SKIP the COMMIT -> UNNECESSARY
						continue;
					}
				}
				else
				{
					ArrayList<File_Change> curr_commit_changes = curr_commit.getFile_changes();
					for(File_Change c:curr_commit_changes)
					{
						String curr_filename = c.getDestination_filename();
						//NORMALIZE FILEPATH
						curr_filename = FileUtils4G1.filePathNormalize(curr_filename);
						if(curr_filename != null)
						{
							if(curr_filename.equalsIgnoreCase(filepath_for_backtracking))
							{
								String curr_checksum = c.getNewer_checksum();
								//TRYING to FIX possible ERROR during BUILDING process
								//NOTE: faster
								
								if(curr_checksum == null)
								{
									curr_checksum = repo_info.getFileChecksumByCommitID(curr_filename, curr_commit.getId_commit());
									c.setNewer_checksum(curr_checksum);
								}
								if((curr_checksum != null)&&(curr_checksum.equalsIgnoreCase(checksum_for_backtracking)))
								{
									search_node = c;
									break;
								}
							}
						}
					}
				}
				
				if(search_node != null)
					break;
			}
			
			if(search_node != null)
			{
				//1)DOWNLOAD FILE->get LINES
				//2)CREATE FILE_VERSION OBJ
				//3)add previous NODE as CHILD
				//4)update VARs for the SEARCH
				search_version = new File_Version(filepath_for_backtracking);
				HashMap<Integer, String> lines = repo_info.getFileLinesByCommitsIDVersion(filepath_for_backtracking, search_node.getCommit().getId_commit());
				search_version.setLines(lines);
				search_version.setDiff(search_node);
				search_version.setChecksum(search_node.getNewer_checksum());
				search_version.setNext(newer_version);//MOST RECENT VERSION->null
				if(newer_version != null)
					newer_version.setPrevious(search_version);
				else
					file_history.setNewest_version(search_version);
				
				file_history.addVersion(search_version);
				file_history.addFilename(filepath_for_backtracking);
				
				filepath_for_backtracking = search_node.getInitial_filename();
				checksum_for_backtracking = search_node.getOlder_checksum();
				//NORMALIZE FILEPATH
				filepath_for_backtracking = FileUtils4G1.filePathNormalize(filepath_for_backtracking);
				
				if(filepath_for_backtracking == null)
				{
					//OLDEST FILE VERSION (or ERROR)->STOP
					continue_versions = false;
					file_history.setOldest_version(search_version);
				}
			}
			else
			{
				continue_versions = false;
				System.out.println("#ERROR! NO FILE CHANGE FOUND for:");
				System.out.println("#FILE:->"+filepath_for_backtracking);
				System.out.println("#CHECKSUM:->"+checksum_for_backtracking);
			}
		}
		System.out.println("DONE!-------------->FILEHISTORY-SIZE:"+file_history.getVersions().size());
		
		return file_history;
	}
}
