package unit;

import inventory.Item;
import inventory.weapon.Weapon;
import manager.FEGemWarManager;
import story.DialogueEvent;

public class Unit implements UnitInterface {

	private String name;
	private UnitClass unitClass;
	private String description;
	private int maxHP;
	private int currentHP;
	private int strength;
	private int magic;
	private int skill;
	private int speed;
	private int luck;
	private int defense;
	private int resistance;
	private int constitution;
	private int movement;
	
	private int hpGrowth;
	private int strengthGrowth;
	private int magicGrowth;
	private int skillGrowth;
	private int speedGrowth;
	private int luckGrowth;
	private int defenseGrowth;
	private int resistanceGrowth;
	private int level;
	private int experience;
	
	private Item specialItem;
	
	private Item heldItem;
	
	private int weaponType;
	private int proficiency;
	
	private boolean essential;
	private boolean leader;
	private int equipped; //0 = special, 1 = held, 2 = none
	private boolean exhausted;
	
	private DialogueEvent talkConvo;
	private DialogueEvent battleQuote;
	private DialogueEvent deathQuote;
	private boolean talkRestricted;
	private Item talkReward;
	private boolean dropsHeldItem;
	
	private AIType ai1;
	private AIType ai2;
	
	
	public Unit(String name, UnitClass unitClass, String description, int maxHP, int strength, int magic,
			int skill, int speed, int luck, int defense, int resistance, int constitution, int movement,
			int hpGrowth, int strengthGrowth, int magicGrowth, int skillGrowth, int speedGrowth, int luckGrowth,
			int defenseGrowth, int resistanceGrowth, Item specialItem, int weaponType, int weaponProf) {
		this.name = name;
		this.description = description;
		this.unitClass = unitClass;
		this.maxHP = maxHP;
		this.currentHP = maxHP;
		this.strength = strength;
		this.magic = magic;
		this.skill = skill;
		this.speed = speed;
		this.luck = luck;
		this.defense = defense;
		this.resistance = resistance;
		this.constitution = constitution;
		this.movement = movement;
		this.hpGrowth = hpGrowth;
		this.strengthGrowth = strengthGrowth;
		this.magicGrowth = magicGrowth;
		this.skillGrowth = skillGrowth;
		this.speedGrowth = speedGrowth;
		this.luckGrowth = luckGrowth;
		this.defenseGrowth = defenseGrowth;
		this.resistanceGrowth = resistanceGrowth;
		this.specialItem = specialItem;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public int getMaxHP() {
		return maxHP;
	}

	public int getCurrentHP() {
		return currentHP;
	}
	
	@Override
	public int getStrength() {
		return strength;
	}
	public int getMagic() {
		return magic;
	}

	@Override
	public int getSkill() {
		return skill;
	}

	@Override
	public int getSpeed() {
		return speed;
	}

	@Override
	public int getLuck() {
		return luck;
	}

	@Override
	public int getDefense() {
		return defense;
	}

	@Override
	public int getResistance() {
		return resistance;
	}

	@Override
	public int getConstitution() {
		return constitution;
	}

	@Override
	public int getMovement() {
		return movement;
	}
	
	public UnitClass getUnitClass() {
		return unitClass;
	}
	
	public Item getHeldItem() {
		return heldItem;
	}

	public void setHeldItem(Item heldItem) {
		this.heldItem = heldItem;
	}

	public Item getSpecialItem() {
		return specialItem;
	}

	public int getBaseAccuracy() {
		return (skill * 2) + luck;
	}
	
	public int getBaseAvoidance() {
		return (speed * 2) + luck;
	}
	
	public int getbaseCrit() {
		return skill;
	}
	
	public int getExperience() {
		return experience;
	}
	public boolean[] addExperience(int exp) {
		experience += exp;
		if (experience >= 100) {
			experience -= 100;
			return levelUp();
		}
		return null;
	}
	public int getLevel() {
		return level;
	}
	private boolean[] levelUp() {
		boolean[] ret = new boolean[8];
		level++;
		if (FEGemWarManager.random0To99() < hpGrowth) {
			maxHP++;
			ret[0] = true;
		}
		if (FEGemWarManager.random0To99() < strengthGrowth) {
			strength++;
			ret[1] = true;
		}
		if (FEGemWarManager.random0To99() < magicGrowth) {
			magic++;
			ret[2] = true;
		}
		if (FEGemWarManager.random0To99() < skillGrowth) {
			skill++;
			ret[3] = true;
		}
		if (FEGemWarManager.random0To99() < speedGrowth) {
			speed++;
			ret[4] = true;
		}
		if (FEGemWarManager.random0To99() < luckGrowth) {
			luck++;
			ret[5] = true;
		}
		if (FEGemWarManager.random0To99() < defenseGrowth) {
			defense++;
			ret[6] = true;
		}
		if (FEGemWarManager.random0To99() < resistanceGrowth) {
			resistance++;
			ret[7] = true;
		}
		return ret;
	}
	
	public boolean isAlive() {
		return currentHP > 0;
	}
	public boolean isEssential() {
		return essential;
	}
	public void setEssential(boolean ess) {
		essential = ess;
	}
	public boolean isLeader() {
		return leader;
	}
	public void setLeader(boolean leader) {
		this.leader = leader;
	}
	
	public boolean takeDamage(int damage, boolean crit) {
		if (crit) {
			damage *= 3;
		}
		currentHP -= damage;
		return isAlive();
	}

	public boolean canFly() {
		return unitClass.isFlying();
	}

	public void unExhaust() {
		exhausted = false;
	}
	public void exhaust() {
		exhausted = true;
	}
	public boolean isExhausted() {
		return exhausted;
	}

	public int heldWeaponType() {
		return weaponType;
	}
	public Weapon getEquippedWeapon() {
		if (equipped == 0) {
			if (specialItem instanceof Weapon) {
				return (Weapon)specialItem;
			}
		} else if (equipped == 1) {
			if (heldItem instanceof Weapon) {
				Weapon h = (Weapon)heldItem;
				if (weaponType == h.typeId()
						&& proficiency >= h.getProficiency()) {
					return (Weapon)heldItem;
				}
			}
			return null;
		} else {
			return null;
		}
		return null;
	}

	public void equipForDistance(int distance) {
		// TODO Auto-generated method stub
		if (equipped == 0) {
			if (specialItem instanceof Weapon) {
				Weapon s = (Weapon)specialItem;
				if (s.getMinRange() <= distance && s.getMaxRange() >= distance) {
					return;
				}
			}
			if (heldItem instanceof Weapon) {
				Weapon h = (Weapon)heldItem;
				if (h.typeId() == weaponType && proficiency >= h.getProficiency() &&
						h.getMinRange() <= distance && h.getMaxRange() >= distance) {
					equipped = 1;
					return;
				}
			}
		} else if (equipped == 1) {
			if (heldItem instanceof Weapon) {
				Weapon h = (Weapon)heldItem;
				if (h.typeId() == weaponType && proficiency >= h.getProficiency() &&
						h.getMinRange() <= distance && h.getMaxRange() >= distance) {
					return;
				}
			}
			if (specialItem instanceof Weapon) {
				Weapon s = (Weapon)specialItem;
				if (s.getMinRange() <= distance && s.getMaxRange() >= distance) {
					equipped = 0;
					return;
				}
			}
		} else {
			if (distance == 1) {
				return;
			}
			if (heldItem instanceof Weapon) {
				Weapon h = (Weapon)heldItem;
				if (h.typeId() == weaponType && proficiency >= h.getProficiency() &&
						h.getMinRange() <= distance && h.getMaxRange() >= distance) {
					equipped = 1;
					return;
				}
			}
			if (specialItem instanceof Weapon) {
				Weapon s = (Weapon)specialItem;
				if (s.getMinRange() <= distance && s.getMaxRange() >= distance) {
					equipped = 0;
					return;
				}
			}
		}
	}
	public void equipSpecial() {
		equipped = 0;
	}
	public void equipHeld() {
		equipped = 1;
	}
	public void equipNone() {
		equipped = 2;
	}
	
	public void setTalkConvo(DialogueEvent de, boolean restriction, Item reward) {
		this.talkConvo = de;
		this.talkRestricted = restriction;
		this.talkReward = reward;
	}
	public void setBattleQuote(DialogueEvent de) {
		this.battleQuote = de;
	}
	public void setDeathQuote(DialogueEvent de) {
		this.deathQuote = de;
	}
	public DialogueEvent getTalkConvo() {
		return this.talkConvo;
	}
	public DialogueEvent getBattleQuote() {
		return this.battleQuote;
	}
	public DialogueEvent getDeathQuote() {
		return this.deathQuote;
	}
	public boolean talkRestricted() {
		return talkRestricted;
	}
	public Item getTalkReward() {
		return talkReward;
	}
	public void setDropItem(boolean drop) {
		this.dropsHeldItem = drop;
	}
	public boolean dropsHeldItem() {
		return dropsHeldItem;
	}
	
	public void setAI(AIType ai1, AIType ai2) {
		this.ai1 = ai1;
		this.ai2 = ai2;
	}
	public AIType getAI1() {
		return ai1;
	}
	public AIType getAI2() {
		return ai2;
	}
	
	public enum AIType {
		ATTACK, GUARD, PURSUE, BURN,
	}

}
