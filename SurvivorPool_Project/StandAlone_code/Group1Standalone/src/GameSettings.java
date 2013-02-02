import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * This class contains info related to gamesettings...
 * a lot of these variables are here so we can treat them like global variables
 * @author m3t4l
 *
 */
public class GameSettings {
	public int num_weeks;			//number of weeks in game
	public int num_players;			//number of players in the game
	public int num_contestants;		//numb cntsts
	public int bet_amount;			//bet amount of each user
	public int current_round;		//curent round we are in
	public String ultimateWinner;
	public int gameEnded;			//1=yes, anything=no
		
	
	public int objectsize=10;
	public static int contesants_max=15;	//Max Conestants
	public static int contesants_min=6;		//min contestants
	
	
	//Save/Restore Files for various modules
	public static String path = "C:\\SDCard\\BlackBerry\\";
	public static String filename_contestants=path + "cntst_text2.txt";   // contestants
	public static String filename_players=path + "Test.txt";   // players
	public static String filename_Qs=path + "Questions2.txt";
	public static String filename_qAnswers=path + "Test3.txt";
	public static String filename_ultWinner=path + "ultimate.txt";
	public static String filename_eliminations=path + "elims.txt";
	
	//important internal files
	public static String filename_settings=path + "settings.txt";
	public static String filename_QuestionsInternalSettings=path+"internalSettings_questions.txt";
	
	public static String filename_winners=path + "winners.txt";
	
	/**
	*Constructor for game settings
	*/
	GameSettings(){
		//shoudl do...
		resetFromFile(filename_settings);
		//setNumWeeks(-1);
		//setNumPlayers(-1);
		//setNumContestants(-1);
		//setBetAmount(-1);
		//setCurrentRound(-1);
		
	}
	
	//--------------------------------------------------
	//Setters
	//--------------------------------------------------
	/**
	* @param numplayers
	*/
	public void setNumPlayers(int numplayers){
		num_players=numplayers;
	}
	/**
	* @param numplayers
	*/
	public void setUlimateWinner(String cID){
		ultimateWinner=cID;
	}
	/**
	* @param numCntsts
	*/
	public void setNumContestants(int numCntsts){
		num_contestants=numCntsts;
		 calculateNumWeeks();
		 setNumWeeks(num_weeks);
		
	}
	
	/**
	* @param numWeeks
	*/
	public void setNumWeeks(int numWeeks){
		num_weeks=numWeeks;
	}
	
	/**
	* @param bet
	*/
	public void setBetAmount(int bet){
		bet_amount=bet;
	}
	
	/**
	* @param round
	*/
	public void setCurrentRound(int round){
		current_round=round;
	}
	
	/**
	* method that calculates how long the game will run for
	*/
	public void calculateNumWeeks(){
		num_weeks=(num_contestants-3)+1;
	}
	
	/**
	* method that calculates how long the game will run for
	*/
	public void setGameEnded(int value){
		gameEnded=value;
	}
	
	//--------------------------------------------------
	//Getters
	//--------------------------------------------------
	/**
	* @return num_players
	*/
	public  int getNumPlayers( ){
		update();
		return num_players;
	}
	
	/**
	* @return num_contestants
	*/
	public int getNumContestants( ){
		update();
		return num_contestants;
	}
	
	/**
	* @return num_weeks
	*/
	public int getNumWeeks( ){
		update();
		calculateNumWeeks();
		return num_weeks;
	}
	
	/**
	* @return bet_amount
	*/
	public int getBetAmount( ){
		update();
		return bet_amount;
	}
	
	/**
	* @return ultimateWinner
	*/
	public String getUltimateWinner( ){
		update();
		return ultimateWinner;
	}
	
	/**
	* @return current_round
	*/
	public int getCurrentRound( ){
		update();
		return current_round;
	}
	
