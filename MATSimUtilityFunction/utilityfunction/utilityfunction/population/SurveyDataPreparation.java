package utilityfunction.population;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SurveyDataPreparation {

	//declaration
	String csvFile;
	BufferedReader br = null;
    String line = "";
    String cvsSplitBy = ";";

	//constructor (input file (String))
	public SurveyDataPreparation(String csvFile){
		this.csvFile = csvFile;
		

        
	}
	//read file 
	
	//getter (return map)
	public void printColumn (int column){
		try {

            br = new BufferedReader(new FileReader(csvFile));
            for (int i=0; i<11; i++){
            	line = br.readLine();
            	String[] person = line.split(cvsSplitBy);
            	System.out.println(person[column]);
            	
            }
//            while ((line = br.readLine()) != null) {

                // use semicolon as separator
  //              String[] person = line.split(cvsSplitBy);

    //            System.out.println("Person-ID [code= " + country[4] + " , name=" + country[5] + "]");

      //      }

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
	}
	
	//setter
	
}
