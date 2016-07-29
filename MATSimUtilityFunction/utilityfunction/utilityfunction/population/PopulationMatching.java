package utilityfunction.population;

public class PopulationMatching {

	public static void main(String[] args) {
		// TODO prepare Data - input: raw survey data; output: data similar to matsim population data (MAP)
		String csvFile = "C:/Users/Maximilian/Dropbox/01_KIT/Abschlussarbeit/UtilityMobility/Files/MIWDataRaw.csv";
		
		SurveyDataPreparation SurveyDataPreparation = new SurveyDataPreparation(csvFile);
		
		//Test getter method (print out first column of csv with an index)
//		int column = 0;	
//		String[] columnName =  SurveyDataPreparation.getSurveyPopulationColumnNameArray();
		
		String[] columnSelection = {"UserID", "DTWmax"};
		
		String[][] surveyPopulation = SurveyDataPreparation.getSurveyPopulationArray(columnSelection);
		int length = surveyPopulation.length;
		
//			System.out.println(columnName[column]);
			//print out Array:
			for (int i=0; i<length; i++){
				System.out.print(i+1 + " ");
				for (int j=0; j<surveyPopulation[i].length; j++){
				
				System.out.print(surveyPopulation[i][j] + " ");
				}
				System.out.println("");
			}
		
		// TODO read/prepare population data
		// TODO match population input: pop-data + survey data
		

	}
// TODO Constructor: get all variables and files necessary (maybe: get config file)
}
