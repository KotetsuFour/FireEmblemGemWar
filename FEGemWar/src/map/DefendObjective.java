package map;

public class DefendObjective implements Objective {

	private int turnsToDefendFor;
	
	public DefendObjective(int turnsToDefendFor) {
		this.turnsToDefendFor = turnsToDefendFor;
	}
	
	@Override
	public boolean checkComplete(Chapter chpt) {
		return chpt.getMap().getTurn() > turnsToDefendFor;
	}

	@Override
	public boolean checkFailed(Chapter chpt) {
		//TODO if the point to be defended is seized, return true
		for (int q = 0; q < chpt.getDefeatedUnits().size(); q++) {
			if (chpt.getDefeatedUnits().get(q).isEssential()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "Defend for " + turnsToDefendFor + " turns";
	}

}
