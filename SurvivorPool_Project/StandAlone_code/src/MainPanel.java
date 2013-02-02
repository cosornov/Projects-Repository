import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainPanel {

	/*Begin Main Panel Component Attributes*/
	private static ImagePanel mainPanel;						//Main Panel JPanel
	private static JPanel contentPanel;						//Content Panel JPanel
	private static JButton listPlayersButton;				//List Players JButton
	private static JButton listContestantsButton;			//List Contestants JButton
	private static JButton listBonusQAButton;				//List Bonus Q&A's JButton
	private static JButton manageEliminationsButton;		//Manage Elimnations JButton
	private static JButton manageScoresButton;				//Manage Scores JButton
	/*End Main Panel Component Attributes*/
	
	/*Image Icons for Buttons*/
	private static ImageIcon listPlayers;
	private static ImageIcon listContestants;
	private static ImageIcon manageBonus;
	private static ImageIcon manageElims;
	private static ImageIcon manageScores;
	/*End Main Panel Component Attributes*/
	
	/*Begin Labels etc for Player Positions*/
	private String winners;
	/*End Labels etc for Player Positions*/
	
	/*Initialize Respective Panels*/
	private StatusPanel statusPanel;
	private PlayersPanel listPlayersPanel;
	private ContestantsPanel listContestantsPanel;
	private BonusQAPanel listBonusQAPanel;
	private EliminationPanel manageEliminationsPanel;
	private ManageScoresPanel manageScoresPanel;
	
	/*Initialize GameSettings & Login Objects*/
	public GameSettings gSettings;
	public Login login;
	
	public String currentStyle = "jungle";
	
	/*Start Action Methods*/
	
	/**
	 * Class ExitHandler - Contains the Action Method for Exiting the Program.
	 *
	 */
	private class ExitHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	/**
	 * ListPlayers Class - Contains Action Method for displaying the listPlayersPanel.
	 *
	 */
	private class ListPlayers implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			contentPanel.setVisible(false);
				listPlayersPanel.getPanel().setVisible(true);
				listPlayersPanel.getEditPanel().setVisible(false);
				listContestantsPanel.getPanel().setVisible(false);
				listContestantsPanel.getEditPanel().setVisible(false);
				listBonusQAPanel.getPanel().setVisible(false);
				listBonusQAPanel.getEditPanel().setVisible(false);
				manageEliminationsPanel.getPanel().setVisible(false);
				manageEliminationsPanel.getEditPanel().setVisible(false);
				manageScoresPanel.getPanel().setVisible(false);
				
				
				/*Set Current State Buttons*/
				SetButtonsInvisible();
				ResetButtonStates("listPlayers", currentStyle);
				SetButtonsVisible();
				
		}
	}
	
	/**
	 * ListContestants Class - Contains Action Method for displaying the listContestantsPanel.
	 *
	 */
	private class listContestants implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			contentPanel.setVisible(false);
			listPlayersPanel.getPanel().setVisible(false);
			listPlayersPanel.getEditPanel().setVisible(false);
			listContestantsPanel.getPanel().setVisible(true);
			listContestantsPanel.getEditPanel().setVisible(false);
			listBonusQAPanel.getPanel().setVisible(false);
			listBonusQAPanel.getEditPanel().setVisible(false);
			manageEliminationsPanel.getPanel().setVisible(false);
			manageEliminationsPanel.getEditPanel().setVisible(false);
			manageScoresPanel.getPanel().setVisible(false);
	
			/*Set Current State Buttons*/
			SetButtonsInvisible();
			ResetButtonStates("listContestants", currentStyle);
			SetButtonsVisible();
		}
	}
	
	/**
	 * showBonusQAPanel Class - Contains Action Method for displaying the listBonusQAPanel.
	 *
	 */
	private class showBonusQAPanel implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			contentPanel.setVisible(false);
			listPlayersPanel.getPanel().setVisible(false);
			listPlayersPanel.getEditPanel().setVisible(false);
			listContestantsPanel.getPanel().setVisible(false);
			listContestantsPanel.getEditPanel().setVisible(false);
			listBonusQAPanel.getPanel().setVisible(true);
			listBonusQAPanel.getEditPanel().setVisible(false);
			manageEliminationsPanel.getPanel().setVisible(false);
			manageEliminationsPanel.getEditPanel().setVisible(false);
			manageScoresPanel.getPanel().setVisible(false);

			/*Set Current State Buttons*/
			SetButtonsInvisible();
			ResetButtonStates("manageBonus", currentStyle);
			SetButtonsVisible();
		}
	}
	
	/**
	 * showManageEliminationsPanel Class - Contains Action Method for displaying the manageEliminationsPanel.
	 *
	 */
	private class showManageEliminationsPanel implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			contentPanel.setVisible(false);
			listPlayersPanel.getPanel().setVisible(false);
			listPlayersPanel.getEditPanel().setVisible(false);
			listContestantsPanel.getPanel().setVisible(false);
			listContestantsPanel.getEditPanel().setVisible(false);
			listBonusQAPanel.getPanel().setVisible(false);
			listBonusQAPanel.getEditPanel().setVisible(false);
			manageEliminationsPanel.getPanel().setVisible(true);
			manageEliminationsPanel.getEditPanel().setVisible(false);
			manageScoresPanel.getPanel().setVisible(false);
			
			/*Set Current State Buttons*/
			SetButtonsInvisible();
			ResetButtonStates("manageElims", currentStyle);
			SetButtonsVisible();
		}
	}
	
	/**
	 * showManageScoresPanel Class - Contains the Method for displaying the manageScoresPanel.
	 *
	 */
	private class showManageScoresPanel implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			contentPanel.setVisible(false);
			listPlayersPanel.getPanel().setVisible(false);
			listPlayersPanel.getEditPanel().setVisible(false);
			listContestantsPanel.getPanel().setVisible(false);
			listContestantsPanel.getEditPanel().setVisible(false);
			listBonusQAPanel.getPanel().setVisible(false);
			listBonusQAPanel.getEditPanel().setVisible(false);
			manageEliminationsPanel.getPanel().setVisible(false);
			manageEliminationsPanel.getEditPanel().setVisible(false);
			manageScoresPanel.getPanel().setVisible(true);
			
			/*Set Current State Buttons*/
			SetButtonsInvisible();
			ResetButtonStates("manageScores", currentStyle);
			SetButtonsVisible();
		}
	}
	/*End Action Methods*/
	
	

	
	/**Transparent Panel Class**/
	class TransparentButton extends JButton {
		public TransparentButton(Icon ico) { 
			super(ico);
			setOpaque(false);
			setBorderPainted(false);
			setFocusPainted(false);
			setBackground(new Color(0, 0, 0, 0));
		}
	}
	
	
	/*New Function*/
	public void SetButtonsVisible (){
		listPlayersButton.setVisible(true);
		listContestantsButton.setVisible(true);
		listBonusQAButton.setVisible(true);
		//manageEliminationsButton.setVisible(true);
		//manageScoresButton.setVisible(true);
		
		//NEIL
		gSettings.update();
		if (gSettings.hasGameStarted()==true){
			manageScoresButton.setVisible(true);
			manageEliminationsButton.setVisible(true);
		}else{
			manageScoresButton.setVisible(false);
			manageEliminationsButton.setVisible(false);
		}
	}
	
	/*New Function*/
	public void SetButtonsInvisible (){
		listPlayersButton.setVisible(false);
		listContestantsButton.setVisible(false);
		listBonusQAButton.setVisible(false);
		manageEliminationsButton.setVisible(false);
		manageScoresButton.setVisible(false);
	}
	
	/*New Function*/
	public void ResetButtonStates (String s, String theme){
		/*Create and set Layout for List Players Button.
		 * Add List Players Button to Main Panel and add Action Listener*/
		if (s.equals("listPlayers")){
			ImageIcon listPlayersCurrent = new ImageIcon("gui_images/buttons/"+ theme +"/hover_states/1.png");
			listPlayersButton = new TransparentButton(listPlayersCurrent);
		}
		else{
			listPlayers = new ImageIcon("gui_images/buttons/"+ theme +"/list_players.png");
			listPlayersButton = new TransparentButton(listPlayers);
		}
		//listPlayersButton = new JButton("List Players");
		mainPanel.add(listPlayersButton);
		listPlayersButton.setBounds(6, 60, 208, 74);
		listPlayersButton.addActionListener(new ListPlayers());
		
		/*Create and set Layout for List Contestants Button.
		 * Add List Contestants to Main Panel and add Action Listener*/
		if (s.equals("listContestants")){
			ImageIcon listContestantsCurrent = new ImageIcon("gui_images/buttons/"+ theme +"/hover_states/2.png");
			listContestantsButton = new TransparentButton(listContestantsCurrent);
		}
		else{
			listContestants = new ImageIcon("gui_images/buttons/"+ theme +"/list_contestants.png");
			listContestantsButton = new TransparentButton(listContestants);
		}
		mainPanel.add(listContestantsButton);
		listContestantsButton.setBounds(6, 150, 208, 74);
		listContestantsButton.addActionListener(new listContestants());
		
		/*Create and set Layout for List Bonus Q&A's Button.
		 * Add List Bonus Q&A's to Main Panel and add Action Listener*/
		if (s.equals("manageBonus")){
			ImageIcon manageBonusCurrent = new ImageIcon("gui_images/buttons/"+ theme +"/hover_states/3.png");
			listBonusQAButton = new TransparentButton(manageBonusCurrent);
		}
		else{
			manageBonus = new ImageIcon("gui_images/buttons/"+ theme +"/manage_bonus.png");
			listBonusQAButton = new TransparentButton(manageBonus);
		}
		mainPanel.add(listBonusQAButton);
		listBonusQAButton.setBounds(6, 240, 208, 74);
		listBonusQAButton.addActionListener(new showBonusQAPanel());
		
		/*Create and set Layout for Manage Eliminations Button.
		 * Add Manage Eliminations Button to Main Panel and add Action Listener*/
		if (s.equals("manageElims")){
			ImageIcon manageElimsCurrent = new ImageIcon("gui_images/buttons/"+ theme +"/hover_states/4.png");
			manageEliminationsButton = new TransparentButton(manageElimsCurrent);
		}
		else{
			manageElims = new ImageIcon("gui_images/buttons/"+ theme +"/manage_elim.png");
			manageEliminationsButton = new TransparentButton(manageElims);
		}
		mainPanel.add(manageEliminationsButton);
		manageEliminationsButton.setBounds(6, 330, 208, 74);
		manageEliminationsButton.addActionListener(new showManageEliminationsPanel());
		
		/*Create and set Layout for Manage Scores Button.
		 * Add Manage Scores Button to Main Panel and add Action Listener*/
		if (s.equals("manageScores")){
			ImageIcon manageScoresCurrent = new ImageIcon("gui_images/buttons/"+ theme +"/hover_states/5.png");
			manageScoresButton = new TransparentButton(manageScoresCurrent);
		}
		else{
			manageScores = new ImageIcon("gui_images/buttons/"+ theme +"/manage_scores.png");
			manageScoresButton = new TransparentButton(manageScores);
		}
		mainPanel.add(manageScoresButton);
		manageScoresButton.setBounds(6, 420, 208, 74);
		manageScoresButton.addActionListener(new showManageScoresPanel());
	
		//NEIL
		gSettings.update();
		if (gSettings.hasGameStarted()==true){
			manageScoresButton.setVisible(true);		
			manageEliminationsButton.setVisible(true);
		}else{
			manageScoresButton.setVisible(false);
			manageEliminationsButton.setVisible(false);
		}
	}
	
	
	/**
	 * MainPanel Constructor.
	 */
