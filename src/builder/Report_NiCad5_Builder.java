package builder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import model.Clone;
import model.CloneClass;
import model.ClonePair;
import model.Report_NiCad5;
import utils.CommentRemover;
import utils.FileUtils4G1;
import utils.StringUtils;

public class Report_NiCad5_Builder {
	
	//NiCad Clone LINES file HTML TAGS
	//USED by:
	//read_clone_lines_report_file();
	//Example:
	//	NICAD_CLONE_LINES_FILE = "./src/files/JHotDraw54b1_functions-blind-clones-0.30-classes-withsource.html";
	public static final String TAG_CLONE_CLASS_START = "<h3>";
	public static final String TAG_CLONE_CLASS_END = "</table>";
	
	public static final String TAG_CLONE_INFO = "<tr><td>";
	public static final String TAG_CLONE_LINES_START = "<pre>";
	public static final String TAG_CLONE_LINES_END = "</pre>";
	

	public Report_NiCad5_Builder(String nicad_root_fullpath, String analyzedsystems_root_folder_name) {
		//ADDING END CHAR "/"
		nicad_root_fullpath = FileUtils4G1.filePathNormalize(nicad_root_fullpath.trim());
		if(!nicad_root_fullpath.endsWith(File.separator))
			nicad_root_fullpath = nicad_root_fullpath+File.separator;
		this.nicad_root_fullpath = nicad_root_fullpath;
		/*
		try
		{
			//REMOVE STARTING-ENDING with "/"
			while(analyzedsystems_root_folder_name.startsWith("/"))
				analyzedsystems_root_folder_name = analyzedsystems_root_folder_name.substring(1);
			while(analyzedsystems_root_folder_name.endsWith("/"))
				analyzedsystems_root_folder_name = analyzedsystems_root_folder_name.substring(0,analyzedsystems_root_folder_name.length()-1);
		}
		catch(IndexOutOfBoundsException e)
		{
			e.printStackTrace();
			analyzedsystems_root_folder_name = null;
		}
		*/
		analyzedsystems_root_folder_name = analyzedsystems_root_folder_name.trim();
		analyzedsystems_root_folder_name = FileUtils4G1.removeStartingSeparator(analyzedsystems_root_folder_name);
		analyzedsystems_root_folder_name = FileUtils4G1.removeTrailingSeparator(analyzedsystems_root_folder_name);
		this.analyzedsystems_root_folder_name = analyzedsystems_root_folder_name;
	}

	//ARGUMENTS:
	//1)String nicad_clone_classes_file -> NiCad Clone CLASSES file XML
	//	Example:
	//		NICAD_CLONE_LINES_FILE = "./src/files/JHotDraw54b1_functions-blind-clones-0.30-classes-withsource.html";
	//2)String commit_id_version -> LAST COMMIT ID of the ANALYZED VERSION
	//	Example:
	//		commit_id_version = "d97b6a0685d59143372bb392ab591dd8dd840b61"
	//NOTE:CLONE LINES directly from the ORIGIN FILES analyzed by NiCad
	public Report_NiCad5 build(String nicad_clone_classes_file, String commit_id_version)
	{
		Report_NiCad5 report = read_clone_classes_report_xml(nicad_clone_classes_file, commit_id_version);
		if(report != null)
		{
			report.setCommit_id_version(commit_id_version);
			for(Clone clone : report.getClones().values())
			{
				getCloneLinesFromOriginFileAnalyzedByNiCad(clone, report.getSystem(), report.getCommit_id_version());
			}
		}
		return report;
	}
	
	//ARGUMENTS:
	//1)String nicad_clone_classes_file -> NiCad Clone CLASSES file XML
	//	Example:
	//		NICAD_CLONE_LINES_FILE = "./src/files/JHotDraw54b1_functions-blind-clones-0.30-classes-withsource.html";
	//2)String commit_id_version -> LAST COMMIT ID of the ANALYZED VERSION
	//	Example:
	//		commit_id_version = "d97b6a0685d59143372bb392ab591dd8dd840b61"
	//NOTE:CLONE LINES directly from the ORIGIN FILES analyzed by NiCad
	//	with ALL COMMENTS REMOVED
	public Report_NiCad5 build_no_comments(String nicad_clone_classes_file, String commit_id_version)
	{
		Report_NiCad5 report = read_clone_classes_report_xml(nicad_clone_classes_file, commit_id_version);
		if(report != null)
		{
			report.setCommit_id_version(commit_id_version);
			for(Clone clone : report.getClones().values())
			{
				getCloneLinesFromOriginFileAnalyzedByNiCadWithoutComments(clone, report.getSystem(), report.getCommit_id_version());
			}
		}
		return report;
	}
	
	//ARGUMENTS:
	//1)String nicad_clone_classes_file -> NiCad Clone CLASSES file XML
	//	Example:
	//		NICAD_CLONE_LINES_FILE = "./src/files/JHotDraw54b1_functions-blind-clones-0.30-classes-withsource.html";
	//2)String nicad_clone_lines_file_html -> NiCad Clone LINES file HTML
	//	Example:
	//		NICAD_CLONE_LINES_FILE = "./src/files/JHotDraw54b1_functions-blind-clones-0.30-classes-withsource.html";
	//3)String commit_id_version -> LAST COMMIT ID of the ANALYZED VERSION
	//	Example:
	//		commit_id_version = "d97b6a0685d59143372bb392ab591dd8dd840b61"
	public Report_NiCad5 build_from_html(String nicad_clone_classes_file, String nicad_clone_lines_file_html, String commit_id_version)
	{
		Report_NiCad5 report = read_clone_classes_report_xml(nicad_clone_classes_file, commit_id_version);
		if(report != null)
		{
			report.setCommit_id_version(commit_id_version);
			read_clone_lines_report_file(nicad_clone_lines_file_html, report);
		}
		return report;
	}

