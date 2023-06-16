package map;

import java.util.HashMap;

import data_structures.List;
import map.Tile.TileType;
import story.StoryEvent;
import unit.Unit;

public class Chapter {

	private Map map;
	private Objective objective;
	private boolean seized;
	private List<Unit> player;
	private List<Unit> enemies;
	private List<Unit> allies;
	private List<Unit> escaped;
	private List<Unit> defeated;
	private List<StoryEvent> sequence;
	private HashMap<TileType, String> design;
	
	public Chapter(Map map, Objective objective) {
		this.map = map;
		this.objective = objective;
		this.player = new List<>();
		this.enemies = new List<>();
		this.allies = new List<>();
		this.escaped = new List<>();
		this.defeated = new List<>();
		this.sequence = new List<>();
		this.design = new HashMap<>();
	}
	
	public Map getMap() {
		return map;
	}
	public List<Unit> getPlayerUnits() {
		return player;
	}
	public List<Unit> getAllyUnits() {
		return allies;
	}
	public List<Unit> getEnemyUnits() {
		return enemies;
	}
	public List<Unit> getEscapedUnits() {
		return escaped;
	}
	public List<Unit> getDefeatedUnits() {
		return defeated;
	}
	
	
	public int getAmountInSequence() {
		return sequence.size();
	}
	public StoryEvent getEvent(int idx) {
		return sequence.get(idx);
	}
	public void addEvent(StoryEvent event) {
		sequence.add(event);
	}
	public HashMap<TileType, String> getDesign() {
		return design;
	}
	public Objective getObjective() {
		return objective;
	}
	public void setSeized(boolean s) {
		seized = s;
	}
	public boolean isSeized() {
		return seized;
	}

}
