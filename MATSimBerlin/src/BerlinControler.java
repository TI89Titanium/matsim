import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;

public class BerlinControler {

	public static void main(String[] args) {
		Config config = ConfigUtils.loadConfig("input/config_be_1pct.xml");
		config.network().setInputFile("input/network.xml.gz");
		config.plans().setInputFile("input/run_160.150.plans_selected.xml.gz");
		config.controler().setLastIteration(0);
		
		config.controler().setOutputDirectory("output/");
		Controler controler = new Controler(config);
		controler.run();
	}

}
