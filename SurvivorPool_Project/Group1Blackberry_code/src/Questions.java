import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;
/**
 * This class creates an interface, which displays the current questions added by the administrator user and allow the player to answer them
 * @author Group 1
 *
 */
public class Questions extends MainScreen implements FieldChangeListener
{
	/* Attributes*/

	private ObjectChoiceField questionsList; // Question Drop-down list
	private ButtonField goBack; // go back button
	private VerticalFieldManager mainManager; // main panel
	private Background defaultBg; // current background

	private String test; // file contents returned by the readTextFile method
	private String currentUser; // current user
	private String[] data_type_list; // first column of the text file (type)
	private String[] data_value_list; // second column of the text file (data)
	private String[] questions_list; // array containing the questions
	private int[] type_list; // array containing the question types
	private String[] multAnswers_list; // array containing the multiple answers of a specific question
	private Question_Class[] question_object_array; // array containing question objects
	private int week; 
	private ReadFile reader;
	private int i;
	private Bitmap backgroundImage; // current background// list of the week the contestants were eliminated in


	public Questions(String currentUser, int week)
	{
		//current user
		this.currentUser = currentUser;
		//set week
		this.week = week;
		//set title
		setTitle("Questions");

		//background
		defaultBg = BackgroundFactory.createLinearGradientBackground(Color.WHITE, Color.LIGHTSKYBLUE, Color.WHITE, Color.GRAY);

		//main manager
		mainManager = (VerticalFieldManager) getMainManager();
		mainManager.setBackground(defaultBg);

		// create  buttons
		goBack = new ButtonField("Go back",ButtonField.FIELD_HCENTER);

		// create reader object
		reader = new ReadFile();

		//read the question file and return its contents
		test = reader.readTextFile("Questions2.txt");

		//process the information
		process_Qfile(test);
		//create and set the question objects
		CreateAndSetQuestionObj();


		//create Question list
		questions_list = CreateQList();

		//drop-down list
		questionsList = new ObjectChoiceField("This week's Questions: ", questions_list);

		//set margins
		questionsList.setMargin(20, 0, 0, 0);
		goBack.setMargin(30, 0, 0, 0);

		//add buttons to the listener
		questionsList.setChangeListener(this);
		goBack.setChangeListener(this);


		//create image and overwrite the paint method of verticalFieldManager
		//backgroundImage = Bitmap.getBitmapResource("beach4 copy1.jpg");
		backgroundImage = Bitmap.getBitmapResource("beach5 copy copy.jpg");
		mainManager = new VerticalFieldManager(VerticalFieldManager.USE_ALL_WIDTH | VerticalFieldManager.USE_ALL_HEIGHT){
			public void paint(Graphics graphics){ 

				graphics.drawBitmap(0,0,480,510, backgroundImage, 0, 0);
				super.paint(graphics);
			}
		};
		this.add(mainManager);


		// add components
		mainManager.add(questionsList);
		mainManager.add(goBack);

		// decide which panel should display
		displayAnswer(); 


	}
	/**
	 * Method for handling Field change events
	 * @param Field The field that changed
	 * @param Information specifying the origin of the change
	 */
	public void fieldChanged(Field field, int context)
	{
		if(field == goBack)
		{
			UiApplication.getUiApplication().pushScreen(new Game(currentUser));
		}

		else if(field == questionsList)
		{
			displayAnswer();
		}
	}

