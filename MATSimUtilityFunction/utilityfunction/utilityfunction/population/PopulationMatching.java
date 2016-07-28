package utilityfunction.population;

public class PopulationMatching {

	public static void main(String[] args) {
		// TODO prepare Data - input: raw survey data; output: data similar to matsim population data (MAP)
		String csvFile = "C:/Users/Maximilian/Dropbox/01_KIT/Abschlussarbeit/UtilityMobility/Files/MIWDataRaw.csv";
		
		SurveyDataPreparation SurveyDataPreparation = new SurveyDataPreparation(csvFile);
		int column = 0;
		SurveyDataPreparation.printColumn(column);
		
		// TODO read/prepare population data
		// TODO match population input: pop-data + survey data
		

	}
// TODO Constructor: get all variables and files necessary (maybe: get config file)
}
