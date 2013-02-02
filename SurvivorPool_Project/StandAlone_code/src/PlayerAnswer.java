/**
 * Class that stores a players answers
 * @author gcfy
 *
 */
public class PlayerAnswer {

	private String playerID, questionID, answer;
	private int round;

	PlayerAnswer(String playerID, String questionID, String answer) {
		this.playerID = playerID;
		this.questionID = questionID;
		this.answer = answer;
		this.round = roundFromID(questionID);
	}

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public String getQuestionID() {
		return questionID;
	}

	public void setQuestionID(String questionID) {
		this.questionID = questionID;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}
	
	/**
	 * return integer of the round...
	 * 1.2 --> 1
	 */
	public int roundFromID(String ID){
		String[] tmp=ID.split("\\.");
		return Integer.parseInt(tmp[0]);
	}
}	
