package test;

import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;

public class TestControler {

	public static void main(String[] args) {
		Config config = ConfigUtils.loadConfig("input/config.xml");
		Controler controler = new Controler(config);
		// controler.setOverwriteFiles(true);
		// controler.setOutputDirectory("../output/");
		controler.run();

	}

}
