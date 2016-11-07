import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.ScrollPaneConstants;
import javax.swing.Scrollable;

import org.jdesktop.swingx.JXTaskPane;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;


@SuppressWarnings("serial")
public class WSNFrame extends JFrame
{
	public int loaded = -1;
	public int saving = -1;
	
	public static Surface surface;
	
	private JFormattedTextField tField_sns1Cnt;		// Sensor count text field: Sensor type 1
	private JFormattedTextField tField_sns1Rng;		// Sensing range text field: Sensor type 1
	private JFormattedTextField tField_cmn1Rng; 	// Communication range text field: Sensor type 1
	private JFormattedTextField tField_sns2Cnt;		// Sensor count text field: Sensor type 2
	private JFormattedTextField tField_sns2Rng;		// Sensing range text field: Sensor type 2
	private JFormattedTextField tField_cmn2Rng; 	// Communication range text field: Sensor type 2
	private JFormattedTextField tField_fieldW;		// Field width text field
	private JFormattedTextField tField_fieldH;		// Field height text field
	private JTextField 			tField_algPar;		// Algorithm parameters
	private JFormattedTextField tField_itrCnt;		// Number of iterations the simulation is to run
	private JFormattedTextField tField_dtcThr;		// Detection threshold value
	private JFormattedTextField tField_graphRng;	// Graph min range text field
	private JFormattedTextField tField_graphRng2;	// Graph max range text field
	private JFormattedTextField tField_graphInc;	// Graph increment value
	
	private Choice choice_itrAlg;	// Intrusion algorithm choice box
	private Choice choice_dtcTyp;	// Detection type choice box
	private Choice choice_configs;	// Configuration ids choice box
	private Choice choice_metric;	// Graph metric choice box
	private Choice choice_yAxis;	// How to graph y-axis on graph
	
	private JScrollPane pnl_surface;
	private JTextArea tArea_log; 	// Text area for log
	private JTextArea tArea_settings;
	
	private JButton btn_startSim;
	private JButton btn_startSimAnim;
	private JButton btn_stopSim;
	private JButton btn_updateSim;
	private JButton btn_updateSns;
	private JButton btn_loadConfig;
	private JButton btn_saveConfig;
	private JButton btn_delConfig;
	private JButton btn_graph;
	
	private GraphSurface graphSurface;
	private JXTaskPane tPnl_updSim;
	private JXTaskPane tPnl_updSns;
	private JXTaskPane tPnl_display;

