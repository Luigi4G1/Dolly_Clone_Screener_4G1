package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Map;

import model.Clone;
import model.CloneClass;
import model.ClonePair;
import model.Commit_Author;
import model.Commit_History;
import model.Commit_Node;
import model.File_Change;
import model.Report_NiCad5;
import persistence.Diff_Save;
import persistence.Link_Author_Commit;
import persistence.Link_Clone_Author;
import persistence.Link_Clone_Class;
import persistence.Link_Clone_Pair;
import persistence.Link_Commit_Parent;
import persistence.Settings;
import persistence.System_Analysis;

public class LoaderTemplateMethod {
	
	public static String COMMIT_HISTORY_SAVE = "commit_history.save";
	public static String COMMIT_HISTORY_LINK_AUTHOR_COMMIT_SAVE = "commit_history_link_author_commit.save";
	public static String COMMIT_HISTORY_LINK_COMMIT_PARENT_SAVE = "commit_history_link_commit_parent.save";
	public static String COMMIT_HISTORY_DIFFS_SAVE = "commit_history_diffs.save";
	
	public static String CLONES_REPORT_SAVE = "clones_report.save";
	public static String CLONES_LINK_CLONE_AUTHOR_SAVE = "clones_link_clone_author.save";
	public static String CLONES_LINK_CLONE_CLASS_SAVE = "clones_link_clone_class.save";
	public static String CLONES_LINK_CLONE_PAIR_SAVE = "clones_link_clone_pair.save";

	public static final System_Analysis loader_linker()
	{
		Settings settings = Settings.loadSettings();
		return loader_linker(settings);
	}
	
	public static final System_Analysis loader_linker(Settings settings)
	{
		//INPUT ERROR CONTROL
		if(settings == null)
		{
			System.out.println("CAN'T LOAD SETTINGS BY FILE");
			return null;
		}
		else
		{
			if((settings.getRepo_info() == null)||(settings.getRepo_info().getSystem_name() == null)||(settings.getSystem_version_commit_id() == null)) 
			{
				System.out.println("CAN'T LOAD SETTINGS BY FILE");
				return null;
			}

			String system_saves_folder = Settings.SAVED_SYSTEMS_FOLDER+settings.getRepo_info().getSystem_name()+File.separatorChar+settings.getSystem_version_commit_id()+File.separatorChar;
			System.out.println("->"+system_saves_folder);
			String commit_history_filename = system_saves_folder+COMMIT_HISTORY_SAVE;
			System.out.println("-> ->"+commit_history_filename);
			
			File commit_history_file = new File(commit_history_filename);
			Commit_History commit_history = loadCommit_HistoryFromFile(commit_history_file);
			if(commit_history != null)
			{
				loadLinks_Commit_History(commit_history, settings.getSystem_version_commit_id());
			}
			
			Report_NiCad5 report = loadNiCadReport(settings);
			if(report != null)
			{
				loadLinks_NiCadReport(report, commit_history);
			}
			
			loadLinks_NiCadReport(report, commit_history);
			loadDiffs_Commit_History(commit_history, report.getCommit_id_version(), report);
			
			return new System_Analysis(settings, commit_history, report);
		}
	}
	
	
	public static final boolean saveAll(Settings settings, Commit_History commit_history, Report_NiCad5 report)
	{
		//INPUT ERROR CONTROL
		if(settings == null)
		{
			String response = "SETTINGS NOT FOUND->NULL";
			System.out.println(response);
			return false;
		}
		else
		{
			//SAVING SETTINGS files
			boolean success = settings.saveSettings();
			if(success)
			{
				//SAVING COMMIT HISTORY files
				success = saveCommit_History(commit_history, settings.getSystem_version_commit_id());
				if(success)
				{
					success = saveLinks_Commit_History(commit_history, settings.getSystem_version_commit_id());
					if(success)
					{
						success = saveDiffs_Commit_History(commit_history, settings.getSystem_version_commit_id());
						//NEXT
						//SAVING CLONES files
						
						//1)REPORT -> 
						//	-> CLONES UNLINKED
						//	-> CLASSES UNLINKED
						//2)Link_Clone_Author -> each CLONE in REPORT (AUTHOR != NULL) -> ArrayList<Link_Clone_Author>
						//3)Link_Clone_Class -> each CLONE in REPORT -> ArrayList<Link_Clone_Class>
						//4)Link_Clone_Pair -> each CLONE in REPORT -> ArrayList<ClonePair> (CLONE1 && CLONE2 != NULL) -> ArrayList<Link_Clone_Pair>
						if(success)
						{
							success = saveNiCadReport(report);
							if(success)
							{
								return saveLinks_NiCadReport(report);
							}
							else
								return false;
						}
						else
							return false;
					}
					else
						return false;
				}
				else
					return false;
			}
			else
				return false;
		}
	}
	
