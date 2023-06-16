package manager;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import data_structures.LinkedQueue;
import data_structures.List;
import inventory.Item;
import inventory.weapon.Armor;
import inventory.weapon.Axe;
import inventory.weapon.Club;
import inventory.weapon.Lance;
import inventory.weapon.Sword;
import inventory.weapon.Weapon;
import inventory.weapon.Whip;
import javafx.scene.image.Image;
import manager.combat.CombatManager;
import map.Chapter;
import map.EscapeObjective;
import map.Map;
import map.Objective;
import map.RoutObjective;
import map.Tile;
import map.Tile.TileType;
import story.DialogueEvent;
import unit.Unit;
import unit.Unit.AIType;
import unit.UnitClass;

public class FEGemWarManager {

	public static final Random RNG = new Random();
	public static final List<Integer> RNG_STORE = new List<>();
	public static boolean futureVision = false;
	public static final List<Unit> roster = new List<>();
	public static final List<Item> convoy = new List<>();
	
	public static int chapterIdx = 0;
	public static int chapterPartIdx = 0;
	public static int turn;
	
	public static final UnitClass lord = new UnitClass("Lord", 18, 4, 0, 2, 3, 0, 2, 0, 5, 6);
	public static final UnitClass servant = new UnitClass("Servant", 20, 3, 0, 5, 6, 0, 2, 0, 6, 6);
	public static final UnitClass soldier = new UnitClass("Soldier", 20, 4, 0, 4, 5, 0, 2, 0, 8, 6);
	public static final UnitClass architect = new UnitClass("Architect", 20, 4, 0, 4, 5, 0, 2, 0, 8, 6);
	
	public static int random0To99() {
		if (futureVision) {
			int ret = RNG.nextInt();
			RNG_STORE.add(ret);
			return ret;
		}
		if (!RNG_STORE.isEmpty()) {
			return RNG_STORE.remove(0);
		}
		return RNG.nextInt(100);
	}
	
	public static Chapter loadTestMission() throws FileNotFoundException {
		Map map = FEGemWarMapLoader.readMap("input/testmap.txt");
		Objective obj = new RoutObjective();
		Chapter chpt = new Chapter(map, obj);
		String rose_desc = "The leader of the Crystal Gem rebellion";
		Weapon rose_shield = new Armor("Rose Quartz Shield", 0, 3, 100, 5, 2, 1, 2, -1, 10);
		Unit rose = new Unit("Rose Quartz", lord, rose_desc,
				22, 4, 0, 2, 5, 6, 3, 0, 5, 6,
				70, 35, 10, 35, 40, 40, 25, 10,
				rose_shield, Weapon.SWORD, 0);
		roster.add(rose);
		chpt.getPlayerUnits().add(rose);
		
		String pearl_desc = "Rose Quartz's loyal companion";
		Weapon pearl_spear = new Lance("Pearl Spear", 0, 7, 70, 0, 9, 1, 2, -1);
		Unit pearl = new Unit("Pearl", servant, pearl_desc,
				20, 3, 1, 6, 10, 6, 2, 1, 4, 6,
				60, 35, 15, 55, 70, 60, 15, 15,
				pearl_spear, Weapon.SWORD, 0);
		roster.add(pearl);
		chpt.getPlayerUnits().add(pearl);
		
		for (int q = 0; q < 3; q++) {
			String quartz_desc = "Soldier serving the diamonds against the rebellion";
			Weapon quartz_axe = new Lance("Quartz Axe", 0, 10, 60, 0, 12, 1, 1, -1);
			Unit quartz = new Unit("Quartz", servant, quartz_desc,
					28, 7, 0, 7, 7, 2, 5, 0, 12, 6,
					80, 40, 5, 20, 30, 30, 30, 5,
					quartz_axe, Weapon.CLUB, 0);
			chpt.getEnemyUnits().add(quartz);
		}
		
		map.getTileAt(0, 0).setOccupant(rose);
		map.getTileAt(0, 1).setOccupant(pearl);
		
		for (int q = 0; q < chpt.getEnemyUnits().size(); q++) {
			Unit u = chpt.getEnemyUnits().get(q);
			map.getTileAt(map.getMap().length - 1, map.getMap()[0].length - q - 1).setOccupant(u);
		}
		
		return chpt;
	}
	
