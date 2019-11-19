package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.Box;
import javax.swing.ImageIcon;
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
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;

import controller.Commit_List_Controller;
import model.Commit_Author;
import model.Commit_History;
import model.Commit_Node;
import persistence.System_Analysis;

public class Commit_List_Report_View extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private System_Analysis analysis_result = null;
	
	private Commit_History history = null;
	
	private JList<Commit_Node> commitList;
	private JLabel errorDialogLabel;
	
	private JScrollPane centerCommitListScroll;
	
	private JComboBox<Object> commitAuthorFilter;
	private static final String NO_FILTER_STRING = "NO FILTER";
	private final int text_char_space = 10;
	
	private JToggleButton asc_descOrderListToggle;
	private static final String ASC_DESC_STRING = "ASC<+>DESC";
	private static final String ASC_STRING = "ASC  /|\\";
	private static final String DESC_STRING = "DESC  \\|/";
	private static final String ORDERING_STRING = "ORDERING...";
	private ImageIcon pressed_toggle_imageIcon;

	private final static int BUTTON_HEIGHT = 40;
	
	private final static int MENUBAR_HEIGHT = 32;
	
	private JLabel commitsLabel;
	
	public Commit_List_Report_View(System_Analysis analysis_result)
	{
		this.analysis_result = analysis_result;
		this.history = this.analysis_result.getCommit_history_analysis();
		
		setTitle("COMMIT HISTORY VIEWER");
		setResizable(false);
		setBounds(100, 100, 700, 400+MENUBAR_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		
		super.setJMenuBar(new MenuBarDollyScreener(this, this.analysis_result));
		
		//SOUTH PANEL -> error label
		errorDialogLabel = new JLabel("Questo e' un messaggio di errore!");
		errorDialogLabel.setBounds(0, 0, 325, 16);
		errorDialogLabel.setForeground(Color.red);
		
		JPanel panelSouth = new JPanel();
		panelSouth.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelSouth.setBounds(0, 0, 690, 20);
		panelSouth.add(errorDialogLabel);
		getContentPane().add(panelSouth, BorderLayout.SOUTH);
		errorDialogLabel.setVisible(false);
		panelSouth.setVisible(true);
		//SOUTH PANEL -> END#
		
		//CENTER PANEL -> list
		JPanel panelCenter = new JPanel();
		panelCenter.setBounds(0, 0, 620, 300);
		panelCenter.setLayout(null);
		getContentPane().add(panelCenter, BorderLayout.CENTER);
		
		commitsLabel = new JLabel("Commits:");
		commitsLabel.setBounds(35, 25, 100, 16);
		panelCenter.add(commitsLabel);
		
		JLabel authorLabel = new JLabel("Author:");
		authorLabel.setBounds(230, 25, 42, 16);
		panelCenter.add(authorLabel);
		
		boolean selected = true;
		//asc_descOrderListToggle = new JToggleButton(ASC_STRING, selected);
		asc_descOrderListToggle = new JToggleButton("", selected);
		/************************************************************************************/
		String pressed_toggle_icon_name = "ordering_768x256";
		pressed_toggle_imageIcon = null;
		try {
			pressed_toggle_imageIcon = new ImageIcon(getClass().getResource("/icons/" + pressed_toggle_icon_name + ".png"));
			Image image = pressed_toggle_imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(96, 32,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			pressed_toggle_imageIcon = new ImageIcon(newimg);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		if (pressed_toggle_imageIcon != null)
			asc_descOrderListToggle.setPressedIcon(pressed_toggle_imageIcon);
		else
			asc_descOrderListToggle.setText(ORDERING_STRING);
		/************************************************************************************/
		asc_descOrderListToggle.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent arg0)
			{
				String author = ""+commitAuthorFilter.getItemAt(commitAuthorFilter.getSelectedIndex());
				boolean asc_order = asc_descOrderListToggle.isSelected();
				System.out.println("PRESSED->"+asc_order);
				if (pressed_toggle_imageIcon != null)
					asc_descOrderListToggle.setText("");
				else
					asc_descOrderListToggle.setText(ORDERING_STRING);

				filterListByAuthor(author, asc_order);
				togglePressed();
			}
			
		});
		
		int jcombobox_max_width = NO_FILTER_STRING.length()*text_char_space;
		ArrayList<Commit_Author> authors = new ArrayList<Commit_Author>(this.history.getCommit_authors().values());
		ArrayList<String> authors_names = new ArrayList<String>();
		
		for(Commit_Author author:authors)
		{
			authors_names.add(author.getId_author());
			if((author.getId_author().length()*text_char_space)>jcombobox_max_width)
				jcombobox_max_width = author.getId_author().length()*text_char_space;
		}
			
		//ASCENDING ORDER
		Collections.sort(authors_names);
		authors_names.add(0,NO_FILTER_STRING);
		String[] filters_author = authors_names.toArray(new String[authors_names.size()]);
		commitAuthorFilter = new JComboBox<Object>(filters_author);
		commitAuthorFilter.setBounds(280, 25, jcombobox_max_width, 16);
		panelCenter.add(commitAuthorFilter);
		/************************************************************************************/
		/*NOTE:SEEMS SLOWER - BUT WORKS CORRECTLY
		commitAuthorFilter.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				String author = ""+commitAuthorFilter.getItemAt(commitAuthorFilter.getSelectedIndex());
				boolean asc_order = asc_descOrderListToggle.isSelected();
				// -> JToggle value
				//true -> ASC
				//false -> DESC
				filterListByAuthor(author, asc_order);
			}
		});*/
		/************************************************************************************/
		/************************************************************************************/
		commitAuthorFilter.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e)
			{
				if(e.getStateChange() == ItemEvent.SELECTED)
				{
					String author = ""+e.getItem();
					boolean asc_order = asc_descOrderListToggle.isSelected();
					// -> JToggle value
					//true -> ASC
					//false -> DESC
					filterListByAuthor(author, asc_order);
				}
			}
		});
		/************************************************************************************/

		centerCommitListScroll = new JScrollPane();
		centerCommitListScroll.setBounds(35, 55, 620, 260);
		panelCenter.add(centerCommitListScroll);
		
		commitList = new JList<Commit_Node>();
		centerCommitListScroll.setViewportView(commitList);
		commitList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		centerCommitListScroll.setVisible(false);
		/***********************************************************************************/
		commitList.addMouseListener(new MouseAdapter() {
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
		        				showInfo((Commit_Node)list.getSelectedValue());
		        			}
		        		});
				        menu.add(itemShowInfo);

				        JMenuItem itemOpenView = new JMenuItem("Open Commit View");
				        itemOpenView.addActionListener(new ActionListener() {
		        			public void actionPerformed(ActionEvent e) {
		        				System.out.println("Selecting the element in position " + list.getSelectedValue());
		        				Commit_List_Controller.getInstance().showCommitView(Commit_List_Report_View.this,(Commit_Node)list.getSelectedValue(),Commit_List_Report_View.this.analysis_result);
		        			}
		        		});
				        menu.add(itemOpenView);

		        		menu.show(list, evt.getPoint().x, evt.getPoint().y);
		        	}
		        	/**************************************************/
		    	}
		    }
		});
		/***********************************************************************************/
		JLabel switchLabel = new JLabel(ASC_DESC_STRING);
		//switchLabel.setBounds(480, 25, 80, 16);
		switchLabel.setBounds(440, 25, 80, 16);
		panelCenter.add(switchLabel);
		
		//asc_descOrderListToggle.setBounds(570, 25, 80, 16);
		asc_descOrderListToggle.setBounds(530, 13, BUTTON_HEIGHT*3, BUTTON_HEIGHT);
		panelCenter.add(asc_descOrderListToggle);
		asc_descOrderListToggle.setVisible(true);
		asc_descOrderListToggle.doClick();
		//CENTER PANEL -> END#
		
		setVisible(true);
	}
	
	public void showMessage(String message) {
		this.errorDialogLabel.setText(message);
		this.errorDialogLabel.setVisible(true);
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
	
	public void showResultListCommit(ArrayList<Commit_Node> commit_arraylist, boolean asc_order) {
		if (this.errorDialogLabel.isVisible())
			this.errorDialogLabel.setVisible(false);
		String message="";
		if (commit_arraylist == null)
			message="|NULL Value(Commit List)!";
		else
		{
			if (commit_arraylist.isEmpty())
				message=message+" |Commit List EMPTY";
			else
			{
				//setListData() -> NEEDS an ARRAY[] as argument
				//Ordered by ASCENDING-DESCENDING DATE
				if(asc_order)
					Collections.sort(commit_arraylist, Commit_Node.Commit_DateASC_Comparator);
				else
					Collections.sort(commit_arraylist, Commit_Node.Commit_DateDESC_Comparator);
				Commit_Node[] commits = commit_arraylist.toArray(new Commit_Node[commit_arraylist.size()]);
				this.commitList.setListData(commits);
				this.commitList.setCellRenderer(new Commit_Renderer());
				this.commitsLabel.setText("Commits:["+commits.length+"]");
			}
		}
		this.centerCommitListScroll.setVisible(true);
		if (!message.equalsIgnoreCase(""))
			this.showMessage(message);
	}
	
	public void fillComboBoxChoices(ArrayList<String> filters)
	{
		if(commitAuthorFilter != null)
		{
			String no_filter = "NO FILTER";
			ArrayList<String> filters_copy = new ArrayList<String>(filters);
			//ASCENDING ORDER
			Collections.sort(filters_copy);
			filters_copy.add(0, no_filter);
			commitAuthorFilter.removeAllItems();
			//commitAuthorFilter
		}
	}
	
	public void filterListByAuthor(String author, boolean asc_order)
	{
		if((author != null) && (!author.isEmpty()))
		{
			ArrayList<Commit_Node> commit_arraylist = null;
			if(author.equalsIgnoreCase(NO_FILTER_STRING))
			{
				commit_arraylist = new ArrayList<Commit_Node>(this.history.getCommit_nodes().values());
			}
			else
			{
				commit_arraylist = this.history.filterCommits_ByAuthorID(author);
			}
			if(commit_arraylist != null)
			{
				showResultListCommit(commit_arraylist, asc_order);
			}
		}
	}
	
	public void togglePressed()
	{
		/************************************************************************************/
		String asc_toggle_icon_name = "elder_to_baby_768x256";
		ImageIcon asc_toggle_imageIcon = null;
		try {
			asc_toggle_imageIcon = new ImageIcon(getClass().getResource("/icons/" +asc_toggle_icon_name + ".png"));
			Image image = asc_toggle_imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(96, 32,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			asc_toggle_imageIcon = new ImageIcon(newimg);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		/************************************************************************************/
		String desc_toggle_icon_name = "baby_to_elder_768x256";
		ImageIcon desc_toggle_imageIcon = null;
		try {
			desc_toggle_imageIcon = new ImageIcon(getClass().getResource("/icons/" +desc_toggle_icon_name + ".png"));
			Image image = desc_toggle_imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(96, 32,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			desc_toggle_imageIcon = new ImageIcon(newimg);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		/************************************************************************************/
		if(asc_descOrderListToggle.isSelected())
		{
			if (asc_toggle_imageIcon != null)
			{
				asc_descOrderListToggle.setText("");
				asc_descOrderListToggle.setIcon(asc_toggle_imageIcon);
			}
			else
				asc_descOrderListToggle.setText(ASC_STRING);
		}
		else
		{
			if (desc_toggle_imageIcon != null)
			{
				asc_descOrderListToggle.setText("");
				asc_descOrderListToggle.setIcon(desc_toggle_imageIcon);
			}
			else
				asc_descOrderListToggle.setText(DESC_STRING);
		}
		/************************************************************************************/
	}
	
	public void showInfo(Commit_Node commit)
	{
		if (this.errorDialogLabel.isVisible())
			this.errorDialogLabel.setVisible(false);
		if (commit == null)
			this.showAlert("ERROR! NULL VALUE FOUND.\nUNABLE TO SHOW DATA!","NO DATA");
		else
		{
			//COMMITPANEL inside Dialog FRAME
			JFrame f= new JFrame();
			JDialog d = new JDialog(f , "Commit Info", true);
			d.getContentPane().setLayout( new FlowLayout(FlowLayout.LEFT) );
	        d.getContentPane().add(Box.createVerticalGlue());
	        //CREATE CommitPanel
	        CommitPanel panPane = new CommitPanel(commit);
	        d.getContentPane().add(panPane);

	        d.setSize(panPane.getWidth(), panPane.getHeight());
			d.setPreferredSize(new Dimension(panPane.getWidth(), panPane.getHeight()));
			d.setResizable(false);
	        d.setVisible(true);
		}
	}

}
