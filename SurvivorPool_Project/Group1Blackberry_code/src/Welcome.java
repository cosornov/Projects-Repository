import javax.microedition.lcdui.Font;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;


/**
 * This class creates an interface, which displays the login screen
 * @author Group 1
 */
public class Welcome extends MainScreen implements FieldChangeListener 
{ 
	/* Attributes */
	
	private LabelField welcomeLabel; // welcome label
	private ObjectChoiceField usersList; // users id drop-down list
	private ButtonField signIn; // signIn button
	private ButtonField about; // help button
	private ButtonField exit; // exit button
	//private CheckboxField checkBox; // keep me signIn check box
	private VerticalFieldManager mainManager; // main panel
	private String currentUser; // current game user
	private Bitmap backgroundImage; // current background

	/**
	 * Initialise all the attributes and creates the interface
	 */
	public Welcome()
	{
		super(NO_VERTICAL_SCROLL);

		//Initialise current user
		currentUser = "";

		//create labels
		welcomeLabel = new LabelField("Welcome to Survivor Pool");

		// create  buttons
		signIn = new ButtonField("Sign in");
		about = new ButtonField("Help");
		exit = new ButtonField("Exit");
		
	

		//set margins
		welcomeLabel.setMargin(25, 0, 0, 80);
		//checkBox.setMargin(158, 0, 0, 38);
		signIn.setMargin(0, 0, 0, 38);
		exit.setMargin(220, 0, 0, 187);
		about.setMargin(220, 0, 0, 8);

		//add buttons to the listener
		signIn.setChangeListener(this);
		about.setChangeListener(this);
		exit.setChangeListener(this);

		//create image and overwrite the paint method of verticalFieldManager
		backgroundImage = Bitmap.getBitmapResource("beach.jpg");
		mainManager = new VerticalFieldManager(VerticalFieldManager.USE_ALL_WIDTH | VerticalFieldManager.USE_ALL_HEIGHT){
			public void paint(Graphics graphics){ 

				graphics.drawBitmap(0,0,480,510, backgroundImage, 0, 0);
				super.paint(graphics);
			}
		};

		this.add(mainManager);

		//vertical manager
		VerticalFieldManager vfm = new VerticalFieldManager();
		mainManager.add(vfm);

		//horizontal manager
		HorizontalFieldManager hfm = new HorizontalFieldManager();
		mainManager.add(hfm);
		
		if ( checkGameStarted()){

			LabelField warning = new LabelField("The game has not started yet. \nTry again later.", LabelField.HCENTER);
			
			FontFamily fontFamily[] = FontFamily.getFontFamilies();
		    net.rim.device.api.ui.Font font = fontFamily[1].getFont(FontFamily.BITMAP_FONT,25 );
		    warning.setFont(font);

			warning.setMargin(80, 0, 0, 0);
			exit.setMargin(42, 0, 0, 150);
			vfm.add(warning);
			hfm.add(exit);
			
		}
		else{
			
			// create user object
			ReadFile reader = new ReadFile();

			//get entire file as a string
			String test = reader.readTextFile("Test.txt");

			//get the array for users
			reader.process_File(test, 1);

			//create drop-down list
			usersList = new ObjectChoiceField("Sign in with user ID ",reader.getFirstField()); 
			
			usersList.setMargin(40, 0, 0, 20);

		// add components
		vfm.add(welcomeLabel);
		vfm.add(usersList);
		vfm.add(signIn);
		hfm.add(exit);
		hfm.add(about);
		}


	}

	/**
	 * Method for handling Field change events
	 * @param Field The field that changed
	 * @param Information specifying the origin of the change
	 */
	public void fieldChanged(Field field, int context)
	{
		if( field == signIn)
		{
			if(checkGameOver()){
				currentUser = (String) usersList.getChoice(usersList.getSelectedIndex());
				UiApplication.getUiApplication().pushScreen(new Game(currentUser));
			}
			else{
				UiApplication.getUiApplication().pushScreen(new GameOver());
			}
		}
		else if(field == about)
		{
			UiApplication.getUiApplication().pushScreen(new AboutSurvivorPool());
		}
		else if(field == exit)
		{
			System.exit(0);
		}

	}
	
	private boolean checkGameStarted(){
		ReadFile reader = new ReadFile();
		String text = reader.readTextFile("settings.txt");
		reader.process_File(text, 5);
		int val = reader.getSecondField()[0];
		if (val >0){
			return false;
		}
		else
			return true;
	}
	
	private boolean checkGameOver(){
		ReadFile reader = new ReadFile();
		String text = reader.readTextFile("settings.txt");
		reader.process_File(text, 8);
		int val = reader.getSecondField()[0];
		if (val >0){
			return false;
		}
		else
			return true;
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
