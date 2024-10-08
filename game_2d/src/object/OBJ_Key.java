package object;

import entity.Entity;
import game_2d.GamePanel;

public class OBJ_Key  extends Entity{
	public OBJ_Key(GamePanel gp) {
		super(gp);
		
		name = "Key";
		down1 = setup("/objects/key",gp.tileSize ,gp.tileSize);

	}
	
	//to specify a solid area for a specific object do it here
	//solidArea.x=5;

}
