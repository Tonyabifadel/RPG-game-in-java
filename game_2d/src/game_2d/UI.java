package game_2d;

import java.awt.BasicStroke; 
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;

import entity.Entity;
import object.OBJ_Heart;
import object.OBJ_Key;

public class UI {
	
	GamePanel gp;
	Graphics2D g2;
	Font arial_40 , arial_80;
	//import your own later
	//Font x , y
	BufferedImage heart_full ,heart_half, heart_blank;
	public boolean messageOn  =false;

	ArrayList<String> message = new ArrayList<>();
	ArrayList<Integer> messageCounter = new ArrayList<>();
	public boolean gameFinished =false;
	public String currentDialogue = "";
	public int commandNum = 0;
	public int titleScreenState = 0; // 0 the first screen, 1 second screen 
	
	
	
	public UI(GamePanel gp) {
		this.gp = gp;
		arial_40 = new Font("Arial" ,Font.PLAIN ,40);
		arial_80 = new Font("Arial" ,Font.BOLD ,80);
		
		//CREATE HUD OBJECT
		Entity heart = new OBJ_Heart(gp);
		heart_full  = heart.image;
		heart_half  = heart.image2;
		heart_blank = heart.image3;
		
		
		/*InputStream is = getClass().getResourcesAsStream('.file path to font');
		 * try{
		 * x = Font.createFont(Font.TRUETYPE_FONT , is)
		 * y = Font.createFont(Font.TRUETYPE_FONT , is)
		 * }catch(FontFormatException e){
		 * e.printStackStrace();
		 * }
		 * 
		 * */
	}
	
	
	public void addMessage(String text) {
		message.add(text);
		messageCounter.add(0);
	}
	
	
	public void draw(Graphics2D g2){
		this.g2 = g2;
		//g2.setFont(x);
		//g2.setFont(y);
		
		g2.setFont(arial_40);
		g2.setColor(Color.white);
		
		//Title state
		if(gp.gameState == gp.titleState) {
			drawTitleScreen();
		}
		
		//Play State
		if(gp.gameState == gp.playState) {
			//Do play state
			drawPlayerLife();
			drawMessage();
		}
		
		//Pause State
		if(gp.gameState == gp.pauseState){
			drawPlayerLife();
			drawPauseScreen();
		}
		
		//Dialogue State
		if(gp.gameState == gp.dialogueState) {
			drawPlayerLife();
			drawDialogueState();
		}
		
		if(gp.gameState == gp.characterState) {
			drawCharacterScreen();
		}
	}

	
	
