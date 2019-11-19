package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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
import javax.swing.JTextField;

import controller.Download_Sys_Version_Controller;
import utils.GitUtils4G1;

public class Download_Sys_Version_View extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7343684120594513908L;

	private final static int WINDOW_WIDTH = 700;
	private final static int WINDOW_HEIGHT = 500;
	
	private final static int SOUTH_PANEL_HEIGHT = 20;

	private final static int BUTTON_HEIGHT = 40;
	
	private final static int TEXTFIELD_HEIGHT = 30;
	private final static int LABEL_HEIGHT = 16;
	
	private JLabel errorDialogLabel;
	
	private Settings_GitPanel gitPanel;
	
	private JTextField versionTextField;
	
	public Download_Sys_Version_View()
	{
		setTitle("DOWNLOAD SYSTEM VERSION");
		setResizable(false);
		setBounds(100, 100, WINDOW_WIDTH, WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		
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
		
		//CENTER PANEL ->
		
		//CREATE EMPTY FIELDS
		int width = WINDOW_WIDTH-70;
		int height = WINDOW_HEIGHT-SOUTH_PANEL_HEIGHT;
		GitUtils4G1 empty_repo_info = new GitUtils4G1("https://github.com/", "./", "");
		gitPanel = new Settings_GitPanel(empty_repo_info, width, height);
		gitPanel.unlockAllTextFields();
		gitPanel.setBounds(0, 0, width, height);//620 MIN->700
		gitPanel.setLayout(null);
		getContentPane().add(gitPanel, BorderLayout.CENTER);
		
		int center_panel_element_height = 270;

		JLabel versionLabel = new JLabel("VERSION - COMMIT ID");
		versionLabel.setBounds(5, center_panel_element_height, WINDOW_WIDTH/3, LABEL_HEIGHT);
		gitPanel.add(versionLabel);
		center_panel_element_height += LABEL_HEIGHT;
		
		versionTextField = new JTextField();
		versionTextField.setColumns(16);
		versionTextField.setBounds(5, center_panel_element_height, width, TEXTFIELD_HEIGHT);
		versionTextField.setPreferredSize(new Dimension(width,TEXTFIELD_HEIGHT));
		gitPanel.add(versionTextField);
		versionTextField.setAlignmentX( Component.LEFT_ALIGNMENT );
		center_panel_element_height += (TEXTFIELD_HEIGHT+10);
		
		JLabel downloadButtonLabel = new JLabel("DOWNLOAD");
		downloadButtonLabel.setBounds(WINDOW_WIDTH-70-BUTTON_HEIGHT-80, center_panel_element_height+((BUTTON_HEIGHT-LABEL_HEIGHT)/2), 70, LABEL_HEIGHT);
		downloadButtonLabel.setForeground(Color.red);
		gitPanel.add(downloadButtonLabel);
		
		JButton downloadButton = new JButton();
		downloadButton.setBounds(WINDOW_WIDTH-BUTTON_HEIGHT-70, center_panel_element_height, BUTTON_HEIGHT, BUTTON_HEIGHT);
		gitPanel.add(downloadButton);
		downloadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				clickButtonDownloadSysVersion();
			}
			
		});
		
		ImageIcon downloadSystemIcon = null;
		try
		{
			downloadSystemIcon = new ImageIcon(getClass().getResource("/icons/" + "download_system_256x256" + ".png"));
			Image image = downloadSystemIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(TEXTFIELD_HEIGHT, TEXTFIELD_HEIGHT,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			downloadSystemIcon = new ImageIcon(newimg);
		}
		catch (NullPointerException e) {
			e.printStackTrace();
		}

		if(downloadSystemIcon != null)
			downloadButton.setIcon(downloadSystemIcon);
		else
			downloadButton.setText("\\/");
		
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
	
	public void highlightRepoURITextField(boolean lightOn)
	{
		this.gitPanel.highlightRepoURITextField(lightOn);
	}
	
	public void clickButtonDownloadSysVersion() {
		//CLEAR ERROR LABEL
		this.errorDialogLabel.setText("");
		this.errorDialogLabel.setVisible(false);
		
		String version_commit_id = versionTextField.getText();
		GitUtils4G1 repo = gitPanel.getModifiedGitUtils4G1();
		Download_Sys_Version_Controller.getInstance().downloadSystemVersion(this, repo, version_commit_id);
	}
}
