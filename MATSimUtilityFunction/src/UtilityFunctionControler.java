import org.matsim.api.core.v01.Scenario;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.scenario.ScenarioUtils;

public class UtilityFunctionControler {

//	private static boolean doModeChoice = true;
	
	private static String configFile;
	
	public static void main(String[] args) {


		if(args.length==0){
//TODO:	to few arguments exception
		} else {
			configFile = args[0];
		}
		
		Config config = ConfigUtils.loadConfig(configFile);
		
		Scenario scenario = ScenarioUtils.loadScenario(config);
		
		Controler controler = new Controler(scenario);
	
		
		controler.run();
	}

}