	public static Chapter loadChapter(int num) {
		if (num == 0) {
			try {
				Chapter ret = new Chapter(FEGemWarMapLoader.readMap("input/chapter1"), new EscapeObjective(2));
				ret.addEvent(new DialogueEvent(0, "dialogue/chapter1_preintro"));
				ret.addEvent(new DialogueEvent(0, "dialogue/chapter1_intro"));
				ret.addEvent(null);
				ret.addEvent(new DialogueEvent(1, "dialogue/chapter1_pearl"));
				ret.addEvent(new DialogueEvent(-1, "dialogue/chapter1_end"));
				loadChapter1Units(ret);
				ret.getDesign().put(TileType.FLOOR, "sky_arena_floor.jpg");
				ret.getDesign().put(TileType.RUBBLE, "sky_arena_rubble.jpg");
				ret.getDesign().put(TileType.PILLAR, "sky_arena_pillar.jpg");
				ret.getDesign().put(TileType.WARP_TILE, "warp_pad.jpg");
				return ret;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else if (num == 1) {
			try {
				Chapter ret = new Chapter(FEGemWarMapLoader.readMap("input/chapter1"), new EscapeObjective(2));
				ret.addEvent(new DialogueEvent(0, "dialogue/chapter2_preintro"));
				ret.addEvent(new DialogueEvent(0, "dialogue/chapter2_intro"));
				ret.addEvent(null);
				ret.addEvent(new DialogueEvent(-1, "dialogue/chapter2_end"));
				loadChapter1Units(ret);
				ret.getDesign().put(TileType.FLOOR, "sky_arena_floor.jpg");
				ret.getDesign().put(TileType.RUBBLE, "sky_arena_rubble.jpg");
				ret.getDesign().put(TileType.WARP_TILE, "warp_pad.jpg");
				return ret;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return null;
	}
	
	public static void loadChapter1Units(Chapter chpt) {
		//Based on Fergus (FE5)
		//10% STR growth buff
		String rose_desc = "The leader of the Crystal Gem rebellion";
		Weapon rose_shield = new Armor("Rose Quartz Shield", 0, 1, 100, 5, 2, 1, 2, -1, 10);
		Unit rose = new Unit("Rose Quartz", lord, rose_desc,
				26, 6, 5, 7, 7, 6, 5, 5, 8, 6,
				65, 45, 10, 45, 35, 40, 25, 10,
				rose_shield, Weapon.SWORD, 0);
		rose.setEssential(true);
		rose.setLeader(true);
		roster.add(rose);
		chpt.getPlayerUnits().add(rose);
		chpt.getMap().getTileAt(5, 1).setOccupant(rose);
		
		//Based on Machua (FE5)
		//2 point SPD nerf
		String pearl_desc = "Rose Quartz's loyal companion";
		Weapon pearl_spear = new Lance("Pearl Spear", 0, 7, 70, 0, 9, 1, 2, -1);
		Unit pearl = new Unit("Pearl", servant, pearl_desc,
				22, 4, 1, 10, 9, 6, 4, 1, 6, 6,
				60, 30, 10, 55, 60, 35, 25, 10,
				pearl_spear, Weapon.SWORD, 0);
		pearl.setEssential(true);
		roster.add(pearl);
		chpt.getPlayerUnits().add(pearl);
		chpt.getMap().getTileAt(6, 1).setOccupant(pearl);
		
		int[] coords = new int[] {0, 6, 0, 9, 2, 7, 3, 9, 9, 6, 8, 9, 6, 10};
		//TODO add variety
		Weapon[] weps = new Weapon[7];
		weps[0] = new Axe("Quartz Axe", 0, 10, 60, 0, 12, 1, 1, -1);
		weps[1] = new Axe("Quartz Axe", 0, 10, 60, 0, 12, 1, 1, -1);
		weps[2] = new Axe("Quartz Axe", 0, 10, 60, 0, 12, 1, 1, -1);
		weps[3] = new Axe("Quartz Axe", 0, 10, 60, 0, 12, 1, 1, -1);
		weps[4] = new Axe("Quartz Axe", 0, 10, 60, 0, 12, 1, 1, -1);
		weps[5] = new Axe("Quartz Axe", 0, 10, 60, 0, 12, 1, 1, -1);
		weps[6] = new Axe("Quartz Axe", 0, 10, 60, 0, 12, 1, 1, -1);
		for (int q = 0; q < 7; q++) {
			String quartz_desc = "Soldier serving the diamonds against the rebellion";
			Weapon weapon = weps[q];
			Unit quartz = new Unit("Quartz", soldier, quartz_desc,
					24, 8, 0, 3, 6, 2, 4, 0, 9, 6,
					80, 40, 5, 20, 30, 30, 30, 5,
					weapon, Weapon.FIST, 0);
			chpt.getEnemyUnits().add(quartz);
			chpt.getMap().getTileAt(coords[q * 2], coords[(q * 2) + 1]).setOccupant(quartz);;
			quartz.setAI(AIType.ATTACK, AIType.GUARD);
		}
		
		String biggs_desc = "A soldier fighting in the war";
		Weapon biggs_whip = new Whip("Jasper Whip", 0, 7, 70, 0, 9, 1, 2, -1);
		Unit biggs = new Unit("Biggs", soldier, biggs_desc,
				28, 7, 0, 7, 7, 2, 5, 0, 12, 6,
				80, 40, 5, 20, 30, 30, 30, 5,
				biggs_whip, Weapon.CLUB, 20);
		chpt.getEnemyUnits().add(biggs);
		chpt.getMap().getTileAt(4, 10).setOccupant(biggs);
		biggs.setTalkConvo(new DialogueEvent(0, "dialogue/chapter1_biggs"), true, null);
		biggs.setAI(AIType.ATTACK, AIType.GUARD);
		
		String ocean_desc = "A soldier fighting in the war";
		Weapon ocean_club = new Club("Jasper Mace", 0, 7, 70, 0, 9, 1, 1, -1);
		Unit ocean = new Unit("Ocean", soldier, ocean_desc,
				28, 7, 0, 7, 7, 2, 5, 0, 12, 6,
				80, 40, 5, 20, 30, 30, 30, 5,
				ocean_club, Weapon.WHIP, 20);
		chpt.getEnemyUnits().add(ocean);
		chpt.getMap().getTileAt(5, 10).setOccupant(ocean);
		ocean.setTalkConvo(new DialogueEvent(0, "dialogue/chapter1_ocean"), false, null);
		ocean.setAI(AIType.GUARD, AIType.GUARD);
		
		String bismuth_desc = "An architect given to Pink Diamond for the Earth colony";
		Weapon bismuth_hammer = new Axe("Bismuth Hammer", 0, 7, 70, 0, 9, 1, 1, -1);
		Weapon iron_sword = new Sword("Iron Sword", 0, 6, 70, 0, 6, 1, 1, 40);
		Unit bismuth = new Unit("Bismuth", architect, bismuth_desc,
				28, 7, 0, 7, 7, 2, 5, 0, 12, 6,
				80, 40, 5, 20, 30, 30, 30, 5,
				bismuth_hammer, Weapon.ARMOR, 0);
		chpt.getEnemyUnits().add(bismuth);
		chpt.getMap().getTileAt(1, 3).setOccupant(bismuth);
		bismuth.setTalkConvo(new DialogueEvent(0, "dialogue/chapter1_bismuth"), true, iron_sword);
	}
	
	public static HashMap<Tile, Integer> getTraversableTiles(Chapter chpt, Unit u, Tile start) {
		HashMap<Tile, Integer> traversable = new HashMap<>(800);
		LinkedQueue<int[]> searchList = new LinkedQueue<>(); //[0] = x, [1] = y, [2] = remainingMovement
		searchList.add(new int[] {start.getX(), start.getY(), u.getMovement()});
		int[] dimensions = new int[] {chpt.getMap().getMap().length, chpt.getMap().getMap()[0].length};
		while (!(searchList.isEmpty())) {
			int[] from = searchList.pop();
			Tile fromTile = chpt.getMap().getTileAt(from[0], from[1]);
			traversable.put(fromTile, from[2]);
			if (from[2] == 0) {
				continue;
			}
			if (from[0] > 0) {
				int checkX = from[0] - 1;
				int checkY = from[1];
				Tile check = chpt.getMap().getTileAt(checkX, checkY);
				if ((traversable.get(check) == null || traversable.get(check) < from[2])
						&& from[2] - check.getMoveCost(u) >= 0
						&& (check.isVacant()
								|| chpt.getEnemyUnits().contains(check.getOccupant()) == chpt.getEnemyUnits().contains(u))) {
					searchList.add(new int[] {checkX, checkY, from[2] - check.getMoveCost(u)});
				}
			}
			if (from[0] < dimensions[0] - 1) {
				int checkX = from[0] + 1;
				int checkY = from[1];
				Tile check = chpt.getMap().getTileAt(checkX, checkY);
				if ((traversable.get(check) == null || traversable.get(check) < from[2])
						&& from[2] - check.getMoveCost(u) >= 0
						&& (check.isVacant()
								|| chpt.getEnemyUnits().contains(check.getOccupant()) == chpt.getEnemyUnits().contains(u))) {
					searchList.add(new int[] {checkX, checkY, from[2] - check.getMoveCost(u)});
				}
			}
			if (from[1] > 0) {
				int checkX = from[0];
				int checkY = from[1] - 1;
				Tile check = chpt.getMap().getTileAt(checkX, checkY);
				if ((traversable.get(check) == null || traversable.get(check) < from[2])
						&& from[2] - check.getMoveCost(u) >= 0
						&& (check.isVacant()
								|| chpt.getEnemyUnits().contains(check.getOccupant()) == chpt.getEnemyUnits().contains(u))) {
					searchList.add(new int[] {checkX, checkY, from[2] - check.getMoveCost(u)});
				}
			}
			if (from[1] < dimensions[1] - 1) {
				int checkX = from[0];
				int checkY = from[1] + 1;
				Tile check = chpt.getMap().getTileAt(checkX, checkY);
				if ((traversable.get(check) == null || traversable.get(check) < from[2])
						&& from[2] - check.getMoveCost(u) >= 0
						&& (check.isVacant()
								|| chpt.getEnemyUnits().contains(check.getOccupant()) == chpt.getEnemyUnits().contains(u))) {
					searchList.add(new int[] {checkX, checkY, from[2] - check.getMoveCost(u)});
				}
			}
		}
		return traversable;

	}

	public static HashMap<Tile, Integer> getAttackableTiles(HashMap<Tile, Integer> traversable, Unit selected,
			Chapter chpt) {
		HashMap<Tile, Integer> ret = new HashMap<>();
		Set<Tile> keys = traversable.keySet();
		for (Tile t : keys) {
			HashMap<Tile, Integer> att = getAttackableBattlegroundTilesFromDestination(
					chpt, selected, t);
			Set<Tile> check = att.keySet();
			for (Tile c : check) {
				if (ret.get(c) == null || ret.get(c) > att.get(c)) {
					ret.put(c, att.get(c));
				}
			}
		}
		return ret;
	}
	
	public static HashMap<Tile, Integer> getAttackableBattlegroundTilesFromDestination(
			Chapter chpt, Unit u, Tile dest) {
		int x = dest.getX();
		int y = dest.getY();
		int minRange = 1;
		int maxRange = 1;
		Weapon w = u.getEquippedWeapon();
		if (w != null) {
			minRange = w.getMinRange();
			maxRange = w.getMaxRange();
		}
		//TODO find actual range
		HashMap<Tile, Integer> traversable = new HashMap<>(800);
		HashMap<Tile, Integer> attackable = new HashMap<>(800); //Gives distance from attacker for each target
		LinkedQueue<int[]> searchList = new LinkedQueue<>(); //[0] = x, [1] = y, [2] = remainingMovement
		searchList.add(new int[] {x, y, maxRange});
		int[] dimensions = new int[] {chpt.getMap().getMap().length, chpt.getMap().getMap()[0].length};
		while (!(searchList.isEmpty())) {
			int[] from = searchList.pop();
			Tile fromTile = chpt.getMap().getTileAt(from[0], from[1]);
			traversable.put(fromTile, from[2]);
			int distance = maxRange - from[2];
			if (distance >= minRange
					&& (fromTile.getOccupant() == null ||
					chpt.getEnemyUnits().contains(fromTile.getOccupant()) != chpt.getEnemyUnits().contains(u))) {
				attackable.put(fromTile, distance);
			}
			if (from[2] == 0) {
				continue;
			}
			if (from[0] > 0) {
				int checkX = from[0] - 1;
				int checkY = from[1];
				Tile check = chpt.getMap().getTileAt(checkX, checkY);
				if ((traversable.get(check) == null || traversable.get(check) < from[2])
						&& from[2] - 1 >= 0) {
					searchList.add(new int[] {checkX, checkY, from[2] - 1});
				}
			}
			if (from[0] < dimensions[0] - 1) {
				int checkX = from[0] + 1;
				int checkY = from[1];
				Tile check = chpt.getMap().getTileAt(checkX, checkY);
				if ((traversable.get(check) == null || traversable.get(check) < from[2])
						&& from[2] - 1 >= 0) {
					searchList.add(new int[] {checkX, checkY, from[2] - 1});
				}
			}
			if (from[1] > 0) {
				int checkX = from[0];
				int checkY = from[1] - 1;
				Tile check = chpt.getMap().getTileAt(checkX, checkY);
				if ((traversable.get(check) == null || traversable.get(check) < from[2])
						&& from[2] - 1 >= 0) {
					searchList.add(new int[] {checkX, checkY, from[2] - 1});
				}
			}
			if (from[1] < dimensions[1] - 1) {
				int checkX = from[0];
				int checkY = from[1] + 1;
				Tile check = chpt.getMap().getTileAt(checkX, checkY);
				if ((traversable.get(check) == null || traversable.get(check) < from[2])
						&& from[2] - 1 >= 0) {
					searchList.add(new int[] {checkX, checkY, from[2] - 1});
				}
			}
		}
		return attackable;
	}

	public static List<Tile> getAttackableTilesWithEnemies(HashMap<Tile, Integer> attackable, Chapter chpt,
			Unit u) {
		// TODO Auto-generated method stub
		List<Tile> ret = new List<>();
		Set<Tile> keys = attackable.keySet();
		for (Tile t : keys) {
			if (t.getOccupant() != null) {
				ret.add(t);
			}
		}
		return ret;
	}

	public static void moveUnit(Unit u, Tile start, Tile dest) {
		start.setOccupant(null);
		dest.setOccupant(u);
		u.exhaust();
	}
	public static void killUnit(Chapter chpt, Unit dead, Tile tile) {
		chpt.getPlayerUnits().remove(dead);
		chpt.getEnemyUnits().remove(dead);
		chpt.getAllyUnits().remove(dead);
		tile.setOccupant(null);
	}


	public static void escapeUnit(Chapter chpt, Tile tile, Unit unit) {
		chpt.getPlayerUnits().remove(unit);
		chpt.getEscapedUnits().add(unit);
		tile.setOccupant(null);
	}
	public static void unexhaustAll(List<Unit> units) {
		for (int q = 0; q < units.size(); q++) {
			units.get(q).unExhaust();
		}
	}

	public static List<Tile> getTalkableTiles(Tile dest, Chapter chpt, Unit selected) {
		List<Tile> ret = new List<>();
		int x = dest.getX();
		int y = dest.getY();
		Map map = chpt.getMap();
		if (x > 0) {
			Tile t = map.getTileAt(x - 1, y);
			Unit u = t.getOccupant();
			if (u != null && u.getTalkConvo() != null
					&& (!u.talkRestricted() || selected == chpt.getPlayerUnits().get(0))) {
				ret.add(t);
			}
		}
		if (x < map.getMap().length - 1) {
			Tile t = map.getTileAt(x + 1, y);
			Unit u = t.getOccupant();
			if (u != null && u.getTalkConvo() != null
					&& (!u.talkRestricted() || selected == chpt.getPlayerUnits().get(0))) {
				ret.add(t);
			}
		}
		if (y > 0) {
			Tile t = map.getTileAt(x, y - 1);
			Unit u = t.getOccupant();
			if (u != null && u.getTalkConvo() != null
					&& (!u.talkRestricted() || selected == chpt.getPlayerUnits().get(0))) {
				ret.add(t);
			}
		}
		if (y < map.getMap()[0].length - 1) {
			Tile t = map.getTileAt(x, y + 1);
			Unit u = t.getOccupant();
			if (u != null && u.getTalkConvo() != null
					&& (!u.talkRestricted() || selected == chpt.getPlayerUnits().get(0))) {
				ret.add(t);
			}
		}
		return ret;
	}

	public static Object[] performUnitAI(Unit u, Chapter chpt) {
		int[] startCoords = findUnit(u, chpt);
		//TODO fix the bug where findUnit() sometimes returns null
		if (startCoords == null) {
			return null;
		}
		List<Tile> target = testAISuccess(u, chpt, startCoords, u.getAI1());
		if (target.isEmpty()) {
			target = testAISuccess(u, chpt, startCoords, u.getAI2());
			if (target.isEmpty()) {
				return null;
			}
			Object[] ret = new Object[4];
			ret[0] = u.getAI2();
			actOnUnitAI(u, chpt, startCoords, u.getAI2(), target, ret);
			return ret;
		}
		Object[] ret = new Object[4];
		ret[0] = u.getAI1();
		actOnUnitAI(u, chpt, startCoords, u.getAI1(), target, ret);
		return ret;
	}
	public static int[] findUnit(Unit u, Chapter chpt) {
		Tile[][] map = chpt.getMap().getMap();
		for (int q = 0; q < map.length; q++) {
			for (int w = 0; w < map[0].length; w++) {
				if (map[q][w].getOccupant() == u) {
					return new int[] {q, w};
				}
			}
		}
//		System.out.println("Didn't find");
		return null;
	}
	public static List<Tile> testAISuccess(Unit u, Chapter chpt, int[] start, Unit.AIType ai) {
		List<Tile> ret = new List<>();
		Tile[][] map = chpt.getMap().getMap();
		Tile startTile = map[start[0]][start[1]];
		if (ai == AIType.ATTACK) {
			HashMap<Tile, Integer> traversable = getTraversableTiles(chpt, u, startTile);
			Set<Tile> dests = traversable.keySet();
			for (Tile dest : dests) {
				if (dest.isVacant()) {
					HashMap<Tile, Integer> att = getAttackableBattlegroundTilesFromDestination(chpt, u, dest);
					List<Tile> realAtt = getAttackableTilesWithEnemies(att, chpt, u);
					if (!realAtt.isEmpty()) {
						ret.add(dest);
					}
				}
			}
		} else if (ai == AIType.BURN) {
			//TODO if there is a path to a house, add that house's tile
		} else if (ai == AIType.GUARD) {
			HashMap<Tile, Integer> attackable = getAttackableBattlegroundTilesFromDestination(chpt, u, startTile);
			List<Tile> enemyTiles = getAttackableTilesWithEnemies(attackable, chpt, u);
			ret.addAll(enemyTiles);
		} else if (ai == AIType.PURSUE) {
			//TODO if there is a path to an enemy, add that enemy's tile
		}
		
		return ret;
	}
	private static void actOnUnitAI(Unit u, Chapter chpt, int[] start, AIType ai,
			List<Tile> target, Object[] report) {
		Tile[][] map = chpt.getMap().getMap();
		Tile startTile = map[start[0]][start[1]];
		if (ai == AIType.ATTACK) {
			//TODO
			int heur = Integer.MIN_VALUE;
			Tile bestDest = null;
			Tile best = null;
			for (int q = 0; q < target.size(); q++) {
				Tile dest = target.get(q);
				HashMap<Tile, Integer> att = getAttackableBattlegroundTilesFromDestination(chpt, u, dest);
				List<Tile> enemTiles = getAttackableTilesWithEnemies(att, chpt, u);
				for (int r = 0; r < enemTiles.size(); r++) {
					Tile dfdTile = enemTiles.get(r);
					int specialHeur = Integer.MIN_VALUE;
					int heldHeur = Integer.MIN_VALUE;
					Unit enemy = dfdTile.getOccupant();
					int dist = Math.abs(dfdTile.getX() - dest.getX()) + Math.abs(dfdTile.getY() - dest.getY());
					if (u.getSpecialItem() instanceof Weapon) {
						Weapon w = (Weapon)u.getSpecialItem();
						if (w.getMaxRange() >= dist && dist <= w.getMinRange()) {
							specialHeur = 0;
							int[] forecast = CombatManager.getBattleForecast(u, enemy, w,
									enemy.getEquippedWeapon(), dest, dfdTile);
							if (forecast[1] * forecast[5] >= forecast[6]) {
								specialHeur += 50;
							} else {
								int bonus = (int)Math.round((forecast[1] * forecast[2]) / 100.0);
								specialHeur += Math.min(40, bonus);
							}
							specialHeur += Math.max(0, 20 - forecast[6]);
							if (forecast[10] == 0) {
								specialHeur += 10;
							} else {
								int penalty = (int)Math.round((forecast[7] * forecast[8]) / 100.0);
								specialHeur -= Math.min(40, penalty);
							}
							specialHeur -= Math.max(0, 20 - (forecast[0] - forecast[7]));
						}
					}
					if (u.getHeldItem() instanceof Weapon) {
						Weapon w = (Weapon)u.getHeldItem();
						if (w.getMaxRange() >= dist && dist <= w.getMinRange()) {
							heldHeur = 0;
							int[] forecast = CombatManager.getBattleForecast(u, enemy, w,
									enemy.getEquippedWeapon(), dest, dfdTile);
							if (forecast[1] * forecast[5] >= forecast[6]) {
								heldHeur += 50;
							} else {
								int bonus = (int)Math.round((forecast[1] * forecast[2]) / 100.0);
								heldHeur += Math.min(40, bonus);
							}
							heldHeur += Math.max(0, 20 - forecast[6]);
							if (forecast[10] == 0) {
								heldHeur += 10;
							} else {
								int penalty = (int)Math.round((forecast[7] * forecast[8]) / 100.0);
								heldHeur -= Math.min(40, penalty);
							}
							heldHeur -= Math.max(0, 20 - (forecast[0] - forecast[7]));
						}
					}
					if (specialHeur > heur) {
						heur = specialHeur;
						best = dfdTile;
						u.equipSpecial();
						bestDest = dest;
					}
					if (heldHeur > heur) {
						heur = heldHeur;
						best = dfdTile;
						u.equipHeld();
						bestDest = dest;
					}
				}
			}
//			System.out.println(startTile == null);
//			System.out.println(bestDest == null);
//			System.out.println(best == null);
			report[1] = startTile;
			report[2] = bestDest;
			report[3] = best;
		} else if (ai == AIType.BURN) {
			//TODO move closer to the house or burn it if possible
		} else if (ai == AIType.GUARD) {
			int heur = Integer.MIN_VALUE;
			int best = 0;
			for (int q = 0; q < target.size(); q++) {
				Unit enemy = target.get(q).getOccupant();
				Weapon specialWep = null;
				Weapon heldWep = null;
				int specialHeur = 0;
				int heldHeur = 0;
				if (u.getSpecialItem() instanceof Weapon) {
					specialWep = (Weapon)u.getSpecialItem();
				}
				if (u.getHeldItem() instanceof Weapon) {
					heldWep = (Weapon)u.getHeldItem();
				}
				int[] w1Forecast = CombatManager.getBattleForecast(u, enemy, specialWep,
						enemy.getEquippedWeapon(), startTile, target.get(q));
				int[] w2Forecast = CombatManager.getBattleForecast(u, enemy, heldWep,
						enemy.getEquippedWeapon(), startTile, target.get(q));
				if (w1Forecast[1] * w1Forecast[5] >= w1Forecast[6]) {
					specialHeur += 50;
				} else {
					int bonus = (int)Math.round((w1Forecast[1] * w1Forecast[2]) / 100.0);
					specialHeur += Math.min(40, bonus);
				}
				if (w2Forecast[1] * w2Forecast[4] * w2Forecast[5] >= w2Forecast[6]) {
					heldHeur += 50;
				} else {
					int bonus = (int)Math.round((w2Forecast[1] * w2Forecast[2]) / 100.0);
					heldHeur += Math.min(40, bonus);
				}
				specialHeur += Math.max(0, 20 - w1Forecast[6]);
				heldHeur += Math.max(0, 20 - w2Forecast[6]);
				if (w1Forecast[10] == 0) {
					specialHeur += 10;
				} else {
					int penalty = (int)Math.round((w1Forecast[7] * w1Forecast[8]) / 100.0);
					specialHeur -= Math.min(40, penalty);
				}
				if (w2Forecast[10] == 0) {
					heldHeur += 10;
				} else {
					int penalty = (int)Math.round((w2Forecast[7] * w2Forecast[8]) / 100.0);
					heldHeur -= Math.min(40, penalty);
				}
				specialHeur -= Math.max(0, 20 - (w1Forecast[0] - w1Forecast[7]));
				heldHeur -= Math.max(0, 20 - (w2Forecast[0] - w2Forecast[7]));
				
				if (specialHeur > heur) {
					heur = specialHeur;
					best = q;
					if (specialWep != null) {
						u.equipSpecial();
					} else {
						u.equipNone();
					}
				}
				if (heldHeur > heur) {
					heur = heldHeur;
					best = q;
					if (heldWep != null) {
						u.equipHeld();
					} else {
						u.equipNone();
					}
				}
			}
			Tile enemyTile = target.get(best);
			report[1] = startTile;
			report[2] = enemyTile;
		} else if (ai == AIType.PURSUE) {
			//TODO move closer to the enemy or attack if possible
		}
	}
}
