package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.LayoutManager;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import utils.GitUtils4G1;

public class Settings_GitPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1674315448412734896L;

	private GitUtils4G1 repo_info = null;
	
	private JTextField repoPathURITextField;
	private JTextField repoLocalPathTextField;
	private JTextField systemNameTextField;

	public Settings_GitPanel(GitUtils4G1 repo_info, int pan_width, int pan_height) {
		super();
		this.repo_info = repo_info;
		
		setLayout(null);
		int y = 0;
		int space = 5;
        /******************************/
		JLabel gitImageLabel = new JLabel();
		ImageIcon imageIcon = null;
		try
		{
			imageIcon = new ImageIcon(getClass().getResource("/icons/" + "git_256x256" + ".png"));
			Image image = imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(64, 64,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			imageIcon = new ImageIcon(newimg);
		}
		catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		if (imageIcon != null)
			gitImageLabel.setIcon(imageIcon);
		else
			System.out.println("NO GIT IMAGE FOUND!");
		
		gitImageLabel.setBounds(space, y, 64, 64);
		gitImageLabel.setPreferredSize(new Dimension(64,64));
		add(gitImageLabel);
		y += 64+space;
		/******************************/
		
		JLabel repoPathURILabel = new JLabel("REPO PATH URI:");
		repoPathURILabel.setBounds(space, y, pan_width, 16);
		repoPathURILabel.setPreferredSize(new Dimension(pan_width,16));
		add(repoPathURILabel);
		repoPathURILabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += 16+space;
		
		repoPathURITextField = new JTextField();
		repoPathURITextField.setColumns(16);
		repoPathURITextField.setBounds(space, y, pan_width, 30);
		repoPathURITextField.setPreferredSize(new Dimension(pan_width,30));
		add(repoPathURITextField);
		repoPathURITextField.setAlignmentX( Component.LEFT_ALIGNMENT );
		repoPathURITextField.setText(this.repo_info.getRepo_path_uri());
		repoPathURITextField.setEditable(false);
		repoPathURITextField.setBackground(Color.WHITE);
		y += 30+space;
		/******************************/
		
		JLabel repoLocalPathLabel = new JLabel("REPO LOCAL PATH:");
		repoLocalPathLabel.setBounds(space, y, pan_width, 16);
		repoLocalPathLabel.setPreferredSize(new Dimension(pan_width,16));
		add(repoLocalPathLabel);
		repoLocalPathLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += 16+space;
		
		repoLocalPathTextField = new JTextField();
		repoLocalPathTextField.setColumns(16);
		repoLocalPathTextField.setBounds(space, y, pan_width, 30);
		repoLocalPathTextField.setPreferredSize(new Dimension(pan_width,30));
		add(repoLocalPathTextField);
		repoLocalPathTextField.setAlignmentX( Component.LEFT_ALIGNMENT );
		repoLocalPathTextField.setText(this.repo_info.getRepo_local_path());
		repoLocalPathTextField.setEditable(false);
		repoLocalPathTextField.setBackground(Color.WHITE);
		y += 30+space;
		/******************************/
		
		JLabel systemNameLabel = new JLabel("SYSTEM NAME:");
		systemNameLabel.setBounds(space, y, pan_width, 16);
		systemNameLabel.setPreferredSize(new Dimension(pan_width,16));
		add(systemNameLabel);
		systemNameLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += 16+space;
		
		systemNameTextField = new JTextField();
		systemNameTextField.setColumns(16);
		systemNameTextField.setBounds(space, y, pan_width, 30);
		systemNameTextField.setPreferredSize(new Dimension(pan_width,30));
		add(systemNameTextField);
		systemNameTextField.setAlignmentX( Component.LEFT_ALIGNMENT );
		systemNameTextField.setText(this.repo_info.getSystem_name());
		systemNameTextField.setEditable(false);
		systemNameTextField.setBackground(Color.WHITE);
		y += 30+space;
		/******************************/
		
		setSize(pan_width,pan_height);
		setPreferredSize(new Dimension(pan_width,pan_height));
        setVisible(true);
	}
	
	public GitUtils4G1 getModifiedGitUtils4G1()
	{
		//RESET COLOR
		repoPathURITextField.setBackground(Color.WHITE);
		repoLocalPathTextField.setBackground(Color.WHITE);
		systemNameTextField.setBackground(Color.WHITE);
		
		GitUtils4G1 toReturn = null;
		String repoPathURI = repoPathURITextField.getText();
		String repoLocalPath = repoLocalPathTextField.getText();
		String systemName = systemNameTextField.getText();
		
		//WRONG INPUT -> RED TEXTFIELD
		if((repoPathURI != null)&&(!repoPathURI.isEmpty()))
		{
			if((repoLocalPath != null)&&(!repoLocalPath.isEmpty()))
			{
				if((systemName != null)&&(!systemName.isEmpty()))
				{
					toReturn = new GitUtils4G1(repoPathURI, repoLocalPath, systemName);
				}
				else
				{
					systemNameTextField.setBackground(Color.RED);
				}
			}
			else
			{
				repoLocalPathTextField.setBackground(Color.RED);
			}
		}
		else
		{
			repoPathURITextField.setBackground(Color.RED);
		}
		return toReturn;
	}
	
	public void highlightRepoURITextField(boolean lightOn)
	{
		if(lightOn)
			repoPathURITextField.setBackground(Color.YELLOW);
		else
			repoPathURITextField.setBackground(Color.WHITE);
	}

	public void unlockAllModifiableTextFields()
	{
		repoPathURITextField.setEditable(true);
		repoLocalPathTextField.setEditable(true);
		systemNameTextField.setEditable(false);
		systemNameTextField.setBackground(Color.RED);
	}
	
	public void unlockAllTextFields()
	{
		repoPathURITextField.setEditable(true);
		repoLocalPathTextField.setEditable(true);
		systemNameTextField.setEditable(true);
	}
	
	public void lockAllTextFields()
	{
		repoPathURITextField.setEditable(false);
		repoPathURITextField.setBackground(Color.WHITE);
		repoLocalPathTextField.setEditable(false);
		repoLocalPathTextField.setBackground(Color.WHITE);
		systemNameTextField.setEditable(false);
		systemNameTextField.setBackground(Color.WHITE);
	}
	
	public Settings_GitPanel() {
		// TODO Auto-generated constructor stub
	}

	public Settings_GitPanel(LayoutManager arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public Settings_GitPanel(boolean arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public Settings_GitPanel(LayoutManager arg0, boolean arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

}
