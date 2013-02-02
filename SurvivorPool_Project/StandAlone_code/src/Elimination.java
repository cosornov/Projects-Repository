import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


/**
 * Class dealing with who was eliminated and to see who voted for who
 * Array list of array lists, every array list element holds a ballot
 * but the each new element is a new round
 * @author gcfy
 *
 */
public class Elimination {

	/*Attributes*/
	private static  ArrayList<ArrayList<Ballot>> playersVotes;
	private static int numPlayers;
	private int totalRounds;
	private int currRound;
	private GameSettings gs;
	private final int objectsize = 3;
	private int roundCount;
	//private String 
	
	

	/*Constructor*/
	public Elimination() {
		gs = new GameSettings();
		numPlayers = gs.getNumPlayers();
		totalRounds = gs.getNumTotalRounds();
		currRound = gs.getCurrentRound();
		playersVotes = new ArrayList<ArrayList<Ballot>>(numPlayers);
		//make an array list(num players) of array lists(num rounds)
		for(int i=0;i<numPlayers;i++) {
			playersVotes.add(i,new ArrayList<Ballot>());
			/*System.out.println("List size: "+playersVotes.size());*/
		}
		//createBallots();
		//getFromFile("elims.txt");
	}

	/**
	 * Finds the user by their user id and returns the index 
	 * of the array where they are located
	 * @return the index of the user
	 */
	public int findUserIndex(String id) {
		for(int i=0;i<numPlayers;i++) {
			if(playersVotes.get(i).isEmpty()){
				//does not exist
				return i;  //<---- wtf is going on here?
							// if nothign exists in this array element we pass it?
							//so that it can be filled?
			} else{
				if(playersVotes.get(i).get(0).getPlayerID().equals(id))
					return i;
			}
		}
		//If the user was not found
		return -1;
	}

	/**
	 * set the users random vote at the specified index
	 * @return randomVoteID - the randomly chosen contestant id
	 */
	public String getRandomVote() {
		/*the randomization variables needed*/
		Contestants tmp = new Contestants();
		Contestant randomCntst;
		String randomVoteID = null;
		Random randomGenerator;
		/*get a random index from the possible contestants*/
		tmp.resetFromFile(gs.filename_contestants);
		randomGenerator = new Random();
		int num;
		int maxIts=10;
		int counter = 0;
		
		/*Make sure the contestant has not been eliminated*/
		do{
		num	 = randomGenerator.nextInt(tmp.population.size());
		randomCntst = tmp.population.get(num);
		randomVoteID = randomCntst.getID();
		counter++;
		}
		while((randomCntst.getRoundEliminated()!=-1) && (counter < maxIts));
			return randomVoteID;
	}

	/**
	 * Sets each players weekly vote
	 * @param theBallot - players weekly ballot
	 */
	public void setWeeklyVote(Ballot theBallot) {
		//Set the delimiter
		String delim = "--";
		String tmpBallot[] = theBallot.toString().split(delim);
		int playerIndex = findUserIndex(tmpBallot[0]);
		
		if(playersVotes.get(playerIndex).isEmpty()){
			playersVotes.get(playerIndex).add(theBallot);
			//System.out.println(playersVotes.get(playerIndex).get(0).toString());
		}else{
				playersVotes.get(playerIndex).add(theBallot);
				//System.out.println(playersVotes.get(playerIndex).get(Integer.parseInt(tmpBallot[1])-1).toString());
			
		}
	}
	

	
	
	/**
	 * ?????Why does this return a string...pretty sure this is not used?
	 * 
	 * Gets one ballot by the specified id and week
	 * @param id
	 * @param week
	 * @return
	 */
	public static String getBallot(String id, int week) {
		String ballot = "";
		
		if(week==playersVotes.get(0).size()) {
			return ballot = "Week out of bounds";
		}
		
		for(int i=0;i<numPlayers;i++) {
			if(playersVotes.get(i).get(week).getPlayerID().equals(id)) {
				ballot = playersVotes.get(i).get(week).toString();
				return ballot;
			}
		}
		return ballot = "No ballot found or player does not exist";
	}
	

	
	
	
	
	public Ballot getBallot(int player, int week) {
		//we need to check that a ballot can exist here!
		if(week==playersVotes.get(0).size()) {
			return null; //week out of bounds
		}
		
		return playersVotes.get(player).get(week);
	}
	
	/**
	 * 
	 * @param player
	 * @param week
	 * @return
	 */
	public String getCntstVote(int player, int week) {
		return playersVotes.get(player).get(week).getContestantIDVote();
	}
	
	public String getPlayerID(int player, int week) {
		return playersVotes.get(player).get(week).getPlayerID();
	}
	
	/**
	 * Get the number of weeks in each round
	 * @return
	 */
	public int getWeeks() {
		return playersVotes.get(0).size();
	}
	

	/**
	 * @return the players votes, by week
	 */
	public String toString_by_week() {
		String out="";
		String eol = System.getProperty("line.separator");
		int tmp = 0;
		for(int i=0;i<numPlayers;i++) {
			if(tmp==playersVotes.get(i).size())
				break;
			//write the players ballot to the file
			out+=playersVotes.get(i).get(tmp).toString();
			out += eol;
			//reset the counters and continue printing
			if(i+1==numPlayers){
				i = -1;
				tmp++;
				out += eol;
			}
		}
		return out;
	}
	
