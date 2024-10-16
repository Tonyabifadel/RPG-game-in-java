package monster;

import java.util.Random;

import entity.Entity;
import entity.Projectile;
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
	
	public void update() {
		super.update();
		
		int xDistance = Math.abs(worldX - gp.player.worldX);
		int yDistance = Math.abs(worldY - gp.player.worldY);
		int tileDistance = (xDistance + yDistance) / gp.tileSize;
		
		if(onPath == false && tileDistance < 5) {
			int i = new Random().nextInt(100)+1;
			if(i>50) {
				onPath = true;
			}
		}
		if(onPath == true && tileDistance > 20) {
			onPath = false;
		}

		
	}
	
	//SETTING SLIME BEHAVIOR
	public void setAction() {
		
		if(onPath==true) {
		
			int goalCol = (gp.player.worldX + gp.player.solidArea.x)/gp.tileSize;
			int goalRow = (gp.player.worldY + gp.player.solidArea.y)/gp.tileSize;
		
			//third param is if we want the character to follow the goal
			searchPath(goalCol, goalRow);
			
			int i = new Random().nextInt(200)+1;
			
			if(i>197 && projectile.alive == false && shotAvailableCounter ==30) {
				projectile.set(worldX, worldY, direction, true, this);

				//Check vacancy
				for(int j =0 ; j< gp.projectileList[1].length;j++){
					if(gp.projectileList[gp.currentMap][j]==null){
						gp.projectileList[gp.currentMap][j] = projectile;
						break;
					}
				}
				
				shotAvailableCounter = 0;
			}
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
