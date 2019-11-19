package persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import utils.FileUtils4G1;

public class NiCad_Settings {

	private static final String NICAD_ROOT_FULLPATH_TAG = "#NICAD_ROOT_FULLPATH:#";
	private static final String NICAD_ANALYZEDSYSTEMS_ROOT_FOLDER_NAME_TAG = "#NICAD_ANALYZEDSYSTEMS_ROOT_FOLDER_NAME:#";
	private static final String NICAD_SYSTEM_VERSION_COMMIT_TAG = "#NICAD_SYSTEM_VERSION_COMMIT:#";
	private static final String NICAD_CLONE_CLASSES_FILE_TAG = "#NICAD_CLONE_CLASSES_FILE:#";
	private static final String NICAD_CLONE_LINES_FILE_TAG = "#NICAD_CLONE_LINES_FILE:#";
	private static final String NICAD_CLONE_PAIRS_FILE_TAG = "#NICAD_CLONE_PAIRS_FILE:#";
	
	public NiCad_Settings(String root_fullpath, String analyzedsystems_root_folder, String version_commit, String clone_classes_file,
			String clone_lines_file, String clone_pairs_file) {
		//TEST INPUT FORMAT
		if(root_fullpath == null)
			root_fullpath = "";
		root_fullpath = FileUtils4G1.filePathNormalize(root_fullpath.trim());
		if(!root_fullpath.endsWith(File.separator))
			root_fullpath = root_fullpath + File.separator;

		if(analyzedsystems_root_folder == null)
			analyzedsystems_root_folder = "";
		analyzedsystems_root_folder = FileUtils4G1.filePathNormalize(analyzedsystems_root_folder.trim());
		while(analyzedsystems_root_folder.startsWith(File.separator))
			analyzedsystems_root_folder = analyzedsystems_root_folder.substring(1);
		while(analyzedsystems_root_folder.endsWith(File.separator))
			analyzedsystems_root_folder = analyzedsystems_root_folder.substring(0, analyzedsystems_root_folder.length()-1);

		if(version_commit == null)
			version_commit = "";
		version_commit = version_commit.trim();

		if(clone_classes_file == null)
			clone_classes_file = "";
		clone_classes_file = FileUtils4G1.filePathNormalize(clone_classes_file.trim());
		while(clone_classes_file.startsWith(File.separator))
			clone_classes_file = clone_classes_file.substring(1);
		
		if(clone_lines_file == null)
			clone_lines_file = "";
		clone_lines_file = FileUtils4G1.filePathNormalize(clone_lines_file.trim());
		while(clone_lines_file.startsWith(File.separator))
			clone_lines_file = clone_lines_file.substring(1);
		
		if(clone_pairs_file == null)
			clone_pairs_file = "";
		clone_pairs_file = FileUtils4G1.filePathNormalize(clone_pairs_file.trim());
		while(clone_pairs_file.startsWith(File.separator))
			clone_pairs_file = clone_pairs_file.substring(1);
		
		this.root_fullpath = root_fullpath;
		this.analyzedsystems_root_folder = analyzedsystems_root_folder;
		this.version_commit = version_commit;
		this.clone_classes_file = clone_classes_file;
		this.clone_lines_file = clone_lines_file;
		this.clone_pairs_file = clone_pairs_file;
	}

