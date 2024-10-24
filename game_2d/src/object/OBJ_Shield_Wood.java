package object;

import entity.Entity;
import game_2d.GamePanel;

public class OBJ_Shield_Wood extends Entity {

	public static final String objName = "Wood shield";

	
	public OBJ_Shield_Wood(GamePanel gp) {
		super(gp);
		type  =type_shield;
		name = objName;
		down1= setup("/objects/shield_wood",gp.tileSize , gp.tileSize);
		defenseValue = 1;
		description = "[" + name + "]\nA Wood Shield";
		price = 55;

	}

}
