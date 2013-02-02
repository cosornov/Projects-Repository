/**
 * Contains all the relevant information of a contestant
 * also defines all the actions of a contestant 
 * contestant is the term used for people that are on the show Survivor
 * the UWOSurvivorPool
 * 
 * inherits all attributes and methods from individual
 * @author m3t4l
 *
 */
import java.io.*;


public class Contestant extends Individual{
	private String picturepath="";		//the folder where pictures are
	private String picture_filename;	//this users picturename
	private String tribe;				//what tribe is this user in!
	private int round_eliminated;		//what round were they eliminated?
										// -1 --> not eliminated
	
	
	
	//----------------------------------------------------------------
	//Constructors	
	//----------------------------------------------------------------

	/**
	 * Set up a Contestant! 
	 * @param firstname
	 * @param lastname
	 */
	Contestant(String firstname, String lastname){
		setFName(firstname);
		setLName(lastname);
		setID("");
		setTribe("");
		setPicturePath("");
		setRoundEliminated(-1);
	}

	/**
	 * Set up a Contestant! 
	 * @param firstname
	 * @param lastname
	 * @param id
	 * @param tribe
	 */
	Contestant(String firstname, String lastname, String id, String tribe, 
				String picture_filename, int round_eliminated){
		setFName(firstname);
		setLName(lastname);
		setID(id);
		setTribe(tribe);
		setPicturePath(picture_filename);
		setRoundEliminated(round_eliminated);
	}
	
	/**
	 * Set up a Contestant! - Specifically for EliminationPanel
	 * @param firstname
	 * @param lastname
	 * @param id
	 * @param round_eliminated
	 */
	Contestant(String firstname, String lastname, String id, int round_eliminated){
		setFName(firstname);
		setLName(lastname);
		setID(id);
		setRoundEliminated(round_eliminated);
	}
	
	
	/**
	 * Generic one....just so I can create the object
	 */
	Contestant(){
		
	}
	
	
	//----------------------------------------------------------------
	//Setters	
	//----------------------------------------------------------------
	/**
	 * Set Tribe to userdefined values
	 * @param tribe
	 */
	public void setTribe(String tribe){
		this.tribe=tribe;
	}
	
	/**
	 * Set PicturePath to userdefined values
	 * @param pic_filename
	 */
	public void setPicturePath(String pic_filename){
		this.picture_filename=pic_filename;
	}
	
	
	/**
	 * Set round_eliminated to userdefined values
	 * @param pic_filename
	 */
	public void setRoundEliminated(int round){
		this.round_eliminated=round;
	}
	//----------------------------------------------------------------
	//Getters	
	//----------------------------------------------------------------
	/**
	 * Get Tribe
	 * @return tribe
	 */
	public String getTribe( ){
		return tribe;
	}
	
	/**
	 * Get PicturePat
	 * @return pic_filename
	 */
	public String getPicturePath(){
		return picture_filename;
	}
	
	/**
	 * Get round_eliminated
	 * @return round_eliminated
	 */
	public int getRoundEliminated( ){
		return round_eliminated;
	}
	
	//----------------------------------------------------------------
	//Output	
	//----------------------------------------------------------------
	/**
	 * @return String representation of object
	 * id--fname--lname-tribe-picture_path
	 */
	public String toString(){
		String tmp="";
		String delim="--";
		String filler="EMPTY";
		
		tmp=getID()+delim+getFName()+delim+getLName();
		tmp=tmp+delim;
		
		if (getTribe()==""){
			tmp=tmp+filler;
		}else{
			tmp=tmp+getTribe();
		}
		tmp=tmp+delim;
		
		
		if (getPicturePath()==""){
			tmp=tmp+filler;
		}else{
			tmp=tmp+getPicturePath();
		}
		
		tmp=tmp+delim;
		tmp=tmp+Integer.toString(getRoundEliminated());
		
		return tmp;
		
	}
	
	
	/**
	 * @return object representation of contestant
	 * id--fname--lname-tribe-picture_path
	 */
	public Object[] prepforTable(){
		Object[] ConestantData={getID(), getFName(), getLName(), getTribe(), getPicturePath(), getRoundEliminated()};
		return ConestantData;
	}
	
	
	
	/**
	 * @return object representation of contestant
	 * id--fname--lname-tribe-picture_path
	 */
	public Object[] prepforElimTable(){
		
		String hasBeenEliminated = (getRoundEliminated() != -1)?"Y":"N";
		Object[] ElimData={getID(), getFName(), getLName(), hasBeenEliminated, getRoundEliminated() };
		return ElimData;
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
