package utils;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.diff.DiffConfig;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.FollowFilter;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

public class GitUtils4G1 {
	
	//EXAMPLE:
	//public static final String REPO_PATH_URI = "https://github.com/dnsjava/dnsjava.git";
	//public static final String REPO_LOCAL_PATH = "./repo_4G1/";
	//public static final String SYSTEM_NAME = "Dnsjava";
	
	private static final String REPO_PATH_URI_TAG = "#REPO_PATH_URI:#";
	private static final String REPO_LOCAL_PATH_TAG = "#REPO_LOCAL_PATH:#";
	private static final String SYSTEM_NAME_TAG = "#SYSTEM_NAME:#";

	public GitUtils4G1(String repo_path_uri, String repo_local_path, String system_name) {
		//TEST INPUT FORMAT
		if(repo_path_uri == null)
			repo_path_uri = "";
		repo_path_uri = repo_path_uri.trim();
		
		if(repo_local_path == null)
			repo_local_path = "";
		repo_local_path.trim();
		repo_local_path = FileUtils4G1.filePathNormalize(repo_local_path);
		if(!repo_local_path.endsWith(File.separator))
			repo_local_path = repo_local_path + File.separator;
		
		if(system_name == null)
			system_name = "";
		system_name.trim();
		system_name = FileUtils4G1.filePathNormalize(system_name);
		/*
		while(system_name.startsWith(File.separator))
			system_name = system_name.substring(1);
		while(system_name.endsWith(File.separator))
			system_name = system_name.substring(0, system_name.length()-1);
		*/
		system_name = FileUtils4G1.removeStartingSeparator(system_name);
		system_name = FileUtils4G1.removeTrailingSeparator(system_name);
		
		this.repo_path_uri = repo_path_uri;
		this.repo_local_path = repo_local_path;
		this.system_name = system_name;
	}
	
	public static GitUtils4G1 readFromFile(String filename)
	{
		//TEST INPUT ERRORS
		if(filename == null)
			return null;
		filename = filename.trim();
		
		File setting_file = new File(filename);
		Scanner setting_scanner = null;
		try
		{
			setting_scanner = new Scanner(setting_file);

			String repo_path_uri = null;
			String repo_local_path = null;
			String system_name = null;

			while(setting_scanner.hasNextLine())
			{
				String line = setting_scanner.nextLine();
				switch(line)
				{
					case REPO_PATH_URI_TAG:
						repo_path_uri = setting_scanner.nextLine().trim();
						break;
					case REPO_LOCAL_PATH_TAG:
						repo_local_path = setting_scanner.nextLine().trim();
						repo_local_path = FileUtils4G1.filePathNormalize(repo_local_path);
						if(!repo_local_path.endsWith(File.separator))
							repo_local_path = repo_local_path + File.separator;
						break;
					case SYSTEM_NAME_TAG:
						system_name = setting_scanner.nextLine().trim();
						system_name = FileUtils4G1.filePathNormalize(system_name);
						/*
						while(system_name.startsWith(File.separator))
							system_name = system_name.substring(1);
						while(system_name.endsWith(File.separator))
							system_name = system_name.substring(0, system_name.length()-1);
						*/
						system_name = FileUtils4G1.removeStartingSeparator(system_name);
						system_name = FileUtils4G1.removeTrailingSeparator(system_name);
						break;
					default:
						setting_scanner.nextLine();
						break;
				}
			}
			setting_scanner.close();
			
			if((repo_path_uri != null)&&(repo_local_path != null)&&(system_name != null))
			{
				return new GitUtils4G1(repo_path_uri, repo_local_path, system_name);
			}
			else
				return null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			if(setting_scanner != null)
				setting_scanner.close();
			return null;
		}
	}
	
