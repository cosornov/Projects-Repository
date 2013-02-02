import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

//Radio Button in Swing Table
class RadioButtonRenderer implements TableCellRenderer {
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (value==null) return null;
		return (Component)value;
	}
}

public class EliminationPanel {

	/*Manage Eliminations Attributes*/
	private JPanel manageEliminationsPanel; 					//Panel used to manage elimination info of contestants
	private JPanel manageEliminationsEditPanel; 				//Panel used to edit any elimination info
	private static DefaultTableModel elimDM; 					//default
	private static Object[][] elimData; 						//2D Object storing elimination data
	private static Object[] elimHeader; 						//Object containing column headers
	private static JTable manageEliminationsTable; 				//Elimination info stored into a table
	private static JScrollPane manageEliminationsScrollPane; 	//Scroll pane for Elimination info
	private static JButton declareWeeklyElimButton; 			//Button to declare weekly eliminated
	private static JButton editWeeklyElimButton; 				//Button to edit elimination info
	private static JButton declareUltimateWinnerButton; 		//Button to declare ultimate survivor winner
	private static JButton editUltimateWinnerButton; 			//Button to edit ultimate survivor winner
	private static JLabel contestantEliminatedLabel; 			//Contestant eliminated label
	private static JTextField contestantEliminatedField; 		//Contestant eliminated field
	private static JButton editUltimateWinner; 					//Button to finalize edit ultimate winner
	private static JButton editWeeklyElimination; 				//Button to finalize editing weekly eliminated
	private static JButton refreshTable;						//Button for refreshing the table

	private static JLabel contestantFNameLabel; 				//Contestant first name label
	private static JLabel contestantLNameLabel; 				//Contestant last name label
	private static JTextField contestantFNameField; 			//Editable contestant fname field
	private static JTextField contestantLNameField; 			//Editable contestant lname field
	private static JLabel contestantElimWeekLabel; 				//Contestant week eliminated label
	private static JTextField contestantElimWeekField; 			//Editable contestant elimination week field
	private static JLabel contestantUIDLabel; 					//Contestant user ID label
	private static JTextField contestantUIDTextArea; 			//User ID field
	private String listPlayerOptionEntry; 						//String storing ok or cancel after prompting 
	private boolean ultimateWinnerSet;							//For checking if the Ultimate Winner is set
	
	/*Initialize Contestants, GameSettings & Elimination Objects*/
	public Contestants contestants;
	public GameSettings gameSettings;
	public ContestantsPanel contestantsPanel;

	/*Regex for players names - only letters, case insensitive*/
	private Pattern LETTERSONLY = Pattern.compile("[a-zA-Z]+");
	private Pattern ALPHANUMERIC = Pattern.compile("[A-Za-z0-9]+");
	private Pattern NUMBER = Pattern.compile("[0-9-]+");
	
	/**
	 * Method to check for invalid input
	 **/
	private boolean areLetters(String s) {
		Matcher m = LETTERSONLY.matcher(s);
		return m.matches();
	}
	
	/**
	 * checkAlphaNumeric Method - Private method to check for alphanumeric input.
	 * @param s
	 * @return m.matches()
	 */
	private boolean checkAlphaNumeric(String s)
	{
		Matcher m = ALPHANUMERIC.matcher(s);
		return m.matches();
	}
	
	/**
	 * checkNumber method - Method for checking for the input of numbers only.
	 * @param s
	 * @return m.matches()
	 */
	private boolean checkNumber(String s)
	{
		Matcher m = NUMBER.matcher(s);
		return m.matches();
	}
	