public MainPanel() {
		
		/*Game Settings*/
		gSettings = new GameSettings();
		//<<<<<<<<<<<<<<<<<<<<<Need to restore the original state!

		/*Create Respective Panels*/
		statusPanel = new StatusPanel();
		listPlayersPanel = new PlayersPanel();
		listContestantsPanel = new ContestantsPanel();
		listBonusQAPanel = new BonusQAPanel();
		manageEliminationsPanel = new EliminationPanel();
		manageScoresPanel = new ManageScoresPanel();
		
		/*Begin Administrator Panel Layout*/
		//mainPanel = new JPanel();
		mainPanel = new ImagePanel("jungle", 1080, 600);
		mainPanel.setLayout(null);

		/*Main Panel Buttons/Main Functionality*/
		ResetButtonStates("nothing", "jungle");
		
		/*Create color 'custom'*/
		Color custom = new Color(225, 225, 225);
		
		/*Create & set Layout of contentPanel JPanel.
		 * Set Layout to null for Absolute Positioning and set Color to 'custom'.*/
		contentPanel = new TransparentPanel();
		contentPanel.setLayout(null);
		contentPanel.setBounds(220, 60, 830, 470);
		//contentPanel.setBackground(custom);
		
		/*Add Respective Panels to Main Panel.*/
		mainPanel.add(statusPanel.getPanel());
		mainPanel.add(contentPanel);
		mainPanel.add(listPlayersPanel.getPanel());
		mainPanel.add(listContestantsPanel.getPanel());
		mainPanel.add(listBonusQAPanel.getPanel());
		mainPanel.add(manageEliminationsPanel.getPanel());
		mainPanel.add(manageScoresPanel.getPanel());
		
		if(gSettings.hasGameEnded()==true){
			DisableAllFunctionality();
			statusPanel.DisableIncrement();			
		}
		
	}

		public MainPanel(String buttonTheme, String background) {
			
			/*Game Settings*/
			gSettings = new GameSettings();
			//<<<<<<<<<<<<<<<<<<<<<Need to restore the original state!
		
			/*Create Respective Panels*/
			statusPanel = new StatusPanel();
			listPlayersPanel = new PlayersPanel();
			listContestantsPanel = new ContestantsPanel();
			listBonusQAPanel = new BonusQAPanel();
			manageEliminationsPanel = new EliminationPanel();
			manageScoresPanel = new ManageScoresPanel();
			
			/*Begin Administrator Panel Layout*/
			//mainPanel = new JPanel();
			mainPanel = new ImagePanel(background, 1080, 600);
			mainPanel.setLayout(null);
		
			/*Main Panel Buttons/Main Functionality*/
			ResetButtonStates("nothing", buttonTheme);
			
			/*Create color 'custom'*/
			Color custom = new Color(225, 225, 225);
			
			/*Create & set Layout of contentPanel JPanel.
			 * Set Layout to null for Absolute Positioning and set Color to 'custom'.*/
			contentPanel = new TransparentPanel();
			contentPanel.setLayout(null);
			contentPanel.setBounds(220, 60, 830, 470);
			//contentPanel.setBackground(custom);
			
			/*Add Respective Panels to Main Panel.*/
			mainPanel.add(statusPanel.getPanel());
			mainPanel.add(contentPanel);
			mainPanel.add(listPlayersPanel.getPanel());
			mainPanel.add(listContestantsPanel.getPanel());
			mainPanel.add(listBonusQAPanel.getPanel());
			mainPanel.add(manageEliminationsPanel.getPanel());
			mainPanel.add(manageScoresPanel.getPanel());
			
			if(gSettings.hasGameEnded()==true){
				DisableAllFunctionality();
				statusPanel.DisableIncrement();
			}
		}
	
	/**
	 * getPanel Method - Returns the mainPanel JPanel.
	 * @return mainPanel
	 */
	public ImagePanel getPanel() {
		return mainPanel;
	}
	
	public String getCurrentStyle(){
		return currentStyle;
	}
	
	public void setCurrentStyle(String s){
		currentStyle=s;
	}
	
	public void DisableAllFunctionality(){
		statusPanel.DisableIncrement();
		statusPanel.GetStandingsButton().setEnabled(true);
		statusPanel.GetStandingsButton().setVisible(true);
		statusPanel.ChangeStatusLabel();
		listPlayersPanel.disableButtons();
		listContestantsPanel.disableButtons();
		listBonusQAPanel.disableButtons();
		manageEliminationsPanel.disableButtons();
		manageScoresPanel.disableButtons();
	}
	
	/**
	 * DisplayWinnersScreen Method - Produces Popup with the Final Standings.
	 */
	public void DisplayWinnersScreen(){
		DisplayWinners dw = new DisplayWinners(gSettings.filename_winners);
		String[] tmp = dw.getInfo();
		String[] another = dw.makeReadable(tmp);
		String erryting="";//<html> <br>";
		
		for(int i=0;i<another.length;i++) {
			
			erryting=erryting+another[i];
			
		}	
		
		//erryting=erryting+"</html>";
		JOptionPane.showMessageDialog(null, erryting,
				 "Final Standings", JOptionPane.DEFAULT_OPTION);
	}
	
}
