import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import net.rim.device.api.io.IOUtilities;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.RadioButtonField;
import net.rim.device.api.ui.component.RadioButtonGroup;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;


//CREAT A READ AND WRITE FILE CLASS
/**
 * This class creates an interface, which displays the correct answer field for the specified question.
 * @author Group 1
 *
 */
public class DisplayAnswer extends MainScreen implements FieldChangeListener 
{
	/* Attributes*/

	private LabelField questionLabel; // label containing the question
	private LabelField answer;
	private EditField answerField;
	private ButtonField submit; // submits the answer
	private ButtonField goBack;
	private VerticalFieldManager mainManager; // main panel
	private VerticalFieldManager vfm;
	private Background defaultBg; // current background
	private Background labelBg; 
	private String currentUser;
	private int type; // question type
	private String[] multAnswersArray; // array containing the multiple answers
	private String userRBGAnswer; // user answer from radio buttons
	private RadioButtonField[] RBG_array; // array containing the radio buttons
	private double QID;
	private int week;
	private ReadFile reader;
	private Bitmap backgroundImage; // current background// list of the week the contestants were eliminated in
	private RadioButtonGroup radioBGroup;

	/**
	 * Initialise all the attributes and creates the interface
	 * @param message Message to be display on the screen
	 */
	public DisplayAnswer(int type, String[] multAnswersArray, String question,String currentUser, double QID, int week)
	{
		//current user
		this.currentUser = currentUser;

		// set the radio button answer String
		userRBGAnswer = null;
		//set week
		this.week = week;
		//set question id
		this.QID = QID;

		// create reader object
		reader = new ReadFile();

		//set type
		this.type = type;
		// set array only if an array is passed
		if(type == 1)
		{
			// multAnswerArray
			this.multAnswersArray = multAnswersArray;
		}

		// radio button answer
		userRBGAnswer = new String();

		//set screen title
		setTitle("Questions");

		//backgrounds
		defaultBg = BackgroundFactory.createLinearGradientBackground(Color.WHITE, Color.LIGHTSKYBLUE, Color.WHITE, Color.GRAY);
		labelBg = BackgroundFactory.createLinearGradientBackground(Color.LIGHTBLUE,Color.WHITESMOKE, Color.LIGHTBLUE, Color.LIGHTBLUE);

		//main manager
		mainManager = (VerticalFieldManager) getMainManager();
		mainManager.setBackground(defaultBg);

		//create labels
		questionLabel = new LabelField(question);
		questionLabel.setBackground(labelBg);
		answer = new LabelField("Answer[Case sensitive]: ");

		//create text fields
		answerField = new EditField("", null);

		// Create border for the answer field
		Border roundedBorder = BorderFactory.createRoundedBorder(new net.rim.device.api.ui.XYEdges(5,5,5,5));
		answerField.setBorder(roundedBorder);

		//create button
		submit = new ButtonField("Submit", ButtonField.FIELD_HCENTER);
		goBack = new ButtonField("Go Back", ButtonField.FIELD_HCENTER);

		//add button to the listener
		submit.setChangeListener(this);
		goBack.setChangeListener(this);

		//set margins
		questionLabel.setMargin(5, 0, 0, 7);
		answer.setMargin(15, 0, 0, 0);
		answerField.setMargin(12, 0, 0, 0);
		submit.setMargin(30, 0, 0, 50);
		goBack.setMargin(30, 0, 0, 15);

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

		// vertical manager
		vfm = new VerticalFieldManager();
		mainManager.add(vfm);

		// horizontal manager
		HorizontalFieldManager hfm = new HorizontalFieldManager();
		mainManager.add(hfm);

		//add components to the panel
		vfm.add(questionLabel);
		vfm.add(answer);
		displayAnswer(); 
		hfm.add(submit);
		hfm.add(goBack);


	}