	/**
	 * This method displays the correct answer field, either a short answer text box or multiple answers, depending on the question type
	 */
	private void displayAnswer()
	{

		int type = 0; // question type
		type_list = CreateQTypeList(); // retrieve types
		String question = (String) questionsList.getChoice(questionsList.getSelectedIndex());

		for(int i=0; i< questions_list.length; i++)
		{
			if(questionsList.getSelectedIndex()== i) // questionList is the drop-down list
				type = type_list[i]; 
		}


		if(type ==0 && !(question.equals("None"))) // if  type is 0, display a panel with a text box
		{
			//displayAnswer constructor format: public DisplayAnswer(int type, String[] multAnswersArray, String question,String currentUser, double QID, int week)
			UiApplication.getUiApplication().pushScreen(new DisplayAnswer(0, null, (String) questionsList.getChoice(questionsList.getSelectedIndex()),currentUser, question_object_array[questionsList.getSelectedIndex()-1].getQID(),week));

		}
		else if (type ==1 && !(question.equals("None"))) // if type is 1, display a panel with radio buttons for multiple answers
		{
			String multAnswers[]= question_object_array[questionsList.getSelectedIndex()-1].getMultAnswers(); //retrieve the multiple answers. 
			//its length minus one because of the none string
			//displayAnswer constructor format: public DisplayAnswer(int type, String[] multAnswersArray, String question,String currentUser, double QID, int week)
			UiApplication.getUiApplication().pushScreen(new DisplayAnswer(1 ,multAnswers, (String) questionsList.getChoice(questionsList.getSelectedIndex()),currentUser, question_object_array[questionsList.getSelectedIndex()-1].getQID(),week));
		}


	}

	/**
	 * This method stores the first column of the file(the data type) on one array and the second column (the data value) on another array
	 * @param contents the contents of the text file
	 */
	public void process_Qfile(String contents){

		String delimiter="-------";	// each attribute separated by --

		//Get all the rows (string array of them)
		String[] contents_split;
		contents_split=reader.split(contents,"\n");

		String[] line_split;
		data_type_list=new String[contents_split.length];// create data type array
		data_value_list=new String[contents_split.length]; // create data value array 
		
		for (int i=0; i<contents_split.length; i++){
			line_split=reader.split(contents_split[i],delimiter); // splits the contents of the file in columns
			data_type_list[i]=line_split[0];  // store the first column containing the data type
			data_value_list[i]=line_split[1];  // store the second column containing the data value
		}
	}


