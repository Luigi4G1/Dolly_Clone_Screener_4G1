package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import builder.Commit_History_Builder;
import builder.Commit_Parser;
import model.Commit_History;
import model.Commit_Node;
import model.Commit_parse;
import model.Diff_Range_Change;
import model.File_Change;
import utils.GitUtils4G1;

public class File_Change_View extends JFrame {

	private static final long serialVersionUID = 1L;
	private final static int WINDOW_WIDTH = 1280;
	private final static int WINDOW_HEIGHT = 720;
	
	private final static int NORTH_PANEL_HEIGHT = 0;
	private final static int SOUTH_PANEL_HEIGHT = 20;
	
	private final static int TEXTFIELD_HEIGHT = 30;
	private final static int RANGE_TEXTFIELD_WIDTH = 200;
	
	private File_Change file_change = null;
	
	private JLabel errorDialogLabel;
	private JScrollPane removedLinesTableScroll;
	private JScrollPane addedLinesTableScroll;
	private JTextField initialFilenameTextField;
	private JTextField destinationFilenameTextField;
	
	private JTextField initialFilenameRangeTextField;
	private JTextField destinationFilenameRangeTextField;
	
	private JComboBox<Object> changeIndexesComboBox;
	
	private final String[] columnNames = {"#", "LINE"};
	private JTable initialFileLinesTable;
	private JTable destinationFileLinesTable;
	
	//TEST -> TO REMOVE
	public static final String CLONETREE_COMMIT_FILE_TEST = "./files/Dnsjava/d97b6a0685d59143372bb392ab591dd8dd840b61/commit_list.txt";
	public static final String REPO_PATH_URI_TEST = "https://github.com/dnsjava/dnsjava.git";
	public static final String REPO_LOCAL_PATH_TEST = "./repo_4G1";
	public static final String SYSTEM_NAME_TEST = "Dnsjava";
	//TEST -> TO REMOVE - END#
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//TEST -> TO REMOVE
					File commit_story_text = new File(CLONETREE_COMMIT_FILE_TEST);
					//Scanner sc_commit_text = new Scanner(commit_story_text);
					//HashMap<String, Commit_parse> commit_map = Commit_Parser.read_commit_history_file(sc_commit_text);
					HashMap<String, Commit_parse> commit_map = Commit_Parser.read_commit_history_file(commit_story_text);
					GitUtils4G1 repo_info = new GitUtils4G1(REPO_PATH_URI_TEST, REPO_LOCAL_PATH_TEST, SYSTEM_NAME_TEST);
					Commit_History commit_history = Commit_History_Builder.commit_history_builder(repo_info, commit_map);

