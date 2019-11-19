package test;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import persistence.Settings;
import utils.FileUtils4G1;
import utils.GitUtils4G1;

@SuppressWarnings("unused")
public class Test_Git_Log_Commitlist {

	public static final String REPO_PATH_URI_TEST = "https://github.com/dnsjava/dnsjava.git";
	public static final String REPO_LOCAL_PATH_TEST = "./repo_4G1";
	public static final String SYSTEM_NAME_TEST = "Dnsjava";
	
	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException {
		GitUtils4G1 repo_info = new GitUtils4G1(REPO_PATH_URI_TEST, REPO_LOCAL_PATH_TEST, SYSTEM_NAME_TEST);
		//TOT 1765
		String commit_id1663 = "d97b6a0685d59143372bb392ab591dd8dd840b61"; //1663
		String commit_id1662 = "0528b3387da244b285cafe57f5849b89baf7a3d3"; //1661
		String commit_id1661 = "f1b2adb206599f2177761c481f4a770d7b125bbf"; //1660
		String commit_id1660 = "44f5b53c7ea0951ca7a06dc6e237ea12d49332ff"; //1645
		//System.out.println(commit_id1663.length());
		//System.out.println("RES->"+downloadCommitListFile(repo_info, commit_id1663));
		
		//System.out.println("RES->"+downloadCommitListFile(repo_info, commit_id1660));
		//diffCommit(repo_info, commit_id1661);
		//diffCommit(repo_info, commit_id1662);
		//diffCommit(repo_info, commit_id1661);
		//diffCommit(repo_info, commit_id1660);
		
		//diffCommit(repo_info, commit_id1663);
		
		String filepath = repo_info.getRepo_local_path()+Settings.COMMIT_LIST_FILE;
		//System.out.println(filepath);
		repo_info.downloadCommitListFile(commit_id1663, filepath);
		////////////////////////////////////////////////////////////
		testdownloadCommitListFile(commit_id1663, filepath, repo_info);
		///////////////////////////////////////////////////////////////////////
	}
	