	/*Action Methods*/
	/**
	 * createBonusQATable Method - Method for creating & updating the BonusQA JTable.
	 * @param createFromFile
	 */
	public void createElimTable(String createFromFile) {
		/*Column titles for elimination Table*/
		elimHeader = new Object[]{"Unique ID",
				"First Name",
				"Last Name",
				"Eliminated",
				"Elimination Week",
				"Ultimate"};
		
		
		/*Conditional used to control is table is created or updated*/
		if (createFromFile.equalsIgnoreCase("y")){
			
			/*Create Contestants & GameSettings Object*/
			contestants = new Contestants();
			
			/*Restore Contestants Data from Text File*/
			contestants.resetFromFile(gameSettings.filename_contestants);
		} else{
			/*Restore Contestants Data from Text File*/
			contestants.resetFromFile(gameSettings.filename_contestants);
		}
		
		//NEED TO MAKE ONE
		elimData = contestants.prepforElimTable();
		

		/*Conditional Statement which updates the JTable*/
		if (createFromFile.equalsIgnoreCase("y")){
			//use "prepforTable"
			elimDM = new DefaultTableModel(){
			    @Override
			    public boolean isCellEditable(int row, int column) {
			       //all cells false
			       return false;
			    }
			};
			elimDM.setDataVector(elimData, elimHeader);
						
			manageEliminationsTable = new JTable(elimDM) {
				public void tableChanged(TableModelEvent e) {
					super.tableChanged(e);
				    repaint();
				}};
			
			/*Set Column Widths*/
			TableColumn column = null;
			for(int i = 0; i < 6; i++){
				column = manageEliminationsTable.getColumnModel().getColumn(i);
				if(i==0 || i==3){column.setPreferredWidth(50);}
			}

			/*Adding Table to Scroll Pane and setting Location*/
			manageEliminationsScrollPane = new JScrollPane(manageEliminationsTable);
			manageEliminationsTable.setFillsViewportHeight(true);
			manageEliminationsPanel.add(manageEliminationsScrollPane);
			manageEliminationsScrollPane.setBounds(10, 10, 600, 210);
			
			/*Create Sorting Functionality*/
			manageEliminationsTable.setAutoCreateRowSorter(true);
			manageEliminationsTable.getTableHeader().setReorderingAllowed(false);
			
		}else{
			elimDM.setDataVector(elimData, elimHeader);
			manageEliminationsTable = new JTable(elimDM);
		}
	}
	
	
	/**
	 * DeclareUltimateWinner - contains method enabling admin to declare ultimate winner
	 */
	private class DeclareUltimateWinner implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			/*Redraw/Update Contestants Table*/
			createElimTable("no");
			
			manageEliminationsEditPanel.setVisible(false);
			ultimateWinnerSet = false;

