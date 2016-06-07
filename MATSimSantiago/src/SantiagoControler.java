import org.matsim.contrib.carsharing.runExample.RunCarsharing;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;

import playground.santiago.run.SantiagoScenarioRunner;

public class SantiagoControler {

	public static void main(String[] args) {
		/*
		Config config = ConfigUtils.loadConfig("input/config_baseCase.xml");
		Controler controler = new Controler(config);
		// controler.setOverwriteFiles(true);
		// controler.setOutputDirectory("../output/");
		controler.run();
		*/
		String inputFilePath[] = {"input/config_baseCase.xml"};
		SantiagoScenarioRunner.main(inputFilePath);
	}

}
