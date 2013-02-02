import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;

public class ContestantsPanel {

	/*Begin ContestantsPanel Attributes*/
	private static JPanel listContestantsPanel;						//List Contestants Panel
	private JPanel listContestantsEditPanel;						//List Contestants Edit (Add/Edit) Panel
	private static DefaultTableModel contestantDM;					//Contestants Default Table Model
	private static Object[][] contestantData;						//2D Object Containing Contestants Data
	private static Object[] contestantHeader;						//Object Containing Contestants Table Columns
	private static JTable listContestantsTable;						//Table for Storing/Displaying Contestants Data
	private static JScrollPane listContestantsscrollPane;			//Scroll Pane for holding Contestants Table
	private static JButton listContestantsAddButton;				//Button for Adding a Contestant
	private static JButton listContestantsEditButton;				//Button for Editing a Contestant
	private static JButton listContestantsDeleteButton;				//Button for Deleting a Contestant
	private static JLabel contestantFNameLabel;						//Contestant First Name Label
	private static JLabel contestantLNameLabel;						//Contestant Last Name Label
	private static JTextField contestantFNameField;					//Contestant First Name Text Field
	private static JTextField contestantLNameField;					//Contestant Last Name Text Field
	private static JLabel contestantUIDLabel;						//Contestant User ID Label
	private static JTextField contestantUIDTextArea;				//Contestant User ID Text Field
	private static JLabel contestantTeamLabel;						//Contestant Team Label
	private static JTextField contestantTeamField;					//Contestant Team Text Field
	private static JLabel contestantElimWeekLabel;					//Contestant Elimination Week Label
	private static JTextField contestantElimWeekField;				//Contestant Elimination Week Text Field
	private static JButton listContestantAddContestantButton;		//Add Contestant (Edit Panel) Button
	private static JButton listContestantEditContestantButton;		//Edit Contestant (Edit Panel) Button
	private String listPlayerOptionEntry;							//String for storing Option Panel data
	
	private static JButton refreshTable;							//refresh the table!
	/*Begin ContestantsPanel Attributes*/
	
	/*Contestant Photo Attributes*/
	private JFileChooser photoChooser;
	private JButton photoButton;
	private File contestantPicFile;
	private BufferedImage contestantPic;
	private JLabel picLabel;
	
	/*Regular expression used to check alphanumeric input*/
	private Pattern ALPHANUMERIC = Pattern.compile("[a-z0-9]+");
	/*Regular expression used to check numbers only*/
	private Pattern NUMBER = Pattern.compile("[0-9-]+");
	
	/*Initialize Contestants and GameSettings*/
	public Contestants contestants;
	public GameSettings gameSettings;
	private long lastUpdated;

	/**
	 * createContestantsTable Method - Method for creating & updating the Contestants JTable.
	 * @param createFromFile
	 */
	public void createContestantsTable(String createFromFile) {
		
		contestantHeader = new Object[]{"Unique ID",
				"First Name",
				"Last Name",
				"Team",
				"Elimination Week"};
		
		if (createFromFile.equalsIgnoreCase("y")){
			/*Create Contestants Object*/
			contestants = new Contestants();
			
			/*Restore Contestants Data from Text File*/
			contestants.resetFromFile(gameSettings.filename_contestants);
		} else{
			contestants.resetFromFile(gameSettings.filename_contestants);
		}

		contestantData = contestants.prepforTable();
		
		/*Code for Swapping Picture and RoundEliminated*/
		if (contestants.getNumContestants()!=0){
			Object [][] tmpData= contestantData;
			for(int i=0; i<contestants.getNumContestants();i++){
				tmpData[i][4]=contestantData[i][5];
				tmpData[i][5]=contestantData[i][4];
			}
			contestantData=tmpData;
		}
		
		
		/*Code which updates the JTable with the new/added/removed data*/
		if (createFromFile.equalsIgnoreCase("y")){
			contestantDM = new DefaultTableModel(){
			    @Override
			    public boolean isCellEditable(int row, int column) {
			       //all cells false
			       return false;
			    }
			};
			contestantDM.setDataVector(contestantData, contestantHeader);
	
			listContestantsTable = new JTable(contestantDM) {
			public void tableChanged(TableModelEvent e) {
				super.tableChanged(e);
			    repaint();
			}};
			
			/*Adds Table to Scroll Pane and set up the Layout*/
			listContestantsscrollPane = new JScrollPane(listContestantsTable);
			listContestantsTable.setFillsViewportHeight(true);
			listContestantsPanel.add(listContestantsscrollPane);
			listContestantsscrollPane.setBounds(10, 10, 600, 210);
			
			/*Enables Sorting of Rows of the Table*/
			listContestantsTable.setAutoCreateRowSorter(true);
			listContestantsTable.getTableHeader().setReorderingAllowed(false);
		}
		else{
			contestantDM.setDataVector(contestantData, contestantHeader);
			listContestantsTable = new JTable(contestantDM);
		}
	}
	
