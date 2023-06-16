package map;

public class FightObjective implements Objective {

	private int unitsToDefeat;
	
	public FightObjective(int unitsToDefeat) {
		this.unitsToDefeat = unitsToDefeat;
	}
	
	@Override
	public boolean checkComplete(Chapter chpt) {
		// TODO Auto-generated method stub
		return false;
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
		return "Defeat " + unitsToDefeat + " enemies";
	}

	
}
