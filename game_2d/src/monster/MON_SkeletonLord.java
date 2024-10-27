package monster;

import java.util.Random;

import entity.Entity;
import game_2d.GamePanel;
import object.OBJ_Bronze_Coin;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;

public class MON_SkeletonLord extends Entity{

	GamePanel gp;
	public static final String monName= "Skeleton Lord"; 
	
	public MON_SkeletonLord(GamePanel gp) {
		super(gp);
		this.gp = gp;
		
		name=monName;
		defaultSpeed = 1;
		speed = defaultSpeed;
		maxLife = 50;
		life = maxLife;
		type = type_monster;
		attack = 10;
		defense = 2;
		exp = 50;
		knockBackPower = 5;
		
		montion1_duration = 25;
		montion2_duration = 50;
		
		//setting solid area
		int size = gp.tileSize * 5;
		solidArea.x = 48;
		solidArea.y = 48;
		solidArea.width = size - 48 * 2;
		solidArea.height = size - 48;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		//attack area is 1 tile
		attackArea.width = 170;
		attackArea.height = 170;
		
		
		getImage();
		getAttackImage();
		
		
		
		
	}
	
	//LOAD AND SCALE IMAGE
	public void getImage() {
		int i = 5;
		
		if(inRage == false) {
			up1 = setup("/monsters/skeletonlord_up_1",gp.tileSize * i ,gp.tileSize* i);
			up2 = setup("/monsters/skeletonlord_up_2",gp.tileSize* i ,gp.tileSize* i);
			down1 = setup("/monsters/skeletonlord_down_1",gp.tileSize* i ,gp.tileSize* i);
			down2 = setup("/monsters/skeletonlord_down_2",gp.tileSize * i,gp.tileSize* i);
			left1 = setup("/monsters/skeletonlord_left_1",gp.tileSize* i ,gp.tileSize* i);
			left2 = setup("/monsters/skeletonlord_left_2",gp.tileSize * i,gp.tileSize* i);
			right1 = setup("/monsters/skeletonlord_right_1",gp.tileSize * i,gp.tileSize* i);
			right2 = setup("/monsters/skeletonlord_right_2",gp.tileSize * i,gp.tileSize* i);
		}
		
		if(inRage == true) {
			up1 = setup("/monsters/skeletonlord_phase2_up_1",gp.tileSize * i ,gp.tileSize* i);
			up2 = setup("/monsters/skeletonlord_phase2_up_2",gp.tileSize* i ,gp.tileSize* i);
			down1 = setup("/monsters/skeletonlord_phase2_down_1",gp.tileSize* i ,gp.tileSize* i);
			down2 = setup("/monsters/skeletonlord_phase2_down_2",gp.tileSize * i,gp.tileSize* i);
			left1 = setup("/monsters/skeletonlord_phase2_left_1",gp.tileSize* i ,gp.tileSize* i);
			left2 = setup("/monsters/skeletonlord_phase2_left_2",gp.tileSize * i,gp.tileSize* i);
			right1 = setup("/monsters/skeletonlord_phase2_right_1",gp.tileSize * i,gp.tileSize* i);
			right2 = setup("/monsters/skeletonlord_phase2_right_2",gp.tileSize * i,gp.tileSize* i);
			
			
		}
		
		}

	public void getAttackImage() { 
			
		int i = 5;
		
		if(inRage == false) {
			attackUp1    = setup("/monsters/skeletonlord_attack_up_1",gp.tileSize *i,gp.tileSize*i*2);
			attackUp2    = setup("/monsters/skeletonlord_attack_up_2",gp.tileSize*i ,gp.tileSize*i*2);
			attackDown1  = setup("/monsters/skeletonlord_attack_down_1",gp.tileSize*i ,gp.tileSize*i*2);
			attackDown2  = setup("/monsters/skeletonlord_attack_down_2",gp.tileSize *i,gp.tileSize*i*2);
			attackLeft1  = setup("/monsters/skeletonlord_attack_left_1",gp.tileSize*i*2 ,gp.tileSize*i);
			attackLeft2  = setup("/monsters/skeletonlord_attack_left_2",gp.tileSize*i*2 ,gp.tileSize*i);
			attackRight1 = setup("/monsters/skeletonlord_attack_right_1",gp.tileSize*i *2,gp.tileSize*i);
			attackRight2 = setup("/monsters/skeletonlord_attack_right_2",gp.tileSize *i*2,gp.tileSize*i);
		}
		
		if(inRage == true) {
			attackUp1    = setup("/monsters/skeletonlord_phase2_attack_up_1",gp.tileSize *i,gp.tileSize*i*2);
			attackUp2    = setup("/monsters/skeletonlord_phase2_attack_up_2",gp.tileSize*i ,gp.tileSize*i*2);
			attackDown1  = setup("/monsters/skeletonlord_phase2_attack_down_1",gp.tileSize*i ,gp.tileSize*i*2);
			attackDown2  = setup("/monsters/skeletonlord_phase2_attack_down_2",gp.tileSize *i,gp.tileSize*i*2);
			attackLeft1  = setup("/monsters/skeletonlord_phase2_attack_left_1",gp.tileSize*i*2 ,gp.tileSize*i);
			attackLeft2  = setup("/monsters/skeletonlord_phase2_attack_left_2",gp.tileSize*i*2 ,gp.tileSize*i);
			attackRight1 = setup("/monsters/skeletonlord_phase2_attack_right_1",gp.tileSize*i *2,gp.tileSize*i);
			attackRight2 = setup("/monsters/skeletonlord_phase2_attack_right_2",gp.tileSize *i*2,gp.tileSize*i);
		
		}
		
		
}
	
	
	//SETTING SLIME BEHAVIOR
	public void setAction() {
		
		if(inRage == false && life < maxLife /2) {
			inRage = true;
			getImage();
			getAttackImage();
			defaultSpeed++;
			speed = defaultSpeed;
			attack *= 2;
		}
		
		//get distance between player and monster
		if(getTileDistance(gp.player) < 10){
			
			//every 60 frame check the move between player
			moveTowardPlayer(60);
		
		}
		else {
			
			
			//interval
			getRandomDirection(120);
		}
		
		//Check if it attacks
		if(attacking == false) {
			//to make it more aggressive choose a number < 30
			checkAttackOrNot(60, gp.tileSize*7 , gp.tileSize*5);
		}
		
	
	}
	
	
	
	public void damageReaction() {
		
		actionLockCounter = 0;
		
		
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
