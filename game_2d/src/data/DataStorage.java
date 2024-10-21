package data;

import java.io.Serializable;
import java.util.ArrayList;

public class DataStorage implements Serializable {

	//Player Stats
	int level;
	int maxLife;
	int life;
	int Maxmana;
	int mana;
	int strength;
	int dexterity;
	int exp;
	int nextLevelup;
	int coin;
	
	//PLAYER INVENTORY
	ArrayList<String> itemNames = new ArrayList<>();
	ArrayList<Integer> itemAmounts = new ArrayList<>();
	int currentWeaponSlot;
	int currentShieldSlot;
	
	//OBJECT ON MAP
	String mapObjectNames[][];
	int mapObjectWorldX[][];
	int mapObjectWorldY[][];
	String mapObjectLootNames[][];
	boolean mapObjectOpened[][];
	
}
