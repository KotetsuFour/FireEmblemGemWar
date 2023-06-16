package inventory.weapon;

import inventory.Item;
import unit.Unit;

public abstract class Weapon implements Item {

	protected String name;
	protected int proficiency;
	protected int might;
	protected int hit;
	protected int crit;
	protected int weight;
	protected int minRange;
	protected int maxRange;
	protected int uses;
	protected int usesLeft;
	protected boolean magic;
	protected boolean brave;
	
	public static int SWORD = 0;
	public static int LANCE = 1;
	public static int AXE = 2;
	public static int ARMOR = 3;
	public static int BOW = 4;
	public static int CLUB = 5;
	public static int FIST = 6;
	public static int SPECIAL = 7;
	public static int WHIP = 8;
	
	
	public Weapon(String name, int proficiency, int might, int hit, int crit, int weight, int minRange, int maxRange, int uses) {
		this.name = name;
		this.proficiency = proficiency;
		this.might = might;
		this.hit = hit;
		this.crit = crit;
		this.weight = weight;
		this.minRange = minRange;
		this.maxRange = maxRange;
		this.uses = uses;
		this.usesLeft = uses;
	}
	public abstract boolean isEffectiveAgainst(Unit u);
	public abstract boolean isAdvantageousAgainst(Weapon w);
	public abstract Weapon clone();
	
	@Override
	public String getName() {
		return name;
	}
	public int getProficiency() {
		return proficiency;
	}
	public int getMight() {
		return might;
	}
	public int getHit() {
		return hit;
	}
	public int getCrit() {
		return crit;
	}
	public int getWeight() {
		return weight;
	}
	public int getMinRange() {
		return minRange;
	}
	public int getMaxRange() {
		return maxRange;
	}
	public int getUses() {
		return uses;
	}
	public int getUsesLeft() {
		return usesLeft;
	}
	public boolean isMagic() {
		return magic;
	}
	public void setMagic(boolean magic) {
		this.magic = magic;
	}
	public void setBrave(boolean brave) {
		this.brave = brave;
	}
	public boolean isBrave() {
		return brave;
	}
	public abstract int typeId();
	
}
