import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.GridFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;

/**
 * This class creates an interface, which displays the game standings
 * @author Group 1
 *
 */
public class Standings extends MainScreen implements FieldChangeListener 
{
	/* attributes */
	
	private LabelField mainLabel; // label displaying the status of the standings
	private ButtonField goBack; // go back button
    private VerticalFieldManager mainManager; // main panel
	private Background defaultBg; // current background
	private String [] users;		// list of users
	private int [] scores;			// list of the users scores
	private GridFieldManager standings;	// for displaying the standings
	private Bitmap backgroundImage; // current background// list of the week the contestants were eliminated in

	/**
	 * Initialise all the attributes and creates the interface
	 */
	public Standings()
	{
		setTitle("Standings");

		//background
		defaultBg = BackgroundFactory.createLinearGradientBackground(Color.WHITE, Color.LIGHTSKYBLUE, Color.WHITE, Color.GRAY);
		
		//main manager
		mainManager = (VerticalFieldManager) getMainManager();
		mainManager.setBackground(defaultBg);
		
		//create labels
		mainLabel = new LabelField("Standings", LabelField.FIELD_HCENTER);
		
		readScores();		//read the users and their scores
		sortStandings();	//sort the users by score
		initTable();		//set up the standings display
		
		// create  buttons
		goBack = new ButtonField("Go back", ButtonField.FIELD_HCENTER);
		
		//set margins
		mainLabel.setMargin(25, 0, 0, 0);
		standings.setMargin(25, 0, 0, 0);
		goBack.setMargin(50, 0, 0, 0);
		
		//add buttons to the listener
	
		goBack.setChangeListener(this);

		//create image and overwrite the paint method of verticalFieldManager
		backgroundImage = Bitmap.getBitmapResource("island455.jpg");
		mainManager = new VerticalFieldManager(VerticalFieldManager.USE_ALL_WIDTH | VerticalFieldManager.USE_ALL_HEIGHT){
			public void paint(Graphics graphics){ 

				graphics.drawBitmap(0,0,480,510, backgroundImage, 0, 0);
				super.paint(graphics);
			}
		};
		this.add(mainManager);

		// add components
		mainManager.add(mainLabel);
		mainManager.add(standings);
		mainManager.add(goBack);
		
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
		if(field == goBack)
		{
			UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
		}
		
	}
	
	/**
	 * This method reads the users data file and gets a list
	 * of users and their scores
	 */
	private void readScores(){
		// create user object
		ReadFile reader = new ReadFile();

		//get entire file as a string
		String test = reader.readTextFile("Test.txt");

		//get the array for users and their scores
		reader.process_File(test, 3);
		users = reader.getFirstField();
		scores = reader.getSecondField();
	}
	
	/**
	 * This method sorts the list of users and their scores from
	 * highest to lowest
	 */
	private void sortStandings(){
		int max_index=0;
	
		for (int i=0; i<scores.length; i++){	//Start at the beginning and move along each array position
			max_index = i;						// initalize the start position as the highest score
			for (int j=i; j<scores.length; j++){	// search the rest of the array for a higher score
				if (scores[j] > scores[max_index]){
					max_index = j;					// save the highest score
				}
			}
			
			//Swap the highest score and the current position in the arrays score
			String tmp_user = users[i];
			int	tmp_score = scores[i];
			users[i] = users[max_index];
			scores[i] = scores[max_index];
			users[max_index] = tmp_user;
			scores[max_index] = tmp_score;
		}
	}
	
	/**
	 * This method creates a GridField that displays the standings from
	 * highest score to lowest score. It also ranks the users by score
	 */
	private void initTable(){
		
		//Initialize the manager and add titles
		standings = new GridFieldManager(users.length+1, 3, GridFieldManager.AUTO_SIZE | GridFieldManager.FIELD_HCENTER);
		standings.insert(new LabelField(" Rank "), 0, 0);
		standings.insert(new LabelField(" Player "), 0, 1);
		standings.insert(new LabelField(" Points "), 0, 2);
		int rank = 1;
		
		//add each user
		for(int i=1; i<=users.length; i++){
			
			//insert the first rank
			if (i==1){
				standings.insert(new LabelField(String.valueOf(i)), i, 0);
			}
			else{
				// give the user the same rank if they are tied with the previous user
				if (scores[i-1] == scores[i-2]){
					standings.insert(new LabelField(standings.getFieldAtCell(i-1, 0).toString()), i,0);
				}
				// otherwise give them the next rank
				else{
					standings.insert(new LabelField(String.valueOf(rank)), i, 0);
				}
			}
			rank++;
			
			//insert the user and their score
			standings.insert(new LabelField(users[i-1]), i, 1);
			standings.insert(new LabelField(String.valueOf(scores[i-1])), i, 2);
		}
	}
	
}
