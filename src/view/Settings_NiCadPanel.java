package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import persistence.NiCad_Settings;
import utils.FileUtils4G1;
import utils.HTMLFilter;
import utils.XMLFilter;

public class Settings_NiCadPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -453671185110303431L;

	private NiCad_Settings nicad_settings = null;
	
	private JTextField nicadRootFullpathTextField;
	private JTextField analyzedSystemsRootFolderNameTextField;
	private JTextField systemVersionTextField;
	private JTextField cloneClassesFileTextField;
	private JTextField cloneLinesFileTextField;
	private JTextField clonePairsFileTextField;
	
	private JButton nicadRootFullpathButton;
	private JButton analyzedSystemsRootFolderNameButton;
	private JButton cloneClassesFileButton;
	private JButton cloneLinesFileButton;
	private JButton clonePairsFileButton;
	
	private final static int TEXTFIELD_HEIGHT = 30;

	public Settings_NiCadPanel(NiCad_Settings nicad_settings, int pan_width, int pan_height) {
		super();
		this.nicad_settings = nicad_settings;
		
		setLayout(null);
		int y = 0;
		int space = 5;
        /******************************/
		JLabel nicadImageLabel = new JLabel();
		ImageIcon imageIcon = null;
		try
		{
			imageIcon = new ImageIcon(getClass().getResource("/icons/" + "nicad_settings_256x256" + ".png"));
			Image image = imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(64, 64,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			imageIcon = new ImageIcon(newimg);
		}
		catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		if (imageIcon != null)
			nicadImageLabel.setIcon(imageIcon);
		else
			System.out.println("NO NICAD IMAGE FOUND!");
		
		nicadImageLabel.setBounds(space, y, 64, 64);
		nicadImageLabel.setPreferredSize(new Dimension(64,64));
		add(nicadImageLabel);
		y += 64+space;
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
		
		JLabel nicadRootFullpathLabel = new JLabel("NICAD ROOT FULLPATH:");
		nicadRootFullpathLabel.setBounds(space, y, pan_width, 16);
		nicadRootFullpathLabel.setPreferredSize(new Dimension(pan_width,16));
		add(nicadRootFullpathLabel);
		nicadRootFullpathLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += 16+space;
		
		nicadRootFullpathTextField = new JTextField();
		nicadRootFullpathTextField.setColumns(16);
		nicadRootFullpathTextField.setBounds(space, y, pan_width-TEXTFIELD_HEIGHT, TEXTFIELD_HEIGHT);
		nicadRootFullpathTextField.setPreferredSize(new Dimension(pan_width-TEXTFIELD_HEIGHT,TEXTFIELD_HEIGHT));
		add(nicadRootFullpathTextField);
		nicadRootFullpathTextField.setAlignmentX( Component.LEFT_ALIGNMENT );
		nicadRootFullpathTextField.setText(this.nicad_settings.getRoot_fullpath());
		nicadRootFullpathTextField.setEditable(false);
		nicadRootFullpathTextField.setBackground(Color.WHITE);
		
		nicadRootFullpathButton = new JButton();
		nicadRootFullpathButton.setBounds(space+pan_width-TEXTFIELD_HEIGHT, y, TEXTFIELD_HEIGHT, TEXTFIELD_HEIGHT);
		add(nicadRootFullpathButton);
		if(openFolderIcon != null)
			nicadRootFullpathButton.setIcon(openFolderIcon);
		else
			nicadRootFullpathButton.setText("+");
		nicadRootFullpathButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setNicadRootFullpathFolder();
			}
			
		});
		
		y += TEXTFIELD_HEIGHT+space;
		/******************************/
		
		JLabel analyzedSystemsRootFolderNameLabel = new JLabel("ANALYZED SYSTEMS ROOT FOLDER NAME:");
		analyzedSystemsRootFolderNameLabel.setBounds(space, y, pan_width, 16);
		analyzedSystemsRootFolderNameLabel.setPreferredSize(new Dimension(pan_width,16));
		add(analyzedSystemsRootFolderNameLabel);
		analyzedSystemsRootFolderNameLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += 16+space;
		
		analyzedSystemsRootFolderNameTextField = new JTextField();
		analyzedSystemsRootFolderNameTextField.setColumns(16);
		analyzedSystemsRootFolderNameTextField.setBounds(space, y, pan_width-TEXTFIELD_HEIGHT, TEXTFIELD_HEIGHT);
		analyzedSystemsRootFolderNameTextField.setPreferredSize(new Dimension(pan_width-TEXTFIELD_HEIGHT,TEXTFIELD_HEIGHT));
		add(analyzedSystemsRootFolderNameTextField);
		analyzedSystemsRootFolderNameTextField.setAlignmentX( Component.LEFT_ALIGNMENT );
		analyzedSystemsRootFolderNameTextField.setText(this.nicad_settings.getAnalyzedsystems_root_folder());
		analyzedSystemsRootFolderNameTextField.setEditable(false);
		analyzedSystemsRootFolderNameTextField.setBackground(Color.WHITE);
		
		analyzedSystemsRootFolderNameButton = new JButton();
		analyzedSystemsRootFolderNameButton.setBounds(space+pan_width-TEXTFIELD_HEIGHT, y, TEXTFIELD_HEIGHT, TEXTFIELD_HEIGHT);
		add(analyzedSystemsRootFolderNameButton);
		if(openFolderIcon != null)
			analyzedSystemsRootFolderNameButton.setIcon(openFolderIcon);
		else
			analyzedSystemsRootFolderNameButton.setText("+");
		analyzedSystemsRootFolderNameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setAnalyzedSystemsRootFolderName();
			}
			
		});
		y += TEXTFIELD_HEIGHT+space;
		/******************************/
		
		JLabel systemVersionLabel = new JLabel("SYSTEM VERSION - COMMIT:");
		systemVersionLabel.setBounds(space, y, pan_width, 16);
		systemVersionLabel.setPreferredSize(new Dimension(pan_width,16));
		add(systemVersionLabel);
		systemVersionLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += 16+space;
		
		systemVersionTextField = new JTextField();
		systemVersionTextField.setColumns(16);
		systemVersionTextField.setBounds(space, y, pan_width-TEXTFIELD_HEIGHT, TEXTFIELD_HEIGHT);
		systemVersionTextField.setPreferredSize(new Dimension(pan_width-TEXTFIELD_HEIGHT,TEXTFIELD_HEIGHT));
		add(systemVersionTextField);
		systemVersionTextField.setAlignmentX( Component.LEFT_ALIGNMENT );
		systemVersionTextField.setText(this.nicad_settings.getVersion_commit());
		systemVersionTextField.setEditable(false);
		systemVersionTextField.setBackground(Color.WHITE);
		y += TEXTFIELD_HEIGHT+space;
		/******************************/
		
		JLabel cloneClassesFileLabel = new JLabel("CLONE CLASSES FILE:");
		cloneClassesFileLabel.setBounds(space, y, pan_width, 16);
		cloneClassesFileLabel.setPreferredSize(new Dimension(pan_width,16));
		add(cloneClassesFileLabel);
		cloneClassesFileLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += 16+space;
		
		cloneClassesFileTextField = new JTextField();
		cloneClassesFileTextField.setColumns(16);
		cloneClassesFileTextField.setBounds(space, y, pan_width-TEXTFIELD_HEIGHT, TEXTFIELD_HEIGHT);
		cloneClassesFileTextField.setPreferredSize(new Dimension(pan_width-TEXTFIELD_HEIGHT,TEXTFIELD_HEIGHT));
		add(cloneClassesFileTextField);
		cloneClassesFileTextField.setAlignmentX( Component.LEFT_ALIGNMENT );
		cloneClassesFileTextField.setText(this.nicad_settings.getClone_classes_file());
		cloneClassesFileTextField.setEditable(false);
		cloneClassesFileTextField.setBackground(Color.WHITE);

		cloneClassesFileButton = new JButton();
		cloneClassesFileButton.setBounds(space+pan_width-TEXTFIELD_HEIGHT, y, TEXTFIELD_HEIGHT, TEXTFIELD_HEIGHT);
		add(cloneClassesFileButton);
		if(openFolderIcon != null)
			cloneClassesFileButton.setIcon(openFolderIcon);
		else
			cloneClassesFileButton.setText("+");
		cloneClassesFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setCloneClassesFile();
			}
			
		});
		
		y += TEXTFIELD_HEIGHT+space;
		/******************************/
		
		JLabel cloneLinesFileLabel = new JLabel("CLONE LINES FILE:");
		cloneLinesFileLabel.setBounds(space, y, pan_width, 16);
		cloneLinesFileLabel.setPreferredSize(new Dimension(pan_width,16));
		add(cloneLinesFileLabel);
		cloneLinesFileLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += 16+space;
		
		cloneLinesFileTextField = new JTextField();
		cloneLinesFileTextField.setColumns(16);
		cloneLinesFileTextField.setBounds(space, y, pan_width-TEXTFIELD_HEIGHT, TEXTFIELD_HEIGHT);
		cloneLinesFileTextField.setPreferredSize(new Dimension(pan_width-TEXTFIELD_HEIGHT,TEXTFIELD_HEIGHT));
		add(cloneLinesFileTextField);
		cloneLinesFileTextField.setAlignmentX( Component.LEFT_ALIGNMENT );
		cloneLinesFileTextField.setText(this.nicad_settings.getClone_lines_file());
		cloneLinesFileTextField.setEditable(false);
		cloneLinesFileTextField.setBackground(Color.WHITE);

		cloneLinesFileButton = new JButton();
		cloneLinesFileButton.setBounds(space+pan_width-TEXTFIELD_HEIGHT, y, TEXTFIELD_HEIGHT, TEXTFIELD_HEIGHT);
		add(cloneLinesFileButton);
		if(openFolderIcon != null)
			cloneLinesFileButton.setIcon(openFolderIcon);
		else
			cloneLinesFileButton.setText("+");
		cloneLinesFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setCloneLinesFile();
			}
			
		});
		y += TEXTFIELD_HEIGHT+space;
		/******************************/
		
		JLabel clonePairsFileLabel = new JLabel("CLONE PAIRS FILE:");
		clonePairsFileLabel.setBounds(space, y, pan_width, 16);
		clonePairsFileLabel.setPreferredSize(new Dimension(pan_width,16));
		add(clonePairsFileLabel);
		clonePairsFileLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += 16+space;
		
		clonePairsFileTextField = new JTextField();
		clonePairsFileTextField.setColumns(16);
		clonePairsFileTextField.setBounds(space, y, pan_width-TEXTFIELD_HEIGHT, TEXTFIELD_HEIGHT);
		clonePairsFileTextField.setPreferredSize(new Dimension(pan_width-TEXTFIELD_HEIGHT,TEXTFIELD_HEIGHT));
		add(clonePairsFileTextField);
		clonePairsFileTextField.setAlignmentX( Component.LEFT_ALIGNMENT );
		clonePairsFileTextField.setText(this.nicad_settings.getClone_pairs_file());
		clonePairsFileTextField.setEditable(false);
		clonePairsFileTextField.setBackground(Color.WHITE);

		clonePairsFileButton = new JButton();
		clonePairsFileButton.setBounds(space+pan_width-TEXTFIELD_HEIGHT, y, TEXTFIELD_HEIGHT, TEXTFIELD_HEIGHT);
		add(clonePairsFileButton);
		if(openFolderIcon != null)
			clonePairsFileButton.setIcon(openFolderIcon);
		else
			clonePairsFileButton.setText("+");
		clonePairsFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setClonePairsFile();
			}
			
		});
		
		y += TEXTFIELD_HEIGHT+space;
		/******************************/
		
		setSize(pan_width,pan_height);
		setPreferredSize(new Dimension(pan_width,pan_height));
        setVisible(true);
	}

	public NiCad_Settings getModifiedNiCad_Settings()
	{
		//RESET COLOR
		nicadRootFullpathTextField.setBackground(Color.WHITE);
		analyzedSystemsRootFolderNameTextField.setBackground(Color.WHITE);
		systemVersionTextField.setBackground(Color.WHITE);
		cloneClassesFileTextField.setBackground(Color.WHITE);
		cloneLinesFileTextField.setBackground(Color.WHITE);
		clonePairsFileTextField.setBackground(Color.WHITE);
		
		NiCad_Settings toReturn = null;
		String nicadRootFullpath = nicadRootFullpathTextField.getText();
		String analyzedSystemsRootFolderName = analyzedSystemsRootFolderNameTextField.getText();
		String systemVersion = systemVersionTextField.getText();
		String cloneClassesFile = cloneClassesFileTextField.getText();
		String cloneLinesFile = cloneLinesFileTextField.getText();
		String clonePairsFile = clonePairsFileTextField.getText();
		
		//WRONG INPUT -> RED TEXTFIELD
		if((nicadRootFullpath != null)&&(!nicadRootFullpath.isEmpty()))
		{
			if((analyzedSystemsRootFolderName != null)&&(!analyzedSystemsRootFolderName.isEmpty()))
			{
				if((systemVersion != null)&&(!systemVersion.isEmpty()))
				{
					if((cloneClassesFile != null)&&(!cloneClassesFile.isEmpty()))
					{
						if((cloneLinesFile != null)&&(!cloneLinesFile.isEmpty()))
						{
							if((clonePairsFile != null)&&(!clonePairsFile.isEmpty()))
							{
								toReturn = new NiCad_Settings(nicadRootFullpath, analyzedSystemsRootFolderName, systemVersion, cloneClassesFile, cloneLinesFile, clonePairsFile);
							}
							else
							{
								clonePairsFileTextField.setBackground(Color.RED);
							}
						}
						else
						{
							cloneLinesFileTextField.setBackground(Color.RED);
						}
					}
					else
					{
						cloneClassesFileTextField.setBackground(Color.RED);
					}
				}
				else
				{
					systemVersionTextField.setBackground(Color.RED);
				}
			}
			else
			{
				analyzedSystemsRootFolderNameTextField.setBackground(Color.RED);
			}
		}
		else
		{
			nicadRootFullpathTextField.setBackground(Color.RED);
		}
		return toReturn;
	}
	
	public void unlockAllModifiableTextFields()
	{
		nicadRootFullpathTextField.setEditable(true);
		analyzedSystemsRootFolderNameTextField.setEditable(true);
		systemVersionTextField.setEditable(false);
		systemVersionTextField.setBackground(Color.RED);
		cloneClassesFileTextField.setEditable(true);
		cloneLinesFileTextField.setEditable(true);
		clonePairsFileTextField.setEditable(true);
		
		nicadRootFullpathButton.setVisible(true);
		analyzedSystemsRootFolderNameButton.setVisible(true);
		cloneClassesFileButton.setVisible(true);
		cloneLinesFileButton.setVisible(true);
		clonePairsFileButton.setVisible(true);
	}
	
	public void unlockAllTextFields()
	{
		nicadRootFullpathTextField.setEditable(true);
		analyzedSystemsRootFolderNameTextField.setEditable(true);
		systemVersionTextField.setEditable(true);
		cloneClassesFileTextField.setEditable(true);
		cloneLinesFileTextField.setEditable(true);
		clonePairsFileTextField.setEditable(true);
		
		nicadRootFullpathButton.setVisible(true);
		analyzedSystemsRootFolderNameButton.setVisible(true);
		cloneClassesFileButton.setVisible(true);
		cloneLinesFileButton.setVisible(true);
		clonePairsFileButton.setVisible(true);
	}
	
	public void lockAllTextFields()
	{
		nicadRootFullpathTextField.setEditable(false);
		nicadRootFullpathTextField.setBackground(Color.WHITE);
		analyzedSystemsRootFolderNameTextField.setEditable(false);
		analyzedSystemsRootFolderNameTextField.setBackground(Color.WHITE);
		systemVersionTextField.setEditable(false);
		systemVersionTextField.setBackground(Color.WHITE);
		cloneClassesFileTextField.setEditable(false);
		cloneClassesFileTextField.setBackground(Color.WHITE);
		cloneLinesFileTextField.setEditable(false);
		cloneLinesFileTextField.setBackground(Color.WHITE);
		clonePairsFileTextField.setEditable(false);
		clonePairsFileTextField.setBackground(Color.WHITE);
		
		nicadRootFullpathButton.setVisible(false);
		analyzedSystemsRootFolderNameButton.setVisible(false);
		cloneClassesFileButton.setVisible(false);
		cloneLinesFileButton.setVisible(false);
		clonePairsFileButton.setVisible(false);
	}

	public void setNicadRootFullpathFolder()
	{
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fc.showDialog(this, "SELECT");
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			this.nicadRootFullpathTextField.setBackground(Color.WHITE);
			this.nicadRootFullpathTextField.setText(fc.getSelectedFile().getAbsolutePath()+File.separator);
		}
	}
	
	public void setAnalyzedSystemsRootFolderName()
	{
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fc.showDialog(this, "SELECT");
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			this.analyzedSystemsRootFolderNameTextField.setBackground(Color.WHITE);
			this.analyzedSystemsRootFolderNameTextField.setText(fc.getSelectedFile().getName());
		}
	}
	
	public void setCloneClassesFile()
	{
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.setFileFilter(new XMLFilter());
		int returnVal = fc.showDialog(this, "SELECT");
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			if(FileUtils4G1.xml.equalsIgnoreCase(FileUtils4G1.getExtension(fc.getSelectedFile())))
			{
				this.cloneClassesFile = fc.getSelectedFile();
				this.cloneClassesFileTextField.setBackground(Color.WHITE);
				this.cloneClassesFileTextField.setText(this.cloneClassesFile.getName());
			}
			else
			{
				String message = "The selected file MUST BE .xml";
				String title = "NOTHING SELECTED";
				JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	public void setCloneClassesFile(File cloneClassesFile)
	{
		this.cloneClassesFile = cloneClassesFile;
	}

	public File getCloneClassesFile()
	{
		return cloneClassesFile;
	}
	
	public void setCloneLinesFile()
	{
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.setFileFilter(new HTMLFilter());
		int returnVal = fc.showDialog(this, "SELECT");
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			if(FileUtils4G1.html.equalsIgnoreCase(FileUtils4G1.getExtension(fc.getSelectedFile())))
			{
				this.cloneLinesFile = fc.getSelectedFile();
				this.cloneLinesFileTextField.setBackground(Color.WHITE);
				this.cloneLinesFileTextField.setText(this.cloneLinesFile.getName());
			}
			else
			{
				String message = "The selected file MUST BE .html";
				String title = "NOTHING SELECTED";
				JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	public void setCloneLinesFile(File cloneLinesFile)
	{
		this.cloneLinesFile = cloneLinesFile;
	}
	
	public File getCloneLinesFile()
	{
		return cloneLinesFile;
	}
	
	public void setClonePairsFile()
	{
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.setFileFilter(new XMLFilter());
		int returnVal = fc.showDialog(this, "SELECT");
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			if(FileUtils4G1.xml.equalsIgnoreCase(FileUtils4G1.getExtension(fc.getSelectedFile())))
			{
				this.clonePairsFile = fc.getSelectedFile();
				this.clonePairsFileTextField.setBackground(Color.WHITE);
				this.clonePairsFileTextField.setText(this.clonePairsFile.getName());
			}
			else
			{
				String message = "The selected file MUST BE .xml";
				String title = "NOTHING SELECTED";
				JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	public void setClonePairsFile(File clonePairsFile)
	{
		this.clonePairsFile = clonePairsFile;
	}
	
	public File getClonePairsFile()
	{
		return clonePairsFile;
	}
	
	public Settings_NiCadPanel() {
		// TODO Auto-generated constructor stub
	}

	public Settings_NiCadPanel(LayoutManager arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public Settings_NiCadPanel(boolean arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public Settings_NiCadPanel(LayoutManager arg0, boolean arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
	
	private File cloneClassesFile = null;
	private File cloneLinesFile = null;
	private File clonePairsFile = null;

}