	/*Included this as there was no Method returning the 'Calculated weeks/Total number of rounds.*/
	/**
	* @return num_weeks
	*/
	public int getNumTotalRounds( ){
		update();
		calculateNumWeeks();
		return num_weeks;
	}
	
	/**
	 * returns the total winnings
	 * @return
	 */
	public int getTotalPot() {
		update();
		return num_players*bet_amount;
	}
	
	
	/**
	 * returns the total winnings
	 * @return
	 */
	public int getGameEnded() {
		update();
		return gameEnded;
	}
	
	//--------------------------------------------------
	//Helper
	//--------------------------------------------------
	/**
	* @return true or false if game has started or not
	*/
	public boolean hasGameStarted(){
		if (current_round!=-1){
			return true;
		} else{
			return false;
		}
	}
	
	
	public boolean hasGameEnded(){
		if (gameEnded==1){
			return true;
		} else{
			return false;
		}
	}
	
	
	public boolean inGame(){
		
		if ((hasGameEnded()==false)&&(hasGameStarted()==true)){
			return true;
		}
		return false;
	}
	//--------------------------------------------------
	//Writers
	//--------------------------------------------------

	/**
	 * Write object to a file
	 * @param filename
	 * id--fname--lname
	 */
	public void toFile(String filename){

		
		 try{
			  // Create file 
			  FileWriter fstream = new FileWriter(filename);
			  BufferedWriter out = new BufferedWriter(fstream);
			  out.write(toString());
			  //Close the output stream
			  out.close();
		 }
		 catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
		 }
		
	}
	
	
	
	/**
	 * @return String representation of object
	 * id--fname--lname
	 */
	public String toString(){
		String tmp="";
		String delim="--";
		
		tmp=getNumPlayers()+delim+getNumContestants()+delim+getNumWeeks()+delim+getBetAmount()+delim+getCurrentRound();
		tmp=tmp+delim+filename_contestants+delim+filename_players+delim+filename_settings;
		
		
		//lazy additions
		tmp=tmp+delim+getUltimateWinner();
		tmp=tmp+delim+getGameEnded();

		return tmp;	
	}
	
	
	//--------------------------------------------------------
		// Read from file
		//http://www.roseindia.net/java/beginners/java-read-file-line-by-line.shtml
		//--------------------------------------------------------
	
		//update GS
		public void update(){
			resetFromFile(filename_settings);
		}
	
	
		/**
		 * Read a Contestants object from a textfile
		 * and then reset "THIS" object to contents of textfile
		 * @param filename
		 */
		public void resetFromFile(String filename){
			 try{
				 // Open the file that is the first 
				  // command line parameter
				  FileInputStream fstream = new FileInputStream(filename);
				  
				  // Get the object of DataInputStream
				  DataInputStream in = new DataInputStream(fstream);
				  BufferedReader br = new BufferedReader(new InputStreamReader(in));
				 
				  
				  //setup variables that will be needed to readin and transfer			  
				  String strLine;
				  String[] tmp_data;
				  String delim="--";
				  
				  
				  
				  //Read File Line By Line
				  while ((strLine = br.readLine()) != null)   {
					  
					  //create a newtmp object to transfer...
					  strLine.replaceAll("\\n", "");
					  tmp_data=strLine.split(delim);
					
					  if (tmp_data.length != objectsize){
						  return;
					  }
		
					  //transfer the relevant variables
					  	setBetAmount(Integer.parseInt(tmp_data[3]));
						setCurrentRound(Integer.parseInt(tmp_data[4]));
						setNumContestants(Integer.parseInt(tmp_data[1]));
						setNumPlayers(Integer.parseInt(tmp_data[0]));
						setNumWeeks(Integer.parseInt(tmp_data[2]));  
						setUlimateWinner(tmp_data[8]);
						setGameEnded(Integer.parseInt(tmp_data[9]));
						
				  }
				  //Close the input stream
				  in.close();
			 }
			 catch (Exception e){//Catch exception if any
				  System.err.println("Error: " + e.getMessage());
			 }
		}
}