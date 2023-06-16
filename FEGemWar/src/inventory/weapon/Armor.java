package inventory.weapon;

import unit.Unit;

public class Armor extends LanceAndArmor {
	
	private int protection;
	
	public Armor(String name, int proficiency, int might, int hit, int crit, int weight,
			int minRange, int maxRange, int uses, int protection) {
		super(name, proficiency, might, hit, crit, weight, minRange, maxRange, uses);
		this.protection = protection;
	}

	@Override
	public boolean isEffectiveAgainst(Unit u) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public int getProtection() {
		return protection;
	}

	@Override
	public Weapon clone() {
		return new Armor(name, proficiency, might, hit, crit, weight,
				minRange, maxRange, uses, protection);
	}

	@Override
	public int typeId() {
		return ARMOR;
	}

}
