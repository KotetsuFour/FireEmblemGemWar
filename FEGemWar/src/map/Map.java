package map;

public class Map {

	private Tile[][] tileMap;
	private int turn;
	
	public Map(Tile[][] tileMap) {
		this.tileMap = tileMap;
	}
	
	public Tile[][] getMap() {
		return tileMap;
	}
	
	public Tile getTileAt(int x, int y) {
		return tileMap[x][y];
	}
	
	public int getTurn() {
		return turn;
	}
	public void incrementTurn() {
		turn++;
	}
}
