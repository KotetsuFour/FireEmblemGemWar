package manager.combat;

import inventory.weapon.Armor;
import inventory.weapon.Weapon;
import manager.FEGemWarManager;
import map.Tile;
import unit.Unit;

public class CombatManager {

	/*
	 * [0] = atkHP
	 * [1] = atkMt
	 * [2] = atkHit
	 * [3] = atkCrit
	 * [4] = atkAttacks (1 for one attack, 2 for double attack)
	 * [5] = atkBrave (1 if no brave effect, 2 if brave effect)
	 * [6] = dfdHP
	 * [7] = dfdMt
	 * [8] = dfdHit
	 * [9] = dfdCrit
	 * [10] = dfdAttacks (0 for no counter, 1 for one attack, 2 for double attack)
	 * [11] = dfdBrave (1 if no brave effect, 2 if brave effect)
	 */
	public static int[] getBattleForecast(Unit atk, Unit dfd, Weapon atkWep, Weapon dfdWep,
			Tile atkTile, Tile dfdTile) {
		int[] ret = new int[12];
		ret[0] = atk.getCurrentHP();
		if (atkWep != null && atkWep.isMagic()) {
			ret[1] = atk.getMagic() + atkWep.getMight() - dfd.getResistance();
		} else {
			ret[1] = atk.getStrength() - dfd.getDefense();
			if (atkWep != null) {
				ret[1] += atkWep.getMight();
			}
		}
		if (dfdWep instanceof Armor) {
			ret[1] -= ((Armor)dfdWep).getProtection();
		}
		ret[2] = atk.getBaseAccuracy() - dfd.getBaseAvoidance();
		if (atkWep != null) {
			ret[2] += atkWep.getHit();
		}
		if (!dfd.getUnitClass().isFlying()) {
			ret[2] -= dfdTile.getAvoidanceBonus();
		}
		if (atkWep != null && atkWep.isAdvantageousAgainst(dfdWep)) {
			ret[2] += 15;
		}
		ret[3] = atk.getbaseCrit() - dfd.getLuck();
		if (atkWep != null) {
			ret[3] += atkWep.getCrit();
		}
		int atkAttackSpeed = atk.getSpeed();
		if (atkWep != null) {
			atkAttackSpeed -= Math.max(0, atkWep.getWeight() - atk.getConstitution());
		}
		int dfdAttackSpeed = dfd.getSpeed();
		if (dfdWep != null) {
			dfdAttackSpeed -= Math.max(0, dfdWep.getWeight() - dfd.getConstitution());
		}
		if (atkAttackSpeed - dfdAttackSpeed >= 4) {
			ret[4] = 2;
		} else {
			ret[4] = 1;
		}
		if (atkWep != null && atkWep.isBrave()) {
			ret[5] = 2;
		} else {
			ret[5] = 1;
		}

		ret[6] = dfd.getCurrentHP();
		
		int distance = Math.abs(atkTile.getX() - dfdTile.getX()) + Math.abs(atkTile.getY() - dfdTile.getY());
		if ((distance != 1 && dfdWep == null) ||
				distance < dfdWep.getMinRange() || distance > dfdWep.getMaxRange()) {
			ret[10] = 0;
			return ret;
		}
		
		if (dfdWep != null && dfdWep.isMagic()) {
			ret[7] = dfd.getMagic() + dfdWep.getMight() - atk.getResistance();
		} else {
			ret[7] = dfd.getStrength() - atk.getDefense();
			if (dfdWep != null) {
				ret[7] += dfdWep.getMight();
			}
		}
		if (atkWep instanceof Armor) {
			ret[7] -= ((Armor)atkWep).getProtection();
		}
		ret[8] = dfd.getBaseAccuracy() - atk.getBaseAvoidance();
		if (dfdWep != null) {
			ret[8] += dfdWep.getHit();
		}
		if (dfdWep != null && dfdWep.isAdvantageousAgainst(atkWep)) {
			ret[8] += 15;
		}
		if (!atk.getUnitClass().isFlying()) {
			ret[8] -= atkTile.getAvoidanceBonus();
		}
		ret[9] = dfd.getbaseCrit() - atk.getLuck();
		if (dfdWep != null) {
			ret[9] += dfdWep.getCrit();
		}
		if (dfdAttackSpeed - atkAttackSpeed >= 4) {
			ret[10] = 2;
		} else {
			ret[10] = 1;
		}
		if (dfdWep != null && dfdWep.isBrave()) {
			ret[11] = 2;
		} else {
			ret[11] = 1;
		}
		
		return ret;
	}
	
	/*
	 * Continual sequence of
	 * [0] = attacker (0 is atk, 1 is dfd),
	 * [1] = result of attack (0 is miss, 1 is hit, 2 is crit),
	 * [2] = special notes (maybe used for different skills)
	 */
	public static int[] decideBattle(int[] forecast) {
		int[] ret = new int[((forecast[4] * forecast[5]) + (forecast[10] * forecast[11])) * 3];
		int idx1 = 0;
		int idx2 = 1;
		int idx3 = 2;
		for (int q = 0; q < forecast[5]; q++) {
			ret[idx1] = 0;
			int rngNumber = FEGemWarManager.random0To99();
			if (rngNumber < forecast[2]) {
				if (rngNumber < forecast[3]) {
					ret[idx2] = 2;
				} else {
					ret[idx2] = 1;
				}
			} else {
				ret[idx2] = 0;
			}
			idx1 += 3;
			idx2 += 3;
			idx3 += 3;
		}
		int num = forecast[10] * forecast[11];
		for (int q = 0; q < num; q++) {
			ret[idx1] = 1;
			int rngNumber = FEGemWarManager.random0To99();
			if (rngNumber < forecast[8]) {
				if (rngNumber < forecast[9]) {
					ret[idx2] = 2;
				} else {
					ret[idx2] = 1;
				}
			} else {
				ret[idx2] = 0;
			}
			idx1 += 3;
			idx2 += 3;
			idx3 += 3;
		}
		if (forecast[4] == 2) {
			for (int q = 0; q < forecast[5]; q++) {
				ret[idx1] = 0;
				int rngNumber = FEGemWarManager.random0To99();
				if (rngNumber < forecast[2]) {
					if (rngNumber < forecast[3]) {
						ret[idx2] = 2;
					} else {
						ret[idx2] = 1;
					}
				} else {
					ret[idx2] = 0;
				}
				idx1 += 3;
				idx2 += 3;
				idx3 += 3;
			}
		}

		return ret;
	}
	
	public static boolean performAttack(Unit dfd, int damage, boolean crit) {
		return dfd.takeDamage(damage, crit);
	}
}