	// Used for options panel
	private static class ScrollablePanel extends JPanel implements Scrollable
	{
	    public Dimension getPreferredScrollableViewportSize() { return super.getPreferredSize(); }
	    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) { return 16; };
	    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) { return 16; }
	    public boolean getScrollableTracksViewportWidth() { return true; }
	    public boolean getScrollableTracksViewportHeight() { return false; }
	}
	
	public WSNFrame()
	{
		surface = new Surface(200,200,this);
		final RunNoAnimation looper = new RunNoAnimation(200,200,this);
		
		surface.run.createTable();
		
		setTitle("WSN Simulator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 800);
		setLocationRelativeTo(null);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				Timer timer = surface.getTimer();
				timer.stop();
			}
		});
		
		// Format for text fields
		NumberFormat intFormat = NumberFormat.getIntegerInstance();
		
		// GUI
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel pnl_sim = new JPanel();
		pnl_sim.setLayout(new BorderLayout(0, 0));
		tabbedPane.addTab("Simulation", null, pnl_sim, null);
		
		// Options panel
		JScrollPane scrPnl_sim_opt = new JScrollPane();
		scrPnl_sim_opt.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrPnl_sim_opt.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		pnl_sim.add(scrPnl_sim_opt, BorderLayout.WEST);
		
		ScrollablePanel pnl_sim_opt = new ScrollablePanel();
		pnl_sim_opt.setLayout(new FormLayout(
			new ColumnSpec[] {
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("pref:grow"),
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("pref:grow"),
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC
			},
			new RowSpec[] {
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC
			})
		);
		scrPnl_sim_opt.setViewportView(pnl_sim_opt);
		
		btn_startSimAnim = new JButton("Start Simulation w/ Animation");
		btn_startSimAnim.setEnabled(false);

		btn_startSim = new JButton("Start Simulation");
		btn_startSim.setEnabled(false);
		
		btn_stopSim = new JButton("Stop Simulation");
		btn_stopSim.setEnabled(false);
		
		JSeparator separator1 = new JSeparator();
		
		tPnl_updSim = new JXTaskPane();
		tPnl_updSim.setTitle("Update Simulation");
		tPnl_updSim.getContentPane().setLayout(new FormLayout(
			new ColumnSpec[] {
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("pref:grow"),
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("pref:grow"),
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC
			},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC
			}
		));
		
		JLabel lbl_itrCnt = new JLabel("Number of Trials:");
		
		tField_itrCnt = new JFormattedTextField(intFormat);
		tField_itrCnt.setText("");
		tField_itrCnt.setColumns(10);
		
		JLabel lbl_sns1Rng = new JLabel("T1 Sensing Range:");
		
		// Sensing range for Type 1 sensors text field
		tField_sns1Rng = new JFormattedTextField(intFormat);
		tField_sns1Rng.setText("");
		tField_sns1Rng.setColumns(8);
		
		JLabel lbl_cmn1Rng = new JLabel("T1 Communication Range:");
		
		// Communication range for Type 1 sensors text field
		tField_cmn1Rng = new JFormattedTextField(intFormat);
		tField_cmn1Rng.setText("");
		tField_cmn1Rng.setColumns(8);
		
        JLabel lbl_sns2Rng = new JLabel("T2 Sensing Range:");
		
		// Sensing range for Type 2 sensors text field
		tField_sns2Rng = new JFormattedTextField(intFormat);
		tField_sns2Rng.setText("");
		tField_sns2Rng.setColumns(8);
		
		JLabel lbl_cmn2Rng = new JLabel("T2 Communication Range:");
		
		// Communication range for Type 2 sensors text field
		tField_cmn2Rng = new JFormattedTextField(intFormat);
		tField_cmn2Rng.setText("");
		tField_cmn2Rng.setColumns(8);
		
		JLabel lbl_itrAlg = new JLabel("Intrusion Algorithm:");
		
		// Intrusion algorithm choice box
		choice_itrAlg = new Choice();
		choice_itrAlg.add("Linear");
		choice_itrAlg.add("Random");
		choice_itrAlg.add("A*");
		choice_itrAlg.add("FollowTheGapMethod");
		choice_itrAlg.add("GoAroundMethod");
		choice_itrAlg.add("SimultaneousAvoidance");
		choice_itrAlg.add("FollowTheGap2");
		
		JLabel lbl_algPar = new JLabel("Parameters:");
		
		tField_algPar = new JTextField();
		tField_algPar.setText("");
		tField_algPar.setColumns(8);
		
		JLabel lbl_dtcTyp = new JLabel("Detection Type:");
		
		// Detection type choice box
		choice_dtcTyp = new Choice();
		choice_dtcTyp.add("Simultaneous");
		choice_dtcTyp.add("Concurrent");
		
		JLabel lbl_dtcThr = new JLabel("Detection Threshold:");
		
		tField_dtcThr = new JFormattedTextField(intFormat);
		tField_dtcThr.setColumns(10);
		
		btn_updateSim = new JButton("Update Simulation");
		btn_updateSim.setEnabled(false);
		
		tPnl_updSns = new JXTaskPane();
		tPnl_updSns.setCollapsed(true);
		tPnl_updSns.setTitle("Update Sensors");
		tPnl_updSns.getContentPane().setLayout(new FormLayout(
			new ColumnSpec[] {
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("pref:grow"),
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("pref:grow"),
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC
			},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
			}
		));
		
		JLabel lbl_sns1Cnt = new JLabel("T1 Sensor Count:");
		
		// Sensor count text field
		tField_sns1Cnt = new JFormattedTextField(intFormat);
		tField_sns1Cnt.setText("");
		tField_sns1Cnt.setColumns(8);
		
        JLabel lbl_sns2Cnt = new JLabel("T2 Sensor Count:");
		
		// Sensor count text field
		tField_sns2Cnt = new JFormattedTextField(intFormat);
		tField_sns2Cnt.setText("");
		tField_sns2Cnt.setColumns(8);

		JLabel lbl_fieldW = new JLabel("Field Width:");
				
		tField_fieldW = new JFormattedTextField(intFormat);
		tField_fieldW.setText("450");
		tField_fieldW.setColumns(10);
		
		JLabel lbl_fieldH = new JLabel("Field Height:");
		
		tField_fieldH = new JFormattedTextField(intFormat);
		tField_fieldH.setText("480");
		tField_fieldH.setColumns(10);
		
		btn_updateSns = new JButton("Update Sensors");
		
		tPnl_display = new JXTaskPane();
		tPnl_display.setTitle("Display Options");
		tPnl_display.setCollapsed(true);
		tPnl_display.getContentPane().setLayout(new FormLayout(
			new ColumnSpec[] {
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("pref:grow"),
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("pref:grow"),
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC
			},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
			}
		));
		
		final JCheckBox cBox_drawComRng = new JCheckBox();
		cBox_drawComRng.setText("Draw communication range");
		cBox_drawComRng.setSelected(true);
		
		final JCheckBox cBox_drawComLines = new JCheckBox();
		cBox_drawComLines.setText("Draw communication lines");
		cBox_drawComLines.setSelected(false);
		
		JLabel lbl_drawOpacity = new JLabel("Sensor Opacity:");
		
		final JSlider sldr_drawOpacity = new JSlider();
		sldr_drawOpacity.setMinimum(0);
		sldr_drawOpacity.setMaximum(100);
		sldr_drawOpacity.setMinorTickSpacing(10);
		sldr_drawOpacity.setMajorTickSpacing(50);
		sldr_drawOpacity.setPaintTicks(true);
		sldr_drawOpacity.setPaintLabels(true);
		sldr_drawOpacity.setPreferredSize(new Dimension(140,45));
		sldr_drawOpacity.setValue((int)(surface.drawSnsOpacity * (float)100/255));
		
		JSeparator separator2 = new JSeparator();
		
		btn_loadConfig = new JButton("Load");
		
		choice_configs = new Choice();
		saving = updateConfigBox() + 1;
		
		btn_saveConfig = new JButton("Save");
		btn_saveConfig.setEnabled(false);
		
		btn_delConfig = new JButton("Delete");
		btn_delConfig.setEnabled(false);

		// Button actions
		final ActionListener updateBtnListener = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int w = Integer.parseInt(tField_fieldW.getText());
				int h = Integer.parseInt(tField_fieldH.getText());
				
				surface.setPreferredSize(new Dimension(w, h));
				setOptions(true);
				if(tField_sns1Cnt.getText().equals("")) 
					tField_sns1Cnt.setText("50");
				if(tField_sns1Rng.getText().equals("")) 
					tField_sns1Rng.setText("20");
				if(tField_cmn1Rng.getText().equals("")) 
					tField_cmn1Rng.setText("40");
				if(tField_sns2Cnt.getText().equals("")) 
					tField_sns2Cnt.setText("0");
				if(tField_sns2Rng.getText().equals("")) 
					tField_sns2Rng.setText("0");
				if(tField_cmn2Rng.getText().equals("")) 
					tField_cmn2Rng.setText("0");
				if(tField_fieldW.getText().equals("")) 
					tField_fieldW.setText("450");
				if(tField_fieldH.getText().equals("")) 
					tField_fieldH.setText("480");
				if(tField_itrCnt.getText().equals("")) 
					tField_itrCnt.setText("20");
				if(tField_dtcThr.getText().equals(""))
					tField_dtcThr.setText("3");
				btn_startSimAnim.setEnabled(true);
				btn_startSim.setEnabled(true);
				btn_stopSim.setEnabled(false);
				btn_loadConfig.setEnabled(true);
				btn_updateSim.setEnabled(true);
				btn_updateSns.setEnabled(true);
				if(loaded >= 0)
					btn_delConfig.setEnabled(true);
				surface.run.updateSettings();
				looper.run.updateSettings();
			}
		};
		
		btn_startSimAnim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearLog();
				surface.iterations = 0;
				pnl_surface.setViewportView(surface);
				
				updateBtnListener.actionPerformed(e);
				btn_startSimAnim.setEnabled(false);
				btn_startSim.setEnabled(false);
				btn_stopSim.setEnabled(true);
				btn_loadConfig.setEnabled(false);
				// btnUpdate.setEnabled(false);
				btn_updateSim.setEnabled(false);
				btn_updateSns.setEnabled(false);
				btn_delConfig.setEnabled(false);
				setOptions(false);
			}
		});
		
		btn_startSim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearLog();
				// scrollPane.remove(surface);
				btn_startSimAnim.setEnabled(false);
				btn_startSim.setEnabled(false);
				btn_stopSim.setEnabled(true);
				btn_loadConfig.setEnabled(false);
				// btnUpdate.setEnabled(false);
				btn_updateSim.setEnabled(false);
				btn_updateSns.setEnabled(false);
				btn_delConfig.setEnabled(false);
				setOptions(false);
				looper.run();
		
				updateBtnListener.actionPerformed(e);
			}
		});

		btn_stopSim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				surface.iterations = getIterationsNum();
				looper.iterations = getIterationsNum();
				pnl_surface.remove(surface);
				updateBtnListener.actionPerformed(e);
			}
		});
		
		btn_updateSim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				surface.run.saveConfig();
				saving = updateConfigBox() + 1;
				surface.iterations = getIterationsNum()+5;
			}
		});
		btn_updateSim.addActionListener(updateBtnListener);
		
		btn_updateSns.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				surface.iterations = getIterationsNum()+5;
				surface.run.resetSurface();
				looper.run.resetSurface();
				surface.run.saveConfig();
				saving = updateConfigBox();
				looper.run.getConfig(saving++);
			}
		});
		btn_updateSns.addActionListener(updateBtnListener);
		
		cBox_drawComRng.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				surface.drawComRng = cBox_drawComRng.isSelected();
			}
		});
		
		cBox_drawComLines.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				surface.drawComLines = cBox_drawComLines.isSelected();
			}
		});
		
		sldr_drawOpacity.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				float p = sldr_drawOpacity.getValue() / (float)100;
				surface.drawSnsOpacity = (int)(p * 255);
			}
		});
		
		// UNEDITED: REQUIRES CHANGING WHEN THE DATABASE IS REMOVED.
		btn_loadConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = (String)choice_configs.getSelectedItem();
				int id = Integer.parseInt(s);
				loaded = id;
				String[] opt = surface.run.getConfig(id);
				
				tField_snsCnt.setText(opt[0]);
				tField_snsRng.setText(opt[1]);
				tField_cmnRng.setText(opt[3]);
				tField_fieldW.setText(opt[6]);
				tField_fieldH.setText(opt[5]);
				tField_itrCnt.setText(opt[7]);
				//textField_7.setText(opt[8]);
				choice_itrAlg.select(opt[2]);
				choice_dtcTyp.select(opt[4]);
				tField_dtcThr.setText(opt[8]);
				
				surface.iterations = getIterationsNum()+5;
				updateBtnListener.actionPerformed(e);
			}
		});
		
		btn_saveConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		btn_delConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(loaded >= 0)
				{
					surface.run.deleteConfig(loaded);
					updateConfigBox();
					btn_startSimAnim.setEnabled(false);
					btn_startSim.setEnabled(false);
				}
				loaded = -1;
			}
		});
		
		// Add things to options panel
		pnl_sim_opt.add(btn_startSim,     "2, 2, 3, 1");
		pnl_sim_opt.add(btn_startSimAnim, "2, 4, 3, 1");
		pnl_sim_opt.add(btn_stopSim,      "2, 6, 3, 1");
		
		pnl_sim_opt.add(separator1, "2, 8, 3, 1");
		
		pnl_sim_opt.add(tPnl_updSim, "2, 10, 3, 1, fill, fill");

		tPnl_updSim.add(lbl_itrCnt,    "2, 2, fill, default");	
		tPnl_updSim.add(tField_itrCnt, "4, 2, fill, default");
		tPnl_updSim.add(lbl_sns1Rng,    "2, 4, fill, default");		
		tPnl_updSim.add(tField_sns1Rng, "4, 4, fill, default");
		tPnl_updSim.add(lbl_cmn1Rng,    "2, 6, fill, default");
		tPnl_updSim.add(tField_cmn1Rng, "4, 6, fill, default");
		tPnl_updSim.add(lbl_sns2Rng,    "2, 8, fill, default");		
		tPnl_updSim.add(tField_sns2Rng, "4, 8, fill, default");
		tPnl_updSim.add(lbl_cmn2Rng,    "2, 10, fill, default");
		tPnl_updSim.add(tField_cmn2Rng, "4, 10, fill, default");
		tPnl_updSim.add(lbl_itrAlg,    "2, 12, fill, default");
		tPnl_updSim.add(choice_itrAlg, "4, 12, fill, default");
		tPnl_updSim.add(lbl_algPar,    "2, 14, fill, default");
		tPnl_updSim.add(tField_algPar, "4, 14, fill, default");
		tPnl_updSim.add(lbl_dtcTyp,    "2, 16, fill, default");		
		tPnl_updSim.add(choice_dtcTyp, "4, 16, fill, default");
		tPnl_updSim.add(lbl_dtcThr,    "2, 18, fill, default"); 
		tPnl_updSim.add(tField_dtcThr, "4, 18, fill, default");
		tPnl_updSim.add(btn_updateSim, "2, 20, 3, 1");
		
		pnl_sim_opt.add(tPnl_updSns, "2, 12, 3, 1, fill, fill");
		
		tPnl_updSns.add(lbl_sns1Cnt,    "2, 2, fill, default");		
		tPnl_updSns.add(tField_sns1Cnt, "4, 2, fill, default");
		tPnl_updSns.add(lbl_sns2Cnt,    "2, 4, fill, default");		
		tPnl_updSns.add(tField_sns2Cnt, "4, 4, fill, default");
		tPnl_updSns.add(lbl_fieldW,    "2, 6, fill, default");		
		tPnl_updSns.add(tField_fieldW, "4, 6, fill, default");
		tPnl_updSns.add(lbl_fieldH,    "2, 8, fill, default");		
		tPnl_updSns.add(tField_fieldH, "4, 8, fill, default");
		tPnl_updSns.add(btn_updateSns, "2, 10, 3, 1");
		
		pnl_sim_opt.add(tPnl_display, "2, 14, 3, 1, fill, fill");
		
		tPnl_display.add(cBox_drawComRng,   "2, 2, 3, 1");
		tPnl_display.add(cBox_drawComLines, "2, 4, 3, 1");
		tPnl_display.add(lbl_drawOpacity,   "2, 6, fill, default");
		tPnl_display.add(sldr_drawOpacity,  "4, 6, fill, default");
		
		pnl_sim_opt.add(separator2, "2, 16, 3, 1");
		
		pnl_sim_opt.add(btn_loadConfig, "2, 18");							
		pnl_sim_opt.add(choice_configs, "4, 18, fill, default");
		pnl_sim_opt.add(btn_saveConfig, "2, 20, 3, 1");
		pnl_sim_opt.add(btn_delConfig,  "2, 22, 3, 1");
		
		// Sim display area
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.9);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		pnl_sim.add(splitPane);
		
		pnl_surface = new JScrollPane();
		splitPane.setLeftComponent(pnl_surface);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		splitPane.setRightComponent(scrollPane_1);
		
		tArea_log = new JTextArea();
		tArea_log.setEditable(false);
		scrollPane_1.setViewportView(tArea_log);
		
		
		// Graphing tab
		JPanel pnl_graph = new JPanel();
		pnl_graph.setLayout(new BorderLayout(0, 0));
		
		tabbedPane.addTab("Graph", null, pnl_graph, null);
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event)
			{
				JTabbedPane sourceTabbedPane = (JTabbedPane)event.getSource();
				int index = sourceTabbedPane.getSelectedIndex();
				if (index == 1)
					updateGraphTextArea();
			}
		});
		
		JPanel pnl_graph_opt = new JPanel();
		pnl_graph_opt.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		pnl_graph_opt.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("30dlu:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("30dlu:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("95dlu"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		pnl_graph.add(pnl_graph_opt, BorderLayout.WEST);
		
		tArea_settings = new JTextArea();
		tArea_settings.setBackground(SystemColor.control);
		tArea_settings.setEditable(false);
		tArea_settings.setFont(new Font("monospaced", Font.PLAIN, 12));
		pnl_graph_opt.add(tArea_settings, "2, 2, 5, 1, fill, fill");
		
		JSeparator separator3 = new JSeparator();
		pnl_graph_opt.add(separator3, "2, 4, 5, 1");
		
		JLabel lbl_metric = new JLabel("Metric:");
		pnl_graph_opt.add(lbl_metric, "2, 6, right, default");
		
		choice_metric = new Choice();
		choice_metric.addItem("Sensor Count");
		choice_metric.addItem("Detection Range");
		choice_metric.addItem("Communication Range");
		pnl_graph_opt.add(choice_metric, "4, 6, 3, 1, fill, default");
		
		JLabel lbl_graphRng1 = new JLabel("Range:");
		pnl_graph_opt.add(lbl_graphRng1, "2, 8, right, default");
		
		tField_graphRng = new JFormattedTextField(intFormat);
		tField_graphRng.setColumns(5);
		tField_graphRng.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				checkGraphButton();
			}
		});
		pnl_graph_opt.add(tField_graphRng, "4, 8, fill, default");
		
		tField_graphRng2 = new JFormattedTextField(intFormat);
		tField_graphRng2.setColumns(5);
		tField_graphRng2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				checkGraphButton();
			}
		});
		pnl_graph_opt.add(tField_graphRng2, "6, 8, fill, default");
		
		JLabel lbl_graphInc = new JLabel("Increment:");
		pnl_graph_opt.add(lbl_graphInc, "2, 10, right, default");
		
		tField_graphInc = new JFormattedTextField(intFormat);
		tField_graphInc.setColumns(5);
		tField_graphInc.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				checkGraphButton();
			}
		});
		pnl_graph_opt.add(tField_graphInc, "4, 10, 3, 1, fill, default");
		
		JLabel lbl_yAxis = new JLabel("Y-Axis:");
		pnl_graph_opt.add(lbl_yAxis, "2, 12, right, default");
		
		choice_yAxis = new Choice();
		choice_yAxis.addItem("Sensor Success Rate");
		choice_yAxis.addItem("Intruder Success Rate");
		pnl_graph_opt.add(choice_yAxis, "4, 12, 3, 1, fill, default");
		
		final JPanel pnl_graph_draw = new JPanel();
		pnl_graph_draw.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		pnl_graph_draw.setLayout(new BorderLayout(0, 0));
		pnl_graph.add(pnl_graph_draw, BorderLayout.CENTER);
		
		btn_graph = new JButton("Graph");
		btn_graph.setEnabled(false);
		btn_graph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String metric = choice_metric.getSelectedItem();
				int r1 = Integer.parseInt( formatNumberString(tField_graphRng.getText()) );
				int r2 = Integer.parseInt( formatNumberString(tField_graphRng2.getText()) );
				int inc = Integer.parseInt( formatNumberString(tField_graphInc.getText()) );
				String yAxis = choice_yAxis.getSelectedItem();
				
				// String s = (String)comboBox.getSelectedItem();
				// int id = Integer.parseInt(s);
				int temp = saving;

				if (graphSurface != null)
				{
					pnl_graph_draw.remove(graphSurface);
				}
				
				if(looper.graphData(metric, r1, r2, inc))
				{
					// System.out.println("FARTS");
					graphSurface = new GraphSurface(temp+1, r1, r2, inc, metric, yAxis);
					pnl_graph_draw.add(graphSurface);
					pnl_graph_draw.revalidate();
				}
				// else
					// System.out.println("NOT FARTS");

			}
		});
		pnl_graph_opt.add(btn_graph, "2, 14, 5, 1");
		
		JButton btnRunTestPlan_1 = new JButton("Run Test W/out Trial Save");
