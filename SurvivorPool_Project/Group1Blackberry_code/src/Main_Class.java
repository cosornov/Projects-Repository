import net.rim.device.api.ui.UiApplication;

/**
 * Class that runs the game, starting from the Welcome class
 * @author Group 1
 *
 */
public class Main_Class extends UiApplication
{
	public static void main(String[] args)
	{
		Main_Class splash = new Main_Class();
		splash. enterEventDispatcher();

	}
	/**
	 * This constructor pushes the Welcome interface into the blackberry screen 
	 */
	public Main_Class()
	{
		pushScreen(new Welcome());
	}
}
