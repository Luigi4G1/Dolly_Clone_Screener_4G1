package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import model.Diff_Range_Change;
import model.File_Change;

public class FileChangePanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2909193627749222413L;

	private final static int WINDOW_WIDTH = 1280;
	private final static int WINDOW_HEIGHT = 720;

	private final static int LIST_HEIGHT = WINDOW_HEIGHT-190;
	
	private final static int SPACE = 10;
	private final static int LABEL_HEIGHT = 16;
	private final static int TEXTFIELD_HEIGHT = 30;
	private final static int RANGE_TEXTFIELD_WIDTH = 200;
	
	private File_Change file_change = null;
	
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
	
	public FileChangePanel(File_Change file_change)
	{
		this.file_change = file_change;
		
		setLayout(null);
		int y = LABEL_HEIGHT;
        /******************************/
		JLabel initialFilenameLabel = new JLabel("Initial Filename:");
		initialFilenameLabel.setBounds(SPACE, y, 150, LABEL_HEIGHT);
		add(initialFilenameLabel);
		
		JLabel destinationFilenameLabel = new JLabel("Destination Filename:");
		destinationFilenameLabel.setBounds(WINDOW_WIDTH/2, y, 150, LABEL_HEIGHT);
		add(destinationFilenameLabel);
		
		y += TEXTFIELD_HEIGHT;
		//FILENAMES
		initialFilenameTextField = new JTextField();
		initialFilenameTextField.setBounds(10, y, WINDOW_WIDTH/2-40, TEXTFIELD_HEIGHT);
		add(initialFilenameTextField);
		initialFilenameTextField.setEditable(false);
		initialFilenameTextField.setText(this.file_change.getInitial_filename());
		initialFilenameTextField.setBackground(Color.RED);

		destinationFilenameTextField = new JTextField();
		destinationFilenameTextField.setBounds(WINDOW_WIDTH/2, y, WINDOW_WIDTH/2-40, TEXTFIELD_HEIGHT);
		add(destinationFilenameTextField);
		destinationFilenameTextField.setEditable(false);
		destinationFilenameTextField.setText(this.file_change.getDestination_filename());
		destinationFilenameTextField.setBackground(Color.GREEN);

		//RANGES
		y += (TEXTFIELD_HEIGHT+5);//90
		initialFilenameRangeTextField = new JTextField();//195
		initialFilenameRangeTextField.setBounds((WINDOW_WIDTH/4)-(RANGE_TEXTFIELD_WIDTH/2), y, RANGE_TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT);
		add(initialFilenameRangeTextField);
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
		changeIndexesComboBox.setBounds(WINDOW_WIDTH/2-45, y+2, 65, TEXTFIELD_HEIGHT-14);
		add(changeIndexesComboBox);
		/************************************************************************************/
		changeIndexesComboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				showResultTableChanges(FileChangePanel.this.file_change.getChanges().get(changeIndexesComboBox.getSelectedIndex()));
			}
		});
		/************************************************************************************/
		
		JLabel numberOfChangeRangesLabel = new JLabel(" / ["+dim_combobox+"]");
		numberOfChangeRangesLabel.setBounds(WINDOW_WIDTH/2+20, y, 50, TEXTFIELD_HEIGHT-14);
		add(numberOfChangeRangesLabel);
		
		destinationFilenameRangeTextField = new JTextField();//825
		destinationFilenameRangeTextField.setBounds((WINDOW_WIDTH*3/4)-(RANGE_TEXTFIELD_WIDTH/2) , y, RANGE_TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT);
		add(destinationFilenameRangeTextField);
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
		initialFileLinesTable.setPreferredScrollableViewportSize(new Dimension(WINDOW_WIDTH/2-40, LIST_HEIGHT));//600x500->530
		destinationFileLinesTable.setPreferredScrollableViewportSize(new Dimension(WINDOW_WIDTH/2-40, LIST_HEIGHT));//600x500->530
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
		
		y += TEXTFIELD_HEIGHT;//120
		removedLinesTableScroll = new JScrollPane(initialFileLinesTable);
		removedLinesTableScroll.setBounds(SPACE, y, WINDOW_WIDTH/2-40, LIST_HEIGHT);//600x500->530
		initialFileLinesTable.setFillsViewportHeight(true);
		add(removedLinesTableScroll);
		removedLinesTableScroll.setVisible(true);

		addedLinesTableScroll = new JScrollPane(destinationFileLinesTable);
		addedLinesTableScroll.setBounds(WINDOW_WIDTH/2, y, WINDOW_WIDTH/2-40, LIST_HEIGHT);//600x500->530
		destinationFileLinesTable.setFillsViewportHeight(true);
		add(addedLinesTableScroll);
		addedLinesTableScroll.setVisible(true);
		
		y +=LIST_HEIGHT;
		
		System.out.println("-------->"+WINDOW_WIDTH+"x"+(y+70)+"<---");
		setSize(WINDOW_WIDTH,y+70);//200x480
		setPreferredSize(new Dimension(WINDOW_WIDTH,y+70));//200x480
        setVisible(true);
	}

	public void showResultTableChanges(Diff_Range_Change range)
	{
		Object[][] addedLinesdata = fromMapToMatrixObjForTable2Col(range.getAdded_lines());
		Object[][] removedLinesdata = fromMapToMatrixObjForTable2Col(range.getRemoved_lines());

		initialFilenameRangeTextField.setText("--- "+range.getRemove_initial_line()+", "+range.getRemove_lenght());
		destinationFilenameRangeTextField.setText("+++ "+range.getAdd_initial_line()+", "+range.getAdd_lenght());
		
		initialFileLinesTable.setModel(new DefaultTableModel(removedLinesdata, columnNames));
		destinationFileLinesTable.setModel(new DefaultTableModel(addedLinesdata, columnNames));
		initialFileLinesTable.setPreferredScrollableViewportSize(new Dimension(WINDOW_WIDTH/2-40, LIST_HEIGHT));//600x500->530
		destinationFileLinesTable.setPreferredScrollableViewportSize(new Dimension(WINDOW_WIDTH/2-40, LIST_HEIGHT));//600x500->530
		initialFileLinesTable.getColumnModel().getColumn(0).setMaxWidth(150);
		destinationFileLinesTable.getColumnModel().getColumn(0).setMaxWidth(150);
		
		initialFileLinesTable.setDefaultRenderer(Object.class,new File_Change_TableCellRenderer());
		destinationFileLinesTable.setDefaultRenderer(Object.class,new File_Change_TableCellRenderer());
		
		initialFileLinesTable.revalidate();
		destinationFileLinesTable.revalidate();
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
