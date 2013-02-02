import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Display the winners of the game
 * after the game has ended
 * @author swatso33
 *
 */
public class DisplayWinners {

	private String[] winnersInfo;
	
	//constructor
	DisplayWinners(String filename) {
		try{
		//open input stream
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(filename));
		//read input from file
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		//Strings + String arrays
		winnersInfo = new String[3];
		String tmpInfo;
		int count = 0;
		while((tmpInfo=reader.readLine())!= null) {
			winnersInfo[count] = tmpInfo;
			count++;
		}
		
		}catch(FileNotFoundException e) {
			System.out.println("Error with file in Display Winners");
			e.printStackTrace();
		}catch(IOException e1) {
			System.out.println("Error with input/output in Display Winners");
			e1.printStackTrace();
		}
	}
	
	/**
	 * return the winners in a string array
	 * @return
	 */
	public String[] getInfo() {
		return winnersInfo;
	}
	
	/**
	 * Take the winning players info and turn it
	 * into a reasonable output
	 * @param blah
	 */
	public String[] makeReadable(String[] blah) {
		String[] plyr1 = blah[0].split("--");
		String[] plyr2 = blah[1].split("--");
		String[] plyr3 = blah[2].split("--");
		
		String[] nice = new String[4];
		String tab = "   ";
		
		for(int i=0;i<nice.length;i++) {
			if(i==0) {
				nice[i] = "Dear Administrator, \n \n";
			}
			else if(i==1){
			nice[i] = "In Rank "+plyr1[1] +", "+plyr1[0]+ " has scored " + plyr1[2] + " points and thus earns  $ " + plyr1[3]+". \n";
			} else if(i==2) {
				nice[i] = "In Rank "+plyr2[1] +", "+plyr2[0]+ " has scored " + plyr2[2] + " points and thus earns  $ " + plyr2[3]+". \n";
			} else {
				nice[i] = "In Rank "+plyr3[1] +", "+plyr3[0]+ " has scored " + plyr3[2] + " points and thus earns  $ " + plyr3[3]+". \n";
				nice[i]=nice[i]+"  \n Yours Truly, \n  Group 101";
			}
		}
		
		return nice;
	}
	
	public static void main(String[] args) {
		DisplayWinners dw = new DisplayWinners("winners.txt");
		String[] tmp = dw.getInfo();
		String[] another = dw.makeReadable(tmp);
		
		for(int i=0;i<another.length;i++) {
			System.out.println(another[i]);
		}		
	}
}
