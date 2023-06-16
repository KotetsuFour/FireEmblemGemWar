package map;

public interface Objective {

	public boolean checkComplete(Chapter chpt);
	
	public boolean checkFailed(Chapter chpt);
	
	public String getName();
}
