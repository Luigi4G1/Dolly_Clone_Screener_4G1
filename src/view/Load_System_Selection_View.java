package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import controller.Load_System_Selection_Controller;
import persistence.System_Version;

public class Load_System_Selection_View extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8521782840656471666L;

	private final static int WINDOW_WIDTH = 640;
	private final static int WINDOW_HEIGHT = 900;
	
	private final static int NORTH_PANEL_HEIGHT = 0;
	private final static int SOUTH_PANEL_HEIGHT = 20;
	
	//private final static int TEXTFIELD_HEIGHT = 30;
	
	private final static int TABLE_WIDTH = WINDOW_WIDTH-20;
	private final static int TABLE_HEIGHT = (WINDOW_HEIGHT-SOUTH_PANEL_HEIGHT-NORTH_PANEL_HEIGHT)/4;
	
	private final static int COMMIT_ID_COLUMN_WIDTH = 8*40;
	
	private final static int BUTTON_WIDTH = 100;
	private final static int BUTTON_HEIGHT = 40;
	
	private JLabel errorDialogLabel;
	private JScrollPane savedSystemsTableScroll;
	private JScrollPane parsingSystemsTableScroll;
	
	private final String[] columnNamesSaved = {"SYSTEM NAME", "VERSION ~> COMMIT ID"};
	private final String[] columnNamesParsing = {"SYSTEM NAME", "VERSION ~> COMMIT ID", "PARSE/SAVED"};
	private JTable savedSystemsTable;
	private JTable parsingSystemsTable;
	
	private JComboBox<System_Version> savedSystemsComboBox;
	private JComboBox<System_Version> parsingSystemsComboBox;
	
	private JButton savedSystemsLoadButton;
	private JButton parsingSystemsParseButton;
	private JButton refreshListsButton;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try 
				{
					Load_System_Selection_View frame = new Load_System_Selection_View();
					frame.setVisible(true);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public Load_System_Selection_View()
	{
		setTitle("LOAD SYSTEM SELECTION");
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

		//CENTER PANEL -> list
		JPanel panelCenter = new JPanel();
		panelCenter.setBounds(0, 0, WINDOW_WIDTH-20, WINDOW_HEIGHT-SOUTH_PANEL_HEIGHT-NORTH_PANEL_HEIGHT);//620 MIN->700
		panelCenter.setLayout(null);
		getContentPane().add(panelCenter, BorderLayout.CENTER);
		
		//CURRENT OBJECT HEIGHT
		int center_panel_element_height = NORTH_PANEL_HEIGHT+25;//25
		
		JLabel savedSystemsLabel = new JLabel("Load SAVED SYSTEMS:");
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
		
		//JTABLES -> SYSTEMS SAVED
		Object[][] savedSystemsTabledata = Load_System_Selection_Controller.getInstance().savedSystemsRowsForTable2Col();
		Object[][] parsingSystemsTabledata = Load_System_Selection_Controller.getInstance().parsingSystemsRowsForTable3Col();
		
		savedSystemsTable = new JTable(savedSystemsTabledata, columnNamesSaved);
		parsingSystemsTable = new JTable(parsingSystemsTabledata, columnNamesParsing);

		savedSystemsTable.setPreferredScrollableViewportSize(new Dimension(TABLE_WIDTH, TABLE_HEIGHT));//600x500->530
		parsingSystemsTable.setPreferredScrollableViewportSize(new Dimension(TABLE_WIDTH, TABLE_HEIGHT));//600x500->530
		
		savedSystemsTable.getColumnModel().getColumn(1).setPreferredWidth(COMMIT_ID_COLUMN_WIDTH);
		savedSystemsTable.getColumnModel().getColumn(1).setMaxWidth(COMMIT_ID_COLUMN_WIDTH);
		savedSystemsTable.setEnabled(false);

		parsingSystemsTable.getColumnModel().getColumn(1).setPreferredWidth(COMMIT_ID_COLUMN_WIDTH);
		parsingSystemsTable.getColumnModel().getColumn(1).setMaxWidth(COMMIT_ID_COLUMN_WIDTH);
		parsingSystemsTable.getColumnModel().getColumn(2).setPreferredWidth(BUTTON_WIDTH);
		parsingSystemsTable.getColumnModel().getColumn(2).setMaxWidth(BUTTON_WIDTH);
		parsingSystemsTable.setRowHeight(BUTTON_HEIGHT);
		parsingSystemsTable.setEnabled(false);
		
		//savedSystemsTable.setDefaultRenderer(Object.class,new Parsing_Systems_TableCellRenderer());
		parsingSystemsTable.setDefaultRenderer(Object.class,new Parsing_Systems_TableCellRenderer());

		savedSystemsTableScroll = new JScrollPane(savedSystemsTable);
		savedSystemsTableScroll.setBounds(10, center_panel_element_height, TABLE_WIDTH, TABLE_HEIGHT);//600x500->530
		savedSystemsTable.setFillsViewportHeight(true);
		panelCenter.add(savedSystemsTableScroll);
		savedSystemsTableScroll.setVisible(true);
		
		center_panel_element_height += TABLE_HEIGHT;//358
		
		System_Version[] savedSystemsComboBox_options = Load_System_Selection_Controller.getInstance().getSaved_system_versions().toArray(new System_Version[Load_System_Selection_Controller.getInstance().getSaved_system_versions().size()]);
		savedSystemsComboBox = new JComboBox<System_Version>(savedSystemsComboBox_options);
		savedSystemsComboBox.setBounds(10, center_panel_element_height+2, TABLE_WIDTH-BUTTON_WIDTH-10, BUTTON_HEIGHT);//595
		savedSystemsComboBox.setRenderer(new System_Version_JComboBoxRenderer());
		panelCenter.add(savedSystemsComboBox);
		
		savedSystemsLoadButton = new JButton();
		savedSystemsLoadButton.setBounds(10+TABLE_WIDTH-BUTTON_WIDTH, center_panel_element_height+2, BUTTON_WIDTH, BUTTON_HEIGHT);//595
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
			savedSystemsLoadButton.setIcon(load_button_imageIcon);
		panelCenter.add(savedSystemsLoadButton);
		
		center_panel_element_height += BUTTON_HEIGHT;//400
		
		//////////////////////////////////////////////
		savedSystemsLoadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				clickButtonLoad();
			}
			
		});
		//////////////////////////////////////////////
		
		//HALF OF THE WIN HEIGHT
		center_panel_element_height = (WINDOW_HEIGHT-BUTTON_HEIGHT)/2;//430

		/////////////////////////////////////////////
		refreshListsButton = new JButton();
		refreshListsButton.setBounds((WINDOW_WIDTH-BUTTON_WIDTH)/2, center_panel_element_height, BUTTON_WIDTH, BUTTON_HEIGHT);//595
		String refresh_button_icon_name = "reloaded_folder_256x256";
		ImageIcon refresh_button_imageIcon = null;
		try {
			refresh_button_imageIcon = new ImageIcon(getClass().getResource("/icons/" + refresh_button_icon_name + ".png"));
			Image image = refresh_button_imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(32, 32,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			refresh_button_imageIcon = new ImageIcon(newimg);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		if (refresh_button_imageIcon != null)
			refreshListsButton.setIcon(refresh_button_imageIcon);
		panelCenter.add(refreshListsButton);
		
		refreshListsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				refreshData();
			}
			
		});
		//////////////////////////////////////////

		center_panel_element_height = (WINDOW_HEIGHT+BUTTON_HEIGHT)/2+10;//480

		JLabel parsingSystemsLabel = new JLabel("PARSE SYSTEMS:");
		parsingSystemsLabel.setBounds(10, center_panel_element_height, WINDOW_WIDTH/2, BUTTON_HEIGHT);
		String parsing_icon_name = "system_to_parse_256x256";
		ImageIcon parsing_imageIcon = null;
		try {
			parsing_imageIcon = new ImageIcon(getClass().getResource("/icons/" + parsing_icon_name + ".png"));
			Image image = parsing_imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(32, 32,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			parsing_imageIcon = new ImageIcon(newimg);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		if (parsing_imageIcon != null)
			parsingSystemsLabel.setIcon(parsing_imageIcon);
		panelCenter.add(parsingSystemsLabel);
		
		center_panel_element_height += BUTTON_HEIGHT;//520
		
		parsingSystemsTableScroll = new JScrollPane(parsingSystemsTable);
		parsingSystemsTableScroll.setBounds(10, center_panel_element_height, TABLE_WIDTH, TABLE_HEIGHT);//600x500->530
		parsingSystemsTable.setFillsViewportHeight(true);
		panelCenter.add(parsingSystemsTableScroll);
		parsingSystemsTableScroll.setVisible(true);
		
		center_panel_element_height += TABLE_HEIGHT;//813
		
		System_Version[] parsingSystemsComboBox_options = Load_System_Selection_Controller.getInstance().getParsing_system_versions().toArray(new System_Version[Load_System_Selection_Controller.getInstance().getParsing_system_versions().size()]);
		parsingSystemsComboBox = new JComboBox<System_Version>(parsingSystemsComboBox_options);
		parsingSystemsComboBox.setBounds(10, center_panel_element_height+2, TABLE_WIDTH-BUTTON_WIDTH-10, BUTTON_HEIGHT);//595
		parsingSystemsComboBox.setRenderer(new System_Version_JComboBoxRenderer());
		panelCenter.add(parsingSystemsComboBox);
		
		////////////////////////////////////////////////////
		
		parsingSystemsParseButton = new JButton();
		parsingSystemsParseButton.setBounds(10+TABLE_WIDTH-BUTTON_WIDTH, center_panel_element_height+2, BUTTON_WIDTH, BUTTON_HEIGHT);//595
		String parse_button_icon_name = "parse_button_icon_256x256";
		ImageIcon parse_button_imageIcon = null;
		try {
			parse_button_imageIcon = new ImageIcon(getClass().getResource("/icons/" + parse_button_icon_name + ".png"));
			Image image = parse_button_imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(32, 32,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			parse_button_imageIcon = new ImageIcon(newimg);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		if (parse_button_imageIcon != null)
			parsingSystemsParseButton.setIcon(parse_button_imageIcon);
		panelCenter.add(parsingSystemsParseButton);
		
		center_panel_element_height += BUTTON_HEIGHT;//855
		///////////////////////////////////////////////////
		parsingSystemsParseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				clickButtonParse();
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
	
	public void refreshData()
	{
		this.errorDialogLabel.setText("");
		this.errorDialogLabel.setVisible(false);
		//JTABLES -> SYSTEMS SAVED
		Object[][] savedSystemsTabledata = Load_System_Selection_Controller.getInstance().savedSystemsRowsForTable2Col();
		Object[][] parsingSystemsTabledata = Load_System_Selection_Controller.getInstance().parsingSystemsRowsForTable3Col();
		
		savedSystemsTable.setModel(new DefaultTableModel(savedSystemsTabledata, columnNamesSaved));
		parsingSystemsTable.setModel(new DefaultTableModel(parsingSystemsTabledata, columnNamesParsing));
		
		savedSystemsTable.setPreferredScrollableViewportSize(new Dimension(TABLE_WIDTH, TABLE_HEIGHT));//600x500->530
		parsingSystemsTable.setPreferredScrollableViewportSize(new Dimension(TABLE_WIDTH, TABLE_HEIGHT));//600x500->530
		
		savedSystemsTable.getColumnModel().getColumn(1).setPreferredWidth(COMMIT_ID_COLUMN_WIDTH);
		savedSystemsTable.getColumnModel().getColumn(1).setMaxWidth(COMMIT_ID_COLUMN_WIDTH);
		
		parsingSystemsTable.getColumnModel().getColumn(1).setPreferredWidth(COMMIT_ID_COLUMN_WIDTH);
		parsingSystemsTable.getColumnModel().getColumn(1).setMaxWidth(COMMIT_ID_COLUMN_WIDTH);
		parsingSystemsTable.getColumnModel().getColumn(2).setPreferredWidth(BUTTON_WIDTH);
		parsingSystemsTable.getColumnModel().getColumn(2).setMaxWidth(BUTTON_WIDTH);
		parsingSystemsTable.setRowHeight(BUTTON_HEIGHT);
		
		//savedSystemsTable.setDefaultRenderer(Object.class,new Parsing_Systems_TableCellRenderer());
		parsingSystemsTable.setDefaultRenderer(Object.class,new Parsing_Systems_TableCellRenderer());

		savedSystemsTable.revalidate();
		parsingSystemsTable.revalidate();
		
		//JComboBoxs
		System_Version[] savedSystemsComboBox_options = Load_System_Selection_Controller.getInstance().getSaved_system_versions().toArray(new System_Version[Load_System_Selection_Controller.getInstance().getSaved_system_versions().size()]);
		savedSystemsComboBox.setModel(new DefaultComboBoxModel<System_Version>(savedSystemsComboBox_options));
		savedSystemsComboBox.setRenderer(new System_Version_JComboBoxRenderer());
		savedSystemsComboBox.revalidate();
		
		System_Version[] parsingSystemsComboBox_options = Load_System_Selection_Controller.getInstance().getParsing_system_versions().toArray(new System_Version[Load_System_Selection_Controller.getInstance().getParsing_system_versions().size()]);
		parsingSystemsComboBox.setModel(new DefaultComboBoxModel<System_Version>(parsingSystemsComboBox_options));
		parsingSystemsComboBox.setRenderer(new System_Version_JComboBoxRenderer());
		parsingSystemsComboBox.revalidate();
		
	}
	
	public void clickButtonLoad()
	{
		this.errorDialogLabel.setText("");
		this.errorDialogLabel.setVisible(false);
		System_Version version_to_load = (System_Version) savedSystemsComboBox.getSelectedItem();
		Load_System_Selection_Controller.getInstance().loadSavedSystem(this, version_to_load);
	}
	
	public void clickButtonParse()
	{
		this.errorDialogLabel.setText("");
		this.errorDialogLabel.setVisible(false);
		System_Version version_to_parse = (System_Version) parsingSystemsComboBox.getSelectedItem();
		
		System.out.println("SAVED?->"+version_to_parse.isSaved());
		if(version_to_parse.isSaved())
		{
			String message = "The selected system was already PARSED.\n"
					+ "Do you want to REPARSE IT?";
			String title = "REPARSE SAVED SYSTEM?";
			boolean continue_op = this.continueOperationAlert(message, title);
			if(continue_op)
			{
				Load_System_Selection_Controller.getInstance().parseSystem(this, version_to_parse);
			}
		}
	}

}
