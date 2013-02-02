import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;




public class ManageScoresPanel {
	
	/*Manage Scores Panel Attributes*/
	private JPanel manageScoresPanel; 					//Panel used to display weekly scores of players
	private JPanel manageScoresEditPanel; 				//Panel used to edit any weekly scores in the table
	private JTable manageScoresTable; 					//Table used to display scores of players
	private JScrollPane manageScoresScrollPane; 		//Scroll bar for table holding scores
	private static Object[] manageScoresColumns; 		//Object for storing the Column Names
	private static Object[][] scoresTabledata; 			//Object for storing the Data
	private static DefaultTableModel scoringDM;			//players Default Table Model
	private static JButton updateScoringTable; 			//JButton for updating the Scores Table
	
	/*Create Instance of Players Object*/
	private Players players;
	private GameSettings gameSettings;
	private Scoring scoring;
	
	/*Has table been created?*/
	private boolean tableInitated;
	
	/**
	 * createScoringTable Method - Method for creating & updating the Manage Scores JTable.
	 * @param createFromFile
	 */
	public void createScoringTable(String createFromFile) {
		
		gameSettings.update();
		//System.out.println(gameSettings.getNumWeeks()+"Asdf");
		String[] headerArray= new String[1+gameSettings.getNumWeeks()];
		headerArray[0]="User ID";
		for (int i=1;i<headerArray.length;i++){
			headerArray[i]="Week "+String.valueOf(i);
		}
		manageScoresColumns=headerArray;
		/*
		manageScoresColumns = new Object[]{"User ID",
				"Week 1",
				"Week 2",
				"Week 3",
				"Week 4",
				};
		*/
		
		

		/*Conditional used to control is table is created or updated*/
		if (createFromFile.equalsIgnoreCase("y")){
			
			/*Create Contestants & GameSettings Object*/
			scoring = new Scoring();
			
		} else{
			scoring = new Scoring();
		}
		
		//Prepare the data for the Table
		scoresTabledata = scoring.prepScoreTable();
		

		/*Conditional Statement which updates the JTable*/
		if (createFromFile.equalsIgnoreCase("y")){
			//use "prepforTable"
			scoringDM = new DefaultTableModel(){
			    @Override
			    public boolean isCellEditable(int row, int column) {
			       //all cells false
			       return false;
			    }
			};
			scoringDM.setDataVector(scoresTabledata, manageScoresColumns);
						
			manageScoresTable = new JTable(scoringDM) {
				public void tableChanged(TableModelEvent e) {
					super.tableChanged(e);
				    repaint();
				}};
			
			/*Set Column Widths*/
			//manageScoresTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			TableColumn column = null;
			/*
			for(int i = 1; i < 5; i++){
				column = manageScoresTable.getColumnModel().getColumn(i);
				column.setPreferredWidth(120);
			}
			*/
			/*Adding Table to Scroll Pane and setting Location*/
			manageScoresScrollPane = new JScrollPane(manageScoresTable);
			manageScoresTable.setFillsViewportHeight(true);
			manageScoresPanel.add(manageScoresScrollPane);
			manageScoresScrollPane.setBounds(10, 10, 810, 210);
			
			/*Create Sorting Functionality*/
			manageScoresTable.setAutoCreateRowSorter(true);
			manageScoresTable.getTableHeader().setReorderingAllowed(false);
			
		}else{
			scoringDM.setDataVector(scoresTabledata, manageScoresColumns);
			manageScoresTable = new JTable(scoringDM);
		}
		
		
		
		
		

		

	}
	
	/*Begin Action Listeners*/
	/**
	 * UpdateScoreTable Class - Contains Method for updating the manageScoresTable.
	 *
	 */
	private class UpdateScoreTable implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			/*Recreate/Update the Table*/
			
			if (tableInitated==true){
				createScoringTable("no");
			}else{
				tableInitated=true;
				createScoringTable("y");
			}
		
		}
	}
	/*End Action Listeners*/
	
	
	/**
	 * Constructor for panel managing the scores
	 */
	public ManageScoresPanel() {
		
		gameSettings=new GameSettings();
		Color custom = new Color(225,225,225);
		
		/*List Bonus Questions and Answers Content Pane Code*/
		manageScoresPanel = new TransparentPanel();
		manageScoresPanel.setLayout(null);
		manageScoresPanel.setBounds(220, 60, 830, 470);
		manageScoresPanel.setBackground(custom);
		manageScoresPanel.setVisible(false);
				
		if (gameSettings.hasGameStarted()==true){
			
			//System.out.println(gameSettings.getCurrentRound()+"--------");
			tableInitated=true;
			createScoringTable("y");
		}else{
			tableInitated=false;

		}
				
		updateScoringTable = new JButton("Update Scoring Table");
		manageScoresPanel.add(updateScoringTable);
		updateScoringTable.setBounds(250, 350, 330, 20);
		updateScoringTable.addActionListener(new UpdateScoreTable());
		
		//Custom Panel Layout Code
		manageScoresEditPanel = new JPanel();
		manageScoresEditPanel.setLayout(null);
		manageScoresPanel.add(manageScoresEditPanel);
		manageScoresEditPanel.setBounds(10, 340, 690, 120);
		manageScoresEditPanel.setBackground(Color.WHITE);
		manageScoresEditPanel.setVisible(false);
	}

	/**
	 * Method that returns the manage scores panel
	 * @return - the panel that holds the players scores info
	 */
	public JPanel getPanel() {
		return manageScoresPanel;
	}
	
	/**
	 * disableButtons Method - Disables the Functionality Buttons.
	 */
	public void disableButtons(){
		updateScoringTable.setEnabled(false);
	}
}
