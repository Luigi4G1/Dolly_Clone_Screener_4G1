package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.Home_System_Analysis_Controller;
import persistence.System_Analysis;

public class Home_System_Analysis_View extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7252488364826262530L;

	private System_Analysis analysis_result = null;
	
	private final static int WINDOW_WIDTH = 400;
	private final static int WINDOW_HEIGHT = 600;
	
	private final static int NORTH_PANEL_HEIGHT = 0;
	private final static int SOUTH_PANEL_HEIGHT = 20;
	
	//private final static int TEXTFIELD_HEIGHT = 30;
	
	private final static int BUTTON_WIDTH = 250;
	private final static int BUTTON_HEIGHT = 40;
	
	private JLabel errorDialogLabel;
	
	private JButton commitHistoryButton;
	private JButton cloneNiCadReportButton;
	private JButton authorsButton;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try 
				{
					Home_System_Analysis_View frame = new Home_System_Analysis_View(null);
					frame.setVisible(true);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
	
	public Home_System_Analysis_View(System_Analysis analysis_result)
	{
		this.analysis_result = analysis_result;
		
		setTitle("SELECT SYSTEM ANALYSIS");
		setResizable(false);
		setBounds(100, 100, WINDOW_WIDTH, WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		
		super.setJMenuBar(new MenuBarDollyScreener(this, this.analysis_result));
		
		//SOUTH PANEL -> error label
		errorDialogLabel = new JLabel("Questo e' un messaggio di errore!");
		errorDialogLabel.setBounds(0, 0, WINDOW_WIDTH, SOUTH_PANEL_HEIGHT-4);
		errorDialogLabel.setForeground(Color.red);

		JPanel panelSouth = new JPanel();
		panelSouth.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelSouth.setBounds(0, 0, WINDOW_WIDTH-20, SOUTH_PANEL_HEIGHT);//prev->690
		panelSouth.add(errorDialogLabel);
		getContentPane().add(panelSouth, BorderLayout.SOUTH);
		errorDialogLabel.setVisible(false);//TEST->true
		panelSouth.setVisible(true);
		//SOUTH PANEL -> END#
		
		//CENTER PANEL -> BUTTON SELECTION
		String background_image_name = "parse_document_256x256";
		///////////////////////////////////////////////
		Image backgroundImage = null;
		try
		{
			ImageIcon backgroundImageIcon = new ImageIcon(getClass().getResource("/icons/" + background_image_name + ".png"));
			backgroundImage = backgroundImageIcon.getImage();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		////////////////////////////////////////
		JPanel panelCenter;
		System.out.println("BACKGROUND IMAGE AVAILABLE?->"+(backgroundImage != null));

		if(backgroundImage != null)
			panelCenter = new ImagePanel(backgroundImage);
		else
			panelCenter = new JPanel();
		int panel_center_height = WINDOW_HEIGHT-SOUTH_PANEL_HEIGHT-NORTH_PANEL_HEIGHT;
		panelCenter.setBounds(0, 0, WINDOW_WIDTH-20, panel_center_height);//620 MIN->700
		panelCenter.setLayout(null);
		getContentPane().add(panelCenter, BorderLayout.CENTER);
		
		//CURRENT OBJECT HEIGHT
		int center_panel_element_height = panel_center_height/3-BUTTON_HEIGHT;
		int center_button = WINDOW_WIDTH/2 - BUTTON_WIDTH/2;
		
		commitHistoryButton = new JButton("COMMIT HISTORY");
		commitHistoryButton.setBounds(center_button, center_panel_element_height, BUTTON_WIDTH, BUTTON_HEIGHT);
		String commit_history_button_icon_name = "commit_icon_256x256";
		ImageIcon commit_history_button_imageIcon = null;
		try {
			commit_history_button_imageIcon = new ImageIcon(getClass().getResource("/icons/" + commit_history_button_icon_name + ".png"));
			Image image = commit_history_button_imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(32, 32,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			commit_history_button_imageIcon = new ImageIcon(newimg);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		if (commit_history_button_imageIcon != null)
			commitHistoryButton.setIcon(commit_history_button_imageIcon);
		panelCenter.add(commitHistoryButton);
		
		commitHistoryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				clickButtonCommitHistory();
			}
			
		});
		
		//CURRENT OBJECT HEIGHT
		center_panel_element_height = panel_center_height/2-BUTTON_HEIGHT/2;
		
		cloneNiCadReportButton = new JButton("CLONES - NiCad Report");
		cloneNiCadReportButton.setBounds(center_button, center_panel_element_height, BUTTON_WIDTH, BUTTON_HEIGHT);
		String clone_report_button_icon_name = "clone_microscope_256x256";
		ImageIcon clone_report_button_imageIcon = null;
		try {
			clone_report_button_imageIcon = new ImageIcon(getClass().getResource("/icons/" + clone_report_button_icon_name + ".png"));
			Image image = clone_report_button_imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(32, 32,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			clone_report_button_imageIcon = new ImageIcon(newimg);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		if (clone_report_button_imageIcon != null)
			cloneNiCadReportButton.setIcon(clone_report_button_imageIcon);
		panelCenter.add(cloneNiCadReportButton);
		
		cloneNiCadReportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				clickButtonCloneNiCadReport();
			}
			
		});
		
		//CURRENT OBJECT HEIGHT
		center_panel_element_height = panel_center_height*2/3+BUTTON_HEIGHT;
		
		authorsButton = new JButton("AUTHORS");
		authorsButton.setBounds(center_button, center_panel_element_height, BUTTON_WIDTH, BUTTON_HEIGHT);
		String authors_button_icon_name = "author_icon_256x256";
		ImageIcon authors_button_imageIcon = null;
		try {
			authors_button_imageIcon = new ImageIcon(getClass().getResource("/icons/" + authors_button_icon_name + ".png"));
			Image image = authors_button_imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(32, 32,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			authors_button_imageIcon = new ImageIcon(newimg);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		if (authors_button_imageIcon != null)
			authorsButton.setIcon(authors_button_imageIcon);
		panelCenter.add(authorsButton);
		
		authorsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				clickButtonAuthorsList();
			}
			
		});

		if(analysis_result == null)
		{
			enabledButtons(false, false, false);
			showAlert("NO SYSTEM DATA AVAILABLE", "ATTENTION");
		}
		else
		{
			boolean commitHistoryEnabled = true;
			boolean cloneNiCadReportEnabled = true;
			boolean authorsEnabled = true;
			if((analysis_result.getCommit_history_analysis() == null)||(analysis_result.getCommit_history_analysis().getCommit_nodes() == null))
			{
				commitHistoryEnabled = false;
				showAlert("NO COMMIT DATA AVAILABLE", "ATTENTION");
			}
			
			if((analysis_result.getReport_clone_analysis() == null)||(analysis_result.getReport_clone_analysis().getClones() == null))
			{
				cloneNiCadReportEnabled = false;
				showAlert("NO CLONE DATA AVAILABLE", "ATTENTION");
			}
			
			if((analysis_result.getCommit_history_analysis() == null)||(analysis_result.getCommit_history_analysis().getCommit_authors() == null))
			{
				authorsEnabled = false;
				showAlert("NO AUTHOR DATA AVAILABLE", "ATTENTION");
			}
			
			enabledButtons(commitHistoryEnabled, cloneNiCadReportEnabled, authorsEnabled);
		}

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
	
	public void enabledButtons(boolean commitHistoryEnabled, boolean cloneNiCadReportEnabled, boolean authorsEnabled)
	{
		commitHistoryButton.setEnabled(commitHistoryEnabled);
		cloneNiCadReportButton.setEnabled(cloneNiCadReportEnabled);
		authorsButton.setEnabled(authorsEnabled);
	}
	
	public void enableCommitHistoryButton(boolean commitHistoryEnabled)
	{
		commitHistoryButton.setEnabled(commitHistoryEnabled);
	}
	
	public void enableCloneNiCadReportButton(boolean cloneNiCadReportEnabled)
	{
		cloneNiCadReportButton.setEnabled(cloneNiCadReportEnabled);
	}
	
	public void enabledAuthorsButton(boolean authorsEnabled)
	{
		authorsButton.setEnabled(authorsEnabled);
	}

	public void clickButtonCommitHistory()
	{
		this.errorDialogLabel.setText("");
		this.errorDialogLabel.setVisible(false);
		
		Home_System_Analysis_Controller.getInstance().goToCommitsList(this, this.analysis_result);
	}
	
	public void clickButtonCloneNiCadReport()
	{
		this.errorDialogLabel.setText("");
		this.errorDialogLabel.setVisible(false);
		
		Home_System_Analysis_Controller.getInstance().goToClonesList(this, this.analysis_result);
	}
	
	public void clickButtonAuthorsList()
	{
		this.errorDialogLabel.setText("");
		this.errorDialogLabel.setVisible(false);
		
		Home_System_Analysis_Controller.getInstance().goToAuthorsList(this, this.analysis_result);
	}
}
