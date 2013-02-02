/**
 * Class that deals with the players weekly votes
 * Includes the playerID, the current round of the game
 * and their contestant chosen to be eliminated
 * @author gcfy
 *
 */
public class Ballot {

	/*Attributes*/
	private String playerID;
	private int roundNum;
	private String contestantIDVote;
	private int playerNum;
	
	/*Constructor*/
	public Ballot(String player, int round, String contestant) {
		this.playerID = player;
		//this.playerNum = playerNum;
		this.roundNum = round;
		this.contestantIDVote = contestant;
	}
	
	/*Setters*/
	/**
	 * @param player
	 */
	public void setPlayerID(String player) {
		this.playerID = player;
	}
	
	public void setPlayerNum(int num) {
		this.playerNum = num;
	}

	/**
	 * 
	 * @param round
	 */
	public void setRoundNum(int round) {
		this.roundNum = round;
	}

	/**
	 * 
	 * @param contestant
	 */
	public void setContestantIDVote(String contestant) {
		this.contestantIDVote = contestant;
	}
	
	/*Getters*/
	/**
	 * @return playerID
	 */
	public String getPlayerID() {
		return playerID;
	}
	
	public int getPlayerNum() {
		return playerNum;
	}
	
	/**
	 * 
	 * @return roundNum
	 */
	public int getRoundNum() {
		return roundNum;
	}

	/**
	 * 
	 * @return contestantIDVote
	 */
	public String getContestantIDVote() {
		return contestantIDVote;
	}
	
	/**
	 * prints the players' ballot
	 * @return ballotString
	 */
	public String toString(){
		String delim="--";
	
		return playerID+delim+Integer.toString(roundNum)+delim+contestantIDVote;
	}
}
