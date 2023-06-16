package inventory.weapon;

import unit.Unit;

public class Whip extends AxeAndWhip {
	
	public Whip(String name, int proficiency, int might, int hit, int crit, int weight, int minRange, int maxRange, int uses) {
		super(name, proficiency, might, hit, crit, weight, minRange, maxRange, uses);
	}

	@Override
	public boolean isEffectiveAgainst(Unit u) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Weapon clone() {
		return new Whip(name, proficiency, might, hit, crit, weight, minRange, maxRange, uses);
	}

	@Override
	public int typeId() {
		return WHIP;
	}

}
