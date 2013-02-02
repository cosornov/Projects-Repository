/**
 * Group of Questions
 * @author m3t4l
 *
 */

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Questions {
	private ArrayList<Question>[]  bonusQs;
	private int num_rounds;
	private int num_questions;
	public int[] roundMaxKeys;
	private GameSettings gs;
	
	
	
	//for I/O
	private static String[] identifiers={"QID","TYPE",
										"QUESTION",
										"CORRECT",
										"OPTION",
										"NUMROUNDS",
										"NUMQS"};
	
	private static String read_split_on="-------";
	private static int objectsize=4;//5 but we dont' want options
	//-----------------------------------------------------
	//Constructor
	//-----------------------------------------------------
	Questions(){
		gs=new GameSettings();
	}
	
	
	//http://www.dreamincode.net/forums/topic/123762-array-of-arraylist%26lt%3Bstring%26gt%3B/
	Questions(int num_rounds){
		gs=new GameSettings();
		this.num_rounds=num_rounds;
		bonusQs =(ArrayList<Question>[])new ArrayList[num_rounds];
		roundMaxKeys=new int[num_rounds];
		
		
		for (int i=0; i<num_rounds; i++){
			bonusQs[i]=new  ArrayList<Question>();
			
			roundMaxKeys[i]= 0;
		}
	
		setNumQuestions(0);

		
	}

	
	//-----------------------------------------------------
	//Helpers
	//-----------------------------------------------------
	
	/**
	 * return integer of the round...
	 * 1.2 --> 1
	 */
	public int roundFromID(String ID){
		String[] tmp=ID.split("\\.");
		return Integer.parseInt(tmp[0]);
	}
	
	
	/**
	 * Is "round" between [1, num_rounds]
	 * @param round
	 * @param num_rounds
	 * @return true or false
	 */
	public boolean roundInBounds(int round){
		
		if ((round>0)&&(round<=num_rounds)){
			return true;
		}
				
		return false;
	}
	
		
	/**
	 * Determines ID for the Question
	 * Round+"."+Question# --> into a string representation.
	 * @param round
	 * @return
	 */
	public String createQID (int round){
		
		if (roundInBounds(round)){
			
			roundMaxKeys[round-1]=roundMaxKeys[round-1]+1; //increment max ID in this round


			
			String tmp=Integer.toString(round)+"."+
						Integer.toString(roundMaxKeys[round-1]);
			
			return tmp;
			
		}
		return null;
		
	}
	
	
	/**
	 * Get index from string
	 * index[0] ==> round_index
	 * index[1] ==> question_index (withoutround)
	 * null if not found
	 * {-1, -1}...if error with round
	 * @param iD
	 * @return index
	 */
	public int[] getIndex(String iD){

		String[] tmp=iD.split("\\."); //have to do this to split on decimal...
									//http://www.velocityreviews.com/forums/t135234-string-split-at-a-decimal-point.html
		int[] index=new int[2];
		
		int tmpRound=Integer.parseInt(tmp[0]);
		
		if (roundInBounds(tmpRound)){
			Question tmpQ=new Question();
			int roundSize=bonusQs[tmpRound-1].size();
			index[0]=tmpRound-1;
			
			for (int i=0; i<roundSize;i++){
				tmpQ=bonusQs[tmpRound-1].get(i);
				
				if (tmpQ.getID().equals(iD)){
					index[1]=i;
					return index;
				}
			}
			
			return null; //no such question existed...
		}
		
		index[0]=-1;
		index[1]=-1;
		return null;//round is out of bounds

	}

	
	/**
	 * Given a Question, and a round...does the Question.question exist?
	 * (returns null if it does not)
	 * (returns index[0]=round, index[1]=index if it does)
	 * if round=-1....check all questions in all rounds
	 * ...otherwise only check specified round
	 * @param Q
	 * @param round
	 * @return
	 */
	public int[] getIndex_question(Question Q, int round){
		int b_round;
		int e_round;
		int[] index= new int[2];
		
		//setting seach parameters of outer for loop
		if (round==-1){
			b_round=0;
			e_round=num_rounds-1;
		} else{
			b_round=round-1;
			e_round=round-1;
		}
		
		
		//is the round valid?
		if(roundInBounds(round)){
			int roundSize;
			Question tmpQ= new Question();
			
			
			//loop through specified rounds
			for (int i=b_round; i<=e_round;i++){
				roundSize=bonusQs[round-1].size();
				
				//System.out.println("i:"+i+" r:"+round+" rs:"+roundSize);
				//loop through round
				for (int j=0; j<roundSize; j++){
					tmpQ=bonusQs[round-1].get(j);
					
					//do the questionstrings match?...if so then retun where they do
					if (tmpQ.getQuestion().equals(Q.getQuestion())){
						index[0]=i;
						index[1]=j;
						
						//System.out.println("i[0]:"+index[0]+" i[1]:"+index[1]);
						/*System.out.println("This Question already exists in R:"+round+ " " 
								+ Q.getQuestion());*/
						return index; //return where the qusetion already exists
					}
				}
			}
			
			return null; // it doesn't exist at all!
		}
		
		
		// sett "error" values
		index[0]=-1;
		index[1]=-1;
		return index; //nope...exit
		
	}
	//-----------------------------------------------------
	//Setters
	//-----------------------------------------------------
	
	/**
	 * number of questions is...
	 */
	public void setNumQuestions(int num){
		num_questions=num;
	}
	
	
	/**
	 * Add question
	 */
	public boolean addQuestion(Question Q, int round){
		
		int orground=round;
		
		int[] index=new int[2];
		
		if (round==-1){
			round=roundFromID(Q.getID());
			
		}		
		if(roundInBounds(round)){
		
			index=getIndex_question( Q, round);
			if (index==null){
				
				if (orground == -1) {
					//just use the ID already in Z

				} else{
					Q.setID(createQID(round));
				}
				
				bonusQs[round-1].add(Q);
				num_questions=num_questions+1;
				
				return true;
			} 
			else if (index[0]==-1){
				//there was an issue with the round (out of bounds)
				return false;
			} 
			else{
				
				//the question already exists!)
				return false;
			}
			
			
			
			
		}
		
		return false;
	}
	
	
	/**
	 * Delete a question based on its qID
	 * @param iD
	 * @return
	 */
	public boolean deleteQuestion(String iD){
		int[] index=getIndex(iD);
		 if (index==null){
			 //question did not exist
			 return false;
		 } 
		 else if (index[0]==-1){
			 //rounds were out of bond
			 return false;
		 }
		 else{
			 bonusQs[index[0]].remove(index[1]);
			 num_questions=num_questions-1;
			return true;
		 }
	
	
	}
	
	//-----------------------------------------------------
	//Getters
	//-----------------------------------------------------
	public Question getQuestion(String iD){
		int[] index=getIndex(iD);
		 if (index==null){
			 //question did not exist
			 return null;
		 } 
		 else if (index[0]==-1){
			 //rounds were out of bond
			 return null;
		 }
		 else{
			 // question found
			 return bonusQs[index[0]].get(index[1]);

		 }
	
		
	}
	
	
	/**
	 * number of questions is...
	 */
	public int getNumQuestions(){
		return num_questions;
	}
	
	/**
	 * number of questions is...
	 */
	public int get_numRounds(){
		return num_rounds;
	}
	
	/**
	 * number of questions is...
	 */
	public int get_objSize(){
		return objectsize;
	}
	//-----------------------------------------------------
	//Output
	//-----------------------------------------------------
	
	/**
	 * Output the entire round...
	 * id -- qtype -- question
	 */
	public String toString_round(int round){
		String nl="\n";
		
	
		if (roundInBounds(round)){
			
			int roundSize=bonusQs[round-1].size();
			String output="";
			
			for (int i=0; i<roundSize; i++){
				
				output=output+bonusQs[round-1].get(i).toString()+nl;	
			}		
			
			return output;
		}
		//round out of bounds
		return null;
		
	}
	

	
	
	/**
	 * output entire object
	 */	
	public String toString(){
		String output="";
	
		//_round (takes input of round...hence we start at 1)
		for (int i=1; i<=num_rounds; i++){
			output=output+toString_round(i); 
		}
		
		return output;
	}
	

	
	/**
	 * Write all questions to a file
	 * @param filename
	 */
	public void  toFile(String filename){
		 try{
			  // Create file 
			  FileWriter fstream = new FileWriter(filename);
			  BufferedWriter out = new BufferedWriter(fstream);
			
			  
			  Question tmpQuestion;
			  int roundSize;
			  int numAnswers;
			  String[] answers;

			  
		
			  out.write(identifiers[5]+read_split_on+num_rounds);
			  out.newLine();

			  out.write(identifiers[6]+read_split_on+num_questions);
			  out.newLine();
			  
			  // go through rounds
			  for (int i=0; i<num_rounds; i++){
				  
				  roundSize=bonusQs[i].size();
				  
				  //go through each round
				  for (int j=0; j<roundSize; j++){
					  tmpQuestion=(Question)bonusQs[i].get(j);
					  
					  //ID
					  out.write(identifiers[0]+read_split_on+tmpQuestion.getID());
					  out.newLine();
	
					  //Question
					  out.write(identifiers[2]+read_split_on+tmpQuestion.getQuestion());
					  out.newLine();
					  
					  //Type
					  out.write(identifiers[1]+read_split_on+tmpQuestion.getQType());
					  out.newLine();
					  
					  //Num (for carolina)
					  numAnswers=tmpQuestion.getNumAnswers(); 
					  out.write("NUM"+read_split_on+numAnswers);
					  out.newLine();
					  
					  
					  //CorrectAnswer
					  out.write(identifiers[3]+read_split_on+tmpQuestion.getCorrectAnswer());
					  out.newLine();
					  
					  //Other Answers (if available)
					  numAnswers=tmpQuestion.getNumAnswers(); 
					  if (numAnswers>1){
						
						  //get the other answers (not correct one)
						  answers=new String [numAnswers-1];
						  answers=tmpQuestion.getOptions();
						  
						  //print out other ones...
						  for (int k=0; k<numAnswers-1; k++){
							  out.write(identifiers[4]+read_split_on+answers[k]);
							  out.newLine();
						  }		 
						  				
					  }//end other answers
				
			
				  }//end current rond
				  
			  }//end rounds
			  //Close the output stream
			  out.close();
			  
			  ////////////////////JUSTADDED
			  toFile_questionSettings("ingame");

			  
		 }
		 catch (Exception e){//Catch exception if any
			 
			 System.out.println("FROM THE TO FILE METHOD");
			  System.err.println("Error: " + e.getMessage());
		 }
	}
	
	
	
	
	
	
	/**
	 * Write some internal settings of Questions to file
	 */
	public void toFile_questionSettings(String mode){
		GameSettings gs=new GameSettings();
	
		 try{
			  // Create file 
			  FileWriter fstream = new FileWriter(gs.filename_QuestionsInternalSettings);
			  BufferedWriter out = new BufferedWriter(fstream);
			  
			  String tmp="";
			  int minRounds=3;
			  
			  
			  if (mode.equals("ingame")){
				  for (int i=0; i< num_rounds; i++){
					  
					  
					  if (i!=num_rounds-1){
						  tmp=tmp+roundMaxKeys[i]+"--";
					  } else{
						  tmp=tmp+roundMaxKeys[i];
					  }
	
				  }
			  } 
			  
			  //to prepare the file 
			  else{
				  //if (mode.equals("newgame")){
				  for (int i=0; i< minRounds; i++){
					  
					  
					  if (i!=minRounds-1){
						  tmp=tmp+"0"+"--";
					  } else{
						  tmp=tmp+"0";
					  }
	
				  }
			  } 
			  
	
			  out.write(tmp);

			  
			  out.close();
		 }	 catch (Exception e){//Catch exception if any
			 System.out.println("FROM THE INTERNAL FILE");
			  System.err.println("Error: " + e.getMessage());
		
		 }
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//--------------------------------------------------------
	// Read from file
	//http://www.roseindia.net/java/beginners/java-read-file-line-by-line.shtml
	//--------------------------------------------------------
	
	/**
	 * get Questions object from a file
	 * and then reset "THIS" object to contents of textfile
	 * @param filename
	 */
	
	public Questions getFromFile(String filename){
		 try{
			 // Open the file that is the first 
			  // command line parameter
			  FileInputStream fstream = new FileInputStream(filename);
			  
			  // Get the object of DataInputStream
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			 
			  
			  //setup variables that will be needed to readin and transfer			  
			  String strLine;
			  String[] tmp_data;
	
	
			  Questions tmp_Qs;
			  Question tmp_Q;
			  boolean set=false;
			  boolean finished=false;
			  int count=0;
			  int tmp_count=0;
			  
			  String tmp_correct_answer;
			  ArrayList<String> tmp_options;
			
			  String tmp_transfer;
			  
			  
			  strLine= br.readLine();
			  
			  
			  if (strLine  != null){
				  
				  
				  //Get numquestions
				
				
				  
				
				  
				  //first line (initialize the tmp_Qs
				  strLine.replaceAll("\\n", "");
				  tmp_data=strLine.split(read_split_on);
				  

				  tmp_Qs=new Questions(Integer.parseInt(tmp_data[1]));;
				  count++;
				  set=false;
					
				  //second line ( number of questions)
				  strLine = br.readLine();
				  strLine.replaceAll("\\n", "");
				  tmp_data=strLine.split(read_split_on);
				  tmp_Qs.num_questions=Integer.parseInt(tmp_data[1]);
				  count++;
				 
				 
				  
				  //Fix to Questions
				  strLine = br.readLine();
				  set=true;
				  
				  tmp_Q=new Question();
				  
			  
				  
				 // System.out.println(tmp_Qs.num_questions+"--"+tmp_Qs.num_rounds);
				  
				  
				  //Read Questions
				  while ((strLine!= null)&&(finished==false))   {
					  tmp_options=new ArrayList<String>();
					  

					  //do we need to get the next line?
					  if (set==false){
						  strLine= br.readLine();						 
					  }
					  strLine.replaceAll("\\n", "");
					  tmp_data=strLine.split(read_split_on);
					  
					  
					 // System.out.println("tmpcount"+tmp_count+"  "+strLine);  //<---------printing contents
					 //  System.out.println("tmpcount"+tmp_count+"  "+tmp_data[0]+"  "+identifiers[3]);  //<---------printing contents


					  //System.out.println(identifiers[2]+" --"+strLine);
					  
					  
					  //QID..."New Question"
					  if ((tmp_data[0].equals(identifiers[0]))&&(tmp_count==0)){
						  
						  tmp_count=1;
						  tmp_Q=new Question();
						  tmp_Q.setID(tmp_data[1]);
						  count++;
						  set=true;
						  strLine = br.readLine();
						  //System.out.println("tmp_Q "+tmp_Q.getID());
						
					  } 
			

					  
					  
					  //"Question"
					  else if((tmp_data[0].equals(identifiers[2]))&&(tmp_count==1)) {
						  tmp_count=2;
						  tmp_Q.setQ(tmp_data[1]);
						  count++;
						  set=true;
						  strLine = br.readLine();
						  
							// System.out.println("Q "+tmp_Q.getQuestion());
					  }
					  
					  
					  //"Type"
					  else if((tmp_data[0].equals(identifiers[1]))&&(tmp_count==2)) {
						  
						 
						 
						  tmp_count=3;
						  tmp_Q.setType(Integer.parseInt(tmp_data[1]));
						  count++;
						  set=true;
						  
						  //System.out.println("type " + tmp_Q.getQType());
						  strLine = br.readLine();
						  strLine = br.readLine();
					  }
					  
					  
					  //answers
					  else if(tmp_count==3){
						  
						

						
					
						  tmp_correct_answer=tmp_data[1];
						  
						  
						  // SA
						  if (tmp_Q.getQType()==0){
							 
							  tmp_Q.setAnswers(tmp_correct_answer,null); 
							  tmp_Qs.addQuestion(tmp_Q, -1);// -1 forces it to use the id we've set!!!!!
							  								//super important!@%!@%
							  set=true;
							  count++;
							  strLine = br.readLine();
							  
						  } //end sa
						  
						  
						  
						  //MC
						  else{
							  //get "Options"
							  strLine = br.readLine();
							  
							  while (strLine != null){

								  strLine.replaceAll("\\n", "");
								  tmp_data=strLine.split(read_split_on);
								  count++;

							

								  if (tmp_data[0].equals(identifiers[4])){
									  tmp_options.add(tmp_data[1]);
								  }
								  else{
									  break;
								  }

								  strLine = br.readLine();
								  
														  
							  }					  
							  
							  
							
							  if (strLine !=null){
								  
								  set=true;
								  finished=false;
							  }else{
								  
								  //we're not done and already have next line
								  set=true;	
								  finished=true;
							  }
							  
							  //addqusetion

							  String[] tmp_arraylist_Qs=new String[tmp_options.size()];
							  for (int i=0; i<tmp_options.size(); i++){
								  tmp_arraylist_Qs[i]=(String)tmp_options.get(i);
								  
								
							  }

							  tmp_Q.setAnswers(tmp_correct_answer, 
									  			tmp_arraylist_Qs);	
							  tmp_Qs.addQuestion(tmp_Q, -1);// <keep id

							 
						
							  
						  }//end mc
						  tmp_count=0;
						  
						  //System.out.println(tmp_Q.getCorrectAnswer());
						  
					  }
  
					
				  }
	
					  
					  

				  
				  //Close the input stream
				  in.close();
				  
				  int half=(int)(0.5*tmp_Qs.getNumQuestions());
				  tmp_Qs.setNumQuestions(half);
				  
	
				  tmp_Qs.roundMaxKeys=getFromFile_questionSettings();
		
				  //transfer the relevant variables
				  if (tmp_Qs==null){
					  System.out.println("MAJOR INTERNAL ERROR....DO NOT MESS AROUND WIHT THE FILES!" +
					  		"Expect Weird Question Functionality");
					  return tmp_Qs;
				  } else{
					  return tmp_Qs;
				  }
				  
			  }else{
				  
				  System.out.println("ERROR: This file contains nothign....");
				  return null;
				  
			  }
			  
		 }
		 catch (Exception e){//Catch exception if any
			  System.out.println("Error in the getfromfile method");
			  System.err.println("Error: " + e.getMessage());
			  return null;
		 }
	}
	
	
	

	
	public int[] getFromFile_questionSettings(){
		
		 try{
			 // Open the file that is the first 
			  // command line parameter
			 gs.update();
			  FileInputStream fstream = new FileInputStream(gs.filename_QuestionsInternalSettings);
			  // Get the object of DataInputStream
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			

			  

			  //setup variables that will be needed to readin and transfer			  
			  String strLine;
			  strLine= br.readLine();
			  /*
			  if (strLine==null){
				  //System.out.println(strLine+"--ccccc--");

			  }else{
				  //System.out.println(strLine+"--dddd--"+strLine); 
			  }
			  */
			  
		
			  String[] tmp=strLine.split("--");

			  roundMaxKeys=new int[tmp.length];

			  for (int i=0; i< tmp.length;i++){
				  
				  roundMaxKeys[i]=Integer.parseInt(tmp[i]);
				  //System.out.println(roundMaxKeys[i]);
			
			  }
			  //System.out.println("==================="); 
	
			  //Close the input stream
			in.close();
			return roundMaxKeys;

		 }
		 catch (Exception e){//Catch exception if any
			 System.out.println("Error in fromFile QuestionSettings");
			  System.err.println("Error: " + e.getMessage());

			  return null;
		 }
	}
	/**
	 * @return stacked Object Representation of contestants
	 * (to be used for displaying this object in table on gui-side)
	 */
	public Object[][] prepforTable(){
	//public void prepforTable(){	
		Object[][] questionsData=new Object[getNumQuestions()][objectsize];
		int count=0;

		for (int i=0; i<num_rounds; i++){
			
			for (int j=0; j<bonusQs[i].size(); j++)
			{
			
				
				for (int k=0; k< objectsize; k++){
					
					questionsData[count][k]=bonusQs[i].get(j).prepforTable()[k];
					//System.out.println(i+","+j+"/"+bonusQs[i].size()+"---"+bonusQs[i].get(j).prepforTable()[k]);
					
				}
			
				count++;
				
			}
		}
		
		
		questionsData=sortForTable(questionsData,count);
		return questionsData;
	}

	
	
	private Object[][] sortForTable(Object[][] questionData, int count){
		
	
		Object[][] sortedArray=new Object[count][objectsize];
		double[] qids=new double[count];
		
		
		//
		String[] tomodifiedIDs=new String[questionData.length];
		for (int i=0; i< tomodifiedIDs.length; i++){
			tomodifiedIDs[i]=(String) questionData[i][0];
		}
		
		String[] modifiedIDs=modifyString(tomodifiedIDs);
		
		int[] idx=new int[count];
		for (int i=0; i< count; i++){
			qids[i]=Double.parseDouble(modifiedIDs[i]);
			idx[i]=i;
		}
		
		int tmp;
		int[]indicies=bubble_srt(qids,idx,count);
		
		
		for (int i= 0; i< count ; i++){
			tmp=indicies[i];
			for (int k=0; k< objectsize; k++){

	
				sortedArray[i][k]=questionData[tmp][k];
			}

		}
		
		
		
		return sortedArray;
	}
	
	
	
	//http://www.roseindia.net/java/beginners/arrayexamples/bubbleSort.shtml
	private  int[] bubble_srt(double a[], int idx[], int n ){
		  int i, j,r=0;
		  double t=0;
		  
		  
		  for(i = 0; i < n; i++){
			  for(j = 1; j < (n-i); j++){
				  if(a[j-1] > a[j]){
			
					  t = a[j-1];
					  r=idx[j-1];
					  
					  a[j-1]=a[j];
					  idx[j-1]=idx[j];
					  
					  a[j]=t;
					  idx[j]=r;
				  }
			  }
			  
		  }
		  return idx;
	}
	
	/**
	 * For sorting the Items in the Table according to their absolute decimal value.**/
	public String[] modifyString(String tmp[]){
        String [][]array= new String[tmp.length][2];
        String[] tmp2=tmp.clone();

        int max=0;

        for (int i=0; i<tmp.length;i++){
                array[i]=tmp[i].split("\\.");

                if (array[i][1].length()>=max){
                        max=array[i][1].length();
                }
                //System.out.println(array[i][0]+"."+array[i][1]);

        }
        //System.out.println("--------------");

        int dif=0;
        String tmpVal="";
        for (int i=0; i< tmp.length; i++){

                dif=max-array[i][1].length();
                tmp2[i]=array[i][0]+"."+array[i][1];

                for (int j=0;j<dif;j++){

                        array[i][1]="0"+array[i][1];
                }


                tmp2[i]=array[i][0]+"."+array[i][1];
        }




        return tmp2;
}
	
}
