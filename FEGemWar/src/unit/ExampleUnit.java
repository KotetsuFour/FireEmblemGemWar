package unit;

import inventory.Item;

public interface ExampleUnit extends UnitInterface {

	int getStrength();
	int getMagic();
	int getSkill();
	int getSpeed();
	int getLuck();
	int getDefense();
	int getResistance();
	void giveItem(Item i);
	
	public enum WeaponLevel {
		E,D,C,B,A,S
	}
}