			if (contestants.getNumContestants()>0){
				/*Prompts for Unique ID of the Contestant*/
				listPlayerOptionEntry = JOptionPane
						.showInputDialog("Enter the Unique ID of the Contestant to be the Ultimate Winner:");
				
				if (listPlayerOptionEntry!=null){
					int index=contestants.existsContestant_byUserID(listPlayerOptionEntry);
					if (index!=-1){
						manageEliminationsEditPanel.setVisible(false);
						/*
						contestantElimWeekLabel.setVisible(true);
						contestantElimWeekField.setVisible(true);
						contestantElimWeekField.setEditable(false);
						*/
						
						/*Get's the Contestant's Data and stores it temporarily*/
						Contestant tmpCntst = contestants.getContestantint(index);
						
						/*Edit Contestant's Elimination Week and Eliminated status in table*/
						tmpCntst.setRoundEliminated(gameSettings.getNumTotalRounds());
						contestants.deleteContestant_byUserID(tmpCntst.getID());
						contestants.addContestant(tmpCntst,"n");
						contestants.toFile(gameSettings.filename_contestants);
						
						
						/*Set Contestant to be Ultimate Winner*/
						gameSettings.setUlimateWinner(tmpCntst.getID());
						
						/*Update File*/
						gameSettings.toFile(gameSettings.filename_settings);
						
						/*Update Table*/
						createElimTable("no");
						
						/*Set Ultimate Winner Checker and Deactivate Button*/
						ultimateWinnerSet = true;
						declareUltimateWinnerButton.setEnabled(false);
					}
					else if (listPlayerOptionEntry.isEmpty()==true){
						/*Show Error Popup*/
						JOptionPane.showMessageDialog(null, 
								"You must enter a valid user id", "Error: Invalid UserID", JOptionPane.ERROR_MESSAGE);
					}
					else{
						/*Show Error Popup*/
						JOptionPane.showMessageDialog(null, 
								"No User with that UserID exists.", "Error: Invalid UserID", JOptionPane.ERROR_MESSAGE);
					}
				}
			}else{
				/*Show Error Popup*/
				JOptionPane.showMessageDialog(null, "There are no Contestants!", "Error: 0 Contestants!", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * EditUltimateWinner - contains method for editing the ultimate winner
	 */
	private class EditUltimateWinner implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			gameSettings.update();
			/*Redraw/Update Contestants Table*/
			createElimTable("no");

			manageEliminationsEditPanel.setVisible(false);

			if (contestants.getNumContestants()>0 ){
				/*Prompts for Unique ID of the Contestant*/
				listPlayerOptionEntry = JOptionPane
						.showInputDialog("Enter the Unique ID of the Contestant " +
								"who is set to be the Current Ultimate Winner:");
				
				if (listPlayerOptionEntry!=null){
					/*Find User with specified ID*/
					int index=contestants.existsContestant_byUserID(listPlayerOptionEntry);
					if ((index!=-1)){
						
						/*Get's the Current Contestant's Data and stores it temporarily*/
						Contestant tmpCntst = contestants.getContestantint(index);
						
				
						if ((!tmpCntst.getID().equals(gameSettings.getUltimateWinner()))){
							JOptionPane.showMessageDialog(null, 
									"This UserID is not the UltimateWinner.", "Error: Not Correct UserID", JOptionPane.ERROR_MESSAGE);

							return;
						}
						/*Prompt for New Ultimate Winner*/
						listPlayerOptionEntry = JOptionPane
								.showInputDialog("Enter the Unique ID of the Contestant " +
										"to be the New Ultimate Winner:");
						
						/*Appropriate Checks and Set New Ultimate Winner*/
						if (listPlayerOptionEntry!=null){
							/*Find User with specified ID*/
							int indextwo = contestants.existsContestant_byUserID(listPlayerOptionEntry);
						
							if (indextwo !=-1){
								
								
								tmpCntst.setRoundEliminated(-1);
								contestants.deleteContestant_byUserID(tmpCntst.getID());
								contestants.addContestant(tmpCntst, "n");
								
								contestants.toFile(gameSettings.filename_contestants);
								contestants.resetFromFile(gameSettings.filename_contestants);
								
								
								//cant set the ultimate winner to be someone who has been elimiated
								//resolve conflict
								Contestant tmpC=contestants.findContestant_byID(listPlayerOptionEntry);
								if (tmpC.getRoundEliminated()>0){
									contestants.deleteContestant_byUserID(tmpC.getID());
									tmpC.setRoundEliminated(gameSettings.getNumTotalRounds());
									System.out.println(tmpC.getRoundEliminated());
									contestants.addContestant(tmpC, "n");
						
									contestants.toFile(gameSettings.filename_contestants);
									contestants.resetFromFile(gameSettings.filename_contestants);
									
						
									
									JOptionPane.showMessageDialog(null, 
											tmpC.getID()+" was already inactive"
											+".  Conflict resolved - they are no longer inactive.  Thus, Now they are the ultimate winner!", 
											"Error: Ultimate Winner must become inactive on finalweek'", JOptionPane.ERROR_MESSAGE);
									
									createElimTable("no");					
								}else{
									contestants.deleteContestant_byUserID(tmpC.getID());
									tmpC.setRoundEliminated(gameSettings.getNumTotalRounds());
									contestants.addContestant(tmpC, "n");
						
									contestants.toFile(gameSettings.filename_contestants);
									contestants.resetFromFile(gameSettings.filename_contestants);
									
								}
								
			
								/*Set the current Ultimate Winner to be "Y"*/
								gameSettings.setUlimateWinner(listPlayerOptionEntry);
								
								/*Update File*/
								gameSettings.toFile(gameSettings.filename_settings);
								
								/*Update Table*/
								createElimTable("no");
							}
							
							/*Error Messages for NEW ULTIMATE WINNER*/
							else if (listPlayerOptionEntry.isEmpty()==true){
								/*Show Error Popup*/
								JOptionPane.showMessageDialog(null, 
										"You must enter a valid user id.", "Error: Invalid UserID", JOptionPane.ERROR_MESSAGE);
							}
							else{
								/*Show Error Popup*/
								JOptionPane.showMessageDialog(null, 
										"No User with that UserID exists.", "Error: Invalid UserID", JOptionPane.ERROR_MESSAGE);
							}
						}
					}
					
					/*Error Messages for CURRENT ULTIMATE WINNER*/
					else if (listPlayerOptionEntry.isEmpty()==true){
						/*Show Error Popup*/
						JOptionPane.showMessageDialog(null, 
								"You must enter a valid user id.", "Error: Invalid UserID", JOptionPane.ERROR_MESSAGE);
					}
					else{
						/*Show Error Popup*/
						JOptionPane.showMessageDialog(null, 
								"No User with that UserID exists.", "Error: Invalid UserID", JOptionPane.ERROR_MESSAGE);
					}
				}
			}else{
				/*Show Error Popup*/
				JOptionPane.showMessageDialog(null, "There are no Contestants!", "Error: 0 Contestants", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * DeclareWeeklyEliminated - contains method to declare the weekly eliminated contestant
	 */
	private class DeclareWeeklyElimination implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			/*Redraw/Update Contestants Table*/

			manageEliminationsEditPanel.setVisible(false);

			if (contestants.getNumContestants()>0){
				/*Prompts for Unique ID of the Contestant*/
				
				listPlayerOptionEntry = JOptionPane
				.showInputDialog("Enter the Unique ID of the Contestant which you wish to Eliminate this week:");
				
				/*If Contestant/UID exists*/
				if (listPlayerOptionEntry!=null){
					int index=contestants.existsContestant_byUserID(listPlayerOptionEntry);
					if (index!=-1){
						
						//can't eliminate ultimate winner
						if (listPlayerOptionEntry.equals(gameSettings.getUltimateWinner())){
							JOptionPane.showMessageDialog(null, 
									"You can't Eliminate the Ultimate Winner!", "Error: Invalid UserID", JOptionPane.ERROR_MESSAGE);

							return;
						}
					
						
						manageEliminationsEditPanel.setVisible(true);
						
						/*Get's the Contestant's Data and stores it temporarily*/
						Contestant tmpCntst = contestants.getContestantint(index);
						
						/*Set the Contestant's Elimination week and Eliminated to be "Y"*/
						contestantUIDTextArea.setText(tmpCntst.getID());
						contestantFNameField.setText(tmpCntst.getFName());
						contestantLNameField.setText(tmpCntst.getLName());
						contestantEliminatedField.setText((tmpCntst.getRoundEliminated()!=-1)?"Y":"N");
						contestantElimWeekField.setText(Integer.toString(tmpCntst.getRoundEliminated()));
						
						/*Update File - necessary?*/	
						
						/*Update Table - necessary?*/
					}
					else if (listPlayerOptionEntry.isEmpty()==true){
						/*Show Error Popup*/
						JOptionPane.showMessageDialog(null, 
								"You must enter a valid UserID", "Error: Invalid UserID", JOptionPane.ERROR_MESSAGE);
					}
					else{
						/*Show Error Popup*/
						JOptionPane.showMessageDialog(null, 
								"No Contestant with that UserID exists.", "Error: Invalid UserID", JOptionPane.ERROR_MESSAGE);
					}
				}
			}else{
				/*Show Error Popup*/
				JOptionPane.showMessageDialog(null, "There are no Contestants!", "Error: 0 Contestants!", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 *EditWeeklyElimination - contains method for editing the weekly eliminated contestant 
	 */
	private class EditWeeklyElimination implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			/*Redraw/Update Contestants Table*/

			manageEliminationsEditPanel.setVisible(false);

			if (contestants.getNumContestants()>0){
				/*Prompts for Unique ID of the Contestant*/
				listPlayerOptionEntry = JOptionPane
						.showInputDialog("Enter the Unique ID of the Contestant who's Elimination" +
								"Records you wish to Edit:");
				
				if (listPlayerOptionEntry!=null){
					int index=contestants.existsContestant_byUserID(listPlayerOptionEntry);
					if (index!=-1){
						manageEliminationsEditPanel.setVisible(false);
						manageEliminationsEditPanel.setVisible(true);
						contestantElimWeekLabel.setVisible(true);
						contestantElimWeekField.setVisible(true);
						editWeeklyElimination.setVisible(true);
						
						/*Get's the Contestant's Data and stores it temporarily*/
						Contestant tmpCntst = contestants.getContestantint(index);
						
						/*Set the appropriate Fields*/
						contestantUIDTextArea.setText(tmpCntst.getID());
						contestantFNameField.setText(tmpCntst.getFName());
						contestantLNameField.setText(tmpCntst.getLName());
						contestantEliminatedField.setText((tmpCntst.getRoundEliminated()!=-1)?"Y":"N");
						contestantElimWeekField.setText(Integer.toString(tmpCntst.getRoundEliminated()));
					}
					/*Error Messages for PREVIOUSLY ELIMINATED CONTESTANT*/
					else if (listPlayerOptionEntry.isEmpty()==true){
						/*Show Error Popup*/
						JOptionPane.showMessageDialog(null, 
								"You must enter a valid user id", "Error: Invalid UserID", JOptionPane.ERROR_MESSAGE);
					}
					else{
						/*Show Error Popup*/
						JOptionPane.showMessageDialog(null, 
								"No User with that UserID exists.", "Error: Invalid UserID", JOptionPane.ERROR_MESSAGE);
					}
				}
			}else{
				/*Show Error Popup*/
				JOptionPane.showMessageDialog(null, "There are no Contestants!", "Error: 0 Contestants!", JOptionPane.ERROR_MESSAGE);
			}	
		}
	}
	
	
	private class CheckEditWeeklyElim implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			/*Field Limitation Checks*/
			//If the EliminationWeek is blank
			if(contestantElimWeekField.getText().equals("") || contestantElimWeekField.getText()==null){
				JOptionPane.showMessageDialog(null, "Please enter a Number Value for the Elimination Week."
						+ "Enter -1 to undo an Elimination, or a valid Elimination Week."
						, "Elimination Week Error!", JOptionPane.ERROR_MESSAGE);
			}
			//Check to see if the EliminationWeekValue is not empty, aNumber
			else if( (contestantElimWeekField.getText().equals("")) || !(checkNumber(contestantElimWeekField.getText())) ){
				JOptionPane.showMessageDialog(null, "Please enter a Number Value for the Elimination Week."
						+ "Enter -1 to undo an Elimination, or a valid Elimination Week."
						, "Elimination Week Error!", JOptionPane.ERROR_MESSAGE);
			}
			/*Check if EliminationWeekValue is a week within range - 
			 * week entered for declare weekly is within range for > 0 < getnumweeks(from gamesettings)*/
			else if ( (contestantElimWeekField.getText().equals(""))
					|| (Integer.parseInt(contestantElimWeekField.getText())==0)
					|| (Integer.parseInt(contestantElimWeekField.getText() )<=-2)
					|| ( !(Integer.parseInt(contestantElimWeekField.getText()) < gameSettings.getNumTotalRounds())) ){
				JOptionPane.showMessageDialog(null, "Please enter an Elimination Week within the range of the game.  Recall, that in the last round " +
						"you must just declare an Ultimate winner!"
						, "Elimination Week Error!", JOptionPane.ERROR_MESSAGE);
			}
			else{
			
				
		
				
				//can't eliminate ultimate winner
				if (contestantUIDTextArea.getText().equals(gameSettings.getUltimateWinner())){
					JOptionPane.showMessageDialog(null, 
							"You can't Eliminate the Ultimate Winner!", "Error: Invalid UserID", JOptionPane.ERROR_MESSAGE);

					return;
				}

				
				
				//making sure if anyone else is elimiated on that week..conflict is resolved
				Contestant tmpC1=contestants.isSomeoneElseAlreadyElimiated_week(
						Integer.parseInt(contestantElimWeekField.getText()),
						contestantUIDTextArea.getText());
				
				if (tmpC1!=null){
					tmpC1.setRoundEliminated(-1);
					contestants.deleteContestant_byUserID(tmpC1.getID());
					contestants.addContestant(tmpC1, "n");
					contestants.toFile(gameSettings.filename_contestants);

					JOptionPane.showMessageDialog(null, 
							tmpC1.getID()+" was already elimiated on week"+contestantElimWeekField.getText()
							+".  Conflict resolved - they are no longer elimiated", 
							"Error: Elimiation Week Conflict", JOptionPane.ERROR_MESSAGE);
					
					createElimTable("no");
					
				}
				
				
				
				/*Get Contestant Data and Populate Text Fields*/
				
				Contestant tmpCntst=contestants.findContestant_byID(contestantUIDTextArea.getText());
				tmpCntst.setRoundEliminated(Integer.parseInt(contestantElimWeekField.getText()));

				
				/*Edit Contestant's Elimination Week and Eliminated status in table*/
				contestants.deleteContestant_byUserID(contestantUIDTextArea.getText());
				contestants.addContestant(tmpCntst,"n");
				contestants.toFile(gameSettings.filename_contestants);
				
				/*Successfully edited*/
				JOptionPane.showMessageDialog(null, "Succesfully edited UserID: "
						+tmpCntst.getID(), "Contestant Elimination Data Set/Edited!", JOptionPane.DEFAULT_OPTION);
						
				/*Update GameSettings (and File)*/
				contestants.toFile(gameSettings.filename_contestants);
				
				/*Update the Contestants Table and Text File*/
				createElimTable("no");
			}
		} 
	}
	
	/*refresh the table*/
		private class RefreshTable implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				createElimTable("n");
			}
			
		}
	/*End Action Fields*/


