
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;

/**
 * This class creates an interface, which displays instructions about how to play the Game.
 * @author Group 1
 *
 */
public class AboutSurvivorPool extends MainScreen implements FieldChangeListener 
{
	/* Attributes*/

	private LabelField main; 					//  the label on top of the page
	private LabelField stage1; 					// description of the first stage of the game
	private LabelField stage1_1;
	private LabelField stage1_2;
	private LabelField stage1_3;
	private LabelField stage2;					// description of the second stage of the game
	private LabelField stage2_1;
	private LabelField stage2_2;
	private LabelField stage2_3;
	private LabelField stage3; 					// description of the third stage of the game
	private LabelField stage3_1;
	private LabelField endOfGame; 				// description of the end-of-game stage
	private LabelField endOfGame2;
	private LabelField lastLabel;
	private ButtonField ok; 					// OK button
	private VerticalFieldManager mainManager; 	// main panel
	private Background defaultBg; 				// current background
	
	/**
	 * Initialise all the attributes and creates the interface
	 */
	public AboutSurvivorPool()
	{

		//background
		defaultBg = BackgroundFactory.createLinearGradientBackground(Color.WHITE, Color.LIGHTSKYBLUE, Color.WHITE, Color.GRAY);

		//main manager
		mainManager = (VerticalFieldManager) getMainManager();
		mainManager.setBackground(defaultBg);

		//set screen title
		setTitle("About Suvival Pool");

		//create help labels
		main = new LabelField("How Survivor Pool works", LabelField.FIELD_HCENTER);
		stage1 = new LabelField("Stage 1, when the game starts:");
		stage1_1 = new LabelField("1. At the start of the game, all players must pick a contestant they think will win the " +
		"whole pool, this pick will be worth 2 points * the number of contestants." );
		stage1_2 = new LabelField("2. Every week players will say who they think will be eliminated. If the player pick the correct contestant" +
		" who will be eliminated, they get 20 points");
		stage1_3 = new LabelField("3. A player can change who they think will win the whole game at " +
		"any point, then they get 2 points * the number of remaining contestants");
		stage2 = new LabelField("Stage 2, during the last week, when there are only 3 contestants left:");
		stage2_1 = new LabelField("1. Players pick which of the 3 remaining contestants will win. If they pick correctly they get 40 points" );
		stage2_2 = new LabelField("2. Players get the points for the overall winner if they picked correctly in an early round");
		stage2_3 = new LabelField("3. Players get the points from each round for picking who would be eliminated");
		stage3 = new LabelField("All Stages, at any point during the game:");
		stage3_1 = new LabelField("1. At any point during the game, players may answer additional bonus questions for bonus marks, for example \"Who wins immunity this week?\", " +
		"or \"How many people received votes during tribal counsel this week?\". Each correct answer to each bonus question is awarded 10 points." );

		endOfGame = new LabelField("At the end of the game:");

		endOfGame2 = new LabelField("There will be first place player, a second place player and third place player. The first place player gets 60% of the amount of " +
		"money, the second place player gets 30% of the amount of money and the third place person gets 10% of the amount of money.");
		lastLabel = new LabelField("Good luck!", LabelField.FIELD_HCENTER);

		//create buttons
		ok = new ButtonField("Ok", ButtonField.FIELD_RIGHT);

		//add buttons to the listener
		ok.setChangeListener(this);

		//set margins
		stage1.setMargin(12, 0, 0, 0);
		stage1_1.setMargin(7, 0, 0, 15);
		stage1_2.setMargin(7, 0, 0, 15);
		stage1_3.setMargin(7, 0, 0, 15);
		stage2.setMargin(15, 0, 0, 0);
		stage2_1.setMargin(7, 0, 0, 15);
		stage2_2.setMargin(7, 0, 0, 15);
		stage2_3.setMargin(7, 0, 0, 15);
		stage3.setMargin(15, 0, 0, 0);
		stage3_1.setMargin(7, 0, 0, 15);
		endOfGame.setMargin(15, 0, 0, 0);
		endOfGame2.setMargin(7, 0, 0, 15);
		lastLabel.setMargin(15, 0, 0, 0);

		//add components
		this.add(main);
		this.add(stage1);
		this.add(stage1_1);
		this.add(stage1_2);
		this.add(stage1_3);
		this.add(stage2);
		this.add(stage2_1);
		this.add(stage2_2);
		this.add(stage2_3);
		this.add(stage3);
		this.add(stage3_1);
		this.add(endOfGame);
		this.add(endOfGame2);
		this.add(lastLabel);
		this.add(ok);

	}

	/**
	 * Method for handling Field change events
	 * @param Field The field that changed
	 * @param Information specifying the origin of the change
	 */
	public void fieldChanged (Field field, int context)
	{
		if(field == ok)
		{
			UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());

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