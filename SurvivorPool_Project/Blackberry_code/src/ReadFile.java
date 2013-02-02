import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import net.rim.device.api.io.IOUtilities;

/**
 * This class reads from and writes to a text file
 * @author Group 1
 *
 */
public class ReadFile {

	/**
	 * initialises the attributes
	 */
	private String [] first_field, second_field, third_field, fourth_field; // store data read from the fields
	ReadFile()
	{
	}

	/**
	 * This method reads from a text file and returns its contents
	 * @param fName Name of the file
	 * @return returns the contents of the file if the file is found; null otherwise
	 */
	public String readTextFile(String fName) {
		String result = null;
		FileConnection fconn = null;
		DataInputStream is = null;

		//read the file
		try {

			fconn = (FileConnection)Connector.open("file:///SDCard/BlackBerry/"+fName);
			is = fconn.openDataInputStream();
			byte[] data = IOUtilities.streamToBytes(is);
			result = new String(data);
			is.close();	//http://www.blackberry.com/knowledgecenterpublic/livelink.exe/fetch/2000/348583/800451/800563/How_To_-_Close_connections.html?nodeid=1261294&vernum=0
			fconn.close();

		} catch (Exception e) {


			System.out.println(e.getMessage());
		} 

		return result; // return the file contents
	}

