package object;

import entity.Entity;
import game_2d.GamePanel;

public class OBJ_Potion_Red extends Entity {
	
	GamePanel gp;
	public static final String objName = "Red Potion";

	
	public OBJ_Potion_Red(GamePanel gp) {
		super(gp);
		this.gp=gp;
		type  =type_consumable;
		name = objName;
		down1= setup("/objects/potion_red",gp.tileSize , gp.tileSize);
		defenseValue = 2;
		value = 5;
		price = 20;
		stackable = true;
	
		description = "[" + name + "]\nA Red Portion that\nheals your life by " + value;
		setDialogue();
		
	}
	private void setDialogue() {
		dialogues[0][0] = "\"You drink the \" + name+ \"!\\n\"\r\n"
				+ "				+ \"Your life has been recovered by \" + value";
		
	}
	public boolean use(Entity entity) {
		startDialogue(this , 0);
		
		entity.life +=value;
		gp.playSE(2);
		return true;

	}

}
