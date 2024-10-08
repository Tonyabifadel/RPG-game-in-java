package object;


import entity.Entity;
import game_2d.GamePanel;

public class OBJ_Boots extends Entity {

	
	public OBJ_Boots(GamePanel gp) {
		
		super(gp);
		name = "Boots";
		down1 = setup("/objects/door",gp.tileSize ,gp.tileSize);
		
		
		
		
	
	
	}
	
}