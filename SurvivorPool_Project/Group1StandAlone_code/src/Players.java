/**
 * A collection of Player objects
 * (so we can do batch operations, and move them around!)
 * @author m3t4l
 *
 */

import java.io.*;
import java.util.ArrayList;
     
public class Players {
	private int num_players;			//number of players currently
	public ArrayList<Player> population;		//collection of players
	public static int objectsize=4;		//number of attributes that will be in txtfile
	
	public static String Players_filename="PlayersList.sav";  //file that we save to!
		
	
	//--------------------------------------------------------
	// Constructors
	//--------------------------------------------------------
	
	/**
	 * Init options without any user input
	 */
	Players(){
		num_players=0;
		population=new ArrayList<Player>();
	}
	
	

	
	//--------------------------------------------------------
	// Getters
	//--------------------------------------------------------
	
	/**
	 * Get Player at index in the arraylist
	 * @return a player in list of players
	 */
	public Player getPlayer(int index){
		
		if ((index<num_players)&&(index>=0)){
			return (Player)population.get(index);
		}
			return null;
	}
	
	/**
	 * return number of players in the list
	 * @return num_players
	 */
	public int getNumPlayers(){
		return num_players;
	}
	
	
	//--------------------------------------------------------
	// Helpers
	//--------------------------------------------------------
	
	/**
	 * Creates a userid (1_fname...upto 6_lname...number if needed)
	 * @param firstname
	 * @param lastname
	 * @return userID
	 */
	public String makeUserID(String firstname, String lastname){
		
		//prepare string datas to make name
		String userID="";
		firstname=firstname.toLowerCase();
		lastname=lastname.toLowerCase();
		userID=Character.toString(firstname.toCharArray()[0]);
		char[] tmplname=lastname.toCharArray();
		
		
		//1 char of fname, and up to 6 from lsat name
		for (int i=0; (  (i<tmplname.length)&& (i<5) );  i++){
			userID=userID+Character.toString(tmplname[i]);			                                                		
		}
		
		
		//add integer to the end...if userID already exist
		int count=1;
		String tmpuserID=userID;
		while (userIDExists(tmpuserID)==true){
			
			tmpuserID=userID+Integer.toString(count);
			count=count+1;  //lets try the next number to see if exist
		}
		userID=tmpuserID;
	
		return userID;
	}
	
	/**
	 *  check if userID is already in population
	 * @param userID
	 * @return true if user id exists, false otherwise
	 */
	public boolean userIDExists(String userID){
		Player tmp;
		
		// lets check our collection of players
		for(int i=0; i<num_players; i++){

				tmp=(Player)population.get(i);  //get current player
				
				//current player has same id as userID ?
				if (tmp.getID().equalsIgnoreCase(userID)){
					
					//yup...so return true!
					if (population.get(i)!=null){		
					return true;
				}
			}
		}
				
		return false;	//userID DNE
	}


	
	//--------------------------------------------------------
	// Get Player (Find and return...)
	//--------------------------------------------------------
	/**
	 * Find a player in the array list
	 * return arraylist of all players with same first and last name
	 * @param firstname
	 * @param lastname
	 * @return players with the same first and last name
	 */
	public Players findPlayer_byNames(String firstname, String lastname){
		
		//prepare  string data
		Players tmpPlayers=new Players();
		Player tmp;
		String fname;
		String lname;
		
		firstname=firstname.toLowerCase();
		lastname=lastname.toLowerCase();
		
		
		//lets find all such players
		for (int i=0; i<num_players; i++){
			tmp=(Player)population.get(i);  //get current player
			
			fname=tmp.getFName().toLowerCase();
			lname=tmp.getLName().toLowerCase();
			//current player has same id as userID ?
			if (fname.equals(firstname) && lname.equals(lastname)){
				tmpPlayers.addPlayer(tmp,"n");
			}
			
				
		}
		
		
		//return null (if empty) or return list of players
		if (tmpPlayers.getNumPlayers()<1){
			return null;
		}
		return tmpPlayers;
		
		
	}
	
	
	/**
	 * Find a player in the array list
	 * return player with the same userID (there can only be 1)
	 * @param userID
	 * @return player with specified userID
	 */
	public Player findPlayer_byUserID(String userID){
		Player tmp;
		String tmpID;
		
		userID=userID.toLowerCase();
		
		//look through populatiion to find (or not find)  this player
		for (int i=0; i<num_players; i++){
			tmp=(Player)population.get(i);  //get current player
			tmpID=tmp.getID().toLowerCase();
			
			if (tmpID.equals(userID)){
				return tmp;	//return this one player
			}
			

		}
		return null; //not found!
	}
	
	
	//--------------------------------------------------------
	// Player Exists?  (are they actually in the arraylist)?
	//--------------------------------------------------------

	
	/**
	 * Does the player exist??
	 * return true if they exist..false if they dont
	 * @param userID
	 * @return -1 if player does not exist, another number if exists
	 */
	public int existsPlayer_byUserID(String userID){
		Player tmp;
		String tmpID;
		
		userID=userID.toLowerCase();
		
		//look through populatiion to find (or not find)  this player
		for (int i=0; i<num_players; i++){
			tmp=(Player)population.get(i);  //get current player
			tmpID=tmp.getID().toLowerCase();
			
			if (tmpID.equals(userID)){
				return i;	//return this one player
			}
			

		}
		return -1; //not found!
	}
	
	
	
