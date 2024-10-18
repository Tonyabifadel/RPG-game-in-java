package object;

import entity.Entity;
import game_2d.GamePanel;

public class OBJ_Tent extends Entity {

	GamePanel gp;
	
	public OBJ_Tent(GamePanel gp) {
		super(gp);
		this.gp =  gp;
		type  = type_consumable;
		name = "Tent";
		
		down1 = setup("/objects/tent" , gp.tileSize , gp.tileSize);
		description = "[" + name + "]\nSleep until next morning";
		
		price = 300;
		stackable = true;
	}
	
	public boolean use(Entity entity) {
		
		gp.gameState = gp.sleepState;
		gp.playSE(14);
		
		gp.player.life = gp.player.maxLife;
		gp.player.mana = gp.player.maxMana;
		gp.player.getSleepingImage(down1);
		
		//return true means we can use it 1 time only before deleting it
		return true;
	}

}