					Commit_Node commit = commit_history.getCommitByID("ad2b645b068bd8dede50f3723b2c29f6ea4a4a54");
					File_Change file_change = commit.getFile_changes().get(1);
					//TEST -> TO REMOVE - END#
					File_Change_View frame = new File_Change_View(file_change);
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public File_Change_View(File_Change file_change) {
		this.file_change = file_change;
		
		setTitle("FILE CHANGE VIEWER");
		setResizable(false);
		setBounds(100, 100, WINDOW_WIDTH, WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		
		//SOUTH PANEL -> error label
		errorDialogLabel = new JLabel("Questo e' un messaggio di errore!");
		errorDialogLabel.setBounds(0, 0, 325, SOUTH_PANEL_HEIGHT-4);
		errorDialogLabel.setForeground(Color.red);

		JPanel panelSouth = new JPanel();
		panelSouth.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelSouth.setBounds(0, 0, WINDOW_WIDTH, SOUTH_PANEL_HEIGHT);//prev->690
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
		
		JLabel initialFilenameLabel = new JLabel("Initial Filename:");
		initialFilenameLabel.setBounds(10, center_panel_element_height, 150, TEXTFIELD_HEIGHT-14);
		panelCenter.add(initialFilenameLabel);
		
		JLabel destinationFilenameLabel = new JLabel("Destination Filename:");
		destinationFilenameLabel.setBounds(WINDOW_WIDTH/2, center_panel_element_height, 150, TEXTFIELD_HEIGHT-14);
		panelCenter.add(destinationFilenameLabel);
		//FILENAMES
		center_panel_element_height += TEXTFIELD_HEIGHT;//55
		initialFilenameTextField = new JTextField();
		initialFilenameTextField.setBounds(10, center_panel_element_height, WINDOW_WIDTH/2-40, TEXTFIELD_HEIGHT);
		panelCenter.add(initialFilenameTextField);
		initialFilenameTextField.setEditable(false);
		initialFilenameTextField.setText(this.file_change.getInitial_filename());
		initialFilenameTextField.setBackground(Color.RED);
		
		destinationFilenameTextField = new JTextField();
		destinationFilenameTextField.setBounds(WINDOW_WIDTH/2, center_panel_element_height, WINDOW_WIDTH/2-40, TEXTFIELD_HEIGHT);
		panelCenter.add(destinationFilenameTextField);
		destinationFilenameTextField.setEditable(false);
		destinationFilenameTextField.setText(this.file_change.getDestination_filename());
		destinationFilenameTextField.setBackground(Color.GREEN);
		//RANGES
		center_panel_element_height += (TEXTFIELD_HEIGHT+5);//90
		initialFilenameRangeTextField = new JTextField();//195
		initialFilenameRangeTextField.setBounds((WINDOW_WIDTH/4)-(RANGE_TEXTFIELD_WIDTH/2), center_panel_element_height, RANGE_TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT);
		panelCenter.add(initialFilenameRangeTextField);
		initialFilenameRangeTextField.setEditable(false);
		initialFilenameRangeTextField.setBackground(Color.RED);
		
		int dim_combobox = 0;
		if(this.file_change != null)
			dim_combobox = this.file_change.getChanges().size();
		ArrayList<String> changes_indexes_list = new ArrayList<String>();
		for(int i= 0;i<dim_combobox; i++)
			changes_indexes_list.add(""+(i+1));

		String[] changes_indexes = changes_indexes_list.toArray(new String[changes_indexes_list.size()]);
		changeIndexesComboBox = new JComboBox<Object>(changes_indexes);
		changeIndexesComboBox.setBounds(WINDOW_WIDTH/2-45, center_panel_element_height+2, 65, TEXTFIELD_HEIGHT-14);//595
		panelCenter.add(changeIndexesComboBox);
		/************************************************************************************/
		changeIndexesComboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				showResultTableChanges(getFile_change().getChanges().get(changeIndexesComboBox.getSelectedIndex()));
			}
		});
		/************************************************************************************/
		
		JLabel numberOfChangeRangesLabel = new JLabel(" / ["+dim_combobox+"]");
		numberOfChangeRangesLabel.setBounds(WINDOW_WIDTH/2+20, center_panel_element_height, 50, TEXTFIELD_HEIGHT-14);//660
		panelCenter.add(numberOfChangeRangesLabel);
		
		destinationFilenameRangeTextField = new JTextField();//825
		destinationFilenameRangeTextField.setBounds((WINDOW_WIDTH*3/4)-(RANGE_TEXTFIELD_WIDTH/2) , center_panel_element_height, RANGE_TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT);
		panelCenter.add(destinationFilenameRangeTextField);
		destinationFilenameRangeTextField.setEditable(false);
		destinationFilenameRangeTextField.setBackground(Color.GREEN);
		//DEFAULT SELECTED COMBOBOX -> 0
		Diff_Range_Change range = this.file_change.getChanges().get(0);
		initialFilenameRangeTextField.setText("--- "+range.getRemove_initial_line()+", "+range.getRemove_lenght());
		destinationFilenameRangeTextField.setText("+++ "+range.getAdd_initial_line()+", "+range.getAdd_lenght());
		initialFilenameRangeTextField.setHorizontalAlignment(JTextField.CENTER);
		destinationFilenameRangeTextField.setHorizontalAlignment(JTextField.CENTER);
		//JTABLES -> LINES
		Object[][] addedLinesdata = fromMapToMatrixObjForTable2Col(range.getAdded_lines());
		Object[][] removedLinesdata = fromMapToMatrixObjForTable2Col(range.getRemoved_lines());
		
		initialFileLinesTable = new JTable(removedLinesdata, columnNames);
		destinationFileLinesTable = new JTable(addedLinesdata, columnNames);
		initialFileLinesTable.setPreferredScrollableViewportSize(new Dimension(WINDOW_WIDTH/2-40, WINDOW_HEIGHT-SOUTH_PANEL_HEIGHT-NORTH_PANEL_HEIGHT-170));//600x500->530
		destinationFileLinesTable.setPreferredScrollableViewportSize(new Dimension(WINDOW_WIDTH/2-40, WINDOW_HEIGHT-SOUTH_PANEL_HEIGHT-NORTH_PANEL_HEIGHT-170));//600x500->530
		initialFileLinesTable.getColumnModel().getColumn(0).setMaxWidth(150);
		destinationFileLinesTable.getColumnModel().getColumn(0).setMaxWidth(150);
		//COLORED:
		//- RED -> REMOVED LINES
		//+ GREEN -> ADDED LINES
		initialFileLinesTable.setDefaultRenderer(Object.class,new File_Change_TableCellRenderer());
		destinationFileLinesTable.setDefaultRenderer(Object.class,new File_Change_TableCellRenderer());
		//NOT EDITABLE
		initialFileLinesTable.setDefaultEditor(Object.class, null);
		destinationFileLinesTable.setDefaultEditor(Object.class, null);

		center_panel_element_height += TEXTFIELD_HEIGHT;//120
		removedLinesTableScroll = new JScrollPane(initialFileLinesTable);
		removedLinesTableScroll.setBounds(10, center_panel_element_height, WINDOW_WIDTH/2-40, WINDOW_HEIGHT-SOUTH_PANEL_HEIGHT-NORTH_PANEL_HEIGHT-170);//600x500->530
		initialFileLinesTable.setFillsViewportHeight(true);
		panelCenter.add(removedLinesTableScroll);
		removedLinesTableScroll.setVisible(true);
		
		addedLinesTableScroll = new JScrollPane(destinationFileLinesTable);
		addedLinesTableScroll.setBounds(WINDOW_WIDTH/2, center_panel_element_height, WINDOW_WIDTH/2-40, WINDOW_HEIGHT-SOUTH_PANEL_HEIGHT-NORTH_PANEL_HEIGHT-170);//600x500->530
		destinationFileLinesTable.setFillsViewportHeight(true);
		panelCenter.add(addedLinesTableScroll);
		addedLinesTableScroll.setVisible(true);
		
		panelCenter.setVisible(true);
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

	public void showResultTableChanges(Diff_Range_Change range)
	{
		Object[][] addedLinesdata = fromMapToMatrixObjForTable2Col(range.getAdded_lines());
		Object[][] removedLinesdata = fromMapToMatrixObjForTable2Col(range.getRemoved_lines());

		initialFilenameRangeTextField.setText("--- "+range.getRemove_initial_line()+", "+range.getRemove_lenght());
		destinationFilenameRangeTextField.setText("+++ "+range.getAdd_initial_line()+", "+range.getAdd_lenght());
		
		initialFileLinesTable.setModel(new DefaultTableModel(removedLinesdata, columnNames));
		destinationFileLinesTable.setModel(new DefaultTableModel(addedLinesdata, columnNames));
		initialFileLinesTable.setPreferredScrollableViewportSize(new Dimension(WINDOW_WIDTH/2-40, WINDOW_HEIGHT-SOUTH_PANEL_HEIGHT-NORTH_PANEL_HEIGHT-170));//600x500->530
		destinationFileLinesTable.setPreferredScrollableViewportSize(new Dimension(WINDOW_WIDTH/2-40, WINDOW_HEIGHT-SOUTH_PANEL_HEIGHT-NORTH_PANEL_HEIGHT-170));//600x500->530
		initialFileLinesTable.getColumnModel().getColumn(0).setMaxWidth(150);
		destinationFileLinesTable.getColumnModel().getColumn(0).setMaxWidth(150);
		
		initialFileLinesTable.setDefaultRenderer(Object.class,new File_Change_TableCellRenderer());
		destinationFileLinesTable.setDefaultRenderer(Object.class,new File_Change_TableCellRenderer());
		
		initialFileLinesTable.revalidate();
		destinationFileLinesTable.revalidate();
	}

	public File_Change getFile_change() {
		return file_change;
	}

	public Object[][] fromMapToMatrixObjForTable2Col(HashMap<Integer,String> map)
	{
		int row_size = map.values().size();
		Object[][] matrix = new Object[row_size][2];
		ArrayList<Integer> keys = new ArrayList<Integer>(map.keySet());
		Collections.sort(keys);
		int row_num = 0;
		for(Integer line_num : keys)
		{
			matrix[row_num][0] = line_num;
			matrix[row_num][1] = map.get(line_num);
			row_num++;
		}
		return matrix;
	}

	
}
