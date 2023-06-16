package inventory.weapon;

import unit.Unit;

public abstract class LanceAndArmor extends Weapon {

	public LanceAndArmor(String name, int proficiency, int might, int hit, int crit, int weight, int minRange, int maxRange, int uses) {
		super(name, proficiency, might, hit, crit, weight, minRange, maxRange, uses);
	}

	@Override
	public boolean isAdvantageousAgainst(Weapon w) {
		return w instanceof SwordAndFist;
	}

}
