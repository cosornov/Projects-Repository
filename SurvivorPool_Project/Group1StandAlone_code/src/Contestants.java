/**
 * A collection of Contestant objects
 * (so we can do batch operations, and move them around!)
 * @author m3t4l
 *
 */

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Contestants {
	private int num_contestants;			//number of players currently
	public ArrayList<Contestant> population;		//collection of players
	public static int objectsize=6;		//number of attributes that will be in txtfile
	
	public static String Contestants_Filename="ContestantssList.sav";  //file that we save to!

	
	//--------------------------------------------------------
	// Constructors
	//--------------------------------------------------------
	
	/**
	 * Init options without any user input
	 */
	Contestants(){
		num_contestants=0;
		population=new ArrayList<Contestant>();
	}
	
	
	//--------------------------------------------------------
	// Getters
	//--------------------------------------------------------
	/**
	 * Get Contestant at index in the arraylist
	 * @param index
	 */
	public Contestant getContestantint(int index){
		
		if ((index<num_contestants)&&(index>=0)){
			return (Contestant)population.get(index);
		}
			return null;
	}
	
	/**
	 * return number of Contestant in the list
	 * @return -number of Contestant in the list
	 */
	public int getNumContestants(){
		return num_contestants;
	}
	
	
	
	//--------------------------------------------------------
	// Helpers
	//--------------------------------------------------------
	
	/**
	 * Creates a CntstID (2random chars)
	 * @return userID - tmpid
	 * http://stackoverflow.com/questions/2626835/is-there-functionality-to-generate-a-random-character-in-java
	 */
	public String makeCntstID(){
		
		//prepare string datas to make name
		Random r = new Random();
		String tmpid="";
		
	    String alphabet = "abcdefghijklmnopqrstuvwxyz";
	    for (int i = 0; i < 2; i++) {
	    	tmpid=tmpid+alphabet.charAt(r.nextInt(alphabet.length()));
	    } 
		
	
	  //add integer to the end...if userID already exist
		int count=1;
		while (cntstIDExists(tmpid)==true){
			tmpid="";
		    for (int i = 0; i < 2; i++) {
		    	tmpid=tmpid+alphabet.charAt(r.nextInt(alphabet.length()));
		    } 
		}
		
		
		return tmpid;
	}

	
	/**
	 *  check if CntstID is already in population
	 * @param CntstID
	 * @return true or false depending on if contestnat id exists
	 */
	public boolean cntstIDExists(String cntstID){
		Contestant tmp;
		
		// lets check our collection of players
		for(int i=0; i<num_contestants; i++){

				tmp=(Contestant)population.get(i);  //get current player
				
				//current player has same id as userID ?
				if (tmp.getID().equalsIgnoreCase(cntstID)){
					
					//yup...so return true!
					if (population.get(i)!=null){		
					return true;
				}
			}
		}
				
		return false;	//userID DNE
	}



	//--------------------------------------------------------
	// Get Contestant (Find and return...)
	//--------------------------------------------------------
	/**
	 * Find a Contestant in the array list
	 * return arraylist of all Contestants with same first and last name
	 * @param firstname
	 * @param lastname
	 * @return Contestants - tmpContestants
	 */
	public Contestants findContestant_byNames(String firstname, String lastname){
		
		//prepare  string data
		Contestants tmpContestants=new Contestants();
		Contestant tmp;
		String fname;
		String lname;
		
		firstname=firstname.toLowerCase();
		lastname=lastname.toLowerCase();
		
		
		//lets find all such players
		for (int i=0; i<num_contestants; i++){
			tmp=(Contestant)population.get(i);  //get current player
		
			
			fname=tmp.getFName().toLowerCase();
			lname=tmp.getLName().toLowerCase();
			//current player has same id as userID ?
			if (fname.equals(firstname) && lname.equals(lastname)){
				//System.out.println(tmp.toString()+"<--");
				tmpContestants.addContestant(tmp,"n");
			}
			
				
		}
		
		
		//return null (if empty) or return list of players
		if (tmpContestants.getNumContestants()<1){
			return null;
		}
		return tmpContestants;
		
		
	}
	
	
	/**
	 * Find a Contestant in the array list
	 * return Contestant with the same cntstID (there can only be 1)
	 * @param cntstID
	 * @return Contestant - tmp
	 */
	public Contestant findContestant_byID(String cntstID){
		Contestant tmp;
		String tmpID;
		
		cntstID=cntstID.toLowerCase();
		
		//look through populatiion to find (or not find)  this player
		for (int i=0; i<num_contestants; i++){
			tmp=(Contestant)population.get(i);  //get current player
			tmpID=tmp.getID().toLowerCase();
			
			if (tmpID.equals(cntstID)){
				
				return tmp;	//return this one player
			}
			

		}
		return null; //not found!
	}


	
	
	//--------------------------------------------------------
	// Contestant Exists?  (are they actually in the arraylist)?
	//--------------------------------------------------------

	
	/**
	 * Does the Contestant exist??
	 * return true if they exist..false if they dont
	 * @param cntstID
	 * @return int - -1 for no, anything else for yes
	 */
	public int existsContestant_byUserID(String cntstID){
		Contestant tmp;
		String tmpID;
		
		cntstID=cntstID.toLowerCase();
		
		//look through populatiion to find (or not find)  this player
		for (int i=0; i<num_contestants; i++){
			tmp=(Contestant)population.get(i);  //get current player
			tmpID=tmp.getID().toLowerCase();
			
			if (tmpID.equals(cntstID)){
				return i;	//return this one player
			}
			

		}
		return -1; //not found!
	}


	
	//--------------------------------------------------------
	// Delete
	//--------------------------------------------------------
	/**
	 * Delete Contestant from the array list
	 * @param cntstID
	 * @return true if successful, false if not
	 */
	public boolean deleteContestant_byUserID(String cntstID){
		
		int index=existsContestant_byUserID(cntstID);
		if (index != -1){
			population.remove(index);
			num_contestants--;
			return true;
		}
		return false;
	}

	
	//--------------------------------------------------------
	// Add
	//--------------------------------------------------------
	/**
	 * @param newContestant
	 * @param makeID  --> Do we want to create an ID?
	 */
	public void addContestant(Contestant newContestant, String makeID){
		
		if (makeID.equalsIgnoreCase("y")){
			String tmpID=makeCntstID();
			newContestant.setID(tmpID);
		}
		
		population.add(newContestant);	
		num_contestants++;
	}


	
	
	
	//--------------------------------------------------------
	// Helper
	//--------------------------------------------------------
	/**
	 * To check if a contestnat has already been elimiated on a given week
	 * We can use this to determine when we are doing weekly eliminations
	 * it will prevent multiple contestants from being elimiated on same week
	 */
	public	Contestant	isSomeoneElseAlreadyElimiated_week(int week, String cID){
		Contestant tmpCntst=new Contestant();
		boolean found=false;
		
		for (int i=0; i<getNumContestants(); i++){
		
			tmpCntst=population.get(i);
	
			if (!tmpCntst.getID().equals(cID)){
				
				if ((tmpCntst.getRoundEliminated()==week) && (week!=-1)){
					found=true;
					return tmpCntst;
				}
				
			}else{
				//do nothing	
			}
		}
		
		if (found==true){
			return tmpCntst;
		} else{
			return null;
		}
		
		
	}
	
	
	
	
	//--------------------------------------------------------
	// Output for Contestant
	//--------------------------------------------------------
	/**
	* @return String - tmp containing contestants info
	*/
	public String toString(){
		String tmp="";
		String nl="\n";
		
		Contestant tmpContestant;
		
		for (int i=0; i<num_contestants; i++){
			tmpContestant=(Contestant)population.get(i);
			tmp=tmp+tmpContestant.toString()+nl;
		}
		return tmp;
	}
	
	
	/**
	 * @return stacked Object Representation of contestants
	 * (to be used for displaying this object in table on gui-side)
	 */
	public Object[][] prepforTable(){
		Object[][] contestantsForTable=new Object[getNumContestants()][objectsize];
		
		for (int i=0; i<getNumContestants(); i++){
			for (int j=0; j<objectsize; j++){
				contestantsForTable[i][j]=population.get(i).prepforTable()[j];
			}
		}
		return contestantsForTable;
	}
	
	/** Added!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 * prepforElimTable Method - to be used for displaying Contestants in EliminationPanel
	 * @return stacked Object Representation of Contestants
	 */
	public Object[][] prepforElimTable(){
		int elimTableDataObjectSize=6;
		
		Object[][] contestantsForElimTable = new Object[getNumContestants()][elimTableDataObjectSize];
		Contestant tmpCntst;
		GameSettings gameSettings= new GameSettings();
		
		for (int i=0; i<getNumContestants(); i++){
			
			tmpCntst=population.get(i);
			for (int j=0; j<elimTableDataObjectSize; j++){
				
				if (j<5){
					contestantsForElimTable[i][j] = tmpCntst.prepforElimTable()[j];
				} else{
					
					
					if (tmpCntst.getID().equals(gameSettings.getUltimateWinner())){
						contestantsForElimTable[i][j] = "Y";
					}
					else{
						contestantsForElimTable[i][j] = "N";
					}
					
					
				}
			}
		}
		return contestantsForElimTable;
	}
	
	
	/**
	 * Write object to a file
	 * @param filename
	 * id--fname--lname-tribe-picture_path (looped...)
	 * 
	 * tribe, picture_path not set--> "EMPTY"
	 */
	public void toFile(String filename){

		 try{
			  // Create file 
			  FileWriter fstream = new FileWriter(filename);
			  BufferedWriter out = new BufferedWriter(fstream);
			  
			  
			  
			  String tmp="";
			  String nl="\n";
				
			  Contestant tmpContestant;
				
			  for (int i=0; i<num_contestants; i++){
				  tmpContestant=(Contestant)population.get(i);
				  out.write(tmpContestant.toString());
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
			  int tmp_num_players=0;
			  Contestants tmp_Contestants=new Contestants();
			  Contestant tmp_Contestant;
			  
			  
			  //Read File Line By Line
			  while ((strLine = br.readLine()) != null)   {
				  
				  //create a newtmp object to transfer...
				  strLine.replaceAll("\\n", "");
				  tmp_data=strLine.split(delim);
				
				  if (tmp_data.length != objectsize){
					  return;
				  }
				  
				  //add data to tmp object
				  tmp_Contestant=new Contestant(tmp_data[1],tmp_data[2],tmp_data[0],
						  						tmp_data[3],tmp_data[4], 
						  						Integer.parseInt(tmp_data[5]));
				  tmp_Contestants.addContestant(tmp_Contestant,"n");
			  }
			  //Close the input stream
			  in.close();
			  
			  //transfer the relevant variables
			  this.num_contestants=tmp_Contestants.num_contestants;
			  this.population=tmp_Contestants.population;
			  
		 }
		 catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
		 }
	}

}
