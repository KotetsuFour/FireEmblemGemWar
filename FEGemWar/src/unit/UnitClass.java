package unit;

public class UnitClass {

	private String name;
	private UnitClass promotion;
	
	private int minMaxHP;
	private int minStrength;
	private int minMagic;
	private int minSkill;
	private int minSpeed;
	private int minLuck;
	private int minDefense;
	private int minResistance;
	private int minConstitution;
	private int minMovement;
	
	private boolean flying;
	
	public UnitClass(String name, int minMaxHP, int minStrength, int minMagic, int minSkill,
			int minSpeed, int minLuck, int minDefense, int minResistance, int minConstitution,
			int minMovement) {
		this.name = name;
		this.minMaxHP = minMaxHP;
		this.minStrength = minStrength;
		this.minMagic = minMagic;
		this.minSkill = minSkill;
		this.minSpeed = minSpeed;
		this.minLuck = minLuck;
		this.minDefense = minDefense;
		this.minResistance = minResistance;
		this.minConstitution = minConstitution;
		this.minMovement = minMovement;
	}

	public UnitClass getPromotion() {
		return promotion;
	}
	public void setPromotion(UnitClass promo) {
		this.promotion = promo;
	}

	public String getName() {
		return name;
	}

	public int getMinMaxHP() {
		return minMaxHP;
	}

	public int getMinStrength() {
		return minStrength;
	}
	
	public int getMinMagic() {
		return minMagic;
	}

	public int getMinSkill() {
		return minSkill;
	}

	public int getMinSpeed() {
		return minSpeed;
	}

	public int getMinLuck() {
		return minLuck;
	}

	public int getMinDefense() {
		return minDefense;
	}

	public int getMinResistance() {
		return minResistance;
	}

	public int getMinConstitution() {
		return minConstitution;
	}

	public int getMinMovement() {
		return minMovement;
	}
	
	public void setFlying(boolean flying) {
		this.flying = flying;
	}
	public boolean isFlying() {
		return flying;
	}

	
}
