import org.matsim.core.controler.Controler;

public class BerlinControler {

	public static void main(String[] args) {
		Controler controler = new Controler("input/config_be_1pct.xml");
		controler.run();
	}

}
