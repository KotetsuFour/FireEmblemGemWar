package map;


public class SurviveObjective implements Objective {

	private int turnsToSurviveFor;
	
	public SurviveObjective(int turnsToSurviveFor) {
		this.turnsToSurviveFor = turnsToSurviveFor;
	}
	
	@Override
	public boolean checkComplete(Chapter chpt) {
		return chpt.getMap().getTurn() > turnsToSurviveFor;
	}

	@Override
	public boolean checkFailed(Chapter chpt) {
		for (int q = 0; q < chpt.getDefeatedUnits().size(); q++) {
			if (chpt.getDefeatedUnits().get(q).isEssential()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "Survive for " + turnsToSurviveFor + " turns";
	}

}