	public boolean saveToFile(String filename)
	{
		//TEST INPUT ERRORS
		if(filename == null)
			return false;
		filename = filename.trim();

		try {
			PrintWriter out = new PrintWriter(filename);

			out.println(REPO_PATH_URI_TAG);
			out.println(this.repo_path_uri);
			out.println(REPO_LOCAL_PATH_TAG);
			out.println(this.repo_local_path);
			out.println(SYSTEM_NAME_TAG);
			out.println(this.system_name);
			
			out.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	public String getRepo_path_uri() {
		return repo_path_uri;
	}

	public void setRepo_path_uri(String repo_path_uri) {
		//TEST INPUT FORMAT
		if(repo_path_uri == null)
			repo_path_uri = "";
		repo_path_uri = repo_path_uri.trim();
		
		this.repo_path_uri = repo_path_uri;
	}

	public String getRepo_local_path() {
		return repo_local_path;
	}

	public void setRepo_local_path(String repo_local_path) {
		//TEST INPUT FORMAT
		if(repo_local_path == null)
			repo_local_path = "";
		repo_local_path = repo_local_path.trim();
		repo_local_path = FileUtils4G1.filePathNormalize(repo_local_path);
		if(!repo_local_path.endsWith(File.separator))
			repo_local_path = repo_local_path + File.separator;
		
		this.repo_local_path = repo_local_path;
	}

	public String getSystem_name() {
		return system_name;
	}

	public void setSystem_name(String system_name) {
		//TEST INPUT FORMAT
		if(system_name == null)
			system_name = "";
		system_name = system_name.trim();
		system_name = FileUtils4G1.filePathNormalize(system_name);
		/*
		while(system_name.startsWith(File.separator))
			system_name = system_name.substring(1);
		while(system_name.endsWith(File.separator))
			system_name = system_name.substring(0, system_name.length()-1);
		*/
		system_name = FileUtils4G1.removeStartingSeparator(system_name);
		system_name = FileUtils4G1.removeTrailingSeparator(system_name);
		
		this.system_name = system_name;
	}

	//String commit_id = "b7b35d634633ac3317dd4e1eb642e24d8ae40037";
	//String filepath = "org/xbill/DNS/SingleNameBase.java";
	//FOR File_Version OBJ
	public HashMap<Integer,String> getFileLinesByCommitsIDVersion(String filepath, String commit_id_string)
	{
		//INPUT ERRORS
		if((filepath == null)||(filepath.trim().isEmpty()))
		{
			System.out.println("FILENAME EMPTY->"+filepath);
			return null;
		}
		filepath = filepath.trim();
		if((commit_id_string == null)||(commit_id_string.trim().isEmpty()))
		{
			System.out.println("COMMIT ID EMPTY->"+commit_id_string);
			return null;
		}
		commit_id_string = commit_id_string.trim();
		//ATTRIBUTE ERRORS
		if((this.getRepo_path_uri() == null)||(this.getRepo_path_uri().trim().isEmpty()))
		{
			System.out.println("REPO URI NOT SET->"+this.getRepo_path_uri());
			return null;
		}
		if((this.getRepo_local_path() == null)||(this.getRepo_local_path().trim().isEmpty()))
		{
			System.out.println("REPO LOCAL PATH NOT SET->"+this.getRepo_local_path());
			return null;
		}
		if((this.getSystem_name() == null)||(this.getSystem_name().trim().isEmpty()))
		{
			System.out.println("SYSTEM NAME NOT SET->"+this.getSystem_name());
			return null;
		}
		repo_path_uri = repo_path_uri.trim();

		repo_local_path = repo_local_path.trim();
		repo_local_path = FileUtils4G1.filePathNormalize(repo_local_path);
		if(!repo_local_path.endsWith(File.separator))
			repo_local_path = repo_local_path + File.separator;

		system_name = system_name.trim();
		system_name = FileUtils4G1.filePathNormalize(system_name);
		/*
		while(system_name.startsWith(File.separator))
			system_name = system_name.substring(1);
		while(system_name.endsWith(File.separator))
			system_name = system_name.substring(0, system_name.length()-1);
		*/
		system_name = FileUtils4G1.removeStartingSeparator(system_name);
		system_name = FileUtils4G1.removeTrailingSeparator(system_name);

		//NEEDED for GIT COMMANDS
		filepath = FileUtils4G1.filePathNormalizeURI(filepath);
		
		HashMap<Integer,String> toReturn = new HashMap<Integer,String>();
		// Cloning the repository
		File repodir = null;
		Git git = null;
		try {
		    String save_folder_path = this.getRepo_local_path()+this.getSystem_name();
		    repodir = new File(save_folder_path);
		    if(repodir.exists())
		    {
		    	System.out.println("REPO FOLDER ALREADY EXISTING -> TRYING to DELETE...["+save_folder_path+"]["+filepath+" - Vers.COMMITID:"+commit_id_string+"]");
		    	FileUtils4G1.deleteDirectoryRecursionJava6(repodir);
		    }
		    
		    BasicConfigurator.configure();
			Logger.getRootLogger().setLevel(Level.INFO);
		    
		    git = Git.cloneRepository()
		    		.setURI(this.repo_path_uri)
		    		.setDirectory(new File(save_folder_path))
		    		.call();

		    System.out.println(filepath);
		    git.checkout().addPath(filepath).setStartPoint(commit_id_string).call();

		    String filename_to_read = save_folder_path+File.separator+FileUtils4G1.filePathNormalize(filepath);
		    File file_to_read = new File(filename_to_read);
		    Scanner sc_file_version = null;
		    try
		    {
		    	if(!file_to_read.exists())
					return null;
				sc_file_version = new Scanner(file_to_read);
				int indexline = 1;
				while(sc_file_version.hasNextLine())
				{
					toReturn.put(new Integer(indexline), sc_file_version.nextLine());
					indexline++;
				}
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
				git.close();
				FileUtils4G1.deleteDirectoryRecursionJava6(repodir);
			}
		}catch (GitAPIException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			if(git != null)
				git.close();
		}
		return toReturn;
	}

	//String commit_id = "b7b35d634633ac3317dd4e1eb642e24d8ae40037";
	//String filepath = "org/xbill/DNS/SingleNameBase.java";
	//TEST RENAME FROM-TO
	public ArrayList<String> getFilenameHistoryOfCommitsID(String filepath, String commit_id_string)
	{
		//INPUT ERRORS
		if((filepath == null)||(filepath.trim().isEmpty()))
		{
			System.out.println("FILENAME EMPTY->"+filepath);
			return null;
		}
		filepath = filepath.trim();
		if((commit_id_string == null)||(commit_id_string.trim().isEmpty()))
		{
			System.out.println("COMMIT ID EMPTY->"+commit_id_string);
			return null;
		}
		commit_id_string = commit_id_string.trim();
		//ATTRIBUTE ERRORS
		if((this.getRepo_path_uri() == null)||(this.getRepo_path_uri().trim().isEmpty()))
		{
			System.out.println("REPO URI NOT SET->"+this.getRepo_path_uri());
			return null;
		}
		if((this.getRepo_local_path() == null)||(this.getRepo_local_path().trim().isEmpty()))
		{
			System.out.println("REPO LOCAL PATH NOT SET->"+this.getRepo_local_path());
			return null;
		}
		if((this.getSystem_name() == null)||(this.getSystem_name().trim().isEmpty()))
		{
			System.out.println("SYSTEM NAME NOT SET->"+this.getSystem_name());
			return null;
		}
		repo_path_uri = repo_path_uri.trim();
		
		repo_local_path = repo_local_path.trim();
		repo_local_path = FileUtils4G1.filePathNormalize(repo_local_path);
		if(!repo_local_path.endsWith(File.separator))
			repo_local_path = repo_local_path + File.separator;
		
		system_name = system_name.trim();
		system_name = FileUtils4G1.filePathNormalize(system_name);
		/*
		while(system_name.startsWith(File.separator))
			system_name = system_name.substring(1);
		while(system_name.endsWith(File.separator))
			system_name = system_name.substring(0, system_name.length()-1);
		*/
		system_name = FileUtils4G1.removeStartingSeparator(system_name);
		system_name = FileUtils4G1.removeTrailingSeparator(system_name);
		//NEEDED for GIT COMMANDS
		filepath = FileUtils4G1.filePathNormalizeURI(filepath);
		
		ArrayList<String> toReturn = new ArrayList<String>();
		// Cloning the repository
		File repodir = null;
		Git git = null;
	    try {
	    	String save_folder_path = this.getRepo_local_path()+this.getSystem_name();
	    	repodir = new File(save_folder_path);
	    	if(repodir.exists())
	    	{
	    		System.out.println("REPO FOLDER ALREADY EXISTING -> TRYING to DELETE...["+save_folder_path+"]["+filepath+" - Vers.COMMITID:"+commit_id_string+"]");
	    		FileUtils4G1.deleteDirectoryRecursionJava6(repodir);
	    	}
	    	
	    	BasicConfigurator.configure();
			Logger.getRootLogger().setLevel(Level.INFO);
	    	
	    	git = Git.cloneRepository()
				    .setURI(this.repo_path_uri)
				    .setDirectory(new File(save_folder_path))
				    .call();

			System.out.println(filepath);
			
			Repository repository = git.getRepository();
			ObjectId commitId = ObjectId.fromString(commit_id_string);
			try (RevWalk revWalk = new RevWalk(repository))
			{
				//CONFIGURE DIFF for RENAME DETECTION
				repository.getConfig().setBoolean("diff", null, "renames", true);
				DiffConfig conf = repository.getConfig().get(DiffConfig.KEY);

				RevCommit start = revWalk.parseCommit(commitId);
				//SET FILTER -> LOG --follow <filepath> COMMAND implementation
				FollowFilter filter = FollowFilter.create(filepath,conf);
				revWalk.setTreeFilter(filter);
				//NEEDS a START COMMIT
				revWalk.markStart(revWalk.parseCommit(start));

				Iterator<RevCommit> iterator = revWalk.iterator();
				String sha1 = null;
				while(iterator.hasNext())
				{
					RevCommit commit = iterator.next();
					sha1 = commit.getName();
					toReturn.add(sha1);
					System.out.println(sha1);
				}
				revWalk.dispose();
				git.close();
				FileUtils4G1.deleteDirectoryRecursionJava6(repodir);
				//################################
			}

	    }catch (GitAPIException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    finally
		{
			if(git != null)
				git.close();
		}
	    return toReturn;
	}

	//SHA1 of a FILE from COMMIT ID
	//String commit_id = "b7b35d634633ac3317dd4e1eb642e24d8ae40037";
	//String filepath = "org/xbill/DNS/SingleNameBase.java";
	//TEST RENAME FROM-TO
	public String getFileChecksumByCommitID(String filepath,String commit_id_string)
	{
		//INPUT ERRORS
		if((filepath == null)||(filepath.trim().isEmpty()))
		{
			System.out.println("FILENAME EMPTY->"+filepath);
			return null;
		}
		filepath = filepath.trim();
		if((commit_id_string == null)||(commit_id_string.trim().isEmpty()))
		{
			System.out.println("COMMIT ID EMPTY->"+commit_id_string);
			return null;
		}
		commit_id_string = commit_id_string.trim();
		//ATTRIBUTE ERRORS
		if((this.getRepo_path_uri() == null)||(this.getRepo_path_uri().trim().isEmpty()))
		{
			System.out.println("REPO URI NOT SET->"+this.getRepo_path_uri());
			return null;
		}
		if((this.getRepo_local_path() == null)||(this.getRepo_local_path().trim().isEmpty()))
		{
			System.out.println("REPO LOCAL PATH NOT SET->"+this.getRepo_local_path());
			return null;
		}
		if((this.getSystem_name() == null)||(this.getSystem_name().trim().isEmpty()))
		{
			System.out.println("SYSTEM NAME NOT SET->"+this.getSystem_name());
			return null;
		}
		repo_path_uri = repo_path_uri.trim();

		repo_local_path = repo_local_path.trim();
		repo_local_path = FileUtils4G1.filePathNormalize(repo_local_path);
		if(!repo_local_path.endsWith(File.separator))
			repo_local_path = repo_local_path + File.separator;

		system_name = system_name.trim();
		system_name = FileUtils4G1.filePathNormalize(system_name);
		/*
		while(system_name.startsWith(File.separator))
			system_name = system_name.substring(1);
		while(system_name.endsWith(File.separator))
			system_name = system_name.substring(0, system_name.length()-1);
		*/
		system_name = FileUtils4G1.removeStartingSeparator(system_name);
		system_name = FileUtils4G1.removeTrailingSeparator(system_name);
		
		//NEEDED for GIT COMMANDS
		filepath = FileUtils4G1.filePathNormalizeURI(filepath);

		String sha1 = null;
		// Cloning the repository
		File repodir = null;
		Git git = null;
	    try {
	    	//File repodir = new File(REPO_LOCAL_PATH);
	    	String save_folder_path = this.getRepo_local_path()+this.getSystem_name();
	    	repodir = new File(save_folder_path);
	    	if(repodir.exists())
	    	{
	    		System.out.println("REPO FOLDER ALREADY EXISTING -> TRYING to DELETE...["+save_folder_path+"]["+filepath+" - Vers.COMMITID:"+commit_id_string+"]");
	    		FileUtils4G1.deleteDirectoryRecursionJava6(repodir);
	    	}
	    	
	    	BasicConfigurator.configure();
			Logger.getRootLogger().setLevel(Level.INFO);
	    	
	    	git = Git.cloneRepository()
				    .setURI(this.repo_path_uri)
				    .setDirectory(new File(save_folder_path))
				    .call();

			System.out.println(filepath);
			
			Repository repository = git.getRepository();
			ObjectId commitId = ObjectId.fromString(commit_id_string);
			try (RevWalk revWalk = new RevWalk(repository))
			{
				RevCommit commit = revWalk.parseCommit(commitId);
				//################################
				// and using commit's tree find the path
				RevTree tree = commit.getTree();
				System.out.println("Having tree: " + tree);
				// now try to find a specific file
				try (TreeWalk treeWalk = new TreeWalk(repository))
				{
					treeWalk.addTree(tree);
					treeWalk.setRecursive(true);
					//FILEPATH
					treeWalk.setFilter(PathFilter.create(filepath));
					if (!treeWalk.next())
					{
						throw new IllegalStateException("Did not find expected file ->"+filepath);
					}
					ObjectId objectId = treeWalk.getObjectId(0);
					sha1 = objectId.getName();
					//ObjectLoader loader = repository.open(objectId);
					// and then one can the loader to read the file
					//loader.copyTo(System.out);
				}
				revWalk.dispose();
				git.close();
				FileUtils4G1.deleteDirectoryRecursionJava6(repodir);
				//################################
			}

	    }catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    finally
		{
			if(git != null)
				git.close();
		}
	    return sha1;
	}
	
	//String commit_id = "b7b35d634633ac3317dd4e1eb642e24d8ae40037";
	//	RETURN-> String save_folder_path -> folder path of the downloaded system
	//	EXAMPLE:
	//	save_folder_path = "./repo_4G1/Dnsjava_b7b35d634633ac3317dd4e1eb642e24d8ae40037";
	public String checkoutRepoByCommitID(String commit_id_string)
	{
		//INPUT ERRORS
		if((commit_id_string == null)||(commit_id_string.trim().isEmpty()))
		{
			System.out.println("COMMIT ID EMPTY->"+commit_id_string);
			return null;
		}
		commit_id_string = commit_id_string.trim();
		//ATTRIBUTE ERRORS
		if((this.getRepo_path_uri() == null)||(this.getRepo_path_uri().trim().isEmpty()))
		{
			System.out.println("REPO URI NOT SET->"+this.getRepo_path_uri());
			return null;
		}
		if((this.getRepo_local_path() == null)||(this.getRepo_local_path().trim().isEmpty()))
		{
			System.out.println("REPO LOCAL PATH NOT SET->"+this.getRepo_local_path());
			return null;
		}
		if((this.getSystem_name() == null)||(this.getSystem_name().trim().isEmpty()))
		{
			System.out.println("SYSTEM NAME NOT SET->"+this.getSystem_name());
			return null;
		}
		repo_path_uri = repo_path_uri.trim();

		repo_local_path = repo_local_path.trim();
		repo_local_path = FileUtils4G1.filePathNormalize(repo_local_path);
		if(!repo_local_path.endsWith(File.separator))
			repo_local_path = repo_local_path + File.separator;

		system_name = system_name.trim();
		system_name = FileUtils4G1.filePathNormalize(system_name);
		/*
		while(system_name.startsWith(File.separator))
			system_name = system_name.substring(1);
		while(system_name.endsWith(File.separator))
			system_name = system_name.substring(0, system_name.length()-1);
		*/
		system_name = FileUtils4G1.removeStartingSeparator(system_name);
		system_name = FileUtils4G1.removeTrailingSeparator(system_name);
		
		String save_folder_path = null;
		// Cloning the repository
		File repodir = null;
		Git git = null;
		try
		{
			//File repodir = new File(REPO_LOCAL_PATH);
	    	save_folder_path = this.getRepo_local_path()+this.getSystem_name()+"_"+commit_id_string;
	    	//save_folder_path = this.getRepo_local_path()+this.getSystem_name()+File.separator+commit_id_string+File.separator+this.getSystem_name();
	    	repodir = new File(save_folder_path);
	    	if(repodir.exists())
	    	{
	    		System.out.println("REPO FOLDER ALREADY EXISTING -> TRYING to DELETE...["+save_folder_path+"]["+this.getSystem_name()+" - Vers.COMMITID:"+commit_id_string+"]");
	    		FileUtils4G1.deleteDirectoryRecursionJava6(repodir);
	    	}
	    	
	    	BasicConfigurator.configure();
			Logger.getRootLogger().setLevel(Level.INFO);
	    	
	    	git = Git.cloneRepository()
				    .setURI(this.repo_path_uri)
				    .setDirectory(new File(save_folder_path))
				    .call();
	    	//TEST IF EXISTS AS A COMMIT
	    	git.getRepository().resolve(commit_id_string+"^{commit}");

			System.out.println("DOWNLOADING to "+save_folder_path);
			//NOTE: this results in a DETACHED HEAD
			git.checkout().setCreateBranch(false).setName(commit_id_string).call();
			//If you need to create a new branch:
			//git.checkout().setCreateBranch(true).setName("new-branch").setStartPoint(commit_id_string).call();
		}
		catch (GitAPIException e) {
			e.printStackTrace();
			save_folder_path = null;
		}
		catch (Exception e) {
			e.printStackTrace();
			save_folder_path = null;
		}
		finally
		{
			if(git != null)
				git.close();
		}
		return save_folder_path;
	}
	
	//String commit_id = "b7b35d634633ac3317dd4e1eb642e24d8ae40037";
	//String folder_for_saves = "./repo_4G1/";
	//	RETURN-> String save_folder_path -> folder path of the downloaded system
	//	EXAMPLE:
	//	save_folder_path = "./repo_4G1/Dnsjava/b7b35d634633ac3317dd4e1eb642e24d8ae40037";
	public String checkoutRepoByCommitIDToFolder(String commit_id_string, String folder_for_saves)
	{
		//INPUT ERRORS
		if((commit_id_string == null)||(commit_id_string.trim().isEmpty()))
		{
			System.out.println("COMMIT ID EMPTY->"+commit_id_string);
			return null;
		}
		commit_id_string = commit_id_string.trim();
		if((folder_for_saves == null)||(folder_for_saves.trim().isEmpty()))
		{
			System.out.println("SAVE FOLDER NAME EMPTY->"+folder_for_saves);
			return null;
		}
		folder_for_saves = folder_for_saves.trim();
		//ATTRIBUTE ERRORS
		if((this.getRepo_path_uri() == null)||(this.getRepo_path_uri().trim().isEmpty()))
		{
			System.out.println("REPO URI NOT SET->"+this.getRepo_path_uri());
			return null;
		}
		if((this.getSystem_name() == null)||(this.getSystem_name().trim().isEmpty()))
		{
			System.out.println("SYSTEM NAME NOT SET->"+this.getSystem_name());
			return null;
		}
		repo_path_uri = repo_path_uri.trim();

		folder_for_saves = FileUtils4G1.filePathNormalize(folder_for_saves);
		if(!folder_for_saves.endsWith(File.separator))
			folder_for_saves = folder_for_saves + File.separator;

		system_name = system_name.trim();
		system_name = FileUtils4G1.filePathNormalize(system_name);
		/*
		while(system_name.startsWith(File.separator))
			system_name = system_name.substring(1);
		while(system_name.endsWith(File.separator))
			system_name = system_name.substring(0, system_name.length()-1);
		*/
		system_name = FileUtils4G1.removeStartingSeparator(system_name);
		system_name = FileUtils4G1.removeTrailingSeparator(system_name);

		String save_folder_path = null;
		// Cloning the repository
		File repodir = null;
		Git git = null;
		try
		{
			//File repodir = new File(REPO_LOCAL_PATH);
			//save_folder_path = this.getRepo_local_path()+this.getSystem_name()+"_"+commit_id_string;
			save_folder_path = folder_for_saves+this.getSystem_name()+File.separator+commit_id_string+File.separator+this.getSystem_name();
			repodir = new File(save_folder_path);
			if(repodir.exists())
			{
			    System.out.println("REPO FOLDER ALREADY EXISTING -> TRYING to DELETE...["+save_folder_path+"]["+this.getSystem_name()+" - Vers.COMMITID:"+commit_id_string+"]");
			    FileUtils4G1.deleteDirectoryRecursionJava6(repodir);
			}
			
			BasicConfigurator.configure();
			Logger.getRootLogger().setLevel(Level.INFO);

			git = Git.cloneRepository()
					.setURI(this.repo_path_uri)
					.setDirectory(new File(save_folder_path))
					.call();

			//TEST IF EXISTS AS A COMMIT
			git.getRepository().resolve(commit_id_string+"^{commit}");

			System.out.println("DOWNLOADING to "+save_folder_path);
			//NOTE: this results in a DETACHED HEAD
			git.checkout().setCreateBranch(false).setName(commit_id_string).call();
			//If you need to create a new branch:
			//git.checkout().setCreateBranch(true).setName("new-branch").setStartPoint(commit_id_string).call();
		}
		catch (GitAPIException e) {
			e.printStackTrace();
			save_folder_path = null;
		}
		catch (Exception e) {
			e.printStackTrace();
			save_folder_path = null;
		}
		finally
		{
			if(git != null)
				git.close();
		}
		return save_folder_path;
	}
	
	//String commit_id = "b7b35d634633ac3317dd4e1eb642e24d8ae40037";
	//String folder_for_saves = "./repo_4G1/";
	//	RETURN-> String save_folder_path -> folder path of the downloaded system
	//	EXAMPLE:
	//	save_folder_path = "./repo_4G1/Dnsjava/b7b35d634633ac3317dd4e1eb642e24d8ae40037";
	//NOTE: it REMOVES -/.git/- FOLDER
	public String downloadRepoByCommitIDToFolderNoGit(String commit_id_string, String folder_for_saves)
	{
		String save_folder_path = checkoutRepoByCommitIDToFolder(commit_id_string, folder_for_saves);
		if(save_folder_path != null)
		{
			System.out.println("REPO FOLDER DOWNLOADED!");
			File git_sett = new File(save_folder_path+File.separator+".git");
			if(git_sett.exists())
			{
				System.out.println("-> TRYING to DELETE...["+git_sett.getAbsolutePath()+"]");
				FileUtils4G1.deleteDirectoryRecursionJava6(git_sett);
			}
		}
		return save_folder_path;
	}

	public void deleteDirectorySystemRepoLocalPath()
	{
		//TEST ATTRIBUTES ERRORS
		if(repo_local_path == null)
			repo_local_path = "";
		repo_local_path = repo_local_path.trim();
		repo_local_path = FileUtils4G1.filePathNormalize(repo_local_path);
		if(!repo_local_path.endsWith(File.separator))
			repo_local_path = repo_local_path + File.separator;
		
		if(system_name == null)
			system_name = "";
		system_name = system_name.trim();
		system_name = FileUtils4G1.filePathNormalize(system_name);
		/*
		while(system_name.startsWith(File.separator))
			system_name = system_name.substring(1);
		while(system_name.endsWith(File.separator))
			system_name = system_name.substring(0, system_name.length()-1);
		*/
		system_name = FileUtils4G1.removeStartingSeparator(system_name);
		system_name = FileUtils4G1.removeTrailingSeparator(system_name);
		
		String save_folder_path = this.getRepo_local_path()+this.getSystem_name();
    	File repodir = new File(save_folder_path);
    	if(repodir.exists())
    	{
    		System.out.println("REPO FOLDER ALREADY EXISTING -> TRYING to DELETE...");
    		FileUtils4G1.deleteDirectoryRecursionJava6(repodir);
    	}
	}
	
	//GIT COMMAND implementation:
	//	git ls-remote <REPO_URL>
	//NOTE:IF NOT VALID URI
	//	->InvalidRemoteException
	//	->org.eclipse.jgit.errors.NoRemoteRepositoryException->TransportException
	//IT ALSO CHECK ANY OTHER ERROR FOUND -> NOT NECESSARILY AN INVALID REPO URI
	//IMPROVE THIS METHOD FOR A BETTER USE -> ONLY USED AS A SUGGESTION IN THIS PROGRAM
	public static boolean isValidRepoUri(String git_repo_uri)
	{
		//INPUT ERROR
		if((git_repo_uri == null)||(git_repo_uri.trim().isEmpty()))
			return false;
		
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.INFO);
		
		final LsRemoteCommand lsCmd = new LsRemoteCommand(null);
		lsCmd.setRemote(git_repo_uri);
		try {
			//PRINT SOME REPO DATA (IF VALID REPO)
			//NOTE: can throw UnknownHostKey
			System.out.println(lsCmd.call().toString());
		}
		catch (InvalidRemoteException e) {
			e.printStackTrace();
			return false;
		} catch (TransportException e) {
			e.printStackTrace();
			//FOR MORE DETAILS CAN CHECK
			/*EXAMPLE CAUSE 1:
			//NOT A VALID REPO URI -> CAUSE = org.eclipse.jgit.errors.NoRemoteRepositoryException
			if(e.getCause() instanceof org.eclipse.jgit.errors.NoRemoteRepositoryException)
			{
				System.out.println("FIRST CAUSE FOUND!"+e.getCause().getCause());
				return false;
			}*/
			
			/*EXAMPLE CAUSE 2:
			//SSH AUTH FAIL -> CAUSE MEX = UnknownHostKey: github.com. RSA key fingerprint is etc..
			if(e.getCause() instanceof org.eclipse.jgit.errors.TransportException)
			{
				System.out.println("FIRST CAUSE FOUND!"+e.getCause().getCause());
				if(e.getCause().getCause() instanceof com.jcraft.jsch.JSchException)
				{
					System.out.println("SECOND CAUSE FOUND!"+e.getCause().getCause().getCause());
				}
			}*/
			return false;
		} catch (GitAPIException e) {
			e.printStackTrace();
			return false;
		}
        catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	///////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////
	//git log -p --date=iso --full-index --pretty=format:"###COMMIT:%n%H%n%cn%n%ce%n%cd%n%P%n%s" >filename.txt
	//->String commit_id_string -> COMMIT ID (FULL INDEX = 40 CHARS)
	//->String filepath -> path of the file to create
	//NOTE: NEEDS implementation of returnStartOfRepoForFiles()->first version of the file AS String
	public boolean downloadCommitListFile(String commit_id_string, String filepath)
	{
		Git git = null;
		File repodir = null;
		try
		{
			String save_folder_path = this.getRepo_local_path()+this.getSystem_name();
		    repodir = new File(save_folder_path);
		    if(repodir.exists())
		    {
		    	System.out.println("REPO FOLDER ALREADY EXISTING -> TRYING to DELETE...["+save_folder_path+"][ - Vers.COMMITID:"+commit_id_string+"]");
		    	FileUtils4G1.deleteDirectoryRecursionJava6(repodir);
		    }
		    git = Git.cloneRepository()
		    		.setURI(this.getRepo_path_uri())
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
					String difflist = getDiffOfCommit(rev, git.getRepository());
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
	
	//NOTE: NEEDS implementation of returnStartOfRepoForFiles()->first version of the file AS String
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
	        //formatter.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);
	        //formatter.setDiffComparator(RawTextComparator.WS_IGNORE_LEADING);
	        //formatter.setDiffComparator(RawTextComparator.WS_IGNORE_TRAILING);
	        //formatter.setDiffComparator(RawTextComparator.WS_IGNORE_CHANGE);
	        formatter.setDiffComparator(RawTextComparator.DEFAULT);
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

	//Helper function to get the previous commit.
	private static RevCommit getPreviousHash(RevCommit commit, Repository repo)
	{
		try (RevWalk walk = new RevWalk(repo)) {
	        // Starting point
			try {
				walk.markStart(commit);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
	        int count = 0;
	        for (RevCommit rev : walk) {
	            // got the previous commit.
	            if (count == 1) {
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
	
	///////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((repo_local_path == null) ? 0 : repo_local_path.hashCode());
		result = prime * result + ((repo_path_uri == null) ? 0 : repo_path_uri.hashCode());
		result = prime * result + ((system_name == null) ? 0 : system_name.hashCode());
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
		GitUtils4G1 other = (GitUtils4G1) obj;
		if (repo_local_path == null) {
			if (other.repo_local_path != null)
				return false;
		} else if (!repo_local_path.equals(other.repo_local_path))
			return false;
		if (repo_path_uri == null) {
			if (other.repo_path_uri != null)
				return false;
		} else if (!repo_path_uri.equals(other.repo_path_uri))
			return false;
		if (system_name == null) {
			if (other.system_name != null)
				return false;
		} else if (!system_name.equals(other.system_name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GitUtils4G1 [repo_path_uri=" + repo_path_uri + ", repo_local_path=" + repo_local_path + ", system_name="
				+ system_name + "]";
	}

	private String repo_path_uri = null;
	private String repo_local_path = null;
	private String system_name = null;
}
