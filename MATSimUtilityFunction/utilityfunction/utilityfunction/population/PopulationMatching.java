package utilityfunction.population;

import java.util.List;

public class PopulationMatching {

	public static void main(String[] args) {
		// TODO prepare Data - input: raw survey data; output: data similar to matsim population data (MAP)
		String csvFile = "C:/Users/Maximilian/Dropbox/01_KIT/Abschlussarbeit/UtilityMobility/Files/MIWDataRaw.csv";
		String configFile = "../MATSimUtilityFunction/input/config_3agents.xml";
		
		SurveyDataPreparation SurveyDataPreparation = new SurveyDataPreparation(csvFile);
		
		//Test getter method (print out first column of csv with an index)
//		int column = 0;	
//		String[] columnName =  SurveyDataPreparation.getSurveyPopulationColumnNameArray();
		
		String[] columnSelection = {"UserID", "DTWmax","MOS3","MOS4", "SOC6"};
		
		//get Array with Population only with data in specific columns
		//String[][] surveyPopulation = SurveyDataPreparation.getSurveyPopulationArray(columnSelection);
		List <String[]> surveyPopulationList = SurveyDataPreparation.getSurveyPopulationList(columnSelection);
		
		
		
		//get population + get Data from Population (act + modes)
		MATSimPopulationPreparation MATSimPopulationPreparation = new MATSimPopulationPreparation(configFile);
		
		
		//add attributes to existing population
		
		//Test: print out array
		int length = surveyPopulationList.size();		
//			System.out.println(columnName[column]);
			//print out Array:
			for (int i=0; i<length; i++){
				System.out.print(i+1 + " ");
				for (int j=0; j<surveyPopulationList.get(i).length; j++){
				
				System.out.print(surveyPopulationList.get(i)[j] + " ");
				}
				System.out.println("");
			}

			
		}

		// TODO match population input: pop-data + survey data
		

	}
// TODO Constructor: get all variables and files necessary (maybe: get config file)