	//--------------------------------------------------------
	// Delete
	//--------------------------------------------------------
	/**
	 * Delete player from the array list
	 * @param userID
	 * @return true if successful deletion, false otherwise
	 */
	public boolean deletePlayer_byUserID(String userID){
		
		int index=existsPlayer_byUserID(userID);
		if (index != -1){
			population.remove(index);
			num_players--;
			return true;
		}
		return false;
	}

	
	
	//--------------------------------------------------------
	// Add
	//--------------------------------------------------------
	/**
	 * @param newPlayer
	 * @param makeID --> Do we need to make a new ID?
	 */
	public void addPlayer(Player newPlayer, String makeID){
		
		if (makeID.equalsIgnoreCase("y")){
			String tmpID=makeUserID(newPlayer.getFName(),newPlayer.getLName());
			newPlayer.setID(tmpID);
		}
		
		population.add(newPlayer);	
		num_players++;
		
	}


	
	//--------------------------------------------------------
	// Output for User
	//--------------------------------------------------------
	/**
	* @return string representation of players info
	*/
	public String toString(){
		String tmp="";
		String nl="\n";
		
		Player tmpPlayer;
		
		for (int i=0; i<num_players; i++){
			tmpPlayer=(Player)population.get(i);
			tmp=tmp+tmpPlayer.toString()+nl;
		}
		
		
		return tmp;
	}
	
	
	/**
	 * @return stacked Object Representation of contestants
	 * (to be used for displaying this object in table on gui-side)
	 */
	public Object[][] prepforTable(){
		Object[][] usersforTable=new Object[getNumPlayers()][objectsize];
		
		for (int i=0; i<getNumPlayers(); i++){
			for (int j=0; j<objectsize; j++){
				usersforTable[i][j]=population.get(i).prepforTable()[j];
			}
		}
		return usersforTable;
	}
	
	
	/**
	 * Write object to a file
	 * @param filename
	 * id--fname--lname (looped...)
	 */
	public void toFile(String filename){

		 try{
			  // Create file 
			  FileWriter fstream = new FileWriter(filename);
			  BufferedWriter out = new BufferedWriter(fstream);
			  
			  
			  
			  String tmp="";
			  String nl="\n";
				
			  Player tmpPlayer;
				
			  for (int i=0; i<num_players; i++){
				tmpPlayer=(Player)population.get(i);
				out.write(tmpPlayer.toString());
				out.newLine();
			  }
				
			  //Close the output stream
			  out.close();
		 }
		 catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
		 }
		
	}

	//--------------------------------------------------------
	// Read from file
	//http://www.roseindia.net/java/beginners/java-read-file-line-by-line.shtml
	//--------------------------------------------------------
	
	/**
	 * Read a players object from a textfile
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
			  int tmp_num_players=0;
			  Players tmp_Players=new Players();
			  Player tmp_Player;
			  
			  
			  //Read File Line By Line
			  while ((strLine = br.readLine()) != null)   {
				  
				  //create a newtmp object to transfer...
				  strLine.replaceAll("\\n", "");
				  tmp_data=strLine.split(delim);
				
				  if (tmp_data.length != objectsize){
					  return;
				  }
				  
				  //add data to tmp object
				  tmp_Player=new Player(tmp_data[1],tmp_data[2],tmp_data[0],Integer.parseInt(tmp_data[3]),"hey", "baby");
				  tmp_Players.addPlayer(tmp_Player,"n");
			  }
			  //Close the input stream
			  in.close();
			  
			  //transfer the relevant variables
			  this.num_players=tmp_Players.num_players;
			  this.population=tmp_Players.population;
			  
		 }
		 catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
		 }
	}

}



