package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import controller.Author_View_Controller;
import model.Clone;
import model.Commit_Author;
import model.Commit_Node;
import persistence.System_Analysis;

public class Author_View extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2015207397593309904L;

	private Commit_Author author = null;
	private System_Analysis analysis_result = null;

	private final static int NORTH_PANEL_HEIGHT = 0;
	private final static int SOUTH_PANEL_HEIGHT = 20;
	
	private final static int MENUBAR_HEIGHT = 32;
	
	private JLabel errorDialogLabel;
	
	public Author_View(Commit_Author author, System_Analysis analysis_result)
	{
		this.author = author;
		this.analysis_result = analysis_result;
		
		setTitle("AUTHORS VIEWER");
		setResizable(false);
		//setBounds(10, 10, WINDOW_WIDTH, WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		
		super.setJMenuBar(new MenuBarDollyScreener(this, this.analysis_result));
		
		//SOUTH PANEL -> error label
		errorDialogLabel = new JLabel("Questo e' un messaggio di errore!");
		errorDialogLabel.setBounds(0, 0, 325, 16);
		errorDialogLabel.setForeground(Color.red);

		JPanel panelSouth = new JPanel();
		panelSouth.setLayout(new FlowLayout(FlowLayout.CENTER));
		//panelSouth.setBounds(0, 0, WINDOW_WIDTH, SOUTH_PANEL_HEIGHT);
		panelSouth.add(errorDialogLabel);
		getContentPane().add(panelSouth, BorderLayout.SOUTH);
		errorDialogLabel.setVisible(false);
		panelSouth.setVisible(true);
		//SOUTH PANEL -> END#
				
		//CENTER PANEL -> list
		AuthorPanel panelCenter = new AuthorPanel(this.author);
		//panelCenter.setBounds(0, 0, WINDOW_WIDTH, CENTER_PANEL_HEIGHT);
		panelCenter.setLayout(null);
		getContentPane().add(panelCenter, BorderLayout.CENTER);
		/***********************************************************************************/
		panelCenter.getCommitList().addMouseListener(new MouseAdapter() {
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
		        				showCommitInfo((Commit_Node)list.getSelectedValue());
		        			}
		        		});
				        menu.add(itemShowInfo);

				        JMenuItem itemOpenView = new JMenuItem("Open Commit View");
				        itemOpenView.addActionListener(new ActionListener() {
		        			public void actionPerformed(ActionEvent e) {
		        				System.out.println("Selecting the element in position " + list.getSelectedValue());
		        				Author_View_Controller.getInstance().showCommitView(Author_View.this,(Commit_Node)list.getSelectedValue(),Author_View.this.analysis_result);
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
		/***********************************************************************************/
		panelCenter.getCloneList().addMouseListener(new MouseAdapter() {
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
		        				showCloneInfo((Clone)list.getSelectedValue());
		        			}
		        		});
				        menu.add(itemShowInfo);

				        JMenuItem itemOpenView = new JMenuItem("Open Clone View");
				        itemOpenView.addActionListener(new ActionListener() {
		        			public void actionPerformed(ActionEvent e) {
		        				System.out.println("Selecting the element in position " + list.getSelectedValue());
		        				Author_View_Controller.getInstance().showCloneView(Author_View.this,(Clone)list.getSelectedValue(),Author_View.this.analysis_result);
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
		
		//CENTER PANEL -> END#
		int center_panel_width = panelCenter.getWidth();
		int center_panel_height = panelCenter.getHeight();
		panelCenter.setBounds(0, 0, center_panel_width, center_panel_height);
		panelSouth.setBounds(0, 0, center_panel_width, SOUTH_PANEL_HEIGHT);
		setBounds(10, 10, center_panel_width, center_panel_height+SOUTH_PANEL_HEIGHT+NORTH_PANEL_HEIGHT+MENUBAR_HEIGHT);
		
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
	
	public void showCommitInfo(Commit_Node commit)
	{
		if (this.errorDialogLabel.isVisible())
			this.errorDialogLabel.setVisible(false);
		if (commit == null)
			this.showAlert("ERROR! NULL VALUE FOUND.\nUNABLE TO SHOW DATA!","NO DATA");
		else
		{
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
	
	public void showCloneInfo(Clone clone)
	{
		if (this.errorDialogLabel.isVisible())
			this.errorDialogLabel.setVisible(false);
		if (clone == null)
			this.showAlert("ERROR! NULL VALUE FOUND.\nUNABLE TO SHOW DATA!","NO DATA");
		else
		{
			JFrame f= new JFrame();
			JDialog d = new JDialog(f , "Clone Info", true);
			d.getContentPane().setLayout( new FlowLayout(FlowLayout.LEFT) );
	        d.getContentPane().add(Box.createVerticalGlue());
	        //CREATE ClonePanel
	        ClonePanel panPane = new ClonePanel(clone);
	        d.getContentPane().add(panPane);

	        d.setSize(panPane.getWidth(), panPane.getHeight());
			d.setPreferredSize(new Dimension(panPane.getWidth(), panPane.getHeight()));
			d.setResizable(false);
	        d.setVisible(true);
		}
	}

}
