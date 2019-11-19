package persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import utils.FileUtils4G1;
import utils.GitUtils4G1;

public class Settings {
	
	//STANDARD NAME FOR SETTINGS FILE -> LAST SAVE
	public static final String SETTINGS_FILE = "./settings.txt";
	public static String SAVED_SYSTEMS_FOLDER = "./saves/";
	public static String PARSING_SYSTEMS_FOLDER = "./files/";
	
	private static final String SETTINGS_SYSTEM_NAME_SAVED_TAG = "#SYSTEM_NAME_SAVED:#";
	private static final String SETTINGS_COMMIT_ID_SAVED_TAG = "#COMMIT_ID_SAVED:#";
	
	//STANDARD NAME FOR SETTINGS FILE -> GIT SETTINGS
	public static final String GIT_SETTINGS_FILE = "settings_git.txt";
	//STANDARD NAME FOR SETTINGS FILE -> NICAD SETTINGS
	public static final String NICAD_SETTINGS_FILE = "settings_nicad.txt";
	
	//STANDARD NAME FOR SETTINGS FILE -> COMMIT LIST -> GIT COMMAND RESULT:
	//git log -p --date=iso --full-index --pretty=format:"###COMMIT:%n%H%n%cn%n%ce%n%cd%n%P%n%s" >commit_list.txt
	public static final String COMMIT_LIST_FILE = "commit_list.txt";
	
	//STANDARD NAME FOR SETTINGS FILE -> LAST SAVE
	public static final String CLONE_BW_EVOLUTION_FOLDER = "clone_bw_evol";
	public static final String CLONE_BW_EVOLUTION_FILE = "clone_bw_evol_";
	public static final String SAVE_FILE_EXT = ".save";

	public Settings() {
	}

	public Settings(String system_name, String system_version_commit_id) {
		if(system_name == null)
			system_name = "";
		system_name = system_name.trim();
		system_name = FileUtils4G1.filePathNormalize(system_name);
		while(system_name.startsWith(File.separator))
			system_name = system_name.substring(1);
		while(system_name.endsWith(File.separator))
			system_name = system_name.substring(0, system_name.length()-1);
		
		if(system_version_commit_id == null)
			system_version_commit_id = "";
		system_version_commit_id = system_version_commit_id.trim();
		
		this.system_name = system_name;
		this.system_version_commit_id = system_version_commit_id;
	}

	public Settings(String system_name, String system_version_commit_id, GitUtils4G1 repo_info, NiCad_Settings nicad_settings) {
		if(system_name == null)
			system_name = "";
		system_name = system_name.trim();
		system_name = FileUtils4G1.filePathNormalize(system_name);
		while(system_name.startsWith(File.separator))
			system_name = system_name.substring(1);
		while(system_name.endsWith(File.separator))
			system_name = system_name.substring(0, system_name.length()-1);
		
		if(system_version_commit_id == null)
			system_version_commit_id = "";
		system_version_commit_id = system_version_commit_id.trim();
		
		this.system_name = system_name;
		this.system_version_commit_id = system_version_commit_id;
		this.repo_info = repo_info;
		this.nicad_settings = nicad_settings;
	}

	public static Settings loadSettingsFile()
	{
		Settings toReturn = null;
		File settings_file = new File(SETTINGS_FILE);
		Scanner setting_scanner = null;
		try
		{
			setting_scanner = new Scanner(settings_file);
			
			String system_name = null;
			String system_version_commit_id = null;
			while(setting_scanner.hasNextLine())
			{
				String line = setting_scanner.nextLine();
				
				//NOTE: the VALUE to read MUST be in the NEXT LINE after the TAG
				switch(line)
				{
					case SETTINGS_SYSTEM_NAME_SAVED_TAG:
						system_name = setting_scanner.nextLine();
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
						break;
					case SETTINGS_COMMIT_ID_SAVED_TAG:
						system_version_commit_id = setting_scanner.nextLine();
						system_version_commit_id = system_version_commit_id.trim();
						break;
					default:
						setting_scanner.nextLine();
						break;
				}
			}
			setting_scanner.close();
			
			if((system_name != null)&&(!system_name.isEmpty())&&(system_version_commit_id != null)&&(!system_version_commit_id.isEmpty()))
				toReturn = new Settings(system_name, system_version_commit_id);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			if(setting_scanner != null)
				setting_scanner.close();
			return null;
		}
		return toReturn;
	}
	
