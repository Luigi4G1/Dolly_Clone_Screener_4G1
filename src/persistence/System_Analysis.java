package persistence;

import model.Commit_History;
import model.Report_NiCad5;

public class System_Analysis {

	public System_Analysis(Settings settings, Commit_History commit_history_analysis,
			Report_NiCad5 report_clone_analysis) {
		this.settings = settings;
		this.commit_history_analysis = commit_history_analysis;
		this.report_clone_analysis = report_clone_analysis;
	}

	public Settings getSettings() {
		return settings;
	}
	public void setSettings(Settings settings) {
		this.settings = settings;
	}
	public Commit_History getCommit_history_analysis() {
		return commit_history_analysis;
	}
	public void setCommit_history_analysis(Commit_History commit_history_analysis) {
		this.commit_history_analysis = commit_history_analysis;
	}
	public Report_NiCad5 getReport_clone_analysis() {
		return report_clone_analysis;
	}
	public void setReport_clone_analysis(Report_NiCad5 report_clone_analysis) {
		this.report_clone_analysis = report_clone_analysis;
	}

	private Settings settings = null;
	private Commit_History commit_history_analysis = null;
	private Report_NiCad5 report_clone_analysis = null;
}