//		btnRunTestPlan_1 = new JButton("Run Test Plan w/out Trial Save");
		btnRunTestPlan_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				int trial;
				int trialsTotal = 0;
				looper.run.mouseAlgorithmType = getAlgorithmType();
				looper.run.detectionAlgorithm = getDetectionType();
				looper.run.detectionThreshold = 1;
				looper.run.h = 500;
				looper.run.w = 500;
				looper.run.createSuccessInfoTable();
				int times = 0;
				for(int sensorNum = 30; sensorNum <= 100; sensorNum+=10)
				{
					looper.run.sensorCount = sensorNum;
					for(int sensR = 5; sensR <= 50; sensR+=5)
					{
						looper.run.sensingRange = sensR;
						for(int commR = 60; commR <= 100; commR+=10)
						{
							looper.run.communicationRange = commR;
							for(int intSenR = 30; intSenR <= 70; intSenR+=10)
							{
								looper.run.intruderSensingRange = intSenR;
								trial = 0;
								trialsTotal = 0;
//								looper.run.saveConfig();
								looper.run.resetSurface();
								while(trial+1 <= 250)
								{
//									Mouse mouse = looper.run.mouse;
									int caught = looper.run.doSimulationIteration();
									if(caught == -1 && !looper.run.mouseSuccess() && caught != 0 && times++ < 5000)
									{
										// this only happens when the mouse isn't caught or succeeding
									}
									else
									{
//										if(times > 2000)
//										{
////											looper.run.maxIterations = 30;
////											looper.run.saveConfig();
////											loaded = 1;
////											looper.run.saveTrial(1);
////											System.out.println(times);
////											System.exit(0);
//											System.out.println("Balls");
//										}
//										System.out.println("Save Trail: "+temp+"; Sensors: "+sensorNum+"; SensingRange: "+sensR+"; CommRange: "+commR+"; IntruderSensing: "+(int)looper.run.intruderSensingRange);
										// System.out.println(run.mouse.getX());
//											looper.run.saveTrial(trial++, temp, mouse);
										trialsTotal += looper.run.mouseSuccess() ? 1 : 0;
										trial++;
										looper.run.resetSurface();
//										System.out.println(times);
										times = 0;
									}
								}
								looper.run.insertIntoSuccessInfoTable((double)trialsTotal/(double)100);
							}
						}
						System.out.println("Sensing Range: "+sensR+"; Heap Size: " + Runtime.getRuntime().totalMemory());
					}
					System.out.println("Done with "+sensorNum);
				}
				System.out.println((char)7);