	public static boolean testdownloadCommitListFile(String commit_id_string, String filepath, GitUtils4G1 repo_info)
	{
		Git git = null;
		File repodir = null;
		try
		{
			String save_folder_path = repo_info.getRepo_local_path()+repo_info.getSystem_name();
		    repodir = new File(save_folder_path);
		    if(repodir.exists())
		    {
		    	System.out.println("REPO FOLDER ALREADY EXISTING -> TRYING to DELETE...["+save_folder_path+"][ - Vers.COMMITID:"+commit_id_string+"]");
		    	FileUtils4G1.deleteDirectoryRecursionJava6(repodir);
		    }
		    git = Git.cloneRepository()
		    		.setURI(repo_info.getRepo_path_uri())
		    		.setDirectory(new File(save_folder_path))
		    		.call();
		    
		    //Repository repository = git.getRepository();
			ObjectId commitId = ObjectId.fromString(commit_id_string);
			LogCommand logCommand = git.log()
					.add(commitId);
					//.all();
			
			Iterable<RevCommit> logs = logCommand
					.call();

			//int count = 0;
			StringBuilder builder = new StringBuilder();
			for (RevCommit rev : logs)
			{
				//###COMMIT:%n%H%n%cn%n%ce%n%cd%n%P%n%s
				//###COMMIT:%n
				builder.append("###COMMIT:"+System.lineSeparator());
				//%H%n%
				builder.append(rev.getId().getName()+System.lineSeparator());
				//cn%n
				builder.append(rev.getCommitterIdent().getName()+System.lineSeparator());
				//%ce%n
				builder.append(rev.getCommitterIdent().getEmailAddress()+System.lineSeparator());
				//%cd%n
				//2018-10-11 19:57:06 +0200
				Date commitDate = rev.getCommitterIdent().getWhen();
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
				df.setTimeZone(rev.getCommitterIdent().getTimeZone());
				builder.append(df.format(commitDate)+System.lineSeparator());
				//%P%n
				RevCommit[] parents = rev.getParents();
				int index_parent = 0;
				int parent_size = parents.length;
				for(RevCommit parent : parents)
				{
					index_parent++;
					if(index_parent == parent_size)
					{
						builder.append(parent.getId().getName()+System.lineSeparator());
					}//MORE THAN 1 PARENT
					else
					{
						builder.append(parent.getId().getName()+" ");
					}
				}
				//%s
				builder.append(rev.getShortMessage()+System.lineSeparator());

				if(parent_size < 2)
				{
					String difflist = testgetDiffOfCommit(rev, git.getRepository());
					if((difflist != null)&&(!difflist.isEmpty()))
						builder.append(difflist+System.lineSeparator());
				}
				//count++;
			}
			//System.out.println(count);
			//System.out.println(builder.toString());
			//WRITE TO FILE
			File file_to_write = new File(filepath);
			try
			{
				if(file_to_write.exists())
					file_to_write.delete();
				
				FileWriter writer = new FileWriter(file_to_write);
				BufferedWriter buffered_writer = new BufferedWriter(writer);
				buffered_writer.write(builder.toString());
				buffered_writer.close();
				return true;
			}
			catch(SecurityException | IOException e)
			{
				e.printStackTrace();
				return false;
			}
			///////////////
		}
		catch (GitAPIException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally
		{
			if(git != null)
				git.close();
			FileUtils4G1.deleteDirectoryRecursionJava6(repodir);
		}
	}
	
	///////////////////////////////////////////
	private static String testgetDiffOfCommit(RevCommit newCommit, Repository repo)
	{
		RevCommit oldCommit = null;
		try (RevWalk walk = new RevWalk(repo))
		{
			try {
				newCommit = walk.parseCommit(repo.resolve(newCommit.getId().getName()));
				
				if(newCommit.getParentCount()>0)
				{
					oldCommit = walk.parseCommit(repo.resolve(newCommit.getParent(0).getId().getName()));

				}
				else
					oldCommit = null;
	
			} catch (RevisionSyntaxException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
		}
		//Get commit that is previous to the current one.
	    if(oldCommit == null){
	    	return "";
	        //return "Start of repo";
	    }
	    //Use treeIterator to diff.
	    OutputStream outputStream = new ByteArrayOutputStream();
	    try (DiffFormatter formatter = new DiffFormatter(outputStream)) {
	        formatter.setRepository(repo);
	        formatter.setDetectRenames(true);
	        formatter.setAbbreviationLength(40);
	        formatter.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);
	        //formatter.setDiffComparator(RawTextComparator.WS_IGNORE_LEADING);
	        //formatter.setDiffComparator(RawTextComparator.WS_IGNORE_TRAILING);
	        //formatter.setDiffComparator(RawTextComparator.WS_IGNORE_CHANGE);
	        //formatter.setDiffComparator(RawTextComparator.DEFAULT);
	        
	        try {
				@SuppressWarnings("unused")
				List<DiffEntry> entries = formatter.scan(oldCommit.getTree(), newCommit.getTree());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return "";
			}
	        
	        try {
				formatter.format(oldCommit.getTree(), newCommit.getTree());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
	    }
	    String diff = outputStream.toString();
	    return diff;
	}
	
	//////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unused")
	private static String getDiffOfCommit(RevCommit newCommit, Repository repo)
	{
		try (RevWalk walk = new RevWalk(repo))
		{
			try {
				newCommit = walk.parseCommit(repo.resolve(newCommit.getId().getName()));
			} catch (RevisionSyntaxException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
		}
		//Get commit that is previous to the current one.
	    RevCommit oldCommit = getPreviousHash(newCommit, repo);
	    if(oldCommit == null){
	    	return "";
	        //return "Start of repo";
	    }
	    //Use treeIterator to diff.
	    AbstractTreeIterator oldTreeIterator = getCanonicalTreeParser(oldCommit, repo);
	    AbstractTreeIterator newTreeIterator = getCanonicalTreeParser(newCommit, repo);
	    OutputStream outputStream = new ByteArrayOutputStream();
	    try (DiffFormatter formatter = new DiffFormatter(outputStream)) {
	        formatter.setRepository(repo);
	        formatter.setDetectRenames(true);
	        formatter.setAbbreviationLength(40);
	        formatter.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);
	        //formatter.setDiffComparator(RawTextComparator.WS_IGNORE_LEADING);
	        //formatter.setDiffComparator(RawTextComparator.WS_IGNORE_TRAILING);
	        //formatter.setDiffComparator(RawTextComparator.WS_IGNORE_CHANGE);
	        //formatter.setDiffComparator(RawTextComparator.DEFAULT);
	        try {
				formatter.format(oldTreeIterator, newTreeIterator);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
	    }
	    String diff = outputStream.toString();
	    return diff;
	}
	//////////////////////////////////////////////////////////////////////
	//Helper function to get the previous commit.
	private static RevCommit getPreviousHash(RevCommit commit, Repository repo)
	{
		try (RevWalk walk = new RevWalk(repo))
		{
		// Starting point
			try {
				walk.markStart(commit);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}

			int count = 0;
			for (RevCommit rev : walk)
			{
			// got the previous commit.
				if (count == 1)
				{
					return rev;
				}
				count++;
			}
			walk.dispose();
		}
		//Reached end and no previous commits.
		return null;
	}
	
	//Helper function to get the tree of the changes in a commit. Written by Rüdiger Herrmann
	private static AbstractTreeIterator getCanonicalTreeParser(ObjectId commitId, Repository repo)
	{
		try (RevWalk walk = new RevWalk(repo))
		{
			RevCommit commit;
			try {
				commit = walk.parseCommit(commitId);

				ObjectId treeId = commit.getTree().getId();
				try (ObjectReader reader = repo.newObjectReader())
				{
					return new CanonicalTreeParser(null, reader, treeId);
				}
	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
	}
	///////////////////////////////////////////////////////////////////////////////

}
