package object;

import entity.Entity;
import game_2d.GamePanel;

public class OBJ_Key  extends Entity{
	GamePanel gp;
	public OBJ_Key(GamePanel gp) {
		
		super(gp);
		this.gp = gp;
		type = type_consumable;
		
		name = "Key";
		down1 = setup("/objects/key",gp.tileSize ,gp.tileSize);
		description = "[" + name + "]\nA Key to open door";
		price = 35;
		stackable = true;
		setDialogue();
	}
	
	public void setDialogue() {
		dialogues[0][0] ="You use the " + name + " and open the door" ;
	
		dialogues[1][0] ="What are you doing??";
		
	}
	public boolean use(Entity entity){
		
		
		int objIndex = getDetected(entity , gp.obj , "Door");
		
		if(objIndex != 999) {
			startDialogue(this , 0);
			gp.playSE(3);
			gp.obj[gp.currentMap][objIndex] = null;
			return true;

		}
		else {
			startDialogue(this , 1);
			return false;
		}

		
		
	}
	//to specify a solid area for a specific object do it here
	//solidArea.x=5;

	

}
