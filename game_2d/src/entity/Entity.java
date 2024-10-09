package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D; 
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

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
	
	
	public boolean attacking = false;
	
	
	String dialogues[] = new String[20];
	int dialogueIndex = 0;
	
	public BufferedImage image , image2, image3;
	public String name;
	public boolean collision = false;
	public int type; //type of entity 0-player-1-npc 2-monster
	
	//Character Status
	public int maxLife;
	public int life;
	
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
	public Entity currentWeapon;
	public Entity currentShield;
	
	public int attackValue;
	public int defenseValue;
	public Projectile projectile;
	
	public String description = "" ;
	
	public int useCost;
	
	
	
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
		
	
	
	public void update() {
		
		setAction();
		collisionOn = false;
		gp.cChecker.checktile(this);
		gp.cChecker.checkObject(this, false);
		gp.cChecker.checkEntity(this, gp.npc);
		gp.cChecker.checkEntity(this, gp.monster);
		boolean contactPlayer = gp.cChecker.checkPlayer(this);
		
		//checking if monster attack player
		if(this.type ==2 && contactPlayer==true) {
			damagePlayer(attack);
			
		}
		
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
			
			switch(direction) {
			case "up":
				if(spriteNum==1) {
					image = up1;	
				}
				if(spriteNum==2) {
					image =up2;
				}
				break;
			case "down":
				if(spriteNum==1) {
					image = down1;	
				}
				if(spriteNum==2) {
					image = down2;	
				}
				break;
				
			case "left":
				if(spriteNum==1) {
					image = left1;	
				}
				if(spriteNum==2) {
					image = left2;	
				}
				break;
			case "right":
				if(spriteNum==1) {
					image = right1;
				}
				if(spriteNum==2) {
					image = right2;
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
			g2.drawImage(image , screenX, screenY, gp.tileSize,gp.tileSize,null);
			
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
}
