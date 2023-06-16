package manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import data_structures.List;
import map.Map;
import map.Tile;
import map.Tile.TileType;

public class FEGemWarMapLoader {

	public static Map readMap(String filename) throws FileNotFoundException {
		Scanner scan = new Scanner(new File(filename));
		List<List<Tile>> mapLists = new List<>();
		int idx1 = 0;
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			List<Tile> list = new List<>();
			mapLists.add(list);
			for (int q = 0; q < line.length(); q++) {
				char c = line.charAt(q);
//				GRASS("Grass", 1, 1, 0),
				if (c == '+') {
					list.add(new Tile(TileType.GRASS, idx1, q));
				}
//				SAND("Sand", 2, 1, 5),
				else if (c == 'S') {
					list.add(new Tile(TileType.SAND, idx1, q));
				}
//				TREE("Tree", 2, 1, 20),
				else if (c == 'T') {
					list.add(new Tile(TileType.TREE, idx1, q));
				}
//				THICKET("Thicket", 6, 1, 40),
				else if (c == 'h') {
					list.add(new Tile(TileType.THICKET, idx1, q));
				}
//				MOUNTAIN("Mountain", 4, 1, 30),
				else if (c == '^') {
					list.add(new Tile(TileType.MOUNTAIN, idx1, q));
				}
//				PEAK("Peak", 4, 1, 40),
				else if (c == 'P') {
					list.add(new Tile(TileType.PEAK, idx1, q));
				}
//				HOUSE("House", 1, 1, 10),
				else if (c == 'H') {
					list.add(new Tile(TileType.HOUSE, idx1, q));
				}
//				HOUSE_DOOR("Door", 1, 1, 10), //Part of a house that can be interacted with
				else if (c == 'd') {
					list.add(new Tile(TileType.HOUSE_DOOR, idx1, q));
				}
//				RUBBLE("Rubble", 2, 1, 0),
				else if (c == 'R') {
					list.add(new Tile(TileType.RUBBLE, idx1, q));
				}
//				PILLAR("Pillar", 2, 6, 20),
				else if (c == '|') {
					list.add(new Tile(TileType.PILLAR, idx1, q));
				}
//				GATE("Gate", 1, 1, 20),
				else if (c == '#') {
					list.add(new Tile(TileType.GATE, idx1, q));
				}
//				WALL("Wall", Integer.MAX_VALUE, 1, 0),
				else if (c == 'W') {
					list.add(new Tile(TileType.WALL, idx1, q));
				}
//				SHALLOW_WATER("Shallow Water", 3, 1, 10),
				else if (c == 'w') {
					list.add(new Tile(TileType.SHALLOW_WATER, idx1, q));
				}
//				DEEP_WATER("Deep Water", Integer.MAX_VALUE, 1, 10),
				else if (c == '~') {
					list.add(new Tile(TileType.DEEP_WATER, idx1, q));
				}
//				CAVE("Cave", 1, 6, 10),
				else if (c == 'C') {
					list.add(new Tile(TileType.CAVE, idx1, q));
				}
//				MAGMA("Molten Rock", 3, 3, 10),
				else if (c == 'M') {
					list.add(new Tile(TileType.MAGMA, idx1, q));
				}
//				FLOOR("Floor", 1, 5, 0),
				else if (c == '_') {
					list.add(new Tile(TileType.FLOOR, idx1, q));
				}
//				THRONE("Throne", 1, 5, 30),
				else if (c == 'T') {
					list.add(new Tile(TileType.THRONE, idx1, q));
				}
//				CHEST("Chest", 1, 5, 0),
				else if (c == 'e') {
					list.add(new Tile(TileType.CHEST, idx1, q));
				}
//				WETLAND("Wetland", 2, 1, 5),
				else if (c == 't') {
					list.add(new Tile(TileType.WETLAND, idx1, q));
				}
//				WASTELAND("Wasteland", 1, 1, 0),
				else if (c == 's') {
					list.add(new Tile(TileType.WASTELAND, idx1, q));
				}
//				ROAD("Road", 1, 1, 0),
				else if (c == '=') {
					list.add(new Tile(TileType.ROAD, idx1, q));
				}
//				WARP_TILE("Warp Tile", 2, 3, 0),
				else if (c == 'r') {
					list.add(new Tile(TileType.WARP_TILE, idx1, q));
				}
//				DECK("Deck", 1, 1, 0),
				else if (c == 'k') {
					list.add(new Tile(TileType.DECK, idx1, q));
				}
//				DOCK("Dock", 1, 1, 5),
				else if (c == '-') {
					list.add(new Tile(TileType.DOCK, idx1, q));
				}
//				SNOW("Snow", 2, 1, 5),
				else if (c == '*') {
					list.add(new Tile(TileType.SNOW, idx1, q));
				}
			}
			idx1++;
		}
		Tile[][] tileMap = new Tile[mapLists.get(0).size()][mapLists.size()];
		for (int q = 0; q < tileMap.length; q++) {
			for (int w = 0; w < tileMap[0].length; w++) {
				tileMap[w][q] = mapLists.get(q).get(w);
				tileMap[w][q].reverseCoords();
			}
		}
		Map ret = new Map(tileMap);
		scan.close();
		
		return ret;
	}
}
