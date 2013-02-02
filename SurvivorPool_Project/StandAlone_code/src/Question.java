
/**
 * Singular Question
 * @author m3t4l
 *
 */
public class Question {
	private String	qID;			//question Id number
									//round.question#
	private String question;	//actual question
	private String correctAnswer;	//correct answer
	private int	qtype;			//SA=0 or MC=1
	
	private String[] AnswerOptions;//	//if MC - holds all options and answer
								   // if SA...null
	
	private String roundNumber;
	private String questionNumber;
	
	
	//-----------------------------------------------------
	//Constructor
	//-----------------------------------------------------
	Question(){
		
	}
	
	Question(String ID,String qstn,
			String correct, String[] Options){


		setID(ID);
		setQ(qstn);
		
		setAnswers(correct, Options);
		
	}
	
	
	//-----------------------------------------------------
	//Setters
	//-----------------------------------------------------
	public void setID(String ID){
		qID=ID;
	}
	
	public void setRoundNumber(String ID){
		roundNumber=ID;
	}
	
	public void setQuestionNumber(String ID){
		questionNumber=ID.valueOf(qID.charAt(3));
	}
	
	public void setQ(String qstn){
		question=qstn;
	}
	
	public void setType(int tmp_type){
		qtype=tmp_type;
	}
	
	
	public boolean setAnswers(String[] correctANDoptions){
		if (correctANDoptions.length==0){
			return false;
		} else{
			
			if (correctANDoptions.length>1){
				String[] options=new String[correctANDoptions.length-1];

				for(int i=0; i<correctANDoptions.length-1;i++){
					options[i]=correctANDoptions[i+1];
				}
				setAnswers(correctANDoptions[0],options);
				
			}else{
				setAnswers(correctANDoptions[0],null);
			}
			return true;
		}
		
	}
	
	public void setAnswers(String correct, String[] Options){
		correctAnswer=correct;
		
		
		//Qusetion is MC or SA
		if (Options==null){
			setType(0);
			AnswerOptions=null;
		} else{
			setType(1);
			
			if (Options.length >4){
				System.out.println("ERROR:Only 5 answers max. (4 + correct answer)");
				return;
			}else{
				AnswerOptions= new String[Options.length];
				AnswerOptions[0]=correct;	
				
				for (int i=0; i< Options.length; i++){
					AnswerOptions[i]=Options[i];
				}
				
				
			}
			
			
		}
		
		
	}

	
	
	
	//-----------------------------------------------------
	//Getters
	//-----------------------------------------------------
	public String getID(){
		return qID;
	}
	
	public String getRoundNumber(){
		return (String) roundNumber;
	}
	
	public String getQuestionNumber(){
		return questionNumber;
	}
	
	public String getQuestion(){
		return question;
	}
	
	public String getCorrectAnswer(){
		return correctAnswer;
	}
	
	public int getQType(){
		return qtype;
	}
	
	
	//return all answers (include correct if MC)
	public String[] getAnswers(){
		String[] tmp;
		if (qtype==0){
			tmp=new String[1];
			tmp[0]=correctAnswer;
	
			
		} else{
			tmp=new String[1+AnswerOptions.length];
			
			
			tmp[0]=correctAnswer;
			for (int i=1; i<tmp.length; i++){
				tmp[i]=AnswerOptions[i];
			}
			
		
			
		}
		
		return tmp;
	}

	
	public String[] getOptions(){
		
		String[] tmp;
		if (qtype==1){
			tmp=new String[AnswerOptions.length];
			for (int i=0; i<tmp.length; i++){
				tmp[i]=AnswerOptions[i];
			}
			return tmp;
		}
		
		//there aren't any options...
		return null;
	}
	
	public int getNumOptions(){
		
		if (qtype==1){
			return AnswerOptions.length;
			
		}
		//there aren't any options...
		return 0;
	}
	
	public int getNumAnswers(){
		int tmp;
		if (qtype==0){
			tmp=1;			
		} else{
			tmp=1+AnswerOptions.length;
		}
		return tmp;	
	}
	
	//-----------------------------------------------------
	//Output
	//-----------------------------------------------------
	
	/**
	 * @return object representation of question
	 * id--fname--lname-tribe-picture_path
	 */
	public Object[] prepforTable(){
		
		
		if (getOptions()==null){
			Object[] QuestionData={getID(), getQuestion(), getQType(), getCorrectAnswer(),"----->"};
			return QuestionData;

		} else{
			Object[] QuestionData={getID(), getQuestion(), getQType(), getCorrectAnswer(), getOptions()};
			return QuestionData;

		}
		
	}
	
	
	/**
	 * qID--Question--type
	 * @return tmpString
	 */
	public String toString(){
		String delim="--";
		return qID+delim+qtype+delim+question;
	}
	
	
	
}