	private void drawMessage() {
		int messageX  = gp.tileSize ;
		int messageY  = gp.tileSize * 4 ;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,32F));
		
		for(int i = 0;i<message.size();i++) {
			
			if(message.get(i)!= null) {
				g2.setColor(Color.black);
				g2.drawString(message.get(i), messageX + 2 , messageY + 2);
				g2.setColor(Color.white);
				g2.drawString(message.get(i), messageX , messageY);
				
				int counter = messageCounter.get(i) + 1; //messageCounter++
				messageCounter.set(i , counter); //set the counter in the arrayList
				messageY += 50;
				
				if(messageCounter.get(i) > 180) {
					message.remove(i);
					messageCounter.remove(i);
				}
			}
		}
		
		
	}


	private void drawCharacterScreen() {
		//Create a sub window
		final int frameX = gp.tileSize;
		final int frameY = gp.tileSize;
		final int frameWidth = gp.tileSize * 6;
		final int frameHeight = gp.tileSize * 10;
		drawSubWindow(frameX, frameY, frameWidth, frameHeight);
		
		//TEXT
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(32F));
		
		int textX = frameX + 20;
		int textY = frameY + gp.tileSize;
		final int lineHeight = 35;
		
		drawCharacterOnWindom("Level", textX, textY);
		textY += lineHeight;
		drawCharacterOnWindom("Life", textX, textY );
		textY += lineHeight;
		drawCharacterOnWindom("Strenght", textX, textY );
		textY += lineHeight;
		drawCharacterOnWindom("Dexterity", textX, textY );
		textY += lineHeight;
		drawCharacterOnWindom("Attack", textX, textY );
		textY += lineHeight;
		drawCharacterOnWindom("Defense", textX, textY );
		textY += lineHeight;
		drawCharacterOnWindom("Exp", textX, textY );
		textY += lineHeight;
		drawCharacterOnWindom("Next Level", textX, textY );
		textY += lineHeight;
		drawCharacterOnWindom("Coin", textX, textY );
		textY += lineHeight + 20;
		drawCharacterOnWindom("Weapon", textX, textY );
		textY += lineHeight + 15;
		drawCharacterOnWindom("Shield", textX, textY);
		textY += lineHeight;
			
		int tailX = (frameX + frameWidth)  - 56 ;
		textY = frameY + gp.tileSize;
		String value;
		
		value = String.valueOf(gp.player.level);
		textX = getXforAlignToRightText(value,tailX);
		g2.drawString(value, tailX, textY);
		textY += lineHeight;

		value = String.valueOf(gp.player.life +"/"+gp.player.maxLife );
		textX = getXforAlignToRightText(value,tailX);
		g2.drawString(value, tailX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.strength);
		textX = getXforAlignToRightText(value,tailX);
		g2.drawString(value, tailX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.dexterity);
		textX = getXforAlignToRightText(value,tailX);
		g2.drawString(value, tailX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.attack);
		textX = getXforAlignToRightText(value,tailX);
		g2.drawString(value, tailX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.defense);
		textX = getXforAlignToRightText(value,tailX);
		g2.drawString(value, tailX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.exp);
		textX = getXforAlignToRightText(value,tailX);
		g2.drawString(value, tailX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.nextLevelExp);
		textX = getXforAlignToRightText(value,tailX);
		g2.drawString(value, tailX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.coin);
		textX = getXforAlignToRightText(value,tailX);
		g2.drawString(value, tailX, textY);
		
		textY += lineHeight;

		g2.drawImage(gp.player.currentWeapon.down1,tailX - 10, textY - 13 , null);
		textY += gp.tileSize;
		g2.drawImage(gp.player.currentShield.down1,tailX - 10 , textY - 13 , null);
		
		
		
		
		
	}
	

	public int getXforAlignToRightText(String text , int tailX) {
		int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = tailX - length;
		return x;
	}

	
	public void drawCharacterOnWindom(String s , int textX , int textY) {
		g2.drawString(s, textX, textY);
	}


	private void drawPlayerLife() {

		//starting x and y
		int x = gp.tileSize/2;
		int y = gp.tileSize/2;
		int i =0;
		
		
		//Draw Blank Heart
		while(i<gp.player.maxLife/2) {
			g2.drawImage(heart_blank, x, y, null);
			i++;
			x+= gp.tileSize;
		}
		
		//Reset Values
		 x = gp.tileSize/2;
		 y = gp.tileSize/2;
		 i = 0;
		 
		 //Draw Current Life
		 while(i<gp.player.life) {
			 g2.drawImage(heart_half, x, y, null);
			 i++;
			 if(i< gp.player.life) {
				 g2.drawImage(heart_full, x, y, null);
			 }
			 i++;
			 x+=gp.tileSize;
		 }
	}


	public void drawTitleScreen() {
		//Title Name
		
		if(titleScreenState == 0) {
			
		
		g2.setColor(new Color(0,0,0));
		g2.fillRect(0, 0, gp.screenWidth,gp.screenHeight);
		
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,75F));
		String text = "Blue Tony adventure";
		int x = getXforCenteredText(text) ;
		int y = gp.tileSize*3;
		

		//Shadow
		g2.setColor(Color.gray);
		g2.drawString(text, x+5, y+5);
		
		//Main Color
		
		g2.setColor(Color.white);
		g2.drawString(text, x, y);
		
		//Blue Tons Image
		x = gp.screenWidth/2 - (gp.tileSize*2)/2;
		y += gp.tileSize*2;
		g2.drawImage(gp.player.down1 , x , y , gp.tileSize*2 , gp.tileSize*2 , null);
		
		//Menu
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,48F));
		
		
		text = "NEW GAME";
		x = getXforCenteredText(text);
		y +=gp.tileSize*3.5;
		g2.drawString(text, x, y);
		
		if(commandNum ==0) {
			g2.drawString(">", x - gp.tileSize, y);
		}

		text = "LOAD GAME";
		x = getXforCenteredText(text);
		y +=gp.tileSize;
		g2.drawString(text, x, y);
		
		if(commandNum ==1) {
			g2.drawString(">", x - gp.tileSize, y);
		}


		text = "QUIT";
		x = getXforCenteredText(text);
		y +=gp.tileSize;
		g2.drawString(text, x, y);
		
		if(commandNum ==2) {
			g2.drawString(">", x - gp.tileSize, y);
		}
	}
		
		else if(titleScreenState ==1) {
			g2.setColor(Color.white);
			g2.setFont(g2.getFont().deriveFont(42F));
			
			String text = "Select your Class!";
			int x = getXforCenteredText(text);
			int y = gp.tileSize*3;
			g2.drawString(text, x, y);
			
			
			text  = "Fighter";
			x = getXforCenteredText(text);
			y += gp.tileSize*3;
		    g2.drawString(text, x, y);
		    if(commandNum==0) {
		    	g2.drawString(">", x-gp.tileSize, y);
		    }
		    
		    text  = "Thief";
			x = getXforCenteredText(text);
			y += gp.tileSize;
		    g2.drawString(text, x, y);
		    if(commandNum==1) {
		    	g2.drawString(">", x-gp.tileSize, y);
		    }
		    
		    text  = "Sorcerer";
			x = getXforCenteredText(text);
			y += gp.tileSize;
		    g2.drawString(text, x, y);
		    if(commandNum==2) {
		    	g2.drawString(">", x-gp.tileSize, y);
		    }
		    
		    text  = "Back";
			x = getXforCenteredText(text);
			y += gp.tileSize*2;
		    g2.drawString(text, x, y);
		    if(commandNum==3) {
		    	g2.drawString(">", x-gp.tileSize, y);
		    }
		}
	}


	public void drawPauseScreen() {
	g2.setFont(g2.getFont().deriveFont(Font.PLAIN,80F));
	String text = "PAUSED";
	int x  = getXforCenteredText(text);
	int y  = gp.screenHeight/2;
	g2.drawString(text, x, y);
	
}

	public void drawDialogueState() {
	//Dialogue window
	int x = gp.tileSize*2;
	int y = gp.tileSize/2;
	int width = gp.screenWidth - (gp.tileSize*4);
	int height = gp.tileSize*4;
	drawSubWindow(x,y,width,height);
	
	g2.setFont(g2.getFont().deriveFont(Font.PLAIN , 28F));
	 x += gp.tileSize;
	 y += gp.tileSize;
	 
	 for(String Line : currentDialogue.split("\n")) {
		 g2.drawString(Line, x, y);
		y+=40;	 
	 }
	 
	
}

	public void drawSubWindow(int x , int y , int width, int height) {	
	
		Color c = new Color(0,0,0,210);
		g2.setColor(c);
		g2.fillRoundRect(x, y, width, height , 35,35);
		
		
		c = new Color(255,255,255);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x+5 , y+5 , width-10, height-10,25,25);
	}
	
	
	
	public int getXforCenteredText(String text) {
		int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = gp.screenWidth/2 - length/2;
		return x;
	}
	

}
