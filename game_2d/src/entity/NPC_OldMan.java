package entity;

import java.awt.Rectangle;
import java.util.Random;
import game_2d.GamePanel;

public class NPC_OldMan extends Entity{

	public NPC_OldMan(GamePanel gp) {
			super(gp);
			direction="down";
			speed = 2;		
			

			solidArea = new Rectangle();
			
			solidArea.x=8;
			solidArea.y=16;
			
			solidAreaDefaultX=solidArea.x;
			solidAreaDefaultY=solidArea.y;
			solidArea.width =30 ;
			solidArea.height = 30;
			
			dialogueSet =-1;
		
			getImage();
			setDialogue();
		}
	
	
	public void getImage() {
		
		up1 = setup("/npc/oldman_up_1",gp.tileSize ,gp.tileSize);
		up2 = setup("/npc/oldman_up_2",gp.tileSize ,gp.tileSize);
		down1 = setup("/npc/oldman_down_1",gp.tileSize ,gp.tileSize);
		down2 = setup("/npc/oldman_down_2",gp.tileSize ,gp.tileSize);
		left1 = setup("/npc/oldman_left_1",gp.tileSize ,gp.tileSize);
		left2 = setup("/npc/oldman_left_2",gp.tileSize ,gp.tileSize);
		right1 = setup("/npc/oldman_right_1",gp.tileSize ,gp.tileSize);
		right2 = setup("/npc/oldman_right_2",gp.tileSize ,gp.tileSize);

	}

	
	public void setDialogue() {
		//First dimension == dialogue set
		
		dialogues[0][0] = "Hello, lad. My name is joe";
		dialogues[0][1] = "So you've come to this island to \n find the treasure?";
		
		
		dialogues[1][0] = "If tired, rest at water";
		dialogues[1][1] = "Monster  may reappear\n be careful";
		
		dialogues[2][0] = "I wonder how to open the door";
	}

	public void setAction() {
		
		if(onPath == true) {
			
//			int goalCol = 12;
//			int goalRow = 10;
			int goalCol = (gp.player.worldX + gp.player.solidArea.x)/gp.tileSize;
			int goalRow = (gp.player.worldY + gp.player.solidArea.y)/gp.tileSize;
			
			//third param is if we want the character to follow the goal
			searchPath(goalCol, goalRow);
			
		}
		else {
			
			
		actionLockCounter++;
		
		if(actionLockCounter ==120) {
			Random random = new Random();
			int i = random.nextInt(100)+1;
			
			if(i<=25) {
				direction="up";
			}
			if(i>25 && i<=50) {
				direction="down";
			}
			
			if(i>50 && i<=75) {
				direction="left";
			}
			
			if(i>75 && i<=100 ) {
				direction="right";
			}
			
	
			actionLockCounter=0;
		}
	}
		
		
	}
	
	
	public void speak() {
		
		facePlayer();
		startDialogue(this, dialogueSet);
		
		dialogueSet++;
		
		if(dialogues[dialogueSet][0] == null) {
			//dialogueSet = 0; for repeating the dialogue
		
			//if we want to repeat the last dialogue
			dialogueSet--;
		}
		
		//if(gp.player.life < gp.player.life /3) {
			//dialogueSet=1;
		//}
		
		//onPath = true;
	}
	
}
