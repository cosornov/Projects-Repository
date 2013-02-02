import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;
//READS EMPTY PICKS AS TWO SPACES
/**
 * This class creates an interface, which displays the game main screen. 
 * @author Group 1
 *
 */
public class Game extends MainScreen implements FieldChangeListener 
{
	/* Attributes*/

	private LabelField welcome; // welcome label
	private LabelField totalScore; // total score label
	private LabelField totalScoreField; // total score field, which can be edited by the admin
	private LabelField currentPickLabel; // current ultimate winner label
	private LabelField currentPickField; // current ultimate winner field, which can be edited by the user
	private LabelField weeklabel; 
	private LabelField weekfield; 
	private LabelField ultimatelabel; 
	private LabelField ultimatefield; 
	private ButtonField standings; // standings button
	private ButtonField contestants; //contestants button
	private ButtonField questions; // questions button
	private ButtonField changeUltimateWinner; // change ultimate winner button
	private ButtonField WeeklyPick; // weekly pick button
	private ButtonField help; // help button
	private ButtonField logout; // logout button
	private ButtonField quit; // quit button
	private VerticalFieldManager mainManager; // main panel
	private Background defaultBg; // current background
	private String currentUser; // current game user
	private int week;
	private Bitmap backgroundImage; // current background// list of the week the contestants were eliminated in

	/**
	 * Initialise all the attributes and creates the interface
	 */
	public Game(String currentUser)
	{
		super(NO_VERTICAL_SCROLL);
		this.currentUser = currentUser;
		getWeek();

		//background
		defaultBg = BackgroundFactory.createLinearGradientBackground(Color.WHITE, Color.LIGHTSKYBLUE, Color.WHITE, Color.GRAY);

		//main manager
		mainManager = (VerticalFieldManager) getMainManager();
		mainManager.setBackground(defaultBg);

		//set screen title
		setTitle("Survivor Pool");

		//create label
		welcome = new LabelField("Welcome " + currentUser);
		totalScore = new LabelField("Total score: ");
		totalScoreField = new LabelField(getScore());
		currentPickLabel = new LabelField("Weekly Pick: ");
		currentPickField = new LabelField(getPick());
		weeklabel = new LabelField("Week: ");
		weekfield = new LabelField(String.valueOf(week));
		ultimatelabel = new LabelField("Ultimate Pick: ");
		ultimatefield = new LabelField(getUltimatePick());
		
		// create buttons
		standings = new ButtonField("See standings", ButtonField.FIELD_HCENTER);
		contestants = new ButtonField("See contestants", ButtonField.FIELD_HCENTER);
		questions = new ButtonField("Questions", ButtonField.FIELD_HCENTER);
		changeUltimateWinner = new ButtonField("Pick/Change ultimate winner", ButtonField.FIELD_HCENTER);
		WeeklyPick = new ButtonField("Weekly pick", ButtonField.FIELD_HCENTER);
		help = new ButtonField("Help");
		logout = new ButtonField("Logout");
		quit = new ButtonField("Quit");

		// add to the listener
		standings.setChangeListener(this);
		contestants.setChangeListener(this);
		questions.setChangeListener(this);
		changeUltimateWinner.setChangeListener(this);
		WeeklyPick.setChangeListener(this);
		help.setChangeListener(this);
		logout.setChangeListener(this);
		quit.setChangeListener(this);

		//set margins 
		welcome.setMargin(10,0,0,5);
		standings.setMargin(50, 0, 0, 0);
		totalScore.setMargin(15,0,0,8);
		totalScoreField.setMargin(15,0,0,8);
		currentPickLabel.setMargin(10,0,0,70);
		currentPickField.setMargin(10,0,0,0);
		weeklabel.setMargin(45, 0, 0, 85);
		weekfield.setMargin(45,0,0, 0);
		ultimatelabel.setMargin(45, 0, 0, 0);
		ultimatefield.setMargin(45, 0, 0, 0);
		help.setMargin(10,0,0,7);
		logout.setMargin(10,0,0,40);
		quit.setMargin(10,0,0,40);
		
		//create image and overwrite the paint method of verticalFieldManager
		backgroundImage = Bitmap.getBitmapResource("island5copy.jpg");
		//backgroundImage = Bitmap.getBitmapResource("beach copy.jpg");
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

		// second vertical manager
		VerticalFieldManager vfm2 = new VerticalFieldManager(Manager.FIELD_HCENTER);
		mainManager.add(vfm2);

		// horizontal manager
		HorizontalFieldManager hfm = new HorizontalFieldManager();
		

		// second horizontal manager
		HorizontalFieldManager hfm2 = new HorizontalFieldManager();
		

		HorizontalFieldManager hfm3 = new HorizontalFieldManager();
		mainManager.add(hfm3);
		mainManager.add(hfm);
		mainManager.add(hfm2);
		
		//add components 
		vfm.add(welcome);
		vfm2.add(standings);
		vfm2.add(contestants);
		vfm2.add(questions);
		vfm2.add(changeUltimateWinner);
		vfm2.add(WeeklyPick);
		hfm3.add(ultimatelabel);
		hfm3.add(ultimatefield);
		hfm3.add(weeklabel);
		hfm3.add(weekfield);
		hfm.add(totalScore);
		hfm.add(totalScoreField);
		hfm.add(currentPickLabel);
		hfm.add(currentPickField);
		hfm2.add(help);
		hfm2.add(logout);
		hfm2.add(quit);

	}

