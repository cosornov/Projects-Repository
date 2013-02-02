import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.MaskFormatter;

/*New imports*/
import javax.swing.plaf.ColorUIResource;

import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.UIManager;

public class BonusQAPanel{
	
	/*Begin Bonus Questions and Answers Attributes*/
	private JPanel listBonusQAPanel;								//BonusQ&A Panel
	private JPanel listBonusQAEditPanel;							//BonusQ&A Edit (Add/Edit) Panel
	private static DefaultTableModel bonusQADM;						//BonusQ&A Default Table Model
	private static Object[][] bonusQAData;							//2D Object Containing BonusQ&A Data
	private static Object[] bonusQAHeader;							//Object Containing BonusQ&A Table Columns
	private static JTable bonusQATable;								//BonusQ&A Table
	private static JScrollPane bonusQAscrollPane;					//Scroll Pane for BonusQA Table
	private static JButton bonusQuestionAddButton;					//Add BonusQ&A Button
	private static JButton bonusQuestionEditButton;					//Edit BonusQ&A Button
	private static JButton bonusQuestionDeleteButton;				//Delete BonusQ&A Button
	private static JLabel bonusQARoundNumber;						//BonusQ&A Round Number
	private static JTextField bonusRoundNumberField;				//BonusQ&A Round Number Text Field
	private static JLabel bonusQAQuestionNumber;					//BonusQ&A Question Number
	private static JTextField bonusQAQuestionNumberField;			//Bonus Q&A Question Number Text Field
	private static JLabel bonusQuestionLabel;						//Bonus Question Label
	private static JTextField bonusQuestionField;					//Bonus Question Text Field
	private static JLabel bonusAnswerOneLabel;						//Bonus Answer One Label
	private static JTextField bonusAnswerOneField;					//Bonus Answer One Text Field
	private static JLabel bonusAnswerTwoLabel;						//Bonus Answer Two Label
	private static JTextField bonusAnswerTwoField;					//Bonus Answer Two Text Field
	private static JLabel bonusAnswerThreeLabel;					//Bonus Answer Three Label
	private static JTextField bonusAnswerThreeField;				//Bonus Answer Three Text Field
	private static JLabel bonusAnswerFourLabel;						//Bonus Answer Four Label
	private static JTextField bonusAnswerFourField;					//Bonus Answer Four Text Field
	private static JLabel bonusCorrectAnswerLabel;					//Bonus Correct Answer Label
	private static JTextField bonusCorrectAnswerField;				//Bonus Correct Answer Text Field
	private static JButton addBonusQAButton;						//Add BonusQ&A (Edit Panel) Button
	private static JButton editBonusQAButton;						//Edit BonusQ&A (Edit Panel) Button
	private String questionOptionEntry;								//String for storing selected Q&A Number
	private static TableColumn answersColumn;						//Column for the answers
	private static MaskFormatter formatter;
	private static JLabel questionTypeHint;							//Label for hinting at Question Type
	private static JLabel shortAnswerHint;
	/*End Bonus Questions and Answers Attributes*/
	
	/*Initialize GameSettings Object*/
	private GameSettings gamesettings;
	private Questions questions;
	
	/*Regex for players names - only letters, case insensitive*/
	private Pattern LETTERSONLY = Pattern.compile("[a-zA-Z ]+");
	private Pattern NUMBERSONLY = Pattern.compile("[0-9]+");
	
	/**
	 * Method to check for invalid input: Letters
	 **/
	private boolean areLetters(String s) {
		Matcher m = LETTERSONLY.matcher(s);
		return m.matches();
	}
	
	/**
	 * Method to check for invalid input: Numbers
	 **/
	private boolean areNumbers(String s) {
		Matcher m = NUMBERSONLY.matcher(s);
		return m.matches();
	}
	
