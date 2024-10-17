package game_2d;

import java.awt.BasicStroke; 
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;

import entity.Entity;
import object.OBJ_Bronze_Coin;
import object.OBJ_Heart;
import object.OBJ_Key;
import object.OBJ_ManaCrystal;

public class UI {
	
	GamePanel gp;
	Graphics2D g2;
	Font arial_40 , arial_80;
	//import your own later
	//Font x , y
	BufferedImage heart_full ,heart_half, heart_blank , crystal_full,crystal_blank , coin;
	public boolean messageOn  =false;

	ArrayList<String> message = new ArrayList<>();
	ArrayList<Integer> messageCounter = new ArrayList<>();
	public boolean gameFinished =false;
	public String currentDialogue = "";
	public int commandNum = 0;
	public int titleScreenState = 0; // 0 the first screen, 1 second screen 
	
	public int playerSlotCol = 0;
	public int playerSlotRow = 0;
	public int npcSlotCol = 0;
	public int npcSlotRow = 0;
	
	int subState = 0;
	int counter = 0;
	public Entity npc;
	
	
	public UI(GamePanel gp) {
		this.gp = gp;
		arial_40 = new Font("Arial" ,Font.PLAIN ,40);
		arial_80 = new Font("Arial" ,Font.BOLD ,80);
		
		//CREATE HUD OBJECT
		Entity heart = new OBJ_Heart(gp);
		heart_full  = heart.image;
		heart_half  = heart.image2;
		heart_blank = heart.image3;
		Entity Crystal = new OBJ_ManaCrystal(gp);
		crystal_full = Crystal.image;
		crystal_blank = Crystal.image2;
		Entity bronzeCoin = new OBJ_Bronze_Coin(gp);
		coin = bronzeCoin.down1;
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
			drawDialogueSreen();
		}
		
		if(gp.gameState == gp.characterState) {
			drawCharacterScreen();
			drawCharacterInventory(gp.player,true);
		}
		
		if(gp.gameState == gp.optionsState) {
			drawOptionScreen();
		}
		
		if(gp.gameState == gp.gameOverState) {
			darwgameOverScreen();
		}

		if(gp.gameState == gp.transitionState) {
			drawTransition();
		}
		