	public static final boolean saveCommit_History(Commit_History commit_history, String system_version_commit_id)
	{
		//INPUT ERROR CONTROL
		if((commit_history == null)||(commit_history.getSystem() == null))
		{
			String response = "COMMIT HISTORY PROBLEM->";
			if(commit_history == null)
				response = response + "NULL";
			else if (commit_history.getSystem() == null)
				response = response + "NULL SYSTEM INFO";
			System.out.println(response);
			return false;
		}
		else if(system_version_commit_id == null)
		{
			String response = "COMMIT HISTORY PROBLEM->NULL VERSION INFO";
			System.out.println(response);
			return false;
		}
		else
		{
			String system_saves_folder_name = Settings.SAVED_SYSTEMS_FOLDER+commit_history.getSystem()+File.separatorChar+system_version_commit_id+File.separatorChar;
			System.out.println("->"+system_saves_folder_name);

			String commit_history_filename = system_saves_folder_name+COMMIT_HISTORY_SAVE;
			//String commit_history_filename = "saves/commit_history.save";
			System.out.println("-> ->"+commit_history_filename);

			try
			{
				File system_saves_folder = new File(system_saves_folder_name);
				if(!system_saves_folder.exists())
					system_saves_folder.mkdirs();
				
				File commit_history_file = new File(commit_history_filename);
				if(commit_history_file.exists())
					commit_history_file.delete();
				
				commit_history_file.createNewFile();
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(commit_history_file));
				out.writeObject(commit_history);
				out.close();
				return true;
				
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
	}
	
	public static final boolean saveLinks_Commit_History(Commit_History commit_history, String system_version_commit_id)
	{
		//INPUT ERROR CONTROL
		if(commit_history == null)
		{
			String response = "COMMIT HISTORY NOT FOUND->NULL";
			System.out.println(response);
			return false;
		}
		else
		{
			if(saveLink_Author_Commit(commit_history, system_version_commit_id))
			{
				if(saveLink_Commit_Parent(commit_history, system_version_commit_id))
					return true;
				else
					return false;
			}
			else
				return false;
		}
	}
	
	public static final void loadLinks_Commit_History(Commit_History commit_history, String system_version_commit_id)
	{
		//INPUT ERROR CONTROL
		if(commit_history == null)
		{
			String response = "COMMIT HISTORY NOT FOUND->NULL";
			System.out.println(response);
			return;
		}
		else
		{
			loadLink_Author_Commit(commit_history, system_version_commit_id);
			loadLink_Commit_Parent(commit_history, system_version_commit_id);
		}
	}
	
	public static final Commit_History loadCommit_HistoryFromFile(File commit_history_file)
	{
		//INPUT ERROR CONTROL
		if(commit_history_file == null)
		{
			String response = "FILE TO READ NOT SET->NULL";
			System.out.println(response);
			return null;
		}
		else
		{
			Commit_History commit_history = null;
			if(commit_history_file.exists())
			{
				System.out.println("COMMIT HISTORY FILE FOUND -> READING...");
				ObjectInputStream in;
				try {
					in = new ObjectInputStream(new FileInputStream(commit_history_file));
					commit_history = (Commit_History)in.readObject();
					in.close();
					//TEST
					System.out.println("COMMITS READ:"+commit_history.getCommit_nodes().size());
					
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			else
			{
				System.out.println("COMMIT HISTORY FILE NOT FOUND -> "+commit_history_file.getName());
			}
			return commit_history;
		}
	}
	
	public static final boolean saveLink_Author_Commit(Commit_History commit_history, String system_version_commit_id)
	{
		//INPUT ERROR CONTROL
		if((commit_history == null)||(commit_history.getSystem() == null))
		{
			String response = "COMMIT HISTORY PROBLEM->";
			if(commit_history == null)
				response = response + "NULL";
			else if (commit_history.getSystem() == null)
				response = response + "NULL SYSTEM INFO";
			System.out.println(response);
			return false;
		}
		else if(system_version_commit_id == null)
		{
			String response = "COMMIT HISTORY PROBLEM->NULL VERSION INFO";
			System.out.println(response);
			return false;
		}
		else
		{
			ArrayList<Link_Author_Commit> links = new ArrayList<Link_Author_Commit>();

			String system_saves_folder_name = Settings.SAVED_SYSTEMS_FOLDER+commit_history.getSystem()+File.separatorChar+system_version_commit_id+File.separatorChar;
			System.out.println("->"+system_saves_folder_name);

			String commit_history_link_filename = system_saves_folder_name+COMMIT_HISTORY_LINK_AUTHOR_COMMIT_SAVE;
			System.out.println("-> ->"+commit_history_link_filename);

			try
			{
				for(Map.Entry<String, Commit_Node> entry :commit_history.getCommit_nodes().entrySet())
				{
					Commit_Node commit = entry.getValue();
					Link_Author_Commit link = new Link_Author_Commit(commit.getAuthor().getId_author(),commit.getId_commit());
					links.add(link);
				}
				
				File system_saves_folder = new File(system_saves_folder_name);
				if(!system_saves_folder.exists())
					system_saves_folder.mkdirs();
				
				File commit_history_link_file = new File(commit_history_link_filename);
				if(commit_history_link_file.exists())
					commit_history_link_file.delete();
				
				commit_history_link_file.createNewFile();
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(commit_history_link_file));
				out.writeObject(links);
				out.close();
				return true;
				
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
	}
	
	public static final void loadLink_Author_Commit(Commit_History commit_history, String system_version_commit_id)
	{
		//INPUT ERROR CONTROL
		if(commit_history == null)
		{
			String response = "COMMIT HISTORY NOT FOUND->NULL";
			System.out.println(response);
			return;
		}
		else
		{
			String system_saves_folder_name = Settings.SAVED_SYSTEMS_FOLDER+commit_history.getSystem()+File.separatorChar+system_version_commit_id+File.separatorChar;
			System.out.println("->"+system_saves_folder_name);
			
			String commit_history_link_filename = system_saves_folder_name+COMMIT_HISTORY_LINK_AUTHOR_COMMIT_SAVE;
			System.out.println("-> ->"+commit_history_link_filename);
			
			File commit_history_link_file = new File(commit_history_link_filename);
			
			if(commit_history_link_file.exists())
			{
				System.out.println("COMMIT HISTORY LINK AUTHOR-COMMIT FILE FOUND -> READING...");
				ObjectInputStream in;
				try {
					in = new ObjectInputStream(new FileInputStream(commit_history_link_file));
					@SuppressWarnings("unchecked")
					ArrayList<Link_Author_Commit> links = (ArrayList<Link_Author_Commit>)in.readObject();
					in.close();
					//TEST
					System.out.println("LINKS AUTHOR-COMMIT READ:"+links.size()+" | LINKING...");
					//LINKING
					for(Link_Author_Commit link : links)
					{
						Commit_Node commit = commit_history.getCommitByID(link.getCommit_id());
						Commit_Author author = commit_history.getAuthorByID(link.getAuthor_id());
						commit.setAuthor(author);
						author.addCommit_Node(commit);
					}
					System.out.println("LINKING...DONE!");
					
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			else
			{
				System.out.println("COMMIT HISTORY LINK AUTHOR-COMMIT FILE NOT FOUND -> "+commit_history_link_filename);
			}
		}
	}
	
	public static final boolean saveLink_Commit_Parent(Commit_History commit_history, String system_version_commit_id)
	{
		//INPUT ERROR CONTROL
		if((commit_history == null)||(commit_history.getSystem() == null))
		{
			String response = "COMMIT HISTORY PROBLEM->";
			if(commit_history == null)
				response = response + "NULL";
			else if (commit_history.getSystem() == null)
				response = response + "NULL SYSTEM INFO";
			System.out.println(response);
			return false;
		}
		else if(system_version_commit_id == null)
		{
			String response = "COMMIT HISTORY PROBLEM->NULL VERSION INFO";
			System.out.println(response);
			return false;
		}
		else
		{
			ArrayList<Link_Commit_Parent> links = new ArrayList<Link_Commit_Parent>();

			String system_saves_folder_name = Settings.SAVED_SYSTEMS_FOLDER+commit_history.getSystem()+File.separatorChar+system_version_commit_id+File.separatorChar;
			System.out.println("->"+system_saves_folder_name);

			String commit_history_link_filename = system_saves_folder_name+COMMIT_HISTORY_LINK_COMMIT_PARENT_SAVE;
			System.out.println("-> ->"+commit_history_link_filename);

			try
			{
				for(Map.Entry<String, Commit_Node> entry :commit_history.getCommit_nodes().entrySet())
				{
					Commit_Node commit = entry.getValue();
					for(Commit_Node parent : commit.getParents())
					{
						Link_Commit_Parent link = new Link_Commit_Parent(commit.getId_commit(),parent.getId_commit());
						links.add(link);
					}
				}

				File system_saves_folder = new File(system_saves_folder_name);
				if(!system_saves_folder.exists())
					system_saves_folder.mkdirs();
				
				File commit_history_link_file = new File(commit_history_link_filename);
				if(commit_history_link_file.exists())
					commit_history_link_file.delete();
				
				commit_history_link_file.createNewFile();
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(commit_history_link_file));
				out.writeObject(links);
				out.close();
				return true;
				
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
	}
	
	public static final void loadLink_Commit_Parent(Commit_History commit_history, String system_version_commit_id)
	{
		//INPUT ERROR CONTROL
		if(commit_history == null)
		{
			String response = "COMMIT HISTORY NOT FOUND->NULL";
			System.out.println(response);
			return;
		}
		else
		{
			String system_saves_folder_name = Settings.SAVED_SYSTEMS_FOLDER+commit_history.getSystem()+File.separatorChar+system_version_commit_id+File.separatorChar;
			System.out.println("->"+system_saves_folder_name);
			
			String commit_history_link_filename = system_saves_folder_name+COMMIT_HISTORY_LINK_COMMIT_PARENT_SAVE;
			System.out.println("-> ->"+commit_history_link_filename);
			
			File commit_history_link_file = new File(commit_history_link_filename);
			
			if(commit_history_link_file.exists())
			{
				System.out.println("COMMIT HISTORY LINK COMMIT-PARENT FILE FOUND -> READING...");
				ObjectInputStream in;
				try {
					in = new ObjectInputStream(new FileInputStream(commit_history_link_file));
					@SuppressWarnings("unchecked")
					ArrayList<Link_Commit_Parent> links = (ArrayList<Link_Commit_Parent>)in.readObject();
					in.close();
					//TEST
					System.out.println("LINKS COMMIT-PARENT READ:"+links.size()+" | LINKING...");
					//LINKING
					for(Link_Commit_Parent link : links)
					{
						Commit_Node commit = commit_history.getCommitByID(link.getCommit_id());
						Commit_Node parent = commit_history.getCommitByID(link.getParent_id());
						commit.addParent(parent);
						parent.addChild(commit);
					}
					System.out.println("LINKING...DONE!");
					
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			else
			{
				System.out.println("COMMIT HISTORY LINK COMMIT-PARENT FILE NOT FOUND -> "+commit_history_link_filename);
			}
		}
	}

	public static final boolean saveDiffs_Commit_History(Commit_History commit_history, String system_version_commit_id)
	{
		//INPUT ERROR CONTROL
		if((commit_history == null)||(commit_history.getSystem() == null))
		{
			String response = "COMMIT HISTORY PROBLEM->";
			if(commit_history == null)
				response = response + "NULL";
			else if (commit_history.getSystem() == null)
				response = response + "NULL SYSTEM INFO";
			System.out.println(response);
			return false;
		}
		else if(system_version_commit_id == null)
		{
			String response = "COMMIT HISTORY PROBLEM->NULL VERSION INFO";
			System.out.println(response);
			return false;
		}
		else
		{
			ArrayList<Diff_Save> diffs = new ArrayList<Diff_Save>();
			
			String system_saves_folder_name = Settings.SAVED_SYSTEMS_FOLDER+commit_history.getSystem()+File.separatorChar+system_version_commit_id+File.separatorChar;
			System.out.println("->"+system_saves_folder_name);

			String commit_history_diffs_filename = system_saves_folder_name+COMMIT_HISTORY_DIFFS_SAVE;
			System.out.println("-> ->"+commit_history_diffs_filename);

			try
			{
				for(Map.Entry<String, Commit_Node> entry :commit_history.getCommit_nodes().entrySet())
				{
					Commit_Node commit = entry.getValue();
					for(File_Change diff : commit.getFile_changes())
					{
						Diff_Save diff_save = new Diff_Save(diff);
						diffs.add(diff_save);
					}
				}
				
				File system_saves_folder = new File(system_saves_folder_name);
				if(!system_saves_folder.exists())
					system_saves_folder.mkdirs();
				
				File commit_history_diffs_file = new File(commit_history_diffs_filename);
				if(commit_history_diffs_file.exists())
					commit_history_diffs_file.delete();

				commit_history_diffs_file.createNewFile();
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(commit_history_diffs_file));
				out.writeObject(diffs);
				out.close();
				return true;
				
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
	}
	
	//LOADIFFS HERE -> NEEDS CLONES
	public static void loadDiffs_Commit_History(Commit_History commit_history, String system_version_commit_id, Report_NiCad5 report)
	{
		//INPUT ERROR CONTROL
		if((commit_history == null)||(commit_history.getSystem() == null)||(commit_history.getCommit_nodes() == null))
		{
			String response = "COMMIT HISTORY PROBLEM->";
			if(commit_history == null)
				response = response + "NULL";
			else if (commit_history.getSystem() == null)
				response = response + "NULL SYSTEM INFO";
			else
				response = response + "EMPTY COMMIT LIST";
			System.out.println(response);
			return;
		}
		else if(system_version_commit_id == null)
		{
			String response = "COMMIT HISTORY PROBLEM->NULL VERSION INFO";
			System.out.println(response);
			return;
		}
		else
		{
			String system_saves_folder_name = Settings.SAVED_SYSTEMS_FOLDER+commit_history.getSystem()+File.separatorChar+system_version_commit_id+File.separatorChar;
			System.out.println("->"+system_saves_folder_name);

			String commit_history_diffs_filename = system_saves_folder_name+COMMIT_HISTORY_DIFFS_SAVE;
			System.out.println("-> ->"+commit_history_diffs_filename);
			
			try
			{
				File commit_history_diffs_file = new File(commit_history_diffs_filename);
				if(!commit_history_diffs_file.exists())
					return;
				
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(commit_history_diffs_file));
				@SuppressWarnings("unchecked")
				ArrayList<Diff_Save> diffs = (ArrayList<Diff_Save>) in.readObject();
				in.close();
				//TEST
				System.out.println("DIFFS LOADED:"+diffs.size());
				if((report != null)&&(report.getClones() != null))
				{
					for(Diff_Save diff_save : diffs)
					{
						if(diff_save.getCommit_id() != null)
						{
							Commit_Node commit = commit_history.getCommitByID(diff_save.getCommit_id());
							if(commit != null)
							{
								if(diff_save.getDiff() != null)
								{
									commit.addFile_change(diff_save.getDiff());
									diff_save.getDiff().setCommit(commit);
								}
							}
						}
						//CLONES
						ArrayList<Integer> clones_ids = diff_save.getClones_ids();
						if(clones_ids != null)
						{
							for(Integer clone_id : clones_ids)
							{
								Clone clone = report.getCloneById(clone_id);
								if(clone != null)
								{
									if(diff_save.getDiff() != null)
									{
										clone.setChange_modify_clone(diff_save.getDiff());
										diff_save.getDiff().addClone(clone);
									}
								}
							}
						}
					}
				}
				else
				{
					for(Diff_Save diff_save : diffs)
					{
						if(diff_save.getCommit_id() != null)
						{
							Commit_Node commit = commit_history.getCommitByID(diff_save.getCommit_id());
							if(commit != null)
							{
								if(diff_save.getDiff() != null)
								{
									commit.addFile_change(diff_save.getDiff());
									diff_save.getDiff().setCommit(commit);
								}
							}
						}
					}
				}
			}
			catch (IOException e) {
				e.printStackTrace();
				return;
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	public static final boolean saveNiCadReport(Report_NiCad5 report)
	{
		//INPUT ERROR CONTROL
		if((report == null)||(report.getSystem() == null)||(report.getCommit_id_version() == null))
		{
			String response = "NICAD CLONE REPORT PROBLEM->";
			if(report == null)
				response = response + "NULL";
			else if (report.getSystem() == null)
				response = response + "NULL SYSTEM INFO";
			else if (report.getCommit_id_version() == null)
				response = response + "NULL VERSION INFO";
			System.out.println(response);
			return false;
		}
		else
		{
			String system_saves_folder_name = Settings.SAVED_SYSTEMS_FOLDER+report.getSystem()+File.separatorChar+report.getCommit_id_version()+File.separatorChar;
			System.out.println("->"+system_saves_folder_name);

			String clones_report_filename = system_saves_folder_name+CLONES_REPORT_SAVE;
			//String clones_report_filename = "saves/clones_report.save";
			System.out.println("-> ->"+clones_report_filename);

			try
			{
				File system_saves_folder = new File(system_saves_folder_name);
				if(!system_saves_folder.exists())
					system_saves_folder.mkdirs();
				
				File clones_report_file = new File(clones_report_filename);
				if(clones_report_file.exists())
					clones_report_file.delete();
				
				clones_report_file.createNewFile();
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(clones_report_file));
				out.writeObject(report);
				out.close();
				return true;
				
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
	}
	
	//LOADREPORT HERE -> CLONES/CLASSES UNLINKED
	public static Report_NiCad5 loadNiCadReport(Settings settings)
	{
		//INPUT ERROR CONTROL
		if((settings == null)||(settings.getSystem_name() == null)||(settings.getSystem_version_commit_id() == null))
		{
			String response = "NICAD CLONE REPORT PROBLEM->";
			if(settings == null)
				response = response + "SETTINGS NULL";
			else if (settings.getSystem_name()  == null)
				response = response + "NULL SYSTEM NAME";
			else if (settings.getSystem_version_commit_id() == null)
				response = response + "NULL VERSION INFO";
			System.out.println(response);
			return null;
		}
		else
		{
			String system_saves_folder_name = Settings.SAVED_SYSTEMS_FOLDER+settings.getSystem_name()+File.separatorChar+settings.getSystem_version_commit_id()+File.separatorChar;
			System.out.println("->"+system_saves_folder_name);

			String clones_report_filename = system_saves_folder_name+CLONES_REPORT_SAVE;
			//String clones_report_filename = "saves/clones_report.save";
			System.out.println("-> ->"+clones_report_filename);
			
			try
			{
				File clones_report_file = new File(clones_report_filename);
				if(!clones_report_file.exists())
					return null;
				
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(clones_report_file));
				Report_NiCad5 report = (Report_NiCad5) in.readObject();
				in.close();
				return report;
			}
			catch (IOException e) {
				e.printStackTrace();
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public static final boolean saveLinks_NiCadReport(Report_NiCad5 report)
	{
		//INPUT ERROR CONTROL
		if((report == null)||(report.getSystem() == null)||(report.getCommit_id_version() == null))
		{
			String response = "NICAD CLONE REPORT PROBLEM->";
			if(report == null)
				response = response + "NULL";
			else if (report.getSystem() == null)
				response = response + "NULL SYSTEM INFO";
			else if (report.getCommit_id_version() == null)
				response = response + "NULL VERSION INFO";
			System.out.println(response);
			return false;
		}
		else
		{
			String system_saves_folder_name = Settings.SAVED_SYSTEMS_FOLDER+report.getSystem()+File.separatorChar+report.getCommit_id_version()+File.separatorChar;
			System.out.println("->"+system_saves_folder_name);

			String clones_link_clone_author_filename = system_saves_folder_name+CLONES_LINK_CLONE_AUTHOR_SAVE;
			//String clones_link_clone_author_filename = "saves/clones_link_clone_author.save";
			System.out.println("-> ->"+clones_link_clone_author_filename);
			
			String clones_link_clone_class_filename = system_saves_folder_name+CLONES_LINK_CLONE_CLASS_SAVE;
			//String clones_link_clone_class_filename = "saves/clones_link_clone_class.save";
			System.out.println("-> ->"+clones_link_clone_class_filename);
			
			String clones_link_clone_pair_filename = system_saves_folder_name+CLONES_LINK_CLONE_PAIR_SAVE;
			//String clones_link_clone_pair_filename = "saves/clones_link_clone_pair.save";
			System.out.println("-> ->"+clones_link_clone_pair_filename);

			ArrayList<Link_Clone_Author> links_clone_author = new ArrayList<Link_Clone_Author>();
			ArrayList<Link_Clone_Class> links_clone_class = new ArrayList<Link_Clone_Class>();
			ArrayList<Link_Clone_Pair> links_clone_pair = new ArrayList<Link_Clone_Pair>();

			try
			{
				ArrayList<Clone> clones = new ArrayList<Clone>(report.getClones().values());
				
				for(Clone clone : clones)
				{
					if(clone.getAuthor() != null)
						links_clone_author.add(new Link_Clone_Author(clone.getId_clone(),clone.getAuthor().getId_author()));
					
					if(clone.getClone_class() != null)
						links_clone_class.add(new Link_Clone_Class(clone.getId_clone(),clone.getClone_class().getId_class()));
					
					if(clone.getPairs() != null)
					{
						for(ClonePair pair : clone.getPairs())
						{
							if((pair.getFirst_clone() != null)&&(pair.getSecond_clone() != null))
							{
								Link_Clone_Pair pair_link = new Link_Clone_Pair(pair.getSimilarity(), pair.getNumber_of_lines(), pair.getFirst_clone().getId_clone(), pair.getSecond_clone().getId_clone());
								if(!links_clone_pair.contains(pair_link))
									links_clone_pair.add(pair_link);
							}
						}
					}
				}
				
				//CREATE SAVE FOLDER -IF NEEDED
				File system_saves_folder = new File(system_saves_folder_name);
				if(!system_saves_folder.exists())
					system_saves_folder.mkdirs();
				
				//DELETE PREVIOUS FILE -IF NEEDED
				File clones_link_clone_author_file = new File(clones_link_clone_author_filename);
				if(clones_link_clone_author_file.exists())
					clones_link_clone_author_file.delete();

				clones_link_clone_author_file.createNewFile();
				
				ObjectOutputStream out_author = new ObjectOutputStream(new FileOutputStream(clones_link_clone_author_file));
				out_author.writeObject(links_clone_author);
				out_author.close();

				//DELETE PREVIOUS FILE -IF NEEDED
				File clones_link_clone_class_file = new File(clones_link_clone_class_filename);
				if(clones_link_clone_class_file.exists())
					clones_link_clone_class_file.delete();

				clones_link_clone_class_file.createNewFile();
				
				ObjectOutputStream out_class = new ObjectOutputStream(new FileOutputStream(clones_link_clone_class_file));
				out_class.writeObject(links_clone_class);
				out_class.close();

				//DELETE PREVIOUS FILE -IF NEEDED
				File clones_link_clone_pair_file = new File(clones_link_clone_pair_filename);
				if(clones_link_clone_pair_file.exists())
					clones_link_clone_pair_file.delete();

				clones_link_clone_pair_file.createNewFile();
				
				ObjectOutputStream out_pair = new ObjectOutputStream(new FileOutputStream(clones_link_clone_pair_file));
				out_pair.writeObject(links_clone_pair);
				out_pair.close();
				
				return true;

			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
	}
	
	public static final void loadLinks_NiCadReport(Report_NiCad5 report, Commit_History commit_history)
	{
		//INPUT ERROR CONTROL
		if((report == null)||(report.getSystem() == null)||(report.getCommit_id_version() == null))
		{
			String response = "NICAD CLONE REPORT PROBLEM->";
			if(report == null)
				response = response + "NULL";
			else if (report.getSystem() == null)
				response = response + "NULL SYSTEM INFO";
			else if (report.getCommit_id_version() == null)
				response = response + "NULL VERSION INFO";
			System.out.println(response);
			return;
		}
		else
		{
			String system_saves_folder_name = Settings.SAVED_SYSTEMS_FOLDER+report.getSystem()+File.separatorChar+report.getCommit_id_version()+File.separatorChar;
			System.out.println("->"+system_saves_folder_name);

			String clones_link_clone_author_filename = system_saves_folder_name+CLONES_LINK_CLONE_AUTHOR_SAVE;
			//String clones_link_clone_author_filename = "saves/clones_link_clone_author.save";
			System.out.println("-> ->"+clones_link_clone_author_filename);
			
			String clones_link_clone_class_filename = system_saves_folder_name+CLONES_LINK_CLONE_CLASS_SAVE;
			//String clones_link_clone_class_filename = "saves/clones_link_clone_class.save";
			System.out.println("-> ->"+clones_link_clone_class_filename);
			
			String clones_link_clone_pair_filename = system_saves_folder_name+CLONES_LINK_CLONE_PAIR_SAVE;
			//String clones_link_clone_pair_filename = "saves/clones_link_clone_pair.save";
			System.out.println("-> ->"+clones_link_clone_pair_filename);
			
			try
			{
				//LINK CLONE AUTHOR
				File clones_link_clone_author_file = new File(clones_link_clone_author_filename);
				if(clones_link_clone_author_file.exists())
				{
					ObjectInputStream in_author = new ObjectInputStream(new FileInputStream(clones_link_clone_author_file));
					@SuppressWarnings("unchecked")
					ArrayList<Link_Clone_Author> links_clone_author = (ArrayList<Link_Clone_Author>) in_author.readObject();
					in_author.close();
					
					if((commit_history != null)&&(commit_history.getCommit_authors() != null))
					{
						for(Link_Clone_Author link_clone_author : links_clone_author)
						{
							Clone clone = report.getCloneById(link_clone_author.getClone_id());
							Commit_Author author = commit_history.getAuthorByID(link_clone_author.getAuthor_id());
							if(clone != null)
								if(author != null)
								{
									clone.setAuthor(author);
									author.addClone(clone);
								}
						}
					}
				}

				//LINK CLONE CLASS
				File clones_link_clone_class_file = new File(clones_link_clone_class_filename);
				if(clones_link_clone_class_file.exists())
				{
					ObjectInputStream in_class = new ObjectInputStream(new FileInputStream(clones_link_clone_class_file));
					@SuppressWarnings("unchecked")
					ArrayList<Link_Clone_Class> links_clone_class = (ArrayList<Link_Clone_Class>) in_class.readObject();
					in_class.close();
					
					for(Link_Clone_Class link_clone_class : links_clone_class)
					{
						Clone clone = report.getCloneById(link_clone_class.getClone_id());
						CloneClass clone_class = report.getClassById(link_clone_class.getClass_id());
						if(clone != null)
							if(clone_class != null)
							{
								clone.setClone_class(clone_class);
								clone_class.addClone(clone);
							}
					}
				}

				//LINK CLONE PAIR
				File clones_link_clone_pair_file = new File(clones_link_clone_pair_filename);
				if(clones_link_clone_pair_file.exists())
				{
					ObjectInputStream in_pair = new ObjectInputStream(new FileInputStream(clones_link_clone_pair_file));
					@SuppressWarnings("unchecked")
					ArrayList<Link_Clone_Pair> links_clone_pair = (ArrayList<Link_Clone_Pair>) in_pair.readObject();
					in_pair.close();
					
					for(Link_Clone_Pair link_clone_pair : links_clone_pair)
					{
						Clone first_clone = report.getCloneById(link_clone_pair.getFirst_clone());
						Clone second_clone = report.getCloneById(link_clone_pair.getSecond_clone());
						if((first_clone != null)&&(second_clone != null))
						{
							ClonePair clone_pair = new ClonePair(link_clone_pair.getSimilarity(), link_clone_pair.getNumber_of_lines(), first_clone, second_clone);
							first_clone.addPair(clone_pair);
							second_clone.addPair(clone_pair);
						}
					}
				}
			}
			catch (IOException e) {
				e.printStackTrace();
				return;
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
}
