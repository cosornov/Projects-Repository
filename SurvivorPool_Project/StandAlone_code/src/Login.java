import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import java.io.*;

public class Login {
	
	/*Begin Login Frame Attributes*/
	private static JFrame loginFrame;					//Login Screen JFrame
	private static JPanel loginPanel;					//Login Screen JPanel
	private static JLabel titleLogin;					//Login Screen Login Title
	private static JLabel usernameLabel;				//Login Screen Username Label
	private static JTextField username;					//Login Screen Username Text Field
	private static JButton proceedLogin;				//Login Screen Proceed Button
	private static JButton exitLogin;					//Login Screen Exit Button
	private static JLabel logoLabel;					//Login Screen LogoLabel
	/*End Login Frame Attributes*/
	
	/*Begin Main Menu bar*/
	private static JFrame mainFrame;					//Main Screen JFrame
	private static JMenuBar menu;						//Main Screen Menu Bar
	private static JMenu file;							//Main Screen Menu Item - File
	private static JMenuItem newGame;					//Main Screen Sub-Menu Item - New Game
	private static JMenuItem endGame;					//Main Screen Sub-Menu Item - End Game
	private static JMenuItem quit;						//Main Screen Sub-Menu Item - Quit
	private static JMenu colorSchemes;					//Main Screen Menu Item - Color Schemes
	private static JMenuItem colorSchemeOne;			//Main Screen Sub-Menu Item - Color Scheme 1
	private static JMenuItem colorSchemeTwo;			//Main Screen Sub-Menu Item - Color Scheme 2
	private static JMenuItem colorSchemeThree;			//Main Screen Sub-Menu Item - Color Scheme 3
	/*End Main Menu bar*/
	
	/*Create MainPanel Object*/
	public MainPanel mainPanel;
	
	//UI Manager
	public UIManager manager;
	
	/*Login Constructor*/
	public Login() {
		mainPanel = new MainPanel();						//Create Main Panel
		
		//Create UIManager object
		manager=new UIManager();
		
		/*Custom Button Gradient*/			
		//Create linked list that will store all gradient information
		//You can try to understand it by change it's value
		LinkedList<Object> a = new LinkedList<Object>();
		a.add(0.3);
		a.add(0.3);
		
		a.add(new ColorUIResource(255,102,0));
		a.add(new ColorUIResource(255,153,0));
		a.add(new ColorUIResource(255,102,0));
		
		//Set Button.gradient key with new value
		manager.put("Button.gradient",a);
		
		/*Begin Login Panel Layout*/
		loginPanel = new ImagePanel("login_bkg", 500, 500);	//Create Login Panel
		loginPanel.setLayout(null);							//Set Login Panel Layout to null
															//for custom coordinate layout
		
		
		logoLabel = new JLabel();
		loginPanel.add(logoLabel);
		logoLabel.setIcon(new ImageIcon("gui_images/logo.png"));
		logoLabel.setVisible(true);
		logoLabel.setBounds(45, 20, 400, 262);
		
		titleLogin = 
				new JLabel("Administrator Login Screen");	//Create Login Title Label
		loginPanel.add(titleLogin);							//Add Login Title Label to Login Panel
		titleLogin.setBounds(145, 290, 300, 20);				//Set Layout for Login Title Label
		titleLogin.setForeground(Color.WHITE);
		titleLogin.setFont(new Font("Sans-Serif", Font.BOLD, 16));
		
		usernameLabel = new JLabel("Username");				//Create User Name Label
		loginPanel.add(usernameLabel);						//Add to Login Panel
		usernameLabel.setBounds(210, 320, 200, 20);			//Set Layout for User Name Label
		usernameLabel.setForeground(Color.WHITE);
		username = new JTextField("Admin", 10);				//Create User Name Text Field
		username.setEditable(false);						//Set User Name Text Field to be Uneditable
		loginPanel.add(username);							//Add to Login Panel
		username.setBounds(150, 340, 200, 20);				//Set Layout for User Name Text Field
		
		proceedLogin = new JButton("Proceed");				//Create Proceed Button
		//proceedLogin.setForeground(Color.WHITE);
		proceedLogin.addActionListener(new adminLogin());	//Add Action Listener for Proceed Button
		loginPanel.add(proceedLogin);						//Add to Login Panel
		proceedLogin.setBounds(140, 385, 100, 20);			//Set Layout for Proceed Button
		exitLogin = new JButton("Exit");					//Create Exit Button
		//exitLogin.setForeground(Color.WHITE);
		exitLogin.addActionListener(new ExitHandler());		//Add Action Listener for Exit Button
		loginPanel.add(exitLogin);							//Add to Login Panel
		exitLogin.setBounds(260, 385, 100, 20);				//Set Layout for Exit Button
		/*End Login Panel Layout*/
	}
	
