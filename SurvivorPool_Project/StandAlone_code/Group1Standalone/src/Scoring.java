import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

/**
 * To handle the back end scoring system!
 *
 * @author m3t4l
 *
 */
public class Scoring {
	//----------------------------------------
	//--------------Attributes
	//----------------------------------------	
	private GameSettings gs;	//contains numplayers
	//         numweeks
	//         ultimatewinner
	private Contestants	cntsts;
	private static Players	players;
	private PlayerAnswers pas;
	private Questions questions;	
	private Elimination elims;
	private static int numPlayers;
	private int numCntsts;
	private static int totalWeeks;
	private String[] playersAnswers;


	// the constants relating to scoring values
	final static int correct_question_score=10;
	final static int correct_elimination_score=20;
	final static int correct_elimination_FinalWeek_score=40;
	final static int correct_ultwinner_score=100;//remember this is based on the week they guessed..
	final static int week_of_ultWinnerPick=0;

	//the percentages of the respective winners earnings
	private final double FIRSTPLACEWINNINGS = 0.6;
	private final double FIRSTPLACETIE = 0.45;
	private final double FIRSTPLACETIES = 0.333;
	private final double SECONDPLACEWINNINGS = 0.3;
	private final double SECONDPLACETIE = 0.2;
	private final double THIRDPLACEWINNINGS = 0.1;





	//----------------------------------------
	//--------------Constructors
	//----------------------------------------

	/**
	 * Constructor
	 */
	Scoring(){

		gs = new GameSettings();
		cntsts = new Contestants();
		cntsts.resetFromFile(gs.filename_contestants);


		questions = new Questions();
		questions = questions.getFromFile(gs.filename_Qs);
		players = new Players();
		players.resetFromFile(gs.filename_players);

		pas = new PlayerAnswers();
		pas.readFromFile(gs.filename_qAnswers);

		numPlayers = gs.getNumPlayers();
		numCntsts = gs.getNumContestants();
		totalWeeks = gs.getNumWeeks();
		playersAnswers = new String[numPlayers*questions.getNumQuestions()]; //intermediate storage
	}

	//----------------------------------------
	//--------------Getters
	//----------------------------------------	
	public String[] getPlayersAnswers() {
		return playersAnswers;
	}

	/**
	 * Given a questions id, return the answer
	 * @param qID
	 * @return
	 */
	public String getQuestionAnswer(String qID) {
		Question tmp;
		tmp = questions.getQuestion(qID);
		return tmp.getCorrectAnswer();
	}

	/**
	 * Reads the players answers from the answers file
	 * and stores them into a string array
	 */
	public void getPlayersAnswersFromFile(String filename) {
		BufferedInputStream in = null;
		String playerAnswer = null;
		String delim = "----";
		String[] tmpData;
		int counter = 0;	//counter for answers array

		try {
			in = new BufferedInputStream(new FileInputStream(filename));
			//make new reader to read the file
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));