	/**
	 * This method writes to a text file
	 * @param fName Name of the file
	 * @param text Text to store in the file
	 */
	public void writeTextFile(String fName, String text) {
		DataOutputStream os = null;
		FileConnection fconn = null;

		try {

			fconn = (FileConnection) Connector.open("file:///SDCard/BlackBerry/"+fName);
			if (!fconn.exists()){
				fconn.create();
			}else{
				fconn.truncate(0);
			}
			os = fconn.openDataOutputStream();
			System.out.println(text);
			os.write(text.getBytes());
		} 
		catch (IOException e) {
			System.out.println(e.getMessage());
		} 
		finally {
			try {
				if (null != os)
					os.close();
				if (null != fconn)
					fconn.close();
			} 
			catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	 * This class process the contents of a file and returns a String array 
	 * @param contents Contents of a file
	 * @param a mode for what needs to be read
	 * @return String array containing user IDs
	 */
	public void process_File(String contents, int mode){

		//int Usersobjectsize=4;  // (4 attributes stored per user)
		String delimiter = new String();
		if (mode != 6)
			delimiter="--";	// each attribute separated by --
		else
			delimiter="----";
		//Get all the rows (string array of them)
		String[] contents_split;
		contents_split=split(contents,"\n");

		//get each attribute from each row
		String[] line_split;
		first_field =new String[contents_split.length];
		second_field = new String[contents_split.length];
		third_field = new String[contents_split.length];
		fourth_field = new String[contents_split.length];
		for (int i=0; i<contents_split.length; i++){
			line_split=split(contents_split[i],delimiter);
			
			// Mode for just reading user IDs
			if (mode == 1){
				first_field[i]=line_split[0];  //only take the user_id and store it
			}
			
			// Mode for reading contestants file
			else if (mode == 2){
				first_field[i] = line_split[0];	//read contestant
				second_field[i] = line_split[5].trim(); //read their elimination week
			}
			
			// Mode for reading users file
			else if (mode == 3) {
				first_field[i] = line_split[0];		// read the user ID
				second_field[i] = line_split[3].trim();	// read their score
			}
			else if (mode == 4) {
				first_field[i] = line_split[0];
				if(line_split.length > 2){
					second_field[i] = line_split[1];
					third_field[i] = line_split[2];
				}
				else{
					second_field[i] = "";
					third_field[i] = "";
				}
			}
			else if (mode == 5){
				second_field[i] = line_split[4];
			}
			else if (mode == 6){
				first_field[i] = line_split[0];
				if(line_split.length > 2){
					second_field[i] = line_split[1];
					third_field[i] = line_split[2];
				}
				else{
					second_field[i] = "";
					third_field[i] = "";
				}
			}
			else if (mode == 7){
				if (line_split.length < 4)
					return;
				else{
					first_field[i] = line_split[0];
					second_field[i] = line_split[1];
					third_field[i] = line_split[2];
					fourth_field[i] = line_split[3].trim();
				}
			}
			else if (mode == 8){
				second_field[i] = line_split[9];
			}
			else if (mode == 9){
				first_field[i] = line_split[2];
				second_field[i] = line_split[4];
			}
		}
	}

	/**
	 * This method splits the contents of the file 
	 * @param strString Contents of the file
	 * @param strDelimiter Specified delimiter
	 * @return an string array
	 */
	public String[] split(String strString, String strDelimiter)
	{
		int iOccurrences = 0;
		int iIndexOfInnerString = 0;
		int iIndexOfDelimiter = 0;
		int iCounter = 0;

		// Check for null input strings.
		if (strString == null)
		{
			throw new NullPointerException("Input string cannot be null.");
		}
		// Check for null or empty delimiter
		// strings.
		if (strDelimiter.length() <= 0 || strDelimiter == null)
		{
			throw new NullPointerException("Delimeter cannot be null or empty.");
		}

		// If strString begins with delimiter
		// then remove it in
		// order
		// to comply with the desired format.

		if (strString.startsWith(strDelimiter))
		{
			strString = strString.substring(strDelimiter.length());
		}

		// If strString does not end with the
		// delimiter then add it
		// to the string in order to comply with
		// the desired format.
		if (!strString.endsWith(strDelimiter))
		{
			strString += strDelimiter;
		}

		// Count occurrences of the delimiter in
		// the string.
		// Occurrences should be the same amount
		// of inner strings.
		while((iIndexOfDelimiter= strString.indexOf(strDelimiter,iIndexOfInnerString))!=-1)
		{
			iOccurrences += 1;
			iIndexOfInnerString = iIndexOfDelimiter + strDelimiter.length();
		}

		// Declare the array with the correct
		// size.
		String[] strArray = new String[iOccurrences];

		// Reset the indices.
		iIndexOfInnerString = 0;
		iIndexOfDelimiter = 0;

		// Walk across the string again and this
		// time add the
		// strings to the array.
		while((iIndexOfDelimiter= strString.indexOf(strDelimiter,iIndexOfInnerString))!=-1)
		{

			// Add string to
			// array.
			strArray[iCounter] = strString.substring(iIndexOfInnerString, iIndexOfDelimiter);

			// Increment the
			// index to the next
			// character after
			// the next
			// delimiter.
			iIndexOfInnerString = iIndexOfDelimiter + strDelimiter.length();

			// Inc the counter.
			iCounter += 1;
		}
		return strArray;
	}
	
	/**
	 * Returns the first field from the file
	 * @return Either user ID or contestant ID depending on mode
	 */
	public String [] getFirstField(){
		return first_field;
	}
	
	/**
	 * Returns the second field from the file. First converts the 
	 * strings to integers.
	 * @return Elimination week or score depending on mode
	 */
	public int [] getSecondField(){
		int [] int_scores = new int[second_field.length];
		for (int i=0; i<second_field.length; i++){
			if (second_field[i].compareTo("") == 0){
				int_scores[i] = 0;
			}
			else{
				int_scores[i]=Integer.parseInt(second_field[i]);
			}

		}
		return int_scores;
	}
	
	public String [] getSecondFieldString(){
		return second_field;
	}
	
	public String [] getThirdField(){
		return third_field;
	}
	
	public String [] getFourthField(){
		return fourth_field;
	}
	
	public void write(String file, String user, String field, String week, int mode){
		if (mode == 1){
			for (int i=0; i < this.first_field.length; i++){
				if (this.first_field[i].compareTo(user) == 0){
					this.second_field[i] = week;
					this.third_field[i]=field + "\r";
					break;
				}
			}
		}
		else if (mode == 2){
			for (int i=0; i < this.first_field.length; i++){
				if (this.first_field[i].compareTo(user) == 0){
					if (this.second_field[i].compareTo(week) == 0){
						this.third_field[i]=field + "\r";
						break;
					}
				}
			}

		}
		String output = "";
		for (int i=0; i<this.first_field.length; i++){
			if (this.first_field[i].compareTo("\r") != 0 && this.first_field[i].compareTo("") != 0){
				output+= this.first_field[i] + "--" + this.second_field[i] + "--" + this.third_field[i] + "\n";
			}
			else{
				output+="\r\n";
			}
		}
		
		writeTextFile(file, output);
	}
	


}