	/*Start Action Methods*/
	private class adminLogin implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			/*Hide Login Frame*/
			loginFrame.setVisible(false);
			loginFrame.setEnabled(false);
			
			/*Create Administrator Frame*/
			mainFrame = new JFrame("Administrator Control Panel");
			
			/*Window Listener responsible for exiting Program when the Main
			 * Frame is Closed*/
			mainFrame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
			
			/*Custom Button Gradient*/
			//Create linked list that will store all gradient information
			//You can try to understand it by change it's value
			LinkedList<Object> a = new LinkedList<Object>();
			a.add(0.3);
			a.add(0.3);
			a.add(new ColorUIResource(0,140,0));
			a.add(new ColorUIResource(0,204,0));
			a.add(new ColorUIResource(0,140,0));
	
			//Set Button.gradient key with new value
			manager.put("Button.gradient",a);
			
			/*Menu Bar Code*/
			menu = new JMenuBar();										//Create Menu Bar
			//menu.setBackground(Color.GREEN);
			file = new JMenu("File");									//Create File Menu Item
			menu.add(file);												//Add to Menu Bar
			newGame = new JMenuItem("New Game");						//Create New Game Menu Sub-Item
			newGame.addActionListener(new StartNewGame());				//Set Action Listener for New Game
			file.add(newGame);											//Add as Child Menu Item of File
			endGame = new JMenuItem("End Game");						//Create End Game Menu Sub-Item
			endGame.addActionListener(new EndGames());					//Action Listener for Ending the Game
			file.add(endGame);											//Add as Child Menu Item of File
			quit = new JMenuItem("Quit");								//Create Quit Menu Sub-Item
			file.add(quit);												//Add as Child Menu Item of File
			quit.addActionListener(new ExitHandler());					//Set Action Listener for Quit
			colorSchemes = new JMenu("Color Schemes");					//Create Color Schemes Menu Item
			menu.add(colorSchemes);										//Add to Menu Bar
			colorSchemeOne = new JMenuItem("Theme 1 - Jungle");			//Create Scheme 1 Menu Sub-Item
			colorSchemes.add(colorSchemeOne);							//Add as Child Menu Item of Color Schemes
			colorSchemeOne.addActionListener(new ThemeOne());			//Action Listener for Theme One
			colorSchemeTwo = new JMenuItem("Theme 2 - Paradise");		//Create Scheme 2 Menu Sub-Item
			colorSchemes.add(colorSchemeTwo);							//Add as Child Menu Item of Color Schemes
			colorSchemeTwo.addActionListener(new ThemeTwo());			//Action Listener for Theme Two
			colorSchemeThree = new JMenuItem("Theme 3 - Caterpillars");	//Create Scheme 3 Menu Sub-Item
			colorSchemes.add(colorSchemeThree);							//Add as Child Menu Item of Color Schemes
			colorSchemeThree.addActionListener(new ThemeThree());		//Action Listener for Theme Three
			

