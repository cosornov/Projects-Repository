/**
 * Contains all the relevant information of a player
 * also defines all the actions of a player 
 * Player is the term used for people that are betting in..
 * the UWOSurvivorPool
 * 
 * inherits all attributes and methods from individual
 * @author m3t4l
 *
 */
import java.io.*;
public class Player extends Individual{
	private int score;
	
	//----------------------------------------------------------------
	//Constructors	
	//----------------------------------------------------------------

	/**
	 * Set up a player! 
	 * (id is defined from the Players class)
	 * @param firstname
	 * @param lastname
	 */
	Player(String firstname, String lastname){
		setFName(firstname);
		setLName(lastname);
		setID("");
		setScore(0);
	}

	/**
	 * Constructor that sets up a player! 
	 * (id is defined from the Players class)
	 * @param firstname
	 * @param lastname
	 * @param id
	 */
	Player(String firstname, String lastname, String id){
		setFName(firstname);
		setLName(lastname);
		setID(id);
		setScore(0);

	}
	
	
	
	/** Set up a player! 
	 * (id is defined from the Players class)
	 * @param firstname
	 * @param lastname
	 * @param id
	 * @param key
	 */
	Player(String firstname, String lastname, String id, int key){
		setFName(firstname);
		setLName(lastname);
		setID(id);
		setKey(key);
		setScore(0);
	}
	
	/** Set up a player! 
	 * (id is defined from the Players class)
	 * @param firstname
	 * @param lastname
	 * @param id
	 * @param key
	 */
	Player(String firstname, String lastname, String id, int key, String blah, String blahblah){
		setFName(firstname);
		setLName(lastname);
		setID(id);
		setKey(key);
		setScore(key);
	}
	
	
	
	/** Set up a player! 
	 * (id is defined from the Players class)
	 * @param firstname
	 * @param lastname
	 * @param id
	 * @param key
	 * @param score
	 */
	Player(String firstname, String lastname, String id, int key, int score){
		setFName(firstname);
		setLName(lastname);
		setID(id);
		setKey(key);
		setScore(score);
	}
	
	//----------------------------------------------------------------
	//Setters
	//----------------------------------------------------------------
	//----------------------------------------------------------------
	/**
	 *Set user's score
	 * @param uscore
	 */
	public void setScore(int uscore){
		score=uscore;
	}
	
	
	//----------------------------------------------------------------
	//Getters
	//----------------------------------------------------------------
	/**
	 * give back the user's score
	 * @return score
	 */
	public int getScore(){
		return score;
	}
	
	//----------------------------------------------------------------
	//Output	
	//----------------------------------------------------------------
	/**
	 * @return String representation of object
	 * id--fname--lname
	 */
	public String toString(){
		String tmp="";
		String delim="--";
		
		tmp=getID()+delim+getFName()+delim+getLName()+delim+getScore();
		return tmp;
		
		
		
	}
	
	
	/**
	 * @return object representation of contestant
	 * id--fname--lname-tribe-picture_path
	 */
	public Object[] prepforTable(){
		Object[] ConestantData={getID(), getFName(), getLName(),getScore()};
		return ConestantData;
	}
	
	
	
	
	
	/**
	 * Write object to a file
	 * @param filename
	 * id--fname--lname
	 */
	public void toFile(String filename){

		 try{
			  // Create file 
			  FileWriter fstream = new FileWriter(filename);
			  BufferedWriter out = new BufferedWriter(fstream);
			  out.write(toString());
			  //Close the output stream
			  out.close();
		 }
		 catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
		 }
		
	}
	
}
