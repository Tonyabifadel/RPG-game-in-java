package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import game_2d.GamePanel;
import game_2d.KeyHandler;
import game_2d.UtilityTool;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;

public class Player extends Entity{
	KeyHandler keyh;
	
	public final int screenX;
	public final int screenY;
	int standingCounter = 0;
	public boolean attackCanceled = false;
	public Player (GamePanel gp , KeyHandler keyh) {
		
		super(gp);
		this.keyh = keyh;
		
		screenX=gp.screenWidth/2 - (gp.tileSize/2);
		screenY = gp.screenHeight/2 - (gp.tileSize/2);
		
		
		solidArea = new Rectangle();
		
		solidArea.x=8;
		solidArea.y=16;
		
		solidAreaDefaultX=solidArea.x;
		solidAreaDefaultY=solidArea.y;
		solidArea.width =32 ;
		solidArea.height = 32;
		
		attackArea.width = 36;
		attackArea.height = 36;
		
		setDefaultValue();
		getPlayerImage();
		getPlayerAttackImage();
	}

	public void setDefaultValue() {	
		
		//position of player where he start
		worldX=gp.tileSize*23;
		worldY=gp.tileSize*21;
		speed=7;
		direction ="down";
		
		
	
		//Player Status
		//2 life equals 1 heart
		level = 1;
		maxLife = 8;
		life = maxLife;
		strength = 1;
		dexterity = 1;
		exp = 0;
		nextLevelExp = 5;
		coin = 0 ;
		currentWeapon = new OBJ_Sword_Normal(gp);
		currentShield = new OBJ_Shield_Wood(gp);
		attack = getAttack();
		defense = getDefense();

		
	}	

	private int getDefense() {
		// TODO Auto-generated method stub
		return  defense = dexterity * currentShield.defenseValue;
	}

	private int getAttack() {
		// TODO Auto-generated method stub
		return attack = strength * currentWeapon.attackValue;
	}

	public void getPlayerImage() {
		
		up1 = setup("/player/boy_up_1" ,gp.tileSize ,gp.tileSize);
		up2 = setup("/player/boy_up_2",gp.tileSize ,gp.tileSize);
		down1 = setup("/player/boy_down_1",gp.tileSize ,gp.tileSize);
		down2 = setup("/player/boy_down_2",gp.tileSize ,gp.tileSize);
		left1 = setup("/player/boy_left_1",gp.tileSize ,gp.tileSize);
		left2 = setup("/player/boy_left_2",gp.tileSize ,gp.tileSize);
		right1 = setup("/player/boy_right_1",gp.tileSize ,gp.tileSize);
		right2 = setup("/player/boy_right_2",gp.tileSize ,gp.tileSize);

	}
	
	public void getPlayerAttackImage() {
		attackUp1    = setup("/player/boy_attack_up_1",gp.tileSize ,gp.tileSize*2);
		attackUp2    = setup("/player/boy_attack_up_2",gp.tileSize ,gp.tileSize*2);
		attackDown1  = setup("/player/boy_attack_down_1",gp.tileSize ,gp.tileSize*2);
		attackDown2  = setup("/player/boy_attack_down_2",gp.tileSize ,gp.tileSize*2);
		attackLeft1  = setup("/player/boy_attack_left_1",gp.tileSize*2 ,gp.tileSize);
		attackLeft2  = setup("/player/boy_attack_left_2",gp.tileSize*2 ,gp.tileSize);
		attackRight1 = setup("/player/boy_attack_right_1",gp.tileSize *2,gp.tileSize);
		attackRight2 = setup("/player/boy_attack_right_2",gp.tileSize *2,gp.tileSize);
	}
	
	
	public void update() {
		if(attacking == true) {
			attacking();
		}
		
		else if(keyh.upPressed==true ||
			keyh.downPressed==true||
			keyh.leftPressed==true||
			keyh.rightPressed==true||
			keyh.enterPressed==true) {
			
		
			if(keyh.upPressed ==true) {
				direction="up";
			}
			else if(keyh.downPressed ==true) {
				direction="down";
			}
			else if(keyh.leftPressed == true) {
				direction="left";
			}
			else if(keyh.rightPressed==true) {
				direction="right";
			}
			
			//Check collision
			collisionOn = false;
			gp.cChecker.checktile(this);
			
			//check object collision
			int objIndex = gp.cChecker.checkObject(this, true);
			pickUpObject(objIndex);
			
			//Check NPC collision
			int npcIndex = gp.cChecker.checkEntity(this,gp.npc);
			interactNPC(npcIndex);
			
			//switch on npc index
			
			//Check monster collision
			int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
			contactMonster(monsterIndex);
			
			//check Event 
			gp.eHandler.checkEvent();
			

			
			//if collision false player can move
			if(collisionOn==false && keyh.enterPressed ==false) {
				switch(direction) {
				case"up": worldY -= speed;   break;
				case"down":	worldY+=speed;   break;
				case"left":	worldX -= speed; break;
				case"right":worldX += speed; break;
				
				}
			}
			if(keyh.enterPressed==true && attackCanceled == false) {
				//gp.playSE(7);
				attacking = true;
				spriteCounter = 0;
			}
			
			attackCanceled = false;
			gp.keyH.enterPressed=false;
			
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
		
		else {
			standingCounter++;
			
			if(standingCounter==20) {
				spriteNum = 1;
				standingCounter=0;
			}
		}
		
		
		//This method needs to be outside of if-statement
		if(invincible==true) {
			invincibleCounter++;
			if(invincibleCounter>60) {
				invincible = false;
				invincibleCounter=0;
			}
			
		}
		
		
		
	}
	
	private void attacking() {
		spriteCounter++;
		
		if(spriteCounter <=5) {
			spriteNum = 1;
		}
		if(spriteCounter > 5 && spriteCounter <=25) {
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
			
			//check monster collision with the new worldx worldy and solidArea
			int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
			damageMonster(monsterIndex);
		
			worldX = currentWorldX;
			worldY = currentWorldY;
			solidArea.width= solidAreaWidth;
			solidArea.height= solidAreaHeight;
		}
		if(spriteCounter > 25) {
			spriteNum = 1;
			spriteCounter=0;
			attacking = false;
		}
		
	}

	private void damageMonster(int i) {
		if(i!=999) {
			
			if(gp.monster[i].invincible ==false) {
				gp.playSE(5);
				gp.monster[i].life -= 1;
				gp.monster[i].invincible = true ;
				gp.monster[i].damageReaction();
				
				if(gp.monster[i].life <=0) {
					gp.monster[i].dying = true;
					
				}
			}
		}
		
		
	}

	private void contactMonster(int i) {
		if(i!=999) {
			if(invincible ==false) {
				gp.playSE(6);
			life -=1;
			invincible = true;
			}
		}
		
	}

	public void pickUpObject(int i) {
		//choose not a number not used in object's array index
		//999 means we did not touch anything 
		if(i != 999) {
			
				
			}
		
		
	}
	
	public void interactNPC(int i) {
		if(gp.keyH.enterPressed == true) {
			if(i != 999) {
				attackCanceled = true;
				gp.gameState = gp.dialogueState;
				gp.npc[i].speak();
					
			}
			
			
		}

		
			
	}
	
public void draw(Graphics2D g2) {
		
		BufferedImage image = null;
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
		
		
		if(invincible==true) {
			
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 0.4f));
		}
		
		g2.drawImage(image,tempScreenX ,tempScreenY ,null);
		
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 1f));

		
	
	
	}
}
