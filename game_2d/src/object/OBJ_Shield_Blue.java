package object;

import entity.Entity;
import game_2d.GamePanel;

public class OBJ_Shield_Blue extends Entity {

	public OBJ_Shield_Blue(GamePanel gp) {
		super(gp);
		type  =type_shield;
		name = "Blue Shield";
		down1= setup("/objects/shield_blue",gp.tileSize , gp.tileSize);
		defenseValue = 2;
		description = "[" + name + "]\nA Blue Shield";
		price = 85;


	}

}