	public boolean saveSettingsFile()
	{
		try {
			PrintWriter out = new PrintWriter(SETTINGS_FILE);
			out.println(SETTINGS_SYSTEM_NAME_SAVED_TAG);
			out.println(this.system_name);
			out.println(SETTINGS_COMMIT_ID_SAVED_TAG);
			out.println(this.system_version_commit_id);
			
			out.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static Settings loadSettingsVersion(String system_name, String system_version_commit_id)
	{
		//INPUT ERROR CONTROL
		if((system_name == null)||(system_version_commit_id == null))
		{
			System.out.println("ERROR: Unable to read SYSTEM PARSING FOLDER-> SYSTEM_NAME:"+system_name+" - VERSION_COMMIT:"+system_version_commit_id);
			return null;
		}
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

		system_version_commit_id = system_version_commit_id.trim();

		if((system_name.isEmpty())||(system_version_commit_id.isEmpty()))
		{
			System.out.println("ERROR: Unable to read SYSTEM PARSING FOLDER-> SYSTEM_NAME:"+system_name+" - VERSION_COMMIT:"+system_version_commit_id);
			return null;
		}
		
		File settings_system_folder = new File(PARSING_SYSTEMS_FOLDER+system_name+File.separator+system_version_commit_id);
		if(settings_system_folder.exists())
		{
			String git_filepath = PARSING_SYSTEMS_FOLDER+system_name+File.separator+system_version_commit_id+File.separator+GIT_SETTINGS_FILE;
			String nicad_filepath = PARSING_SYSTEMS_FOLDER+system_name+File.separator+system_version_commit_id+File.separator+NICAD_SETTINGS_FILE;
			
			GitUtils4G1 repo_info = GitUtils4G1.readFromFile(git_filepath);
			NiCad_Settings nicad_settings = NiCad_Settings.readFromFile(nicad_filepath);
			if(repo_info == null)
			{
				System.out.println("ERROR: Unable to read GIT SETTINGS FILE->"+git_filepath);
				return null;
			}
			if(nicad_settings == null)
			{
				System.out.println("ERROR: Unable to read NICAD SETTINGS FILE->"+nicad_filepath);
				return null;
			}
			return new Settings(system_name, system_version_commit_id, repo_info, nicad_settings);
		}
		else
		{
			System.out.println("ERROR: Unable to find SYSTEM PARSING FOLDER->"+settings_system_folder.getAbsolutePath());
			return null;
		}
	}
	
	public static Settings loadSettings()
	{
		Settings settings_file_content = Settings.loadSettingsFile();
		if(settings_file_content != null)
		{
			String system_name = settings_file_content.getSystem_name();
			String system_version_commit_id = settings_file_content.getSystem_version_commit_id();
			if((system_name != null)&&(system_version_commit_id != null))
			{
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

				system_version_commit_id = system_version_commit_id.trim();
				
				if((system_name.isEmpty())||(system_version_commit_id.isEmpty()))
				{
					System.out.println("ERROR: Unable to read SYSTEM PARSING FOLDER-> SYSTEM_NAME:"+system_name+" - VERSION_COMMIT:"+system_version_commit_id);
					return null;
				}

				File settings_system_folder = new File(PARSING_SYSTEMS_FOLDER+system_name+File.separator+system_version_commit_id);
				if(settings_system_folder.exists())
				{
					String git_filepath = PARSING_SYSTEMS_FOLDER+system_name+File.separator+system_version_commit_id+File.separator+GIT_SETTINGS_FILE;
					String nicad_filepath = PARSING_SYSTEMS_FOLDER+system_name+File.separator+system_version_commit_id+File.separator+NICAD_SETTINGS_FILE;
					
					GitUtils4G1 repo_info = GitUtils4G1.readFromFile(git_filepath);
					NiCad_Settings nicad_settings = NiCad_Settings.readFromFile(nicad_filepath);
					if(repo_info == null)
					{
						System.out.println("ERROR: Unable to read GIT SETTINGS FILE->"+git_filepath);
						return null;
					}
					if(nicad_settings == null)
					{
						System.out.println("ERROR: Unable to read NICAD SETTINGS FILE->"+nicad_filepath);
						return null;
					}
					return new Settings(system_name, system_version_commit_id, repo_info, nicad_settings);
				}
				else
				{
					System.out.println("ERROR: Unable to find SYSTEM PARSING FOLDER->"+settings_system_folder.getAbsolutePath());
					return null;
				}
			}
			else
			{
				System.out.println("ERROR: Unable to read SYSTEM PARSING FOLDER-> SYSTEM_NAME:"+system_name+" - VERSION_COMMIT:"+system_version_commit_id);
				return null;
			}
		}
		else
		{
			System.out.println("ERROR: Unable to read SETTINGS FILE->"+SETTINGS_FILE);
			return null;
		}
	}
	
	public boolean saveSettings()
	{
		//SAVING all SETTINGs to FILES
		boolean creation_possible = true;
		
		//CREATE ./FILES/ FOLDER if NOT EXISTS
		File settings_folder = new File(PARSING_SYSTEMS_FOLDER);
		if(!settings_folder.exists())
		{
			try {
				creation_possible = settings_folder.mkdirs();
			} catch (SecurityException e1) {
				e1.printStackTrace();
				creation_possible = false;
			}
		}
			
		//CREATE ./FILES/<SYTEM_NAME>/<COMMIT_ID> FOLDER if NOT EXISTS
		if(creation_possible)
		{
			if((system_name != null)&&(system_version_commit_id != null))
			{
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

				system_version_commit_id = system_version_commit_id.trim();
				
				if((system_name.isEmpty())||(system_version_commit_id.isEmpty()))
				{
					System.out.println("ERROR: Unable to read SYSTEM PARSING FOLDER-> SYSTEM_NAME:"+system_name+" - VERSION_COMMIT:"+system_version_commit_id);
					return false;
				}

				File settings_system_folder = new File(PARSING_SYSTEMS_FOLDER+system_name+File.separator+system_version_commit_id);
				if(!settings_system_folder.exists())
				{
					try {
						creation_possible = settings_system_folder.mkdirs();
					} catch (SecurityException e1) {
						e1.printStackTrace();
						creation_possible = false;
					}
				}
				
				if(creation_possible)
				{
					//CREATE HERE FILES ->
					String git_filepath = PARSING_SYSTEMS_FOLDER+system_name+File.separator+system_version_commit_id+File.separator+GIT_SETTINGS_FILE;
					creation_possible = this.repo_info.saveToFile(git_filepath);
					if(creation_possible)
					{
						System.out.println("GIT SETTINGS FILE SAVED!->"+git_filepath);
						
						String nicad_filepath = PARSING_SYSTEMS_FOLDER+system_name+File.separator+system_version_commit_id+File.separator+NICAD_SETTINGS_FILE;
						creation_possible = this.nicad_settings.saveToFile(nicad_filepath);
						if(creation_possible)
						{
							System.out.println("NICAD SETTINGS FILE SAVED!->"+nicad_filepath);
							
							if(saveSettingsFile())
							{
								System.out.println("SETTINGS FILE SAVED!->"+SETTINGS_FILE);
								return true;
							}
							else
							{
								System.out.println("ERROR: Unable to save SETTINGS FILE->"+SETTINGS_FILE);
								return false;
							}
						}
						else
						{
							System.out.println("ERROR: Unable to save NICAD SETTINGS FILE->"+nicad_filepath);
							return false;
						}
					}
					else
					{
						System.out.println("ERROR: Unable to save GIT SETTINGS FILE->"+git_filepath);
						return false;
					}
				}
				else
				{
					System.out.println("ERROR: Unable to create SYSTEM PARSING FOLDER->"+settings_system_folder.getAbsolutePath());
					return false;
				}
				
			}
			else
			{
				System.out.println("ERROR: Unable to create SYSTEM PARSING FOLDER-> SYSTEM_NAME:"+system_name+" - VERSION_COMMIT:"+system_version_commit_id);
				return false;
			}
		}
		else
		{
			System.out.println("ERROR: Unable to create SETTINGS FOLDER->"+settings_folder.getAbsolutePath());
			return false;
		}
	}
	
	public boolean createSettingsFiles()
	{
		//SAVING Git and NiCad SETTINGs to FILES
		boolean creation_possible = true;
		
		//CREATE ./FILES/ FOLDER if NOT EXISTS
		File settings_folder = new File(PARSING_SYSTEMS_FOLDER);
		if(!settings_folder.exists())
		{
			try {
				creation_possible = settings_folder.mkdirs();
			} catch (SecurityException e1) {
				e1.printStackTrace();
				creation_possible = false;
			}
		}
			
		//CREATE ./FILES/<SYTEM_NAME>/<COMMIT_ID> FOLDER if NOT EXISTS
		if(creation_possible)
		{
			if((system_name != null)&&(system_version_commit_id != null))
			{
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

				system_version_commit_id = system_version_commit_id.trim();
				
				if((system_name.isEmpty())||(system_version_commit_id.isEmpty()))
				{
					System.out.println("ERROR: Unable to read SYSTEM PARSING FOLDER-> SYSTEM_NAME:"+system_name+" - VERSION_COMMIT:"+system_version_commit_id);
					return false;
				}

				File settings_system_folder = new File(PARSING_SYSTEMS_FOLDER+system_name+File.separator+system_version_commit_id);
				if(!settings_system_folder.exists())
				{
					try {
						creation_possible = settings_system_folder.mkdirs();
					} catch (SecurityException e1) {
						e1.printStackTrace();
						creation_possible = false;
					}
				}
				
				if(creation_possible)
				{
					//CREATE HERE FILES ->
					String git_filepath = PARSING_SYSTEMS_FOLDER+system_name+File.separator+system_version_commit_id+File.separator+GIT_SETTINGS_FILE;
					creation_possible = this.repo_info.saveToFile(git_filepath);
					if(creation_possible)
					{
						System.out.println("GIT SETTINGS FILE SAVED!->"+git_filepath);
						
						String nicad_filepath = PARSING_SYSTEMS_FOLDER+system_name+File.separator+system_version_commit_id+File.separator+NICAD_SETTINGS_FILE;
						creation_possible = this.nicad_settings.saveToFile(nicad_filepath);
						if(creation_possible)
						{
							System.out.println("NICAD SETTINGS FILE SAVED!->"+nicad_filepath);
							return true;
						}
						else
						{
							System.out.println("ERROR: Unable to save NICAD SETTINGS FILE->"+nicad_filepath);
							return false;
						}
					}
					else
					{
						System.out.println("ERROR: Unable to save GIT SETTINGS FILE->"+git_filepath);
						return false;
					}
				}
				else
				{
					System.out.println("ERROR: Unable to create SYSTEM PARSING FOLDER->"+settings_system_folder.getAbsolutePath());
					return false;
				}
				
			}
			else
			{
				System.out.println("ERROR: Unable to create SYSTEM PARSING FOLDER-> SYSTEM_NAME:"+system_name+" - VERSION_COMMIT:"+system_version_commit_id);
				return false;
			}
		}
		else
		{
			System.out.println("ERROR: Unable to create SETTINGS FOLDER->"+settings_folder.getAbsolutePath());
			return false;
		}
	}

	public String getSystem_name() {
		return system_name;
	}

	public void setSystem_name(String system_name) {
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
		
		this.system_name = system_name;
	}

	public String getSystem_version_commit_id() {
		return system_version_commit_id;
	}

	public void setSystem_version_commit_id(String system_version_commit_id) {
		if(system_version_commit_id == null)
			system_version_commit_id = "";
		system_version_commit_id = system_version_commit_id.trim();
		
		this.system_version_commit_id = system_version_commit_id;
	}
	public GitUtils4G1 getRepo_info() {
		return repo_info;
	}
	public void setRepo_info(GitUtils4G1 repo_info) {
		this.repo_info = repo_info;
	}
	public NiCad_Settings getNicad_settings() {
		return nicad_settings;
	}
	public void setNicad_settings(NiCad_Settings nicad_settings) {
		this.nicad_settings = nicad_settings;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nicad_settings == null) ? 0 : nicad_settings.hashCode());
		result = prime * result + ((repo_info == null) ? 0 : repo_info.hashCode());
		result = prime * result + ((system_name == null) ? 0 : system_name.hashCode());
		result = prime * result + ((system_version_commit_id == null) ? 0 : system_version_commit_id.hashCode());
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
		Settings other = (Settings) obj;
		if (nicad_settings == null) {
			if (other.nicad_settings != null)
				return false;
		} else if (!nicad_settings.equals(other.nicad_settings))
			return false;
		if (repo_info == null) {
			if (other.repo_info != null)
				return false;
		} else if (!repo_info.equals(other.repo_info))
			return false;
		if (system_name == null) {
			if (other.system_name != null)
				return false;
		} else if (!system_name.equals(other.system_name))
			return false;
		if (system_version_commit_id == null) {
			if (other.system_version_commit_id != null)
				return false;
		} else if (!system_version_commit_id.equals(other.system_version_commit_id))
			return false;
		return true;
	}

	private String system_name = null;
	private String system_version_commit_id = null;

	private GitUtils4G1 repo_info = null;
	private NiCad_Settings nicad_settings = null;
}
