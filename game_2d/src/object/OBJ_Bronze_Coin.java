package object;

import entity.Entity;
import game_2d.GamePanel;

public class OBJ_Bronze_Coin extends Entity{
	public static final String objName = "Bronze Coin";
	GamePanel gp;
	
	public OBJ_Bronze_Coin(GamePanel gp) {
		super(gp);
		this.gp = gp;
		
		type = type_pickUp_Only;
		name = objName;
		down1 = setup("/objects/coin_bronze",gp.tileSize,gp.tileSize);
		value = 1;

		
	}
	
	public boolean use(Entity entity) {
		gp.playSE(1);
		gp.ui.addMessage("Coin +" + value);
		gp.player.coin += value;
		//return true if used the item
		return true;
	}

}
