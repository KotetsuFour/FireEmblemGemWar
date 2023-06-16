package map;

public class RoutObjective implements Objective {

	@Override
	public boolean checkComplete(Chapter chpt) {
		return chpt.getEnemyUnits().isEmpty();
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
		return "Defeat all enemies";
	}

}