//				looper.run.formatResults();
			}
		});
		pnl_graph_opt.add(btnRunTestPlan_1, "2, 16, 5, 1");
	}
	
	public int updateConfigBox()
	{
		String[] values = surface.run.getConfigIds();
		for (String id : values)
			choice_configs.addItem(id);
		return values.length;
	}
	
	public void updateGraphTextArea()
	{
		int width = 35;
		String str = "";
		
		String[] labels = new String[] {
			"Number of Trials",
			"T1 Sensing Range",
			"T1 Communication Range",
			"T2 Sensing Range",
			"T2 Communication Range",
			"Intrusion Algorithm",
			"Detection Type",
			"Detection Threshold",
			"T1 Sensor Count",
			"T2 Sensor Count",
			"Field Width",
			"Field Height"
		};
		
		String[] values = new String[] {
			tField_itrCnt.getText(),
			tField_sns1Rng.getText(),
			tField_cmn1Rng.getText(),
			tField_sns2Rng.getText(),
			tField_cmn2Rng.getText(),
			choice_itrAlg.getSelectedItem(),
			choice_dtcTyp.getSelectedItem(),
			tField_dtcThr.getText(),
			tField_sns1Cnt.getText(),
			tField_sns2Cnt.getText(),
			tField_fieldW.getText(),
			tField_fieldH.getText()
		};
		
		for (int i=0; i<labels.length; i++)
		{
			str += labels[i];
			int len = labels[i].length();
			int len2 = values[i].length();
			for (int j=width-len; j>len2; j--)
				str += '.';
			str += values[i] + '\n';
		}
		
		tArea_settings.setText(str);
	}
	
	public void printToLog(String line)
	{
		tArea_log.append(line);
	}
	
	public void clearLog()
	{
		tArea_log.setText("");
	}

	public void setOptions(boolean enabled)
	{
		tField_sns1Cnt.setEnabled(enabled);
		tField_sns1Rng.setEnabled(enabled);
		tField_cmn1Rng.setEnabled(enabled);
		tField_sns2Cnt.setEnabled(enabled);
		tField_sns2Rng.setEnabled(enabled);
		tField_cmn2Rng.setEnabled(enabled);
		tField_fieldW.setEnabled(enabled);
		tField_fieldH.setEnabled(enabled);
		tField_itrCnt.setEnabled(enabled);
		tField_dtcThr.setEnabled(enabled);
		tField_algPar.setEnabled(enabled);
		choice_itrAlg.setEnabled(enabled);
		choice_dtcTyp.setEnabled(enabled);
	}
	
	public void resetButtons()
	{
		btn_startSim.setEnabled(true);
		btn_startSimAnim.setEnabled(true);
		btn_stopSim.setEnabled(false);
		btn_loadConfig.setEnabled(true);
		btn_updateSim.setEnabled(true);
		btn_updateSns.setEnabled(true);
	}
	
	public void checkGraphButton()
	{
		String r1Text = tField_graphRng.getText();
		String r2Text = tField_graphRng2.getText();
		String incText = tField_graphInc.getText();
		
		if (r1Text.isEmpty() || r2Text.isEmpty() || incText.isEmpty())
			btn_graph.setEnabled(false);
		else
			btn_graph.setEnabled(true);
	}
	
	// Returns a parsed integer for the Type 1 sensor count
	public int getT1SensorCount() { return Integer.parseInt( formatNumberString(tField_sns1Cnt.getText()) ); }
	
	// Returns a parsed float for the Type 1 sensor sensing range
	public int getT1SensingRange() { return Integer.parseInt( formatNumberString(tField_sns1Rng.getText()) ); }
	
	// Returns a parsed float for the Type 1 sensor communication range
	public int getT1CommunicationRange() { return Integer.parseInt( formatNumberString(tField_cmn1Rng.getText()) ); }
	
	// Returns a parsed integer for the Type 2 sensor count
	public int getT2SensorCount() { return Integer.parseInt( formatNumberString(tField_sns2Cnt.getText()) ); }
	
	// Returns a parsed float for the Type 2 sensor sensing range
	public int getT2SensingRange() { return Integer.parseInt( formatNumberString(tField_sns2Rng.getText()) ); }
	
	// Returns a parsed float for the Type 2 sensor communication range
	public int getT2CommunicationRange() { return Integer.parseInt( formatNumberString(tField_cmn2Rng.getText()) ); }

	// Returns a parsed int for the detection threshold
	public int getDetectionThreshold() { return Integer.parseInt( formatNumberString(tField_dtcThr.getText()) ); }
	
	// Returns a parsed int for the field width
	public int getFieldWidth() { return Integer.parseInt( formatNumberString(tField_fieldW.getText()) ); }
	
	// Returns a parsed int for the field height
	public int getFieldHeight() { return Integer.parseInt( formatNumberString(tField_fieldH.getText()) ); }

	// Returns a parsed int for the number of iterations to be ran
	public int getIterationsNum() { return Integer.parseInt( formatNumberString(tField_itrCnt.getText()) ); }
	
	// Returns the type of mouse movement algorithm
	public String getAlgorithmType() { return choice_itrAlg.getSelectedItem(); }

	// Returns the type of sensor detection
	public String getDetectionType() { return choice_dtcTyp.getSelectedItem(); }

	public double[] getAlgorithmParameters()
	{
		String[] strValues = tField_algPar.getText().split(";");
		double[] values = new double[strValues.length];
		for (int i=0; i<strValues.length; i++)
		{
			if (!strValues[i].isEmpty())
			{
				values[i] = Float.parseFloat(strValues[i]);
			}
		}
		
		return values;
	}
	
	private String formatNumberString(String str)
	{
		String ret = "";
		for(int i = 0; i < str.length(); i++)
		{
			if(str.charAt(i) != ',' && str.charAt(i) != '-')
				ret += str.charAt(i);
		}
		return ret;
	}
}
