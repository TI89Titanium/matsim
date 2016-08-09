package utilityfunction.population;

import java.util.List;

import org.matsim.api.core.v01.Scenario;
import org.matsim.core.controler.Controler;

import utilityfunction.controler.UtilityFunctionControler;

public class PopulationMatching {

	public static void main(String[] args) {
		//prepare Data - input: raw survey data; output: data similar to matsim population data (MAP)
		String csvFile = "C:/Users/Maximilian/Dropbox/01_KIT/Abschlussarbeit/UtilityMobility/Files/MIWDataRaw.csv";
		String configFile = "../MATSimUtilityFunction/input/config_popmatching.xml";
		
		SurveyDataPreparation SurveyDataPreparation = new SurveyDataPreparation(csvFile);
		
		//Test getter method (print out first column of csv with an index)
//		int column = 0;	
//		String[] columnName =  SurveyDataPreparation.getSurveyPopulationColumnNameArray();
		
		String[] columnSelection = {"UserID", "DTWmax","MOS3","MOS4", "SOC6"};
		
		//get Array with Population only with data in specific columns
		//String[][] surveyPopulation = SurveyDataPreparation.getSurveyPopulationArray(columnSelection);
		List <String[]> surveyPopulationList = SurveyDataPreparation.getSurveyPopulationList(columnSelection);
//		testPrintSurveyPop(surveyPopulationList);
		
		//get population + get Data from Population (act + modes)
		MATSimPopulation matSimPopulation = new MATSimPopulation(configFile);
		
		List <String[]> matSimPopulationList = matSimPopulation.getMATSimPopulationList();
//		testPrintMATSimPopList(matSimPopulationList);
		printMATSimPopInfo(matSimPopulationList);
		
		matSimPopulation.addAttributesToMATSimPopulation(surveyPopulationList);
		
		Scenario scenario = matSimPopulation.getSzenario();
		

		//Start controler to get check person Attributes File
		Controler controler = new Controler(scenario);
		
		//TODO: define routing for other modes beside car or define other modes
				// first try agents with "normal" matsim-modes
				UtilityFunctionControler.setNetworkModeRouting(controler);
				
		//TODO: adding pt fare
				
				// adding basic strategies for car and non-car users
				UtilityFunctionControler.setBasicStrategiesForSubpopulations(controler);
		
		controler.run();
		

	}
	
	//Test: print out array
	public static void testPrintSurveyPop (List <String[]> surveyPopulationList){
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
	public static void testPrintMATSimPopList (List <String[]> matSimPopulationList){
		for(String[] ausgabe : matSimPopulationList){
			System.out.println(ausgabe[0] + " " + ausgabe[1] + " " + ausgabe[2]);
		}
		System.out.println(matSimPopulationList.size());
	}
	
	//Test: print out matSimList
	public static void printMATSimPopInfo (List <String[]> matSimPopulationList){
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

