package utilityfunction.scoring.function;

import org.matsim.api.core.v01.events.Event;
import org.matsim.api.core.v01.population.Leg;

/**
 * This is a implementation of the utlity function with additional parameters based on the CharyparNagel function
 * from the MATSim Score
 * 
 * @author Maximilian Wolf (GitHub: <a href="https://github.com/TI89Titanium">TI89Titanium</a>)
 */

public class WolfLegScoring implements org.matsim.core.scoring.SumScoringFunction.LegScoring, org.matsim.core.scoring.SumScoringFunction.ArbitraryEventScoring {

	protected double score;
	
	//declare parameters
	//TODO add parameter (instance of parameter-class "WolfScoringParameters"
	//public final double monetaryDistanceRate; etc.
	
		
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getScore() {
		// TODO Auto-generated method stub
		return this.score;
	}

	@Override
	public void handleEvent(Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleLeg(Leg leg) {
		// TODO Auto-generated method stub
		
	}

}
