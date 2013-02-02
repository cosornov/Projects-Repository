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

/**
 * Class that handles the ultimate winner picks 
 * of the players
 * @author gcfy
 *
 */
public class UltimateWinner {
	
	/*Attributes*/
	private ArrayList<Ballot> ultWinnerVotes; 	//ultimate winner picks
	private int numPlayers; 					//number of players
	private int totalRounds;					//total rounds in the game
	private int currRound;						//
	private GameSettings gs;					//the game's main information
	private int index;
	
	/*Constructor*/
	public UltimateWinner() {
		gs = new GameSettings();
		numPlayers = gs.getNumPlayers();
		totalRounds = gs.getNumTotalRounds();
		currRound = gs.getCurrentRound();
		ultWinnerVotes = new ArrayList<Ballot>(numPlayers);
	}
	
	/**
	 * finds and returns the ballot of a player
	 * 
	 * @return
	 */
	public Ballot findBallotByPlayer(String id) {
		
		for(int i=0;i<ultWinnerVotes.size();i++) {
			if(ultWinnerVotes.get(i).getPlayerID().equals(id))
				return ultWinnerVotes.get(i);
		}
		//ballot not found
		return null;
	}
	
	/**
	 * Give the user id, return their ultimate winner vote
	 * @param id
	 * @return
	 */
	public String getVoteOnBallot(String id) {
		for(int i=0;i<ultWinnerVotes.size();i++) {
			if(ultWinnerVotes.get(i).getPlayerID().equals(id))
				return ultWinnerVotes.get(i).getContestantIDVote();
		}
		//ballot not found
		return null;
	}
	
	/**
	 * Allows the ultimate winner pick of any player 
	 * to be rechosen
	 */
	public void rechooseWinner(String playerid, String cntst) {
		Players p = new Players();
		Player p1;
		p.resetFromFile("text1.txt");
		p1 = p.findPlayer_byUserID(playerid);
		
		
		for(int i=0;i<ultWinnerVotes.size();i++) {
			if(ultWinnerVotes.get(i).getPlayerID().equals(p1.getID())){
				ultWinnerVotes.get(i).setRoundNum(currRound);
				ultWinnerVotes.get(i).setContestantIDVote(cntst);
				break;
			}
		}

		toFile("ult.txt");
	}
	/**
	 * Write to file
	 * @param filename
	 */
	public void toFile(String filename) {
		try{
			int counter = 0;
			BufferedWriter out = new BufferedWriter(new FileWriter(filename));
			
			while(counter<ultWinnerVotes.size()) {
				out.write(ultWinnerVotes.get(counter).toString());
				out.newLine();
				counter++;
			}
			//Close the output stream
			out.close();
		}
		catch (Exception e){//Catch exception if any
			System.err.println("Error in writing to file: " + e.getMessage());
		}
	}
	
	/**
	 * Get the players ultimate winner votes from a file
	 */
	public void readFromFile(String filename) {
		BufferedInputStream in = null;
		String tmpUltWinner = null;
		String delim = "--";
		String[] tmpData;
		Ballot tmpBallot;
		
		try {
			in = new BufferedInputStream(new FileInputStream(filename));
			//make new reader to read the file
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			//keep reading until the the file is empty
			while((tmpUltWinner = reader.readLine())!=null) {
				tmpData = tmpUltWinner.split(delim);
				tmpBallot = new Ballot(tmpData[0],Integer.parseInt(tmpData[1]),tmpData[2]);
				ultWinnerVotes.add(index++, tmpBallot);
			}
			//return ultWinnerVotes;
		} catch (FileNotFoundException e) {
			System.err.println("Error reading Ultimate Winner textfile");
		} catch (IOException e) {
			System.err.println("Error with IO in Ultimate Winner");
		}
	}
	
	
	/*
	public static void main(String[] args) {
		UltimateWinner uw = new UltimateWinner();
		uw.readFromFile("ult.txt");
		uw.rechooseWinner("ndub", "qv");
		System.out.println(uw.getVoteOnBallot("ndub"));
	}
	*/
	
}