			while((playerAnswer=reader.readLine())!=null) {
				playersAnswers[counter] = playerAnswer;
				counter++;
			}
		} catch(FileNotFoundException e) {
			System.out.println("File cannot be read in scoring");
		} catch(IOException e) {
			System.out.println("IO Exception in scoring");
		}

	}



	//----------------------------------------
	//--------------Setters
	//----------------------------------------		

	/**
	 * To update the scores
	 * if save is set....then actually commit the changes
	 * (that is use...contestants.toFile(gameSettings.filename_contestants);
	 */
	public int[][] update_scores(boolean save){

		Object[][] qScores=update_scores_questions();

		Object[][] eScores=update_scores_eliminations();

		Object[] uwScores=update_scores_ultWinner();




		int[][] sumScores=new int[numPlayers][totalWeeks];

		int[] scores=new int[numPlayers];
		int tmp=0; //transfer

		for (int i=0; i<numPlayers; i++){
			scores[i]=0;
			for (int j=0; j<totalWeeks;j++){


				//System.out.println(i+" "+j);
				tmp=((Integer) qScores[i][j]).intValue()+
						((Integer) eScores[i][j]).intValue();

				//do we need to include ultimate winner score?
				if (j==totalWeeks-1){
					tmp=tmp+((Integer) uwScores[i]).intValue();
				}

				scores[i]=scores[i]+tmp;
				sumScores[i][j]=tmp;


			}
			players.getPlayer(i).setScore(scores[i]);
		}

		if (save==true){
			GameSettings gSettings=new GameSettings();
			players.toFile(gSettings.filename_players);
		}
		return sumScores;
	}


	/**
	 * get a 2d arrray 
	 * 	--each row stores the question scores of a player by week
	 * @return
	 */
	public Object[][] update_scores_questions(){
		Object[][] updatedScores=new Object[numPlayers][totalWeeks];
		int score = 0;
		Player p;
		PlayerAnswer pa;
		PlayerAnswer[] tmpAnswers;
		Question tmpQ;
		int tmp; //to transfer value of object array
		gs.update();

		questions = questions.getFromFile(gs.filename_Qs);


		for(int i=0;i<numPlayers;i++) {
			p = players.getPlayer(i);
			score = 0;


			for(int w=0;w<totalWeeks;w++) {
				if(w>=gs.getCurrentRound()) {
					//System.out.println(w);
					updatedScores[i][w] = 0;
				} else{


					tmpAnswers=pas.getPlayerQuestionHistory_round(p.getID(), w+1);
					updatedScores[i][w] = 0;


					if (tmpAnswers==null){
					} 
					else{

						for (int qr=0; qr< tmpAnswers.length; qr++){


							pa=tmpAnswers[qr];
							tmpQ=questions.getQuestion(pa.getQuestionID());

							if (tmpQ==null){
							} 
							else{


								//System.out.println(p.getID()+" "+tmpQ.getID()+" G:"+pa.getAnswer()+" Q:"+tmpQ.getCorrectAnswer());
								//is correct?

								if (pa.getAnswer().equals(tmpQ.getCorrectAnswer())){
									tmp=((Integer)updatedScores[i][w]).intValue()+ correct_question_score;

									updatedScores[i][w] =tmp;
								} else{
									updatedScores[i][w] = 0;
								}

							}

						}					

					}


				}//end else "when not null"
			}//end for loop over weeks
		}//end for loop over players
		return updatedScores;                                 
	}



	/**
	 * get a 2d arrray 
	 * 	--each row stores the eliminations scores of a player by week
	 * 
	 * Don't forget to include the 'ultimate winner' score!!!
	 * @return
	 */
	public Object[][] update_scores_eliminations(){
		//total weeks-1 b/c first week is week 0
		Object[][] updatedScores=new Object[numPlayers][totalWeeks]; 
		cntsts.resetFromFile(gs.filename_contestants);
		players.resetFromFile(gs.filename_players);
		Ballot b;
		String tmpCntstVote;
		String tmpPlayerID;
		Contestant c;
		Player p;
		int score;
		int userIndex;

		gs.update();
		elims = new Elimination();
		//elims.errorCheck();
		elims.getFromFile(gs.filename_eliminations);


		for(int i=0;i<numPlayers;i++) {
			p = players.getPlayer(i);
			score = 0;
			userIndex= elims.findUserIndex(p.getID());


			for(int w=0;w<totalWeeks;w++) {


				if(w>=gs.getCurrentRound()) {
					updatedScores[i][w] = 0;
				} else{



					//	b = elims.getBallot(i, w);
					b = elims.getBallot(userIndex, w);

					if (b==null){
						System.out.println("BALLOT ERROR IN scoring: update_scores_eliminations()");
						return null;
					}


					c = cntsts.findContestant_byID(b.getContestantIDVote().trim());
					if(c==null) {
						

						/*System.out.println("Invalid ID");*/
						updatedScores[i][w] = 0;

					}
					else if ((b.getRoundNum()==gs.getNumTotalRounds()) &&
							b.getContestantIDVote().trim().equals(gs.getUltimateWinner())){


						score += correct_elimination_FinalWeek_score;
						updatedScores[i][w] = correct_elimination_FinalWeek_score;

					}
					else if(c.getRoundEliminated()==b.getRoundNum()) {

						score += correct_elimination_score;
						updatedScores[i][w] = correct_elimination_score;

					}

					else {
						updatedScores[i][w] = 0;
					}

				}
			}
		}

		return updatedScores;                                    
	}


	/**
	 * update scores based on ult winner vote
	 *return null if not everyone has voted!!!!!!!!!!!
	 * @return
	 */
	public Object[] update_scores_ultWinner(){
		gs.update();
		Object[] scores= new Object[numPlayers];

		//set to 0
		for (int i=0; i<numPlayers; i++){
			scores[i]=0;
		}


		//check if we can update there scores
		UltimateWinner uw= new UltimateWinner();
		uw.readFromFile(gs.filename_ultWinner);
		Player p;
		Ballot b;
		Contestants cs=new Contestants();
		cs.resetFromFile(gs.filename_contestants);
		String ultWinner=gs.getUltimateWinner();
		Boolean ultWinnerExists=(cs.findContestant_byID(ultWinner)!=null);


		for (int i=0; i<numPlayers; i++){
			p=players.getPlayer(i);

			b=uw.findBallotByPlayer(p.getID());

			//have they actually voted??
			if(b.getContestantIDVote().trim().equals("")){
				//System.out.println(p.getID()+" has not voted on an UltWinner!!!! ERRROR");
			}
			else if ((gs.getCurrentRound()==gs.getNumTotalRounds())&&
					(ultWinnerExists==true)){
			//cool, they have, assign points if its final round!
			//and if an ultimate winner has been set
			
				//we can give them real scores

				if (b.getContestantIDVote().trim().equals(ultWinner)){
					if( b.getRoundNum()<=0){
						scores[i]=2*gs.getNumContestants();
					} else{
						//total contestants  -  remaining cntsts
						scores[i]=2*(gs.getNumContestants()-(b.getRoundNum()-1));
						//-1 becuase in 1st round all contestants exist...
						//2nd round, 1 player has been elimated etc...

					}

				}

			} 


		}


		return scores;
	}



	//----------------------------------------
	//--------------Helper Methods
	//----------------------------------------	
	private double[] setWinningAmt() {
		//the total amount of money in the pot
		int pot=0;
		//the winnings assigned will be placed here with element 0=1st place,
		//1=2nd place, 2=3rd place
		double[] placing = new double[3];
		//get the total pot
		GameSettings gs = new GameSettings();
		pot=gs.getTotalPot();

		/*if(p1.getScore()==p2.getScore()==p3.getScore()) {
			placing[0] = pot*FIRSTPLACETIES;
		} else if(p1.getScore()>p2.getScore() && p2.getScore()==p3.getScore()) {
			placing[0] = pot*FIRSTPLACEWINNINGS;
			placing[1] = pot*SECONDPLACETIE;
		} else if(p1.getScore()==p2.getScore() || p1.get)
			placing[0] = pot*FIRSTPLACEWINNINGS;
			placing[1] = pot*SECONDPLACEWINNINGS;
			placing[2] = pot*THIRDPLACEWINNINGS;*/
		return placing;

		/*if(p1.getScore()>p2.getScore() && p1.getScore()>p3.getScore())
			placing[0] = pot*FIRSTPLACEWINNINGS;
		else if(p2.getScore()>p3.getScore()) {
			placing[1] = pot*SECONDPLACEWINNINGS;
			placing[2] = pot*THIRDPLACEWINNINGS;
			return placing;
		}else {
			placing[1] = pot*
		}*/
	}


	//----------------------------------------
	//--------------Output Methods
	//----------------------------------------	


	/**NOT DONE
	 * To display the individual scoring
	 * for each round including their bonus 
	 * @return
	 */
	public Object[][] prepScoreTable() {
		//totalWeeks*2+1 for each round and round bonus
		Object[][] prepScoreTable = new Object[numPlayers][totalWeeks+1];

		//store the ids in the first column
		for(int k=0;k<numPlayers;k++) {
			prepScoreTable[k][0] = players.getPlayer(k).getID();
		}

		int[][] sumScores=update_scores(true);

		String delim="--";
		for (int i=0; i<numPlayers; i++){
			//System.out.print(prepScoreTable[i][0]);
			for (int j=1; j<=totalWeeks;j++){
				prepScoreTable[i][j]=sumScores[i][j-1];
				//System.out.print(delim+prepScoreTable[i][j]);


			}
			System.out.println();
		}




		return prepScoreTable;
	}

	/**
	 * Player at p[length-1] gets double[0]
	 * Player at p[length-2] gets double[1]
	 * Player at p[length-3] gets double[2]
	 * !!!!!!!!!!!CHANGE GETKEY to GETSCORE!!!!!!!!
	 * @param p
	 * @return
	 */
	public double[] assignMoneyToWinners(Player[] p) {

		//the total amount of money in the pot 

		int pot=0;

		//the winnings assigned will be placed here with element 0=1st place,
		//1=2nd place, 2=3rd place

		double[] placing = {0,0,0};
		
		if(p[0]==null)
			return placing;
		//reset and get the most recent game settings

		gs.resetFromFile(gs.filename_settings);

		//get the total amount of money

		pot=gs.getTotalPot();

		//split first place winnings

		if((p[p.length-1].getScore() == p[p.length-2].getScore()) && 

				(p[p.length-1].getScore() == p[p.length-3].getScore())) {

			placing[0] = pot*FIRSTPLACETIES;

			placing[1] = pot*FIRSTPLACETIES;

			placing[2] = pot*FIRSTPLACETIES;

			return placing;

			//first place, two tie for second

		} else if((p[p.length-1].getScore() > p[p.length-2].getScore()) && 

				(p[p.length-2].getScore() == p[p.length-3].getScore())) {

			placing[0] = pot*FIRSTPLACEWINNINGS;

			placing[1] = pot*SECONDPLACETIE;

			placing[2] = pot*SECONDPLACETIE;

			return placing;

			//two tie first place, one for third

		} else if((p[p.length-1].getScore() == p[p.length-2].getScore()) && 

				(p[p.length-1].getScore() > p[p.length-3].getScore())) {

			placing[0] = pot*FIRSTPLACETIE;

			placing[1] = pot*FIRSTPLACETIE;

			placing[2] = pot*THIRDPLACEWINNINGS;

			return placing;

			//first place, 2nd, 3rd

		} else if((p[p.length-1].getScore() > p[p.length-2].getScore()) && 

				(p[p.length-2].getScore() > p[p.length-3].getScore())) {

			placing[0] = pot*FIRSTPLACEWINNINGS;

			placing[1] = pot*SECONDPLACEWINNINGS;

			placing[2] = pot*THIRDPLACEWINNINGS;

			return placing;

		}

		return placing;

	}


	/**
	 * Reads in all the players scores and calculates
	 * the three highest scores
	 * CHANGE pARRAY TO  GLOBAL VARIABLE
	 * @param players object with all their scores;
	 */
	public Player[] setScoreOrdering(Players p, int round) {

		Player[] pArray = new Player[p.getNumPlayers()];

		if(round!=gs.getNumWeeks()) {

			System.out.println("Not in final round!");
			return pArray;

		}

		else {

			for(int i=0;i<p.population.size();i++) {

				pArray[i] = p.getPlayer(i);

			}

			for(int j=0; j<pArray.length;j++){



				sort(pArray, pArray.length);  

			}

		}

		return pArray;

	}


	/**
	 * From rose india, insertion sort algorithm
	 * CHANGE THE GETKEY METHOD TO GETSCORE
	 * @param p
	 * @param n
	 */
	private static void sort(Player p[], int n){

		for (int i=1;i<n;i++){

			int j = i;

			Player tmp = p[i];

			while ((j > 0) && (p[j-1].getScore() > tmp.getScore())){

				p[j] = p[j-1];

				j--;

			}

			p[j] = tmp;

		}

	}


	/**
	 * Round double to 2 places
	 * Use on the double integer array!
	 * @param d
	 * @return
	 */
	public double roundTwoPlaces(double d) {

		DecimalFormat twoDForm = new DecimalFormat("#.##");

		return Double.valueOf(twoDForm.format(d));

	}


/*
	public static void main(String[] args) {
		Scoring sc = new Scoring();
		GameSettings gs= new GameSettings();
		sc.getPlayersAnswersFromFile(gs.filename_qAnswers);
		String[] hi = sc.getPlayersAnswers();
		//System.out.println(hi[0]);
		//	sc.update_scores_questions();
		//System.out.println("total weeks/: "+ totalWeeks);
		//Object[][] ob = sc.update_scores_eliminations();
		//Object[][] ob = sc.update_scores_questions();

		for(int i=0;i<numPlayers;i++) {
			for(int j=0;j<totalWeeks;j++) {
				System.out.print(ob[i][j] + " ");
			}
			System.out.println();
		}
		 
		players.resetFromFile(gs.filename_players);
		sc.prepScoreTable();
		//sc.elims.toFile("fail.txt");
		sc.elims.prepFile(4, gs.filename_eliminations);
		Player[] p = sc.setScoreOrdering(players, gs.getCurrentRound());
		double[] boobs = sc.assignMoneyToWinners(p);
		for(int i=0;i<boobs.length;i++) {
			System.out.println("Place " +(i+1) + " " + sc.roundTwoPlaces(boobs[i]));
			System.out.println();
		}
	}
*/

}
