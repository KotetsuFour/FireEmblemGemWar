package map;

public class EscapeObjective implements Objective {

	private int unitsNeededToEscape;
	
	public EscapeObjective(int unitsNeededToEscape) {
		this.unitsNeededToEscape = unitsNeededToEscape;
	}
	
	@Override
	public boolean checkComplete(Chapter chpt) {
		int num = 0;
		for (int q = 0; q < chpt.getEscapedUnits().size(); q++) {
			if (chpt.getEscapedUnits().get(q).isEssential()) {
				num++;
			}
		}
		return num >= unitsNeededToEscape;
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
		return "Escape to Warp";
	}

}
