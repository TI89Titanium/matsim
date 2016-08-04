package utilityfunction.population;

import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.population.PersonImpl;
import org.matsim.core.population.PlanImpl;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.utils.objectattributes.ObjectAttributes;

public class MATSimPopulationPreparation {
	public MATSimPopulationPreparation(String configFile){
		//TODO: get population from configFile
		final Scenario scenario = ScenarioUtils.loadScenario(ConfigUtils.loadConfig(configFile));
		final Population population = scenario.getPopulation();
		
		for (Person person : scenario.getPopulation().getPersons().values()) {
			Activity firstActivity = ((PlanImpl)(person.getSelectedPlan())).getFirstActivity();
			if (firstActivity.getType().contains("home")){
				//
			}
			else{
				break;
			}
		}
		
	}
}
