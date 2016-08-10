package utilityfunction.population;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.core.population.PlanImpl;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.utils.objectattributes.ObjectAttributes;

public class MATSimPopulation {
	
	final Scenario scenario;
	final Population population;
	List<String[]> matchingList = new ArrayList<String[]>();
	
	public MATSimPopulation(String configFile){
		//get population from configFile
		this.scenario = ScenarioUtils.loadScenario(ConfigUtils.loadConfig(configFile));
		this.population = scenario.getPopulation();
	}
	
	public Population getPopulation(){
		return this.population;
	}
		
	public List<String[]> getMATSimPopulationList(){
		//go through all Agents (person)
		for (Person person : scenario.getPopulation().getPersons().values()) {
			List <PlanElement> actsLegs = ((PlanImpl)(person.getSelectedPlan())).getPlanElements();
			double distanceToWork = 0;
			double longestDistance = 0;
			String mode = "";
			String lastMode ="";
			String currentMode ="";
			boolean hasHomeAct = false;
			boolean hasWorkAct = false;
			int numberOfLegs = 0;
			boolean modeIsCombination = false;
			//go through all Persons Plan, sum distanceToWork
			for (PlanElement actLeg : actsLegs){
				if (actLeg instanceof Activity){ //Plan Element is an Activity
					if (((Activity) actLeg).getType().contains("home")){ //check if activity is "home"-Activity
						distanceToWork = 0;
						hasHomeAct = true; //start adding 
					}
					else if(((Activity) actLeg).getType().contains("work")){
						hasWorkAct = true; //stop adding
						break; //stops going through Plan
					}
				}else if (actLeg instanceof Leg){
					if (hasHomeAct){
						numberOfLegs++; //count number of Legs to work
						double distance = ((Leg) actLeg).getRoute().getDistance();
						distanceToWork = distanceToWork + distance;
						currentMode = ((Leg) actLeg).getMode();
						if (distance >= longestDistance){
							longestDistance = distance;
							mode = currentMode;
						}
						//if more then one mode (excluding walking) is used to get to work, set mode to "combination"
						if (numberOfLegs > 1 && !currentMode.equals(lastMode) && !currentMode.contains("walk") && !lastMode.contains("walk")){
							modeIsCombination = true;
						}
						lastMode = currentMode;
					}
					
				}
				
			}
			if (hasHomeAct && hasWorkAct){
				//TODO save distancetoWork with Person-ID (List)
				//Characteristics (person ID, distance to work, main mode) saved in String-Array
				String[] characteristics = new String[3];
				matchingList.add(characteristics);
				characteristics[0] = person.getId().toString();
				characteristics[1] = String.valueOf(distanceToWork);
				if (modeIsCombination){ //set mode to "combination" if more than one mode (excluding walking) is used
					characteristics[2] = "combination";
				}
				else{
					characteristics[2] = mode;
				}
				
			}
		}
		return matchingList;
	}
	
	
	public ObjectAttributes addAttributes(List <String[]> surveyPopulationList){
		
		// surveyPopulationList needs to be {"UserID", "DTWmax","MOS3","MOS4", "SOC6", "MPT"}
		// MOS3: Distance to Work A through F
		// MOS4: Main Mode to Work A though H
		// SOC6: monthly income A through F
		// MPT: ModePreferenceType (based on usage): {"Pragmatic Long-Distance Driver", "Auto Addicted", "Active Public Transportation Lover", "Lazy Public Transportation Lover", "Active Auto Lover"}
		// TODO add attributes population input: pop-data + survey data
		
//		public static void addAttributesToMATSimPop (Population population, List<String[]> surveyPopulationList){
			
//		}
		matchingList = this.getMATSimPopulationList(); //MATSimPopulation List needs to be: {"ID", "DistanceToWork", "ModeToWork"}
		final ObjectAttributes personAttributes = population.getPersonAttributes();
		final String DAILY_INCOME = "DAILY_INCOME";
		
		int noMatches = 0;
		
		for (String[] agent : matchingList){
			//go through all survey Pop and find all matching -> number of matches
			int modeNumberMATSim = this.setModeNumberMATSim(agent[2]); //mode information
			int numberOfMatches = 0;
			
			List<Double> dailyIncome = new ArrayList<Double>();
			
			List<String> modePreferenceType = new ArrayList<String>();
			
			for (String[] person: surveyPopulationList){
				int modeNumberSurvey = this.setModeNumberSurvey(person[3]); //mode: "MOS4"
				if (modeNumberSurvey == modeNumberMATSim){
					//cast String to double and compare distance "MOS3"
					if(this.compareDistance(agent[1], person[2])){
						numberOfMatches++;
						//save Attributes to List : income, mode-Preference-Type
						
						dailyIncome.add(this.getDailyIncome(person[4],1)); //set number of persons in HH to 1
						
						//modePreferenceType.add(person[5]); //set up survey with column MPT
						
					}
					
				}
				
			}
			if (numberOfMatches == 0){
				//set attributes random over all survey pop
				Random randomGenerator = new Random(); 
				int index = randomGenerator.nextInt(surveyPopulationList.size());
				personAttributes.putAttribute(agent[0], DAILY_INCOME, this.getDailyIncome(surveyPopulationList.get(index)[4],1)); //one person in houshold
				noMatches++;
			}
			else if (numberOfMatches == 1){
				//set Attributes from spesific surveyPerson
				personAttributes.putAttribute(agent[0], DAILY_INCOME, dailyIncome.get(0));
				
			}
			else if (numberOfMatches > 1){
				//set Attributes random over matching survey Pop
//				dailyIncome.removeIf(Objects::isNull); //remove "null" elements (not neccessary)
				Random randomGenerator = new Random(); 
				int index = randomGenerator.nextInt(dailyIncome.size()); //generates random Integer between 0 (included) and size of List dailyIncome
				personAttributes.putAttribute(agent[0], DAILY_INCOME, dailyIncome.get(index));
			}
			//reset (empty) Attribute-List
//			System.out.println(numberOfMatches);
		}
	
		System.out.println("Number of Agents w/o matches in Sruvey: " + noMatches);
		
		int allreadyhasIncomeAttribute = 0;
		//add Attributes random to other population
		for (Person person : population.getPersons().values()) {
			if (personAttributes.getAttribute(person.getId().toString(), DAILY_INCOME) == null) { //person has no income attribute
				
				Random randomGenerator = new Random(); 
				int index = randomGenerator.nextInt(surveyPopulationList.size());
				personAttributes.putAttribute(person.getId().toString(), DAILY_INCOME, this.getDailyIncome(surveyPopulationList.get(index)[4],1));
			}
			else{
				allreadyhasIncomeAttribute++;
			}
			if (true){ //person has MPT attribute
				
			}
		}
		System.out.println("agents already having an income Attribute (must be equal to number of agents having home-work info): " + allreadyhasIncomeAttribute);
		
		return personAttributes; //maybe void (just generating/adding to the attribute xml-file
	}
	
