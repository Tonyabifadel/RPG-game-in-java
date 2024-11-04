package object;

import entity.Entity;
import game_2d.GamePanel;

public class OBJ_BlueHeart extends Entity {
	
	GamePanel gp;
	public static final String objName = "Blue Heart";
	
	public OBJ_BlueHeart(GamePanel gp) {
		super(gp);
		
		this.gp =gp;
		type = type_pickUp_Only;
		name = objName;
		down1 = setup("/objects/blueheart" , gp.tileSize , gp.tileSize);
		
		setDialogues();
	}

	public void setDialogues() {
		
		dialogues[0][0] = "You pick up a beautifull gem";
		dialogues[0][1] = "You find the blue heart!";
		
	}
	
	public boolean use(Entity entity) {
		gp.gameState = gp.cutsceneState;
		gp.csManager.sceneNum = gp.csManager.ending;
		return true;
	}
	

}
