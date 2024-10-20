package monster;

import java.util.Random;

import entity.Entity;
import game_2d.GamePanel;
import object.OBJ_Bronze_Coin;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import object.OBJ_Rock;

public class MON_Orc extends Entity{

	GamePanel gp;
	
	public MON_Orc(GamePanel gp) {
		super(gp);
		this.gp = gp;
		
		name="Orc";
		defaultSpeed = 1;
		speed = defaultSpeed;
		maxLife = 10;
		life = maxLife;
		type = type_monster;
		attack = 8;
		defense = 2;
		exp = 2;

		montion1_duration = 40;
		montion2_duration = 85;
		
		//setting solid area
		solidArea.x = 4;
		solidArea.y = 4;
		solidArea.width = 40;
		solidArea.height = 44;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		//attack area is 1 tile
		attackArea.width = 48;
		attackArea.height = 48;
		
		
		getImage();
		getAttackImage();
		
		
		
		
	}
	
	//LOAD AND SCALE IMAGE
	public void getImage() {
		up1 = setup("/monsters/orc_up_1",gp.tileSize ,gp.tileSize);
		up2 = setup("/monsters/orc_up_2",gp.tileSize ,gp.tileSize);
		down1 = setup("/monsters/orc_down_1",gp.tileSize ,gp.tileSize);
		down2 = setup("/monsters/orc_down_2",gp.tileSize ,gp.tileSize);
		left1 = setup("/monsters/orc_left_1",gp.tileSize ,gp.tileSize);
		left2 = setup("/monsters/orc_left_2",gp.tileSize ,gp.tileSize);
		right1 = setup("/monsters/orc_right_1",gp.tileSize ,gp.tileSize);
		right2 = setup("/monsters/orc_right_2",gp.tileSize ,gp.tileSize);

		
		}

	public void getAttackImage() { 
			
		
		attackUp1    = setup("/monsters/orc_attack_up_1",gp.tileSize ,gp.tileSize*2);
		attackUp2    = setup("/monsters/orc_attack_up_2",gp.tileSize ,gp.tileSize*2);
		attackDown1  = setup("/monsters/orc_attack_down_1",gp.tileSize ,gp.tileSize*2);
		attackDown2  = setup("/monsters/orc_attack_down_2",gp.tileSize ,gp.tileSize*2);
		attackLeft1  = setup("/monsters/orc_attack_left_1",gp.tileSize*2 ,gp.tileSize);
		attackLeft2  = setup("/monsters/orc_attack_left_2",gp.tileSize*2 ,gp.tileSize);
		attackRight1 = setup("/monsters/orc_attack_right_1",gp.tileSize *2,gp.tileSize);
		attackRight2 = setup("/monsters/orc_attack_right_2",gp.tileSize *2,gp.tileSize);
	
		
}
	
	
	//SETTING SLIME BEHAVIOR
	public void setAction() {
		
		
		if(onPath == true) {
			
			checkStopChasing(gp.player , 15 , 100);
			
			//third param is if we want the character to follow the goal
			searchPath(getGoalCol(gp.player)  , getGoalRow(gp.player));
			
		
		}
		else {
			//Check if it starts chasing
			checkStartChasing(gp.player , 5 , 100);
			
			getRandomDirection();
		}
		
		//Check if it attacks
		if(attacking == false) {
			//to make it more aggressive choose a number < 30
			checkAttackOrNot(30 , gp.tileSize*4 , gp.tileSize);
		}
		
	
	}
	
	public void damageReaction() {
		
		actionLockCounter = 0;
		//direction = gp.player.direction;
		onPath = true;
		
	}
	
	public void checkDrop() {
		
		int i = new Random().nextInt(100)+1;
		
		//set the monster drop
		if(i < 50) {
			dropItem(new OBJ_Bronze_Coin(gp));
		}
		if(i >= 50 &  i<75) {
			dropItem(new OBJ_Heart(gp));
		}
		
		if(i >= 75 &  i<100) {
			dropItem(new OBJ_ManaCrystal(gp));
		}
		
	}

}