	//ARGUMENTS:
	//1)String filename -> NiCad Clone PAIRS file XML
	//	Example:
	//		NICAD_CLONE_PAIRS_FILE_TEST = "./src/files/JHotDraw54b1_functions-blind-clones-0.30.xml";
	public void read_clone_pairs_report_xml(String filename, Report_NiCad5 report)
	{
		//INPUT ERROR CONTROL
		if((filename == null)||(filename.trim().isEmpty()))
		{
			String response = "FILENAME NOT SET->";
			if(filename == null)
				response = response + "NULL";
			else
				response = response + "EMPTY";
			System.out.println(response);
			return;
		}
		if(report == null)
		{
			String response = "REPORT NOT SET->NULL";
			System.out.println(response);
			return;
		}
		//ATTRIBUTE ERROR CONTROL
		if((analyzedsystems_root_folder_name == null)||(analyzedsystems_root_folder_name.trim().isEmpty()))
		{
			String response = "ANALYZED SYSTEMS ROOT FOLDER NAME NOT SET->";
			if(analyzedsystems_root_folder_name == null)
				response = response + "NULL";
			else
				response = response + "EMPTY";
			System.out.println(response);
			return;
		}
		//NORMALIZE FILEPATH
		filename = FileUtils4G1.filePathNormalize(filename.trim());
		
		DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder domparser;
		Document doc = null;
		try {
			domparser = dbfactory.newDocumentBuilder();
			doc = domparser.parse(new File(filename));
			doc.getDocumentElement().normalize();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		NodeList nodi = doc.getElementsByTagName("clones");

		Element elemento = (Element) nodi.item(0);

		Node node_systeminfo = elemento.getElementsByTagName("systeminfo").item(0);

		String processor = node_systeminfo.getAttributes().getNamedItem("processor").getNodeValue();
		String system = node_systeminfo.getAttributes().getNamedItem("system").getNodeValue();
		String granularity = node_systeminfo.getAttributes().getNamedItem("granularity").getNodeValue();
		String threshold_string = node_systeminfo.getAttributes().getNamedItem("threshold").getNodeValue();
		threshold_string = threshold_string.substring(0, threshold_string.indexOf("%"));
		String min_string = node_systeminfo.getAttributes().getNamedItem("minlines").getNodeValue();
		String max_string = node_systeminfo.getAttributes().getNamedItem("maxlines").getNodeValue();
		
		int threshold, minlines, maxlines;
		
		try
		{
			threshold = Integer.parseInt(threshold_string);
		}
		catch(NumberFormatException e)
		{
			e.printStackTrace();
			threshold = 0;
		}
		
		try
		{
			minlines = Integer.parseInt(min_string);
		}
		catch(NumberFormatException e)
		{
			e.printStackTrace();
			minlines = 0;
		}
		
		try
		{
			maxlines = Integer.parseInt(max_string);
		}
		catch(NumberFormatException e)
		{
			e.printStackTrace();
			maxlines = 0;
		}

		//CONTROLLING REPORT PARAMETERS - [PROCESSOR, SYSTEM, GRANULARITY, THRESHOLD, MINLINES, MAXLINES]
		if((report.getProcessor() == null )||(!report.getProcessor().equalsIgnoreCase(processor)))
		{
			System.out.println("WRONG REPORT FILE - PROCESSOR["+report.getProcessor()+"]-> READ["+processor+"]");
			return;
		}
		if((report.getSystem() == null )||(!report.getSystem().equalsIgnoreCase(system)))
		{
			System.out.println("WRONG REPORT FILE - SYSTEM["+report.getSystem()+"]-> READ["+system+"]");
			return;
		}
		if((report.getGranularity() == null )||(!report.getGranularity().equalsIgnoreCase(granularity)))
		{
			System.out.println("WRONG REPORT FILE - GRANULARITY["+report.getGranularity()+"]-> READ["+granularity+"]");
			return;
		}
		if(report.getThreshold() != threshold)
		{
			System.out.println("WRONG REPORT FILE - THRESHOLD["+report.getThreshold()+"]-> READ["+threshold+"]");
			return;
		}
		if(report.getMinlines() != minlines)
		{
			System.out.println("WRONG REPORT FILE - MINLINES["+report.getMinlines()+"]-> READ["+minlines+"]");
			return;
		}
		if(report.getMaxlines() != maxlines)
		{
			System.out.println("WRONG REPORT FILE - MAXLINES["+report.getMaxlines()+"]-> READ["+maxlines+"]");
			return;
		}

		NodeList node_clone_pairs_list = elemento.getElementsByTagName("clone");
		System.out.println("PAIRS:"+node_clone_pairs_list.getLength());
		for(int index = 0; index<node_clone_pairs_list.getLength(); index++)
		{
			Node clone_pair_node = node_clone_pairs_list.item(index);
			String similarity_string = clone_pair_node.getAttributes().getNamedItem("similarity").getNodeValue();
			String number_of_lines_string = clone_pair_node.getAttributes().getNamedItem("nlines").getNodeValue();
			System.out.println("SIM:"+similarity_string+" - NLINES:"+number_of_lines_string);
			int similarity, number_of_lines;
			try
			{
				similarity = Integer.parseInt(similarity_string);
			}
			catch(NumberFormatException e)
			{
				e.printStackTrace();
				similarity = 0;
			}
			try
			{
				number_of_lines = Integer.parseInt(number_of_lines_string);
			}
			catch(NumberFormatException e)
			{
				e.printStackTrace();
				number_of_lines = 0;
			}

			NodeList pair_clones = clone_pair_node.getChildNodes();
			
			Clone first_clone = null;
			Clone second_clone = null;
			int num_clone_for = 0;
			for(int i = 0; i<pair_clones.getLength(); i++)
			{
				Node clone_node = pair_clones.item(i);
				if(clone_node.getNodeName().equalsIgnoreCase("source"))
				{
					num_clone_for++;
					String id_clone_string = clone_node.getAttributes().getNamedItem("pcid").getNodeValue();
					String filename_clone = clone_node.getAttributes().getNamedItem("file").getNodeValue();
					String start_line_string = clone_node.getAttributes().getNamedItem("startline").getNodeValue();
					String end_line_string = clone_node.getAttributes().getNamedItem("endline").getNodeValue();
					int id_clone, start_line, end_line;
					try
					{
						id_clone = Integer.parseInt(id_clone_string);
					}
					catch(NumberFormatException e)
					{
						e.printStackTrace();
						id_clone = 0;
					}
					try
					{
						start_line = Integer.parseInt(start_line_string);
					}
					catch(NumberFormatException e)
					{
						e.printStackTrace();
						start_line = 0;
					}
					try
					{
						end_line = Integer.parseInt(end_line_string);
					}
					catch(NumberFormatException e)
					{
						e.printStackTrace();
						end_line = 0;
					}
					//NORMALIZE FILEPATH
					filename_clone = FileUtils4G1.filePathNormalize(filename_clone.trim());
					String appendixToRemove = getAppendixToRemoveMultiVersionsAnalysis(report.getSystem(), report.getCommit_id_version());
					//AVOIDING IndexOutOfBoundsException
					if((appendixToRemove == null)||(appendixToRemove.length()>filename_clone.length()))
					{
						System.out.println("ERROR! UNABLE TO REMOVE APPENDIX of CLONE FILENAME -> ID["+id_clone+"]");
						//NOTE: USING break; -> ANALYZING NEXT CLONE of the PAIR NODE -> UNNECESSARY -> PURPOSE: TESTING ALL CLONE NODES
						break;
					}
					filename_clone = filename_clone.substring(appendixToRemove.length());
					
					Clone clone = report.getCloneById(id_clone);
					boolean clone_error = false;
					if(clone == null)
					{
						System.out.println("ERROR! CLONE NOT FOUND -> ID["+id_clone+"]");
						clone_error = true;
						//COMMENTED "return" -> SKIP -> ANALYZING NEXT CLONE
						//return;
						//NOTE: USING break; -> ANALYZING NEXT CLONE of the PAIR NODE -> UNNECESSARY -> PURPOSE: TESTING ALL CLONE NODES
						break;
					}
					else
					{
						//CONTROLLING CLONE PARAMETERS - [FILENAME, STARTLINE, ENDLINE]
						if(clone.getFilename() == null )
						{
							System.out.println("WRONG CLONE INFO[ID:"+id_clone+"] - FILENAME["+clone.getFilename()+"]-> READ["+filename_clone+"]");
							clone_error = true;
							break;
						}
						else
						{
							//NORMALIZE FILEPATH
							clone.setFilename(FileUtils4G1.filePathNormalize(clone.getFilename().trim()));
							if(!clone.getFilename().equalsIgnoreCase(filename_clone))
							{
								System.out.println("WRONG CLONE INFO[ID:"+id_clone+"] - FILENAME["+clone.getFilename()+"]-> READ["+filename_clone+"]");
								clone_error = true;
								break;
							}
						}
						if(clone.getStart_line() != start_line)
						{
							System.out.println("WRONG CLONE INFO[ID:"+id_clone+"] - STARTLINE["+clone.getStart_line()+"]-> READ["+start_line+"]");
							clone_error = true;
							break;
						}
						if(clone.getEnd_line() != end_line)
						{
							System.out.println("WRONG CLONE INFO[ID:"+id_clone+"] - ENDLINE["+clone.getEnd_line()+"]-> READ["+end_line+"]");
							clone_error = true;
							break;
						}
					}
					
					if(num_clone_for==1)
					{
						if(clone_error)
							first_clone = null;
						else
							first_clone = clone;
					}
					else if(num_clone_for==2)
					{
						if(clone_error)
							second_clone = null;
						else
							second_clone = clone;
					}
				}
			}
			////////////////////////////////////////////////////////////////////
			//MUST BE a PAIR -> 2 CLONES -> 2 CHILDNODES with NodeName == "source"
			if(num_clone_for != 2)
			{
				System.out.println("WRONG FORMAT for CLONE PAIRS -> READ["+num_clone_for+" CLONES]->[MUST BE -2-]");
				//COMMENTED "return" -> SKIP PAIR -> ANALYZING NEXT PAIR
				return;
			}
			else
			{
				if((first_clone == null)||(second_clone == null))
				{
					//ERROR
					System.out.println(">ERROR! Impossible to create CLONE PAIR<");
				}
				else
				{
					ClonePair clone_pair = new ClonePair(similarity, number_of_lines, first_clone, second_clone);
					first_clone.addPair(clone_pair);
					second_clone.addPair(clone_pair);
					System.out.println(">CLONE PAIR created -> ["+first_clone.getId_clone()+"]-["+second_clone.getId_clone()+"]<");
				}
			}
			/////////////////////////////////////////////////////////////////
		}
	}
	
	//ARGUMENTS:
	//1)String filename -> NiCad Clone CLASSES file XML
	//	Example:
	//		NICAD_CLONE_LINES_FILE = "./src/files/JHotDraw54b1_functions-blind-clones-0.30-classes-withsource.html";
	//2)String commit_id_version -> LAST COMMIT ID of the ANALYZED VERSION
	//	Example:
	//		commit_id_version = "d97b6a0685d59143372bb392ab591dd8dd840b61"
	public Report_NiCad5 read_clone_classes_report_xml(String filename, String commit_id_version)
	{
		//INPUT ERROR CONTROL
		if((filename == null)||(filename.trim().isEmpty()))
		{
			String response = "FILENAME NOT SET->";
			if(filename == null)
				response = response + "NULL";
			else
				response = response + "EMPTY";
			System.out.println(response);
			return null;
		}
		if((commit_id_version == null)||(commit_id_version.trim().isEmpty()))
		{
			String response = "VERSION NOT SET->";
			if(commit_id_version == null)
				response = response + "NULL";
			else
				response = response + "EMPTY";
			System.out.println(response);
			return null;
		}
		commit_id_version = commit_id_version.trim();
		//ATTRIBUTE ERROR CONTROL
		if((analyzedsystems_root_folder_name == null)||(analyzedsystems_root_folder_name.trim().isEmpty()))
		{
			String response = "ANALYZED SYSTEMS ROOT FOLDER NAME NOT SET->";
			if(analyzedsystems_root_folder_name == null)
				response = response + "NULL";
			else
				response = response + "EMPTY";
			System.out.println(response);
			return null;
		}
		//NORMALIZE FILEPATH
		filename = FileUtils4G1.filePathNormalize(filename.trim());

		DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder domparser;
		Document doc = null;
		try {
			domparser = dbfactory.newDocumentBuilder();
			doc = domparser.parse(new File(filename));
			doc.getDocumentElement().normalize();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		NodeList nodi = doc.getElementsByTagName("clones");

		Element elemento = (Element) nodi.item(0);

		Node node_systeminfo = elemento.getElementsByTagName("systeminfo").item(0);

		String processor = node_systeminfo.getAttributes().getNamedItem("processor").getNodeValue();
		String system = node_systeminfo.getAttributes().getNamedItem("system").getNodeValue();
		String granularity = node_systeminfo.getAttributes().getNamedItem("granularity").getNodeValue();
		String threshold_string = node_systeminfo.getAttributes().getNamedItem("threshold").getNodeValue();
		threshold_string = threshold_string.substring(0, threshold_string.indexOf("%"));
		String min_string = node_systeminfo.getAttributes().getNamedItem("minlines").getNodeValue();
		String max_string = node_systeminfo.getAttributes().getNamedItem("maxlines").getNodeValue();
		
		int threshold, minlines, maxlines;
		
		try
		{
			threshold = Integer.parseInt(threshold_string);
		}
		catch(NumberFormatException e)
		{
			e.printStackTrace();
			threshold = 0;
		}
		
		try
		{
			minlines = Integer.parseInt(min_string);
		}
		catch(NumberFormatException e)
		{
			e.printStackTrace();
			minlines = 0;
		}
		
		try
		{
			maxlines = Integer.parseInt(max_string);
		}
		catch(NumberFormatException e)
		{
			e.printStackTrace();
			maxlines = 0;
		}
		
		String nclasses_string = elemento.getElementsByTagName("classinfo").item(0).getAttributes().getNamedItem("nclasses").getNodeValue();

		int num_classes;
		try
		{
			num_classes = Integer.parseInt(nclasses_string);
		}
		catch(NumberFormatException e)
		{
			e.printStackTrace();
			num_classes = 0;
		}
		
		Report_NiCad5 toReturn = new Report_NiCad5(processor, system, granularity, threshold, minlines, maxlines, num_classes);
		toReturn.setCommit_id_version(commit_id_version);

		NodeList node_classes_list = elemento.getElementsByTagName("class");
		for(int index = 0; index<node_classes_list.getLength(); index++)
		{
			Node class_node = node_classes_list.item(index);
			String id_class_string = class_node.getAttributes().getNamedItem("classid").getNodeValue();
			String clone_counter_string = class_node.getAttributes().getNamedItem("nclones").getNodeValue();
			String similarity_string = class_node.getAttributes().getNamedItem("similarity").getNodeValue();
			String number_of_lines_string = class_node.getAttributes().getNamedItem("nlines").getNodeValue();
			int id_class, clone_counter, similarity, number_of_lines;
			try
			{
				id_class = Integer.parseInt(id_class_string);
			}
			catch(NumberFormatException e)
			{
				e.printStackTrace();
				id_class = 0;
			}
			try
			{
				clone_counter = Integer.parseInt(clone_counter_string);
			}
			catch(NumberFormatException e)
			{
				e.printStackTrace();
				clone_counter = 0;
			}
			try
			{
				similarity = Integer.parseInt(similarity_string);
			}
			catch(NumberFormatException e)
			{
				e.printStackTrace();
				similarity = 0;
			}
			try
			{
				number_of_lines = Integer.parseInt(number_of_lines_string);
			}
			catch(NumberFormatException e)
			{
				e.printStackTrace();
				number_of_lines = 0;
			}
			
			CloneClass clone_class = new CloneClass(id_class, clone_counter, similarity, number_of_lines);
			toReturn.addClass(clone_class);
			NodeList class_clones = class_node.getChildNodes();
			for(int i = 0; i<class_clones.getLength(); i++)
			{
				Node clone_node = class_clones.item(i);
				if(clone_node.getNodeName().equalsIgnoreCase("source"))
				{
					String id_clone_string = clone_node.getAttributes().getNamedItem("pcid").getNodeValue();
					String filename_clone = clone_node.getAttributes().getNamedItem("file").getNodeValue();
					String start_line_string = clone_node.getAttributes().getNamedItem("startline").getNodeValue();
					String end_line_string = clone_node.getAttributes().getNamedItem("endline").getNodeValue();
					int id_clone, start_line, end_line;
					try
					{
						id_clone = Integer.parseInt(id_clone_string);
					}
					catch(NumberFormatException e)
					{
						e.printStackTrace();
						id_clone = 0;
					}
					try
					{
						start_line = Integer.parseInt(start_line_string);
					}
					catch(NumberFormatException e)
					{
						e.printStackTrace();
						start_line = 0;
					}
					try
					{
						end_line = Integer.parseInt(end_line_string);
					}
					catch(NumberFormatException e)
					{
						e.printStackTrace();
						end_line = 0;
					}
					//NORMALIZE FILEPATH
					filename_clone = FileUtils4G1.filePathNormalize(filename_clone.trim());
					String appendixToRemove = getAppendixToRemoveMultiVersionsAnalysis(toReturn.getSystem(), toReturn.getCommit_id_version());
					//AVOIDING IndexOutOfBoundsException
					if((appendixToRemove == null)||(appendixToRemove.length()>filename_clone.length()))
					{
						System.out.println("ERROR! UNABLE TO REMOVE APPENDIX of CLONE FILENAME -> ID["+id_clone+"]");
						//NOTE: USING break; -> ANALYZING NEXT CLONE of the CLASS
						break;
					}
					filename_clone = filename_clone.substring(appendixToRemove.length());

					Clone clone = new Clone(id_clone, filename_clone, start_line, end_line);
					clone.setClone_class(clone_class);
					clone_class.addClone(clone);
					toReturn.addClone(clone);
				}
			}
		}
		
		return toReturn;
	}

	//ARGUMENTS:
	//1)String nicad_clone_classes_file -> NiCad Clone LINES file HTML
	//	Example:
	//		NICAD_CLONE_LINES_FILE = "./src/files/JHotDraw54b1_functions-blind-clones-0.30-classes-withsource.html";
	public void read_clone_lines_report_file(String filename_clone_lines_html, Report_NiCad5 report)
	{
		//INPUT ERROR CONTROL
		if((filename_clone_lines_html == null)||(filename_clone_lines_html.trim().isEmpty()))
		{
			String response = "FILENAME NOT SET->";
			if(filename_clone_lines_html == null)
				response = response + "NULL";
			else
				response = response + "EMPTY";
			System.out.println(response);
			return;
		}
		if(report == null)
		{
			String response = "NiCad REPORT NOT SET->NULL";
			System.out.println(response);
			return;
		}
		else if((report.getSystem() == null)||(report.getSystem().trim().isEmpty())||(report.getCommit_id_version() == null)||(report.getCommit_id_version().trim().isEmpty()))
		{
			String response = "NiCad REPORT PROBLEM->\n";
			System.out.println(response+report.toString());
			return;
		}
		//ATTRIBUTE ERROR CONTROL
		if((analyzedsystems_root_folder_name == null)||(analyzedsystems_root_folder_name.trim().isEmpty()))
		{
			String response = "ANALYZED SYSTEMS ROOT FOLDER NAME NOT SET->";
			if(analyzedsystems_root_folder_name == null)
				response = response + "NULL";
			else
				response = response + "EMPTY";
			System.out.println(response);
			return;
		}
		//NORMALIZE FILEPATH
		filename_clone_lines_html = FileUtils4G1.filePathNormalize(filename_clone_lines_html.trim());
		//CREATE a SCANNER for the FILE to READ
		File clone_lines_html_file = new File(filename_clone_lines_html);
		Scanner sc_classes_report_text = null;
		try {
			sc_classes_report_text = new Scanner(clone_lines_html_file);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			System.out.println("#FILE NOT FOUND EXCEPTION#");
			return;
		}
		//////////////////////////////////////////////////////////////////////
		String lineToAnalyze = "";
		while(!lineToAnalyze.startsWith(TAG_CLONE_CLASS_START))
		{
			if(sc_classes_report_text.hasNextLine())
				lineToAnalyze = sc_classes_report_text.nextLine();
			else
				lineToAnalyze = TAG_CLONE_CLASS_START;
		}
		//JUMP LINES NOT USED
		
		while(sc_classes_report_text.hasNextLine())
		{
			//CLASS FOUND
			if(lineToAnalyze.startsWith(TAG_CLONE_CLASS_START))
			{
				String id_class_string = lineToAnalyze.substring("<h3>Clone class ".length(),lineToAnalyze.indexOf(","));
				int id_class;
				try
				{
					id_class = Integer.parseInt(id_class_string);
				}
				catch(NumberFormatException e)
				{
					e.printStackTrace();
					id_class = 0;
				}
				CloneClass clone_class = report.getClassById(id_class);
				if(clone_class != null)
				{
					//EXAMPLE CLONE:
					//<tr><td>
					//Lines 461 - 475 of systems/JHotDraw54b1/src/CH/ifa/draw/applet/DrawApplet.java
					//<pre>
					//clone lines to read
					//</pre>
					//</td></tr>
					
					//JUMP LINES NOT USED
					while(!lineToAnalyze.startsWith(TAG_CLONE_CLASS_END))
					{
						//JUMP LINES NOT USED
						while(!lineToAnalyze.startsWith(TAG_CLONE_INFO))
						{
							if(sc_classes_report_text.hasNextLine())
								lineToAnalyze = sc_classes_report_text.nextLine();
							else
								lineToAnalyze = TAG_CLONE_INFO;
						}
						
						lineToAnalyze = sc_classes_report_text.nextLine();
						int first_separator_index = lineToAnalyze.indexOf(" - ");
						String start_line_string = lineToAnalyze.substring("Lines ".length(), first_separator_index);
						int end_separator_index = lineToAnalyze.indexOf(" of ");
						String end_line_string = lineToAnalyze.substring((first_separator_index+(" - ".length())), end_separator_index);
						int start_line, end_line;
						try
						{
							start_line = Integer.parseInt(start_line_string);
							end_line = Integer.parseInt(start_line_string);
						}
						catch(NumberFormatException e)
						{
							e.printStackTrace();
							start_line = 0;
						}
						try
						{
							end_line = Integer.parseInt(end_line_string);
						}
						catch(NumberFormatException e)
						{
							e.printStackTrace();
							end_line = 0;
						}
						
						if((start_line == 0)||(end_line == 0))
						{
							System.err.println("#CLONE NOT PARSED->start_line["+start_line+"] - end_line["+end_line+"]");
							if(sc_classes_report_text.hasNextLine())
								lineToAnalyze = sc_classes_report_text.nextLine();
							else
								lineToAnalyze = TAG_CLONE_CLASS_END;
						}
						else
						{
							/*
							String clone_filename = lineToAnalyze.substring(end_separator_index+(" of ".length())+(this.analyzedsystems_root_folder_name.length()+1)+report.getSystem().length()+1+report.getCommit_id_version().length()+1+report.getSystem().length()+1);
							clone_filename = FileUtils4G1.filePathNormalize(clone_filename.trim());
							*/
							//NORMALIZE FILEPATH
							lineToAnalyze = FileUtils4G1.filePathNormalize(lineToAnalyze.trim());
							String appendixToRemove = getAppendixToRemoveMultiVersionsAnalysis(report.getSystem(), report.getCommit_id_version());
							if(appendixToRemove != null)
							{
								//AVOIDING IndexOutOfBoundsException
								int index_substring = end_separator_index+(" of ".length())+appendixToRemove.length();
								if(index_substring<=lineToAnalyze.length())
								{
									String clone_filename = lineToAnalyze.substring(index_substring).trim();
									/******************************************************************************/
									Clone found_clone = null;
									for(Map.Entry<Integer, Clone> entryClone : clone_class.getClones().entrySet())
									{
										Clone clone = entryClone.getValue();
										//NORMALIZE FILEPATH
										clone.setFilename(FileUtils4G1.filePathNormalize(clone.getFilename().trim()));
										if((clone.getFilename().equalsIgnoreCase(clone_filename))&&(clone.getStart_line()==start_line)&&(clone.getEnd_line()==end_line))
										{
											found_clone = clone;
											break;
										}
									}
									
									if(found_clone != null)
									{
										//ADD LINES
										lineToAnalyze = sc_classes_report_text.nextLine();
										if(lineToAnalyze.startsWith(TAG_CLONE_LINES_START))
											lineToAnalyze = sc_classes_report_text.nextLine();
										int line_index = start_line;
										while(!lineToAnalyze.startsWith(TAG_CLONE_LINES_END))
										{
											lineToAnalyze = StringUtils.decodeHtml(lineToAnalyze);
											found_clone.addLine(line_index, lineToAnalyze);
											line_index++;
											if(sc_classes_report_text.hasNextLine())
												lineToAnalyze = sc_classes_report_text.nextLine();
											else
												lineToAnalyze = TAG_CLONE_LINES_END;
										}
										//TEST LINES READ
										if((found_clone.getEnd_line()-found_clone.getStart_line()+1)!=found_clone.getLines().size())
										{
											System.out.println("##CLONE INCONSISTENCY[LINES:"+found_clone.getLines().size()+"]:\n["+found_clone.getFilename()+"]\n["+found_clone.getStart_line()+" - "+found_clone.getEnd_line()+"]\nTRYING TO RESOLVE READING ORIGIN FILE");
											//getCloneLinesFromOriginFileAnalyzedByNiCad(found_clone, report.getSystem(), report.getCommit_id_version());
											getCloneLinesFromOriginFileAnalyzedByNiCadWithoutComments(found_clone, report.getSystem(), report.getCommit_id_version());
										}
										//TEST LINES READ - END
										//REMOVE </td></tr>
										if(sc_classes_report_text.hasNextLine())
											lineToAnalyze = sc_classes_report_text.nextLine();
										else
											lineToAnalyze = TAG_CLONE_CLASS_END;
										//NEXT:
										//case 1- another CLONE -> <tr><td>
										//case 2- end CLONE CLASS -> </table>
										if(sc_classes_report_text.hasNextLine())
											lineToAnalyze = sc_classes_report_text.nextLine();
										else
											lineToAnalyze = TAG_CLONE_CLASS_END;
									}
									else
									{
										System.err.println("#CLONE NOT FOUND->clone_filename["+clone_filename+"]:start_line["+start_line+"] - end_line["+end_line+"]");
										if(sc_classes_report_text.hasNextLine())
											lineToAnalyze = sc_classes_report_text.nextLine();
										else
											lineToAnalyze = TAG_CLONE_CLASS_END;
									}
								}
							}
							else
								System.out.println("ERROR! UNABLE TO REMOVE APPENDIX of CLONE FILENAME -> LINE["+lineToAnalyze+"]");
						}
					}
				}
				else
					System.err.println("#CLONE CLASS NOT FOUND->ID["+id_class+"]");
			}
			else
				lineToAnalyze = sc_classes_report_text.nextLine();
		}
		if(sc_classes_report_text != null)
			sc_classes_report_text.close();
	}
	
	public void getCloneLinesFromOriginFileAnalyzedByNiCad(Clone clone, String projectname, String commit_id_version)
	{
		//INPUT ERROR CONTROL
		if(clone == null)
		{
			String response = "CLONE NOT SET->NULL";
			System.out.println(response);
			return;
		}
		if((projectname == null)||(projectname.trim().isEmpty()))
		{
			String response = "PROJECT NAME NOT SET->";
			if(projectname == null)
				response = response + "NULL";
			else
				response = response + "EMPTY";
			System.out.println(response);
			return;
		}
		if((commit_id_version == null)||(commit_id_version.trim().isEmpty()))
		{
			String response = "PROJECT VERSION NOT SET->";
			if(commit_id_version == null)
				response = response + "NULL";
			else
				response = response + "EMPTY";
			System.out.println(response);
			return;
		}
		//ATTRIBUTE ERROR CONTROL
		if((this.nicad_root_fullpath == null)||(this.nicad_root_fullpath.trim().isEmpty()))
		{
			String response = "NICAD ROOT FOLDER NAME NOT SET->";
			if(this.nicad_root_fullpath == null)
				response = response + "NULL";
			else
				response = response + "EMPTY";
			System.out.println(response);
			return;
		}
		//NORMALIZE FILEPATH
		String clone_norm_filename = FileUtils4G1.filePathNormalize(clone.getFilename().trim());
		clone_norm_filename = FileUtils4G1.removeStartingSeparator(clone_norm_filename);
		clone.setFilename(clone_norm_filename);
		//String filename = nicad_root_fullpath+analyzedsystems_root_folder_name+File.separator+projectname+File.separator+commit_id_version+File.separator+projectname+File.separator+clone_norm_filename;
		String appendix = getAppendixToRemoveMultiVersionsAnalysis(projectname, commit_id_version);
		if(appendix == null)
		{
			System.out.println("ERROR! UNABLE TO CREATE APPENDIX for CLONE FILENAME FOLDER -> FILENAME["+clone_norm_filename+"]");
			return;
		}
		String filename = nicad_root_fullpath+appendix+clone_norm_filename;

		System.out.println("FULLPATH:\n"+filename);
		File clone_lines_text = new File(filename);
		Scanner sc_clone_lines_text = null;
		try
		{
			sc_clone_lines_text = new Scanner(clone_lines_text);
			int index=0;
			String fileline=null;
			while(sc_clone_lines_text.hasNextLine()&&(index<clone.getStart_line()))
			{
				fileline = sc_clone_lines_text.nextLine();
				index++;
			}
			
			if(index == clone.getStart_line())
			{
				HashMap<Integer,String> lines = new HashMap<Integer,String>();
				lines.put(new Integer(index), fileline);
				
				boolean continue_cicle = sc_clone_lines_text.hasNextLine();
				while(continue_cicle)
				{
					if(index<clone.getEnd_line())
					{
						if(sc_clone_lines_text.hasNextLine())
						{
							fileline = sc_clone_lines_text.nextLine();
							index++;
							lines.put(new Integer(index), fileline);
						}
						else
							continue_cicle=false;
					}
					else
						continue_cicle=false;
				}
				
				if((clone.getEnd_line()-clone.getStart_line()+1)==lines.size())
				{
					clone.setLines(lines);
					System.out.println("#SOLVED#");
				}
				else
					System.out.println("#UNABLE to find all LINES:["+clone.getFilename()+"]["+clone.getStart_line()+" - "+clone.getEnd_line()+"]");
			}
			sc_clone_lines_text.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			System.out.println("#FILE NOT FOUND EXCEPTION#");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			if(sc_clone_lines_text != null)
				sc_clone_lines_text.close();
		}
	}
	
	//COMMENTS REMOVED from the FILE
	public void getCloneLinesFromOriginFileAnalyzedByNiCadWithoutComments(Clone clone, String projectname, String commit_id_version)
	{
		//INPUT ERROR CONTROL
		if(clone == null)
		{
			String response = "CLONE NOT SET->NULL";
			System.out.println(response);
			return;
		}
		if((projectname == null)||(projectname.trim().isEmpty()))
		{
			String response = "PROJECT NAME NOT SET->";
			if(projectname == null)
				response = response + "NULL";
			else
				response = response + "EMPTY";
			System.out.println(response);
			return;
		}
		if((commit_id_version == null)||(commit_id_version.trim().isEmpty()))
		{
			String response = "PROJECT VERSION NOT SET->";
			if(commit_id_version == null)
				response = response + "NULL";
			else
				response = response + "EMPTY";
			System.out.println(response);
			return;
		}
		//ATTRIBUTE ERROR CONTROL
		if((this.nicad_root_fullpath == null)||(this.nicad_root_fullpath.trim().isEmpty()))
		{
			String response = "NICAD ROOT FOLDER NAME NOT SET->";
			if(this.nicad_root_fullpath == null)
				response = response + "NULL";
			else
				response = response + "EMPTY";
			System.out.println(response);
			return;
		}
		/****************************************************************************/
		//NORMALIZE FILEPATH
		String clone_norm_filename = FileUtils4G1.filePathNormalize(clone.getFilename().trim());
		clone_norm_filename = FileUtils4G1.removeStartingSeparator(clone_norm_filename);
		clone.setFilename(clone_norm_filename);
		//String filename = nicad_root_fullpath+analyzedsystems_root_folder_name+File.separator+projectname+File.separator+commit_id_version+File.separator+projectname+File.separator+clone_norm_filename;
		String appendix = getAppendixToRemoveMultiVersionsAnalysis(projectname, commit_id_version);
		if(appendix == null)
		{
			System.out.println("ERROR! UNABLE TO CREATE APPENDIX for CLONE FILENAME FOLDER -> FILENAME["+clone_norm_filename+"]");
			return;
		}
		String filename = nicad_root_fullpath+appendix+clone_norm_filename;
		/********************************************************************************/
		System.out.println("FULLPATH:\n"+filename);
		HashMap<Integer, String> full_file_lines = CommentRemover.getLinesWithRemovedComments(filename);
		 if(full_file_lines == null)
			 System.out.println("NULL MAP of LINES for the file ["+filename+"]");
		 else
		 {
			 HashMap<Integer, String> clone_lines = new HashMap<Integer, String>();
			 //int line_number = clone.getStart_line();
			 //CHECK line numbers of clones -> AVOID INFINITE LOOP
			 if(clone.getEnd_line()<clone.getStart_line())
			 {
				 System.out.println("CLONE inconsistency: START["+clone.getStart_line()+"]->END["+clone.getEnd_line()+"]");
					return;
			 }
			 for(int line_number = clone.getStart_line(); line_number <= clone.getEnd_line(); line_number++)
			 {
				 String line = full_file_lines.get(new Integer(line_number));
				 if(line == null)
					 System.out.println("CLONE LINE ["+line_number+"] NULL");
				 clone_lines.put(new Integer(line_number), line);
			 }
			 clone.setLines(clone_lines);
		 }
	}

	//COMMENTS REMOVED from the FILE
	public static void readCloneLinesFromFile(Clone clone, String filename)
	{
		//INPUT ERROR CONTROL
		if((filename == null)||(filename.trim().isEmpty()))
		{
			String response = "FILENAME NOT SET->";
			if(filename == null)
				response = response + "NULL";
			else
				response = response + "EMPTY";
			System.out.println(response);
			return;
		}
		//NORMALIZE FILEPATH
		filename = FileUtils4G1.filePathNormalize(filename.trim());
		
		HashMap<Integer, String> full_file_lines = CommentRemover.getLinesWithRemovedComments(filename);
		 if(full_file_lines == null)
			 System.out.println("NULL MAP of LINES for the file ["+filename+"]");
		 else
		 {
			 HashMap<Integer, String> clone_lines = new HashMap<Integer, String>();
			 //int line_number = clone.getStart_line();
			 //CHECK line numbers of clones -> AVOID INFINITE LOOP
			 if(clone.getEnd_line()<clone.getStart_line())
			 {
				 System.out.println("CLONE inconsistency: START["+clone.getStart_line()+"]->END["+clone.getEnd_line()+"]");
					return;
			 }
			 for(int line_number = clone.getStart_line(); line_number <= clone.getEnd_line(); line_number++)
			 {
				 String line = full_file_lines.get(new Integer(line_number));
				 if(line == null)
					 System.out.println("CLONE LINE ["+line_number+"] NULL");
				 clone_lines.put(new Integer(line_number), line);
			 }
			 clone.setLines(clone_lines);
		 }
	}
	
	//The FOLDER NAME of the ANALYZED SYSTEM when analyzing only 1 version of the SYSTEM
	//NOTE:FOLLOW SUGGESTIONS about NiCad FOLDERS GERARCHY inside README.TXT
	//NOTE:NORMALIZE FILEPATH BEFORE USING IT
	public String getAppendixToRemove(String system_name)
	{
		//INPUT ERROR CONTROL
		if((system_name == null)||(system_name.trim().isEmpty()))
		{
			String response = "SYSTEM NAME NOT SET->";
			if(system_name == null)
				response = response + "NULL";
			else
				response = response + "EMPTY";
			System.out.println(response);
			return null;
		}
		//ATTRIBUTE ERROR CONTROL
		if((analyzedsystems_root_folder_name == null)||(analyzedsystems_root_folder_name.trim().isEmpty()))
		{
			String response = "ANALYZED SYSTEMS ROOT FOLDER NAME NOT SET->";
			if(analyzedsystems_root_folder_name == null)
				response = response + "NULL";
			else
				response = response + "EMPTY";
			System.out.println(response);
			return null;
		}
		system_name = system_name.trim();
		analyzedsystems_root_folder_name = analyzedsystems_root_folder_name.trim();
		
		return analyzedsystems_root_folder_name+File.separator+system_name+File.separator;
	}
	
	//The FOLDER NAME of the ANALYZED SYSTEM when analyzing more versions of the SYSTEM
	//NOTE:FOLLOW SUGGESTIONS about NiCad FOLDERS GERARCHY inside README.TXT
	//NOTE:NORMALIZE FILEPATH BEFORE USING IT
	public String getAppendixToRemoveMultiVersionsAnalysis(String system_name, String system_version)
	{
		//INPUT ERROR CONTROL
		if((system_name == null)||(system_name.trim().isEmpty()))
		{
			String response = "SYSTEM NAME NOT SET->";
			if(system_name == null)
				response = response + "NULL";
			else
				response = response + "EMPTY";
			System.out.println(response);
			return null;
		}
		if((system_version == null)||(system_version.trim().isEmpty()))
		{
			String response = "SYSTEM VERSION NOT SET->";
			if(system_version == null)
				response = response + "NULL";
			else
				response = response + "EMPTY";
			System.out.println(response);
			return null;
		}
		//ATTRIBUTE ERROR CONTROL
		if((analyzedsystems_root_folder_name == null)||(analyzedsystems_root_folder_name.trim().isEmpty()))
		{
			String response = "ANALYZED SYSTEMS ROOT FOLDER NAME NOT SET->";
			if(analyzedsystems_root_folder_name == null)
				response = response + "NULL";
			else
				response = response + "EMPTY";
			System.out.println(response);
			return null;
		}
		system_name = system_name.trim();
		system_version = system_version.trim();
		analyzedsystems_root_folder_name = analyzedsystems_root_folder_name.trim();
				
		return analyzedsystems_root_folder_name+File.separator+system_name+File.separator+system_version+File.separator+system_name+File.separator;
	}
	
	//EXAMPLES:
	//"C:/cygwin64/home/YourUserName/NiCad-5.0/"; ->END with "/"
	//"systems"; -> NO NULL-EMPTY STRING allowed
	private String nicad_root_fullpath = null;
	private String analyzedsystems_root_folder_name = null;
	
}
