package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.Settings_Create_Controller;
import persistence.Settings;
import utils.FileUtils4G1;
import utils.TXTFilter;

public class Settings_Create_View extends JFrame implements Settings_View{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2151172379425492862L;

	private final static int WINDOW_WIDTH = 700;
	private final static int WINDOW_HEIGHT = 600;
	
	private final static int NORTH_PANEL_HEIGHT = 0;
	private final static int SOUTH_PANEL_HEIGHT = 20;

	private final static int BUTTON_WIDTH = 100;
	private final static int BUTTON_HEIGHT = 40;
	
	private final static int TEXTFIELD_HEIGHT = 30;
	private final static int LABEL_HEIGHT = 26;
	
	private JLabel errorDialogLabel;
	private JButton testSettingsButton;
	private JButton saveSettingsButton;
	
	private JTextField commitlistFileTextField;
	private JButton commitlistFileButton;
	
	private SettingsCreatePanel panelCenter;
	
	public Settings_Create_View()
	{
		setTitle("SETTINGS - CREATE");
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
		//670x500
		int width = 670;
		int height = 500;
		panelCenter = new SettingsCreatePanel(width, height);
		panelCenter.setBounds(0, 0, WINDOW_WIDTH-20, WINDOW_HEIGHT-SOUTH_PANEL_HEIGHT-NORTH_PANEL_HEIGHT);//620 MIN->700
		panelCenter.setLayout(null);
		getContentPane().add(panelCenter, BorderLayout.CENTER);

		int center_panel_element_height = height;

		JLabel commitlistLabel = new JLabel(Settings.COMMIT_LIST_FILE);
		commitlistLabel.setBounds(10, center_panel_element_height+(BUTTON_HEIGHT-TEXTFIELD_HEIGHT)/2-LABEL_HEIGHT, WINDOW_WIDTH/3, LABEL_HEIGHT);
		panelCenter.add(commitlistLabel);
		
		JLabel testSettingsButtonLabel = new JLabel("TEST");
		testSettingsButtonLabel.setBounds(WINDOW_WIDTH-52-2*BUTTON_WIDTH-40, center_panel_element_height-2-BUTTON_HEIGHT+7, 40, LABEL_HEIGHT);
		testSettingsButtonLabel.setForeground(Color.red);
		panelCenter.add(testSettingsButtonLabel);

		testSettingsButton = new JButton();
		testSettingsButton.setBounds(WINDOW_WIDTH-52-2*BUTTON_WIDTH, center_panel_element_height-2-BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
		String test_button_icon_name = "test_settings_icon_256x256";
		ImageIcon test_button_imageIcon = null;
		try {
			test_button_imageIcon = new ImageIcon(getClass().getResource("/icons/" + test_button_icon_name + ".png"));
			Image image = test_button_imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(32, 32,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			test_button_imageIcon = new ImageIcon(newimg);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		if (test_button_imageIcon != null)
			testSettingsButton.setIcon(test_button_imageIcon);
		panelCenter.add(testSettingsButton);

		///////////////////////////////////////////////////
		testSettingsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				clickButtonTest();
			}
		});
		///////////////////////////////////////////////////

		commitlistFileTextField = new JTextField();
		commitlistFileTextField.setColumns(16);
		commitlistFileTextField.setBounds(10, center_panel_element_height+(BUTTON_HEIGHT-TEXTFIELD_HEIGHT)/2, WINDOW_WIDTH/2, TEXTFIELD_HEIGHT);
		commitlistFileTextField.setPreferredSize(new Dimension(WINDOW_WIDTH/2,TEXTFIELD_HEIGHT));
		panelCenter.add(commitlistFileTextField);
		commitlistFileTextField.setAlignmentX( Component.LEFT_ALIGNMENT );
		commitlistFileTextField.setText("SELECT COMMIT LIST FILE -TXT-");
		/******************************/
		ImageIcon openFolderIcon = null;
		try
		{
			openFolderIcon = new ImageIcon(getClass().getResource("/icons/" + "download_folder_256x256" + ".png"));
			Image image = openFolderIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(TEXTFIELD_HEIGHT, TEXTFIELD_HEIGHT,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			openFolderIcon = new ImageIcon(newimg);
		}
		catch (NullPointerException e) {
			e.printStackTrace();
		}
		/******************************/
		commitlistFileButton = new JButton();
		commitlistFileButton.setBounds(10+WINDOW_WIDTH/2, center_panel_element_height+(BUTTON_HEIGHT-TEXTFIELD_HEIGHT)/2, TEXTFIELD_HEIGHT, TEXTFIELD_HEIGHT);
		panelCenter.add(commitlistFileButton);
		if(openFolderIcon != null)
			commitlistFileButton.setIcon(openFolderIcon);
		else
			commitlistFileButton.setText("+");
		commitlistFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setCommitlistFile();
			}
			
		});
		/*********************************/
		
		saveSettingsButton = new JButton();
		saveSettingsButton.setBounds(WINDOW_WIDTH-50-BUTTON_WIDTH, center_panel_element_height, BUTTON_WIDTH, BUTTON_HEIGHT);
		String save_button_icon_name = "floppy_disk_save_256x256";
		ImageIcon save_button_imageIcon = null;
		try {
			save_button_imageIcon = new ImageIcon(getClass().getResource("/icons/" + save_button_icon_name + ".png"));
			Image image = save_button_imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(32, 32,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			save_button_imageIcon = new ImageIcon(newimg);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		if (save_button_imageIcon != null)
			saveSettingsButton.setIcon(save_button_imageIcon);
		panelCenter.add(saveSettingsButton);
		///////////////////////////////////////////////////
		saveSettingsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				clickButtonSave();
			}
		});
		///////////////////////////////////////////////////
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
	
	public void clickButtonSave()
	{
		//CLEAR ERROR LABEL
		this.errorDialogLabel.setText("");
		this.errorDialogLabel.setVisible(false);
		//1-TEST
		//2-SAVE
		Settings settings_to_test = panelCenter.getCreateSettings();
		String message = "";
		String title ="";
		int messageType = JOptionPane.INFORMATION_MESSAGE;
		ImageIcon message_imageIcon = null;
		String message_icon_name = null;
		boolean show_created_settings = false;
		if(show_created_settings = Settings_Create_Controller.getInstance().saveSettings(this, settings_to_test, panelCenter.getCloneClassesFile(), panelCenter.getCloneLinesFile(), panelCenter.getClonePairsFile(),this.commitlistFileTextField.getText(),this.commitlistFile))
		{
			message_icon_name = "padlock_256x256";
			message = "SETTINGS CORRECTLY SAVED!";
			title ="SETTINGS SAVED!";
		}
		else
		{
			message_icon_name = "stop_x_256x256";
			message = "UNABLE TO SAVE SETTINGS!";
			title ="SETTINGS NOT SAVED!";
		}
		
		try {
			message_imageIcon = new ImageIcon(getClass().getResource("/icons/" + message_icon_name + ".png"));
			Image image = message_imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(32, 32,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			message_imageIcon = new ImageIcon(newimg);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		if(message_imageIcon != null)
			JOptionPane.showMessageDialog(null, message, title, messageType, message_imageIcon);
		else
			JOptionPane.showMessageDialog(null, message, title, messageType);
		
		if(show_created_settings)
			Settings_Create_Controller.getInstance().showCreatedSettings(this, panelCenter.getCreateSettings());
	}
	
	public void clickButtonTest()
	{
		//CLEAR ERROR LABEL
		this.errorDialogLabel.setText("");
		this.errorDialogLabel.setVisible(false);
		//1-TEST
		Settings settings_to_test = panelCenter.getCreateSettings();
		String message = "";
		String title ="";
		int messageType = JOptionPane.INFORMATION_MESSAGE;
		ImageIcon message_imageIcon = null;
		String message_icon_name = null;
		if(Settings_Create_Controller.getInstance().testSettings(this, settings_to_test, panelCenter.getCloneClassesFile(), panelCenter.getCloneLinesFile(), panelCenter.getClonePairsFile(),this.commitlistFileTextField.getText(),this.commitlistFile))
		{
			message_icon_name = "test_ok_256x256";
			message = "TEST RESULT -> PASSED!";
			title ="TEST PASSED!";
		}
		else
		{
			message_icon_name = "prohibited_256x256";
			message = "TEST RESULT -> NOT PASSED!";
			title ="TEST NOT PASSED!";
		}
		
		try {
			message_imageIcon = new ImageIcon(getClass().getResource("/icons/" + message_icon_name + ".png"));
			Image image = message_imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(32, 32,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			message_imageIcon = new ImageIcon(newimg);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		if(message_imageIcon != null)
			JOptionPane.showMessageDialog(null, message, title, messageType, message_imageIcon);
		else
			JOptionPane.showMessageDialog(null, message, title, messageType);
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
	
	public void highlightRepoURITextField(boolean lightOn)
	{
		this.panelCenter.highlightRepoURITextField(lightOn);
	}
	
	public File getCloneClassesFile()
	{
		return this.panelCenter.getCloneClassesFile();
	}
	
	public void setCloneClassesFile(File cloneClassesFile)
	{
		this.panelCenter.setCloneClassesFile(cloneClassesFile);
	}
	
	public File getCloneLinesFile()
	{
		return this.panelCenter.getCloneLinesFile();
	}
	
	public void setCloneLinesFile(File cloneLinesFile)
	{
		this.panelCenter.setCloneLinesFile(cloneLinesFile);
	}
	
	public File getClonePairsFile()
	{
		return this.panelCenter.getClonePairsFile();
	}
	
	public void setClonePairsFile(File clonePairsFile)
	{
		this.panelCenter.setClonePairsFile(clonePairsFile);
	}
	
	public void setCommitlistFile()
	{
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.setFileFilter(new TXTFilter());
		int returnVal = fc.showDialog(this, "SELECT");
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			if(FileUtils4G1.txt.equalsIgnoreCase(FileUtils4G1.getExtension(fc.getSelectedFile())))
			{
				this.commitlistFile = fc.getSelectedFile();
				this.commitlistFileTextField.setBackground(Color.WHITE);
				this.commitlistFileTextField.setText(this.commitlistFile.getName());
			}
			else
			{
				String message = "The selected file MUST BE .txt";
				String title = "NOTHING SELECTED";
				JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	public void setCommitlistFile(File commitlistFile)
	{
		this.commitlistFile = commitlistFile;
	}
	
	public File getCommitlistFile()
	{
		return commitlistFile;
	}
	
	private File commitlistFile = null;
}
