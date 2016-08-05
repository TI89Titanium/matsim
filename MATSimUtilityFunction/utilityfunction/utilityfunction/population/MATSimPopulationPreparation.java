package utilityfunction.population;

import java.util.ArrayList;
import java.util.List;

import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.core.population.PersonImpl;
import org.matsim.core.population.PlanImpl;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.utils.objectattributes.ObjectAttributes;

public class MATSimPopulationPreparation {
	
	final Scenario scenario;
	final Population population;
	List<String[]> matchingList = new ArrayList<String[]>();
	
	public MATSimPopulationPreparation(String configFile){
		//TODO: get population from configFile
		this.scenario = ScenarioUtils.loadScenario(ConfigUtils.loadConfig(configFile));
		this.population = scenario.getPopulation();
	}
		
	public List<String[]> getMATSimPopulationList(){
		//go through all Agents (person)
		for (Person person : scenario.getPopulation().getPersons().values()) {
			Activity firstActivity = ((PlanImpl)(person.getSelectedPlan())).getFirstActivity();
			List <PlanElement> actsLegs = ((PlanImpl)(person.getSelectedPlan())).getPlanElements();
			double distanceToWork = 0;
			boolean hasHomeAct = false;
			boolean hasWorkAct = false;
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
						distanceToWork = distanceToWork +((Leg) actLeg).getRoute().getDistance();
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
				characteristics[2] = "mode"; //not yet implemented
				
			}
		}
		return matchingList;
	}
}
