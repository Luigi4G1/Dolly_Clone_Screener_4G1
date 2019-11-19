package view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.LayoutManager;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import model.Report_NiCad5;

public class NiCadPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8321258925766778942L;

	private Report_NiCad5 report =null;
	
	private final static int SPACE = 5;
	private final static int LABEL_WIDTH = 400;
	private final static int LABEL_HEIGHT = 16;
	private final static int TEXTFIELD_HEIGHT = 30;
	
	private final static int PANEL_WIDTH = LABEL_WIDTH+50;
	private final static int IMAGE_HEIGHT = 64;
	
	public NiCadPanel(Report_NiCad5 report) {
		this.report = report;
		
		setLayout(null);
		int y = 0;
		
		/******************************/
		JLabel reportImageLabel = new JLabel();
		String report_image_name = "medical_history_256x256";
		ImageIcon report_imageIcon = null;
		try {
			report_imageIcon = new ImageIcon(getClass().getResource("/icons/" + report_image_name + ".png"));
			Image image = report_imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(IMAGE_HEIGHT, IMAGE_HEIGHT,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			report_imageIcon = new ImageIcon(newimg);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		if (report_imageIcon != null)
			reportImageLabel.setIcon(report_imageIcon);
		reportImageLabel.setBounds(SPACE, y, IMAGE_HEIGHT, IMAGE_HEIGHT);
		reportImageLabel.setPreferredSize(new Dimension(IMAGE_HEIGHT,IMAGE_HEIGHT));
		add(reportImageLabel);
		y += IMAGE_HEIGHT+SPACE;
		/******************************/
		
		JLabel processorLabel = new JLabel("PROCESSOR:");
		processorLabel.setBounds(SPACE, y, LABEL_WIDTH, LABEL_HEIGHT);
		processorLabel.setPreferredSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
		add(processorLabel);
		processorLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += LABEL_HEIGHT+SPACE;
		
		JTextField processorTextField = new JTextField();
		processorTextField.setColumns(16);
		processorTextField.setBounds(SPACE, y, LABEL_WIDTH/2, TEXTFIELD_HEIGHT);
		processorTextField.setPreferredSize(new Dimension(LABEL_WIDTH,TEXTFIELD_HEIGHT));
		add(processorTextField);
		processorTextField.setAlignmentX( Component.LEFT_ALIGNMENT );
		processorTextField.setText(this.report.getProcessor()+"");
		processorTextField.setEditable(false);
		y += TEXTFIELD_HEIGHT+SPACE;
		
		JLabel granularityLabel = new JLabel("GRANULARITY:");
		granularityLabel.setBounds(SPACE, y, LABEL_WIDTH, LABEL_HEIGHT);
		granularityLabel.setPreferredSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
		add(granularityLabel);
		granularityLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += LABEL_HEIGHT+SPACE;
		
		JTextField granularityTextField = new JTextField();
		granularityTextField.setColumns(16);
		granularityTextField.setBounds(SPACE, y, LABEL_WIDTH/2, TEXTFIELD_HEIGHT);
		granularityTextField.setPreferredSize(new Dimension(LABEL_WIDTH,TEXTFIELD_HEIGHT));
		add(granularityTextField);
		granularityTextField.setAlignmentX( Component.LEFT_ALIGNMENT );
		granularityTextField.setText(this.report.getGranularity()+"");
		granularityTextField.setEditable(false);
		y += TEXTFIELD_HEIGHT+SPACE;
		
		JLabel thresholdLabel = new JLabel("THRESHOLD:");
		thresholdLabel.setBounds(SPACE, y, LABEL_WIDTH, LABEL_HEIGHT);
		thresholdLabel.setPreferredSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
		add(thresholdLabel);
		thresholdLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += LABEL_HEIGHT+SPACE;
		
		JTextField thresholdTextField = new JTextField();
		thresholdTextField.setColumns(16);
		thresholdTextField.setBounds(SPACE, y, LABEL_WIDTH/2, TEXTFIELD_HEIGHT);
		thresholdTextField.setPreferredSize(new Dimension(LABEL_WIDTH,TEXTFIELD_HEIGHT));
		add(thresholdTextField);
		thresholdTextField.setAlignmentX( Component.LEFT_ALIGNMENT );
		thresholdTextField.setText(this.report.getThreshold()+"");
		thresholdTextField.setEditable(false);
		y += TEXTFIELD_HEIGHT+SPACE;
		
		JLabel minlinesLabel = new JLabel("MINLINES:");
		minlinesLabel.setBounds(SPACE, y, LABEL_WIDTH, LABEL_HEIGHT);
		minlinesLabel.setPreferredSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
		add(minlinesLabel);
		minlinesLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += LABEL_HEIGHT+SPACE;
		
		JTextField minlinesTextField = new JTextField();
		minlinesTextField.setColumns(16);
		minlinesTextField.setBounds(SPACE, y, LABEL_WIDTH/2, TEXTFIELD_HEIGHT);
		minlinesTextField.setPreferredSize(new Dimension(LABEL_WIDTH,TEXTFIELD_HEIGHT));
		add(minlinesTextField);
		minlinesTextField.setAlignmentX( Component.LEFT_ALIGNMENT );
		minlinesTextField.setText(this.report.getMinlines()+"");
		minlinesTextField.setEditable(false);
		y += TEXTFIELD_HEIGHT+SPACE;
		
		JLabel maxlinesLabel = new JLabel("MAXLINES:");
		maxlinesLabel.setBounds(SPACE, y, LABEL_WIDTH, LABEL_HEIGHT);
		maxlinesLabel.setPreferredSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
		add(maxlinesLabel);
		maxlinesLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += LABEL_HEIGHT+SPACE;
		
		JTextField maxlinesTextField = new JTextField();
		maxlinesTextField.setColumns(16);
		maxlinesTextField.setBounds(SPACE, y, LABEL_WIDTH/2, TEXTFIELD_HEIGHT);
		maxlinesTextField.setPreferredSize(new Dimension(LABEL_WIDTH,TEXTFIELD_HEIGHT));
		add(maxlinesTextField);
		maxlinesTextField.setAlignmentX( Component.LEFT_ALIGNMENT );
		maxlinesTextField.setText(this.report.getMaxlines()+"");
		maxlinesTextField.setEditable(false);
		y += TEXTFIELD_HEIGHT+SPACE;
		
		//////////////////////////////////////////////////
		//SEPARATOR
		JSeparator separator_result_start = new JSeparator(JSeparator.HORIZONTAL);
		separator_result_start.setBounds(0, y, PANEL_WIDTH-20, LABEL_HEIGHT);
		add(separator_result_start);
		y += LABEL_HEIGHT+SPACE;
		
		JLabel systemLabel = new JLabel("SYSTEM ANALYZED:");
		systemLabel.setBounds(SPACE, y, LABEL_WIDTH, LABEL_HEIGHT);
		systemLabel.setPreferredSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
		add(systemLabel);
		systemLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += LABEL_HEIGHT+SPACE;
		
		JTextField systemTextField = new JTextField();
		systemTextField.setColumns(16);
		systemTextField.setBounds(SPACE, y, LABEL_WIDTH, TEXTFIELD_HEIGHT);
		systemTextField.setPreferredSize(new Dimension(LABEL_WIDTH,TEXTFIELD_HEIGHT));
		add(systemTextField);
		systemTextField.setAlignmentX( Component.LEFT_ALIGNMENT );
		systemTextField.setText(this.report.getSystem()+"");
		systemTextField.setEditable(false);
		y += TEXTFIELD_HEIGHT+SPACE;
		
		JLabel versionLabel = new JLabel("VERSION-COMMIT:");
		versionLabel.setBounds(SPACE, y, LABEL_WIDTH, LABEL_HEIGHT);
		versionLabel.setPreferredSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
		add(versionLabel);
		versionLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += LABEL_HEIGHT+SPACE;
		
		JTextField versionTextField = new JTextField();
		versionTextField.setColumns(16);
		versionTextField.setBounds(SPACE, y, LABEL_WIDTH, TEXTFIELD_HEIGHT);
		versionTextField.setPreferredSize(new Dimension(LABEL_WIDTH,TEXTFIELD_HEIGHT));
		add(versionTextField);
		versionTextField.setAlignmentX( Component.LEFT_ALIGNMENT );
		versionTextField.setText(this.report.getCommit_id_version()+"");
		versionTextField.setEditable(false);
		y += TEXTFIELD_HEIGHT+SPACE;
		
		int numclasses = 0;
		if(this.report.getClone_classes() != null)
			numclasses = this.report.getClone_classes().size();
		JLabel numclassesLabel = new JLabel("CLONE CLASSES:["+this.report.getNum_classes()+"]->READ["+numclasses+"]");
		numclassesLabel.setBounds(SPACE, y, LABEL_WIDTH, LABEL_HEIGHT);
		numclassesLabel.setPreferredSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
		add(numclassesLabel);
		numclassesLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += LABEL_HEIGHT+SPACE;
		
		int numclones = 0;
		if(this.report.getClones() != null)
			numclones = this.report.getClones().size();
		JLabel numclonesLabel = new JLabel("CLONES:["+numclones+"]");
		numclonesLabel.setBounds(SPACE, y, LABEL_WIDTH, LABEL_HEIGHT);
		numclonesLabel.setPreferredSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
		add(numclonesLabel);
		numclonesLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		y += LABEL_HEIGHT+SPACE;
		
		//////////////////////////////////////////////////
		//SEPARATOR
		JSeparator separator_result_end = new JSeparator(JSeparator.HORIZONTAL);
		separator_result_end.setBounds(0, y, PANEL_WIDTH-20, LABEL_HEIGHT);
		add(separator_result_end);
		y += LABEL_HEIGHT+SPACE;
		
		System.out.println("-------->"+PANEL_WIDTH+"x"+(y+70)+"<---");
		setSize(PANEL_WIDTH,y+70);//200x480
		setPreferredSize(new Dimension(PANEL_WIDTH,y+70));//200x480
        setVisible(true);
	}
	
	public NiCadPanel() {
		// TODO Auto-generated constructor stub
	}

	public NiCadPanel(LayoutManager arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public NiCadPanel(boolean arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public NiCadPanel(LayoutManager arg0, boolean arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

}
