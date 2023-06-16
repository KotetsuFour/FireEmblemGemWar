package inventory.weapon;

import unit.Unit;

public class Club extends Weapon {

	public Club(String name, int proficiency, int might, int hit, int crit, int weight, int minRange, int maxRange,
			int uses) {
		super(name, proficiency, might, hit, crit, weight, minRange, maxRange, uses);
		// TODO Auto-generated constructor stub
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
		return new Club(name, proficiency, might, hit, crit, weight, minRange, maxRange, uses);
	}

	@Override
	public int typeId() {
		return CLUB;
	}

}