	/**
	 * Method to get the round from the question id
	 * THIS MIGHT NOT BE THE MOST EFFIENCENT WAY TO DO
	 * THIS BUT BLACKBERRY DOES NOT HAVE A STRING SPLIT
	 * METHOD SO I DID IT THIS WAY...
	 * @param qid
	 * @return
	 */
	public int roundInQID(String qid) {
		int count = 0;
		char[] tmp ;
		char t;
		//trim any additional white space that was read
		qid = qid.trim();
		
		char char_at_0;
		char char_at_1;
		
		char_at_0=qid.charAt(0);
		char_at_1=qid.charAt(1);
		////////////////////////////MAJOR EDIT!!!!  JONATHON's FAULT
		if (char_at_1=='.'){
			
			return Integer.parseInt(String.valueOf(char_at_0));
		} 
		else{
			return Integer.parseInt(String.valueOf(char_at_0) + String.valueOf(char_at_1));
		}
		
		
		/*
		//check the qid length to allocate the appropriate amount of space
		//for the round number
		if(qid.length()==3)
			tmp = new char[1];
		else
			tmp = new char[2];

		qid.charAt(0)
		while(count<qid.length()) {
			if((t=qid.charAt(count))!='.')
				tmp[count] = t;
			else if(qid.charAt(count)=='.')
				break;
			count++;
		}
		//return the integer stored in the character array
		return Integer.parseInt(new String(tmp));
		
		*/
	}
	/**
	 * This method creates Question objects and sets their Question string ,type, multiple answers array, and the number of multiple answers  
	 */
	public void CreateAndSetQuestionObj()
	{
		question_object_array = new Question_Class[data_type_list.length]; // array containing Question objects


		int j = 0; // counter for questions_list
		int k = 0; // counter for type_list
		int m = 0; // counter for num_list
		int n = -1; // counter to set the mulAnswerArray
		int num =0; // number of multiple Answers of an specific Question
		int h = 0; // counter for type_list
		boolean objectFlag = false;


		for(i=0; i< data_type_list.length; i++)
		{
			//copy the data in the corresponding array
			
			if(data_type_list[i].equals("QID") )
			{
				boolean flag = false;
				if(roundInQID(data_value_list[i])==week){

					// create the question object and store the question in the array 
					Question_Class question = new Question_Class();
					// set the question ID
					question.setID(Double.parseDouble(data_value_list[i].trim()));
					//store in the  object array
					question_object_array[h] = question;
					h++;
					objectFlag = true;

				}
				else {

					int x = i+1;
					objectFlag = false;
					while(x<data_type_list.length && flag == false){
						if(data_type_list[x].equals("QID" )){

							i = x-1;
							num=0;

							flag= true;		
						}

						x++;

					}
				}
			}
			else if(data_type_list[i].equals("QUESTION") && objectFlag == true)
			{
				question_object_array[j].setQuestion(data_value_list[i]);

				j++;
				n++; // n is incremented here because not every question has multiple choice, but we still need to increment the question array position to place
				// the correct answers on the right Question object
			}
			else if(data_type_list[i].equals("TYPE")&& objectFlag == true)
			{
				//store the type of the specified question.
				question_object_array[k].setType(Integer.parseInt(data_value_list[i].trim()));
				k++;
			}
			else if(data_type_list[i].equals("NUM")&& objectFlag == true)
			{

				//store the number of multiple answers of the specified question
				question_object_array[m].setNum(Integer.parseInt(data_value_list[i].trim()));
				num = question_object_array[m].getNum(); // to remember the number of multiple answers of the specified question
				m++;

			}
			else if(((data_type_list[i].equals("OPTION") || data_type_list[i].equals("CORRECT")) && num > 1) && objectFlag == true)
			{

				multAnswers_list = new String[num]; // temporary array holding the multiple answers of the specified question

				int g = 0; // counter for multAnswers_list
				// store all multiple answers of the specified question in an temporary array
				for(int counter = 0; counter < num; counter++) 
				{
					multAnswers_list[g] = data_value_list[i].trim();
					g++;
					i++;
				}
				i--; 

				multAnswers_list = reduceArraySize(multAnswers_list, g); // reduce the size
				question_object_array[n].setMultAnswerArray(multAnswers_list); // store the multiple answer array in the correct question object	

			}
		}

		question_object_array = reduceArraySize(question_object_array, j); // reduce the size of the array to avoid null pointer exceptions


	}
	/**
	 * This method creates the a list of questions 
	 * @return a String array containing questions
	 */
	private String[] CreateQList()
	{
		String[] list = new String[question_object_array.length+1]; // added one to the length for the none string
		list[0] = "None";
		for(int i=0,j=1; i< question_object_array.length; i++,j++)
		{
			list[j] = question_object_array[i].getQuestion();
		}
		return list;
	}
	/**
	 * This method creates the a list of question types 
	 * @return a String array containing question types
	 */
	private int[] CreateQTypeList()
	{
		int[] list = new int[question_object_array.length+1]; // one added for "none" question value
		list[0] = 0;
		for(int i=0, j=1; i< question_object_array.length; i++, j++)
		{
			list[j] = question_object_array[i].getType();
		}
		return list;
	}


	/**
	 * This method decreases the size of the passed array to the specified size
	 * @param array passed array
	 * @param size the passed size
	 * @return  return a array of type String, which is the smaller version of the passed array.
	 */
	private String[] reduceArraySize(String[] array, int size)
	{
		String[] smallerArray = new String[size];
		for(int i = 0; i < smallerArray.length; i++)
			smallerArray[i] = array[i];
		return smallerArray;
	}
	/**
	 * This method decreases the size of the passed array to the specified size
	 * @param array passed array
	 * @param size the passed size
	 * @return return a array of type Question_Class, which is the smaller version of the passed array.
	 */
	private Question_Class[] reduceArraySize(Question_Class[] array, int size)
	{
		Question_Class[] smallerArray = new Question_Class[size];
		for(int i = 0; i < smallerArray.length; i++)
			smallerArray[i] = array[i];
		return smallerArray;
	}

	/**
	 * This method overrides  main's OnsavePrompt method
	 * @return true if prompt's resulting choice is to save; false if the prompt's resulting choice is to cancel.
	 */
	public boolean onSavePrompt()
	{
		return true;
	}



}
