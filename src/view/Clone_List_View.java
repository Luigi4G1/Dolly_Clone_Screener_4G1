package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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

import controller.Clone_List_Controller;
import model.Clone;
import model.Report_NiCad5;
import persistence.System_Analysis;

public class Clone_List_View extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2239447528106276090L;

	private System_Analysis analysis_result = null;
	
	private final static int WINDOW_WIDTH = 900;
	private final static int WINDOW_HEIGHT = 450;
	
	private final static int MENUBAR_HEIGHT = 32;
	
	private final static int NORTH_PANEL_HEIGHT = 0;
	private final static int SOUTH_PANEL_HEIGHT = 20;
	private final static int CENTER_PANEL_HEIGHT = WINDOW_HEIGHT-NORTH_PANEL_HEIGHT-2*SOUTH_PANEL_HEIGHT-MENUBAR_HEIGHT;
	
	private final static int SPACE = 10;
	private final static int LABEL_HEIGHT = 16;
	private final static int IMAGE_HEIGHT = 64;
	
	private JLabel errorDialogLabel;

	private JList<Clone> cloneList;
	private JScrollPane cloneListScroll;
	
	private JLabel cloneImageLabel;
	
	private JButton findAllAuthorsButton;
	private JFrame splash_frame = null;
	
	public Clone_List_View(System_Analysis analysis_result)
	{
		this.analysis_result = analysis_result;
		
		setTitle("CLONE LIST VIEWER");
		setResizable(false);
		setBounds(100, 100, WINDOW_WIDTH, WINDOW_HEIGHT);
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
		int center_panel_y = SPACE;
		
		JPanel panelCenter = new JPanel();
		panelCenter.setBounds(0, 0, WINDOW_WIDTH, CENTER_PANEL_HEIGHT);
		panelCenter.setLayout(null);
		getContentPane().add(panelCenter, BorderLayout.CENTER);
		
		/********************************************************************/
		findAllAuthorsButton = new JButton("FIND ALL AUTHORS");
		findAllAuthorsButton.setBounds(WINDOW_WIDTH/2+SPACE-110, center_panel_y, 220, IMAGE_HEIGHT);
		findAllAuthorsButton.setSize(220, IMAGE_HEIGHT);
		panelCenter.add(findAllAuthorsButton);
		
		findAllAuthorsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				clickButtonFindAllAuthors();
			}
		});
		String all_authors_image_name = "search_author_256x256";
		ImageIcon all_authors_imageIcon = null;
		try {
			all_authors_imageIcon = new ImageIcon(getClass().getResource("/icons/" + all_authors_image_name + ".png"));
			Image image = all_authors_imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(IMAGE_HEIGHT-5, IMAGE_HEIGHT-5,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			all_authors_imageIcon = new ImageIcon(newimg);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		if (all_authors_imageIcon != null)
			findAllAuthorsButton.setIcon(all_authors_imageIcon);
		/********************************************************************/
		JButton showNiCadButton = new JButton();
		showNiCadButton.setBounds(WINDOW_WIDTH-IMAGE_HEIGHT-50, center_panel_y, IMAGE_HEIGHT, IMAGE_HEIGHT);
		showNiCadButton.setSize(IMAGE_HEIGHT, IMAGE_HEIGHT);
		panelCenter.add(showNiCadButton);
		
		showNiCadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showInfoNiCad(Clone_List_View.this.analysis_result.getReport_clone_analysis());
			}
		});
		String nicad_image_name = "medical_history_256x256";
		ImageIcon nicad_imageIcon = null;
		try {
			nicad_imageIcon = new ImageIcon(getClass().getResource("/icons/" + nicad_image_name + ".png"));
			Image image = nicad_imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(IMAGE_HEIGHT, IMAGE_HEIGHT,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			nicad_imageIcon = new ImageIcon(newimg);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		if (nicad_imageIcon != null)
			showNiCadButton.setIcon(nicad_imageIcon);
		else
			showNiCadButton.setText("NiCad");
		
		cloneImageLabel = new JLabel("Clones found:");
		cloneImageLabel.setBounds(SPACE, center_panel_y, WINDOW_WIDTH/2, IMAGE_HEIGHT);
		panelCenter.add(cloneImageLabel);
		center_panel_y += IMAGE_HEIGHT;
		
		String clone_icon_name = "cloning_machine_256x256";
		ImageIcon clone_label_imageIcon = null;
		try {
			clone_label_imageIcon = new ImageIcon(getClass().getResource("/icons/" + clone_icon_name + ".png"));
			Image image = clone_label_imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(IMAGE_HEIGHT, IMAGE_HEIGHT,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			clone_label_imageIcon = new ImageIcon(newimg);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		if (clone_label_imageIcon != null)
			cloneImageLabel.setIcon(clone_label_imageIcon);

		cloneListScroll = new JScrollPane();
		cloneListScroll.setBounds(SPACE, center_panel_y, WINDOW_WIDTH-3*SPACE, CENTER_PANEL_HEIGHT-center_panel_y-3*SPACE);
		panelCenter.add(cloneListScroll);
		
		cloneList = new JList<Clone>();
		cloneListScroll.setViewportView(cloneList);
		cloneList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		cloneListScroll.setVisible(false);
		showResultListClone(new ArrayList<Clone>(this.analysis_result.getReport_clone_analysis().getClones().values()));
		/////////////////////////////////////////////////////////////////
		cloneList.addMouseListener(new MouseAdapter() {
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
		        				showInfo((Clone)list.getSelectedValue());
		        			}
		        		});
				        menu.add(itemShowInfo);

				        JMenuItem itemOpenView = new JMenuItem("Open Clone View");
				        itemOpenView.addActionListener(new ActionListener() {
		        			public void actionPerformed(ActionEvent e) {
		        				System.out.println("Selecting the element in position " + list.getSelectedValue());
		        				Clone_List_Controller.getInstance().showCloneView(Clone_List_View.this,(Clone)list.getSelectedValue(),Clone_List_View.this.analysis_result);
		        			}
		        		});
				        menu.add(itemOpenView);
				        
				        JMenuItem itemOpenBackWEvolView = new JMenuItem("Open Clone BW Evolution");
				        itemOpenBackWEvolView.addActionListener(new ActionListener() {
		        			public void actionPerformed(ActionEvent e) {
		        				System.out.println("Selecting the element in position " + list.getSelectedValue());
		        				Clone_List_Controller.getInstance().showCloneBWEvolution(Clone_List_View.this,(Clone)list.getSelectedValue(),Clone_List_View.this.analysis_result);
		        			}
		        		});
				        menu.add(itemOpenBackWEvolView);

		        		menu.show(list, evt.getPoint().x, evt.getPoint().y);
		        	}
		        	/**************************************************/
		    	}
		    }
		});
		////////////////////////////////////////////////////////////////
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
	
	public void showResultListClone(ArrayList<Clone> clone_arraylist) {
		if (this.errorDialogLabel.isVisible())
			this.errorDialogLabel.setVisible(false);
		String message="";
		if (clone_arraylist == null)
			message="|NULL Value(Clone List)!";
		else
		{
			if (clone_arraylist.isEmpty())
				message=message+" |Clone List EMPTY";
			else
			{
				//setListData() -> NEEDS an ARRAY[] as argument
				Clone[] commits = clone_arraylist.toArray(new Clone[clone_arraylist.size()]);
				this.cloneList.setListData(commits);
				this.cloneList.setCellRenderer(new Clone_Renderer());
				this.cloneImageLabel.setText("Clones found:["+commits.length+"]");
			}
		}
		this.cloneListScroll.setVisible(true);
		if (!message.equalsIgnoreCase(""))
			this.showMessage(message);
	}
	
	public void showInfo(Clone clone)
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
	
	public void showInfoNiCad(Report_NiCad5 report)
	{
		if (this.errorDialogLabel.isVisible())
			this.errorDialogLabel.setVisible(false);
		if (report == null)
			this.showAlert("ERROR! NULL VALUE FOUND.\nUNABLE TO SHOW DATA!","NO DATA");
		else
		{
			JFrame f= new JFrame();
			JDialog d = new JDialog(f , "NiCad Report Info", true);
			d.getContentPane().setLayout( new FlowLayout(FlowLayout.LEFT) );
	        d.getContentPane().add(Box.createVerticalGlue());
	        //CREATE ClonePanel
	        NiCadPanel panPane = new NiCadPanel(report);
	        d.getContentPane().add(panPane);

	        d.setSize(panPane.getWidth(), panPane.getHeight());
			d.setPreferredSize(new Dimension(panPane.getWidth(), panPane.getHeight()));
			d.setResizable(false);
	        d.setVisible(true);
		}
	}

	public void clickButtonFindAllAuthors()
	{
		String message = "THIS OPERATION MAY TAKE A LOT OF TIME TO COMPLETE!\n"
				+ "Do you want to search for ALL CLONE AUTHORS?";
		String title = "ATTENTION";
		boolean continue_op = this.continueOperationAlert(message, title);
		
		if(continue_op)
		{
			findAllAuthorsButton.getModel().setPressed(true);
			findAllAuthorsButton.setEnabled(false);
			Thread splash_thread = new Thread(new Runnable() {
				public void run() {
					try 
					{
						showSearchingSplash();

					}catch (Exception e) {
						e.printStackTrace();
						findAllAuthorsButton.setEnabled(true);
						findAllAuthorsButton.getModel().setPressed(false);
					}
				}
			});
			
			Thread function_thread = new Thread(new Runnable() {
				@SuppressWarnings("static-access")
				public void run() {
					try 
					{
						Thread.currentThread().yield();
						Clone_List_Controller.getInstance().findAllClonesAuthors(Clone_List_View.this, analysis_result);
						//showFindAuthorImageButton();
						closeSearchingSplash();
						
					}catch (Exception e) {
						e.printStackTrace();
						findAllAuthorsButton.setEnabled(true);
						findAllAuthorsButton.getModel().setPressed(false);
					}
					finally
					{
						findAllAuthorsButton.setEnabled(true);
						findAllAuthorsButton.getModel().setPressed(false);
					}
				}
			});
			
			splash_thread.start();
			function_thread.start();
		}
	}
	
	public void showSearchingSplash()
	{
		splash_frame = new JFrame();
		JDialog d = new JDialog(splash_frame , "", false);
		d.setUndecorated(true);
		//Standard window bounds
		int width = 450;
		int height =115;
		JLabel labelSplashImage = new JLabel();
		
		ImageIcon backgroundImageIcon = null;
		String filename = "fingerprint_loading_rotating_speedx4_256x256.gif";
		try {
			backgroundImageIcon = new ImageIcon(getClass().getResource("/splash_images/"+filename));
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		if(backgroundImageIcon != null)
		{
			labelSplashImage.setIcon(backgroundImageIcon);
			width = backgroundImageIcon.getIconWidth();
			height = backgroundImageIcon.getIconHeight();
			labelSplashImage.setOpaque(true);
			d.getContentPane().add(labelSplashImage);
		}

		//Center the window
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width-width)/2;
		int y = (screen.height-height)/2;
		d.setBounds(x,y,width,height);
		
        d.setSize(width, height);
		d.setPreferredSize(new Dimension(width, height));
		d.setResizable(false);
		d.setAlwaysOnTop(true);
		d.setVisible(true);
	}
	
	public void closeSearchingSplash()
	{
		if(splash_frame != null)
			splash_frame.dispose();
	}
}