	/**
	 * Constructor for the Elimination panel
	 */
	public EliminationPanel() {

		Color custom = new Color(225,225,225);

		/*Manage Eliminations Pane Code*/
		manageEliminationsPanel = new TransparentPanel();
		manageEliminationsPanel.setLayout(null);
		manageEliminationsPanel.setBounds(220, 60, 830, 470);
		manageEliminationsPanel.setBackground(custom);
		manageEliminationsPanel.setVisible(false);

		/*Initialize Objects and Create Table*/
		gameSettings = new GameSettings();
		contestantsPanel = new ContestantsPanel();
		createElimTable("y");
			
		
		
		editUltimateWinnerButton = new JButton("Change Ultimate Winner");
		editUltimateWinnerButton.addActionListener(new EditUltimateWinner());
		manageEliminationsPanel.add(editUltimateWinnerButton);
		editUltimateWinnerButton.setBounds(620, 50, 200, 20);
		
		declareUltimateWinnerButton = new JButton("Declare Ultimate Winner");
		declareUltimateWinnerButton.addActionListener(new DeclareUltimateWinner());			
		manageEliminationsPanel.add(declareUltimateWinnerButton);
		declareUltimateWinnerButton.setBounds(620, 10, 200, 20);

		//NEIL CHECK
				if(gameSettings.getCurrentRound()==gameSettings.getNumTotalRounds()){
					declareUltimateWinnerButton.setEnabled(true);
					editUltimateWinnerButton.setEnabled(true);
					
					if (gameSettings.getUltimateWinner().length()==2){
						declareUltimateWinnerButton.setEnabled(false);
					}
				}
				else{
					/*
					if (gameSettings.getUltimateWinner().length()==2){
						declareUltimateWinnerButton.setEnabled(false);
					}
					*/
					
					editUltimateWinnerButton.setEnabled(false);
					declareUltimateWinnerButton.setEnabled(false);
					
				}
		
		declareWeeklyElimButton = new JButton("Declare Weekly Elimination");
		declareWeeklyElimButton.addActionListener(new DeclareWeeklyElimination());
		manageEliminationsPanel.add(declareWeeklyElimButton);
		declareWeeklyElimButton.setBounds(620, 90, 200, 20);
	

		editWeeklyElimButton = new JButton("Edit Weekly Elimination");
		editWeeklyElimButton.addActionListener(new EditWeeklyElimination());
		manageEliminationsPanel.add(editWeeklyElimButton);
		editWeeklyElimButton.setBounds(620, 130, 200, 20);
		
		refreshTable = new JButton("Refresh Table");
		refreshTable.addActionListener(new RefreshTable());
		manageEliminationsPanel.add(refreshTable);
		refreshTable.setBounds(620, 170, 200, 20);

		//Custom Panel Layout Code
		manageEliminationsEditPanel = new JPanel();
		manageEliminationsEditPanel.setLayout(null);
		manageEliminationsPanel.add(manageEliminationsEditPanel);
		manageEliminationsEditPanel.setBounds(10, 240, 810, 220);
		manageEliminationsEditPanel.setBackground(Color.WHITE);

		contestantUIDLabel = new JLabel("Contestant Unique ID:");
		manageEliminationsEditPanel.add(contestantUIDLabel);
		contestantUIDLabel.setBounds(10, 10, 200, 20);
		contestantUIDTextArea = new RoundJTextField(10);
		manageEliminationsEditPanel.add(contestantUIDTextArea);
		contestantUIDTextArea.setBounds(10, 30, 200, 20);
		contestantUIDTextArea.setEditable(false);

		contestantFNameLabel = new JLabel("First Name:");			
		manageEliminationsEditPanel.add(contestantFNameLabel);
		contestantFNameLabel.setBounds(260, 10, 200, 20);
		contestantFNameField = new RoundJTextField(10);
		manageEliminationsEditPanel.add(contestantFNameField);
		contestantFNameField.setBounds(260, 30, 200, 20);
		contestantFNameField.setEditable(false);

		contestantLNameLabel = new JLabel("Last Name:");
		manageEliminationsEditPanel.add(contestantLNameLabel);
		contestantLNameLabel.setBounds(520, 10, 100, 20);
		contestantLNameField = new RoundJTextField(10);
		manageEliminationsEditPanel.add(contestantLNameField);
		contestantLNameField.setBounds(520, 30, 200, 20);
		contestantLNameField.setEditable(false);


		contestantEliminatedLabel = new JLabel("Eliminated:");
		manageEliminationsEditPanel.add(contestantEliminatedLabel);
		contestantEliminatedLabel.setBounds(10, 60, 200, 20);
		contestantEliminatedField = new RoundJTextField(10);
		manageEliminationsEditPanel.add(contestantEliminatedField);
		contestantEliminatedField.setBounds(10, 80, 200, 20);
		contestantEliminatedField.setEditable(false);

		contestantElimWeekLabel = new JLabel("Elimination Week:");
		manageEliminationsEditPanel.add(contestantElimWeekLabel);
		contestantElimWeekLabel.setBounds(260, 60, 200, 20);
		contestantElimWeekField = new RoundJTextField(10);
		manageEliminationsEditPanel.add(contestantElimWeekField);
		contestantElimWeekField.setBounds(260, 80, 200, 20);
		
		editWeeklyElimination = new JButton("Set/Edit Weekly Elimination");
		manageEliminationsEditPanel.add(editWeeklyElimination);
		editWeeklyElimination.setBounds(600, 190, 200, 20);
		editWeeklyElimination.addActionListener(new CheckEditWeeklyElim());

		/*End Administrator Panel Layout*/
	}
	
	/**
	 * Method that returns the manage eliminations panel
	 * @return - the panel that holds the elimination info
	 */
	public JPanel getPanel() {
		return manageEliminationsPanel;
	}

	/**
	 * Method that returns the editable elimination panel
	 * @return - the panel that holds the area where elimination info is edited
	 */
	public JPanel getEditPanel() {
		return manageEliminationsEditPanel;
	}
	
	/**
	 * disableButtons Method - Disables the Functionality Buttons.
	 */
	public void disableButtons(){
		declareWeeklyElimButton.setEnabled(false);
		editWeeklyElimButton.setEnabled(false);
		declareUltimateWinnerButton.setEnabled(false);
		editUltimateWinnerButton.setEnabled(false);
	}
	
	/**
	 * getDeclareUltimateButton Method - returns the declareUltimateWinnerButton Button.
	 * @return declareUltimateWinnerButton
	 */
	public JButton getDeclareUltimateButton(){
		return declareUltimateWinnerButton;
	}
	
	/**
	 * getEditUltimateButton Method - returns the EditUltimateWinnerButton Button.
	 * @return declareUltimateWinnerButton
	 */
	public JButton getEditUltimateButton(){
		return editUltimateWinnerButton;
	}
}
