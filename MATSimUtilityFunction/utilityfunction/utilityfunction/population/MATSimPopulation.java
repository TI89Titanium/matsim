package utilityfunction.population;

import java.util.ArrayList;
import java.util.List;

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
	public ObjectAttributes addAttributesToMATSimPopulation(List <String[]> surveyPopulationList){
		
		
		return null;
	}
}
