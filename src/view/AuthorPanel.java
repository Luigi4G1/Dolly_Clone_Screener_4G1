package view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import model.Clone;
import model.Commit_Author;
import model.Commit_Node;

public class AuthorPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1258921781364138360L;

	private Commit_Author author = null;
	
	private final static int SPACE = 5;
	private final static int LABEL_WIDTH = 700;
	private final static int LABEL_HEIGHT = 16;
	private final static int TEXTFIELD_HEIGHT = 30;
	private final static int BUTTON_HEIGHT = 40;
	private final static int LIST_HEIGHT = 200;
	private final static int PANEL_WIDTH = LABEL_WIDTH+50;
	
	private JList<Commit_Node> commitList;
	private JScrollPane centerCommitListScroll;
	
	private JList<Clone> cloneList;
	private JScrollPane centerCloneListScroll;

	public AuthorPanel(Commit_Author author) {
		this.author = author;
		
		setLayout(null);
		int y = 0;
		
        /******************************/
		JLabel authorImageLabel = new JLabel();
		String author_image_name = "author_512x512";
		ImageIcon author_imageIcon = null;
		try {
			author_imageIcon = new ImageIcon(getClass().getResource("/icons/" + author_image_name + ".png"));
			Image image = author_imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(128, 128,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			author_imageIcon = new ImageIcon(newimg);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		if (author_imageIcon != null)
			authorImageLabel.setIcon(author_imageIcon);
		authorImageLabel.setBounds(SPACE, y, 128, 128);
		authorImageLabel.setPreferredSize(new Dimension(128,128));
		add(authorImageLabel);
		y += 128+SPACE;
		/******************************/
		
		JLabel nameLabel = new JLabel("NAME:");
		nameLabel.setBounds(SPACE, y, LABEL_WIDTH, LABEL_HEIGHT);
		nameLabel.setPreferredSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
		add(nameLabel);
		nameLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += LABEL_HEIGHT+SPACE;
		
		JTextField nameTextField = new JTextField();
		nameTextField.setColumns(16);
		nameTextField.setBounds(SPACE, y, LABEL_WIDTH, TEXTFIELD_HEIGHT);
		nameTextField.setPreferredSize(new Dimension(LABEL_WIDTH,TEXTFIELD_HEIGHT));
		add(nameTextField);
		nameTextField.setAlignmentX( Component.LEFT_ALIGNMENT );
		nameTextField.setText(this.author.getId_author()+"");
		nameTextField.setEditable(false);
		y += TEXTFIELD_HEIGHT+SPACE;
		
		JLabel emailLabel = new JLabel("EMAIL:");
		emailLabel.setBounds(SPACE, y, LABEL_WIDTH, LABEL_HEIGHT);
		emailLabel.setPreferredSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
		add(emailLabel);
		emailLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += LABEL_HEIGHT+SPACE;
		
		JTextField emailTextField = new JTextField();
		emailTextField.setColumns(16);
		emailTextField.setBounds(SPACE, y, LABEL_WIDTH, TEXTFIELD_HEIGHT);
		emailTextField.setPreferredSize(new Dimension(LABEL_WIDTH,TEXTFIELD_HEIGHT));
		add(emailTextField);
		emailTextField.setAlignmentX( Component.LEFT_ALIGNMENT );
		emailTextField.setText(this.author.getEmail_author()+"");
		emailTextField.setEditable(false);
		y += TEXTFIELD_HEIGHT+SPACE;
		
		JLabel commitLabel = new JLabel("COMMITs DONE:");
		commitLabel.setBounds(SPACE, y, LABEL_WIDTH, BUTTON_HEIGHT);
		commitLabel.setPreferredSize(new Dimension(LABEL_WIDTH,BUTTON_HEIGHT));
		add(commitLabel);
		commitLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += BUTTON_HEIGHT+SPACE;
		
		ImageIcon commit_imageIcon = null;
		try {
			commit_imageIcon = new ImageIcon(getClass().getResource("/icons/" + "github_logo_256x256" + ".png"));
			Image image = commit_imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(32, 32,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			commit_imageIcon = new ImageIcon(newimg);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		if (commit_imageIcon != null)
			commitLabel.setIcon(commit_imageIcon);
		//HERE COMMIT LIST
		centerCommitListScroll = new JScrollPane();
		centerCommitListScroll.setBounds(SPACE, y, LABEL_WIDTH, LIST_HEIGHT);
		add(centerCommitListScroll);
		y += LIST_HEIGHT+SPACE;
		
		ArrayList<Commit_Node> commit_arraylist = new ArrayList<Commit_Node>(this.author.getCommit_done_map().values());
		Collections.sort(commit_arraylist, Commit_Node.Commit_DateDESC_Comparator);
		Commit_Node[] commits = commit_arraylist.toArray(new Commit_Node[commit_arraylist.size()]);
		commitList = new JList<Commit_Node>(commits);
		commitList.setCellRenderer(new Commit_Renderer());
		centerCommitListScroll.setViewportView(commitList);
		commitList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		centerCommitListScroll.setVisible(true);
		
		commitLabel.setText("COMMITs DONE:["+commits.length+"]");
		/***********************************************************************************/
		//LISTENER
		/***********************************************************************************/
		JLabel cloneLabel = new JLabel("CLONEs CREATED:");
		cloneLabel.setBounds(SPACE, y, LABEL_WIDTH, BUTTON_HEIGHT);
		cloneLabel.setPreferredSize(new Dimension(LABEL_WIDTH,BUTTON_HEIGHT));
		add(cloneLabel);
		cloneLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += BUTTON_HEIGHT+SPACE;
		
		ImageIcon clone_imageIcon = null;
		try {
			clone_imageIcon = new ImageIcon(getClass().getResource("/icons/" + "clone_create_256x256" + ".png"));
			Image image = clone_imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(32, 32,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			clone_imageIcon = new ImageIcon(newimg);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		if (clone_imageIcon != null)
			cloneLabel.setIcon(clone_imageIcon);
		//HERE CLONE LIST
		centerCloneListScroll = new JScrollPane();
		centerCloneListScroll.setBounds(SPACE, y, LABEL_WIDTH, LIST_HEIGHT);
		add(centerCloneListScroll);
		y += LIST_HEIGHT+SPACE;
		
		ArrayList<Clone> clone_arraylist = new ArrayList<Clone>(this.author.getCreated_clones().values());
		Clone[] clones = clone_arraylist.toArray(new Clone[clone_arraylist.size()]);
		cloneList = new JList<Clone>(clones);
		cloneList.setCellRenderer(new Clone_Renderer());
		centerCloneListScroll.setViewportView(cloneList);
		cloneList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		centerCloneListScroll.setVisible(true);
		/***********************************************************************************/
		//LISTENER
		/***********************************************************************************/
		cloneLabel.setText("CLONEs CREATED:["+clones.length+"]");

		setSize(PANEL_WIDTH,y+70);//200x480
		setPreferredSize(new Dimension(PANEL_WIDTH,y+70));//200x480
        setVisible(true);
	}

	public AuthorPanel() {
		// TODO Auto-generated constructor stub
	}

	public AuthorPanel(LayoutManager arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public AuthorPanel(boolean arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public AuthorPanel(LayoutManager arg0, boolean arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public JList<Commit_Node> getCommitList() {
		return commitList;
	}

	public void setCommitList(JList<Commit_Node> commitList) {
		this.commitList = commitList;
	}

	public JList<Clone> getCloneList() {
		return cloneList;
	}

	public void setCloneList(JList<Clone> cloneList) {
		this.cloneList = cloneList;
	}

}
