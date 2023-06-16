package manager.combat;

import inventory.weapon.Weapon;
import unit.Unit;

public class CombatReport {

	private Unit attacker;
	
	private Unit defender;
	
	private Weapon atkWeapon;
	
	private Weapon dfdWeapon;
	
	//[0] = Might
	//[1] = Hit
	//[2] = Critical
	//[3] = Number of Attacks
	private int[] atkStats;
	
	private int[] dfdStats;
	
	private boolean atkEffective;
	
	private boolean dfdEffective;
	
	//a for attacker advantage
	//d for defender advantage
	//n for neither
	private char advantage;

	public CombatReport(Unit attacker, Unit defender, Weapon atkWeapon, Weapon dfdWeapon, int[] atkStats,
			int[] dfdStats) {
		super();
		this.attacker = attacker;
		this.defender = defender;
		this.atkWeapon = atkWeapon;
		this.dfdWeapon = dfdWeapon;
		this.atkStats = atkStats;
		this.dfdStats = dfdStats;
		this.atkEffective = atkWeapon.isEffectiveAgainst(defender);
		this.dfdEffective = dfdWeapon.isEffectiveAgainst(attacker);
		if (atkWeapon.isAdvantageousAgainst(dfdWeapon)) {
			this.advantage = 'a';
		} else if (dfdWeapon.isAdvantageousAgainst(atkWeapon)) {
			this.advantage = 'd';
		} else {
			this.advantage = 'n';
		}
	}

	public String getAttackerName() {
		return attacker.getName();
	}

	public String getDefenderName() {
		return defender.getName();
	}

	public Weapon getAtkWeapon() {
		return atkWeapon;
	}

	public Weapon getDfdWeapon() {
		return dfdWeapon;
	}

	public int getAtkMight() {
		return atkStats[0];
	}
	public int getAtkHit() {
		return atkStats[1];
	}
	public int getAtkCritical() {
		return atkStats[2];
	}
	public int getAtkRepeats() {
		return atkStats[3];
	}

	public int getDfdMight() {
		return dfdStats[0];
	}
	public int getDfdHit() {
		return dfdStats[1];
	}
	public int getDfdCritical() {
		return dfdStats[2];
	}
	public int getDfdRepeats() {
		return dfdStats[3];
	}

	public boolean isAtkEffective() {
		return atkEffective;
	}

	public boolean isDfdEffective() {
		return dfdEffective;
	}

	public char getAdvantage() {
		return advantage;
	}
}
