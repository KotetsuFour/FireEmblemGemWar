package story;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import data_structures.List;

public class DialogueEvent extends StoryEvent {

	private String filename;
	private Scanner scan;
	private List<List<String>> rets;
	private int idx;
	
	public DialogueEvent(int turn, String filename) {
		super(turn);
		this.filename = filename;
		this.rets = new List<>();
		this.idx = 0;
	}
	
	public void readDialogue() throws FileNotFoundException {
		scan = new Scanner(new File(filename));
		while (scan.hasNextLine()) {
			List<String> ret = new List<>();
			while (scan.hasNextInt()) {
				ret.add(scan.nextLine());
			}
			ret.add(scan.nextLine());
			ret.add(scan.nextLine());
			rets.add(ret);
		}
		scan.close();
	}
	/*
	 * For lines beginning with integers, the integer represents
	 * the position of the picture to change
	 * (0 = background, 1 = near left, 2 = near right, 3 = far left, 4 = far right, 5 = center)
	 * After this integer is the filename of the picture to change to
	 * 
	 * After those lines is one line describing the speaker
	 * Then one line with the dialogue. Use wrap-text to split it up
	 */
	public List<String> nextDialogueComponent() {
		if (idx >= rets.size()) {
			return null;
		}
		List<String> ret = rets.get(idx);
		idx++;
		return ret;
	}
}