	//monthlyIncome times 12 devided by 240 (working days) 
	//(can also be devided by number of houshold members if information is available)
	private Double getDailyIncome(String income, int personsInHoushold) {
		double dailyIncome = -1;
		if (income.equals("A")){
			dailyIncome = (double) 500 * 12 / (personsInHoushold * 240);
			}
		else if (income.equals("B")){
			dailyIncome = (double) 1500 * 12 / (personsInHoushold * 240);
		}
		else if (income.equals("C")){
			dailyIncome = (double) 2500 * 12 / (personsInHoushold * 240);
		}
		else if (income.equals("D")){
			dailyIncome = (double) 3500 * 12 / (personsInHoushold * 240);
		}
		else if (income.equals("E")){
			dailyIncome = (double) 4500 * 12 / (personsInHoushold * 240);
		}
		else if (income.equals("F")){
			dailyIncome = (double) 5500 * 12 / (personsInHoushold * 240);
		}
		return dailyIncome;
}


	private int setModeNumberMATSim (String mode){
		//declare mode-compare Integer 1 - "walk", 2 - "bike", 3 - "pt", 4 - "car", 5 - "ride", 6 - "train", 7- "combination", 8 - "other"};
		int modeNumber = 0;
		if (mode.equals("walk") || mode.equals("transit_walk")){
			modeNumber = 1;
		}
		else if (mode.equals("bike")){
			modeNumber = 2;
		}
		else if (mode.equals("colectivo")||mode.equals("pt")){
			modeNumber = 3;
		}
		else if (mode.equals("car")){
			modeNumber = 4;
		}
		else if (mode.equals("ride")||mode.equals("taxi")){
			modeNumber = 5;
		}
		else if (mode.equals("train")){
			modeNumber = 6;
		}
		else if (mode.equals("combination")){
			modeNumber = 7;
		}
		else if (mode.equals("other")){
			modeNumber = 8;
	}
		return modeNumber;
}
	private int setModeNumberSurvey (String mode){
		//declare mode-compare Integer 1 - "walk", 2 - "bike", 3 - "pt", 4 - "car", 5 - "ride", 6 - "train", 7- "combination", 8 - "other"}
		
		int modeNumber = 0;
		if (mode.equals("A")){
			modeNumber = 1;
		}
		else if (mode.equals("B")){
			modeNumber = 2;
		}
		else if (mode.equals("C")){
			modeNumber = 3;
		}
		else if (mode.equals("D") || mode.equals("F")){ //motorbike counts as car
			modeNumber = 4;
		}
		else if (mode.equals("E")){
			modeNumber = 5;
		}
		else if (mode.equals("G")){
			modeNumber = 6;
		}
		else if (mode.equals("H")){
			modeNumber = 7;
		}
		
		//no mode "other" in survey
//		else if (mode.equals("other")){
//			modeNumber = 8;

		return modeNumber;
	}
	