		if(gp.gameState == gp.tradeState) {
			drawTradeScreen();
		}
		
	}

	
	
	public void drawTradeScreen() {
		switch(subState) {
		case 0: trade_select(); break;
		case 1: trade_buy(); break;
		case 2: trade_sell(); break;
		}
		gp.keyH.enterPressed = false;
		
	}


	private void trade_sell() {
		//Draw player's inventory
		drawCharacterInventory(gp.player,true);
		
		//DRAW HINT WINDOW
		int x  = gp.tileSize * 2;
		int y  = gp.tileSize * 9;
		int width = gp.tileSize*6;
		int height  = gp.tileSize * 2;
		
		drawSubWindow(x,y,width,height);
		g2.drawString("[ESC] BACK", x+24, y+60);
		
		//DRAW PLAYER COIN WINDOW
		x = gp.tileSize * 12;
		y = gp.tileSize * 9;
		width = gp.tileSize * 6;
		height = gp.tileSize * 2;
		
		
		drawSubWindow(x,y,width,height);
		g2.drawString("YOUR COIN: "+ gp.player.coin, x+24, y+60);
		
		//DRAW PRICE WINDOW
		int itemIndex = getItemIndexonSlot(playerSlotCol , playerSlotRow);
		
		if(itemIndex < gp.player.inventory.size()){
			x = (int)(gp.tileSize * 15.5);
			y = (int)(gp.tileSize * 5.5);
			width = (int)(gp.tileSize * 2.5);
			height= gp.tileSize;
			drawSubWindow(x,y,width,height);
			g2.drawImage(coin, x+10, y+8,32,32, gp);
			
			int price = gp.player.inventory.get(itemIndex).price / 2;
			String text = ""+ price;
			x = getXforAlignToRightText(text , gp.tileSize*18-20);
			g2.drawString(text, x, y+34);
			
			//Sell am item
			if(gp.keyH.enterPressed==true) {
				if(gp.player.inventory.get(itemIndex) == gp.player.currentWeapon ||
						gp.player.inventory.get(itemIndex) == gp.player.currentShield	) {
					commandNum = 0;
					subState = 0;
					gp.gameState = gp.dialogueState;
					currentDialogue = "You cannot sell an equiped item";
				}
				else {
					if(gp.player.inventory.get(itemIndex).amount>1) {
						gp.player.inventory.get(itemIndex).amount--;
					}
					else {
						gp.player.inventory.remove(itemIndex);
					}
					gp.player.coin += price;
				
					}
				
			}
		}
		
		
	}


	private void trade_buy() {
		//Draw player's inventory
		drawCharacterInventory(gp.player,false);
		
		//Draw npc inventory
		drawCharacterInventory(npc,true);
		
		//DRAW HINT WINDOW
		int x  = gp.tileSize * 2;
		int y  = gp.tileSize * 9;
		int width = gp.tileSize*6;
		int height  = gp.tileSize * 2;
		
		drawSubWindow(x,y,width,height);
		g2.drawString("[ESC] BACK", x+24, y+60);
		
		//DRAW PLAYER COIN WINDOW
		x = gp.tileSize * 12;
		y = gp.tileSize * 9;
		width = gp.tileSize * 6;
		height = gp.tileSize * 2;
		
		
		drawSubWindow(x,y,width,height);
		g2.drawString("YOUR COIN: "+ gp.player.coin, x+24, y+60);
		
		//DRAW PRICE WINDOW
		int itemIndex = getItemIndexonSlot(npcSlotCol , npcSlotRow);
		
		if(itemIndex < npc.inventory.size()){
			x = (int)(gp.tileSize * 5.5);
			y = (int)(gp.tileSize * 5.5);
			width = (int)(gp.tileSize * 2.5);
			height= gp.tileSize;
			drawSubWindow(x,y,width,height);
			g2.drawImage(coin, x+10, y+8,32,32, gp);
			
			int price = npc.inventory.get(itemIndex).price;
			String text = ""+ price;
			x = getXforAlignToRightText(text , gp.tileSize*8-20);
			g2.drawString(text, x, y+34);
			
			//Buy am item
			if(gp.keyH.enterPressed==true) {
				if(npc.inventory.get(itemIndex).price > gp.player.coin) {
					subState = 0;
					gp.gameState = gp.dialogueState;
					currentDialogue = "You need more coin!";
					drawDialogueSreen();
				}
				else {
					if(gp.player.canObtainItem(npc.inventory.get(itemIndex)) == true) {
						gp.player.coin -=npc.inventory.get(itemIndex).price;

					}
					else {
						subState = 0;
						gp.gameState = gp.dialogueState;
						currentDialogue = "You can't carry any more items!";
					}
				}
			
			}
		}
		
		
	}


	private void trade_select() {
		drawDialogueSreen();
		
		//Window
		int x = gp.tileSize * 15 ;
		int y = gp.tileSize * 4;
		int width  = gp.tileSize *3;
		int height = (int) (gp.tileSize * 3.5);
		
		drawSubWindow(x, y, width, height);
		
		//DRAW TEXTS
		x+= gp.tileSize;
		y+= gp.tileSize;
		g2.drawString("Buy", x, y);
		if(commandNum==0) {
			g2.drawString(">",x-24,y);
			if(gp.keyH.enterPressed==true) {
				subState = 1;
			}
		}
		
		y+= gp.tileSize;
		g2.drawString("Sell", x, y);
		if(commandNum==1) {
			g2.drawString(">",x-24,y);
			if(gp.keyH.enterPressed==true) {
				subState = 2;
			}
		}
		
		
		y+= gp.tileSize;
		g2.drawString("Leave", x, y);
		if(commandNum==2) {
			g2.drawString(">",x-24,y);
			if(gp.keyH.enterPressed==true) {
				commandNum = 0;
				gp.gameState = gp.dialogueState;
				currentDialogue ="Come Again!";

			}
		}
		
	}


	public void drawTransition() {
		counter++;
		g2.setColor(new Color(0,0,0,counter*5));
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		
		
		if(counter ==50) {
			counter = 0;
			gp.gameState = gp.playState;
			gp.currentMap = gp.eHandler.tempMap;
			gp.player.worldX = gp.tileSize * gp.eHandler.tempCol;
			gp.player.worldY = gp.tileSize * gp.eHandler.tempRow;
			gp.eHandler.previousEventX = gp.player.worldX;
			gp.eHandler.previousEventY = gp.player.worldY;
		}
		
	}


	public void darwgameOverScreen() {
		g2.setColor(new Color(0,0,0,150));
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		
		int x ;
		int y ;
		String text;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD , 110f));
		
		text = "Game Over";
		//SHADOW
		g2.setColor(Color.black);
		x = getXforCenteredText(text);
		y = gp.tileSize * 4;
		g2.drawString(text, x, y);
		//MAIN
		g2.setColor(Color.white);
		g2.drawString(text, x-4, y-4);
		
		//Retry
		g2.setFont(g2.getFont().deriveFont(50f));
		text = "Retry";
		x = getXforCenteredText(text);
		y += gp.tileSize * 4;
		g2.drawString(text, x, y);
		if(commandNum == 0) {
			g2.drawString(">", x-40, y);
		}
		
		
		//Quit
		text = "Quit";
		x = getXforCenteredText(text);
		y += 55;
		g2.drawString(text, x, y);
		if(commandNum == 1) {
			g2.drawString(">", x-40, y);
		}
	
	}


	private void drawOptionScreen() {
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(30F));
		
		//SUB WINDOW
		int frameX = gp.tileSize * 6;
		int frameY = gp.tileSize ;
		int frameWidth = gp.tileSize * 9; 
		int frameHeight = gp.tileSize * 10;
		
		
		drawSubWindow(frameX, frameY, frameWidth, frameHeight);
		
		
		switch(subState) {
		
		case 0: options_top(frameX , frameY); break;
		case 1: options_fullScreenNotification(frameX ,frameY); break;
		case 2: options_control(frameX , frameY); break;
		case 3: options_endGameConfirmation(frameX, frameY); break;

		}
		
		gp.keyH.enterPressed = false;
		
	}
	



	public void options_top(int frameX , int frameY) {
		int textX ; 
		int textY ;
		
		//Title
		String text = "Options";
		textX = getXforCenteredText(text);
		textY = frameY + gp.tileSize;
		g2.drawString(text, textX, textY);
		
		//FULL SCREEN ON/OFF
		textX = frameX + gp.tileSize - 20;
		textY += gp.tileSize * 2;
		g2.drawString("Full Screen", textX, textY);
		if(commandNum == 0) {
			g2.drawString(">", textX-25 , textY);
			if(gp.keyH.enterPressed ==true) {
				if(gp.fullScreenOn==false) {
					gp.fullScreenOn=true;
				}
				else if(gp.fullScreenOn==true) {
					gp.fullScreenOn=false;
				} 
					
			subState = 1;	
			}
			
			
		}
			
		//MUSIC
		textY += gp.tileSize;
		g2.drawString("Music", textX, textY);
		if(commandNum == 1) {
			g2.drawString(">", textX-25 , textY);
		}
		
		//SE
		textY += gp.tileSize;
		g2.drawString("SE", textX, textY);
		if(commandNum == 2) {
			g2.drawString(">", textX-25 , textY);
		}
		
		//CONTROL
		textY += gp.tileSize;
		g2.drawString("CONTROL", textX, textY);
		if(commandNum == 3) {
			g2.drawString(">", textX-25 , textY);
			if(gp.keyH.enterPressed ==true) {
				subState = 2;
				commandNum = 0;
			}
		}
		
		//END GAME
		textY += gp.tileSize;
		g2.drawString("END GAME", textX, textY);
		if(commandNum == 4) {
			g2.drawString(">", textX-25 , textY);
			if(gp.keyH.enterPressed==true) {
				subState = 3;
				commandNum = 0;
			}
		}
		
		//BACK
		textY += gp.tileSize * 2;
		g2.drawString("BACK", textX, textY);
		if(commandNum == 5) {
			g2.drawString(">", textX-25 , textY);
			if(gp.keyH.enterPressed ==true) {
				gp.gameState = gp.playState;
				commandNum = 0;
			}
		}
		
		//FULL SCREEN CHECK BOX
		textX = (int) frameX + (int)(gp.tileSize *4.5);
		textY = frameY + gp.tileSize *2 + 24;
		g2.setStroke(new BasicStroke(3));
		g2.drawRect(textX, textY , 24, 24);
		
		if(gp.fullScreenOn == true) {
			g2.fillRect(textX, textY, 24, 24);
		}
		
		
		//MUSIC VOLUME
		textY += gp.tileSize;
		g2.drawRect(textX, textY, 120, 24); //120/5 = 24
		int volumeWidth = 24 * gp.music.volumeScale;
		g2.fillRect(textX, textY, volumeWidth, 24);
		
		//SE VOLUME
		textY += gp.tileSize;
		g2.drawRect(textX, textY, 120, 24);
		volumeWidth = 24 * gp.se.volumeScale;
		g2.fillRect(textX, textY, volumeWidth, 24);
		
		//CONTROL VOLUME
		textY += gp.tileSize;
		g2.drawRect(textX, textY, 24, 24);
		
		//ENG GAME
		textY += gp.tileSize;
		g2.drawRect(textX, textY, 24, 24);
		
		//BACK
		textY += gp.tileSize * 2;
		g2.drawRect(textX, textY, 24, 24);
		
	
	}

	public void options_fullScreenNotification(int frameX , int frameY) {
		int textX = frameX + gp.tileSize;
		int textY = frameY + gp.tileSize *3;
		
		currentDialogue = "The change will take\neffectafter restarting\nthe game";
		
		for(String line: currentDialogue.split("\n")) {
			g2.drawString(line, textX, textY);
			textY +=40;
		}
		
		
		//Back
		textY  = frameY + gp.tileSize*9;
		g2.drawString("Back", textX, textY);
		if(commandNum == 0) {
				g2.drawString(">", textX-25, textY);
				if(gp.keyH.enterPressed ==true) {
					subState = 0;
				}
		}
		
	}
	
	public void options_control(int frameX, int frameY) {
		int textX;
		int textY;
		
		//TITLE
		String text = "Control";
		textX = getXforCenteredText(text);
		textY = frameY + gp.tileSize;
		g2.drawString(text, textX, textY);
		
		textX = frameX + gp.tileSize;
		textY += gp.tileSize;
		g2.drawString("Move", textX, textY); textY += gp.tileSize;
		g2.drawString("Confirm/Attack", textX, textY); textY += gp.tileSize;
		g2.drawString("Shoot/Cast", textX, textY); textY += gp.tileSize;
		g2.drawString("Character Screen", textX, textY); textY += gp.tileSize;
		g2.drawString("Pause", textX, textY); textY += gp.tileSize;
		g2.drawString("Option", textX, textY); textY += gp.tileSize;
		
		textX = frameX +gp.tileSize *6;
		textY = frameY + gp.tileSize * 2;
		
		g2.drawString("WASD", textX, textY); textY += gp.tileSize;
		g2.drawString("ENTER", textX, textY); textY += gp.tileSize;
		g2.drawString("F", textX, textY); textY += gp.tileSize;
		g2.drawString("C", textX, textY); textY += gp.tileSize;
		g2.drawString("P", textX, textY); textY += gp.tileSize;
		g2.drawString("ESC", textX, textY); textY += gp.tileSize;
		
		//Back
		textX = frameX +gp.tileSize;
		textY = frameY + gp.tileSize * 9;
		g2.drawString("Back", textX, textY);
		if(commandNum==0) {
			g2.drawString(">", textX -10, textY);
			if(gp.keyH.enterPressed==true) {
				subState = 0;
				commandNum = 3;
			}
		}
		
	}

	private void options_endGameConfirmation(int frameX, int frameY) {
		
		int textX = frameX + gp.tileSize;
		int textY = frameY + gp.tileSize * 3;
		
		currentDialogue = "Quit the game and \nreturn to the title Screen";
		
		for(String line: currentDialogue.split("\n")) {
			g2.drawString(line, textX, textY);
			textY +=40;
		}
		
		//Yes
		String text = "Yes";
		textX = getXforCenteredText(text);
		textY += gp.tileSize * 3;
		g2.drawString(text, textX, textY);
		if(commandNum ==0 ){
			g2.drawString(">", textX -25 , textY);
			if(gp.keyH.enterPressed==true) {
				subState = 0;
				gp.gameState = gp.titleState;
				gp.ui.titleScreenState = 0;
			}
			
		}
		//No
		
		text = "No";
		textX = getXforCenteredText(text);
		textY += gp.tileSize;
		g2.drawString(text, textX, textY);
		if(commandNum == 1){
			g2.drawString(">", textX - 25 , textY);
			if(gp.keyH.enterPressed==true) {
				subState = 0;
				commandNum = 4;
			}
			
		}
		
	}
	
	private void drawCharacterInventory(Entity entity, boolean cursor) {  
		int frameX = 0;
		int frameY = 0;
		int frameWidth = 0;
		int frameHeight = 0;
		int slotCol = 0;
		int slotRow = 0;
		
		
		if(entity == gp.player) {
			
			//Frame
			frameX = gp.tileSize* 12 ;
			frameY = gp.tileSize;
			frameWidth = gp.tileSize * 6;
			frameHeight = gp.tileSize * 5;
			slotCol =  playerSlotCol;
			slotRow = playerSlotRow;
			
		}
		else {
			frameX = gp.tileSize* 2 ;
			frameY = gp.tileSize;
			frameWidth = gp.tileSize * 6;
			frameHeight = gp.tileSize * 5;
			slotCol =  npcSlotCol;
			slotRow = npcSlotRow;
			
		}
		
		

		
		drawSubWindow(frameX, frameY, frameWidth, frameHeight);
		
		//20 SLOT FOR NOW
		final int slotXstart = frameX + 20;
		final int slotYstart = frameY + 20;
		int slotX = slotXstart;
		int slotY = slotYstart;
		int slotSize = gp.tileSize + 3; 
		
		
	
		//Draw Player's Items
		for(int i =0;i<entity.inventory.size();i++) {
			
			//Equip Cursor
			if(entity.inventory.get(i) == entity.currentShield || entity.inventory.get(i) == entity.currentWeapon) {
				g2.setColor(new Color(240,190,90));
				g2.fillRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10,10);
				

			}
			
			g2.drawImage(entity.inventory.get(i).down1 , slotX , slotY , null);
			
			//Display Amount
			if(entity == gp.player && entity.inventory.get(i).amount > 1) {
				
				g2.setFont(g2.getFont().deriveFont(23f));
				int amountX ;
				int amountY ;
				
				String s = "" + entity.inventory.get(i).amount;
				amountX = getXforAlignToRightText(s , slotX + 44);
				amountY = slotY + gp.tileSize;
				
				//Shadow
				g2.setColor(new Color(60,60,60));
				g2.drawString(s, amountX, amountY);
				
				//Number
				g2.setColor(Color.white);
				g2.drawString(s, amountX -3, amountY-3);
			}
			slotX += slotSize;
			
			if(i == 4 || i == 9 || i== 14) {
				slotX = slotXstart;
				slotY += slotSize;
			}
			
		}
		
		
		

		if(cursor==true) {
			
		
			//Drawing cursor
			int cursorX = slotXstart + (slotSize * slotCol);
			int cursorY  = slotYstart + (slotSize * slotRow);
			int cursorWidth = gp.tileSize;
			int cursorHeight = gp.tileSize;
			
			
			//Draw Cursor
			g2.setColor(Color.white);
			g2.setStroke(new BasicStroke(3));
			g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight , 10,10);
		
			//Desc Frame
			int dFrameX = frameX;
			int dFrameY = frameY + frameHeight;
			int dFrameWidth = frameWidth;
			int dFrameHeight = gp.tileSize*3;
			
			
			//Draw Desc Text 
			int textX = dFrameX + 20;
			int textY = dFrameY + gp.tileSize;
			g2.setFont(g2.getFont().deriveFont(28F));
			int itemIndex = getItemIndexonSlot(slotCol ,slotRow);
			
			if(itemIndex < entity.inventory.size()) {
				drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);
		
				for(String line: entity.inventory.get(itemIndex).description.split("\n")){
					g2.drawString(line, textX, textY);
					textY +=32;
				}
			}
			
		}
		
		
	}
 
	public int getItemIndexonSlot(int slotCol , int slotRow) {  
		int itemIndex = slotCol + (slotRow*5);
		return itemIndex;
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
		final int frameX = gp.tileSize * 2;
		final int frameY = gp.tileSize;
		final int frameWidth = gp.tileSize * 6;
		final int frameHeight = gp.tileSize * 10;
		drawSubWindow(frameX, frameY, frameWidth, frameHeight);
		
		//TEXT
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(30F));
		
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
		g2.drawString(value, tailX -5, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.mana +"/"+gp.player.maxMana );
		textX = getXforAlignToRightText(value,tailX);
		g2.drawString(value, tailX-5, textY);
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

		g2.drawImage(gp.player.currentWeapon.down1,tailX - 10, textY - 24 , null);
		textY += gp.tileSize;
		g2.drawImage(gp.player.currentShield.down1,tailX - 10 , textY - 24 , null);
		
		
		
		
		
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
		 
		 //Draw Max MANA
		 x = (gp.tileSize/2) - 5;
		 y = (int) (gp.tileSize*1.5) ;
		 i=0;
		 
		 while(i<gp.player.maxMana) {
			 g2.drawImage(crystal_blank, x, y, null);
			 i++;
			 x+= 35;
		 }
		 
		 //DRAW MANA
		 x = (gp.tileSize/2) - 5;
		 y = (int) (gp.tileSize*1.5) ;
		 i=0;
		 
		 while(i<gp.player.mana) {
			 g2.drawImage(crystal_full, x, y, null);
			 i++;
			 x+= 35;
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

	public void drawDialogueSreen() {
	//Dialogue window
	int x = gp.tileSize*3;
	int y = gp.tileSize/2;
	int width = gp.screenWidth - (gp.tileSize*6);
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
