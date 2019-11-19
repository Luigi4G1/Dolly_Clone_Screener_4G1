package persistence;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import model.Clone;
import model.File_Change;

public class Diff_Save implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5874604828321599621L;

	public Diff_Save(File_Change diff) {
		this.diff = diff;
		this.commit_id = diff.getCommit().getId_commit();
		this.clones_ids = new ArrayList<Integer>();
		for(Clone clone : diff.getAdded_clones())
			this.clones_ids.add(new Integer(clone.getId_clone()));
	}

	public File_Change getDiff() {
		return diff;
	}

	public String getCommit_id() {
		return commit_id;
	}

	public ArrayList<Integer> getClones_ids() {
		return clones_ids;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(diff);
		out.writeObject(commit_id);
		out.writeObject(clones_ids);
		//SAVER
	}

	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		diff = (File_Change) in.readObject();
		commit_id = (String) in.readObject();
		clones_ids = (ArrayList<Integer>) in.readObject();
		//LINKER
	}
	
	private File_Change diff = null;
	//Links
	private String commit_id = null;
	private ArrayList<Integer> clones_ids = new ArrayList<Integer>();
}
