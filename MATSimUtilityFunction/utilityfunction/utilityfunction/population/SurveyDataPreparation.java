package utilityfunction.population;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;


public class SurveyDataPreparation {

	//declaration
	String csvFile;
	BufferedReader br = null;
    String line = "";
    String cvsSplitBy = ";"; //use semicolon as separator
    //survey-population with id, distance to work, main-mode
    String [] columnName;
    String[][] surveyPopulation;
    
    //private final ObjectAttributes personAttributes = new ObjectAttributes(); //Attributes (distance to work, main-mode)
    //Map<Id<Person>, ObjectAttributes> surveyPopulation = new Map<Id<Person>, ObjectAttributes>();


	//constructor (input file (String))
	public SurveyDataPreparation(String csvFile){
		this.csvFile = csvFile;
		this.surveyPopulation = readFile();

        
	}
	//read file 
	//read file save in array or hasmap
	private String[][] readFile(){
		
		int numberOfLines = 0;
		//get number of lines (persons)
		try {

	        br = new BufferedReader(new FileReader(csvFile));
	                  	        	        
	        while (((line = br.readLine()) != null)) {   
	        	numberOfLines++;
	    	 }
	        

	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if (br != null) {
	            try {
	                br.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }	
		
		surveyPopulation = new String[numberOfLines-1][];//initialise without columnName-Line
		
	try {

        br = new BufferedReader(new FileReader(csvFile));
        
        
        int i = 0;
        
        	        
        while (((line = br.readLine()) != null)) {

        	if (i==0){
        		columnName = line.split(cvsSplitBy); //use semicolon as separator
        	}
        	else {
        		surveyPopulation[i-1] = line.split(cvsSplitBy);
        	}
        
        	
        	i++;
    	 }
        

    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
	return surveyPopulation;
	
	}
	
	//getter (return String Array)
	public String[][] getSurveyPopulationArray(){
		surveyPopulation = translatePopulationData(surveyPopulation);
		return this.surveyPopulation;
	}
	
	public String[][] getSurveyPopulationArray(String[] columnSelection){
		int numberOfColumns = columnSelection.length;
		int numberOfRows = surveyPopulation.length;
		Integer [] columnIdx = new Integer[numberOfColumns];
		String [][] surveyPopulationColumnSelection = new String[numberOfRows][numberOfColumns];
		
		//Search for String (columnName)
		for (int i=0; i<numberOfColumns; i++){
			columnIdx[i] = Arrays.asList(columnName).indexOf(columnSelection[i]);
		}
		
		for (int i=0; i<numberOfRows; i++){
			for (int j=0; j<numberOfColumns; j++){
					//Fill Selection array
				if (surveyPopulation[i].length<columnIdx[j]){
					surveyPopulationColumnSelection[i][j] = "";
				}
				else {
					surveyPopulationColumnSelection[i][j] = surveyPopulation[i][columnIdx[j]];
				}
			}
		}
		if(columnSelection[0].equals("UserID")){
			surveyPopulationColumnSelection = translatePopulationData(surveyPopulationColumnSelection);
		}
		
		
		return surveyPopulationColumnSelection;
	}
	
	public String[] getSurveyPopulationColumnNameArray(){
		return this.columnName;
	}
	
	private String[][] translatePopulationData(String[][] surveyPopulation){
		
		//output: MatSim Population File
		
		//add sufix to ID (first column)
		String surveyPopulationPrefix = "SP";
		for (int i=0; i<surveyPopulation.length; i++){
			surveyPopulation[i][0] = surveyPopulationPrefix + surveyPopulation[i][0];
		}
		
		
		return surveyPopulation;
	}

		
	
	//setter
	
}
