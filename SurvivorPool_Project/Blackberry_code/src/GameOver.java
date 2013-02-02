import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.GridFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;


public class GameOver extends MainScreen implements FieldChangeListener{

	private GridFieldManager winners;
	private LabelField title;
	private ButtonField quit;
	private Background defaultBg;
	private VerticalFieldManager mainManager;
	private String[] users, ranks, scores, money;
	private Bitmap backgroundImage; // current background
	
	public GameOver(){
		setTitle("Game Over");

		//background
		defaultBg = BackgroundFactory.createLinearGradientBackground(Color.WHITE, Color.LIGHTSKYBLUE, Color.WHITE, Color.GRAY);
		
		//main manager
		mainManager = (VerticalFieldManager) getMainManager();
		mainManager.setBackground(defaultBg);
		
		//create labels
		title = new LabelField("The game is over and the winners are ...", LabelField.FIELD_HCENTER);
		
		getWinners();
		
		// create  buttons
		quit = new ButtonField("Logout", ButtonField.FIELD_HCENTER);
		
		//set margins
		title.setMargin(25, 0, 0, 0);
		winners.setMargin(55, 0, 0, 0);
		quit.setMargin(170, 0, 0, 0);
		
		//add buttons to the listener
	
		quit.setChangeListener(this);
		
		//create image and overwrite the paint method of verticalFieldManager
		backgroundImage = Bitmap.getBitmapResource("flag-400.jpg");
		mainManager = new VerticalFieldManager(VerticalFieldManager.USE_ALL_WIDTH | VerticalFieldManager.USE_ALL_HEIGHT){
			public void paint(Graphics graphics){ 

				graphics.drawBitmap(0,0,480,510, backgroundImage, 0, 0);
				super.paint(graphics);
			}
		};
		this.add(mainManager);

		// add components
		mainManager.add(title);
		mainManager.add(winners);
		mainManager.add(quit);
	}
	
	private void getWinners(){
		// create user object
		ReadFile reader = new ReadFile();

		//get entire file as a string
		String test = reader.readTextFile("winners.txt");

		//get the array for users and their scores
		reader.process_File(test, 7);
		users = reader.getFirstField();
		ranks = reader.getSecondFieldString();
		scores = reader.getThirdField();
		money = reader.getFourthField();
		
		//Initialize the manager and add titles
		winners = new GridFieldManager(users.length+1, 4, GridFieldManager.AUTO_SIZE | GridFieldManager.FIELD_HCENTER);
		winners.insert(new LabelField(" Rank "), 0, 0);
		winners.insert(new LabelField(" Player "), 0, 1);
		winners.insert(new LabelField(" Points "), 0, 2);
		winners.insert(new LabelField(" Winnings "), 0, 3);
		
		
		if (ranks[0] != null){
			for(int i=1; i<=users.length; i++){
				//insert the user and their score
				winners.insert(new LabelField(ranks[i-1]), i, 0);
				winners.insert(new LabelField(users[i-1]), i, 1);
				winners.insert(new LabelField(scores[i-1]), i, 2);
				winners.insert(new LabelField("$" + money[i-1]), i, 3);
			}
		}		
	}
	
	public void fieldChanged(Field arg0, int arg1) {
		if (arg0 == quit){
			UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
		}
		
	}

}
