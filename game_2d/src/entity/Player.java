package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import game_2d.GamePanel;
import game_2d.KeyHandler;
import game_2d.UtilityTool;
import object.OBJ_Axe;
import object.OBJ_Fireball;
import object.OBJ_Key;
import object.OBJ_Rock;
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
		solidArea.width =32 ;
		solidArea.height = 32;
		
		solidAreaDefaultX=solidArea.x;
		solidAreaDefaultY=solidArea.y;
//		attackArea.width = 36;
//		attackArea.height = 36;
		
		maxMana = 4;
		mana = maxMana;
		ammo = 10;
		
		setDefaultValue();
		getPlayerImage();
		getPlayerAttackImage();
		setItems();
	}

	public void setDefaultValue() {	 
		
		//position of player where he start
		
		worldX=gp.tileSize*23;
		worldY=gp.tileSize*21;
	
//		worldX=gp.tileSize*12;
//		worldY=gp.tileSize*10;
		
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
		coin = 200 ;
		defaultSpeed = 7;
		speed=defaultSpeed;
		currentWeapon = new OBJ_Sword_Normal(gp);
		currentShield = new OBJ_Shield_Wood(gp);
		projectile = new OBJ_Fireball(gp);
//		projectile = new OBJ_Rock(gp);

		attack = getAttack();
		defense = getDefense();
		
		
	}	
	public void setDefaultPosition() {
		worldX=gp.tileSize*23;
		worldY=gp.tileSize*21;
		direction ="down";
	}
	
	public void restoreLifeAndMana() {
		life = maxLife;
		mana = maxMana;
		invincible = false;
	}

	public void setItems() {
		inventory.clear();
		inventory.add(currentWeapon);
		inventory.add(currentShield);
		inventory.add(new OBJ_Axe(gp));
		inventory.add(new OBJ_Key(gp));

		
	}
	private int getDefense() {
		// TODO Auto-generated method stub
		return  defense = dexterity * currentShield.defenseValue;
	}

	private int getAttack() {
		attackArea = currentWeapon.attackArea;
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
		if(currentWeapon.type == type_sword) {
			
		
		attackUp1    = setup("/player/boy_attack_up_1",gp.tileSize ,gp.tileSize*2);
		attackUp2    = setup("/player/boy_attack_up_2",gp.tileSize ,gp.tileSize*2);
		attackDown1  = setup("/player/boy_attack_down_1",gp.tileSize ,gp.tileSize*2);
		attackDown2  = setup("/player/boy_attack_down_2",gp.tileSize ,gp.tileSize*2);
		attackLeft1  = setup("/player/boy_attack_left_1",gp.tileSize*2 ,gp.tileSize);
		attackLeft2  = setup("/player/boy_attack_left_2",gp.tileSize*2 ,gp.tileSize);
		attackRight1 = setup("/player/boy_attack_right_1",gp.tileSize *2,gp.tileSize);
		attackRight2 = setup("/player/boy_attack_right_2",gp.tileSize *2,gp.tileSize);
	}
		
	if(currentWeapon.type == type_axe) {
		
		
		attackUp1    = setup("/player/boy_axe_up_1",gp.tileSize ,gp.tileSize*2);
		attackUp2    = setup("/player/boy_axe_up_2",gp.tileSize ,gp.tileSize*2);
		attackDown1  = setup("/player/boy_axe_down_1",gp.tileSize ,gp.tileSize*2);
		attackDown2  = setup("/player/boy_axe_down_2",gp.tileSize ,gp.tileSize*2);
		attackLeft1  = setup("/player/boy_axe_left_1",gp.tileSize*2 ,gp.tileSize);
		attackLeft2  = setup("/player/boy_axe_left_2",gp.tileSize*2 ,gp.tileSize);
		attackRight1 = setup("/player/boy_axe_right_1",gp.tileSize *2,gp.tileSize);
		attackRight2 = setup("/player/boy_axe_right_2",gp.tileSize *2,gp.tileSize);
	}
		
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
			
			gp.cChecker.checkEntity(this, gp.iTile);
			
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
		
		
		// projectile.alive == false means you can only shoot one at a time 
		if(gp.keyH.shotKeyPressed == true && projectile.alive == false && shotAvailableCounter == 30 && projectile.haveResource(this) == true) {
			
			//Set default coordinates , direction and user
			projectile.set(worldX , worldY , direction , true , this);
			
			//Subtract cost after shooting
			projectile.subtractResource(this);

			for(int i = 0; i<gp.projectileList[1].length;i++) {
				if(gp.projectileList[gp.currentMap][i]==null) {
					gp.projectileList[gp.currentMap][i] = projectile;
					break;
				}
			}
				
			shotAvailableCounter = 0;
			gp.playSE(10);
		}
		
		//This method needs to be outside of if-statement
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
		
		if(life > maxLife) {life = maxLife;}
		if(mana > maxMana) {mana = maxMana;}
		if(life<=0) {
			gp.gameState = gp.gameOverState;
			gp.ui.commandNum = -1;
			gp.stopMusic();
			gp.playSE(12);
		}
		
		
		
	}
	
	private void attacking() {
		spriteCounter++;
				
		if(spriteCounter <=5) {
			spriteNum = 1;
		}

		//for more challenging gameplay, adjust spriteCounter to make it smaller
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
			damageMonster(monsterIndex , attack , currentWeapon.knockBackPower);
		
			int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
			damageInteractiveTile(iTileIndex);
			
			//CHECK IF ATTACKING METHOD COLLISION WITH PROJECTILT
			int projectileIndex = gp.cChecker.checkEntity(this, gp.projectileList);
			damageProjectile(projectileIndex);
			
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

	private void damageProjectile(int i) {
		
		if(i!=999) {
			Entity projectile = gp.projectileList[gp.currentMap][i];
			projectile.alive = false;
			generateParticle(projectile, projectile);
			
		}
		
	}

	private void damageInteractiveTile(int iTileIndex) {
		
		if(iTileIndex!=999 && gp.iTile[gp.currentMap][iTileIndex].destructible == true
			&& gp.iTile[gp.currentMap][iTileIndex].isCorrectItem(this) == true
			&& gp.iTile[gp.currentMap][iTileIndex].invincible == false) {
			
			gp.iTile[gp.currentMap][iTileIndex].playSE();
			gp.iTile[gp.currentMap][iTileIndex].life --;
			gp.iTile[gp.currentMap][iTileIndex].invincible = true;
			
			generateParticle(gp.iTile[gp.currentMap][iTileIndex] , gp.iTile[gp.currentMap][iTileIndex]);
			
			if(gp.iTile[gp.currentMap][iTileIndex].life ==0) {
				
			
			gp.iTile[gp.currentMap][iTileIndex]= gp.iTile[gp.currentMap][iTileIndex].getDestroyedForm();
		}
	}
	
}

	public void damageMonster(int i , int attack , int knockBackPower) {
		if(i!=999) {
			
			if(gp.monster[gp.currentMap][i].invincible ==false) {
				gp.playSE(5);
				
				if(knockBackPower > 0) {
					knockBack(gp.monster[gp.currentMap][i] , knockBackPower);

				}
				
				int damage = attack - gp.monster[gp.currentMap][i].defense;
				if(damage < 0) {
					damage = 0;
				}

				
				gp.monster[gp.currentMap][i].life -= damage;
				gp.ui.addMessage(damage +" damage!");
				gp.monster[gp.currentMap][i].invincible = true;
				gp.monster[gp.currentMap][i].damageReaction();
				
				if(gp.monster[gp.currentMap][i].life <=0) {
					gp.monster[gp.currentMap][i].dying = true;
					gp.ui.addMessage("Killed the "+gp.monster[gp.currentMap][i].name + "!");
					gp.ui.addMessage("Exp "+gp.monster[gp.currentMap][i].exp);
					exp += gp.monster[gp.currentMap][i].exp;
					checkLevelUp();
				}
			}
		}
		
		
	}

	private void checkLevelUp() {
		if(exp>=nextLevelExp) {
			level++;
			nextLevelExp= nextLevelExp *2;
			maxLife+=2;
			strength++;
			dexterity++;
			attack = getAttack();
			defense = getDefense();
			
			gp.playSE(8);
			gp.gameState = gp.dialogueState;
			gp.ui.currentDialogue = "You are level " + level + " now!\n";
		}
		
	}

	private void contactMonster(int i) {
		if(i!=999) {
			if(invincible ==false && gp.monster[gp.currentMap][i].dying == false) {
				gp.playSE(6);
				int damage = gp.monster[gp.currentMap][i].defense - attack ;
				if(damage < 0) {
					damage = 0;
				}
				
			life -=1;
			invincible = true;
			}
		}
		
	}

	public void pickUpObject(int i) {
		//choose not a number not used in object's array index
		//999 means we did not touch anything 
		
		if(i!=999) {
			
		
		//PickUp objects
		if(gp.obj[gp.currentMap][i].type == type_pickUp_Only) {
			
			gp.obj[gp.currentMap][i].use(this);
			gp.obj[gp.currentMap][i] = null;
		}
		
		else if(gp.obj[gp.currentMap][i].type == type_Obstacle) {
			if(gp.keyH.enterPressed == true) {
				attackCanceled = true;
				gp.obj[gp.currentMap][i].interact();
			}
		}
		//inventory items
		else {
			String text ;
			if(inventory.size() != inventoryMaxSize){
				inventory.add(gp.obj[gp.currentMap][i]);
				gp.playSE(1);
				text = "GOT A " + gp.obj[gp.currentMap][i].name +"!";
				
			}
			else {
				text = "INVENTORY FULL";
			}
				
			gp.ui.addMessage(text);
			gp.obj[gp.currentMap][i] = null;
			
		}
	
	}
	}
	
	public void interactNPC(int i) {
		if(gp.keyH.enterPressed == true) {
			if(i != 999) {
				attackCanceled = true;
				gp.gameState = gp.dialogueState;
				gp.npc[gp.currentMap][i].speak();
					
			}
			
			
		}
		
	}
	
	public void knockBack(Entity entity , int knockBackPower) {
		entity.direction = direction;
		entity.speed += knockBackPower;
		entity.knockBack = true;
	}
	
	public void selectItem() {
		
		int itemIndex = gp.ui.getItemIndexonSlot(gp.ui.playerSlotCol , gp.ui.playerSlotRow);
		
		if(itemIndex < inventory.size()) {
			Entity selectedItem = inventory.get(itemIndex);

			if(selectedItem.type == type_sword || selectedItem.type == type_axe) {
				currentWeapon = selectedItem;
				attack = getAttack();
				getPlayerAttackImage();
			}
			
			if(selectedItem.type == type_shield) {
				currentShield= selectedItem;
				defense = getDefense();
			}
			
			if(selectedItem.type == type_consumable) {
				
			if(selectedItem.use(this) == true) {
				inventory.remove(itemIndex);
			}
			
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
