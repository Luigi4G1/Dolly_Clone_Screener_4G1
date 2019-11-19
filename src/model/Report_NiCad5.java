package model;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

public class Report_NiCad5 implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1776613146585350733L;

	public Report_NiCad5(String processor, String system, String granularity, int threshold, int minlines, int maxlines,
			int num_classes) {
		this.processor = processor;
		this.system = system;
		this.commit_id_version = null;
		this.granularity = granularity;
		this.threshold = threshold;
		this.minlines = minlines;
		this.maxlines = maxlines;
		this.num_classes = num_classes;
		this.clone_classes = new HashMap<Integer,CloneClass>();
		this.clones = new HashMap<Integer,Clone>();
	}

	public String getProcessor() {
		return processor;
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getCommit_id_version() {
		return commit_id_version;
	}

	public void setCommit_id_version(String commit_id_version) {
		this.commit_id_version = commit_id_version;
	}

	public String getGranularity() {
		return granularity;
	}

	public void setGranularity(String granularity) {
		this.granularity = granularity;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public int getMinlines() {
		return minlines;
	}

	public void setMinlines(int minlines) {
		this.minlines = minlines;
	}

	public int getMaxlines() {
		return maxlines;
	}

	public void setMaxlines(int maxlines) {
		this.maxlines = maxlines;
	}

	public int getNum_classes() {
		return num_classes;
	}

	public void setNum_classes(int num_classes) {
		this.num_classes = num_classes;
	}

	public HashMap<Integer, CloneClass> getClone_classes() {
		if(this.clone_classes == null)
			this.clone_classes = new HashMap<Integer, CloneClass>();
		return clone_classes;
	}

	public void setClone_classes(HashMap<Integer, CloneClass> clone_classes) {
		this.clone_classes = clone_classes;
	}
	
	public HashMap<Integer, Clone> getClones() {
		if(this.clones == null)
			this.clones = new HashMap<Integer, Clone>();
		return clones;
	}

	public void setClones(HashMap<Integer, Clone> clones) {
		this.clones = clones;
	}

	public void addClass(CloneClass clone_class)
	{
		if(this.clone_classes == null)
			this.clone_classes = new HashMap<Integer,CloneClass>();
		this.clone_classes.put(new Integer(clone_class.getId_class()), clone_class);
	}
	
	public CloneClass getClassById(int id_class)
	{
		if(this.clone_classes == null)
			return null;
		return this.clone_classes.get(new Integer(id_class));
	}
	
	public CloneClass getClassById(Integer id_class)
	{
		if(this.clone_classes == null)
			return null;
		return this.clone_classes.get(id_class);
	}
	
	public void addClone(Clone clone)
	{
		if(this.clones == null)
			this.clones = new HashMap<Integer,Clone>();
		this.clones.put(new Integer(clone.getId_clone()), clone);
	}
	
	public Clone getCloneById(int id_clone)
	{
		if(this.clones == null)
			return null;
		return this.clones.get(new Integer(id_clone));
	}
	
	public Clone getCloneById(Integer id_clone)
	{
		if(this.clones == null)
			return null;
		return this.clones.get(id_clone);
	}
	
	@Override
	public String toString() {
		String class_num = null;
		if(clone_classes == null)
			class_num = "NULL";
		else
			
			class_num = ""+clone_classes.size();
		
		String clones_num = null;
		if(clones == null)
			clones_num = "NULL";
		else
			
			clones_num = ""+clones.size();
		
		return "Report_NiCad5 [processor=" + processor + ", system=" + system + ", granularity=" + granularity
				+ ", threshold=" + threshold + "%, minlines=" + minlines + ", maxlines=" + maxlines + ", num_classes="
				+ num_classes + ", clone_classes=" + class_num + ", clones=" + clones_num + "]";
	}
	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(processor);
		out.writeObject(system);
		out.writeObject(commit_id_version);
		out.writeObject(granularity);
		
		out.writeInt(threshold);
		out.writeInt(minlines);
		out.writeInt(maxlines);
		out.writeInt(num_classes);
		
		out.writeObject(clones);
		out.writeObject(clone_classes);
		//SAVER
	}
	
	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		processor = (String) in.readObject();
		system = (String) in.readObject();
		commit_id_version = (String) in.readObject();
		granularity = (String) in.readObject();
		
		threshold = (int) in.readInt();
		minlines = (int) in.readInt();
		maxlines = (int) in.readInt();
		num_classes = (int) in.readInt();
		//UNLINKED -> TO LINK
		clones = (HashMap<Integer,Clone>) in.readObject();
		clone_classes = (HashMap<Integer,CloneClass>) in.readObject();
		//LINKER
	}
	
	private String processor;
	private String system;
	private String commit_id_version = null;
	private String granularity;
	private int threshold;
	private int minlines;
	private int maxlines;
	private int num_classes;
	
	private HashMap<Integer,CloneClass> clone_classes = new HashMap<Integer,CloneClass>();
	private HashMap<Integer,Clone> clones = new HashMap<Integer,Clone>();
}