	/*Action Methods*/
	/**
	 * createBonusQATable Method - Method for creating & updating the BonusQA JTable.
	 * @param createFromFile
	 */
	public void createBonusQATable(String createFromFile) {
		/*playersHeader Object containing the Columns for the listPlayersTable*/
		bonusQAHeader = new Object[] {
				"Round.QuestionID #",
				"Question",
				"Type",
				"Correct Answer"};
		
		/*Conditional used to control is table is created or updated*/
		if (createFromFile.equalsIgnoreCase("y")){
			questions = new Questions(); 
			
			/*Restore Players Data from Players Text File*/
			questions=questions.getFromFile(gamesettings.filename_Qs);
		} else{
			questions.getFromFile_questionSettings();
		}
		
		
		/*Prepare Questions Data for Table*/
		bonusQAData = questions.prepforTable();
		

		/*Conditional Statement which updates the JTable*/
		if (createFromFile.equalsIgnoreCase("y")){
			//use "prepforTable"
			bonusQADM = new DefaultTableModel(){

			    @Override
			    public boolean isCellEditable(int row, int column) {
			       //all cells false
			       return false;
			    }
			};

			bonusQADM.setDataVector(bonusQAData, bonusQAHeader);
						
			bonusQATable = new JTable(bonusQADM) {
				public void tableChanged(TableModelEvent e) {
					super.tableChanged(e);
				    repaint();
				}};
			
			/*Set Column Widths - Future Implementation! TBDealt with*/
		
			/*Adding Table to Scroll Pane and setting Location*/
			bonusQAscrollPane = new JScrollPane(bonusQATable);
			bonusQATable.setFillsViewportHeight(true);
			listBonusQAPanel.add(bonusQAscrollPane);
			bonusQAscrollPane.setBounds(10, 10, 600, 210);
			
			/*Create Sorting Functionality*/
			bonusQATable.setAutoCreateRowSorter(false);
			bonusQATable.getTableHeader().setReorderingAllowed(false);
			
		}else{
			bonusQADM.setDataVector(bonusQAData, bonusQAHeader);
			bonusQATable = new JTable(bonusQADM);
		}
		
	}
	
	/**
	 * AddBonusQA Class - Contains Method for Adding a Bonus Question & Answer.
	 *
	 */
	private class AddBonusQA implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			/*Recreate/Update the Table*/
			createBonusQATable("no");

