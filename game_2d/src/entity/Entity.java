package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D; 
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import game_2d.GamePanel;
import game_2d.UtilityTool;

//This class will store variables for player, monster, NPC 
public class Entity {
	
	GamePanel gp;
	public int worldX,worldY;
	public int speed;

	//buffered image is used to store the images/sprites
	public BufferedImage up1, up2, down1,down2,left1,left2,right1,right2;
	public BufferedImage attackUp1 , attackUp2 , attackDown1 , attackDown2,
	attackLeft1 , attackLeft2 , attackRight1 , attackRight2;
	public String direction = "down";
	
	
	public boolean alive = true;
	public boolean dying = false;
	public boolean hpBarOn = false;
	
	
	public int spriteCounter=0;
	public int spriteNum = 1;
	
	public Rectangle solidArea = new Rectangle(0,0,48,48);
	public Rectangle attackArea = new Rectangle(0,0,0,0);
	public int solidAreaDefaultX,solidAreaDefaultY;
	
	public boolean collisionOn = false;
	public int actionLockCounter = 0;
	public boolean invincible = false;
	public int invincibleCounter = 0;
	public int shotAvailableCounter = 0;
	int dyingCounter = 0;
	int hpBarCounter = 0;
	public boolean onPath = false;
	public boolean knockBack = false;
	public int knockBackCounter = 0;
	public boolean stackable  =false;
	public int  amount = 1;
	public int lightRadius ;
	
	public int value;
	public boolean attacking = false;
	
	
	String dialogues[] = new String[20];
	int dialogueIndex = 0;
	public Entity attacker;
	
	public BufferedImage image , image2, image3;
	public boolean collision = false;
	
	public int type; //type of entity 0-player-1-npc 2-monster
	public final int type_player = 0;
	public final int type_npc = 1;
	public final int type_monster = 2;
	public final int type_sword = 3;
	public final int type_axe = 4;
	public final int type_shield = 5;
	public final int type_consumable = 6;
	public final int type_pickUp_Only = 7;
	public final int type_Obstacle = 8;
	public final int type_light = 9;


	
	//Character Status
	
	public ArrayList<Entity> inventory = new ArrayList<>();
	public final int inventoryMaxSize = 20;
	
	public String name;
	//default speed is for knockback add it to any class that you want to have knockback
	public int defaultSpeed;
	public int maxLife;
	public int life;
	public int ammo;
	public int maxMana;
	public int mana;
	public int level;
	public int strength;
	public int dexterity;
	public int attack;
	public int defense;
	public int exp;
	public int nextLevelExp;
	public int coin;
	public int montion1_duration;
	public int montion2_duration;
	public Entity currentWeapon;
	public Entity currentShield;
	public Entity currentLight;
	
	
	public int attackValue;
	public int defenseValue;
	public Projectile projectile;
	
	public String description = "" ;
	
	public int useCost;
	public int price;
	public int knockBackPower = 0;
	public String KnockBackDirection;
	
	
	
	public Entity(GamePanel gp) {
		this.gp = gp;
		
	}
	
