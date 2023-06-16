package inventory.weapon;

import unit.Unit;

public class SpecialWeapon extends Weapon {

	public SpecialWeapon(String name, int proficiency, int might, int hit, int crit, int weight, int minRange,
			int maxRange, int uses) {
		super(name, proficiency, might, hit, crit, weight, minRange, maxRange, uses);
	}

	@Override
	public boolean isEffectiveAgainst(Unit u) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAdvantageousAgainst(Weapon w) {
		return false;
	}

	@Override
	public Weapon clone() {
		return new SpecialWeapon(name, proficiency, might, hit, crit, weight, minRange, maxRange, uses);
	}

	@Override
	public int typeId() {
		return SPECIAL;
	}

}
