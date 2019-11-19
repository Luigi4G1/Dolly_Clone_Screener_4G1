package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import model.Clone;
import model.Current_Clone_File_Version;
import persistence.System_Analysis;

public class Clone_Evolution_View extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1980595392319802562L;

	private System_Analysis analysis_result = null;
	
	private final static int WINDOW_WIDTH = 900;
	private final static int WINDOW_HEIGHT = 450;
	
	private final static int MENUBAR_HEIGHT = 32;
	
	private final static int NORTH_PANEL_HEIGHT = 0;
	private final static int SOUTH_PANEL_HEIGHT = 20;

	private final static int SPACE = 10;
	private final static int LABEL_HEIGHT = 16;
	private final static int IMAGE_HEIGHT = 64;
	private final static int LIST_HEIGHT = 100;
	
	private JLabel errorDialogLabel;

	private Clone clone;
	private JList<Current_Clone_File_Version> cloneVersionList;
	private JScrollPane cloneVersionListScroll;
	
	public Clone_Evolution_View(Clone clone, ArrayList<Current_Clone_File_Version> clone_backward_evolution, System_Analysis analysis_result) {
		
		this.clone = clone;
		this.analysis_result = analysis_result;
		
		setTitle("CLONE EVOLUTION VIEWER");
		setResizable(false);
		setBounds(10, 10, WINDOW_WIDTH, WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		
		super.setJMenuBar(new MenuBarDollyScreener(this, this.analysis_result));

		//SOUTH PANEL -> error label
		errorDialogLabel = new JLabel("Questo e' un messaggio di errore!");
		errorDialogLabel.setBounds(0, 0, WINDOW_WIDTH, LABEL_HEIGHT);
		errorDialogLabel.setForeground(Color.red);
		
		JPanel panelSouth = new JPanel();
		panelSouth.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelSouth.setBounds(0, 0, WINDOW_WIDTH, SOUTH_PANEL_HEIGHT);
		panelSouth.add(errorDialogLabel);
		getContentPane().add(panelSouth, BorderLayout.SOUTH);
		errorDialogLabel.setVisible(false);
		panelSouth.setVisible(true);
		//SOUTH PANEL -> END#
		
		//CENTER PANEL -> list
		ClonePanel panelCenter = new ClonePanel(this.clone);
		panelCenter.setLayout(null);
		getContentPane().add(panelCenter, BorderLayout.CENTER);

		int center_panel_height = panelCenter.getHeight() - IMAGE_HEIGHT;
		int center_panel_width = panelCenter.getWidth();
		
		JLabel timeImageLabel = new JLabel("Clones backward evolution:");
		timeImageLabel.setBounds(SPACE, center_panel_height, WINDOW_WIDTH/2, IMAGE_HEIGHT);
		panelCenter.add(timeImageLabel);
		center_panel_height += IMAGE_HEIGHT;
		
		String time_icon_name = "time_back_256x256";
		ImageIcon time_label_imageIcon = null;
		try {
			time_label_imageIcon = new ImageIcon(getClass().getResource("/icons/" + time_icon_name + ".png"));
			Image image = time_label_imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(IMAGE_HEIGHT, IMAGE_HEIGHT,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			time_label_imageIcon = new ImageIcon(newimg);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		if (time_label_imageIcon != null)
			timeImageLabel.setIcon(time_label_imageIcon);
		
		//LIST
		cloneVersionListScroll = new JScrollPane();
		cloneVersionListScroll.setBounds(SPACE, center_panel_height, center_panel_width-3*SPACE, LIST_HEIGHT);
		panelCenter.add(cloneVersionListScroll);
		center_panel_height += LIST_HEIGHT+70;
		
		cloneVersionList = new JList<Current_Clone_File_Version>();
		cloneVersionListScroll.setViewportView(cloneVersionList);
		cloneVersionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		Current_Clone_File_Version[] evolutions = new Current_Clone_File_Version[clone_backward_evolution.size()];
		for(int i =0; i<evolutions.length; i++)
			evolutions[i] = clone_backward_evolution.get(i);
		this.cloneVersionList.setListData(evolutions);
		this.cloneVersionList.setCellRenderer(new Current_Clone_File_Version_Renderer());
		cloneVersionListScroll.setVisible(true);

		cloneVersionList.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent evt) {
				
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
		        				showInfo((Current_Clone_File_Version)list.getSelectedValue());
		        			}
		        		});
				        menu.add(itemShowInfo);
				        
				        menu.show(list, evt.getPoint().x, evt.getPoint().y);
		        	}
		    	}
			}
		});

		//CENTER PANEL -> END#
		panelCenter.setBounds(0, 0, center_panel_width, center_panel_height);
		panelSouth.setBounds(0, 0, center_panel_width, SOUTH_PANEL_HEIGHT);
		setBounds(SPACE, SPACE, center_panel_width, center_panel_height+SOUTH_PANEL_HEIGHT+NORTH_PANEL_HEIGHT);
		setSize(center_panel_width, center_panel_height+SOUTH_PANEL_HEIGHT+NORTH_PANEL_HEIGHT+MENUBAR_HEIGHT);

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
	
	public void showInfo(Current_Clone_File_Version version)
	{
		if (this.errorDialogLabel.isVisible())
			this.errorDialogLabel.setVisible(false);
		if (version == null)
			this.showAlert("ERROR! NULL VALUE FOUND.\nUNABLE TO SHOW DATA!","NO DATA");
		else
		{
			JFrame f= new JFrame();
			JDialog d = new JDialog(f , "File Chronology Version Info", false);
			d.getContentPane().setLayout( new FlowLayout(FlowLayout.LEFT) );
	        d.getContentPane().add(Box.createVerticalGlue());
	        
	        //CREATE ClonePanel
	        Current_Clone_File_Version_Panel panPane = new Current_Clone_File_Version_Panel(version);
	        d.getContentPane().add(panPane);

	        d.setSize(panPane.getWidth(), panPane.getHeight());
			d.setPreferredSize(new Dimension(panPane.getWidth(), panPane.getHeight()));
			d.setResizable(false);
	        d.setVisible(true);
		}
	}

}
