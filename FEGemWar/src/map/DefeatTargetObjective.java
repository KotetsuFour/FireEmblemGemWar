package map;

import unit.Unit;

public class DefeatTargetObjective implements Objective {

	private Unit target;
	
	public DefeatTargetObjective(Unit target) {
		this.target = target;
	}
	
	@Override
	public boolean checkComplete(Chapter chpt) {
		for (int q = 0; q < chpt.getEnemyUnits().size(); q++) {
			if (chpt.getEnemyUnits().get(q) == target) {
				return false;
			}
		}
		return true;
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
		return "Defeat " + target.getName();
	}

}
