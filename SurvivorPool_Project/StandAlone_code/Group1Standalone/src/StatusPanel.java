import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StatusPanel {

	/*Begin Status Panel Components*/
	private static JPanel statusPanel;							//Status Panel JPanel
	private static JLabel gameStatusLabel;						//Game Status Label JLabel
	private static JLabel betAmount;							//Bet Amount JLabel
	private static JLabel roundDisplay;							//Current Round JLabel
	private static JButton beginGame;							//Begin Game JButton
	private static String betAmountPrompt;						//Bet Amount String
	/*End Status Panel Components*/
	
	public GameSettings gsettings;								//Initialize Game Settings Object
	public Contestants contestants;								//Initialize Contestants Object
	
	/*Initializing Respective Panel Objects*/
	private PlayersPanel listPlayersPanel;						//Initialize Players Panel Object
	private ContestantsPanel listContestantsPanel;				//Initialize Contestants Panel Object
	private BonusQAPanel listBonusQAPanel;						//Initialize Bonus Q/A Panel Object
	private EliminationPanel manageEliminationsPanel;			//Initialize Manage Eliminations Panel Object
	private ManageScoresPanel manageScoresPanel;				//Initialize Manage Scores Panel Object
	private Login lg;											//Initialize Login Panel Object

	/*For Testing Purposes ONLY*/
	private static JButton roundButton;
	private String testingOption;
	private static JButton showStandings;
	
	/*Start Action Methods*/
	/**
	 * Begin Game Class - Contains Action Method for Starting the Game.
	 *
	 */
		private class BeginGame implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			/*Getting current data - used for testing for the moment*/
			gsettings.resetFromFile(gsettings.filename_settings);
			
			/*Prompt to add bet amount*/
			betAmountPrompt = JOptionPane
					.showInputDialog(null,"Begin Game: Bet Amount","Enter the Bet Amount:"
									,JOptionPane.QUESTION_MESSAGE);
			
			
			/*Conditional statements to check and see the following:
			 * 1)If Cancel is Clicked
			 * 2)If the Entered Bet Amount Field is Empty
			 * 3)If the Entered Bet Amount is Numeric
			 * 4)If the there are less than 6 Contestants
			 * 5)If there are 6-15 Contestants
			 */
			if (betAmountPrompt==null){
				//Do Nothing if they click Cancel
			}
			else if (betAmountPrompt.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Error! You did not enter a bet amount!", "Error! No Bet Value!", JOptionPane.ERROR_MESSAGE);
			}
			else if(isNumeric(betAmountPrompt)==false){
				JOptionPane.showMessageDialog(null, "Error! Must enter an integer! ", "Error! Invalid Input Data", JOptionPane.ERROR_MESSAGE);

			}
			else if (betAmountPrompt.toCharArray().length > 7){
				JOptionPane.showMessageDialog(null, "Your bet cannot exceed 7 digits!  This is not a High Stakes game. ;)",
						"Error! Invalid Input Data", JOptionPane.ERROR_MESSAGE);

			}
			else if(Integer.parseInt(betAmountPrompt) < 1){
				JOptionPane.showMessageDialog(null, "Error! Must be an integer larger than 0! ", "Error! Invalid Input Data", JOptionPane.ERROR_MESSAGE);

			}
			else if (gsettings.getNumContestants() < 6){
				JOptionPane.showMessageDialog(null, "Error! Not enough Contestants!", "Error! Not enough Contestants!", JOptionPane.ERROR_MESSAGE);
			} 
			else if(gsettings.getNumPlayers() <3){
				JOptionPane.showMessageDialog(null, "Error!  We will rank 3 players at the end of the game, so there must be at least 3 players to start the game!", "Error! Not enough Players!", JOptionPane.ERROR_MESSAGE);

			}
			else if( (gsettings.getNumContestants() >= 6)
					&& (gsettings.getNumContestants() < 16)
					&& (isNumeric(betAmountPrompt))&&
					(gsettings.getNumPlayers()>=3)){ 
				
				/*
				if (haveAllPlayersVotedforUlt()!=true){
					JOptionPane.showMessageDialog(null, "Error! Ever Player must vote for an Ultimate Winner *before* the game can start" +
												"!", "Error! Missing Ultimate Winner Vote", JOptionPane.ERROR_MESSAGE);

					return;
				}
				*/
				
				//prepare he ultimate winner text file for Sean
				gsettings.update();
				
				/*Initialize and set the Current Round Bet Amount to the Text Field*/
				gsettings.setCurrentRound(1);
				gsettings.setBetAmount(Integer.parseInt(betAmountPrompt));
				gsettings.toFile(gsettings.filename_settings);
				
				/*Reset Round Display Contents and Layout*/
				roundDisplay.setVisible(false);
				roundDisplay = new JLabel("Current Round: " + gsettings.getCurrentRound() + "/" + gsettings.getNumTotalRounds());
				statusPanel.add(roundDisplay);
				roundDisplay.setBounds(330, 0, 150, 20);
				
				/*Remove Add Player Button*/
				listPlayersPanel.getAddButton().setVisible(false);
				listPlayersPanel.getPanel().remove(listPlayersPanel.getAddButton());
				
				/*Remove Delete Player Button*/
				listPlayersPanel.getDeleteButton().setVisible(false);
				listPlayersPanel.getPanel().remove(listPlayersPanel.getDeleteButton());
				
				/*Remove Add Player Button*/
				listContestantsPanel.getAddButton().setVisible(false);
				listContestantsPanel.getPanel().remove(listContestantsPanel.getAddButton());
				
				/*Remove Delete Player Button*/
				listContestantsPanel.getDeleteButton().setVisible(false);
				listContestantsPanel.getPanel().remove(listContestantsPanel.getDeleteButton());
				
				
				/*Remove 'Editability' of Edit Panel/Button*/
				listContestantsPanel.getFirstName().setEditable(false);
				listContestantsPanel.getLastName().setEditable(false);
				listContestantsPanel.getElimWeek().setEditable(false);
				/*Set Upload Image Button as uninteractive-able HERE*/
				listContestantsPanel.getPhotoButton().setEnabled(false);
				listContestantsPanel.getPhotoButton().setVisible(false);
				listContestantsPanel.getEditPanel().remove(listContestantsPanel.getPhotoButton());
				
				/*Start Game - Remove Functionality*/
				beginGame.setEnabled(false);
				
				/*Set Game Status, Bet Amount and Current Round Values Respectively*/
				gameStatusLabel.setText("Game Status:"+" Started!"); 
				betAmount.setText("Bet Amount: $"+gsettings.getBetAmount());
				roundDisplay.setText("Current Round: " + gsettings.getCurrentRound() + "/" + gsettings.getNumWeeks()+"wks");
			
				/*Remove Begin Game Button*/
				beginGame.setEnabled(false);
				
				/*Code to be used for Later Project
				 * statusPanel.add(gameStarted);
				 * gameStarted.setBounds(520, 0, 170, 20);
				 */
				updateQuestionsInternalFile_onstart();
				updateQuestionFile_onstart();
				
				//update the eliminations textfile!
				Elimination tmpElim=new Elimination();
				tmpElim.prepFile(1, gsettings.filename_eliminations);
				tmpElim.prepFile(1, gsettings.filename_ultWinner);
				
				listContestantsPanel.disablePhotoButton();
			}
			else{
				JOptionPane.showMessageDialog(null, "Error!  Cannot Start Game. Contact an Administrator", "Error! Not enough Players!", JOptionPane.ERROR_MESSAGE);

			}
		}
	}
	/*End Action Methods*/
	
	/*For incrementing the Round*/
	private class SetRound implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			gsettings.update();
			
			if (gsettings.inGame()==false){
				JOptionPane.showMessageDialog(null, "Can't change round unless the game is on! " , 
						"Error: Game hasn't started, or has already ended", JOptionPane.ERROR_MESSAGE);
				return;
			}
		
			//String storing the value entered into the Dialog Box
			testingOption = String.valueOf(1 + gsettings.getCurrentRound());/*JOptionPane
					.showInputDialog("Enter the Round you wish to set the Game to:");*/
			
			if (testingOption!=null){
				
				if ((Integer.parseInt(testingOption))<=gsettings.getNumWeeks()){
					//set current round to the gamesettings object
					gsettings.setCurrentRound(Integer.parseInt(testingOption));
					
					//write gamesettings object with changes to file
					gsettings.toFile(gsettings.filename_settings);
					
					//update status panel
					roundDisplay.setText("Current Round: " + gsettings.getCurrentRound() + "/" + gsettings.getNumTotalRounds()+"wks");
				
					//update the eliminations textfile!
					Elimination tmpElim=new Elimination();
					tmpElim.prepFile(Integer.parseInt(testingOption), gsettings.filename_eliminations);
					
					if(gsettings.getCurrentRound()==gsettings.num_weeks){
						manageEliminationsPanel.getDeclareUltimateButton().setEnabled(true);
						manageEliminationsPanel.getEditUltimateButton().setEnabled(true);
					}
					else
					{
						manageEliminationsPanel.getDeclareUltimateButton().setEnabled(false);
						manageEliminationsPanel.getEditUltimateButton().setEnabled(false);
					}
					
				
				} else{
					/*Show Error Popup*/
					JOptionPane.showMessageDialog(null, "You are already in the final week!  No more weeks remaining! " , 
											"Error: No more weeks!", JOptionPane.ERROR_MESSAGE);
					return;
				}

				
			} else{
				
			}
		}
	}

	/**
	 * Action Listener
	 * @author monster
	 *
	 */
	private class DisplayStandings implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			DisplayWinners dw = new DisplayWinners(gsettings.filename_winners);
			String[] tmp = dw.getInfo();
			String[] another = dw.makeReadable(tmp);
			String erryting="";//<html> <br>";
			
			for(int i=0;i<another.length;i++) {
				
				erryting=erryting+another[i];
				
			}	
			
			//erryting=erryting+"</html>";
			JOptionPane.showMessageDialog(null, erryting,
					 "Final Standings", JOptionPane.DEFAULT_OPTION);
		}
	}
	
	/**
	 * Constructor for StatusPanel
	 */
	public StatusPanel() {
		
		/*Set Game Settings and Contestants*/
		gsettings = new GameSettings();
		contestants = new Contestants();

		/*Creation of Respective Panel Objects*/
		listPlayersPanel = new PlayersPanel();
		listContestantsPanel = new ContestantsPanel();
		listBonusQAPanel = new BonusQAPanel();
		manageEliminationsPanel = new EliminationPanel();
		manageScoresPanel = new ManageScoresPanel();
		
		/*Status Panel Code*/
		statusPanel = new TransparentPanel();							//Create Status Panel JPanel
		statusPanel.setLayout(null);						//Set Status Panel Layout to null for
															//Absolute Layout Values
		statusPanel.setBounds(220, 20, 830, 20);			//Set Layout of Status Panel
		Color custom = new Color(225, 225, 225);			//Create Color defined as 'custom'
		statusPanel.setBackground(custom);					//Set Background color to 'custom'
		
		/*Check to see if the Game has started*/
		if ((gsettings.hasGameStarted()==false)){
			gameStatusLabel = new JLabel("Game Status:"+" Pending.");
		} else{
			gameStatusLabel = new JLabel("Game Status:"+" Started!");
		}
		
		/*Add and set Layout of Status Label to Status Panel*/
		statusPanel.add(gameStatusLabel);
		gameStatusLabel.setBounds(5, 0, 150, 20);
		
		/*Check to see if game has started*/
		if ((gsettings.hasGameStarted()==false)){
			roundDisplay = new JLabel("Current Round:  n/a");
		}else{
			roundDisplay = new JLabel("Current Round: " + gsettings.getCurrentRound() + "/" + gsettings.getNumWeeks()+"wks");
		}
		
		/*ROUND BUTTON CODE HERE!!*/
		roundButton = new JButton("Increment Round");
		statusPanel.add(roundButton);
		roundButton.setBounds(160, 0, 150, 20);
		roundButton.addActionListener(new SetRound());
		
		/*Add and set Layout of Round Display Label to Status Panel*/
		statusPanel.add(roundDisplay);
		roundDisplay.setBounds(330, 0, 150, 20);
				
		/*Check to see if the game has started*/
		if ((gsettings.hasGameStarted()==false)){
			betAmount = new JLabel("Bet Amount:    Not Set");
		}
		else{
			betAmount = new JLabel("Bet Amount: $"+gsettings.getBetAmount());
		}
		/*Add and set Layout of Bet Amount Label to Status Panel*/
		statusPanel.add(betAmount);
		betAmount.setBounds(500, 0, 150, 20);
		
		/*Add, set Layout of Game Button. Conditionals: display Game Buttons only if the game has not Begun*/
		beginGame = new JButton("Begin Game");
		if ((gsettings.hasGameStarted()==false)){
			beginGame.addActionListener(new BeginGame());
		} else{
			beginGame.setEnabled(false);
		}
		statusPanel.add(beginGame);
		beginGame.setBounds(650, 0, 170, 20);
		
		/*Initialize the showStandings Button*/
		showStandings = new JButton("Show Standings");
		statusPanel.add(showStandings);
		showStandings.setBounds(160, 0, 150, 20);
		showStandings.addActionListener(new DisplayStandings());
		showStandings.setEnabled(false);
		
	}
	
	
	/**
	 * Method isNumeric - Used for Validating the Input.
	 * @param input
	 * @return boolean: true or false
	 */
	public boolean isNumeric(String input) {
		  try {
		    Integer.parseInt(input);
		    return true;
		  }
		  catch (NumberFormatException e) {
		    return false;
		  }
		}
	
	/**
	 * Method for returning the statusPanel JPanel.
	 * @return statusPanel
	 */
	public JPanel getPanel() {
		return statusPanel;
	}
	
	/**
	 * 
	 * @return roundButton
	 */
	public void DisableIncrement(){
		 roundButton.setVisible(false);
	}
	
	//-----------------------------------------------------
		//Helpers (to check if game can start)
		//-----------------------------------------------------
		
		
		private boolean updateQuestionsInternalFile_onstart(){
			
			
			 try{
				  // Create file 
				  FileInputStream fstream = new FileInputStream(gsettings.filename_QuestionsInternalSettings);
				  // Get the object of DataInputStream
				  DataInputStream in = new DataInputStream(fstream);
				  BufferedReader br = new BufferedReader(new InputStreamReader(in));
				  String strLine = br.readLine();
				  
				  in.close();
				  
				  String delim="--";
				  String[] arr=strLine.split(delim);
				  
				  
				  FileWriter fstream2 = new FileWriter(gsettings.filename_QuestionsInternalSettings);
				  BufferedWriter out = new BufferedWriter(fstream2);
				  
				  
				  for (int i=0; i<gsettings.getNumTotalRounds();i++){
					  if (i <arr.length){
						  
					  }else{
						  strLine=strLine+delim+"0";
					  }
					  
					
					  
				  }
				 out.write(strLine);
				 	out.close();
				  
			
			 }	 catch (Exception ee){//Catch exception if any
				 System.out.println("FROM THE INTERNAL FILE");
				  System.err.println("Error: " + ee.getMessage());
				  return false;
				  
			 }
			 return true;
		}


		private boolean updateQuestionFile_onstart(){
			try{
			//read in entire file and count number of lines
				  // Create file 
				  FileInputStream fstream = new FileInputStream(gsettings.filename_Qs);
				  // Get the object of DataInputStream
				  DataInputStream in = new DataInputStream(fstream);
				  BufferedReader br = new BufferedReader(new InputStreamReader(in));
				  String strLine;
				  int count=0;
				  while( br.readLine()!=null){
					  count++;
					  
				  }
				  in.close();
				 
				  
				  FileInputStream fstream2 = new FileInputStream(gsettings.filename_Qs);
				  // Get the object of DataInputStream
				  DataInputStream in2 = new DataInputStream(fstream2);
				  BufferedReader br2 = new BufferedReader(new InputStreamReader(in2));
			
				  br2 = new BufferedReader(new InputStreamReader(in2));
				  String[] contents=new String[count];

				  for (int i=0; i<count;i++){
					  contents[i]=br2.readLine();
				  }
			
				
				  
				  
			//edit the first line in the string array
				  String delim="-------";
				  String[] text= contents[0].split(delim);
				  contents[0]=text[0]+delim+String.valueOf(gsettings.getNumTotalRounds());
				  
				  
			//write it out
				  
				  FileWriter fstream3 = new FileWriter(gsettings.filename_Qs);
				  BufferedWriter out = new BufferedWriter(fstream3);
				  for (int i=0; i<contents.length;i++){
					  out.write(contents[i]);
					  out.newLine();
				  }
				  out.close();
			 }	 catch (Exception ee){//Catch exception if any
				 System.out.println("FROM THE INTERNAL FILE");
				  System.err.println("Error: " + ee.getMessage());
				  return false;
				  
			 }
				
			return true;
		}
		
		