	public BufferedImage setup(String imagePath , int width , int height){
		UtilityTool uTool = new UtilityTool();
		BufferedImage scaleImage = null;
		
		try {
			scaleImage = ImageIO.read(getClass().getResourceAsStream(imagePath+".png"));
			scaleImage =uTool.scaleImage(scaleImage, width, height); 
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return scaleImage;
	}
	
	
	
	public void setAction() {
		
	}
	
	public void damageReaction() {
		
	}
	public boolean use(Entity entity) { 
		return false;
		
	}
	
	public void checkDrop() {
		
	}
	
	public void interact() {
		
	}
	
	public int getLeftX() {
		return worldX + solidArea.x;
	}
	
	public int getRightX() {
		return worldX + solidArea.x + solidArea.width;
	}
	
	public int getTopY() {
		return worldY + solidArea.y;
	}
	
	public int getBottomY() {
		return worldY + solidArea.y + solidArea.height;
	}
	
	public int getCol() {
		return (worldX + solidArea.x)/gp.tileSize;
	}
	
	public int getRow() {
		return (worldY + solidArea.y)/gp.tileSize;
	}
	
	public int getXDistance(Entity target) {
		int xDistance = Math.abs(worldX - target.worldX);
		return xDistance;	
	}
	
	public int getYDistance(Entity target) {
		int yDistance = Math.abs(worldY - target.worldY);
		return yDistance;	
	}
	
	public int getTileDistance(Entity target) {
		int tileDistance = (getXDistance(target) + getYDistance(target)) / gp.tileSize;
		return tileDistance;	
	}
	
	public int getGoalCol (Entity target) {
		int goalCol = (target.worldX + target.solidArea.x)/gp.tileSize;
	
		return goalCol;
	
	}
	
	public int getGoalRow (Entity target) {
		int goalRow = (target.worldY + target.solidArea.y)/gp.tileSize;
	
		return goalRow;
	
	}
	
	
	public Color getParticleColor() {
		Color color = new Color(0,0,0);
		return color;
	}
	
	public int getParticleSize() {
		int size =  0; //6 pixels
		return size;
	}
	public int getParticleSpeed() {
		int speed = 0;
		return speed;
	}
	
	public int getParticleMaxLife() {
		int maxLife= 0;
		return maxLife;
	}
	
	
	
	public void generateParticle(Entity generator , Entity target) {
		//later in code
		Color color = generator.getParticleColor();
		int size = generator.getParticleSize();
		int speed = generator.getParticleSpeed();
		int maxLife = generator.getParticleMaxLife();
		
		Particle p1 = new Particle(gp , target , color , size, speed ,maxLife , -2 , -1);
		Particle p2 = new Particle(gp , target, color , size, speed ,maxLife , 2 , -1);
		Particle p3 = new Particle(gp , target, color , size, speed ,maxLife , -2 , 1);
		Particle p4 = new Particle(gp , target , color , size, speed ,maxLife , 2 , 1);

		gp.particleList.add(p1);
		gp.particleList.add(p2);
		gp.particleList.add(p3);
		gp.particleList.add(p4);
	}
	
	public void dropItem(Entity droppedItem) {
		for(int i = 0;i<gp.obj[1].length;i++) {
			if(gp.obj[gp.currentMap][i] == null) {
				gp.obj[gp.currentMap][i] = droppedItem;
				gp.obj[gp.currentMap][i].worldX = worldX; //dead monster's worldx and worldy
				gp.obj[gp.currentMap][i].worldY = worldY;
				break;
			}
		}
		
	}
	
	public void speak() {
		if(dialogues[dialogueIndex]==null) {
			dialogueIndex = 0;
		}
	gp.ui.currentDialogue = dialogues[dialogueIndex];
	dialogueIndex++;
		
	switch(gp.player.direction) {
	case"up":		
		direction="down";
		break;
	case"down":
		direction="up";
		break;
	case"left":
		direction="right";
		break;
	case"right":
		direction="left";
		break;
	}
	}
		
	

	public void checkStopChasing(Entity target , int distance , int rate) {
		
		if(getTileDistance(target) > distance) {
			int i = new Random().nextInt(rate);
			if(i==0) {
				onPath = false;
			}
			
		}
	}
	
public void checkStartChasing(Entity target , int distance , int rate) {
		
		if(getTileDistance(target) < distance) {
			int i = new Random().nextInt(rate);
			if(i==0) {
				onPath = true;
			}
			
		}
	}
	
	public void checkShoot(int rate, int shotInterval){
		
		int i = new Random().nextInt(rate);
		if(i == 0 && projectile.alive == false && shotAvailableCounter ==shotInterval) {
			
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
	
	public void checkAttackOrNot(int rate , int straight , int horizontal){
		
		boolean targetInRange = false;
		int xDis = getXDistance(gp.player);
		int yDis = getYDistance(gp.player);
		
		switch(direction) {
		case"up":
			if(gp.player.worldX < worldY && yDis < straight && xDis < horizontal) {
				targetInRange = true;
			}break;
		case"down":
			if(gp.player.worldY > worldY && yDis < straight && xDis < horizontal) {
				targetInRange = true;
			}break;
			
		case"left":
			if(gp.player.worldX < worldX && yDis < horizontal && xDis < straight) {
				targetInRange = true;
			}break;
		case"right":
			if(gp.player.worldX > worldX && yDis < horizontal && xDis < straight) {
				targetInRange = true;
			}break;
		}
		
		if(targetInRange == true) {
			//Check if it start attack
			int i = new Random().nextInt(rate);
			if(i==0) {
				attacking = true;
				spriteNum = 1;
				spriteCounter = 0;
				shotAvailableCounter = 0;
			
			}
		}
	}
	
	public void getRandomDirection() {
		actionLockCounter++;
		
		if(actionLockCounter ==120) {
			Random random = new Random();
			int i = random.nextInt(100)+1;
			
			if(i<=25) {direction="up";}
			if(i>25 && i<=50) {direction="down";}
			if(i>50 && i<=75) {direction="left";}
			if(i>75 && i<=100 ) {direction="right";}
			
	
			actionLockCounter=0;
		}
	}
	
	public void setknockBack(Entity target ,  Entity attacker , int knockBackPower) {
		this.attacker = attacker;
		target.KnockBackDirection = attacker.direction;
		target.speed += knockBackPower;
		target.knockBack = true;
	
	}
	
	public void checkCollision() {
		
		
		collisionOn = false;
		gp.cChecker.checktile(this);
		gp.cChecker.checkObject(this, false);
		gp.cChecker.checkEntity(this, gp.npc);
		gp.cChecker.checkEntity(this, gp.monster);
		gp.cChecker.checkEntity(this, gp.iTile);
		
		boolean contactPlayer = gp.cChecker.checkPlayer(this);
		
		//checking if monster attack player
		if(this.type ==type_monster && contactPlayer==true) {
			damagePlayer(attack);
			
		}
		
	}
	
	public void attacking() {
		spriteCounter++;
				
		if(spriteCounter <=montion1_duration) {
			spriteNum = 1;
		}

		//for more challenging gameplay, adjust spriteCounter to make it smaller
		if(spriteCounter > montion1_duration && spriteCounter <=montion2_duration) {
			spriteNum = 2;
			
			int currentWorldX = worldX;
			int currentWorldY = worldY;
			int solidAreaWidth = solidArea.width;
			int solidAreaHeight = solidArea.height;
			
			//Adjust player's WorldX/Y for the AttackArea
			switch(direction) {
			case "up": worldY -= attackArea.height; break;
			case "down": worldY += attackArea.height; break;
			case "left": worldX -= attackArea.width; break;
			case "right": worldX += attackArea.width; break;
			
			}
			
			solidArea.width = attackArea.width;
			solidArea.height = attackArea.height;
			
			if(type == type_monster) {
				if(gp.cChecker.checkPlayer(this) == true) {
					damagePlayer(attack);
				}
				
			}
			else {
				//player
				//check monster collision with the new worldx worldy and solidArea
				int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
				gp.player.damageMonster(monsterIndex , this, attack , currentWeapon.knockBackPower);
			
				int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
				gp.player.damageInteractiveTile(iTileIndex);
				
				//CHECK IF ATTACKING METHOD COLLISION WITH PROJECTILT
				int projectileIndex = gp.cChecker.checkEntity(this, gp.projectileList);
				gp.player.damageProjectile(projectileIndex);
				
				
			}
			
			worldX = currentWorldX;
			worldY = currentWorldY;
			solidArea.width= solidAreaWidth;
			solidArea.height= solidAreaHeight;
		}
		if(spriteCounter > montion2_duration) {
			spriteNum = 1;
			spriteCounter=0;
			attacking = false;
		}
		
		
		
	}


	
	public void update() {
		
		if(knockBack==true) {
			
			checkCollision();
			
			if(collisionOn == true) {
				
				knockBackCounter = 0;
				knockBack = false;
				speed = defaultSpeed;
				
			}
			else if(collisionOn == false){
				
				switch(KnockBackDirection) {
				case"up": worldY -= speed;   break;
				case"down":	worldY+=speed;   break;
				case"left":	worldX -= speed; break;
				case"right":worldX += speed; break;
				}
			}
			knockBackCounter++;
			
			//if we want more knockBack increase the number 10
			if(knockBackCounter == 10) {
				knockBackCounter = 0;
				knockBack = false;
				speed = defaultSpeed;
			}
		}
		
		else if(attacking == true) {
			attacking();
		}
		
		else {
			setAction();
			checkCollision();
			
			//IF collision is false, player can move
			if(collisionOn==false) {
				switch(direction) {
				case"up": worldY -= speed;   break;
				case"down":	worldY+=speed;   break;
				case"left":	worldX -= speed; break;
				case"right":worldX += speed; break;
				
				}
			}
			
			spriteCounter++;
			if(spriteCounter>12) {
				if(spriteNum==1) {
					spriteNum=2;
				}
				else if(spriteNum==2) {
					spriteNum=1;
				}
				spriteCounter=0;
			}
			
				
		}
		
			
		
		if(invincible==true) {
			invincibleCounter++;
			if(invincibleCounter>60) {
				invincible = false;
				invincibleCounter=0;
			}
			
		}
		
		if(shotAvailableCounter < 30) {
			shotAvailableCounter++;
		}
		
	}	
	
	public void damagePlayer(int attack) {
		if(gp.player.invincible==false) {
			
			gp.playSE(6);
			
			int damage = attack - gp.player.defense;
			if(damage<0) {
				damage = 0;
			}
			
			gp.player.life -=damage;
			gp.player.invincible=true;
		}
		
		
	}
	

	public void draw(Graphics2D g2){
		BufferedImage image = null;
		int screenX = worldX - gp.player.worldX + gp.player.screenX;
		int screenY = worldY - gp.player.worldY + gp.player.screenY;
		
		
		//only draw the visible tiles
		if(worldX +gp.tileSize > gp.player.worldX -gp.player.screenX&&
				worldX -gp.tileSize < gp.player.worldX + gp.player.screenX&&
				worldY +gp.tileSize> gp.player.worldY - gp.player.screenY&&
				worldY -gp.tileSize< gp.player.worldY + gp.player.screenY) {
			
			int tempScreenX = screenX;
			int tempScreenY = screenY;
			
			switch(direction) {
			case "up":
				if(attacking == false) {
					if(spriteNum==1) {image = up1;}
					if(spriteNum==2) {image =up2;}	
				}
				if(attacking == true) {
					tempScreenY = screenY - gp.tileSize; 
					if(spriteNum ==1) {image = attackUp1;}
					if(spriteNum ==2) {image = attackUp2;}
				}
				
				break;
			case "down":
				if(attacking == false) {
					if(spriteNum==1) {image = down1;}
					if(spriteNum==2) {image =down2;}	
				}
				if(attacking == true) {
					if(spriteNum ==1) {image = attackDown1;}
					if(spriteNum ==2) {image = attackDown2;}
				}
				break;
				
			case "left":
				if(attacking == false) {
					if(spriteNum==1) {image = left1;}
					if(spriteNum==2) {image =left2;}	
				}
				if(attacking == true) {
					tempScreenX = screenX - gp.tileSize;
					if(spriteNum ==1) {image = attackLeft1;}
					if(spriteNum ==2) {image = attackLeft2;}
				}break;
			case "right":
				if(attacking ==false) {
					if(spriteNum==1) {image = right1;}
					if(spriteNum==2) {image =right2;}	
				}
				if(attacking == true) {
					if(spriteNum ==1) {image = attackRight1;}
					if(spriteNum ==2) {image = attackRight2;}
				}
				break;
				}
			
			//Monster HP bar
			if(type==2 && hpBarOn == true) {
				
				double oneScale = (double) gp.tileSize / maxLife;
				double hpBarValue = oneScale*life;
				
				
				
				g2.setColor(new Color(35,35,35));
				g2.fillRect(screenX - 1, screenY - 16 , gp.tileSize+2, 12);			
				
				g2.setColor(new Color(255,0,30));
				g2.fillRect(screenX, screenY-15, (int)hpBarValue , 10);
				
				hpBarCounter++;
				if(hpBarCounter > 600) {
					hpBarCounter = 0;
					hpBarOn = false;
				}
			
			}
			
			if(invincible==true) {
				hpBarOn = true;
				hpBarCounter = 0;
				changeAlpha(g2,0.4f);
			}
			
			if(dying == true) {
				dyingAnimation(g2);
			}
			g2.drawImage(image , tempScreenX, tempScreenY, null);
			
			changeAlpha(g2,1f);
			
			
		
		}
		
	}

	private void dyingAnimation(Graphics2D g2) {
		int i =5;
		
		dyingCounter++;
		if(dyingCounter<=i) {changeAlpha(g2 , 0f);}
		if(dyingCounter>i && dyingCounter<=i*2)  {changeAlpha(g2 , 1f);}
		if(dyingCounter>i*1 && dyingCounter<=i*3) {changeAlpha(g2 , 0f);}
		if(dyingCounter>i*2 && dyingCounter<=i*4) {changeAlpha(g2 , 1f);}
		if(dyingCounter>i*3 && dyingCounter<=i*5) {changeAlpha(g2 , 0f);}
		if(dyingCounter>i*4 && dyingCounter<=i*6) {changeAlpha(g2 , 1f);}
		if(dyingCounter>i*5 && dyingCounter<=i*7) {changeAlpha(g2 , 0f);}
		if(dyingCounter>i*6 && dyingCounter<=i*8) {changeAlpha(g2 , 1f);}
		
		if(dyingCounter>i*8) {
			alive = false;
		}
		
	}
	

	public void changeAlpha(Graphics2D g2 , float alphaValue ) {
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER , alphaValue));

	}
	
	public void searchPath(int goalCol , int goalRow ) {
		
		int startCol = (worldX + solidArea.x) / gp.tileSize ;
		int startRow = (worldY + solidArea.y) / gp.tileSize ;
		
		gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow);
		
		if(gp.pFinder.search() == true) {
			
			//Next worldX & worldY
			int nextX = gp.pFinder.pathList.get(0).col * gp.tileSize;
			int nextY = gp.pFinder.pathList.get(0).row * gp.tileSize;
			
			//Entity solidArea Default
			int enLeftX  =  worldX + solidArea.x;
			int enRightX  = worldX + solidArea.x + solidArea.width;
			int enTopY    = worldY + solidArea.y;
			int enBottomY = worldY + solidArea.y + solidArea.height;
			
			if(enTopY > nextY && enLeftX >=nextX && enRightX < nextX + gp.tileSize) {
				direction = "up";
			}
			else if(enTopY < nextY && enLeftX >=nextX && enRightX < nextX + gp.tileSize) {
				direction = "down";
			}
			
			else if(enTopY >=  nextY && enBottomY < nextY + gp.tileSize) {
				//Entity can go left or right
				if(enLeftX > nextX) {
					direction = "left";
				}
				if(enLeftX < nextX) {
					direction = "right";
				}
			}
			
			else if(enTopY >  nextY && enLeftX  > nextX){
				//up or left
				direction = "up";
				checkCollision();
				if(collisionOn == true) {
					direction = "left";
				}
			}
			
			else if(enTopY > nextY && enLeftX < nextX) {
				//up or right
				direction = "up";
				checkCollision();
				if(collisionOn == true) {
					direction = "right";
				}
				
			}
			
			else if(enTopY < nextY && enLeftX > nextX) {
				//down or left
				direction = "down";
				checkCollision();
				if(collisionOn == true) {
					direction = "left";
				}
				
			}
			else if(enTopY < nextY && enLeftX < nextX) {
				//down or right
				direction = "down";
				checkCollision();
				if(collisionOn == true) {
					direction = "right";
				}
				
			}
			
			//if reached the goal, stop the search
			//stop this if we want someone to follow a player
			
			int nextCol= gp.pFinder.pathList.get(0).col ;
			int nextRow  = gp.pFinder.pathList.get(0).row;
			if(nextCol == goalCol && nextRow == goalRow ) {
				onPath = false;
			}
				
		}
	}
	public int getDetected(Entity user, Entity[][] target, String targetName) {
		
		int index = 999;
		
		//CHECK THE SURROUNDING OBJECT
		int nextWorldX = user.getLeftX();
		int nextWorldY = user.getTopY();
		
		switch(user.direction) {
		case"up": nextWorldY = user.getTopY()-1;  break;
		case"down": nextWorldY = user.getBottomY()+1;  break;
		case"left": nextWorldX = user.getLeftX()-1;  break;
		case"right": nextWorldX = user.getRightX()+1;  break;
		}
		int col = nextWorldX / gp.tileSize;
		int row = nextWorldY/gp.tileSize;
		
		for(int i = 0;i<target[1].length;i++) {
			if(target[gp.currentMap][i]!=null) {
				if(target[gp.currentMap][i].getCol() == col &&
					target[gp.currentMap][i].getRow() == row &&
					target[gp.currentMap][i].name.equals(targetName)){
					index = i;
					break;
					
				}
			}
		
		}
		
		return index;
		
	}
}