			/*Display appropriate Buttons and Text Fields*/		
			clearQuestionPanelFields();
			editBonusQAButton.setVisible(false);
			listBonusQAEditPanel.setVisible(true);
			addBonusQAButton.setVisible(true);
			bonusRoundNumberField.setEditable(true);
			bonusAnswerOneField.setEditable(true);
			bonusAnswerTwoField.setEditable(true);
			bonusAnswerThreeField.setEditable(true);
			bonusAnswerFourField.setEditable(true);
			clearQuestionPanelFields();
		}
	}	
	
	/**
	 * EditBonusQA Class - Contains Method for Editing a Bonus Question & Answer.
	 *
	 */
	private class EditBonusQA implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			/*Redraw/Update the Table*/
			createBonusQATable("no");
		
			/*If there are more than 0 Players*/
			if (questions.getNumQuestions()>0){ 
				questionOptionEntry = JOptionPane
						.showInputDialog("Enter the Round Number and Question Number " +
								"(format: 'round.questionNumber') which you wish to EDIT:");
				
				
				/*If the Value entered into the Option Pane is not empty or not Cancel*/
				if((questionOptionEntry!=null)&&(questionOptionEntry.isEmpty()!=true)){
					
					
					if(	validDoubleInput()== false){
						JOptionPane.showMessageDialog(null, 
								"No such Round and Question Number combination exists!", 
								"Error: Round and/or Question Number", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					
					//Set Up visibility if the the round/question combination exists
					if(questions.getIndex(questionOptionEntry)!=null){ 
						clearQuestionPanelFields();
						listBonusQAEditPanel.setVisible(false);
						listBonusQAEditPanel.setVisible(true);
						addBonusQAButton.setVisible(false);
						editBonusQAButton.setVisible(true);

						//NEED to check code to make sure options,and conditionals work//
						/*Get Question information and populate Appropriate Text Fields with data*/
						Question tmpQ=questions.getQuestion(questionOptionEntry);
						
						///Some String Splitting
						String[] parseQuestionOptionEntry=questionOptionEntry.split("\\.");
						
						bonusRoundNumberField.setEditable(false);
						bonusRoundNumberField.setText(parseQuestionOptionEntry[0]); //CHANGE GUI TO SHOW ROUND.QEUSTION
						bonusQAQuestionNumberField.setText(parseQuestionOptionEntry[1]);
						bonusQuestionField.setText(tmpQ.getQuestion());
						bonusCorrectAnswerField.setText(tmpQ.getCorrectAnswer());

						setAnswers(tmpQ);

						//why we come here??	
					}else{
						JOptionPane.showMessageDialog(null, 
								"No such Round and Question Number combination exists!", 
								"Error: Round and/or Question Number", JOptionPane.ERROR_MESSAGE);
					}
						
				}else{
					/*Show Error Popup*/
					JOptionPane.showMessageDialog(null, 
							"No such Round and Question Number combination exists!", 
							"Error: Round and/or Question Number", JOptionPane.ERROR_MESSAGE);
				}
				}
				else if (questionOptionEntry==null){
				}
		
			/*else, if there are no Questions, display error Popup*/
			else{
				/*Show Error Popup*/
				JOptionPane.showMessageDialog(null, "There are no Questions/Answers to Edit!", 
						"Error: 0 Questions", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * DeleteBonusQA Class - Contains Method for Deleting a Bonus Question & Answer.
	 *
	 */
	private class DeleteBonusQA implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			questions=questions.getFromFile(GameSettings.filename_Qs);
			listBonusQAEditPanel.setVisible(false);
			
			/*Redraw/Update the Table*/
			createBonusQATable("no");

			/*Initially Set the Edit Panel to not be displayed.*/
			listBonusQAEditPanel.setVisible(false);
			
			/*If there are more than 0 Rounds/Questions*/
			if (questions.getNumQuestions()>0){
				questionOptionEntry = JOptionPane
						.showInputDialog("Enter the Round Number and Question Number " +
								"(format: 'round.questionNumber') which you wish to DELETE:");
			
				if (questionOptionEntry!=null){
					/*Only delete Round.Question if they Exist*/
			
					
					if(	validDoubleInput()== false){
						return;
					}
					
					
					if (questions.getIndex(questionOptionEntry)!=null){
						questions.deleteQuestion(questionOptionEntry);
						
						/*Update Text File*/
						questions.toFile(gamesettings.filename_Qs);

						questions=questions.getFromFile(GameSettings.filename_Qs);
						/*Display Message confirming deletion*/
						JOptionPane.showMessageDialog(null, "Deleted Round.Question: " + questionOptionEntry, 
								"deleted", JOptionPane.ERROR_MESSAGE); //-----------------create appropriate message: deleted!
						
						/*Redraw/Update Table*/
						createBonusQATable("no");
						
						//I take it that we wont be needing to write anything to GameSettings for Bonus Questions and Answers?
						//put that code here if we do!
					}
					else{
						/*Show Error Popup*/
						JOptionPane.showMessageDialog(null, "No such Round.Question Combination exists.", 
												"Error: Invalid Round.Question Combination", JOptionPane.ERROR_MESSAGE);
					}	
				}
			}
			else{
				/*Show Error Popup*/
				JOptionPane.showMessageDialog(null, "There are no Questions to Delete!", "Error: 0 Questions!",
											JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * CheckValidAdd Method - Contains action used to Add (a) Bonus Question/Answer(s).
	 *
	 */
	private class CheckValidAdd implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			/*Field Limitation Checks*/
			
			questions=questions.getFromFile(GameSettings.filename_Qs);
			
			if(!areNumbers(bonusRoundNumberField.getText().trim())){
				JOptionPane.showMessageDialog(null, "The Round needs to be a number.");
			}else if(bonusQuestionField.getText().trim().length()>200 ||                    
					!areLetters(bonusQuestionField.getText().trim())) { 
				JOptionPane.showMessageDialog(null, "The Question must be 1-200 letters." +
						"No Special Characters are allowed!");
			} else if(bonusCorrectAnswerField.getText().trim().length()>200 || 
					!areLetters(bonusCorrectAnswerField.getText().trim())) {            
				JOptionPane.showMessageDialog(null, "The Answer must be 1-200 letters." +
						"No Special Characters are allowed!");
			} else if(bonusRoundNumberField.getText().trim().equals("")){
				JOptionPane.showMessageDialog(null, "You need to enter a Round number!");
			}
			else if(questions.roundInBounds(Integer.parseInt(bonusRoundNumberField.getText().trim()) )==false){
				JOptionPane.showMessageDialog(null, "That round does not exist!");
			}
			else if (answerFieldsInBounds()==false){
				return ;
			}
			else{
				/*Add Question*/
				Question tmpQ= new Question();
				tmpQ.setAnswers(bonusCorrectAnswerField.getText().trim(), null);
				tmpQ.setQ(bonusQuestionField.getText().trim());
				tmpQ.setAnswers(getAnswers());
				
				boolean isItThere = questions.addQuestion(tmpQ,Integer.parseInt(bonusRoundNumberField.getText().trim()));
				if(isItThere==false){
					JOptionPane.showMessageDialog(null, "That Question already Exists!"
							, "Question Not added!", JOptionPane.ERROR_MESSAGE);
					return;
				}

					/*Update Text File*/
					questions.toFile(gamesettings.filename_Qs);
					questions=questions.getFromFile(GameSettings.filename_Qs);
					
					/*Show message dialog verifying that a User was added*/
					JOptionPane.showMessageDialog(null, "Succesfully added Round.Question: "
								+ tmpQ.getID(), "Round.Question Added!", JOptionPane.DEFAULT_OPTION);
					
					/*Redraw/Update Table*/
					clearQuestionPanelFields();
					createBonusQATable("no");
					
					/*Update Game Settings?*/

			}
		}
	}
	
	/**
	 * CheckValidEdit Method - Contains action used to Edit (a) Bonus Question/Answer(s).
	 *
	 */
	private class CheckValidEdit implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			questions=questions.getFromFile(GameSettings.filename_Qs);
			String bonusID=bonusRoundNumberField.getText().trim()+"."+bonusQAQuestionNumberField.getText().trim();
			
			/*Field Limitation Checks*/	
			if(!areNumbers(bonusRoundNumberField.getText().trim())){
				JOptionPane.showMessageDialog(null, "The Round needs to be a number.");
			}else if(bonusQuestionField.getText().trim().length()>200 ||                    
					!areLetters(bonusQuestionField.getText().trim())) { 
				JOptionPane.showMessageDialog(null, "The Question must be 1-200 letters." +
						"No Special Characters are allowed!");
			} else if(bonusCorrectAnswerField.getText().trim().length()>200 || 
					!areLetters(bonusCorrectAnswerField.getText().trim())) {            
				JOptionPane.showMessageDialog(null, "The Answer must be 1-200 letters." +
						"No Special Characters are allowed!");
			} else if(questions.roundInBounds (   Integer.parseInt( bonusRoundNumberField.getText().trim())  )==false){
				JOptionPane.showMessageDialog(null, "That round does not exist!");
			}
			else if (answerFieldsInBounds()==false){
				return;
			}
			
			/*NOTE! This (above) currently only checks one of the answers, I'll implement the other checks
			once I see how you've formatted the code and uploaded the working Questions Code*/
			
			
			else{
				/*Remove current question, re-add questions*/
				Question tmpQ= new Question();
				tmpQ.setAnswers(bonusCorrectAnswerField.getText().trim(), null);
				tmpQ.setQ(bonusQuestionField.getText().trim());
				tmpQ.setID(bonusID);
				
				tmpQ.setAnswers(getAnswers());
				
				
				questions.deleteQuestion(questionOptionEntry);

				//override and use the ID I've given tmpQ above
				boolean isItThere = questions.addQuestion(tmpQ,-1);
				if(isItThere==false){
					JOptionPane.showMessageDialog(null, "You cannot have duplicate Questions in the same round!"
							, "Question Not added!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				/*Update Text File*/
				questions.toFile(gamesettings.filename_Qs);
				questions=questions.getFromFile(GameSettings.filename_Qs);
				
				/*Show message dialog verifying that a User was Edited*/
				JOptionPane.showMessageDialog(null, "Succesfully Edited Round.Question:  "+ tmpQ.getID(), "Round.Question Edited!", JOptionPane.DEFAULT_OPTION);
				
				/*Redraw/Update Table*/
				clearQuestionPanelFields();
				createBonusQATable("no");
			}
			createBonusQATable("no");
		}
	}
	/*End Action Methods*/

	
	/**
	 * BonusQAPanel Constructor.
	 */
	public BonusQAPanel(){
		
		Color custom = new Color(225,225,225);
		
		gamesettings = new GameSettings();
		
		
		/*List Bonus Questions and Answers Content Pane Code*/
		listBonusQAPanel = new TransparentPanel();
		listBonusQAPanel.setLayout(null);
		listBonusQAPanel.setBounds(220, 60, 830, 470);
		listBonusQAPanel.setBackground(custom);
		listBonusQAPanel.setVisible(false);
		
		createBonusQATable("y");
		
		
		questionTypeHint = new JLabel("Note: For the Types" +
				"Column: 0 is a Short Answer Question; 1 is a Multiple Choice Question.");	
		listBonusQAPanel.add(questionTypeHint);
		questionTypeHint.setBounds(50, 220, 600, 20);
		
		shortAnswerHint = new JLabel("For short answers only fill in the correct Answer Field.");	
		listBonusQAPanel.add(shortAnswerHint);
		shortAnswerHint.setBounds(140, 240, 600, 20);
		
		bonusQuestionAddButton = new ColoredJButton("Add Q&A");
		bonusQuestionAddButton.addActionListener(new AddBonusQA());
		listBonusQAPanel.add(bonusQuestionAddButton);
		bonusQuestionAddButton.setBounds(620, 10, 200, 20);
				
		
		bonusQuestionEditButton = new JButton("Edit Q&A");
		bonusQuestionEditButton.addActionListener(new EditBonusQA());
		listBonusQAPanel.add(bonusQuestionEditButton);
		bonusQuestionEditButton.setBounds(620, 50, 200, 20);
				
		
		bonusQuestionDeleteButton = new JButton("Delete Q&A");
		bonusQuestionDeleteButton.addActionListener(new DeleteBonusQA());
		listBonusQAPanel.add(bonusQuestionDeleteButton);
		bonusQuestionDeleteButton.setBounds(620, 90, 200, 20);
				
		/*Begin BonusQ&AEditPanel Layout Code*/
		listBonusQAEditPanel = new JPanel();
		listBonusQAEditPanel.setLayout(null);
		listBonusQAPanel.add(listBonusQAEditPanel);
		listBonusQAEditPanel.setBounds(10, 260, 810, 200);
		listBonusQAEditPanel.setBackground(Color.WHITE);
		listBonusQAEditPanel.setVisible(false);
				
		
		bonusQARoundNumber = new JLabel("Round");
		listBonusQAEditPanel.add(bonusQARoundNumber);
		bonusQARoundNumber.setBounds(10, 10, 200, 20);
		bonusRoundNumberField = new RoundJTextField(10);
		listBonusQAEditPanel.add(bonusRoundNumberField);
		bonusRoundNumberField.setBounds(10, 30, 200, 20);
		bonusRoundNumberField.setToolTipText("Enter the Round Number");
		
		
		bonusQAQuestionNumber = new JLabel("QuestionID Number:");
		listBonusQAEditPanel.add(bonusQAQuestionNumber);
		bonusQAQuestionNumber.setBounds(260, 10, 200, 20);
		bonusQAQuestionNumberField = new RoundJTextField(10);
		listBonusQAEditPanel.add(bonusQAQuestionNumberField);
		bonusQAQuestionNumberField.setBounds(260, 30, 200, 20);
		bonusQAQuestionNumberField.setEditable(false);
		
		
		bonusQuestionLabel = new JLabel("Bonus Question:");			
		listBonusQAEditPanel.add(bonusQuestionLabel);
		bonusQuestionLabel.setBounds(520, 10, 200, 20);
		bonusQuestionField = new RoundJTextField(10);
		listBonusQAEditPanel.add(bonusQuestionField);
		bonusQuestionField.setBounds(520, 30, 200, 20);
		bonusQuestionField.setToolTipText("Enter a Question");
				
		
		bonusCorrectAnswerLabel = new JLabel("Correct Answer:");
		listBonusQAEditPanel.add(bonusCorrectAnswerLabel);
		bonusCorrectAnswerLabel.setBounds(10, 60, 200, 20);
		bonusCorrectAnswerField = new RoundJTextField(10);
		listBonusQAEditPanel.add(bonusCorrectAnswerField);
		bonusCorrectAnswerField.setBounds(10, 80, 200, 20);
		bonusCorrectAnswerField.setToolTipText("Enter the Correct Answer for the Question");
		
		
		bonusAnswerOneLabel = new JLabel("Wrong Answer One:");
		listBonusQAEditPanel.add(bonusAnswerOneLabel);
		bonusAnswerOneLabel.setBounds(260, 60, 200, 20);
		bonusAnswerOneField = new RoundJTextField(10);
		listBonusQAEditPanel.add(bonusAnswerOneField);
		bonusAnswerOneField.setBounds(260, 80, 200, 20);
		bonusAnswerOneField.setToolTipText("Enter the Option, if any");
		
		
		bonusAnswerTwoLabel = new JLabel("Wrong Answer Two:");
		listBonusQAEditPanel.add(bonusAnswerTwoLabel);
		bonusAnswerTwoLabel.setBounds(520, 60, 200, 20);
		bonusAnswerTwoField = new RoundJTextField(10);
		listBonusQAEditPanel.add(bonusAnswerTwoField);
		bonusAnswerTwoField.setBounds(520, 80, 200, 20);
		bonusAnswerTwoField.setToolTipText("Enter the Option, if any");
		
		
		bonusAnswerThreeLabel = new JLabel("Wrong Answer Three:");
		listBonusQAEditPanel.add(bonusAnswerThreeLabel);
		bonusAnswerThreeLabel.setBounds(10, 110, 200, 20);
		bonusAnswerThreeField = new RoundJTextField(10);
		listBonusQAEditPanel.add(bonusAnswerThreeField);
		bonusAnswerThreeField.setBounds(10, 130, 200, 20);
		bonusAnswerThreeField.setToolTipText("Enter the Option, if any");
		
		
		bonusAnswerFourLabel = new JLabel("Wrong Answer Four:");
		listBonusQAEditPanel.add(bonusAnswerFourLabel);
		bonusAnswerFourLabel.setBounds(260, 110, 200, 20);
		bonusAnswerFourField = new RoundJTextField(10);
		listBonusQAEditPanel.add(bonusAnswerFourField);
		bonusAnswerFourField.setBounds(260, 130, 200, 20);
		bonusAnswerFourField.setToolTipText("Enter the Option, if any");
		
		
		addBonusQAButton = new JButton("Add Bonus QA");
		listBonusQAEditPanel.add(addBonusQAButton);
		addBonusQAButton.setBounds(600, 170, 200, 20);
		addBonusQAButton.addActionListener(new CheckValidAdd());
		
		
		editBonusQAButton = new JButton("Edit Bonus QA");
		listBonusQAEditPanel.add(editBonusQAButton);
		editBonusQAButton.setBounds(600, 170, 200, 20);
		editBonusQAButton.addActionListener(new CheckValidEdit());
		/*End BonusQ&APanel Layout Code*/
	}
	
	/**
	 * getPanel Method - Returns listBonusQAPanel JPanel
	 * @return listBonusQAPanel
	 */
	public JPanel getPanel() {
		return listBonusQAPanel;
	}
	
	/**
	 * getEditPanel Method - Returns listBonusQAEditPanel JPanel
	 * @return listBonusQAEditPanel
	 */
	public JPanel getEditPanel() {
		return listBonusQAEditPanel;
	}
	
	/**
	 * getAddQuestionContestant Method - Returns
	 * @return bonusQuestionAddButton
	 */
	public JButton getAddContestant(){
		return bonusQuestionAddButton;
	}
	
	public JButton getEditContestant(){
		return bonusQuestionEditButton;
	}

	public JButton getDeleteContestant(){
		return bonusQuestionDeleteButton;
	}
	
	/**
	 * disableButtons Method - Disables the Functionality Buttons.
	 */
	public void disableButtons(){
		bonusQuestionAddButton.setEnabled(false);
		bonusQuestionEditButton.setEnabled(false);
		bonusQuestionDeleteButton.setEnabled(false);
	}
	
	
	/*Helper Methods*/
	/**
	 * Checks the conditionals, and sets the answers 
	 * Used in editbonusqa
	 */
	private void setAnswers(Question tmpQ){
		//really lazy way of getting all the options to display..
		if (tmpQ.getQType()==1){
			
			String[] AOptions= tmpQ.getOptions();
			
			if (tmpQ.getNumOptions()>0){
				
				if(tmpQ.getNumOptions()>3){

					bonusAnswerFourField.setText(AOptions[3]);
					bonusAnswerFourField.setEditable(true);
				}
				if(tmpQ.getNumOptions()>2){
					bonusAnswerThreeField.setText(AOptions[2]);
					bonusAnswerThreeField.setEditable(true);
				}
				if(tmpQ.getNumOptions()>1){
					bonusAnswerTwoField.setText(AOptions[1]);				
					bonusAnswerTwoField.setEditable(true);
				}
				if(tmpQ.getNumOptions()>0){

					bonusAnswerOneField.setText(AOptions[0]);
					bonusAnswerOneField.setEditable(true);
				}												
			}

		} else{
			bonusAnswerOneField.setText("");
			bonusAnswerTwoField.setText("");
			bonusAnswerThreeField.setText("");
			bonusAnswerFourField.setText("");
			bonusAnswerOneField.setEditable(false);
			bonusAnswerTwoField.setEditable(false);
			bonusAnswerThreeField.setEditable(false);
			bonusAnswerFourField.setEditable(false);
		}
			
	}
	
	
	

	
	/**
	 * returns an array of the answers
	 * gathers all the answer fields (including correct answer)
	 * then you will use this with question.setAnswers(String[] tmp);
	 * ...this is an overloaded method :)
	 * @return
	 */
	private String[] getAnswers(){
		 ArrayList<String> answers= new  ArrayList<String>();
		
		if (bonusCorrectAnswerField.getText().trim().equals("")){
			return null;
		}else{
			answers.add(bonusCorrectAnswerField.getText().trim());
			
			if(!bonusAnswerOneField.getText().trim().equals("")){
				answers.add(bonusAnswerOneField.getText().trim());
			}
			if(!bonusAnswerTwoField.getText().trim().equals("")){
				answers.add(bonusAnswerTwoField.getText().trim());
			}
			if(!bonusAnswerThreeField.getText().trim().equals("")){
				answers.add(bonusAnswerThreeField.getText().trim());
			}
			if(!bonusAnswerFourField.getText().trim().equals("")){
				answers.add(bonusAnswerFourField.getText().trim());
			}
			

			String[] tmpanswers= new String[answers.size()];
			for(int i=0; i<answers.size();i++){
				tmpanswers[i]=answers.get(i);
				/* Code Commented out System.out.println(tmpanswers[i]);*/
			}
			
			return tmpanswers;
			
		}

	}
	

	
	/**
	 * making sure fields are in bounds and valid
	 * @return
	 */
	private boolean answerFieldsInBounds(){
		
		boolean invalid=true;
		String[] pairwiseEqual = new String[5];
		for (int i=0; i<pairwiseEqual.length;i++){
			pairwiseEqual[i]="";
		}
		
		if (bonusCorrectAnswerField.getText().trim().length()>200){
			invalid= false;
		}else{
			pairwiseEqual[0]=bonusCorrectAnswerField.getText().trim();
		}
			
		if(bonusAnswerOneField.getText().trim().length()>200){
			invalid= false;
		}else{
			pairwiseEqual[1]=bonusAnswerOneField.getText().trim();
		}
		
		
		if(bonusAnswerTwoField.getText().trim().length()>200){
			invalid= false;
		}else{
			pairwiseEqual[2]=bonusAnswerTwoField.getText().trim();
		}
		if(bonusAnswerThreeField.getText().trim().length()>200){
			invalid= false;
		}else{
			pairwiseEqual[3]=bonusAnswerThreeField.getText().trim();
		}
		if(bonusAnswerFourField.getText().trim().length()>200){
			invalid= false;
		}else{
			pairwiseEqual[4]=bonusAnswerFourField.getText().trim();
		}
		
		if (invalid==false){
			invalidInput();
			return false;
		} 
		for (int i=0;i<pairwiseEqual.length-1;i++){
			for (int j=i+1;j<pairwiseEqual.length;j++){
				
				//no two answers can be correct
				if ((pairwiseEqual[i].equals(pairwiseEqual[j])) && (!pairwiseEqual[i].equals(""))&&
					 (!pairwiseEqual[j].equals(""))){
					pairWiseEqual();
					return false;
				}
			}
		}
		return true;
	}
	
	private void invalidInput(){
		JOptionPane.showMessageDialog(null, "The Answer must be 1-200 letters." +
		"No Special Characters are allowed!");
	}
	
	private void pairWiseEqual(){
		JOptionPane.showMessageDialog(null, "Answers must be distinct " +
		"Error: Duplicate");
	}
	
	/**
	 * clear all the question editing/adding panel fields
	 */
	private void clearQuestionPanelFields(){
		
		bonusRoundNumberField.setText("");
		bonusQAQuestionNumberField.setText("");
		bonusQuestionField.setText("");
		bonusCorrectAnswerField.setText("");
		bonusAnswerOneField.setText("");
		bonusAnswerTwoField.setText("");
		bonusAnswerThreeField.setText("");
		bonusAnswerFourField.setText("");
	}

	
	/**
	 * check if the user qid input is in the format of a double
	 * @return
	 */
	private boolean validDoubleInput(){
		//create index variable for round/question
		try{
			double qid=Double.valueOf(questionOptionEntry.trim()).doubleValue();
		}catch(Exception erre){
			JOptionPane.showMessageDialog(null, "The qid must be in the format of a decimal number!");
			return false;
		}
		return true;
	}

	
}
