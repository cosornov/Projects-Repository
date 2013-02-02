import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Hold all the player answers
 * @author gcfy
 *
 */
public class PlayerAnswers {

	private ArrayList<PlayerAnswer> pa;	//array of player answers
	
	PlayerAnswers() {
		pa = new ArrayList<PlayerAnswer>();
	}
	
	public PlayerAnswer[] getPlayerQuestionHistory_round(String id, int round) {
		ArrayList<PlayerAnswer> history = new ArrayList<PlayerAnswer>();
		for(int i=0;i<pa.size();i++) {
			if(pa.get(i).getPlayerID().equals(id) && pa.get(i).getRound()==round) {
				history.add(pa.get(i));
			}
		}
		
		
		if(history==null) 
			return null;

		PlayerAnswer[] histArray = new PlayerAnswer[history.size()];
		
		for(int j=0;j<histArray.length;j++) {
			histArray[j] = history.get(j);
		}
		return histArray;
	}
	
	public void readFromFile(String filename) {
		BufferedInputStream in = null;
		String playerAnswer = null;
		String delim = "----";
		String[] tmpData;
		int objectsize=3;

		try {
			in = new BufferedInputStream(new FileInputStream(filename));
			//make new reader to read the file
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			
			while((playerAnswer=reader.readLine())!=null) {
				tmpData = playerAnswer.split(delim);
				if(playerAnswer.equals("") || tmpData.length<objectsize) {
					//nothing to do
				}else {
					//System.out.println("1: "+tmpData[0] + " 2: "+tmpData[1]+" 3: "+tmpData[2]);
					pa.add(new PlayerAnswer(tmpData[0],tmpData[1],tmpData[2]));
				}
			}
		}catch(FileNotFoundException e) {
			System.out.println("File reading error in PlayerAnswers");
		}catch(IOException e) {
			System.out.println("Input output exception in PlayerAnswers");
		}
	}
	/*
	public static void main(String[] args) {
		PlayerAnswers pa = new PlayerAnswers();
		pa.readFromFile("Test3.txt");
		PlayerAnswer[] hi;
		hi = pa.getPlayerQuestionHistory_round("cdub", 1);
		for(int i=0;i<hi.length;i++) {
			System.out.println("QID: "+hi[i].getQuestionID()+" Answer: " + hi[i].getAnswer() );
		}
	}
	*/
	
}