	public static NiCad_Settings readFromFile(String filename)
	{
		//TEST INPUT ERRORS
		if(filename == null)
			return null;
		filename = filename.trim();
		//TEST
		System.out.println("NiCad_Settings->"+filename);
		File setting_file = new File(filename);
		Scanner setting_scanner = null;
		try
		{
			setting_scanner = new Scanner(setting_file);
			
			String root_fullpath = null;
			String analyzedsystems_root_folder = null;
			String version_commit = null;
			String clone_classes_file = null;
			String clone_lines_file = null;
			String clone_pairs_file = null;
			
			while(setting_scanner.hasNextLine())
			{
				String line = setting_scanner.nextLine();
				switch(line)
				{
					case NICAD_ROOT_FULLPATH_TAG:
						root_fullpath = setting_scanner.nextLine().trim();
						root_fullpath = FileUtils4G1.filePathNormalize(root_fullpath);
						if(!root_fullpath.endsWith(File.separator))
							root_fullpath = root_fullpath + File.separator;
						break;
					case NICAD_ANALYZEDSYSTEMS_ROOT_FOLDER_NAME_TAG:
						analyzedsystems_root_folder = setting_scanner.nextLine().trim();
						analyzedsystems_root_folder = FileUtils4G1.filePathNormalize(analyzedsystems_root_folder);
						while(analyzedsystems_root_folder.startsWith(File.separator))
							analyzedsystems_root_folder = analyzedsystems_root_folder.substring(1);
						while(analyzedsystems_root_folder.endsWith(File.separator))
							analyzedsystems_root_folder = analyzedsystems_root_folder.substring(0, analyzedsystems_root_folder.length()-1);
						break;
					case NICAD_SYSTEM_VERSION_COMMIT_TAG:
						version_commit = setting_scanner.nextLine().trim();
						break;
					case NICAD_CLONE_CLASSES_FILE_TAG:
						clone_classes_file = setting_scanner.nextLine().trim();
						clone_classes_file = FileUtils4G1.filePathNormalize(clone_classes_file);
						while(clone_classes_file.startsWith(File.separator))
							clone_classes_file = clone_classes_file.substring(1);
						break;
					case NICAD_CLONE_LINES_FILE_TAG:
						clone_lines_file = setting_scanner.nextLine().trim();
						clone_lines_file = FileUtils4G1.filePathNormalize(clone_lines_file);
						while(clone_lines_file.startsWith(File.separator))
							clone_lines_file = clone_lines_file.substring(1);
						break;
					case NICAD_CLONE_PAIRS_FILE_TAG:
						clone_pairs_file = setting_scanner.nextLine().trim();
						clone_pairs_file = FileUtils4G1.filePathNormalize(clone_pairs_file);
						while(clone_pairs_file.startsWith(File.separator))
							clone_pairs_file = clone_pairs_file.substring(1);
						break;
					default:
						setting_scanner.nextLine();
						break;
				}
			}
			setting_scanner.close();
			
			if((root_fullpath != null)&&(analyzedsystems_root_folder != null)&&(version_commit != null)&&(clone_classes_file != null)&&(clone_lines_file != null)&&(clone_pairs_file != null))
			{
				return new NiCad_Settings(root_fullpath, analyzedsystems_root_folder, version_commit, clone_classes_file, clone_lines_file, clone_pairs_file);
			}
			else
			{
				//TEST -> ERROR during READING?
				System.out.println("RET->NULL");
				System.out.println("ROOT->"+root_fullpath);
				System.out.println("ANFOLD->"+analyzedsystems_root_folder);
				System.out.println("VERS->"+version_commit);
				System.out.println("CLASSES->"+clone_classes_file);
				System.out.println("LINES->"+clone_lines_file);
				System.out.println("PAIRS->"+clone_pairs_file);
				return null;
			}
				
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

			out.println(NICAD_ROOT_FULLPATH_TAG);
			out.println(this.root_fullpath);
			out.println(NICAD_ANALYZEDSYSTEMS_ROOT_FOLDER_NAME_TAG);
			out.println(this.analyzedsystems_root_folder);
			out.println(NICAD_SYSTEM_VERSION_COMMIT_TAG);
			out.println(this.version_commit);
			out.println(NICAD_CLONE_CLASSES_FILE_TAG);
			out.println(this.clone_classes_file);
			out.println(NICAD_CLONE_LINES_FILE_TAG);
			out.println(this.clone_lines_file);
			out.println(NICAD_CLONE_PAIRS_FILE_TAG);
			out.println(this.clone_pairs_file);
			
			out.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	public String getRoot_fullpath() {
		return root_fullpath;
	}
	public void setRoot_fullpath(String root_fullpath) {
		//TEST INPUT FORMAT
		if(root_fullpath == null)
			root_fullpath = "";
		root_fullpath = FileUtils4G1.filePathNormalize(root_fullpath.trim());
		if(!root_fullpath.endsWith(File.separator))
			root_fullpath = root_fullpath + File.separator;

		this.root_fullpath = root_fullpath;
	}
	public String getAnalyzedsystems_root_folder() {
		return analyzedsystems_root_folder;
	}
	public void setAnalyzedsystems_root_folder(String analyzedsystems_root_folder) {
		//TEST INPUT FORMAT
		if(analyzedsystems_root_folder == null)
			analyzedsystems_root_folder = "";
		analyzedsystems_root_folder = FileUtils4G1.filePathNormalize(analyzedsystems_root_folder.trim());
		while(analyzedsystems_root_folder.startsWith(File.separator))
			analyzedsystems_root_folder = analyzedsystems_root_folder.substring(1);
		while(analyzedsystems_root_folder.endsWith(File.separator))
			analyzedsystems_root_folder = analyzedsystems_root_folder.substring(0, analyzedsystems_root_folder.length()-1);

		this.analyzedsystems_root_folder = analyzedsystems_root_folder;
	}
	public String getVersion_commit() {
		return version_commit;
	}
	public void setVersion_commit(String version_commit) {
		//TEST INPUT ERRORS
		if(version_commit == null)
			version_commit = "";
		version_commit = version_commit.trim();
		this.version_commit = version_commit;
	}
	public String getClone_classes_file() {
		return clone_classes_file;
	}
	public void setClone_classes_file(String clone_classes_file) {
		//TEST INPUT FORMAT
		if(clone_classes_file == null)
			clone_classes_file = "";
		clone_classes_file = FileUtils4G1.filePathNormalize(clone_classes_file.trim());
		while(clone_classes_file.startsWith(File.separator))
			clone_classes_file = clone_classes_file.substring(1);
		
		this.clone_classes_file = clone_classes_file;
	}
	public String getClone_lines_file() {
		return clone_lines_file;
	}
	public void setClone_lines_file(String clone_lines_file) {
		//TEST INPUT FORMAT
		if(clone_lines_file == null)
			clone_lines_file = "";
		clone_lines_file = FileUtils4G1.filePathNormalize(clone_lines_file.trim());
		while(clone_lines_file.startsWith(File.separator))
			clone_lines_file = clone_lines_file.substring(1);

		this.clone_lines_file = clone_lines_file;
	}
	public String getClone_pairs_file() {
		return clone_pairs_file;
	}
	public void setClone_pairs_file(String clone_pairs_file) {
		//TEST INPUT FORMAT
		if(clone_pairs_file == null)
			clone_pairs_file = "";
		clone_pairs_file = FileUtils4G1.filePathNormalize(clone_pairs_file.trim());
		while(clone_pairs_file.startsWith(File.separator))
			clone_pairs_file = clone_pairs_file.substring(1);

		this.clone_pairs_file = clone_pairs_file;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((analyzedsystems_root_folder == null) ? 0 : analyzedsystems_root_folder.hashCode());
		result = prime * result + ((clone_classes_file == null) ? 0 : clone_classes_file.hashCode());
		result = prime * result + ((clone_lines_file == null) ? 0 : clone_lines_file.hashCode());
		result = prime * result + ((clone_pairs_file == null) ? 0 : clone_pairs_file.hashCode());
		result = prime * result + ((root_fullpath == null) ? 0 : root_fullpath.hashCode());
		result = prime * result + ((version_commit == null) ? 0 : version_commit.hashCode());
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
		NiCad_Settings other = (NiCad_Settings) obj;
		if (analyzedsystems_root_folder == null) {
			if (other.analyzedsystems_root_folder != null)
				return false;
		} else if (!analyzedsystems_root_folder.equals(other.analyzedsystems_root_folder))
			return false;
		if (clone_classes_file == null) {
			if (other.clone_classes_file != null)
				return false;
		} else if (!clone_classes_file.equals(other.clone_classes_file))
			return false;
		if (clone_lines_file == null) {
			if (other.clone_lines_file != null)
				return false;
		} else if (!clone_lines_file.equals(other.clone_lines_file))
			return false;
		if (clone_pairs_file == null) {
			if (other.clone_pairs_file != null)
				return false;
		} else if (!clone_pairs_file.equals(other.clone_pairs_file))
			return false;
		if (root_fullpath == null) {
			if (other.root_fullpath != null)
				return false;
		} else if (!root_fullpath.equals(other.root_fullpath))
			return false;
		if (version_commit == null) {
			if (other.version_commit != null)
				return false;
		} else if (!version_commit.equals(other.version_commit))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NiCad_Settings [root_fullpath=" + root_fullpath
				+ ", analyzedsystems_root_folder=" + analyzedsystems_root_folder
				+ ", version_commit=" + version_commit
				+ ", clone_classes_file=" + clone_classes_file
				+ ", clone_lines_file=" + clone_lines_file
				+ ", clone_pairs_file=" + clone_pairs_file + "]";
	}

	private String root_fullpath;
	private String analyzedsystems_root_folder;
	private String version_commit;
	private String clone_classes_file;
	private String clone_lines_file;
	private String clone_pairs_file;

}
