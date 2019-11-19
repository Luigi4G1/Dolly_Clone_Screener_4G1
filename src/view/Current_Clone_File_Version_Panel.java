package view;

import java.awt.Color;
import java.awt.Dimension;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import model.Current_Clone_File_Version;

public class Current_Clone_File_Version_Panel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -87478648822126843L;

	private final static int WINDOW_WIDTH = 1280;
	private final static int WINDOW_HEIGHT = 720;

	private final static int LIST_HEIGHT = WINDOW_HEIGHT-190;
	
	private final static int SPACE = 10;
	private final static int LABEL_HEIGHT = 16;
	private final static int TEXTFIELD_HEIGHT = 30;
	
	private Current_Clone_File_Version version;
	
	private JScrollPane cloneLinesTableScroll;
	private JScrollPane fileLinesTableScroll;
	
	private final String[] columnNames = {"#", "LINE"};
	private JTable cloneLinesTable;
	private JTable fileLinesTable;
	
	public Current_Clone_File_Version_Panel(Current_Clone_File_Version version)
	{
		this.version = version;
		
		setLayout(null);
		int y = SPACE;
		
		JLabel filenameLabel = new JLabel("Filename:");
		filenameLabel.setBounds(SPACE, y, 150, LABEL_HEIGHT);
		add(filenameLabel);
		y += LABEL_HEIGHT;
		
		JTextField filenameTextField = new JTextField();
		filenameTextField.setBounds(10, y, WINDOW_WIDTH/2-40, TEXTFIELD_HEIGHT);
		add(filenameTextField);
		filenameTextField.setEditable(false);
		if(this.version.getCurrent_file_version() != null)
			filenameTextField.setText(this.version.getCurrent_file_version().getFilename());
		y += (TEXTFIELD_HEIGHT+SPACE);
		
		JLabel checksumLabel = new JLabel("Checksum SHA-1:");
		checksumLabel.setBounds(SPACE, y, 150, LABEL_HEIGHT);
		add(checksumLabel);
		y += LABEL_HEIGHT;
		
		JTextField checksumTextField = new JTextField("UNKNOWN");
		checksumTextField.setBounds(10, y, WINDOW_WIDTH/2-40, TEXTFIELD_HEIGHT);
		add(checksumTextField);
		checksumTextField.setEditable(false);
		if(this.version.getCurrent_file_version() != null)
			checksumTextField.setText(this.version.getCurrent_file_version().getChecksum());
		y += (TEXTFIELD_HEIGHT+SPACE);
		
		JLabel versionDateLabel = new JLabel("Version Date:");
		versionDateLabel.setBounds(SPACE, y, 150, LABEL_HEIGHT);
		add(versionDateLabel);
		y += LABEL_HEIGHT;
		
		JTextField versionDateTextField = new JTextField("UNKNOWN");
		versionDateTextField.setBounds(10, y, WINDOW_WIDTH/2-40, TEXTFIELD_HEIGHT);
		add(versionDateTextField);
		versionDateTextField.setEditable(false);
		if(this.version.getCurrent_file_version() != null)
			if(this.version.getCurrent_file_version().getDiff() != null)
				if(this.version.getCurrent_file_version().getDiff().getCommit() != null)
					versionDateTextField.setText(this.version.getCurrent_file_version().getDiff().getCommit().dateString());
		y += (TEXTFIELD_HEIGHT+SPACE);
		
		JLabel commitLabel = new JLabel("From Commit:");
		commitLabel.setBounds(SPACE, y, 150, LABEL_HEIGHT);
		add(commitLabel);
		y += LABEL_HEIGHT;
		
		JTextField commitTextField = new JTextField("UNKNOWN");
		commitTextField.setBounds(10, y, WINDOW_WIDTH/2-40, TEXTFIELD_HEIGHT);
		add(commitTextField);
		commitTextField.setEditable(false);
		if(this.version.getCurrent_file_version() != null)
			if(this.version.getCurrent_file_version().getDiff() != null)
				if(this.version.getCurrent_file_version().getDiff().getCommit() != null)
					commitTextField.setText(this.version.getCurrent_file_version().getDiff().getCommit().getId_commit());
		y += (TEXTFIELD_HEIGHT+SPACE);
		
		JLabel authorCommitLabel = new JLabel("Author:");
		authorCommitLabel.setBounds(SPACE, y, 150, LABEL_HEIGHT);
		add(authorCommitLabel);
		y += LABEL_HEIGHT;
		
		JTextField authorCommitTextField = new JTextField("UNKNOWN");
		authorCommitTextField.setBounds(10, y, WINDOW_WIDTH/2-40, TEXTFIELD_HEIGHT);
		add(authorCommitTextField);
		authorCommitTextField.setEditable(false);
		if(this.version.getCurrent_file_version() != null)
			if(this.version.getCurrent_file_version().getDiff() != null)
				if(this.version.getCurrent_file_version().getDiff().getCommit() != null)
					if(this.version.getCurrent_file_version().getDiff().getCommit().getAuthor() != null)
						authorCommitTextField.setText(this.version.getCurrent_file_version().getDiff().getCommit().getAuthor().getId_author());
		y += (TEXTFIELD_HEIGHT+SPACE);

		JLabel positionLinesLabel = new JLabel("Current Clone Position Lines:");
		positionLinesLabel.setBounds(WINDOW_WIDTH/4, y, WINDOW_WIDTH/2-40, LABEL_HEIGHT);
		add(positionLinesLabel);
		y += LABEL_HEIGHT;
		
		JTextField positionLinesTextField = new JTextField();
		//positionLinesTextField.setBounds(10, y, WINDOW_WIDTH/2-40, TEXTFIELD_HEIGHT);
		positionLinesTextField.setBounds(WINDOW_WIDTH/4, y, WINDOW_WIDTH/2-40, TEXTFIELD_HEIGHT);
		add(positionLinesTextField);
		positionLinesTextField.setEditable(false);
		positionLinesTextField.setHorizontalAlignment(JTextField.CENTER);
		String position_string = "[";
		
		int start_line = 0;
		int end_line = 0;
		if(this.version != null)
		{
			start_line = this.version.getInitialLineNumber();
			if(start_line>0)
				position_string = position_string+start_line+" - ";
			else
				position_string = position_string+"UNKNOWN - ";
			
			end_line = this.version.getFinalLineNumber();
			if(end_line>0)
				position_string = position_string+end_line+"]";
			else
				position_string = position_string+"UNKNOWN]";
		}
		else
			position_string = position_string+"UNKNOWN]";
		
		positionLinesTextField.setText(position_string);
		if((start_line>0)&&(end_line>0))
		{
			positionLinesTextField.setBackground(Color.GREEN);
			try
			{
				if(this.version.getCurrent_file_version().getDiff().getCommit().getData_commit().before(this.version.getClone().getChange_modify_clone().getCommit().getData_commit()))
					positionLinesTextField.setBackground(Color.RED);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else
			positionLinesTextField.setBackground(Color.RED);
		y += (TEXTFIELD_HEIGHT+SPACE);
		
		//LOC modified
		JLabel modifiesLabel = new JLabel("LOC modified:");
		modifiesLabel.setBounds(WINDOW_WIDTH/2-75, y, 120, LABEL_HEIGHT);
		modifiesLabel.setHorizontalAlignment(JTextField.CENTER);
		add(modifiesLabel);
		y += LABEL_HEIGHT;
		
		String modified_percentage = ""+this.version.getModified_clone_lines_counter()+" (";
		float perc = (this.version.getModified_clone_lines_counter()*100)/(this.version.getClone().getEnd_line()-this.version.getClone().getStart_line()+1);
		if(perc < 0)
			perc = Math.abs(perc);
		if(perc < 1)
			modified_percentage = modified_percentage+"0";
		DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String numberAsString = decimalFormat.format(perc);
		modified_percentage = modified_percentage+numberAsString+" %)";
		
		JTextField modifies = new JTextField(modified_percentage);
		modifies.setBounds(WINDOW_WIDTH/2-75, y, 120, TEXTFIELD_HEIGHT);
		add(modifies);
		modifies.setEditable(false);
		modifies.setHorizontalAlignment(JTextField.CENTER);
		y += (TEXTFIELD_HEIGHT+SPACE);
		
		/******************************/
		JLabel cloneLinesLabel = new JLabel("Clone Lines:");
		cloneLinesLabel.setBounds(SPACE, y, 150, LABEL_HEIGHT);
		add(cloneLinesLabel);
		
		JLabel fileLinesLabel = new JLabel("File Lines:");
		fileLinesLabel.setBounds(WINDOW_WIDTH/2, y, 150, LABEL_HEIGHT);
		add(fileLinesLabel);
		
		y += TEXTFIELD_HEIGHT;
		
		//JTABLES -> LINES
		Object[][] cloneLinesdata = fromMapToMatrixObjForTable2Col(this.version.getCurrent_clone_lines());
		Object[][] fileLinesdata = fromMapToMatrixObjForTable2Col(this.version.getCurrent_file_version().getLines());
		
		cloneLinesTable = new JTable(cloneLinesdata, columnNames);
		fileLinesTable = new JTable(fileLinesdata, columnNames);
		cloneLinesTable.setPreferredScrollableViewportSize(new Dimension(WINDOW_WIDTH/2-40, LIST_HEIGHT));//600x500->530
		fileLinesTable.setPreferredScrollableViewportSize(new Dimension(WINDOW_WIDTH/2-40, LIST_HEIGHT));//600x500->530
		cloneLinesTable.getColumnModel().getColumn(0).setMaxWidth(150);
		fileLinesTable.getColumnModel().getColumn(0).setMaxWidth(150);
		
		//HIGHLIGHT CLONE LINES
		cloneLinesTable.setDefaultRenderer(Object.class,new FileLines_Highlight_TableCellRenderer(start_line, end_line));
		fileLinesTable.setDefaultRenderer(Object.class,new FileLines_Highlight_TableCellRenderer(start_line, end_line));
		//NOT EDITABLE
		cloneLinesTable.setDefaultEditor(Object.class, null);
		fileLinesTable.setDefaultEditor(Object.class, null);
		
		cloneLinesTableScroll = new JScrollPane(cloneLinesTable);
		cloneLinesTableScroll.setBounds(SPACE, y, WINDOW_WIDTH/2-40, LIST_HEIGHT);//600x500->530
		cloneLinesTable.setFillsViewportHeight(true);
		add(cloneLinesTableScroll);
		cloneLinesTableScroll.setVisible(true);
		
		fileLinesTableScroll = new JScrollPane(fileLinesTable);
		fileLinesTableScroll.setBounds(WINDOW_WIDTH/2, y, WINDOW_WIDTH/2-40, LIST_HEIGHT);//600x500->530
		fileLinesTable.setFillsViewportHeight(true);
		add(fileLinesTableScroll);
		fileLinesTableScroll.setVisible(true);
		
		y +=LIST_HEIGHT;
		
		setSize(WINDOW_WIDTH,y+70);//200x480
		setPreferredSize(new Dimension(WINDOW_WIDTH,y+70));//200x480
        setVisible(true);
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