private boolean haveAllPlayersVotedforUlt(){
		
		gsettings.update();
		
		//read entire file into array
		try{
			
			
			//read in entire file and count number of lines
			  // Create file 
			  FileInputStream fstream = new FileInputStream(gsettings.filename_ultWinner);
			  // Get the object of DataInputStream
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  int count=0;
			  while( br.readLine()!=null){
				  count++;
				  
			  }
			  in.close();
	 
			  
			  FileInputStream fstream2 = new FileInputStream(gsettings.filename_ultWinner);
			  // Get the object of DataInputStream
			  DataInputStream in2 = new DataInputStream(fstream2);
			  BufferedReader br2 = new BufferedReader(new InputStreamReader(in2));
		
			  br2 = new BufferedReader(new InputStreamReader(in2));
			  String[] contents=new String[count];

			  for (int i=0; i<count;i++){
				  contents[i]=br2.readLine();
			  }
			  
			  
				//load players array
				Players ps= new Players();
				Contestants cs=new Contestants();
				cs.resetFromFile(gsettings.filename_contestants);
				ps.resetFromFile(gsettings.filename_players);
				Player p;
				Contestant c;
				String tmp[];
				String delim="--";
				boolean found;
				
				//for player p...
				for (int i=0; i<gsettings.getNumPlayers();i++){
					p=ps.getPlayer(i);
					found=false;
					/*System.out.println(p.getID()+"-----------");*/

				
					//loop through entire ultfile...
					for (int j=0; j<count; j++){
						
					
						//Split and does anyline contain their PID?
						tmp=contents[j].split(delim);
						if (p.getID().equals(tmp[0])){
							
							if (cs.findContestant_byID(tmp[2].trim())!=null){
								j=count;
								found=true;
							} else{
								
							}
						}
						
					}
					
					// if not...f`ail!

					if (found==false){
						return false;
					}
				}
			  
				  in2.close();
				  
				  
		}  catch (Exception ee){//Catch exception if any
			 System.out.println("FROM THE Checking if all voted Ulimate Func");
			  System.err.println("Error: " + ee.getMessage());
			  return false;
			  
		}
		
		return true;
	}

	/**
	 * 
	 * @return gameStatusLabel
	 * **/
	public void ChangeStatusLabel(){
		gameStatusLabel.setText("Game Status: Ended!");
		
	}
	
	public JButton GetStandingsButton(){
		return showStandings;
	}

}