	/*Start Action Methods*/
	
	
	//refresh the table
	private class refreshTable implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			createContestantsTable("n");
		}
		
	}
	
	
	/**
	 * AddContestant Class - Contains Method responsible for adding Contestants to the Game.
	 *
	 */
	private class AddContestant implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			/*Redraw/Update Contestants Table*/
			createContestantsTable("no");

			if(gameSettings.getNumContestants()>=gameSettings.contesants_max){
				JOptionPane.showMessageDialog(null, "Cannot Add More Contestants!");
			}
			else if (contestants.getNumContestants()<=GameSettings.contesants_max-1){
				listContestantEditContestantButton.setVisible(false);
				listContestantsEditPanel.setVisible(true);
				picLabel.setVisible(false);
				listContestantAddContestantButton.setVisible(true);
				
				contestantPicFile=new File("");
				
				/*Clear Contestant Add (Edit) Text Fields*/
				contestantUIDTextArea.setText("");
				contestantTeamField.setText("");
				contestantFNameField.setText("");
				contestantLNameField.setText("");
				contestantElimWeekLabel.setVisible(false);
				contestantElimWeekField.setVisible(false);
				
			}else{
				listContestantEditContestantButton.setVisible(false);
				listContestantsEditPanel.setVisible(false);
				picLabel.setVisible(false);
				listContestantAddContestantButton.setVisible(false);
			}
		}
	}
	
	
	/**
	 * listContestantsEditContestant Class - Contains the Method for Editing Contestants in the Game.
	 *
	 */
	private class listContestantsEditContestant implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			/*Redraw/Update Contestants Table*/
			createContestantsTable("no");

			listContestantsEditPanel.setVisible(false);
			listContestantAddContestantButton.setVisible(false);

			if (contestants.getNumContestants()>0){
				/*Prompts for Unique ID of the Contestant*/
				listPlayerOptionEntry = JOptionPane
						.showInputDialog("Enter the Unique ID of the Contestant which you wish to EDIT:");
				
				/*If Contestant exists*/
				if (listPlayerOptionEntry!=null){
					int index=contestants.existsContestant_byUserID(listPlayerOptionEntry);
					if (index!=-1){
						listContestantsEditPanel.setVisible(false);
						listContestantsEditPanel.setVisible(true);
						listContestantAddContestantButton.setVisible(false);
						listContestantEditContestantButton.setVisible(true);
						contestantElimWeekLabel.setVisible(true);
						contestantElimWeekField.setVisible(true);
						contestantElimWeekField.setEditable(false);
						if(gameSettings.hasGameStarted()){
							contestantFNameField.setEnabled(false);
							contestantLNameField.setEnabled(false);
							photoButton.setVisible(false);
							photoButton.setEnabled(false);
						}
						picLabel.setVisible(true);
						
						/*Get's the Contestant's Data and stores it temporarily*/
						Contestant tmpCntst=contestants.getContestantint(index);
						
						/*Populates the respective TextFields with the Contestant's Data*/
						contestantFNameField.setText(tmpCntst.getFName());
						contestantLNameField.setText(tmpCntst.getLName());
						contestantUIDTextArea.setText(tmpCntst.getID());
						contestantUIDTextArea.setEditable(false);
						contestantTeamField.setText(tmpCntst.getTribe());		
						ImageIcon ii=new ImageIcon(tmpCntst.getPicturePath()); 

						/*Ensures that we have set the variable contestantPicFile to add in
						 * the Contestant Constructor*/
						File tmpFile=new File(tmpCntst.getPicturePath());
						if (tmpFile.exists()){
							contestantPicFile=tmpFile;
						} else{
							contestantPicFile=new File("");
						}
						
						/*Set's the Image of the Contestant*/
						Image image= ii.getImage().getScaledInstance(150, 90, Image.SCALE_SMOOTH);
			            picLabel.setIcon(new ImageIcon(image));
			            picLabel.setVisible(true);
						
			            /*Stores Picture Path*/
						contestantElimWeekField.setText(Integer.toString(tmpCntst.getRoundEliminated()));
					}
					else if (listPlayerOptionEntry.isEmpty()==true){
						/*Show Error Popup*/
						JOptionPane.showMessageDialog(null, 
								"You must enter a valid user id", "Error: Invalid UserID", JOptionPane.ERROR_MESSAGE);
					}
					else{
						/*Show Error Popup*/
						JOptionPane.showMessageDialog(null, 
								"No User with that UserID exists...", "Error: Invalid UserID", JOptionPane.ERROR_MESSAGE);
					}
				}
				/*Somehow have to show the photo of the contestant that the admin picks*/
			}else{
				/*Show Error Popup*/
				JOptionPane.showMessageDialog(null, "There are no Contestants to Edit!", "Error: 0 players", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * listContestantsDeleteContestant Class - Contains Method for Deleting Contestants.
	 *
	 */
	private class listContestantsDeleteContestant implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			gameSettings.update();
			/*Redraw/Update Table*/
			createContestantsTable("no");

			/*Initially have respective Components invisible*/
			listContestantsEditPanel.setVisible(false);
			listContestantAddContestantButton.setVisible(false);
			listContestantEditContestantButton.setVisible(false);

			/*If there are more than 0 contestants*/
			if (contestants.getNumContestants()>0){
				/*Dialog Box requesting Contestant Unique ID*/
				listPlayerOptionEntry = JOptionPane
						.showInputDialog("Enter the Unique ID of the Contestant which you wish to DELETE:");
				
				/*If there exists Contestants*/
				if (listPlayerOptionEntry!=null){
					int index=contestants.existsContestant_byUserID(listPlayerOptionEntry);
					if (index!=-1){
						/*Delete Contestant and display confirmation popup*/
						contestants.deleteContestant_byUserID(listPlayerOptionEntry);
						JOptionPane.showMessageDialog(null, "Succesfully Deleted "+listPlayerOptionEntry
								, "Player Deleted!", JOptionPane.DEFAULT_OPTION);
						
						/*Redraw/Update Table*/
						createContestantsTable("no");
					}
					else{
						/*Display Error Popup*/
						JOptionPane.showMessageDialog(null, "No User with that UserID exists.. ", 
												"Error: Invalid User ID", JOptionPane.ERROR_MESSAGE);

					}
					/*Delete Contestant and Update Text File*/
					contestants.deleteContestant_byUserID(listPlayerOptionEntry);

					contestants.toFile(gameSettings.filename_contestants);
					createContestantsTable("no");

					/*Update GameSettings*/
					gameSettings.setNumContestants(contestants.getNumContestants());
					gameSettings.toFile(gameSettings.filename_settings);
				}
			}else{
				/*Display Error Popup*/
				JOptionPane.showMessageDialog(null, 
						"There are no Contestants to delete!", "Error: 0 players", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	/**
	 * ChoosePhoto Class - Contains action used to display the file chooser.
	 * @author monster
	 *
	 */
	
	private class ChoosePhoto implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			
			gameSettings.update();
			
		
			File tmpfile=contestantPicFile;
			
			if (gameSettings.hasGameStarted()==true){
				JOptionPane.showMessageDialog(null,"Can't update picture anymore!");
				disablePhotoButton();
				return;
			}
			
		
			
			int state = photoChooser.showOpenDialog(null);
			contestantPicFile = photoChooser.getSelectedFile();
			if(contestantPicFile != null && state == JFileChooser.APPROVE_OPTION) 
			{
				contestantPic = null;
				try {
					contestantPic = ImageIO.read(contestantPicFile.getAbsoluteFile());
				} catch (IOException e1) {
					//File cannot be read
					JOptionPane.showMessageDialog(null,"File cannot be read, try again");
				}
				
				/*Creating Image Icon to store and display resized Image*/
				ImageIcon ii=new ImageIcon(contestantPic);  
	            Image image= ii.getImage().getScaledInstance(150, 90, Image.SCALE_SMOOTH);
				
				picLabel.setIcon(new ImageIcon(image));
				picLabel.setVisible(true);
				listContestantsEditPanel.repaint();
	
			}
		}
	}
	
	/**
	 * CheckValidAdd Method - Contains action used to add a Contestant.
	 *
	 */
	private class CheckValidAdd implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			gameSettings.update();
			

			if(gameSettings.hasGameEnded()){		
				disableButtons();
				JOptionPane.showMessageDialog(null, "Game Has Ended, you can't make any more changes! ");
				return ;
			}
			
			
			if(gameSettings.getNumContestants()>=gameSettings.contesants_max){
				JOptionPane.showMessageDialog(null, "Cannot Add More Contestants!");
			}
			
			//(because we start from 0)
			if (contestants.getNumContestants()<=GameSettings.contesants_max-1){
				
				/*Field Limitation Checks*/
				if(	contestantFNameField.getText().length()>20 || 
							!checkAlphaNumeric(contestantFNameField.getText())) {
					JOptionPane.showMessageDialog(null, "First name must be 1-20 alphanumeric " +
							"lowercase characters");
				} else if(contestantLNameField.getText().length()>20 ||
						!checkAlphaNumeric(contestantLNameField.getText())) {
					JOptionPane.showMessageDialog(null, "Last name must be 1-20 alphanumeric " + 
							"lowercase characters");
				} else if(contestantTeamField.getText().length()>30 || 
						!checkAlphaNumeric(contestantTeamField.getText())) {
					JOptionPane.showMessageDialog(null, "Tribe name must be 1-30 alphanumeric " +
							"lowercase characters");
				}
				else {
					//added just to make sure we never get a weird nul pointer exception
					//that was caused by panel visibilty
					if (contestantPicFile==null){
						contestantPicFile=new File(" ");
					}
					
					Contestant tmpCntst=new Contestant( contestantFNameField.getText(),
							contestantLNameField.getText(),
							"",
							contestantTeamField.getText(),
							contestantPicFile.getAbsolutePath(),//<------NEED THE REAL PICTURE PATH!!@$!$!@$
							-1
							);
					contestants.addContestant(tmpCntst,"y");
					JOptionPane.showMessageDialog(null, "Succesfully added.  The UserID is: "
							+contestants.getContestantint(contestants.getNumContestants()-1).getID()
							, "Contestant Added!", JOptionPane.DEFAULT_OPTION);
					
					/*Update the Contestants Table and Text File*/
					contestants.toFile(gameSettings.filename_contestants);
					createContestantsTable("no");
					

					/*Update GameSettings (and File)*/
					gameSettings.setNumContestants(contestants.getNumContestants());
					gameSettings.toFile(gameSettings.filename_settings);
				}
			} 
			else{
				listContestantEditContestantButton.setVisible(false);
				listContestantsEditPanel.setVisible(false);
				picLabel.setVisible(false);
				listContestantAddContestantButton.setVisible(false);
			}
		}
	}

	/**
	 * CheckValidEdit Method - Contains action used to Edit a Contestant.
	 *
	 */
		private class CheckValidEdit implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				
				gameSettings.update();
				
				
		
				if(gameSettings.hasGameEnded()){		
					disableButtons();
					JOptionPane.showMessageDialog(null, "Game Has Ended, you can't make any more changes! ");
					return ;
				}
				
				

				/*Field Limitation Checks*/
				if(	contestantFNameField.getText().length()>20 || 
							!checkAlphaNumeric(contestantFNameField.getText())) {
					JOptionPane.showMessageDialog(null, "First name must be 1-20 alphanumeric " +
							"lowercase characters");
				} else if(contestantLNameField.getText().length()>20 ||
						!checkAlphaNumeric(contestantLNameField.getText())) {
					JOptionPane.showMessageDialog(null, "Last name must be 1-20 alphanumeric " + 
							"lowercase characters");
				} else if(contestantTeamField.getText().length()>30 || 
						!checkAlphaNumeric(contestantTeamField.getText())) {
					JOptionPane.showMessageDialog(null, "Tribe name must be 1-30 alphanumeric " +
							"lowercase characters");
				} else if(!checkNumber(contestantElimWeekField.getText())) {
					JOptionPane.showMessageDialog(null, "Elimination week must only be a number");
				}
				else{
					//System.out.println(contestantPicFile.getAbsolutePath());
					/*Get Contestant Data and Populate Text Fields*/
					Contestant tmpCntst=new Contestant( contestantFNameField.getText(),
							contestantLNameField.getText(),
							contestantUIDTextArea.getText(),
							contestantTeamField.getText(),
							contestantPicFile.getAbsolutePath(),
							-1
							);
						
					/*Delete Contestant, Add Contestant, write to Text File, Display Confirmation Popup
					 * and Update Table*/
					contestants.deleteContestant_byUserID(contestantUIDTextArea.getText());
					contestants.addContestant(tmpCntst,"n");
					contestants.toFile(gameSettings.filename_contestants);
					JOptionPane.showMessageDialog(null, "Succesfully Edited "+contestantUIDTextArea.getText()
							, "Contestant Edited!", JOptionPane.DEFAULT_OPTION);
					
					/*Update GameSettings (and File)*/
					gameSettings.setNumContestants(contestants.getNumContestants());
					gameSettings.toFile(gameSettings.filename_settings);
					createContestantsTable("no");
					
					
				}
			}
		}
	/*End Action Methods*/
	
		
	/**
	 * checkAlphaNumeric Method - Private method to check for alphanumeric input.
	 * @param s
	 * @return m.matches()
	 */
	private boolean checkAlphaNumeric(String s)
	{
		Matcher m = ALPHANUMERIC.matcher(s);
		return m.matches();
	}
	
	/**
	 * checkNumber method - Method for checking for the input of numbers only.
	 * @param s
	 * @return m.matches()
	 */
	private boolean checkNumber(String s)
	{
		Matcher m = NUMBER.matcher(s);
		return m.matches();
	}
	
	
	/**
	 * paintComponent Method - Overidden paint component method.
	 * @param g
	 */
    public void paintComponent(Graphics g) {
	      g.drawImage(contestantPic, 0, 0, null);

	}
    
	
    
    /**
     * ContestantsPanel Constructor.
     */
	public ContestantsPanel() {
		/*Create GameSettings Object*/
		gameSettings= new GameSettings();
		
		/*Custom Color 'custom'*/
		Color custom = new Color(225,225,225);
		
		/*List Contestants Content Pane Code*/
		listContestantsPanel = new TransparentPanel();
		listContestantsPanel.setLayout(null);
		listContestantsPanel.setBounds(220, 60, 830, 470);
		listContestantsPanel.setBackground(custom);
		listContestantsPanel.setVisible(false);
		
		/*Redraw/Update Contestants Table*/
		createContestantsTable("y");
		
		/*Add Contestant Button - Creation, Layout, Action Listener*/
		listContestantsAddButton = new JButton("Add Contestant");
		if ((gameSettings.hasGameStarted()==false)){
			listContestantsAddButton.addActionListener(new AddContestant());
		} else{
			listContestantsAddButton.setEnabled(false);
		}
		listContestantsPanel.add(listContestantsAddButton);
		listContestantsAddButton.setBounds(620, 10, 200, 20);
		
		/*Edit Contestant Button - Creation, Layout, Action Listener*/
		listContestantsEditButton = new JButton("Edit Contestant");
		listContestantsEditButton.addActionListener(new listContestantsEditContestant());
		listContestantsPanel.add(listContestantsEditButton);
		listContestantsEditButton.setBounds(620, 50, 200, 20);
		
		/*Delete Contestant Button - Creation, Layout, Action Listener*/
		listContestantsDeleteButton = new JButton("Delete Contestant");
		if ((gameSettings.hasGameStarted()==false)){		
			listContestantsDeleteButton.addActionListener(new listContestantsDeleteContestant());

		} else{
			listContestantsDeleteButton.setEnabled(false);
		}
		listContestantsPanel.add(listContestantsDeleteButton);
		listContestantsDeleteButton.setBounds(620, 90, 200, 20);
		
		
		/*refresh table*/
		refreshTable=new JButton("Refresh Data");
		listContestantsPanel.add(refreshTable);
		refreshTable.setBounds(620, 130, 200, 20);
		refreshTable.addActionListener(new refreshTable());
				
		/*Custom Panel Layout Code*/
		/*Contestants Edit Panel - Creation, Layout*/
		listContestantsEditPanel = new JPanel();
		listContestantsEditPanel.setLayout(null);
		listContestantsPanel.add(listContestantsEditPanel);
		listContestantsEditPanel.setBounds(10, 240, 810, 220);
		listContestantsEditPanel.setBackground(Color.WHITE);
		listContestantsEditPanel.setVisible(false);

		/*Create New File Chooser*/
		photoButton = new JButton("Upload Photo");
		photoButton.addActionListener(new ChoosePhoto());
		photoChooser = new JFileChooser("Choose a contestant photo to upload");
		photoChooser.setFileFilter(new PhotoFilter());
		photoChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		photoChooser.setAcceptAllFileFilterUsed(false);
		photoButton.setBounds(10, 120, 220, 40);
			
		
		/*Photo Button - Creation, Layout*/
		listContestantsEditPanel.add(photoButton);
		picLabel = new JLabel("");
		listContestantsEditPanel.add(picLabel);
		picLabel.setBounds(240, 120, 150, 90);
		
		/*Contestant User ID Label & Text Field Code - Creation, Layout*/
		contestantUIDLabel = new JLabel("Contestant Unique ID:");
		listContestantsEditPanel.add(contestantUIDLabel);
		contestantUIDLabel.setBounds(10, 10, 200, 20);
		contestantUIDTextArea = new RoundJTextField(10);
		contestantUIDTextArea.setEditable(false);
		listContestantsEditPanel.add(contestantUIDTextArea);
		contestantUIDTextArea.setBounds(10, 30, 200, 20);
		
		/*First Name Label & Text Field Code - Creation, Layout*/
		contestantFNameLabel = new JLabel("First Name:");			
		listContestantsEditPanel.add(contestantFNameLabel);
		contestantFNameLabel.setBounds(260, 10, 200, 20);
		contestantFNameField = new RoundJTextField(10);
		listContestantsEditPanel.add(contestantFNameField);
		contestantFNameField.setBounds(260, 30, 200, 20);
		
		/*Last Name Label & Text Field Code - Creation, Layout*/
		contestantLNameLabel = new JLabel("Last Name:");
		listContestantsEditPanel.add(contestantLNameLabel);
		contestantLNameLabel.setBounds(520, 10, 200, 20);
		contestantLNameField = new RoundJTextField(10);
		listContestantsEditPanel.add(contestantLNameField);
		contestantLNameField.setBounds(520, 30, 200, 20);
		
		/*Team Label & Text Field Code - Creation, Layout*/
		contestantTeamLabel = new JLabel("Team:");
		listContestantsEditPanel.add(contestantTeamLabel);
		contestantTeamLabel.setBounds(10, 60, 200, 20);
		contestantTeamField = new RoundJTextField(10);
		listContestantsEditPanel.add(contestantTeamField);
		contestantTeamField.setBounds(10, 80, 200, 20);
		
		/*Elimination Week Label & Text Field Code - Creation, Layout*/
		contestantElimWeekLabel = new JLabel("Elimination Week:");
		listContestantsEditPanel.add(contestantElimWeekLabel);
		contestantElimWeekLabel.setBounds(260, 60, 200, 20);
		contestantElimWeekField = new RoundJTextField(10);
		listContestantsEditPanel.add(contestantElimWeekField);
		contestantElimWeekField.setBounds(260, 80, 200, 20);
		contestantElimWeekLabel.setVisible(false);
		contestantElimWeekField.setVisible(false);
				
		
		
		/*Add Contestant (Edit Panel) Label & Text Field Code - Creation, Layout, Action Listener*/
		
		listContestantAddContestantButton = new JButton("Add Contestant to Pool");
		listContestantsEditPanel.add(listContestantAddContestantButton);
		listContestantAddContestantButton.setBounds(600, 190, 200, 20);
		listContestantAddContestantButton.addActionListener(new CheckValidAdd());
		
		/*Edit Contestant (Edit Panel) Label & Text Field Code - Creation, Layout, Action Listener*/
		listContestantEditContestantButton = new JButton("Commit Changes");
		listContestantsEditPanel.add(listContestantEditContestantButton);
		listContestantEditContestantButton.setBounds(600, 190, 200, 20);
		listContestantEditContestantButton.addActionListener(new CheckValidEdit());	
		
		
		if (gameSettings.hasGameEnded()==true){
			
			disableButtons();
		}

	}
	
	/**
	 * getPanel Method - Returns listContestantsPanel JPanel
	 * @return listContestantsPanel
	 */
	public JPanel getPanel() {
		return listContestantsPanel;
	}
	
	/**
	 * getEditPanel Method - Returns listContestantsEditPanel JPanel
	 * @return listContestantsEditPanel
	 */
	public JPanel getEditPanel() {
		return listContestantsEditPanel;
	}
	
	/**
	 * getAddButton Method - Returns listContestantsAddButton JButton
	 * @return listContestantsAddButton
	 */
	public JButton getAddButton(){
		return listContestantsAddButton;
	}
	
	/**
	 * getDeleteButton Method - Returns listContestantsDeleteButton JButton
	 * @return listContestantsDeleteButton
	 */
	public JButton getDeleteButton(){
		return listContestantsDeleteButton;
	}
	
	/**
	 * getFirstName Method - Returns contestantFNameField JTextField
	 * @return contestantFNameField
	 */
	public JTextField getFirstName(){
		return contestantFNameField;
	}
	
	/**
	 * getLastName Method - Returns contestantLNameField JTextField
	 * @return contestantLNameField
	 */
	public JTextField getLastName(){
		return contestantLNameField;
	}
	
	/**
	 * getElimWeek Method - Returns getElimWeek JTextField
	 * @return contestantElimWeekField
	 */
	public JTextField getElimWeek(){
		return contestantElimWeekField;
	}
	
	/**
	 * getPhotoButton Method - Returns photoButton JButton
	 * @return photoButton
	 */
	public JButton getPhotoButton(){
		return photoButton;
	}
	
	/**
	 * disableButtons Method - Disables the Functionality Buttons.
	 */
	public void disableButtons(){
		listContestantsAddButton.setEnabled(false);
		listContestantsEditButton.setEnabled(false);
		listContestantsDeleteButton.setEnabled(false);
		listContestantAddContestantButton.setEnabled(false);
		photoButton.setEnabled(false);
		listContestantsEditPanel.setVisible(false);
	}
	
	public void disablePhotoButton(){
		photoButton.setVisible(false);
		photoButton.setEnabled(false);

	}
}
