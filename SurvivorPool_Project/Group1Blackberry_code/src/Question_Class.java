/**
 * @author group1
 * Class that holds all information dealing with
 * a question and its answer(s) along with user answer
 */
public class Question_Class
{


	/*Question Attributes*/
	private String question;		//The question
	private int type;			//question type
	private int numOfChoices;		// number of multiple answers
	private String[] MultAnswerArray;
	private double QID;
	/*End Question Attributes*/

	/**
	 * Question constructor
	 */
	public Question_Class()
	{

	}

	/**
	 * Sets the type
	 * @param type Question type
	 */
	public void setType(int type) {
		this.type = type;

	}
	/**
	 * Sets the number of multiple choices
	 * @param num Possible answer to question
	 */
	public void setNum(int num) {
		this.numOfChoices = num;

	}
	/**
	 * Sets the the question ID
	 * @param QID passed question ID
	 */
	public void setID(double QID) {
		this.QID = QID;

	}
	
	/**
	 * Sets the question
	 * @param question question to be set
	 */
	public void setQuestion(String question) {
		this.question = question;

	}

	/**
	 * Sets the multipleAnswer array for a question containing multiple choice answers
	 * @param MultAnswerArray  array containing multiple answers
	 */
	public void setMultAnswerArray(String[] MultAnswerArray) {
		this.MultAnswerArray = new String[MultAnswerArray.length];
		for(int i= 0; i< MultAnswerArray.length ; i++ )
		{
			this.MultAnswerArray[i] = MultAnswerArray[i];
		}


	}

	/**
	 * Sets the players answer to the question
	 * @param user_answers Players answers to question
	 */
	public void setUser_Answers(String[] user_answers) {
		

	}
	/**
	 * Get the number of multiple answers for a specified question
	 * @return  number of multiple answers
	 */
	public int getNum() {
		return numOfChoices;

	}
	/**
	 * Get Question ID
	 * @return  question ID
	 */
	public double getQID() {
		return QID;

	}

	/**
	 * Get the question
	 * @return  question
	 */
	public String getQuestion() {

		return question;
	}
	/**
	 * Get the Question Type
	 * @return The Question Type
	 */
	public int getType() {

		return type;
	}


	/**
	 * Gets all the players answers
	 * @return the players answers to question
	 */
	public String[] getUsers_Answers() {
		return null;

	}
	/**
	 * Gets the array containing multiple answers
	 * @return multAnswers array
	 */
	public String[] getMultAnswers() {
		return MultAnswerArray;

	}
}