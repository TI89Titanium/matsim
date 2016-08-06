package utilityfunction.population;

import java.util.List;

public class PopulationMatching {

	public static void main(String[] args) {
		// TODO prepare Data - input: raw survey data; output: data similar to matsim population data (MAP)
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
		testPrintSurveyPop(surveyPopulationList);
		
		//get population + get Data from Population (act + modes)
		MATSimPopulationPreparation matSimPopulationPreparation = new MATSimPopulationPreparation(configFile);
		
		List <String[]> matSimPopulationList = matSimPopulationPreparation.getMATSimPopulationList();
		
		testPrintMATSimPopList(matSimPopulationList);
		
		
		// TODO match population input: pop-data + survey data
		

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
}
// TODO Constructor: get all variables and files necessary (maybe: get config file)

