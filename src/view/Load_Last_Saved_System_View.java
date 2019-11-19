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
import javax.swing.JTextField;

import controller.Load_Saved_System_Controller;
import persistence.System_Version;

public class Load_Last_Saved_System_View extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -322498203794969492L;

	private System_Version last_save = null;
	
	private final static int WINDOW_WIDTH = 360;
	private final static int WINDOW_HEIGHT = 640;
	
	private final static int NORTH_PANEL_HEIGHT = 0;
	private final static int SOUTH_PANEL_HEIGHT = 20;
	
	private final static int TEXTFIELD_HEIGHT = 30;
	
	private final static int BUTTON_WIDTH = 40;
	private final static int BUTTON_HEIGHT = 40;
	
	private JLabel errorDialogLabel;
	
	private JTextField systemNameTextField;
	private JTextField systemVersionTextField;
	
	private JButton loadSavedSystemButton;
	private JButton goToSelectionButton;
	private JButton goToCreateButton;
	private JButton goToDownloadSysVersionButton;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try 
				{
					Load_Last_Saved_System_View frame = new Load_Last_Saved_System_View();
					frame.setVisible(true);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
	
	public Load_Last_Saved_System_View()
	{
		setTitle("LOAD LAST SAVE");
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
		
		//CENTER PANEL -> LAST SAVE
		String background_image_name = "floppy_disk_load_360x640";
		//String background_image_name = "floppy_disk_save_256x256";
		///////////////////////////////////////////////
		Image backgroundImage = null;
		try {
			ImageIcon backgroundImageIcon = new ImageIcon(getClass().getResource("/splash_images/" + background_image_name + ".png"));
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
		//JPanel panelCenter = new JPanel();
		//panelCenter = new ImagePanel(backgroundImage);
		panelCenter.setBounds(0, 0, WINDOW_WIDTH-20, WINDOW_HEIGHT-SOUTH_PANEL_HEIGHT-NORTH_PANEL_HEIGHT);//620 MIN->700
		panelCenter.setLayout(null);
		getContentPane().add(panelCenter, BorderLayout.CENTER);
				
		//CURRENT OBJECT HEIGHT
		int center_panel_element_height = NORTH_PANEL_HEIGHT+25;//25
		
		JLabel savedSystemsLabel = new JLabel("|LAST SAVED SYSTEM|");
		savedSystemsLabel.setBounds(10, center_panel_element_height, WINDOW_WIDTH/2, BUTTON_HEIGHT);
		String saved_icon_name = "floppy_disk_256x256";
		ImageIcon saved_imageIcon = null;
		try {
			saved_imageIcon = new ImageIcon(getClass().getResource("/icons/" + saved_icon_name + ".png"));
			Image image = saved_imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(32, 32,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			saved_imageIcon = new ImageIcon(newimg);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		if (saved_imageIcon != null)
			savedSystemsLabel.setIcon(saved_imageIcon);
		panelCenter.add(savedSystemsLabel);
		center_panel_element_height += BUTTON_HEIGHT;//65
		
		center_panel_element_height += TEXTFIELD_HEIGHT;//95
		
		JLabel savedSystemNameLabel = new JLabel("SYSTEM NAME:");
		savedSystemNameLabel.setBounds(10, center_panel_element_height, WINDOW_WIDTH-20, TEXTFIELD_HEIGHT);
		panelCenter.add(savedSystemNameLabel);
		center_panel_element_height += TEXTFIELD_HEIGHT;//125
		
		String system_name = null;
		String system_version = null;
		
		this.last_save = Load_Saved_System_Controller.getInstance().getLastSavedSystem(this);
		if(last_save != null)
		{
			system_name = last_save.getSystem_name();
			system_version = last_save.getSystem_version();
		}
		
		systemNameTextField = new JTextField(system_name);
		systemNameTextField.setBounds(10, center_panel_element_height, WINDOW_WIDTH-20, TEXTFIELD_HEIGHT);
		systemNameTextField.setEditable(false);
		systemNameTextField.setBackground(Color.WHITE);
		panelCenter.add(systemNameTextField);
		center_panel_element_height += TEXTFIELD_HEIGHT;//155
		
		JLabel savedSystemVersionLabel = new JLabel("SYSTEM VERSION (LAST COMMIT ID):");
		savedSystemVersionLabel.setBounds(10, center_panel_element_height, WINDOW_WIDTH-20, TEXTFIELD_HEIGHT);
		panelCenter.add(savedSystemVersionLabel);
		center_panel_element_height += TEXTFIELD_HEIGHT;//185

		systemVersionTextField = new JTextField(system_version);
		systemVersionTextField.setBounds(10, center_panel_element_height, WINDOW_WIDTH-20, TEXTFIELD_HEIGHT);
		systemVersionTextField.setEditable(false);
		systemVersionTextField.setBackground(Color.WHITE);
		panelCenter.add(systemVersionTextField);
		center_panel_element_height += TEXTFIELD_HEIGHT;//215
		
		JLabel loadButtonLabel = new JLabel("LOAD");
		loadButtonLabel.setBounds(10+WINDOW_WIDTH-20-BUTTON_WIDTH-50, center_panel_element_height+(BUTTON_HEIGHT-TEXTFIELD_HEIGHT)/2, 50, TEXTFIELD_HEIGHT);
		panelCenter.add(loadButtonLabel);
		
		loadSavedSystemButton = new JButton();
		loadSavedSystemButton.setBounds(10+WINDOW_WIDTH-20-BUTTON_WIDTH, center_panel_element_height, BUTTON_WIDTH, BUTTON_HEIGHT);
		String load_button_icon_name = "load_button_icon_256x256";
		ImageIcon load_button_imageIcon = null;
		try {
			load_button_imageIcon = new ImageIcon(getClass().getResource("/icons/" + load_button_icon_name + ".png"));
			Image image = load_button_imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(32, 32,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			load_button_imageIcon = new ImageIcon(newimg);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		if (load_button_imageIcon != null)
			loadSavedSystemButton.setIcon(load_button_imageIcon);
		panelCenter.add(loadSavedSystemButton);
		center_panel_element_height += BUTTON_HEIGHT;//255
		
		loadSavedSystemButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				clickButtonLoad();
			}
			
		});
		
		center_panel_element_height += BUTTON_HEIGHT;//295
		
		goToSelectionButton = new JButton("SELECT OTHER SYSTEMS");
		goToSelectionButton.setBounds(10, center_panel_element_height, WINDOW_WIDTH-20, BUTTON_HEIGHT);
		panelCenter.add(goToSelectionButton);
		center_panel_element_height += BUTTON_HEIGHT;//335
		
		goToSelectionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				clickButtonGoToSelection();
			}
			
		});
		
		center_panel_element_height += BUTTON_HEIGHT;//375
		
		goToCreateButton = new JButton("CREATE NEW SETTINGS");
		goToCreateButton.setBounds(10, center_panel_element_height, WINDOW_WIDTH-20, BUTTON_HEIGHT);
		panelCenter.add(goToCreateButton);
		center_panel_element_height += BUTTON_HEIGHT;//415
		
		goToCreateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				clickButtonGoToCreate();
			}
			
		});
		
		center_panel_element_height += (2*BUTTON_HEIGHT);//495
		
		goToDownloadSysVersionButton = new JButton("DOWNLOAD SYSTEM VERSION");
		goToDownloadSysVersionButton.setBounds(10, center_panel_element_height, WINDOW_WIDTH-20, BUTTON_HEIGHT);
		panelCenter.add(goToDownloadSysVersionButton);
		center_panel_element_height += BUTTON_HEIGHT;//415
		
		goToDownloadSysVersionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				clickButtonGoToDownloadSysVersion();
			}
			
		});

		System.out.println("CENTERPANE:"+panelCenter.getWidth()+"x"+panelCenter.getHeight());
		System.out.println("WIN:"+getWidth()+"x"+getHeight());
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
	
	public void clickButtonLoad()
	{
		this.errorDialogLabel.setText("");
		this.errorDialogLabel.setVisible(false);
		Load_Saved_System_Controller.getInstance().loadSavedSystem(this, this.last_save);
	}
	
	public void clickButtonGoToSelection()
	{
		this.errorDialogLabel.setText("");
		this.errorDialogLabel.setVisible(false);
		Load_Saved_System_Controller.getInstance().goToSystemSelection(this);
	}
	
	public void clickButtonGoToCreate()
	{
		this.errorDialogLabel.setText("");
		this.errorDialogLabel.setVisible(false);
		Load_Saved_System_Controller.getInstance().goToCreateSettings(this);
	}
	
	public void clickButtonGoToDownloadSysVersion()
	{
		this.errorDialogLabel.setText("");
		this.errorDialogLabel.setVisible(false);
		Load_Saved_System_Controller.getInstance().goToDownloadSysVersion(this);
	}
}
