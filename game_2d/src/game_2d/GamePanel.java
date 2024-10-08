package game_2d;

import java.awt.Color; 
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

import entity.Entity;
import entity.Player;
import tiles.TileManager;

public class GamePanel extends JPanel implements Runnable{
	//Screen settings
	final int originalTileSize = 16;//16x16 tile size
									//by default we use 16x16 in 2d game but 16x16 might look small on screen so we need to scale it
	final int scale = 3;
	
	public final int  tileSize = originalTileSize * scale; //48x48 tile
	public final int maxScreenCol =16;
	public final int maxScreenRow =16;
	public final int screenWidth = tileSize * maxScreenCol;  //768 pixels
	public final int screenHeight = tileSize * maxScreenRow; //576 pixels
	
	//World Settings
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;
	
	int FPS = 60;
	
	//System
	TileManager tileM = new TileManager(this);
	public KeyHandler keyH = new KeyHandler(this);
	Sound music = new Sound();
	Sound se = new Sound();
	
	Thread gameThread;
	public CollisionChecker cChecker   = new CollisionChecker(this);
	public AssetSetter aSetter = new AssetSetter(this);
	public UI ui = new UI(this);
	public EventHandler eHandler = new EventHandler(this);
	
	//Entity and Object
	public Player player = new Player(this,keyH);
	//the number 10 means that we can show only 10 objects at the same time
	//only 10 at 1 time
	public Entity obj[] = new Entity[10];
	public Entity npc[] = new Entity[10];
	public Entity monster[] = new Entity[25];
	ArrayList<Entity> entityList = new ArrayList<Entity>();
	
	//GAME STATE	
	public int gameState;
	public final int titleState = 0;
	public final int playState=1;
	public final int pauseState=2;
	public final int dialogueState =3;
	public final int characterState =4;


	public GamePanel() {
		this.setPreferredSize(new Dimension (screenWidth , screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		//focus to receive game key input 
		this.setFocusable(true);
		
	}
	
	public void setupGame() {
		aSetter.setObject();
		aSetter.setNPC();
		aSetter.setMonster();
		gameState = titleState;
	}

	public void StartGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	
	
	//here we will create the game loop
	//2 things to do:
	//this is one way to create a decent game loop
	public void run() {
		//this means draw the screen every 0.01666 seconds
		double drawInterval = 1000000000/FPS;
		double nextDrawTime = System.nanoTime()+drawInterval; 
		
		while(gameThread !=null) {
			
			// update the information of the player
			update();
			
			// draw the screen with new information
					
			repaint();
			try {
			double remainingTime = nextDrawTime - System.nanoTime();
			remainingTime = remainingTime/1000000;
			
			if(remainingTime <0) {
				remainingTime=0;
			}
			
				Thread.sleep((long) remainingTime );
				nextDrawTime += drawInterval; 
			}catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
//	//another way to create a game loop
//	  
//	  public void run(){
//	  double drawInterval = 1000000000/FPS;
//	  double delta = 0;
//	  long lastTime = System.nanoTime();
//	  long currentTime;
//	  long timer =0;
//	  long drawCount = 0;
//	  
//	  while(gameThread!=null){
//		  
//		  currentTime=System.nanoTime();
//		  
//		  delta += (currentTime - lastTime) /drawInterval;
//		  timer += (currentTime - lastTime);
//		  lastTime = currentTime;
//		  
//		  
//		  if(delta>=1) {
//			  update();
//			  repaint();
//			  delta--;
//			  drawCount++;
//		  }
//		  
//		  if(timer>=1000000000) {
//			  System.out.println("FPS is" + drawCount);
//			  drawCount=0;
//			  timer=0;
//		  }
//		 
//	  
//	  }
//	  
//	  }
	  
	  
	 
	
	
	public void update() {
		if(gameState==playState) {
			//Player
			player.update();
			
			//NPC
			for(int i =0;i<npc.length;i++) {
				
				if(npc[i]!=null) {
					npc[i].update();
				}
			}
			
			for(int i = 0 ;i<monster.length;i++) {
				if(monster[i]!=null) {
					if(monster[i].alive ==true && monster[i].dying == false) {
						monster[i].update();
					}
					if(monster[i].alive ==false) {
						monster[i] =null;
					}
					
				}
			}
			
		}
		if(gameState==pauseState) {
			//nothing
			
		}
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		long drawStart = 0;
		if(keyH.showDebugText ==true) {
			drawStart = System.nanoTime();
		}
		
		if(gameState == titleState) {
			ui.draw(g2);
			
		}
		else {
			
		
		
		//Tile Draw
		tileM.draw(g2);
	
		//add entity to list
		entityList.add(player);
		
		for(int i=0;i<npc.length;i++) {
			if(npc[i]!=null) {
				entityList.add(npc[i]);
			}
		}
		
		for(int i = 0;i<obj.length;i++) {
			if(obj[i]!=null) {
				entityList.add(obj[i]);
			}
		}
		
		for(int i = 0;i<monster.length;i++) {
			if(monster[i]!=null) {
				entityList.add(monster[i]);
			}
		}
		
		Collections.sort(entityList , new Comparator<Entity>() {
			public int compare(Entity e1 ,Entity e2) {
				int result = Integer.compare(e1.worldY,e2.worldY); 
				
				return result;
			}
		});
		
		
		//Draw Entities
		for(int i = 0;i<entityList.size();i++) {
			entityList.get(i).draw(g2);
		}
		
		//reset list after drawing
		entityList.clear();
		
		//UI
		ui.draw(g2);
		
		}
		
		if(keyH.showDebugText ==true) {
			long drawEnd = System.nanoTime();
			long passed = drawEnd-drawStart;
			
			g2.setFont(new Font("Arial", Font.PLAIN, 20));
			g2.setColor(Color.white);
			int x = 10;
			int y = 400;
			int lineheight = 20;			
			
			g2.drawString("WorldX: "+player.worldX , x , y); y+=lineheight;
			g2.drawString("WorldY: "+player.worldY, x , y);y+=lineheight;
			g2.drawString("Col: "+ (player.worldX +  player.solidArea.x )/tileSize , x , y);y+=lineheight;
			g2.drawString("Row: "+  (player.worldY +  player.solidArea.y )/tileSize  , x , y);y+=lineheight;
			g2.drawString("Draw Time: " +passed , x , y);	
		}
		
		
		g2.dispose();
		
		
	}

	public void playerMusic(int i) {
		music.setFile(i);
		music.play();
		music.loop();
	}
	
	public void stopMusic() {
		music.stop();
	}
	
	
	public void playSE(int i) {
		se.setFile(i);
		se.play();
		
	}
}
