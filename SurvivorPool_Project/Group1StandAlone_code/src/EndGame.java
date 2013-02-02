
public class EndGame {
	private GameSettings gs;
	
	EndGame(){
		gs=new GameSettings();
	}
	
	
	public boolean commitGame(){
		gs.update();
		
		if ((gs.getNumWeeks()==gs.getCurrentRound())&&(gs.hasGameStarted()==true)){
			gs.setGameEnded(1);
			gs.toFile(gs.filename_settings);
			return true;
			
			
		} else{
			return false;
		}
		
		
	}
	
	
	
}
