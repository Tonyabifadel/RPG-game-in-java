package monster;

import java.util.Random;

import entity.Entity;
import entity.Projectile;
import game_2d.GamePanel;
import object.OBJ_Rock;

public class MON_GreenSlime extends Entity{

	GamePanel gp;
	public MON_GreenSlime(GamePanel gp) {
		super(gp);
		this.gp = gp;
		
		name="Green slime";
		speed = 1;
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
		int i = new Random().nextInt(100)+1;
		if(i>99 && projectile.alive == false && shotAvailableCounter ==30) {
			projectile.set(worldX, worldY, direction, true, this);
			gp.projectileList.add(projectile);
			shotAvailableCounter = 0;
		}
		
	}
	
	public void damageReaction() {
		actionLockCounter = 0;
		direction = gp.player.direction;
		
		
	}

}
