package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import model.Clone;

public class ClonePanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1103474184378047747L;

	private Clone clone = null;
	
	private final static int SPACE = 5;
	private final static int LABEL_WIDTH = 700;
	private final static int LABEL_HEIGHT = 16;
	private final static int TEXTFIELD_HEIGHT = 30;
	private final static int LIST_HEIGHT = 200;
	private final static int PANEL_WIDTH = LABEL_WIDTH+50;
	private final static int IMAGE_HEIGHT = 64;
	
	private final String[] columnNames = {"#", "LINE"};
	
	public ClonePanel(Clone clone) {
		this.clone = clone;
		
		setLayout(null);
		int y = 0;
		
        /******************************/
		JLabel cloneImageLabel = new JLabel();
		String clone_image_name = "clone_microscope_256x256";
		ImageIcon clone_imageIcon = null;
		try {
			clone_imageIcon = new ImageIcon(getClass().getResource("/icons/" + clone_image_name + ".png"));
			Image image = clone_imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(IMAGE_HEIGHT, IMAGE_HEIGHT,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			clone_imageIcon = new ImageIcon(newimg);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		if (clone_imageIcon != null)
			cloneImageLabel.setIcon(clone_imageIcon);
		cloneImageLabel.setBounds(SPACE, y, IMAGE_HEIGHT, IMAGE_HEIGHT);
		cloneImageLabel.setPreferredSize(new Dimension(IMAGE_HEIGHT,IMAGE_HEIGHT));
		add(cloneImageLabel);
		y += IMAGE_HEIGHT+SPACE;
		/******************************/
		JLabel idLabel = new JLabel("ID:");
		idLabel.setBounds(SPACE, y, LABEL_WIDTH, LABEL_HEIGHT);
		idLabel.setPreferredSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
		add(idLabel);
		idLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += LABEL_HEIGHT+SPACE;
		
		JTextField idTextField = new JTextField();
		idTextField.setColumns(16);
		idTextField.setBounds(SPACE, y, LABEL_WIDTH/2, TEXTFIELD_HEIGHT);
		idTextField.setPreferredSize(new Dimension(LABEL_WIDTH,TEXTFIELD_HEIGHT));
		add(idTextField);
		idTextField.setAlignmentX( Component.LEFT_ALIGNMENT );
		idTextField.setText(this.clone.getId_clone()+"");
		idTextField.setEditable(false);
		y += TEXTFIELD_HEIGHT+SPACE;
		
		JLabel filenameLabel = new JLabel("FILENAME:");
		filenameLabel.setBounds(SPACE, y, LABEL_WIDTH, LABEL_HEIGHT);
		filenameLabel.setPreferredSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
		add(filenameLabel);
		filenameLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += LABEL_HEIGHT+SPACE;
		
		JTextField filenameTextField = new JTextField();
		filenameTextField.setColumns(16);
		filenameTextField.setBounds(SPACE, y, LABEL_WIDTH, TEXTFIELD_HEIGHT);
		filenameTextField.setPreferredSize(new Dimension(LABEL_WIDTH,TEXTFIELD_HEIGHT));
		add(filenameTextField);
		filenameTextField.setAlignmentX( Component.LEFT_ALIGNMENT );
		filenameTextField.setText(this.clone.getFilename()+"");
		filenameTextField.setEditable(false);
		y += TEXTFIELD_HEIGHT+SPACE;
		
		JLabel linesLabel = new JLabel("LINES:");
		linesLabel.setBounds(SPACE, y, LABEL_WIDTH, LABEL_HEIGHT);
		linesLabel.setPreferredSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
		add(linesLabel);
		linesLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += LABEL_HEIGHT+SPACE;
		
		JTextField linesTextField = new JTextField();
		linesTextField.setColumns(16);
		linesTextField.setBounds(SPACE, y, LABEL_WIDTH/2, TEXTFIELD_HEIGHT);
		linesTextField.setPreferredSize(new Dimension(LABEL_WIDTH,TEXTFIELD_HEIGHT));
		add(linesTextField);
		linesTextField.setAlignmentX( Component.LEFT_ALIGNMENT );
		linesTextField.setText("["+this.clone.getStart_line()+"->"+this.clone.getEnd_line()+"]("+this.clone.getLines().size()+")");
		linesTextField.setEditable(false);
		y += TEXTFIELD_HEIGHT+SPACE;
		
		//JTABLES -> LINES
		Object[][] cloneLinesdata = fromMapToMatrixObjForTable2Col(this.clone.getLines());
		JTable cloneLinesTable = new JTable(cloneLinesdata, columnNames);
		
		cloneLinesTable.setPreferredScrollableViewportSize(new Dimension(LABEL_WIDTH-4*SPACE, LIST_HEIGHT));//600x500->530
		cloneLinesTable.getColumnModel().getColumn(0).setMaxWidth(150);
		//NOT EDITABLE
		cloneLinesTable.setDefaultEditor(Object.class, null);
		
		JScrollPane cloneLinesTableScroll = new JScrollPane(cloneLinesTable);
		cloneLinesTableScroll.setBounds(SPACE, y, LABEL_WIDTH-2*SPACE, LIST_HEIGHT);//600x500->530
		cloneLinesTable.setFillsViewportHeight(true);
		add(cloneLinesTableScroll);
		cloneLinesTableScroll.setVisible(true);
		
		y +=LIST_HEIGHT+SPACE;
		
		JLabel classLabel = new JLabel("CLASS:");
		classLabel.setBounds(SPACE, y, LABEL_WIDTH, LABEL_HEIGHT);
		classLabel.setPreferredSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
		add(classLabel);
		classLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += LABEL_HEIGHT+SPACE;
		
		JTextField classTextField = new JTextField();
		classTextField.setColumns(16);
		classTextField.setBounds(SPACE, y, LABEL_WIDTH/2, TEXTFIELD_HEIGHT);
		classTextField.setPreferredSize(new Dimension(LABEL_WIDTH,TEXTFIELD_HEIGHT));
		add(classTextField);
		classTextField.setAlignmentX( Component.LEFT_ALIGNMENT );
		classTextField.setText(this.clone.getClone_class().getId_class()+"");
		classTextField.setEditable(false);
		y += TEXTFIELD_HEIGHT+SPACE;
		
		JLabel authorImageLabel = new JLabel();
		String author_image_name = "anonymous_256x256";
		if(this.clone.getAuthor() != null)
			author_image_name = "author_icon_256x256";
		ImageIcon author_imageIcon = null;
		try {
			author_imageIcon = new ImageIcon(getClass().getResource("/icons/" + author_image_name + ".png"));
			Image image = author_imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(IMAGE_HEIGHT, IMAGE_HEIGHT,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			author_imageIcon = new ImageIcon(newimg);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		if (author_imageIcon != null)
			authorImageLabel.setIcon(author_imageIcon);
		authorImageLabel.setBounds(SPACE, y, IMAGE_HEIGHT, IMAGE_HEIGHT);
		authorImageLabel.setPreferredSize(new Dimension(IMAGE_HEIGHT,IMAGE_HEIGHT));
		add(authorImageLabel);
		
		JLabel authorLabel = new JLabel("AUTHOR:");
		authorLabel.setBounds(SPACE+IMAGE_HEIGHT, y+(IMAGE_HEIGHT-LABEL_HEIGHT)/2, LABEL_WIDTH/2, LABEL_HEIGHT);
		authorLabel.setPreferredSize(new Dimension(LABEL_WIDTH/2,LABEL_HEIGHT));
		add(authorLabel);
		authorLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += IMAGE_HEIGHT+SPACE;

		String author_name = "UNKNOWN";
		if(this.clone.getAuthor() != null)
			author_name = this.clone.getAuthor().getId_author();
		JTextField authorTextField = new JTextField();
		authorTextField.setColumns(16);
		authorTextField.setBounds(SPACE, y, LABEL_WIDTH, TEXTFIELD_HEIGHT);
		authorTextField.setPreferredSize(new Dimension(LABEL_WIDTH,TEXTFIELD_HEIGHT));
		add(authorTextField);
		authorTextField.setAlignmentX( Component.LEFT_ALIGNMENT );
		authorTextField.setText(author_name);
		authorTextField.setEditable(false);
		y += TEXTFIELD_HEIGHT+SPACE;
		
		String diff_set = "DIFF NOT SET";
		if(this.clone.getChange_modify_clone() != null)
			diff_set = "DIFF SET";
		JLabel diffLabel = new JLabel(diff_set);
		diffLabel.setBounds(SPACE, y, LABEL_WIDTH/2, LABEL_HEIGHT);
		diffLabel.setPreferredSize(new Dimension(LABEL_WIDTH/2,LABEL_HEIGHT));
		add(diffLabel);
		diffLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		if(this.clone.getChange_modify_clone() != null)
			diffLabel.setForeground(Color.GREEN);
		else
			diffLabel.setForeground(Color.RED);
		y += LABEL_HEIGHT+SPACE;

		String commit_data = "NO DATE - COMMIT NOT SET";
		if(this.clone.getChange_modify_clone() != null)
			commit_data = this.clone.getChange_modify_clone().getCommit().dateString()+" - COMMIT ID:"+this.clone.getChange_modify_clone().getCommit().getId_commit();
		JTextField commitDataTextField = new JTextField();
		commitDataTextField.setColumns(16);
		commitDataTextField.setBounds(SPACE, y, LABEL_WIDTH, TEXTFIELD_HEIGHT);
		commitDataTextField.setPreferredSize(new Dimension(LABEL_WIDTH,TEXTFIELD_HEIGHT));
		add(commitDataTextField);
		commitDataTextField.setAlignmentX( Component.LEFT_ALIGNMENT );
		commitDataTextField.setText(commit_data);
		commitDataTextField.setEditable(false);
		y += TEXTFIELD_HEIGHT+SPACE;

		System.out.println("-------->"+PANEL_WIDTH+"x"+(y+70)+"<---");
		setSize(PANEL_WIDTH,y+70);//200x480
		setPreferredSize(new Dimension(PANEL_WIDTH,y+70));//200x480
        setVisible(true);
	}
	
	public ClonePanel() {
		// TODO Auto-generated constructor stub
	}

	public ClonePanel(LayoutManager arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public ClonePanel(boolean arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public ClonePanel(LayoutManager arg0, boolean arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
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
