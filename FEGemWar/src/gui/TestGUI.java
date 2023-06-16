package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.InputMismatchException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import manager.FEGemWarManager;
import map.Chapter;
import map.Tile;

public class TestGUI extends JFrame {

	/**Obligatory serial version*/
	private static final long serialVersionUID = 1L;

	private JPanel p;

	private CardLayout cl;
	
	public static final String VIEW = "View";
	
	public TestGUI() {

		p = new JPanel();
		cl = new CardLayout();
		p.setLayout(cl);
		
		//Window formatting
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    setSize(900, 700);
	    setTitle("Fire Emblem: Gem War (Test GUI)");

	    p.add(new IntroPanel(), "Intro");	    
	    cl.show(p, "Intro");
	    getContentPane().add(p, BorderLayout.CENTER);

	    setVisible(true);
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		TestGUI hg = new TestGUI();
	}
	
	private void switchToPanel(JPanel panel, String name) {
		p.add(panel, name);
		cl.show(p, name);
		validate();
		repaint();
	}	
	private String validateInputName(String s, String field) {
		//Assume the string is not null or empty, because that just means a random name
		//should be used
		if (s == null || s.equals("")) {
			throw new IllegalArgumentException("Input for " + field + " cannot be null or empty");
		}
		s.trim();
//			if (s.length() > 24) {
//				throw new IllegalArgumentException("Too long name for " + field);
//			}
		if (s.length() < 1) {
			throw new IllegalArgumentException("Input for " + field + " cannot just be whitespace");
		}
			for (int q = 0; q < s.length(); q++) {
				char c = s.charAt(q);
				if (!Character.isLetter(c)
						&& !Character.isDigit(c)
						&& c != ' '
						&& c != '-'
						&& c != '_'
						&& c != '.'
						) {
					throw new IllegalArgumentException(field + " contains an illegal character");
				}
			}
		return s;
	}
	private int parseValidDigitWithinBounds(String s, int min, int max, String field)
			throws IllegalArgumentException {
		s.trim();
		try {
			int num = Integer.parseInt(s);
			if (num >= min && num <= max) {
				return num;
			}
		} catch (InputMismatchException e) {
			throw new IllegalArgumentException("Entered a non-integer for " + field);
		}
		throw new IllegalArgumentException("Entered an out-of-bounds value for " + field);
	}

	private class IntroPanel extends JPanel {
		public IntroPanel() {
			Chapter chpt = null;
			try {
				chpt = FEGemWarManager.loadTestMission();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}
			Tile[][] map = chpt.getMap().getMap();
			
			setLayout(new GridLayout(map.length, map[0].length));
			for (int q = 0; q < map.length; q++) {
				for (int w = 0; w < map[0].length; w++) {
					Tile t = map[q][w];
					JButton button = new JButton();
					if (t.getOccupant() != null) {
						if (chpt.getPlayerUnits().contains(t.getOccupant())) {
							button.setBackground(Color.BLUE);
						} else if (chpt.getEnemyUnits().contains(t.getOccupant())) {
							button.setBackground(Color.RED);
						}
					}
					add(button);
				}
			}
		}
	}

}
