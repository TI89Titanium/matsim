package utilityfunction.population;

import java.util.List;

import org.matsim.api.core.v01.Scenario;


public class PopulationMatching {
	
	public static final String[] columnSelection = {"UserID", "DTWmax","MOS3","MOS4", "SOC6"};
	private String csvFile; 
	private String configFile;
	
	public PopulationMatching (String configFile, String csvFile){
		this.configFile = configFile;
		this.csvFile = csvFile;
		
	}
	
	public Scenario matchPopulation() {
		
		//prepare Data - input: raw survey data
		SurveyData surveyData = new SurveyData(csvFile);
		
		//get Array with Population only with data in specific columns
		List <String[]> surveyPopulationList = surveyData.getSurveyPopulationList(columnSelection);
		
//		testPrintSurveyPop(surveyPopulationList);
		
		//get population + get Data from Population (act + modes)
		MATSimPopulation matSimPopulation = new MATSimPopulation(configFile);
		
		List <String[]> matSimPopulationList = matSimPopulation.getMATSimPopulationList();

		printMATSimPopInfo(matSimPopulationList);
		
		matSimPopulation.addAttributes(surveyPopulationList);
		
		Scenario scenario = matSimPopulation.getScenario();
		
		System.out.println("---------------------------------------------");
		System.out.println("---------------------------------------------");
		System.out.println("Added additional Attributes to Attributes-File");
		System.out.println("---------------------------------------------");
		System.out.println("---------------------------------------------");
		
		return scenario;
		
	}
	
	//Test: print out array
	private void testPrintSurveyPop (List <String[]> surveyPopulationList){
		int length = surveyPopulationList.size();		
//		System.out.println(columnName[column]);
		//print out Array:
		for (int i=0; i<length; i++){
			System.out.print(i+1 + " ");
			for (int j=0; j<surveyPopulationList.get(i).length; j++){
			
			System.out.print(surveyPopulationList.get(i)[j] + " ");
			}
			System.out.println("");
		}
	}
	
	//Test: print out matSimList
	private void testPrintMATSimPopList (List <String[]> matSimPopulationList){
		for(String[] ausgabe : matSimPopulationList){
			System.out.println(ausgabe[0] + " " + ausgabe[1] + " " + ausgabe[2]);
		}
		System.out.println(matSimPopulationList.size());
	}
	
	//Test: print out matSimList
	private void printMATSimPopInfo (List <String[]> matSimPopulationList){
		//Occ of modes walk (walk, transit_walk); bike; pt (colectivo, pt); car; ride(ride, taxi); train; combination; other

		Integer[] modeOcc = {0,0,0,0,0,0,0,0};
		int sum = 0;
		String [] modes = {"walk", "bike", "pt", "car", "ride", "train", "combination", "other"};
		
		for(String[] print : matSimPopulationList){
			if (print[2].equals("walk") || print[2].equals("transit_walk")){
				modeOcc[0]++;
				sum++;
			}
			else if (print[2].equals("bike")){
				modeOcc[1]++;
				sum++;
			}
			else if (print[2].equals("colectivo")||print[2].equals("pt")){
				modeOcc[2]++;
				sum++;
			}
			else if (print[2].equals("car")){
				modeOcc[3]++;
				sum++;
			}
			else if (print[2].equals("ride")||print[2].equals("taxi")){
				modeOcc[4]++;
				sum++;
			}
			else if (print[2].equals("train")){
				modeOcc[5]++;
				sum++;
			}
			else if (print[2].equals("combination")){
				modeOcc[6]++;
				sum++;
			}
			else if (print[2].equals("other")){
				modeOcc[7]++;
				sum++;
			}
				
		}
		System.out.println("Number of Agents: " + matSimPopulationList.size());
		System.out.println("Number of Agents with recorded Mode: " + sum);
		for (int i = 0; i<modeOcc.length; i++){
			System.out.println("Mode " + modes[i] + ": " + modeOcc[i]);
		}
	}
	
}
// TODO Constructor: get all variables and files necessary (maybe: get config file)

