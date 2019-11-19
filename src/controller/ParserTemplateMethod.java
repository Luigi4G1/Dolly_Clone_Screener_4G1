package controller;

import java.io.File;
import java.util.HashMap;

import builder.Commit_History_Builder;
import builder.Commit_Parser;
import builder.Report_NiCad5_Builder;
import model.Commit_History;
import model.Commit_parse;
import model.Report_NiCad5;
import persistence.Settings;
import persistence.System_Analysis;

public class ParserTemplateMethod {
	
	public static System_Analysis parseSystemAnalysis(Settings settings)
	{
		//INPUT ERROR CONTROL
		if(settings == null)
		{
			System.out.println("NO SETTINGS FILE SET");
			return null;
		}

		System_Analysis system_analysis = null;
		//COMMITS
		Commit_History commit_history = parseCommitHistory(settings);
		//NEXT -> CLONES
		Report_NiCad5 nicad_report = parseClones(settings);
		
		system_analysis = new System_Analysis(settings, commit_history, nicad_report);

		return system_analysis;
	}

	private static Report_NiCad5 parseClones(Settings settings) {
		//INPUT ERROR CONTROL
		if(settings == null)
		{
			System.out.println("NO SETTINGS FILE SET");
			return null;
		}
		if((settings.getNicad_settings() == null)||(settings.getNicad_settings().getRoot_fullpath() == null)||(settings.getNicad_settings().getAnalyzedsystems_root_folder() == null)||(settings.getNicad_settings().getClone_classes_file() == null)||(settings.getNicad_settings().getVersion_commit() == null)) 
		{
			System.out.println("CAN'T LOAD CLONES BY FILE->NICAD REPORT\n"+settings.getNicad_settings().toString());
			return null;
		}
		
		Report_NiCad5_Builder builder = new Report_NiCad5_Builder(settings.getNicad_settings().getRoot_fullpath(), settings.getNicad_settings().getAnalyzedsystems_root_folder());
		String parsing_folder_path = Settings.PARSING_SYSTEMS_FOLDER
				+ settings.getSystem_name() + File.separator
				+ settings.getSystem_version_commit_id() +File.separator;
		Report_NiCad5 report = builder.build_no_comments(parsing_folder_path + settings.getNicad_settings().getClone_classes_file(), settings.getNicad_settings().getVersion_commit());
		if(settings.getNicad_settings().getClone_pairs_file() != null)
		{
			builder.read_clone_pairs_report_xml(parsing_folder_path+settings.getNicad_settings().getClone_pairs_file(), report);
		}
		else
			System.out.println("----->ATTENTION!CAN'T FIND CLONES PAIRS FILE->NO PAIR SET");
		return report;
	}

	private static Commit_History parseCommitHistory(Settings settings)
	{
		//INPUT ERROR CONTROL
		if(settings == null)
		{
			System.out.println("NO SETTINGS FILE SET");
			return null;
		}
		if((settings.getRepo_info() == null)||(settings.getRepo_info().getSystem_name() == null)||(settings.getSystem_version_commit_id() == null)) 
		{
			System.out.println("CAN'T LOAD COMMITS BY FILE->COMMIT HISTORY");
			return null;
		}

		String commitlist_filepath = Settings.PARSING_SYSTEMS_FOLDER+settings.getRepo_info().getSystem_name()+File.separatorChar+settings.getSystem_version_commit_id()+File.separatorChar+Settings.COMMIT_LIST_FILE;
		File commit_story_text = new File(commitlist_filepath);
		try
		{
			/*
			Scanner sc_commit_text = new Scanner(commit_story_text);
			HashMap<String, Commit_parse> commit_map = Commit_Parser.read_commit_history_file(sc_commit_text);
			*/
			HashMap<String, Commit_parse> commit_map = Commit_Parser.read_commit_history_file(commit_story_text);
			
			Commit_History commit_history = Commit_History_Builder.commit_history_builder(settings.getRepo_info(), commit_map);
			return commit_history;
		}/*
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
			System.out.println("#FILE NOT FOUND EXCEPTION#");
			return null;
		}*/
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
