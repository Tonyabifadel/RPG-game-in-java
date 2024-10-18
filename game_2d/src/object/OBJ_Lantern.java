package object;

import entity.Entity;
import game_2d.GamePanel;

public class OBJ_Lantern extends Entity {

	public OBJ_Lantern(GamePanel gp) {
		super(gp);
		
		type  = type_light;
		name = "Lantern";
		
		down1 = setup("/objects/lantern" , gp.tileSize , gp.tileSize);
		description = "[" + name + "]\nShine the way";
		
		price = 200;
		lightRadius = 250;
	}

}