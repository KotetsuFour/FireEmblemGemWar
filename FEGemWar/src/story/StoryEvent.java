package story;

public abstract class StoryEvent {

	private int turn; //The turn that the event activates on
	
	public StoryEvent(int turn) {
		this.turn = turn;
	}
	
	public int getTurn() {
		return turn;
	}
}
