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
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;
/**
 * This class creates an interface showing the the remaining contestants and lets the user pick his/her ultimate Winner
 * @author Group 1
 *
 */
public class ChangeUltimateWinner extends MainScreen implements FieldChangeListener 
{
	/* Attributes*/

	private LabelField mainLabel; //label on top of the page
	private ObjectChoiceField contestantsList; // contestants id drop-down list
	private ButtonField change; 	// change button
	private ButtonField goBack; 	// goBack button
	private VerticalFieldManager mainManager; 	// main panel
	private Background defaultBg; 	// current background
	private Background labelBg; 	// main label background
	private String[] contestants, remaining;	// contestants and remaining contetants lists
	private int[] elim_week;	// elimination week for each contestant
	private String user;
	private int week;
	private Bitmap backgroundImage; // current background// list of the week the contestants were eliminated in

	/**
	 * Initialise all the attributes and creates the interface
	 */
	public ChangeUltimateWinner(String user, int week)
	{
		
		this.user = user;
		this.week = week;
		//set screen title
		setTitle("Ultimate Winner");

		//background
		defaultBg = BackgroundFactory.createLinearGradientBackground(Color.WHITE, Color.LIGHTSKYBLUE, Color.WHITE, Color.GRAY);
		labelBg = BackgroundFactory.createLinearGradientBackground(Color.WHEAT,Color.WHEAT, Color.WHEAT, Color.WHITESMOKE);


		//main manager
		mainManager = (VerticalFieldManager) getMainManager();
		mainManager.setBackground(defaultBg);
		
		//create labels
		mainLabel = new LabelField("Who do you think will be the ultimate winner?\n\n WARNING: Updating your ultimate winner pick could affect the number of points you get for a correct pick");
		mainLabel.setBackground(labelBg);

		// read the contestant info and get the remaining ones
		readContestants();
		getRemaining();
		
		// create dropdown list
		contestantsList = new ObjectChoiceField("", remaining);
		
		// create  buttons
		change = new ButtonField("Change");
		goBack = new ButtonField("Go back");

		//set margins
		mainLabel.setMargin(0, 0, 0, 0);
		contestantsList.setMargin(14, 0, 0, 0);
		change.setMargin(200, 0, 0, 60);
		goBack.setMargin(200, 0, 0, 20);


		//add buttons to the listener
		change.setChangeListener(this);
		goBack.setChangeListener(this);

		//create image and overwrite the paint method of verticalFieldManager
		backgroundImage = Bitmap.getBitmapResource("desafio1 copy.jpg");
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
		hfm.add(change);
		hfm.add(goBack);

	}
	
	/**
	 * This method opens the data file containing contestant information
	 * and reads all of the contestants and the weeks they were eliminated
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
	 * This method gets all of the contestants that have not been
	 * eliminated yet and stores them in an array of Strings
	 */
	private void getRemaining(){
		String [] tmp_remaining = new String[contestants.length];
		int remaining_count = 0;
		
		//Get all of the remaining contestants
		for(int i=0; i<elim_week.length; i++){
			if (elim_week[i] == -1){
				tmp_remaining[remaining_count] = contestants[i];
				remaining_count++;
			}
		}
		
		//Remove extra length from array
		remaining = new String[remaining_count];
		for (int i=0; i<remaining_count; i++){
			remaining[i] = tmp_remaining[i];
		}
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
		if(field == change)
		{
				changePick((String) contestantsList.getChoice(contestantsList.getSelectedIndex()));
				UiApplication.getUiApplication().pushScreen(new Confirmation(user, " Your ultimate winner has been updated.", false));
		}
			
		else if(field == goBack)
		{
			UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
		}



	}
	
	private void changePick(String pick){
		ReadFile reader = new ReadFile();
		//get entire file as a string
		String test = reader.readTextFile("ultimate.txt");

		//get the array for contestants and their elimination weeks
		reader.process_File(test, 4);
		reader.write("ultimate.txt", user, pick, String.valueOf(week), 1);


	}

}
