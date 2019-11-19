package view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import model.Commit_Node;
import model.File_Change;

public class CommitPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7267301895490559348L;

	private Commit_Node commit = null;

	private final static int SPACE = 5;
	private final static int LABEL_WIDTH = 700;
	private final static int LABEL_HEIGHT = 16;
	private final static int TEXTFIELD_HEIGHT = 30;
	private final static int BUTTON_HEIGHT = 40;
	private final static int LIST_HEIGHT = 150;
	private final static int PANEL_WIDTH = LABEL_WIDTH+50;
	
	private JList<File_Change> changesList;
	private JScrollPane centerChangesListScroll;
	
	private JList<Commit_Node> parentsList;
	private JScrollPane parentsListScroll;
	
	private JList<Commit_Node> childrenList;
	private JScrollPane childrenListScroll;
	
	public CommitPanel(Commit_Node commit)
	{
		this.commit = commit;
		
		setLayout(null);
		int y = 0;
		
        /******************************/
		JLabel commitImageLabel = new JLabel();
		String commit_image_name = "github_logo_256x256";
		ImageIcon commit_imageIcon = null;
		try {
			commit_imageIcon = new ImageIcon(getClass().getResource("/icons/" + commit_image_name + ".png"));
			Image image = commit_imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(128, 128,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			commit_imageIcon = new ImageIcon(newimg);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		if (commit_imageIcon != null)
			commitImageLabel.setIcon(commit_imageIcon);
		commitImageLabel.setBounds(SPACE, y, 128, 128);
		commitImageLabel.setPreferredSize(new Dimension(128,128));
		add(commitImageLabel);
		y += 128+SPACE;
		/******************************/
		
		JLabel commitIDLabel = new JLabel("COMMIT ID:");
		commitIDLabel.setBounds(SPACE, y, LABEL_WIDTH, LABEL_HEIGHT);
		commitIDLabel.setPreferredSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
		add(commitIDLabel);
		commitIDLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += LABEL_HEIGHT+SPACE;
		
		JTextField commitIDTextField = new JTextField();
		commitIDTextField.setColumns(16);
		commitIDTextField.setBounds(SPACE, y, LABEL_WIDTH, TEXTFIELD_HEIGHT);
		commitIDTextField.setPreferredSize(new Dimension(LABEL_WIDTH,TEXTFIELD_HEIGHT));
		add(commitIDTextField);
		commitIDTextField.setAlignmentX( Component.LEFT_ALIGNMENT );
		commitIDTextField.setText(this.commit.getId_commit()+"");
		commitIDTextField.setEditable(false);
		y += TEXTFIELD_HEIGHT+SPACE;

		JLabel commitDateLabel = new JLabel("COMMIT DATE:");
		commitDateLabel.setBounds(SPACE, y, LABEL_WIDTH, LABEL_HEIGHT);
		commitDateLabel.setPreferredSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
		add(commitDateLabel);
		commitDateLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += LABEL_HEIGHT+SPACE;
		
		JTextField commitDateTextField = new JTextField();
		commitDateTextField.setColumns(16);
		commitDateTextField.setBounds(SPACE, y, LABEL_WIDTH, TEXTFIELD_HEIGHT);
		commitDateTextField.setPreferredSize(new Dimension(LABEL_WIDTH,TEXTFIELD_HEIGHT));
		add(commitDateTextField);
		commitDateTextField.setAlignmentX( Component.LEFT_ALIGNMENT );
		commitDateTextField.setText(this.commit.dateString());
		commitDateTextField.setEditable(false);
		y += TEXTFIELD_HEIGHT+SPACE;
		
		//////////////////////////////////////////////////
		//SEPARATOR
		JSeparator separator_author_start = new JSeparator(JSeparator.HORIZONTAL);
		separator_author_start.setBounds(0, y, PANEL_WIDTH-20, LABEL_HEIGHT);
		add(separator_author_start);
		y += LABEL_HEIGHT+SPACE;
		
		JLabel commitAuthorLabel = new JLabel("AUTHOR:");
		commitAuthorLabel.setBounds(SPACE, y, LABEL_WIDTH, LABEL_HEIGHT);
		commitAuthorLabel.setPreferredSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
		add(commitAuthorLabel);
		commitAuthorLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += LABEL_HEIGHT+SPACE;
		
		JTextField commitAuthorTextField = new JTextField();
		commitAuthorTextField.setColumns(16);
		commitAuthorTextField.setBounds(SPACE, y, LABEL_WIDTH, TEXTFIELD_HEIGHT);
		commitAuthorTextField.setPreferredSize(new Dimension(LABEL_WIDTH,TEXTFIELD_HEIGHT));
		add(commitAuthorTextField);
		commitAuthorTextField.setAlignmentX( Component.LEFT_ALIGNMENT );
		commitAuthorTextField.setText(this.commit.getAuthor().getId_author());
		commitAuthorTextField.setEditable(false);
		y += TEXTFIELD_HEIGHT+SPACE;
		
		JLabel commitAuthorEMailLabel = new JLabel("AUTHOR EMAIL:");
		commitAuthorEMailLabel.setBounds(SPACE, y, LABEL_WIDTH, LABEL_HEIGHT);
		commitAuthorEMailLabel.setPreferredSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
		add(commitAuthorEMailLabel);
		commitAuthorEMailLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += LABEL_HEIGHT+SPACE;
		
		JTextField commitAuthorEMailTextField = new JTextField();
		commitAuthorEMailTextField.setColumns(16);
		commitAuthorEMailTextField.setBounds(SPACE, y, LABEL_WIDTH, TEXTFIELD_HEIGHT);
		commitAuthorEMailTextField.setPreferredSize(new Dimension(LABEL_WIDTH,TEXTFIELD_HEIGHT));
		add(commitAuthorEMailTextField);
		commitAuthorEMailTextField.setAlignmentX( Component.LEFT_ALIGNMENT );
		commitAuthorEMailTextField.setText(this.commit.getAuthor().getEmail_author());
		commitAuthorEMailTextField.setEditable(false);
		y += TEXTFIELD_HEIGHT+SPACE;
		
		//SEPARATOR
		JSeparator separator_author_end = new JSeparator(JSeparator.HORIZONTAL);
		separator_author_end.setBounds(0, y, PANEL_WIDTH-20, LABEL_HEIGHT);
		add(separator_author_end);
		y += LABEL_HEIGHT+SPACE;
		
		JLabel commitDescriptionLabel = new JLabel("DESCRIPTION:");
		commitDescriptionLabel.setBounds(SPACE, y, LABEL_WIDTH, LABEL_HEIGHT);
		commitDescriptionLabel.setPreferredSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
		add(commitDescriptionLabel);
		commitDescriptionLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += LABEL_HEIGHT+SPACE;
		
		JTextArea commitDescriptionTextArea = new JTextArea(this.commit.getDescription());
		commitDescriptionTextArea.setEditable(false);
		
		JScrollPane commitDescriptionScroll = new JScrollPane(commitDescriptionTextArea);
		commitDescriptionScroll.setBounds(SPACE, y, LABEL_WIDTH, 50);
		add(commitDescriptionScroll);
		commitDescriptionScroll.setVisible(true);
		y += 50+SPACE;
		
		JLabel commitFileChangesLabel = new JLabel("FILE CHANGES:");
		commitFileChangesLabel.setBounds(SPACE, y, LABEL_WIDTH, LABEL_HEIGHT);
		commitFileChangesLabel.setPreferredSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
		add(commitFileChangesLabel);
		commitFileChangesLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += LABEL_HEIGHT+SPACE;
		
		centerChangesListScroll = new JScrollPane();
		centerChangesListScroll.setBounds(SPACE, y, LABEL_WIDTH, LIST_HEIGHT);
		add(centerChangesListScroll);
		
		changesList = new JList<File_Change>();
		centerChangesListScroll.setViewportView(changesList);
		changesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		showResultListFileChanges(this.commit.getFile_changes());
		
		y += LIST_HEIGHT+SPACE;
		
		//COMMIT PARENTS
		String parents_label_string = "PARENTS:";
		if(this.commit.getParents().size()>0)
			parents_label_string = parents_label_string+" ["+this.commit.getParents().size()+"]";
		else
			parents_label_string = parents_label_string+" [NONE]";
		JLabel commitParentsLabel = new JLabel(parents_label_string);
		commitParentsLabel.setBounds(SPACE, y, LABEL_WIDTH, LABEL_HEIGHT);
		commitParentsLabel.setPreferredSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
		add(commitParentsLabel);
		commitParentsLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += LABEL_HEIGHT+SPACE;
		
		parentsListScroll = new JScrollPane();
		parentsListScroll.setBounds(SPACE, y, LABEL_WIDTH, BUTTON_HEIGHT*2);
		add(parentsListScroll);
		
		parentsList = new JList<Commit_Node>();
		parentsListScroll.setViewportView(parentsList);
		parentsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		showResultListParents(this.commit.getParents());
		
		y += BUTTON_HEIGHT*2+SPACE;
		
		//COMMIT CHILDS
		String children_label_string = "CHILDREN:";
		if(this.commit.getChilds().size()>0)
			children_label_string = children_label_string+" ["+this.commit.getChilds().size()+"]";
		else
			children_label_string = children_label_string+" [NONE]";
		JLabel commitChildrenLabel = new JLabel(children_label_string);
		commitChildrenLabel.setBounds(SPACE, y, LABEL_WIDTH, LABEL_HEIGHT);
		commitChildrenLabel.setPreferredSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
		add(commitChildrenLabel);
		commitChildrenLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += LABEL_HEIGHT+SPACE;
		
		childrenListScroll = new JScrollPane();
		childrenListScroll.setBounds(SPACE, y, LABEL_WIDTH, BUTTON_HEIGHT*2);
		add(childrenListScroll);
		
		childrenList = new JList<Commit_Node>();
		childrenListScroll.setViewportView(childrenList);
		childrenList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		showResultListChildren(this.commit.getChilds());
		
		y += BUTTON_HEIGHT*2+SPACE;
		
		setSize(PANEL_WIDTH,y+70);//200x480
		setPreferredSize(new Dimension(PANEL_WIDTH,y+70));//200x480
        setVisible(true);
	}
	
	public void showResultListFileChanges(ArrayList<File_Change> changes_arraylist) {
		if (changes_arraylist != null)
		{
			if (!changes_arraylist.isEmpty())
			{
				//setListData() -> NEEDS an ARRAY[] as argument
				File_Change[] changes = changes_arraylist.toArray(new File_Change[changes_arraylist.size()]);
				this.changesList.setListData(changes);
				this.changesList.setCellRenderer(new File_Change_Renderer());
			}
		}
		this.centerChangesListScroll.setVisible(true);
	}
	
	public void showResultListParents(ArrayList<Commit_Node> parents_arraylist) {
		if (parents_arraylist != null)
		{
			if (!parents_arraylist.isEmpty())
			{
				//setListData() -> NEEDS an ARRAY[] as argument
				Commit_Node[] parents = parents_arraylist.toArray(new Commit_Node[parents_arraylist.size()]);
				this.parentsList.setListData(parents);
				this.parentsList.setCellRenderer(new Commit_Renderer());
			}
		}
		this.parentsListScroll.setVisible(true);
	}
	
	public void showResultListChildren(ArrayList<Commit_Node> children_arraylist) {
		if (children_arraylist != null)
		{
			if (!children_arraylist.isEmpty())
			{
				//setListData() -> NEEDS an ARRAY[] as argument
				Commit_Node[] children = children_arraylist.toArray(new Commit_Node[children_arraylist.size()]);
				this.childrenList.setListData(children);
				this.childrenList.setCellRenderer(new Commit_Renderer());
			}
		}
		this.childrenListScroll.setVisible(true);
	}

}
