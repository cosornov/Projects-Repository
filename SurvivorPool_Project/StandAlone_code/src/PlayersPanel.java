import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class PlayersPanel{

	/*Begin PlayersPanel Attributes*/
	private static JPanel listPlayersPanel;						//List Players Panel
	private JPanel listPlayersEditPanel;						//List Players Edit Panel
	private static DefaultTableModel playersDM;					//Players Default Table Model
	private static Object[][] playersData;						//2D Object to store Players Data
	private static Object[] playersHeader;						//Object to store Players Table Columns
	private static JTable listPlayersTable;						//List Players Table
	private static JScrollPane listPlayersscrollPane;			//List Players Table Scroll Pane
	private static JButton listPlayersAddButton;				//Add Player Button
	private static JButton listPlayersEditButton;				//Edit Player Button
	private static JButton listPlayersDeleteButton;				//Delete Player Button
	private static String listPlayerOptionEntry;				//String for storing Option Panel content
	private static JLabel fNameLabel;							//First Name Label
	private static JLabel lNameLabel;							//Last Name Label
	private static RoundJTextField fNameField;						//First Name Text Field
	private static JTextField lNameField;						//Last Name Text Field
	private static JLabel uIDLabel;								//User ID Label
	private static JTextField uIDTextArea;						//User ID Text Field
	private static JLabel totalScoreLabel;						//Total Score Label
	private static JTextField totalScoreTextArea;				//Total Score Text field
	private static JButton listPlayerAddPlayerButton;			//Add Player (Edit Panel) Button
	private static JButton listPlayerEditPlayerButton;			//Edit Player (Edit Panel) Button
	private static JButton refreshPTable;						//Refresh Table Button
	/*End PlayersPanel Attributes*/
	
	/*Players (Backend) Object*/
	private Players players;
	
	/*Game Settings Object*/
	public GameSettings gameSettings;
	
	/*Regex for players names - only letters, case insensitive*/
	private Pattern LETTERSONLY = Pattern.compile("[a-zA-Z]+");
	
	
	/*Action Methods*/
	/**
	 * createPlayersTable Method - Method for creating & updating the Players JTable.
	 * @param createFromFile
	 */
	public void createPlayersTable(String createFromFile) {
		/*playersHeader Object containing the Columns for the listPlayersTable*/
		playersHeader = new Object[] {
				"User ID",
				"First Name",
				"Last Name",
				"Total Score"};
		
		/*Conditional used to control is table is created or updated*/
		if (createFromFile.equalsIgnoreCase("y")){
			players = new Players();
			
			/*Restore Players Data from Players Text File*/
			players.resetFromFile(GameSettings.filename_players);
		} else{
			players.resetFromFile(GameSettings.filename_players);
		}
		
		playersData = players.prepforTable();

		/*Conditional Statement which updates the JTable*/
		if (createFromFile.equalsIgnoreCase("y")){
			//use "prepforTable"
			playersDM = new DefaultTableModel(){
			    @Override
			    public boolean isCellEditable(int row, int column) {
			       //all cells false
			       return false;
			    }
			};
			playersDM.setDataVector(playersData, playersHeader);
						
			listPlayersTable = new JTable(playersDM) {
				public void tableChanged(TableModelEvent e) {
					super.tableChanged(e);
				    repaint();
				}};
				
			/*Adding Table to Scroll Pane and setting Location*/
			listPlayersscrollPane = new JScrollPane(listPlayersTable);
			listPlayersTable.setFillsViewportHeight(true);
			listPlayersPanel.add(listPlayersscrollPane);
			listPlayersscrollPane.setBounds(10, 10, 600, 210);
			
			/*Create Sorting Functionality*/
			listPlayersTable.setAutoCreateRowSorter(true);
			listPlayersTable.getTableHeader().setReorderingAllowed(false);
		}else{
			playersDM.setDataVector(playersData, playersHeader);
			listPlayersTable = new JTable(playersDM);
		}
	}
	
	
	/**
	 * listPlayersAddPlayer Class - Contains Method for displaying the Panel for Adding Players.
	 *
	 */
	private class listPlayersAddPlayer implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			/*Recreate/Update the Table*/
			gameSettings.update();
			createPlayersTable("no");

				/*Display appropriate Buttons and Text Fields*/
				listPlayerEditPlayerButton.setVisible(false);
				listPlayersEditPanel.setVisible(true);
				listPlayerAddPlayerButton.setVisible(true);
				fNameField.setText("");
				lNameField.setText("");
				uIDTextArea.setText("");

		}
	}
	
	/**
	 * listPlayersEditPlayer Class - Contains Method for displaying the Panel for Editing Players.
	 *
	 */
	private class listPlayersEditPlayer implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			/*Redraw/Update the Table*/
			createPlayersTable("no");
		
			/*If there are more than 0 Players*/
			if (players.getNumPlayers()>0){
				listPlayerOptionEntry = JOptionPane
						.showInputDialog("Enter the User ID of the Player which you wish to EDIT:");
				
				/*If the Value entered into the Option Pane is not empty or not Cancel*/
				if((listPlayerOptionEntry!=null)&&(listPlayerOptionEntry.isEmpty()!=true)){
					int index=players.existsPlayer_byUserID(listPlayerOptionEntry);
					if (index!=-1){
						listPlayersEditPanel.setVisible(false);
						listPlayersEditPanel.setVisible(true);
						listPlayerAddPlayerButton.setVisible(false);
						listPlayerEditPlayerButton.setVisible(true);
						
						/*Get User information and populate Appropriate Text Fields with data*/
						Player tmpPlayer=players.getPlayer(index);
						fNameField.setText(tmpPlayer.getFName());
						lNameField.setText(tmpPlayer.getLName());
						uIDTextArea.setEditable(false);
						uIDTextArea.setText(listPlayerOptionEntry);
						totalScoreTextArea.setEditable(false);
						totalScoreTextArea.setText(Integer.toString(tmpPlayer.getScore()));
					} else{
						/*Show Error Popup*/
						JOptionPane.showMessageDialog(null, 
								"No User with that UserID exists...", "Error: Invalid UserID", JOptionPane.ERROR_MESSAGE);
					}
				}
				else if (listPlayerOptionEntry==null){
					//Players Clicked Cancel
				}
			}
			else if (players.getNumPlayers()==0){
				/*Show Error Popup*/
				JOptionPane.showMessageDialog(null, "There are no Players to Edit!", "Error: 0 Players", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * listPlayersDeletePlayer Class - Contains Method for Displaying and Updating the Panel for Deleting Players.
	 *
	 */
	private class listPlayersDeletePlayer implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			gameSettings.update();
			/*Redraw/Update the Table*/
			createPlayersTable("no");

			/*Initially Set the Edit Panel to not be displayed.*/
			listPlayersEditPanel.setVisible(false);
			
			/*If there are more than 0 Players*/
			if (players.getNumPlayers()>0){
				listPlayerOptionEntry = JOptionPane
						.showInputDialog("Enter the User ID of the Player which you wish to DELETE:");

				if (listPlayerOptionEntry!=null){
					/*Only delete Player if they Exist*/
					int index=players.existsPlayer_byUserID(listPlayerOptionEntry);
					if (index!=-1){
						players.deletePlayer_byUserID(listPlayerOptionEntry);
						
						/*Update Text File*/
						players.toFile(gameSettings.filename_players);

						/*Display Message confirming deletion*/
						JOptionPane.showMessageDialog(null, "Succesfully Deleted "+listPlayerOptionEntry
								, "Player Deleted!", JOptionPane.DEFAULT_OPTION);
						/*Redraw/Update Table*/
						createPlayersTable("no");
						gameSettings.setNumPlayers(players.getNumPlayers());
						gameSettings.toFile(gameSettings.filename_settings);
					}
					else{
						/*Show Error Popup*/
						JOptionPane.showMessageDialog(null, "No User with that UserID exists.. ", 
												"Error: Invalid User ID", JOptionPane.ERROR_MESSAGE);
					}	
				}
			}
			else{
				/*Show Error Popup*/
				JOptionPane.showMessageDialog(null, "There are no Players to Delete!", "Error: 0 players",
											JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	
	//NEIL!
	/**
	 * 
	 **/
		private class refreshTable implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				createPlayersTable("n");
			}
			
		}
	
	/**
	 * CheckValidAdd Class - Contains Method for Adding a Player to the Pool.
	 *
	 */
	private class CheckValidAdd implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			gameSettings.update();
			
			/*Field Limitation Checks*/
			
			if(fNameField.getText().length()>20 ||
					!areLetters(fNameField.getText())) {
				JOptionPane.showMessageDialog(null, "First name must be 1-20 letters");
			} else if(lNameField.getText().length()>20 ||
					!areLetters(lNameField.getText())) {
				JOptionPane.showMessageDialog(null, "Last name must be 1-20 letters");
			}
			else{
				Player tmpPlayer= new Player(fNameField.getText(), lNameField.getText());
				players.addPlayer(tmpPlayer, "y");
				
				/*Update Text File*/
				players.toFile(gameSettings.filename_players);

				/*Show message dialog verifying that a User was added*/
				JOptionPane.showMessageDialog(null, "Succesfully added.  The UserID is: "
							+players.getPlayer(players.getNumPlayers()-1).getID()
							, "Player Added!", JOptionPane.DEFAULT_OPTION);
				
				/*Update Game Settings*/
				gameSettings.setNumPlayers(players.getNumPlayers());
				gameSettings.toFile(gameSettings.filename_settings);
				
				/*Redraw/Update Table*/
				createPlayersTable("no");	
				
				fNameField.setText("");
				lNameField.setText("");
			}
		}
	}
	
	/**
	 * CheckValidEdit Class - Contains Method for Editing a Player ing the Pool.
	 *
	 */
	private class CheckValidEdit implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			gameSettings.update();
			/*Field Limitation Checks*/
			if(fNameField.getText().length()>20 ||
					!areLetters(fNameField.getText())) {
				JOptionPane.showMessageDialog(null, "First name must be 1-20 letters");
			} else if(lNameField.getText().length()>20 ||
					!areLetters(lNameField.getText())) {
				JOptionPane.showMessageDialog(null, "Last name must be 1-20 letters");
			}
			else{
				Player tmpPlayer= new Player(fNameField.getText(), lNameField.getText());
				tmpPlayer.setID(uIDTextArea.getText());
				players.deletePlayer_byUserID(uIDTextArea.getText());
				players.addPlayer(tmpPlayer, "n");
				
				/*Update Text File*/
				players.toFile(gameSettings.filename_players);

				/*Show message dialog verifying that a User was Edited*/
				JOptionPane.showMessageDialog(null, "Succesfully Edited "+uIDTextArea.getText()
						, "Player Edited!", JOptionPane.DEFAULT_OPTION);

				/*Update Game Settings*/
				gameSettings.setNumPlayers(players.getNumPlayers());
				gameSettings.toFile(gameSettings.filename_settings);
				
				/*Redraw/Update Table*/
				createPlayersTable("no");
			}
		}
	}
	/*End Action Methods*/
	
	
	/**
	 * Method to check for invalid input
	 **/
	private boolean areLetters(String s) {
		Matcher m = LETTERSONLY.matcher(s);
		return m.matches();
	}
	
	
	/**
	 * PlayersPanel Constructor.
	 */
	public PlayersPanel() {
		/*Create Game Settings Object*/
		gameSettings= new GameSettings();
		
		/*Create Color 'custom'*/
		Color custom = new Color(225, 225, 225);
		
		/*List Players Content Pane Code*/
		listPlayersPanel = new TransparentPanel();
		listPlayersPanel.setLayout(null);
		listPlayersPanel.setBounds(220, 60, 830, 470);
		listPlayersPanel.setBackground(custom);
		listPlayersPanel.setVisible(false);
		
		/*Create Players Table*/
		createPlayersTable("y");
		
		/*Add Player Button Code - Creation, Layout & Action Listener*/
		listPlayersAddButton = new JButton("Add Player");
		if ((gameSettings.hasGameStarted()==false)){
			listPlayersAddButton.addActionListener(new listPlayersAddPlayer());
		}	else{
			
			listPlayersAddButton.setEnabled(false);
		}
		listPlayersPanel.add(listPlayersAddButton);
		listPlayersAddButton.setBounds(620, 10, 200, 20);
		
		/*Edit Player Button Code - Creation, Layout & Action Listener*/
		listPlayersEditButton = new JButton("Edit Player");
		listPlayersEditButton.addActionListener(new listPlayersEditPlayer());
		listPlayersPanel.add(listPlayersEditButton);
		listPlayersEditButton.setBounds(620, 50, 200, 20);
		
		/*Edit Player Button Code - Creation, Layout & Action Listener*/
		listPlayersDeleteButton = new JButton("Delete Player");
		listPlayersDeleteButton.setVisible(true);
		if ((gameSettings.hasGameStarted()==false)){
			listPlayersDeleteButton.addActionListener(new listPlayersDeletePlayer());
		}	else{
			listPlayersDeleteButton.setEnabled(false);
		}
		listPlayersPanel.add(listPlayersDeleteButton);
		listPlayersDeleteButton.setBounds(620, 90, 200, 20);
		
		//NEIL
		refreshPTable = new JButton("Refresh Table");
		listPlayersPanel.add(refreshPTable);
		refreshPTable.setVisible(true);
		refreshPTable.addActionListener(new refreshTable());
		refreshPTable.setBounds(620, 130, 200, 20);
		
		/*Begin Edit Panel (Add/Delete Functionality) Code*/
		listPlayersEditPanel = new JPanel();
		listPlayersEditPanel.setLayout(null);
		listPlayersPanel.add(listPlayersEditPanel);
		listPlayersEditPanel.setBounds(10, 240, 810, 220);
		listPlayersEditPanel.setBackground(Color.WHITE);
		
		/*First Name Label & Text Field Code - Creation, Layout*/
		fNameLabel = new JLabel("First Name:");
		listPlayersEditPanel.add(fNameLabel);
		fNameLabel.setBounds(260, 10, 200, 20);
		fNameField = new RoundJTextField(10);
		listPlayersEditPanel.add(fNameField);
		fNameField.setText("firstname");
		fNameField.setBounds(260, 30, 200, 20);
		
		/*Last Name Label & Text Field Code - Creation, Layout*/
		lNameLabel = new JLabel("Last Name:");
		listPlayersEditPanel.add(lNameLabel);
		lNameLabel.setBounds(520, 10, 200, 20);
		lNameField = new RoundJTextField(10);
		listPlayersEditPanel.add(lNameField);
		lNameField.setText("lastname");
		lNameField.setBounds(520, 30, 200, 20);
		
		/*User IS Label & Text Field Code - Creation, Layout*/
		uIDLabel = new JLabel("User ID:");
		listPlayersEditPanel.add(uIDLabel);
		uIDLabel.setBounds(10, 10, 200, 20);
		uIDTextArea = new RoundJTextField(10);
		uIDTextArea.setEditable(false);
		listPlayersEditPanel.add(uIDTextArea);
		uIDTextArea.setBounds(10, 30, 200, 20);
		
		/*Total Score Label & Text Field Code - Creation, Layout*/
		totalScoreLabel = new JLabel("Total Score:");
		listPlayersEditPanel.add(totalScoreLabel);
		totalScoreLabel.setBounds(10, 60, 200, 20);
		totalScoreTextArea = new RoundJTextField(10);
		totalScoreTextArea.setText("0");
		totalScoreTextArea.setEditable(false);
		listPlayersEditPanel.add(totalScoreTextArea);
		totalScoreTextArea.setBounds(10, 80, 200, 20);
		
		/*Add Player (Edit Panel) Label & Text Field Code - Creation, Layout, Action Listener*/
		listPlayerAddPlayerButton = new JButton("Add Player to Pool");
		listPlayersEditPanel.add(listPlayerAddPlayerButton);
		listPlayerAddPlayerButton.setBounds(600, 190, 200, 20);
		listPlayerAddPlayerButton.addActionListener(new CheckValidAdd());
		listPlayerAddPlayerButton.setVisible(false);
		
		/*Edit Player (Edit Panel) Label & Text Field Code - Creation, Layout, Action Listener*/
		listPlayerEditPlayerButton = new JButton("Edit Player in Pool");
		listPlayersEditPanel.add(listPlayerEditPlayerButton);
		listPlayerEditPlayerButton.setBounds(600, 190, 200, 20);
		listPlayerEditPlayerButton.addActionListener(new CheckValidEdit());
		listPlayerEditPlayerButton.setVisible(true);
		/*End Edit Panel (Add/Delete Functionality) Code*/
	}
	
	
	/**
	 * getPanel Method - Returns the listPlayersPanel JPanel.
	 * @return listPlayersPanel
	 */
	public JPanel getPanel() {
		return listPlayersPanel;
	}
	
	/**
	 * getEditPanel Method - Returns the listPlayersEditPanel JPanel.
	 * @return listPlayersEditPanel
	 */
	public JPanel getEditPanel() {
		return listPlayersEditPanel;
	}
	
	/**
	 * getAddButton Method - Returns the listPlayersAddButton JButton.
	 * @return listPlayersAddButton
	 */
	public JButton getAddButton(){
		return listPlayersAddButton;
	}

	/**
	 * getEditButton Method - Returns the listPlayersEditButton JButton.
	 * @return listPlayersEditButton
	 */
	public JButton getEditButton(){
		return listPlayersEditButton;
	}
	
	/**
	 * getDeleteButton Method - Returns the listPlayersDeleteButton JButton.
	 * @return listPlayersDeleteButton
	 */
	public JButton getDeleteButton(){
		return listPlayersDeleteButton;
	}
	
	/**
	 * disableButtons Method - Disables the Functionality Buttons.
	 */
	public void disableButtons(){
		listPlayersAddButton.setEnabled(false);
		listPlayersEditButton.setEnabled(false);
		listPlayersDeleteButton.setEnabled(false);
	}
	
}
