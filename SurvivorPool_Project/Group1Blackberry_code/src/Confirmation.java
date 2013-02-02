import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;

/**
 * This class creates a confirmation interface, which displays the changes made by the user on the game.
 * @author Group 1
 *
 */
public class Confirmation extends MainScreen implements FieldChangeListener 
{
	/* Attributes*/

	private LabelField confirmationMsg; // label containing the message to be displayed on the screen
	private ButtonField continueToGame; // button to continue to the game screen
	private VerticalFieldManager mainManager; // main panel
	private Background defaultBg; // current background
	private boolean questionFlag; // flag is equals to true only if the confirmation class is called by the DisplayAnswer class
	private String currentUser;
	private Bitmap backgroundImage; // current background// list of the week the contestants were eliminated in

	/**
	 * Initialise all the attributes and creates the interface
	 * @param message Message to be display on the screen
	 */
	public Confirmation(String currentUser, String message, boolean questionFlag)
	{
		this.currentUser = currentUser;
		//set questionFlag
		this.questionFlag = questionFlag;
		//set screen title
		setTitle("Confirmation");

		//backgrounds
		defaultBg = BackgroundFactory.createLinearGradientBackground(Color.WHITE, Color.LIGHTSKYBLUE, Color.WHITE, Color.GRAY);

		//main manager
		mainManager = (VerticalFieldManager) getMainManager();
		mainManager.setBackground(defaultBg);

		//create labels
		confirmationMsg = new LabelField(message);

		//create button
		continueToGame = new ButtonField("Continue", ButtonField.FIELD_HCENTER);

		//add button to the listener
		continueToGame.setChangeListener(this);
		
		//set margins
		confirmationMsg.setMargin(20, 0, 0, 7);
		continueToGame.setMargin(30, 0, 0, 0);

		//create image and overwrite the paint method of verticalFieldManager
		backgroundImage = Bitmap.getBitmapResource("beach2.jpg");
		mainManager = new VerticalFieldManager(VerticalFieldManager.USE_ALL_WIDTH | VerticalFieldManager.USE_ALL_HEIGHT){
			public void paint(Graphics graphics){ 

				graphics.drawBitmap(0,0,480,510, backgroundImage, 0, 0);
				super.paint(graphics);
			}
		};
		this.add(mainManager);

		//add components to the panel
		mainManager.add(confirmationMsg);
		mainManager.add(continueToGame);


	}

	/**
	 * Method for handling Field change events
	 * @param Field The field that changed
	 * @param Information specifying the origin of the change
	 */
	public void fieldChanged (Field field, int context)
	{
		if(field == continueToGame && questionFlag == true)
		{
			UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
			UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
			UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
			UiApplication.getUiApplication().pushScreen(new Game(currentUser));

		}
		else
		{
			UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
			UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
			UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
			UiApplication.getUiApplication().pushScreen(new Game(currentUser));
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

}