			/*Set-up Main Panel Layout*/
			mainFrame.setJMenuBar(menu);
			mainFrame.getContentPane().add(mainPanel.getPanel());
			mainFrame.pack();
			mainFrame.setSize(1080, 600);
			mainFrame.setVisible(true);
			mainFrame.setResizable(false);
		}
	}
	
	
	/**
	 * ThemeOne Class - Contains the Method to be called for enabling ThemeOne Settings
	 *
	 */
	private class ThemeOne implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			/*Custom Button Gradient*/
			//Create linked list that will store all gradient information
			//You can try to understand it by change it's value
			LinkedList<Object> a = new LinkedList<Object>();
			a.add(0.3);
			a.add(0.3);
			a.add(new ColorUIResource(0,140,0));
			a.add(new ColorUIResource(0,204,0));
			a.add(new ColorUIResource(0,140,0));
	
			//Set Button.gradient key with new value
			manager.put("Button.gradient",a);
			
			
			mainPanel.SetButtonsInvisible();
			mainPanel.setCurrentStyle("jungle");
			mainPanel.ResetButtonStates("nothing", "jungle");
			mainPanel.SetButtonsVisible();
			
			try {
				mainPanel.getPanel().setImage("gui_images/backgrounds/jungle.jpg");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * ThemeTwo Class - Contains the Method to be called for enabling ThemeTwo Settings
	 *
	 */
	private class ThemeTwo implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			/*Custom Button Gradient*/			
			//Create linked list that will store all gradient information
			//You can try to understand it by change it's value
			LinkedList<Object> a = new LinkedList<Object>();
			a.add(0.3);
			a.add(0.3);
			
			//First colour : R=2,G=208,B=206
			a.add(new ColorUIResource(2,208,206));
			
			//Second colour : R=136,G=255,B=254
			a.add(new ColorUIResource(136,255,254));
			
			//Third colour : R=0,G=142,B=140
			a.add(new ColorUIResource(0,142,140));
			
			//Set Button.gradient key with new value
			manager.put("Button.gradient",a);
			
			
			mainPanel.SetButtonsInvisible();
			mainPanel.setCurrentStyle("beach");
			mainPanel.ResetButtonStates("nothing", "beach");
			mainPanel.SetButtonsVisible();
			
			try {
				mainPanel.getPanel().setImage("gui_images/backgrounds/paradise.jpg");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * ThemeThree Class - Contains the Method to be called for enabling ThemeThree Settings
	 *
	 */
	private class ThemeThree implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			/*Custom Button Gradient*/			
			//Create linked list that will store all gradient information
			//You can try to understand it by change it's value
			LinkedList<Object> a = new LinkedList<Object>();
			a.add(0.3);
			a.add(0.3);
			
			//First colour : R=2,G=208,B=206
			a.add(new ColorUIResource(195,190,0));
			
			//Second colour : R=136,G=255,B=254
			a.add(new ColorUIResource(225,235,0));
			
			//Third colour : R=0,G=142,B=140
			a.add(new ColorUIResource(195,190,0));
			
			//Set Button.gradient key with new value
			manager.put("Button.gradient",a);
			
			mainPanel.SetButtonsInvisible();
			mainPanel.setCurrentStyle("caterpillar");
			mainPanel.ResetButtonStates("nothing", "caterpillar");
			mainPanel.SetButtonsVisible();
			
			try {
				mainPanel.getPanel().setImage("gui_images/backgrounds/caterpillar.jpg");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Logout Class - Contains the Method to be called for Logging out of the Program
	 *
	 */
	private class Logout implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			/*Custom Button Gradient*/			
			//Create linked list that will store all gradient information
			//You can try to understand it by change it's value
			LinkedList<Object> a = new LinkedList<Object>();
			a.add(0.3);
			a.add(0.3);
			
			a.add(new ColorUIResource(255,102,0));
			a.add(new ColorUIResource(255,153,0));
			a.add(new ColorUIResource(255,102,0));
			
			//Set Button.gradient key with new value
			manager.put("Button.gradient",a);
			
			
			mainFrame.setVisible(false);
			loginFrame.setVisible(true);
			loginFrame.setEnabled(true);
		}
	}
	
	/**
	 * EndGame Class - Contains the Method to be called for Ending the Game.
	 *
	 */
	private class EndGames implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			GameSettings gs= new GameSettings();
			
			

			if (gs.current_round!=gs.getNumTotalRounds()){
				JOptionPane.showMessageDialog(null, "You must be in the final week to end the game","Not in final week", JOptionPane.ERROR_MESSAGE);
				
				return;
			}
			if (gs.hasGameEnded()==true){
				JOptionPane.showMessageDialog(null, "The Game has already ended!", 
						"Game Already Ended", JOptionPane.ERROR_MESSAGE);
				
				return;
			}
				
			Contestants contestants= new Contestants();
			contestants.resetFromFile(gs.filename_contestants);
			Contestant c=new Contestant();
			int remaining=0;
			
			for (int i=0; i< gs.getNumContestants();i++){
				c=contestants.getContestantint(i);
				if (c.getRoundEliminated()==-1){
					remaining++;
				}
				
		
			}
			
			if (remaining<2){
				JOptionPane.showMessageDialog(null, "Too many players have been eliminated...internal error!", 
						"Game Ended", JOptionPane.ERROR_MESSAGE);
					return;	
			}else if(remaining>2){
				JOptionPane.showMessageDialog(null, "Not enough players have been eliminated!" +
						"Be sure to manage your eliminations properly!", 
						"Game Ended", JOptionPane.ERROR_MESSAGE);
				return ;
			}
			
			EndGame eg=new EndGame();
			if(eg.commitGame()){
				
				
				//create winners textfile
				 
				  
				File file=new File(gs.filename_winners);
		
					try {
						FileWriter fstream2 = new FileWriter(file);
						BufferedWriter out = new BufferedWriter(fstream2);
						
						String strLine;
						Player[] p_arr;
						Players ps= new Players();
						double[] winnings;
						ps.resetFromFile(gs.filename_players);
						
						Scoring sc= new Scoring();
						sc.update_scores(true);
						p_arr=sc.setScoreOrdering(ps, gs.getCurrentRound());
						winnings=sc.assignMoneyToWinners(p_arr);
						String delim="--";
						int idx;
						

						for (int i=0; i<winnings.length; i++){
							idx=p_arr.length-1-i;
							
							out.write(p_arr[idx].getID()+delim+(i+1)+delim+p_arr[idx].getScore()+delim+sc.roundTwoPlaces(winnings[i]));
							out.newLine();
						}

						out.close();
						gs.setGameEnded(1);
						gs.toFile(gs.filename_settings);
						
						mainPanel.DisableAllFunctionality();
						mainPanel.DisplayWinnersScreen();
						JOptionPane.showMessageDialog(null, "The Game is now over.  All Functionality has been Disabled.", 
								"Game Over!", JOptionPane.INFORMATION_MESSAGE);
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						
						JOptionPane.showMessageDialog(null, "There has been an internal error!", 
								"ERROR: Cannot create the winners textfile", JOptionPane.INFORMATION_MESSAGE);
						e1.printStackTrace();
					}
					
				}else{
					JOptionPane.showMessageDialog(null, "There has been an internal error!", 
							"ERROR: C", JOptionPane.INFORMATION_MESSAGE);
				}
			
				
				//output
		}
	}
	
	
	
	/**
	 * EndGame Class - Contains the Method to be called for Ending the Game.
	 *
	 */
	private class StartNewGame implements ActionListener {
		
		
		public void actionPerformed(ActionEvent e) {
			  NewGame ng= new NewGame();
			  if (ng.allFilesExists()){
				  ng.createNewGame();
					JOptionPane.showMessageDialog(null, "You must Restart the program for the changes to take effect", 
							"Create New Game", JOptionPane.INFORMATION_MESSAGE);
					
				GameSettings gs=new GameSettings();
				gs.setCurrentRound(-1);
				gs.setBetAmount(-1);
				gs.setGameEnded(0);
				gs.setUlimateWinner("notYETset");
				gs.toFile(gs.filename_settings);
				//System.out.println("----=-----------------------------------------------]" + gs.getCurrentRound());
				gs.toFile(gs.filename_settings);
				  System.exit(0);
				  
			  }else{
			  }
		}
	}
	
	/**
	 * Exit Handler - Contains Method to be called for Exiting the Program.
	 *
	 */
	private class ExitHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	/*End Action Methods*/
	
	
	/**
	 * Main Function Call. Creates the Login Frame from which the Main Screen can be accessed.
	 * @param s
	 * @throws IOException
	 */
	public static void main(String s[]) throws IOException {

		/*Build the Login Screen*/
		Login loginScreen = new Login();

		/*Create Login Frame and Set Layout*/
		loginFrame = new JFrame("CS2212 Group Assignment 2");
		loginFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		loginFrame.getContentPane().add(loginPanel);
		loginFrame.pack();
		loginFrame.setSize(500, 500);
		loginFrame.setVisible(true);
		loginFrame.setResizable(false);
		
	}
}
