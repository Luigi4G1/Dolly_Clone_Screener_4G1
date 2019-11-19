package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import controller.Author_List_Controller;
import model.Commit_Author;
import persistence.System_Analysis;

public class Author_List_View extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2943603684241580969L;

	private final static int WINDOW_WIDTH = 1280;
	private final static int WINDOW_HEIGHT = 720;
	
	private final static int MENUBAR_HEIGHT = 32;
	
	private final static int NORTH_PANEL_HEIGHT = 0;
	private final static int SOUTH_PANEL_HEIGHT = 20;
	
	private final static int CENTER_PANEL_HEIGHT = (WINDOW_HEIGHT-SOUTH_PANEL_HEIGHT-NORTH_PANEL_HEIGHT-MENUBAR_HEIGHT);

	private final static int SPACE = 10;
	private final static int LABEL_HEIGHT = 16;
	private final static int TABLE_WIDTH = WINDOW_WIDTH-3*SPACE;
	private final static int TABLE_HEIGHT = CENTER_PANEL_HEIGHT*3/4;

	private System_Analysis analysis_result = null;
	
	private JLabel errorDialogLabel;
	
	private JScrollPane authorsTableScroll;
	private JTable authorsTable;
	
	private final String[] columnNames = {"NAME", "EMAIL", "#COMMITS", "#CLONES"};

	public Author_List_View(System_Analysis analysis_result)
	{
		this.analysis_result = analysis_result;
		
		setTitle("AUTHORS LIST VIEWER");
		setResizable(false);
		setBounds(SPACE, SPACE, WINDOW_WIDTH, WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		
		super.setJMenuBar(new MenuBarDollyScreener(this, this.analysis_result));

		//SOUTH PANEL -> error label
		errorDialogLabel = new JLabel("Questo e' un messaggio di errore!");
		errorDialogLabel.setBounds(0, 0, WINDOW_WIDTH, LABEL_HEIGHT);
		errorDialogLabel.setForeground(Color.red);
		
		JPanel panelSouth = new JPanel();
		panelSouth.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelSouth.setBounds(0, 0, WINDOW_WIDTH, SOUTH_PANEL_HEIGHT);
		panelSouth.add(errorDialogLabel);
		getContentPane().add(panelSouth, BorderLayout.SOUTH);
		errorDialogLabel.setVisible(false);
		panelSouth.setVisible(true);
		//SOUTH PANEL -> END#
		
		//CENTER PANEL -> list
		JPanel panelCenter = new JPanel();
		panelCenter.setBounds(0, 0, WINDOW_WIDTH, CENTER_PANEL_HEIGHT);
		panelCenter.setLayout(null);
		getContentPane().add(panelCenter, BorderLayout.CENTER);
		
		//JTABLES -> AUTHORS LIST
		Object[][] authorsTabledata = authorsRowsForTable4Col();
		
		int large_column_width = TABLE_WIDTH*7/16;
		int small_column_width = TABLE_WIDTH/16;
		authorsTable = new JTable(authorsTabledata, columnNames);
		authorsTable.setPreferredScrollableViewportSize(new Dimension(TABLE_WIDTH, TABLE_HEIGHT));
		//authorsTable.setPreferredScrollableViewportSize(new Dimension(TABLE_WIDTH, authorsTabledata.length*authorsTable.getRowHeight()));
		authorsTable.setMaximumSize(new Dimension(TABLE_WIDTH, TABLE_HEIGHT));
		//authorsTable.setMaximumSize(new Dimension(TABLE_WIDTH, authorsTabledata.length*authorsTable.getRowHeight()));
		authorsTable.getColumnModel().getColumn(0).setPreferredWidth(large_column_width);
		authorsTable.getColumnModel().getColumn(0).setMaxWidth(large_column_width);
		authorsTable.getColumnModel().getColumn(1).setPreferredWidth(large_column_width);
		authorsTable.getColumnModel().getColumn(1).setMaxWidth(large_column_width);
		authorsTable.getColumnModel().getColumn(2).setPreferredWidth(small_column_width);
		authorsTable.getColumnModel().getColumn(2).setMaxWidth(small_column_width);
		authorsTable.getColumnModel().getColumn(3).setPreferredWidth(small_column_width);
		authorsTable.getColumnModel().getColumn(3).setMaxWidth(small_column_width);
		authorsTable.setRowSelectionAllowed(true);
		//NOT EDITABLE CELLS
		authorsTable.setDefaultEditor(Object.class, null);
		authorsTable.setRowHeight(SOUTH_PANEL_HEIGHT);
		/*************************************************************************************/
		/*
		authorsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
		    @Override
		    public void valueChanged(ListSelectionEvent event) {
		        if (authorsTable.getSelectedRow() > -1) {
		            // print first column value from selected row
		            System.out.println(authorsTable.getValueAt(authorsTable.getSelectedRow(), 0).toString());
		        }
		    }
		});
		*/
		/*************************************************************************************/
		authorsTable.addMouseListener(new MouseAdapter() {
		    public void mouseReleased(MouseEvent evt)
		    {
		    	JTable table = (JTable)evt.getSource();
		    	int r = table.rowAtPoint(evt.getPoint());
		    	if (r >= 0 && r < table.getRowCount())
		    		table.setRowSelectionInterval(r, r);
		        else
		        	table.clearSelection();

		        int rowindex = table.getSelectedRow();
		        if (rowindex < 0)
		        	return;
		        	
		        if (evt.isPopupTrigger() && evt.getComponent() instanceof JTable )
		        {
		        	/****************************************************/
		        	JPopupMenu menu = new JPopupMenu();
		        	JMenuItem itemShowInfo = new JMenuItem("Show Info");
		        	itemShowInfo.addActionListener(new ActionListener() {
		        		public void actionPerformed(ActionEvent e)
		        		{
		        			System.out.println("Selecting the element in position " + rowindex);
		        			Commit_Author author = analysis_result.getCommit_history_analysis().getAuthorByID(authorsTable.getValueAt(authorsTable.getSelectedRow(), 0).toString());
		        			showInfo(author);
		        		}
		        	});
		        	menu.add(itemShowInfo);
		        	
		        	JMenuItem itemOpenView = new JMenuItem("Open Author View");
		        	itemOpenView.addActionListener(new ActionListener() {
		        		public void actionPerformed(ActionEvent e)
		        		{
		        				System.out.println("Selecting the element in position " + rowindex);
		        				Commit_Author author = analysis_result.getCommit_history_analysis().getAuthorByID(authorsTable.getValueAt(authorsTable.getSelectedRow(), 0).toString());
		        				Author_List_Controller.getInstance().showAuthorView(Author_List_View.this, author, Author_List_View.this.analysis_result);
		        		}
		        	});
		        	menu.add(itemOpenView);
		        	
		        	menu.show(evt.getComponent(), evt.getX(), evt.getY());
		        	/*******************************************/
		        }
		        errorDialogLabel.setText("");
		    	errorDialogLabel.setVisible(false);
		    }
		});
		/********************************************************************/
		authorsTableScroll = new JScrollPane(authorsTable);
		authorsTableScroll.setBounds(SPACE, SPACE, TABLE_WIDTH, TABLE_HEIGHT);
		//authorsTableScroll.setBounds(SPACE, SPACE, TABLE_WIDTH, (authorsTabledata.length*(authorsTable.getRowHeight()+1)));
		authorsTable.setFillsViewportHeight(false);
		panelCenter.add(authorsTableScroll);
		authorsTableScroll.setVisible(true);
		
		int num_author = 0;
		if(authorsTabledata != null)
			num_author = authorsTabledata.length;
		JLabel authorLabel = new JLabel("Authors:["+num_author+"]");
		authorLabel.setBounds(SPACE, TABLE_HEIGHT+SPACE, WINDOW_WIDTH/2, LABEL_HEIGHT);
		authorLabel.setSize(WINDOW_WIDTH/2, LABEL_HEIGHT);
		panelCenter.add(authorLabel);

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
	
	public Object[][] authorsRowsForTable4Col()
	{
		if((analysis_result == null)||(analysis_result.getCommit_history_analysis()==null)||(analysis_result.getCommit_history_analysis().getCommit_authors()==null))
		{
			showAlert("DATA NOT SET CORRECTLY.\nUNABLE TO SHOW ON TABLE!", "WRONG TABLE DATA");
			return new Object[0][4];
		}
		else
		{
			Object[][] matrix = new Object[analysis_result.getCommit_history_analysis().getCommit_authors().size()][4];
			int row_index = 0;
			for(Map.Entry<String, Commit_Author> entry:analysis_result.getCommit_history_analysis().getCommit_authors().entrySet())
			{
				matrix[row_index][0] = entry.getKey();
				matrix[row_index][1] = entry.getValue().getEmail_author();
				matrix[row_index][2] = entry.getValue().getCommit_done_map().size();
				matrix[row_index][3] = entry.getValue().getCreated_clones().size();
				row_index++;
			}
			return matrix;
		}
	}
	
	public void showInfo(Commit_Author result)
	{
		if (this.errorDialogLabel.isVisible())
			this.errorDialogLabel.setVisible(false);
		if (result == null)
			this.showAlert("ERROR! NULL VALUE FOUND.\nUNABLE TO SHOW DATA!","NO DATA");
		else
		{
			JFrame f= new JFrame();
			JDialog d = new JDialog(f , "Author Info", true);
			d.getContentPane().setLayout( new FlowLayout(FlowLayout.LEFT) );
	        d.getContentPane().add(Box.createVerticalGlue());
	        //CREATE PanelAuthor
	        AuthorPanel panPane = new AuthorPanel(result);
	        d.getContentPane().add(panPane);

	        d.setSize(panPane.getWidth(), panPane.getHeight());
			d.setPreferredSize(new Dimension(panPane.getWidth(), panPane.getHeight()));
			d.setResizable(false);
	        d.setVisible(true);
		}
	}

}
