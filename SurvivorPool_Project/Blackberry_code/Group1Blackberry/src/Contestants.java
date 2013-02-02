import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.GridFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;


public class Contestants extends MainScreen implements FieldChangeListener {

	private ButtonField goBack; // go back button
    private VerticalFieldManager mainManager; // main panel
	private Background defaultBg; // current background
	private VerticalFieldManager remaining;	// manager for all remaining contestants
	private GridFieldManager eliminated;	// manager for the eliminated contestants
	private String [] contestants;			// list of contestants
	private int [] elim_week;				// list of the week the contestants were eliminated in
	private Bitmap backgroundImage; // current background// list of the week the contestants were eliminated in
	
	public Contestants(){
		setTitle("Contestants");

		//background
		defaultBg = BackgroundFactory.createLinearGradientBackground(Color.WHITE, Color.LIGHTSKYBLUE, Color.WHITE, Color.GRAY);
		
		//main manager
		mainManager = (VerticalFieldManager) getMainManager();
		mainManager.setBackground(defaultBg);
		
		// create  buttons
		goBack = new ButtonField("Go back", ButtonField.FIELD_HCENTER);
		goBack.setChangeListener(this);
		
		readContestants();	//read all of the contestants and their elimination weeks
		sortContestants();	// sort them by elimination week
		getRemaining();		// get all the remaining contestants
		getEliminated();	// get the eliminated contestants
		
		//set margins
		remaining.setMargin(25, 0, 0, 0);
		eliminated.setMargin(50, 0, 0, 0);
		goBack.setMargin(50, 0, 0, 0);
		
		//create image and overwrite the paint method of verticalFieldManager
		backgroundImage = Bitmap.getBitmapResource("contest480 2.jpg");
		//backgroundImage = Bitmap.getBitmapResource("contest480 4.jpg");
		mainManager = new VerticalFieldManager(VerticalFieldManager.USE_ALL_WIDTH | VerticalFieldManager.USE_ALL_HEIGHT){
			public void paint(Graphics graphics){ 

				graphics.drawBitmap(0,0,480,510, backgroundImage, 0, 0);
				super.paint(graphics);
			}
		};
		this.add(mainManager);
		
		//add components
		mainManager.add(remaining);
		mainManager.add(eliminated);
		mainManager.add(new SeparatorField());
		mainManager.add(goBack);
	}

	public void fieldChanged(Field field, int context) {
		if(field == goBack)
		{
			UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
		}
		
	}
	
	/**
	 * This method read the contestant data from a file and stores a list
	 * of all the contestants and their elimination weeks
	 */
	private void readContestants(){
		// create user object
		ReadFile reader = new ReadFile();

		//get entire file as a string
		String test = reader.readTextFile("cntst_text2.txt");

		//get the array for contestants and their elimination weeks
		reader.process_File(test, 2);
		contestants = reader.getFirstField();
		elim_week = reader.getSecondField();
	}
	
	/**
	 * This method extracts all the remaining contestants from the list of all
	 * contestants
	 */
	private void getRemaining(){
		remaining = new VerticalFieldManager(Manager.FIELD_HCENTER);
		
		// add a title
		remaining.add(new LabelField("Remaining Contestants", LabelField.FIELD_HCENTER));
		remaining.add(new SeparatorField());
		
		// add each contestant that hasnt been eliminated
		for(int i=0; i<elim_week.length; i++){
			if (elim_week[i] == -1){
				remaining.add(new LabelField(contestants[i], LabelField.FIELD_HCENTER));
			}
		}
	}
	
	/**
	 * This method extracts all the eliminated contestants and diplays their id and the week
	 * they were eliminated
	 */
	private void getEliminated(){
		eliminated = new GridFieldManager(contestants.length+1, 2, GridFieldManager.AUTO_SIZE | GridFieldManager.FIELD_HCENTER);
		
		// set titles
		eliminated.insert(new LabelField(" Contestant "), 0, 0);
		eliminated.insert(new LabelField(" Week Eliminated "), 0, 1);
		
		
		// add the eliminated contestants one by one
		for(int i=1; i<contestants.length+1; i++){
			
			// done when it reaches the remaining contestants
			if(elim_week[i-1] == -1){
				break;
			}
			
			// insert the contestant and their elimination week
			else{
				eliminated.insert(new LabelField(contestants[i-1]), i, 0);
				eliminated.insert(new LabelField(String.valueOf(elim_week[i-1])), i, 1);
			}
		}
	}
	
	/**
	 * This method sorts the contestants by their elimination week. From the most recent
	 * elimination to the first elimination followed by the remainining contestants
	 */
	private void sortContestants(){
		int max_index=0;
		for (int i=0; i<elim_week.length; i++){			// check each spot in the list
			max_index = i;								// initalize the most recent elimination as the current spot
			for (int j=i; j<elim_week.length; j++){		// check all spots for a more recent week
				if (elim_week[j] > elim_week[max_index]){
					max_index = j;						// swap the week if it is more recent
				}
			}
			
			//Swap the most recent week into the current spot
			String tmp_contestant = contestants[i];
			int	tmp_elim_week = elim_week[i];
			contestants[i] = contestants[max_index];
			elim_week[i] = elim_week[max_index];
			contestants[max_index] = tmp_contestant;
			elim_week[max_index] = tmp_elim_week;
		}
	}
}
