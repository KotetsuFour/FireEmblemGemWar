package story;

import data_structures.List;
import map.Map;
import unit.Unit;

public class ReinforcementsEvent extends StoryEvent {

	List<Unit> units;
	List<int[]> coords;
	
	public ReinforcementsEvent(int turn) {
		super(turn);
	}
	
	public void addUnit(Unit u, int x, int y) {
		units.add(u);
		coords.add(new int[] {x, y});
	}
	
	public void putUnitOnMap(Map map) {
		for (int q = 0; q < units.size(); q++) {
			Unit u = units.get(q);
			int[] c = coords.get(q);
			//TODO fix so that no overwriting happens
			map.getTileAt(c[0], c[1]).setOccupant(u);
		}
	}
}
