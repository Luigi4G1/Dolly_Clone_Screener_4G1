package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import controller.Commit_View_Controller;
import model.Commit_Node;
import model.File_Change;
import persistence.System_Analysis;

public class Commit_View extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2390350236423200493L;

	private final static int WINDOW_WIDTH = 640;
	private final static int WINDOW_HEIGHT = 900;
	
	private final static int MENUBAR_HEIGHT = 32;
	
	private final static int NORTH_PANEL_HEIGHT = 0;
	private final static int SOUTH_PANEL_HEIGHT = 20;
	
	private final static int TEXTFIELD_HEIGHT = 30;
	private final static int LABEL_HEIGHT = 16;
	
	private final static int BUTTON_WIDTH = 150;
	private final static int BUTTON_HEIGHT = 25;
	
	private Commit_Node commit = null;
	private System_Analysis analysis_result = null;
	
	private JLabel errorDialogLabel;
	private JList<File_Change> changesList;
	private JScrollPane centerChangesListScroll;
	
	private JComboBox<Object> parentsIDsComboBox;
	private JComboBox<Object> childsIDsComboBox;

	public Commit_View(Commit_Node commit, System_Analysis analysis_result)
	{
		this.commit = commit;
		this.analysis_result = analysis_result;
		
		setTitle("COMMIT VIEWER");
		setResizable(false);
		setBounds(100, 100, WINDOW_WIDTH, WINDOW_HEIGHT+MENUBAR_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		
		super.setJMenuBar(new MenuBarDollyScreener(this, this.analysis_result));
		
		//SOUTH PANEL -> error label
		errorDialogLabel = new JLabel("Questo e' un messaggio di errore!");
		errorDialogLabel.setBounds(0, 0, WINDOW_WIDTH, SOUTH_PANEL_HEIGHT-4);//prev->325
		errorDialogLabel.setForeground(Color.red);

		JPanel panelSouth = new JPanel();
		panelSouth.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelSouth.setBounds(0, 0, WINDOW_WIDTH, SOUTH_PANEL_HEIGHT);
		panelSouth.add(errorDialogLabel);
		getContentPane().add(panelSouth, BorderLayout.SOUTH);
		errorDialogLabel.setVisible(true);//TEST->true
		panelSouth.setVisible(true);
		//SOUTH PANEL -> END#
		
		
		//CENTER PANEL -> list
		JPanel panelCenter = new JPanel();
		panelCenter.setBounds(0, 0, WINDOW_WIDTH-20, WINDOW_HEIGHT-SOUTH_PANEL_HEIGHT-NORTH_PANEL_HEIGHT);
		panelCenter.setLayout(null);
		getContentPane().add(panelCenter, BorderLayout.CENTER);

		//CURRENT OBJECT HEIGHT
		int center_panel_element_height = NORTH_PANEL_HEIGHT+25;//25

		//COMMIT ID
		JLabel commitIDLabel = new JLabel("COMMIT ID:");
		commitIDLabel.setBounds(10, center_panel_element_height, 150, LABEL_HEIGHT);
		panelCenter.add(commitIDLabel);

		center_panel_element_height += LABEL_HEIGHT;//41
		JTextField commitIDTextField = new JTextField();
		commitIDTextField.setBounds(10, center_panel_element_height, WINDOW_WIDTH-40, TEXTFIELD_HEIGHT);
		panelCenter.add(commitIDTextField);
		commitIDTextField.setEditable(false);
		commitIDTextField.setText(this.commit.getId_commit());
		commitIDTextField.setBackground(Color.MAGENTA);

		//COMMIT DATE
		center_panel_element_height += (TEXTFIELD_HEIGHT+10);//81
		JLabel commitDateLabel = new JLabel("COMMIT DATE:");
		commitDateLabel.setBounds(10, center_panel_element_height, 150, LABEL_HEIGHT);
		panelCenter.add(commitDateLabel);

		center_panel_element_height += LABEL_HEIGHT;//97
		JTextField commitDateTextField = new JTextField();
		commitDateTextField.setBounds(10, center_panel_element_height, WINDOW_WIDTH/2, TEXTFIELD_HEIGHT);
		panelCenter.add(commitDateTextField);
		commitDateTextField.setEditable(false);
		commitDateTextField.setText(this.commit.dateString());
		
		center_panel_element_height += (TEXTFIELD_HEIGHT+LABEL_HEIGHT);//143
		//SEPARATOR
		JSeparator separator_author_start = new JSeparator(JSeparator.HORIZONTAL);
		separator_author_start.setBounds(10, center_panel_element_height, WINDOW_WIDTH-40, LABEL_HEIGHT);
		panelCenter.add(separator_author_start);
		
		center_panel_element_height += LABEL_HEIGHT;//159
		
		//COMMIT AUTHOR
		JLabel commitAuthorLabel = new JLabel("AUTHOR:");
		commitAuthorLabel.setBounds(10, center_panel_element_height, 150, LABEL_HEIGHT);
		panelCenter.add(commitAuthorLabel);

		center_panel_element_height += LABEL_HEIGHT;//175
		JTextField commitAuthorTextField = new JTextField();
		commitAuthorTextField.setBounds(10, center_panel_element_height, WINDOW_WIDTH/2, TEXTFIELD_HEIGHT);
		panelCenter.add(commitAuthorTextField);
		commitAuthorTextField.setEditable(false);
		commitAuthorTextField.setText(this.commit.getAuthor().getId_author());
		commitAuthorTextField.setBackground(Color.YELLOW);
		
		center_panel_element_height += TEXTFIELD_HEIGHT;//205
		
		//COMMIT AUTHOR EMAIL
		JLabel commitAuthorEMailLabel = new JLabel("AUTHOR EMAIL:");
		commitAuthorEMailLabel.setBounds(10, center_panel_element_height, 150, LABEL_HEIGHT);
		panelCenter.add(commitAuthorEMailLabel);

		center_panel_element_height += LABEL_HEIGHT;//221
		JTextField commitAuthorEMailTextField = new JTextField();
		commitAuthorEMailTextField.setBounds(10, center_panel_element_height, WINDOW_WIDTH*3/4, TEXTFIELD_HEIGHT);
		panelCenter.add(commitAuthorEMailTextField);
		commitAuthorEMailTextField.setEditable(false);
		commitAuthorEMailTextField.setText(this.commit.getAuthor().getEmail_author());
		
		center_panel_element_height += (TEXTFIELD_HEIGHT+LABEL_HEIGHT);//267
		//SEPARATOR
		JSeparator separator_author_end = new JSeparator(JSeparator.HORIZONTAL);
		separator_author_end.setBounds(10, center_panel_element_height, WINDOW_WIDTH-40, LABEL_HEIGHT);
		panelCenter.add(separator_author_end);
		
		center_panel_element_height += LABEL_HEIGHT;//283
		
		//COMMIT DESCRIPTION
		JLabel commitDescriptionLabel = new JLabel("DESCRIPTION:");
		commitDescriptionLabel.setBounds(10, center_panel_element_height, 150, LABEL_HEIGHT);
		panelCenter.add(commitDescriptionLabel);
		center_panel_element_height += LABEL_HEIGHT;//299
		
		JTextArea commitDescriptionTextArea = new JTextArea(this.commit.getDescription());
		commitDescriptionTextArea.setEditable(false);
		
		JScrollPane commitDescriptionScroll = new JScrollPane(commitDescriptionTextArea);
		commitDescriptionScroll.setBounds(10, center_panel_element_height, WINDOW_WIDTH-40, 50);
		panelCenter.add(commitDescriptionScroll);
		commitDescriptionScroll.setVisible(true);
		center_panel_element_height += 50;//349
		
		//COMMIT FILE CHANGES
		JLabel commitFileChangesLabel = new JLabel("FILE CHANGES:");
		commitFileChangesLabel.setBounds(10, center_panel_element_height, 150, LABEL_HEIGHT);
		panelCenter.add(commitFileChangesLabel);
		center_panel_element_height += LABEL_HEIGHT;//365
		
		centerChangesListScroll = new JScrollPane();
		centerChangesListScroll.setBounds(10, center_panel_element_height, WINDOW_WIDTH-40, 300);
		panelCenter.add(centerChangesListScroll);
		
		changesList = new JList<File_Change>();
		centerChangesListScroll.setViewportView(changesList);
		changesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		showResultListaFileChanges(this.commit.getFile_changes());
		
		center_panel_element_height += 300;//665
		//////////////////////////////////////////////////////////////////////////////////
		changesList.addMouseListener(new MouseAdapter() {
		    public void mouseReleased(MouseEvent evt)
		    {
		    	errorDialogLabel.setText("");
		    	errorDialogLabel.setVisible(false);
		    	if (evt.isPopupTrigger() && evt.getComponent() instanceof JList )
		    	{
		    		JList<?> list = (JList<?>)evt.getSource();
			    	/**************************************************/
			    	// Single-click of right button detected
		        	Rectangle r = list.getCellBounds(0, list.getLastVisibleIndex());
		        	if (r != null && r.contains(evt.getPoint()))
		        	{
		        		//index detected
		        		//possible use with ListModel class and its get by index method
		        		int index = list.locationToIndex(evt.getPoint());
		        		list.setSelectedIndex(index);
		        		//Set the Input Text Field to the selected value
		        				System.out.println("SINGLE CLICK index: "+index);
		        		/****************************************************/
				        JPopupMenu menu = new JPopupMenu();
				        JMenuItem itemShowInfo = new JMenuItem("Show Info");
				        itemShowInfo.addActionListener(new ActionListener() {
		        			public void actionPerformed(ActionEvent e) {
		        				System.out.println("Selecting the element in position " + list.getSelectedValue());
		        				showFileChangeInfo((File_Change)list.getSelectedValue());
		        			}
		        		});
				        menu.add(itemShowInfo);
		        		menu.show(list, evt.getPoint().x, evt.getPoint().y);
		        	}
		        	/**************************************************/
		    	}
		    }
		});
		//////////////////////////////////////////////////////////////////////////////////
		//COMMIT PARENTS
		String parents_label_string = "PARENTS:";
		if(this.commit.getParents().size()>0)
			parents_label_string = parents_label_string+" ["+this.commit.getParents().size()+"]";
		else
			parents_label_string = parents_label_string+" [NONE]";
		JLabel commitParentsComboBoxLabel = new JLabel(parents_label_string);
		commitParentsComboBoxLabel.setBounds(10, center_panel_element_height, 150, LABEL_HEIGHT);
		panelCenter.add(commitParentsComboBoxLabel);
		center_panel_element_height += LABEL_HEIGHT;//681
		
		String[] parents_ids = new String[this.commit.getParents().size()];
		for(int parent_index=0;parent_index<parents_ids.length;parent_index++)
			parents_ids[parent_index]=this.commit.getParents().get(parent_index).getId_commit();

		parentsIDsComboBox = new JComboBox<Object>(parents_ids);
		parentsIDsComboBox.setBounds(10, center_panel_element_height, WINDOW_WIDTH-40, LABEL_HEIGHT);
		panelCenter.add(parentsIDsComboBox);
		center_panel_element_height += LABEL_HEIGHT;//697

		JButton goToParentButton = new JButton("SHOW PARENT");
		goToParentButton.setBounds(WINDOW_WIDTH-BUTTON_WIDTH-30, center_panel_element_height, BUTTON_WIDTH, BUTTON_HEIGHT);
		goToParentButton.setMargin(new Insets(0,0,0,0));
		panelCenter.add(goToParentButton);
		goToParentButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				clickShowParentButtonPressed();
			}
		});
		center_panel_element_height += BUTTON_HEIGHT;//722
		
		//COMMIT CHILDS
		String children_label_string = "CHILDREN:";
		if(this.commit.getChilds().size()>0)
			children_label_string = children_label_string+" ["+this.commit.getChilds().size()+"]";
		else
			children_label_string = children_label_string+" [NONE]";
		JLabel commitChildsComboBoxLabel = new JLabel(children_label_string);
		commitChildsComboBoxLabel.setBounds(10, center_panel_element_height, 150, LABEL_HEIGHT);
		panelCenter.add(commitChildsComboBoxLabel);
		center_panel_element_height += LABEL_HEIGHT;//738

		String[] childs_ids = new String[this.commit.getChilds().size()];
		for(int child_index=0;child_index<childs_ids.length;child_index++)
			childs_ids[child_index]=this.commit.getChilds().get(child_index).getId_commit();

		childsIDsComboBox = new JComboBox<Object>(childs_ids);
		childsIDsComboBox.setBounds(10, center_panel_element_height, WINDOW_WIDTH-40, LABEL_HEIGHT);
		panelCenter.add(childsIDsComboBox);
		center_panel_element_height += LABEL_HEIGHT;//754

		JButton goToChildButton = new JButton("SHOW CHILD");
		goToChildButton.setBounds(WINDOW_WIDTH-BUTTON_WIDTH-30, center_panel_element_height, BUTTON_WIDTH, BUTTON_HEIGHT);
		goToChildButton.setMargin(new Insets(0,0,0,0));
		panelCenter.add(goToChildButton);
		goToChildButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				clickShowChildButtonPressed();
			}
		});
		center_panel_element_height += BUTTON_HEIGHT;//779
		
		panelCenter.setVisible(true);
		//CENTER PANEL -> END#
		
		setVisible(true);
		
	}

	public void showMessage(String message) {
		this.errorDialogLabel.setText(message);
		this.errorDialogLabel.setVisible(true);
	}
	
	public void showResultListaFileChanges(ArrayList<File_Change> changes_arraylist) {
		if (this.errorDialogLabel.isVisible())
			this.errorDialogLabel.setVisible(false);
		String message="";
		if (changes_arraylist == null)
			message="|Valore NULL(Changes)!";
		else
		{
			if (changes_arraylist.isEmpty())
				message=message+" |Lista Changes VUOTA";
			else
			{
				//setListData() -> NEEDS an ARRAY[] as argument
				File_Change[] changes = changes_arraylist.toArray(new File_Change[changes_arraylist.size()]);
				this.changesList.setListData(changes);
				this.changesList.setCellRenderer(new File_Change_Renderer());
			}
		}
		this.centerChangesListScroll.setVisible(true);
	}

	public void closeForm() {
		this.dispose();
	}
	
	public void showMessagePopUp(String message, String title)
	{
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void showAlert(String message, String title)
	{
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
	}
	
	public boolean continueOperationAlert(String message, String title)
	{
		int result = JOptionPane.DEFAULT_OPTION;
		while((result!=JOptionPane.YES_OPTION)&&(result!=JOptionPane.NO_OPTION))
			result = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
		
		if(result == JOptionPane.YES_OPTION)
			return true;
		else
			return false;
	}

	public void clickShowParentButtonPressed() {
		String parent_id = String.valueOf(parentsIDsComboBox.getSelectedItem());
		//NOTE: String.valueOf() returns a STRING with "null" if the OBJECT is NULL
		if(parent_id.isEmpty())
		{
			System.out.println("->EMPTY PARENT ID");
		}
		else if(!parent_id.equalsIgnoreCase("null"))
			goToCommit(parent_id);
	}
	
	public void clickShowChildButtonPressed() {
		String child_id = String.valueOf(childsIDsComboBox.getSelectedItem());
		//NOTE: String.valueOf() returns a STRING with "null" if the OBJECT is NULL
		if(child_id.isEmpty())
		{
			System.out.println("->EMPTY CHILD ID");
		}
		else if(!child_id.equalsIgnoreCase("null"))
			goToCommit(child_id);
	}

	public void goToCommit(String commit_id) {
		if (this.errorDialogLabel.isVisible())
			this.errorDialogLabel.setVisible(false);
		System.out.println("->COMMI ID=>"+commit_id);
		Commit_View_Controller.getInstance().showCommitView(this, this.analysis_result.getCommit_history_analysis().getCommitByID(commit_id),this.analysis_result);
	}

	public void showFileChangeInfo(File_Change change)
	{
		if (this.errorDialogLabel.isVisible())
			this.errorDialogLabel.setVisible(false);
		if (change == null)
			this.showAlert("ERROR! NULL VALUE FOUND.\nUNABLE TO SHOW DATA!","NO DATA");
		else
		{
			JFrame f= new JFrame();
			JDialog d = new JDialog(f , "FILE CHANGE Info", true);
			d.getContentPane().setLayout( new FlowLayout(FlowLayout.LEFT) );
	        d.getContentPane().add(Box.createVerticalGlue());
	        //CREATE FileChangePanel
	        FileChangePanel panPane = new FileChangePanel(change);
	        d.getContentPane().add(panPane);

	        d.setSize(panPane.getWidth(), panPane.getHeight());
			d.setPreferredSize(new Dimension(panPane.getWidth(), panPane.getHeight()));
			d.setResizable(false);
	        d.setVisible(true);
		}
	}

}
