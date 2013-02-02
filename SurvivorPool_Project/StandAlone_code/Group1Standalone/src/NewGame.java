import java.io.*;

public class NewGame {
	private GameSettings gs;
	
	
	NewGame(){
		gs=new GameSettings();
		

	}
	
	
	
	
	//-----------------------------------------------------
	//Restarting Sequence
	//-----------------------------------------------------
	public void createNewGame(){
		prepPlayersFile();
		prepContestantsFile();
		prepPlayerQuestionAnswersFile();
		prepPlayerWeeklyEliminations();
		prepQuestionsFile();
		prepInternalQuestions();
		prepPlayerUltimateEliminations();

		prepGameSettingsFile();
	}
	
	

	
	//-----------------------------------------------------
	//Prepare TextFiles
	//-----------------------------------------------------
	public void prepGameSettingsFile(){
		gs.update();
		
		File file=new File(gs.ultimateWinner);
		String st="";
		
		
		if (clearFile(file, st)){
			gs.setBetAmount(-1);
			gs.setNumContestants(0);
			gs.setNumPlayers(0);
			gs.setNumWeeks(0);
			gs.setUlimateWinner("notYETset");
			gs.setGameEnded(0);
			gs.setCurrentRound(2);

			gs.toFile(gs.filename_settings);
			}
		else{
			//there was an error clearing the players file

		}
	}

	
	public void prepPlayersFile(){
		
		File file=new File(gs.filename_players);
		String st="";
		
		if(clearFile(file, st)){
		    gs.setNumPlayers(0);
		    gs.toFile(gs.filename_settings);

		}else{
			//there was an error clearing the players file
		}

		
	}
	
	
	public void prepContestantsFile(){
		File file=new File(gs.filename_contestants);
		String st="";
		
		if(clearFile(file, st)){
		    gs.setNumContestants(0);
		    gs.setNumWeeks(0);
		    gs.toFile(gs.filename_settings);
		}else{
			//there was an error clearing the players file
		}

	}
		
	
	public void prepPlayerQuestionAnswersFile(){
		File file=new File(gs.filename_qAnswers);
		String st="";
		
		if(clearFile(file, st)){
		}else{
			//there was an error clearing the players file
		}
	}
	
	public void prepPlayerWeeklyEliminations(){
		File file=new File(gs.filename_eliminations);
		String st="";
		
		if(clearFile(file, st)){
		}else{
			//there was an error clearing the players file
		}
	}
	
	public void prepQuestionsFile(){
		File file=new File(gs.filename_Qs);
		String st="NUMROUNDS-------3\r\nNUMQS-------0\r\n";
		
		//we set it to 3 rounds because until game starts...we don't know numrounds!
		if(clearFile(file, st)){
		}else{
			//there was an error clearing the players file
		}
	}

	
	public void prepInternalQuestions(){
		Questions Q=new Questions();
		//Q.toFile_questionSettings("newgame");
		Q.toFile_questionSettings("newgame");//set 3 rounds make it 0
	}
	

	public void prepPlayerUltimateEliminations(){
		File file=new File(gs.filename_ultWinner);
		String st="";
		
		if(clearFile(file, st)){
		}else{
			//there was an error clearing the players file
		}
	}
	
	//-----------------------------------------------------
	//FilesExist
	//-----------------------------------------------------
	public boolean allFilesExists(){
		
		int numfiles=8;
		 File[] file=new File[numfiles];
		file[0]=new File(gs.filename_contestants);
		file[1]=new File(gs.filename_eliminations);
		file[2]=new File(gs.filename_players);
		file[3]=new File(gs.filename_qAnswers);
		file[4]=new File(gs.filename_settings);
		file[5]=new File(gs.filename_Qs);
		file[6]=new File(gs.filename_QuestionsInternalSettings);
		file[7]=new File(gs.filename_ultWinner);
		
		for (int i=0; i<numfiles; i++){
			if(file[i].exists()==false){
				return false;
			}	
		}
		
		return true;
	}
	
	
	//-----------------------------------------------------
	//Helper
	//-----------------------------------------------------
	//http://www.roseindia.net/tutorial/java/core/files/fileoverwrite.html
	private boolean clearFile(File file, String st){
		 if (file.exists()) {
             try {
                FileOutputStream fos = new FileOutputStream(file, false);
				fos.write(st.getBytes());
			    fos.close();
			    return true;
			   
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
         

		 } else{
			 return false;
		 }
	}
	
	/*
	public static void main(String[] args) {
		NewGame ng= new NewGame();
		ng.createNewGame();
	}
	*/
}
