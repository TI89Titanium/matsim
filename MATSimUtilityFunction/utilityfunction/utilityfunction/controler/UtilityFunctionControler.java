package utilityfunction.controler;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.StrategyConfigGroup.StrategySettings;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.MatsimServices;
import org.matsim.core.replanning.strategies.DefaultPlanStrategiesModule;
import org.matsim.core.scenario.ScenarioUtils;

import utilityfunction.constants.UtilityFunctionConstants;

public class UtilityFunctionControler {
	
	private static String inputPath = "../MATSimUtilityFunction/input/";

//	private static boolean doModeChoice = true;
	
	private static String configFile;
	
	public static void main(String[] args) {


		if(args.length==0){
			// use default config file
			configFile = inputPath + "config_3agents.xml";

		} else {
			configFile = args[0];
		}
		
		Config config = ConfigUtils.loadConfig(configFile);
		
		Scenario scenario = ScenarioUtils.loadScenario(config);
		
		Controler controler = new Controler(scenario);
	
//TODO: define routing for other modes beside car or define other modes
		// first try agents with "normal" matsim-modes
		setNetworkModeRouting(controler);
		
//TODO: adding pt fare
		
		// adding basic strategies for car and non-car users
		setBasicStrategiesForSubpopulations(controler);
		
//TODO: mapping agents' activities to links on the road network to avoid being stuck on the transit network
		
		controler.run();
	}

	// methods for routing-settings for all modes
	private static void setNetworkModeRouting(Controler controler) {
		controler.addOverridingModule(new AbstractModule() {
			@Override
			public void install() {
				addTravelTimeBinding(TransportMode.ride).to(networkTravelTime());
				addTravelDisutilityFactoryBinding(TransportMode.ride).to(carTravelDisutilityFactoryKey());
				addTravelTimeBinding(UtilityFunctionConstants.Modes.taxi.toString()).to(networkTravelTime());
				addTravelDisutilityFactoryBinding(UtilityFunctionConstants.Modes.taxi.toString()).to(carTravelDisutilityFactoryKey());
				addTravelTimeBinding(UtilityFunctionConstants.Modes.colectivo.toString()).to(networkTravelTime());
				addTravelDisutilityFactoryBinding(UtilityFunctionConstants.Modes.colectivo.toString()).to(carTravelDisutilityFactoryKey());
				addTravelTimeBinding(UtilityFunctionConstants.Modes.other.toString()).to(networkTravelTime());
				addTravelDisutilityFactoryBinding(UtilityFunctionConstants.Modes.other.toString()).to(carTravelDisutilityFactoryKey());
			}
		});
	}
	
	
	
	// mehtods for strategy setting
	private static void setBasicStrategiesForSubpopulations(MatsimServices controler) {
		setReroute("carAvail", controler);
		setChangeExp("carAvail", controler);
		setReroute(null, controler);
		setChangeExp(null, controler);
	}

	private static void setChangeExp(String subpopName, MatsimServices controler) {
		StrategySettings changeExpSettings = new StrategySettings();
		changeExpSettings.setStrategyName(DefaultPlanStrategiesModule.DefaultSelector.ChangeExpBeta.toString());
		changeExpSettings.setSubpopulation(subpopName);
		changeExpSettings.setWeight(0.7);
		controler.getConfig().strategy().addStrategySettings(changeExpSettings);
	}
	
	private static void setReroute(String subpopName, MatsimServices controler) {
		StrategySettings reRouteSettings = new StrategySettings();
		reRouteSettings.setStrategyName(DefaultPlanStrategiesModule.DefaultStrategy.ReRoute.toString());
		reRouteSettings.setSubpopulation(subpopName);
		reRouteSettings.setWeight(0.15);
		controler.getConfig().strategy().addStrategySettings(reRouteSettings);
	}
}
