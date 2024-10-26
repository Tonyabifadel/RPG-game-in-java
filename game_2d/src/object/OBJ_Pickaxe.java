package object;

import entity.Entity;
import game_2d.GamePanel;

public class OBJ_Pickaxe extends Entity {

	public static final String objName = "Pickaxe";
	public OBJ_Pickaxe (GamePanel gp) {
		super(gp);
		type = type_pickaxe;
		name = objName;
		down1 = setup("/objects/pickaxe" , gp.tileSize , gp.tileSize);
		attackValue = 2;
		description = "[" + name + "]\nYou will dig it!!";
		
		attackArea.width = 30;
		attackArea.height  =30;
		price = 75;
		knockBackPower = 10;
		montion1_duration = 10;
		montion2_duration = 20;
	}

}