	/**
	 * Method for handling Field change events
	 * @param Field The field that changed
	 * @param Information specifying the origin of the change
	 */
	public void fieldChanged (Field field, int context)
	{
		if(field == quit)
		{
			System.exit(0);
		}
		else if(field == logout)
		{
			UiApplication.getUiApplication().pushScreen(new Welcome());
		}
		else if(field == help)
		{
			UiApplication.getUiApplication().pushScreen(new AboutSurvivorPool());
		}
		else if(field == questions)
		{
			UiApplication.getUiApplication().pushScreen(new Questions(currentUser, week));
		}
		else if(field == WeeklyPick)
		{
			String test = ultimatefield.toString();
			if (ultimatefield.toString().compareTo("") != 0){
				UiApplication.getUiApplication().pushScreen(new WeeklyPick(currentUser, week));
			}
			else{
				Dialog.alert("You can not make a weekly pick until you have selected and ultimate winner" + test);
			}
		}
		else if(field == changeUltimateWinner )
		{
			UiApplication.getUiApplication().pushScreen(new ChangeUltimateWinner(currentUser, week));
		}
		else if(field == standings )
		{
			UiApplication.getUiApplication().pushScreen(new Standings());
		}
		else if(field == contestants )
		{
			UiApplication.getUiApplication().pushScreen(new Contestants());
		}

	}
	
	private String getScore(){
		
		ReadFile reader = new ReadFile();
		String text = reader.readTextFile("Test.txt");
		reader.process_File(text, 3);
		String [] users = reader.getFirstField();
		int [] scores = reader.getSecondField();
		for (int i = 0; i < users.length; i++){
			if(users[i].compareTo(currentUser) == 0){
				return String.valueOf(scores[i]);
			}
		}
		return "";
	}
	
	private String getPick(){
		ReadFile reader = new ReadFile();
		String text = reader.readTextFile("elims.txt");
		reader.process_File(text, 4);
		String [] users = reader.getFirstField();
		int [] weeks = reader.getSecondField();
		String [] picks = reader.getThirdField();
		for (int i = 0; i < users.length; i++){
			if(users[i].compareTo(currentUser) == 0){
				if(weeks[i] == week){
					if (picks[i].trim().compareTo("") != 0)
						return picks[i];
					break;
				}
				
			}
		}
		return "";
	}
	
	private String getUltimatePick(){
		ReadFile reader= new ReadFile();
		String text = reader.readTextFile("ultimate.txt");
		reader.process_File(text, 4);
		String [] users = reader.getFirstField();
		String [] picks = reader.getThirdField();
		for (int i = 0; i < users.length; i++){
			if(users[i].compareTo(currentUser) == 0){
				if (picks[i].trim().compareTo("") != 0)
					return picks[i];
				break;
			}
		}
		return "";
	}
	
	private void getWeek(){
		ReadFile reader = new ReadFile();
		String text = reader.readTextFile("settings.txt");
		reader.process_File(text, 5);
		int [] week = reader.getSecondField();
		this.week = week[0];
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