	/**
	 * Method for handling Field change events
	 * @param Field The field that changed
	 * @param Information specifying the origin of the change
	 */
	public void fieldChanged (Field field, int context)
	{
		if(field == submit)
		{
			if(type == 0){
				if(answerField.getText().length()!=0)
				{
					//read the answer file and return its contents
					String test = reader.readTextFile("Test3.txt");
					reader.process_File(test, 6);
					String [] users = reader.getFirstField();
					String [] qid = reader.getSecondFieldString();
					String [] answer = reader.getThirdField();
					boolean found = false;
					for (int i = 0; i < users.length; i++){
						if(users[i].compareTo(currentUser) == 0){
							if(qid[i].compareTo(String.valueOf(this.QID)) == 0){
								answer[i] = answerField.getText() + "\r";
								found = true;
								break;
							}

						}
					}
					if (found){
						String output = "";
						for (int i=0; i<users.length; i++){
							output+= users[i] + "----" + String.valueOf(qid[i]) + "----" + answer[i] + "\n";
						}
						reader.writeTextFile("Test3.txt", output);
					}
					else{
						//process the information
						String newFileContents = process_Afile(test, currentUser + "----" + this.QID + "----" + answerField.getText() + "\r\n");
						reader.writeTextFile("Test3.txt",newFileContents); // save to file
					}
					UiApplication.getUiApplication().pushScreen(new Confirmation(currentUser,"Your answer has been submitted.", true));
				}
				else
					Dialog.alert("Answer field empty");
			}
			else
			{

				getUserRBGAnswer(); // set the userRBGAnswer
				if(userRBGAnswer.compareTo("") != 0)
				{
					//read the answer file and return its contents
					String test = reader.readTextFile("Test3.txt");
					reader.process_File(test, 6);
					String [] users2 = reader.getFirstField();
					String [] qid2 = reader.getSecondFieldString();
					String [] answer2 = reader.getThirdField();
					boolean found = false;
					for (int i = 0; i < users2.length; i++){
						if(users2[i].compareTo(currentUser) == 0){
							if(qid2[i].compareTo(String.valueOf(this.QID)) == 0){
								answer2[i] = userRBGAnswer + "\r";
								found = true;
								break;
							}

						}
					}
					if (found){
						String output = "";
						for (int i=0; i<users2.length; i++){
							output+= users2[i] + "----" + String.valueOf(qid2[i]) + "----" + answer2[i] + "\n";
						}
						reader.writeTextFile("Test3.txt", output);
					}
					else
					{
						String newFileContents = process_Afile(test, currentUser + "----" + this.QID + "----" + userRBGAnswer + "\r\n");
						reader.writeTextFile("Test3.txt",newFileContents); // save the information
					}

					UiApplication.getUiApplication().pushScreen(new Confirmation(currentUser,"Your answer has been submitted.", true));
				}
				else
					Dialog.alert("Please select an answer");
			}

		}
		if(field == goBack)
		{
			UiApplication.getUiApplication().pushScreen(new Questions(currentUser, week));
		}
	}
	/**
	 * This method displays the correct answer field, a text box or radio buttons depending of the question type
	 */
	private void displayAnswer()
	{
		if(type==0) // display a text box
		{
			vfm.add(answerField);
		}
		else // display radio buttons
		{
			radioBGroup = new RadioButtonGroup();
			RBG_array = new RadioButtonField[multAnswersArray.length]; // Initialise the radio button array
			int num = multAnswersArray.length;

			for(int i=0; i<num; i++)
			{
				RadioButtonField contest1Rbg = new RadioButtonField(multAnswersArray[i],radioBGroup,false); // create radio button
				vfm.add(contest1Rbg); // add it to the screen
				RBG_array[i] = contest1Rbg;
			}
		}
	}

	/**
	 * This method determines which radio button was selected
	 */
	private void getUserRBGAnswer()
	{
		if(radioBGroup.getSelectedIndex() < 0)
			return;
		for(int i=0;i< RBG_array.length;i++)
		{
			if(RBG_array[i].isSelected())
			{
				userRBGAnswer = RBG_array[i].getLabel();
			}
		}
		
		
	
		  
	}

	/**
	 * This method writes to a text file
	 * @param fName Name of the file
	 * @param text Text to store in the file
	 */
	public void writeTextFile(String fName, String text) {
		DataOutputStream os = null;
		FileConnection fconn = null;
		fName="File:///SDCard/BlackBerry/"+fName;

		try {

			fconn = (FileConnection) Connector.open(fName, Connector.READ_WRITE);
			if (!fconn.exists()){
				fconn.create();
			}else{

			}
			os = fconn.openDataOutputStream();
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

	//GET RID OF THE BLANK IN THE NEW FILE
	public String process_Afile(String contents, String newData){

		//Get all the rows (string array of them)
		String[] contents_split;
		contents_split=reader.split(contents,"\n");

		String string = new String();
		for(int i=0; i < contents_split.length; i++)
		{
			if (contents_split[i].length() < 3){

			}
			else{
				string = string + contents_split[i] + "\n" ;
			}

		}
		string = string + newData;

		return string;

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
