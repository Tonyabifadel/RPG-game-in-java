package object;

import entity.Entity;
import game_2d.GamePanel;

public class OBJ_Sword_Normal extends Entity{

	public OBJ_Sword_Normal(GamePanel gp) {
		super(gp);
		
		type = type_sword;
		name = "Normal Sword";
		down1 = setup("/objects/sword_normal", gp.tileSize,  gp.tileSize);
		attackValue = 1;
		description = "[" + name + "]\nAn old sword";
		attackArea.width = 36;
		attackArea.height  =36;
		price = 100;
		knockBackPower = 2;
		montion1_duration = 5;
		montion2_duration = 25;

	}
	
	
}
