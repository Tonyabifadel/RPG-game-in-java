package object;

import entity.Entity;
import game_2d.GamePanel;

public class OBJ_Axe extends Entity {

	public static final String objName = "Woodcutter Axe";
	public OBJ_Axe(GamePanel gp) {
		super(gp);
		type = type_axe;
		name = objName;
		down1 = setup("/objects/axe" , gp.tileSize , gp.tileSize);
		attackValue = 2;
		description = "[" + name + "]\nRusty but still sharp\nenough to cut trees";
		
		attackArea.width = 30;
		attackArea.height  =30;
		price = 75;
		knockBackPower = 10;
		montion1_duration = 20;
		montion2_duration = 40;
	}

}
