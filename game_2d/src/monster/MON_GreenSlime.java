package monster;

import java.util.Random; 

import entity.Entity;
import game_2d.GamePanel;
import object.OBJ_Bronze_Coin;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import object.OBJ_Rock;

public class MON_GreenSlime extends Entity{

	GamePanel gp;
	public MON_GreenSlime(GamePanel gp) {
		super(gp);
		this.gp = gp;
		
		name="Green slime";
		defaultSpeed = 1;
		speed = defaultSpeed;
		maxLife = 4;
		life = maxLife;
		type = type_monster;
		attack = 5;
		defense = 0;
		exp = 2;
		projectile  = new OBJ_Rock(gp); 
		//setting solid area
		solidArea.x = 3;
		solidArea.y = 10;
		solidArea.width = 42;
		solidArea.height = 30;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		
		getImage();
		
		
		
		
	}
	
	//LOAD AND SCALE IMAGE
	public void getImage() {
		up1 = setup("/monsters/greenslime_down_1",gp.tileSize ,gp.tileSize);
		up2 = setup("/monsters/greenslime_down_2",gp.tileSize ,gp.tileSize);
		down1 = setup("/monsters/greenslime_down_1",gp.tileSize ,gp.tileSize);
		down2 = setup("/monsters/greenslime_down_2",gp.tileSize ,gp.tileSize);
		left1 = setup("/monsters/greenslime_down_1",gp.tileSize ,gp.tileSize);
		left2 = setup("/monsters/greenslime_down_2",gp.tileSize ,gp.tileSize);
		right1 = setup("/monsters/greenslime_down_1",gp.tileSize ,gp.tileSize);
		right2 = setup("/monsters/greenslime_down_2",gp.tileSize ,gp.tileSize);

		
		}

	
	
	//SETTING SLIME BEHAVIOR
	public void setAction() {
		
		
		if(onPath == true) {
			
			checkStopChasing(gp.player , 15 , 100);
			
			//third param is if we want the character to follow the goal
			searchPath(getGoalCol(gp.player)  , getGoalRow(gp.player));
			
			//Check if it shoots a projectile
			//checkShoot(200 , 30);
			
		
		}
		else {
			//Check if it starts chasing
			checkStartChasing(gp.player , 5 , 100);
			
			getRandomDirection(120);
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
