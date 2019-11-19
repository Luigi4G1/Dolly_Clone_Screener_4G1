package controller;

import persistence.Settings;
import persistence.System_Analysis;
import view.Home_System_Analysis_View;
import view.Settings_Modify_View;
import view.Settings_Parser_View;

public class Settings_Parser_Controller {

	private static Settings_Parser_Controller instance = null;
	
	public static Settings_Parser_Controller getInstance()
	{
		if(instance == null)
			instance = new Settings_Parser_Controller();
		return instance;
	}
	
	public void parseSettingsSystem(Settings_Parser_View frame, Settings settings_to_parse)
	{
		frame.showMessage("Parsing System...");
		//CALL PARSER
		System_Analysis system_analysis = ParserTemplateMethod.parseSystemAnalysis(settings_to_parse);
		if(system_analysis == null)
		{
			frame.showAlert("UNABLE TO PARSE SYSTEM ANALYSIS", "PARSING ERROR");
		}
		else
		{
			frame.closeForm();
			new Home_System_Analysis_View(system_analysis);
		}
	}
	
	public void goToSettingsModify(Settings_Parser_View frame, Settings settings_to_modify)
	{
		frame.closeForm();
		new Settings_Modify_View(settings_to_modify);
	}
}