	/**
	 *  @return the players votes, by player
	 */
	public String toString_by_player() {
		String out = "";
		String eol = System.getProperty("line.separator");
		Iterator iter = playersVotes.iterator();
		
		while(iter.hasNext()) {
			out += iter.next();
			out += eol;
		}
		return out;
	}
	
	public static int numBallots() {
		int numBallots = 0;
		int tmp = 0;
		for(int i=0;i<numPlayers;i++) {
			if(tmp==playersVotes.get(i).size())
				break;
			numBallots++;
			//reset the counters and continue printing
			if(i+1==numPlayers){
				i = -1;
				tmp++;
			}
		}
		return numBallots;
	}

	
	
	/**
	 *Write the eliminations to the file
	 *PROBLEM, Cannot read the last 12 or 13 votes 
	 * @param - the filname to write to 
	 */
	/*
	public void toFile(String filename){
		try{
			int count = 0;
			
		
			// Create file 
			File file = new File(filename);
			BufferedWriter out = new BufferedWriter(new FileWriter(file.getName()));

			int tmp = 0;
			for(int i=0;i<numPlayers;i++) {
				if(tmp==playersVotes.get(0).size())
					break;
				//write the players ballot to the file
				out.write(playersVotes.get(i).get(tmp).toString());
				out.newLine();
				//System.out.println("success "+ count++);
				//reset the counters and continue printing
				if(i+1==numPlayers){
					out.newLine();
					i = -1;
					tmp++;
				}
			}
			//Close the output stream
			out.close();
		}
		catch (Exception e){//Catch exception if any
			System.err.println("Error in writing to file: " + e.getMessage());
		}
	}
	*/

	/*Gets all the votes from the file*/
	public void getFromFile(String filename) {
		BufferedInputStream in = null;
		String playerInfo = null;
		String delim = "--";
		String[] tmpData;
		String tmpID;
		Ballot tmpBallot;
		try {
			in = new BufferedInputStream(new FileInputStream(filename));

			//make new reader to read the file
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));

			while((playerInfo=reader.readLine()) != null) {
				tmpData = playerInfo.split(delim);
				
				if(playerInfo.equals("")) {
					roundCount++;
				//	System.out.println("Entering round " + roundCount);
				}else{
					//Give the player a random vote if they have not voted
					if(tmpData.length !=objectsize){
						tmpID = getRandomVote();
						tmpBallot = new Ballot(tmpData[0],Integer.parseInt(tmpData[1]),tmpID);
						setWeeklyVote(tmpBallot);
						
					}else{
						tmpBallot = new Ballot(tmpData[0],Integer.parseInt(tmpData[1]),tmpData[2]);
						setWeeklyVote(tmpBallot);
						
					}
					//System.out.println("The random id is: " + tmpBallot.getContestantIDVote() + " Playerid: " 
							//+ tmpBallot.getPlayerID() + " round: " + tmpBallot.getRoundNum());
				}
			}
			//Close the input stream reader
			in.close();
		}catch (FileNotFoundException e) {
			System.err.println("Error getting from" + filename);
		}catch (IOException e1) {
			System.err.println("Error getting from" + filename + "2");
		}
	}

	
	
	
	public void prepFile(int round, String filename){
		BufferedInputStream in = null;
		String playerInfo = null;
		String delim = "--";
		String[] tmpData;
		String tmpID;
		Ballot tmpBallot;

		if ((round>0)&&(round<=gs.getNumWeeks())){
			
				
			//read in entire file to stringarray
			try{
				
				gs.update();
				  // Create file 
				  FileWriter fstream = new FileWriter(filename,true);
				  BufferedWriter out = new BufferedWriter(fstream);
				  
				  if (round!=1){
					  out.newLine();
				  }
				  
				  //System.out.println("="+filename);
				  Player p;
				  Players ps=new Players();
				  ps.resetFromFile(gs.filename_players);
				  //System.out.println("="+gs.getNumPlayers());
				  for (int i=0; i<gs.getNumPlayers(); i++){
					  
					  //System.out.println("==="+i+"/"+gs.getNumPlayers());
					  p=ps.getPlayer(i);
					  out.write(p.getID()+delim+round+delim+"  ");
					  
					  if (i!=gs.getNumPlayers()-1){
						  out.newLine();
					  }
				  }
				  //System.out.println("=ihjv");

				  //Close the output stream
				  out.close();
			}
			catch (Exception e){//Catch exception if any
				  System.err.println("Error: " + e.getMessage());
			}
				 

		
			
		} else{
			return;
		}
		
	}
	
	
	public void errorCheck() {
		Elimination e = new Elimination();
		
		e.getFromFile(gs.filename_eliminations);
		//System.out.println(e.getBallot(6, 1).getContestantIDVote());
	}
	
	
	/*public static void main(String[] args) {
		//Testing adding new ballots
		Elimination e = new Elimination();
		e.getFromFile(file_eliminationAnswers);
		System.out.println(e.getBallot(6, 1).getContestantIDVote());
		//e.toFile("elims.txt");
		//e.toFile("elims.txt");
		/*String hi = "";
		hi = getBallot("gyeung4", 2);
		System.out.println(hi);
		System.out.println(e.toString_by_week());
		System.out.println(e.toString_by_player());
		System.out.println(numBallots());
		//e.getFromFile("elims.txt");
		//System.out.println(e.toString());

	}*/
	
}
