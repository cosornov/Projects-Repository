import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;

/**
 * This class creates an interface, which displays all the remaining contestants and lets the user choose a contestant he/she thinks will be eliminated
 * @author Group
 *
 */
public class WeeklyPick extends MainScreen implements FieldChangeListener 
{
	/* attributes */

	private LabelField mainLabel; //label on top of the page
	private ObjectChoiceField contestantsList; // contestants id drop-down list
	private ButtonField vote; // vote button
	private ButtonField goBack; // go back button
	private VerticalFieldManager mainManager; // main panel
	private Background defaultBg; // current background
	private Background labelBg; 	// main label background
	private String[] contestants, remaining; // contestant and remaining contestant lists
	private int[] elim_week;	// the week each contestant was eliminated
	private String user;
	private int week;
	private Bitmap backgroundImage; // current background// list of the week the contestants were eliminated in

	/**
	 * Initialise all the attributes and creates the interface
	 */
	public WeeklyPick(String user, int week)
	{
		this.user = user;
		this.week = week;
		
		//set screen title
		setTitle("Weekly Pick");

		//background
		defaultBg = BackgroundFactory.createLinearGradientBackground(Color.WHITE, Color.LIGHTSKYBLUE, Color.WHITE, Color.GRAY);
		labelBg = BackgroundFactory.createLinearGradientBackground(Color.WHEAT,Color.WHEAT, Color.WHEAT, Color.WHITESMOKE);

		//main manager
		mainManager = (VerticalFieldManager) getMainManager();
		mainManager.setBackground(defaultBg);

		
		//read the contestant info and get the remaining ones
		readContestants();
		getRemaining();
		
		// create dropdown list
		contestantsList = new ObjectChoiceField("", remaining);
		
		//create labels
		if(!finalRound()){
			mainLabel = new LabelField("Who do you think will be eliminated this week?");
		}
		else{
			mainLabel = new LabelField("Of the final contestants who do you think will win the game?");
		}
		
		mainLabel.setBackground(labelBg);
		
		// create  buttons
		vote = new ButtonField("Vote");
		goBack = new ButtonField("Go back");


		//set margins
		mainLabel.setMargin(0, 0, 0, 0);
		contestantsList.setMargin(40,0,0,0);
		vote.setMargin(100, 0, 0, 80);
		goBack.setMargin(100, 0, 0, 20);

		//add buttons to the listener
		vote.setChangeListener(this);
		goBack.setChangeListener(this);
		
		//create image and overwrite the paint method of verticalFieldManager
		backgroundImage = Bitmap.getBitmapResource("contest460.jpg");
		mainManager = new VerticalFieldManager(VerticalFieldManager.USE_ALL_WIDTH | VerticalFieldManager.USE_ALL_HEIGHT){
			public void paint(Graphics graphics){ 

				graphics.drawBitmap(0,0,480,510, backgroundImage, 0, 0);
				super.paint(graphics);
			}
		};
		this.add(mainManager);
		
		// vertical manager
		VerticalFieldManager vfm = new VerticalFieldManager();
		mainManager.add(vfm);
		//horizontal manager
		HorizontalFieldManager hfm = new HorizontalFieldManager();
		mainManager.add(hfm);

		//add components
		vfm.add(mainLabel);
		vfm.add(contestantsList);
		hfm.add(vote);
		hfm.add(goBack);


	}
	
	/**
	 * This method reads the contestant data from a file and gets
	 * the list of contestants and their elimination weeks
	 */
	private void readContestants(){
		// create user object
		ReadFile reader = new ReadFile();

		//get entire file as a string
		String test = reader.readTextFile("cntst_text2.txt");

		//get the array for contestants and the week they were eliminated
		reader.process_File(test, 2);
		contestants = reader.getFirstField();
		elim_week = reader.getSecondField();
	}
	
	/**
	 * This method extracts the remaining contestants from the list
	 * of all contestants
	 */
	private void getRemaining(){
		String [] tmp_remaining = new String[contestants.length];
		int remaining_count = 0;
		
		//Read the remaining contestants
		for(int i=0; i<elim_week.length; i++){
			if (elim_week[i] == -1){
				tmp_remaining[remaining_count] = contestants[i];
				remaining_count++;
			}
		}
		
		//Remove the extra length from the array
		remaining = new String[remaining_count];
		for (int i=0; i<remaining_count; i++){
			remaining[i] = tmp_remaining[i];
		}
	}
	
	private boolean finalRound(){
		ReadFile reader = new ReadFile();
		String text = reader.readTextFile("settings.txt");
		reader.process_File(text, 9);
		if (reader.getFirstField()[0].compareTo(reader.getSecondFieldString()[0]) == 0){
			return true;
		}
		else
			return false;
	}
	
	/**
	 * This method overrides  main's OnsavePrompt method
	 * @return true if prompt's resulting choice is to save; false if the prompt's resulting choice is to cancel.
	 */	
	public boolean onSavePrompt()
	{
		return true;
	}

	/**
	 * Method for handling Field change events
	 * @param Field The field that changed
	 * @param Information specifying the origin of the change
	 */
	public void fieldChanged(Field field, int context)
	{
		if(field == vote)
		{
			changePick((String) contestantsList.getChoice(contestantsList.getSelectedIndex()));
			UiApplication.getUiApplication().pushScreen(new Confirmation(user, "Your vote has been submitted.", false));

		}
		else if(field == goBack)
		{
			UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
		}
	}
	
	private void changePick(String pick){
		ReadFile reader = new ReadFile();
		//get entire file as a string
		String text = reader.readTextFile("elims.txt");

		//get the array for contestants and their elimination weeks
		reader.process_File(text, 4);
		reader.write("elims.txt", user, pick, String.valueOf(week), 2);
	}

}
