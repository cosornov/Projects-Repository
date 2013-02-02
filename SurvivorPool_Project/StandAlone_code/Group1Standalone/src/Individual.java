/**
 * This is a generic person.  Player, and Contestant are persons.
 * So they can inherit from this class.
 * @author m3t4l
 *
 */
public class Individual {
	private String fname;	//firstname of the person
	private	String lname;	//lastname of the person
	private String id;		// user id
	private int key;		//primary key  -- to be used if in a list of player objects
	
	
	//----------------------------------------------------------------
	//Constructors	
	//----------------------------------------------------------------

	/**
	 * Declare fname and lname as empty
	 */
	Individual(){
		fname="";
		lname="";
		id="";
		key=-1;
	}
	
	/**
	 * Set fname and lname to userdefined values
	 * @param firstname
	 * @param lastname
	 */
	Individual(String firstname, String lastname){
		fname=firstname;
		lname=lastname;
		key=-1;
	}
	

	
	
	//----------------------------------------------------------------
	//Setters	
	//----------------------------------------------------------------
	
	/**
	 * Set fname and lname to userdefined values
	 * @param firstname
	 * @param lastname
	 */
	public void setName(String firstname, String lastname){
		setFName(firstname);
		setLName(lastname);
	}
	
	/**
	 * Set fname to userdefined values
	 * @param firstname
	 */
	public void setFName(String firstname){
		fname=firstname;
	}
	
	/**
	 * Set lname to userdefined values
	 * @param firstname
	 */
	public void setLName(String lastname){
		lname=lastname;
	}
	

	
	/**
	 * Set id to userdefined values
	 * @param firstname
	 */
	public void setID(String player_id){
		id=player_id;
	}
	
	
	/**
	 * Set key to userdefined values
	 * @param firstname
	 */
	public void setKey(int key){
		this.key=key;
	}
	//----------------------------------------------------------------
	//Getters	
	//----------------------------------------------------------------
	/**
	 * Get fname
	 * @return fname
	 */
	public String getFName(){
		return fname;
	}
	

	/**
	 * Get lname
	 * @return lname
	 */
	public String getLName(){
		return lname;
	}


	/**
	 * Get id
	 * @return id
	 */
	public String getID(){
		return id;
	}
	
	
	/**
	 * Get key
	 * @return key
	 */
	public int getKey(){
		return key;
	}
}