	private boolean compareDistance (String distanceMATSim, String distanceRangeSurvey){
		boolean sameDistance = false;
		double distanceMATSimValue = Double.parseDouble(distanceMATSim);
		double lowerBound;
		double upperBound;
		
		//set bounds for survey-answers and compare to distanceMATSimValue
		if (distanceRangeSurvey.equals("A")){
			upperBound = 1;
			if (distanceMATSimValue <= upperBound){
				sameDistance = true;
			}
		}
		else if (distanceRangeSurvey.equals("B")){
			lowerBound = 1;
			upperBound = 5;
			if (distanceMATSimValue > lowerBound || distanceMATSimValue <= upperBound){
				sameDistance = true;
			}
		}
		else if (distanceRangeSurvey.equals("C")){
			lowerBound = 5;
			upperBound = 10;
			if (distanceMATSimValue > lowerBound || distanceMATSimValue <= upperBound){
				sameDistance = true;
			}
		}
		else if (distanceRangeSurvey.equals("D")){
			lowerBound = 10;
			upperBound = 30;
			if (distanceMATSimValue > lowerBound || distanceMATSimValue <= upperBound){
				sameDistance = true;
			}
		}
		else if (distanceRangeSurvey.equals("E")){
			lowerBound = 30;
			upperBound = 50;
			if (distanceMATSimValue > lowerBound || distanceMATSimValue <= upperBound){
				sameDistance = true;
			}
		}
		else if (distanceRangeSurvey.equals("F")){
			lowerBound = 50;
			if (distanceMATSimValue > lowerBound){
				sameDistance = true;
			}
		}
		
		return sameDistance;
		
		
	}

	public Scenario getScenario() {
		return this.scenario;
	}
}
