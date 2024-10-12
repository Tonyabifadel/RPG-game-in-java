package game_2d;

import java.awt.Color; 
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

import entity.Entity;
import entity.Player;
import tile_interactive.InteractiveTile;
import tiles.TileManager;

public class GamePanel extends JPanel implements Runnable{
	//Screen settings
	final int originalTileSize = 16;//16x16 tile size
									//by default we use 16x16 in 2d game but 16x16 might look small on screen so we need to scale it
	final int scale = 3;
	
	public final int  tileSize = originalTileSize * scale; //48x48 tile
	public final int maxScreenCol = 20;
	public final int maxScreenRow = 12;
	public final int screenWidth = tileSize * maxScreenCol;  //960 pixels
	public final int screenHeight = tileSize * maxScreenRow; //576 pixels
	
	//World Settings
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;
	
	int screenWidth2 = screenWidth;
	int screenHeight2 = screenHeight;
	BufferedImage tempScreen;
	Graphics2D g2;

	
	public boolean FullScreenOn = false;
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
	public Entity obj[] = new Entity[20];
	public Entity npc[] = new Entity[20];
	public Entity monster[] = new Entity[25];
	
	public InteractiveTile iTile[] = new InteractiveTile[50];
	public ArrayList<Entity> projectileList = new ArrayList<Entity>();
	public ArrayList<Entity> particleList = new ArrayList<>(); 
	ArrayList<Entity> entityList = new ArrayList<Entity>();
	
	//GAME STATE	
	public int gameState;
	public final int titleState = 0;
	public final int playState=1;
	public final int pauseState=2;
	public final int dialogueState =3;
	public final int characterState =4;
	public final int optionsState = 5;

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
		aSetter.setInteractiveTile();
		gameState = titleState;
		
		tempScreen = new BufferedImage(screenWidth, screenHeight , BufferedImage.TYPE_INT_ARGB);
		g2 = (Graphics2D) tempScreen.getGraphics();
		
		//setFullScreen();
	}
	
	public void setFullScreen() {
		//Get Local screen device
		GraphicsEnvironment g2  = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = g2.getDefaultScreenDevice();
		gd.setFullScreenWindow(Main.window);
	
		//get full screen width and height
		screenWidth2 = Main.window.getWidth();
		screenHeight2 = Main.window.getHeight();

	}

	public void StartGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	
	
	//here we will create the game loop
	//2 things to do:
	//this is one way to create a decent game loop
//	public void run() {
//		//this means draw the screen every 0.01666 seconds
//		double drawInterval = 1000000000/FPS;
//		double nextDrawTime = System.nanoTime()+drawInterval; 
//		
//		while(gameThread !=null) {
//			
//			// update the information of the player
//			update();
//			
//			// draw the screen with new information
//					
//			repaint();
//			try {
//			double remainingTime = nextDrawTime - System.nanoTime();
//			remainingTime = remainingTime/1000000;
//			
//			if(remainingTime <0) {
//				remainingTime=0;
//			}
//			
//				Thread.sleep((long) remainingTime );
//				nextDrawTime += drawInterval; 
//			}catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}
//		
//	}
//	
//	another way to create a game loop
	  
	  public void run(){
	  double drawInterval = 1000000000/FPS;
	  double delta = 0;
	  long lastTime = System.nanoTime();
	  long currentTime;
	  long timer =0;
	  long drawCount = 0;
	  
	  while(gameThread!=null){
		  
		  currentTime=System.nanoTime();
		  
		  delta += (currentTime - lastTime) /drawInterval;
		  timer += (currentTime - lastTime);
		  lastTime = currentTime;
		  
		  
		  if(delta>=1) {
			  update();
			  drawToTempScreen(); //draw everything to the bufferedImage
			  drawToScreen(); //draw the buffered
			  delta--;
			  drawCount++;
		  }
		  
		  if(timer>=1000000000) {
			  //System.out.println("FPS is" + drawCount);
			  drawCount=0;
			  timer=0;
		  }
		 
	  
	  }
	  
	  }
	  
	  
	 
	
	
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
						monster[i].checkDrop();
						monster[i] =null;
					}
					
				}
			}
			
			for(int i = 0 ;i<projectileList.size() ;i++) {
				if(projectileList.get(i)!=null) {
					if(projectileList.get(i).alive ==true) {
						projectileList.get(i).update();
					}
					if(projectileList.get(i).alive ==false) {
						projectileList.remove(i);
					}
					
				}
			}
			
			for(int i = 0 ;i<particleList.size() ;i++) {
				if(particleList.get(i)!=null) {
					if(particleList.get(i).alive ==true) {
						particleList.get(i).update();
					}
					if(particleList.get(i).alive ==false) {
						particleList.remove(i);
					}
					
				}
			}
			
		}
		for(int i = 0;i<iTile.length;i++) {
			if(iTile[i]!=null) {
				iTile[i].update();
			}
		}
		if(gameState==pauseState) {
			//nothing
			
		}
		
	}
	
	public void drawToTempScreen () {
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
	
		//draw interactive tile
		for(int i =0 ;i<iTile.length;i++) {
			if(iTile[i]!=null) {
				iTile[i].draw(g2);
			}
		}
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
		
		for(int i = 0;i<projectileList.size();i++) {
			if(projectileList.get(i)!=null) {
				entityList.add(projectileList.get(i));
			}
		}
		
		for(int i = 0;i<particleList.size();i++) {
			if(particleList.get(i)!=null) {
				entityList.add(particleList.get(i));
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
		

	}
	
	public void drawToScreen() {
		Graphics g = getGraphics();
		g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
		g.dispose();
		
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
