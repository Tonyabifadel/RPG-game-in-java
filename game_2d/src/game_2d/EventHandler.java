package game_2d;

import entity.Entity;

public class EventHandler {
	GamePanel gp;
	EventRect eventRect[][][];
	
	int previousEventX , previousEventY;
	boolean canTouchEvent = true;
	int tempMap, tempCol ,tempRow;
	
	public EventHandler(GamePanel gp) {
		this.gp = gp;
		eventRect = new EventRect[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
		
		int map = 0;
		int col = 0;
		int row = 0;
		while(map < gp.maxMap && col < gp.maxWorldCol && row < gp.maxWorldRow) {
			
		
		
		eventRect[map][col][row] = new EventRect();
		eventRect[map][col][row].x = 23;
		eventRect[map][col][row].y = 23;
		eventRect[map][col][row].width  = 2;
		eventRect[map][col][row].height = 2;
		eventRect[map][col][row].eventRectDefaultX = eventRect[map][col][row].x;
		eventRect[map][col][row].eventRectDefaultY = eventRect[map][col][row].y;
		col++;
		if(col==gp.maxWorldCol) {
			col=0;
			row++;
			if(row ==gp.maxWorldRow) {
				row = 0;
				map++;
			}
		}
		
	}
}


	public void checkEvent() {
		//check if the player character is more than 1 tile away from the last event
		int xDistance = Math.abs(gp.player.worldX - previousEventX);
		int yDistance = Math.abs(gp.player.worldY - previousEventY);

		int distance = Math.max(xDistance, yDistance);
		
		//this if check if I can trigger the event again
		if(distance > gp.tileSize) {
			canTouchEvent =true;
		}
		if(canTouchEvent == true) {
			
			if(hit(27,16,"right",0)==true) {damagePit(gp.dialogueState);}
			//if(hit(27,16,"right")==true) {teleport(gp.dialogueState);}
			else if(hit(23,12,"up",0)==true) {healingPool(gp.dialogueState);}
			else if(hit(10,39,"any",0)==true) {teleporttoMap(1,12,13) ;}
			else if(hit(12,13,"any",1)==true) {teleporttoMap(0,10,39) ;}
			else if(hit(12,9,"up",1)==true) {speak(gp.npc[1][0]) ;}



		}
	}
	
	private void speak(Entity entity) {
		if(gp.keyH.enterPressed ==true) {
			gp.gameState = gp.dialogueState;
			gp.player.attackCanceled = true;
			entity.speak();
		}
		
	}


	public void teleporttoMap( int map , int col ,int row) {
		gp.gameState = gp.transitionState;
		tempMap =  map;
		tempCol = col;
		tempRow = row;
		
		
//		gp.currentMap = map;
//		gp.player.worldX = gp.tileSize * col;
//		gp.player.worldY = gp.tileSize * row;
//		
//		previousEventX = gp.player.worldX;
//		previousEventY = gp.player.worldY;
		canTouchEvent = false;
		gp.playSE(13);
		
	}


	public void teleport(int gameState) {

		gp.gameState = gameState;
		gp.ui.currentDialogue = "Teleport";
		gp.player.worldX = gp.tileSize*37;
		gp.player.worldY = gp.tileSize*10;
	}


	public void damagePit(int gameState) {
		gp.gameState = gameState;
		gp.playSE(6);
		gp.ui.currentDialogue = "You fell into a pet";
		gp.player.life -=1;
		canTouchEvent = false;
		
	}


	public boolean hit(int col , int row , String reqDirection , int map) {
		boolean hit = false;
		
		if(map == gp.currentMap) {
			
		
		//get player current solid Area position
		gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
		gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
		eventRect[map][col][row].x = col * gp.tileSize + eventRect[map][col][row].x;
		eventRect[map][col][row].y = row * gp.tileSize + eventRect[map][col][row].y;
		
		//check if player solid area colliding with event solid area
		if(gp.player.solidArea.intersects(eventRect[map][col][row]) && eventRect[map][col][row].eventDone==false) {
			if(gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
				hit = true;
				previousEventX = gp.player.worldX;
				previousEventY = gp.player.worldY;
				
				
			}
			
		}
		//reset the solid area x and y after collision
		gp.player.solidArea.x = gp.player.solidAreaDefaultX;
		gp.player.solidArea.y = gp.player.solidAreaDefaultY;
		eventRect[map][col][row].x = eventRect[map][col][row].eventRectDefaultX;
		eventRect[map][col][row].y = eventRect[map][col][row].eventRectDefaultY;
		
		}
		return hit;
	}
	
	public void healingPool(int gameState) {
		if(gp.keyH.enterPressed==true) {
			gp.gameState = gameState;
			gp.player.attackCanceled = true;
			gp.playSE(2);
			gp.ui.currentDialogue = "Drink this water. \nIt will heal your health and mana!\nThe progress has been saved";
			gp.player.life = gp.player.maxLife;
			gp.player.mana = gp.player.maxMana;
			gp.aSetter.setMonster();
			gp.saveLoad.save();
		}
	}
}
