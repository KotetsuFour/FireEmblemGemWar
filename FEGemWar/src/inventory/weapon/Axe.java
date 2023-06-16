package inventory.weapon;

import unit.Unit;

public class Axe extends AxeAndWhip {
	
	public Axe(String name, int proficiency, int might, int hit, int crit, int weight, int minRange, int maxRange, int uses) {
		super(name, proficiency, might, hit, crit, weight, minRange, maxRange, uses);
	}

	@Override
	public boolean isEffectiveAgainst(Unit u) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Weapon clone() {
		return new Axe(name, proficiency, might, hit, crit, weight, minRange, maxRange, uses);
	}

	@Override
	public int typeId() {
		return AXE;
	}

}
